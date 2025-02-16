package com.example.voiture.models;

import java.util.Date;

public class Promotions {
    private int id;
    private int voiture_id;
    private double reduction;
    private Date date_debut;
    private Date date_fin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoiture_id() {
        return voiture_id;
    }

    public void setVoiture_id(int voiture_id) {
        this.voiture_id = voiture_id;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
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

    public Promotions(int id, int voiture_id, double reduction, Date date_debut, Date date_fin) {
        this.id = id;
        this.voiture_id = voiture_id;
        this.reduction = reduction;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    public Promotions(int voiture_id, double reduction, Date date_debut, Date date_fin) {
        this.voiture_id = voiture_id;
        this.reduction = reduction;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    @Override
    public String toString() {
        return "Promotions{" +
                "id=" + id +
                ", voiture_id=" + voiture_id +
                ", reduction=" + reduction +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                '}';
    }
}
