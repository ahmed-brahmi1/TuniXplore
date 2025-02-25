package com.esprit.services;

import com.esprit.models.Commentaire;
import com.esprit.models.Post;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CommentaireService implements IService<Commentaire> {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(Commentaire com) {
        String req = "INSERT INTO commentaire (id_user,contenu,id_post) VALUES (?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, com.getId_user());
            pst.setString(2, com.getCommentaire());

            pst.setInt(3, com.getId_post());
            pst.executeUpdate();
            System.out.println("commentaire ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Commentaire com) {
        String req = "UPDATE commentaire SET contenu=? WHERE id_commentaire=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, com.getCommentaire());
            pst.setInt(2, com.getId_commentaire());

            pst.executeUpdate();
            System.out.println(" le contenu du commentaire est  modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE from commentaire WHERE id_commentaire=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("commentaire supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<Commentaire, User> afficherCommentaire(int idPost) {
        Map<Commentaire,User> comMap = new HashMap<>();

        String req = "SELECT commentaire.contenu, commentaire.date_creation, user.nom, user.prenom " +
                "FROM commentaire " +
                "JOIN user ON commentaire.id_user = user.id " +
                "WHERE commentaire.id_post = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, idPost);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
               //int userId = rs.getInt("id");
                String userNom = rs.getString("nom");
                String userPrenom = rs.getString("prenom");

               // int postId = rs.getInt("id_post");
                String content = rs.getString("contenu");
                Timestamp dateCreation = rs.getTimestamp("date_creation");



                // Créer l'utilisateur
                User userObj = new User(0, userNom, userPrenom,"");

                // Créer le post
                Commentaire commentaire = new Commentaire(0,0,0,content,dateCreation);
                comMap.put(commentaire,userObj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return comMap;
    }
}
