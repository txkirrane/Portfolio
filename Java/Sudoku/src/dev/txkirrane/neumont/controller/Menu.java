package dev.txkirrane.neumont.controller;

import dev.txkirrane.neumont.model.Computer;
import dev.txkirrane.neumont.model.Person;
import dev.txkirrane.neumont.view.Input;

public class Menu {

    private boolean hasExited = false;
    private Input input;

    private Game game;

    public Menu(){
        game = new Game();
        input = new Input();

        while(!hasExited){
            System.out.println("\n\n- - - Welcome to Connect4! - - -");
            System.out.println("Please select a play mode from the following:\n" +
                    "1: Human vs. Human\n2: Human vs. CPU\n3: CPU vs. CPU\n4: Exit\n");
            int response = input.getInputInt("Please select an option (1, 2, 3, 4)",
                    1, 4);

            switch(response){
                case 1:
                    game.start(new Person(), new Person());
                    break;
                case 2:
                    game.start(new Person(), new Computer());
                    break;
                case 3:
                    game.start(new Computer(), new Computer());
                    break;
                case 4:
                    this.hasExited = true;
                    break;
                default:
                    System.out.println("Please enter a valid option.");
            }
        }
    }
}
