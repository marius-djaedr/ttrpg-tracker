
const fs = require('fs/promises');

module.exports = function() {
    this.aggregate = async function(folderName, aggInput, aggOutputs){
        let fileName = folderName+'/backup.json';
        await fs.writeFile(folderName+'/backup.json',JSON.stringify(aggInput))
        return [fileName];
    }
}