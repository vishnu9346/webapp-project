package in.saidemy.webapp.powerbi;

/**
 * Configuration class for Power BI connection settings
 */
public class PowerBIConfig {
    
    private String tenantId;
    private String clientId;
    private String clientSecret;
    private String resourceUrl = "https://analysis.windows.net/powerbi/api";
    private String authorityUrl = "https://login.microsoftonline.com/";
    private String powerBIApiUrl = "https://api.powerbi.com/v1.0/myorg/";
    
    public PowerBIConfig() {
        // Default constructor
    }
    
    public PowerBIConfig(String tenantId, String clientId, String clientSecret) {
        this.tenantId = tenantId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
    
    // Getters and setters
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public String getResourceUrl() {
        return resourceUrl;
    }
    
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
    
    public String getAuthorityUrl() {
        return authorityUrl;
    }
    
    public void setAuthorityUrl(String authorityUrl) {
        this.authorityUrl = authorityUrl;
    }
    
    public String getPowerBIApiUrl() {
        return powerBIApiUrl;
    }
    
    public void setPowerBIApiUrl(String powerBIApiUrl) {
        this.powerBIApiUrl = powerBIApiUrl;
    }
    
    public String getTokenUrl() {
        return authorityUrl + tenantId + "/oauth2/token";
    }
}