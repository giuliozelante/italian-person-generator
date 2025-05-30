package it.gzelante.italianperson

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import jakarta.inject.Singleton

@Controller
@Singleton
class PersonController {

    @Get("/health")
    fun health(): HttpResponse<HealthResponse> {
        val response = HealthResponse(
            status = "OK",
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            service = "Italian Person Generator API"
        )
        return HttpResponse.ok(response)
    }

    @Get("/api/v1/person")
    fun generatePerson(): HttpResponse<ItalianPerson> {
        val person = PersonGenerator.generatePerson()
        return HttpResponse.ok(person)
    }

    @Get("/api/v1/persons")
    fun generatePersons(@QueryValue(defaultValue = "1") count: Int): HttpResponse<PersonsResponse> {
        val actualCount = count.coerceIn(1, 100)
        val persons = (1..actualCount).map { PersonGenerator.generatePerson() }
        val response = PersonsResponse(count = actualCount, persons = persons)
        return HttpResponse.ok(response)
    }

    @Get("/")
    fun apiInfo(): HttpResponse<ApiInfoResponse> {
        val endpoints = listOf(
            EndpointInfo("/health", "GET", "Health check endpoint"),
            EndpointInfo("/api/v1/person", "GET", "Generate a single Italian person"),
            EndpointInfo("/api/v1/persons?count=N", "GET", "Generate multiple Italian persons (max 100)"),
            EndpointInfo("/", "GET", "API information")
        )
        
        val response = ApiInfoResponse(
            name = "Italian Person Generator API",
            version = "1.0.0",
            description = "REST API per generare dati autentici di persone italiane con nomi, indirizzi, codici fiscali e altri dati realistici.",
            endpoints = endpoints
        )
        return HttpResponse.ok(response)
    }

