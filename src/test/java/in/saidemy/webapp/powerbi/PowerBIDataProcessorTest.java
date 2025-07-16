package in.saidemy.webapp.powerbi;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Power BI Data Processor
 */
public class PowerBIDataProcessorTest {
    
    private PowerBIDataProcessor processor;
    private DefaultCamelContext context;
    
    @Before
    public void setUp() {
        processor = new PowerBIDataProcessor();
        context = new DefaultCamelContext();
    }
    
    @Test
    public void testTransformXmlToJson() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<person user=\"john\">\n" +
                "  <firstName>John</firstName>\n" +
                "  <lastName>Doe</lastName>\n" +
                "  <city>London</city>\n" +
                "</person>";
        
        exchange.getIn().setBody(xmlInput);
        exchange.getIn().setHeader("dataOperation", "transform");
        
        processor.process(exchange);
        
        String result = exchange.getIn().getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("\"user\":\"john\""));
        assertTrue(result.contains("\"firstName\":\"John\""));
        assertTrue(result.contains("\"lastName\":\"Doe\""));
        assertTrue(result.contains("\"city\":\"London\""));
    }
    
    @Test
    public void testAggregateData() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        String jsonInput = "{\"city\":\"London\",\"user\":\"john\"}";
        
        exchange.getIn().setBody(jsonInput);
        exchange.getIn().setHeader("dataOperation", "aggregate");
        
        processor.process(exchange);
        
        String result = exchange.getIn().getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("\"city\":\"London\""));
        assertTrue(result.contains("\"recordCount\":1"));
        assertTrue(result.contains("\"region\":\"UK\""));
    }
    
    @Test
    public void testFilterData() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        String jsonInput = "{\"city\":\"London\",\"user\":\"john\"}";
        
        exchange.getIn().setBody(jsonInput);
        exchange.getIn().setHeader("dataOperation", "filter");
        exchange.getIn().setHeader("filterCriteria", "city=London");
        
        processor.process(exchange);
        
        String result = exchange.getIn().getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("\"filtered\":true"));
        assertTrue(result.contains("\"city\":\"London\""));
    }
    
    @Test
    public void testEnrichData() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        String jsonInput = "{\"city\":\"London\",\"user\":\"john\"}";
        
        exchange.getIn().setBody(jsonInput);
        exchange.getIn().setHeader("dataOperation", "enrich");
        
        processor.process(exchange);
        
        String result = exchange.getIn().getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("\"city\":\"London\""));
        assertTrue(result.contains("\"country\":\"UK\""));
        assertTrue(result.contains("\"continent\":\"Europe\""));
        assertTrue(result.contains("\"dataSource\":\"Camel-PowerBI-Integration\""));
    }
    
    @Test
    public void testDefaultOperation() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<person user=\"jane\">\n" +
                "  <firstName>Jane</firstName>\n" +
                "  <lastName>Smith</lastName>\n" +
                "  <city>New York</city>\n" +
                "</person>";
        
        exchange.getIn().setBody(xmlInput);
        // No dataOperation header - should default to transform
        
        processor.process(exchange);
        
        String result = exchange.getIn().getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("\"user\":\"jane\""));
        assertTrue(result.contains("\"firstName\":\"Jane\""));
        assertTrue(result.contains("\"city\":\"New York\""));
    }
}