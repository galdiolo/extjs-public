/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.desktop.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.event.WindowManagerEvent;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A desktop represents a desktop like application which contains a task bar,
 * start menu, and shortcuts. 
 * 
 * <p/> Rather than adding content directly to the
 * root panel, content should be wrapped in windows. Windows can be opened via
 * shortcuts and the start menu.
 */
public class Desktop {

  private TaskBar taskBar = new TaskBar();
  private WindowListener listener;
  private Listener<WindowManagerEvent> managerListener;
  private Viewport viewport;
  private Window activeWindow;
  private List<Shortcut> shortcuts = new ArrayList<Shortcut>();
  private El shortcutEl;
  
  public Desktop() {
    initListeners();

    viewport = new Viewport();
    viewport.setLayout(new RowLayout());

    LayoutContainer lc = new LayoutContainer() {
      @Override
      protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        el().appendChild(XDOM.getElementById("x-desktop"));
      }
    };

    viewport.add(lc, new RowData(1, 1));
    viewport.add(taskBar, new RowData(1, 30));

    Element el = XDOM.getElementById("x-shortcuts");
    if (el == null) {
      el = DOM.createDiv();
      el.setClassName("x-shortcuts");
      XDOM.getBody().appendChild(el);
    }
    shortcutEl = new El(el);

    RootPanel.get().add(viewport);
  }

  public void addShortcut(Shortcut shortcut) {
    shortcuts.add(shortcut);
    shortcut.render(shortcutEl.dom);
    ComponentHelper.doAttach(shortcut);
  }

  public void removeShortcut(Shortcut shortcut) {
    shortcuts.remove(shortcut);
    shortcutEl.dom.removeChild(shortcut.getElement());
    ComponentHelper.doDetach(shortcut);
  }

  public StartMenu getStartMenu() {
    return taskBar.getStartMenu();
  }

  public TaskBar getTaskBar() {
    return taskBar;
  }

  public void minimizeWindow(Window window) {
    window.setData("minimize", true);
    window.hide();
  }

  protected void initListeners() {
    listener = new WindowListener() {
      @Override
      public void windowActivate(WindowEvent we) {
        markActive(we.window);
      }

      @Override
      public void windowDeactivate(WindowEvent we) {
        markInactive(we.window);
      }

      @Override
      public void windowMinimize(WindowEvent we) {
        minimizeWindow(we.window);
      }

    };

    managerListener = new Listener<WindowManagerEvent>() {

      public void handleEvent(WindowManagerEvent be) {
        switch (be.type) {
          case Events.Register:
            onRegister(be.window);
            break;
          case Events.Unregister:
            onUnregister(be.window);
            break;
        }
      }

    };

    WindowManager.get().addListener(Events.Register, managerListener);
    WindowManager.get().addListener(Events.Unregister, managerListener);
  }

  private void onRegister(Window window) {
    taskBar.addTaskButton(window);
    window.addWindowListener(listener);
  }

  private void onUnregister(Window window) {
    if (activeWindow == window) {
      activeWindow = null;
    }
    taskBar.removeTaskButton((TaskButton) window.getData("taskButton"));
  }

  private void markActive(Window window) {
    if (activeWindow != null && activeWindow != window) {
      markInactive(activeWindow);
    }
    taskBar.setActiveButton((TaskButton) window.getData("taskButton"));
    activeWindow = window;
    TaskButton btn = window.getData("taskButton");
    btn.addStyleName("active-win");
    window.setData("minimize", null);
  }

  private void markInactive(Window window) {
    if (window == activeWindow) {
      activeWindow = null;
      TaskButton btn = window.getData("taskButton");
      btn.removeStyleName("active-win");
    }
  }

}
