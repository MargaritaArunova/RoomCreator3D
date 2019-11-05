package com.roomcreator.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;
import com.roomcreator.interfaces.OnFurnitureAdded;
import com.roomcreator.roommakers.Furniture;
import com.roomcreator.screens.CreateRoom;

import java.util.ArrayList;

/**
 * Диалоговое окно с параметрами мебели
 */
public class FurnitureInformationDialog extends Dialog {
    private Main main;
    private CreateRoom createRoom;
    private OnFurnitureAdded action;
    private FontMaker fontMaker;
    public Furniture furniture;

    private Skin skin;
    private ScrollPane scrollPane;
    private Table dimensionTable, nameTable, texturesTable, mainTable, pictureTable, mtlTable;
    private Button edit, add;
    private TextButton CANCEL, OK;
    public Label texture, mtl, mtlFile;
    public Label[] titles, dimensions;
    public ArrayList<Label> textures;
    public ArrayList<Button> deletes;
    public ArrayList<String> paths;
    public Integer numberOfTexture;
    public String modelPath, mtlPath;

    private Integer roomPart;
    private Boolean enableToClick, isNull;

    public FurnitureInformationDialog(String title, Skin skin, final Main main, final CreateRoom createRoom,
                                      final OnFurnitureAdded action) {
        super(title, skin);
        this.main = main;
        this.createRoom = createRoom;
        this.action = action;
        this.skin = skin;

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);

