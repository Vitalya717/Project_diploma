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
    private final SelenideElement incorrectCard = $(byText("Неверный формат")).parent().$(".input__sub"); // неверный формат поля
    private final SelenideElement nonExistent = $(byText("Неверно указан срок действия карты")).parent().$(".input__sub"); // несуществующий месяц либо год
    private final SelenideElement lessThanThis = $(byText("Истёк срок действия карты")).parent().$(".input__sub"); // меньще текушего года либо месяц
    private final SelenideElement blankHolderField = $(byText("Поле обязательно для заполнения")).parent().$(".input__sub"); //  полe владедец должно быть заполнено


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

    public void messageIncorrectFormat() {
        incorrectCard.shouldBe(visible); // сообщение о не верном формате поля
    }


    public void messageNonExistent() {
        nonExistent.shouldBe(visible); // сообщение о несуществующем месяце либо годе
    }


    public void messageDateSpecifiedLessThanCurrentOne() {
        lessThanThis.shouldBe(visible); // сообщение, что дата указана меньше, чем текущая
    }


    public void messageBlankHolderField() {
        blankHolderField.shouldBe(visible); // сообщение о том, что поля владелец должно быть заполнено
    }
}
