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
import static netology.web.data.SQLHelper.cleanDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyPageApiTest {
    netology.web.page.BuyingTourPage BuyingTourPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        BuyingTourPage = open("http://localhost:8080", BuyingTourPage.class);
    }

    // Backend Позитивные сценарии:
    @Test
    @DisplayName("Send a purchase request with an active bank card and valid data")
    void sendPurchaseRequestWithAnActiveBankCardAndValidData() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 200);
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Send a purchase request with a blocked bank card and valid data")
    void sendPurchaseRequestWithBlockedBankCardAndValidData() {
        CartInfo cartInfo = new CartInfo(getCartDeclined(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 403);
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("DECLINED", SQLHelper.getPurchaseStatus());
    }

    // Backend негативные сценарии:
    @Test
    @DisplayName("Send a purchase request with an active bank card and an invalid month")
    void sendPurchaseRequestWithActiveBankCardAndInvalidMonth() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getNonExistentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 400);
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Send a purchase request with an active bank card and a value less than the current year for one year")
    void sendPurchaseRequestWithActiveBankCardAndCurrentYearValueLessThanOneYear() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearLessCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 400);
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Send a purchase request with an active bank card and an invalid owner")
    void sendPurchaseRequestWithActiveBankCardAndAnInvalidOwner() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getHolderInvalid(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 400);
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Send a purchase request with an active bank card and invalid cvc")
    void sendPurchaseRequestWithActiveBankCardAndInvalidCvc() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcTwoDigits());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 400);
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Send a purchase request with a random bank card and valid data")
    void sendPurchaseRequestWithRandomBankCardAndInvalidCvc() {
        CartInfo cartInfo = new CartInfo(getGenerateCart(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestOnCredit(cartInfo, "/api/v1/pay", 400);
        assertEquals(0, SQLHelper.getNumberPurchases());
    }
}
