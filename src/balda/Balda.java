package balda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Запуск формы игры
 */
public class Balda extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Задаём заголовок формы
        stage.setTitle("Игра 'Балда'");

        Parent root = FXMLLoader.load(getClass().getResource("SceneForm.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("game.css");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("balda.png")));

        stage.setScene(scene);
        stage.show();

    }

}
