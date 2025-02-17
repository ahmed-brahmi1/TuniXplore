package com.esprit.services.User;

import java.util.List;

public interface CrudParrainage<T> {
    void ajouter(T t);

    void modifier(T t);

    void supprimer(int id);

    List<T> afficher();

}
