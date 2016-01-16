package com.rabbit.gui.layout.argument;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ILayoutArgument {
    
    String fieldName();
}
