package ru.yandex.practicum.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersForgotPasswordPage {
    private final WebDriver driver;
    private final By logonLink = By.xpath(".//a[@class='Auth_link__1fOlj']");
    private final By recoverButton = By.xpath(".//button[text()='Восстановить']");

    public StellarBurgersForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForLoadForgotPasswordPage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.elementToBeClickable(recoverButton));
    }

    public void clickLogonLink() {
        driver.findElement(logonLink).click();
    }

}
