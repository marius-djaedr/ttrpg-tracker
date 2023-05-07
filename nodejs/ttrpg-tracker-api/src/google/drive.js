const googleCommon = require('./googleCommon');
const logger = require('../logger');
const {google} = require('googleapis');
const path = require('path');
const process = require('process');
const mime = require('mime');
const fs = require('fs/promises');

const TOKEN_PATH = path.join(process.cwd(),'tokens','token-drive.json');
const SCOPES = ['https://www.googleapis.com/auth/drive'];

exports.directoryReplace = async function(filesToUpload, folderChain){
    const auth = await googleCommon.buildAuth(SCOPES, TOKEN_PATH);
    const service = google.drive({version: 'v3', auth});

    logger.info("Finding directory for folder chain "+folderChain);
    const folderId = await drillIntoFolder(service, folderChain, true);

    logger.info("Executing batch");
    await copyAllFiles(service, folderId, filesToUpload);
}


async function drillIntoFolder(service, folderChain, wipeIfExisting) {
    let previousParent = null;
    let currentParent = 'root';
    let finalName = null;
    let createdNew = false;

    for(const link of folderChain) {
        previousParent = currentParent;

        const response = await service.files.list({
            q: "mimeType = 'application/vnd.google-apps.folder' and name = '" + link + "' and '" + currentParent + "' in parents and trashed = false",
            spaces: 'drive',
            fields: 'files(id,name,parents)',
        });

        const foundFiles = response.data.files;

        if(foundFiles.length > 1) {
            throw new IllegalArgumentException("Multiple folders found with name " + link + " in parent " + currentParent);
        } else if(foundFiles.length == 0) {
            currentParent = await createFolder(service, currentParent, link);
            createdNew = true;
        } else {
            currentParent = foundFiles[0].id;
        }
        finalName = link;
    }

    if(wipeIfExisting && !createdNew) {
        logger.info("Wiping out all existing in folder id ["+currentParent+"]");
        await service.files.delete({
            fileId:currentParent
        });
        currentParent = await createFolder(service, previousParent, finalName);
    }
    return currentParent;
}


async function createFolder(service, parent, name) {
    logger.info("Creating new folder ["+name+"] in folder id ["+parent+"]");

    const fileMetadata = {
        name: name,
        mimeType: 'application/vnd.google-apps.folder',
        parents: [parent]
    }

    const response = await service.files.create({
        resource: fileMetadata,
        fields: 'id',
    });

    return response.data.id;
}

async function copyAllFiles(service, folderId, filesToUpload){
    let queue = [...filesToUpload]
    let promiseMap = {};
    let count = 0;
    while(queue.length > 0){
        while(Object.keys(promiseMap).length <= 20){
            let localFileLocation = queue.pop();
            let id = count++;
            logger.info(id+' - '+localFileLocation)
            promiseMap[id] = uploadOne(id, service, folderId, localFileLocation)
        }
        let finishedId = await Promise.any(Object.values(promiseMap))
        logger.info(finishedId+' - finished')
        delete promiseMap[finishedId]
    }

    const allResponses = await Promise.allSettled(Object.values(promiseMap))
    allResponses.forEach((result) => {
        if(result.status === 'fulfilled'){
            logger.info(result.value+' - finished')
        }else{
            logger.error(result.reason);
        }
    });

}

async function uploadOne(threadId, service, folderId, localFileLocation){
    //https://developers.google.com/drive/api/guides/manage-uploads#multipart
    const file = await fs.open(localFileLocation)

    const requestBody = {
        name: path.basename(localFileLocation),
        parents: [folderId]
    };
    const media = {
        mimeType: mime.getType(localFileLocation),
        body: file.createReadStream(),
    };

    try{
        await service.files.create({
            requestBody,
            media: media,
        });
    }catch(err){
        logger.info('Problem with file '+localFileLocation)
        logger.error(err)
    }finally{
        file.close()
    }
    return threadId;
}