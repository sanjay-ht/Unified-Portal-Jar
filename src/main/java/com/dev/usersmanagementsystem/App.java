/*
Author: Ankit Kumar Sharma
 */
package com.dev.usersmanagementsystem;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.sql.Timestamp;

public class App {

    private WebDriver driver;

    private final String baseUrl = "http://13.126.48.191:8080/api/store-logs";

    public void setup() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opt = new EdgeOptions();
        opt.addArguments("--remote-allow-origins=*");
        driver = new EdgeDriver(opt);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    public void storeLog(Timestamp endTime, String errorLog, long responseTime, Timestamp startTime, String status, String title, String url, int userId) {
        try {
            String encodedEndTime = URLEncoder.encode(String.valueOf(endTime), StandardCharsets.UTF_8);
            String encodedStartTime = URLEncoder.encode(String.valueOf(startTime), StandardCharsets.UTF_8);
            String encodedErrorLog = URLEncoder.encode(errorLog, StandardCharsets.UTF_8);
            String encodedResponseTime = URLEncoder.encode(String.valueOf(responseTime), StandardCharsets.UTF_8);
            String encodedStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
            String encodedUserId = URLEncoder.encode(String.valueOf(userId), StandardCharsets.UTF_8);
            if (status.equals("Failed")) {
                encodedEndTime = "0";
                encodedStartTime = "0";
                encodedResponseTime = "0";
            }
            String fullUrl = baseUrl + "?endTime=" + encodedEndTime
                    + "&startTime=" + encodedStartTime
                    + "&errorLog=" + encodedErrorLog
                    + "&responseTime=" + encodedResponseTime
                    + "&status=" + encodedStatus
                    + "&title=" + encodedTitle
                    + "&url=" + encodedUrl
                    + "&userId=" + encodedUserId;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.print(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getNodeLength(JsonNode eventsNode) {
        int count = 0;
        if (eventsNode.isArray() && eventsNode != null) {
            for (JsonNode ignored : eventsNode) {
                count++;
            }
        }
        return count;
    }

    private void waitForElementVisibility(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5000));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
    }

    private void enterText(By Selector, String text) {
        WebElement element = driver.findElement(Selector);
        if (element != null) {
            element.clear();
            element.sendKeys(text);
        }
    }

    private void navigateAndWait(String url) {
        System.out.println("Url: " + url);
        if (url != null) {
            driver.get(url);
        }
        waitForElementVisibility(By.xpath("/html/body"));
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return js.executeScript("return document.readyState").equals("complete");
            } catch (Exception e) {
                throw e;
            }
        });
    }

    private void clickOnElement(By locator) {
        waitForElementVisibility(locator);
        WebElement button = driver.findElement(locator);
        button.click();

    }

    public void runCode(String jsonContent, int userId) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonContent);
        JsonNode stepsNode = rootNode.get("steps");
        System.out.println("Steps: " + stepsNode);
        int nodelen = getNodeLength(stepsNode);
        System.out.println("Length of Steps are " + nodelen);
        Timestamp zero=new Timestamp(0);
        try{
            for (JsonNode step : stepsNode) {
                System.out.println("Step: " + step);
                String type = step.get("type").asText();
                if (type.equals("navigate")) {
                    String url = step.get("url").asText();
                    JsonNode assertedEventsNode = step.get("assertedEvents");
                    String title = "";
                    if (assertedEventsNode != null && !assertedEventsNode.isEmpty()) {
                        title = assertedEventsNode.get(0).get("title").asText();
                        System.out.println("Title: " + title);
                    }
                    try {
                        System.out.println("Navigating to URL: " + url);
                        long startTime = System.currentTimeMillis();
                        Timestamp startTimeStamp = new Timestamp(startTime);
                        navigateAndWait(url);
                        long endTime = System.currentTimeMillis();
                        Timestamp endTimeStamp = new Timestamp(endTime);
                        storeLog(endTimeStamp, "", endTime - startTime, startTimeStamp, "Success", title, url, userId);
                    } catch (Exception e) {
                        storeLog(zero, e.toString(), 0, zero, "Failed", title, url, userId);
                        break;

                    }
                }
                if (type.equals("click")) {
                    JsonNode selectorsGroup = step.get("selectors");
                    JsonNode assertedEvents=step.get("assertedEvents");
                    String url="";
                    String title="";
                    if (assertedEvents != null && !assertedEvents.isEmpty()) {
                         url=assertedEvents.get(0).get("url").asText();
                         title=assertedEvents.get(0).get("title").asText();
                    }
                    for (JsonNode selectors : selectorsGroup) {
                        for (JsonNode selector : selectors) {
                            String SelectorText = selector.asText();
                            System.out.println("Selector: " + SelectorText);
                            try {
                                 if(SelectorText.startsWith("xpath")) {
                                    String xpath = SelectorText.replace("xpath/", "");
                                    System.out.println(xpath);
                                    long startTime = System.currentTimeMillis();
                                    Timestamp startTimeStamp = new Timestamp(startTime);

                                    waitForElementVisibility(By.xpath(xpath));

                                    long endTime = System.currentTimeMillis();
                                    Timestamp endTimeStamp = new Timestamp(endTime);
                                    clickOnElement(By.xpath(xpath));
                                    if(!title.equals("")){
                                        storeLog(endTimeStamp, "", endTime - startTime, startTimeStamp, "Success", title, url, userId);

                                    }
//                                    break;  // Stop after first successful click (you can remove this if you want to keep trying other selectors)
                                }
                            } catch (NoSuchElementException e) {
                                // Handle XPath failure and fallback to CSS selector
                                String regex = "component-[\\w-]+";  // Refined regex for better matching
                                Pattern pattern = Pattern.compile(regex);
                                Matcher matcher = pattern.matcher(SelectorText);
                                System.out.println("Xpath Didn't Work, Trying With Css Selector");

                                if (matcher.find()) {
                                    String csspath = matcher.group(); // Extracted ID part
                                    csspath = "#" + csspath;  // Format as CSS selector
                                    System.out.println("Extracted ID: " + csspath);
                                    long startTime = System.currentTimeMillis();
                                    Timestamp startTimeStamp = new Timestamp(startTime);


                                    waitForElementVisibility(By.cssSelector(csspath));
                                    long endTime = System.currentTimeMillis();
                                    Timestamp endTimeStamp = new Timestamp(endTime);

                                    clickOnElement(By.cssSelector(csspath));
                                    if(!title.equals("")){
                                        storeLog(endTimeStamp, "", endTime - startTime, startTimeStamp, "Success", title, url, userId);
                                    }

//                                    break;  // Stop after successful click (same as before, optional)
                                } else {
                                    if(!title.equals("")){
                                        storeLog(zero, "", 0, zero, "Failed", title, url, userId);
                                    }
                                    System.out.println("ID not found in fallback attempt.");
//                                    break;
                                    driver.quit();
                                    return;
                                }
                            }
                        }
                    }
                }


                if (type.equals("wait")) {
                    System.out.println("Start Time");
                    Thread.sleep(20000);
                    System.out.println("End Time");
                }
                if (type.equals("change")) {
                    JsonNode selectorsGroup = step.get("selectors");
                    String url="";
                    String title="";
                    JsonNode assertedEvents=step.get("assertedEvents");
                    if (assertedEvents != null && !assertedEvents.isEmpty()) {
                        url=assertedEvents.get(0).get("url").asText();
                        title=assertedEvents.get(0).get("title").asText();
                    }
                    for (JsonNode selectors : selectorsGroup) {
                        for (JsonNode selector : selectors) {
                            String SelectorText = selector.asText();
                            System.out.println("Selector: " + SelectorText);
                            try {
                                 if (SelectorText.startsWith("xpath")) {
                                    String xpath = SelectorText.replace("xpath/", "");
                                    System.out.println(xpath);
                                    String Text = step.get("value").asText();
                                    long startTime = System.currentTimeMillis();
                                    Timestamp startTimeStamp = new Timestamp(startTime);
                                    waitForElementVisibility(By.xpath(xpath));
                                    long endTime = System.currentTimeMillis();
                                    Timestamp endTimeStamp = new Timestamp(endTime);
                                    enterText(By.xpath(xpath), Text);
                                    if(!title.equals("")){
                                        storeLog(endTimeStamp, "", endTime - startTime, startTimeStamp, "Success", title, url, userId);
                                    }

//                                    break;
                                }
                            } catch (Exception e) {
                                if(!title.equals("")){
                                    storeLog(zero, "", 0, zero, "Failed", title, url, userId);
                                }
                                System.out.println(e.toString());


                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            Thread.sleep(50000);
            driver.quit();
        }


    }
}
