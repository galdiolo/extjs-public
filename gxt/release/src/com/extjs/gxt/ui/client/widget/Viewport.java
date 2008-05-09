/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;

/**
 * <p/>A WidgetContainer that fills the viewport and monitors window resizing.
 * Viewports are best used for applications that will fill the browser without
 * window scrolling. Children of the viewport can allow scrolling.
 * 
 * <p/>The viewport is not added to the root panel automatically. Is is not
 * neccesary to call {@link #layout()} after adding the viewport to the
 * RootPanel. Layout will be called in a deferred command after being added to
 * the root panel.
 */
public class Viewport extends Container {

  private String loadingPanelId = "loading";
  private boolean initialized;
  private boolean layoutOnAttach = true;
  private int delay = 400;
  private boolean enableScroll = false;
  
  private DelayedTask task = new DelayedTask(new Listener<ComponentEvent>() {
    public void handleEvent(ComponentEvent ce) {
      setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
    }
  });
  

  /**
   * Returns the window resize delay.
   * 
   * @return the delay
   */
  public int getDelay() {
    return delay;
  }

  /**
   * Returns the window resizing state.
   * 
   * @return true if window scrolling is enabled
   */
  public boolean getEnableScroll() {
    return enableScroll;
  }

  /**
   * The loading panel id.
   * 
   * @return the id
   */
  public String getLoadingPanelId() {
    return loadingPanelId;
  }

  public void onAttach() {
    super.onAttach();
    GXT.hideLoadingPanel(getLoadingPanelId());
    if (layoutOnAttach) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          layout();
        }
      });
    }
  }

  /**
   * Sets delay in milliseconds used to buffer window resizing (defaults to
   * 400).
   * 
   * @param delay the delay
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * Sets wether window scrolling is enabled.
   * 
   * @param enableScroll the window scroll state
   */
  public void setEnableScroll(boolean enableScroll) {
    this.enableScroll = enableScroll;
  }

  /**
   * The element id of the loading panel which will be hidden when the viewport
   * is attached (defaults to 'loading').
   * 
   * @param loadingPanelId the loading panel element id
   */
  public void setLoadingPanelId(String loadingPanelId) {
    this.loadingPanelId = loadingPanelId;
  }

  protected boolean onLayout() {
    if (!initialized) {
      initialized = true;
      el.setLeftTop(0, 0);
      el.setSize(Window.getClientWidth(), Window.getClientHeight());
    }
    lastSize = null;
    return super.onLayout();
  }

  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    el.makePositionable();
    Window.enableScrolling(enableScroll);

    Window.addWindowResizeListener(new WindowResizeListener() {
      public void onWindowResized(int width, int height) {
        task.delay(delay);
      }
    });
  }

}
