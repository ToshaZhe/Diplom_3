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
import ru.yandex.practicum.ui.pages.StellarBurgersLoginPage;
import ru.yandex.practicum.ui.pages.StellarBurgersMainPage;
import ru.yandex.practicum.ui.pages.StellarBurgersRegisterPage;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getUserRequest;

@RunWith(Parameterized.class)
public class CreatingUserParametrizedPasswordTest {
    private final String password;
    private final boolean isPositiveTest;
    private final String browser;
    WebDriver driver;
    private String name;
    private String email;
    private UserRestApiSteps userRequest;
    private String token;

    public CreatingUserParametrizedPasswordTest(String password, boolean isPositiveTest, String browser) {
        this.password = password;
        this.isPositiveTest = isPositiveTest;
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Object[][] getPasswordExpectedResultBrowser() {
        return new Object[][]{{"qwerty", true, "chrome"}, {"qwer", false, "chrome"},
                {"qwerty", true, "yandex"}, {"qwer", false, "yandex"}};
    }

    @Before
    public void setup() {
        driver = Config.getWebDriver(browser);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(Config.getBaseUrl());
        name = "TestUser" + RandomStringUtils.randomAlphanumeric(4);
        email = name + "@ya.ru";
        token = null;
        userRequest = new UserRestApiSteps();
    }

    @After
    public void tearDown() {
        driver.quit();
        if (token != null) {
            userRequest.deleteUser(token).assertThat().statusCode(SC_ACCEPTED).and().
                    body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Создание пользователя с валидным и невалидным паролем в Chrome и Yandex браузерах ")
    public void userCreatingParametrizedTest() {
        UserRequest userRequestForDelete;
        StellarBurgersMainPage objStellarBurgersMainPage = new StellarBurgersMainPage(driver);
        StellarBurgersLoginPage objStellarBurgersLoginPage = new StellarBurgersLoginPage(driver);
        StellarBurgersRegisterPage objStellarBurgersRegisterPage = new StellarBurgersRegisterPage(driver);

        objStellarBurgersMainPage.waitForLoadMainPage();
        objStellarBurgersMainPage.clickLogonButton();
        objStellarBurgersLoginPage.waitForLoadLoginPage();
        objStellarBurgersLoginPage.clickSignUpLink();
        objStellarBurgersRegisterPage.waitForLoadRegisterPage();
        objStellarBurgersRegisterPage.signUpUser(name, email, password);

        if (isPositiveTest) {
            Assert.assertTrue(objStellarBurgersLoginPage.isLogonButtonVisible());

            userRequestForDelete = getUserRequest(name, email, password);
            ValidatableResponse loginResponse = userRequest.loginUser(userRequestForDelete);
            loginResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
            token = loginResponse.extract().path("accessToken");
        } else {
            Assert.assertTrue(objStellarBurgersRegisterPage.isIncorrectPasswordWarnDisplayed());
        }
    }
}
