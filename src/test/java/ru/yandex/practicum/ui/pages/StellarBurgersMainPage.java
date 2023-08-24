package ru.yandex.practicum.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersMainPage {
    private final WebDriver driver;
    private final By logonButton = By.xpath(".//div/button[text()='Войти в аккаунт']");
    private final By placeOrderButton = By.xpath(".//button[text()='Оформить заказ']");
    private final By personalAccountButton = By.xpath(".//p[text()='Личный Кабинет']");
    private final By createBurgerText = By.xpath(".//h1[text()='Соберите бургер']");
    private final By bunTab = By.xpath(".//span[text()='Булки']//parent::div");
    private final By sauceTab = By.xpath(".//span[text()='Соусы']//parent::div");
    private final By fillingTab = By.xpath(".//span[text()='Начинки']//parent::div");

    public StellarBurgersMainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForLoadMainPage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(createBurgerText));
    }

    public void clickLogonButton() {
        driver.findElement(logonButton).click();
    }

    public void clickPersonalAccountButton() {
        driver.findElement(personalAccountButton).click();
    }

    public void clickBunTub() {
        driver.findElement(bunTab).click();
    }

    public void clickSauceTab() {
        driver.findElement(sauceTab).click();
    }

    public void clickFillingTab() {
        driver.findElement(fillingTab).click();
    }

    public boolean isPlaceOrderButtonVisible() {
        return driver.findElement(placeOrderButton).isDisplayed();
    }

    public boolean isBunTabSelected() {
        return driver.findElement(bunTab).getAttribute("class").contains("_current_");
    }

    public boolean isSauceTabSelected() {
        return driver.findElement(sauceTab).getAttribute("class").contains("_current_");
    }

    public boolean isFillingTabSelected() {
        return driver.findElement(fillingTab).getAttribute("class").contains("_current_");
    }

    public String getCreateBurgerText() {
        return driver.findElement(createBurgerText).getText();
    }
}
