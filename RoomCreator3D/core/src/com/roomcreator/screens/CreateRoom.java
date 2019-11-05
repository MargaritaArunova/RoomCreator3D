package com.roomcreator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;
import com.roomcreator.controllers.Direction;
import com.roomcreator.controllers.Joystick;
import com.roomcreator.dialogs.DimensionalityDialog;
import com.roomcreator.dialogs.ExceptionDialog;
import com.roomcreator.dialogs.FileChooser;
import com.roomcreator.dialogs.FurnitureDialog;
import com.roomcreator.dialogs.FurnitureInformationDialog;
import com.roomcreator.dialogs.InformDialog;
import com.roomcreator.dialogs.InputDialog;
import com.roomcreator.interfaces.OnDimensionChange;
import com.roomcreator.interfaces.OnFileClick;
import com.roomcreator.interfaces.OnFurnitureAdded;
import com.roomcreator.interfaces.OnInformDialogHide;
import com.roomcreator.interfaces.OnInputDialogHide;
import com.roomcreator.roommakers.Floor;
import com.roomcreator.roommakers.Furniture;
import com.roomcreator.roommakers.Pair;
import com.roomcreator.roommakers.PictureEditor;
import com.roomcreator.roommakers.Room;
import com.roomcreator.roommakers.Walls;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Экран создания кнопок приложения. Данный экран содержит в себе кнопки, необходимые для
 * создания комнаты
 **/
public class CreateRoom implements Screen, OnInputDialogHide, OnInformDialogHide, OnFileClick, OnDimensionChange, OnFurnitureAdded {

    public Main main;

    //диалоговое окно для вывода информации
    public InformDialog informDialog;
    //диалоговое окно для ввода значений
    public InputDialog inputDialog;
    //диалоговое окно для вывода сообщения об ошибках
    public ExceptionDialog exceptionDialog;
    //диалоговое окно для вывода информации о частях комнаты
    public DimensionalityDialog dimensionalityDialog;
    //диалоговое окно для просмотра и создания мебели
    public FurnitureDialog furnitureDialog;
    //диалоговое окно для вывода информации для создания мебели
    public FurnitureInformationDialog furnitureInformationDialog;
    //диалоговое окно для выбора файла
    public FileChooser fileChooser;
    //пол
    public Floor floor;
    //стены
    public Walls walls;
    //мебель
    private Furniture currentFurniture;
    //комната
    private Room room;
    //джойстик для управления комнатой
    private Joystick joystick;
    //создатель шрифта
    private FontMaker fontMaker;
    //создатель картинок: обоев, пола
    private PictureEditor pictureEditor;


    public Stage stage;
    private Json json;
    private ModelInstance floorInstance, currentFurnitureInstance;
    private ArrayList<ModelInstance> wallInstances, floorFurniture;
    private ArrayList<ModelInstance>[] wallsFurniture;
    private ArrayList<Material> wallMaterials, newMaterials;
    private ArrayList<BlendingAttribute> blendingAttributes;
    private ArrayList<String> newWallMaterialPaths;
    private ArrayList<Color> newWallMaterialColors;
    private ArrayList<Pair> newRealWallMaterialsSizes;
    private Environment environment;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Vector3 vector3, positionVector, dimensionVector, modelDimensionsVector;
    private Ray xRay;
    private Table table, roomEditTable;
    private Skin skin, dialogSkin;
    private Button add, back, importRoom, saveRoom, createWalls, createFloor, editFloor, editWalls;
    private TextButton importRoomText, saveRoomText, createWallsText, createFloorText;
    private FileHandle fileHandle;
    //переменные для установки камеры, мебели
    private float X = -200f, Y = 200f, Z = 0f, viewAt = 50f, rotation = 0f, w = 0f, h = 0f, l = 0f,
            furnitureX = 0f, furnitureY = 0f, furnitureZ = 0f, partDimension = 0f;
    //переменные для установки мебели
    private Integer currentFurniturePart = -1, furnitureRotationX = 0, furnitureRotationY = 0, furnitureRotationZ = 0,
            unusedPart = -1;
    //переменные для определения функционала приложения в текущий момент
    private Boolean floorIsCreated = false, wallsIsCreated = false, roomIsImported = false, changesIsSaved = true,
            tableIsShown = false, enableMoving = true, canBeRendered = false;
    public Boolean fromFurnitureDialog = false, furnitureAdded = false;

