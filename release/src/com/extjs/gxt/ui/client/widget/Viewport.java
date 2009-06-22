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
 * A LayoutContainer that fills the browser window and monitors window resizing.
 * Viewports are best used for applications that will fill the browser without
 * window scrolling. Children of the viewport can allow scrolling.
 * <p/>
 * Code snippet:
 * 
 * <pre>
 * Viewport viewport = new Viewport();
 * viewport.add(new ContentPanel(), new MarginData(10));
 * RootPanel.get().add(viewport);
 * </pre>
 * 
 * <p/>
 * The viewport is not added to the root panel automatically. Is is not
 * necessary to call {@link #layout()} after adding the viewport to the
 * RootPanel. Layout will be called in a deferred command after being added to
 * the root panel.
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>LayoutContainer AfterLayout</dt>
 * <dd>ScrollContainer Scroll</dd>
 * <dd>Container BeforeAdd</dd>
 * <dd>Container Add</dd>
 * <dd>Container BeforeRemove</dd>
 * <dd>Container Remove</dd>
 * <dd>BoxComponent Move</dd>
 * <dd>BoxComponent Resize</dd>
 * <dd>Component Enable</dd>
 * <dd>Component Disable</dd>
 * <dd>Component BeforeHide</dd>
 * <dd>Component Hide</dd>
 * <dd>Component BeforeShow</dd>
 * <dd>Component Show</dd>
 * <dd>Component Attach</dd>
 * <dd>Component Detach</dd>
 * <dd>Component BeforeRender</dd>
 * <dd>Component Render</dd>
 * <dd>Component BrowserEvent</dd>
 * <dd>Component BeforeStateRestore</dd>
 * <dd>Component StateRestore</dd>
 * <dd>Component BeforeStateSave</dd>
 * <dd>Component SaveState</dd>
 * </dl>
 */
public class Viewport extends LayoutContainer {

  private String loadingPanelId = "loading";
  private int delay = 100;
  private boolean enableScroll = false;

  private DelayedTask task = new DelayedTask(new Listener<ComponentEvent>() {
    public void handleEvent(ComponentEvent ce) {
      Viewport.this.setBounds(0, 0, Window.getClientWidth(), Window.getClientHeight());
      layout();
    }
  });
  
  private WindowResizeListener listener;

  public Viewport() {
    baseStyle = "x-viewport";
    layoutOnAttach = true;
  }

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

  @Override
  public void onAttach() {
    super.onAttach();
    task.delay(delay);
    GXT.hideLoadingPanel(loadingPanelId);
    Window.addWindowResizeListener(listener);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    Window.removeWindowResizeListener(listener);
  }

  /**
   * Sets delay in milliseconds used to buffer window resizing (defaults to
   * 100).
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

  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    el().setLeftTop(0, 0);

    Window.enableScrolling(enableScroll);

    listener = new WindowResizeListener() {
      public void onWindowResized(int width, int height) {
        task.delay(delay);
      }
    };
  }

}
