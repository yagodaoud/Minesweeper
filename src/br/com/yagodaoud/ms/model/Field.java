package br.com.yagodaoud.ms.model;

import br.com.yagodaoud.ms.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final int row;
    private final int column;

    private boolean hasMine;
    private boolean open;
    private boolean marked;

    private List<Field> neighbors = new ArrayList<>();

    Field(int row, int column){
        this.row = row;
        this.column = column;
    }

    boolean addNeighbor(Field neighbor) {
        boolean differentRow = row != neighbor.row;
        boolean differentColumn = column != neighbor.column;
        boolean diagonal = differentColumn && differentRow;

        int deltaRow = Math.abs(row - neighbor.row);
        int deltaColumn = Math.abs(column - neighbor.column);
        int deltaAll = deltaRow + deltaColumn;

        if (deltaAll == 1 && !diagonal) {
            neighbors.add(neighbor);
            return true;
        } else if (deltaAll == 2 && diagonal) {
            neighbors.add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    void toggleMark(){
        if(!open){
            marked = !marked;
        }
    }

    public boolean isMarked(){
        return marked;
    }

    boolean open() {
        if (!open && !marked) {
            open = true;

            if (hasMine) {
                throw new ExplosionException();
            }

            if (safeNeighbor()) {
                neighbors.forEach(n -> n.open());
            }
            return true;
        } else {
            return false;
        }
    }

    boolean safeNeighbor(){
        return neighbors.stream().noneMatch(n -> n.hasMine);
    }

}
