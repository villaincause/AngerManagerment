package application;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class GameUI {
    private BorderPane root;
    private Player player1;
    private Player player2;
    private String player1Move = "";
    private String player2Move = "";
    private Label p1MessageLabel;
    private Label p2MessageLabel;

    private VBox startScreen;
    private Label gameTitle;
    private Label roundLabel;
    private Button startButton;
    private boolean gameStarted = false;

    private VBox p1MoveBox;
    private VBox p2MoveBox;
    private HBox actionButtonsBox;

    private MediaPlayer bgMusic;
    private AudioClip punchSound, kickSound, slapSound;

    public GameUI() {
    	
    	// Action buttons container
        actionButtonsBox = new HBox(20);
        actionButtonsBox.setAlignment(Pos.CENTER);
        actionButtonsBox.getStyleClass().add("image-button-container");

    	
        // Root layout and start screen
        root = new BorderPane();
        createStartScreen();
        root.setCenter(startScreen);

        
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        
        roundLabel = new Label();
        roundLabel.getStyleClass().add("round-label");

        loadSounds();
        playBackgroundMusic();
        

        // Message labels
        p1MessageLabel = createMessageLabel();
        p2MessageLabel = createMessageLabel();

        // Move selection boxes
        p1MoveBox = createMoveSelectionBox("Player 1");
        p2MoveBox = createMoveSelectionBox("Player 2");

            }

    private void createStartScreen() {
        gameTitle = new Label("ANGER MANAGEMENT");
        gameTitle.getStyleClass().add("game-title");

        startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGame());
        startButton.getStyleClass().add("control-button");

        startScreen = new VBox(20, gameTitle, startButton);
        startScreen.setAlignment(Pos.CENTER);
    }

    private void startGame() {
        gameStarted = true;

        // RoundLabel and title
        VBox topUI = new VBox(10);
        topUI.setAlignment(Pos.TOP_CENTER);
        topUI.setPadding(new Insets(20, 0, 10, 0));
        topUI.getStyleClass().add("round-container");

        topUI.getChildren().addAll(gameTitle, roundLabel);
        BorderPane.setAlignment(topUI, Pos.TOP_CENTER);
        root.setTop(topUI);

        // Player UI boxes
        VBox p1Box = player1.getUIBox();
        VBox p2Box = player2.getUIBox();
        p1Box.getStyleClass().addAll("player-ui", "player1-box");
        p2Box.getStyleClass().addAll("player-ui", "player2-box");
        p1Box.setPadding(new Insets(50));
        p2Box.setPadding(new Insets(50));
        root.setLeft(p1Box);
        root.setRight(p2Box);
        
        root.setCenter(new HBox(50, p1MoveBox, p2MoveBox));
        
        HBox centerBox = new HBox(50, p1MoveBox, p2MoveBox);
        centerBox.setAlignment(Pos.CENTER); // important!
        root.setCenter(centerBox);

        // Bottom action buttons
        actionButtonsBox.getStyleClass().add("image-button-container");
        root.setBottom(actionButtonsBox);

        //Give Up buttons
        Button p1GiveUp = new Button("Give Up");
        p1GiveUp.setOnAction(e -> handleGiveUp(player1));
        p1GiveUp.getStyleClass().add("control-button");

        Button p2GiveUp = new Button("Give Up");
        p2GiveUp.setOnAction(e -> handleGiveUp(player2));
        p2GiveUp.getStyleClass().add("control-button");

        player1.getUIBox().getChildren().add(p1GiveUp);
        player2.getUIBox().getChildren().add(p2GiveUp);
        

        roundLabel.setText("Round: " + GameLogic.getRound());
    }


    private VBox createTopUI() {
        HBox messageBox = new HBox(p1MessageLabel, roundLabel, p2MessageLabel);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setSpacing(150);

        VBox topBox = new VBox(gameTitle, messageBox);
        topBox.setAlignment(Pos.CENTER);
        return topBox;
    }

    private void handleGiveUp(Player quitter) {
        showMessage(p1MessageLabel, quitter.getName() + " gave up!");
        disableAllButtons();
    }

    private void loadSounds() {
        punchSound = new AudioClip(new File("src/application/sounds/Punch.mp3").toURI().toString());
        kickSound = new AudioClip(new File("src/application/sounds/Kick.mp3").toURI().toString());
        slapSound = new AudioClip(new File("src/application/sounds/Slap.mp3").toURI().toString());
    }

    private void playBackgroundMusic() {
        Media bg = new Media(new File("src/application/sounds/BackgroundMusic.MP3").toURI().toString());
        bgMusic = new MediaPlayer(bg);
        bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusic.play();
    }

    private Label createMessageLabel() {
        Label label = new Label();
        label.setOpacity(0);
        label.getStyleClass().add("action-message");
        return label;
    }

    private VBox createMoveSelectionBox(String player) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        ImageView rock = createImageButton("rock.jpg", () -> selectMove(player, "rock"));
        ImageView paper = createImageButton("paper.jpg", () -> selectMove(player, "paper"));
        ImageView scissors = createImageButton("scissors.jpg", () -> selectMove(player, "scissors"));

        box.getChildren().addAll(rock, paper, scissors);
        return box;
    }

    private ImageView createImageButton(String imageName, Runnable action) {
        ImageView imageView = new ImageView(new Image("file:src/application/images/" + imageName));
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setOnMouseClicked(e -> action.run());
        imageView.getStyleClass().add("image-button");
        return imageView;
    }

    private void selectMove(String player, String move) {
        if (player.equals("Player 1") && player1Move.isEmpty()) {
            player1Move = move;
        } else if (player.equals("Player 2") && player2Move.isEmpty()) {
            player2Move = move;
        }

        if (!player1Move.isEmpty() && !player2Move.isEmpty()) {
            GameLogic.Result result = GameLogic.determineWinner(player1Move, player2Move);
            if (result == GameLogic.Result.DRAW) {
                showMessage(p1MessageLabel, "Draw!");
                showMessage(p2MessageLabel, "Draw!");
                resetMoves();
            } else {
                if (result == GameLogic.Result.P1_WIN) {
                    showActionButtons(player1, player2, p1MessageLabel);
                } else {
                    showActionButtons(player2, player1, p2MessageLabel);
                }
            }
        }
    }

    private void showActionButtons(Player winner, Player loser, Label messageLabel) {
        actionButtonsBox.getChildren().clear();

        actionButtonsBox.getChildren().addAll(
                createActionButton("kick.jpg", "Kick", winner, loser, messageLabel),
                createActionButton("punch.jpg", "Punch", winner, loser, messageLabel),
                createActionButton("slap.jpg", "Slap", winner, loser, messageLabel)
        );

        GameLogic.nextRound();
        if (roundLabel != null) {
            roundLabel.setText("Round: " + GameLogic.getRound());
        }
    }

    private ImageView createActionButton(String img, String action, Player winner, Player loser, Label label) {
        ImageView imgView = new ImageView(new Image("file:src/application/images/" + img));
        imgView.setFitWidth(80);
        imgView.setFitHeight(80);
        imgView.setOnMouseClicked(e -> {
            winner.incrementScore();
            winner.changeStateOfMind("confidence", +10);
            winner.changeStateOfMind("anger", -10);
            winner.changeStateOfMind("satisfaction", +5);
            loser.changeStateOfMind("anger", +10);
            loser.changeStateOfMind("satisfaction", -5);

            playSound(action);
            showMessage(label, winner.getName() + " " + action.toLowerCase() + "ed " + loser.getName() + "!");

            resetMoves();
            actionButtonsBox.getChildren().clear();
        });
        imgView.getStyleClass().add("image-button");
        return imgView;
    }

    private void playSound(String action) {
        switch (action) {
            case "Punch": punchSound.play(); break;
            case "Kick": kickSound.play(); break;
            case "Slap": slapSound.play(); break;
        }
    }

    private void showMessage(Label label, String text) {
        label.setText(text);
        FadeTransition ft = new FadeTransition(Duration.seconds(1.5), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
    }

    private void resetMoves() {
        player1Move = "";
        player2Move = "";
    }

    private void disableAllButtons() {
        for (javafx.scene.Node node : p1MoveBox.getChildren()) {
            node.setDisable(true);
        }
        for (javafx.scene.Node node : p2MoveBox.getChildren()) {
            node.setDisable(true);
        }
        for (javafx.scene.Node node : actionButtonsBox.getChildren()) {
            node.setDisable(true);
        }
    }

    public Pane getRoot() {
        return root;
    }
}
