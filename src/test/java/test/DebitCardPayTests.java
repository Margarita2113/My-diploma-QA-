package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.DebitCardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DebitCardPayTests {

    private MainPage mainPage;
    private DebitCardPage debitCardPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    public void setup() {
        mainPage = open("http://localhost:8080/", MainPage.class);
    }

    @AfterEach
    void clean() {
        SQLHelper.clear();
    }

    @Test
    //Позитивный сценарий 2.1
    public void shouldSuccessfullyWithApprovedCard() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitSuccessfulNotification();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    //Позитивный сценарий 2.2
    public void shouldDenyWithRefusalCard() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getSecondCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitErrorNotification();
        var expected = DataHelper.getSecondCardStatus();
        var actual = SQLHelper.getDebitPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    //Сценарий 2.1
    public void shouldBeEmptyCardFieldWithLatinChars() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("EN");
        debitCardPage.onlyCardField(cardNumber);
        debitCardPage.emptyCardField();
    }

    @Test
    //Сценарий 2.2
    public void shouldBeEmptyCardFieldWithCyrillicChars() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("RU");
        debitCardPage.onlyCardField(cardNumber);
        debitCardPage.emptyCardField();
    }

    @Test
    //Сценарий 2.3
    public void shouldDenyWithThreeDigitsCardField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("123");
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.onlyCardField(cardNumber);
        debitCardPage.waitEmptyField();
    }


    @Test
    // Сценарий 2.4
    public void shouldDenyWithEmptyCardField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getEmptyCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitEmptyField();
    }

    @Test
    // Сценарий 2.5
    public void shouldDenyOwnerFieldWithCyrillicChars() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("RU");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitWrongFormat();
    }

    @Test
    // Сценарий 2.6
    public void shouldDenyOwnerFieldWithHieroglyphsChars() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("ja");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitWrongFormat();
    }

    @Test
    // Сценарий 2.7
    public void shouldDenyOwnerFieldWithNumbersChars() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getGenerateNumberOwner(8);
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitWrongFormat();
    }

    @Test
    // Сценарий 2.8
    public void shouldDenyWithEmptyOwnerField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getEmptyOwner();
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitEmptyFieldOwner();
    }

    @Test
    // Сценарий 2.9
    public void shouldBeErrorWithEmptyField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getEmptyCardInfo();
        var month = DataHelper.getEmptyMonth();
        var year = DataHelper.getEmptyYear();
        var owner = DataHelper.getEmptyOwner();
        var cvc = DataHelper.getEmptyCVC();
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitEmptyField();
    }

    @Test
    // // Сценарий 2.10
    public void shouldDenyWithInvalidDateMonth() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateInvalidMonthDate(2);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitWrongCardDate();
    }

    @Test
    // Сценарий 2.11
    public void shouldDenyWithEmptyMonthField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getEmptyMonth();
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitEmptyField();
    }

    @Test
    // Сценарий 2.12
    public void shouldDenyWithInPreviousYearField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getGenerateInvalidYearDate(2);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitCardExpired();
    }

    @Test
    // Сценарий 2.13
    public void shouldDenyWithEmptyYearField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getEmptyYear();
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitEmptyField();
    }

    @Test
    // Сценарий 2.14
    public void shouldErrorWithTwoNumbersCVCField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cvc = DataHelper.getGenerateInvalidCvcCode("12");
        debitCardPage.onlyCVCField(cvc);
        debitCardPage.emptyCVCField();
    }


    @Test
    // Сценарий 2.15
    public void shouldDenyEmptyCVCField() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEmptyCVC();
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        debitCardPage.waitEmptyField();
    }


    @Test
    public void shouldAddPaymentIDInOrderEntry() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        var expected = SQLHelper.getDebitPaymentID();
        var actual = SQLHelper.getDebitOrderEntryId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDonTAddPaymentIDInOrderEntryStatusDeclined() {
        debitCardPage = mainPage.payWithDebitCard();
        var cardNumber = DataHelper.getSecondCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        debitCardPage.filledForm(cardNumber, month, year, owner, cvc);
        var expected = SQLHelper.getDebitPaymentID();
        var actual = SQLHelper.getDebitOrderEntryId();
        assertNotEquals(expected, actual);
    }
}