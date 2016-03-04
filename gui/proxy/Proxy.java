package com.rabbit.gui.proxy;

import com.rabbit.gui.base.Stage;
import com.rabbit.gui.show.IShow;

public interface Proxy {
        public void renderGUI();
        
        public void display(IShow show);

        public Stage getCurrentStage();

		public void init();
}