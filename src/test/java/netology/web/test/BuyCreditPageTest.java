package netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import netology.web.data.CartInfo;
import netology.web.data.SQLHelper;
import netology.web.page.BuyingTourPage;
import org.junit.jupiter.api.*;
import static com.codeborne.selenide.Selenide.open;
import static netology.web.data.DataHelper.*;
import static netology.web.data.Rest.*;
import static netology.web.data.Rest.sendPurchaseRequestCreditWithActiveBankCardAndInvalidData;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyCreditPageTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    BuyingTourPage BuyingTourPage;

    @AfterEach
    void tearDown() {
        SQLHelper.cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        BuyingTourPage = open("http://localhost:8080", BuyingTourPage.class);
    }

    // Frontend Позитивные сценарии в кредит:
    @Test
    @DisplayName("Fill in the form fields with valid values with the active card on credit")
    void fillFormFieldsWithValidValuesWithActiveCardCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the Month field with the value 01, and the remaining fields of the form with valid values of the active card on credit")
    void fillInTheMonthFieldWithValue01AndRemainingFieldsFormWithValidValuesCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getFirstMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the Month field with the value 12, and the remaining fields of the form with valid values of the active card on credit")
    void fillInTheMonthFieldWithValue12AndRemainingFieldsFormWithValidValuesCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getLastMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the Year field with the current year plus five years value, and the remaining fields of the form with the valid values of the active card on credit")
    void fillInTheYearFieldWithCurrentYearValueForMoreFiveYearsAndRemainingFieldsFormWithValidValuesCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearFiveYearsLongerCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the Owner field with a hyphen-separated value, and the remaining fields of the form with valid values of the active card on credit")
    void fillInHolderFieldWithHyphenSeparatedValueAndRestFormFieldsWithValidValuesCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolderDoubleSurname(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with the value 001, and the remaining fields of the form with valid values of the active card on credit")
    void fillInTheCvcCvvFieldWithValue001AndRemainingFieldsFormWithValidValuesCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcFirstNumber());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with the value 999, and the remaining fields of the form with valid values of the active card on credit")
    void fillInTheCvcCvvFieldWithValue999AndRemainingFieldsFormWithValidValuesCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcLastNumber());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Fill in the form fields with valid values with the blocked card on credit")
    void fillFormFieldsWithValidValuesWithBlockedCardCredit() {
        CartInfo cartInfo = new CartInfo(getCartDeclined(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageRefusalTheOperation();
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("DECLINED", SQLHelper.getPurchaseCreditStatus());
    }

    // Frontend Негативные сценарии в кредит:
    @Test
    @DisplayName("Fill in the Card Number field with a value of less than 16 digits, and the remaining fields of the form with valid values on credit")
    void fillCardNumberFieldWithValueOfLessThan16DigitsAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCardNumberFifteenDigits(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCardFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Leave the Card number field empty, and the remaining fields of the form with valid values on credit")
    void leaveCardNumberFieldEmptyAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCardNumberNotFilled(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCardFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Card Number field with a value consisting of 16 zeros, and the remaining fields of the form with valid values on credit")
    void fillCardNumberFieldWithValueConsisting16ZerosAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCardNumberAllZeros(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageRefusalTheOperation();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Card number field with a 16-digit value starting from zero, and the remaining fields of the form with valid values on credit")
    void fillCardNumberFieldWith16DigitValueStartingFromZeroAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getGenerateCartBeginningZero(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageRefusalTheOperation();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Card Number field with a value of than 1 digits, and the remaining fields of the form with valid values on credit")
    void fillCardNumberFieldWithValueOfThan1DigitsAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCardNumberSingleDigit(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCardFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Month field with a single-digit value, and the remaining fields of the form with valid values on credit")
    void fillMonthFieldWithSingleDigitValueAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getMonthConsistingOneDigit(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectMonthFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Leave the Month field empty, and the remaining fields of the form with valid values on credit")
    void leaveMonthFieldEmptyAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getEmptyFieldMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectMonthFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Month field with a value consisting of two zeros, and the remaining fields of the form with valid values on credit")
    void fillMonthFieldWithValueConsistingTwoZerosAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getMonthConsistingZeros(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageNonExistentMonth();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Month field with a value greater than 12, and the remaining fields of the form with valid values on credit")
    void fillMonthFieldWithValueGreaterThan12AndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getNonExistentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageNonExistentMonth();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Month field with a value less than the current month, and the remaining fields of the form with valid values on credit")
    void fillMonthFieldWithValueLessThanCurrentMonthAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYear(), getMonthLessCurrentOne(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageNonExistentMonth();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Year field with a single-digit value, and the remaining fields of the form with valid values on credit")
    void fillYearFieldWithSingleDigitValueAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearConsistingOneDigit(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectYearFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Leave the Year field empty and the remaining fields of the form with valid values on credit")
    void leaveYearFieldEmptyAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getEmptyFieldYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectYearFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Year field with a value consisting of two zeros, and the remaining fields of the form with valid values on credit")
    void fillYearFieldWithValueConsistingTwoZerosAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearConsistingZeros(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageLessThanThisYear();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Year field with a value more than five years ahead of the current date, and the remaining fields of the form with valid values on credit")
    void fillYearFieldWithValueMoreThanFiveYearsAheadCurrentDateAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearSixYearsLongerCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageFiveYearsMoreThanCurrentYear();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Year field with a value less than the current year, and the remaining fields of the form with valid values on credit")
    void fillYearFieldWithValueLessThanCurrentYearAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearLessCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageLessThanThisYear();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with one Latin letter and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithOneLatinLetterAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getOneLatinLetter(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a Cyrillic letter value and the rest of the form fields with valid values on credit")
    void fillOwnerFieldWithCyrillicLetterValueAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getHolderInvalid(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value consisting of characters and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithValueConsistingCharactersAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getFilledWithSpecialCharactersHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a numeric value and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithNumericValueAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getFilledWithNumbersHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value starting with a hyphen and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithValueStartingWithHyphenAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getStartsWithHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value ending in a hyphen and the rest of the form fields with valid values on credit")
    void fillOwnerFieldWithValueEndingHyphenAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getEndsWithHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value starting with a Spaces and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithValueStartingWithSpacesAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getStartsWithSpaceHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value ending in a Spaces and the rest of the form fields with valid values on credit")
    void fillOwnerFieldWithValueEndingSpacesAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getEndsWithSpaceHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectHolderFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Leave the Owner field empty, and the remaining fields of the form with valid values on credit")
    void leaveOwnerFieldEmptyAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getEmptyFieldHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageBlankHolderField();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value consisting only of a valid last name and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithValueConsistingOnlyValidLastNameAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getLastNameHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageBlankHolderField();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value consisting only of a valid name and the remaining fields of the form with valid values on credit")
    void fillOwnerFieldWithValueConsistingOnlyValidNameAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getNameHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageBlankHolderField();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with a value less than three digits, and the remaining fields of the form with valid values on credit")
    void fillCvcFieldWithValueLessThanThreeDigitsAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcTwoDigits());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCvcFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with a single-digit value and the remaining fields of the form with valid values on credit")
    void fillCvcFieldWithSingleDigitValueAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcOneDigit());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCvcFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with a value consisting of three zeros and the remaining fields of the form with valid values on credit")
    void fillCvcFieldWithValueConsistingThreeZerosAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcZeros());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCvcFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Leave the CVC/CVV field empty and the remaining fields of the form with valid values on credit")
    void leaveCvcFieldEmptyAndRemainingFieldsFormWithValidValuesOnCredit() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcEmpty());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchaseCreditPage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectCvcFormat();
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    // Backend Позитивные сценарии в кредит:
    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and valid data")
    void sendPurchaseRequestOnCreditWithAnActiveBankCardAndValidData() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendCreditRequestWithActiveCard(cartInfo);
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("APPROVED", SQLHelper.getPurchaseCreditStatus());
    }

    @Test
    @DisplayName("Send a purchase request on credit with a blocked bank card and valid data")
    void sendPurchaseRequestCreditWithBlockedBankCardAndValidData() {
        CartInfo cartInfo = new CartInfo(getCartDeclined(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendCreditRequestWithBlockedCard(cartInfo);
        assertEquals(1, SQLHelper.getNumberPurchasesCredit());
        assertEquals("DECLINED", SQLHelper.getPurchaseCreditStatus());
    }

    // Backend негативные сценарии в кредит:
    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and an invalid month")
    void sendPurchaseRequestCreditWithActiveBankCardAndInvalidMonth() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getNonExistentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestCreditWithActiveBankCardAndInvalidData(cartInfo);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and the value is less than the current year for the current year")
    void sendPurchaseRequestCreditWithActiveBankCardAndCurrentYearValueLessThanOneYear() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearLessCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestCreditWithActiveBankCardAndInvalidData(cartInfo);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and an invalid owner")
    void sendPurchaseRequestCreditWithActiveBankCardAndAnInvalidOwner() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getHolderInvalid(), getValidCvc());
        sendPurchaseRequestCreditWithActiveBankCardAndInvalidData(cartInfo);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with an active bank card and invalid CVC")
    void sendPurchaseRequestCreditWithActiveBankCardAndInvalidCvc() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcTwoDigits());
        sendPurchaseRequestCreditWithActiveBankCardAndInvalidData(cartInfo);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

    @Test
    @DisplayName("Send a purchase request on credit with a random bank card and valid data")
    void sendPurchaseRequestCreditWithRandomBankCardAndInvalidCvc() {
        CartInfo cartInfo = new CartInfo(getGenerateCart(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        sendPurchaseRequestCreditWithActiveBankCardAndInvalidData(cartInfo);
        assertEquals(0, SQLHelper.getNumberPurchasesCredit());
    }

}
