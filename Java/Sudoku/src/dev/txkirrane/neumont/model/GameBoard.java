package dev.txkirrane.neumont.model;

import dev.txkirrane.neumont.enums.PLAYER_COLOR;

import java.util.ArrayList;

public class GameBoard {

    private ArrayList<ArrayList<PLAYER_COLOR>> gameBoard;

    private int rows;
    private int columns;

    public GameBoard(int rows, int columns){

        // Create a new game board with dimensions 7*6
        this.gameBoard = new ArrayList<>();

        this.rows = rows;
        this.columns = columns;

        // Create columns, setting first index to
        for (int i = 0; i < columns; i++) {
            ArrayList<PLAYER_COLOR> col = new ArrayList<>();
            col.add(PLAYER_COLOR.NULL);
            this.getGameBoard().add(col);
        }

    }

    public ArrayList<ArrayList<PLAYER_COLOR>> getGameBoard(){
        return this.gameBoard;
    }

    public void printGameBoard(){
        for(int i = this.rows; i > 0; i--){
            String row = "";
            for(int j = 0; j < this.columns; j++){

                PLAYER_COLOR pColor = PLAYER_COLOR.NULL;
                try{
                    pColor = this.getGameBoard().get(j).get(i - 1);
                } catch(Exception e){

                }
                row = row.concat(pColor.token + " ");
            }

            System.out.println(row);
        }

        System.out.println("\n1 2 3 4 5 6 7\n");
    }
}
