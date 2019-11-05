package com.roomcreator.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * Класс, отвечающий за управление комнатой
 */
public class Joystick {
    private Skin joystickSkin;
    public Table joystick, zoom, furnitureJoystick, rotationJoystick;
    public Button left, right, up, down, plus, minus, arrowUp, arrowDown, arrowRight, arrowLeft, rotateX, rotateY, rotateZ, OK;
    public Slider slider;
    public Direction direction, modelDirection;


    public Joystick() {
        direction = Direction.STAY;

        joystickSkin = new Skin(Gdx.files.internal("skins\\joystick\\joystick.json"));

        left = new Button(joystickSkin.get("left", Button.ButtonStyle.class));
        right = new Button(joystickSkin.get("right", Button.ButtonStyle.class));
        up = new Button(joystickSkin.get("up", Button.ButtonStyle.class));
        down = new Button(joystickSkin.get("down", Button.ButtonStyle.class));

        rotateX = new Button(joystickSkin.get("rotate-z", Button.ButtonStyle.class));
        rotateY = new Button(joystickSkin.get("rotate-y", Button.ButtonStyle.class));
        rotateZ = new Button(joystickSkin.get("rotate-x", Button.ButtonStyle.class));

        arrowRight = new Button(joystickSkin.get("arrow-right", Button.ButtonStyle.class));
        arrowLeft = new Button(joystickSkin.get("arrow-left", Button.ButtonStyle.class));
        arrowUp = new Button(joystickSkin.get("arrow-up", Button.ButtonStyle.class));
        arrowDown = new Button(joystickSkin.get("arrow-down", Button.ButtonStyle.class));

        plus = new Button(joystickSkin.get("plus", Button.ButtonStyle.class));
        minus = new Button(joystickSkin.get("minus", Button.ButtonStyle.class));

        OK = new Button(joystickSkin.get("ok", Button.ButtonStyle.class));
        OK.setWidth(Gdx.graphics.getHeight() / 11);
        OK.setHeight(Gdx.graphics.getHeight() / 11);
        OK.setX(Gdx.graphics.getHeight() / 20);
        OK.setY(Gdx.graphics.getHeight() / 20);
        OK.setVisible(false);

        slider = new Slider(0f, 100, 1f, true,
                joystickSkin.get("default-vertical", Slider.SliderStyle.class));
        slider.getStyle().knob.setMinWidth(Gdx.graphics.getHeight() / 10);
        slider.getStyle().knob.setMinHeight(Gdx.graphics.getHeight() / 10);
        slider.getStyle().knobDown.setMinWidth(Gdx.graphics.getHeight() / 10);
        slider.getStyle().knobDown.setMinHeight(Gdx.graphics.getHeight() / 10);
        slider.setHeight(Gdx.graphics.getHeight() / 11 * 3 + 2f);
        slider.setX(Gdx.graphics.getWidth() - Gdx.graphics.getHeight() / 11 * 3 -Gdx.graphics.getHeight() / 9
                - Gdx.graphics.getHeight() / 30);
        slider.setY(Gdx.graphics.getHeight() / 160);
        slider.setValue(50f);
        slider.setDisabled(true);

        createZoom();
        createJoystick();
        createFurnitureJoystick();
    }

    //выключение джойстика
    public void disableJoystick(){
        furnitureJoystick.setVisible(true);
        OK.setVisible(true);
        joystick.setVisible(false);
        slider.setVisible(false);
    }

    //включение джойстика
    public void enableJoystick(){
        furnitureJoystick.setVisible(false);
        OK.setVisible(false);
        joystick.setVisible(true);
        slider.setVisible(true);
    }

