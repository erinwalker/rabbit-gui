package com.rabbit.gui;

import com.rabbit.gui.proxy.Proxy;
import com.rabbit.gui.reference.Reference;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

//does this need to be its own mod? we can probably just have the code live with the others
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
