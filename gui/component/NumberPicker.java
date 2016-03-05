package com.rabbit.gui.component;

import org.lwjgl.input.Keyboard;

import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NumberPicker extends GuiWidget {

	public static interface NumberChangeListener {
		void onNumberChange(NumberPicker picker, int value);
	}

	protected int jumpValue = 10;
	protected int value = 0;
	protected int minValue = Integer.MIN_VALUE;
	protected int maxValue = Integer.MAX_VALUE;

	protected NumberChangeListener listener = (p, v) -> {
	};

	public NumberPicker() {
	}

	public NumberPicker(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public NumberPicker(int x, int y, int width, int height, int value) {
		this(x, y, width, height);
		this.value = value;
	}

	private void decrease() {
		int newValue = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? this.value - this.jumpValue : this.value - 1;
		if (newValue > this.minValue) {
			this.value = newValue;
		} else {
			this.value = this.minValue;
		}
		if (this.getListener() != null) {
			this.getListener().onNumberChange(this, this.value);
		}
	}

	public NumberChangeListener getListener() {
		return this.listener;
	}

	public int getValue() {
		return this.value;
	}

	private void increase() {
		int newValue = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? this.value + this.jumpValue : this.value + 1;
		if (newValue < this.maxValue) {
			this.value = newValue;
		} else {
			this.value = this.maxValue;
		}
		if (this.getListener() != null) {
			this.getListener().onNumberChange(this, this.value);
		}
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		super.onDraw(mouseX, mouseY, partialTicks);
		TextRenderer.renderString(this.getX() + (this.getWidth() / 2), (this.getY() + (this.getHeight() / 2)) - 5,
				String.valueOf(this.value), TextAlignment.CENTER);
	}

	public NumberPicker setJumpValue(int jumpValue) {
		this.jumpValue = jumpValue;
		return this;
	}

	public NumberPicker setListener(NumberChangeListener listener) {
		this.listener = listener;
		return this;
	}

	public NumberPicker setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	public NumberPicker setMinValue(int minValue) {
		this.minValue = minValue;
		return this;
	}

	@Override
	public void setup() {
		super.setup();
		this.registerComponent(new Button(this.getX(), this.getY(), this.getWidth(), this.getHeight() / 3, "+")
				.setClickListener(btn -> this.increase()));
		this.registerComponent(new Button(this.getX(), this.getY() + ((this.getHeight() / 3) * 2), this.getWidth(),
				this.getHeight() / 3, "-").setClickListener(btn -> this.decrease()));
	}

	public void setValue(int value) {
		this.value = value;
	}
}
