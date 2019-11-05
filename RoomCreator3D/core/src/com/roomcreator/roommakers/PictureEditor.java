package com.roomcreator.roommakers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Класс, создающий обои по параметрам пользователей
 */
public class PictureEditor {
    private Texture texture;
    private TextureRegion textureRegion;

    public PictureEditor() {
    }

    public TextureRegion getImage(String picturePath, float realWidth, float realHeight, float neededWidth, float neededHeight) {
        texture = new Texture(Gdx.files.external(picturePath));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        //k - сколько кусков в ширину, m - сколько кусков в высоту
        float k = neededWidth / realWidth, m = neededHeight / realHeight;
        textureRegion = new TextureRegion(texture, 0, 0, (int) (texture.getWidth() * k), (int) (texture.getHeight() * m));

        return textureRegion;
    }

}
