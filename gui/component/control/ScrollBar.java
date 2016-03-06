package com.rabbit.gui.component.control;

import org.lwjgl.input.Mouse;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.utils.Geometry;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ScrollBar extends GuiWidget {

	public static interface OnProgressChanged {
		void onProgressChanged(ScrollBar bar, float modifier);
	}

	protected float scrolled = 0;

	protected int scrollerSize;

	protected boolean isScrolling = false;

	protected boolean visible = true;

	protected OnProgressChanged progressChangedListener = (bar, mod) -> {
	};

	protected boolean handleMouseWheel;

	public ScrollBar(int xPos, int yPos, int width, int height, int scrollerSize) {
		super(xPos, yPos, width, height);
		this.scrollerSize = scrollerSize;
	}

	/**
	 * Calculates scroller progress based on mouse y pos
	 *
	 * @param mouseY
	 */
	private void calculateScroller(int mouseY) {
		if (this.isScrolling) {
			float magic = (((mouseY - this.getY()) + 2) - 10F)
					/ ((this.getY() + this.height) - (this.getY() + 2) - 15.0F);
			this.updateProgress(magic - this.scrolled);
		}
	}

	private void drawScroller(int xPos, int yPos, int width, int height) {
		Minecraft.getMinecraft().renderEngine
				.bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tabs.png"));
		Renderer.drawContinuousTexturedBox(xPos, yPos, this.isScrolling() ? 244 : 232, 0, width, height, 12, 15, 1, 2,
				2, 2);
	}

	/**
	 * Returns a float value between 0 and 1,
	 */
	public float getProgress() {
		return this.scrolled;
	}

	public OnProgressChanged getProgressChangedListener() {
		return this.progressChangedListener;
	}

	public boolean isScrolling() {
		return this.isScrolling;
	}

	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		super.onDraw(mouseX, mouseY, partialTicks);
		if (this.isVisible()) {
			this.calculateScroller(mouseY);
			Minecraft.getMinecraft().renderEngine
					.bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png"));
			Renderer.drawContinuousTexturedBox(this.getX(), this.getY(), 174 - 1, 17 - 1, this.width, this.height,
					14 + 2, 112 + 2, 2, 2, 2, 2);
			int scrollerHeight = (int) (this.getY() + 2 + (this.scrolled * (this.height - 4 - this.scrollerSize)));
			this.drawScroller(this.getX() + 2, scrollerHeight, this.width - 4, this.scrollerSize);
		}
	}

	@Override
	public boolean onMouseClicked(int posX, int posY, int mouseButtonIndex, boolean overlap) {
		super.onMouseClicked(posX, posY, mouseButtonIndex, overlap);
		this.isScrolling = !overlap && Geometry.isDotInArea(this.getX() + 2,
				(int) (this.getY() + 2 + (this.scrolled * (this.height - this.scrollerSize))), this.width - 4,
				this.scrollerSize, posX, posY);
		return this.isScrolling;
	}

	@Override
	public void onMouseInput() {
		super.onMouseInput();
		if (this.shouldHandleMouseWheel()) {
			double delta = Mouse.getDWheel();
			if (delta < 0) {
				this.updateProgress(0.10F);
			}
			if (delta > 0) {
				this.updateProgress(-0.10F);
			}
		}
	}

	@Override
	public void onMouseRelease(int mouseX, int mouseY) {
		super.onMouseRelease(mouseX, mouseY);
		this.isScrolling = false;
	}

	private void revalidateScroller() {
		if (this.scrolled < 0) {
			this.scrolled = 0;
		}
		if (this.scrolled > 1) {
			this.scrolled = 1;
		}
	}

	public ScrollBar setHandleMouseWheel(boolean status) {
		this.handleMouseWheel = status;
		return this;
	}

	public ScrollBar setProgress(float scroll) {
		this.scrolled = scroll;
		this.revalidateScroller();
		return this;
	}

	public ScrollBar setProgressChangedListener(OnProgressChanged progressChangedListener) {
		this.progressChangedListener = progressChangedListener;
		return this;
	}

	public ScrollBar setScrollerSize(int size) {
		this.scrollerSize = size;
		return this;
	}

	public ScrollBar setVisiblie(boolean visible) {
		this.visible = visible;
		return this;
	}

	public boolean shouldHandleMouseWheel() {
		return this.handleMouseWheel;
	}

	public void updateProgress(float modifier) {
		this.setProgress(this.scrolled + modifier);
		this.getProgressChangedListener().onProgressChanged(this, modifier);
	}

}
