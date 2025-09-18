package tr.com.turksat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FeatureWriter {
    public static void main(String[] args) {
        StringBuilder feature = new StringBuilder();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        String url = "https://www.turkiye.gov.tr/kadikoy-belediyesi-arsa-rayic-degeri-sorgulama-v2";
        driver.get(url);
        String serviceName = driver.findElement(By.xpath("//*[@id=\"pageContentBlock\"]/section[1]/div[1]/h2/em")).getText();
        feature.append("Feature: ").append(serviceName).append("\n");
        WebElement content = driver.findElement(By.id("contentStart"));
        String serviceDesc = content.findElement(By.className("richText")).getText();
        WebElement form = content.findElement(By.id("mainForm"));
        ArrayList<String> inputNames = new ArrayList<>();
        for(WebElement input : form.findElements(By.xpath(".//select | .//input[@type= 'text']"))) {
            inputNames.add(input.findElement(By.xpath("./parent::*")).findElement(By.xpath("./*")).getText());
        }
        inputNames.removeIf(s -> s.trim().isEmpty());
        int value = 0;
        if(inputNames.isEmpty()){
            feature.append("\tScenario: ").append(serviceDesc).append("\n");
            feature.append("\t\tGiven user is on the page\n");
            feature.append("\t\tWhen user presses button");
        }
        else{
            feature.append("\tScenario Outline: ").append(serviceDesc).append("\n");
            feature.append("\t\tGiven user is on the page\n");
            feature.append("\t\tWhen user enters \"<value1>\" to ").append(inputNames.getFirst()).append("\n");
            value++;
            for(String name : inputNames.subList(1, inputNames.size())){
                value++;
                feature.append("\t\tAnd user enters \"<value").append(value).append(">\" to ").append(name).append("\n");
            }
            feature.append("\t\tAnd user presses button\n");
        }
        feature.append("\t\tThen user should be directed to next page\n");
        if(value > 0){
            feature.append("\t\tExamples:\n\t\t\t| ");
            for (int i = 1 ; i <= value ; i++){
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
}