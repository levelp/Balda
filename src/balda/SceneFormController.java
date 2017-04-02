package balda;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * TODO: После ввода буквы надо будет указать или ввести слово
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

    @FXML
    private TextField word;

    /**
     * Словарь
     */
    private List<String> lines;

    /**
     * Размеры поля
     */
    private int rows;
    private int cols;

    /**
     * Загрузка словаря
     */
    public void loadLibrary() {

        String checkLibrary = "Существует: " + (new File("Library.cfg")).exists();
        System.out.println(checkLibrary);

        try {
            lines = Files.readAllLines(Paths.get("Library.cfg"), Charset.forName("UTF-8")); // Заменить на сервер "http://SFiles.mcpj.ml/Balda/Library.cfg"
            System.out.println("Количество строк: " + lines.size());
            System.out.println(lines);

            Random gen = new Random();
            do {
                int idx = gen.nextInt(lines.size());
                firstWord = lines.get(idx).trim().toUpperCase();
            } while (firstWord.length() == 0);
        } catch (IOException ex) {
            Logger.getLogger(SceneFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void checkWord() {
        Letter letter = getLetter();
        if (letter == null) {
            return;
        }

        // Получаем слово для проверки
        String s = word.getText().trim().toUpperCase();
        if (lines.contains(s)) {
            System.out.println("Нашли слово \"" + s + "\" в словаре");

            if (s.contains(letter.text)) {
                System.out.println("\"" + letter.text + "\" есть в слове \"" + s + "\"");

                gameCells[letter.row][letter.col].setVisible(false);
                fixedLetter(letter.text.charAt(0), rows, cols)
                ;
            }

        } else {
            System.out.println("Не нашли слово \"" + s + "\" в словаре");
        }
    }

    private Letter getLetter() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (gameCells[r][c] != null) {
                    String text = gameCells[r][c].getText().trim().toUpperCase();
                    if (text.length() > 0) {
                        System.out.println("Вы ввели: " + text
                                + " в строку " + r + " и в столбец " + c);
                        return new Letter(text, r, c);
                    }
                }
            }
        }
        System.out.println("Введите букву");
        return null; // Ничего не нашли
    }

    /**
     * Начальное слово
     */
    private String firstWord;

    /**
     * Игровое поле (пустые клетки - пробелы), слова записаны по буквам
     */
    private char[][] game;

    private TextField[][] gameCells;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rows = 8; //getRowCount(gameField);
        cols = 11; //getColCount(gameField);
        System.out.println("rows = " + rows + "  cols = " + cols);
        loadLibrary();
        // Заполняем массив пробелами
        game = new char[rows][cols];
        for (char[] cs : game) {
            for (int j = 0; j < cs.length; j++) {
                cs[j] = ' ';
            }
        }
        gameCells = new TextField[rows][cols];
        // Скрываем прототипы
        prototype.setVisible(false);
        textPrototype.setVisible(false);
        // Переводим всё в CAPS LOCK
        String capsLockWord = firstWord.toUpperCase();
        for (int i = 0; i < capsLockWord.length(); i++) {
            fixedLetter(capsLockWord.charAt(i), 2, i + 1);
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
                    gameCells[row][col] = tf;
                }
            }
        }
    }

    private void fixedLetter(final char symbol, int row, int col) {
        Label l = new Label("" + symbol);
        l.setFont(prototype.getFont());
        l.setAlignment(Pos.CENTER);
        l.setPrefWidth(prototype.getPrefWidth());
        gameField.add(l, col, row);
        game[row][col] = symbol;
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
