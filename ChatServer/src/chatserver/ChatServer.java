package chatserver;

import java.io.*; 
import java.net.*;
import java.util.*;

/**
 * Classe: ChatServer
 * Realizza l'implementazione del server per la chat tra molteplici client.
 * @author MA.FIGINI
 */
public class ChatServer 
{
    static ArrayList<ClientHandler> clientList = new ArrayList<>();
    
    public static void main(String[] args) throws IOException 
    {
        System.setProperty("javax.net.ssl.keyStore", "makey.store");
        System.setProperty("javax.net.ssl.keyStorePassword", "matteofigini");
        
        Server server = new Server(1234);
        // Generazione della chiave pubblica e della chiave privata
        /*RSA rsa = new RSA(2048);
        System.out.println("Chiavi di cifratura RSA generate.");
        
        // Connessione con il database
        DBConnection dbConn = new DBConnection("localhost", "3306", "chat", "root", "");
        dbConn.resetTable();
        dbConn.writePublicKey("server", rsa.getE().toString(), rsa.getN().toString());
        
        // Istanziamento della socket
        Socket s;
        ServerSocket ss = new ServerSocket(1234);
        System.out.println("Socket istanziata: server pronto all'ascolto...");
        
        // Ascolto sulla porta istanziata e creazione del ClientHandler apposito
        while (true) {
            s = ss.accept();
            System.out.println("Nuova richiesta di connessione: " + s);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF("client " + i);
            
            System.out.println("Creazione di un handler per il nuovo client...");
            ClientHandler clienthandler = new ClientHandler(s, "client " + i, dis, dos);
            Thread thread = new Thread(clienthandler);
            clientList.add(clienthandler);
            thread.start();
            i++;
            System.out.println("Client aggiunto alla lista dei client attivi.");
        }*/
    }
} 

/*class ClientHandler implements Runnable 
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
    
    public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) 
    {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin = true;
    }
    
    @Override
    public void run() 
    {
        while (true) {
            try {
                String destinatario = this.dis.readUTF();
                if(destinatario.equalsIgnoreCase("logout")) {
                    this.isloggedin = false;
                    this.dos.close();
                    this.dis.close();
                    this.s.close();
                    break;
                }
                
                int length = dis.readInt();
                byte[] encryptedMessage = null;
                if (length > 0) {
                    encryptedMessage = new byte[length];
                    dis.readFully(encryptedMessage);
                    if (destinatario.equalsIgnoreCase("all")) {
                        for (ClientHandler mc : ChatServer.clientList) {
                            if (mc.isloggedin) {
                                mc.dos.writeUTF(this.name);
                                mc.dos.writeUTF(destinatario);
                                mc.dos.writeInt(encryptedMessage.length);
                                mc.dos.write(encryptedMessage);
                            }
                        }
                    }
                    else {
                        for (ClientHandler mc : ChatServer.clientList) {
                            if (mc.name.equalsIgnoreCase(destinatario) && mc.isloggedin) {
                                mc.dos.writeUTF(this.name);
                                mc.dos.writeUTF(destinatario);
                                mc.dos.writeInt(encryptedMessage.length);
                                mc.dos.write(encryptedMessage);
                                break;
                            }
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            this.dis.close();
            this.dos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    } 
} */
