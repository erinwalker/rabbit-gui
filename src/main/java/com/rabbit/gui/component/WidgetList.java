package com.rabbit.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Represents gui element which contains and displays multiple objects
 * @param <T> - type of contained objects
 */

@SideOnly(Side.CLIENT)
public interface WidgetList<T> {
    
    public WidgetList<T> add(T object);
    
    public WidgetList<T> addAll(T ... objects);
    
    public WidgetList<T> remove(T object);
    
    public WidgetList<T> clear();
    
    public Object getContent();
}
