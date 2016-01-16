package com.rabbit.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents element which position can be changed after show setup
 */

@SideOnly(Side.CLIENT)
public interface Shiftable {
    public void shiftX(int x);
    
    public void shiftY(int y);
}
