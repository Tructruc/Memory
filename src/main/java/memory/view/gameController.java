package memory.view;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import memory.om.Jeu;
import memory.om.Reponse;

import java.net.URL;
import java.util.*;

public class gameController implements Initializable {

    @FXML
    public GridPane gameGrid;

    @FXML
    public BorderPane gamePane;
    @FXML
    public Menu score;

    private final int windowSize = 800;
    private boolean triche = false;

    private int gridSize =4;

    private ArrayList<Label> labels = new ArrayList<>();

    private int firstClickIndex = 0;

    private final IntegerProperty nbCoups = new SimpleIntegerProperty(0);
//    private FadeTransition firstClickTransition = null;
//    private FadeTransition secondClickTransition = null;

    private void disableLabel(Label label) {
//        if (firstClickTransition != null){
//            if (firstClickTransition.getNode() == label) {
//                firstClickTransition = null;
//            }
//        }
//        if (secondClickTransition != null){
//            if (secondClickTransition.getNode() == label) {
//                secondClickTransition=null;
//
//            }
//        }

        label.setDisable(true);
        label.setStyle("-fx-background-color: #555555; -fx-text-fill: #ffffff;");
    }

    @FXML
    public void onSize(Event event){
        String name = ((MenuItem)event.getSource()).getText();

        switch (name) {
            case "2x2" -> gridSize = 2;
            case "4x4" -> gridSize = 4;
            case "6x6" -> gridSize = 6;
            case "8x8" -> gridSize = 8;
            case "10x10" -> gridSize = 10;
            case "12x12" -> gridSize = 12;
            case "14x14" -> gridSize = 14;
            case "16x16" -> gridSize = 16;
            case "18x18" -> gridSize = 18;
            case "20x20" -> gridSize = 20;
        }
        onRestart();
    }

