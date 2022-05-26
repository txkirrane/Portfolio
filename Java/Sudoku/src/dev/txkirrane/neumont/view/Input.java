package dev.txkirrane.neumont.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {

    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public String getInputString(){
        return getInputString(null, null);
    }

    public String getInputString(String prompt){
        return getInputString(prompt, null);
    }

    public String getInputString(String prompt, String defaultAnswer){

        // Check if prompt is null, if not, display it
        if(prompt != null){
            System.out.println(prompt);
        }

        // Get input from console
        String input = "";
        try {
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If defaultAnswer was specified, and the input
        // was null return the default answer
        if(input.isEmpty()){
            return defaultAnswer;
        }

        // Return given input
        return input;
    }

    public int getInputInt(String prompt){
        boolean hasAnswered = false;
        int input = 0;
        while (!hasAnswered){
            try{
                input = Integer.parseInt(getInputString(prompt));
                hasAnswered = true;
            } catch (Exception e){
                System.out.println("Please enter a valid integer!");
            }
        }

        return input;
    }

    public int getInputInt(String prompt, int lowerBound, int higherBound){
        boolean hasAnswered = false;
        int input = 0;
        while(!hasAnswered){
            input = getInputInt(prompt);
            if ((input >= lowerBound) && (input <= higherBound)){
                hasAnswered = true;
            } else {
                System.out.println("Please enter an integer between " +
                        lowerBound + " and " + higherBound);
            }
        }

        return input;
    }
}
