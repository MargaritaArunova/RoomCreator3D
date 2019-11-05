package com.roomcreator.roommakers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;

/**
 * Мебель
 */
public class Furniture {
    public String path, mtlPath;
    public ArrayList<String> texturePaths;
    public float width, length, height;
    public float x, y, z;
    public int rotationX, rotationY, rotationZ;

    public Furniture() {

    }

    public Furniture(String path, String mtlPath, float width, float length, float height, float x, float y, float z,
                     int rotationX, int rotationY, int rotationZ, ArrayList<String> texturePaths) {
        this.path = path;
        this.mtlPath = mtlPath;
        this.width = width;
        this.length = length;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.texturePaths = texturePaths;
    }

    //установка параметров мебели
    public void setParameters(float width, float length, float height) {
        this.width = width;
        this.length = length;
        this.height = height;
    }

    //создание модели мебели по данным пользователя
    public ModelInstance createInstance() throws Exception {
        ObjLoader objLoader = new ObjLoader();
        for (String i : texturePaths) {
            if (!getFirstName(path).equals(getFirstName(i))) {
                Gdx.files.external(i).copyTo(Gdx.files.external(getFirstName(path) + getLastName(i)));
            }
        }
        if (!getFirstName(path).equals(getFirstName(mtlPath))){
            Gdx.files.external(mtlPath).copyTo(Gdx.files.external(getFirstName(path) + getLastName(mtlPath)));
        }
        TextureProvider textureProvider = new TextureProvider() {
            @Override
            public Texture load(String fileName) {
                Texture result = new Texture(Gdx.files.external(fileName));
                result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                result.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                return result;
            }
        };
        for (int i = 0; i < texturePaths.size(); i++) {
            textureProvider.load(texturePaths.get(i));
        }

        ModelInstance modelInstance = new ModelInstance(objLoader.loadModel(
                Gdx.files.external(path), textureProvider), x, y, z);

        if (modelInstance != null) {
            Vector3 vector3 = modelInstance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3());
            modelInstance.transform.setFromEulerAngles(rotationY, rotationX, rotationZ);
            modelInstance.transform.setTranslation(x, y, z);
            modelInstance.transform.scale(1f / vector3.x, 1f / vector3.y, 1f / vector3.z);
            modelInstance.transform.scale(width, height, length);
        }
        return modelInstance;
    }

    //получение имени модели
    public String getModelName() {
        String str = "";
        for (int i = path.length() - 1; i > 0; i--) {
            if (path.charAt(i) != '\\') {
                str = path.charAt(i) + str;
            } else {
                break;
            }
        }
        return str;
    }

    //получение имени файла
    public String getLastName(String path) {
        String str = "";
        for (int i = path.length() - 1; i > 0; i--) {
            if (path.charAt(i) != '\\') {
                str = path.charAt(i) + str;
            } else {
                break;
            }
        }
        return str;
    }

    //получение директории в которой находится файл
    public String getFirstName(String path) {
        for (int i = path.length() - 1; i >= 0; i--) {
            if (path.charAt(i) == '\\') {
                path = path.substring(0, i+1);
                break;
            }
        }
        return path;
    }

}
