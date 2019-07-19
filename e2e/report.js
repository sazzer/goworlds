const reporter = require('cucumber-html-reporter');

const options = {
    theme: 'hierarchy',
    jsonFile: 'output/report/cucumber_report.json',
    output: 'output/report/cucumber_report.html',
    reportSuiteAsScenarios: true,
    launchReport: false,
    storeScreenshots: true,
    noInlineScreenshots: true,
};

reporter.generate(options);
