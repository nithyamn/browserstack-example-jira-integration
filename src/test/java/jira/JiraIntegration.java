package jira;

import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class JiraIntegration{

    public void createJira(String sessionData) {
        try {
            String jiraURL = System.getenv("JIRA_URL"); //e.g. something.atlassian.net
            String jiraEmail = System.getenv("JIRA_EMAIL"); // The email id linked to your Jira instance
            String jiraAccessToken = System.getenv("JIRA_ACCESS_TOKEN"); //Jira password or AccessToken

            URL url = new URL("https://"+jiraURL+"/rest/api/2/issue");
            String jiraCreds = jiraEmail+":"+jiraAccessToken;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String encodedData = getJSON_Body(sessionData);


            System.out.println("\n**** Jira JSON Body ****");
            JSONObject json = new JSONObject(encodedData);
            System.out.println(json.toString(4));

            conn.setRequestMethod("POST");
            System.out.println();
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(jiraCreds.getBytes()));
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getOutputStream().write(encodedData.getBytes());

            try {
                InputStream inputStream = conn.getInputStream();
                System.out.println(inputStream);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getJSON_Body(String sessionData) throws Exception {
        String summary = "Issue in build : "+ SessionDetails.buildName;
        JsonObject createIssue = Json.createObjectBuilder()
                .add("fields",
                        Json.createObjectBuilder().add("project",
                                Json.createObjectBuilder().add("key", "JIRA_BOARD_NAME")) //This should be the jira board name where you want to log issues
                                .add("summary", summary)
                                .add("description",sessionData)
                                .add("issuetype",
                                        Json.createObjectBuilder().add("name", "ISSUE_TYPE")) //This value can be any jira issue-type, e.g Story or Bug
                ).build();

        return createIssue.toString();
    }

}
