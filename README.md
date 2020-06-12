ISTRUZIONI PER LA CONFIGURAZIONE E L'USO:

1)  Compilazione dei sorgenti
    Per compilare i programmi sorgenti, si consiglia di aprire le rispettive cartelle di progetto con
    Netbeans (versione 8.2) e compilare con la versione di Java n° 8.
    Compilare premendo Maiusc + F11, oppure scegliendo l'opzione "Clean and Build".
    Nella directory <directory di progetto>/dist/ è presente una sottodirectory /lib/, necessaria per
    le operazioni di connessione al DB.

2)  Creazione della cartella di utilizzo
    Spostare o copiare i file Client.jar e Server.jar appena compilati in una cartella personalizzata.
    Questa cartella deve contenere anche la sottodirectory /lib/ e il file makey.store (Certificato
    del Server).

3)  Configurazione del DB
    Per provare l'esecuzione del programma, assicurarsi di avere configurato MySQL sul proprio PC.
    Importare il database "chat" presente nel file "chat.sql".
    Verrà creata una tabella "chiavi_pubbliche" contenente 3 attributi.

4)  Esecuzione del programma
    Avviare da terminale il file Server.jar mediante il comando "java -jar Server.jar".
    Automaticamente il Server inizia la configurazione: se essa ha buon esito, verrà mostrato un
    messaggio di fine configurazione, altrimenti l'esecuzione si interromperà con un messaggio
    (assicurarsi che il database MySQL sia connesso e raggiungibile).
    Avviare il file Client.jar, sempre da terminale.
    All'apertura della finestra, scegliere uno username e l'indirizzo IP del server (se sulla stessa macchina,
    127.0.0.1). Se la connessione va a buon fine, il server registra la connessione di una nuova utenza e
    il client mostra le finestre di scrittura e lettura dei messaggi.
    Per provare a utilizzare la comunicazione, inizializzare una seconda istanza di Client.jar.