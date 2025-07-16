package in.saidemy.webapp.powerbi;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Data transformation processor for Power BI integration
 */
public class PowerBIDataProcessor implements Processor {
    
    private static final Logger logger = LoggerFactory.getLogger(PowerBIDataProcessor.class);
    private final ObjectMapper objectMapper;
    
    public PowerBIDataProcessor() {
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        String inputData = exchange.getIn().getBody(String.class);
        String operation = exchange.getIn().getHeader("dataOperation", String.class);
        
        logger.info("Processing data for Power BI operation: " + operation);
        
        try {
            String result = null;
            
            switch (operation != null ? operation.toLowerCase() : "transform") {
                case "transform":
                    result = transformXmlToJson(inputData);
                    break;
                    
                case "aggregate":
                    result = aggregateData(inputData);
                    break;
                    
                case "filter":
                    String filterCriteria = exchange.getIn().getHeader("filterCriteria", String.class);
                    result = filterData(inputData, filterCriteria);
                    break;
                    
                case "enrich":
                    result = enrichData(inputData);
                    break;
                    
                default:
                    result = transformXmlToJson(inputData);
                    break;
            }
            
            exchange.getIn().setBody(result);
            exchange.getIn().setHeader("Content-Type", "application/json");
            
        } catch (Exception e) {
            logger.error("Error processing data for Power BI", e);
            exchange.getIn().setBody("{\"error\": \"Data processing failed: " + e.getMessage() + "\"}");
            exchange.getIn().setHeader("Content-Type", "application/json");
        }
    }
    
    /**
     * Transform XML data to JSON format suitable for Power BI
     */
    private String transformXmlToJson(String xmlData) throws Exception {
        if (xmlData == null || xmlData.isEmpty()) {
            return "{}";
        }
        
        // Simple XML to JSON transformation for person data
        ObjectNode jsonObject = objectMapper.createObjectNode();
        
        if (xmlData.contains("<person")) {
            // Extract person data
            String user = extractValue(xmlData, "user=\"", "\"");
            String firstName = extractValue(xmlData, "<firstName>", "</firstName>");
            String lastName = extractValue(xmlData, "<lastName>", "</lastName>");
            String city = extractValue(xmlData, "<city>", "</city>");
            
            jsonObject.put("user", user);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("city", city);
            jsonObject.put("timestamp", System.currentTimeMillis());
            jsonObject.put("processedDate", new java.util.Date().toString());
        } else {
            // Generic data transformation
            jsonObject.put("rawData", xmlData);
            jsonObject.put("timestamp", System.currentTimeMillis());
            jsonObject.put("processedDate", new java.util.Date().toString());
        }
        
        return objectMapper.writeValueAsString(jsonObject);
    }
    
    /**
     * Aggregate data for Power BI reporting
     */
    private String aggregateData(String jsonData) throws Exception {
        ObjectNode result = objectMapper.createObjectNode();
        
        // Sample aggregation logic
        if (jsonData.contains("city")) {
            JsonNode inputNode = objectMapper.readTree(jsonData);
            String city = inputNode.get("city").asText();
            
            result.put("city", city);
            result.put("recordCount", 1);
            result.put("aggregatedAt", new java.util.Date().toString());
            
            // Add more aggregation logic as needed
            if ("London".equals(city)) {
                result.put("region", "UK");
                result.put("timezone", "GMT");
            } else {
                result.put("region", "Other");
                result.put("timezone", "Unknown");
            }
        }
        
        return objectMapper.writeValueAsString(result);
    }
    
    /**
     * Filter data based on criteria
     */
    private String filterData(String jsonData, String filterCriteria) throws Exception {
        JsonNode inputNode = objectMapper.readTree(jsonData);
        ObjectNode result = objectMapper.createObjectNode();
        
        boolean includeRecord = true;
        
        if (filterCriteria != null && !filterCriteria.isEmpty()) {
            if (filterCriteria.contains("city=London")) {
                String city = inputNode.get("city").asText();
                includeRecord = "London".equals(city);
            }
            // Add more filter logic as needed
        }
        
        if (includeRecord) {
            result.setAll((ObjectNode) inputNode);
            result.put("filtered", true);
            result.put("filterCriteria", filterCriteria);
        } else {
            result.put("filtered", false);
            result.put("reason", "Record does not meet filter criteria");
        }
        
        return objectMapper.writeValueAsString(result);
    }
    
    /**
     * Enrich data with additional information
     */
    private String enrichData(String jsonData) throws Exception {
        JsonNode inputNode = objectMapper.readTree(jsonData);
        ObjectNode result = objectMapper.createObjectNode();
        
        // Copy original data
        result.setAll((ObjectNode) inputNode);
        
        // Add enrichment data
        result.put("enrichedAt", new java.util.Date().toString());
        result.put("dataSource", "Camel-PowerBI-Integration");
        result.put("version", "1.0");
        
        // Add business logic enrichment
        if (inputNode.has("city")) {
            String city = inputNode.get("city").asText();
            switch (city) {
                case "London":
                    result.put("country", "UK");
                    result.put("continent", "Europe");
                    result.put("population", 8900000);
                    break;
                case "New York":
                    result.put("country", "USA");
                    result.put("continent", "North America");
                    result.put("population", 8400000);
                    break;
                default:
                    result.put("country", "Unknown");
                    result.put("continent", "Unknown");
                    result.put("population", 0);
                    break;
            }
        }
        
        return objectMapper.writeValueAsString(result);
    }
    
    /**
     * Helper method to extract values from XML
     */
    private String extractValue(String xml, String startTag, String endTag) {
        int start = xml.indexOf(startTag);
        if (start == -1) return "";
        
        start += startTag.length();
        int end = xml.indexOf(endTag, start);
        if (end == -1) return "";
        
        return xml.substring(start, end);
    }
}