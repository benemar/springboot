DOMANDE SPRING
**Domanda: Qual è Spring Framework?**
*Spiegazione:* Spring Framework è un framework di sviluppo open-source per la creazione
di applicazioni Java. Fornisce un'ampia gamma di funzionalità per semplificare lo sviluppo,
inclusa l'inversione di controllo (IoC) e l'implementazione dell'AOP (Aspect-Oriented
Programming).

**Domanda: Cos'è l'Inversione di Controllo (IoC) in Spring?**
*Spiegazione:* L'Inversione di Controllo è un principio chiave di Spring. Invece di creare
oggetti manualmente, Spring li crea e li gestisce per te. Questo consente di concentrarsi
sulla logica dell'applicazione piuttosto che sulla gestione dei componenti.

**Domanda: Qual è un bean Spring?**
*Spiegazione:* Un bean Spring è un oggetto gestito dal container Spring. Viene configurato
mediante metadati, come ad esempio annotazioni o file di configurazione XML, e può essere
gestito dal ciclo di vita del container.

**Domanda: Cos'è l'AOP (Aspect-Oriented Programming) in Spring?**
*Spiegazione:* L'AOP è un paradigma di programmazione che consente di separare le
preoccupazioni trasversali, come il logging o la sicurezza, dalla logica di business principale.
In Spring, l'AOP può essere implementato utilizzando gli aspetti, che consentono di iniettare
comportamenti aggiuntivi in punti specifici del codice.

**Domanda: Qual è il vantaggio dell'annotazione `@Autowired`?**
*Spiegazione:* L'annotazione `@Autowired` viene utilizzata per l'iniezione automatica delle
dipendenze. Questo significa che Spring risolve automaticamente le dipendenze di un bean
e le inietta quando il bean viene creato.

**Domanda: Cosa sono i profili in Spring?**
*Spiegazione:* I profili in Spring consentono di definire configurazioni specifiche per diversi
ambienti (ad esempio, sviluppo, test, produzione). Ciò permette di separare le configurazioni
senza dover modificare il codice sorgente.

**Domanda: Qual è l'obiettivo di Spring Boot?**
*Spiegazione:* Spring Boot è una sottoprogetto di Spring che semplifica la configurazione e
l'avvio rapido di applicazioni Spring. Fornisce impostazioni predefinite sensate e un'ampia
gamma di funzionalità per sviluppare applicazioni stand-alone.

**Domanda: Cosa sono gli starter in Spring Boot?**
*Spiegazione:* Gli starter in Spring Boot sono dipendenze preconfigurate che semplificano
l'aggiunta di funzionalità specifiche alle applicazioni. Ad esempio, lo starter
"spring-boot-starter-web" include tutto il necessario per sviluppare applicazioni web.

**Domanda: Come gestisce Spring Boot la configurazione delle applicazioni?**
*Spiegazione:* Spring Boot utilizza il concetto di "opinione per configurazione" (convention
over configuration) per ridurre la configurazione esplicita. Fornisce impostazioni predefinite
intelligenti, ma è comunque possibile personalizzarle se necessario.**Domanda: Cosa sono le transazioni dichiarative in Spring?**
*Spiegazione:* Le transazioni dichiarative consentono di gestire le transazioni utilizzando
metadati, come annotazioni o configurazioni XML. Spring gestirà automaticamente il commit
o il rollback delle transazioni in base al comportamento definito.

**Domanda: Come si configura la sicurezza basata su ruoli in Spring?**
*Spiegazione:* La sicurezza basata su ruoli in Spring può essere configurata utilizzando
l'annotazione `@EnableWebSecurity` e la classe che estende
`WebSecurityConfigurerAdapter`. È possibile definire ruoli, autorizzazioni e regole di
accesso per le diverse parti dell'applicazione.

**Domanda: Cos'è l'iniezione delle dipendenze?**
*Spiegazione:* L'iniezione delle dipendenze è un concetto chiave in Spring, che permette di
fornire le dipendenze necessarie a un oggetto da parte di un entità esterna (ad esempio, un
framework di inversione di controllo come Spring). Ciò favorisce la separazione delle
preoccupazioni e la riusabilità del codice.

