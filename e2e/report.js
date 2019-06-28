const reporter = require('cucumber-html-reporter');

const options = {
    theme: 'foundation',
    jsonFile: 'output/report/cucumber_report.json',
    output: 'output/report/cucumber_report.html',
    reportSuiteAsScenarios: true,
    launchReport: false,
};

reporter.generate(options);
