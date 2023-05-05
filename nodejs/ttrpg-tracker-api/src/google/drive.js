const googleCommon = require('./googleCommon');
const logger = require('../logger');
const {google} = require('googleapis');
const path = require('path');
const process = require('process');
const mime = require('mime');
const fs = require('fs/promises');

const TOKEN_PATH = path.join(process.cwd(),'tokens','token-drive.json');
const SCOPES = ['https://www.googleapis.com/auth/drive'];

//values is 2d array
exports.directoryReplace = async function(filesToUpload, folderChain){
    const auth = await googleCommon.buildAuth(SCOPES, TOKEN_PATH);
    const service = google.drive({version: 'v3', auth});

    logger.info("Finding directory for folder chain "+folderChain);
    const folderId = await drillIntoFolder(service, folderChain, true);

    logger.info("Building batch");
    //https://developers.google.com/drive/api/guides/manage-uploads#multipart
    //const list = copyAllFiles(parentDir, folderId);
    logger.info("Executing batch");
    //batchExecute(list);
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

async function uploadOne(service, parentId, localFileLocation){
    const file = await fs.open(localFileLocation)

    const requestBody = {
        name: path.basename(localFileLocation)
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
        throw err
    }finally{
        file.close()
    }

}