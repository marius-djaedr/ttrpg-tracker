const {authenticate} = require('@google-cloud/local-auth');
const {google} = require('googleapis');
const fs = require('fs/promises');
const path = require('path');
const process = require('process');

const CREDENTIALS_PATH = path.join(process.cwd(), 'credentials-google.json');

exports.buildAuth = async function(scopes, tokenPath){
    let client = await loadSavedCredentialsIfExist(tokenPath);
    if (client) {
        return client;
    }
    client = await authenticate({
        scopes: scopes,
        keyfilePath: CREDENTIALS_PATH,
    });
    if (client.credentials) {
        await saveCredentials(client, tokenPath);
    }
    return client;
}

async function loadSavedCredentialsIfExist(tokenPath) {
    try {
        const content = await fs.readFile(tokenPath);
        const credentials = JSON.parse(content);
        return google.auth.fromJSON(credentials);
    } catch (err) {
        return null;
    }
}

async function saveCredentials(client, tokenPath) {
    const content = await fs.readFile(CREDENTIALS_PATH);
    const keys = JSON.parse(content);
    const key = keys.installed || keys.web;
    const payload = JSON.stringify({
        type: 'authorized_user',
        client_id: key.client_id,
        client_secret: key.client_secret,
        refresh_token: client.credentials.refresh_token,
    });
    await fs.writeFile(tokenPath, payload);
}