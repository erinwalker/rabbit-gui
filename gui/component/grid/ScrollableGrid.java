package com.rabbit.gui.component.grid;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.rabbit.gui.component.control.ScrollBar;
import com.rabbit.gui.component.grid.entries.GridEntry;
import com.rabbit.gui.layout.LayoutComponent;
import com.rabbit.gui.utils.Geometry;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@LayoutComponent
public class ScrollableGrid extends Grid {

	protected ScrollBar scrollBar;

	public ScrollableGrid(int xPos, int yPos, int width, int height, int slotWidth,
			int slotHeight, List<GridEntry> content) {
		super(xPos, yPos, width, height, slotWidth, slotHeight, content);
	}

	/**
	 * Returns true if content height of list is not more that list actual
	 * height
	 */
	private boolean canFit() {
		return ((this.content.size() / this.xSlots) * this.slotHeight) < this.height;
	}

	@Override
	protected void drawGridContent(int mouseX, int mouseY) {
		this.scrollBar.setVisiblie(!this.canFit());
		this.scrollBar.setHandleMouseWheel(!this.canFit());
		this.scrollBar.setScrollerSize(this.getScrollerSize());
		int scale = Geometry.computeScaleFactor();
		for (int i = 0; i < this.content.size(); i++) {
			GridEntry entry = this.content.get(i);
			int slotPosX = this.getX() + ((i % this.xSlots) * this.slotWidth);
			int slotPosY = ((this.getY() + ((i / this.xSlots) * this.slotHeight))
					- (int) (((this.slotHeight * this.scrollBar.getProgress() * this.content.size()) / this.xSlots)
							* 0.925F));
			int slotWidth = this.slotWidth;
			int slotHeight = this.slotHeight;
			if ((slotPosY < (this.getY() + this.height)) && ((slotPosY + slotHeight) > this.getY())) {
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				Minecraft mc = Minecraft.getMinecraft();
				GL11.glScissor(this.getX() * scale, mc.displayHeight - ((this.getY() + this.getHeight()) * scale),
						this.getWidth() * scale, this.getHeight() * scale);
				entry.onDraw(this, slotPosX + 1, slotPosY + 1, slotWidth - 2, slotHeight - 2, mouseX, mouseY);
				// entry.onDraw(this, slotPosX, slotPosY, slotWidth, slotHeight,
				// mouseX, mouseY);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				GL11.glPopMatrix();
			}
		}
	}

	private int getScrollerSize() {
		return (int) (((1F * this.height) / ((this.content.size() / this.xSlots) * this.slotHeight))
				* (this.height - 4)) / 2;
	}

	@Override
	protected void handleMouseClickGrid(int mouseX, int mouseY) {
		for (int i = 0; i < this.content.size(); i++) {
			GridEntry entry = this.content.get(i);
			int slotPosX = this.getX() + ((i % this.xSlots) * this.slotWidth);
			int slotPosY = ((this.getY() + ((i / this.xSlots) * this.slotHeight))
					- (int) (((this.slotHeight * this.scrollBar.getProgress() * this.content.size()) / this.xSlots)
							* 0.925F));
			int slotWidth = this.slotWidth;
			int slotHeight = this.slotHeight;
			boolean scrollbarActive = this.scrollBar.isScrolling() && this.scrollBar.isVisible();
			if (((slotPosY + slotHeight) <= (this.getY() + this.height)) && (slotPosY >= this.getY())
					&& !scrollbarActive) {
				boolean clickedOnEntry = Geometry.isDotInArea(slotPosX, slotPosY, slotWidth, slotHeight, mouseX,
						mouseY);
				if (clickedOnEntry) {
					entry.onClick(this, mouseX, mouseY);
				}
			}
		}
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		super.onDraw(mouseX, mouseY, partialTicks);

	}

	@Override
	public void setup() {
		super.setup();
		int scrollerSize = this.height / (this.content.isEmpty() ? 1 : this.content.size());
		if (scrollerSize < 10) {
			scrollerSize = 10;
		}
		if (this.content.size() < (this.height / this.slotHeight)) {
			scrollerSize = this.height - 4;
		}
		this.scrollBar = new ScrollBar((this.getX() + this.width) - 10, this.getY(), 10, this.height, scrollerSize);
		this.registerComponent(this.scrollBar);
	}

}
