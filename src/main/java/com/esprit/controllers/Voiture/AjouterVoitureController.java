package com.esprit.controllers.Voiture;

import com.esprit.models.Voiture;
import com.esprit.services.services_voiture.ServiceVoiture;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AjouterVoitureController {

    @FXML
    private Button ajouterButton, annulerButton, voirListeButton;

    @FXML
    private void handleMouseEnteredAjouter() {
        ajouterButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
    }

    @FXML
    private void handleMouseExitedAjouter() {
        ajouterButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
    }

    @FXML
    private void handleMouseEnteredAnnuler() {
        annulerButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
    }

    @FXML
    private void handleMouseExitedAnnuler() {
        annulerButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
    }

    @FXML
    private void handleMouseEnteredVoirListe() {
        voirListeButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
    }

    @FXML
    private void handleMouseExitedVoirListe() {
        voirListeButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
    }

    @FXML
    private TextField marqueField, modeleField, anneeField, prixField, conducteurSupplementaireField;
    @FXML
    private ComboBox<String> statutComboBox;
    @FXML
    private ImageView voitureImageView;

    private final ServiceVoiture serviceVoiture = new ServiceVoiture();
    private Voiture voitureActuelle = null; // Stocke la voiture en cours de modification
    private String imagePath = null; // Stocke le chemin de l’image sélectionnée

    @FXML
    public void initialize() {
        statutComboBox.getItems().addAll("Disponible", "Louée", "En réparation");
    }

    public void initVoiture(Voiture voiture) {
        this.voitureActuelle = voiture;
        marqueField.setText(voiture.getMarque());
        modeleField.setText(voiture.getModele());
        anneeField.setText(String.valueOf(voiture.getAnnee()));
        prixField.setText(String.valueOf(voiture.getPrix_par_jour()));
        statutComboBox.setValue(voiture.getStatut());
        this.imagePath = voiture.getImagePath();
        conducteurSupplementaireField.setText(String.valueOf(voiture.getConducteurSupplementaire()));

        if (imagePath != null && !imagePath.isEmpty()) {
            voitureImageView.setImage(new Image(new File(imagePath).toURI().toString()));
        }

        ajouterButton.setText("Modifier");
    }

    @FXML
    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            voitureImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    public void ajouterVoiture() {
        try {
            String marque = marqueField.getText().trim();
            String modele = modeleField.getText().trim();
            String anneeText = anneeField.getText().trim();
            String prixText = prixField.getText().trim();
            String statut = statutComboBox.getValue();
            String conducteurSuppText = conducteurSupplementaireField.getText().trim();

            if (marque.isEmpty() || modele.isEmpty() || anneeText.isEmpty() || prixText.isEmpty() || conducteurSuppText.isEmpty() || statut == null) {
                showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs !");
                return;
            }

            int annee;
            double prix, conducteurSupp;
            try {
                annee = Integer.parseInt(anneeText);
                prix = Double.parseDouble(prixText);
                conducteurSupp = Double.parseDouble(conducteurSuppText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer des valeurs valides pour l'année, le prix et le conducteur supp. !");
                return;
            }

            if (annee < 1900 || annee > 2100) {
                showAlert(Alert.AlertType.ERROR, "Année invalide", "L'année doit être comprise entre 1900 et 2100 !");
                return;
            }
            if (prix <= 0 || conducteurSupp < 0) {
                showAlert(Alert.AlertType.ERROR, "Prix invalide", "Le prix doit être positif !");
                return;
            }

            if (voitureActuelle == null) {
                Voiture nouvelleVoiture = new Voiture(marque, modele, annee, prix, statut, imagePath,conducteurSupp);
                serviceVoiture.ajouter(nouvelleVoiture);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Voiture ajoutée avec succès !");
                resetFields();
            } else {
                voitureActuelle.setMarque(marque);
                voitureActuelle.setModele(modele);
                voitureActuelle.setAnnee(annee);
                voitureActuelle.setPrix_par_jour(prix);
                voitureActuelle.setStatut(statut);
                voitureActuelle.setImagePath(imagePath);
                voitureActuelle.setConducteurSupplementaire(conducteurSupp);

                serviceVoiture.modifier(voitureActuelle);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Voiture modifiée avec succès !");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void resetFields() {
        marqueField.clear();
        modeleField.clear();
        anneeField.clear();
        prixField.clear();
        statutComboBox.setValue(null);
        imagePath = null;
        conducteurSupplementaireField.clear();
    }

    @FXML
    public void ouvrirListeVoitures() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewVoiture/voiture/liste_voitures.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Voitures");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) voirListeButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des voitures : " + e.getMessage());
        }
    }

    @FXML
    public void fermerFenetre() {
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
