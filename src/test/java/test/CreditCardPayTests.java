package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.CreditCardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreditCardPayTests {
    private MainPage mainPage;
    private CreditCardPage creditCardPage;

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
    // Позитивные сценарии для опции “Купить" с одобренной картой
    public void shouldPurchaseWithApprovedCard() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitSuccessfulNotification();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    // Позитивные сценарии для опции “Купить" с отклоненной картой
    public void shouldDenyWithRejectedCard() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getSecondCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitErrorNotification();
        var expected = DataHelper.getSecondCardStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    // 1 тестовый сценарий
    public void shouldBeErrorForCardFieldWithLatinChars() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("EN");
        creditCardPage.onlyCardField(cardNumber);
        creditCardPage.emptyCardField();
    }

    @Test
    // 2 тестовый сценарий
    public void shouldBeErrorForCardFieldWithCyrillicChars() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("RU");
        creditCardPage.onlyCardField(cardNumber);
        creditCardPage.emptyCardField();
    }

    @Test
    // 3 тестовый сценарий
    public void shouldDenyWithTwoDigitsCardField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("12");
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.onlyCardField(cardNumber);
        creditCardPage.waitWrongFormat();
    }


    @Test
    // 4 тестовый сценарий
    public void shouldDenyWithEmptyCardField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getEmptyCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitEmptyField();
    }

    @Test
    // 5 тестовый сценарий
    public void shouldDenyOwnerFieldWithCyrillicChars() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("RU");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitWrongFormat();
    }

    @Test
    // 6 тестовый сценарий
    public void shouldDenyOwnerFieldWithHieroglyphsChars() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("ja");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitWrongFormat();
    }

    @Test
    // 7 тестовый сценарий
    public void shouldDenyOwnerFieldWithNumbersChars() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getGenerateNumberOwner(8);
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitWrongFormat();
    }

    @Test
    // 8 тестовый сценарий
    public void shouldDenyWithEmptyOwnerField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getEmptyOwner();
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitEmptyFieldOwner();
    }

    // 9 тестовый сценарий
    @Test
    public void shouldBeErrorWithEmptyField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getEmptyCardInfo();
        var month = DataHelper.getEmptyMonth();
        var year = DataHelper.getEmptyYear();
        var owner = DataHelper.getEmptyOwner();
        var cvc = DataHelper.getEmptyCVC();
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitEmptyField();
    }

    // 10 тестовый сценарий
    @Test
    public void shouldDenyWithInvalidDateMonth() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateInvalidMonthDate(4);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitWrongCardDate();
    }

    @Test
    // 11 тестовый сценарий
    public void shouldDenyWithEmptyMonthField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getEmptyMonth();
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitEmptyField();
    }

    // 12 тестовый сценарий
    @Test
    public void shouldErrorWithPreviousYearField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getGenerateInvalidYearDate(2);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitCardExpired();
    }

    @Test
    // 13 тестовый сценарий
    public void shouldDenyWithEmptyYearField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getEmptyYear();
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitEmptyField();
    }

    // 14 тестовый сценарий
    @Test
    public void shouldErrorWithTwoNumbersCVCField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cvc = DataHelper.getGenerateInvalidCvcCode("12");
        creditCardPage.onlyCVCField(cvc);
        creditCardPage.emptyCVCField();
    }

    @Test
    // 15 тестовый сценарий
    public void shouldDenyEmptyCVCField() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEmptyCVC();
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        creditCardPage.waitEmptyField();
    }

    @Test
    public void shouldAddCreditInOrderEntry() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        var expected = SQLHelper.getCreditRequestReEntryId();
        var actual = SQLHelper.getCreditOrderEntryId();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDonTAddCreditInOrderEntryStatusDeclined() {
        creditCardPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getSecondCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        creditCardPage.filledForm(cardNumber, month, year, owner, cvc);
        var expected = SQLHelper.getCreditRequestReEntryId();
        var actual = SQLHelper.getCreditOrderEntryId();
        assertNotEquals(expected, actual);
    }
}