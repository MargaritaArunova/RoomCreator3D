package com.roomcreator.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.roomcreator.Main;
import com.roomcreator.interfaces.OnFileClick;
import com.roomcreator.screens.CreateRoom;
import java.util.ArrayList;

/**
 * Диалоговое окно с проводником
 */
public class FileChooser extends Dialog {
    public Main main;
    private CreateRoom createRoom;
    private OnFileClick action;
    private ScrollPane scrollPane;
    private Table scrollTable, chooser, chooserMenu;
    private FileHandle fileHandle;
    private FileHandle[] files;
    private ArrayList<TextButton> textButtons;
    private TextButton CANCEL;
    private Button back, next;
    private Label nothing, directory;
    public String path = "", previousPath = "", str1 = "", str2 = "", filter = "";
    private Integer m = 0, key;

    public FileChooser(String title, Skin skin, final Main main,
                       final CreateRoom createRoom, final OnFileClick action) {
        super(title, skin);
        this.main = main;
        this.createRoom = createRoom;
        this.action = action;

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 11);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);

        textButtons = new ArrayList<TextButton>();

        nothing = new Label(main.languages.get("Nothing to show"), skin.get("null", Label.LabelStyle.class));
        nothing.setAlignment(Align.center);

        directory = new Label("", skin.get("default", Label.LabelStyle.class));
        directory.setAlignment(Align.center);

        back = new Button(skin.get("back", Button.ButtonStyle.class));
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPath(1);
            }
        });

        next = new Button(skin.get("next", Button.ButtonStyle.class));
        next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPath(2);
            }
        });

        scrollTable = new Table();
        chooser = new Table();
        chooserMenu = new Table();
    }

    @Override
    public Dialog show(Stage stage) {
        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 11);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
        return super.show(stage);
    }

    //установка текущего дезайна
    public void createPane(Integer key) {
        this.key = key;
        textButtons.clear();
        scrollTable.clear();
        chooserMenu.clear();
        chooser.clear();
        directory.setText("");
        this.getButtonTable().clear();
        this.getContentTable().clear();
        this.getTitleTable().clear();

        switch (key) {
            case 1: { //импортировать комнату
                fileHandle = Gdx.files.local("savedfiles\\");
                files = fileHandle.list();
                for (int i = 0; i < files.length; i++) {
                    textButtons.add(new TextButton(
                            files[i].name().substring(0, files[i].name().length() - 5),
                            this.getSkin().get("scroll", TextButton.TextButtonStyle.class)));
                    final int k = i;
                    textButtons.get(i).addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            action.getRoomDesign(textButtons.get(k).getText().toString());
                            hide();
                        }
                    });
                    scrollTable.add(textButtons.get(i)).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14)
                            .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                    Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                    scrollTable.row();
                }

                scrollPane = new ScrollPane(scrollTable, this.getSkin().get("default", ScrollPane.ScrollPaneStyle.class));
                scrollPane.setFadeScrollBars(false);

                break;
            }
            case 2: { //импортировать модель
                path = "";
                openDirectory("", "obj");

                scrollPane = new ScrollPane(scrollTable, this.getSkin().get("default", ScrollPane.ScrollPaneStyle.class));
                scrollPane.setFadeScrollBars(false);

                chooserMenu.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserMenu.add(back).width(Gdx.graphics.getHeight() / 8).height(Gdx.graphics.getHeight() / 10);
                chooserMenu.add(directory).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14);
                chooserMenu.add(next).width(Gdx.graphics.getHeight() / 8).height(Gdx.graphics.getHeight() / 10);


                chooser.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooser.add(chooserMenu);
                chooser.row();
                chooser.add(scrollPane).width(Gdx.graphics.getWidth() / 2).height(Gdx.graphics.getHeight() / 12 * 5)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 50,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);


                chooser.background(getSkin().getDrawable("textfield"));
                chooser.pack();

                break;
            }
            case 3: { //импортировать картинку
                path = "";
                openDirectory("", "png");

                scrollPane = new ScrollPane(scrollTable, this.getSkin().get("default", ScrollPane.ScrollPaneStyle.class));
                scrollPane.setFadeScrollBars(false);

                chooserMenu.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserMenu.add(back).width(Gdx.graphics.getHeight() / 8).height(Gdx.graphics.getHeight() / 10);
                chooserMenu.add(directory).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14);
                chooserMenu.add(next).width(Gdx.graphics.getHeight() / 8).height(Gdx.graphics.getHeight() / 10);


                chooser.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooser.add(chooserMenu);
                chooser.row();
                chooser.add(scrollPane).width(Gdx.graphics.getWidth() / 2).height(Gdx.graphics.getHeight() / 12 * 5)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 50,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);


                chooser.background(getSkin().getDrawable("textfield"));
                chooser.pack();

                break;
            }
            case 4:{
                path = "";
                openDirectory("", "mtl");

                scrollPane = new ScrollPane(scrollTable, this.getSkin().get("default", ScrollPane.ScrollPaneStyle.class));
                scrollPane.setFadeScrollBars(false);

                chooserMenu.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooserMenu.add(back).width(Gdx.graphics.getHeight() / 8).height(Gdx.graphics.getHeight() / 10);
                chooserMenu.add(directory).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14);
                chooserMenu.add(next).width(Gdx.graphics.getHeight() / 8).height(Gdx.graphics.getHeight() / 10);


                chooser.pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                chooser.add(chooserMenu);
                chooser.row();
                chooser.add(scrollPane).width(Gdx.graphics.getWidth() / 2).height(Gdx.graphics.getHeight() / 12 * 5)
                        .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 50,
                                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);


                chooser.background(getSkin().getDrawable("textfield"));
                chooser.pack();

                break;
            }
        }

        CANCEL = new TextButton(main.languages.get("ButtonCancel"),
                this.getSkin().get("default", TextButton.TextButtonStyle.class));
        CANCEL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getContentTable().clear();
                getButtonTable().clear();
                action.onCancelPressed();
                hide(Actions.fadeOut(0f));
            }
        });

        this.getTitleLabel().setText(main.languages.get("FileChoosing"));
        this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
        this.getTitleLabel().setAlignment(Align.center);
        this.text(this.getTitleLabel()).center();

        this.getContentTable().pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
        this.getContentTable().row();
        switch (key) {
            case 1: {
                this.getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() / 2).height(Gdx.graphics.getHeight() / 2);
                break;
            }
            default: {
                this.getContentTable().add(chooser).width(Gdx.graphics.getWidth() / 8 * 5).height(Gdx.graphics.getHeight() / 8 * 5);
                break;
            }
        }

        this.getButtonTable().add(CANCEL).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 25,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();
    }

    //открытие папки
    public void openDirectory(String file, final String filter) {
        if (scrollPane != null) {
            scrollPane.scrollTo(0, 100000f,
                    0, 0, true, true);
        }

        textButtons.clear();
        scrollTable.clear();
        m = -1;

        this.filter = filter;
        this.path = file;

        if (!comparePaths()) {
            previousPath = path;
        }

        fileHandle = Gdx.files.external(path);
        files = fileHandle.list();

        for (int i = 0; i < files.length; i++) {
            if (files[i].name().charAt(0) != '.') {
                if (files[i].isDirectory()) {
                    textButtons.add(new TextButton(files[i].name(),
                            this.getSkin().get("scroll", TextButton.TextButtonStyle.class)));
                    m++;
                    final int k = m;
                    textButtons.get(k).addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            directory.setText(textButtons.get(k).getText().toString());
                            openDirectory(path + "\\" + textButtons.get(k).getText().toString(), filter);
                        }
                    });
                    scrollTable.add(textButtons.get(k)).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14)
                            .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                    Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                    scrollTable.row();
                } else if (files[i].path().substring(files[i].path().length() - filter.length(),
                        files[i].path().length()).equals(filter)) {
                    textButtons.add(new TextButton(files[i].name(),
                            this.getSkin().get("scroll", TextButton.TextButtonStyle.class)));
                    m++;
                    final int k = m;
                    textButtons.get(k).addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            switch (key) {
                                case 2: {
                                    action.getModelPath(path + "\\" + textButtons.get(k).getText().toString());
                                    break;
                                }
                                case 3: {
                                    action.getPicturePath(path + "\\" + textButtons.get(k).getText().toString());
                                    break;
                                }
                                case 4: {
                                    action.getMTLFile(path + "\\" + textButtons.get(k).getText().toString());
                                    break;
                                }
                            }
                            hide();
                        }
                    });
                    scrollTable.add(textButtons.get(k)).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14)
                            .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                                    Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
                    scrollTable.row();
                }
            }
        }
        if (m == -1) {
            scrollTable.add(nothing).width(Gdx.graphics.getWidth() / 12 * 5).height(Gdx.graphics.getWidth() / 14)
                    .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                            Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100);
            scrollTable.row();
        }
    }

    //установка пути
    public void setPath(Integer key) {
        switch (key) {
            case 1: { //back
                if (!directory.getText().toString().equals("")) {
                    if (!previousPath.contains(path)) {
                        previousPath = path;
                    }
                    str1 = path.substring(0, path.lastIndexOf("\\"));
                    directory.setText(str1.substring(str1.lastIndexOf("\\") + 1, str1.length()));

                    openDirectory(str1, filter);
                }
                break;
            }
            case 2: { //next
                if (comparePaths()) {
                    str2 = previousPath.substring(path.length() + 1, previousPath.length());
                    if (path.equals("")) {
                        if (str2.contains("\\")) {
                            for (int i = 1; i < str2.length(); i++) {
                                if (str2.charAt(i) == '\\') {
                                    directory.setText(str2.substring(0, i));
                                    openDirectory(path + "\\" + str2.substring(0, i), filter);
                                    break;
                                }
                            }
                        } else {
                            directory.setText(str2);
                            openDirectory(path + "\\" + str2, filter);
                        }
                    } else if (str2.contains("\\")) {
                        for (int i = 0; i < str2.length(); i++) {
                            if (str2.charAt(i) == '\\') {
                                directory.setText(str2.substring(0, i));
                                openDirectory(path + "\\" + str2.substring(0, i), filter);
                                break;
                            }
                        }
                    } else {
                        directory.setText(str2);
                        openDirectory(previousPath, filter);
                    }
                }
                break;
            }
        }
    }

    //сравнение путей
    private boolean comparePaths() {
        if (previousPath.contains(path) && !previousPath.equals(path) && previousPath.length() > path.length()) {
            if (previousPath.charAt(path.length()) == '\\')
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

}
