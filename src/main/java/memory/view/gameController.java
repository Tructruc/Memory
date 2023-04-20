package memory.view;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import memory.om.Carte;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class gameController implements Initializable {

    @FXML
    public GridPane gameGrid;

    private int windowSize = 800;

    private int gridSize = 4;

    private ArrayList<Label> labels = new ArrayList<>();

    private ArrayList<Carte> cartes = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

                labels.add(label);
                gameGrid.add(label, i, j);
            }
        }

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setText(String.valueOf(i));
            labels.get(i).setOnMouseClicked(mouseEvent -> {
                Label label = (Label) mouseEvent.getSource();
                label.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
                cartes.get(Integer.parseInt(label.getText())).setTrouvee(true);

                FadeTransition ft = new FadeTransition();
                ft.setDelay(javafx.util.Duration.millis(1000));
                ft.setNode(label);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.setCycleCount(1);
                ft.setAutoReverse(true);
                ft.play();

            });

            cartes.add(new Carte(i));
        }




    }
}