    //создание кнопок для регулировки zoom
    public void createZoom(){
        zoom = new Table();
        zoom.add(plus).width(Gdx.graphics.getHeight() / 11).height(Gdx.graphics.getHeight() / 11)
                .pad(Gdx.graphics.getHeight() / 11).left();
        zoom.add(minus).width(Gdx.graphics.getHeight() / 11).height(Gdx.graphics.getHeight() / 11).right();

        zoom.background(joystickSkin.getDrawable("null"));
        zoom.getBackground().setMinWidth(Gdx.graphics.getHeight() / 11 * 2 + Gdx.graphics.getHeight() / 160);
        zoom.getBackground().setMinHeight(Gdx.graphics.getHeight() / 11 + Gdx.graphics.getHeight() / 160);
        zoom.setX(Gdx.graphics.getWidth() - Gdx.graphics.getHeight() / 11 * 2 - Gdx.graphics.getHeight() / 80);
        zoom.setY(Gdx.graphics.getHeight() / 160 + Gdx.graphics.getHeight() / 3);

        plus.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.PLUS;
                return true;
            }
        });

        minus.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.MINUS;
                return true;
            }
        });
    }

    //создание джойстика для движения комнаты
    public void createJoystick(){
        joystick = new Table();

        joystick.add();
        joystick.add(up).width(Gdx.graphics.getHeight() / 11).height(Gdx.graphics.getHeight() / 11);
        joystick.add();
        joystick.row();

        joystick.add(left).width(Gdx.graphics.getHeight() / 11).height(Gdx.graphics.getHeight() / 11);
        joystick.add();
        joystick.add(right).width(Gdx.graphics.getHeight() / 11).height(Gdx.graphics.getHeight() / 11);
        joystick.row();

        joystick.add();
        joystick.add(down).width(Gdx.graphics.getHeight() / 11).height(Gdx.graphics.getHeight() / 11);
        joystick.add();

        joystick.background(joystickSkin.getDrawable("null"));
        joystick.getBackground().setMinWidth(Gdx.graphics.getHeight() / 11 * 3 + Gdx.graphics.getHeight() / 160);
        joystick.getBackground().setMinHeight(Gdx.graphics.getHeight() / 11 * 3 + Gdx.graphics.getHeight() / 160);
        joystick.setX(Gdx.graphics.getWidth() - Gdx.graphics.getHeight() / 11 * 3 - Gdx.graphics.getHeight() / 80);
        joystick.setY(Gdx.graphics.getHeight() / 160);

        joystick.pack();

        left.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.LEFT;
                return true;
            }
        });

        right.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.RIGHT;
                return true;
            }
        });

        up.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.UP;
                return true;
            }
        });

        down.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                direction = Direction.DOWN;
                return true;
            }
        });

    }

    //создание джойстика для движения мебели
    public void createFurnitureJoystick(){
        furnitureJoystick = new Table();

        furnitureJoystick.add();
        furnitureJoystick.add(arrowUp).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10);
        furnitureJoystick.add();
        furnitureJoystick.row();
        furnitureJoystick.add(arrowLeft).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10);
        furnitureJoystick.add();
        furnitureJoystick.add(arrowRight).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10);
        furnitureJoystick.row();
        furnitureJoystick.add();
        furnitureJoystick.add(arrowDown).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10);
        furnitureJoystick.add();
        furnitureJoystick.row();
        furnitureJoystick.add(rotateX).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10)
                .pad(Gdx.graphics.getHeight()/100, Gdx.graphics.getHeight()/100,
                        Gdx.graphics.getHeight()/100, Gdx.graphics.getHeight()/100);
        furnitureJoystick.add(rotateY).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10)
                .pad(Gdx.graphics.getHeight()/100, Gdx.graphics.getHeight()/100,
                        Gdx.graphics.getHeight()/100, Gdx.graphics.getHeight()/100);;
        furnitureJoystick.add(rotateZ).width(Gdx.graphics.getHeight() / 10).height(Gdx.graphics.getHeight() / 10)
                .pad(Gdx.graphics.getHeight()/100, Gdx.graphics.getHeight()/100,
                        Gdx.graphics.getHeight()/100, Gdx.graphics.getHeight()/100);;

        furnitureJoystick.background(joystickSkin.getDrawable("null"));
        furnitureJoystick.getBackground().setMinWidth(Gdx.graphics.getHeight() / 10 * 3 + Gdx.graphics.getHeight() / 160);
        furnitureJoystick.getBackground().setMinHeight(Gdx.graphics.getHeight() / 10 * 3 + Gdx.graphics.getHeight() / 160);
        furnitureJoystick.setX(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 20 * 3);
        furnitureJoystick.setY(Gdx.graphics.getHeight() / 160);
        furnitureJoystick.setVisible(false);

        furnitureJoystick.pack();

        modelDirection = Direction.STAY;

        arrowUp.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.UP;
                return true;
            }
        });
        arrowDown.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.DOWN;
                return true;
            }
        });
        arrowLeft.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.LEFT;
                return true;
            }
        });
        arrowRight.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.RIGHT;
                return true;
            }
        });
        rotateX.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.ROTATE_X;
                return true;
            }
        });
        rotateY.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.ROTATE_Y;
                return true;
            }
        });
        rotateZ.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.STAY;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                modelDirection = Direction.ROTATE_Z;
                return true;
            }
        });
    }
}
