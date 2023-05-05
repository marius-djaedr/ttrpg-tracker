const googleCommon = require('./googleCommon');
const {google} = require('googleapis');
const path = require('path');
const process = require('process');

const TOKEN_PATH = path.join(process.cwd(),'tokens','token-sheets.json');
const SCOPES = ['https://www.googleapis.com/auth/spreadsheets'];

//values is 2d array
exports.writeToSheet = async function(spreadsheetId, range, valueInputOption, values){
    const auth = await googleCommon.buildAuth(SCOPES, TOKEN_PATH);
    const service = google.sheets({version: 'v4', auth});
    const resource = {values};

    await service.spreadsheets.values.update({
        spreadsheetId,
        range,
        valueInputOption,
        resource,
    });
}