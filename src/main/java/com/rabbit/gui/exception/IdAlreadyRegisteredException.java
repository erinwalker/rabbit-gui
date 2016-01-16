package com.rabbit.gui.exception;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IdAlreadyRegisteredException extends RuntimeException {

    public IdAlreadyRegisteredException() {
        super();
    }

    public IdAlreadyRegisteredException(String message) {
        super(message);
    }
    
}
