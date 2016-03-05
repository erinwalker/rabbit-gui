package com.rabbit.gui.component.display;

import java.util.List;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.component.Shiftable;
import com.rabbit.gui.layout.LayoutComponent;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@LayoutComponent
public class TextLabel extends GuiWidget implements Shiftable {

	@LayoutComponent
	protected String text;

	@LayoutComponent
	protected boolean isVisible = true;

	@LayoutComponent
	protected boolean multiline = false;

	@LayoutComponent
	protected boolean drawBackground = false;

	@LayoutComponent
	protected TextAlignment alignment = TextAlignment.LEFT;

	public TextLabel(int xPos, int yPos, int width, int height, String text) {
		this(xPos, yPos, width, height, text, TextAlignment.LEFT);
	}

	public TextLabel(int xPos, int yPos, int width, int height, String text,
			TextAlignment align) {
		super(xPos, yPos, width, height);
		this.text = text;
		this.alignment = align;
	}

	public TextLabel(int xPos, int yPos, int width, String text) {
		this(xPos, yPos, width, 9, text);
	}

	protected void drawAlignedLine(int x, int y, int width, String text,
			TextAlignment alignment) {
		if (alignment == TextAlignment.CENTER) {
			x = x + (this.getWidth() / 2);
		} else if (alignment == TextAlignment.RIGHT) {
			x = x + this.getWidth();
		}
		TextRenderer.renderString(x, y, text, alignment);
	}

	private void drawBackground() {
		Renderer.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.width + 2, this.getY() + this.height + 3,
				-6250336);
		Renderer.drawRect(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 2,
				-0xFFFFFF - 1);
	}

	protected void drawMultilined() {
		List<String> displayLines = TextRenderer.getFontRenderer().listFormattedStringToWidth(this.text,
				this.width);
		for (int i = 0; i < displayLines.size(); i++) {
			String displayLine = displayLines.get(i);
			int y = this.getY() + (i * TextRenderer.getFontRenderer().FONT_HEIGHT);
			if (y >= (this.getY() + this.height)) {
				break;
			}
			this.drawAlignedLine(this.getX(), y, this.getWidth(), displayLine, this.alignment);
		}
	}

	protected void drawOneLined() {
		String displayText = TextRenderer.getFontRenderer().trimStringToWidth(this.text, this.width);
		this.drawAlignedLine(this.getX(), this.getY(), this.getWidth(), displayText, this.alignment);
	}

	public String getText() {
		return this.text;
	}

	public TextAlignment getTextAlignment() {
		return this.alignment;
	}

	public boolean isMultilined() {
		return this.multiline;
	}

	public boolean isVisible() {
		return this.isVisible;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		super.onDraw(mouseX, mouseY, partialTicks);
		if (this.isVisible()) {
			if (this.shouldDrawBackground()) {
				this.drawBackground();
			}
			if (this.isMultilined()) {
				this.drawMultilined();
			} else {
				this.drawOneLined();
			}
		}
	}

	public TextLabel setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
		return this;
	}

	@Override
	public TextLabel setId(String id) {
		this.assignId(id);
		return this;
	}

	public TextLabel setIsVisible(boolean visible) {
		this.isVisible = visible;
		return this;
	}

	public TextLabel setMultilined(boolean multilined) {
		this.multiline = multilined;
		return this;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextLabel setTextAlignment(TextAlignment align) {
		this.alignment = align;
		return this;
	}

	@Override
	public void shiftX(int x) {
		this.setX(this.getX() + x);
	}

	@Override
	public void shiftY(int y) {
		this.setY(this.getY() + y);
	}

	public boolean shouldDrawBackground() {
		return this.drawBackground;
	}
}
