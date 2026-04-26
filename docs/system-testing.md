# Системное тестирование Web-интерфейса

## Назначение

Системные тесты проверяют работу приложения через Web-интерфейс: открытие JSP-страниц, отправку HTML-форм, обработку успешных и ошибочных результатов контроллерами Spring MVC.

Тесты реализованы с помощью Selenium WebDriver в классе:

`src/systemTest/java/java_prac/system/WebInterfaceSystemTest.java`

Запуск:

```bash
./gradlew systemTest
```

или через Ant:

```bash
ant system-test
```

Перед запуском системных тестов база данных должна быть создана и заполнена начальными данными из `db/create.sql` и `db/init.sql`. Полный цикл подготовки БД:

```bash
ant db-reset
```

## Проверяемые сценарии

| Вариант использования | Существенный результат | Проверка |
| --- | --- | --- |
| Открытие главной страницы | Пользователь видит ссылки на основные разделы | `homePageContainsAllMainUseCases` |
| Добавление компании | Компания успешно сохраняется, открывается карточка созданной компании | `companyCanBeCreatedSuccessfully` |
| Добавление компании без названия | Контроллер возвращает форму с сообщением валидации | `companyCreateShowsValidationErrorForBlankName` |
| Добавление компании с повторяющимся названием | Контроллер возвращает форму с сообщением об ошибке сохранения | `companyCreateShowsDuplicateNameError` |
| Добавление обучающегося и запись на курс | Обучающийся создается, затем успешно записывается на выбранный курс | `studentCanBeCreatedAndEnrolledToCourse` |
| Добавление преподавателя | Преподаватель успешно сохраняется, открывается карточка созданного преподавателя | `teacherCanBeCreatedSuccessfully` |
| Добавление курса | Курс успешно сохраняется, открывается карточка созданного курса | `courseCanBeCreatedSuccessfully` |
| Добавление курса с пустыми обязательными полями | Форма показывает все основные сообщения валидации | `courseCreateShowsValidationErrorsForMissingRequiredFields` |
| Добавление занятия | Занятие успешно создается и отображается в карточке курса | `lessonCanBeCreatedSuccessfully` |
| Добавление занятия с некорректным временем | Контроллер показывает ошибку: окончание раньше начала | `lessonCreateRejectsEndTimeBeforeStartTime` |
| Поиск расписания обучающегося | Открывается страница результата, отображаются занятия за период | `scheduleSearchReturnsStudentLessons` |
| Поиск расписания без выбора обучающегося | Контроллер возвращает форму с сообщением об ошибке | `scheduleSearchShowsErrorWhenStudentIsNotSelected` |
| Поиск расписания без выбора преподавателя | Контроллер возвращает форму с сообщением об ошибке | `scheduleSearchShowsErrorWhenTeacherIsNotSelected` |
| Поиск расписания с некорректным периодом | Контроллер возвращает форму с сообщением о неверном периоде | `scheduleSearchRejectsInvalidPeriod` |

## Требования к окружению

Для запуска Selenium-тестов нужен установленный Google Chrome. Драйвер создается через Selenium Manager/ChromeDriver. Если браузер или драйвер недоступен, тестовый класс помечает системные тесты как пропущенные.

Системные тесты используют реальный HTTP-сервер Spring Boot на случайном порту и реальную PostgreSQL-базу из конфигурации приложения.

## Результаты этапа

| Артефакт | Расположение |
| --- | --- |
| Код классов-контроллеров | `src/main/java/java_prac/controller` |
| Код JSP-страниц | `src/main/webapp/WEB-INF/jsp` |
| Конфигурационный файл Spring | `src/main/java/java_prac/config/WebMvcConfig.java`, `src/main/java/java_prac/config/DaoConfig.java` |
| Конфигурация основного сервлета приложения | `src/main/java/java_prac/JavaPracApplication.java` |
| Ant-сборка и развертывание | `build.xml`, цели `full-build`, `war`, `deploy`, `system-test` |
| Описание сценариев системного тестирования | `docs/system-testing.md` |
| Selenium-системные тесты | `src/systemTest/java/java_prac/system/WebInterfaceSystemTest.java` |
