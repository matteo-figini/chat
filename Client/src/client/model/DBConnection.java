package client.model;

import java.sql.*;
import java.util.logging.*;
import java.util.ArrayList;

/**
 * Classe: DBConnection
 * Realizza la connessione al database e permette le manipolazioni necessarie.
 * @author matte
 */
public class DBConnection 
{
    private Connection connection;
    
    /**
     * Metodo: DBConnection (costruttore)
     * Inizializza la connessione al database, in base ai parametri passati come input.
     * @param ipAddress [String]: indirizzo IP del database (generalmente, localhost)
     * @param port [String]: porta su cui sta operando il DB (MySQL opera di default sulla 3306)
     * @param dbName [String]: nome del database (di default, "chat")
     * @param username [String]: username dell'utente (di default, "root")
     * @param password [String]: password dell'utente (di default, "")
     */
    public DBConnection(String ipAddress, String port, String dbName, String username, String password)
    {
        String connString = "jdbc:mysql://";
        this.connection = null;
        connString = connString.concat(ipAddress);
        connString = connString.concat(":");
        connString = connString.concat(port);
        connString = connString.concat("/");
        connString = connString.concat(dbName);
        connString = connString.concat("?user=");
        connString = connString.concat(username);
        connString = connString.concat("&password=");
        connString = connString.concat(password);
        connString = connString.concat("&serverTimezone=UTC");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(connString);
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (connection == null) {
            System.out.println("Impossibile stabilire una connessione con il DB.");
        }
        else {
            System.out.println("Connessione al DB riuscita.");
        }
    }
    
    /**
     * Metodo: resetTable
     * Resetta la tabella "chiavi_pubbliche" nel database.
     * Richiamato in genere all'inizio dell'avvio del server.
     */
    public void resetTable()
    {
        String queryString = "DELETE FROM chiavi_pubbliche";
        try {
            PreparedStatement statement = this.connection.prepareStatement(queryString);
            statement.executeUpdate();
            System.out.println("Inizializzazione completata con successo.");
        }
        catch (SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println("Inizializzazione della tabella completata con successo.");
    }
    
    /**
     * Metodo: writePublicKey
     * Scrive nella tabella "chiavi_pubbliche" le chiavi generate dall'utente.
     * Deve essere richiamato dopo la generazione delle chiavi RSA.
     * @param name [String]: nome dell'utente da inserire.
     * @param publicE [String]: parametro E della chiave pubblica
     * @param publicN [String]: parametro N della chiave pubblica
     */
    public void writePublicKey(String name, String publicE, String publicN)
    {
        String queryString = "INSERT INTO chiavi_pubbliche (NomeClient, PublicE, PublicN) "
                + "VALUES ('" + name + "', '" + publicE + "', '" + publicN + "')";
        try {
            PreparedStatement statement = this.connection.prepareStatement(queryString);
            statement.executeUpdate();
            System.out.println("Inserimento delle chiavi di " + name + " completato.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo: getPublicKeyE
     * Ritorna il parametro E della chiave pubblica dell'utente con nome specificato nei parametri.
     * @param name [String]: nome dell'utente da cercare
     * @return publicE [String]: parametro E della chiave pubblica
     */
    public String getPublicKeyE(String name)
    {
        String publicE = null;
        String queryString = "SELECT PublicE FROM chiavi_pubbliche WHERE NomeClient='" + name + "'";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(queryString);
            while (rs.next()) {
                publicE = rs.getString("PublicE");
            }
            return publicE;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Metodo: getPublicKeyN
     * Ritorna il parametro N della chiave pubblica dell'utente con nome specificato nei parametri.
     * @param name [String]: nome dell'utente da cercare
     * @return publicN [String]: parametro N della chiave pubblica
     */
    public String getPublicKeyN(String name)
    {
        String publicN = null;
        String queryString = "SELECT PublicN FROM chiavi_pubbliche WHERE NomeClient='" + name + "'";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(queryString);
            while (rs.next()) {
                publicN = rs.getString("PublicN");
            }
            return publicN;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void deleteUser (String name) {
        String query = "DELETE FROM chiavi_pubbliche WHERE NomeClient = '" + name + "'";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> getParticipants(String myUsername) {
        ArrayList<String> partecipanti = new ArrayList<String>();
        String query = "SELECT NomeClient FROM chiavi_pubbliche WHERE NomeClient != '" + myUsername + "'";
        partecipanti.add("all");
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                partecipanti.add(rs.getString("NomeClient"));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            partecipanti = null;
        }
        return partecipanti;
    }
}
