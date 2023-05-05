const {authenticate} = require('@google-cloud/local-auth');
const {google} = require('googleapis');
const fs = require('fs/promises');
const path = require('path');
const process = require('process');

const CREDENTIALS_PATH = path.join(process.cwd(), 'credentials-google.json');
const TOKEN_PATH = path.join(process.cwd(),'tokens','sheets','token-sheets.json');

//values is 2d array
exports.writeToSheet = async function(spreadsheetId, range, valueInputOption, values){
    const auth = await buildAuth();
    const service = google.sheets({version: 'v4', auth});
    const resource = {values};

    await service.spreadsheets.values.update({
        spreadsheetId,
        range,
        valueInputOption,
        resource,
    });
}

async function buildAuth(){
    let client = await loadSavedCredentialsIfExist();
    if (client) {
        return client;
    }
    client = await authenticate({
        scopes: ['https://www.googleapis.com/auth/spreadsheets'],
        keyfilePath: CREDENTIALS_PATH,
    });
    if (client.credentials) {
        await saveCredentials(client);
    }
    return client;
}

async function loadSavedCredentialsIfExist() {
    try {
        const content = await fs.readFile(TOKEN_PATH);
        const credentials = JSON.parse(content);
        return google.auth.fromJSON(credentials);
    } catch (err) {
        return null;
    }
}

async function saveCredentials(client) {
    const content = await fs.readFile(CREDENTIALS_PATH);
    const keys = JSON.parse(content);
    const key = keys.installed || keys.web;
    const payload = JSON.stringify({
        type: 'authorized_user',
        client_id: key.client_id,
        client_secret: key.client_secret,
        refresh_token: client.credentials.refresh_token,
    });
    await fs.writeFile(TOKEN_PATH, payload);
}