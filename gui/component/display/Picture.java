package com.rabbit.gui.component.display;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.render.Renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Picture extends GuiWidget {

	private ResourceLocation texture;
	private int imageWidth;
	private int imageHeight;

	public Picture(int xPos, int yPos, int width, int height, ResourceLocation texture) {
		super(xPos, yPos, width, height);
		this.texture = texture;
		try {
			BufferedImage image = ImageIO
					.read(Minecraft.getMinecraft().getResourceManager().getResource(texture).getInputStream());
			this.imageWidth = image.getWidth();
			this.imageHeight = image.getHeight();
		} catch (IOException ioex) {
			throw new RuntimeException("Can't get resource", ioex);
		}
	}

	@Override
	public void onDraw(int xMouse, int yMouse, float partialTicks) {
		super.onDraw(xMouse, yMouse, partialTicks);
		this.renderPicture();
	}

	private void renderPicture() {
		GL11.glPushMatrix();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
		Renderer.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(),
				this.getWidth(), this.getHeight(), 0);
		GL11.glPopMatrix();
	}
}
