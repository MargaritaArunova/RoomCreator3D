package com.roomcreator.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;
import com.roomcreator.interfaces.OnInputDialogHide;
import com.roomcreator.screens.CreateRoom;

/**
 * Диалоговое окно для ввода параметров
 */
public class InputDialog extends Dialog {
    private TextButton OK, CANCEL;
    private TextField INPUT1, INPUT2, INPUT3;
    private ImageButton MULTIPLY, MULTIPLY1;
    private Label exception, width, length, height, instruction;
    private Skin skin;
    private Table table;
    private Boolean canPressed1 = false, canPressed2 = false, canPressed3 = false, canHide = false;
    private String text1 = "", text2 = "", text3 = "";
    private Integer key;
    private Main main;
    private CreateRoom createRoom;

    public InputDialog(String title, Skin skin, final Main main, final CreateRoom createRoom, final OnInputDialogHide action) {
        super(title, skin);
        this.main = main;
        this.skin = skin;

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);

        exception = new Label("", skin.get("exception", Label.LabelStyle.class));
        exception.setAlignment(Align.center);
        exception.setVisible(false);

        MULTIPLY = new ImageButton(this.getSkin().get("default", ImageButton.ImageButtonStyle.class));
        MULTIPLY1 = new ImageButton(this.getSkin().get("default", ImageButton.ImageButtonStyle.class));

        width = new Label(main.languages.get("Dimension_1"), skin.get("null", Label.LabelStyle.class));
        length = new Label(main.languages.get("Dimension_2"), skin.get("null", Label.LabelStyle.class));
        height = new Label(main.languages.get("Dimension_3"), skin.get("null", Label.LabelStyle.class));
        instruction = new Label("", skin.get("null", Label.LabelStyle.class));
        width.setColor(0f, 0f, 0f, 1f);
        length.setColor(0f, 0f, 0f, 1f);
        height.setColor(0f, 0f, 0f, 1f);
        instruction.setColor(0f, 0f, 0f, 1f);
        width.setAlignment(Align.center);
        length.setAlignment(Align.center);
        height.setAlignment(Align.center);
        instruction.setAlignment(Align.center);


