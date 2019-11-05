package com.roomcreator;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.roomcreator.screens.Menu;

import java.util.HashSet;
import java.util.TreeSet;

public class AndroidLauncher extends AndroidApplication {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private Main main;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		sharedPreferences = getPreferences(MODE_PRIVATE);
		main = new Main();
		main.language = sharedPreferences.getString("language", "en");
		main.savedFilesNames = (HashSet<String>) (sharedPreferences.getStringSet("savedFilesNames", new HashSet<String>()));
		initialize(main, config);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
			}
		}

	}


	@Override
	protected void onPause() {
		super.onPause();
		editor = sharedPreferences.edit();
		editor.putString("language", main.language);
		editor.clear();
		editor.putStringSet("savedFilesNames", main.savedFilesNames);
		editor.commit();
	}

	@Override
	protected void onStop() {
		super.onStop();
		editor = sharedPreferences.edit();
		editor.putString("language", main.language);
		editor.clear();
		editor.putStringSet("savedFilesNames", main.savedFilesNames);
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		main.language = sharedPreferences.getString("language", "en");
        main.savedFilesNames = (HashSet<String>) (sharedPreferences.getStringSet("savedFilesNames", new HashSet<String>()));
    }

	@Override
	protected void onStart() {
		super.onStart();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
			}
		}
		main.language = sharedPreferences.getString("language", "en");
        main.savedFilesNames = (HashSet<String>) (sharedPreferences.getStringSet("savedFilesNames", new HashSet<String>()));
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		editor = sharedPreferences.edit();
		editor.putString("language", main.language);
		editor.clear();
		editor.putStringSet("savedFilesNames", main.savedFilesNames);
		editor.commit();
	}
}
