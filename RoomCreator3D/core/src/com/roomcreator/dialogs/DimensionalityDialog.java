package com.roomcreator.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;
import com.roomcreator.interfaces.OnDimensionChange;
import com.roomcreator.screens.CreateRoom;

/**
 * Диалоговое окно с параметрами комнаты
 */
public class DimensionalityDialog extends Dialog {
    private Main main;
    private CreateRoom createRoom;
    private OnDimensionChange action;
    private FontMaker fontMaker;

    private TextButton OK;
    private Button edit, add;
    private Button[] edits, adds;
    private ImageButton multiply;
    private TextField INPUT1, INPUT2, pictureWidth, pictureLength;
    private TextField[] pictureSizes;
    private ScrollPane scrollPane;
    private Skin skin;
    private Table dimensionalityTable, inputTable, materialTable, furnitureTable, mainInput, editTable, floorSizesInputTable, floorEdit;
    private Table[] wallsPictureSizes, wallsEdits, informationTables, furnitureAdds;
    public Label material, information, wallNumberMaterial, wallNumberFurniture, inputException, exception;
    public Label[] labels, wallMaterials;
    private Boolean canPressed1 = false, canPressed2 = false, canHide = false, exceptionIsAdded = false, anExceptionHere = false,
            invalidExceptionIsAdded = false;
    private String text1 = "", text2 = "";
    private Integer key;
    public Integer currentMaterial;
    public String floorMaterialPath, floorPictureWidth = "", floorPictureLength = "";
    public String[] wallsMaterialPaths, texts;

    public DimensionalityDialog(String title, Skin skin, final Main main, final CreateRoom createRoom,
                                final OnDimensionChange action) {
        super(title, skin);
        this.main = main;
        this.createRoom = createRoom;
        this.action = action;
        this.skin = skin;

        fontMaker = new FontMaker();

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);

        material = new Label("", skin.get("dimension", Label.LabelStyle.class));
        inputException = new Label(main.languages.get("Input_Exception_1"), skin.get("exception", Label.LabelStyle.class));
        inputException.setVisible(false);
        exception = new Label("", skin.get("exception", Label.LabelStyle.class));
        exception.setAlignment(Align.center);
        exception.setVisible(false);

        labels = new Label[6];
        for (int i = 0; i < 6; i++) {
            labels[i] = new Label(main.languages.get("Dimension_" + String.valueOf(i + 1)), skin.get("null", Label.LabelStyle.class));
            labels[i].setAlignment(Align.center);
        }

