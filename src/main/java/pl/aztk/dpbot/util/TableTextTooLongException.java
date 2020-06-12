package pl.aztk.dpbot.util;

public class TableTextTooLongException extends RuntimeException {

    private final int currentRowNumber;
    private final Table table;
    private final String text;

    public TableTextTooLongException(int currentRowNumber, Table table, String text){
        super("Built table text exceeds Discord's message length limit!");
        this.currentRowNumber = currentRowNumber;
        this.table = table;
        this.text = text;
    }

    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    public Table getTable() {
        return table;
    }

    public String getText() {
        return text;
    }
}
