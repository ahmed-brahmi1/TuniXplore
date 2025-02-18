package com.esprit.models;

public class Discussion {
    private int idDiscussion;
    private int idUser1;
    private int idUser2;

    public Discussion(int idDiscussion, int idUser1, int idUser2) {
        this.idDiscussion = idDiscussion;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
    }

    public int getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(int idUser1) {
        this.idUser1 = idUser1;
    }

    public int getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(int idUser2) {
        this.idUser2 = idUser2;
    }

    @Override
    public String toString() {
        return "Discussion{" +
                "idDiscussion=" + idDiscussion +
                ", idUser1=" + idUser1 +
                ", idUser2=" + idUser2 +
                '}';
    }
}
