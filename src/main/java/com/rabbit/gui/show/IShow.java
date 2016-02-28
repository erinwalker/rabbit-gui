package com.rabbit.gui.show;

import com.rabbit.gui.base.Stage;
import com.rabbit.gui.component.IBackground;
import com.rabbit.gui.component.IGui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IShow extends IGui{
    
    void onInit();
 
    void setSize(int width, int height);
    
    Stage getStage();
    
    void setStage(Stage stage);
    
    String getTitle();
    
    void setTitle(String title);
    
    int getWidth();
    
    int getHeight();
    
    void setBackground(IBackground background);

    boolean hasBeenInitialized();

    IBackground getBackground();
}
