package org.zakariafarih.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final OkHttpClient client;
    private final Cache<String, String> cache;
    private final List<Cookie> initialCookies;

    public ApiClient(List<Cookie> initialCookies) {
        this.initialCookies = initialCookies;

        // Initialize OkHttpClient with provided cookies
        this.client = new OkHttpClient.Builder()
                .protocols(List.of(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final List<Cookie> cookies = new java.util.ArrayList<>(initialCookies);

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        this.cookies.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookies;
                    }
                })
                .build();

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();

        // Ensure productDetails directory exists
        try {
            Files.createDirectories(Paths.get("productDetails"));
            logger.info("Ensured 'productDetails' directory exists.");
        } catch (IOException e) {
            logger.error("Failed to create 'productDetails' directory: {}", e.getMessage());
        }
    }

    /**
     * Generates the Cookie header from the list of cookies.
     */
    private String generateCookieHeader() {
        return initialCookies.stream()
                .map(cookie -> cookie.name() + "=" + cookie.value())
                .collect(Collectors.joining("; "));
    }

    /**
     * Makes a GET request to the specified URL and returns the response body as a String.
     * Utilizes caching to store responses for 60 seconds.
     *
     * @param url The API endpoint URL.
     * @return The JSON response as a String.
     * @throws IOException If an I/O error occurs.
     */
    public String getProductListing(String url) throws IOException {
        String cachedResponse = cache.getIfPresent(url);
        if (cachedResponse != null) {
            logger.info("Fetching response from cache for URL: {}", url);
            return cachedResponse;
        }

        logger.info("Making GET request to URL: {}", url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:132.0) Gecko/20100101 Firefox/132.0")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "none")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", generateCookieHeader())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("Unexpected response code: {}", response.code());
                throw new IOException("Unexpected response code: " + response.code());
            }
            String responseBody = response.body().string();
            cache.put(url, responseBody);

            String filePath = "productListing.json";
            Files.write(Paths.get(filePath), responseBody.getBytes(), StandardOpenOption.CREATE);
            logger.info("Response Body saved to file: {}", filePath);

            logger.debug("Response Body cached: {}", responseBody);
            return responseBody;
        } catch (IOException e) {
            logger.error("Error during request: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Fetches detailed product information for a given product ID.
     *
     * @param productId The unique product ID.
     * @return The detailed product JSON as a String.
     * @throws IOException If an I/O error occurs.
     */
    public String getProductDetails(String productId) throws IOException {
        String url = "https://www.adidas.es/plp-app/api/product/" + productId;
        String cachedResponse = cache.getIfPresent(url);
        if (cachedResponse != null) {
            logger.info("Fetching product details from cache for ID: {}", productId);
            return cachedResponse;
        }

        logger.info("Making GET request for product details ID: {}", productId);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:132.0) Gecko/20100101 Firefox/132.0")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en-US,en;q=0.5")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "none")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cookie", generateCookieHeader())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("Unexpected response code for product ID {}: {}", productId, response.code());
                throw new IOException("Unexpected response code: " + response.code());
            }
            String responseBody = response.body().string();
            cache.put(url, responseBody);

            String filePath = "productDetails/" + productId + ".json";
            Files.write(Paths.get(filePath), responseBody.getBytes(), StandardOpenOption.CREATE);
            logger.info("Product details saved to file: {}", filePath);

            logger.debug("Product details cached for ID: {}", productId);
            return responseBody;
        } catch (IOException e) {
            logger.error("Error fetching product details for ID {}: {}", productId, e.getMessage());
            throw e;
        }
    }
}
