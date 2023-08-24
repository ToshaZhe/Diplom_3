package ru.yandex.practicum.ui.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import ru.yandex.practicum.api.pojo.UserRequest;
import ru.yandex.practicum.api.steps.UserRestApiSteps;
import ru.yandex.practicum.configs.Config;
import ru.yandex.practicum.ui.pages.StellarBurgersForgotPasswordPage;
import ru.yandex.practicum.ui.pages.StellarBurgersLoginPage;
import ru.yandex.practicum.ui.pages.StellarBurgersMainPage;
import ru.yandex.practicum.ui.pages.StellarBurgersRegisterPage;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getUserRequest;

@RunWith(Parameterized.class)
public class LogonUserParametrizedTest {
    private final String logonPlace;
    private final String browser;
    WebDriver driver;
    private String email;
    private String password;
    private UserRestApiSteps userRequestApi;
    private String token;

    public LogonUserParametrizedTest(String logonPlace, String browser) {
        this.logonPlace = logonPlace;
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Object[][] getLogonPlaceAndBrowser() {
        return new Object[][]{{"main page logon button", "chrome"}, {"main page personal account button", "chrome"},
                {"registration page", "yandex"}, {"forgot password page", "yandex"}};
    }

    @Before
    public void setup() {
        UserRequest userRequest;
        String name = "TestUser" + RandomStringUtils.randomAlphanumeric(4);
        email = name + "@ya.ru";
        password = "qwerty";
        token = null;
        userRequestApi = new UserRestApiSteps();

        driver = Config.getWebDriver(browser);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Config.getBaseUrl());

        userRequest = getUserRequest(name, email, password);
        ValidatableResponse createResponse = userRequestApi.createUser(userRequest);
        createResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = createResponse.extract().path("accessToken");
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
    @DisplayName("Вход пользователя из различных мест")
    public void successLogonFromDifferentPlacesTest() {
        StellarBurgersMainPage objStellarBurgersMainPage = new StellarBurgersMainPage(driver);
        StellarBurgersLoginPage objStellarBurgersLoginPage = new StellarBurgersLoginPage(driver);
        StellarBurgersRegisterPage objStellarBurgersRegisterPage = new StellarBurgersRegisterPage(driver);
        StellarBurgersForgotPasswordPage objStellarBurgersForgotPasswordPage = new StellarBurgersForgotPasswordPage(driver);

        objStellarBurgersMainPage.waitForLoadMainPage();

        switch (logonPlace) {
            case "main page logon button":
                objStellarBurgersMainPage.clickLogonButton();
                break;
            case "main page personal account button":
                objStellarBurgersMainPage.clickPersonalAccountButton();
                break;
            case "registration page":
                objStellarBurgersMainPage.clickLogonButton();
                objStellarBurgersLoginPage.waitForLoadLoginPage();
                objStellarBurgersLoginPage.clickSignUpLink();
                objStellarBurgersRegisterPage.waitForLoadRegisterPage();
                objStellarBurgersRegisterPage.clickLogonLink();
                break;
            case "forgot password page":
                objStellarBurgersMainPage.clickLogonButton();
                objStellarBurgersLoginPage.waitForLoadLoginPage();
                objStellarBurgersLoginPage.clickForgotPasswordLink();
                objStellarBurgersForgotPasswordPage.waitForLoadForgotPasswordPage();
                objStellarBurgersForgotPasswordPage.clickLogonLink();
                break;
            default:
                throw new RuntimeException("Неподдерживаемое место для логона");
        }

        objStellarBurgersLoginPage.waitForLoadLoginPage();
        objStellarBurgersLoginPage.logonUser(email, password);
        Assert.assertTrue(objStellarBurgersMainPage.isPlaceOrderButtonVisible());
    }
}
