package com.rabbit.gui.component.list.entries;

import com.rabbit.gui.component.list.DisplayList;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implementation of the ListEntry witch draws the given string in the center of
 * entry slot
 */
@SideOnly(Side.CLIENT)
public class StringEntry implements ListEntry {

	public static interface OnClickListener {
		void onClick(StringEntry entry, DisplayList list, int mouseX, int mouseY);
	}

	/**
	 * boolean of weather entry is selected
	 */
	private boolean selected;

	/**
	 * String which would be drawn in the center of the entry <br>
	 * If it doesn't fits into slot width it would be trimmed
	 */
	private final String title;

	/**
	 * Listener which would be called when user click the entry
	 */
	private OnClickListener listener;

	public StringEntry(String title) {
		this(title, null);
	}

	public StringEntry(String title, OnClickListener listener) {
		this.title = title;
		this.listener = listener;
		selected = false;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public void onClick(DisplayList list, int mouseX, int mouseY) {
		selected = true;
		if (listener != null) {
			listener.onClick(this, list, mouseX, mouseY);
		}
	}

	@Override
	public void onDraw(DisplayList list, int posX, int posY, int width, int height, int mouseX, int mouseY) {
		if (selected) {
			Renderer.drawRect(posX, posY, posX + width, posY + height, 0x7FA9A9FF);
		}
		TextRenderer.renderString(posX + (width / 2), (posY + (height / 2)) - 5,
				TextRenderer.getFontRenderer().trimStringToWidth(title, width), TextAlignment.CENTER);
	}

	@Override
	public void setSelected(boolean state) {
		selected = state;
	}
}
