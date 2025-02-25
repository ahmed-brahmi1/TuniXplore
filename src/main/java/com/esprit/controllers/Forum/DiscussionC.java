package com.esprit.controllers;

import com.esprit.models.Commentaire;
import com.esprit.models.Discussion;
import com.esprit.models.Message;
import com.esprit.models.User;
import com.esprit.services.DiscussionService;
import com.esprit.services.IService;
import com.esprit.services.MessageService;
import com.esprit.utils.DataSource;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;


import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DiscussionC  {
    private Connection connection = DataSource.getInstance().getConnection();
    @FXML
    private VBox discussionContainer;

    @FXML
    private TextField searchField;
    @FXML
    private  Label username_chat;
    @FXML
    private ImageView profile_chat;

    @FXML
    private VBox messagesContainer;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ScrollPane  discussionScroll;
    private int idDiscussion;
    private final User AUTHUSER = new User(1,"Amen","Laatig","aziz.png");
    private User user;

    private List<Pane> allDiscussions = new ArrayList<>();


    public void initialize() {

        loadDiscussions();
        // Ne pas appeler loadMessages ici, car idDiscussion et user ne sont pas encore définis
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDiscussions(newValue.toLowerCase());
        });
    }

    // Méthode pour initialiser les données APRES avoir passé les paramètres
    public void initData(int idDiscussion, User user) {
        this.idDiscussion = idDiscussion;
        this.user = user;

        // Maintenant, on peut charger la discussion correctement
        loadMessages(idDiscussion, user.getNom(),  user.getPhoto_profil());
    }

    // Filtrer les discussions
    private void filterDiscussions(String query) {
        discussionContainer.getChildren().clear(); // Vider l'affichage actuel

        if (query.isEmpty()) {
            // Si la barre de recherche est vide, afficher toutes les discussions
            discussionContainer.getChildren().addAll(allDiscussions);
        } else {
            for (Pane discussion : allDiscussions) {
                Label nameLabel = (Label) discussion.getChildren().get(1); // Récupérer le nom

                if (nameLabel.getText().toLowerCase().contains(query)) {
                    discussionContainer.getChildren().add(discussion);
                }
            }
        }
    }

    private void loadDiscussions(){
        int authUser= AUTHUSER.getId();
        String req = "SELECT " +
                "    c.id_discussion AS discussion_id, " +
                "    u.nom, " +
                "u.prenom , "+
                "    u.photo_profil, " +
                "    m.contenu AS last_message, " +
                "    CASE " +
                "        WHEN DATE(m.date_creation) = CURDATE() " +
                "        THEN DATE_FORMAT(m.date_creation, '%H:%i') " +
                "        WHEN YEAR(m.date_creation) = YEAR(CURDATE()) " +
                "        THEN DATE_FORMAT(m.date_creation, '%d %b %H:%i') " +
                "        ELSE DATE_FORMAT(m.date_creation, '%d %b %Y %H:%i') " +
                "    END AS last_message_time " +
                "FROM discussion c " +
                "JOIN user u " +
                "    ON (CASE " +
                "            WHEN c.id_user1 = ? THEN c.id_user2 " +
                "            ELSE c.id_user1 " +
                "        END) = u.id " +
                " JOIN message m " +
                "    ON m.id_discussion = c.id_discussion " +
                "    AND m.date_creation = ( " +
                "        SELECT MAX(m2.date_creation) " +
                "        FROM message m2 " +
                "        WHERE m2.id_discussion = c.id_discussion " +
                "    ) " +
                "WHERE c.id_user1 = ? " +
                "   OR c.id_user2 = ? " +
                "ORDER BY m.date_creation DESC;";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1,authUser);
            pst.setInt(2,authUser);
            pst.setInt(3,authUser);

            ResultSet rs = pst.executeQuery();
            discussionContainer.getChildren().clear();
            allDiscussions.clear();

            while (rs.next()) {
                Pane discussionPane = createDiscussionPane(
                        rs.getInt("discussion_id"),
                        rs.getString("nom"),
                        rs.getString("photo_profil"),
                        rs.getString("last_message"),
                        rs.getString("last_message_time")
                );
                discussionContainer.getChildren().add(discussionPane);
                allDiscussions.add(discussionPane);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private Pane createDiscussionPane(int discussionId, String username, String profilePic, String lastMessage, String lastMessageTime) {
        Pane pane = new Pane();
        pane.setPrefSize(258, 89);
        pane.getStyleClass().add("historique");

        // ImageView (photo de profil)
        ImageView profileImage = new ImageView(new Image(profilePic));
        profileImage.setFitWidth(57);
        profileImage.setFitHeight(57);
        profileImage.setLayoutY(14);
        profileImage.setPreserveRatio(true);

        // Nom de l'utilisateur
        Label nameLabel = new Label(username);
        nameLabel.setLayoutX(67);
        nameLabel.setLayoutY(14);

        // Dernière activité
        Label timeLabel = new Label(lastMessageTime);
        timeLabel.setLayoutX(181);
        timeLabel.setLayoutY(14);
        timeLabel.setTextFill(javafx.scene.paint.Color.web("#8c8c8c"));

        // Dernier message
        Label messageLabel = new Label(lastMessage);
        messageLabel.setLayoutX(67);
        messageLabel.setLayoutY(47);
        messageLabel.setPrefWidth(146);
        messageLabel.setTextFill(javafx.scene.paint.Color.web("#8c8c8c"));

        // Ajout des enfants au Pane
        pane.getChildren().addAll(profileImage, nameLabel, timeLabel, messageLabel);

        // Ajouter un événement de clic
        pane.setOnMouseClicked(event -> {
            loadMessages(discussionId, username, profilePic);
        });


        return pane;
    }
    private void loadMessages(int discussionId, String username, String profilePic){
        messagesContainer.getChildren().clear();
        int authUser= AUTHUSER.getId();
        username_chat.setText(username);
        profile_chat.setImage(new Image(profilePic));

        //affichage des messages
        String req = "SELECT id_message , contenu,id_sender from message WHERE id_discussion=? ORDER BY date_creation ASC" ;
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, discussionId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("id_message"),discussionId,rs.getString("contenu"),rs.getInt("id_sender"),null);
                boolean isSender = (rs.getInt("id_sender") == authUser);
                HBox messageBox = createMessageBubble(message, isSender);
                messagesContainer.getChildren().add(messageBox);
            }
            // Défilement vers le bas après chargement des messages
            Platform.runLater(() -> scrollPane.setVvalue(1.0));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Ajouter un événement de clic pour envoyer un message
        sendButton.setOnAction(event -> sendMessage(discussionId, authUser));
    }

    private HBox createMessageBubble(Message message, boolean isSender ) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(isSender ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.setSpacing(5);

        // Création du Label du message
        Label messageLabel = new Label(message.getContenu());
        messageLabel.setFont(new Font(14));
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-background-color: " + (isSender ? "#0E70A6" : "white") +
                "; -fx-text-fill: " + (isSender ? "white" : "black") +
                "; -fx-padding: 10px; -fx-background-radius: 15px;");

        // Bouton Smile
        Button btnSmile = new Button();
        FontAwesomeIcon smileIcon = new FontAwesomeIcon();
        smileIcon.setGlyphName("HEART");
        smileIcon.setFill(Color.RED);
        smileIcon.setSize("18px");
        btnSmile.setGraphic(smileIcon);
        btnSmile.setStyle("-fx-background-color: transparent;");
        btnSmile.setVisible(false); // Caché par défaut

        // Création du bouton avec un menu déroulant
        MenuButton btnOptions = new MenuButton();
        FontAwesomeIcon optionsIcon = new FontAwesomeIcon();
        optionsIcon.setGlyphName("ELLIPSIS_V");
        optionsIcon.setFill(Color.GRAY);
        optionsIcon.setSize("18px");
        btnOptions.setGraphic(optionsIcon);
        btnOptions.setStyle("-fx-background-color: transparent;");
        btnOptions.setVisible(false); // Caché par défaut
