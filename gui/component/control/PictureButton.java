package com.rabbit.gui.component.control;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.rabbit.gui.layout.LayoutComponent;
import com.rabbit.gui.render.Renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@LayoutComponent
public class PictureButton extends Button {

	private ResourceLocation pictureTexture;
	private int imageWidth;
	private int imageHeight;

	public PictureButton(int xPos, int yPos, int width, int height, ResourceLocation texture) {
		super(xPos, yPos, width, height, "");
		pictureTexture = texture;
		try {
			BufferedImage image = ImageIO
					.read(Minecraft.getMinecraft().getResourceManager().getResource(texture).getInputStream());
			imageWidth = image.getWidth();
			imageHeight = image.getHeight();
		} catch (IOException ioex) {
			throw new RuntimeException("Can't get resource", ioex);
		}

	}

	public ResourceLocation getPictureTexture() {
		return pictureTexture;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		if (isVisible()) {
			GL11.glPushMatrix();
			prepareRender();
			if (!isEnabled()) {
				drawButton(DISABLED_STATE);
				renderPicture();
			} else if (isButtonUnderMouse(mouseX, mouseY)) {
				drawButton(HOVER_STATE);
				renderPicture();
				if (drawHoverText) {
					Renderer.drawxHoveringText(hoverText, mouseX, mouseY);
				}
			} else {
				drawButton(IDLE_STATE);
				renderPicture();
			}
			endRender();
			GL11.glPopMatrix();
		}
	}

	private void renderPicture() {
		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(pictureTexture);
		Renderer.drawTexturedModalRect(getX() + 1, getY(), 0, 0, getWidth() - 2, getHeight() - 2, getWidth() - 2,
				getHeight() - 2, 0);
		GL11.glPopMatrix();
	}

	public PictureButton setPictureTexture(ResourceLocation res) {
		pictureTexture = res;
		return this;
	}

}
