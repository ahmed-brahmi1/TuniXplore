package com.esprit.services;

import com.esprit.models.Post;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.*;

public class PostService implements IService<Post> {

    private Connection connection = DataSource.getInstance().getConnection();


    public void ajouter(Post post) {
        String req = "INSERT INTO post(id_user,contenu,nb_likes,image_url,id_post) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, post.getId_user());
            pst.setString(2, post.getContenu());
            pst.setInt(3, post.getNb_likes());
            pst.setString(4, post.getImage());
            pst.setInt(5, post.getId_post());

            pst.executeUpdate();
            System.out.println("Post ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @Override
    public void modifier(Post post) {
        String req = "UPDATE post SET contenu=? WHERE id_post=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, post.getContenu());
            pst.setInt(2, post.getId_post());

            pst.executeUpdate();
            System.out.println(" le contenu du post modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifierLikes(int id) {



            String req = "UPDATE post SET nb_likes=nb_likes+1 WHERE id_post=?";
            try {
                PreparedStatement pst1 = connection.prepareStatement(req);

                pst1.setInt(1, id);

                pst1.executeUpdate();
                System.out.println(" les likes de post sont incrémenté");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }




    }
   @Override
    public void supprimer(int id ){
        String req = "DELETE from post WHERE id_post=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("post supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Map<Post,User> afficherPost() {
        Map<Post,User> post = new LinkedHashMap<>();

        String req = "SELECT post.*, user.nom, user.prenom , user.id , user.photo_profil FROM post JOIN user ON post.id_user = user.id ORDER BY date_creation DESC " ;

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);
            while (rs.next()) {
                int userId = rs.getInt("id");
                String userNom = rs.getString("nom");
                String userPrenom = rs.getString("prenom");
                String userPhoto = rs.getString("photo_profil");

                int postId = rs.getInt("id_post");
                String content = rs.getString("contenu");
                Timestamp dateCreation = rs.getTimestamp("date_creation");
                int nb_likes = rs.getInt("nb_likes");
                String postImage = rs.getString("image_url");



                // Créer l'utilisateur
                User userObj = new User(userId, userNom, userPrenom,userPhoto);

                // Créer le post
                Post postObj = new Post(postId,userId, content,nb_likes, dateCreation,postImage);
                post.put(postObj,userObj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return post;
    }

}







