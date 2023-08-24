package ru.yandex.practicum.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StellarBurgersProfilePage {
    private final WebDriver driver;
    private final By nameField = By.xpath(".//div/label[text()='Имя']/../input");
    private final By loginField = By.xpath(".//div/label[text()='Логин']/../input");
    private final By profileButton = By.xpath(".//a[text()='Профиль']");
    private final By logoutButton = By.xpath(".//button[text()='Выход']");
    private final By logoStellarBurgers = By.xpath(".//div[@class='AppHeader_header__logo__2D0X2']");
    private final By constructorButton = By.xpath(".//p[@class='AppHeader_header__linkText__3q_va ml-2'" +
            "and text()='Конструктор']");

    public StellarBurgersProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForProfilePage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.elementToBeClickable(profileButton));
    }

    public String getNameField() {
        return driver.findElement(nameField).getAttribute("value");
    }

    public String getLoginField() {
        return driver.findElement(loginField).getAttribute("value");
    }

    public void clickConstructorButton() {
        driver.findElement(constructorButton).click();
    }

    public void clickLogoButton() {
        driver.findElement(logoStellarBurgers).click();
    }

    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }

}
