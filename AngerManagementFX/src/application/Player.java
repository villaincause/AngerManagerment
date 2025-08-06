package application;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Player {
    private String name;
    private int score;
    private int stateOfMind; 
    
    private int anger = 50;
    private int satisfaction = 25;
    private int confidence = 0;
    
    private ProgressBar angerBar;
    private ProgressBar satisfactionBar;
    private ProgressBar confidenceBar;
    
    private Label nameLabel;
    private Label scoreLabel;
    private ProgressBar mindBar;
    private VBox uiBox;

    public static final String P1_WIN = "P1_WIN";
    public static final String P2_WIN = "P2_WIN";

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.stateOfMind = 100;

        nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", 18));
        nameLabel.getStyleClass().add("name-label");

        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("score-label");

        mindBar = new ProgressBar(1.0);
        angerBar = createStateBar();
        satisfactionBar = createStateBar();
        confidenceBar = createStateBar();

        mindBar.setPrefWidth(150);
        mindBar.getStyleClass().add("mind-bar");

        uiBox = new VBox(5, nameLabel, scoreLabel, mindBar, angerBar, satisfactionBar, confidenceBar);
        uiBox.getStyleClass().add("player-ui");
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score += (1 + confidence / 50);
        updateUI();
    }

    public void changeStateOfMind(int delta) {
        stateOfMind += delta;
        if (stateOfMind > 100) stateOfMind = 100;
        if (stateOfMind < 0) stateOfMind = 0;
        updateUI();
    }

    public int getStateOfMind() {
        return stateOfMind;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public ProgressBar getMindBar() {
        return mindBar;
    }

    public VBox getUIBox() {
        return uiBox;
    }

    public void setUIBars() {
        scoreLabel.setText("Score: " + score);
        mindBar.setProgress(stateOfMind / 100.0);
    }
    
    private ProgressBar createStateBar() {
        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(150);
        bar.getStyleClass().add("mind-bar");
        return bar;
    }

    public void changeStateOfMind(String type, int delta) {
        switch (type) {
            case "anger":
                anger = clamp(anger + delta);
                break;
            case "satisfaction":
                satisfaction = clamp(satisfaction + delta);
                break;
            case "confidence":
                confidence = clamp(confidence + delta);
                break;
        }
        updateUI();
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private void updateUI() {
        scoreLabel.setText("Score: " + score);
        mindBar.setProgress(stateOfMind / 100.0);
        angerBar.setProgress(anger / 100.0);
        satisfactionBar.setProgress(satisfaction / 100.0);
        confidenceBar.setProgress(confidence / 100.0);
    }
} 