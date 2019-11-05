package com.roomcreator.interfaces;

import com.roomcreator.roommakers.Furniture;

import java.util.ArrayList;
import java.util.TreeSet;

public interface OnFurnitureAdded {

    void getFloorFurniture(Integer number, String path, String mtlPath, Float width,
                           Float length, Float height, ArrayList<String> texturePaths);

    void getWallFurniture(Integer number, String path, String mtlPath, Float width,
                          Float length, Float height, ArrayList<String> texturePaths);

    void deleteFurniture(Integer number, TreeSet<Integer> removes);

    void getFloorFurnitureChanges(Integer number, Furniture furniture, Float width,
                                  Float length, Float height);

    void getWallFurnitureChanges(Integer number, Furniture furniture, Float width,
                                 Float length, Float height);

}
