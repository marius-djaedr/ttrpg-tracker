
const fs = require('fs/promises');

module.exports = function() {
    this.aggregate = async function(folderName, aggInput, aggOutputs){
        const writePromises = [];
        for(aggOutput of aggOutputs){
            writePromises.push(processAgg(aggOutput, folderName));
        }
        const writePromiseResult = await Promise.allSettled(writePromises);
        
        const filesToUpload = [];
        writePromiseResult.forEach((result) => {
            if(result.status === 'fulfilled'){
                const file = result.value;
                if(file !=null){
                    filesToUpload.push(file);
                }
            }else{
                logger.error(result.reason);
            }
        });

        return filesToUpload;
    }

    async function processAgg(aggOutput, folderName){
        const fileContent = buildFileContent(aggOutput);
        const fileName = folderName + '/' + aggOutput.chartTypeReadable + ' - ' + aggOutput.name + '.html';
        await fs.writeFile(fileName,fileContent);

        return fileName;
    }

    function buildFileContent(aggOutput){
        aggOutput.options.height = 800;

        const lines = [...TOP_LINES];
        lines.push("google.charts.load('current', " + JSON.stringify(aggOutput.settings) + ");");
        lines.push(...PRE_COLUMN_LINES);

        const colTypes = [];
        for(col of aggOutput.columnDefinitions){
            lines.push('data.addColumn('+JSON.stringify(col)+')');
            colTypes.push(col.type);
        }


        lines.push('data.addRows([');
        for(row of aggOutput.rows){
            let line = '[';
            for(let i=0; i<colTypes.length; i++){
                const field = row[i];
                const type = colTypes[i];
                if(field==null){
                    line += 'null';
                }else if(type === 'string'){
                    line += '`'+field+'`';
                }else if(type === 'date'){
                    line += 'new '+field;
                }else{
                    line += field;
                }
                line += ',';
            }
            lines.push(line + '],');
        }
        lines.push(']);');

        
        lines.push('');
        lines.push('// Set chart options');
        lines.push('var options = ' + JSON.stringify(aggOutput.options));
        lines.push('');
        lines.push('// Instantiate and draw our chart, passing in some options.');
        lines.push('var chart = new google.visualization.' + aggOutput.chartTypeGoogle + "(document.getElementById('chart_div'));");
        lines.push(...BOTTOM_LINES);
        return lines.join('\n');
    }

    const TOP_LINES = [
        '<html>',
        '<head>',
        '<!--https://developers.google.com/chart/interactive/docs/quick_start-->',
        '<!--Load the AJAX API-->',
        '<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>',
        '<script type="text/javascript">',
        '// Load the Visualization API and the corechart package.'
    ];

    const PRE_COLUMN_LINES = [
        'google.charts.setOnLoadCallback(drawChart);',
        '',
        'function drawChart() {',
        '// Create the data table.',
        'var data = new google.visualization.DataTable();'
    ];

    const BOTTOM_LINES = [
        'chart.draw(data, options);',
        '}',
        '</script>',
        '</head>',
        '',
        '<body>',
        '<!--Div that will hold the pie chart-->',
        '<div id=\"chart_div\"></div>',
        '</body>',
        '</html>'
    ];
}