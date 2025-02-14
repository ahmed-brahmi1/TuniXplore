package com.esprit.services.User;

import com.esprit.models.User;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements CrudService<User>{

    private Connection connection;
    public ServiceUser() {
        connection= DataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter(User user) {
        String req="INSERT INTO `user`( `nom`, `prenom`, `age`, `genre`, `num_tel`, `email`, `mdp`, `role`) VALUES ('"+user.getNom()+"','"+user.getPrenom()+"',"+user.getAge()+",'"+user.getGenre()+"',"+user.getTel()+",'"+user.getEmail()+"','"+user.getMdp()+"','voyageur')";
        try {
            Statement statment=connection.createStatement();
            statment.executeUpdate(req);
            System.out.println("ajout de l'utilisateur avec succes!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void modifier(User user) {
        String req="UPDATE `user` SET `nom`=?,`prenom`=?,`age`=?,`genre`=?,`num_tel`=?,`email`=?,`mdp`=?,`role`=? WHERE id=?";
        try {
            PreparedStatement prep=connection.prepareStatement(req);
            prep.setInt(9, user.getId());

            prep.setString(1, user.getNom());
            prep.setString(2, user.getPrenom());
            prep.setInt(3, user.getAge());
            prep.setString(4, user.getGenre());
            prep.setInt(5, user.getTel());
            prep.setString(6, user.getEmail());
            prep.setString(7, user.getMdp());
            prep.setString(8, user.getRole());
            prep.executeUpdate();

            System.out.println("modification de l'utilisateur avec succes!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());        }
    }

    @Override
    public void supprimer(int id) {
        String req="DELETE FROM `user` WHERE id=?";

        try {
            PreparedStatement prep=connection.prepareStatement(req);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("supprimer l'utilisateur avec succes!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());        }


    }

    @Override
    public List<User> afficher() {
        List<User> users=new ArrayList<>();
        String req="SELECT * FROM `user`";
        try {
            Statement statment=connection.createStatement();
            ResultSet rs=statment.executeQuery(req);

            while (rs.next()) {
                users.add(new User(rs.getInt("id"),rs.getString("nom"),rs.getString("prenom"),rs.getInt("age"),rs.getString("genre"),rs.getString("email"),rs.getString("mdp"),rs.getString("role"),rs.getInt("num_tel")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());        }
        return users;
    }
}
