/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

/**
 *
 * @author matte
 */
public class Message {
    private String sender;
    private String receiver;
    private String message;
    
    public Message() {
        this.sender = "";
        this.receiver = "";
        this.message = "";
    }
    
    public Message(String message) {
        this.sender = "";
        this.receiver = "";
        this.message = message;
    }
    
    public Message(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
