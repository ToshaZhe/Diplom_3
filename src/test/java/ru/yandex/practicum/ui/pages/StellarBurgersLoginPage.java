package ru.yandex.practicum.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersLoginPage {
    private final WebDriver driver;
    private final By logonButton = By.xpath(".//div/form/button[text()='Войти']");
    private final By signUpLink = By.xpath(".//a[text()='Зарегистрироваться']");
    private final By forgotPasswordLink = By.xpath(".//a[text()='Восстановить пароль']");
    private final By emailField = By.xpath(".//div/label[text()='Email']/../input");
    private final By passwordField = By.xpath(".//div/label[text()='Пароль']/../input");

    public StellarBurgersLoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForLoadLoginPage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.elementToBeClickable(logonButton));
    }

    public void clickSignUpLink() {
        driver.findElement(signUpLink).click();
    }

    public void clickForgotPasswordLink() {
        driver.findElement(forgotPasswordLink).click();
    }

    public void clickLogonButton() {
        driver.findElement(logonButton).click();
    }

    public void setEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void setPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public boolean isLogonButtonVisible() {
        return driver.findElement(logonButton).isDisplayed();
    }

    public void logonUser(String email, String password) {
        setEmail(email);
        setPassword(password);
        clickLogonButton();
    }
}
