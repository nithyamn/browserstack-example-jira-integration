package jira;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;

import java.net.URI;

public class SessionDetails {
    public static String buildName="";
    URI uri;
    String jiraData = "";

    /**This method gets session details based on the product and logs it in the Jira dashboard**/
    public void sessionData(SessionId sessionId, DesiredCapabilities caps) throws Exception{
        String username = System.getenv("BROWSERSTACK_USERNAME"); //"YOUR_BROWSERSTACK_USERNAME";
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");//"YOUR_BROWSERSTACK_ACCESS_KEY";
        JiraIntegration jiraIntegration = new JiraIntegration();

        /**If the "app" capability is present then it is an App Automate session, otherwise it is an Automate session**/
        //System.out.println("app: "+caps.getCapability("app"));

        if(caps.getCapability("app")!=null){
           uri = new URI("https://"+username+":"+accessKey+"@api.browserstack.com/app-automate/sessions/"+sessionId+".json"); //App Automate
        }
        else{
            uri = new URI("https://"+username+":"+accessKey+"@api.browserstack.com/automate/sessions/"+sessionId+".json"); //Automate
        }
        HttpGet getRequest = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse httpresponse = httpclient.execute(getRequest);

        String jsonResponseData = EntityUtils.toString(httpresponse.getEntity());
        String trimResponseData = jsonResponseData.substring(22, jsonResponseData.length()-1);

        JSONParser parser = new JSONParser();
        JSONObject bsSessionData = (JSONObject) parser.parse(trimResponseData);
        buildName = (String) bsSessionData.get("build_name");

        jiraData = "\nSession ID: "+bsSessionData.get("hashed_id")
                + "\nName: "+bsSessionData.get("name")
                +"\nBuild: "+bsSessionData.get("build_name")
                +"\nProject: "+bsSessionData.get("project_name")
                +"\nDevice: "+bsSessionData.get("device")
                +"\nOS: "+bsSessionData.get("os")
                +"\nOS Version: "+bsSessionData.get("os_version")
                +"\nBrowser: "+bsSessionData.get("browser")
                +"\nBrowser Version: "+bsSessionData.get("browser_version")
                +"\nTest status: "+bsSessionData.get("status")
                +"\nBrowserStack status: "+bsSessionData.get("browserstack_status")
                +"\nReason: "+bsSessionData.get("reason")
                +"\nPublic Session URL: "+bsSessionData.get("public_url")
                +"\nRaw logs URL: "+bsSessionData.get("logs")
                +"\nSelenium logs URL: "+bsSessionData.get("selenium_logs_url")
                +"\nConsole logs URL: "+bsSessionData.get("browser_console_logs_url")
                +"\nNetwork logs URL: "+bsSessionData.get("har_logs_url")
                +"\nVideo logs URL: "+bsSessionData.get("video_url");

        if(caps.getCapability("app")!=null){
            jiraData +="\nAppium logs URL: "+bsSessionData.get("appium_logs_url")
                    +"\nDevice logs: "+bsSessionData.get("device_logs_url")
                    +"\nApp details: "+bsSessionData.get("app_details");
        }

        System.out.println(jiraData);

        /**Create Jira Ticket**/
        jiraIntegration.createJira(jiraData);
    }
}
