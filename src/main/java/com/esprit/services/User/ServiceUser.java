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
        String req = "INSERT INTO `user`(`nom`, `prenom`, `age`, `genre`, `num_tel`, `email`, `mdp`, `role`) " +
                "VALUES ('" + user.getNom() + "','" + user.getPrenom() + "'," + user.getAge() + ",'" +
                user.getGenre() + "'," + user.getTel() + ",'" + user.getEmail() + "','" +
                user.getMdp() + "','voyageur')";

        try {
            // ✅ Crée un Statement classique
            Statement statement = connection.createStatement();

            // ✅ Exécute la requête avec RETURN_GENERATED_KEYS
            statement.executeUpdate(req, Statement.RETURN_GENERATED_KEYS);
            System.out.println("ajout de l'utilisateur avec succes!");

            // ✅ Récupère l'ID généré automatiquement
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));  // ⚠️ Met à jour l'ID de l'utilisateur
                System.out.println("ID utilisateur généré : " + user.getId());
            } else {
                System.out.println("Échec de récupération de l'ID généré.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
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

    public void ajouterParAdmin(User user) {
        String req = "INSERT INTO `user`(`nom`, `prenom`, `age`, `genre`, `num_tel`, `email`, `mdp`, `role`) " +
                "VALUES ('" + user.getNom() + "','" + user.getPrenom() + "'," + user.getAge() + ",'" +
                user.getGenre() + "'," + user.getTel() + ",'" + user.getEmail() + "','" +
                user.getMdp() + "','" + user.getRole() + "')";

        try {
            // ✅ Création du Statement avec récupération des clés
            Statement statement = connection.createStatement();
            statement.executeUpdate(req, Statement.RETURN_GENERATED_KEYS);

            // ✅ Récupération de l'ID généré automatiquement
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                user.setId(userId);
                System.out.println("ID de l'utilisateur ajouté : " + userId);


            }
            statement.close();

            System.out.println("Ajout de l'utilisateur avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }




    public User authenticate(String email, String password) {
        String req = "SELECT * FROM `user` WHERE `email` = ? AND `mdp` = ?";
        try {
            PreparedStatement prep = connection.prepareStatement(req);
            prep.setString(1, email);
            prep.setString(2, password);
            ResultSet rs = prep.executeQuery();

            if (rs.next()) {
                // Utilisateur trouvé, retourner l'objet User
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age"),
                        rs.getString("genre"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role"),
                        rs.getInt("num_tel")
                );
            } else {
                // Aucun utilisateur trouvé
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT id, nom, prenom FROM user";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                userList.add(new User(id, nom, prenom));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
