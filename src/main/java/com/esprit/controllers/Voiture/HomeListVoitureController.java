package com.esprit.controllers.Voiture;

import com.esprit.models.Voiture;
import com.esprit.services.services_voiture.ServiceVoiture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;


public class HomeListVoitureController {

    @FXML
    private GridPane voituresGrid;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> marqueFilter;
    @FXML
    private ComboBox<String> statutFilter;
    @FXML
    private Slider prixSlider;
    @FXML
    private Label prixLabel;
    @FXML
    private TextField anneeFilter;
    @FXML
    private Button filtrerButton;



    private final ServiceVoiture serviceVoiture = new ServiceVoiture();
    private ObservableList<Voiture> voituresList = FXCollections.observableArrayList();





    @FXML
    public void initialize() {
        chargerVoitures();
        setupFiltrage();
        chargerFiltres();

    }




    private void chargerVoitures() {
        voituresList.setAll(serviceVoiture.recupererToutes());
        updateGrid(voituresList);
    }

    private void setupFiltrage() {
        prixSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            prixLabel.setText("Max: " + newVal.intValue() + " TND");
        });

        filtrerButton.setOnAction(event -> filtrerVoitures());
    }

    private void filtrerVoitures() {
        String keyword = searchField.getText().toLowerCase();
        String marque = marqueFilter.getValue();
        String statut = statutFilter.getValue();
        int maxPrix = (int) prixSlider.getValue();
        String annee = anneeFilter.getText();

        List<Voiture> filteredList = voituresList.stream()
                .filter(v -> keyword.isEmpty() || v.getMarque().toLowerCase().contains(keyword) || v.getModele().toLowerCase().contains(keyword))
                .filter(v -> marque == null || v.getMarque().equals(marque))
                .filter(v -> statut == null || v.getStatut().equals(statut))
                .filter(v -> v.getPrix_par_jour() <= maxPrix)
                .filter(v -> annee.isEmpty() || String.valueOf(v.getAnnee()).contains(annee))
                .collect(Collectors.toList());

        updateGrid(FXCollections.observableArrayList(filteredList));
    }

    private void updateGrid(ObservableList<Voiture> list) {
        voituresGrid.getChildren().clear(); // Nettoyer la grille

        if (list.isEmpty()) {
            Label noDataLabel = new Label("Aucune voiture trouvée.");
            noDataLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            voituresGrid.add(noDataLabel, 0, 0); // Ajouter le message au centre
            GridPane.setColumnSpan(noDataLabel, 3); // Occuper toute la largeur
            return;
        }

        int col = 0, row = 0;
        for (Voiture voiture : list) {
            VBox voitureCard = creerCarteVoiture(voiture);
            voituresGrid.add(voitureCard, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }

    private VBox creerCarteVoiture(Voiture voiture) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-padding: 10px; -fx-border-color: #ddd;");

        // 💡 Effet d'ombre pour un design plus moderne
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(10);
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        card.setEffect(shadow);

        ImageView carImage = new ImageView();
        carImage.setFitWidth(150);
        carImage.setFitHeight(100);
        chargerImageVoiture(carImage, voiture.getImagePath());

        Label carMarque = new Label(voiture.getMarque());
        carMarque.setStyle("-fx-font-weight: bold;");

        Label carModel = new Label(voiture.getModele());
        Label carPrice = new Label(voiture.getPrix_par_jour() + " TND Par Jour");
        carPrice.setStyle("-fx-text-fill: green;");
        Label conducteurPrice = new Label("Conducteur suppl.: " + voiture.getConducteurSupplementaire() + " TND");
        conducteurPrice.setStyle("-fx-text-fill: blue;");

        Button reserverButton = new Button("📅 Réserver");

        // ✅ Désactiver le bouton "Réserver" si la voiture est déjà réservée
        if ("Réservée".equalsIgnoreCase(voiture.getStatut())) {
            reserverButton.setDisable(true);
            reserverButton.setText("⛔ Indisponible");
            reserverButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: default;");
        } else {
            reserverButton.setOnAction(event -> reserverVoiture(event, voiture));
        }


        HBox buttonBox = new HBox(10, reserverButton);
        buttonBox.setAlignment(Pos.CENTER);

        card.getChildren().addAll(carImage, carMarque, carModel, carPrice, conducteurPrice, buttonBox);
        return card;
    }


    private void reserverVoiture(javafx.event.ActionEvent event, Voiture voiture) {
        try {
            // Charger la page de réservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewVoiture/voiture/EspaceClientVoiture/ReservationForm.fxml"));
            Parent reservationRoot = loader.load();

            // Récupérer le contrôleur de la page de réservation
            ReservationFormController reservationController = loader.getController();

            // Transmettre les informations de la voiture sélectionnée
            reservationController.setVoitureDetails(voiture);

            // Changer de scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(reservationRoot));
            stage.setTitle("Réservation de " + voiture.getMarque() + " " + voiture.getModele());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void chargerImageVoiture(ImageView imageView, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                imageView.setImage(new Image(file.toURI().toString()));
            } else {
                imageView.setImage(new Image("/path/to/default/image.jpg"));
            }
        } else {
            imageView.setImage(new Image("/path/to/default/image.jpg"));
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void chargerFiltres() {
        // Récupérer toutes les voitures
        List<Voiture> voitures = serviceVoiture.recupererToutes();

        // Extraire les marques uniques
        List<String> marques = voitures.stream()
                .map(Voiture::getMarque)
                .distinct()
                .collect(Collectors.toList());

        // Extraire les statuts uniques
        List<String> statuts = voitures.stream()
                .map(Voiture::getStatut)
                .distinct()
                .collect(Collectors.toList());

        // Ajouter les valeurs aux ComboBox
        marqueFilter.setItems(FXCollections.observableArrayList(marques));
        statutFilter.setItems(FXCollections.observableArrayList(statuts));
    }


    @FXML
    private Button modeSombreButton;

    private boolean modeSombreActive = false;

    @FXML
    private void toggleModeSombre() {
        Scene scene = modeSombreButton.getScene();

        // Chemin du fichier CSS (assurez-vous qu'il est bien dans resources/com/example/voiture/)
        String darkThemePath = "/ViewVoiture/voiture/dark-theme.css";

        URL darkThemeUrl = getClass().getResource(darkThemePath);

        if (darkThemeUrl == null) {
            System.err.println("❌ Erreur : Fichier " + darkThemePath + " introuvable !");
            return;
        }

        if (modeSombreActive) {
            scene.getStylesheets().clear(); // Désactiver le mode sombre
            modeSombreButton.setText("🌙 Mode Sombre");
        } else {
            scene.getStylesheets().add(darkThemeUrl.toExternalForm());
            modeSombreButton.setText("☀ Mode Clair");
        }

        modeSombreActive = !modeSombreActive;
    }


    @FXML
    private void afficherReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewVoiture/voiture/EspaceClientVoiture/ListeReservations.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mes Réservations");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