// Création des options du menu
        MenuItem deleteItem = new MenuItem("Retirer");
        deleteItem.setOnAction(event -> handleDeleteMessage(messageBox,message.getIdMessage()));

        MenuItem editItem = new MenuItem("Modifier");
        editItem.setOnAction(event -> handleEditMessage(messageLabel,message));

// Ajout des éléments au MenuButton
        btnOptions.getItems().addAll(deleteItem, editItem);




        // Regrouper les éléments selon l'expéditeur
        if (isSender) {
            messageBox.getChildren().addAll( btnOptions, btnSmile,messageLabel );
        } else {
            messageBox.getChildren().addAll( messageLabel ,btnSmile);
        }

        // Gérer l'affichage des boutons au survol
        messageBox.setOnMouseEntered(event -> {
            btnSmile.setVisible(true);
            btnOptions.setVisible(true);
        });

        messageBox.setOnMouseExited(event -> {
            if (!btnOptions.isHover() && !btnSmile.isHover()) {
                btnSmile.setVisible(false);
                btnOptions.setVisible(false);
            }
        });

        return messageBox;
    }

    private void handleEditMessage(Label messageLabel , Message message) {
        // Mettre le message actuel dans le champ d'input
        messageInput.setText(messageLabel.getText());

        // Changer le comportement du bouton "Send"
        sendButton.setOnAction(event -> {
            String newMessage = messageInput.getText().trim();

            if (!newMessage.isEmpty()) {
                // Mise à jour dans la base de données
              message.setContenu(newMessage);
              MessageService ms=new MessageService();
              ms.modifier(message);

                // Mise à jour du Label dans l'interface
                messageLabel.setText(newMessage);

                // Réinitialiser l'état
                messageInput.clear();


                // Remettre l'événement original du bouton Send
                sendButton.setOnAction(e -> sendMessage(message.getId_discussion(),message.getIdMessage()));
            }
        });



    }



    private void sendMessage(int discussionId, int userId) {
        String contenu = messageInput.getText().trim();

        if (contenu.isEmpty()) {
            System.out.println("Le message est vide !");
            return;
        }


        // Créer un objet Message
        Message newMessage = new Message(0, discussionId, contenu, userId, null);

        // Appeler la méthode ajouter() pour l'insérer dans la base de données
        MessageService messageService = new MessageService();
        messageService.ajouter(newMessage);

        // Ajouter le message instantanément à l'interface
        HBox messageBox = createMessageBubble(newMessage, true);
        messagesContainer.getChildren().add(messageBox);

        // Défilement vers le bas après envoi du message
        Platform.runLater(() -> scrollPane.setVvalue(1.0));

        // Vider le champ de texte
        messageInput.clear();
    }
    private void handleDeleteMessage(HBox messageBox , int id_message) {
        System.out.println("Suppression du message...");
        messagesContainer.getChildren().remove(messageBox); // Supprime le message de l'UI
        // Ajoute ici la suppression dans la base de données si nécessaire
        MessageService ms = new MessageService();
        ms.supprimer(id_message);

    }


}
