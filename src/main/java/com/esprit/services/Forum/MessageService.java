package com.esprit.services;

import com.esprit.models.Message;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.*;

public class MessageService implements IService<Message> {
    private Connection connection = DataSource.getInstance().getConnection();

     public void ajouter(Message m) {

        String id_message = "SELECT Max(id_message) from message WHERE id_discussion=? " ;
        String req = "INSERT INTO message (id_sender,id_discussion,contenu) VALUES (?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, m.getId_sender());
            pst.setInt(2, m.getId_discussion());
            pst.setString(3, m.getContenu());
            pst.executeUpdate();
            System.out.println("message ajouté");

            // Récupérer le max(id_discussion) existant
            PreparedStatement stmt = connection.prepareStatement(id_message);
            stmt.setInt(1, m.getId_discussion());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                 m.setIdMessage(rs.getInt(1)); // Incrémentation
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void modifier(Message m) {
        String req = "UPDATE message SET contenu=? WHERE id_message=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, m.getContenu());
            pst.setInt(2, m.getIdMessage());

            pst.executeUpdate();
            System.out.println(" message modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE from message WHERE id_message=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("message supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Message> afficherMessage(int id_discussion) {
        List<Message> messages = new ArrayList<>();

        String req = "SELECT contenu,id_sender from message WHERE id_discussion=?" ;

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id_discussion);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id_sender = rs.getInt("id_sender");
                String content = rs.getString("contenu");





                // Créer le post
                Message message = new Message(0,0, content,id_sender,null);
             messages.add(message);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

}
