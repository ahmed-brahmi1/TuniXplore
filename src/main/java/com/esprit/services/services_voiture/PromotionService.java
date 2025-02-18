package com.esprit.services.services_voiture;

import com.esprit.models.Promotions;
import com.esprit.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {
    private Connection connection;

    public PromotionService() {
        connection = DataBase.getInstance().getConnection();
    }

    public boolean ajouterPromotion(Promotions promo) {
        String query = "INSERT INTO promotions (voiture_id, reduction, date_debut, date_fin) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, promo.getVoiture_id());
            pstmt.setDouble(2, promo.getReduction());
            pstmt.setDate(3, new java.sql.Date(promo.getDate_debut().getTime()));
            pstmt.setDate(4, new java.sql.Date(promo.getDate_fin().getTime()));

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la promotion : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerPromotion(int id) {
        String query = "DELETE FROM promotions WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Promotions> getAllPromotions() {
        List<Promotions> promotionsList = new ArrayList<>();
        String query = "SELECT * FROM promotions";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Promotions promotion = new Promotions(
                        rs.getInt("id"),
                        rs.getInt("voiture_id"),
                        rs.getDouble("reduction"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin")
                );
                promotionsList.add(promotion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotionsList;
    }

    public List<Integer> getAllVoitureIds() {
        List<Integer> voitureIds = new ArrayList<>();
        String query = "SELECT id FROM voitures";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                voitureIds.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voitureIds;
    }


}
