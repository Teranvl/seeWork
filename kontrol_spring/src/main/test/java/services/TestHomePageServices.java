package services;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import java.util.concurrent.TimeUnit;


public class TestHomePageServices {


    private int port = 8080; //порт
    private String prefix = "/service"; //путь к представлению
    private static HtmlUnitDriver driver; //средсто для просмотра html страницы по тегам

    @BeforeClass //запускаем один раз при запуске тестирования
    public static void setup() {
        driver = new HtmlUnitDriver(); //создаем объект
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //время ожидания
    }

    @AfterClass //запускаем полсе работы всех тестовых методов
    public static void teardown() {
        driver.quit(); //зыкрываем
    }

    @Test//тестовый сценарий
    public void testHomePage() {
        String homePage = "http://localhost:" + port; //путь к серверу
        driver.get(homePage + prefix); //добавляем префикс

        String titleHomePage = driver.getTitle(); //получаем название страницы
        Assert.assertEquals("Home page for Service", titleHomePage); //сравниваем


        String firstHeaderHomePage = driver.findElementByTagName("h2").getText(); //получаем в теле страницы заголовок h2
        Assert.assertEquals("Hello from Service", firstHeaderHomePage);//сравниваем


        String pathImageFromHomePage = driver.findElementByTagName("img").getAttribute("src"); //получаем путь до image
        Assert.assertEquals(homePage + "/images/carService.png", pathImageFromHomePage);//сравниваем
    }







}