        OK = new TextButton("OK", skin.get("default", TextButton.TextButtonStyle.class));
        OK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (canPressed1) {
                    switch (key) {
                        case 1: {
                            if (canPressed2) {
                                canHide = false;
                                if (Float.valueOf(text1) > 10f || Float.valueOf(text2) > 10f) {
                                    setExceptionText(2);
                                } else if (Float.valueOf(text1) < 2f || Float.valueOf(text2) < 2f) {
                                    setExceptionText(3);
                                } else {
                                    canHide = true;
                                    action.getParameters(Float.valueOf(text1), Float.valueOf(text2));
                                }
                            } else {
                                setExceptionText(1);
                                canHide = false;
                            }
                            break;
                        }
                        case 2: {
                            canHide = false;
                            if (Float.valueOf(text1) > 5f) {
                                setExceptionText(4);
                            } else if (Float.valueOf(text1) < 2f) {
                                setExceptionText(3);
                            } else {
                                canHide = true;
                                action.getParameters(Float.valueOf(text1));
                            }
                            break;
                        }
                        case 3: {
                            canHide = false;
                            if (main.savedFilesNames.contains(text1)) {
                                setExceptionText(5);
                            } else {
                                canHide = true;
                                action.getName(text1);
                            }
                            break;
                        }
                        case 4: {
                            canHide = false;
                            if (canPressed2 && canPressed3) {
                                float o1 = Float.valueOf(text1), o2 = Float.valueOf(text2), o3 = Float.valueOf(text3);
                                if (o1 > 5f || o2 > 5f || o3 > 5f) {
                                    setExceptionText(6);
                                } else if (o1 < 0.2f || o2 < 0.2f || o3 < 0.2f) {
                                    setExceptionText(7);
                                } else {
                                    canHide = true;
                                    action.getFurnitureDimensions(o1, o2, o3);
                                }
                            } else {
                                setExceptionText(1);
                            }
                            break;
                        }
                    }
                } else {
                    setExceptionText(1);
                    canHide = false;
                }
                if (canHide) {
                    getContentTable().clear();
                    getButtonTable().clear();
                    hide(Actions.fadeOut(0.5f));
                }
            }
        });

        CANCEL = new TextButton(main.languages.get("ButtonCancel"), skin.get("default", TextButton.TextButtonStyle.class));
        CANCEL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getContentTable().clear();
                getButtonTable().clear();
                if (key == 4) {
                    createRoom.furnitureInformationDialog.setVisible(true);
                }
                hide(Actions.fadeOut(0.5f));
            }
        });
    }

    @Override
    public Dialog show(Stage stage) {
        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
        return super.show(stage);
    }

    //установка текущего дизайна
    public void setDesign(Integer key) {
        this.key = key;
        canHide = false;
        canPressed1 = false;
        canPressed2 = false;

        this.getTitleLabel().clear();
        this.getContentTable().clear();
        this.getButtonTable().clear();

        instruction.setText(new FontMaker().decodeString(main.languages.get("Input_" + String.valueOf(key))));

        this.getContentTable().add(instruction)
                .pad(0f, Gdx.graphics.getHeight() / 100,
                        0f, Gdx.graphics.getHeight() / 100);
        this.getContentTable().row();

        exception = new Label("", skin.get("exception", Label.LabelStyle.class));
        exception.setAlignment(Align.center);
        exception.setVisible(false);

        if (key == 1) { //для ввода параметров пола
            INPUT1 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT2 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT1.setBlinkTime(0.5f);
            INPUT2.setBlinkTime(0.5f);
            INPUT1.setMaxLength(5);
            INPUT2.setMaxLength(5);

            table = new Table();

            table.add(width).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).left();
            table.add();
            table.add(length).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).right();
            table.row();
            table.add(INPUT1).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).left();
            table.add(MULTIPLY).width(Gdx.graphics.getHeight() / 24).height(Gdx.graphics.getHeight() / 24)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();
            table.add(INPUT2).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).right();
            this.getContentTable().add(table);
            this.getContentTable().row();

            INPUT1.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    text1 = INPUT1.getText();
                    for (int i = 0; i < text1.length(); i++) {
                        if (text1.charAt(i) == ',') {
                            text1 = text1.substring(0, i) + "." + text1.substring(i + 1, text1.length());
                        }
                        if (text1.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(text1);
                            canPressed1 = true;
                            INPUT1.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            canPressed1 = false;
                            INPUT1.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }

                    } else {
                        canPressed1 = false;
                        INPUT1.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });

            INPUT2.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    text2 = INPUT2.getText();
                    for (int i = 0; i < text2.length(); i++) {
                        if (text2.charAt(i) == ',') {
                            text2 = text2.substring(0, i) + "." + text2.substring(i + 1, text2.length());
                        }
                        if (text2.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(text2);
                            canPressed2 = true;
                            INPUT2.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            canPressed2 = false;
                            INPUT2.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }

                    } else {
                        canPressed2 = false;
                        INPUT2.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });
        }
        if (key == 2) { //для ввода параметров стены
            INPUT1 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT1.setBlinkTime(0.5f);
            INPUT1.setMaxLength(5);

            INPUT1.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    text1 = INPUT1.getText();
                    for (int i = 0; i < text1.length(); i++) {
                        if (text1.charAt(i) == ',') {
                            text1 = text1.substring(0, i) + "." + text1.substring(i + 1, text1.length());
                        }
                        if (text1.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(text1);
                            canPressed1 = true;
                            INPUT1.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            canPressed1 = false;
                            INPUT1.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }

                    } else {
                        canPressed1 = false;
                        INPUT1.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });
            table = new Table();
            table.add(height).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).center();
            table.row();
            table.add(INPUT1).width(Gdx.graphics.getHeight() / 4).height(Gdx.graphics.getHeight() / 8)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).center();
            this.getContentTable().add(table);
            this.getContentTable().row();
        }
        if (key == 3) { //для ввода названия комнаты
            INPUT1 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT1.setBlinkTime(0.5f);
            INPUT1.setMaxLength(32);

            INPUT1.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    text1 = INPUT1.getText();
                    if (text1.equals("") || text1.contains("/") || text1.contains("*") || text1.contains("\"")
                            || text1.contains(":") || text1.contains("?") || text1.contains("\\") || text1.contains("|")
                            || text1.contains("<") || text1.contains(">")) {
                        canPressed1 = false;
                    } else {
                        canPressed1 = true;
                    }
                    return true;
                }
            });

            this.getContentTable().row();
            this.getContentTable().add(INPUT1).width(Gdx.graphics.getHeight() / 4 * 3).height(Gdx.graphics.getHeight() / 14 * 3)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();
            this.getContentTable().row();
        }
        if (key == 4) { //для ввода параметров модели
            INPUT1 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT2 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT3 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    this.getSkin().get("default", TextField.TextFieldStyle.class));
            INPUT1.setBlinkTime(0.5f);
            INPUT2.setBlinkTime(0.5f);
            INPUT3.setBlinkTime(0.5f);
            INPUT1.setMaxLength(5);
            INPUT2.setMaxLength(5);
            INPUT3.setMaxLength(5);

            instruction.setFontScale(0.9f);
            width.setFontScale(0.9f);
            length.setFontScale(0.9f);
            height.setFontScale(0.9f);

            table = new Table();
            table.add(width).width(Gdx.graphics.getWidth() / 9).height(Gdx.graphics.getWidth() / 18)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).left();
            table.add();
            table.add(length).width(Gdx.graphics.getWidth() / 9).height(Gdx.graphics.getWidth() / 18)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).right();
            table.add();
            table.add(height).width(Gdx.graphics.getWidth() / 9).height(Gdx.graphics.getWidth() / 18)
                    .pad(0f, Gdx.graphics.getHeight() / 100,
                            0f, Gdx.graphics.getHeight() / 100).right();
            table.row();
            table.add(INPUT1).width(Gdx.graphics.getWidth() / 9).height(Gdx.graphics.getWidth() / 18)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100).left();
            table.add(MULTIPLY).width(Gdx.graphics.getHeight() / 24).height(Gdx.graphics.getHeight() / 24)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100).center();
            table.add(INPUT2).width(Gdx.graphics.getWidth() / 9).height(Gdx.graphics.getWidth() / 18)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100).right();
            table.add(MULTIPLY1).width(Gdx.graphics.getHeight() / 24).height(Gdx.graphics.getHeight() / 24)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100).center();
            table.add(INPUT3).width(Gdx.graphics.getWidth() / 9).height(Gdx.graphics.getWidth() / 18)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100).right();
            this.getContentTable().row();
            this.getContentTable().add(table);
            this.getContentTable().row();

            INPUT1.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    text1 = INPUT1.getText();
                    for (int i = 0; i < text1.length(); i++) {
                        if (text1.charAt(i) == ',') {
                            text1 = text1.substring(0, i) + "." + text1.substring(i + 1, text1.length());
                        }
                        if (text1.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(text1);
                            canPressed1 = true;
                            INPUT1.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            canPressed1 = false;
                            INPUT1.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }

                    } else {
                        canPressed1 = false;
                        INPUT1.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });

            INPUT2.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    text2 = INPUT2.getText();
                    for (int i = 0; i < text2.length(); i++) {
                        if (text2.charAt(i) == ',') {
                            text2 = text2.substring(0, i) + "." + text2.substring(i + 1, text2.length());
                        }
                        if (text2.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(text2);
                            canPressed2 = true;
                            INPUT2.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            canPressed2 = false;
                            INPUT2.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }

                    } else {
                        canPressed2 = false;
                        INPUT2.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });

            INPUT3.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    text3 = INPUT3.getText();
                    for (int i = 0; i < text3.length(); i++) {
                        if (text3.charAt(i) == ',') {
                            text3 = text3.substring(0, i) + "." + text3.substring(i + 1, text3.length());
                        }
                        if (text3.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(text3);
                            canPressed3 = true;
                            INPUT3.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            canPressed3 = false;
                            INPUT3.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }

                    } else {
                        canPressed3 = false;
                        INPUT3.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });

            exception.setFontScale(0.9f);
        }
        exception.setVisible(false);

        this.getContentTable().add(exception);
        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25).left();
        this.getButtonTable().add(CANCEL).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).right();
    }

    //установка сообщения об ошибке
    private void setExceptionText(Integer e) {
        exception.setText(new FontMaker().decodeString(main.languages.get("Input_Exception_" + String.valueOf(e))));
        exception.setVisible(true);
        /*
         1. введено неверное занчение
         2. введено слишком большое значение
         3. введено слишком маленькое значение
         4. введено существующее имя
        */
    }

}
