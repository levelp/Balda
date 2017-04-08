package balda;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: После ввода буквы надо будет указать или ввести слово
 */
public class SceneFormController implements Initializable {

    @FXML
    public Pane Menu;

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
     * Начальное слово
     */
    private String firstWord;
    /**
     * Игровое поле (пустые клетки - пробелы), слова записаны по буквам
     */
    private char[][] game;
    private TextField[][] gameCells;

    /**
     * Загрузка словаря
     */
    private void loadLibrary() {
        try {
            // Загрузка словаря с сайта URL
            URL url = new URL("http://SFiles.mcpj.ml/Balda/Library.cfg");
            lines = new ArrayList<>();
            try (Scanner scan = new Scanner(url.openStream(), "UTF-8")) {
                while (scan.hasNextLine()) {
                    String word = scan.nextLine().trim();
                    if (word.length() == 0 || !Character.isLetter(word.charAt(0)))
                        continue;
                    lines.add(word);
                }
            }
            // Если не удалось скачать с сайта => пробуем прочитать локальный файл
            /*
            if (lines.size() == 0) {
                lines = Files.readAllLines(Paths.get("Library.cfg"), Charset.forName("UTF-8")); // Заменить на сервер "http://SFiles.mcpj.ml/Balda/Library.cfg"
            }
            */
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

                boolean[][] used = new boolean[rows][cols];
                game[letter.row][letter.col] = letter.text.charAt(0);
                if (wordFound(s, 0, letter.row, letter.col, used)) {

                    // Получаем клеточку с буквой
                    TextField field = gameCells[letter.row][letter.col];
                    field.setVisible(false); // Скрываем её
                    field.setText(""); // Очищаем текст
                    fixedLetter(letter.text.charAt(0), letter.row, letter.col);

                    // Расставляем вокруг пустые клеточки если их нет
                    addLetterTextFields();
                } else {
                    System.out.println("Word not found " + s);
                    game[letter.row][letter.col] = ' ';
                }
            }

        } else {
            System.out.println("Не нашли слово \"" + s + "\" в словаре");
        }
    }

    private boolean wordFound(String s, int pos, int row, int col, boolean[][] used) {
        // Если клетка за пределами => не нашли
        if (row < 0 || col < 0 || row >= rows || col >= cols)
            return false;
        if (used[row][col])
            return false;
        // Если буква не подходит, то не нашли
        if (s.charAt(pos) != game[row][col])
            return false;
        // Если буква подходит и это последняя буква, то возвращаем true
        if ((s.charAt(pos) == game[row][col])
                && (pos == s.length() - 1)) {
            System.out.println("Нашли слово, последняя буква: '" + s.charAt(pos) + "': " + row + " x " + col);
            return true;
        }
        used[row][col] = true;
        System.out.println("Нашли букву '" + s.charAt(pos) + "': " + row + " x " + col);
        // Проверяем все соседние буквы
        return wordFound(s, pos + 1, row + 1, col, used) ||
                wordFound(s, pos + 1, row - 1, col, used) ||
                wordFound(s, pos + 1, row, col + 1, used) ||
                wordFound(s, pos + 1, row, col - 1, used);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameField.getStyleClass().add("cell");
        gameField.setGridLinesVisible(true);

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
        addLetterTextFields();
    }

    /**
     * Добавляем текстовые поля для ввода
     */
    private void addLetterTextFields() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (game[row][col] == ' ' && ((row > 0 && game[row - 1][col] != ' ')
                        || (col > 0 && game[row][col - 1] != ' ')
                        || (row < rows - 1 && game[row + 1][col] != ' ')
                        || (col < cols - 1 && game[row][col + 1] != ' '))) {
                    if (gameCells[row][col] == null) {
                        TextField tf = new TextField();
                        tf.setFont(textPrototype.getFont());
                        gameField.add(tf, col, row);
                        gameCells[row][col] = tf;
                    } else {
                        // Очищаем если уже есть
                        gameCells[row][col].setText("");
                    }
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
