package com.esprit.services.services_voiture;

import com.esprit.models.Voiture;
import com.esprit.utils.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ServiceVoiture {
    private final Connection connection;

    public ServiceVoiture() {
        connection = DataBase.getInstance().getConnection();
    }

    // ✅ Ajouter une voiture avec image et récupérer l'ID auto-incrémenté
    public int ajouter(Voiture voiture) {
        String req = "INSERT INTO voitures (marque, modele, annee, prix_par_jour, statut, image_path, conducteur_supplementaire) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, voiture.getMarque());
            ps.setString(2, voiture.getModele());
            ps.setInt(3, voiture.getAnnee());
            ps.setDouble(4, voiture.getPrix_par_jour());
            ps.setString(5, voiture.getStatut());
            ps.setString(6, voiture.getImagePath());
            ps.setDouble(7, voiture.getConducteurSupplementaire());
            ps.executeUpdate();

            // Récupérer l'ID auto-incrémenté
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                System.out.println("🚗 Voiture ajoutée avec ID: " + generatedId);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout: " + e.getMessage());
        }
        return generatedId;
    }

    // ✅ Modifier une voiture existante (avec mise à jour de l'image)
    public boolean modifier(Voiture voiture) {
        String req = "UPDATE voitures SET marque=?, modele=?, annee=?, prix_par_jour=?, statut=?, image_path=?,conducteur_supplementaire=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, voiture.getMarque());
            ps.setString(2, voiture.getModele());
            ps.setInt(3, voiture.getAnnee());
            ps.setDouble(4, voiture.getPrix_par_jour());
            ps.setString(5, voiture.getStatut());
            ps.setString(6, voiture.getImagePath());
            ps.setDouble(7, voiture.getConducteurSupplementaire());// ✅ Mise à jour de l'image
            ps.setInt(8, voiture.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification: " + e.getMessage());
            return false;
        }
    }

    // ✅ Supprimer une voiture
    public boolean supprimer(int id) {
        String req = "DELETE FROM voitures WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    // ✅ Récupérer toutes les voitures sous forme d'ObservableList (avec image)
    public ObservableList<Voiture> recupererToutes() {
        ObservableList<Voiture> voitures = FXCollections.observableArrayList();
        String req = "SELECT id, marque, modele, annee, prix_par_jour, statut, image_path, conducteur_supplementaire FROM voitures";

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                voitures.add(new Voiture(
                        rs.getInt("id"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee"),
                        rs.getDouble("prix_par_jour"),
                        rs.getString("statut"),
                        rs.getString("image_path"),
                        rs.getDouble("conducteur_supplementaire")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des voitures: " + e.getMessage());
        }
        return voitures;
    }
}