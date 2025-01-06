package netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import netology.web.data.CartInfo;
import netology.web.data.SQLHelper;
import netology.web.page.BuyingTourPage;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;
import static netology.web.data.DataHelper.*;
import static netology.web.data.SQLHelper.cleanDatabase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyPageTest {

    BuyingTourPage BuyingTourPage;

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

    // Frontend Позитивные сценарии
    @Test
    @DisplayName("Fill in the form fields with valid values with the active card")
    void fillFormFieldsWithValidValuesWithActiveCard() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Fill in the Month field with the value 01, and the remaining fields of the form with valid values of the active card")
    void fillInTheMonthFieldWithValue01AndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getFirstMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Fill in the Month field with the value 12, and the remaining fields of the form with valid values of the active card")
    void fillInTheMonthFieldWithValue12AndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getLastMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Fill in the Year field with the current year plus five years value, and the remaining fields of the form with the valid values of the active card")
    void fillInTheYearFieldWithCurrentYearValueForMoreFiveYearsAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearFiveYearsLongerCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Fill in the Owner field with a hyphen-separated value, and the remaining fields of the form with valid values of the active card")
    void fillInHolderFieldWithHyphenSeparatedValueAndRestFormFieldsWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolderDoubleSurname(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with the value 001, and the remaining fields of the form with valid values of the active card")
    void fillInTheCvcCvvFieldWithValue001AndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcFirstNumber());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with the value 999, and the remaining fields of the form with valid values of the active card")
    void fillInTheCvcCvvFieldWithValue999AndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcLastNumber());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
    }

    @Test
    @DisplayName("The purchase amount displayed in the database using the active card")
    void amountPurchaseUsingActiveCard() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageSuccessfulOperation();
        assertEquals("APPROVED", SQLHelper.getPurchaseStatus());
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals(45000, SQLHelper.getPurchaseAmount());
    }

    @Test
    @DisplayName("Fill in the form fields with valid values with the blocked card")
    void fillFormFieldsWithValidValuesWithBlockedCard() {
        CartInfo cartInfo = new CartInfo(getCartDeclined(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageRefusalTheOperation();
        assertEquals(1, SQLHelper.getNumberPurchases());
        assertEquals("DECLINED", SQLHelper.getPurchaseStatus());
    }

    // Frontend Негативные сценарии
    @Test
    @DisplayName("Fill in the Card Number field with a value of less than 16 digits, and the remaining fields of the form with valid values")
    void fillCardNumberFieldWithValueOfLessThan16DigitsAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCardNumberFifteenDigits(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Leave the Card number field empty, and the remaining fields of the form with valid values")
    void leaveCardNumberFieldEmptyAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCardNumberNotFilled(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Card Number field with a value consisting of 16 zeros, and the remaining fields of the form with valid values")
    void fillCardNumberFieldWithValueConsisting16ZerosAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCardNumberAllZeros(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageRefusalTheOperation();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Card number field with a 16-digit value starting from zero, and the remaining fields of the form with valid values")
    void fillCardNumberFieldWith16DigitValueStartingFromZeroAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getGenerateCartBeginningZero(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageRefusalTheOperation();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Card Number field with a value of than 1 digits, and the remaining fields of the form with valid values")
    void fillCardNumberFieldWithValueOfThan1DigitsAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCardNumberSingleDigit(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Month field with a single-digit value, and the remaining fields of the form with valid values")
    void fillMonthFieldWithSingleDigitValueAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getMonthConsistingOneDigit(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Leave the Month field empty, and the remaining fields of the form with valid values")
    void leaveMonthFieldEmptyAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getEmptyFieldMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Month field with a value consisting of two zeros, and the remaining fields of the form with valid values")
    void fillMonthFieldWithValueConsistingTwoZerosAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getMonthConsistingZeros(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageNonExistent();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Month field with a value greater than 12, and the remaining fields of the form with valid values")
    void fillMonthFieldWithValueGreaterThan12AndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getNonExistentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageNonExistent();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    //@Test
    //@DisplayName("Fill in the Month field with a value less than the current month, and the remaining fields of the form with valid values")
    void fillMonthFieldWithValueLessThanCurrentMonthAndRemainingFieldsFormWithValidValues() {
        //CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYear(), getMonthLessCurrentOne(), getValidHolder(), getValidCvc());
        //var startPage = new BuyingTourPage();
        //var purchase = startPage.goToPurchasePage();
        //purchase.completedForm(cartInfo);
        //purchase.messageNonExistentMonth();
        //assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Year field with a single-digit value, and the remaining fields of the form with valid values")
    void fillYearFieldWithSingleDigitValueAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearConsistingOneDigit(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Leave the Year field empty and the remaining fields of the form with valid values")
    void leaveYearFieldEmptyAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getEmptyFieldYear(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Year field with a value consisting of two zeros, and the remaining fields of the form with valid values")
    void fillYearFieldWithValueConsistingTwoZerosAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearConsistingZeros(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageDateSpecifiedLessThanCurrentOne();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Year field with a value more than five years ahead of the current date, and the remaining fields of the form with valid values")
    void fillYearFieldWithValueMoreThanFiveYearsAheadCurrentDateAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearSixYearsLongerCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageNonExistent();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Year field with a value less than the current year, and the remaining fields of the form with valid values")
    void fillYearFieldWithValueLessThanCurrentYearAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getYearLessCurrentOne(), getCurrentMonth(), getValidHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageDateSpecifiedLessThanCurrentOne();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with one Latin letter and the remaining fields of the form with valid values")
    void fillOwnerFieldWithOneLatinLetterAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getOneLatinLetter(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a Cyrillic letter value and the rest of the form fields with valid values")
    void fillOwnerFieldWithCyrillicLetterValueAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getHolderInvalid(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value consisting of characters and the remaining fields of the form with valid values")
    void fillOwnerFieldWithValueConsistingCharactersAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getFilledWithSpecialCharactersHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a numeric value and the remaining fields of the form with valid values")
    void fillOwnerFieldWithNumericValueAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getFilledWithNumbersHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value starting with a hyphen and the remaining fields of the form with valid values")
    void fillOwnerFieldWithValueStartingWithHyphenAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getStartsWithHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value ending in a hyphen and the rest of the form fields with valid values")
    void fillOwnerFieldWithValueEndingHyphenAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getEndsWithHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value starting with a Spaces and the remaining fields of the form with valid values")
    void fillOwnerFieldWithValueStartingWithSpacesAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getStartsWithSpaceHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value ending in a Spaces and the rest of the form fields with valid values")
    void fillOwnerFieldWithValueEndingSpacesAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getEndsWithSpaceHyphen(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Leave the Owner field empty, and the remaining fields of the form with valid values")
    void leaveOwnerFieldEmptyAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getEmptyFieldHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageBlankHolderField();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value consisting only of a valid last name and the remaining fields of the form with valid values")
    void fillOwnerFieldWithValueConsistingOnlyValidLastNameAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getLastNameHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageBlankHolderField();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the Owner field with a value consisting only of a valid name and the remaining fields of the form with valid values")
    void fillOwnerFieldWithValueConsistingOnlyValidNameAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getNameHolder(), getValidCvc());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageBlankHolderField();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with a value less than three digits, and the remaining fields of the form with valid values")
    void fillCvcFieldWithValueLessThanThreeDigitsAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcTwoDigits());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with a single-digit value and the remaining fields of the form with valid values")
    void fillCvcFieldWithSingleDigitValueAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcOneDigit());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Fill in the CVC/CVV field with a value consisting of three zeros and the remaining fields of the form with valid values")
    void fillCvcFieldWithValueConsistingThreeZerosAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcZeros());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

    @Test
    @DisplayName("Leave the CVC/CVV field empty and the remaining fields of the form with valid values")
    void leaveCvcFieldEmptyAndRemainingFieldsFormWithValidValues() {
        CartInfo cartInfo = new CartInfo(getCartApproved(), getCurrentYearIsUpByOneYear(), getCurrentMonth(), getValidHolder(), getCvcEmpty());
        var startPage = new BuyingTourPage();
        var purchase = startPage.goToPurchasePage();
        purchase.completedForm(cartInfo);
        purchase.messageIncorrectFormat();
        assertEquals(0, SQLHelper.getNumberPurchases());
    }

}
