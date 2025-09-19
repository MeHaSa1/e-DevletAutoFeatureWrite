package tr.com.turksat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeatureWriter {
    private static int valueNumber = 1;
    public static void main(String[] args) {
        StringBuilder feature = new StringBuilder();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        boolean login = false;
        if(login){
            driver.get("https://giris.turkiye.gov.tr/Giris/gir");
            driver.findElement(By.id("tridField")).sendKeys("tc");
            driver.findElement(By.id("egpField")).sendKeys("şifre");
            driver.findElement(By.name("submitButton")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchField")));
        }
        String url = "https://www.turkiye.gov.tr/onikisubat-belediyesi-askidaki-imar-plani-sorgulama";
        driver.get(url);
        String serviceName = driver.findElement(By.xpath("//*[@id=\"pageContentBlock\"]/section[1]/div[1]/h2/em")).getText();
        feature.append("Feature: ").append(serviceName).append("\n");
        WebElement content = driver.findElement(By.id("contentStart"));
        String serviceDesc = content.findElement(By.className("richText")).getText();
        ArrayList<Input> inputs = new ArrayList<>();
        List<String> buttonTypes = Arrays.asList("button","submit","checkbox");
        List<String> textTypes = Arrays.asList("text","password");
        int valueCount = 0;
        for(WebElement input : content.findElements(By.xpath(".//select[not(ancestor::table) and not(ancestor::nav)] | .//input[not(ancestor::table) and not(ancestor::nav)] | .//a[not(ancestor::table) and not(ancestor::nav)]"))) {
            if(!input.isDisplayed()) continue;
            if(input.getTagName().equals("select")){
                inputs.add(new Input("select",getLabel(input)));
                valueCount++;
                continue;
            }
            if(input.getTagName().equals("a")){
                inputs.add(new Input("link",input.getText()));
                continue;
            }
            if(input.getTagName().equals("input")){
                String type = input.getAttribute("type");
                if(buttonTypes.contains(type)){
                    inputs.add(new Input("button",input.getAttribute("value")));
                    continue;
                }
                if(textTypes.contains(type)){
                    inputs.add(new Input("text",getLabel(input)));
                    valueCount++;
                }
            }
        }
        if(valueCount == 0){
            feature.append("\tScenario: ").append(serviceDesc).append("\n");
        }
        else {
            feature.append("\tScenario Outline: ").append(serviceDesc).append("\n");
        }
        feature.append("\t\tGiven user is on the page \"").append(url).append("\"\n");
        for(WebElement element : driver.findElements(By.tagName("table"))){
            String name = element.findElement(By.tagName("caption")).getText();
            StringBuilder columns = new StringBuilder();
            for(WebElement column : element.findElements(By.xpath("./thead/tr/th"))){
                columns.append(column.getText()).append("/");
            }
            feature.append("\t\tThen user should see table ").append(name).append(" with columns:\"").append(columns).append("\"\n");
        }
        if(!inputs.isEmpty()){
            feature.append("\t\tWhen ").append(toStepText(inputs.getFirst())).append("\n");
        }
        for (Input input : inputs.subList(1, inputs.size())) {
            feature.append("\t\tAnd ").append(toStepText(input)).append("\n");
        }
        feature.append("\t\tThen user should be directed to next page\n");
        if(valueCount > 0){
            feature.append("\t\tExamples:\n\t\t\t| ");
            for (int i = 1 ; i <= valueCount ; i++){
                feature.append("value").append(i).append(" | ");
            }
            feature.append("\n");
        }
        try(FileWriter writer = new FileWriter("test.feature")) {
            writer.write(feature.toString());
        } catch (IOException e) {
            System.out.println("File error");
        }
        driver.quit();
    }

    private static String getLabel(WebElement element){
        return element.findElement(By.xpath("./parent::*")).findElement(By.xpath("./*")).getText();
    }

    private static String toStepText(Input input){
        String stepText;
        if(input.type.equals("text") || input.type.equals("select")){
            stepText = "user enters \"<value" + valueNumber + ">\" to " + input.name + " " + input.type;
            valueNumber++;
            return stepText;
        }
        stepText = "user clicks the " + input.name + " " + input.type;
        return stepText;
    }
}

class Input{
    String type;
    String name;
    Input(String type, String name){
        this.type = type;
        this.name = name;
    }
}