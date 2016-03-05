package com.rabbit.gui.show;

import com.rabbit.gui.base.Stage;
import com.rabbit.gui.component.IBackground;
import com.rabbit.gui.component.IGui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IShow extends IGui {

	IBackground getBackground();

	int getHeight();

	Stage getStage();

	String getTitle();

	int getWidth();

	boolean hasBeenInitialized();

	void onInit();

	void setBackground(IBackground background);

	void setSize(int width, int height);

	void setStage(Stage stage);

	void setTitle(String title);
}
