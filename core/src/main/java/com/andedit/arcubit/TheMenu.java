package com.andedit.arcubit;

import static com.andedit.arcubit.Main.main;

import com.badlogic.gdx.ScreenAdapter;

/** The main menu instance */
public class TheMenu extends ScreenAdapter {
	@Override
	public void show() {
		main.setScreen(new TheGame());
	}
}
