package ru.yandex.practicum.configs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Config {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static WebDriver getWebDriver(String browserName) {
        ChromeOptions chromeOptions = new ChromeOptions();
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                chromeOptions.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
                return new ChromeDriver(chromeOptions);
            case "yandex":
                WebDriverManager.chromedriver().setup();
                chromeOptions.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
                return new ChromeDriver(chromeOptions.setBinary("C:\\Users\\AZhenodarov\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe"));
            default:
                throw new RuntimeException("Неподдерживаемый браузер");
        }
    }

}