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
		this.pictureTexture = texture;
		try {
			BufferedImage image = ImageIO
					.read(Minecraft.getMinecraft().getResourceManager().getResource(texture).getInputStream());
			this.imageWidth = image.getWidth();
			this.imageHeight = image.getHeight();
		} catch (IOException ioex) {
			throw new RuntimeException("Can't get resource", ioex);
		}

	}

	public ResourceLocation getPictureTexture() {
		return this.pictureTexture;
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		if (this.isVisible()) {
			GL11.glPushMatrix();
			this.prepareRender();
			if (!this.isEnabled()) {
				this.drawButton(DISABLED_STATE);
				this.renderPicture();
			} else if (this.isButtonUnderMouse(mouseX, mouseY)) {
				this.drawButton(HOVER_STATE);
				this.renderPicture();
				if (this.drawHoverText) {
					Renderer.drawxHoveringText(this.hoverText, mouseX, mouseY);
				}
			} else {
				this.drawButton(IDLE_STATE);
				this.renderPicture();
			}
			this.endRender();
			GL11.glPopMatrix();
		}
	}

	private void renderPicture() {
		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(this.pictureTexture);
		Renderer.drawTexturedModalRect(this.getX() + 1, this.getY(), 0, 0, this.getWidth(), this.getHeight(),
				this.getWidth() - 2, this.getHeight() - 2, 0);
		GL11.glPopMatrix();
	}

	public PictureButton setPictureTexture(ResourceLocation res) {
		this.pictureTexture = res;
		return this;
	}

}
