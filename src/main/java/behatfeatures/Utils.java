package behatfeatures;

import org.openqa.selenium.WebDriver;

public class Utils {
    static WebDriver driver;

     public static boolean isTextAvailable(String text)
    {
        return driver.getPageSource().contains(text);
    }

    public static void pause(int time)
    {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setDriver(WebDriver driver) {

    }
}
