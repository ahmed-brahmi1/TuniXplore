package com.esprit.controllers.Voiture;

import com.esprit.models.Promotions;
import com.esprit.services.services_voiture.PromotionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.List;

public class GestionPromotionsController {

    @FXML
    private TableView<Promotions> tablePromotions;
    @FXML
    private TableColumn<Promotions, Integer> colId;
    @FXML
    private TableColumn<Promotions, Integer> colVoitureId;
    @FXML
    private TableColumn<Promotions, Double> colReduction;
    @FXML
    private TableColumn<Promotions, Date> colDateDebut;
    @FXML
    private TableColumn<Promotions, Date> colDateFin;

    @FXML
    private ComboBox<Integer> comboVoitureId;
    @FXML
    private TextField txtReduction;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnSupprimer;



    private final PromotionService promotionService = new PromotionService();

    @FXML
    public void initialize() {
        // Associer les colonnes aux attributs du modèle
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colVoitureId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getVoiture_id()).asObject());
        colReduction.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getReduction()).asObject());
        colDateDebut.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(new java.sql.Date(cellData.getValue().getDate_debut().getTime()))
        );
        colDateFin.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(new java.sql.Date(cellData.getValue().getDate_fin().getTime()))
        );

        // Charger les promotions
        loadPromotions();
        loadVoitureIds();

        // Ajouter une promotion
        btnAjouter.setOnAction(event -> ajouterPromotion());

        // Supprimer une promotion
        btnSupprimer.setOnAction(event -> supprimerPromotion());
    }

    private void loadPromotions() {
        List<Promotions> promotions = promotionService.getAllPromotions();
        ObservableList<Promotions> observableList = FXCollections.observableArrayList(promotions);
        tablePromotions.setItems(observableList);
    }

    private void loadVoitureIds() {
        List<Integer> voitureIds = promotionService.getAllVoitureIds(); // On récupère les IDs des voitures
        comboVoitureId.setItems(FXCollections.observableArrayList(voitureIds));
    }


    private void ajouterPromotion() {
        try {
            Integer voitureId = comboVoitureId.getValue();
            if (voitureId == null) {
                showAlert("Erreur", "Veuillez sélectionner une voiture !");
                return;
            }
            double reduction = Double.parseDouble(txtReduction.getText());
            Date debut = Date.valueOf(dateDebut.getValue());
            Date fin = Date.valueOf(dateFin.getValue());

            Promotions promotion = new Promotions(voitureId, reduction, debut, fin);
            boolean success = promotionService.ajouterPromotion(promotion); // Maintenant, ça retourne un booléen

            if (success) {
                showAlert("Succès", "Promotion ajoutée avec succès !");
                loadPromotions(); // Recharger la liste
            } else {
                showAlert("Erreur", "Impossible d'ajouter la promotion !");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez vérifier vos saisies !");
        }
    }


    private void supprimerPromotion() {
        Promotions selected = tablePromotions.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean success = promotionService.supprimerPromotion(selected.getId());
            if (success) {
                showAlert("Succès", "Promotion supprimée !");
                loadPromotions();
            } else {
                showAlert("Erreur", "Impossible de supprimer !");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une promotion !");
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