        add = new Button(skin.get("add", Button.ButtonStyle.class));
        edit = new Button(skin.get("edit", Button.ButtonStyle.class));
        edit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createRoom.inputDialog.setDesign(4);
                createRoom.inputDialog.show(createRoom.stage);
                setVisible(false);
            }
        });
        CANCEL = new TextButton(main.languages.get("ButtonCancel"), skin.get("default", TextButton.TextButtonStyle.class));
        CANCEL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getContentTable().clear();
                getButtonTable().clear();
                createRoom.furnitureDialog.setVisible(true);
                hide(Actions.fadeOut(0.5f));
            }
        });
        OK = new TextButton("OK", skin.get("default", TextButton.TextButtonStyle.class));
        OK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createRoom.furnitureDialog.hide();
                createRoom.dimensionalityDialog.hide();
                if (isNull) {
                    if (roomPart < 1) { //floor
                        action.getFloorFurniture(0, modelPath, mtlPath,
                                Float.valueOf(dimensions[1].getText().toString()),
                                Float.valueOf(dimensions[2].getText().toString()),
                                Float.valueOf(dimensions[3].getText().toString()), paths);
                        createRoom.dimensionalityDialog.hide();
                        createRoom.furnitureDialog.hide();
                        hide();
                    } else { //walls
                        action.getWallFurniture(roomPart, modelPath, mtlPath,
                                Float.valueOf(dimensions[1].getText().toString()),
                                Float.valueOf(dimensions[2].getText().toString()),
                                Float.valueOf(dimensions[3].getText().toString()), paths);
                        createRoom.dimensionalityDialog.hide();
                        createRoom.furnitureDialog.hide();
                        hide();
                    }
                } else {
                    if (roomPart < 1) { //floor
                        action.getFloorFurnitureChanges(0, furniture,
                                Float.valueOf(dimensions[1].getText().toString()),
                                Float.valueOf(dimensions[2].getText().toString()),
                                Float.valueOf(dimensions[3].getText().toString()));
                        createRoom.dimensionalityDialog.hide();
                        createRoom.furnitureDialog.hide();
                        hide();
                    } else { //walls
                        action.getWallFurnitureChanges(roomPart, furniture,
                                Float.valueOf(dimensions[1].getText().toString()),
                                Float.valueOf(dimensions[2].getText().toString()),
                                Float.valueOf(dimensions[3].getText().toString()));
                        createRoom.dimensionalityDialog.hide();
                        createRoom.furnitureDialog.hide();
                        hide();
                    }
                }
            }
        });

        texture = new Label(main.languages.get("Furniture_2"), skin.get("null", Label.LabelStyle.class));
        texture.setColor(Color.BLACK);
        texture.setAlignment(Align.center);
        mtl = new Label(main.languages.get("Format_3"), skin.get("null", Label.LabelStyle.class));
        mtl.setColor(Color.BLACK);
        mtl.setAlignment(Align.center);
        mtlFile = new Label(main.languages.get("Format_3"), skin.get("dimension", Label.LabelStyle.class));

        titles = new Label[4];
        for (int i = 0; i < 4; i++) {
            if (i < 1)
                titles[i] = new Label(main.languages.get("Furniture_" + String.valueOf(i + 1)),
                        skin.get("null", Label.LabelStyle.class));
            else
                titles[i] = new Label(main.languages.get("Dimension_" + String.valueOf(i)),
                        skin.get("null", Label.LabelStyle.class));
            titles[i].setColor(Color.BLACK);
            titles[i].setAlignment(Align.center);
        }

        dimensions = new Label[4];
        for (int i = 0; i < 4; i++) {
            if (i < 1)
                dimensions[i] = new Label(main.languages.get("Format_" + String.valueOf(i + 1)),
                        skin.get("dimension", Label.LabelStyle.class));
            else
                dimensions[i] = new Label("1", skin.get("dimension", Label.LabelStyle.class));
        }
        dimensionTable = new Table();
        for (int i = 1; i < 4; i++)
            dimensionTable.add(titles[i]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100);
        dimensionTable.row();
        for (int i = 1; i < 4; i++)
            dimensionTable.add(dimensions[i]).width(Gdx.graphics.getWidth() / 7).height(Gdx.graphics.getWidth() / 14)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100);
        dimensionTable.row();
        dimensionTable.add();
        dimensionTable.add(edit).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        dimensionTable.add();

        nameTable = new Table();
        nameTable.add(titles[0]).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                .pad(0, Gdx.graphics.getHeight() / 100,
                        0, Gdx.graphics.getHeight() / 100);
        nameTable.row();
        nameTable.add(dimensions[0]).width(Gdx.graphics.getWidth() / 1.7f).height(Gdx.graphics.getWidth() / 14)
                .pad(0, Gdx.graphics.getHeight() / 100,
                        0, Gdx.graphics.getHeight() / 100);

        textures = new ArrayList<Label>();
        deletes = new ArrayList<Button>();

        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25);
        this.getButtonTable().add(CANCEL).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
    }

    @Override
    public Dialog show(Stage stage) {
        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
        return super.show(stage);
    }

    //установка текущего дизайна
    public void setDesign(Integer roomPart, Furniture furniture) {
        this.roomPart = roomPart;
        this.getContentTable().clear();
        this.getButtonTable().clear();

        textures = new ArrayList<Label>();
        deletes = new ArrayList<Button>();
        paths = new ArrayList<String>();
        modelPath = "";
        mtlPath = "";
        texturesTable = new Table();
        mainTable = new Table();
        pictureTable = new Table();
        mtlTable = new Table();

        if (furniture != null) {
            this.furniture = furniture;
            isNull = false;
        }

        if (furniture == null) {
            isNull = true;
            enableToClick = true;
            dimensions[0].setText(main.languages.get("Format_" + String.valueOf(1)));
            dimensions[0].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (enableToClick) {
                        createRoom.fileChooser.createPane(2);
                        createRoom.fileChooser.show(createRoom.stage);
                        createRoom.fromFurnitureDialog = true;
                        setVisible(false);
                    }
                }
            });
            mtlFile.setText(main.languages.get("Format_3"));
            mtlFile.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (enableToClick) {
                        createRoom.fileChooser.createPane(4);
                        createRoom.fileChooser.show(createRoom.stage);
                        createRoom.fromFurnitureDialog = true;
                        setVisible(false);
                    }
                }
            });

            textures.add(new Label(main.languages.get("Format_2"), skin.get("dimension", Label.LabelStyle.class)));
            deletes.add(new ImageButton(skin.get("default", ImageButton.ImageButtonStyle.class)));
            texturesTable.add(textures.get(0)).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100);
            texturesTable.add(deletes.get(0)).width(Gdx.graphics.getHeight() / 14).height(Gdx.graphics.getHeight() / 14)
                    .pad(0, Gdx.graphics.getHeight() / 100,
                            0, Gdx.graphics.getHeight() / 100);
            texturesTable.row();
            textures.get(0).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (enableToClick) {
                        createRoom.fileChooser.createPane(3);
                        createRoom.fileChooser.show(createRoom.stage);
                        createRoom.fromFurnitureDialog = true;
                        numberOfTexture = 0;
                        setVisible(false);
                    }
                }
            });

            deletes.get(0).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (enableToClick) {
                        texturesTable.removeActor(textures.get(0));
                        texturesTable.removeActor(deletes.get(0));
                    }
                }
            });

            paths.add("");
            for (int i = 1; i < textures.size(); i++) {
                textures.add(new Label(paths.get(i), skin.get("dimension", Label.LabelStyle.class)));
                deletes.add(new ImageButton(skin.get("default", ImageButton.ImageButtonStyle.class)));
                texturesTable.add(textures.get(i)).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                        .pad(0, Gdx.graphics.getHeight() / 100,
                                0, Gdx.graphics.getHeight() / 100);
                texturesTable.add(deletes.get(i)).width(Gdx.graphics.getHeight() / 14).height(Gdx.graphics.getHeight() / 14)
                        .pad(0, Gdx.graphics.getHeight() / 100,
                                0, Gdx.graphics.getHeight() / 100);
                texturesTable.row();

                final int k = i;
                textures.get(i).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (enableToClick) {
                            createRoom.fileChooser.createPane(3);
                            createRoom.fileChooser.show(createRoom.stage);
                            createRoom.fromFurnitureDialog = true;
                            numberOfTexture = k;
                            setVisible(false);
                        }
                    }
                });

                deletes.get(i).addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (enableToClick) {
                            texturesTable.removeActor(textures.get(k));
                            texturesTable.removeActor(deletes.get(k));
                        }
                    }
                });
            }

            add.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    paths.add("");
                    textures.add(new Label(main.languages.get("Format_2"), skin.get("dimension", Label.LabelStyle.class)));
                    deletes.add(new ImageButton(skin.get("default", ImageButton.ImageButtonStyle.class)));
                    texturesTable.add(textures.get(textures.size() - 1)).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                            .pad(0, Gdx.graphics.getHeight() / 100,
                                    0, Gdx.graphics.getHeight() / 100);
                    texturesTable.add(deletes.get(textures.size() - 1)).width(Gdx.graphics.getHeight() / 14).height(Gdx.graphics.getHeight() / 14)
                            .pad(0, Gdx.graphics.getHeight() / 100,
                                    0, Gdx.graphics.getHeight() / 100);
                    texturesTable.row();

                    final int l = textures.size() - 1;
                    textures.get(l).addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            createRoom.fileChooser.createPane(3);
                            createRoom.fileChooser.show(createRoom.stage);
                            createRoom.fromFurnitureDialog = true;
                            numberOfTexture = l;
                            setVisible(false);
                        }
                    });

                    deletes.get(l).addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            texturesTable.removeActor(textures.get(l), true);
                            texturesTable.removeActor(deletes.get(l), true);
                        }
                    });
                }
            });

            pictureTable.add(texture).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14);
            pictureTable.row();
            pictureTable.add(texturesTable);
            pictureTable.row();
            pictureTable.add(add).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9).center();

            mtlTable.add(mtl).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14);
            mtlTable.row();
            mtlTable.add(mtlFile).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14);

            mainTable.add(nameTable).center();
            mainTable.row().pad(Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100);
            mainTable.add(dimensionTable).center();
            mainTable.row().pad(Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100);
            mainTable.add(mtlTable);
            mainTable.row().pad(Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100);
            mainTable.add(pictureTable);
        } else {
            isNull = false;
            enableToClick = false;

            paths = furniture.texturePaths;

            dimensions[0].setText(furniture.getModelName());//имя модели
            dimensions[1].setText(String.valueOf(furniture.width / 50f));
            dimensions[2].setText(String.valueOf(furniture.length / 50f));
            dimensions[3].setText(String.valueOf(furniture.height / 50f));

            mtlFile.setText(furniture.getLastName(furniture.mtlPath));

            for (int i = 0; i < furniture.texturePaths.size(); i++) {
                textures.add(new Label(furniture.getLastName(furniture.texturePaths.get(i)), skin.get("dimension", Label.LabelStyle.class)));
                texturesTable.add(textures.get(i)).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                        .pad(0, Gdx.graphics.getHeight() / 100,
                                0, Gdx.graphics.getHeight() / 100);
                texturesTable.row();
            }
            pictureTable.add(texture).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14);
            pictureTable.row();
            pictureTable.add(texturesTable);

            mtlTable.add(mtl).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14);
            mtlTable.row();
            mtlTable.add(mtlFile).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14);

            mainTable.add(nameTable);
            mainTable.row().pad(Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100);
            mainTable.add(dimensionTable);
            mainTable.row().pad(Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100);
            mainTable.add(mtlTable);
            mainTable.row().pad(Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100,
                    Gdx.graphics.getHeight() / 60, Gdx.graphics.getHeight() / 100);
            mainTable.add(pictureTable);
        }
        scrollPane = new ScrollPane(mainTable, this.getSkin().get("furniture-chooser", ScrollPane.ScrollPaneStyle.class));
        scrollPane.setFadeScrollBars(false);

        this.getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() / 16 * 11).height(Gdx.graphics.getHeight() / 16 * 11)
                .pad(0f, Gdx.graphics.getHeight() / 100,
                        0f, Gdx.graphics.getHeight() / 100);

        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25);
        this.getButtonTable().add(CANCEL).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
    }

}
