package com.esprit.models;

import java.sql.Timestamp;

public class Commentaire {
    private int id_commentaire;
    private int id_user;
    private int id_post ;
    private String commentaire;
    private Timestamp date_commentaire;

    public Commentaire(int id_commentaire,int id_post, int id_user, String commentaire, Timestamp date_commentaire) {
        this.id_commentaire = id_commentaire;
        this.id_user = id_user;
        this.id_post = id_post;
        this.commentaire = commentaire;
        this.date_commentaire = date_commentaire;
    }
    public Commentaire() {}

    public int getId_commentaire() {
        return id_commentaire;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public void setId_commentaire(int id_commentaire) {
        this.id_commentaire = id_commentaire;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Timestamp getDate_commentaire() {
        return date_commentaire;
    }

    public void setDate_commentaire(Timestamp date_commentaire) {
        this.date_commentaire = date_commentaire;
    }

    @Override
    public String toString() {
        return  "\n contenu :  " + commentaire + "\n date_commentaire : "+ date_commentaire ;
    }
}
