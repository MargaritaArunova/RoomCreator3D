package com.roomcreator.roommakers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;

/**
 * Пол
 **/
public class Floor extends Model {
    private ModelBuilder modelBuilder;

    //путь к материалу пола
    public String materialPath = "";
    //значения цвета пола
    public Color floorColor = null;
    //длина и ширина пола
    public float width, length;
    //пара из значений ширины и длины фотографии в жизни
    public Pair realFloorMaterialSize;
    //мебель
    public ArrayList<Furniture> floorFurniture;

    //50f - 1 метр

    public Floor() {

    }

    public Floor(float width, float length) {
        if (width < length) {
            this.width = width;
            this.length = length;
        } else {
            this.width = length;
            this.length = width;
        }
        realFloorMaterialSize = new Pair(50f, 50f);
        floorFurniture = new ArrayList<Furniture>();
    }

    //создание модели по значениям
    public Model generateFloor() {
        modelBuilder = new ModelBuilder();
        return modelBuilder.createBox(this.width + 20f, 10f, this.length + 20f, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    //установка материала
    public void setFloorMaterialPath(String materialPath) {
        this.materialPath = materialPath;
    }

    //установка цвета
    public void setFloorMaterialColor(Color floorColor) {
        this.floorColor = floorColor;
    }

    //установка настоящих размеров материала
    public void setRealMaterialSize(float width, float height) {
        realFloorMaterialSize = new Pair(width, height);
    }

}
