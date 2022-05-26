package com.example.roombacontrol;

import java.util.ArrayList;

public class RoombaSwarm {

    private ArrayList<Roomba> roombas = new ArrayList<>();

    public void addRoomba(Roomba roomba) throws RoombaAlreadyExistsException{

        // Check to see if Roomba has already been added
        for(int i = 0; i < roombas.size(); i++){
            if(roombas.get(i).getAddress() == roomba.getAddress()){
                throw new RoombaAlreadyExistsException();
            }
        }

        // Roomba has not been added yet, add to swarm
        roombas.add(roomba);
    }

    public ArrayList<Roomba> getRoombas() {
        return this.roombas;
    }

}
