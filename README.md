[![Jenkins Plugins](https://img.shields.io/jenkins/plugin/v/redmine-metrics-report.svg)](https://plugins.jenkins.io/redmine-metrics-report)
[![MIT License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE.md)
[![Build Status](https://ci.jenkins.io/buildStatus/icon?job=Plugins/redmine-metrics-report-plugin/master)](https://ci.jenkins.io/blue/organizations/jenkins/Plugins%2Fredmine-metrics-report-plugin/branches)

# redmine-metrics-report
A Jenkins plugin which generates Redmine reports. [Wiki Page](https://wiki.jenkins.io/display/JENKINS/Redmine+Metrics+Report+Plugin)

## How To Use

1. Open project configure page, in build section click [Add build step] -> [Generate Redmine Metrics Report].

![Build Step](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/howTo1.png)

2. Click [Add] to add multiple Redmine Projects.

![Add Projects](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/howTo2.png)

3. Specify Redmine Info.
* `Redmine URL`: Specify Redmine's URL
* `API Key`: Specify Redmine API Key which can be found in Redmine's [My Account] -> API access key
* `Project Name`: Specify Redmine's Project Identifier which you want to generate report for, use Identifier not Name in the project setting page.
* `Custom Query ID`: Specify the Redmine custom query id. You can create a redmine custom query to search the issues for generating report.
* `Sprint Size`: Specify the time span(day). e.g.: if you want to generate report on a weekly basis you should specify 7.

![Specify Info](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/howTo3.png)

4. In the Post-build Actions, use the [[HTML Publisher Plugin]](https://plugins.jenkins.io/htmlpublisher) to display the generated html reports.

![Set HTML Plugin](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/howTo4.png)

#### Note: In order to allow Jenkins to run html page with javascript, You have to set the following script in Jenkins script console and clean browser cache. This setup won't survive after Jenkins service restart, so you have to set it again and clean browser cache.
```
System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")
```

![Script Console](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/howTo5.png)

5. After the project is built successfully, click [Redmine Report] on the side menu to see the report.

![Show Report](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/howTo6.png)

## Feature

* `Issue Increment Chart (Open Issues)`: Show increment report of open issues

![Show Report](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/what1.png)

* `Issue Open & Close Chart`: Show Open & Close Report of issues

![Show Report](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/what2.png)

* `Assignee Chart`: Show Assignee report of issues

![Show Report](https://github.com/jenkinsci/redmine-metrics-report-plugin/blob/master/images/what3.png)

## Release

### 1.0.1 (Oct 31, 2018)
* Minor bug fix.
* Add description for field [project name]

### 1.0.0 (Sep 27, 2018)
* Initial release.

## Author

* [Song Li](https://github.com/bestoak)

## License

The MIT License

Copyright (c) 2018- Shepherd Lee

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
