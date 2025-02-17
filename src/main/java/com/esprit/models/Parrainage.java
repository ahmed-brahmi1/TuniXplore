package com.esprit.models;

public class Parrainage {
    private int id;
    private int id_sender;
    private String code;

    public Parrainage() {

    }
    public Parrainage(int id,int id_sender,String code ) {
        this.id = id;
        this.code = code;

        this.id_sender = id_sender;
    }
    public Parrainage(int id_sender,String code ) {
        this.code = code;

        this.id_sender = id_sender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_sender() {
        return id_sender;
    }

    public void setId_sender(int id_sender) {
        this.id_sender = id_sender;
    }

    @Override
    public String toString() {
        return "Parrainage{" +
                "code='" + code + '\'' +
                ", id=" + id +
                ", id_sender=" + id_sender +
                '}';
    }
}

