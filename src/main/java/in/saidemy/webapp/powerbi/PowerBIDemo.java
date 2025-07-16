package in.saidemy.webapp.powerbi;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;

/**
 * Demo class to show Power BI integration functionality
 */
public class PowerBIDemo {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Power BI Integration Demo ===");
            
            // Create Camel context
            CamelContext context = new DefaultCamelContext();
            
            // Test data processor
            testDataProcessor(context);
            
            // Test configuration
            testConfiguration();
            
            System.out.println("\n=== Demo completed successfully ===");
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testDataProcessor(CamelContext context) throws Exception {
        System.out.println("\n1. Testing Power BI Data Processor:");
        
        PowerBIDataProcessor processor = new PowerBIDataProcessor();
        
        // Test XML to JSON transformation
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<person user=\"demo\">\n" +
                "  <firstName>Demo</firstName>\n" +
                "  <lastName>User</lastName>\n" +
                "  <city>London</city>\n" +
                "</person>";
        
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody(xmlData);
        exchange.getIn().setHeader("dataOperation", "transform");
        
        processor.process(exchange);
        
        System.out.println("   Input XML: " + xmlData.replaceAll("\\n", "").replaceAll("\\s+", " "));
        System.out.println("   Output JSON: " + exchange.getIn().getBody(String.class));
        
        // Test aggregation
        String jsonData = "{\"city\":\"London\",\"user\":\"demo\"}";
        exchange.getIn().setBody(jsonData);
        exchange.getIn().setHeader("dataOperation", "aggregate");
        
        processor.process(exchange);
        
        System.out.println("   Aggregated Data: " + exchange.getIn().getBody(String.class));
        
        // Test enrichment
        exchange.getIn().setBody(jsonData);
        exchange.getIn().setHeader("dataOperation", "enrich");
        
        processor.process(exchange);
        
        System.out.println("   Enriched Data: " + exchange.getIn().getBody(String.class));
    }
    
    private static void testConfiguration() {
        System.out.println("\n2. Testing Power BI Configuration:");
        
        PowerBIConfig config = new PowerBIConfig();
        config.setTenantId("sample-tenant-id");
        config.setClientId("sample-client-id");
        config.setClientSecret("sample-client-secret");
        
        System.out.println("   Tenant ID: " + config.getTenantId());
        System.out.println("   Client ID: " + config.getClientId());
        System.out.println("   Power BI API URL: " + config.getPowerBIApiUrl());
        System.out.println("   Token URL: " + config.getTokenUrl());
        
        // Test service initialization
        PowerBIService service = new PowerBIService(config);
        System.out.println("   Power BI Service initialized successfully");
        
        // Test processor initialization
        PowerBIProcessor processor = new PowerBIProcessor(service);
        System.out.println("   Power BI Processor initialized successfully");
    }
}