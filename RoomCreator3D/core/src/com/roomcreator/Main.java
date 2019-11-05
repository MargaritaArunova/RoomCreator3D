package com.roomcreator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.roomcreator.screens.Menu;

import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Главный класс, который хранит значения переменных и получает сохранённые файлы
 **/
public class Main extends Game implements ApplicationListener {

	//значение языка
	public String language = "en";

	//массив значений на разных языках
	public LinkedHashMap<String, String> languages;

	//массив названий существующих комнат
	public HashSet<String> savedFilesNames = new HashSet<String>();

	@Override
	public void create() {
		FontMaker font = new FontMaker();
		languages = font.makeLanguagePreference(language);
		this.setScreen(new Menu(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
		FontMaker font = new FontMaker();
		languages = font.makeLanguagePreference(language);
	}

}
