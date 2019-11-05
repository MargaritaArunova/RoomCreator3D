package com.roomcreator.roommakers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;

/**
 * Стены
 */
public class Walls {
    private ModelBuilder modelBuilder;

    //путь к материалам стен
    public ArrayList<String> materialPaths;
    //значения цветов стен
    public ArrayList<Color> materialColors;
    //пары из значений ширины и длины фотографии в жизни
    public ArrayList<Pair> realWallMaterialsSizes;
    //координаты стен
    public ArrayList<Coordinates> coordinates;
    //мебель
    public ArrayList<Furniture>[] wallsFurniture;
    //пол комнаты
    public Floor floor;
    //высота стен
    public float height;

    public Walls() {

    }

    public Walls(float height, Floor floor) {
        this.height = height;
        this.floor = floor;

        createArray();
    }

    //создание массивов описывающих стены
    private void createArray() {
        materialPaths = new ArrayList<String>();
        materialColors = new ArrayList<Color>();
        realWallMaterialsSizes = new ArrayList<Pair>();
        wallsFurniture = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            materialPaths.add("");
            materialColors.add(null);
            realWallMaterialsSizes.add(new Pair(50f, 50f));
            wallsFurniture[i] = new ArrayList<Furniture>();
        }

        coordinates = new ArrayList<Coordinates>();
        //horizontal
        coordinates.add(new Coordinates(floor.width / 2 + 5f, height / 2, 0f, floor.length));
        coordinates.add(new Coordinates(-floor.width / 2 - 5f, height / 2, 0f, floor.length));
        //vertical
        coordinates.add(new Coordinates(0f, height / 2, floor.length / 2 + 5f, floor.width));
        coordinates.add(new Coordinates(0f, height / 2, -floor.length / 2 - 5f, floor.width));
    }

    //создание модели по значениям
    public Model generateWalls(Integer key) {
        modelBuilder = new ModelBuilder();
        switch (key) {
            case 1: //vertical
                return modelBuilder.createBox(height, floor.width + 20f, 10f,
                        new Material(),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
            case 2: //horizontal
                return modelBuilder.createBox(10f, floor.length + 20f, height,
                        new Material(),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        }
        return null;
    }

    //установка материала
    public void setWallMaterialPath(Integer number, String path) {
        materialPaths.set(number, path);
    }

    //установка цвета
    public void setWallMaterialColor(Integer number, Color wallColor) {
        materialColors.set(number, wallColor);
    }

    //установка настоящих размеров материала
    public void setRealMaterialsSizes(Integer number, float width, float height) {
        realWallMaterialsSizes.set(number, new Pair(width, height));
    }

}
