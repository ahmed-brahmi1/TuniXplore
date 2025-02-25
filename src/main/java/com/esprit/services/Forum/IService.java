package com.esprit.services;

import java.util.List;

public interface IService<T> {

   // int ajouter(T t);
    void modifier(T t);
    void supprimer(int id);
    //List<T> afficherPost();
}
