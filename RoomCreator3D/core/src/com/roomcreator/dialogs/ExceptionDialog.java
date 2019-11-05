package com.roomcreator.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.roomcreator.FontMaker;
import com.roomcreator.Main;

/**
 * Диалоговое окно с сообщениями об ошибках
 */
public class ExceptionDialog extends com.badlogic.gdx.scenes.scene2d.ui.Dialog {
    private TextButton OK;
    private Main main;

    public ExceptionDialog(String title, Skin skin, final Main main) {
        super(title, skin);
        this.main = main;

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 11);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
    }

    @Override
    public Dialog show(Stage stage) {
        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 11);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);
        return super.show(stage);
    }

    //установка текущего дезайна
    public void setText(Integer key) {
        if (key == 1) { // если на поле уже есть комната/пол, то высвечивается
            // сообщение о невозможности импортировать комнату
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_1")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }
        if (key == 2) { // если импортировал/уже создал пол, то высвечивается
            // сообщение о невозможности создать пол
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_2")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }
        if (key == 3) { // если пол не создан или стены уже созданы, то высвечивается
            // сообщение о невозможности создать стены
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_3")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }
        if (key == 4) { // если имя файла не указано/указано неверно/уже существует файл с таким имененем, то
            // высвечивается сообщение об невозможности сохранения
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_4")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }
        if (key == 5) { // если пол не создан, то высвечивается
            // сообщение о невозможности сохранить комнату
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_5")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }
        if (key == 6) {  // если невозможно загрузить модель
            // высвечивается сообщение об невозможности загрузки
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_6")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }
        if (key == 7) {  // если невозможно загрузить все части комнаты
            // высвечивается сообщение об невозможности полной загрузки
            this.getTitleLabel().setText(new FontMaker().decodeString(main.languages.get("Exception_7")));
            this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
            this.getTitleLabel().setAlignment(Align.center);
            this.text(this.getTitleLabel()).center();
        }

        OK = new TextButton("OK", this.getSkin().get("default", TextButton.TextButtonStyle.class));
        OK.setWidth(Gdx.graphics.getHeight() / 6);
        OK.setHeight(Gdx.graphics.getHeight() / 12);
        OK.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getContentTable().clear();
                getButtonTable().clear();
                hide(Actions.fadeOut(0.5f));
            }
        });

        this.getButtonTable().add(OK).width(Gdx.graphics.getWidth() / 5).height(Gdx.graphics.getWidth() / 14)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 25, Gdx.graphics.getHeight() / 100).center();
    }

}
