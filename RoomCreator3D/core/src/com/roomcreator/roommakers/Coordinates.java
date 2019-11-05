package com.roomcreator.roommakers;

/**
 * Класс, хранящий координаты и длину каждой стены
 */
public class Coordinates {
    public float x, y, z;
    public float length;

    public Coordinates(float x, float y, float z, float length) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.length = length;
    }

    public Coordinates() {

    }

}
