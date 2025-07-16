package in.saidemy.webapp.powerbi;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for Power BI API operations
 */
public class PowerBIService {
    
    private static final Logger logger = LoggerFactory.getLogger(PowerBIService.class);
    private final PowerBIConfig config;
    private final ObjectMapper objectMapper;
    private String accessToken;
    
    public PowerBIService(PowerBIConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Authenticate with Power BI API and get access token
     */
    public String authenticate() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            HttpPost httpPost = new HttpPost(config.getTokenUrl());
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
            String requestBody = "grant_type=client_credentials" +
                    "&client_id=" + config.getClientId() +
                    "&client_secret=" + config.getClientSecret() +
                    "&resource=" + config.getResourceUrl();
            
            httpPost.setEntity(new StringEntity(requestBody));
            
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 200) {
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                accessToken = jsonResponse.get("access_token").asText();
                logger.info("Successfully authenticated with Power BI API");
                return accessToken;
            } else {
                logger.error("Authentication failed: " + responseBody);
                throw new RuntimeException("Authentication failed: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
    
    /**
     * Get list of workspaces
     */
    public String getWorkspaces() throws IOException {
        if (accessToken == null) {
            authenticate();
        }
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            HttpGet httpGet = new HttpGet(config.getPowerBIApiUrl() + "groups");
            httpGet.setHeader("Authorization", "Bearer " + accessToken);
            httpGet.setHeader("Content-Type", "application/json");
            
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Successfully retrieved workspaces");
                return responseBody;
            } else {
                logger.error("Failed to get workspaces: " + responseBody);
                throw new RuntimeException("Failed to get workspaces: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
    
    /**
     * Get datasets in a workspace
     */
    public String getDatasets(String workspaceId) throws IOException {
        if (accessToken == null) {
            authenticate();
        }
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            String url = config.getPowerBIApiUrl() + "groups/" + workspaceId + "/datasets";
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Bearer " + accessToken);
            httpGet.setHeader("Content-Type", "application/json");
            
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Successfully retrieved datasets for workspace: " + workspaceId);
                return responseBody;
            } else {
                logger.error("Failed to get datasets: " + responseBody);
                throw new RuntimeException("Failed to get datasets: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
    
    /**
     * Get reports in a workspace
     */
    public String getReports(String workspaceId) throws IOException {
        if (accessToken == null) {
            authenticate();
        }
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            String url = config.getPowerBIApiUrl() + "groups/" + workspaceId + "/reports";
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Bearer " + accessToken);
            httpGet.setHeader("Content-Type", "application/json");
            
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Successfully retrieved reports for workspace: " + workspaceId);
                return responseBody;
            } else {
                logger.error("Failed to get reports: " + responseBody);
                throw new RuntimeException("Failed to get reports: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
    
    /**
     * Refresh a dataset
     */
    public String refreshDataset(String workspaceId, String datasetId) throws IOException {
        if (accessToken == null) {
            authenticate();
        }
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            String url = config.getPowerBIApiUrl() + "groups/" + workspaceId + "/datasets/" + datasetId + "/refreshes";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Authorization", "Bearer " + accessToken);
            httpPost.setHeader("Content-Type", "application/json");
            
            // Empty body for refresh request
            httpPost.setEntity(new StringEntity("{}"));
            
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 202) {
                logger.info("Successfully triggered dataset refresh for: " + datasetId);
                return "Dataset refresh triggered successfully";
            } else {
                logger.error("Failed to refresh dataset: " + responseBody);
                throw new RuntimeException("Failed to refresh dataset: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
    
    /**
     * Get refresh history for a dataset
     */
    public String getRefreshHistory(String workspaceId, String datasetId) throws IOException {
        if (accessToken == null) {
            authenticate();
        }
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            String url = config.getPowerBIApiUrl() + "groups/" + workspaceId + "/datasets/" + datasetId + "/refreshes";
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Bearer " + accessToken);
            httpGet.setHeader("Content-Type", "application/json");
            
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 200) {
                logger.info("Successfully retrieved refresh history for dataset: " + datasetId);
                return responseBody;
            } else {
                logger.error("Failed to get refresh history: " + responseBody);
                throw new RuntimeException("Failed to get refresh history: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
    
    /**
     * Export report to PDF
     */
    public String exportReport(String workspaceId, String reportId, String format) throws IOException {
        if (accessToken == null) {
            authenticate();
        }
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        
        try {
            String url = config.getPowerBIApiUrl() + "groups/" + workspaceId + "/reports/" + reportId + "/ExportTo";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Authorization", "Bearer " + accessToken);
            httpPost.setHeader("Content-Type", "application/json");
            
            Map<String, String> exportRequest = new HashMap<>();
            exportRequest.put("format", format);
            
            String requestBody = objectMapper.writeValueAsString(exportRequest);
            httpPost.setEntity(new StringEntity(requestBody));
            
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (response.getStatusLine().getStatusCode() == 202) {
                logger.info("Successfully triggered report export for: " + reportId);
                return responseBody;
            } else {
                logger.error("Failed to export report: " + responseBody);
                throw new RuntimeException("Failed to export report: " + responseBody);
            }
        } finally {
            httpClient.close();
        }
    }
}