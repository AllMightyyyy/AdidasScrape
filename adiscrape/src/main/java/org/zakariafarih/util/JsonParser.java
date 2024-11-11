package org.zakariafarih.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);
    private final ObjectMapper objectMapper;

    public JsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Parses a JSON string into a JsonNode.
     *
     * @param json The JSON string.
     * @return The root JsonNode.
     * @throws IOException If parsing fails.
     */
    public JsonNode parseJson(String json) throws IOException {
        logger.debug("Parsing JSON");
        return objectMapper.readTree(json);
    }
}
