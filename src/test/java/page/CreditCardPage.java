package page;

import data.DataHelper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditCardPage {
    private SelenideElement cardNumberForm = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthForm = $("[placeholder='08']");
    private SelenideElement yearForm = $("[placeholder='22']");
    private SelenideElement ownerForm = $$("[class='input__control']").get(3);
    private SelenideElement cvcForm = $("[placeholder='999']");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement successfulNotification = $(byText("Операция одобрена Банком."));
    private SelenideElement errorNotification = $(byText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement emptyFieldOwner = $(byText("Поле обязательно для заполнения"));
    private SelenideElement emptyField = $(byText("Неверный формат"));
    private SelenideElement wrongFormat = $(byText("Неверный формат"));
    private SelenideElement wrongCardDate = $(byText("Неверно указан срок действия карты"));
    private SelenideElement cardExpired = $(byText("Истёк срок действия карты"));

    public void filledForm(DataHelper.CardInfo cardInfo, DataHelper.MonthInfo monthInfo, DataHelper.YearInfo yearInfo, DataHelper.OwnerInfo ownerInfo, DataHelper.CvcInfo cvcInfo) {
        cardNumberForm.setValue(cardInfo.getCardNumber());
        monthForm.setValue(monthInfo.getMonth());
        yearForm.setValue(yearInfo.getYear());
        ownerForm.setValue(ownerInfo.getOwner());
        cvcForm.setValue(cvcInfo.getCvc());
        continueButton.click();
    }


    public void waitSuccessfulNotification() {
        successfulNotification.should(visible, Duration.ofSeconds(10));
    }

    public void waitErrorNotification() {
        errorNotification.should(visible, Duration.ofSeconds(5));
    }

    public void waitEmptyField() {
        emptyField.should(visible);
    }

    public void waitEmptyFieldOwner() {
        emptyFieldOwner.should(visible);
    }

    public void waitWrongFormat() {
        wrongFormat.should(visible);
    }

    public void waitWrongCardDate() {
        wrongCardDate.should(visible);
    }

    public void waitCardExpired() {
        cardExpired.should(visible);
    }

    public void onlyCardField(DataHelper.CardInfo cardInfo) {
        cardNumberForm.setValue(cardInfo.getCardNumber());
    }

    public void emptyCardField() {
        cardNumberForm.should(Condition.empty);
    }


    public void onlyCVCField(DataHelper.CvcInfo cvcInfo) {
        cvcForm.setValue(cvcInfo.getCvc());
    }

    public void emptyCVCField() {
        cvcForm.should(empty);
    }

}