        INPUT1 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                skin.get("default", TextField.TextFieldStyle.class));
        INPUT2 = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                skin.get("default", TextField.TextFieldStyle.class));
        pictureLength = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                skin.get("default", TextField.TextFieldStyle.class));
        pictureWidth = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                skin.get("default", TextField.TextFieldStyle.class));
        INPUT1.setBlinkTime(0.5f);
        INPUT2.setBlinkTime(0.5f);
        pictureLength.setBlinkTime(0.5f);
        pictureWidth.setBlinkTime(0.5f);
        INPUT1.setMaxLength(5);
        INPUT2.setMaxLength(5);
        pictureLength.setMaxLength(5);
        pictureWidth.setMaxLength(5);

        dimensionalityTable = new Table();
        inputTable = new Table();
        materialTable = new Table();
        mainInput = new Table();
        editTable = new Table();
        floorSizesInputTable = new Table();
        floorEdit = new Table();
        furnitureTable = new Table();

        edit = new Button(skin.get("edit", Button.ButtonStyle.class));
        add = new Button(skin.get("add", Button.ButtonStyle.class));

        wallsEdits = new Table[4];
        wallsPictureSizes = new Table[4];
        pictureSizes = new TextField[8];
        for (int i = 0; i < 4; i++) {
            wallsEdits[i] = new Table();
            wallsPictureSizes[i] = new Table();
            multiply = new ImageButton(this.getSkin().get("default", ImageButton.ImageButtonStyle.class));
            pictureSizes[2 * i] = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    skin.get("default", TextField.TextFieldStyle.class));
            pictureSizes[2 * i + 1] = new com.badlogic.gdx.scenes.scene2d.ui.TextField("",
                    skin.get("default", TextField.TextFieldStyle.class));
            wallsPictureSizes[i].add(pictureSizes[2 * i]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).left();
            wallsPictureSizes[i].add(multiply).width(Gdx.graphics.getHeight() / 24).height(Gdx.graphics.getHeight() / 24)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();
            wallsPictureSizes[i].add(pictureSizes[2 * i + 1]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).right();

            pictureSizes[2 * i].setBlinkTime(0.5f);
            pictureSizes[2 * i + 1].setBlinkTime(0.5f);
            pictureSizes[2 * i].setMaxLength(5);
            pictureSizes[2 * i + 1].setMaxLength(5);
        }

        floorMaterialPath = null;
        wallsMaterialPaths = new String[4];
        informationTables = new Table[4];
        furnitureAdds = new Table[4];
        edits = new Button[4];
        adds = new Button[4];
        texts = new String[8];
        for (int i = 0; i < 4; i++) {
            texts[2 * i] = "";
            texts[2 * i + 1] = "";
            wallsMaterialPaths[i] = null;
            edits[i] = new Button(skin.get("edit", Button.ButtonStyle.class));
            adds[i] = new Button(skin.get("add", Button.ButtonStyle.class));
            informationTables[i] = new Table();
            furnitureAdds[i] = new Table();
        }

        OK = new TextButton("OK", skin.get("default", TextButton.TextButtonStyle.class));
        OK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!anExceptionHere) {
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
                                        action.getFloorDimensionChange(Float.valueOf(text1) * 50f,
                                                Float.valueOf(text2) * 50f, floorMaterialPath,
                                                Float.valueOf(floorPictureWidth) * 50f,
                                                Float.valueOf(floorPictureLength) * 50f);
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
                                    action.getWallsDimensionChange(Float.valueOf(text1) * 50f);
                                    action.getFirstWallDimensionChange(wallsMaterialPaths[0],
                                            Float.valueOf(texts[0]) * 50f,
                                            Float.valueOf(texts[1]) * 50f);
                                    action.getSecondWallDimensionChange(wallsMaterialPaths[1],
                                            Float.valueOf(texts[2]) * 50f,
                                            Float.valueOf(texts[3]) * 50f);
                                    action.getThirdWallDimensionChange(wallsMaterialPaths[2],
                                            Float.valueOf(texts[4]) * 50f,
                                            Float.valueOf(texts[5]) * 50f);
                                    action.getFourthWallDimensionChange(wallsMaterialPaths[3],
                                            Float.valueOf(texts[6]) * 50f,
                                            Float.valueOf(texts[7]) * 50f);
                                }
                                break;
                            }
                        }
                    } else {
                        setExceptionText(1);
                        canHide = false;
                    }

                } else {
                    setInputException();
                }
                if (canHide) {
                    getContentTable().clear();
                    getButtonTable().clear();
                    hide(Actions.fadeOut(0.5f));
                }
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
        exceptionIsAdded = false;
        invalidExceptionIsAdded = false;

        inputTable.clear();
        materialTable.clear();
        mainInput.clear();
        dimensionalityTable.clear();
        editTable.clear();
        floorEdit.clear();
        floorSizesInputTable.clear();
        furnitureTable.clear();
        getTitleTable().clear();
        getContentTable().clear();

        text1 = "";
        text2 = "";
        inputException.setVisible(false);

        if (key == 1) { //пол
            this.getTitleLabel().setText(main.languages.get("Floor"));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();

            INPUT1.setText(String.valueOf(createRoom.floor.width / 50f));
            INPUT2.setText(String.valueOf(createRoom.floor.length / 50f));
            pictureWidth.setText(String.valueOf(createRoom.floor.realFloorMaterialSize.realMaterialWidth / 50f));
            pictureLength.setText(String.valueOf(createRoom.floor.realFloorMaterialSize.realMaterialHeight / 50f));
            text1 = INPUT1.getText();
            text2 = INPUT2.getText();
            floorPictureWidth = pictureWidth.getText();
            floorPictureLength = pictureLength.getText();
            canPressed1 = true;
            canPressed2 = true;

            if (!createRoom.furnitureAdded) {
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
            }else {
                INPUT1.setDisabled(true);
                INPUT2.setDisabled(true);
            }
            edit.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentMaterial = 0;

                    createRoom.fileChooser.createPane(3);
                    createRoom.fileChooser.show(createRoom.stage);

                    setVisible(false);
                }
            });
            pictureWidth.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    floorPictureWidth = pictureWidth.getText();
                    for (int i = 0; i < floorPictureWidth.length(); i++) {
                        if (floorPictureWidth.charAt(i) == ',') {
                            floorPictureWidth = floorPictureWidth.substring(0, i) + "."
                                    + floorPictureWidth.substring(i + 1, floorPictureWidth.length());
                        }
                        if (floorPictureWidth.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(floorPictureWidth);
                            anExceptionHere = false;
                            pictureWidth.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            anExceptionHere = true;
                            pictureWidth.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }
                    } else {
                        anExceptionHere = true;
                        pictureWidth.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });
            pictureLength.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    boolean l = true;
                    floorPictureLength = pictureLength.getText();
                    for (int i = 0; i < floorPictureLength.length(); i++) {
                        if (floorPictureLength.charAt(i) == ',') {
                            floorPictureLength = floorPictureLength.substring(0, i) + "."
                                    + floorPictureLength.substring(i + 1, floorPictureLength.length());
                        }
                        if (floorPictureLength.charAt(i) > 57) {
                            l = false;
                            break;
                        }
                    }
                    if (l) {
                        try {
                            Float.valueOf(floorPictureLength);
                            anExceptionHere = false;
                            pictureLength.setStyle(skin.get("default", TextField.TextFieldStyle.class));
                        } catch (Exception e) {
                            anExceptionHere = true;
                            pictureLength.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }
                    } else {
                        anExceptionHere = true;
                        pictureLength.setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                    }
                    return true;
                }
            });
            information = new Label(fontMaker.decodeString(main.languages.get("Input_4")),
                    this.getSkin().get("null", Label.LabelStyle.class));
            information.setAlignment(Align.center);

            if (createRoom.floor.materialPath != "")
                material.setText(getLastName(createRoom.floor.materialPath));
            else
                material.setText("Color");
            //width, height input
            inputTable.add(labels[0]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            inputTable.add(INPUT1).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            inputTable.row();
            inputTable.add(labels[1]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            inputTable.add(INPUT2).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            inputTable.background(this.getSkin().getDrawable("button-up"));

            mainInput.add(labels[4]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            mainInput.row();
            mainInput.add(inputTable).width(Gdx.graphics.getWidth() / 32 * 17)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            mainInput.row();
            mainInput.background(this.getSkin().getDrawable("scroll-up"));

            //material menu
            editTable.add(material).width(Gdx.graphics.getWidth() / 8 * 3).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            editTable.add(edit).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);

            floorSizesInputTable.add(pictureWidth).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).left();
            floorSizesInputTable.add(multiply).width(Gdx.graphics.getHeight() / 24).height(Gdx.graphics.getHeight() / 24)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();
            floorSizesInputTable.add(pictureLength).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).right();

            floorEdit.add(editTable);
            floorEdit.row();
            floorEdit.add(information);
            floorEdit.row();
            floorEdit.add(floorSizesInputTable);
            floorEdit.row();
            floorEdit.background(this.getSkin().getDrawable("button-up"));

            materialTable.add(labels[3]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            materialTable.row();
            materialTable.add(floorEdit).width(Gdx.graphics.getWidth() / 32 * 17);
            materialTable.row();
            materialTable.add(inputException);
            materialTable.background(this.getSkin().getDrawable("scroll-up"));

            furnitureTable.add(labels[5]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            furnitureTable.row();
            furnitureTable.add(add).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9);
            furnitureTable.background(this.getSkin().getDrawable("scroll-up"));

            add.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setVisible(false);
                    createRoom.furnitureDialog.setDesign(0, createRoom.floor.floorFurniture);
                    createRoom.furnitureDialog.show(createRoom.stage);
                }
            });

            dimensionalityTable.add(mainInput).width(Gdx.graphics.getWidth() / 16 * 9);
            dimensionalityTable.row().pad(Gdx.graphics.getHeight() / 50, 0f,
                    Gdx.graphics.getHeight() / 50, 0f);
            dimensionalityTable.add(materialTable).width(Gdx.graphics.getWidth() / 16 * 9);
            dimensionalityTable.row().pad(Gdx.graphics.getHeight() / 50, 0f,
                    Gdx.graphics.getHeight() / 50, 0f);
            dimensionalityTable.add(furnitureTable).width(Gdx.graphics.getWidth() / 16 * 9);

            scrollPane = new ScrollPane(dimensionalityTable, this.getSkin().get("default", ScrollPane.ScrollPaneStyle.class));
            scrollPane.setFadeScrollBars(false);

            this.getContentTable().row();
            this.getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() / 16 * 11).height(Gdx.graphics.getHeight() / 16 * 10)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 50,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);


        }
        if (key == 2) { //стена
            this.getTitleLabel().setText(main.languages.get("Wall"));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();

            INPUT1.setText(String.valueOf(createRoom.walls.height / 50f));
            text1 = INPUT1.getText();
            canPressed1 = true;

            if (!createRoom.furnitureAdded) {
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
            }else {
                INPUT1.setDisabled(true);
            }

            for (int i = 0; i < 4; i++) {
                final int k = i;
                edits[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        currentMaterial = k + 1;

                        createRoom.fileChooser.createPane(3);
                        createRoom.fileChooser.show(createRoom.stage);

                        setVisible(false);
                    }
                });
            }

            furnitureTable.add(labels[5]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            furnitureTable.row();
            furnitureTable.background(this.getSkin().getDrawable("scroll-up"));

            wallMaterials = new Label[4];
            for (int i = 0; i < 4; i++) {
                wallMaterials[i] = new Label("", this.getSkin().get("dimension", Label.LabelStyle.class));
                information = new Label(fontMaker.decodeString(main.languages.get("Input_4")),
                        this.getSkin().get("null", Label.LabelStyle.class));
                information.setAlignment(Align.center);
                wallNumberMaterial = new Label(main.languages.get("Number_" + String.valueOf(i + 1)) + " " + main.languages.get("Wall"),
                        this.getSkin().get("null", Label.LabelStyle.class));
                wallNumberMaterial.setAlignment(Align.center);
                wallNumberFurniture = new Label(main.languages.get("Number_" + String.valueOf(i + 1)) + " " + main.languages.get("Wall"),
                        this.getSkin().get("null", Label.LabelStyle.class));
                wallNumberFurniture.setAlignment(Align.center);

                if (!createRoom.walls.materialPaths.get(i).equals(""))
                    wallMaterials[i].setText(getLastName(createRoom.walls.materialPaths.get(i)));
                else
                    wallMaterials[i].setText("Color");

                wallsEdits[i].clear();
                wallsEdits[i].add(wallMaterials[i]).width(Gdx.graphics.getWidth() / 8 * 3).height(Gdx.graphics.getWidth() / 14)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                wallsEdits[i].add(edits[i]).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                wallsEdits[i].row();

                pictureSizes[2 * i].setText(String.valueOf(createRoom.walls.realWallMaterialsSizes.get(i).realMaterialWidth / 50f));
                pictureSizes[2 * i + 1].setText(String.valueOf(createRoom.walls.realWallMaterialsSizes.get(i).realMaterialHeight / 50f));
                texts[2 * i] = pictureSizes[2 * i].getText();
                texts[2 * i + 1] = pictureSizes[2 * i + 1].getText();

                final int m = i;
                pictureSizes[2 * i].addListener(new InputListener() {
                    @Override
                    public boolean keyTyped(InputEvent event, char character) {
                        boolean l = true;
                        texts[2 * m] = pictureSizes[2 * m].getText();
                        for (int i = 0; i < texts[2 * m].length(); i++) {
                            if (texts[2 * m].charAt(i) == ',') {
                                texts[2 * m] = texts[2 * m].substring(0, i) + "."
                                        + texts[2 * m].substring(i + 1, texts[2 * m].length());
                            }
                            if (texts[2 * m].charAt(i) > 57) {
                                l = false;
                                break;
                            }
                        }
                        if (l && texts[2 * m].length() != 0) {
                            try {
                                Float.valueOf(texts[2 * m]);
                                anExceptionHere = false;
                                pictureSizes[2 * m].setStyle(skin.get("default", TextField.TextFieldStyle.class));
                            } catch (Exception e) {
                                anExceptionHere = true;
                                pictureSizes[2 * m].setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                            }
                        } else {
                            anExceptionHere = true;
                            pictureSizes[2 * m].setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }
                        return true;
                    }
                });
                pictureSizes[2 * i + 1].addListener(new InputListener() {
                    @Override
                    public boolean keyTyped(InputEvent event, char character) {
                        boolean l = true;
                        texts[2 * m + 1] = pictureSizes[2 * m + 1].getText();
                        for (int i = 0; i < texts[2 * m + 1].length(); i++) {
                            if (texts[2 * m + 1].charAt(i) == ',') {
                                texts[2 * m + 1] = texts[2 * m + 1].substring(0, i) + "."
                                        + texts[2 * m + 1].substring(i + 1, texts[2 * m + 1].length());
                            }
                            if (texts[2 * m + 1].charAt(i) > 57) {
                                l = false;
                                break;
                            }
                        }
                        if (l && texts[2 * m + 1].length() != 0) {
                            try {
                                Float.valueOf(texts[2 * m + 1]);
                                anExceptionHere = false;
                                pictureSizes[2 * m + 1].setStyle(skin.get("default", TextField.TextFieldStyle.class));
                            } catch (Exception e) {
                                anExceptionHere = true;
                                pictureSizes[2 * m + 1].setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                            }
                        } else {
                            anExceptionHere = true;
                            pictureSizes[2 * m + 1].setStyle(skin.get("exception", TextField.TextFieldStyle.class));
                        }
                        return true;
                    }
                });

                informationTables[i].clear();
                informationTables[i].add(wallNumberMaterial);
                informationTables[i].row();
                informationTables[i].add(wallsEdits[i]);
                informationTables[i].row();
                informationTables[i].add(information);
                informationTables[i].row();
                informationTables[i].add(wallsPictureSizes[i]);
                informationTables[i].row();
                informationTables[i].background(this.getSkin().getDrawable("button-up"));

                furnitureAdds[i].clear();
                furnitureAdds[i].add(wallNumberFurniture);
                furnitureAdds[i].row();
                furnitureAdds[i].add(adds[i]).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9);
                furnitureAdds[i].background(skin.getDrawable("button-up"));

                final int k = i + 1;
                adds[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        setVisible(false);
                        createRoom.furnitureDialog.setDesign(k, createRoom.walls.wallsFurniture[k - 1]);
                        createRoom.furnitureDialog.show(createRoom.stage);
                    }
                });

                editTable.add(informationTables[i]).width(Gdx.graphics.getWidth() / 32 * 17)
                        .pad(0f, 0f, Gdx.graphics.getHeight() / 20, 0f);
                editTable.row();
                furnitureTable.add(furnitureAdds[i]).width(Gdx.graphics.getWidth() / 32 * 17)
                        .pad(0f, 0f, Gdx.graphics.getHeight() / 20, 0f);
                furnitureTable.row();
            }


            inputTable.add(labels[2]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            inputTable.add(INPUT1).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            inputTable.background(this.getSkin().getDrawable("button-up"));

            mainInput.add(labels[4]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            mainInput.row();
            mainInput.add(inputTable).width(Gdx.graphics.getWidth() / 32 * 17)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            mainInput.row();
            mainInput.background(this.getSkin().getDrawable("scroll-up"));

            materialTable.add(labels[3]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            materialTable.row();
            materialTable.add(editTable);
            materialTable.row();
            materialTable.add(inputException);
            materialTable.background(this.getSkin().getDrawable("scroll-up"));

            dimensionalityTable.add(mainInput).width(Gdx.graphics.getWidth() / 16 * 9);
            dimensionalityTable.row().pad(Gdx.graphics.getHeight() / 50, 0f,
                    Gdx.graphics.getHeight() / 50, 0f);
            dimensionalityTable.add(materialTable).width(Gdx.graphics.getWidth() / 16 * 9);
            dimensionalityTable.row().pad(Gdx.graphics.getHeight() / 50, 0f,
                    Gdx.graphics.getHeight() / 50, 0f);
            dimensionalityTable.add(furnitureTable).width(Gdx.graphics.getWidth() / 16 * 9);

            scrollPane = new ScrollPane(dimensionalityTable, this.getSkin().get("default", ScrollPane.ScrollPaneStyle.class));
            scrollPane.setFadeScrollBars(false);

            this.getContentTable().row();
            this.getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() / 16 * 11).height(Gdx.graphics.getHeight() / 16 * 10)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 50,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);

        }
        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();

    }

    //сообщение об ошибке
    private void setExceptionText(Integer e) {
        if (!exceptionIsAdded) {
            mainInput.add(exception).pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            exceptionIsAdded = true;
        }
        exception.setText(new FontMaker().decodeString(main.languages.get("Input_Exception_" + String.valueOf(e))));
        exception.setVisible(true);
        /*
         1. введено неверное занчение
         2. введено слишком большое значение
         3. введено слишком маленькое значение
         4. введено существующее имя
        */
    }

    //установка текста ошибки
    private void setInputException() {
        if (!invalidExceptionIsAdded) {
            inputException.setVisible(true);
            invalidExceptionIsAdded = true;
        }
        scrollPane.scrollTo(0, 0, 0, 0, true, true);
    }

    //получение имени материала
    public void setMaterialName(String path, Integer number) {
        switch (number) {
            case 0: {
                floorMaterialPath = path;
                material.setText(getLastName(path));
                break;
            }
            case 1: {
                wallsMaterialPaths[0] = path;
                wallMaterials[0].setText(getLastName(path));
                break;
            }
            case 2: {
                wallsMaterialPaths[1] = path;
                wallMaterials[1].setText(getLastName(path));
                break;
            }
            case 3: {
                wallsMaterialPaths[2] = path;
                wallMaterials[2].setText(getLastName(path));
                break;
            }
            case 4: {
                wallsMaterialPaths[3] = path;
                wallMaterials[3].setText(getLastName(path));
                break;
            }
        }
    }

    //получение имени
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

}
