package com.rabbit.gui.component.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Table extends GuiWidget {

	protected List<Row> rows;

	protected boolean isVisible = true;
	protected boolean isEnabled = true;

	protected boolean verticalLines = true;
	protected boolean horizontalLines = true;

	protected boolean drawBackground = true;

	public Table(int xPos, int yPos, int width, int height, Row... rows) {
		super(xPos, yPos, width, height);
		this.rows = new ArrayList<Row>(Arrays.asList(rows));
	}

	public Table addRow(Row row) {
		this.rows.add(row);
		return this;
	}

	private void drawBackground() {
		Renderer.drawRect(this.getX() - 1, this.getY() - 1, this.getX() + this.getWidth() + 1,
				this.getY() + this.getHeight() + 1, -6250336);
		Renderer.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(),
				-16777216);
	}

	public boolean drawHorizontalLines() {
		return this.horizontalLines;
	}

	private void drawRow(int xPos, int yPos, int width, int height, int oneLineHeight,
			Row row) {
		TextRenderer.renderString(xPos + (width / 2), yPos + 5, EnumChatFormatting.UNDERLINE + row.getName(),
				TextAlignment.CENTER);
		List<String> lines = row.getStringContent();
		for (int i = 0; i < row.getContent().size(); i++) {
			TextRenderer.renderString(xPos + (width / 2), yPos + (oneLineHeight / 2) + (oneLineHeight * i),
					lines.get(i), TextAlignment.CENTER);
			if (((i + 1) != row.getContent().size()) && this.drawHorizontalLines()) {
				Renderer.drawRect(xPos + 5, yPos + (oneLineHeight * i) + oneLineHeight, (xPos + width) - 5,
						yPos + (oneLineHeight * i) + oneLineHeight + 1, -6250336);
			}
		}
	}

	public boolean drawVerticalLines() {
		return this.verticalLines;
	}

	private Row getLongestRow() {
		Row row = null;
		for (Row r : this.getRows()) {
			if ((row == null) || (row.getContent().size() < r.getContent().size())) {
				row = r;
			}
		}
		return row;
	}

	public List<Row> getRows() {
		return this.rows;
	}

	public boolean isVisible() {
		return this.isVisible;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		if (this.isVisible()) {
			if (this.shouldDrawBackground()) {
				this.drawBackground();
			}
			int oneRowWidth = this.getWidth() / this.getRows().size();
			int oneLineHeight = this.getHeight() / this.getLongestRow().getContent().size();
			for (int i = 0; i < this.getRows().size(); i++) {
				this.drawRow(this.getX() + (oneRowWidth * i), this.getY(), oneRowWidth, this.getHeight(), oneLineHeight,
						this.getRows().get(i));
				if (((i + 1) != this.getRows().size()) && this.drawVerticalLines()) {
					Renderer.drawRect((this.getX() + (oneRowWidth * i) + oneRowWidth) - 1, this.getY() + 5,
							this.getX() + (oneRowWidth * i) + oneRowWidth, (this.getY() + this.getHeight()) - 5,
							-6250336);
				}
			}
		}
	}

	public Table setDrawBackground(boolean flag) {
		this.drawBackground = flag;
		return this;
	}

	public Table setDrawHorizontalLines(boolean flag) {
		this.horizontalLines = flag;
		return this;
	}

	public Table setDrawVerticalLines(boolean flag) {
		this.verticalLines = flag;
		return this;
	}

	@Override
	public Table setId(String id) {
		this.assignId(id);
		return this;
	}

	public Table setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
		return this;
	}

	public boolean shouldDrawBackground() {
		return this.drawBackground;
	}

}
