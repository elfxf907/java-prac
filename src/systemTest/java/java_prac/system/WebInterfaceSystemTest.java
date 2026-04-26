package java_prac.system;

import java.time.Duration;

import java_prac.JavaPracApplication;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest(
        classes = JavaPracApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WebInterfaceSystemTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass(alwaysRun = true)
    public void setUpBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1280,900");

        try {
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        } catch (RuntimeException ex) {
            throw new SkipException("Chrome/ChromeDriver is not available for Selenium system tests", ex);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(groups = "system")
    public void homePageContainsAllMainUseCases() {
        open("/");

        assertPageContains("Система учебного центра");
        assertPageContains("Обучающиеся");
        assertPageContains("Преподаватели");
        assertPageContains("Компании");
        assertPageContains("Курсы");
        assertPageContains("Поиск расписания");
    }

    @Test(groups = "system")
    public void companyCanBeCreatedSuccessfully() {
        String name = "Selenium Company " + System.nanoTime();

        open("/companies/new");
        byId("name").sendKeys(name);
        byId("address").sendKeys("Selenium address");
        submitForm();

        assertPageContains("Карточка компании");
        assertPageContains(name);
        assertPageContains("Selenium address");
    }

    @Test(groups = "system")
    public void companyCreateShowsValidationErrorForBlankName() {
        open("/companies/new");
        submitForm();

        assertPageContains("Название компании не должно быть пустым");
    }

    @Test(groups = "system")
    public void companyCreateShowsDuplicateNameError() {
        String name = "Selenium Duplicate Company " + System.nanoTime();

        open("/companies/new");
        byId("name").sendKeys(name);
        submitForm();
        assertPageContains("Карточка компании");

        open("/companies/new");
        byId("name").sendKeys(name);
        submitForm();

        assertPageContains("Не удалось сохранить компанию");
    }

    @Test(groups = "system")
    public void studentCanBeCreatedAndEnrolledToCourse() {
        String fullName = "Selenium Student " + System.nanoTime();

        open("/students/new");
        byId("fullName").sendKeys(fullName);
        submitForm();

        assertPageContains("Карточка обучающегося");
        assertPageContains(fullName);

        Select courseSelect = new Select(byId("courseId"));
        courseSelect.selectByIndex(1);
        String selectedCourseTitle = courseSelect.getFirstSelectedOption().getText();
        submitForm();

        assertPageContains("Курсы обучающегося");
        assertPageContains(selectedCourseTitle);
    }

    @Test(groups = "system")
    public void teacherCanBeCreatedSuccessfully() {
        String fullName = "Selenium Teacher " + System.nanoTime();

        open("/teachers/new");
        byId("fullName").sendKeys(fullName);
        new Select(byId("companyId")).selectByIndex(1);
        submitForm();

        assertPageContains("Карточка преподавателя");
        assertPageContains(fullName);
        assertPageContains("Компания");
    }

    @Test(groups = "system")
    public void courseCanBeCreatedSuccessfully() {
        String title = "Selenium Course " + System.nanoTime();

        open("/courses/new");
        byId("title").sendKeys(title);
        byId("description").sendKeys("Selenium course description");
        new Select(byId("companyId")).selectByIndex(1);
        new Select(byId("durationType")).selectByValue("DAY");
        byId("hoursPerDay").sendKeys("4");
        submitForm();

        assertPageContains("Карточка курса");
        assertPageContains(title);
        assertPageContains("Selenium course description");
    }

    @Test(groups = "system")
    public void courseCreateShowsValidationErrorsForMissingRequiredFields() {
        open("/courses/new");
        submitForm();

        assertPageContains("Название курса не должно быть пустым");
        assertPageContains("Нужно выбрать компанию");
        assertPageContains("Нужно указать тип длительности");
        assertPageContains("Нужно указать часов в день");
    }

    @Test(groups = "system")
    public void lessonCanBeCreatedSuccessfully() {
        open("/courses/1/lessons/new");
        new Select(byId("teacherId")).selectByIndex(1);
        setDateTimeValue("startTime", "2026-07-01T10:00");
        setDateTimeValue("endTime", "2026-07-01T12:00");
        submitForm();

        assertPageContains("Карточка курса");
        assertPageContains("2026-07-01T10:00");
        assertPageContains("2026-07-01T12:00");
    }

    @Test(groups = "system")
    public void lessonCreateRejectsEndTimeBeforeStartTime() {
        open("/courses/1/lessons/new");
        new Select(byId("teacherId")).selectByIndex(1);
        setDateTimeValue("startTime", "2026-06-01T12:00");
        setDateTimeValue("endTime", "2026-06-01T10:00");
        submitForm();

        assertPageContains("Время окончания должно быть позже времени начала");
    }

    @Test(groups = "system")
    public void scheduleSearchReturnsStudentLessons() {
        open("/schedule");
        new Select(byId("searchType")).selectByValue("student");
        new Select(byId("studentId")).selectByIndex(1);
        setDateTimeValue("from", "2026-03-01T00:00");
        setDateTimeValue("to", "2026-03-10T23:59");
        submitForm();

        assertPageContains("Результат поиска расписания");
        assertPageContains("Найденные занятия");
        assertPageContains("Основы Java");
    }

    @Test(groups = "system")
    public void scheduleSearchShowsErrorWhenStudentIsNotSelected() {
        open("/schedule");
        new Select(byId("searchType")).selectByValue("student");
        setDateTimeValue("from", "2026-03-01T00:00");
        setDateTimeValue("to", "2026-03-10T23:59");
        submitForm();

        assertPageContains("Нужно выбрать обучающегося");
    }

    @Test(groups = "system")
    public void scheduleSearchShowsErrorWhenTeacherIsNotSelected() {
        open("/schedule");
        new Select(byId("searchType")).selectByValue("teacher");
        setDateTimeValue("from", "2026-03-01T00:00");
        setDateTimeValue("to", "2026-03-10T23:59");
        submitForm();

        assertPageContains("Нужно выбрать преподавателя");
    }

    @Test(groups = "system")
    public void scheduleSearchRejectsInvalidPeriod() {
        open("/schedule");
        new Select(byId("searchType")).selectByValue("teacher");
        new Select(byId("teacherId")).selectByIndex(1);
        setDateTimeValue("from", "2026-03-10T23:59");
        setDateTimeValue("to", "2026-03-01T00:00");
        submitForm();

        assertPageContains("Конец периода не может быть раньше начала");
    }

    private void open(String path) {
        driver.get("http://localhost:" + port + path);
    }

    private WebElement byId(String id) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }

    private void submitForm() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private void setDateTimeValue(String elementId, String value) {
        WebElement element = byId(elementId);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element,
                value
        );
    }

    private void assertPageContains(String text) {
        wait.until(webDriver -> webDriver.getPageSource().contains(text));
        Assert.assertTrue(driver.getPageSource().contains(text), "Page should contain: " + text);
    }
}
