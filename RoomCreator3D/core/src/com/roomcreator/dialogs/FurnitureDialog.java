package com.roomcreator.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;
import com.roomcreator.interfaces.OnFurnitureAdded;
import com.roomcreator.roommakers.Furniture;
import com.roomcreator.screens.CreateRoom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Диалоговое окно с мебелью, добавленной в комнату
 */
public class FurnitureDialog extends Dialog {
    private Main main;
    private CreateRoom createRoom;
    private OnFurnitureAdded action;
    private FontMaker fontMaker;

    private Skin skin;
    private ScrollPane scrollPane;
    private ImageButton[] delete;
    private TextButton[] furnitureButtons;
    private Button add;
    private Table chooserTables;
    private TreeSet<Integer> removes;

    private TextButton OK;
    private Stage stage;

    private Integer key = -1;

    public FurnitureDialog(String title, Skin skin, final Main main, final CreateRoom createRoom, final OnFurnitureAdded action) {
        super(title, skin);
        this.main = main;
        this.createRoom = createRoom;
        this.action = action;
        this.skin = skin;

        fontMaker = new FontMaker();

        chooserTables = new Table();
        add = new Button(skin.get("add", Button.ButtonStyle.class));

        removes = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer t1, Integer t2) {
                if (t1 < t2) {
                    return 1;
                } else if (t1 > t2) {
                    return -1;
                }
                return 0;
            }
        });

        OK = new TextButton("OK", skin.get("default", TextButton.TextButtonStyle.class));
        OK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getContentTable().clear();
                getButtonTable().clear();
                action.deleteFurniture(getKey(), removes);
                createRoom.dimensionalityDialog.setVisible(true);
                hide(Actions.fadeOut(0.5f));
            }
        });

        this.getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() / 16 * 11).height(Gdx.graphics.getHeight() / 16 * 10)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);

        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
    }

    @Override
    public Dialog show(Stage stage) {
        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 13);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
        this.stage = stage;
        return super.show(stage);

    }

    //установка текущего дезайна
    public void setDesign(final int key, final ArrayList<Furniture> furniture) {
        this.key = key;

        this.getContentTable().clear();
        this.getButtonTable().clear();
        this.getTitleTable().clear();

        final FurnitureDialog furnitureDialog = this;
        furnitureButtons = new TextButton[furniture.size()];
        delete = new ImageButton[furniture.size()];
        chooserTables = new Table();

        for (int i = 0; i < furniture.size(); i++) {
            delete[i] = new ImageButton(skin.get("default", ImageButton.ImageButtonStyle.class));
        }

        if (key == 0) { //floor
            for (int i = 0; i < furniture.size(); i++) {
                furnitureButtons[i] = new TextButton(furniture.get(i).getModelName(),
                        skin.get("default", TextButton.TextButtonStyle.class));
            }
            for (int i = 0; i < furniture.size(); i++) {
                chooserTables.add(furnitureButtons[i]).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserTables.add(delete[i]).width(Gdx.graphics.getHeight() / 14).height(Gdx.graphics.getHeight() / 14)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserTables.row();

                final int k = i;
                furnitureButtons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        createRoom.furnitureInformationDialog.setDesign(0, furniture.get(k));
                        createRoom.furnitureInformationDialog.show(stage);
                        setVisible(false);
                    }
                });

                delete[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        removes.add(k);
                        chooserTables.removeActor(furnitureButtons[k]);
                        chooserTables.removeActor(delete[k]);
                    }
                });
            }
        } else { //walls
            for (int i = 0; i < furniture.size(); i++) {
                furnitureButtons[i] = new TextButton(furniture.get(i).getModelName(),
                        skin.get("default", TextButton.TextButtonStyle.class));
            }
            for (int i = 0; i < furniture.size(); i++) {
                chooserTables.add(furnitureButtons[i]).width(Gdx.graphics.getWidth() / 3).height(Gdx.graphics.getWidth() / 14)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserTables.add(delete[i]).width(Gdx.graphics.getHeight() / 14).height(Gdx.graphics.getHeight() / 14)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserTables.row();

                final int k = i;
                furnitureButtons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        createRoom.furnitureInformationDialog.setDesign(getKey(), furniture.get(k));
                        createRoom.furnitureInformationDialog.show(stage);
                        setVisible(false);
                    }
                });

                delete[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        removes.add(k);
                        chooserTables.removeActor(furnitureButtons[k]);
                        chooserTables.removeActor(delete[k]);
                    }
                });
            }
        }
        add.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createRoom.furnitureInformationDialog.setDesign(getKey(), null);
                createRoom.furnitureInformationDialog.show(stage);
                setVisible(false);
            }
        });

        scrollPane = new ScrollPane(chooserTables, this.getSkin().get("furniture-chooser", ScrollPane.ScrollPaneStyle.class));
        scrollPane.setFadeScrollBars(false);

        this.getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() / 16 * 11).height(Gdx.graphics.getHeight() / 16 * 9)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        0f, Gdx.graphics.getHeight() / 100);
        this.getContentTable().row();
        this.getContentTable().add(add).width(Gdx.graphics.getHeight() / 9).height(Gdx.graphics.getHeight() / 9)
                .pad(0f, Gdx.graphics.getHeight() / 100,
                        0f, Gdx.graphics.getHeight() / 100);
        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
    }

    //получение текущей части комнаты
    public Integer getKey() {
        return key;
    }

}
