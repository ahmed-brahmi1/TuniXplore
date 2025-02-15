package com.esprit.models;

import javafx.beans.property.*;

public class Voiture {
    private final IntegerProperty id;
    private final StringProperty marque;
    private final StringProperty modele;
    private final IntegerProperty annee;
    private final DoubleProperty prix_par_jour;
    private final StringProperty statut;
    private final StringProperty imagePath; // ✅ Propriété pour le chemin de l’image
    private final DoubleProperty conducteurSupplementaire;

    // ✅ Constructeur sans ID (Ajout d'une nouvelle voiture)
    public Voiture(String marque, String modele, int annee, double prix_par_jour, String statut, String imagePath,double conducteurSupplementaire) {
        this.id = new SimpleIntegerProperty(0); // ID temporaire
        this.marque = new SimpleStringProperty(marque);
        this.modele = new SimpleStringProperty(modele);
        this.annee = new SimpleIntegerProperty(annee);
        this.prix_par_jour = new SimpleDoubleProperty(prix_par_jour);
        this.statut = new SimpleStringProperty(statut);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.conducteurSupplementaire = new SimpleDoubleProperty(conducteurSupplementaire);
    }

    // ✅ Constructeur avec ID (Mise à jour ou affichage depuis la BD)
    public Voiture(int id, String marque, String modele, int annee, double prix_par_jour, String statut, String imagePath,double conducteurSupplementaire) {
        this.id = new SimpleIntegerProperty(id);
        this.marque = new SimpleStringProperty(marque);
        this.modele = new SimpleStringProperty(modele);
        this.annee = new SimpleIntegerProperty(annee);
        this.prix_par_jour = new SimpleDoubleProperty(prix_par_jour);
        this.statut = new SimpleStringProperty(statut);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.conducteurSupplementaire = new SimpleDoubleProperty(conducteurSupplementaire);
    }

    // ✅ Getters pour JavaFX (Propriétés observables)
    public IntegerProperty idProperty() { return id; }
    public StringProperty marqueProperty() { return marque; }
    public StringProperty modeleProperty() { return modele; }
    public IntegerProperty anneeProperty() { return annee; }
    public DoubleProperty prix_par_jourProperty() { return prix_par_jour; }
    public StringProperty statutProperty() { return statut; }
    public StringProperty imagePathProperty() { return imagePath; }
    public DoubleProperty conducteurSupplementaireProperty() { return conducteurSupplementaire; }// ✅ Observable pour l’image

    // ✅ Getters classiques
    public int getId() { return id.get(); }
    public String getMarque() { return marque.get(); }
    public String getModele() { return modele.get(); }
    public int getAnnee() { return annee.get(); }
    public double getPrix_par_jour() { return prix_par_jour.get(); }
    public String getStatut() { return statut.get(); }
    public String getImagePath() { return imagePath.get(); } // ✅ Getter pour l’image
    public Double getConducteurSupplementaire() { return conducteurSupplementaire.get(); }

    // ✅ Setters
    public void setId(int id) { this.id.set(id); }
    public void setMarque(String marque) { this.marque.set(marque); }
    public void setModele(String modele) { this.modele.set(modele); }
    public void setAnnee(int annee) { this.annee.set(annee); }
    public void setPrix_par_jour(double prix_par_jour) { this.prix_par_jour.set(prix_par_jour); }
    public void setStatut(String statut) { this.statut.set(statut); }
    public void setImagePath(String imagePath) { this.imagePath.set(imagePath); } // ✅ Setter pour l’image
    public void setConducteurSupplementaire(double conducteurSupplementaire) { this.conducteurSupplementaire.set(conducteurSupplementaire); }

    // ✅ toString() pour affichage
    @Override
    public String toString() {
        return "Voiture{" +
                "id=" + getId() +
                ", marque='" + getMarque() + '\'' +
                ", modele='" + getModele() + '\'' +
                ", annee=" + getAnnee() +
                ", prix_par_jour=" + getPrix_par_jour() +
                ", statut='" + getStatut() + '\'' +
                ", imagePath='" + getImagePath() + '\'' + // ✅ Affichage de l’image
                ", conducteursupplementaire='" + getConducteurSupplementaire() + '\'' +
                '}';
    }
}