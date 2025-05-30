package it.gzelante.italianperson

import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDate
import kotlin.random.Random

/**
 * Represents a generated Italian person with authentic data
 */
@Serdeable
data class ItalianPerson(
    val nome: String,
    val cognome: String,
    val nomeCompleto: String,
    val sesso: String,
    val dataNascita: LocalDate,
    val luogoNascita: String,
    val provincia: String,
    val indirizzo: String,
    val citta: String,
    val cap: String,
    val regione: String,
    val codiceFiscale: String,
    val cellulare: String,
    val email: String,
    val cartaIdentita: String
)

/**
 * Response wrapper for multiple persons
 */
@Serdeable
data class PersonsResponse(
    val count: Int,
    val persons: List<ItalianPerson>
)

/**
 * Response for health check
 */
@Serdeable
data class HealthResponse(
    val status: String,
    val timestamp: String,
    val service: String
)

/**
 * API information response
 */
@Serdeable
data class ApiInfoResponse(
    val name: String,
    val version: String,
    val description: String,
    val endpoints: List<EndpointInfo>
)

@Serdeable
data class EndpointInfo(
    val path: String,
    val method: String,
    val description: String
)

/**
 * Generates authentic Italian person data
 */
object PersonGenerator {
    
    private val nomiMaschili = listOf(
        "Marco", "Giuseppe", "Antonio", "Luca", "Francesco", "Alessandro", "Matteo", "Lorenzo", "Andrea", "Gabriele",
        "Stefano", "Davide", "Simone", "Nicola", "Paolo", "Riccardo", "Salvatore", "Vincenzo", "Giovanni", "Roberto",
        "Emanuele", "Fabio", "Massimo", "Michele", "Claudio", "Federico", "Daniele", "Giorgio", "Franco", "Enrico"
    )
    
    private val nomiFemminili = listOf(
        "Maria", "Anna", "Francesca", "Laura", "Giulia", "Elena", "Valentina", "Chiara", "Sara", "Paola",
        "Alessandra", "Barbara", "Roberta", "Federica", "Silvia", "Monica", "Cristina", "Elisabetta", "Giovanna", "Stefania",
        "Martina", "Giorgia", "Serena", "Claudia", "Michela", "Daniela", "Patrizia", "Rosa", "Caterina", "Luciana"
    )
    
    private val cognomi = listOf(
        "Rossi", "Russo", "Ferrari", "Esposito", "Bianchi", "Romano", "Colombo", "Ricci", "Marino", "Greco",
        "Bruno", "Gallo", "Conti", "De Luca", "Mancini", "Costa", "Giordano", "Rizzo", "Lombardi", "Moretti",
        "Barbieri", "Fontana", "Santoro", "Mariani", "Rinaldi", "Caruso", "Ferrara", "Galli", "Martini", "Leone",
        "Longo", "Gentile", "Martinelli", "Vitale", "Lombardo", "Serra", "Coppola", "De Santis", "D'Angelo", "Marchetti"
    )
    
    private val cittaConProvinciaECap = listOf(
        Triple("Roma", "RM", "00100"), Triple("Milano", "MI", "20100"), Triple("Napoli", "NA", "80100"),
        Triple("Torino", "TO", "10100"), Triple("Palermo", "PA", "90100"), Triple("Genova", "GE", "16100"),
        Triple("Bologna", "BO", "40100"), Triple("Firenze", "FI", "50100"), Triple("Bari", "BA", "70100"),
        Triple("Catania", "CT", "95100"), Triple("Venezia", "VE", "30100"), Triple("Verona", "VR", "37100"),
        Triple("Messina", "ME", "98100"), Triple("Padova", "PD", "35100"), Triple("Trieste", "TS", "34100"),
        Triple("Brescia", "BS", "25100"), Triple("Taranto", "TA", "74100"), Triple("Prato", "PO", "59100"),
        Triple("Reggio Calabria", "RC", "89100"), Triple("Modena", "MO", "41100"), Triple("Reggio Emilia", "RE", "42100"),
        Triple("Perugia", "PG", "06100"), Triple("Livorno", "LI", "57100"), Triple("Ravenna", "RA", "48100"),
        Triple("Cagliari", "CA", "09100"), Triple("Foggia", "FG", "71100"), Triple("Rimini", "RN", "47900"),
        Triple("Salerno", "SA", "84100"), Triple("Ferrara", "FE", "44100"), Triple("Sassari", "SS", "07100")
    )
    
    private val regioni = mapOf(
        "RM" to "Lazio", "MI" to "Lombardia", "NA" to "Campania", "TO" to "Piemonte",
        "PA" to "Sicilia", "GE" to "Liguria", "BO" to "Emilia-Romagna", "FI" to "Toscana",
        "BA" to "Puglia", "CT" to "Sicilia", "VE" to "Veneto", "VR" to "Veneto",
        "ME" to "Sicilia", "PD" to "Veneto", "TS" to "Friuli-Venezia Giulia", "BS" to "Lombardia",
        "TA" to "Puglia", "PO" to "Toscana", "RC" to "Calabria", "MO" to "Emilia-Romagna",
        "RE" to "Emilia-Romagna", "PG" to "Umbria", "LI" to "Toscana", "RA" to "Emilia-Romagna",
        "CA" to "Sardegna", "FG" to "Puglia", "RN" to "Emilia-Romagna", "SA" to "Campania",
        "FE" to "Emilia-Romagna", "SS" to "Sardegna"
    )
    
