package com.esprit.models;

import java.util.Date;

public class Reservations {
    private int id;
    private int utilisateur_id;
    private int voiture_id;
    private Date date_debut;
    private Date date_fin;
    private String statut;
    private Double prix_finale;
    private boolean conducteur_supplementaire;

    // 🔹 Constructeur principal
    public Reservations(int id, int utilisateur_id, int voiture_id, Date date_debut, Date date_fin, String statut, Double prix_finale, boolean conducteur_supplementaire) {
        this.id = id;
        this.utilisateur_id = utilisateur_id;
        this.voiture_id = voiture_id;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut = statut;
        this.prix_finale = prix_finale;
        this.conducteur_supplementaire = conducteur_supplementaire;
    }

    // 🔹 Constructeur sans ID (cas d'une nouvelle réservation)
    public Reservations(int utilisateur_id, int voiture_id, Date date_debut, Date date_fin, String statut, Double prix_finale, boolean conducteur_supplementaire) {
        this.utilisateur_id = utilisateur_id;
        this.voiture_id = voiture_id;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut = statut;
        this.prix_finale = prix_finale;
        this.conducteur_supplementaire = conducteur_supplementaire;
    }

    public Reservations(int id, int utilisateurId, int voitureId, java.sql.Date dateDebut, java.sql.Date dateFin, String statut, Double prixFinale) {
    }

    // 🔹 Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public int getVoiture_id() {
        return voiture_id;
    }

    public void setVoiture_id(int voiture_id) {
        this.voiture_id = voiture_id;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getPrix_finale() {
        return prix_finale;
    }

    public void setPrix_finale(Double prix_finale) {
        this.prix_finale = prix_finale;
    }

    public boolean isConducteur_supplementaire() {
        return conducteur_supplementaire;
    }

    public void setConducteur_supplementaire(boolean conducteur_supplementaire) {
        this.conducteur_supplementaire = conducteur_supplementaire;
    }

    @Override
    public String toString() {
        return "Reservations{" +
                "id=" + id +
                ", utilisateur_id=" + utilisateur_id +
                ", voiture_id=" + voiture_id +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", statut='" + statut + '\'' +
                ", prix_finale=" + prix_finale +
                ", conducteur_supplementaire=" + conducteur_supplementaire +
                '}';
    }

}
