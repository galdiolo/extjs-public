/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout provides the basic foundation for all other layout classes in GXT.
 * It is a non-visual class that simply provides the base logic required to
 * function as a layout. This class is intended to be extended.
 * 
 * @see Container
 */
public abstract class Layout {

  /**
   * An optional extra CSS style name that will be added to the container
   * (defaults to null). This can be useful for adding customized styles to the
   * container or any of its children using standard CSS rules.
   */
  public String extraStyle;

  /**
   * True to hide each contained item on render (defaults to false).
   */
  public boolean renderHidden;

  /**
   * True to execute this layout when the browser window is resized (defaults to
   * false). This is usefull when a container is isolated and not part of the
   * container hierarchy.
   */
  public boolean monitorWindowResize;

  /**
   * The number of milliseconds to buffer resize events (defaults to 400). Only
   * applies when {@link #monitorWindowResize} = true.
   */
  public int resizeDelay = 400;

  protected Container container;
  protected Component activeItem;

  private DelayedTask task = new DelayedTask(new Listener() {
    public void handleEvent(BaseEvent be) {
      if (container != null) {
        container.layout(true);
      }
    }
  });

  /**
   * Executes the layout.
   * 
   * @param container the container component
   */
  public void layout(Container container) {
    this.container = container;
    El target = container.getLayoutTarget();
    onLayout(container, target);
  }

  /**
   * Sets the layout's container.
   * 
   * @param ct the container
   */
  public void setContainer(Container ct) {
    this.container = ct;
    if (monitorWindowResize) {
      Window.addWindowResizeListener(new WindowResizeListener() {
        public void onWindowResized(int width, int height) {
          task.delay(resizeDelay);
        }
      });
    }
  }

  protected El fly(Element elem) {
    return El.fly(elem);
  }

  protected boolean isValidParent(Element elem, Element parent) {
    if (DOM.getParent(elem) == parent) {
      return true;
    }
    return false;
  }

  protected void onLayout(Container container, El target) {
    container.el.setVisibility(false);
    renderAll(container, target);
    container.el.setVisibility(true);
  }

  protected void renderAll(Container container, El target) {
    int count = container.getItemCount();
    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      if (!c.isRendered() || !isValidParent(c.el.dom, target.dom)) {
        renderComponent(c, i, target);
      }
    }
  }

  protected void renderComponent(Component component, int index, El target) {
    if (extraStyle != null) {
      component.addStyleName(extraStyle);
    }

    if (component.isRendered()) {
      target.insertChild(component.el.dom, index);
    } else {
      component.render(target.dom, index);
    }

    if (renderHidden && component != activeItem) {
      component.setVisible(false);
    }
  }

  protected void setBounds(Widget w, int x, int y, int width, int height) {
    if (w instanceof BoxComponent) {
      ((BoxComponent) w).setBounds(x, y, width, height);
    } else {
      fly(w.getElement()).setBounds(x, y, width, height, true);
    }
  }

  protected void setSize(Component c, int width, int height) {
    if (c instanceof BoxComponent) {
      ((BoxComponent) c).setSize(width, height);
    } else if (c.isRendered()){
      fly(c.getElement()).setSize(width, height, true);
    }
  }

}