    @Get(value = "/demo", produces = [MediaType.TEXT_HTML])
    fun demoPage(): HttpResponse<String> {
        val html = """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>🇮🇹 Italian Person Generator - Demo</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
            
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background: linear-gradient(135deg, #009246 0%, #ffffff 50%, #ce2b37 100%);
                        min-height: 100vh;
                        padding: 20px;
                    }
            
                    .container {
                        max-width: 1200px;
                        margin: 0 auto;
                        background: rgba(255, 255, 255, 0.95);
                        border-radius: 20px;
                        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }
            
                    .header {
                        background: linear-gradient(45deg, #009246, #009246);
                        color: white;
                        padding: 40px;
                        text-align: center;
                    }
            
                    .header h1 {
                        font-size: 3em;
                        margin-bottom: 10px;
                        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
                    }
            
                    .header p {
                        font-size: 1.2em;
                        opacity: 0.9;
                    }
            
                    .content {
                        padding: 40px;
                    }
            
                    .demo-section {
                        margin-bottom: 40px;
                    }
            
                    .demo-section h2 {
                        color: #009246;
                        margin-bottom: 20px;
                        font-size: 2em;
                        border-bottom: 3px solid #ce2b37;
                        padding-bottom: 10px;
                    }
            
                    .buttons {
                        display: flex;
                        gap: 15px;
                        margin-bottom: 30px;
                        flex-wrap: wrap;
                    }
            
                    .btn {
                        background: linear-gradient(45deg, #009246, #228B22);
                        color: white;
                        border: none;
                        padding: 15px 30px;
                        border-radius: 10px;
                        cursor: pointer;
                        font-size: 1.1em;
                        font-weight: bold;
                        transition: all 0.3s ease;
                        box-shadow: 0 4px 15px rgba(0, 146, 70, 0.3);
                    }
            
                    .btn:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 8px 25px rgba(0, 146, 70, 0.4);
                    }
            
                    .btn.secondary {
                        background: linear-gradient(45deg, #ce2b37, #dc143c);
                        box-shadow: 0 4px 15px rgba(206, 43, 55, 0.3);
                    }
            
                    .btn.secondary:hover {
                        box-shadow: 0 8px 25px rgba(206, 43, 55, 0.4);
                    }
            
                    .result {
                        background: #f8f9fa;
                        border: 2px solid #e9ecef;
                        border-radius: 15px;
                        padding: 25px;
                        margin-top: 20px;
                        font-family: 'Courier New', monospace;
                        max-height: 400px;
                        overflow-y: auto;
                    }
            
                    .result pre {
                        white-space: pre-wrap;
                        word-wrap: break-word;
                        color: #2d3748;
                        line-height: 1.6;
                    }
            
                    .loading {
                        color: #009246;
                        font-style: italic;
                    }
            
                    .api-section {
                        background: linear-gradient(45deg, #f8f9fa, #ffffff);
                        border-radius: 15px;
                        padding: 30px;
                        margin-top: 30px;
                    }
            
                    .endpoint {
                        background: white;
                        border-left: 4px solid #009246;
                        padding: 20px;
                        margin-bottom: 15px;
                        border-radius: 0 10px 10px 0;
                        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
                    }
            
                    .endpoint-method {
                        background: #009246;
                        color: white;
                        padding: 5px 12px;
                        border-radius: 5px;
                        font-weight: bold;
                        font-size: 0.9em;
                        margin-right: 10px;
                    }
            
                    .endpoint-url {
                        font-family: 'Courier New', monospace;
                        background: #f1f3f4;
                        padding: 8px 12px;
                        border-radius: 5px;
                        margin: 10px 0;
                        color: #2d3748;
                        border: 1px solid #e2e8f0;
                    }
            
                    .footer {
                        text-align: center;
                        padding: 30px;
                        background: #f8f9fa;
                        color: #6c757d;
                        font-style: italic;
                    }
            
                    @media (max-width: 768px) {
                        .header h1 {
                            font-size: 2em;
                        }
                        
                        .buttons {
                            flex-direction: column;
                        }
                        
                        .btn {
                            width: 100%;
                        }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🇮🇹 Italian Person Generator</h1>
                        <p>Genera dati autentici di persone italiane</p>
                    </div>
            
                    <div class="content">
                        <div class="demo-section">
                            <h2>🎯 Demo Live</h2>
                            <div class="buttons">
                                <button class="btn" onclick="generateSinglePerson()">
                                    👤 Genera 1 Persona
                                </button>
                                <button class="btn" onclick="generateMultiplePersons(5)">
                                    👥 Genera 5 Persone
                                </button>
                                <button class="btn secondary" onclick="generateMultiplePersons(10)">
                                    🏛️ Genera 10 Persone
                                </button>
                                <button class="btn secondary" onclick="checkHealth()">
                                    💚 Health Check
                                </button>
                            </div>
                            <div id="result" class="result" style="display: none;">
                                <pre id="resultContent"></pre>
                            </div>
                        </div>
            
                        <div class="api-section">
                            <h2>📡 Documentazione API</h2>
                            
                            <div class="endpoint">
                                <div>
                                    <span class="endpoint-method">GET</span>
                                    <strong>Genera una singola persona</strong>
                                </div>
                                <div class="endpoint-url">/api/v1/person</div>
                                <p>Restituisce i dati completi di una persona italiana generata casualmente con nome, cognome, indirizzo, codice fiscale, telefono e email.</p>
                            </div>
            
                            <div class="endpoint">
                                <div>
                                    <span class="endpoint-method">GET</span>
                                    <strong>Genera più persone</strong>
                                </div>
                                <div class="endpoint-url">/api/v1/persons?count=N</div>
                                <p>Genera un numero specificato di persone (massimo 100). Il parametro <code>count</code> è opzionale, default = 1.</p>
                            </div>
            
                            <div class="endpoint">
                                <div>
                                    <span class="endpoint-method">GET</span>
                                    <strong>Controllo stato</strong>
                                </div>
                                <div class="endpoint-url">/health</div>
                                <p>Verifica che il servizio sia attivo e funzionante.</p>
                            </div>
            
                            <div class="endpoint">
                                <div>
                                    <span class="endpoint-method">GET</span>
                                    <strong>Informazioni API</strong>
                                </div>
                                <div class="endpoint-url">/</div>
                                <p>Restituisce le informazioni generali dell'API e l'elenco degli endpoint disponibili.</p>
                            </div>
                        </div>
                    </div>
            
                    <div class="footer">
                        <p>🚀 Powered by Micronaut & GraalVM • Made with ❤️ for Italy</p>
                    </div>
                </div>
            
                <script>
                    async function makeRequest(url) {
                        const resultDiv = document.getElementById('result');
                        const resultContent = document.getElementById('resultContent');
                        
                        resultDiv.style.display = 'block';
                        resultContent.innerHTML = '<span class="loading">⏳ Generazione in corso...</span>';
                        
                        try {
                            const response = await fetch(url);
                            const data = await response.json();
                            resultContent.textContent = JSON.stringify(data, null, 2);
                        } catch (error) {
                            resultContent.innerHTML = '<span style="color: #ce2b37;">❌ Errore: ' + error.message + '</span>';
                        }
                    }
            
                    function generateSinglePerson() {
                        makeRequest('/api/v1/person');
                    }
            
                    function generateMultiplePersons(count) {
                        makeRequest('/api/v1/persons?count=' + count);
                    }
            
                    function checkHealth() {
                        makeRequest('/health');
                    }
                </script>
            </body>
            </html>
        """.trimIndent()
        
        return HttpResponse.ok(html)
    }
} 