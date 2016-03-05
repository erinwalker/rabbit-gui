package com.rabbit.gui.show;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.Display;

import com.rabbit.gui.base.Stage;
import com.rabbit.gui.base.WidgetContainer;
import com.rabbit.gui.component.IBackground;
import com.rabbit.gui.component.IGui;

import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Show implements IShow, WidgetContainer {

	protected List<IGui> components = new ArrayList();
	protected String id;
	protected int width, height;
	protected Stage stage;
	protected String title;
	private IBackground background;
	private boolean initialized = false;

	@Override
	public IBackground getBackground() {
		return this.background;
	}

	@Override
	public List<IGui> getComponentsList() {
		return this.components;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public WidgetContainer getParent() {
		return null;
	}

	@Override
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public boolean hasBeenInitialized() {
		return this.initialized;
	}

	@Override
	public void onClose() {
		this.getComponentsList().forEach(com -> com.onClose());
		Display.setTitle("Minecraft 1.8");
	}

	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		if (this.getBackground() != null) {
			this.getBackground().onDraw(this.width, this.height, mouseX, mouseY, partialTicks);
		}
		// we need to draw components in reversed order, the last added element
		// will be under earlier
		for (ListIterator<IGui> it = this.getComponentsList().listIterator(this.getComponentsList().size()); it
				.hasPrevious();) {
			it.previous().onDraw(mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public void onInit() {
		if (!StringUtils.isNullOrEmpty(this.title)) {
			this.updateDisplayTitle();
		}
		this.initialized = true;
	}

	@Override
	public void onKeyTyped(char typedChar, int typedIndex) {
		this.getComponentsList().forEach(com -> com.onKeyTyped(typedChar, typedIndex));
	}

	@Override
	public boolean onMouseClicked(int posX, int posY, int mouseButtonIndex, boolean overlap) {
		boolean clicked = false;
		for (IGui com : this.getComponentsList()) {
			clicked = com.onMouseClicked(posX, posY, mouseButtonIndex, clicked) || clicked;
		}
		return clicked;
	}

	@Override
	public void onMouseInput() {
		this.getComponentsList().forEach(com -> com.onMouseInput());
	}

	@Override
	public void onMouseRelease(int mouseX, int mouseY) {
		this.getComponentsList().forEach(com -> com.onMouseRelease(mouseX, mouseY));
	}

	@Override
	public void onRegistered(WidgetContainer pane) {
		Validate.isTrue(pane instanceof Stage, "Provided WidgetContainer should be Stage");
		this.setStage((Stage) pane);
	}

	@Override
	public void onUpdate() {
		this.getComponentsList().forEach(com -> com.onUpdate());
	}

	@Override
	public void registerComponent(IGui component) {
		this.components.add(component);
		if (component instanceof WidgetContainer) {
			((WidgetContainer) component).onRegistered(this);
		}
	}

	@Override
	public void setBackground(IBackground background) {
		this.background = background;
	}

	@Override
	public <T> IGui setId(String id) {
		this.id = id;
		return this;
	}

	/*
	 * DO NOT CALL THIS METHOD. Use #setStage instead!
	 */
	@Deprecated
	@Override
	public final void setParent(WidgetContainer c) {
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		this.updateDisplayTitle();
	}

	@Override
	public void setup() {
		this.getComponentsList().forEach(com -> com.setup());
	}

	private void updateDisplayTitle() {
		Display.setTitle("Minecraft 1.8" + " - " + this.title);
	}

}
