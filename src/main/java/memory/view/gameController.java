package memory.view;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import memory.ModeJeu;
import memory.ModeTrace;
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
    private ModeTrace modeTrace = ModeTrace.HORIZONTAL;
    private ArrayList<Label> labels = new ArrayList<>();
    private int firstClickIndex = 0;
    private final IntegerProperty nbCoups = new SimpleIntegerProperty(0);
    private ModeJeu modeJeu = ModeJeu.CHIFFRES;
    private final double gap = 10;

    private void disableLabel(Label label) {
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
    public void onModeJeu(Event event){
        String name = ((MenuItem)event.getSource()).getText();

        switch (name) {
            case "Chiffres" -> modeJeu = ModeJeu.CHIFFRES;
            case "Lettres" -> modeJeu = ModeJeu.LETTRES;
            case "Minecraft" -> modeJeu = ModeJeu.MINECRAFT;
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

    @FXML
    public void onModeTrace(Event event){
        String name = ((RadioMenuItem)event.getSource()).getText();
        if (name.equals("Horizontal") && modeTrace == ModeTrace.HORIZONTAL) return;
        if (name.equals("Vertical") && modeTrace == ModeTrace.VERTICAL) return;
        if (name.equals("Spirale") && modeTrace == ModeTrace.SPIRALE) return;

        switch (name) {
            case "Horizontal" -> modeTrace = ModeTrace.HORIZONTAL;
            case "Vertical" -> modeTrace = ModeTrace.VERTICAL;
            case "Spirale" -> modeTrace = ModeTrace.SPIRALE;
        }

        onRestart();
    }

    private void initLabel(MouseEvent mouseEvent, Jeu jeu){
        Label label = (Label) mouseEvent.getSource();
        Reponse reponse = jeu.jouer(Integer.parseInt(label.getId()));

        if (reponse != Reponse.ERREUR){
            if (reponse != Reponse.PREMIERE) {
                nbCoups.set(nbCoups.get() + 1);
                if (reponse == Reponse.PERDU) {


                    if (modeJeu == ModeJeu.LETTRES || modeJeu == ModeJeu.CHIFFRES) {
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
                    } else {
                        label.getGraphic().setVisible(true);
                        int firstClickIndexSave = firstClickIndex;
                        TimerTask t = new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    if (firstClickIndexSave != firstClickIndex) {
                                        labels.get(firstClickIndexSave).getGraphic().setVisible(false);
                                    }
                                    if (label.getId() != String.valueOf(firstClickIndex)) {
                                        label.getGraphic().setVisible(false);
                                    }
                                });
                            }
                        };

                        Timer timer = new Timer();
                        timer.schedule(t, 500);
                        firstClickIndex = -1;
                    }



                } else if (reponse == Reponse.GAGNE) {
                    disableLabel(label);
                    disableLabel(labels.get(firstClickIndex));
                    label.getGraphic().setVisible(true);
                }

            } else {
                firstClickIndex = Integer.parseInt(label.getId());

                if (modeJeu == ModeJeu.LETTRES || modeJeu == ModeJeu.CHIFFRES) {
                    label.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
                } else {
                    label.getGraphic().setVisible(true);
                }
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
    }

    public static int getValueInSpiral(int x, int y, int size) {
        int[][] matrix = new int[size][size];
        int left = 0, right = size-1, top = 0, bottom = size-1;
        int num = 1;
        while (left <= right && top <= bottom) {
            for (int i = left; i <= right; i++) {
                matrix[top][i] = num;
                num++;
            }
            for (int i = top+1; i <= bottom; i++) {
                matrix[i][right] = num;
                num++;
            }
            if (top < bottom) {
                for (int i = right-1; i >= left; i--) {
                    matrix[bottom][i] = num;
                    num++;
                }
            }
            if (left < right) {
                for (int i = bottom-1; i > top; i--) {
                    matrix[i][left] = num;
                    num++;
                }
            }
            left++;
            right--;
            top++;
            bottom--;
        }
        int value = matrix[x][y];
        return value-1;
    }

    private int getNumLablel(int x, int y){
        switch (modeTrace){
            case HORIZONTAL -> {
                return x * gridSize + y;
            }
            case VERTICAL -> {
                return y * gridSize + x;
            }
            case SPIRALE -> {
                return getValueInSpiral(x, y, gridSize);
            }
        }
        return 0;
    }

    private double getLabelSize(){
        double size = (windowSize - gap * (gridSize)) / gridSize;
        return size;

    }

    private void setLabelContent(Label label, Jeu jeu, int x, int y){
        switch (modeJeu) {
            case CHIFFRES -> label.setText(String.valueOf(jeu.getCarteValeur(getNumLablel(x, y))));
            case LETTRES -> label.setText(Character.toString((char) (jeu.getCarteValeur(getNumLablel(x, y))) + 65));
            case MINECRAFT -> {
                double imageSize = getLabelSize();
                System.out.println(imageSize);
                if (imageSize > 100) imageSize = 100;
                Image image = new Image(getClass().getResourceAsStream("images/" + jeu.getCarteValeur(getNumLablel(x, y)) + ".png"), imageSize, imageSize, true, true);
                ImageView imageView = new ImageView(image);
                label.setGraphic(imageView);
                label.getGraphic().setVisible(false);
            }
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
        gameGrid.setHgap(gap);
        gameGrid.setVgap(gap);

        labels = new ArrayList<>();
        for (int i = 0; i < gridSize*gridSize; i++) {
            labels.add(new Label());
        }

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Label label = new Label();
                label.setPrefSize(windowSize / gridSize, windowSize / gridSize);
                label.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #0000;");
                label.setAlignment(javafx.geometry.Pos.CENTER);
                if (modeTrace == ModeTrace.VERTICAL) {
                    label.setId(String.valueOf(i* gridSize + j));
                    setLabelContent(label, jeu, j, i);
                    labels.set(Integer.parseInt(label.getId()), label);
                    gameGrid.add(label, i, j);
                } else if (modeTrace == ModeTrace.HORIZONTAL) {
                    label.setId(String.valueOf(j * gridSize + i));
                    setLabelContent(label, jeu, j, i);
                    labels.set(Integer.parseInt(label.getId()), label);
                    gameGrid.add(label, i, j);
                } else if (modeTrace == ModeTrace.SPIRALE) {
                    label.setId(String.valueOf(getValueInSpiral(i, j, gridSize)));

                    labels.set(Integer.parseInt(label.getId()), label);
                    gameGrid.add(label, j, i);
                    setLabelContent(label, jeu, i, j);

                } else {
                    System.out.println("Mode de tracé non présent ou inconnu");
                    System.exit(2);
                }
            }
        }

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setOnMouseClicked(mouseEvent -> initLabel(mouseEvent, jeu));
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
