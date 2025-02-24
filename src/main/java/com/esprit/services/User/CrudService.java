package com.esprit.services.User;

import com.esprit.models.User;

import java.util.List;

public interface CrudService<T> {
    void ajouter(T t);

    void modifier(T t);

    void supprimer(int id);

    List<T> afficher();

    void ajouterParAdmin(T t);

}
