package com.rabbit.gui.layout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Annotated type or field can be used in the gui layout
 */
@SideOnly(Side.CLIENT)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutComponent {}
