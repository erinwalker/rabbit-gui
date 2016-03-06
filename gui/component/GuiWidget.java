package com.rabbit.gui.component;

import java.util.ArrayList;
import java.util.List;

import com.rabbit.gui.base.WidgetContainer;
import com.rabbit.gui.exception.IdAlreadyRegisteredException;
import com.rabbit.gui.layout.LayoutComponent;

/**
 * Represents components of the screen
 *
 * @author RedEnergy
 */
public abstract class GuiWidget implements IGui, WidgetContainer {

	protected WidgetContainer parent;
	protected List<IGui> components;

	@LayoutComponent
	protected String id;

	@LayoutComponent
	protected int x;

	@LayoutComponent
	protected int y;

	@LayoutComponent
	protected int width;

	@LayoutComponent
	protected int height;

	protected GuiWidget() {
		this.x = -1;
		this.y = -1;
		this.width = -1;
		this.height = -1;
	}

	protected GuiWidget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected final void assignId(String id) {
		if (this.findComponentById(id) != null) {
			throw new IdAlreadyRegisteredException("Id " + id + " already occupied");
		}
		this.id = id;
	}

	@Override
	public List<IGui> getComponentsList() {
		if (this.components == null) {
			this.components = new ArrayList<>();
		}
		return this.components;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * Returns and id component, can be <code>null</code>
	 *
	 * @return
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * Returns pane in which this component registered
	 *
	 * @return parent pane - can be null if it hasn't been registered yet
	 */
	@Override
	public WidgetContainer getParent() {
		return this.parent;
	}

	public int getWidth() {
		return this.width;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	/**
	 * Called when screen is about to be closed
	 */
	@Override
	public void onClose() {
		this.getComponentsList().forEach(com -> com.onClose());
	}

	/**
	 * Called on every render tick
	 *
	 * @param mouseX
	 * @param mouseY
	 * @param partialTicks
	 */
	@Override
	public void onDraw(int mouseX, int mouseY, float partialTicks) {
		this.getComponentsList().forEach(com -> com.onDraw(mouseX, mouseY, partialTicks));
	}

	/**
	 * Called when user presses key on keyboard
	 *
	 * @param typedChar
	 * @param typedIndex
	 */
	@Override
	public void onKeyTyped(char typedChar, int typedIndex) {
		this.getComponentsList().forEach(com -> com.onKeyTyped(typedChar, typedIndex));
	}

	/**
	 * Called when user clicks mouse
	 *
	 * @param posX
	 * @param posY
	 * @param mouseButtonIndex
	 *
	 * @return <code>true</code> if the element has been clicked
	 */
	@Override
	public boolean onMouseClicked(int posX, int posY, int mouseButtonIndex, boolean overlap) {
		boolean clicked = false;
		for (IGui com : this.getComponentsList()) {
			clicked = com.onMouseClicked(posX, posY, mouseButtonIndex, clicked) || clicked;
		}
		return clicked;
	}

	/**
	 * Called then mouse moved or scrolled
	 */
	@Override
	public void onMouseInput() {
		this.getComponentsList().forEach(com -> com.onMouseInput());
	}

	@Override
	public void onMouseRelease(int mouseX, int mouseY) {
		this.getComponentsList().forEach(com -> com.onMouseRelease(mouseX, mouseY));
	}

	/**
	 * Called when component registered in pane
	 */
	@Override
	public void onRegistered(WidgetContainer pane) {
		this.setParent(pane);
	}

	/**
	 * Called every update tick (usually 20 times in second)
	 */
	@Override
	public void onUpdate() {
		this.getComponentsList().forEach(com -> com.onUpdate());
	}

	/**
	 * Registers component as a part of pane
	 *
	 * @param component
	 */
	@Override
	public void registerComponent(IGui component) {
		this.getComponentsList().add(component);
		if (component instanceof WidgetContainer) {
			((WidgetContainer) component).onRegistered(this);
		}
	}

	public GuiWidget setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * Sets this component id to provided
	 *
	 * @return self
	 */
	@Override
	public <T> IGui setId(String id) {
		this.assignId(id);
		return this;
	}

	@Override
	public void setParent(WidgetContainer pane) {
		this.parent = pane;
	}

	/**
	 * Called on every screen resize. All components recommended to be
	 * registered in this moment
	 */
	@Override
	public void setup() {
		this.getComponentsList().clear();
	}

	public GuiWidget setWidth(int width) {
		this.width = width;
		return this;
	}

	public GuiWidget setX(int x) {
		this.x = x;
		return this;
	}

	public GuiWidget setY(int y) {
		this.y = y;
		return this;
	}

}
