package com.fpt.ida.idasignhub.util;
import com.ruiyun.jvppeteer.api.core.Browser;
import com.ruiyun.jvppeteer.api.core.Page;
import com.ruiyun.jvppeteer.cdp.core.Puppeteer;
import com.ruiyun.jvppeteer.cdp.entities.LaunchOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PupperTeerUtils {
    private static final String FTP_SERVER = "103.161.119.75";
    private static final String FTP_USER = "nhantv16";
    private static final String FTP_PASSWORD = "Abc@12345";
    private static final String REMOTE_DIRECTORY = "upload";

    public static void runPupperTeer() throws Exception {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        // 2. Khởi tạo ChromeDriver với tuỳ chọn (ví dụ: tắt thông báo)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        WebDriverManager.chromedriver()
                .browserVersion("136.0.7103.116")
                .setup();
        WebDriver driver = new ChromeDriver(options);
        try {
            // 3. Tới trang login
            driver.get("https://kholuutru.quangnam.gov.vn/login");

            // 4. Khai báo WebDriverWait để chờ element ( tối đa 15 giây )
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // 5. Chờ và điền username
            WebElement usernameInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("username"))
            );
            usernameInput.sendKeys("YOUR_USERNAME");

            // 6. Chờ và điền password
            WebElement passwordInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("password"))
            );
            passwordInput.sendKeys("YOUR_PASSWORD");

            // 7. Chờ và click nút đăng nhập
            WebElement loginButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
            );
            loginButton.click();

            // 8. Chờ cho đến khi có một element đặc trưng của trang sau login xuất hiện
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("dashboard"))
            );

            System.out.println("✅ Login thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 9. Đóng trình duyệt
            driver.quit();
        }
    }


}
