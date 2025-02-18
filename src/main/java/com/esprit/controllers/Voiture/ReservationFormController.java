package com.esprit.controllers.Voiture;

import com.esprit.models.Promotions;
import com.esprit.models.Voiture;
import com.esprit.utils.DataBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationFormController {

    @FXML private TextField marqueField, modeleField, idField, statutField, prixField, nomClientField;
    @FXML private DatePicker dateDebutPicker, dateFinPicker;
    @FXML private Button confirmerButton, annulerButton;
    @FXML private Label prixFinalLabel;
    @FXML private CheckBox conducteurCheckBox;

    private Connection connection;

    public ReservationFormController() {
        connection = DataBase.getInstance().getConnection();
    }

    private Voiture voitureSelectionnee;

    @FXML
    public void initialize() {
        dateDebutPicker.setOnAction(event -> calculerPrixFinal());
        dateFinPicker.setOnAction(event -> calculerPrixFinal());
        conducteurCheckBox.setOnAction(event -> calculerPrixFinal());
    }

    public void setVoitureDetails(Voiture voiture) {
        this.voitureSelectionnee = voiture;
        marqueField.setText(voiture.getMarque());
        modeleField.setText(voiture.getModele());
        idField.setText(String.valueOf(voiture.getId()));
        statutField.setText(voiture.getStatut());
        prixField.setText(voiture.getPrix_par_jour() + " TND/Jour");
    }

    private void calculerPrixFinal() {
        if (voitureSelectionnee == null) {
            prixFinalLabel.setText("Erreur : voiture non définie !");
            return;
        }

        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (dateDebut != null && dateFin != null) {
            long jours = ChronoUnit.DAYS.between(dateDebut, dateFin);

            if (jours > 0) {
                double prixBase = jours * voitureSelectionnee.getPrix_par_jour();
                double prixConducteur = conducteurCheckBox.isSelected() ? jours * voitureSelectionnee.getConducteurSupplementaire() : 0;

                Promotions promo = getPromotionForVoiture(voitureSelectionnee.getId());
                double reduction = (promo != null) ? promo.getReduction() / 100.0 : 0.0;

                double prixFinal = (prixBase + prixConducteur) * (1 - reduction);

                if (promo != null) {
                    prixFinalLabel.setText(String.format("Prix Final: %.2f TND (-%.0f%% promo)", prixFinal, promo.getReduction()));
                } else {
                    prixFinalLabel.setText(String.format("Prix Final: %.2f TND", prixFinal));
                }
            } else {
                prixFinalLabel.setText("Sélectionner des dates valides !");
            }
        } else {
            prixFinalLabel.setText("Veuillez sélectionner les dates.");
        }
    }

    private Promotions getPromotionForVoiture(int voitureId) {
        Promotions promo = null;
        String query = "SELECT reduction FROM promotions WHERE voiture_id = ? AND NOW() BETWEEN date_debut AND date_fin";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, voitureId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                promo = new Promotions();
                promo.setReduction(rs.getDouble("reduction"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return promo;
    }

    @FXML
    public void annulerReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewVoiture/voiture/EspaceClientVoiture/Home_Client.fxml"));
            Parent homePage = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homePage));
            stage.setTitle("Espace Client");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void enregistrerReservation() {
        try {
            int utilisateur_id = 1;
            int voiture_id = Integer.parseInt(idField.getText());
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String statut = "En attente";

            if (nomClientField.getText().isEmpty() || dateDebut == null || dateFin == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
                return;
            }

            long daysBetween = ChronoUnit.DAYS.between(dateDebut, dateFin);
            if (daysBetween <= 0) {
                showAlert("Erreur", "La date de fin doit être après la date de début !");
                return;
            }

            String prixText = prixFinalLabel.getText().replaceAll("[^0-9.,]", "").trim();
            if (prixText.contains(",")) {
                prixText = prixText.replace(",", ".");
            }

            double prixFinale;
            try {
                prixFinale = Double.parseDouble(prixText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le prix final est invalide !");
                return;
            }

            boolean conducteurSupplementaire = conducteurCheckBox.isSelected();

            String query = "INSERT INTO reservations (utilisateur_id, voiture_id, date_debut, date_fin, statut, prix_finale, conducteur_supplementaire) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, utilisateur_id);
            preparedStatement.setInt(2, voiture_id);
            preparedStatement.setDate(3, Date.valueOf(dateDebut));
            preparedStatement.setDate(4, Date.valueOf(dateFin));
            preparedStatement.setString(5, statut);
            preparedStatement.setDouble(6, prixFinale);
            preparedStatement.setBoolean(7, conducteurSupplementaire);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Succès", "Réservation enregistrée avec succès !");
                retournerAListeVoitures();
            } else {
                showAlert("Erreur", "Échec de l'enregistrement !");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Une erreur est survenue lors de l'enregistrement !");
        }
    }

    private void retournerAListeVoitures() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewVoiture/voiture/EspaceClientVoiture/Home_Client.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) confirmerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page des voitures !");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
