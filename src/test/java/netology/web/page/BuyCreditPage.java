package netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import netology.web.data.CartInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyCreditPage {
    private final SelenideElement paymentOnCredit = $$("h3").find(exactText("Кредит по данным карты"));
    private final SelenideElement numberField = $(byText("Номер карты")).parent().$(".input__control");
    private final SelenideElement monthField = $(byText("Месяц")).parent().$(".input__control");
    private final SelenideElement yearField = $(byText("Год")).parent().$(".input__control");
    private final SelenideElement holderField = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement cvcField = $(byText("CVC/CVV")).parent().$(".input__control");
    private final SelenideElement continueButton = $$(".button").find(exactText("Продолжить"));
    private final SelenideElement messageAboutSending = $$(".button").find(exactText("Отправляем запрос в Банк..."));

    private final SelenideElement successfulOperation = $(".notification_status_ok"); // успешная операция
    private final SelenideElement refusalTheOperation = $(".notification_status_error"); // отказ в операции
    private final SelenideElement incorrectCardFormat = $(byText("Неверный формат")).parent().$(".input__sub"); // неверный формат поля карты
    private final SelenideElement incorrectMonthFormat = $(byText("Неверный формат")).parent().$(".input__sub"); // неверный формат поля месяц
    private final SelenideElement nonExistentMonth = $(byText("Неверно указан срок действия карты")).parent().$(".input__sub"); // несуществующий месяц
    private final SelenideElement incorrectYearFormat = $(byText("Неверный формат")).parent().$(".input__sub"); // неверный формат поля год
    private final SelenideElement lessThanThisYear = $(byText("Истёк срок действия карты")).parent().$(".input__sub"); // меньще текушего года
    private final SelenideElement fiveYearsMoreThanCurrentYear = $(byText("Неверно указан срок действия карты")).parent().$(".input__sub"); // больше текушего года на пять лет
    private final SelenideElement incorrectHolderFormat = $(byText("Неверный формат")).parent().$(".input__sub"); // неверный формат поля владедец
    private final SelenideElement blankHolderField = $(byText("Поле обязательно для заполнения")).parent().$(".input__sub"); //  полe владедец должно быть заполнено
    private final SelenideElement incorrectCvcFormat = $(byText("Неверный формат")).parent().$(".input__sub"); // неверный формат поля cvc

    public BuyCreditPage() {
        paymentOnCredit.shouldBe(Condition.visible);
    }

    public BuyCreditPage completedForm(CartInfo info) { // заполненная форма данными
        numberField.setValue(info.getNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        holderField.setValue(info.getHolder());
        cvcField.setValue(info.getCvc());
        continueButton.click();
        return new BuyCreditPage();
    }

    public void messageSuccessfulOperation() {
        messageAboutSending.getText();
        successfulOperation.shouldBe(visible, Duration.ofSeconds(15)); // сообщение об успешной операции

    }

    public void messageRefusalTheOperation() {
        messageAboutSending.getText();
        refusalTheOperation.shouldBe(visible, Duration.ofSeconds(15)); // сообщение об отказе в операции
    }

    public void messageIncorrectCardFormat() {
        incorrectCardFormat.shouldBe(Condition.visible); // сообщение о не верном формате поля карта
    }

    public void messageIncorrectMonthFormat() {
        incorrectMonthFormat.shouldBe(Condition.visible); // сообщение о не верном формате поля месяц
    }

    public void messageNonExistentMonth() {
        nonExistentMonth.shouldBe(Condition.visible); // сообщение о несуществующем месяце
    }

    public void messageIncorrectYearFormat() {
        incorrectYearFormat.shouldBe(Condition.visible); // сообщение о не верном формате поля год
    }

    public void messageLessThanThisYear() {
        lessThanThisYear.shouldBe(Condition.visible); // сообщение, что год указан меньше, чем текущий год
    }

    public void messageFiveYearsMoreThanCurrentYear() {
        fiveYearsMoreThanCurrentYear.shouldBe(Condition.visible); // сообщение, что год указан больше на пять лет, чем текущий год
    }

    public void messageIncorrectHolderFormat() {
        incorrectHolderFormat.shouldBe(Condition.visible); // сообщение о не верном формате поля владелец
    }

    public void messageBlankHolderField() {
        blankHolderField.shouldBe(visible); // сообщение о том, что поля владелец должно быть заполнено
    }

    public void messageIncorrectCvcFormat() {
        incorrectCvcFormat.shouldBe(Condition.visible); // сообщение о не верном формате поля Cvc
    }
}
