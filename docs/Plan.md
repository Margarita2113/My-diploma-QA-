<h1 align="center">План внедрения автоматизации <a href="https://daniilshat.ru/" target="_blank"> комплексного сервиса, взаимодействующего с СУБД и API Банка. Для веб приложения "Путешествие дня"  
<img src="https://github.com/blackcater/blackcater/raw/main/images/Hi.gif" height="32"/></h1> 


## Перечень автоматизируемых сценариев: :page_with_curl:
      
### Предисловие:
Приложение — это веб-сервис, который предлагает купить тур по определённой цене двумя способами:
- Обычная оплата по дебетовой карте.
- Уникальная технология: выдача кредита по данным банковской карты.
Само приложение не обрабатывает данные по картам, а пересылает их банковским сервисам:
- сервису платежей, далее Payment Gate;
- кредитному сервису, далее Credit Gate.

### Предусло́вие
Для тестирования сценариев требуется:
1. Открыть [страницу](https://localhost:8080/) веб приложения;
2. Нажать на кнопку "Купить" или "Купить в кредит" в зависимости от выполняемого сценария

### Тестовые данные:
- Банковская карта с номером №4444 4444 4444 4441, статус карты “APPROVED” (карта 1);
- Банковская карта с номером №4444 4444 4444 4442, статус карты “DECLINED” (карта 2);

### Требования к содержимому полей при оформлении заказа:
- [X] ***Для поля "Номер карты":***
* записывается в формате XXXX XXXX XXXX XXXX, где X (кол-во 16) – это арабские цифры.

* - [X] ***Для поля "Месяц":***
* записывается цифры от 01 до 12, но не ранее текущего месяца, если в поле "Год" будет указан текущий год.

* - [X] ***Для поля "Год":***
* записывается последние две цифры года, но не ранее текущего года.

* - [X] ***Для поля "CVC/CVV":***
* записывается комбинация, состоящая из трех арабских цифр.

- [X] ***Для поля "Владелец":***
* записывается минимум 2 символа на латинице.

### Позитивные сценарии для опции “Купить": :chart_with_upwards_trend:
- [X] 1 ***Для поля карты № 4444 4444 4444 4441:***
1. В появившемся окне ввести валидные данные, а в поле "номер карты" указать "4444 4444 4444 4441", которая используется для симулятора;
2. Нажать "Продолжить"
  -> Появилось уведомление "Успешно. Операция одобрена банком".
  -> В БД присутсвует информация о совершенной покупке со статусом “APPROVED”
- [X] 2 ***Для поля карты № 4444 4444 4444 4442:***
1. В появившемся окне ввести валидные данные, а в поле "номер карты" указать "4444 4444 4444 4442", которая используется для симулятора;
2. Нажать "Продолжить"
   -> Появилось уведомление "“Ошибка! Банк отказал в проведении операции”;
   -> В БД присутсвует информация об отклонении операции покупки со статусом “DECLINED”.

### Негативные сценарии для опции “Купить”: :chart_with_downwards_trend:
- [X] 1 ***Для поля "Номер карты" c латинскими символами:***
1. В появившемся окне в поле "Номер карты" ввести латинские символы, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 2 ***Для поля "Номер карты" c кириллическими символами:***
1. В появившемся окне в поле "Номер карты" ввести кириллические символы, остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 3 ***Для поля "Номер карты" c заполнением цифр меньше валидного значения:***
1. В появившемся окне в поле "Номер карты" ввести 12 цифр, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 4 ***Для поля "Номер карты" c заполнением цифр больше валидного значения***
1. В появившемся окне в поле "Номер карты" ввести 18 цифр, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 5 ***Для поля "Номер карты" с пустым полем:***
1. В появившемся окне поле "Номер карты" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> система выдаст ошибку "Поле обязательно для заполнения"
- [X] 6 ***Для поля "Владелец" c использованием кириллических символами:***
1. В появившемся окне поле "Владелец" заполнить кириллическими символами, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 7 ***Для поля "Владелец" c использованием иероглифов:***
1. В появившемся окне в поле "Владелец" заполнить иероглифической системой, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 8 ***Для поля "Владелец" c использованием цифр:***
1. В появившемся окне в поле "Владелец" заполнить цифрами, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> выдаваст ошибку "Неверный формат"
- [X] 9 ***Для поля "Владелец" с пустым полем:***
1. В появившемся окне поле "Владелец" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> система выдаст ошибку "Поле обязательно для заполнения"
- [X] 10 ***Пустая форма***
1. В появившемся окне поля оставляем пустыми;
2. Нажать на кнопку "Продолжить"
    -> система выдаст ошибку "Поле обязательно для заполнения"
- [X] 11 ***Для поля "Месяц" с прошедшим месяцем текущего года:***
1. В появившемся окне в поле "Месяц" указать месяц предшествующему текущий, в поле "Год" ввести текущий год, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
    -> система выдаст ошибку "Неверно указан срок действия карты"
- [X] 12 ***Для поля "Месяц" с пустым полем:***
1. В появившемся окне поле "Месяц" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Неверный формат"
- [X] 13 ***Для поля "Год" с указанием прошедшего года:***
1. В появившемся окне в поле "Год" указать последние 2 цифры любого прошлого года, например 21, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Истёк срок действия карты"
- [X] 14 ***Для поля "Год" с пустым полем:***
1. В появившемся окне поле "Год" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Неверный формат"
- [X] 15 ***Для поля "CVC/CVV" с указанием меньшего количества цифр:***
1. В появившемся окне в поле "CVC/CVV" указать 2 любые цифры, например 21, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Истёк срок действия карты"
- [X] 16 ***Для поля "CVC/CVV" с пустым полем:***
1. В появившемся окне поле "CVC/CVV" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Неверный формат"
### Позитивные сценарии для опции “Купить в кредит": :chart_with_upwards_trend:
- [X] 1.2 ***Для поля карты № 4444 4444 4444 4441:***
1. В появившемся окне ввести валидные данные, а в поле "номер карты" указать "4444 4444 4444 4441", которая используется для симулятора;
2. Нажать "Продолжить"
  -> Появилось уведомление "Успешно. Операция одобрена банком".
  -> В БД присутсвует информация о совершенной покупке со статусом “APPROVED”
- [X] 2.2 ***Для поля карты № 4444 4444 4444 4442:***
1. В появившемся окне ввести валидные данные, а в поле "номер карты" указать "4444 4444 4444 4442", которая используется для симулятора;
2. Нажать "Продолжить"
   -> Появилось уведомление "“Ошибка! Банк отказал в проведении операции”;
   -> В БД присутсвует информация об отклонении операции покупки со статусом “DECLINED”.
### Негативные сценарии для опции “Купить в кредит”: :chart_with_downwards_trend:
- [X] 2.1 ***Для поля "Номер карты" c латинскими символами:***
1. В появившемся окне в поле "Номер карты" ввести латинские символы, а поле "Владелец" валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.2 ***Для поля "Номер карты" c кириллическими символами:***
1. В появившемся окне в поле "Номер карты" ввести кириллические символы, а поле "Владелец" Валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.3 ***Для поля "Номер карты" c заполнением цифр меньше валидного значения:***
1. В появившемся окне в поле "Номер карты" ввести 12 цифр, а поле "Владелец" Валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.4 ***Для поля "Номер карты" c заполнением цифр больше валидного значения***
1. В появившемся окне в поле "Номер карты" ввести 18 цифр, а поле "Владелец" Валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.5 ***Для поля "Номер карты" с пустым полем:***
1. В появившемся окне поле "Номер карты" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   ->  система выдаст ошибку "Поле обязательно для заполнения"
- [X] 2.6 ***Для поля "Владелец" c использованием кириллических символами:***
1. В появившемся окне в поле "Номер карты" ввести валидные значения. В поле "Владелец" заполнить кириллическими символами;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.7 ***Для поля "Владелец" c использованием иероглифов:***
1. В появившемся окне в поле "Номер карты" ввести валидные значения. В поле "Владелец" заполнить иероглифической системой;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.8 ***Для поля "Владелец" c использованием цифр:***
1. В появившемся окне в поле "Номер карты" ввести валидные значения. В поле "Владелец" заполнить цифрами;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.9 ***Для поля "Владелец" с пустым полем:***
1. В появившемся окне поле "Владелец" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   ->  система выдаст ошибку "Поле обязательно для заполнения"
- [X] 2.10 ***Пустая форма***
1. В появившемся окне поля оставляем пустыми;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.11 ***Для поля "Месяц" с прошедшим месяцем текущего года:***
1. В появившемся окне в поле "Месяц" указать месяц предшествующему текущий, в поле "Год" ввести текущий год, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Неверно указан срок действия карты"
- [X] 2.12 ***Для поля "Месяц" с пустым полем:***
1. В появившемся окне поле "Месяц" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.13 ***Для поля "Год" с указанием прошедшего года:***
1. В появившемся окне в поле "Год" указать последние 2 цифры любого прошлого года, например 21, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   ->  система выдаст ошибку "Истёк срок действия карты"
- [X] 2.14 ***Для поля "Год" с пустым полем:***
1. В появившемся окне поле "Год" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
- [X] 2.15 ***Для поля "CVC/CVV" с указанием меньшего количества цифр:***
1. В появившемся окне в поле "CVC/CVV" указать 2 любые цифры, например 21, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> система выдаст ошибку "Истёк срок действия карты"
- [X] 2.16 ***Для поля "CVC/CVV" с пустым полем:***
1. В появившемся окне поле "CVC/CVV" оставить пустым, а остальные поля заполнить валидными данными;
2. Нажать на кнопку "Продолжить"
   -> выдаваст ошибку "Неверный формат"
  
## Перечень используемых инструментов: :convenience_store:
     - IntelliJ IDEA, удобная среда подготовки авто-тестов;
     - Gradle, так как он более выиграшный, когда речь идет о зависимостях API и реализации, а также о возможности одновременного безопасного кэширования.
     - JDK 11 или выше, т.к. потребуются инструменты работающие с Java;
     - JUnit Jupiter, инструмент тестирования, более превычен нежели JUnit4 или TestNG;
     - Selenide, очень удобен при тестировании веб-интерфейса;
     - DBeaver, для просмотра базы данных;
     - GIT, распространненная система управления версиями;
     - Docker. Работа с Docker помогает разработчикам исключить повторяющиеся процессы настройки и упростить разработку приложений. Как следствие скорить процесс выхода приложения в пром. 
     - Allure, для создания отчетов о выполнении авто-тестов;

  
 ## Перечень необходимых разрешений/данных/доступов: :pencil:
- Разрешение на проведение тестирования и автоматизацию.
- Получение доступа к API и БД для проверки результатов выполнения тестов.
- Тех. документация, чтобы понимать какие данные будут являться валидными, а какие - нет.

  
## Перечень и описание возможных рисков при автоматизации: :dart:
- Авто-тесты не проверяют графическую составляющую, а именно едет ли верстка при тех или иных действиях, комфортна ли выбранная цветовая схема оформления и тд.
- Авто-тесты могут не запускаться ввиду ошибок в приложении

## Перечень необходимых специалистов для автоматизации: :point_up_2:
 - Будет достаточно одного автоматизатора тестирования, который умеет работать с указанными выше инструментами.
   
   
## Интервальная оценка с учётом рисков (в часах): :clock930:
- Ориентировочно потребуется 168 часов
  