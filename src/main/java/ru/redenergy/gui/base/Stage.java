package ru.redenergy.gui.base;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import ru.redenergy.gui.show.IShow;

public class Stage extends GuiScreen{
    
    protected IShow show;
    protected boolean hasBeenInitialized;
    
    public Stage(){}
    
    public Stage(IShow show){
        this.show = show;
        this.show.setStage(this);
    }
    
    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        show.onDraw(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        show.onKeyTyped(p_73869_1_, p_73869_2_);
        if(p_73869_2_ == Keyboard.KEY_ESCAPE){
            Minecraft.getMinecraft().setIngameFocus();
        }
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        show.onMouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    public final void initGui() {
        show.setSize(width, height);
        if(show instanceof ComponentContainer) ((ComponentContainer)show).getComponentsList().clear();
        if (!hasBeenInitialized) {
            show.onInit();
            hasBeenInitialized = true;
        }
        show.setup();
        if(show instanceof ComponentContainer) ((ComponentContainer)show).getComponentsList().forEach(component -> component.setup());
    }

    @Override
    public void updateScreen() {
        show.onUpdate();
    }

    @Override
    public void onGuiClosed() {
        show.onClose();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public Stage setShow(IShow show){
        this.show = show;
        return this;
    }
    
    public IShow getShow(){
        return show;
    }
    
}
