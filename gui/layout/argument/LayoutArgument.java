package com.rabbit.gui.layout.argument;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Represents component argument parsed from json layout
 */
@SideOnly(Side.CLIENT)
public class LayoutArgument<T> implements ILayoutArgument{
    
    private String fieldName;
    private T value;
    
    public LayoutArgument(String fieldname, T value){
        this.fieldName = fieldname;
        this.value = value;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
    
    public T get(){
        return value;
    }
}
