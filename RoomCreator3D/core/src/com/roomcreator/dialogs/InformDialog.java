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
import com.roomcreator.interfaces.OnInformDialogHide;

/**
 * Диалоговое окно с игформацией о несохранёных изменениях
 */
public class InformDialog extends Dialog {
    private TextButton SAVE, DELETE, CANCEL;
    private Main main;

    public InformDialog(String title, Skin skin, final Main main, final OnInformDialogHide action) {
        super(title, skin);
        this.main = main;

        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 11);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);

        SAVE = new TextButton(main.languages.get("ButtonSave"), skin.get("default", TextButton.TextButtonStyle.class));
        DELETE = new TextButton(new FontMaker().decodeString(main.languages.get("ButtonDelete")), skin.get("default", TextButton.TextButtonStyle.class));
        CANCEL = new TextButton(main.languages.get("ButtonCancel"), skin.get("default", TextButton.TextButtonStyle.class));

        SAVE.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.getInformation(1);
                hide(Actions.fadeOut(0.5f));
            }
        });

        DELETE.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.getInformation(2);
                hide(Actions.fadeOut(0.5f));
            }
        });

        CANCEL.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide(Actions.fadeOut(0.5f));
            }
        });

        this.getTitleLabel().setText(main.languages.get("Information"));
        this.getTitleLabel().setColor(0f, 0f, 0f, 1f);
        this.getTitleLabel().setAlignment(Align.center);
        this.text(this.getTitleLabel()).center();

        this.getButtonTable().add(SAVE).width(Gdx.graphics.getWidth() / 40 * 9).height(Gdx.graphics.getWidth() / 10)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).left();
        this.getButtonTable().add(DELETE).width(Gdx.graphics.getWidth() / 40 * 9).height(Gdx.graphics.getWidth() / 10)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).center();
        this.getButtonTable().add(CANCEL).width(Gdx.graphics.getWidth() / 40 * 9).height(Gdx.graphics.getWidth() / 10)
                .pad(Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100,
                        Gdx.graphics.getHeight() / 100, Gdx.graphics.getHeight() / 100).right();
    }

    @Override
    public Dialog show(Stage stage) {
        this.getBackground().setMinWidth(Gdx.graphics.getWidth() / 16 * 11);
        this.getBackground().setMinHeight(Gdx.graphics.getHeight() / 16 * 15);

        return super.show(stage);
    }

}
