# SABD 2020/2021 Progetto 2
Marco Marcucci, Giuseppe Lasco

## Prerequisiti

* Installare Apache Flink
* Eseguire `export FLINK_HOME=<flink-path> `
* Eseguire `export PATH=$PATH:$FLINK_HOME/bin `
* Installare Apache Kafka
* Eseguire `export KAFKA_HOME=<kafka-path> `
* Eseguire `export PATH=$PATH:$KAFKA_HOME/bin `
* Installare Docker
* Installare Docker Compose

## Ambiente Docker e script 

Nella directory `/scripts` sono presenti diversi script che permettono l'avvio 
dell'applicazione in varie modalità:

Lo script `start-project.sh` permette di:
* Avviare i servizi (Flink, Kafka)
* Caricare le configurazioni di Flink
* Creare i topic Kafka
* Eseguire l'applicazione Flink
* Aprire il terminale connesso al consumer Kafka 
* Eseguire il processo Producer. 
  
Lo script `start-project-consumer.sh` permette di:
* Avviare i servizi (Flink, Kafka)
* Caricare le configurazioni di Flink
* Creare i topic Kafka
* Eseguire l'applicazione Flink
* Aprire dei terminali connessi ai consumer Kafka
* Eseguire il processo Producer. 
  
Lo script `warm-restart-project.sh` riavvia l'applicazione Flink e il Producer. Viene utilizzato a sistema avviato.

Lo script `warm-restart-project-consumer.sh` riavvia l'applicazione Flink, i terminali connessi ai consumer e il Producer. Viene utilizzato a sistema avviato.

Nel caso in cui si riavvii l'applicazione, è consigliabile terminare tutti i terminali connessi ai consumer.

Lo script `cancel-job.sh` permette di arrestare il job Flink in esecuzione.

Lo script `delete-files.sh` permette di eliminare tutti i files dei risultati

Lo script `maven-package.sh` avvia il processo di building e packaging del codice.
 
Lo script `stop-project.sh` permette, infine, di arrestare tutti i servizi precedentemente avviati.

Al primo avvio del progetto è necessario eseguire il build del progetto, perciò è utile seguire i seguenti passi:

* Avviare lo script `maven-package.sh` nella directory `scripts`
* Avviare lo script `start-project.sh` o lo `start-project-consumer.sh`
* Monitorare eventualmente i terminali dei consumer
* Collegarsi eventualmente al seguente indirizzo <http://localhost:8081> per monitorare lo stato di esecuzione dell'applicazione Flink

## Configurazioni

Nella directory `/flink-config` è possibile trovare il file `flink-conf.yml` contenente l'insieme dei settaggi dell'ambiente
Flink. La cartella `/resources` contiene due file di configurazione:
* `config.properties` nel quale è possibile settare il tempo totale di esecuzione del replay dei dati da parte del processo
Producer
* `kafka.properties` nel quale vengono indicati gli indirizzi dei broker Kafka.

## Struttura dell'applicazione
L'applicazione si compone di tre package `queries`, `utils` e `benchmark`. 

Nel primo sono presenti le classi che implementano le query e il main di avvio dell'applicazione.

Il secondo package contiene le classi di utilità, tra cui `ShipData` che permette di creare oggetti ad hoc per la 
computazione, `SinkUtils` che permette di formattare correttamente i dati in output e `KafkaProperties` che contiene
i metodi che permettono di recuperare le configurazioni, `Producer` che permette di inviare i dati su Kafka, `Consumer` 
che permette di ricevere e scrivere su file i dati da Kafka.

Nel terzo package sono presenti le classi che permetto di valutare le prestazioni in termini di throughput e latenza 
dell'applicazione.

## Directory
Nella cartella `Results` si trovano i risultati delle 
query, in formato `csv`, come richiesto dalla specifica.
