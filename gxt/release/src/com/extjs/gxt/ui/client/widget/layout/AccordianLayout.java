/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;


import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;

/**
 * This is a layout that contains multiple content panels in an expandable
 * accordion style such that only one panel can be open at any given time.
 * <p>
 * <b>Note:</b> All children added to the container must be
 * <code>ContentPanel</code> instances.
 * </p>
 */
public class AccordianLayout extends FitLayout {

  /**
   * True to swap the position of each panel as it is expanded so that it
   * becomes the first item in the container, false to keep the panels in the
   * rendered order. (defaults to false).
   */
  public boolean activeOnTop = false;

  /**
   * True to set each contained item's width to 'auto', false to use the item's
   * current width (defaults to true).
   */
  public boolean autoWidth = true;

  /**
   * True to adjust the active item's height to fill the available space in the
   * container, false to use the item's current height (defaults to true).
   */
  public boolean fill = true;

  /**
   * True to hide the contained panels' collapse/expand toggle buttons, false to
   * display them (defaults to false). When set to true, {@link #titleCollapse}
   * should be true also.
   */
  public boolean hideCollapseTool = false;

  /**
   * True to allow expand/collapse of each contained panel by clicking anywhere
   * on the title bar, false to allow expand/collapse only when the toggle tool
   * button is clicked (defaults to true). When set to false,
   * {@link #hideCollapseTool} should be false also.
   */
  public boolean titleCollapse = true;

  private Listener listener;

  public AccordianLayout() {
    renderAll = true;
    listener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        setActiveItem(ce.component);
      }
    };
  }

  public void setActiveItem(Component item) {
    if (container == null || !container.isRendered()) {
      activeItem = item;
      return;
    }
    if (activeItem != null) {
      ((ContentPanel) activeItem).collapse();
    }
    activeItem = item;
    markExpanded((ContentPanel) activeItem);
    layout(container);
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);
    updateStyles();
  }

  @Override
  protected void renderComponent(Component component, int index, El target) {
    ContentPanel cp = (ContentPanel) component;
    cp.collapsible = true;
    cp.animCollapse = false;
    cp.border = false;

    if (autoWidth) {
      cp.autoWidth = autoWidth;
    }
    if (titleCollapse) {
      cp.titleCollapse = true;
    }
    if (hideCollapseTool) {
      cp.hideCollapseTool = true;
    }

    if (activeItem == null || activeItem == component) {
      activeItem = component;
    } else {
      cp.collapsed = true;
    }

    super.renderComponent(component, index, target);
    El.fly(cp.getElement("header")).addStyleName("x-accordion-hd");
    cp.addListener(Events.BeforeExpand, listener);
    cp.addListener(Events.Collapse, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        updateStyles();
      }
    });
  }

  @Override
  protected void setItemSize(Component item, int width, int height) {
    if (fill && item != null) {
      int count = container.getItemCount();
      int hh = 0;
      for (int i = 0, len = count; i < len; i++) {
        ContentPanel cp = (ContentPanel) container.getItem(i);
        if (cp != item) {
          hh += (cp.getOffsetHeight() - El.fly(cp.getElement("bwrap")).getHeight());
          cp.el.setWidth(width);
        }
      }
      height -= hh;
      setSize(item, width, height);
    }
  }

  protected void updateStyles() {
    int count = container.getItemCount();
    boolean anyexpanded = false;
    for (int i = 0; i < count; i++) {
      ContentPanel panel = (ContentPanel) container.getItem(i);
      panel.getHeader().removeStyleName("x-border-bottom-none");
      if (i != (count - 1) && panel.isExpanded()) {
        anyexpanded = true;
      }
      panel.getHeader().el.setStyleName("x-border-top", isPriorExpanded(i));
      if (i == (count - 1) && (anyexpanded && !panel.isExpanded())) {
        panel.getHeader().addStyleName("x-border-bottom-none");
      }
    }
  }

  private boolean isPriorExpanded(int current) {
    if (--current >= 0) {
      ContentPanel panel = (ContentPanel) container.getItem(current);
      return panel.isExpanded();
    }
    return false;
  }

  private native void markExpanded(ContentPanel panel) /*-{
     panel.@com.extjs.gxt.ui.client.widget.GenericContentPanel::collapsed = false;
   }-*/;

}
