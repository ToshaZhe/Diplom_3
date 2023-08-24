package ru.yandex.practicum.ui.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import ru.yandex.practicum.api.pojo.UserRequest;
import ru.yandex.practicum.api.steps.UserRestApiSteps;
import ru.yandex.practicum.configs.Config;
import ru.yandex.practicum.ui.pages.StellarBurgersLoginPage;
import ru.yandex.practicum.ui.pages.StellarBurgersMainPage;
import ru.yandex.practicum.ui.pages.StellarBurgersProfilePage;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getUserRequest;

public class PersonalAccountTest {
    WebDriver driver;
    private String name;
    private String email;
    private String token;
    private UserRestApiSteps userRequestApi;
    private StellarBurgersMainPage objStellarBurgersMainPage;
    private StellarBurgersLoginPage objStellarBurgersLoginPage;
    private StellarBurgersProfilePage objStellarBurgersProfilePage;

    @Before
    public void setup() {
        UserRequest userRequest;
        name = "TestUser" + RandomStringUtils.randomAlphanumeric(4);
        email = name + "@ya.ru";
        String password = "qwerty";
        token = null;
        userRequestApi = new UserRestApiSteps();

        driver = Config.getWebDriver("chrome");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Config.getBaseUrl());

        objStellarBurgersMainPage = new StellarBurgersMainPage(driver);
        objStellarBurgersLoginPage = new StellarBurgersLoginPage(driver);
        objStellarBurgersProfilePage = new StellarBurgersProfilePage(driver);

        userRequest = getUserRequest(name, email, password);
        ValidatableResponse createResponse = userRequestApi.createUser(userRequest);
        createResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = createResponse.extract().path("accessToken");

        objStellarBurgersMainPage.clickLogonButton();
        objStellarBurgersLoginPage.waitForLoadLoginPage();
        objStellarBurgersLoginPage.logonUser(email, password);
    }

    @After
    public void tearDown() {
        driver.quit();
        if (token != null) {
            userRequestApi.deleteUser(token).assertThat().statusCode(SC_ACCEPTED).and().
                    body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Переход в личный кабинет с главной страницы")
    public void clickOnPersonAccButtonShouldBeTransferToProfilePageTest() {
        objStellarBurgersMainPage.waitForLoadMainPage();
        objStellarBurgersMainPage.clickPersonalAccountButton();
        objStellarBurgersProfilePage.waitForProfilePage();

        Assert.assertEquals(name, objStellarBurgersProfilePage.getNameField());
        Assert.assertEquals(email.toLowerCase(), objStellarBurgersProfilePage.getLoginField());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструткор по кнопке «Конструктор»")
    public void clickOnConstructorShouldBeTransferToMainPageTest() {
        objStellarBurgersMainPage.waitForLoadMainPage();
        objStellarBurgersMainPage.clickPersonalAccountButton();
        objStellarBurgersProfilePage.waitForProfilePage();
        objStellarBurgersProfilePage.clickConstructorButton();
        objStellarBurgersMainPage.waitForLoadMainPage();

        Assert.assertEquals("Соберите бургер", objStellarBurgersMainPage.getCreateBurgerText());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструткор по клику на логотип")
    public void clickOnLogoShouldBeTransferToMainPageTest() {
        objStellarBurgersMainPage.waitForLoadMainPage();
        objStellarBurgersMainPage.clickPersonalAccountButton();
        objStellarBurgersProfilePage.waitForProfilePage();
        objStellarBurgersProfilePage.clickLogoButton();
        objStellarBurgersMainPage.waitForLoadMainPage();

        Assert.assertEquals("Соберите бургер", objStellarBurgersMainPage.getCreateBurgerText());
    }

    @Test
    @DisplayName("Выход из аккаунта по кнопке «Выйти» в личном кабинете")
    public void clickOnLogoutButtonShouldBeLogOutUserTest() {
        objStellarBurgersMainPage.waitForLoadMainPage();
        objStellarBurgersMainPage.clickPersonalAccountButton();
        objStellarBurgersProfilePage.waitForProfilePage();
        objStellarBurgersProfilePage.clickLogoutButton();
        objStellarBurgersLoginPage.waitForLoadLoginPage();

        Assert.assertTrue(objStellarBurgersLoginPage.isLogonButtonVisible());
    }
}
