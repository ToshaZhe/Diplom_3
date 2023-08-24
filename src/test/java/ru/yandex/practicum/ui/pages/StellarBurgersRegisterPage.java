package ru.yandex.practicum.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersRegisterPage {
    private final WebDriver driver;
    private final By signUpButton = By.xpath(".//button[text()='Зарегистрироваться']");
    private final By nameField = By.xpath(".//div/label[text()='Имя']/../input");
    private final By emailField = By.xpath(".//div/label[text()='Email']/../input");
    private final By passwordField = By.xpath(".//div/label[text()='Пароль']/../input");
    private final By logonLink = By.xpath(".//a[@class='Auth_link__1fOlj']");
    private final By incorrectPasswordWarning = By.xpath(".//div/p[text()='Некорректный пароль']");

    public StellarBurgersRegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForLoadRegisterPage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.elementToBeClickable(signUpButton));
    }

    public void setName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void setEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void setPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogonLink() {
        driver.findElement(logonLink).click();
    }

    public void clickSignUpButton() {
        driver.findElement(signUpButton).click();
    }

    public boolean isIncorrectPasswordWarnDisplayed() {
        return driver.findElement(incorrectPasswordWarning).isDisplayed();
    }

    public void signUpUser(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        clickSignUpButton();
    }
}
