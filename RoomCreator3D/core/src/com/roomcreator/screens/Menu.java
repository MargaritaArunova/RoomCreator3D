package com.roomcreator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.roomcreator.Main;

/**
 * Экран меню приложения. Данный экран содержит в себе 3 кнопки: выход,
 * чтобы пользователь мог выйти из приложения, создать новую комнату, настройки
 **/
public class Menu implements Screen {

    public Main main;
    private Skin skin;
    private Stage stage;
    private TextButton makeNewRoom, exit, settings;
    private Texture background;

    public Menu(final Main main) {
        this.main = main;

        background = new Texture("images\\menubackground.png");
        skin = new Skin(Gdx.files.internal("skins\\menu\\menu.json"));

        settings = new TextButton(main.languages.get("Settings"), skin.get("default", TextButton.TextButtonStyle.class));
        settings.setWidth(Gdx.graphics.getHeight() / 9 * 5);
        settings.setHeight(Gdx.graphics.getHeight() / 6);
        settings.setX(Gdx.graphics.getWidth() / 3);
        settings.setY(Gdx.graphics.getHeight() / 12 * 5 + Gdx.graphics.getHeight());
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.2f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        main.setScreen(new Settings(main));
                    }
                })));
            }
        });
        makeNewRoom = new TextButton(main.languages.get("Create new room"), skin.get("default", TextButton.TextButtonStyle.class));
        makeNewRoom.setWidth(Gdx.graphics.getHeight() / 9 * 5);
        makeNewRoom.setHeight(Gdx.graphics.getHeight() / 6);
        makeNewRoom.setX(Gdx.graphics.getWidth() / 3);
        makeNewRoom.setY(Gdx.graphics.getHeight() / 24 * 17 + Gdx.graphics.getHeight());
        makeNewRoom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(0.2f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        main.setScreen(new CreateRoom(main));
                    }
                })));

            }
        });
        exit = new TextButton(main.languages.get("Exit"), skin.get("default", TextButton.TextButtonStyle.class));
        exit.setWidth(Gdx.graphics.getHeight() / 9 * 5);
        exit.setHeight(Gdx.graphics.getHeight() / 6);
        exit.setX(Gdx.graphics.getWidth() / 3);
        exit.setY(Gdx.graphics.getHeight() / 8 + Gdx.graphics.getHeight());
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage = new Stage(new ScreenViewport());
        stage.addActor(makeNewRoom);
        stage.addActor(settings);
        stage.addActor(exit);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        stage.addAction(Actions.moveTo(0, -Gdx.graphics.getHeight(), 0.7f));
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