    public CreateRoom(final Main main) {
        this.main = main;

        joystick = new Joystick();
        fontMaker = new FontMaker();
        pictureEditor = new PictureEditor();
        floor = null;
        walls = null;

        skin = new Skin(Gdx.files.internal("skins\\createroom\\createroom.json"));

        back = new Button(skin.get("back", Button.ButtonStyle.class));
        back.setWidth(Gdx.graphics.getHeight() / 8);
        back.setHeight(Gdx.graphics.getHeight() / 10);
        back.setX(Gdx.graphics.getHeight() / 80);
        back.setY(Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 10 - Gdx.graphics.getHeight() / 160);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (enableMoving) {
                    if (changesIsSaved) {
                        stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                main.setScreen(new Menu(main));
                            }
                        })));
                    } else {
                        informDialog.show(stage);
                    }
                }
            }
        });
        add = new Button(skin.get("add", Button.ButtonStyle.class));
        add.setWidth(Gdx.graphics.getHeight() / 11);
        add.setHeight(Gdx.graphics.getHeight() / 11);
        add.setX(Gdx.graphics.getHeight() / 80);
        add.setY(Gdx.graphics.getHeight() / 160);
        add.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enableMoving) {
                    if (!tableIsShown)
                        table.addAction(Actions.moveBy(Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    if (tableIsShown)
                        table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = !tableIsShown;
                }
            }
        });

        createJoystick();
        createTable();

        editFloor = new Button(skin.get("createfloor", Button.ButtonStyle.class));
        editWalls = new Button(skin.get("createwalls", Button.ButtonStyle.class));

        roomEditTable = new Table();
        roomEditTable.setX(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8);
        roomEditTable.setY(Gdx.graphics.getHeight());
        roomEditTable.background(skin.getDrawable("edittable-background"));
        roomEditTable.getBackground().setMinWidth(Gdx.graphics.getWidth() / 4);
        roomEditTable.getBackground().setMinHeight(Gdx.graphics.getHeight() / 10);
        roomEditTable.pack();

        environment = new com.badlogic.gdx.graphics.g3d.Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.add(new PointLight().set(1f, 1f, 1f, 0f, 250f, 0f, 20000f));
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(X, Y, Z);
        camera.lookAt(0f, viewAt, 0f);
        camera.near = 1f;
        camera.far = 10000f;
        camera.update();

        xRay = new Ray(new Vector3(0f, 0f, 0f), new Vector3(-1f, 0.1f, 0f));
        vector3 = new Vector3();
        json = new Json();

        modelBatch = new ModelBatch();
        wallInstances = new ArrayList<ModelInstance>();
        wallsFurniture = new ArrayList[4];
        floorFurniture = new ArrayList<ModelInstance>();
        newWallMaterialColors = new ArrayList<Color>();
        newWallMaterialPaths = new ArrayList<String>();

        for (int i = 0; i < 4; i++) {
            wallsFurniture[i] = new ArrayList<ModelInstance>();
        }

        stage = new Stage();
        stage.addActor(back);
        stage.addActor(add);
        stage.addActor(table);
        stage.addActor(roomEditTable);
        stage.addActor(joystick.joystick);
        stage.addActor(joystick.zoom);
        stage.addActor(joystick.slider);
        stage.addActor(joystick.furnitureJoystick);
        stage.addActor(joystick.OK);

        Gdx.input.setInputProcessor(stage);
    }

    //создание таблицы с базовыми кнопками
    private void createTable() {
        importRoomText = new TextButton(main.languages.get("Import"), skin.get("default", TextButton.TextButtonStyle.class));
        importRoomText.getLabel().setAlignment(Align.left);

        saveRoomText = new TextButton(main.languages.get("Save"), skin.get("default", TextButton.TextButtonStyle.class));
        saveRoomText.getLabel().setAlignment(Align.left);

        createWallsText = new TextButton(main.languages.get("Create walls"), skin.get("default", TextButton.TextButtonStyle.class));
        createWallsText.getLabel().setAlignment(Align.left);

        createFloorText = new TextButton(main.languages.get("Create floor"), skin.get("default", TextButton.TextButtonStyle.class));
        createFloorText.getLabel().setAlignment(Align.left);

        importRoom = new Button(skin.get("import", Button.ButtonStyle.class));
        saveRoom = new Button(skin.get("save", Button.ButtonStyle.class));
        createWalls = new Button(skin.get("createwalls", Button.ButtonStyle.class));
        createFloor = new Button(skin.get("createfloor", Button.ButtonStyle.class));

        table = new Table();
        table.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 50,
                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        table.add(importRoom).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160);
        table.add(importRoomText).width(Gdx.graphics.getHeight() / 12 * 5).height(Gdx.graphics.getHeight() / 12);
        table.row().pad(Gdx.graphics.getHeight() / 100);
        table.add(saveRoom).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160);
        table.add(saveRoomText).width(Gdx.graphics.getHeight() / 12 * 5).height(Gdx.graphics.getHeight() / 12);
        table.row().pad(Gdx.graphics.getHeight() / 100);
        table.add(createWalls).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160);
        table.add(createWallsText).width(Gdx.graphics.getHeight() / 12 * 5).height(Gdx.graphics.getHeight() / 12);
        table.row().pad(Gdx.graphics.getHeight() / 100);
        table.add(createFloor).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160);
        table.add(createFloorText).width(Gdx.graphics.getHeight() / 12 * 5).height(Gdx.graphics.getHeight() / 12);

        table.setX(-Gdx.graphics.getHeight() / 3 * 2 + Gdx.graphics.getHeight() / 160);
        table.setY(Gdx.graphics.getHeight() / 8);
        table.background(skin.getDrawable("table-background"));
        table.getBackground().setMinWidth(Gdx.graphics.getHeight() / 8 * 5);
        table.getBackground().setMinHeight(Gdx.graphics.getHeight() / 2);
        table.pack();

        createDialog();

        importRoom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (floorIsCreated || roomIsImported) {
                    exceptionDialog.setText(1);
                    exceptionDialog.show(stage);
                } else {
                    fileChooser.createPane(1);
                    fileChooser.show(stage);
                }
            }
        });
        importRoomText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (floorIsCreated || roomIsImported) {
                    exceptionDialog.setText(1);
                    exceptionDialog.show(stage);
                } else {
                    fileChooser.createPane(1);
                    fileChooser.show(stage);
                }
            }
        });
        saveRoom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changesIsSaved = false;
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (!floorIsCreated) {
                    exceptionDialog.setText(5);
                    exceptionDialog.show(stage);
                } else {
                    inputDialog.setDesign(3);
                    inputDialog.show(stage);
                }

            }
        });
        saveRoomText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (!floorIsCreated) {
                    exceptionDialog.setText(5);
                    exceptionDialog.show(stage);
                } else {
                    inputDialog.setDesign(3);
                    inputDialog.show(stage);
                }
            }
        });
        createWalls.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (!floorIsCreated || wallsIsCreated || roomIsImported) {
                    exceptionDialog.setText(3);
                    exceptionDialog.show(stage);
                } else {
                    inputDialog.setDesign(2);
                    inputDialog.show(stage);
                }
            }
        });
        createWallsText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (!floorIsCreated || wallsIsCreated || roomIsImported) {
                    exceptionDialog.setText(3);
                    exceptionDialog.show(stage);
                } else {
                    inputDialog.setDesign(2);
                    inputDialog.show(stage);
                }
            }
        });
        createFloor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (floorIsCreated || roomIsImported) {
                    exceptionDialog.setText(2);
                    exceptionDialog.show(stage);
                } else {
                    inputDialog.setDesign(1);
                    inputDialog.show(stage);
                }
            }
        });
        createFloorText.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (floorIsCreated || roomIsImported) {
                    exceptionDialog.setText(2);
                    exceptionDialog.show(stage);
                } else {
                    inputDialog.setDesign(1);
                    inputDialog.show(stage);
                }
            }
        });
    }

    //создание диалоговых окон
    private void createDialog() {

        dialogSkin = new Skin(Gdx.files.internal("skins\\dialog\\dialog.json"));

        exceptionDialog = new ExceptionDialog("", dialogSkin, main);
        inputDialog = new InputDialog("", dialogSkin, main, this, this);
        informDialog = new InformDialog("", dialogSkin, main, this);
        dimensionalityDialog = new DimensionalityDialog("", dialogSkin, main, this, this);
        furnitureDialog = new FurnitureDialog("", dialogSkin, main, this, this);
        furnitureInformationDialog = new FurnitureInformationDialog("", dialogSkin, main, this, this);
        fileChooser = new FileChooser("", dialogSkin, main, this, this);
    }

    //создание джойстика
    private void createJoystick() {
        joystick.slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (tableIsShown) {
                    table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                    tableIsShown = false;
                }
                if (enableMoving) {
                    camera.translate(0f, -Y + -50f + 3 * joystick.slider.getValue(), 0f);
                    Y = -50f + 3 * joystick.slider.getValue();
                    viewAt = Y;
                    camera.lookAt(0f, viewAt, 0f);
                    camera.update();
                }
            }
        });
        joystick.OK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!enableMoving) {
                    enableButtons();
                    canBeRendered = false;
                    switch (currentFurniturePart) {
                        case 0: {
                            floor.floorFurniture.add(new Furniture(
                                    currentFurniture.path, currentFurniture.mtlPath,
                                    currentFurniture.width, currentFurniture.length, currentFurniture.height,
                                    furnitureX, furnitureY, furnitureZ,
                                    furnitureRotationX, furnitureRotationY, furnitureRotationZ,
                                    currentFurniture.texturePaths));
                            try {
                                floorFurniture.add(floor.floorFurniture.get(floor.floorFurniture.size() - 1).createInstance());
                            } catch (Exception e) {
                            }
                            currentFurnitureInstance = null;
                            currentFurniturePart = -1;
                            camera.translate(X, Y - 500f, Z);
                            furnitureAdded = true;
                            break;
                        }
                        default: {
                            walls.wallsFurniture[currentFurniturePart - 1].add(new Furniture(
                                    currentFurniture.path, currentFurniture.mtlPath,
                                    currentFurniture.width, currentFurniture.length, currentFurniture.height,
                                    furnitureX, furnitureY, furnitureZ,
                                    furnitureRotationX, furnitureRotationY, furnitureRotationZ,
                                    currentFurniture.texturePaths));
                            try {
                                wallsFurniture[currentFurniturePart - 1].add(walls.wallsFurniture[currentFurniturePart - 1]
                                        .get(walls.wallsFurniture[currentFurniturePart - 1].size() - 1).createInstance());
                            } catch (Exception e) {
                            }
                            currentFurnitureInstance = null;
                            currentFurniturePart = -1;
                            camera.translate(0f, Y - walls.height / 2, 0f);
                            furnitureAdded = true;
                        }
                    }
                }
            }
        });
    }

    //выключение кнопок
    private void disableButtons() {
        add.setVisible(false);
        back.setVisible(false);
        roomEditTable.setVisible(false);

        joystick.disableJoystick();

        enableMoving = false;
    }

    //включение кнопок
    private void enableButtons() {
        add.setVisible(true);
        back.setVisible(true);
        roomEditTable.setVisible(true);

        joystick.enableJoystick();

        enableMoving = true;
    }

    //движение комнаты
    private void move() {
        for (int i = 0; i < wallInstances.size(); i++) {
            xRay = new Ray(new Vector3(0f, 0f, 0f), new Vector3(-camera.direction.x,
                    0f, -camera.direction.z));
            vector3 = wallInstances.get(i).transform.getTranslation(vector3);
            vector3.y = walls.coordinates.get(i).x / 2;
            if (Intersector.intersectRaySphere(xRay, vector3,
                    walls.coordinates.get(i).length / 2 - 5f, null)) {
                setWallOpacity(i);
                unusedPart = i;
            }
        }
        if (joystick.direction != Direction.STAY && tableIsShown) {
            table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
            tableIsShown = !tableIsShown;
        }
        if (enableMoving) {
            if (floorIsCreated) {
                if (joystick.direction == Direction.LEFT) {
                    camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, -1f);
                    rotation--;
                }
                if (joystick.direction == Direction.RIGHT) {
                    camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, 1f);
                    rotation++;
                }
                if (joystick.direction == Direction.DOWN) {
                    viewAt = 50f;
                    this.Y -= 7f;
                    camera.translate(0f, -7f, 0f);
                }
                if (joystick.direction == Direction.UP) {
                    viewAt = 50f;
                    this.Y += 7f;
                    camera.translate(0f, 7f, 0f);
                }
                rotation = rotation % 360;
                camera.lookAt(0f, viewAt, 0f);
            }
        }
        if (joystick.direction == Direction.PLUS && camera.fieldOfView > 5f) {
            camera.fieldOfView--;
        }
        if (joystick.direction == Direction.MINUS && camera.fieldOfView < 175f) {
            camera.fieldOfView++;
        }
        camera.update();
    }

    //движение комнаты до определённого состояния
    private void moveTo(Integer number) {
        switch (number) {
            case 0: {
                camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, -rotation);
                camera.translate(-X, -Y + 500f, -Z);
                camera.lookAt(0f, 0f, 0f);
                camera.update();
                rotation = 0f;
                break;
            }
            case 1: {
                camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, -rotation);
                camera.translate(0f, -Y + walls.height / 2, 0f);
                camera.lookAt(0f, walls.height / 2, 0f);
                camera.update();
                rotation = 0f;
                break;
            }
            case 2: {
                camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, -rotation + 180f);
                camera.translate(0f, -Y + walls.height / 2, 0f);
                camera.lookAt(0f, walls.height / 2, 0f);
                camera.update();
                rotation = 180f;
                break;
            }
            case 3: {
                camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, -rotation - 90f);
                camera.translate(0f, -Y + walls.height / 2, 0f);
                camera.lookAt(0f, walls.height / 2, 0f);
                camera.update();
                rotation = 270f;
                break;
            }
            case 4: {
                camera.rotateAround(new Vector3(0f, viewAt, 0f), Vector3.Y, -rotation + 90f);
                camera.translate(0f, -Y + walls.height / 2, 0f);
                camera.lookAt(0f, walls.height / 2, 0f);
                camera.update();
                rotation = 90f;
                break;
            }
        }
        camera.update();
    }

    //движение мебели
    private void moveFurniture() {
        if (currentFurnitureInstance != null) {
            if (!enableMoving) {
                positionVector = currentFurnitureInstance.transform.getTranslation(new Vector3());
                if (currentFurniturePart < 3) {
                    partDimension = floor.length / 2;
                } else {
                    partDimension = floor.width / 2;
                }
                switch (currentFurniturePart) {
                    case 0: {
                        if (joystick.modelDirection == Direction.LEFT &&
                                positionVector.z - dimensionVector.z / 2f >= -floor.length / 2) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY, furnitureZ - 1f);
                            furnitureZ -= 1f;
                        }
                        if (joystick.modelDirection == Direction.RIGHT &&
                                positionVector.z + dimensionVector.z / 2f <= floor.length / 2) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY, furnitureZ + 1f);
                            furnitureZ += 1f;
                        }
                        if (joystick.modelDirection == Direction.DOWN &&
                                positionVector.x - dimensionVector.x / 2f >= -floor.width / 2) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX - 1f, furnitureY, furnitureZ);
                            furnitureX -= 1f;
                        }
                        if (joystick.modelDirection == Direction.UP &&
                                positionVector.x + dimensionVector.x / 2f <= floor.width / 2) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX + 1f, furnitureY, furnitureZ);
                            furnitureX += 1f;
                        }
                        break;
                    }
                    case 1: {
                        if (joystick.modelDirection == Direction.LEFT &&
                                positionVector.z - dimensionVector.z / 2f >= -partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY, furnitureZ - 1f);
                            furnitureZ -= 1f;
                        }
                        if (joystick.modelDirection == Direction.RIGHT &&
                                positionVector.z + dimensionVector.z / 2f <= partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY, furnitureZ + 1f);
                            furnitureZ += 1f;
                        }
                        if (joystick.modelDirection == Direction.DOWN &&
                                positionVector.y >= 0f) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY - 1f, furnitureZ);
                            furnitureY -= 1f;
                        }
                        if (joystick.modelDirection == Direction.UP &&
                                positionVector.y + dimensionVector.y <= walls.height) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY + 1f, furnitureZ);
                            furnitureY += 1f;
                        }
                        break;
                    }
                    case 2: {
                        if (joystick.modelDirection == Direction.RIGHT &&
                                positionVector.z - dimensionVector.z / 2f >= -partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY, furnitureZ - 1f);
                            furnitureZ -= 1f;
                        }
                        if (joystick.modelDirection == Direction.LEFT &&
                                positionVector.z + dimensionVector.z / 2f <= partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY, furnitureZ + 1f);
                            furnitureZ += 1f;
                        }
                        if (joystick.modelDirection == Direction.DOWN &&
                                positionVector.y >= 0f) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY - 1f, furnitureZ);
                            furnitureY -= 1f;
                        }
                        if (joystick.modelDirection == Direction.UP &&
                                positionVector.y + dimensionVector.y <= walls.height) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY + 1f, furnitureZ);
                            furnitureY += 1f;
                        }
                        break;
                    }
                    case 3: {
                        if (joystick.modelDirection == Direction.RIGHT &&
                                positionVector.x - dimensionVector.x / 2f >= -partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX - 1f, furnitureY, furnitureZ);
                            furnitureX -= 1f;
                        }
                        if (joystick.modelDirection == Direction.LEFT &&
                                positionVector.x + dimensionVector.x / 2f <= partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX + 1f, furnitureY, furnitureZ);
                            furnitureX += 1f;
                        }
                        if (joystick.modelDirection == Direction.DOWN &&
                                positionVector.y >= 0f) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY - 1f, furnitureZ);
                            furnitureY -= 1f;
                        }
                        if (joystick.modelDirection == Direction.UP &&
                                positionVector.y + dimensionVector.y <= walls.height) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY + 1f, furnitureZ);
                            furnitureY += 1f;
                        }
                        break;
                    }
                    case 4: {
                        if (joystick.modelDirection == Direction.LEFT &&
                                positionVector.x - dimensionVector.x / 2f >= -partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX - 1f, furnitureY, furnitureZ);
                            furnitureX -= 1f;
                        }
                        if (joystick.modelDirection == Direction.RIGHT &&
                                positionVector.x + dimensionVector.x / 2f <= partDimension) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX + 1f, furnitureY, furnitureZ);
                            furnitureX += 1f;
                        }
                        if (joystick.modelDirection == Direction.DOWN &&
                                positionVector.y >= 0f) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY - 1f, furnitureZ);
                            furnitureY -= 1f;
                        }
                        if (joystick.modelDirection == Direction.UP &&
                                positionVector.y + dimensionVector.y <= walls.height) {
                            currentFurnitureInstance.transform
                                    .setTranslation(furnitureX, furnitureY + 1f, furnitureZ);
                            furnitureY += 1f;
                        }
                        break;
                    }
                }
                if (joystick.modelDirection == Direction.ROTATE_X) {
                    currentFurnitureInstance.transform.setFromEulerAngles(furnitureRotationY,
                            furnitureRotationX - 1f, furnitureRotationZ);
                    furnitureRotationX = (furnitureRotationX - 1) % 360;
                    currentFurnitureInstance.transform.scale(1f / modelDimensionsVector.x,
                            1f / modelDimensionsVector.y,
                            1f / modelDimensionsVector.z);
                    currentFurnitureInstance.transform.scale(w, h, l);
                    currentFurnitureInstance.transform.setTranslation(furnitureX, furnitureY, furnitureZ);
                }
                if (joystick.modelDirection == Direction.ROTATE_Y) {
                    currentFurnitureInstance.transform.setFromEulerAngles(furnitureRotationY - 1f,
                            furnitureRotationX, furnitureRotationZ);
                    furnitureRotationY = (furnitureRotationY - 1) % 360;
                    currentFurnitureInstance.transform.scale(1f / modelDimensionsVector.x,
                            1f / modelDimensionsVector.y,
                            1f / modelDimensionsVector.z);
                    currentFurnitureInstance.transform.scale(w, h, l);
                    currentFurnitureInstance.transform.setTranslation(furnitureX, furnitureY, furnitureZ);
                }
                if (joystick.modelDirection == Direction.ROTATE_Z) {
                    currentFurnitureInstance.transform.setFromEulerAngles(furnitureRotationY,
                            furnitureRotationX, furnitureRotationZ - 1f);
                    furnitureRotationZ = (furnitureRotationZ - 1) % 360;
                    currentFurnitureInstance.transform.scale(1f / modelDimensionsVector.x,
                            1f / modelDimensionsVector.y,
                            1f / modelDimensionsVector.z);
                    currentFurnitureInstance.transform.scale(w, h, l);
                    currentFurnitureInstance.transform.setTranslation(furnitureX, furnitureY, furnitureZ);
                }
            }
        }
    }

    @Override
    public void show() {
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.7f)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(203 / 255f, 213 / 255f, 228 / 255f, 1f);

        modelBatch.begin(camera);
        if (floorIsCreated) {
            modelBatch.render(floorInstance, environment);
        }
        if (wallsIsCreated) {
            for (ModelInstance i : wallInstances) {
                modelBatch.render(i, environment);
            }
        }
        if (currentFurniturePart == -1) {
            for (int j = 0; j < 4; j++) {
                if (j != unusedPart) {
                    for (ModelInstance i : wallsFurniture[j]) {
                        modelBatch.render(i, environment);
                    }
                }
            }
            for (ModelInstance i : floorFurniture) {
                modelBatch.render(i, environment);
            }
        } else if (currentFurniturePart == 0) {
            for (ModelInstance i : floorFurniture) {
                modelBatch.render(i, environment);
            }
        } else {
            for (ModelInstance i : wallsFurniture[currentFurniturePart - 1]) {
                modelBatch.render(i, environment);
            }
        }
        if (currentFurnitureInstance != null && canBeRendered) {
            modelBatch.render(currentFurnitureInstance, environment);
        }

        modelBatch.end();

        stage.act();
        stage.draw();

        move();
        moveFurniture();
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
        modelBatch.dispose();
        stage.dispose();
        floorInstance.model.dispose();
        for (int i = 0; i < wallInstances.size(); i++) {
            wallInstances.get(i).model.dispose();
        }
        for (int i = 0; i < floorFurniture.size(); i++) {
            floorFurniture.get(i).model.dispose();
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < wallsFurniture[i].size(); j++) {
                wallsFurniture[i].get(j).model.dispose();
            }
        }

        if (currentFurnitureInstance != null) {
            currentFurnitureInstance.model.dispose();
        }
    }

    //для загрузки мебели
    @Override
    public void getModelPath(String path) {
        furnitureInformationDialog.dimensions[0].setText(getLastName(path));
        furnitureInformationDialog.modelPath = path;
        furnitureInformationDialog.setVisible(true);
    }

    //для загрузки картинки
    @Override
    public void getPicturePath(String path) {
        if (fromFurnitureDialog) {
            furnitureInformationDialog.textures.get(furnitureInformationDialog.numberOfTexture).setText(getLastName(path));
            furnitureInformationDialog.paths.set(furnitureInformationDialog.numberOfTexture, path);
            furnitureInformationDialog.setVisible(true);
            fromFurnitureDialog = false;
        } else {
            dimensionalityDialog.setVisible(true);
            dimensionalityDialog.setMaterialName(path, dimensionalityDialog.currentMaterial);
        }
    }

    //для загрузки mtl-файла
    @Override
    public void getMTLFile(String path) {
        furnitureInformationDialog.mtlFile.setText(getLastName(path));
        furnitureInformationDialog.mtlPath = path;
        furnitureInformationDialog.setVisible(true);
    }

    //при отмене изменений
    @Override
    public void onCancelPressed() {
        if (fromFurnitureDialog) {
            furnitureInformationDialog.setVisible(true);
            fromFurnitureDialog = false;
        } else {
            dimensionalityDialog.setVisible(true);
        }
    }

    //получение имени комнаты
    @Override
    public void getName(String name) {
        if (main.savedFilesNames.contains(name)) {
            exceptionDialog.setText(4);
            exceptionDialog.show(stage);
        } else {
            writeToJson(name);
            changesIsSaved = true;
        }
    }

    //получение значений стен
    @Override
    public void getParameters(float value) {
        walls = new Walls(value * 50f, floor);
        createWallInstances(walls);

        changesIsSaved = false;
    }

    //получение значений пола
    @Override
    public void getParameters(float value1, float value2) {
        floor = new Floor(value1 * 50f, value2 * 50f);
        createFloorInstance(floor);

        changesIsSaved = false;
    }

    //получение значений мебели
    @Override
    public void getFurnitureDimensions(float width, float length, float height) {
        w = width;
        l = length;
        h = height;

        furnitureInformationDialog.dimensions[1].setText(String.valueOf(width));
        furnitureInformationDialog.dimensions[2].setText(String.valueOf(length));
        furnitureInformationDialog.dimensions[3].setText(String.valueOf(height));
        furnitureInformationDialog.setVisible(true);
    }

    //метод получающий отказ или согласие на сообщение о сохранении комнаты
    @Override
    public void getInformation(Integer buttonKey) {
        switch (buttonKey) {
            case 1: { //save
                inputDialog.setDesign(3);
                inputDialog.show(stage);
                break;
            }
            case 2: { //delete
                stage.getRoot().addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        main.setScreen(new Menu(main));
                    }
                })));
                break;
            }
        }
    }

    //получение комнаты
    @Override
    public void getRoomDesign(String name) {
        readFromJson(name);
    }

    //получение изменений в значениях пола
    @Override
    public void getFloorDimensionChange(float width, float length, String material, float realMaterialWidth, float realMaterialLength) {
        float k = floor.length, m = floor.width;

        if (width != 0f || length != 0f) {
            changesIsSaved = false;
            if (floor.width != width || floor.length != length) {
                if (width < length) {
                    floor.length = length;
                    floor.width = width;
                } else {
                    floor.width = length;
                    floor.length = width;
                }
                floorInstance = new ModelInstance(floor.generateFloor());
                if (!floor.materialPath.equals("")) {
                    setFloorMaterial(floor.materialPath);
                } else {
                    setFloorColor(floor.floorColor);
                }
            }
            if (wallsIsCreated && (k != length || m != width)) {
                newWallMaterialColors = walls.materialColors;
                newWallMaterialPaths = walls.materialPaths;
                newRealWallMaterialsSizes = walls.realWallMaterialsSizes;
                walls = new Walls(walls.height, floor);
                walls.materialColors = newWallMaterialColors;
                walls.materialPaths = newWallMaterialPaths;
                walls.realWallMaterialsSizes = newRealWallMaterialsSizes;

                wallInstances = new ArrayList<ModelInstance>();

                newMaterials = wallMaterials;
                wallMaterials = new ArrayList<Material>();
                for (int i = 0; i < 4; i++) {
                    if (i < 2) {
                        wallInstances.add(new ModelInstance(walls.generateWalls(2), walls.coordinates.get(i).x,
                                walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                        if (i == 0) {
                            wallInstances.get(i).transform.rotate(Vector3.X, 90f);
                        } else {
                            wallInstances.get(i).transform.rotate(Vector3.X, 270f);
                        }
                    } else {
                        wallInstances.add(new ModelInstance(walls.generateWalls(1), walls.coordinates.get(i).x,
                                walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                        wallInstances.get(i).transform.rotate(Vector3.Z, 90f);
                    }
                    wallMaterials.add(wallInstances.get(i).materials.get(0));
                }
                recoverMaterial(newMaterials);
                for (int i = 0; i < 4; i++) {
                    if (!walls.materialPaths.get(i).equals("")) {
                        setWallMaterial(i, walls.materialPaths.get(i));
                    }
                }
                blendingAttributes = new ArrayList<BlendingAttribute>();
                for (int i = 0; i < wallMaterials.size(); i++) {
                    blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                    wallMaterials.get(i).set(blendingAttributes.get(i));
                }
            }
        }
        if ((material != null && !floor.materialPath.equals(material)) ||
                (floor.realFloorMaterialSize.realMaterialWidth != realMaterialWidth ||
                        floor.realFloorMaterialSize.realMaterialWidth != realMaterialLength)) {
            changesIsSaved = false;
            floor.setRealMaterialSize(realMaterialWidth, realMaterialLength);
            setFloorMaterial(material);
        }
    }

    //получение изменений в значениях стен
    @Override
    public void getWallsDimensionChange(float height) {
        if (height != 0 && height != walls.height) {
            changesIsSaved = false;

            newWallMaterialColors = walls.materialColors;
            newWallMaterialPaths = walls.materialPaths;
            newRealWallMaterialsSizes = walls.realWallMaterialsSizes;
            walls = new Walls(height, floor);
            walls.materialColors = newWallMaterialColors;
            walls.materialPaths = newWallMaterialPaths;
            walls.realWallMaterialsSizes = newRealWallMaterialsSizes;

            wallInstances = new ArrayList<ModelInstance>();

            newMaterials = wallMaterials;
            wallMaterials = new ArrayList<Material>();
            for (int i = 0; i < 4; i++) {
                if (i < 2) {
                    wallInstances.add(new ModelInstance(walls.generateWalls(2), walls.coordinates.get(i).x,
                            walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                    if (i == 0) {
                        wallInstances.get(i).transform.rotate(Vector3.X, 90f);
                    } else {
                        wallInstances.get(i).transform.rotate(Vector3.X, 270f);
                    }
                } else {
                    wallInstances.add(new ModelInstance(walls.generateWalls(1), walls.coordinates.get(i).x,
                            walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                    wallInstances.get(i).transform.rotate(Vector3.Z, 90f);
                }
                wallMaterials.add(wallInstances.get(i).materials.get(0));
            }
            recoverMaterial(newMaterials);
            for (int i = 0; i < 4; i++) {
                if (!walls.materialPaths.get(i).equals("")) {
                    setWallMaterial(i, walls.materialPaths.get(i));
                }
            }
            blendingAttributes = new ArrayList<BlendingAttribute>();
            for (int i = 0; i < 4; i++) {
                blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                wallMaterials.get(i).set(blendingAttributes.get(i));
            }
        }
    }

    //получение изменений в значениях первой стены
    @Override
    public void getFirstWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength) {
        if (material != null) {
            changesIsSaved = false;
            if (!walls.materialPaths.get(0).equals(material) ||
                    walls.realWallMaterialsSizes.get(0).realMaterialWidth != realMaterialWidth ||
                    walls.realWallMaterialsSizes.get(0).realMaterialHeight != realMaterialLength) {
                walls.setRealMaterialsSizes(0, realMaterialWidth, realMaterialLength);
                setWallMaterial(0, material);

                blendingAttributes = new ArrayList<BlendingAttribute>();
                for (int i = 0; i < 4; i++) {
                    blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                    wallMaterials.get(i).set(blendingAttributes.get(i));
                }
            }
        }
    }

    //получение изменений в значениях второй стены
    @Override
    public void getSecondWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength) {
        if (material != null) {
            changesIsSaved = false;
            if (!walls.materialPaths.get(1).equals(material) ||
                    walls.realWallMaterialsSizes.get(1).realMaterialWidth != realMaterialWidth ||
                    walls.realWallMaterialsSizes.get(1).realMaterialHeight != realMaterialLength) {
                walls.setRealMaterialsSizes(1, realMaterialWidth, realMaterialLength);

                setWallMaterial(1, material);

                blendingAttributes = new ArrayList<BlendingAttribute>();
                for (int i = 0; i < 4; i++) {
                    blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                    wallMaterials.get(i).set(blendingAttributes.get(i));
                }
            }
        }
    }

    //получение изменений в значениях третьей стены
    @Override
    public void getThirdWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength) {
        if (material != null) {
            changesIsSaved = false;
            if (!walls.materialPaths.get(2).equals(material) ||
                    walls.realWallMaterialsSizes.get(2).realMaterialWidth != realMaterialWidth ||
                    walls.realWallMaterialsSizes.get(2).realMaterialHeight != realMaterialLength) {
                walls.setRealMaterialsSizes(2, realMaterialWidth, realMaterialLength);
                setWallMaterial(2, material);

                blendingAttributes = new ArrayList<BlendingAttribute>();
                for (int i = 0; i < 4; i++) {
                    blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                    wallMaterials.get(i).set(blendingAttributes.get(i));
                }
            }
        }
    }

    //получение изменений в значениях четвёртой стены
    @Override
    public void getFourthWallDimensionChange(String material, float realMaterialWidth, float realMaterialLength) {
        if (material != null) {
            changesIsSaved = false;
            if (!walls.materialPaths.get(3).equals(material) ||
                    walls.realWallMaterialsSizes.get(3).realMaterialWidth != realMaterialWidth ||
                    walls.realWallMaterialsSizes.get(3).realMaterialHeight != realMaterialLength) {
                walls.setRealMaterialsSizes(3, realMaterialWidth, realMaterialLength);
                setWallMaterial(3, material);

                blendingAttributes = new ArrayList<BlendingAttribute>();
                for (int i = 0; i < 4; i++) {
                    blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
                    wallMaterials.get(i).set(blendingAttributes.get(i));
                }
            }
        }
    }

    //получение мебели пола
    @Override
    public void getFloorFurniture(Integer number, String path, String mtlPath, Float width, Float length, Float height, ArrayList<String> texturePaths) {
        dimensionalityDialog.setVisible(true);
        furnitureDialog.setVisible(true);
        disableButtons();

        w = width * 50f;
        h = height * 50f;
        l = length * 50f;

        furnitureX = 0f;
        furnitureY = 0f;
        furnitureZ = 0f;
        furnitureRotationX = 0;
        furnitureRotationY = 0;
        furnitureRotationZ = 0;
        currentFurniture = new Furniture(path, mtlPath, w, l, h, 0, 0, 0, 0, 0, 0, texturePaths);
        if (!path.equals("")) {
            try {
                currentFurnitureInstance = currentFurniture.createInstance();
            } catch (Exception e) {
                exceptionDialog.setText(6);
                exceptionDialog.show(stage);
            }
        }
        if (currentFurnitureInstance != null) {
            currentFurniturePart = number;
            dimensionVector = new Vector3(w, h, l);
            modelDimensionsVector = currentFurnitureInstance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3());
            canBeRendered = true;

            moveTo(0);
        } else {
            enableButtons();
        }

        changesIsSaved = false;
    }

    //получение мебели стен
    @Override
    public void getWallFurniture(Integer number, String path, String mtlPath, Float width, Float length, Float height, ArrayList<String> texturePaths) {
        dimensionalityDialog.setVisible(true);
        furnitureDialog.setVisible(true);

        disableButtons();

        w = width * 50f;
        h = height * 50f;
        l = length * 50f;

        furnitureX = 0f;
        furnitureY = 0f;
        furnitureZ = 0f;
        furnitureRotationX = 0;
        furnitureRotationY = 0;
        furnitureRotationZ = 0;
        currentFurniture = new Furniture(path, mtlPath, w, l, h, 0, 0, 0, 0, 0, 0, texturePaths);
        try {
            currentFurnitureInstance = currentFurniture.createInstance();
        } catch (Exception e) {
            exceptionDialog.setText(6);
            exceptionDialog.show(stage);
        }
        if (currentFurnitureInstance != null) {
            currentFurniturePart = number;
            dimensionVector = new Vector3(w, h, l);
            modelDimensionsVector = currentFurnitureInstance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3());

            switch (number) {
                case 1: {
                    furnitureX = floor.width / 2 - dimensionVector.x / 2;
                    break;
                }
                case 2: {
                    furnitureX = -floor.width / 2 + dimensionVector.x / 2;
                    break;
                }
                case 3: {
                    furnitureZ = floor.length / 2 - dimensionVector.z / 2;
                    break;
                }
                case 4: {
                    furnitureZ = -floor.length / 2 + dimensionVector.z / 2;
                    break;
                }
            }
            currentFurnitureInstance.transform.setTranslation(furnitureX, furnitureY, furnitureZ);
            canBeRendered = true;

            moveTo(number);
        } else {
            enableButtons();
        }

        changesIsSaved = false;
    }

    //удаление мебели
    @Override
    public void deleteFurniture(Integer number, TreeSet<Integer> removes) {
        if (number == 0) {
            for (int i : removes) {
                floor.floorFurniture.remove(i);
                floorFurniture.remove(i);
            }
        } else {
            for (int i : removes) {
                walls.wallsFurniture[number - 1].remove(i);
                wallsFurniture[number - 1].remove(i);
            }
        }

        changesIsSaved = false;
    }

    //получение изменений мебели пола
    @Override
    public void getFloorFurnitureChanges(Integer number, Furniture furniture, Float width, Float length, Float height) {
        dimensionalityDialog.setVisible(true);
        furnitureDialog.setVisible(true);
        int i = floor.floorFurniture.indexOf(furniture);
        floor.floorFurniture.get(i).setParameters(width * 50f, length * 50f, height * 50f);
        try {
            floorFurniture.set(i, floor.floorFurniture.get(i).createInstance());
        } catch (Exception e) {
        }

        changesIsSaved = false;
    }

    //получение изменений мебели стен
    @Override
    public void getWallFurnitureChanges(Integer number, Furniture furniture, Float width, Float length, Float height) {
        dimensionalityDialog.setVisible(true);
        furnitureDialog.setVisible(true);
        int i = walls.wallsFurniture[number - 1].indexOf(furniture);
        walls.wallsFurniture[number - 1].get(i).setParameters(width * 50f, length * 50f, height * 50f);
        try {
            wallsFurniture[number - 1].set(i, walls.wallsFurniture[number - 1].get(i).createInstance());
        } catch (Exception e) {
        }

        changesIsSaved = false;
    }

    //создание модели пола
    private void createFloorInstance(Floor floor) {
        floorInstance = new ModelInstance(floor.generateFloor());
        setFloorColor(new Color(100f / 255f, 100f / 255f, 100f / 255f, 1f));

        floorIsCreated = true;
        joystick.slider.setDisabled(false);

        roomEditTable.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        roomEditTable.add(editFloor).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60);
        editFloor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enableMoving) {
                    if (tableIsShown) {
                        table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                        tableIsShown = false;
                    }
                    dimensionalityDialog.setDesign(1);
                    dimensionalityDialog.show(stage);
                }
            }
        });

        roomEditTable.addAction(Actions.moveBy(0,
                -Gdx.graphics.getHeight() / 10 - Gdx.graphics.getHeight() / 160, 0.3f));
    }

    //создание моделей стен
    private void createWallInstances(Walls walls) {
        wallInstances = new ArrayList<ModelInstance>();
        for (int i = 0; i < 4; i++) {
            if (i < 2) {
                wallInstances.add(new ModelInstance(walls.generateWalls(2), walls.coordinates.get(i).x,
                        walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                if (i == 0) {
                    wallInstances.get(i).transform.rotate(Vector3.X, 90f);
                } else {
                    wallInstances.get(i).transform.rotate(Vector3.X, 270f);
                }
            } else {
                wallInstances.add(new ModelInstance(walls.generateWalls(1), walls.coordinates.get(i).x,
                        walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                wallInstances.get(i).transform.rotate(Vector3.Z, 90f);
            }
        }
        wallMaterials = new ArrayList<Material>();
        for (int i = 0; i < 4; i++) {
            wallMaterials.add(wallInstances.get(i).materials.get(0));
            setWallColor(i, new Color(i * 1f / 255f, i * 10f / 255f, i * 100f / 255f, 1f));
        }

        blendingAttributes = new ArrayList<BlendingAttribute>();
        for (int i = 0; i < 4; i++) {
            blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            wallMaterials.get(i).set(blendingAttributes.get(i));
        }
        wallsIsCreated = true;

        roomEditTable.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        roomEditTable.add(editWalls).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160);
        editWalls.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enableMoving) {
                    if (tableIsShown) {
                        table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                        tableIsShown = false;
                    }
                    dimensionalityDialog.setDesign(2);
                    dimensionalityDialog.show(stage);
                }
            }
        });
    }

    //установка материала на модель пола
    private void setFloorMaterial(String path) {
        floorInstance.materials.get(0).clear();
        floorInstance.materials.get(0).set(TextureAttribute.createDiffuse(pictureEditor.getImage(path,
                floor.realFloorMaterialSize.realMaterialWidth, floor.realFloorMaterialSize.realMaterialHeight,
                floor.length, floor.width)));
        floor.setFloorMaterialColor(null);
        floor.setFloorMaterialPath(path);
    }

    //установка цвета на модель пола (вызывается, когда пол создан впервые)
    private void setFloorColor(Color floorColor) {
        floorInstance.materials.get(0).clear();
        floorInstance.materials.get(0).set(ColorAttribute.createDiffuse(floorColor));
        floor.setFloorMaterialColor(floorColor);
        floor.setFloorMaterialPath("");
    }

    //установка материала на модель станы (вызывается, когда пол создан впервые)
    private void setWallMaterial(Integer number, String path) {
        wallMaterials.get(number).clear();
        if (number < 2) {
            wallMaterials.get(number).set(TextureAttribute.createDiffuse(pictureEditor.getImage(path,
                    walls.realWallMaterialsSizes.get(number).realMaterialWidth,
                    walls.realWallMaterialsSizes.get(number).realMaterialHeight,
                    walls.height, floor.length)));
        } else {
            float degrees;
            if (number == 2) {
                degrees = 90f;
            } else {
                degrees = 270f;
            }
            wallMaterials.get(number).set(TextureAttribute.createDiffuse(pictureEditor.getImage(path,
                    walls.realWallMaterialsSizes.get(number).realMaterialWidth,
                    walls.realWallMaterialsSizes.get(number).realMaterialHeight,
                    walls.height, floor.width)));
        }
        walls.setWallMaterialPath(number, path);
        walls.setWallMaterialColor(number, null);
    }

    //установка цвета на модель стены (вызывается, когда пол создан впервые)
    private void setWallColor(Integer number, Color wallColor) {
        wallMaterials.get(number).clear();
        wallMaterials.get(number).set(ColorAttribute.createDiffuse(wallColor));
        walls.setWallMaterialColor(number, wallColor);
        walls.setWallMaterialPath(number, "");
    }

    //восстановление материалов стен после изменений
    private void recoverMaterial(ArrayList<Material> materials) {
        for (int i = 0; i < 4; i++) {
            wallMaterials.get(i).set(materials.get(i));
        }
    }

    //установка прозрачности материала стены
    private void setWallOpacity(Integer number) {
        for (int i = 0; i < blendingAttributes.size(); i++) {
            blendingAttributes.get(i).opacity = 1f;
        }
        if (blendingAttributes.size() > number && blendingAttributes.get(number).opacity != 0f) {
            blendingAttributes.get(number).opacity = 0f;
        }
    }

    //запись комнаты в JSON-файл
    private void writeToJson(String name) {
        room = new Room(floor, walls);

        json.setOutputType(JsonWriter.OutputType.json);
        json.setElementType(Room.class, "floor", Floor.class);
        json.setElementType(Room.class, "walls", Walls.class);
        fileHandle = Gdx.files.local("savedfiles\\" + name + ".json");
        fileHandle.writeString(json.prettyPrint(json.toJson(room)), true);
        main.savedFilesNames.add(name);
    }

    //чтение комнаты из JSON-файла
    private void readFromJson(String name) {
        room = json.fromJson(Room.class, Gdx.files.local("savedfiles\\" + name + ".json"));
        jsonDecoder(room);
    }

    //декодировка комнаты из JSON-файла
    private void jsonDecoder(Room room) {
        floor = room.floor;
        walls = room.walls;
        if (floor != null) {
            createFloorInstanceFromJson(floor);
        }
        if (walls != null) {
            createWallInstancesFromJson(walls);
        }
    }

    //создание пола из JSON-файла
    private void createFloorInstanceFromJson(Floor floor) {
        floorInstance = new ModelInstance(floor.generateFloor());
        floorInstance.transform.translate(0f, -5f, 0f);
        if (floor.materialPath.equals("")) {
            setFloorColor(floor.floorColor);
        } else {
            setFloorMaterial(floor.materialPath);
        }
        for (int i = 0; i < floor.floorFurniture.size(); i++) {
            try {
                floorFurniture.add(floor.floorFurniture.get(i).createInstance());
            } catch (Exception e) {
                exceptionDialog.setText(7);
                exceptionDialog.show(stage);
            }
        }
        if (floorFurniture.size() > 0) furnitureAdded = true;
        floorIsCreated = true;
        joystick.slider.setDisabled(false);

        roomEditTable.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        roomEditTable.add(editFloor).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60);
        editFloor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enableMoving) {
                    if (tableIsShown) {
                        table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                        tableIsShown = false;
                    }
                    dimensionalityDialog.setDesign(1);
                    dimensionalityDialog.show(stage);
                }
            }
        });
        roomEditTable.addAction(Actions.moveBy(0,
                -Gdx.graphics.getHeight() / 10 - Gdx.graphics.getHeight() / 160, 0.3f));
    }

    //создание стен из JSON-файла
    private void createWallInstancesFromJson(Walls walls) {
        wallInstances = new ArrayList<ModelInstance>();
        wallInstances = new ArrayList<ModelInstance>();
        for (int i = 0; i < 4; i++) {
            if (i < 2) {
                wallInstances.add(new ModelInstance(walls.generateWalls(2), walls.coordinates.get(i).x,
                        walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                if (i == 0) {
                    wallInstances.get(i).transform.rotate(Vector3.X, 90f);
                } else {
                    wallInstances.get(i).transform.rotate(Vector3.X, 270f);
                }
            } else {
                wallInstances.add(new ModelInstance(walls.generateWalls(1), walls.coordinates.get(i).x,
                        walls.coordinates.get(i).y, walls.coordinates.get(i).z));
                wallInstances.get(i).transform.rotate(Vector3.Z, 90f);
            }
        }
        wallMaterials = new ArrayList<Material>();
        for (int i = 0; i < 4; i++) {
            wallMaterials.add(wallInstances.get(i).materials.get(0));
            if (walls.materialPaths.get(i).equals("")) {
                setWallColor(i, walls.materialColors.get(i));
            } else {
                setWallMaterial(i, walls.materialPaths.get(i));
            }
        }
        blendingAttributes = new ArrayList<BlendingAttribute>();
        for (int i = 0; i < 4; i++) {
            blendingAttributes.add(new BlendingAttribute(GL20.GL_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
            wallMaterials.get(i).set(blendingAttributes.get(i));
        }
        for (int i = 0; i < walls.wallsFurniture.length; i++) {
            for (int j = 0; j < walls.wallsFurniture[i].size(); j++) {
                try {
                    wallsFurniture[i].add(walls.wallsFurniture[i].get(j).createInstance());
                } catch (Exception e) {
                    exceptionDialog.setText(7);
                    exceptionDialog.show(stage);
                }
            }
            if (wallsFurniture[i].size() > 0) furnitureAdded = true;
        }
        wallsIsCreated = true;

        roomEditTable.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        roomEditTable.add(editWalls).width(Gdx.graphics.getHeight() / 12).height(Gdx.graphics.getHeight() / 12)
                .pad(Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 60,
                        Gdx.graphics.getHeight() / 160, Gdx.graphics.getHeight() / 160);
        editWalls.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enableMoving) {
                    if (tableIsShown) {
                        table.addAction(Actions.moveBy(-Gdx.graphics.getHeight() / 3 * 2, 0, 0.3f));
                        tableIsShown = false;
                    }
                    dimensionalityDialog.setDesign(2);
                    dimensionalityDialog.show(stage);
                }
            }
        });
    }

    //получение имя файла
    private String getLastName(String path) {
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
