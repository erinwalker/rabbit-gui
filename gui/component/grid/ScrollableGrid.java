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

	public ScrollableGrid(int xPos, int yPos, int width, int height, int slotWidth, int slotHeight,
			List<GridEntry> content) {
		super(xPos, yPos, width, height, slotWidth, slotHeight, content);
	}

	/**
	 * Returns true if content height of list is not more that list actual
	 * height
	 */
	private boolean canFit() {
		return ((content.size() / xSlots) * slotHeight) < height;
	}

	@Override
	protected void drawGridContent(int mouseX, int mouseY) {
		scrollBar.setVisiblie(!canFit());
		scrollBar.setHandleMouseWheel(!canFit());
		scrollBar.setScrollerSize(getScrollerSize());
		int scale = Geometry.computeScaleFactor();
		for (int i = 0; i < content.size(); i++) {
			GridEntry entry = content.get(i);
			int slotPosX = getX() + ((i % xSlots) * slotWidth);
			int slotPosY = ((getY() + ((i / xSlots) * slotHeight))
					- (int) (((slotHeight * scrollBar.getProgress() * content.size()) / xSlots) * 0.925F));
			int slotWidth = this.slotWidth;
			int slotHeight = this.slotHeight;
			if ((slotPosY < (getY() + height)) && ((slotPosY + slotHeight) > getY())) {
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				Minecraft mc = Minecraft.getMinecraft();
				GL11.glScissor(getX() * scale, mc.displayHeight - ((getY() + getHeight()) * scale), getWidth() * scale,
						getHeight() * scale);
				entry.onDraw(this, slotPosX + 1, slotPosY + 1, slotWidth - 2, slotHeight - 2, mouseX, mouseY);
				// entry.onDraw(this, slotPosX, slotPosY, slotWidth, slotHeight,
				// mouseX, mouseY);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				GL11.glPopMatrix();
			}
		}
	}

	private int getScrollerSize() {
		return (int) (((1F * height) / ((content.size() / xSlots) * slotHeight)) * (height - 4)) / 2;
	}

	@Override
	protected void handleMouseClickGrid(int mouseX, int mouseY) {
		for (int i = 0; i < content.size(); i++) {
			GridEntry entry = content.get(i);
			int slotPosX = getX() + ((i % xSlots) * slotWidth);
			int slotPosY = ((getY() + ((i / xSlots) * slotHeight))
					- (int) (((slotHeight * scrollBar.getProgress() * content.size()) / xSlots) * 0.925F));
			int slotWidth = this.slotWidth;
			int slotHeight = this.slotHeight;
			boolean scrollbarActive = scrollBar.isScrolling() && scrollBar.isVisible();
			if (((slotPosY + slotHeight) <= (getY() + height)) && (slotPosY >= getY()) && !scrollbarActive) {
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
		int scrollerSize = height / (content.isEmpty() ? 1 : content.size());
		if (scrollerSize < 10) {
			scrollerSize = 10;
		}
		if (content.size() < (height / slotHeight)) {
			scrollerSize = height - 4;
		}
		scrollBar = new ScrollBar((getX() + width) - 10, getY(), 10, height, scrollerSize);
		registerComponent(scrollBar);
	}

}
