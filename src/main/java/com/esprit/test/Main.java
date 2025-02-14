package com.esprit.test;


import com.esprit.utils.DataBase;


public class Main {
    public static void main(String[] args) {
        DataBase db = DataBase.getInstance();
        System.out.println(db.getConnection());





    }
}