package netology.web.data;

import com.github.javafaker.Faker;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerCyrillic = new Faker(new Locale("ru"));

    public static String getCartApproved() { //активная банковская карта
        return "4444 4444 4444 4441";
    }
    public static String getCartDeclined() { //заблокированная банковская карта
        return "4444 4444 4444 4442";
    }
    public static String getGenerateCart() { //сгенерировать банковскую карту из 16 цифр
        String cart = faker.numerify("#### #### #### ####");
        return cart;
    }
    public static String getGenerateCartBeginningZero() { //сгенерировать банковскую карту из 16 цифр начинаюшиеся с 0
        String cart = faker.numerify("0### #### #### ####");
        return cart;
    }
    public static String getCardNumberAllZeros() { //номер банковской карты со веми нулями
        return "0000 0000 0000 0000";
    }
    public static String getCardNumberNotFilled() { //не заполненный номер бакуковской карты
        return "";
    }
    public static String getCardNumberFifteenDigits() { //сгенерировать банковскую карту из 15 цифр
        String cart = faker.numerify("#### #### #### ###");
        return cart;
    }
    public static String getCardNumberSingleDigit() { //сгенерировать банковскую карту из 1 цифры
        String cart = faker.numerify("#");
        return cart;
    }
    public static String getCurrentYearIsUpByOneYear() { //текущий год, больше на один год
        String year = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }
    public static String getCurrentYear() { //текущий год
        String year = LocalDate.now().plusYears(0).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }
    public static String getYearLessCurrentOne() { // год меньше текущего
        String year = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }
    public static String getYearSixYearsLongerCurrentOne() { // год на шесть лет больше чем текущий
        String year = LocalDate.now().plusYears(6).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }
    public static String getYearFiveYearsLongerCurrentOne() { // год на пять лет больше чем текущий
        String year = LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy"));
        return year;
    }
    public static String getYearConsistingZeros() { // год состоящий из нулей
        return "00";
    }
    public static String getYearConsistingOneDigit() { // год состоящий из одной цифры
        String year = faker.numerify("#");
        return year;
    }
    public static String getEmptyFieldYear() { // год отсутствует
        return "";
    }
    public static String getCurrentMonth() { //текущий месяц
        String month = LocalDate.now().plusMonths(0).format(DateTimeFormatter.ofPattern("MM"));
        return month;
    }
    public static String getMonthLessCurrentOne() { //месяц меньше текущего
        String month = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MM"));
        return month;
    }
    public static String getFirstMonth() { //первый месяц
        return "01";
    }
    public static String getLastMonth() { //последний месяц
        return "12";
    }
    public static String getNonExistentMonth() { //первый месяц
        return "13";
    }

    public static String getMonthConsistingZeros() { // месяц состоящий из нулей
        return "00";
    }
    public static String getMonthConsistingOneDigit() { // месяц состоящий из одной цифры
        String month = faker.numerify("#");
        return month;
    }
    public static String getEmptyFieldMonth() { // месяц отсутствует
        return "";
    }

    public static String getValidHolder() { //сгенерированный валидного владелеца
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String holder = lastName + " " + firstName;
        return holder;
    }
    public static String getValidHolderDoubleSurname() { //сгенерированный валидного владелеца с двойной фамилией
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String holder = lastName + "-" + lastName + " " + firstName;
        return holder;
    }
    public static String getHolderInvalid() { //сгенерированный владелец буквами кириллицы
        String firstName = fakerCyrillic.name().firstName();
        String lastName = fakerCyrillic.name().lastName();
        String holder = lastName + " " + firstName;
        return holder;
    }
    public static String getEmptyFieldHolder() { //пустое поле владелеца
        return "";
    }
    public static String getOneLatinLetter() { //одна латинская буква в поле владелеца
        return "V";
    }
    public static String getFilledWithNumbersHolder() { // владелец заполненный цифрами
        String firstName = faker.numerify("#######");
        String lastName = faker.numerify("########");
        String holder = lastName + " " + firstName;
        return holder;
    }
    public static String getFilledWithSpecialCharactersHolder() { // владелец заполненный цифрами
        return "!$*|/";
    }
    public static String getNameHolder() { //сгенерировать имя владелеца
        String firstName = faker.name().firstName();
        return firstName;
    }

    public static String getLastNameHolder() { //сгенерировать фамилию владелеца
        String lastName = faker.name().lastName();
        return lastName;
    }
    public static String getStartsWithHyphen() { //владелиц начинается с дефиса
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String holder = "-" + lastName + " " + firstName;
        return holder;
    }
    public static String getEndsWithHyphen() { //владелиц оканчивается на дефис
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String holder = lastName + " " + firstName + "-";
        return holder;
    }

    public static String getStartsWithSpaceHyphen() { //владелиц начинается с пробела
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String holder = " " + lastName + " " + firstName;
        return holder;
    }
    public static String getEndsWithSpaceHyphen() { //владелиц оканчивается на пробел
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String holder = lastName + " " + firstName + " ";
        return holder;
    }
    public static String getValidCvc() { //сгенерировать валидный cvc
        String cvc = faker.numerify("###");
        return cvc;
    }
    public static String getCvcTwoDigits() { //сгенерировать cvc из двцх цифр
        String cvc = faker.numerify("##");
        return cvc;
    }
    public static String getCvcOneDigit() { //сгенерировать cvc из одной цифр
        String cvc = faker.numerify("#");
        return cvc;
    }
    public static String getCvcZeros() { //сгенерировать cvc из нулей
        return "000";
    }
    public static String getCvcFirstNumber() {
        return "001";
    }
    public static String getCvcLastNumber() {
        return "999";
    }

    public static String getCvcEmpty() { //пустой cvc
        return "";
    }

}
