package dev.txkirrane.neumont.model;

import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player{

    public int generateMove(GameBoard gBoard){

        // Generate a random number, check if that
        ArrayList<Integer> availableRows = new ArrayList<>();

        // Loop through columns, check if full.
        for (int i = 0; i < gBoard.getGameBoard().size(); i++) {
            if (gBoard.getGameBoard().get(i).size() < 6){
                availableRows.add(i);
            }
        }

        // Pick a random index from availableRows
        return availableRows.get(
                new Random().nextInt(availableRows.size())
        );
    }
}
