package in.saidemy.webapp.powerbi;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Camel processor for handling Power BI API requests
 */
public class PowerBIProcessor implements Processor {
    
    private static final Logger logger = LoggerFactory.getLogger(PowerBIProcessor.class);
    private final PowerBIService powerBIService;
    
    public PowerBIProcessor(PowerBIService powerBIService) {
        this.powerBIService = powerBIService;
    }
    
    @Override
    public void process(Exchange exchange) throws Exception {
        String operation = exchange.getIn().getHeader("operation", String.class);
        String workspaceId = exchange.getIn().getHeader("workspaceId", String.class);
        String datasetId = exchange.getIn().getHeader("datasetId", String.class);
        String reportId = exchange.getIn().getHeader("reportId", String.class);
        String format = exchange.getIn().getHeader("format", String.class);
        
        logger.info("Processing Power BI operation: " + operation);
        
        try {
            String result = null;
            
            switch (operation.toLowerCase()) {
                case "authenticate":
                    result = powerBIService.authenticate();
                    break;
                    
                case "workspaces":
                    result = powerBIService.getWorkspaces();
                    break;
                    
                case "datasets":
                    if (workspaceId == null) {
                        throw new IllegalArgumentException("workspaceId is required for datasets operation");
                    }
                    result = powerBIService.getDatasets(workspaceId);
                    break;
                    
                case "reports":
                    if (workspaceId == null) {
                        throw new IllegalArgumentException("workspaceId is required for reports operation");
                    }
                    result = powerBIService.getReports(workspaceId);
                    break;
                    
                case "refresh":
                    if (workspaceId == null || datasetId == null) {
                        throw new IllegalArgumentException("workspaceId and datasetId are required for refresh operation");
                    }
                    result = powerBIService.refreshDataset(workspaceId, datasetId);
                    break;
                    
                case "refreshhistory":
                    if (workspaceId == null || datasetId == null) {
                        throw new IllegalArgumentException("workspaceId and datasetId are required for refresh history operation");
                    }
                    result = powerBIService.getRefreshHistory(workspaceId, datasetId);
                    break;
                    
                case "export":
                    if (workspaceId == null || reportId == null) {
                        throw new IllegalArgumentException("workspaceId and reportId are required for export operation");
                    }
                    if (format == null) {
                        format = "PDF"; // default format
                    }
                    result = powerBIService.exportReport(workspaceId, reportId, format);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Unknown operation: " + operation);
            }
            
            exchange.getIn().setBody(result);
            exchange.getIn().setHeader("Content-Type", "application/json");
            
        } catch (Exception e) {
            logger.error("Error processing Power BI operation: " + operation, e);
            exchange.getIn().setBody("{\"error\": \"" + e.getMessage() + "\"}");
            exchange.getIn().setHeader("Content-Type", "application/json");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 500);
        }
    }
}