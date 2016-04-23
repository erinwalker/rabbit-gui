package com.rabbit.gui.render;

import java.awt.Color;
import java.util.List;
import java.util.stream.IntStream;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderer {

	/**
	 * Draws a textured box of any size (smallest size is borderSize * 2 square)
	 * based on a fixed size textured box with continuous borders and filler. It
	 * is assumed that the desired texture ResourceLocation object has been
	 * bound using
	 * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation
	 * ).
	 *
	 * @param x
	 *            x axis offset
	 * @param y
	 *            y axis offset
	 * @param u
	 *            bound resource location image x offset
	 * @param v
	 *            bound resource location image y offset
	 * @param width
	 *            the desired box width
	 * @param height
	 *            the desired box height
	 * @param textureWidth
	 *            the width of the box texture in the resource location image
	 * @param textureHeight
	 *            the height of the box texture in the resource location image
	 * @param topBorder
	 *            the size of the box's top border
	 * @param bottomBorder
	 *            the size of the box's bottom border
	 * @param leftBorder
	 *            the size of the box's left border
	 * @param rightBorder
	 *            the size of the box's right border
	 */
	public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth,
			int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int fillerWidth = textureWidth - leftBorder - rightBorder;
		int fillerHeight = textureHeight - topBorder - bottomBorder;
		int canvasWidth = width - leftBorder - rightBorder;
		int canvasHeight = height - topBorder - bottomBorder;
		int xPasses = canvasWidth / fillerWidth;
		int remainderWidth = canvasWidth % fillerWidth;
		int yPasses = canvasHeight / fillerHeight;
		int remainderHeight = canvasHeight % fillerHeight;

		drawTexturedModalRect(x, y, u, v, leftBorder, topBorder);
		drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder);
		drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder,
				bottomBorder);
		drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth,
				v + topBorder + fillerHeight, rightBorder, bottomBorder);
		IntStream.range(0, xPasses + (remainderWidth > 0 ? 1 : 0)).forEach(i -> {
			drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v,
					(i == xPasses ? remainderWidth : fillerWidth), topBorder);
			drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder,
					v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder);
			IntStream.range(0, yPasses + (remainderHeight > 0 ? 1 : 0)).forEach(j -> {
				drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight),
						u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth),
						(j == yPasses ? remainderHeight : fillerHeight));
			});
		});
		IntStream.range(0, yPasses + (remainderHeight > 0 ? 1 : 0)).forEach(j -> {
			drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder,
					(j == yPasses ? remainderHeight : fillerHeight));
			drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight),
					u + leftBorder + fillerWidth, v + topBorder, rightBorder,
					(j == yPasses ? remainderHeight : fillerHeight));
		});
	}

	/**
	 * Draws filled arc with the given color centered in the given location with
	 * the given size
	 *
	 * @param xCenter
	 *            - x center of the arc
	 * @param yCenter
	 *            - y center of the arc
	 * @param radius
	 *            - size of the arc
	 * @param startDegrees
	 *            - start angle of the arc
	 * @param finishDegrees
	 *            - finish angle of the arc
	 * @param color
	 *            - rgb color of the arc
	 */
	public static void drawFilledArc(int xCenter, int yCenter, int radius, double startDegrees, double finishDegrees,
			int color) {
		GL11.glPushMatrix();
        GlStateManager.disableTexture2D();
		glColorRGB(color);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(xCenter, yCenter);
		for (double i = startDegrees; i <= finishDegrees; i += 0.05) {
			double theta = (2 * Math.PI * i) / 360.0;
			double dotX = xCenter + (Math.sin(theta) * radius);
			double dotY = yCenter + (Math.cos(theta) * radius);
			GL11.glVertex2d(dotX, dotY);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GL11.glPopMatrix();
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors
	 * in the given points
	 *
	 * @param xTop
	 *            - top x point
	 * @param yTop
	 *            - top y point
	 * @param xBot
	 *            - bottom x point
	 * @param yBot
	 *            - bottom y point
	 * @param firstColor
	 *            - first gradient color
	 * @param secondColor
	 *            - second gradient color
	 */
	public static void drawGradient(int xTop, int yTop, int xBot, int yBot, int firstColor, int secondColor) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_QUADS);
		glColorRGB(firstColor);
		GL11.glVertex2d(xBot, yTop);
		GL11.glVertex2d(xTop, yTop);
		glColorRGB(secondColor);
		GL11.glVertex2d(xTop, yBot);
		GL11.glVertex2d(xBot, yBot);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Draw a 1 pixel wide horizontal line. Args: x1, x2, y, color
	 */
	public static void drawHorizontalLine(int startX, int endX, int y, int color) {
		if (endX < startX) {
			int i1 = startX;
			startX = endX;
			endX = i1;
		}

		drawRect(startX, y, endX + 1, y + 1, color);
	}

	public static void drawHoveringText(List<String> content, int xPos, int yPos) {
		if (!content.isEmpty()) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 1);

			int width = 0;
			for (String line : content) {
				int lineWidth = TextRenderer.getFontRenderer().getStringWidth(line);
				width = Math.max(width, lineWidth);
			}
			int x = xPos + 12;
			int y = yPos - 12;
			int additional = 8;

			if (content.size() > 1) {
				additional += 2 + ((content.size() - 1) * 10);
			}

			int firstColor = -267386864;
			int secondColor = 1347420415;

			drawGradient(x - 3, y - 4, x + width + 3, y - 3, firstColor, firstColor);
			drawGradient(x - 3, y + additional + 3, x + width + 3, y + additional + 4, firstColor, firstColor);
			drawGradient(x - 3, y - 3, x + width + 3, y + additional + 3, firstColor, firstColor);
			drawGradient(x - 4, y - 3, x - 3, y + additional + 3, firstColor, firstColor);
			drawGradient(x + width + 3, y - 3, x + width + 4, y + additional + 3, firstColor, firstColor);
			int l1 = ((secondColor & 16711422) >> 1) | (secondColor & -16777216);
			drawGradient(x - 3, (y - 3) + 1, (x - 3) + 1, (y + additional + 3) - 1, secondColor, l1);
			drawGradient(x + width + 2, (y - 3) + 1, x + width + 3, (y + additional + 3) - 1, secondColor, l1);
			drawGradient(x - 3, y - 3, x + width + 3, (y - 3) + 1, secondColor, secondColor);
			drawGradient(x - 3, y + additional + 2, x + width + 3, y + additional + 3, l1, l1);

			for (int i = 0; i < content.size(); ++i) {
				String line = content.get(i);
				TextRenderer.renderString(x, y, line, Color.white, true, TextAlignment.LEFT);
				if (i == 0) {
					y += 2;
				}
				y += 10;
			}

			GL11.glTranslatef(0, 0, -1);
			GL11.glPopMatrix();
		}
	}

	public static void drawHoveringTextInScissoredArea(List<String> content, int xPos, int yPos) {
		if (!content.isEmpty()) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			GL11.glTranslatef(0, 0, 1);

			int width = 0;
			for (String line : content) {
				int lineWidth = TextRenderer.getFontRenderer().getStringWidth(line);
				width = Math.max(width, lineWidth);
			}
			int x = xPos + 12;
			int y = yPos - 12;
			int additional = 8;

			if (content.size() > 1) {
				additional += 2 + ((content.size() - 1) * 10);
			}

			int firstColor = -267386864;
			int secondColor = 1347420415;

			drawGradient(x - 3, y - 4, x + width + 3, y - 3, firstColor, firstColor);
			drawGradient(x - 3, y + additional + 3, x + width + 3, y + additional + 4, firstColor, firstColor);
			drawGradient(x - 3, y - 3, x + width + 3, y + additional + 3, firstColor, firstColor);
			drawGradient(x - 4, y - 3, x - 3, y + additional + 3, firstColor, firstColor);
			drawGradient(x + width + 3, y - 3, x + width + 4, y + additional + 3, firstColor, firstColor);
			int l1 = ((secondColor & 16711422) >> 1) | (secondColor & -16777216);
			drawGradient(x - 3, (y - 3) + 1, (x - 3) + 1, (y + additional + 3) - 1, secondColor, l1);
			drawGradient(x + width + 2, (y - 3) + 1, x + width + 3, (y + additional + 3) - 1, secondColor, l1);
			drawGradient(x - 3, y - 3, x + width + 3, (y - 3) + 1, secondColor, secondColor);
			drawGradient(x - 3, y + additional + 2, x + width + 3, y + additional + 3, l1, l1);

			for (int i = 0; i < content.size(); ++i) {
				String line = content.get(i);
				TextRenderer.renderString(x, y, line, Color.white, true, TextAlignment.LEFT);
				if (i == 0) {
					y += 2;
				}
				y += 10;
			}

			GL11.glTranslatef(0, 0, -1);
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glPopMatrix();
		}
	}

	public static void drawItemTooltip(ItemStack stack, int xPos, int yPos) {
		List<String> content = stack.getTooltip(Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
		for (int i = 0; i < content.size(); ++i) {
			if (i == 0) {
				content.set(i, stack.getRarity().rarityColor + content.get(i));
			} else {
				content.set(i, EnumChatFormatting.GRAY + content.get(i));
			}
		}
		drawHoveringText(content, xPos, yPos);
	}

	/**
	 * Draws line from first given point to second with given line width
	 *
	 *
	 * @param fromX
	 *            - first point x
	 * @param fromY
	 *            - first point y
	 * @param toX
	 *            - second point x
	 * @param toY
	 *            - second point y
	 * @param color
	 *            - rgb color
	 * @param width
	 *            - line width
	 */
	public static void drawLine(int fromX, int fromY, int toX, int toY, int color, float width) {
		GL11.glPushMatrix();
		glColorRGB(color);
		GlStateManager.disableTexture2D();
		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2i(fromX, fromY);
		GL11.glVertex2i(toX, toY);
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GL11.glPopMatrix();
	}

	/**
	 * Draws solid rectangle with the given color with top left point at xTop,
	 * yTop and bottom right point at xBot, yBot
	 *
	 * @param xTop
	 * @param yTop
	 * @param xBot
	 * @param yBot
	 * @param color
	 */
	public static void drawRect(int xTop, int yTop, int xBot, int yBot, int color) {
		int temp;
		if (xTop < xBot) {
			temp = xTop;
			xTop = xBot;
			xBot = temp;
		}
		if (yTop < yBot) {
			temp = yTop;
			yTop = yBot;
			yBot = temp;
		}
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		glColorRGB(color);
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(xTop, yBot, 0.0D).endVertex();
		renderer.pos(xBot, yBot, 0.0D).endVertex();
		renderer.pos(xBot, yTop, 0.0D).endVertex();
		renderer.pos(xTop, yTop, 0.0D).endVertex();
		tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}

	public static void drawRectWithSpecialGL(int xTop, int yTop, int xBot, int yBot, int color, Runnable specialGL) {
		int temp;
		if (xTop < xBot) {
			temp = xTop;
			xTop = xBot;
			xBot = temp;
		}
		if (yTop < yBot) {
			temp = yTop;
			yTop = yBot;
			yBot = temp;
		}
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
		specialGL.run();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		glColorRGB(color);
		renderer.begin(7, DefaultVertexFormats.POSITION);
		renderer.pos(xTop, yBot, 0.0D).endVertex();
		renderer.pos(xBot, yBot, 0.0D).endVertex();
		renderer.pos(xBot, yTop, 0.0D).endVertex();
		renderer.pos(xTop, yTop, 0.0D).endVertex();
		tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}

	/**
	 * Draws rectangle with the previously binded texture
	 *
	 * @param posX
	 *            - Position on the screen for X-axis
	 * @param posY
	 *            - Position on the screen for Y-axis
	 * @param uPos
	 *            - X position of image on binded texture
	 * @param vPos
	 *            - Y position of image on binded texture
	 * @param width
	 *            - width of rectangle
	 * @param height
	 *            - height of rectangle
	 */
	public static void drawTexturedModalRect(int posX, int posY, int uPos, int vPos, int width, int height) {
		float f = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(posX + 0, posY + height, 0).tex((uPos + 0) * f, (vPos + height) * f).endVertex();
		renderer.pos(posX + width, posY + height, 0).tex((uPos + width) * f, (vPos + height) * f).endVertex();
		renderer.pos(posX + width, posY + 0, 0).tex((uPos + width) * f, (vPos + 0) * f).endVertex();
		renderer.pos(posX + 0, posY + 0, 0).tex((uPos + 0) * f, (vPos + 0) * f).endVertex();
		tessellator.draw();
	}

	/**
	 * Draws image and scales picture (without cutting it)
	 *
	 * @param xPos
	 * @param yPos
	 * @param u
	 * @param v
	 * @param imageWidth
	 * @param imageHeight
	 * @param width
	 * @param height
	 * @param zLevel
	 */
	public static void drawTexturedModalRect(int xPos, int yPos, int u, int v, int imageWidth, int imageHeight,
			int width, int height, float zLevel) {
		float f = 1F / width;
		float f1 = 1F / height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(xPos, yPos + imageHeight, zLevel).tex( u * f, (v + imageHeight) * f1).endVertex();
		renderer.pos(xPos + imageWidth, yPos + imageHeight, zLevel).tex((u + imageWidth) * f,
				(v + imageHeight) * f1).endVertex();
		renderer.pos(xPos + imageWidth, yPos, zLevel).tex((u + imageWidth) * f, v * f1).endVertex();
		renderer.pos(xPos, yPos, zLevel).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	/**
	 * Draws a texture rectangle using the texture currently bound to the
	 * TextureManager
	 */
	public static void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int width,
			int height) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(xCoord + 0, yCoord + height, 0).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
		worldrenderer.pos(xCoord + width, yCoord + height, 0).tex( textureSprite.getMaxU(),
				textureSprite.getMaxV()).endVertex();
		worldrenderer.pos(xCoord + width, yCoord + 0, 0).tex( textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
		worldrenderer.pos(xCoord + 0, yCoord + 0, 0).tex( textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
		tessellator.draw();
	}

	/**
	 * Draws triangle pointed at the top, if you need to rotate it use glRotate
	 * before
	 *
	 * @param leftX
	 *            - left dot x
	 * @param leftY
	 *            - left dot y
	 * @param topX
	 *            - top dot x
	 * @param topY
	 *            - top dot y
	 * @param rightX
	 *            - right dot x
	 * @param rightY
	 *            - right dot y
	 * @param color
	 *            - rgb color
	 */
	public static void drawTriangle(int leftX, int leftY, int topX, int topY, int rightX, int rightY, int color) {
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glColorRGB(color);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
		renderer.pos(topX, topY, 0).endVertex();
		renderer.pos(leftX, leftY, 0).endVertex();
		renderer.pos(rightX, rightY, 0).endVertex();
		tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	/**
	 * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
	 */
	public static void drawVerticalLine(int x, int startY, int endY, int color) {
		if (endY < startY) {
			int i1 = startY;
			startY = endY;
			endY = i1;
		}

		drawRect(x, startY + 1, x + 1, endY, color);
	}

	public static TextureAtlasSprite getIcon(Block block) {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
				.getTexture(block.getDefaultState());
	}

	public static RenderItem getRenderItem() {
		return Minecraft.getMinecraft().getRenderItem();
	}

	/**
	 * Evaluates rgb from given color and bind it to GL
	 *
	 * @param color
	 *            - awt color
	 */
	public static void glColorAWT(Color color) {
		glColorRGB(color.getRGB());
	}

	/**
	 * Evaluates red, green, blue and alpha from given color and binds them to
	 * GL
	 *
	 * @param rgb
	 *            - rgb color
	 */
	public static void glColorRGB(int rgb) {
		float alpha = ((rgb >> 24) & 255) / 255.0F;
		float red = ((rgb >> 16) & 255) / 255.0F;
		float green = ((rgb >> 8) & 255) / 255.0F;
		float blue = (rgb & 255) / 255.0F;
		GlStateManager.color(red, green, blue, alpha);
	}
}
