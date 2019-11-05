package com.roomcreator.roommakers;

/**
 * Комната
 */
public class Room {
    //пол
    public Floor floor;
    //стены
    public Walls walls;

    public Room() {

    }

    public Room(Floor floor, Walls walls) {
        this.floor = floor;
        this.walls = walls;
    }

}
