package com.rabbit.gui.component.control;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.rabbit.gui.component.GuiWidget;
import com.rabbit.gui.component.Shiftable;
import com.rabbit.gui.layout.LayoutComponent;
import com.rabbit.gui.render.Renderer;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.render.TextRenderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
@LayoutComponent
public class CheckBox extends GuiWidget implements Shiftable{

    //width and height of checkbox are hardcoded and can't be changed
    //if you need to change it use glScalef
    
    private static final Color DISABLED_COLOR = new Color(127, 127, 127);
    private static final Color HOVER_COLOR = new Color(255, 255, 160);
    private static final Color COMMON_COLOR = Color.white;
    
    protected static final int WIDTH = 11;
    protected static final int HEIGHT = 11;

    protected ResourceLocation buttonTexture = new ResourceLocation("textures/gui/widgets.png");
    
    @LayoutComponent
    protected boolean isChecked;
    
    @LayoutComponent
    protected String text;
    
    protected int width = WIDTH;
    protected int height = HEIGHT;

    @LayoutComponent
    protected boolean isVisible = true;
    
    @LayoutComponent
    protected boolean isEnabled = true;
    
    protected CheckBoxStatusChangedListener onStatusChangedListener;
    
    private CheckBox(){}
    
    public CheckBox(int xPos, int yPos, String title, boolean checked) {
        super(xPos, yPos, WIDTH, HEIGHT);
        this.text = title;
        this.isChecked = checked;
    }
    
    void b(ResourceLocation loc){
        Minecraft.getMinecraft().getTextureManager().getTexture(loc);
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            prepareRender();
            drawButton();
            Color color;
            if (!isEnabled()) {
                color = DISABLED_COLOR;
            } else if (isButtonUnderMouse(mouseX, mouseY)) {
                color = HOVER_COLOR;
            } else {
                color = COMMON_COLOR;
            }
            if(isChecked()){
                TextRenderer.renderString(getX() + getWidth() / 2 + 1, getY() + 1,  "x", color, TextAlignment.CENTER);
            }
            TextRenderer.renderString(getX() + getWidth() + 2, getY() + getHeight() / 2 - 3, getText());
        }
    }
    
    protected void prepareRender(){
        Minecraft.getMinecraft().getTextureManager().bindTexture(getButtonTexture());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    protected void drawButton() {
        Renderer.drawContinuousTexturedBox(getX(), getY(), 0, 46, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2); 
    }

    @Override
    public boolean onMouseClicked(int posX, int posY, int mouseButtonIndex, boolean overlap) {
        boolean clicked = isButtonUnderMouse(posX, posY) && isEnabled() && !overlap;
        if (clicked) {
            setIsCheckedWithNotify(!isChecked());
            playClickSound();
        }
        return clicked;
    }

    public boolean isChecked(){
        return isChecked;
    }
    
    public CheckBox setIsChecked(boolean state){
        this.isChecked = state;
        return this;
    }

    public CheckBox setIsCheckedWithNotify(boolean state){
        setIsChecked(state);
        if(getStatusChangedListener() != null) getStatusChangedListener().onStatusChanged(this);
        return this;
    }
    
    public CheckBox setStatusChangedListener(CheckBoxStatusChangedListener listener){
        this.onStatusChangedListener = listener;
        return this;
    }
    
    public boolean isButtonUnderMouse(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    /**
     * @return <code> true </code> if button would be rendered
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @return <code> true</code> if button can be clicked
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    public CheckBox setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    public CheckBox setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public ResourceLocation getButtonTexture() {
        return buttonTexture;
    }

    public CheckBox setCustomTexture(ResourceLocation res) {
        this.buttonTexture = res;
        return this;
    }
    
    public CheckBox setText(String text){
        this.text = text;
        return this;
    }
    
    public String getText(){
        return text;
    }

    protected void playClickSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }
    
    public CheckBoxStatusChangedListener getStatusChangedListener(){
        return onStatusChangedListener;
    }
    
    @Override
    public CheckBox setId(String id) {
        assignId(id);
        return this;
    }
    
    @FunctionalInterface
    public interface CheckBoxStatusChangedListener{
        void onStatusChanged(CheckBox box);
    }

    @Override
    public void shiftX(int x) {
        this.setX(getX() + x);
    }

    @Override
    public void shiftY(int y) {
        this.setY(getY() + y);
    }
    
}
