package com.rabbit.gui.component.list.entries;

import com.rabbit.gui.component.list.DisplayList;
import com.rabbit.gui.render.Renderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SelectListEntry implements ListEntry {

	protected boolean selected = false;

	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public void onClick(DisplayList list, int mouseX, int mouseY) {
		this.selected = !this.selected;
	}

	@Override
	public void onDraw(DisplayList list, int posX, int posY, int width, int height, int mouseX, int mouseY) {
		if (this.isSelected()) {
			Renderer.drawRect(posX, posY, posX + width, posY + height, 0x7FA9A9FF);
		}
	}

	public void setIsSelected(boolean selected) {
		this.selected = selected;
	}
}
