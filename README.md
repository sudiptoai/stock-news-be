# Stock News Backend

Backend service for stock market news and analysis. This Spring Boot application provides REST APIs to fetch stock market news and stock data from various third-party providers.

## Features

- **Market News API**: Fetch latest market news filtered by category (ALL, STOCKS, CRYPTO, FOREX, etc.)
- **Stock Quote API**: Get current stock quotes by ticker symbol
- **Stock Recommendations API**: Retrieve analyst recommendations for stocks
- **Provider Abstraction**: Pluggable provider architecture supporting multiple data sources
- **Caching**: Built-in caching using Caffeine for improved performance
- **CORS Support**: Configurable CORS for frontend integration

## Prerequisites

Before running the application locally, ensure you have the following installed:

- **Java 17** or higher ([Download](https://adoptium.net/))
- **Maven 3.9.x** or higher ([Download](https://maven.apache.org/download.cgi))
- **Git** (for cloning the repository)

## API Keys

The application requires API keys from third-party providers:

1. **NewsAPI** - For market news data
   - Sign up at [https://newsapi.org](https://newsapi.org)
   - Get your free API key

2. **Alpha Vantage** - For stock market data
   - Sign up at [https://www.alphavantage.co](https://www.alphavantage.co)
   - Get your free API key

## Local Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/sudiptoai/stock-news-be.git
cd stock-news-be
```

### 2. Configure API Keys

You can configure API keys in one of the following ways:

**Option A: Environment Variables (Recommended)**

Set the following environment variables:

```bash
export NEWSAPI_KEY=your_newsapi_key_here
export ALPHAVANTAGE_KEY=your_alphavantage_key_here
```

On Windows (Command Prompt):
```cmd
set NEWSAPI_KEY=your_newsapi_key_here
set ALPHAVANTAGE_KEY=your_alphavantage_key_here
```

On Windows (PowerShell):
```powershell
$env:NEWSAPI_KEY="your_newsapi_key_here"
$env:ALPHAVANTAGE_KEY="your_alphavantage_key_here"
```

**Option B: Application Configuration**

Edit `src/main/resources/application.yml` and replace the default values:

```yaml
newsapi:
  api-key: your_newsapi_key_here

alphavantage:
  api-key: your_alphavantage_key_here
```

**Note**: For development, the application will work with demo keys but may have limited functionality.

### 3. Build the Application

```bash
mvn clean package
```

This will:
- Download all dependencies
- Compile the source code
- Run tests
- Create an executable JAR file in the `target/` directory

### 4. Run the Application

**Option A: Using Maven**

```bash
mvn spring-boot:run
```

**Option B: Using the JAR file**

```bash
java -jar target/stock-news-be-0.0.1-SNAPSHOT.jar
```

**Option C: With Environment Variables**

```bash
NEWSAPI_KEY=your_key ALPHAVANTAGE_KEY=your_key mvn spring-boot:run
```

### 5. Verify the Application

The application will start on `http://localhost:8080`

You should see console output similar to:
```
Started StockNewsApplication in X.XXX seconds
```

## Testing the APIs

Once the application is running, you can test the endpoints:

### Get Latest Market News

```bash
# Get all news (default)
curl http://localhost:8080/api/v1/news

# Get stock-specific news
curl http://localhost:8080/api/v1/news?category=STOCKS

# Get crypto news with custom page size
curl http://localhost:8080/api/v1/news?category=CRYPTO&pageSize=10
```

### Get Stock Quote

```bash
# Get Apple (AAPL) stock quote
curl http://localhost:8080/api/v1/stocks/AAPL/quote

# Get Tesla (TSLA) stock quote
curl http://localhost:8080/api/v1/stocks/TSLA/quote
```

### Get Stock Recommendations

```bash
# Get analyst recommendations for Apple
curl http://localhost:8080/api/v1/stocks/AAPL/recommendations
```

## Running Tests

To run the test suite:

```bash
mvn test
```

To run tests with coverage:

```bash
mvn clean test
```

## Configuration

The application can be configured via `src/main/resources/application.yml`:

- **Server Port**: Change `server.port` (default: 8080)
- **Provider Selection**: Change `news.provider` or `stock.provider` to switch between providers
- **CORS Origins**: Modify `cors.allowed-origins` to allow your frontend URLs
- **Logging Level**: Adjust `logging.level.com.stocknews` for debug logs

## Development

### Project Structure

```
src/main/java/com/stocknews/
├── StockNewsApplication.java    # Main application class
├── config/                      # Configuration classes
├── controller/                  # REST controllers
│   ├── NewsController.java
│   └── StockController.java
├── model/                       # Data models
├── provider/                    # Provider implementations
│   ├── factory/                # Provider factories
│   ├── newsapi/                # NewsAPI integration
│   └── alphavantage/           # Alpha Vantage integration
└── service/                     # Business logic services
```

### Adding a New Provider

The application uses a factory pattern for providers:

1. Implement `NewsProvider` or `StockDataProvider` interface
2. Add `@Component` annotation
3. Update `news.provider` or `stock.provider` in configuration

The factory will automatically discover and use the new provider.

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, change it:

```bash
# Via environment variable
SERVER_PORT=8081 mvn spring-boot:run

# Or edit application.yml
server:
  port: 8081
```

### API Key Issues

- Verify your API keys are correctly set
- Check for extra spaces or quotes in environment variables
- Ensure you're not hitting rate limits on free API plans
- Try using demo keys first to verify the application runs

### Build Failures

```bash
# Clean and rebuild
mvn clean install

# Skip tests if needed (not recommended)
mvn clean package -DskipTests
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.