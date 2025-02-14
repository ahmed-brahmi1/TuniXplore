package com.esprit.models;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private int age;
    private int tel;
    private String email;
    private String mdp;
    private String role;
    private String genre;


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getMdp() {
        return mdp;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRole() {
        return role;
    }

    public int getTel() {
        return tel;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public User() {
    }

    public User(String nom,String prenom,int age,String genre, String email, String mdp, String role, int tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
        this.mdp = mdp;
        this.genre = genre;
        this.role = role;
        this.tel = tel;
    }
    public User(int id,String nom,String prenom,int age,String genre, String email, String mdp, String role, int tel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
        this.mdp = mdp;
        this.genre = genre;
        this.role = role;
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel=" + tel +
                ", email='" + email + '\'' +
                ", mdp='" + mdp + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
