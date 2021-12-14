![BrowserStack Logo](https://camo.githubusercontent.com/09765325129b9ca76d770b128dbe30665379b7f2915d9b60bf57fc44d9920305/68747470733a2f2f7777772e62726f77736572737461636b2e636f6d2f696d616765732f7374617469632f6865616465722d6c6f676f2e6a7067)


# BrowserStack Example - Jira Integration

Log the errored sessions on your JIRA dashabord based on the test status of BrowserStack sessions using REST APIs.

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)


## Setup steps
* Set Environment Variables 
  * Identify your BrowserStack username and access key from the [BrowserStack Automate Dashboard](https://automate.browserstack.com/) and export them as environment variables using the below commands.
    * For *nix based and Mac machines:
        ```
        export BROWSERSTACK_USERNAME=<browserstack-username> &&
        export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
        ```
    * For Windows:
        ```
        set BROWSERSTACK_USERNAME=<browserstack-username>
        set BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
        ```
    * If you wish to hardcode these values, it needs to be done in the [AutomateExample.java](https://github.com/nithyamn/BSJiraIntegration/blob/main/src/test/java/scripts/AutomateExample.java), [AppAutomateExample.java](https://github.com/nithyamn/BSJiraIntegration/blob/main/src/test/java/scripts/AppAutomateExample.java) and [SessionDetails.java](https://github.com/nithyamn/BSJiraIntegration/blob/main/src/test/java/jira/SessionDetails.java) files. 
      
* Set your Jira board details
    * For *nix based and Mac machines:
        ```
        export JIRA_URL=<jira-url> &&
        export JIRA_EMAIL=<jira-email> &&
        export JIRA_ACCESS_TOKEN=<jira-api-token>
        ```
    * For Windows:
        ```
        set JIRA_URL=<jira-url>
        set JIRA_EMAIL=<jira-email>
        set JIRA_ACCESS_TOKEN=<jira-api-token>
        ```
    * If you wish to hardcode these values, it needs to be done in the [JiraIntegration.java](https://github.com/nithyamn/BSJiraIntegration/blob/main/src/test/java/jira/JiraIntegration.java) file.
    * Add your Jira board name and the type of issue you want to log in the [getJSON_Body](https://github.com/nithyamn/BSJiraIntegration/blob/main/src/test/java/jira/JiraIntegration.java#L52) method of JiraIntegration class.


## References
1. Code ref:  https://community.atlassian.com/t5/Answers-Developer-Questions/Create-issue-via-REST-API-useing-Java/qaq-p/518478
2. REST APIs: 
   * JIRA: https://developer.atlassian.com/server/jira/platform/jira-rest-api-example-create-issue-7897248/
   * BrowserStack Automate : https://www.browserstack.com/docs/automate/api-reference/selenium/session#get-session-list
   * BrowserStack App Automate: https://www.browserstack.com/docs/app-automate/api-reference/appium/sessions#get-session-details 
3. API Tokens: https://confluence.atlassian.com/cloud/api-tokens-938839638.html