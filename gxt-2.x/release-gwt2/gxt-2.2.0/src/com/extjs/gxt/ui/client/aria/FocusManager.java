/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.aria;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentManager;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class FocusManager {

  public static final EventType TabNext = new EventType();

  public static final EventType TabPrevious = new EventType();

  public static FocusManager get() {
    if (instance == null) {
      instance = new FocusManager();
    }
    return instance;
  }

  private List<FocusHandler> handlers = new ArrayList<FocusHandler>();
  private List<NavigationHandler> navigationHandlers = new ArrayList<NavigationHandler>();
  private BaseEventPreview preview;
  private static FocusManager instance;
  private boolean managed = true;
  private boolean insertExitFocusElement = true;
  private Component focusComponent;
  private WindowManager mgr = WindowManager.get();

  private FocusManager() {
    preview = new BaseEventPreview() {

      protected void onClick(PreviewEvent pe) {
        Component c = ComponentManager.get().find(pe.getTarget());
        if (c != null) {
          if (isWindow(c) == null && focusComponent == null) {
            focusComponent = c;
          }
        }

      };

      @Override
      protected void onPreviewKeyPress(PreviewEvent pe) {
        super.onPreviewKeyPress(pe);
        int key = pe.getKeyCode();

        Component c = ComponentManager.get().find(pe.getTarget());
        if (key == 117) {
          onToggle(c, pe);
        }
        Window w = null;
        if (c != null) {
          w = isWindow(c);
          if (w == null) {
            focusComponent = c;
          }
          for (int i = 0; i < handlers.size(); i++) {
            FocusHandler handler = handlers.get(i);
            if (handler.canHandleKeyPress(c, pe)) {
              switch (key) {
                case KeyCodes.KEY_TAB:
                  if (!pe.isControlKey()) {
                    handler.onTab(c, pe);
                  }
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

  public NavigationHandler findNavigationHandler(Component comp) {
    for (int i = 0, len = navigationHandlers.size(); i < len; i++) {
      NavigationHandler h = navigationHandlers.get(i);
      if (h.canHandleTabKey(comp)) {
        return h;
      }
    }
    return null;
  }

  public boolean isInsertExitFocusElement() {
    return insertExitFocusElement;
  }

  public boolean isManaged() {
    return managed;
  }

  public void register(FocusHandler handler) {
    if (!handlers.contains(handler)) {
      handlers.add(handler);
    }
  }

  public void register(NavigationHandler handler) {
    navigationHandlers.add(handler);
  }

  public void setInsertExitFocusElement(boolean insertExitFocusElement) {
    this.insertExitFocusElement = insertExitFocusElement;
  }

  public void setManaged(boolean managed) {
    this.managed = managed;
  }

  public void unregister(FocusHandler handler) {
    handlers.remove(handler);
  }

  public void unregister(NavigationHandler handler) {
    navigationHandlers.remove(handler);
  }

  protected void initHandlers() {
    register(new FieldSetHandler());
    register(new HtmlEditorHandler());
    register(new ToolButtonHandler());
    register(new ToolBarHandler());
    register(new MenuHandler());
    register(new TabPanelHandler());
    register(new InputSliderHandler());
    register(new DualListFieldHandler());
    register(new FieldHandler());
    register(new HeaderHandler());

    // always last
    register(new DefaultHandler());

    register(new ContentPanelNavigationHandler());

  }

  protected void onToggle(Component c, PreviewEvent pe) {
    pe.stopEvent();
    if (c != null) {
      Window w = isWindow(c);
      if (w != null) {
        onWindowToggle(w, pe);
      } else {
        onComponentToggle(c, pe);
      }
    } else {
      List<Window> windows = mgr.getWindows();
      if (windows.size() > 0) {
        mgr.bringToFront(windows.get(0));
      }
    }

  }

  private Window isWindow(Component c) {
    Widget w = c;
    while (w != null && !(w instanceof Window)) {
      w = w.getParent();
    }
    if (w instanceof Window) {
      return (Window) w;
    }
    return null;
  }

  private void moveToApp() {
    if (focusComponent != null) {
      focusComponent.focus();
    } else {
      FocusFrame.get().unframe();
      if (RootPanel.get().getWidgetCount() > 0) {
        Widget w = RootPanel.get().getWidget(0);
        El.fly(w.getElement()).focus();
      } else {
        El.fly(RootPanel.get().getElement()).focus();
      }
    }
  }

  private void onComponentToggle(Component c, PreviewEvent pe) {
    List<Window> windows = mgr.getWindows();
    if (windows.size() > 0) {
      if (pe.isShiftKey()) {
        mgr.bringToFront(windows.get(windows.size() - 1));
      } else {
        mgr.bringToFront(windows.get(0));
      }
    }
  }

  private void onWindowToggle(Window w, PreviewEvent pe) {
    if (pe.isShiftKey()) {
      if (!mgr.activatePrevious(w)) {
        moveToApp();
      }

    } else {
      if (!mgr.activateNext(w)) {
        moveToApp();
      }
    }
  }

}
