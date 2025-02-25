package com.esprit.models;

import java.util.Objects;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String photo_profil;

    public User(int id, String nom, String prenom, String photo_profil) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.photo_profil =photo_profil;

    }
    public User(){}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPhoto_profil() {
        return photo_profil;
    }

    public void setPhoto_profil(String photo_profil) {
        this.photo_profil = photo_profil;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return " \n id_user : "+ id + " \n nom_user : "+ nom + " \n prenom_user : "+ prenom;
    }
}
