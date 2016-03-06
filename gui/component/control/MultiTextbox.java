package com.rabbit.gui.component.control;

import java.util.ArrayList;
import java.util.List;

import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextRenderer;

import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MultiTextbox extends TextBox {

	private ScrollBar scrollBar;

	protected int maxStringLenght = 1000;

	private int listHeight;

	public MultiTextbox(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
		this.listHeight = height;
	}

	public MultiTextbox(int xPos, int yPos, int width, int height, String initialText) {
		super(xPos, yPos, width, height, initialText);
		this.listHeight = height;
	}

	@Override
	protected void drawBox() {
		if (this.isVisible()) {
			if (this.isBackgroundVisible()) {
				this.drawTextBoxBackground();
			}
			int color = 0xFFFFFF;
			boolean renderCursor = this.isFocused() && (((this.cursorCounter / 6) % 2) == 0);
			int startLine = this.getStartLineY();
			int maxLineAmount = (this.height / TextRenderer.getFontRenderer().FONT_HEIGHT) + startLine;
			List<String> lines = this.getLines();
			int charCount = 0;
			int lineCount = 0;
			int maxWidth = this.width - 4;
			for (int i = 0; i < lines.size(); ++i) {
				String wholeLine = lines.get(i);
				String line = "";
				char[] chars = wholeLine.toCharArray();
				for (char c : chars) {
					if (TextRenderer.getFontRenderer().getStringWidth(line + c) > maxWidth) {
						if ((lineCount >= startLine) && (lineCount < maxLineAmount)) {
							TextRenderer.getFontRenderer().drawString(line, this.getX() + 4,
									this.getY() + 4
											+ ((lineCount - startLine) * TextRenderer.getFontRenderer().FONT_HEIGHT),
									color);
						}
						line = "";
						lineCount++;
					}
					if (renderCursor && (charCount == this.getCursorPosition()) && (lineCount >= startLine)
							&& (lineCount < maxLineAmount)) {
						int cursorX = this.getX() + TextRenderer.getFontRenderer().getStringWidth(line) + 3;
						int cursorY = this.getY()
								+ ((lineCount - startLine) * TextRenderer.getFontRenderer().FONT_HEIGHT) + 4;
						if (this.getText().length() == this.getCursorPosition()) {
							TextRenderer.getFontRenderer().drawString("_", cursorX, cursorY, color);
						} else {
							Renderer.drawRect(cursorX, cursorY, cursorX + 1, cursorY + 10, 0xFFFFFFFF);
						}
					}
					charCount++;
					line += c;
				}
				if ((lineCount >= startLine) && (lineCount < maxLineAmount)) {
					TextRenderer.getFontRenderer().drawString(line, this.getX() + 4,
							this.getY() + 4 + ((lineCount - startLine) * TextRenderer.getFontRenderer().FONT_HEIGHT),
							color);
					if (renderCursor && (charCount == this.getCursorPosition())) {
						int cursorX = this.getX() + TextRenderer.getFontRenderer().getStringWidth(line) + 3;
						int cursorY = this.getY()
								+ ((lineCount - startLine) * TextRenderer.getFontRenderer().FONT_HEIGHT) + 4;
						if (this.getText().length() == this.getCursorPosition()) {
							TextRenderer.getFontRenderer().drawString("_", cursorX, cursorY, color);
						} else {
							Renderer.drawRect(cursorX, cursorY, cursorX + 1,
									cursorY + TextRenderer.getFontRenderer().FONT_HEIGHT, color);
						}
					}
				}
				++lineCount;
				++charCount;
			}
			this.listHeight = lineCount * TextRenderer.getFontRenderer().FONT_HEIGHT;
			this.scrollBar.setVisiblie(this.listHeight > (this.height - 4));
			this.scrollBar.setScrollerSize((this.getScrollerSize()));
		}
	}

	public List<String> getLines() {
		List<String> lines = new ArrayList<String>();
		StringBuffer currentLine = new StringBuffer();
		char[] chars = this.getText().toCharArray();
		for (char symbol : chars) {
			if ((symbol == '\r') || (symbol == '\n')) {
				lines.add(currentLine.toString());
				currentLine.delete(0, currentLine.length());
			} else {
				currentLine.append(symbol);
			}
		}
		lines.add(currentLine.toString());
		return lines;
	}

	@Override
	public int getMaxLength() {
		return this.maxStringLenght;
	}

	private int getScrollerSize() {
		return (int) (((1F * this.height) / this.listHeight) * (this.height - 4));
	}

	private int getStartLineY() {
		float scrolled = this.scrollBar.scrolled;
		return MathHelper
				.ceiling_double_int((scrolled * this.getHeight()) / TextRenderer.getFontRenderer().FONT_HEIGHT);
	}

	@Override
	protected void handleKey(char typedChar, int typedIndex) {
		if (!this.isFocused()) {
			return;
		}
		String originalText = this.getText();
		if ((typedChar == 13) || (typedChar == 10)) {
			this.setText(originalText.substring(0, this.getCursorPosition()) + typedChar
					+ originalText.substring(this.getCursorPosition()));
		}

		boolean isSpecialCharCombination = this.handleSpecialCharComb(typedChar, typedIndex);
		if (!isSpecialCharCombination) {
			this.handleInput(typedChar, typedIndex);
		}
	}

	@Override
	protected boolean handleMouseClick(int posX, int posY, int mouseButtonIndex, boolean overlap) {
		boolean clicked = !overlap && this.isTextBoxUnderMouse(posX, posY);
		this.setIsFocused(clicked);
		if (this.isFocused() && (mouseButtonIndex == 0)) {
			int lenght = posX - this.getX();
			String temp = TextRenderer.getFontRenderer().trimStringToWidth(this.text.substring(this.scrollOffset),
					this.getWidth());
			this.setCursorPosition(
					TextRenderer.getFontRenderer().trimStringToWidth(temp, lenght).length() + this.scrollOffset);
			int x = posX - this.getX();
			int y = ((posY - this.getY() - 4) / TextRenderer.getFontRenderer().FONT_HEIGHT) + this.getStartLineY();
			this.cursorPos = 0;
			List<String> lines = this.getLines();
			int charCount = 0;
			int lineCount = 0;
			int maxWidth = this.getWidth() - 4;
			for (int i = 0; i < lines.size(); ++i) {
				String wholeLine = lines.get(i);
				String line = "";
				char[] chars = wholeLine.toCharArray();
				for (char c : chars) {
					this.setCursorPosition(charCount);
					if (TextRenderer.getFontRenderer().getStringWidth(line + c) > maxWidth) {
						lineCount++;
						line = "";
						if (y < lineCount) {
							break;
						}
					}
					if ((lineCount == y) && (x <= TextRenderer.getFontRenderer().getStringWidth(line + c))) {
						return clicked;
					}
					charCount++;
					line += c;
				}
				this.setCursorPosition(charCount);
				charCount++;
				lineCount++;
				if (y < lineCount) {
					break;
				}

			}
			if (y >= lineCount) {
				this.setCursorPosition(this.getText().length());
			}
		}
		return clicked;
	}

	@Override
	public void setup() {
		this.registerComponent(
				this.scrollBar = new ScrollBar(this.getX() + this.getWidth(), this.getY(), 15, this.getHeight(), 20)
						.setVisiblie(false));
	}

}
