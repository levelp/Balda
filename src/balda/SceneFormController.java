package balda;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * TODO: Загрузку из текстового файла и выбор случайного слова при запуске
 * TODO: После ввода буквы надо будет указать или ввести слово
 * TODO: Редактор формы - JavaFX Scene Builder
 */
public class SceneFormController implements Initializable {

    /**
     * Прототип для всех Label (правильные настройки штрифтов)
     */
    @FXML
    private Label prototype;

    /**
     * Прототип для полей для ввода текста
     */
    @FXML
    private TextField textPrototype;

    @FXML
    private GridPane gameField;

    @FXML
    private Label label;

    /**
     * Начальное слово
     */
    String firstWord = "привет";

    /**
     * Игровое поле (пустые клетки - пробелы), слова записаны по буквам
     */
    char[][] game;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int rows = 8; //getRowCount(gameField);
        int cols = 11; //getColCount(gameField);
        System.out.println("rows = " + rows + "  cols = " + cols);
        // Заполняем массив пробелами
        game = new char[rows][cols];
        for (char[] cs : game) {
            for (int j = 0; j < cs.length; j++) {
                cs[j] = ' ';
            }
        }
        // Скрываем прототипы
        prototype.setVisible(false);
        textPrototype.setVisible(false);
        // Переводим всё в CAPS LOCK
        String capsLockWord = firstWord.toUpperCase();
        for (int i = 0; i < capsLockWord.length(); i++) {
            Label l = new Label("" + capsLockWord.charAt(i));
            l.setFont(prototype.getFont());
            l.setAlignment(Pos.CENTER);
            l.setPrefWidth(prototype.getPrefWidth());
            int col = i + 1;
            gameField.add(l, col, 2);
            game[2][col] = capsLockWord.charAt(i);
        }
        // Добавляем текстовые поля для ввода
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (game[row][col] == ' ' && ((row > 0 && game[row - 1][col] != ' ')
                        || (col > 0 && game[row][col - 1] != ' ')
                        || (row < rows - 1 && game[row + 1][col] != ' ')
                        || (col < cols - 1 && game[row][col + 1] != ' '))) {
                    TextField tf = new TextField();
                    tf.setFont(textPrototype.getFont());
                    gameField.add(tf, col, row);
                }
            }
        }
    }

    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if (rowIndex != null) {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }

    private int getColCount(GridPane pane) {
        int numCols = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer colIndex = GridPane.getRowIndex(child);
                if (colIndex != null) {
                    numCols = Math.max(numCols, colIndex + 1);
                }
            }
        }
        return numCols;
    }
}
