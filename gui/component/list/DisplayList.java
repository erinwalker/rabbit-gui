package com.rabbit.gui.component.list;

import java.util.Arrays;
import java.util.List;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.component.WidgetList;
import com.rabbit.gui.component.list.entries.ListEntry;
import com.rabbit.gui.layout.LayoutComponent;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.utils.Geometry;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@LayoutComponent
public class DisplayList extends GuiWidget implements WidgetList<ListEntry> {

	protected boolean visibleBackground = true;

	@LayoutComponent
	protected int slotHeight;

	@LayoutComponent
	protected List<ListEntry> content;

	protected DisplayList() {
	}

	public DisplayList(int xPos, int yPos, int width, int height, int slotHeight, List<ListEntry> content) {
		super(xPos, yPos, width, height);
		this.slotHeight = slotHeight;
		this.content = content;
	}

	@Override
	public DisplayList add(ListEntry object) {
		content.add(object);
		return this;
	}

	@Override
	public DisplayList addAll(ListEntry... objects) {
		content.addAll(Arrays.asList(objects));
		return this;
	}

	@Override
	public DisplayList clear() {
		content.clear();
		return this;
	}

	protected void drawListBackground() {
		Renderer.drawRect(getX() - 1, getY() - 1, getX() + width + 1, getY() + height + 1, -6250336);
		Renderer.drawRect(getX(), getY(), getX() + width, getY() + height, -0xFFFFFF - 1);
	}

	protected void drawListContent(int mouseX, int mouseY) {
		for (int i = 0; i < content.size(); i++) {
			ListEntry entry = content.get(i);
			int slotPosX = getX();
			int slotPosY = getY() + (i * slotHeight);
			int slotWidth = width;
			int slotHeight = this.slotHeight;
			GlStateManager.resetColor();
			entry.onDraw(this, slotPosX, slotPosY, slotWidth, slotHeight, mouseX, mouseY);
		}
	}

	@Override
	public List<ListEntry> getContent() {
		return content;
	}

	protected void handleMouseClickList(int mouseX, int mouseY) {
		for (int i = 0; i < content.size(); i++) {
			ListEntry entry = content.get(i);
			entry.setSelected(false);
			int slotPosX = getX();
			int slotPosY = getY() + (i * slotHeight);
			int slotWidth = width;
			int slotHeight = this.slotHeight;
			boolean clickedOnEntry = Geometry.isDotInArea(slotPosX, slotPosY, slotWidth, slotHeight, mouseX, mouseY);
			if (clickedOnEntry) {
				entry.onClick(this, mouseX, mouseY);
			}
		}
	}

	public boolean isVisibleBackground() {
		return visibleBackground;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		if (isVisibleBackground()) {
			drawListBackground();
		}
		drawListContent(mouseX, mouseY);
		super.onDraw(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean onMouseClicked(int posX, int posY, int mouseButtonIndex, boolean overlap) {
		super.onMouseClicked(posX, posY, mouseButtonIndex, overlap);
		boolean clickedOnList = !overlap && Geometry.isDotInArea(getX(), getY(), width, height, posX, posY);
		if (clickedOnList) {
			handleMouseClickList(posX, posY);
		}
		return clickedOnList;
	}

	@Override
	public DisplayList remove(ListEntry object) {
		content.remove(object);
		return this;
	}

	@Override
	public DisplayList setId(String id) {
		assignId(id);
		return this;
	}

	public DisplayList setVisibleBackground(boolean visibleBackground) {
		this.visibleBackground = visibleBackground;
		return this;
	}
}
