package com.rabbit.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents background elements of the show
 */
@SideOnly(Side.CLIENT)
public interface IBackground {
    
    void onDraw(int width, int height, int mouseX, int mouseY, float partialTicks);        
}
