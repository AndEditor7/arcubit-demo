package com.andedit.arcubit.handle;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.andedit.arcubit.util.StringUtils;

class WorldFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String name = StringUtils.fastLowerCase(f.getName());
		return name.endsWith(".arcubit");
	}

	@Override
	public String getDescription() {
		return "Arcubit (*.arcubit)";
	}

}
