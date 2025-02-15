package com.esprit.controllers.Voiture;

import com.esprit.models.Voiture;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.temporal.ChronoUnit;

public class ReservationFormController {

    @FXML private TextField marqueField, modeleField, idField, statutField, prixField, nomClientField;
    @FXML private DatePicker dateDebutPicker, dateFinPicker;
    @FXML private Button confirmerButton, annulerButton;
    @FXML private Label prixFinalLabel;
    @FXML private CheckBox conducteurCheckBox;


    private Voiture voitureSelectionnee;

    @FXML
    public void initialize() {
        // Ajoute des événements pour recalculer le prix dès que l'utilisateur choisit une date
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

        if (dateDebutPicker.getValue() != null && dateFinPicker.getValue() != null) {
            long jours = ChronoUnit.DAYS.between(dateDebutPicker.getValue(), dateFinPicker.getValue());

            if (jours > 0) {
                double prixBase = jours * voitureSelectionnee.getPrix_par_jour();
                double prixConducteur = conducteurCheckBox.isSelected() ? jours * voitureSelectionnee.getConducteurSupplementaire() : 0;

                double prixFinal = prixBase + prixConducteur;

                prixFinalLabel.setText("Prix Final: " + prixFinal + " TND");
            } else {
                prixFinalLabel.setText("Sélectionner des dates valides !");
            }
        } else {
            prixFinalLabel.setText("Veuillez sélectionner les dates.");
        }
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

}
