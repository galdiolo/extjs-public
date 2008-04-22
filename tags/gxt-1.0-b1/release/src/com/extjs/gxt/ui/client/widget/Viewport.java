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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;

/**
 * A WidgetContainer that fills the viewport and monitors window resizing.
 * Viewports are best used for applications that will fill the browser without
 * window scrolling. Children of the viewport can allow scrolling.
 * <p>
 * Note: The viewport is not added to the root panel automatically.
 * </p>
 */
public class Viewport extends Container {

  /**
   * The delay in milliseconds used to buffer window resizing (@link
   * {@link DelayedTask#delay(int)}.
   */
  public int delay = 400;

  /**
   * True to enable window scrolling (defaults to false).
   */
  public boolean enableScroll = false;

  /**
   * The element id of the loading panel which will be hidden when the viewport
   * is attached (defaults to 'loading').
   */
  public String loadingPanelId = "loading";

  private boolean initialized;

  private DelayedTask task = new DelayedTask(new Listener<ComponentEvent>() {
    public void handleEvent(ComponentEvent ce) {
      setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
    }
  });

  /**
   * Creates a new viewport.
   */
  public Viewport() {
   
  }

  public void onAttach() {
    super.onAttach();
    GXT.hideLoadingPanel(loadingPanelId);
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
