package balda;

/**
 * Буква с координатами
 */
class Letter {

    public final String text;
    public final int row;
    public final int col;

    Letter(String text, int r, int c) {
        this.text = text;
        this.row = r;
        this.col = c;
    }
}