    private val vie = listOf(
        "Via Roma", "Via Milano", "Via Napoli", "Via Giuseppe Garibaldi", "Via Antonio Gramsci",
        "Via Vittorio Emanuele", "Via Dante Alighieri", "Via Alessandro Manzoni", "Via Giuseppe Verdi",
        "Via Francesco Petrarca", "Via Giovanni Boccaccio", "Via Giacomo Leopardi", "Via Ugo Foscolo",
        "Via Luigi Pirandello", "Via Giosuè Carducci", "Via Salvatore Quasimodo", "Via Eugenio Montale",
        "Via Cesare Pavese", "Corso Italia", "Corso Europa", "Piazza del Duomo", "Piazza Garibaldi",
        "Piazza della Repubblica", "Piazza San Marco", "Piazza del Popolo"
    )

    fun generatePerson(): ItalianPerson {
        val isMale = Random.nextBoolean()
        val nome = if (isMale) nomiMaschili.random() else nomiFemminili.random()
        val cognome = cognomi.random()
        val nomeCompleto = "$nome $cognome"
        val sesso = if (isMale) "M" else "F"
        
        val dataNascita = generateRandomDate()
        val eta = LocalDate.now().year - dataNascita.year
        
        val (citta, provincia, cap) = cittaConProvinciaECap.random()
        val regione = regioni[provincia] ?: "Unknown"
        val indirizzo = "${vie.random()}, ${Random.nextInt(1, 200)}"
        
        val codiceFiscale = generateCodiceFiscale(nome, cognome, dataNascita, sesso, citta)
        val cellulare = generateCellulare()
        val email = generateEmail(nome, cognome)
        val cartaIdentita = generateCartaIdentita()
        
        return ItalianPerson(
            nome = nome,
            cognome = cognome,
            nomeCompleto = nomeCompleto,
            sesso = sesso,
            dataNascita = dataNascita,
            luogoNascita = citta,
            provincia = provincia,
            indirizzo = indirizzo,
            citta = citta,
            cap = cap,
            regione = regione,
            codiceFiscale = codiceFiscale,
            cellulare = cellulare,
            email = email,
            cartaIdentita = cartaIdentita
        )
    }
    
    private fun generateRandomDate(): LocalDate {
        val currentYear = LocalDate.now().year
        val startYear = currentYear - 80
        val endYear = currentYear - 18
        
        val year = Random.nextInt(startYear, endYear + 1)
        val month = Random.nextInt(1, 13)
        val dayOfMonth = when (month) {
            2 -> if (isLeapYear(year)) Random.nextInt(1, 30) else Random.nextInt(1, 29)
            4, 6, 9, 11 -> Random.nextInt(1, 31)
            else -> Random.nextInt(1, 32)
        }
        
        return LocalDate.of(year, month, dayOfMonth)
    }
    
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
    
    private fun generateCodiceFiscale(nome: String, cognome: String, dataNascita: LocalDate, sesso: String, luogoNascita: String): String {
        fun getConsonants(str: String): String = str.filter { it.isLetter() && it.uppercase() !in "AEIOU" }
        fun getVowels(str: String): String = str.filter { it.uppercase() in "AEIOU" }
        
        fun encodeString(str: String): String {
            val consonants = getConsonants(str.uppercase())
            val vowels = getVowels(str.uppercase())
            val combined = consonants + vowels + "XXX"
            return if (consonants.length >= 4) {
                consonants[0].toString() + consonants[2].toString() + consonants[3].toString()
            } else {
                combined.take(3)
            }
        }
        
        val cognomePart = encodeString(cognome)
        val nomePart = encodeString(nome)
        
        val anno = (dataNascita.year % 100).toString().padStart(2, '0')
        val mesi = "ABCDEHLMPRST"
        val mese = mesi[dataNascita.monthValue - 1]
        
        val giorno = if (sesso == "M") {
            dataNascita.dayOfMonth.toString().padStart(2, '0')
        } else {
            (dataNascita.dayOfMonth + 40).toString()
        }
        
        val luogoCodice = "A001" // Simplified location code
        
        val controllo = Random.nextInt(10, 100)
        
        return "${cognomePart}${nomePart}${anno}${mese}${giorno}${luogoCodice}${controllo}"
    }
    
    private fun generateCellulare(): String {
        val prefissi = listOf("320", "330", "340", "347", "348", "349", "360", "366", "368", "380", "383", "388", "389", "390", "391", "392", "393", "394", "395", "396", "397", "398", "399")
        val prefisso = prefissi.random()
        val numero = (1000000..9999999).random()
        return "$prefisso$numero"
    }
    
    private fun generateEmail(nome: String, cognome: String): String {
        val domini = listOf("gmail.com", "libero.it", "yahoo.it", "hotmail.it", "alice.it", "virgilio.it", "tin.it", "tiscali.it")
        val dominio = domini.random()
        val numero = Random.nextInt(1, 100)
        return "${nome.lowercase()}.${cognome.lowercase()}$numero@$dominio"
    }
    
    private fun generateCartaIdentita(): String {
        val lettere = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val numeri = "0123456789"
        
        return buildString {
            repeat(2) { append(lettere.random()) }
            repeat(5) { append(numeri.random()) }
            repeat(2) { append(lettere.random()) }
        }
    }
} 