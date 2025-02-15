package com.esprit.controllers.Voiture;

import com.esprit.models.Reservations;
import com.esprit.models.Voiture;
import com.esprit.services.services_voiture.ServiceVoiture;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;

public class InvoiceController {

    @FXML
    private Label lblReservationId;
    @FXML
    private Label lblClientId;
    @FXML
    private Label lblVoiture;
    @FXML
    private Label lblDateDebut;
    @FXML
    private Label lblDateFin;
    @FXML
    private Label lblStatut;
    @FXML
    private Label lblPrix;
    @FXML
    private Label lblConducteurSupp;
    @FXML
    private Label lblMarque;
    @FXML
    private Label lblModele;
    @FXML
    private Label lblAnnee;
    @FXML
    private Label lblPrixJour;
    @FXML
    private Label lblStatutVoiture;
    @FXML
    private ImageView voitureImage; // Ajout d'une ImageView pour afficher l'image

    private ServiceVoiture voitureService = new ServiceVoiture();

    public void setReservationDetails(Reservations reservation) {
        lblReservationId.setText(String.valueOf(reservation.getId()));
        lblClientId.setText(String.valueOf(reservation.getUtilisateur_id()));
        lblDateDebut.setText(reservation.getDate_debut().toString());
        lblDateFin.setText(reservation.getDate_fin().toString());
        lblStatut.setText(reservation.getStatut());
        lblPrix.setText(reservation.getPrix_finale() + " TND");
        lblConducteurSupp.setText(reservation.isConducteur_supplementaire() ? "✅ Oui" : "❌ Non");

        // Récupérer les infos de la voiture
        Voiture voiture = voitureService.getVoitureById(reservation.getVoiture_id());
        if (voiture != null) {
            lblVoiture.setText("" + voiture.getId());
            lblMarque.setText(voiture.getMarque());
            lblModele.setText(voiture.getModele());
            lblAnnee.setText(String.valueOf(voiture.getAnnee()));
            lblPrixJour.setText(voiture.getPrix_par_jour() + " TND/Jour");
            lblStatutVoiture.setText(voiture.getStatut());

            // Charger l'image correctement
            if (voiture.getImagePath() != null && !voiture.getImagePath().isEmpty()) {
                File file = new File(voiture.getImagePath());
                if (file.exists()) {
                    System.out.println("Image trouvée : " + file.toURI().toString()); // Debug
                    Image img = new Image(file.toURI().toString());
                    voitureImage.setImage(img);
                } else {
                    System.err.println("Image introuvable : " + voiture.getImagePath());
                    voitureImage.setImage(null); // Mettre une image par défaut si nécessaire
                }
            } else {
                System.err.println("Aucun chemin d'image défini pour cette voiture.");
                voitureImage.setImage(null);
            }
        } else {
            lblVoiture.setText("🚗 Voiture introuvable");
            lblMarque.setText("N/A");
            lblModele.setText("N/A");
            lblAnnee.setText("N/A");
            lblPrixJour.setText("N/A");
            lblStatutVoiture.setText("N/A");
            voitureImage.setImage(null);
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) lblReservationId.getScene().getWindow();
        stage.close();
    }
}
