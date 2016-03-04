package com.rabbit.gui.component.control;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.rabbit.gui.GuiFoundation;
import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.component.Shiftable;
import com.rabbit.gui.layout.LayoutComponent;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

/**
 * Simple button component <br>
 * Supported width: <b> 0 - 400 </b> (due to texture length it can't be larger)
 * <br>
 * Supported height: <b> 5 - INFINITY </b> <br>
 * 
 * Use {@link #setClickListener(ButtonClickListener)} to define action on button
 * pressed
 */

@SideOnly(Side.CLIENT)
@LayoutComponent
public class Button extends GuiWidget implements Shiftable {

	protected boolean drawHoverText = false;
	protected List<String> originalHoverText = new ArrayList();
	protected List<String> hoverText = new ArrayList();

	protected static final int DISABLED_STATE = 0;
	protected static final int IDLE_STATE = 1;
	protected static final int HOVER_STATE = 2;

	protected ResourceLocation buttonTexture = new ResourceLocation("textures/gui/widgets.png");

	@LayoutComponent
	protected String text;

	@LayoutComponent
	protected boolean isVisible = true;

	@LayoutComponent
	protected boolean isEnabled = true;

	protected ButtonClickListener onClick;

	protected boolean drawToLeft;

	/** Dummy constructor. Used in layout */
	protected Button() {
	}

	public Button(int xPos, int yPos, int width, int height, String title) {
		super(xPos, yPos, width, height);
		this.text = title;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		if (isVisible()) {
			prepareRender();
			if (!isEnabled()) {
				drawButton(DISABLED_STATE);
			} else if (isButtonUnderMouse(mouseX, mouseY)) {
				drawButton(HOVER_STATE);
				if (this.drawHoverText) {
					Renderer.drawHoveringText(this.hoverText, mouseX, mouseY);
				}
			} else {
				drawButton(IDLE_STATE);
			}
			TextRenderer.renderString(getX() + getWidth() / 2, getY() + getHeight() / 2 - 4, getText(),
					TextAlignment.CENTER);
		}
	}

	protected void prepareRender() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(getButtonTexture());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	protected void endRender() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void drawButton(int state) {
		Renderer.drawContinuousTexturedBox(getX(), getY(), 0, 46 + (20 * state), getWidth(), getHeight(), 200, 20, 2, 3,
				2, 2);
	}

	@Override
	public boolean onMouseClicked(int posX, int posY, int mouseButtonIndex, boolean overlap) {
		boolean clicked = isButtonUnderMouse(posX, posY) && isEnabled() && !overlap;
		if (clicked) {
			if (getClickListener() != null) {
				getClickListener().onClick(this);
			}
			playClickSound();
		}
		return clicked;
	}

	public boolean isButtonUnderMouse(int mouseX, int mouseY) {
		return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
	}

	/**
	 * Provided listener will be executed by pressing the button
	 * 
	 * @param onClicked
	 *            listener
	 * @return self
	 */
	public Button setClickListener(ButtonClickListener onClicked) {
		this.onClick = onClicked;
		return this;
	}

	public ButtonClickListener getClickListener() {
		return onClick;
	}

	/**
	 * @return <code> true </code> if button would be rendered
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @return <code> true</code> if button can be clicked
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	public Button setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
		return this;
	}

	public Button setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		return this;
	}

	public ResourceLocation getButtonTexture() {
		return buttonTexture;
	}

	public Button setCustomTexture(ResourceLocation res) {
		this.buttonTexture = res;
		return this;
	}

	public Button setText(String text) {
		this.text = text;
		return this;
	}

	public String getText() {
		return text;
	}

	protected void playClickSound() {
		Minecraft.getMinecraft().getSoundHandler()
				.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}

	@Override
	public Button setId(String id) {
		assignId(id);
		return this;
	}

	@FunctionalInterface
	public static interface ButtonClickListener {
		void onClick(Button button);
	}

	@Override
	public void shiftX(int x) {
		this.setX(getX() + x);
	}

	@Override
	public void shiftY(int y) {
		this.setY(getY() + y);
	}

	public Button doesDrawHoverText(boolean state) {
		this.drawHoverText = state;
		return this;
	}

	public Button addHoverText(String text) {
		this.hoverText.add(text);
		return this;
	}

	public Button setHoverText(List<String> text) {
		this.hoverText = text;
		return this;
	}

	public List<String> getHoverText() {
		return this.hoverText;
	}

	protected void verifyHoverText(int mouseX, int mouseY) {
		int tlineWidth = 0;
		for (String line : this.originalHoverText) {
			tlineWidth = TextRenderer.getFontRenderer().getStringWidth(line) > tlineWidth
					? TextRenderer.getFontRenderer().getStringWidth(line) : tlineWidth;
		}
		int dWidth = GuiFoundation.proxy.getCurrentStage().width;
		if (tlineWidth + mouseX > dWidth && mouseX + 1 > dWidth / 2) {
			// the button is on the right half of the screen
			this.drawToLeft = true;
		}
		List<String> newHoverText = new ArrayList();
		if (drawToLeft) {
			for (String line : this.originalHoverText) {
				int lineWidth = TextRenderer.getFontRenderer().getStringWidth(line) + 12;
				// if the line length is longer than the button is from the left
				// side of the screen we have to split
				if (lineWidth > mouseX) {
					// the line is too long lets split it
					String newString = "";
					for (String substring : line.split(" ")) {
						// we can fit the string, we are ok
						if (TextRenderer.getFontRenderer().getStringWidth(newString)
								+ TextRenderer.getFontRenderer().getStringWidth(substring) < mouseX - 12) {
							newString += substring + " ";
						} else {
							newHoverText.add(newString);
							newString = substring + " ";
						}
					}
					newHoverText.add(newString);
				} else {
					newHoverText.add(line);
				}
			}
		} else {
			for (String line : this.originalHoverText) {
				int lineWidth = TextRenderer.getFontRenderer().getStringWidth(line) + 12;
				// we just need to know what the right most side of the button
				// is
				if (lineWidth > dWidth - mouseX) {
					// the line is too long lets split it
					String newString = "";
					for (String substring : line.split(" ")) {
						// we can fit the string, we are ok
						if (TextRenderer.getFontRenderer().getStringWidth(newString)
								+ TextRenderer.getFontRenderer().getStringWidth(substring) < dWidth - mouseX - 12) {
							newString += substring + " ";
						} else {
							newHoverText.add(newString);
							newString = substring + " ";
						}
					}
					newHoverText.add(newString);
				} else {
					newHoverText.add(line);
				}
			}
		}
		this.hoverText = newHoverText;
	}
}