    @FXML
    public void onRestart(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Recommencer");
        alert.setHeaderText("Voulez-vous vraiment recommencer ?");
        alert.setContentText("Vous allez perdre votre progression actuelle");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            onStartGame();
        }
    }

    @FXML
    public void onTricheActive(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Triche");
        if (triche)alert.setHeaderText("Voulez-vous vraiment désactiver la triche ?");
        else alert.setHeaderText("Voulez-vous vraiment activer la triche ?");
        alert.setContentText("Vous allez perdre votre progression");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            triche = !triche;
            onStartGame();
        }
    }



    public void onStartGame() {
        score.textProperty().bindBidirectional(nbCoups, new NumberStringConverter());
        nbCoups.set(0);
        gameGrid = new GridPane();
        labels.clear();
        gamePane.setCenter(gameGrid);

        Jeu jeu = new Jeu(gridSize * gridSize / 2, triche);



        gameGrid.setMaxSize(windowSize, windowSize);
        gameGrid.setMinSize(windowSize, windowSize);

        gameGrid.setGridLinesVisible(true);
        gameGrid.setHgap(10);
        gameGrid.setVgap(10);



        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Label label = new Label();
                label.setPrefSize(windowSize / gridSize, windowSize / gridSize);
                label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
                label.setAlignment(javafx.geometry.Pos.CENTER);
                label.setId(String.valueOf(i*gridSize + j));
                labels.add(label);
                gameGrid.add(label, i, j);
            }
        }

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setText(String.valueOf(jeu.getCarteValeur(i)));
            labels.get(i).setOnMouseClicked(mouseEvent -> {
                Label label = (Label) mouseEvent.getSource();


                Reponse reponse = jeu.jouer(Integer.parseInt(label.getId()));

                if (reponse != Reponse.ERREUR){
                    if (reponse != Reponse.PREMIERE) {
                        nbCoups.set(nbCoups.get() + 1);
                        if (reponse == Reponse.PERDU) {
                            label.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");

                            int firstClickIndexSave = firstClickIndex;
                            TimerTask t = new TimerTask() {
                                @Override
                                public void run() {
                                    Platform.runLater(() -> {
                                        if (firstClickIndexSave != firstClickIndex) {
                                            labels.get(firstClickIndexSave).setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
                                        }
                                        if (label.getId() != String.valueOf(firstClickIndex)) {
                                            label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
                                        }
                                    });
                                }
                            };


                            Timer timer = new Timer();
                            timer.schedule(t, 500);
                            firstClickIndex = -1;


//                        firstClickTransition = new FadeTransition();
//                        firstClickTransition.setDelay(javafx.util.Duration.millis(500));
//                        firstClickTransition.setNode(label);
//                        firstClickTransition.setFromValue(1.0);
//                        firstClickTransition.setToValue(0.0);
//                        firstClickTransition.setCycleCount(1);
//                        firstClickTransition.setAutoReverse(true);
//                        firstClickTransition.onFinishedProperty().set(actionEvent -> {
//                            label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
//                            label.setOpacity(1.0);
//
//                            if (jeu.isCarteTrouvee(Integer.parseInt(label.getId()))) {
//                                disableLabel(label);
//                            }
//
//                        });
//                        firstClickTransition.play();
//
//                        secondClickTransition = new FadeTransition();
//                        secondClickTransition.setDelay(javafx.util.Duration.millis(500));
//                        secondClickTransition.setNode(labels.get(firstClickIndex));
//                        secondClickTransition.setFromValue(1.0);
//                        secondClickTransition.setToValue(0.0);
//                        secondClickTransition.setCycleCount(1);
//                        secondClickTransition.setAutoReverse(true);
//                        secondClickTransition.onFinishedProperty().set(actionEvent -> {
//                            labels.get(firstClickIndex).setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
//                            labels.get(firstClickIndex).setOpacity(1.0);
//
//                            if (jeu.isCarteTrouvee(firstClickIndex)) {
//                                disableLabel(labels.get(firstClickIndex));
//                            }
//                        });
//                        secondClickTransition.play();
                        } else if (reponse == Reponse.GAGNE) {
                            disableLabel(label);
                            disableLabel(labels.get(firstClickIndex));
                        }


                    } else {
                        firstClickIndex = Integer.parseInt(label.getId());
//                    if (firstClickTransition != null){
//                        if (!jeu.isCarteTrouvee(Integer.parseInt(firstClickTransition.getNode().getId()))) {
//                            firstClickTransition.pause();
//                            firstClickTransition.getNode().setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
//                        }
//                    }
//                    if (secondClickTransition != null){
//                        if (!jeu.isCarteTrouvee(Integer.parseInt(secondClickTransition.getNode().getId()))){
//                            secondClickTransition.pause();
//                            secondClickTransition.getNode().setStyle("-fx-background-color: #ffffff; -fx-text-fill: #ffffff;");
//                        }
//                    }
                        label.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
                    }

                    if (jeu.isPartieTerminee()) {
                        System.out.println("Partie terminée");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Gagné");
                        alert.setHeaderText("Vous avez gagné");
                        alert.setContentText("Vous avez gagné en " + nbCoups.get() + " coups");
                        alert.showAndWait();
                    }

                    if (jeu.isCarteTrouvee(Integer.parseInt(label.getId()))) {
                        disableLabel(label);
                    }
                }
            });

        }

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        score.setStyle("-fx-text-fill: ffffff");
        score.setText("");
        Slider slider = new Slider(2, 10, 4);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);

        Button startBtn = new Button("Start");
        startBtn.setOnAction(e->onStartGame());


        AnchorPane menuPane = new AnchorPane();

        menuPane.getChildren().add(startBtn);
        AnchorPane.setTopAnchor(startBtn, 0.0);
        AnchorPane.setLeftAnchor(startBtn, 0.0);

        menuPane.setMinSize(windowSize, windowSize);
        menuPane.setMaxSize(windowSize,windowSize);

        gamePane.setCenter(menuPane);


    }

    @FXML
    public void onQuitter(Event event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Quitter");
        alert.setHeaderText("Voulez-vous vraiment quitter ?");
        alert.setContentText("Si vous quittez, vous perdrez votre partie en cours.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.exit(0);
        } else {
            alert.close();
            event.consume();
        }
    }
}
