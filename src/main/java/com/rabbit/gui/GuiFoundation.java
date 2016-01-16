package com.rabbit.gui;

import com.rabbit.gui.proxy.Proxy;
import com.rabbit.gui.reference.Reference;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "rabbit-gui", name = "Rabbit Gui Library", version = "v1.3.0")
public class GuiFoundation {

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static Proxy proxy;
	
    @Mod.EventHandler
    public void postLoad(FMLPostInitializationEvent event) {
    	proxy.init();
        FMLLog.info("Rabbit Gui has been successfully initialized");
    }

   

}
