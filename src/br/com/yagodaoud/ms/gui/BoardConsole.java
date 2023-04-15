package br.com.yagodaoud.ms.gui;

import br.com.yagodaoud.ms.exceptions.ExitException;
import br.com.yagodaoud.ms.exceptions.ExplosionException;
import br.com.yagodaoud.ms.model.Board;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class BoardConsole {

    private Board board;
    private Scanner input = new Scanner(System.in);

    public BoardConsole(Board board){
        this.board = board;

        startGame();
    }

    private void startGame() {
        try {
            boolean keepGoing= true;
            while (keepGoing){
                gameCycle();
                System.out.println("Another game? (y/n) ");
                String response = input.nextLine();

                if("y".equalsIgnoreCase((response))) {
                    keepGoing = false;
                } else {
                    board.restart();
                }
            }
        } catch (ExitException e){
            System.out.println("See you space cowboy");
        } finally {
            input.close();
        }
    }

    private void gameCycle() {
        try{
            while(!board.goalAchieved()) {
                System.out.println(board);

                String typed = getTypedValue("Type (x,y):");

                Iterator<Integer> xy = Arrays.stream(typed.split(","))
                        .map(e -> Integer.parseInt(e.trim())).iterator();

                typed = getTypedValue("1 - Open or 2 - (Un)Mark: ");

                if(1 == Integer.parseInt(typed)) {
                    board.open(xy.next(), xy.next());
                } else if (2 == Integer.parseInt(typed)) {
                    board.mark(xy.next(), xy.next());
                }
            }
            System.out.println(board);
            System.out.println(("Game won"));
        } catch (ExplosionException e){
            System.out.println(board);
            System.out.println("Game lost");
        }
    }

    private String getTypedValue(String text) {
        System.out.println(text);
        String typed = input.next();

        if("exit".equalsIgnoreCase(typed)) {
            throw new ExitException();
        }
        return typed;
    }
}
