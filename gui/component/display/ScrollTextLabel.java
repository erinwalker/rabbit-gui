package com.rabbit.gui.component.display;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.rabbit.gui.component.control.ScrollBar;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;
import com.rabbit.gui.utils.Geometry;

import net.minecraft.client.Minecraft;

public class ScrollTextLabel extends TextLabel {

	private ScrollBar scrollBar;

	public ScrollTextLabel(int xPos, int yPos, int width, int height, String text) {
		super(xPos, yPos, width, height, text);
	}

	public ScrollTextLabel(int xPos, int yPos, int width, int height, String text, TextAlignment align) {
		super(xPos, yPos, width, height, text, align);
	}

	public ScrollTextLabel(int xPos, int yPos, int width, String text) {
		super(xPos, yPos, width, text);
	}

	/**
	 * Evaluates if current content of textlabel can fit into it's height <br/>
	 * Used to determine if scrollbar should be vissble
	 */
	private boolean canFit() {
		int content = this.getLines().size() * 10;
		return content < this.height; // 10 - height of one symbol
	}

	@Override
	protected void drawMultilined() {
		this.scrollBar.setVisiblie(!this.canFit());
		List<String> displayLines = this.getLines();
		int scale = Geometry.computeScaleFactor();
		for (int i = 0; i < displayLines.size(); i++) {
			String line = displayLines.get(i);
			int lineY = ((this.getY() + (i * 10)) - (int) ((10 * this.scrollBar.getProgress() * displayLines.size())
					- (((this.height - 10) * (this.scrollBar.getProgress())) / 1)));
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(this.getX() * scale,
					Minecraft.getMinecraft().displayHeight - ((this.getY() + this.getHeight()) * scale),
					this.getWidth() * scale, this.getHeight() * scale);
			this.drawAlignedLine(this.getX(), lineY, this.getWidth() - 10, line, this.alignment);
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}

	}

	private List<String> getLines() {
		return TextRenderer.getFontRenderer().listFormattedStringToWidth(this.getText(), this.width - 10);
	}

	@Override
	public void setup() {
		super.setup();
		if (this.isMultilined()) {
			int scrollerSize = Math.max(this.height / this.getLines().size(), 10);
			this.scrollBar = new ScrollBar((this.getX() + this.width) - 10, this.getY(), 10, this.height, scrollerSize)
					.setHandleMouseWheel(false);
			this.registerComponent(this.scrollBar);
		}
	}
}
