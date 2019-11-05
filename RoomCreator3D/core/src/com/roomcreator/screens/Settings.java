package com.roomcreator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;

/**
* Экран настроек приложения. Данный экран содержит в себе 2 основные кнопки: назад(в Меню),
* чтобы пользователь мог выйти из настроек, выбор языка
**/
public class Settings implements Screen {

    public Main main;
    private FontMaker fontMaker;

    private Skin skin;
    private Stage stage;
    private Button back;
    private Table table;
    private TextButton[] languages;
    private Label languageField;
    private Texture background;
    private String[] languageArray;

    public Settings(final Main main) {
        this.main = main;
        fontMaker = new FontMaker();

        languageArray = new String[]{"en", "ru", "de"};

        //создание заднего фона
        background = new Texture(Gdx.files.internal("images\\settingsbackground.png"));
        skin = new Skin(Gdx.files.internal("skins\\settings\\settings.json"));

        //создание кнопок, позволяющих пользователю выбрать язык приложения
        makeLanguageButtons(main.language);

        //создание кнопок с надписями "язык" на языке, выбранном пользователем
        languageField = new Label(main.languages.get("LanguageField"), skin.get("default", Label.LabelStyle.class));
        //создание кнопки "назад"
        back = new Button(skin.get("back", Button.ButtonStyle.class));

        //параметры кнопок: длина, ширина, расположение
        languageField.setWidth(Gdx.graphics.getHeight() / 3);
        languageField.setHeight(Gdx.graphics.getHeight() / 6);
        languageField.setAlignment(Align.center);
        languageField.setX(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 10);
        languageField.setY(-Gdx.graphics.getHeight() / 4);

        back.setWidth(Gdx.graphics.getHeight() / 32 * 5);
        back.setHeight(Gdx.graphics.getHeight() / 8);
        back.setX(Gdx.graphics.getHeight() / 80);
        back.setY(-Gdx.graphics.getHeight() / 8 - Gdx.graphics.getHeight() / 160);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        main.setScreen(new Menu(main));
                    }
                })));
            }
        });

        stage = new Stage(new ScreenViewport());
        stage.addActor(table);
        stage.addActor(languageField);
        stage.addActor(back);

        Gdx.input.setInputProcessor(stage);
    }

    //создание кнопок, позволяющих пользователю выбрать язык приложения
    private void makeLanguageButtons(final String language) {
        languages = new TextButton[3];

        for (int i = 0; i < 3; i++) {
            if (language.equals(languageArray[i])) {
                main.languages = fontMaker.makeLanguagePreference(language);
                languages[i] = new TextButton(fontMaker.makeLanguagePreference(languageArray[i]).get("Language"),
                        skin.get("language-checked", TextButton.TextButtonStyle.class));
            } else {
                languages[i] = new TextButton(fontMaker.makeLanguagePreference(languageArray[i]).get("Language"),
                        skin.get("language-unchecked", TextButton.TextButtonStyle.class));
            }
        }

        table = new Table();
        for (int i = 0; i < 3; i++) {
            table.add(languages[i]).width(Gdx.graphics.getHeight() / 8 * 3).height(Gdx.graphics.getHeight() / 8)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 30,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 30);
        }
        table.setX(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 3 * 2);
        table.setY(-Gdx.graphics.getHeight() / 28 * 13);
        table.pack();

        languages[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeStyle(0);
                main.language = "en";
                main.languages = fontMaker.makeLanguagePreference("en");

                languageField.setText("Language:");
            }
        });
        languages[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeStyle(1);
                main.language = "ru";
                main.languages = fontMaker.makeLanguagePreference("ru");

                languageField.setText("Язык:");

            }
        });
        languages[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeStyle(2);
                main.language = "de";
                main.languages = fontMaker.makeLanguagePreference("de");

                languageField.setText("Sprache:");
            }
        });

    }

    private void changeStyle(Integer key) {
        for (int i = 0; i < 3; i++) {
            if (i == key) {
                languages[i].setStyle(skin.get("language-checked", TextButton.TextButtonStyle.class));
            } else {
                languages[i].setStyle(skin.get("language-unchecked", TextButton.TextButtonStyle.class));
            }
        }
    }


    @Override
    public void show() {
        stage.addAction(Actions.moveTo(0, Gdx.graphics.getHeight(), 0.7f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}