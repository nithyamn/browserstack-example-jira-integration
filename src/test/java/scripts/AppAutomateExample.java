package scripts;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import jira.SessionDetails;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

public class AppAutomateExample {

    public String userName = System.getenv("BROWSERSTACK_USERNAME"); //"YOUR_BROWSERSTACK_USERNAME";
    public String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");//"YOUR_BROWSERSTACK_ACCESS_KEY";

    @Test
    public void appAutomateTest() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("device", "Samsung Galaxy S9");
        caps.setCapability("os_version", "8.0");
        caps.setCapability("build", "browserstack-app-automate-jira-integration");
        caps.setCapability("name", "app_automate_test");
        caps.setCapability("app", "BROWSERSTACK_APP_ID");

        AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL("https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        SessionId sessionId = driver.getSessionId();

        /**This class will populate the session data and create a Jira ticket**/
        SessionDetails getSessionData = new SessionDetails();

        try{
            AndroidElement searchElement = (AndroidElement) new WebDriverWait(driver, 30).until(
                    ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Search Wikipedia")));
            searchElement.click();
            AndroidElement insertTextElement = (AndroidElement) new WebDriverWait(driver, 30).until(
                    ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia.alpha:id/search_src_text")));
            insertTextElement.sendKeys("BrowserStack");
            Thread.sleep(5000);

            List<AndroidElement> allProductsName = driver.findElementsByClassName("android.widget.TextView");

            //allProductsName.size() > 0 - passing condition
            if(allProductsName.size() < 0) //failing condition
                ((JavascriptExecutor)driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"passed\", \"reason\": \"Expected results\"}}");
            else {
                ((JavascriptExecutor) driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"Unexpected results\"}}");
                /**Get session data and create a jira ticket**/
                /**Passing an object of DesiredCapabilities to identify the product - Automate or App Automate**/
                getSessionData.sessionData(sessionId,caps);
            }
        }catch (Exception e){
            System.out.println(e);
            ((JavascriptExecutor)driver).executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"Something went wrong!\"}}");
            getSessionData.sessionData(sessionId,caps);
            driver.quit();
        }
        driver.quit();
    }
}
