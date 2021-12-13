package scripts;

import jira.SessionDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AutomateExample{
    public String username = System.getenv("BROWSERSTACK_USERNAME");//"YOUR_BROWSERSTACK_USERNAME";
    public String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");//"YOUR_BROWSERSTACK_ACCESS_KEY";

    @Test
    public void automateTest() throws Exception{
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os_version", "10");
        caps.setCapability("browser", "Chrome");
        caps.setCapability("browser_version", "latest");
        caps.setCapability("os", "Windows");
        caps.setCapability("name", "automate_test"); // test name
        caps.setCapability("build", "browserstack-automate-jira-integration");

        WebDriver driver = new RemoteWebDriver(new URL("https://"+username+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"),caps);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        /**This class will populate the session data and create a Jira ticket**/
        SessionDetails getSessionData = new SessionDetails();

        SessionId sessionID = ((RemoteWebDriver)driver).getSessionId();
        try
        {
            driver.get("https://bstackdemo.com/");
            driver.findElement(By.id("signin")).click();
            driver.findElement(By.cssSelector("#username input")).sendKeys("fav_user", Keys.ENTER);
            driver.findElement(By.cssSelector("#password input")).sendKeys("testingisfun99",Keys.ENTER);

            driver.findElement(By.id("login-btn")).click();
            String verifyUser = driver.findElement(By.className("username")).getText();
            if (verifyUser.equals("demouser")) //failing condition
            {
                ((JavascriptExecutor)driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"passed\", \"reason\": \"Expected\"}}");
            }
            else {
                ((JavascriptExecutor) driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"Unexpected\"}}");
                /**Get session data and create a jira ticket**/
                /**Passing an object of DesiredCapabilities to identify the product - Automate or App Automate**/
                getSessionData.sessionData(sessionID,caps);
            }
        }catch(Exception e)
        {
            ((JavascriptExecutor)driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"Something went wrong!\"}}");
            System.out.println(e);
            getSessionData.sessionData(sessionID,caps);
            driver.quit();
        }
        driver.quit();
    }

}
