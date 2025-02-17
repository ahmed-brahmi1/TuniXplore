package com.esprit.test;


import com.esprit.utils.DataBase;

import com.esprit.models.Parrainage;
import com.esprit.services.User.ServiceParrainage;

import com.esprit.services.User.ServiceParrainage;
public class Main {
    public static void main(String[] args) {
        DataBase db = DataBase.getInstance();
        System.out.println(db.getConnection());
        ServiceParrainage sp = new ServiceParrainage();
        //sp.ajouter(new Parrainage(3, "Groun"));

        //sp.modifier(new Parrainage(2,3, "Grounnnn"));
        //sp.supprimer(2);

        System.out.println(sp.afficher());






    }
}