package com.rabbit.gui.component.display;

import com.rabbit.gui.component.GuiWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class Shape extends GuiWidget {

    private ShapeType shapeType;
    private Color color;

    private Shape() {}

    public Shape(int x, int y, int width, int height, ShapeType type, Color color) {
        super(x, y, width, height);
        this.shapeType = type;
        this.color = color;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        super.onDraw(mouseX, mouseY, partialTicks);
        shapeType.draw(x, y, width, height, color.getRGB());
    }
}
