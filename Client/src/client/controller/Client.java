package client.controller;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import javax.net.ssl.*;
import client.model.*;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Classe: Client
 * Implementa la composizione del client, controllando le azioni
 * che può eseguire.
 * @author matte
 */
public class Client {
    private String ipServer;
    private final int port;
    private String username;
    
    private SSLSocket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    
    private DBConnection dbConn;
    private RSA rsa;
    
    public ArrayList<Message> listaMessaggi;
    
    public Client() {
        System.setProperty("javax.net.ssl.trustStore", "makey.store");
        System.setProperty("javax.net.ssl.trustStorePassword", "matteofigini");
        port = 1234;
        listaMessaggi = new ArrayList<Message>();
        rsa = new RSA(2048);
    }
    
    public Client(String ipServer, String username) {
        System.setProperty("javax.net.ssl.trustStore", "makey.store");
        System.setProperty("javax.net.ssl.trustStorePassword", "matteofigini");
        this.ipServer = ipServer;
        this.username = username;
        port = 1234;
        listaMessaggi = new ArrayList<Message>();
        rsa = new RSA(2048);
    }
    
    /**
     * @return 
     */
    public String connectSocket() {
        String logMessage = "";
        try {
            socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(InetAddress.getByName(this.ipServer), port);
            socket.setEnabledProtocols(new String[] {"TLSv1.2"});
            socket.setEnabledCipherSuites(new String[] {"TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            
            dbConn = new DBConnection(this.ipServer, "3306", "chat", "root", "");
            dbConn.writePublicKey(this.username, rsa.getE().toString(), rsa.getN().toString());
            
            dos.writeUTF(username);
            logMessage = "Connessione stabilita con il server (" + socket.getInetAddress() + ").";
            sendMessage (new Message(username, "all", username + " FA PARTE DELLA CHAT.\n"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            logMessage = ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            logMessage = ex.getMessage();
        }
        return logMessage;
    }
    
    public String closeProgram() {
        Message closingMessage = new Message();
        closingMessage.setSender(this.username);
        closingMessage.setReceiver("logout");
        if (socket != null) {
            try {
                dos.writeUTF(closingMessage.getSender());
                dos.writeUTF(closingMessage.getReceiver());
                dos.close();
                dis.close();
                socket.close();
            }
            catch (IOException exc) {
                return "Problema con la disconnessione...";
            }
        }
        dbConn.deleteUser(this.username);
        return "Bye.";
    }
    
    public void sendMessage (Message message) {
        message.setSender(this.username);
        byte[] encryptedMessage;
        if (message.getReceiver().equalsIgnoreCase("all")) {
            encryptedMessage = rsa.encrypt(message.getMessage().getBytes(), rsa.getD(), rsa.getN());
        }
        else {
            BigInteger destPublicE, destPublicN;
            destPublicE = new BigInteger(dbConn.getPublicKeyE(message.getReceiver()));
            destPublicN = new BigInteger(dbConn.getPublicKeyN(message.getReceiver()));
            encryptedMessage = rsa.encrypt(message.getMessage().getBytes(), destPublicE, destPublicN);
        }
        
        try {
            dos.writeUTF(message.getSender());
            dos.writeUTF(message.getReceiver());
            dos.writeInt(encryptedMessage.length);
            dos.write(encryptedMessage);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public DataInputStream getDataInputStream() {
        return dis;
    }
    
    public DBConnection getDBConn() {
        return dbConn;
    }
    
    public RSA getRSA() {
        return rsa;
    }
    
    public String getUsername() {
        return this.username;
    }
}
