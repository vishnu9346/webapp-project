# Power BI Automation Integration

This document describes the Power BI automation features integrated into the webapp-project.

## Overview

The webapp-project now includes comprehensive Power BI automation capabilities built on top of the existing Apache Camel integration framework. This allows for seamless data processing, transformation, and integration with Power BI services.

## Features

### 1. Power BI API Integration
- **Authentication**: Secure OAuth 2.0 authentication with Azure AD
- **Workspace Management**: List and manage Power BI workspaces
- **Dataset Operations**: Retrieve and refresh datasets
- **Report Management**: Access and export Power BI reports
- **Automated Refresh**: Schedule and monitor dataset refreshes

### 2. Data Processing Pipeline
- **XML to JSON Transformation**: Convert XML data to JSON format for Power BI
- **Data Aggregation**: Aggregate data for reporting and analytics
- **Data Filtering**: Apply filters based on business rules
- **Data Enrichment**: Add additional context and metadata

### 3. REST API Endpoints

#### Power BI Management Endpoints
- `GET /powerbi/workspaces` - List all Power BI workspaces
- `GET /powerbi/workspaces/{workspaceId}/datasets` - Get datasets in a workspace
- `GET /powerbi/workspaces/{workspaceId}/reports` - Get reports in a workspace
- `POST /powerbi/workspaces/{workspaceId}/datasets/{datasetId}/refresh` - Refresh a dataset
- `GET /powerbi/workspaces/{workspaceId}/datasets/{datasetId}/refreshes` - Get refresh history
- `POST /powerbi/workspaces/{workspaceId}/reports/{reportId}/export` - Export a report
- `POST /powerbi/authenticate` - Authenticate with Power BI API

#### Data Processing Endpoints
- `POST /powerbi/transform` - Transform data for Power BI
- `POST /powerbi/aggregate` - Aggregate data for reporting
- `POST /powerbi/filter` - Filter data based on criteria

### 4. Automated Workflows
- **Scheduled Data Processing**: Automatically process incoming data every 5 minutes
- **Real-time Data Transformation**: Transform data as it flows through the system
- **Automated File Processing**: Process files and prepare them for Power BI consumption

## Configuration

### Power BI Application Setup

1. **Register Azure AD Application**:
   - Go to Azure Portal > Azure Active Directory > App registrations
   - Create a new application registration
   - Note down the Application (client) ID and Directory (tenant) ID
   - Create a client secret

2. **Configure Power BI Permissions**:
   - Grant the following API permissions:
     - Power BI Service API permissions
     - Microsoft Graph API permissions (if needed)

3. **Update Configuration**:
   Edit `src/main/resources/powerbi.properties`:
   ```properties
   powerbi.tenant.id=your-tenant-id
   powerbi.client.id=your-client-id
   powerbi.client.secret=your-client-secret
   ```

### Environment Configuration

The application uses the following environment variables or properties:
- `powerbi.tenant.id`: Your Azure AD tenant ID
- `powerbi.client.id`: Your registered application client ID
- `powerbi.client.secret`: Your application client secret

## Usage Examples

### 1. Authentication
```bash
curl -X POST http://localhost:8080/powerbi/authenticate
```

### 2. List Workspaces
```bash
curl -X GET http://localhost:8080/powerbi/workspaces
```

### 3. Get Datasets
```bash
curl -X GET http://localhost:8080/powerbi/workspaces/{workspace-id}/datasets
```

### 4. Refresh Dataset
```bash
curl -X POST http://localhost:8080/powerbi/workspaces/{workspace-id}/datasets/{dataset-id}/refresh
```

### 5. Transform Data
```bash
curl -X POST http://localhost:8080/powerbi/transform \
  -H "Content-Type: application/json" \
  -d '{"rawData": "your-data-here"}'
```

## Data Flow

1. **Input**: Data files are processed from `src/data` directory
2. **Transformation**: Data is transformed using the PowerBIDataProcessor
3. **Routing**: Data is routed based on business rules (e.g., UK vs Other)
4. **Output**: Processed data is saved to `target/powerbi/` directories
5. **Integration**: Data can be consumed by Power BI through the REST API

## Monitoring and Logging

The application provides comprehensive logging for:
- Power BI API calls and responses
- Data transformation operations
- Authentication status
- Error handling and troubleshooting

Logs are available in the application console and can be configured in `log4j.properties`.

## Security Considerations

- Client secrets should be stored securely and not committed to version control
- Use environment variables or secure configuration management
- Implement proper access controls for the REST endpoints
- Monitor API usage and implement rate limiting if needed

## Troubleshooting

### Common Issues

1. **Authentication Failures**:
   - Verify tenant ID, client ID, and client secret
   - Check Azure AD application permissions
   - Ensure Power BI API permissions are granted

2. **Connection Issues**:
   - Check network connectivity to Power BI APIs
   - Verify firewall settings
   - Check proxy configuration if applicable

3. **Data Processing Errors**:
   - Verify input data format
   - Check transformation logic
   - Review error logs for details

### Support

For issues and support:
1. Check the application logs
2. Verify configuration settings
3. Test individual endpoints
4. Review Power BI API documentation

## Future Enhancements

Potential future improvements:
- Real-time streaming data integration
- Advanced data transformation capabilities
- Custom report generation
- Integration with other Microsoft services
- Enhanced monitoring and alerting
- Support for Power BI embedded scenarios