/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.LayoutEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout provides the basic foundation for all other layout classes in GXT. It
 * is a non-visual class that simply provides the base logic required to
 * function as a layout. This class is intended to be extended.
 * 
 * <p/> Layout instances should not be shared with multiple containers.
 * 
 * @see LayoutContainer
 */
public abstract class Layout extends BaseObservable {

  protected Container<Component> container;
  protected Component activeItem;
  protected boolean renderHidden;
  protected boolean monitorResize;

  private String extraStyle;
  private int resizeDelay = 100;

  private Listener resizeListener = new Listener<ComponentEvent>() {
    public void handleEvent(ComponentEvent be) {
      if (container.isMonitorResize()) {
        layout();
      }
    }
  };

  private DelayedTask task = new DelayedTask(new Listener() {
    public void handleEvent(BaseEvent be) {
      if (container != null) {
        layout();
      }
    }
  });

  protected void onResize(ComponentEvent ce) {
    if (resizeDelay != -1) {
      task.delay(resizeDelay);
    } else {
      layout();
    }
  }

  public void layout() {
    El target = container.getLayoutTarget();
    onLayout(container, target);
    fireEvent(Events.AfterLayout, new LayoutEvent(container, this));
  }

  /**
   * Returns the extra style name.
   * 
   * @return the extra style
   */
  public String getExtraStyle() {
    return extraStyle;
  }

  /**
   * Returns the window resize delay.
   * 
   * @return the delay
   */
  public int getResizeDelay() {
    return resizeDelay;
  }

  /**
   * Returns true if the container will be render child components hidden.
   * 
   * @return the render hidden state
   */
  public boolean isRenderHidden() {
    return renderHidden;
  }

  /**
   * Sets the layout's container.
   * 
   * @param ct the container
   */
  public void setContainer(Container ct) {
    if (monitorResize && container != ct) {
      if (container != null) {
        container.removeListener(Events.Resize, resizeListener);
      }
      if (ct != null) {
        ct.addListener(Events.Resize, resizeListener);
      }

    }
    this.container = ct;
  }

  /**
   * Sets an optional extra CSS style name that will be added to the container.
   * This can be useful for adding customized styles to the container or any of
   * its children using standard CSS rules.
   * 
   * @param extraStyle the extra style name
   */
  public void setExtraStyle(String extraStyle) {
    this.extraStyle = extraStyle;
  }

  /**
   * True to hide each contained component on render (defaults to false).
   * 
   * @param renderHidden true to render hidden
   */
  public void setRenderHidden(boolean renderHidden) {
    this.renderHidden = renderHidden;
  }

  /**
   * Sets the number of milliseconds to buffer resize events (defaults to 400).
   * Only applies when {@link #monitorResize} = true.
   * 
   * @param resizeDelay the delay in milliseconds
   */
  public void setResizeDelay(int resizeDelay) {
    this.resizeDelay = resizeDelay;
  }

  protected El fly(Element elem) {
    return El.fly(elem);
  }
  
  protected El fly(com.google.gwt.dom.client.Element elem) {
    return El.fly(elem);
  }

  protected boolean isValidParent(Element elem, Element parent) {
    if (DOM.getParent(elem) == parent) {
      return true;
    }
    return false;
  }

  protected void onLayout(Container container, El target) {
    renderAll(container, target);
  }

  protected void renderAll(Container container, El target) {
    int count = container.getItemCount();
    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      if (!c.isRendered() || !isValidParent(c.el().dom, target.dom)) {
        renderComponent(c, i, target);
      }
    }
  }

  protected void renderComponent(Component component, int index, El target) {
    if (component.isRendered()) {
      target.insertChild(component.el().dom, index);
    } else {
      component.render(target.dom, index);
    }
    if (extraStyle != null) {
      component.addStyleName(extraStyle);
    }
    if (renderHidden && component != activeItem) {
      component.setVisible(false);
    }
    Object data = component.getData();
    if (data != null && data instanceof LayoutData) {
      LayoutData ld = (LayoutData)data;
      applyMargins(component.el(), ld.getMargins());
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
    } else if (c.isRendered()) {
      fly(c.getElement()).setSize(width, height, true);
    }
  }
  
  protected void layoutContainer() {
    container.layout();
  }
  
  protected void applyPadding(El target, Margins margins) {
    target.setStyleAttribute("paddingLeft", margins.left);
    target.setStyleAttribute("paddingTop", margins.top);
    target.setStyleAttribute("paddingRight", margins.right);
    target.setStyleAttribute("paddingBottom", margins.bottom);
  }
  
  protected void applyMargins(El target, Margins margins) {
    if (margins == null) return;
    target.setStyleAttribute("marginLeft", margins.left);
    target.setStyleAttribute("marginTop", margins.top);
    target.setStyleAttribute("marginRight", margins.right);
    target.setStyleAttribute("marginBottom", margins.bottom);
  }

}
