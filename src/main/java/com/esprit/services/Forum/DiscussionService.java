package com.esprit.services;

import com.esprit.models.Discussion;
import com.esprit.models.Message;
import com.esprit.models.Post;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DiscussionService implements IService<Discussion> {
    private Connection connection = DataSource.getInstance().getConnection();
    private final User AUTHUSER = new User(1,"laatig","amen","assets/aziz.png");

    public int ajouter(Discussion d) {
        int newId = 1; // Valeur par défaut au cas où la table est vide
        String selectMaxId = "SELECT MAX(id_discussion) FROM discussion";
        String insertQuery = "INSERT INTO discussion ( id_user1, id_user2) VALUES ( ?, ?)";

        try {

            // Insérer la nouvelle discussion avec l'ID calculé
            PreparedStatement pst = connection.prepareStatement(insertQuery);

            pst.setInt(1, d.getIdUser1());
            pst.setInt(2, d.getIdUser2());
            pst.executeUpdate();

            System.out.println("Discussion ajoutée avec ID : " + newId);

            // Récupérer le max(id_discussion) existant
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectMaxId);
            if (rs.next() && rs.getInt(1) > 0) {
                newId = rs.getInt(1); // Incrémentation
            }
            return newId; // Retourner l'ID de la discussion ajoutée

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la discussion : " + e.getMessage());
            return -1; // Retourner -1 en cas d'erreur
        }
    }

    @Override
    public void modifier(Discussion discussion) {

    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE from discussion WHERE id_discussion=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("discussion supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<Message, User> afficherDiscussion() {
        int authUser=AUTHUSER.getId();
        Map<Message,User> discussions = new HashMap<>();

        String req = "SELECT " +
                "    c.id_discussion AS discussion_id, " +
                "    u.nom, " +
                "u.prenom , "+
                "    u.photo_profil, " +
                "    m.contenu AS last_message, " +
                "    CASE " +
                "        WHEN DATE(m.date_creation) = CURDATE() " +
                "        THEN DATE_FORMAT(m.date_creation, '%H:%i') " +
                "        WHEN YEAR(m.date_creation) = YEAR(CURDATE()) " +
                "        THEN DATE_FORMAT(m.date_creation, '%d %b %H:%i') " +
                "        ELSE DATE_FORMAT(m.date_creation, '%d %b %Y %H:%i') " +
                "    END AS last_message_time " +
                "FROM discussion c " +
                "JOIN user u " +
                "    ON (CASE " +
                "            WHEN c.id_user1 = ? THEN c.id_user2 " +
                "            ELSE c.id_user1 " +
                "        END) = u.id " +
                " JOIN message m " +
                "    ON m.id_discussion = c.id_discussion " +
                "    AND m.date_creation = ( " +
                "        SELECT MAX(m2.date_creation) " +
                "        FROM message m2 " +
                "        WHERE m2.id_discussion = c.id_discussion " +
                "    ) " +
                "WHERE c.id_user1 = ? " +
                "   OR c.id_user2 = ? " +
                "ORDER BY m.date_creation DESC;";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1,authUser);
            pst.setInt(2,authUser);
            pst.setInt(3,authUser);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String userNom = rs.getString("nom");
                String userPrenom = rs.getString("prenom");
                String userPhoto = rs.getString("photo_profil");

                int idDiscussion = rs.getInt("discussion_id");
                String content = rs.getString("last_message");
                String dateCreation = rs.getString("last_message_time");



                // Créer l'utilisateur
                User userObj = new User(0, userNom, userPrenom,userPhoto);

                // Créer le post
                Message discussion = new Message(0,idDiscussion,content,0,dateCreation) ;
                discussions.put(discussion,userObj);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return discussions;
    }

   public int IdDiscussion(int idUser){
        int authUser=AUTHUSER.getId();
        int idDiscussion = 0; // Valeur par défaut si aucune discussion n'est trouvée
        String query = "SELECT id_discussion FROM discussion WHERE " +
                "(id_user1 = ? AND id_user2 = ?) OR (id_user1 = ? AND id_user2 = ?)";

        try
            {
                PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idUser);
            stmt.setInt(2, authUser);
            stmt.setInt(3, authUser);
            stmt.setInt(4, idUser);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idDiscussion = rs.getInt("id_discussion");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Tu peux logger l’erreur ou la gérer autrement
        }

        return idDiscussion;


    }



}
