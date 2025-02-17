package com.esprit.services.User;

import com.esprit.models.Parrainage;
import com.esprit.models.User;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceParrainage implements CrudService<Parrainage> {

    private Connection connection;

    public ServiceParrainage() {
        connection = DataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Parrainage parrainage) {
        String req = "INSERT INTO `parrainage`(`id_parrain`, `code_parrainage`) VALUES ("+parrainage.getId_sender()+",'"+parrainage.getCode()+"')";

        try {
            Statement statment=connection.createStatement();
            statment.executeUpdate(req);
            System.out.println("ajout de parrainage avec succes!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Parrainage parrainage) {
        // Exemple de modification : mettre à jour les points d'un parrainage (si besoin)
        String req = "UPDATE `parrainage` SET `id_parrain`=?,`code_parrainage`=? WHERE id=?";

        try {
            PreparedStatement prep=connection.prepareStatement(req);
            prep.setInt(3, parrainage.getId());

            prep.setInt(1, parrainage.getId_sender());
            prep.setString(2, parrainage.getCode());

            prep.executeUpdate();

            System.out.println("modification de parrainage avec succes!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM `parrainage` WHERE `id` = ?";

        try {
            PreparedStatement prep=connection.prepareStatement(req);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("supprimer parrainage avec succes!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());        }
    }

    @Override
    public List<Parrainage> afficher() {

        List<Parrainage> parrainages=new ArrayList<>();
        String req="SELECT * FROM `parrainage`";
        try {
            Statement statment=connection.createStatement();
            ResultSet rs=statment.executeQuery(req);

            while (rs.next()) {
                parrainages.add(new Parrainage(rs.getInt("id"),rs.getInt("id_parrain"),rs.getString("code_parrainage")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());        }
        return parrainages;
    }

    @Override
    public void ajouterParAdmin(Parrainage parrainage) {
        // Fonction similaire à ajouter(), mais spécifique à l'ajout par l'admin, si nécessaire
        ajouter(parrainage);
    }
}
