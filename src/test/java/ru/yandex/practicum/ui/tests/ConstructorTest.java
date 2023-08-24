package ru.yandex.practicum.ui.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.yandex.practicum.configs.Config;
import ru.yandex.practicum.ui.pages.StellarBurgersMainPage;

import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class ConstructorTest {
    private final String checkingTab;
    private final boolean isBunTabSelected;
    private final boolean isSauceTabSelected;
    private final boolean isFillingTabSelected;
    private final String browser;
    WebDriver driver;

    public ConstructorTest(String checkingTab, boolean isBunTabSelected, boolean isSauceTabSelected, boolean isFillingTabSelected, String browser) {
        this.checkingTab = checkingTab;
        this.isBunTabSelected = isBunTabSelected;
        this.isSauceTabSelected = isSauceTabSelected;
        this.isFillingTabSelected = isFillingTabSelected;
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Object[][] getTabParametersAdBrowser() {
        return new Object[][]{{"Булки", true, false, false, "chrome"}, {"Соусы", false, true, false, "yandex"},
                {"Начинки", false, false, true, "yandex"}};
    }

    @Before
    public void setup() {
        driver = Config.getWebDriver(browser);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Config.getBaseUrl());
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Раздел «Конструктор».Проверка, что работают переходы к разделам:\n" +
            "          * «Булки»,\n" +
            "          * «Соусы»,\n" +
            "          * «Начинки».")
    public void constructorChangeTabParametrizedTest() {
        StellarBurgersMainPage objStellarBurgersMainPage = new StellarBurgersMainPage(driver);

        objStellarBurgersMainPage.waitForLoadMainPage();

        switch (checkingTab) {
            case "Булки":
                objStellarBurgersMainPage.clickSauceTab();
                objStellarBurgersMainPage.clickBunTub();
                break;
            case "Соусы":
                objStellarBurgersMainPage.clickSauceTab();
                break;
            case "Начинки":
                objStellarBurgersMainPage.clickFillingTab();
                break;
            default:
                System.out.println("Неподдерживаемый тип вкладки конструктора");
        }

        Assert.assertEquals(isBunTabSelected, objStellarBurgersMainPage.isBunTabSelected());
        Assert.assertEquals(isSauceTabSelected, objStellarBurgersMainPage.isSauceTabSelected());
        Assert.assertEquals(isFillingTabSelected, objStellarBurgersMainPage.isFillingTabSelected());
    }
}
