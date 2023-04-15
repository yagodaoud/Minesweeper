package br.com.yagodaoud.ms.model;

import br.com.yagodaoud.ms.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {

    private int rows;
    private int columns;
    private int mines;

    private final List<Field> fields = new ArrayList<>();

    public Board(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        connectNeighbors();
        draftMines();
    }

    private void generateFields() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                fields.add(new Field(r, c));
            }
        }
    }

    private void connectNeighbors() {
        for(Field f1 : fields){
            for(Field f2 : fields){
                f1.addNeighbor(f2);
            }
        }
    }

    private void draftMines() {
        long minesSet = 0;
        Predicate<Field> hasMine = f -> f.isMined();

        do {
            minesSet = fields.stream().filter(hasMine).count();
            int random = (int) (Math.random() * fields.size());
            fields.get(random).mine();
        } while(minesSet < mines);
    }

    public boolean goalAchieved() {
        return fields.stream().allMatch(n -> n.goalAchieved());
    }

    public void restart() {
        fields.stream().forEach(f -> f.restart());
        draftMines();
    }

    public void open(int rows, int columns) {
        try {
            fields.parallelStream()
                    .filter(n -> n.getRow() == rows && n.getColumn() == columns)
                    .findFirst()
                    .ifPresent(n -> n.open());
        } catch (ExplosionException e) {
            fields.forEach(f -> f.setOpen(true));
            throw e;
        }
    }

    public void mark(int rows, int columns) {
        fields.parallelStream()
                .filter(n -> n.getRow() == rows && n.getColumn() == columns)
                .findFirst()
                .ifPresent(n -> n.toggleMark());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int c = 0; c < columns; c++) {
            sb.append(" ");
            sb.append(c);
            sb.append(" ");
        }
        sb.append("\n");

        int i = 0;
        for (int r = 0; r < rows; r++) {
            sb.append(r);
            sb.append(" ");
            for (int c = 0; c < columns; c++) {
                sb.append(" ");
                sb.append(fields.get(i));
                sb.append(" ");
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
