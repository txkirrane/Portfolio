package dev.txkirrane.neumont.controller;

import dev.txkirrane.neumont.enums.GAME_STATE;
import dev.txkirrane.neumont.enums.PLAYER_COLOR;
import dev.txkirrane.neumont.exceptions.ColumnFullException;
import dev.txkirrane.neumont.model.Computer;
import dev.txkirrane.neumont.model.GameBoard;
import dev.txkirrane.neumont.model.Person;
import dev.txkirrane.neumont.model.Player;
import dev.txkirrane.neumont.view.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Game {

    // TODO: Add comments

    private static final int BOARD_ROWS = 6;
    private static final int BOARD_COLUMNS = 7;

    private GameBoard gameBoard;
    private Input input;

    public void start(Player player1, Player player2){

        // Get an input object
        input = new Input();

        // Initialize a new gameBoard object
        this.gameBoard = new GameBoard(BOARD_ROWS, BOARD_COLUMNS);

        // Ask for player1's name
        String nameP1 = input.getInputString("Please enter a name for Player 1:", "P1");

        // Ask for player2's name
        String nameP2 = input.getInputString("Please enter a name for Player 2:", "P2");

        // Randomly decide which player goes first
        if(new Random().nextBoolean()){
            player1.initPlayer(nameP1, PLAYER_COLOR.YELLOW);
            player2.initPlayer(nameP2, PLAYER_COLOR.RED);
            this.play(player1, player2);
        } else {
            player1.initPlayer(nameP1, PLAYER_COLOR.RED);
            player2.initPlayer(nameP2, PLAYER_COLOR.YELLOW);
            this.play(player2, player1);
        }
    }

    private void play(Player player1, Player player2){
        GAME_STATE state = GAME_STATE.PENDING;

        Player currentPlayer = player1;
        while (state == GAME_STATE.PENDING){

            // Print out gameBoard
            gameBoard.printGameBoard();

            System.out.println(currentPlayer.getName() + "'s turn, placing an " + currentPlayer.getColor().token + "\n");
            //
            int col = 0;
            if(currentPlayer.getClass() == Person.class){
                col = input.getInputInt("What column would you like to place a piece in?");
                col--;
            } else if(currentPlayer.getClass() == Computer.class){
                Computer c = (Computer) currentPlayer;
                col = c.generateMove(this.gameBoard);
            }

            try {
                this.placeToken(this.gameBoard, currentPlayer.getColor(), col);
            } catch (ColumnFullException e) {
                System.out.println("Column " + col + " is full, please choose another column.");
            }

            // Check for any win states
            state = this.checkGameState(gameBoard.getGameBoard());

            // Switch to other player
            currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
        }

        gameBoard.printGameBoard();

        if(state == GAME_STATE.DRAW){
            System.out.println("Game was a draw!");
        } else {
            // Get winning player
            Player winningPlayer = player1.getColor() == state.color ? player1 : player2;

            // Display win message
            System.out.println("Player " + winningPlayer.getName() + " ("+
                    winningPlayer.getColor().token+") has won! Good game!");
        }
    }

    private GAME_STATE checkGameState(ArrayList<ArrayList<PLAYER_COLOR>> gBoard){

        // Check to see if 4 in a row has been reached for either player, return appropriate state

        PLAYER_COLOR winningColor = PLAYER_COLOR.NULL;
        try {
            // Check for win on columns
            for (ArrayList<PLAYER_COLOR> col : gBoard) {
                winningColor = checkArrayForFourInRow(col);
                if (winningColor != PLAYER_COLOR.NULL){
                    throw new Exception();
                }
            }

            // Convert to array of rows
            for (int i = 0; i < BOARD_ROWS; i++) {

                ArrayList<PLAYER_COLOR> row = new ArrayList<>();

                for (int j = 0; j < BOARD_COLUMNS; j++) {
                    try {
                        row.add(gBoard.get(j).get(i));
                    } catch (Exception e) {
                        row.add(PLAYER_COLOR.NULL);
                    }
                }

                winningColor = checkArrayForFourInRow(row);
                if (winningColor != PLAYER_COLOR.NULL){
                    throw new Exception();
                }
            }

            /* Check for diagonals */
            PLAYER_COLOR[][] gameBoardArray = this.convertToArray(gBoard);

            // Check diagonals in normal orientation
            winningColor = checkDiagonals(gameBoardArray, gBoard);
            if (winningColor != PLAYER_COLOR.NULL){
                throw new Exception();
            }

            // Reverse on Y axis, check diagonals
            Collections.reverse(Arrays.asList(gameBoardArray));
            winningColor = checkDiagonals(gameBoardArray, gBoard);
            if (winningColor != PLAYER_COLOR.NULL){
                throw new Exception();
            }

            // Reverse on X axis, check diagonals
            for (PLAYER_COLOR[] col : gameBoardArray) {
                Collections.reverse(Arrays.asList(col));
            }
            winningColor = checkDiagonals(gameBoardArray, gBoard);
            if (winningColor != PLAYER_COLOR.NULL){
                throw new Exception();
            }

            // Reverse on X axis again
            Collections.reverse(Arrays.asList(gameBoardArray));
            winningColor = checkDiagonals(gameBoardArray, gBoard);
            if (winningColor != PLAYER_COLOR.NULL){
                throw new Exception();
            }

            // Check to see if board is full
            int fullColumns = 0;
            for (ArrayList<PLAYER_COLOR> col : gBoard) {
                if (col.size() == BOARD_ROWS) fullColumns++;
            }

            if(fullColumns == BOARD_COLUMNS){
                return GAME_STATE.DRAW;
            }

        } catch (Exception e){
        }

        if(winningColor == PLAYER_COLOR.RED){
            return GAME_STATE.WIN_RED;
        } else if(winningColor == PLAYER_COLOR.YELLOW){
            return GAME_STATE.WIN_YELLOW;
        }

        // No win has been detected, return pending
        return GAME_STATE.PENDING;
    }

    private PLAYER_COLOR checkDiagonals(PLAYER_COLOR[][] array, ArrayList<ArrayList<PLAYER_COLOR>> gBoard){
        PLAYER_COLOR winningColor = PLAYER_COLOR.NULL;
        for (int i = 0; i < array.length; i++) {
            ArrayList<PLAYER_COLOR> dRow = new ArrayList<>();
            for (int j = 0; j < array[i].length; j++) {
                PLAYER_COLOR pC;
                try{
                    pC = array[i - j][j];
                } catch (Exception e){
                    pC = PLAYER_COLOR.NULL;
                }
                dRow.add(pC);
            }

            //System.out.println(Arrays.deepToString(dRow.toArray()));
            winningColor = checkArrayForFourInRow(dRow);
            if (winningColor != PLAYER_COLOR.NULL) break;
        }

        return winningColor;
    }

    private void placeToken(GameBoard gameBoard, PLAYER_COLOR pColor, int column) throws ColumnFullException {

        // Check to see if given column exists, and if so whether that row is full
        if(gameBoard.getGameBoard().get(column).size() < BOARD_ROWS){

            // Check if size is one, and if the 0th element is NULL
            if(gameBoard.getGameBoard().get(column).get(0) == PLAYER_COLOR.NULL){
                gameBoard.getGameBoard().get(column).set(0, pColor);
            } else {
                // Given column is not full, add a token of the given color
                gameBoard.getGameBoard().get(column).add(pColor);
            }

        } else {

            // Given column is full, throw an exception
            throw new ColumnFullException();

        }
    }

    private PLAYER_COLOR[][] convertToArray(ArrayList<ArrayList<PLAYER_COLOR>> aList){
        // Convert arraylist to an array
        PLAYER_COLOR[][] gameBoardArray = new PLAYER_COLOR[BOARD_COLUMNS][BOARD_ROWS];

        //
        for(int i = 0; i < BOARD_COLUMNS; i++){
            for(int j = 0; j < BOARD_ROWS; j++){
                PLAYER_COLOR pC = PLAYER_COLOR.NULL;
                try {
                    pC = aList.get(i).get(j);
                } catch (Exception e){
                }
                gameBoardArray[i][j] = pC;
            }
        }

        return gameBoardArray;
    }

    /*

     */
    private PLAYER_COLOR checkArrayForFourInRow(ArrayList<PLAYER_COLOR> array){

        if(array.size() < 4){
            return PLAYER_COLOR.NULL;
        }

        PLAYER_COLOR pColor = PLAYER_COLOR.NULL;
        int pCount = 0;

        for (int i = 0; i < array.size(); i++) {
            if(array.get(i) == pColor){
                pCount++;
            } else {
                pColor = array.get(i);
                pCount = 1;
            }

            if(pCount >= 4) break;
        }

        if(pCount >= 4 && pColor != PLAYER_COLOR.NULL){
            return pColor;
        }

        return PLAYER_COLOR.NULL;
    }

}
