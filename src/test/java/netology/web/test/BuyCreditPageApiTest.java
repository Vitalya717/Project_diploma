package netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import netology.web.data.CartInfo;
import netology.web.data.SQLHelper;
import netology.web.page.BuyingTourPage;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;
import static netology.web.data.DataHelper.*;
import static netology.web.data.Rest.sendPurchaseRequestOnCredit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyCreditPageApiTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    netology.web.page.BuyingTourPage BuyingTourPage;

    @AfterEach
    void tearDown() {
        SQLHelper.cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        BuyingTourPage = open("http://localhost:8080", BuyingTourPage.class);
    }

    // Backend Позитивные сценарии в кредит:
    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and valid data")
    void sendPurchaseRequestOnCreditWithAnActiveBankCardAndValidData() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 200);
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Send a purchase request on credit with a blocked bank card and valid data")
    void sendPurchaseRequestCreditWithBlockedBankCardAndValidData() {
        CartInfo cartInfo = new CartInfo(getCartDeclined(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 403);
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("DECLINED", SQLHelper.getPurchaseCreditStatus());
    }

    // Backend негативные сценарии в кредит:
    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and an invalid month")
    void sendPurchaseRequestCreditWithActiveBankCardAndInvalidMonth() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getNonExistentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 400);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and the value is less than the current year for the current year")
    void sendPurchaseRequestCreditWithActiveBankCardAndCurrentYearValueLessThanOneYear() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearLessCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 400);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and an invalid owner")
    void sendPurchaseRequestCreditWithActiveBankCardAndAnInvalidOwner() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getHolderInvalid(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 400);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and invalid CVC")
    void sendPurchaseRequestCreditWithActiveBankCardAndInvalidCvc() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcTwoDigits());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 400);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with a random bank card and valid data")
    void sendPurchaseRequestCreditWithRandomBankCardAndInvalidCvc() {
        CartInfo cartInfo = new CartInfo(getGenerateCart(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/credit", 400);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }
}
