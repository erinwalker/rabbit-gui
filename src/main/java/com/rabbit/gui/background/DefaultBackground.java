package com.rabbit.gui.background;

import java.awt.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultBackground extends ColorBackground {

    public DefaultBackground() {
        super(new Color(16, 16, 16, 192));
    }
    
}
