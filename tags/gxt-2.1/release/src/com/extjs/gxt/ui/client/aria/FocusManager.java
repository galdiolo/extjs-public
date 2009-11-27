/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.aria;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentManager;
import com.google.gwt.event.dom.client.KeyCodes;

public class FocusManager {

  public static FocusManager get() {
    if (instance == null) {
      instance = new FocusManager();
    }
    return instance;
  }

  private List<FocusHandler> handlers = new ArrayList<FocusHandler>();
  private BaseEventPreview preview;
  private static FocusManager instance;
  
  private FocusManager() {
    preview = new BaseEventPreview() {
      @Override
      protected void onPreviewKeyPress(PreviewEvent pe) {
        super.onPreviewKeyPress(pe);

        Component c = ComponentManager.get().find(pe.getTarget());
        if (c != null) {
          int key = pe.getKeyCode();
          for (int i = 0; i < handlers.size(); i++) {
            FocusHandler handler = handlers.get(i);
            if (handler.canHandleKeyPress(c, pe)) {
              switch (key) {
                case KeyCodes.KEY_TAB:
                  handler.onTab(c, pe);
                  break;
                case KeyCodes.KEY_LEFT:
                  handler.onLeft(c, pe);
                  break;
                case KeyCodes.KEY_RIGHT:
                  handler.onRight(c, pe);
                  break;
                case KeyCodes.KEY_ESCAPE:
                  handler.onEscape(c, pe);
                  break;
                case KeyCodes.KEY_ENTER:
                  handler.onEnter(c, pe);
                  break;
              }
              return;
            }
          }
        }
      }

    };
    preview.setAutoHide(false);
    initHandlers();
  }
  
  public void disable() {
    preview.remove();
  }
  
  public void enable() {
    preview.add();
  }

  public void register(FocusHandler handler) {
    if (!handlers.contains(handler)) {
      handlers.add(handler);
    }
  }

  public void unregister(FocusHandler handler) {
    handlers.remove(handler);
  }

  protected void activate(Component component) {
//    for (int i = 0; i < handlers.size(); i++) {
//      FocusHandler handler = handlers.get(i);
//      if (handler.canActivate(component)) {
//        handler.onActivate(component);
//      }
//    }
  }
  
  protected void initHandlers() {
    register(new ButtonBarHandler());
    register(new MenuHandler());
    register(new TabPanelHandler());
    register(new DefaultHandler());
  }

}
