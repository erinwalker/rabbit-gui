package com.rabbit.gui.component.display.graph;

import java.awt.Color;
import java.util.stream.DoubleStream;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PieChart extends GuiWidget {

	/**
	 * Contains colors which will be used in diagram, by default it's filled
	 * with six common colors from java.awt.Color class
	 */
	protected Color[] colors = { Color.BLUE, Color.RED, Color.ORANGE, Color.MAGENTA, Color.GREEN, Color.pink };
	/**
	 * Width and height of the diagram
	 */
	protected int size;
	/**
	 * Each data value represents piece of diagram
	 */
	protected double[] data = new double[0];
	/**
	 * Contains display angle for each data value, usually calculated in
	 * constructor
	 */
	protected double[] angles = new double[0];
	/**
	 * Contains titles per each value, titles length may be differ from value
	 * length
	 */
	protected String[] titles = new String[0];

	public PieChart(int x, int y, int size, double[] data) {
		this(x, y, size, data, new String[0]);
	}

	public PieChart(int x, int y, int size, double[] data, String[] titles) {
		super(x, y, size, size);
		this.size = size;
		this.data = data;
		this.titles = titles;
		this.initialCalculate();
	}

	/**
	 * Display angle for each data value
	 */
	public double[] getAngles() {
		return this.angles;
	}

	public double[] getData() {
		return this.data;
	}

	public String[] getTitles() {
		return this.titles;
	}

	protected void initialCalculate() {
		this.angles = new double[this.data.length];
		double total = DoubleStream.of(this.data).sum();

		for (int i = 0; i < this.data.length; i++) {
			this.angles[i] = (this.data[i] / total) * 360;
		}
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		super.onDraw(mouseX, mouseY, partialTicks);
		double prevAngle = 0;
		for (int i = 0; i < this.data.length; i++) {
			Color color = this.colors[i % this.colors.length];
			Renderer.drawFilledArc(this.x + (this.width / 2), this.y + (this.height / 2), this.size / 2, prevAngle,
					this.angles[i] + prevAngle, color.getRGB());

			if ((i < this.titles.length) && (this.angles[i] > 0)) { // if title
																	// exist and
																	// slice has
																	// been
																	// drawn
				double textAngle = Math.toRadians(prevAngle + (this.angles[i] / 2));
				int textX = (int) (this.x + (this.width / 2) + ((Math.sin(textAngle) * this.size) / 4));
				int textY = (int) (this.y + (this.height / 2) + ((Math.cos(textAngle) * this.size) / 4));
				textY -= 5;
				TextRenderer.renderString(textX, textY, this.titles[i], TextAlignment.CENTER);
			}
			prevAngle += this.angles[i];
		}
	}

	public PieChart setColors(Color[] colors) {
		this.colors = colors;
		return this;
	}

	/**
	 * Updates data and recalculates angles
	 */
	public PieChart setData(double[] data) {
		this.data = data;
		this.initialCalculate();
		return this;
	}

	public PieChart setTitles(String[] titles) {
		this.titles = titles;
		return this;
	}
}
