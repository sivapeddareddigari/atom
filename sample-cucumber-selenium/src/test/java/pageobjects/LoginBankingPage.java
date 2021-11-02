package pageobjects;

import automation.library.cucumber.selenium.BaseSteps;
import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;


public class LoginBankingPage extends BasePO {

    private static final By txtUserName = By.id("username");
    private static final By txtPassword = By.id("password");
    private static final By btnSignIn = By.id("submit");
    private static final By btnAdvanced = By.id("details-button");
    private static final By lnkProceedLink = By.id("proceed-link");

    public void login() throws IOException{
        String url = "https://" + getHostIP().trim() + ":8443/bank/login";
        performDriverOperation("goto", url);
        performElementOperation("click", "button", "", "Advanced", btnAdvanced);
        performElementOperation("click", "button", "", "Proceed Link", lnkProceedLink);
        performElementOperation("sendKeys", "text", "jsmith@demo.io", "userName", txtUserName );
        performElementOperation("sendKeys", "text", "Demo123!", "userName", txtPassword );
        performElementOperation("click", "button", "", "SignIn", btnSignIn);
    }

    public String getHostIP() throws IOException {

        String hostIP;
        String resultStr = "";
        hostIP=InetAddress.getLocalHost().getHostAddress();
        if (System.getProperty("os.name").equalsIgnoreCase("linux"))
        {
            try {
                InputStream fis = new FileInputStream("/home/ubuntu/project/TTPObjectSpy/hostIP.txt");
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
                hostIP = br.readLine();
                hostIP = hostIP.replaceAll("[^a-zA-Z0-9\\.+]", "");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }
        return hostIP;
    }
}
