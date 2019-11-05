package com.roomcreator.interfaces;

public interface OnDimensionChange {

    void getFloorDimensionChange(float length, float width,
                                 String material, float realMaterialWidth, float realMaterialLength);

    void getWallsDimensionChange(float height);

    void getFirstWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength);

    void getSecondWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength);

    void getThirdWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength);

    void getFourthWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength);

}