**Domanda: Cosa sono i profili di Spring?**
*Spiegazione:* I profili di Spring consentono di definire insiemi specifici di componenti e
configurazioni da utilizzare in base all'ambiente dell'applicazione. I profili possono essere
attivati tramite proprietà di sistema, variabili d'ambiente o altri meccanismi.

**Domanda: Qual è l'obiettivo principale di Spring Data?**
*Spiegazione:* Spring Data mira a semplificare l'accesso e la manipolazione dei dati dai
database e da altre fonti dati. Fornisce un'astrazione uniforme per interagire con diversi tipi
di database, riducendo la quantità di codice boilerplate.

**Domanda: Cosa offre Spring MVC?**
*Spiegazione:* Spring MVC è un modulo di Spring che fornisce un framework per lo sviluppo
di applicazioni web. Offre una struttura per gestire richieste e risposte HTTP, supportando il
modello MVC (Model-View-Controller) per separare la logica di business dalla
presentazione.**

**Domanda: Cosa è il pattern MVC:**
Il pattern MVC (Model-View-Controller) è un modello architetturale utilizzato per separare le
responsabilità all'interno di un'applicazione. Il Model rappresenta i dati e la logica di
business, la View gestisce la presentazione dell'interfaccia utente e il Controller gestisce la
logica di gestione delle richieste e delle azioni dell'utente.

**Domanda: Creazione di un progetto da zero:**
Per creare un progetto da zero, solitamente si utilizza uno strumento di build come Maven o
Gradle. Si crea la struttura delle cartelle del progetto, si configura il file di build con le
dipendenze e si inizia a scrivere il codice sorgente.

**Domanda: Dipendenze per un'app web:**
Per un'applicazione web in Spring, potresti utilizzare le seguenti dipendenze di base:
- `spring-boot-starter-web`: Per creare un'applicazione web.- `spring-boot-starter-data-jpa`: Per l'accesso ai dati tramite JPA.
- `spring-boot-starter-thymeleaf` o `spring-boot-starter-freemarker`: Per la gestione dei
template.
- Altre dipendenze specifiche in base alle tue esigenze, ad esempio per la sicurezza o per la
gestione delle sessioni.

**Domanda: Microservizi:**
  I microservizi sono un approccio architetturale in cui un'applicazione è suddivisa in
componenti più piccoli e autonomi chiamati microservizi. Ogni microservizio svolge una
funzione specifica e può essere sviluppato, distribuito e scalato indipendentemente dagli
altri.

**Domanda: Cosa è un database relazionale:**
Un database relazionale è un tipo di database che organizza i dati in tabelle con righe e
colonne. Le tabelle possono essere collegate tramite relazioni, consentendo di definire
strutture complesse e di gestire dati con vincoli di integrità.

**Domanda: Creazione di una query nel repository:**
In Spring Data, puoi creare query utilizzando i metodi di query derivati o annotando metodi
con `@Query`. Ad esempio, per trovare un utente con un certo nome usando JPA:
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
User findByName(String name);
}

```
**Domanda: Come collegare un progetto a un database:**
Per collegare un progetto Spring a un database, devi configurare il file `application.yml` con
le impostazioni di connessione. Successivamente, puoi creare un'entità JPA annotata e un
repository per interagire con il database.

**Domanda: Cosa rappresenta l’acronimo CRUD:**
CRUD sta per Create, Read, Update e Delete, che sono le operazioni di base per la
gestione dei dati in un'applicazione.

**Domanda: Creazione di un metodo con ciclo for e if:**
Ecco un esempio di metodo con un ciclo for e un'istruzione if:
```java
public void processList(List<Boolean> values) {
for (Boolean value : values) {
if (value) {
// Fai qualcosa se il valore è true
}
}
}
```**Domanda: Cosa sono Git e GitHub:**
Git è un sistema di controllo delle versioni distribuito utilizzato per tracciare le modifiche al
codice. GitHub è una piattaforma web che offre hosting per repository Git, facilitando la
collaborazione e la condivisione del codice.
**Domanda: Cos'è Docker:**
Docker è una piattaforma di containerizzazione che consente di confezionare, distribuire e
gestire le applicazioni insieme alle loro dipendenze in container leggeri e portabili. Questo
consente di isolare l'applicazione e garantire che funzioni in modo coerente in diversi
ambienti.
