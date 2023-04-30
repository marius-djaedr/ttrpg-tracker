const util = require('node:util'); 
const moment = require('moment');

exports.info = function(str, ...optional) {
    console.log(buildPre()+str,...optional);
}
exports.error = function(err) {
    console.error(buildPre()+'ERROR');
    console.error(err);
}

function buildPre(){
    return moment().format("YYYY-MM-DD-HH-mm-ss-SSS")+': ';
}