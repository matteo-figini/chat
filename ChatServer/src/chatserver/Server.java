/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import static chatserver.ChatServer.clientList;
import java.io.*;
import java.util.logging.*;
import javax.net.ssl.*;

/**
 *
 * @author matte
 */
public class Server 
{
    private int port;
    private SSLServerSocket serverSocket;
    
    private RSA rsa;
    
    public Server(int port) {
        configureServer(port);
        while (true) {
            try {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                socket.setEnabledProtocols(new String[] {"TLSv1.2"});
                socket.setEnabledCipherSuites(new String[] {"TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
                establishConnection(socket);
            }
            catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void configureServer(int port) {
        System.out.println("Welcome!");
        System.out.println("***************************************");
        System.out.println("SERVER CONFIGURATION");
        
        this.port = port;
        //this.rsa = new RSA(2048);
        
        // Istanziamento della socket
        try {
            serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(this.port);
            serverSocket.setEnabledProtocols(new String[] {"TLSv1.2"});
            serverSocket.setEnabledCipherSuites(new String[] {"TLS_DHE_RSA_WITH_AES_256_CBC_SHA256", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256"});
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Operazioni sul DB
        DBConnection dbConn = new DBConnection("localhost", "3306", "chat", "root", "");
        dbConn.resetTable();
        //dbConn.writePublicKey("server", rsa.getE().toString(), rsa.getN().toString());
        
        System.out.println("Socket ready for listening on port " + this.port + ".");
        System.out.println("END OF SERVER CONFIGURATION");
        System.out.println("***************************************");
    }
    
    private void establishConnection(SSLSocket socket) {
        System.out.println("***************************************");
        System.out.println("NEW SOCKET CONFIGURATION");
        System.out.println("New connection request: " + socket);
        
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Data I/O stream for the socket created.");
            
            String name = dis.readUTF();
            System.out.println("Creating an handler for \"" + name + "\"...");
            
            ClientHandler clienthandler = new ClientHandler(socket, name, dis, dos);
            Thread thread = new Thread(clienthandler);
            thread.start();
            clientList.add(clienthandler);
            System.out.println("Client added in the active client list.");
            System.out.println("END OF SOCKET CONFIGURATION");
            System.out.println("***************************************");
            
        } 
        catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ClientHandler implements Runnable 
{
    private final String name;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final SSLSocket socket;
    private boolean isloggedin;
    
    public ClientHandler(SSLSocket socket, String name, DataInputStream dis, DataOutputStream dos) 
    {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.socket = socket;
        this.isloggedin = true;
    }
    
    @Override
    public void run() 
    {
        System.out.println("SENDER\t | RECEIVER");
        while (true) {
            try {
                Message message = new Message();
                message.setSender(dis.readUTF());
                message.setReceiver(dis.readUTF());
                System.out.println(message.getSender() + "\t | " + message.getReceiver());
                
                if (message.getReceiver().equalsIgnoreCase("logout")) {
                    System.out.println("Closing " + this.socket + " by \"" + this.name + "\"...");
                    this.isloggedin = false;
                    break;
                }
                
                int length = dis.readInt();
                byte[] encryptedMessage = null;
                
                if (length > 0) {
                    encryptedMessage = new byte[length];
                    dis.readFully(encryptedMessage);
                    if (message.getReceiver().equalsIgnoreCase("all")) {
                        for (ClientHandler mc : ChatServer.clientList) {
                            if (mc.isloggedin) {
                                mc.dos.writeUTF(message.getSender());
                                mc.dos.writeUTF(message.getReceiver());
                                mc.dos.writeInt(encryptedMessage.length);
                                mc.dos.write(encryptedMessage);
                            }
                        }
                    }
                    else {
                        for (ClientHandler mc : ChatServer.clientList) {
                            if (mc.name.equalsIgnoreCase(message.getReceiver()) && mc.isloggedin) {
                                mc.dos.writeUTF(message.getSender());
                                mc.dos.writeUTF(message.getReceiver());
                                mc.dos.writeInt(encryptedMessage.length);
                                mc.dos.write(encryptedMessage);
                                break;
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        try {
            this.dos.close();
            this.dis.close();
            this.socket.close();
        } catch(IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} 




