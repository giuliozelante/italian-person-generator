package it.gzelante.italianperson

import kotlinx.serialization.Serializable
import kotlin.random.Random
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class ItalianPerson(
    val name: String,
    val surname: String,
    val dateOfBirth: String,
    val address: String,
    val cap: String,
    val fiscalCode: String,
    val mobilePhone: String,
    val idCard: String,
    val idCardIssueDate: String
)

@Serializable
data class ItalianCity(
    val name: String,
    val cap: String,
    val province: String
)

class ItalianPersonGenerator {
    
    private val maleNames = listOf(
        "Marco", "Giuseppe", "Francesco", "Antonio", "Alessandro", "Andrea", "Matteo", 
        "Lorenzo", "Davide", "Luca", "Stefano", "Roberto", "Simone", "Federico", 
        "Giulio", "Riccardo", "Michele", "Gabriele", "Emanuele", "Daniele"
    )
    
    private val femaleNames = listOf(
        "Maria", "Anna", "Giulia", "Sara", "Francesca", "Chiara", "Valentina", 
        "Alessandra", "Elena", "Martina", "Federica", "Silvia", "Giorgia", 
        "Elisa", "Serena", "Paola", "Laura", "Caterina", "Beatrice", "Roberta"
    )
    
    private val surnames = listOf(
        "Rossi", "Russo", "Ferrari", "Esposito", "Bianchi", "Romano", "Colombo", 
        "Ricci", "Marino", "Greco", "Bruno", "Gallo", "Conti", "De Luca", 
        "Mancini", "Costa", "Giordano", "Rizzo", "Lombardi", "Moretti", 
        "Barbieri", "Fontana", "Santoro", "Mariani", "Rinaldi", "Caruso"
    )
    
    private val streets = listOf(
        "Via Roma", "Via Garibaldi", "Via Mazzini", "Via Dante", "Via Verdi", 
        "Via Nazionale", "Via Milano", "Via Torino", "Via Napoli", "Via Venezia",
        "Corso Italia", "Corso Vittorio Emanuele", "Piazza San Marco", 
        "Via dei Fiori", "Via del Sole", "Via della Pace"
    )
    
    private val cities = listOf(
        ItalianCity("Roma", "00100", "RM"),
        ItalianCity("Milano", "20100", "MI"),
        ItalianCity("Napoli", "80100", "NA"),
        ItalianCity("Torino", "10100", "TO"),
        ItalianCity("Palermo", "90100", "PA"),
        ItalianCity("Genova", "16100", "GE"),
        ItalianCity("Bologna", "40100", "BO"),
        ItalianCity("Firenze", "50100", "FI"),
        ItalianCity("Bari", "70100", "BA"),
        ItalianCity("Catania", "95100", "CT"),
        ItalianCity("Venezia", "30100", "VE"),
        ItalianCity("Verona", "37100", "VR")
    )
    
    private val italianDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    fun generatePerson(): ItalianPerson {
        val isMale = Random.nextBoolean()
        val name = if (isMale) maleNames.random() else femaleNames.random()
        val surname = surnames.random()
        
        // Generate birth date (between 18 and 80 years old)
        val currentYear = LocalDate.now().year
        val birthYear = Random.nextInt(currentYear - 80, currentYear - 18)
        val birthMonth = Random.nextInt(1, 13)
        val birthDay = Random.nextInt(1, 29) // Use 28 to avoid February issues
        val birthDate = LocalDate.of(birthYear, birthMonth, birthDay)
        val dateOfBirth = birthDate.format(italianDateFormatter)
        
        val street = streets.random()
        val streetNumber = Random.nextInt(1, 200)
        val city = cities.random()
        // Italian address format: Via Nome, Numero, CAP Città Provincia
        val address = "$street, $streetNumber, ${city.cap} ${city.name} ${city.province}"
        
        val fiscalCode = generateFiscalCode(name, surname, birthDate, isMale)
        val mobilePhone = generateMobilePhone()
        val idCard = generateIdCard()
        
        // Generate ID card issue date (after 18th birthday, within last 10 years)
        val eighteenthBirthday = birthDate.plusYears(18)
        val maxIssueDate = LocalDate.now()
        val minIssueDate = maxOf(eighteenthBirthday, maxIssueDate.minusYears(10))
        
        val issueYear = Random.nextInt(minIssueDate.year, maxIssueDate.year + 1)
        val issueMonth = if (issueYear == maxIssueDate.year) {
            Random.nextInt(1, maxIssueDate.monthValue + 1)
        } else {
            Random.nextInt(1, 13)
        }
        val issueDay = Random.nextInt(1, 29)
        val idCardIssueDate = LocalDate.of(issueYear, issueMonth, issueDay).format(italianDateFormatter)
        
        return ItalianPerson(
            name = name,
            surname = surname,
            dateOfBirth = dateOfBirth,
            address = address,
            cap = city.cap,
            fiscalCode = fiscalCode,
            mobilePhone = mobilePhone,
            idCard = idCard,
            idCardIssueDate = idCardIssueDate
        )
    }
    
    private fun generateFiscalCode(name: String, surname: String, birthDate: LocalDate, isMale: Boolean): String {
        // Extract consonants for surname and name
        val surnameCode = extractConsonants(surname).take(3).padEnd(3, 'X')
        val nameCode = extractConsonants(name).take(3).padEnd(3, 'X')
        
        // Year (last 2 digits)
        val year = birthDate.year.toString().takeLast(2)
        
        // Month encoding for fiscal code
        val monthCodes = arrayOf("A", "B", "C", "D", "E", "H", "L", "M", "P", "R", "S", "T")
        val month = monthCodes[birthDate.monthValue - 1]
        
        // Day (for females, add 40)
        val day = if (isMale) {
            birthDate.dayOfMonth.toString().padStart(2, '0')
        } else {
            (birthDate.dayOfMonth + 40).toString()
        }
        
        val location = "H501" // Rome code as default
        val controlChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Random.nextInt(26)]
        
        return "${surnameCode}${nameCode}${year}${month}${day}${location}${controlChar}"
    }
    
    private fun generateIdCard(): String {
        // Italian ID card (CIE) format: XXXXXXXXX (no spaces)
        // Two capital letters, five numbers, two capital letters
        val firstTwoLetters = (1..2).map { 
            ('A'..'Z').random() 
        }.joinToString("")
        
        val fiveNumbers = (1..5).map { 
            Random.nextInt(0, 10) 
        }.joinToString("")
        
        val lastTwoLetters = (1..2).map { 
            ('A'..'Z').random() 
        }.joinToString("")
        
        return "$firstTwoLetters$fiveNumbers$lastTwoLetters"
    }
    
    private fun extractConsonants(text: String): String {
        val vowels = setOf('A', 'E', 'I', 'O', 'U')
        return text.uppercase().filter { it.isLetter() && it !in vowels }
    }
    
    private fun generateMobilePhone(): String {
        // Italian mobile prefixes
        val prefixes = listOf("320", "321", "322", "323", "324", "325", "326", "327", "328", "329",
                             "330", "331", "333", "334", "335", "336", "337", "338", "339",
                             "340", "342", "343", "345", "346", "347", "348", "349",
                             "350", "351", "360", "361", "362", "363", "366", "368", "380", "383", "388", "389")
        
        val prefix = prefixes.random()
        val number = (1..7).map { Random.nextInt(10) }.joinToString("")
        
        return "+39 $prefix$number"
    }
}