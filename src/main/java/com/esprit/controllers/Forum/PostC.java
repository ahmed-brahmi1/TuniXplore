package com.esprit.controllers;

import com.esprit.models.Discussion;
import com.esprit.models.Post;
import com.esprit.models.User;
import com.esprit.services.DiscussionService;
import com.esprit.services.PostService;
import com.esprit.utils.DataSource;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


public class PostC {
    private Connection connection = DataSource.getInstance().getConnection();
    @FXML
    private ListView<Post> postsListView;
    @FXML
    private ImageView postImageView;
    @FXML
    private TextField postTextField;
    @FXML
    private Button addImageButton;
    @FXML
    private Button publishButton;

    private String selectedImageName = "";
    private int id_post=0;

    private FileChooser fileChooser;
   public Map<Post, User> listPosts = new TreeMap<>((p1,p2)->p2.getDate_creation().compareTo(p1.getDate_creation()));

    private final User AUTHUSER = new User(1,"laatig","amen","assets/aziz.png");
    // Déclaré dans ton fichier FXML

    public void initialize() {
        loadPosts();

        // Initialisation du FileChooser pour choisir une image
        initFileChooser();

        // Gestion du bouton d'ajout d'image
        addImageButton.setOnAction(event -> ajouterPhoto());

        // Gestion du bouton de publication
        publishButton.setOnAction(event -> publierPost());
    }
    private void loadPosts() {

        PostService postService = new PostService();

        // Récupération des posts triés par date de création
        // Conserve l'ordre d'insertion
        if (listPosts.isEmpty()) {
            listPosts.putAll(postService.afficherPost()); // Récupérer les posts depuis la BD
        }


        // Création d'une liste observable pour la ListView
        ObservableList<Post> observablePosts = FXCollections.observableArrayList(listPosts.keySet());

        // Associer la liste à la ListView
        postsListView.setItems(observablePosts);

        // Personnalisation de l'affichage des posts
        postsListView.setCellFactory(param -> new ListCell<Post>() {
            @Override
            protected void updateItem(Post post, boolean empty) {
                super.updateItem(post, empty);
                if (empty || post == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Créer une vue personnalisée pour chaque post
                    Pane postPane = createPostPane(post, listPosts.get(post));
                    // Créer un conteneur avec un espacement vertical
                    VBox container = new VBox(postPane);
                    container.setPadding(new Insets(20, 0, 20, 0));
                    container.setSpacing(25); // Définit l'espace entre les posts

                    setGraphic(container);
                }
            }
        });
    }

    // Méthode pour créer l'affichage de chaque post
    private Pane createPostPane(Post post, User user) {
        Pane postPane = new Pane();

        postPane.getStyleClass().add("PostingPane");
        postPane.setPrefSize(470, 319);
        // Image de profil
        ImageView profileImage = new ImageView(new Image(user.getPhoto_profil()));
        profileImage.setFitHeight(40);
        profileImage.setFitWidth(38);
        profileImage.setLayoutX(14);
        profileImage.setLayoutY(14);

        // Nom utilisateur
        Label usernameLabel = new Label(user.getPrenom() + " " + user.getNom());
        usernameLabel.setLayoutX(59);
        usernameLabel.setLayoutY(14);
        usernameLabel.setPrefWidth(90);
        usernameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        // Date du post
        Label dateLabel = new Label(post.getDate_creation().toString());
        dateLabel.setLayoutX(59);
        dateLabel.setLayoutY(35);
        dateLabel.setTextFill(javafx.scene.paint.Color.GREY);

        // Contenu du post
        Label contentLabel = new Label(post.getContenu());
        contentLabel.setLayoutX(33);
        contentLabel.setLayoutY(66);
        contentLabel.setPrefWidth(396);
        contentLabel.setWrapText(true);

        // Image du post (vérifier si l'image est disponible)
        ImageView postImage = new ImageView();
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            postImage.setImage(new Image("assets/" + post.getImage()));
            postImage.setFitHeight(159);
            postImage.setFitWidth(214);
            postImage.setLayoutX(140);
            postImage.setLayoutY(119);
            postPane.setPrefSize(470, 319);
        }
        else{
            postPane.setPrefHeight(150);
        }

        // Bouton options (3 points)
        Button optionsButton = new Button();
        
        optionsButton.setLayoutX(403);
        optionsButton.setLayoutY(19);
        optionsButton.getStyleClass().add("LikeBTN");
        FontAwesomeIcon option = new FontAwesomeIcon();
        option.setGlyphName("ELLIPSIS_H");
        option.setFill(javafx.scene.paint.Color.valueOf("#0E70A6"));
        option.setSize("18px");
        optionsButton.setGraphic(option);

        // Création du menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem chatItem = new MenuItem("Aller à chat privé");
        chatItem.setOnAction(event -> ouvrirChat(user, optionsButton));

        if (AUTHUSER.getId() == user.getId()) { // Vérifier si c'est le propriétaire du post
            MenuItem editItem = new MenuItem("Modifier");
            editItem.setOnAction(event -> modifierPost(post));

            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> supprimerPost(post, postPane));

