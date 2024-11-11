package org.zakariafarih.client;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import okhttp3.Cookie;

public class SeleniumCookieFetcher {
    private static final Logger logger = LoggerFactory.getLogger(SeleniumCookieFetcher.class);
    private WebDriver driver;

    public SeleniumCookieFetcher() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Enable headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Let Selenium Manager automatically handle ChromeDriver version
        this.driver = new ChromeDriver(options);
    }

    /**
     * Navigates to the specified URL and retrieves cookies.
     *
     * @param url The target URL to navigate to.
     * @return A list of OkHttp Cookie objects.
     */
    public List<Cookie> fetchCookies(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);

        try {
            Thread.sleep(5000); // Simple wait; consider using WebDriverWait for better handling
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted: {}", e.getMessage());
        }

        // Retrieve cookies from WebDriver
        Set<org.openqa.selenium.Cookie> seleniumCookies = driver.manage().getCookies();
        List<Cookie> okhttpCookies = new ArrayList<>();

        for (org.openqa.selenium.Cookie sc : seleniumCookies) {
            Cookie okc = new Cookie.Builder()
                    .name(sc.getName())
                    .value(sc.getValue())
                    .domain(sc.getDomain())
                    .path(sc.getPath())
                    .expiresAt(sc.getExpiry() != null ? sc.getExpiry().getTime() : Long.MAX_VALUE)
                    .secure()
                    .httpOnly()
                    .build();
            okhttpCookies.add(okc);
        }

        logger.info("Fetched {} cookies from Selenium", okhttpCookies.size());

        // Close the browser
        driver.quit();

        return okhttpCookies;
    }
}
