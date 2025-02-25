package com.esprit.models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class Post {
    private int id_post;
    private int id_user;
    private String contenu;
    private int nb_likes;
    private String image;

    private Timestamp date_creation;

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getNb_likes() {
        return nb_likes;
    }

    public void setNb_likes(int nb_likes) {
        this.nb_likes = nb_likes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(Timestamp date_creation) {
        this.date_creation = date_creation;
    }

    public Post(int id_post, int id_user, String contenu, int nb_likes,Timestamp date_creation , String image) {
        this.id_post = id_post;
        this.id_user = id_user;
        this.contenu = contenu;
        this.nb_likes = nb_likes;

        this.date_creation = date_creation;
        this.image = image;
    }
    public Post() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id_post == post.id_post;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_post);
    }

    @Override
    public String toString() {
        return "id_post : " + id_post + " \n contenu : " + contenu + "\n date création : " + date_creation + "\n nb_likes : " + nb_likes ;
    }

}