            contextMenu.getItems().addAll(chatItem, editItem, deleteItem);
        } else {
            contextMenu.getItems().add(chatItem);
        }

        // Associer le menu contextuel au bouton options
        optionsButton.setOnAction(event -> contextMenu.show(optionsButton, Side.BOTTOM, 0, 0));



        // Bouton like

        Button likeButton = new Button();
        likeButton.setLayoutX(14);
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            likeButton.setLayoutY(280);
        }
        else{
            likeButton.setLayoutY(110);
        }
        likeButton.getStyleClass().add("LikeBTN");
        FontAwesomeIcon likeIcon = new FontAwesomeIcon();
        likeIcon.setGlyphName("HEART");
        likeIcon.setFill(javafx.scene.paint.Color.valueOf("#0E70A6"));
        likeIcon.setSize("18px");
        likeButton.setGraphic(likeIcon);


        // Action du bouton Like
        likeButton.setOnAction(event -> incrementerLikes(post,postPane));

        // Label nombre de likes
        Label likeCountLabel = new Label(String.valueOf(post.getNb_likes()));
        likeCountLabel.setLayoutX(48);
        if (post.getImage() != null && !post.getImage().isEmpty()) {
        likeCountLabel.setLayoutY(286);}
        else{
            likeCountLabel.setLayoutY(116);
        }
        likeCountLabel.getStyleClass().add("LikeCount");

        // Ajouter les éléments au pane
        postPane.getChildren().addAll(profileImage, usernameLabel, dateLabel, contentLabel, postImage, optionsButton, likeButton, likeCountLabel);
        return postPane;
    }

    private void supprimerPost(Post post, Pane postPane) {
        // Supprimer le post de la Map
        listPosts.remove(post);

        // Rafraîchir la ListView
        postsListView.setItems(FXCollections.observableArrayList(listPosts.keySet()));

        PostService postService = new PostService();
        postService.supprimer(post.getId_post());

    }

    private void modifierPost(Post post) {
        postTextField.setText(post.getContenu());


        // Charger l'image si elle existe
        if (post.getImage() != null) {
            Image image = new Image("assets/"+post.getImage());
            postImageView.setImage(image);
            selectedImageName = post.getImage();
        }
        publishButton.setOnAction(event -> publierPostM(post));

    }

    private void publierPostM(Post newPost) {
        String contenu = postTextField.getText().trim();

        if (contenu.isEmpty()) {
            // Afficher une alerte si le champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le contenu du post ne peut pas être vide !");
            alert.showAndWait();
            return;
        }
        newPost.setContenu(contenu);
        newPost.setImage(selectedImageName);
        listPosts.put(newPost, AUTHUSER);
        // Rafraîchir la ListView
        postsListView.setItems(FXCollections.observableArrayList(listPosts.keySet()));

        PostService postService = new PostService();
        postService.modifier(newPost);

        // Réinitialiser les champs après la publication
        postTextField.clear();
        postImageView.setImage(null);
        selectedImageName = "";
    }

    private void ouvrirChat(User user , Node sourceNode) {
        DiscussionService ds = new DiscussionService();
        int authUserId= AUTHUSER.getId();
        int  autreUserId = user.getId();

        // Vérifier si une discussion existe
        int idDiscussion = ds.IdDiscussion(autreUserId);
        if (idDiscussion == 0) {
            Discussion nouvelleDiscussion = new Discussion(0, authUserId, autreUserId);
            idDiscussion=ds.ajouter(nouvelleDiscussion);
        }

        // Rediriger vers la page discussions.fxml avec l'ID de la discussion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/discussions.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page et lui passer l'ID de la discussion
            DiscussionC controller = loader.getController();
            controller.initData(idDiscussion, user);
            // Charger la nouvelle scène
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void incrementerLikes(Post post, Pane postPane){
        PostService postService = new PostService();
        postService.modifierLikes(post.getId_post()); // Met à jour la BD
        post.setNb_likes(post.getNb_likes() + 1); // Met à jour localement

        // Récupérer le label des likes à partir du postPane
        for (Node node : postPane.getChildren()) {
            if (node instanceof Label && ((Label) node).getStyleClass().contains("LikeCount")) {
                ((Label) node).setText(String.valueOf(post.getNb_likes())); // Mise à jour de l'affichage
                break;
            }
        }
    }

    private void initFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
    }

    private void ajouterPhoto() {
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Copier l'image dans le dossier "assets/"
            String destinationPath = "src/main/resources/assets/" + selectedFile.getName();
            File destinationFile = new File(destinationPath);

            try {
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedImageName = selectedFile.getName(); // Stocker seulement le nom

                // Afficher l'image sélectionnée
                postImageView.setImage(new Image(destinationFile.toURI().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void publierPost() {
        String contenu = postTextField.getText().trim();

        if (contenu.isEmpty()) {
            // Afficher une alerte si le champ est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le contenu du post ne peut pas être vide !");
            alert.showAndWait();
            return;
        }
        // 🔥 Trouver l'ID de post maximum dans `listPosts.keySet()`
        Optional<Integer> maxIdPost = listPosts.keySet().stream()
                .map(Post::getId_post) // Transformer chaque `Post` en `id_post`
                .max(Integer::compareTo); // Récupérer le maximum
        // 🔥 Si un post existe, récupérer la valeur, sinon retourner -1
        id_post = maxIdPost.orElse(-1);
        id_post++;
        // Créer un nouveau post avec le texte et le nom de l’image (si présente)
        Post newPost = new Post(id_post, AUTHUSER.getId(), contenu,0,  Timestamp.valueOf(LocalDateTime.now()), selectedImageName);



        // Ajouter à la base de données
        PostService postService = new PostService();
        postService.ajouter(newPost);

        // Ajouter le post et l'utilisateur dans `listPosts`
        listPosts.put(newPost, AUTHUSER); // 🔥 Ceci garantit que `user` ne sera pas null


        // Rafraîchir la ListView
        postsListView.setItems(FXCollections.observableArrayList(listPosts.keySet()));


        // Réinitialiser les champs après la publication
        postTextField.clear();
         postImageView.setImage(null);
            selectedImageName = "";


    }





}
