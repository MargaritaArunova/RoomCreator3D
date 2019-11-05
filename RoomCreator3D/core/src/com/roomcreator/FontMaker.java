package com.roomcreator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import java.util.LinkedHashMap;

/**
 * Класс создающий шрифт
 **/
public class FontMaker {

    //шрифт
    private BitmapFont font;

    //массив со значениями на текущем языке
    public LinkedHashMap<String, String> languages;

    public FontMaker() {
        FreeTypeFontGenerator freeTypeFontGenerator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts\\comic\\comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" +
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "ÄäßÖöÜü0123456789][_!$%#@|\\/?-+=()*&.:;,{}\"´`'<>";
        parameter.size = Gdx.graphics.getHeight() / 13;
        parameter.color = Color.WHITE;
        font = freeTypeFontGenerator.generateFont(parameter);
        freeTypeFontGenerator.dispose();
    }

    public LinkedHashMap<String, String> makeLanguagePreference(String language) {
        languages = new LinkedHashMap<String, String>();
        Array<XmlReader.Element> elementsLanguages;
        Array<XmlReader.Element> stringValues;

        XmlReader.Element mainElement = new XmlReader().parse(Gdx.files.internal("xml\\languages.xml"));
        elementsLanguages = mainElement.getChildrenByName("lang");
        for (int i = 0; i < elementsLanguages.size; i++) {
            if (elementsLanguages.get(i).getAttribute("key").equals(language)) {
                stringValues = elementsLanguages.get(i).getChildrenByName("string");
                for (int j = 0; j < stringValues.size; j++) {
                    languages.put(stringValues.get(j).getAttribute("key"), stringValues.get(j).getText());
                }
            }
        }
        return languages;
    }

    //метод, расшифровывающий сообщения, выводимые в диалоговых окнах
    public String decodeString(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') {
                str = str.substring(0, i) + "\n" + str.substring(i + 1);
            }
        }
        return str;
    }

    public BitmapFont getFontWithSize(int size) {
        FreeTypeFontGenerator freeTypeFontGenerator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts\\comic\\comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" +
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "ÄäẞßÖöÜü0123456789][_!$%#@|\\/?-+=()*&.:;,{}\"´`'<>";
        parameter.size = size;
        parameter.color = Color.WHITE;
        BitmapFont font = freeTypeFontGenerator.generateFont(parameter);
        freeTypeFontGenerator.dispose();
        return font;
    }

    public BitmapFont getFont() {
        return font;
    }

}
