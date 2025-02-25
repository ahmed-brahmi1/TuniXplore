package com.esprit.models;

import java.sql.Timestamp;

public class Message {
    int idMessage;
    int id_discussion;
    String contenu;
    int id_sender;
    private String date_creation;


    public Message(int idMessage,int id_discussion, String contenu, int id_sender, String date_creation) {
        this.idMessage = idMessage;
        this.contenu = contenu;
        this.id_sender = id_sender;
        this.date_creation = date_creation;
        this.id_discussion = id_discussion;
    }

    public int getId_discussion() {
        return id_discussion;
    }

    public void setId_discussion(int id_discussion) {
        this.id_discussion = id_discussion;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public int getId_sender() {
        return id_sender;
    }

    public void setId_sender(int id_sender) {
        this.id_sender = id_sender;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "idMessage=" + idMessage +
                ", contenu='" + contenu + '\'' +
                ", id_sender=" + id_sender +
                ", date_creation=" + date_creation +
                ", id_discussion=" + id_discussion +
                '}';
    }
}
