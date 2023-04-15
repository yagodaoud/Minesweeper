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

    public boolean isMined() {
        return hasMine;
    }

    public boolean isMarked(){
        return marked;
    }

     void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen(){
        return marked;
    }

    public boolean isClosed(){
        return !isOpen();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    boolean goalAchieved() {
        boolean found = !hasMine && open;
        boolean safe = hasMine && marked;
        return found || safe;
    }

    long minesOnTheNeighborhood() {
        return neighbors.stream().filter(n -> n.hasMine).count();
    }

    void restart() {
        open = false;
        hasMine = false;
        marked = false;
    }

    void mine(){
        hasMine = true;
    }

    boolean open() {
        if (!open && !marked) {
            open = true;

            if (hasMine) {
                throw new ExplosionException();
            }

            if (safeNeighbor()) {
                neighbors.forEach(Field::open);
            }
            return true;
        } else {
            return false;
        }
    }

    boolean safeNeighbor(){
        return neighbors.stream().noneMatch(n -> n.hasMine);
    }

    @Override
    public String toString() {
        if(marked) {
            return "x";
        } else if(open && hasMine) {
            return "*";
        } else if(open && minesOnTheNeighborhood() > 0) {
            return Long.toString(minesOnTheNeighborhood());
        } else if(open){
            return " ";
        } else {
            return "?";
        }
    }
}
