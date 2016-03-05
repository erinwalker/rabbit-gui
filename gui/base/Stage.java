package com.rabbit.gui.base;

import java.io.IOException;
import java.util.Stack;

import org.lwjgl.input.Keyboard;

import com.rabbit.gui.component.IGui;
import com.rabbit.gui.show.IShow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Stage extends GuiScreen {

	/**
	 * Currently displayed show
	 */
	protected IShow show;

	/**
	 * Contains all opened shows (including current)
	 */
	private Stack<IShow> showHistory = new Stack<>();

	/**
	 * Will create an empty show <br>
	 * Note: If you try to render empty stage the crash may occur
	 */
	public Stage() {
	}

	/**
	 * Creates stage and places the given show on it
	 *
	 * @param show
	 *            - displayed show
	 */
	public Stage(IShow show) {
		this.display(show);
	}

	public void close() {
		Minecraft.getMinecraft().setIngameFocus();
	}

	/**
	 * Puts the given show on stage
	 *
	 * @param show
	 *            - show to display
	 */
	public void display(IShow show) {
		this.setShow(show);
		show.setStage(this);
		this.reinitShow(true);
		this.pushHistory(this.show);
	}

	/**
	 * Displays previously opened show <br>
	 * If current show is the only opened show this stage will be closed <br>
	 * If history is empty nothing will happen
	 */
	public void displayPrevious() {
		if (this.getShowHistory().size() != 0) {
			if (this.getShowHistory().size() == 1) {
				this.close();
			} else {
				this.getShowHistory().pop(); // remove current
				this.display(this.getShowHistory().pop()); // remove and open
															// previous
			}
		}
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.show.onDraw(mouseX, mouseY, partialTicks);
	}

	/**
	 * Returns currently displayed show
	 *
	 * @return currently display show
	 */
	public IShow getShow() {
		return this.show;
	}

	/**
	 * @return This stage history
	 */
	public Stack<IShow> getShowHistory() {
		return this.showHistory;
	}

	/**
	 * Wrapper for vanilla method
	 *
	 * @throws IOException
	 */
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.show.onMouseInput();
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	public final void initGui() {
		this.reinitShow();
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	protected void keyTyped(char typedChar, int typedKeyIndex) {
		this.show.onKeyTyped(typedChar, typedKeyIndex);
		if (typedKeyIndex == Keyboard.KEY_ESCAPE) {
			Minecraft.getMinecraft().setIngameFocus();
		}
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	public void mouseClicked(int clickX, int clickY, int mouseIndex) {
		this.show.onMouseClicked(clickX, clickY, mouseIndex, false);
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int type) {
		super.mouseReleased(mouseX, mouseY, type);
		if ((type == 0) || (type == 1)) {
			this.show.onMouseRelease(mouseX, mouseY);
		}
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	public void onGuiClosed() {
		this.show.onClose();
	}

	/**
	 * Updated stage's history and adds the given show <br>
	 * If given show already in the history it will be moved to the start
	 *
	 * @param show
	 *            - show which must be placed in history
	 */
	private void pushHistory(IShow show) {
		if (this.showHistory.contains(show)) {
			this.showHistory.remove(show);
		}
		this.showHistory.push(show);
	}

	/**
	 * Shortcut for #reinitShow(false), provided for backward compatibility
	 */
	public void reinitShow() {
		this.reinitShow(false);
	}

	/**
	 * Reinitialized currently opened shows, updates it's resolution and
	 * re-setups it. <br>
	 * If <code>forceInit</code> is <code>true</code> show#onInit() will be
	 * called even if it's been already initialized
	 *
	 * @param forceInit
	 *            - if <code>true</code> show#onInit() will be called event if
	 *            it's been already initialized
	 */
	public void reinitShow(boolean forceInit) {
		this.show.setSize(this.width, this.height);
		if (this.show instanceof WidgetContainer) {
			((WidgetContainer) this.show).getComponentsList().clear();
		}
		if (!this.show.hasBeenInitialized() || forceInit) {
			this.show.onInit();
		}
		this.show.setup();
		if (this.show instanceof WidgetContainer) {
			((WidgetContainer) this.show).getComponentsList().forEach(IGui::setup);
		}
	}

	/**
	 * Setter for show field, if you want to display show use
	 * {@link #display(IShow)} instead
	 *
	 * @param show
	 *            - new show
	 * @return current instance of Stage
	 */
	public Stage setShow(IShow show) {
		this.show = show;
		return this;
	}

	/**
	 * Wrapper for vanilla method
	 */
	@Override
	public void updateScreen() {
		this.show.onUpdate();
	}
}
