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
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;

/**
 * This is a layout that contains multiple content panels in an expandable
 * accordion style such that only one panel can be open at any given time.
 * 
 * <p />
 * Child Widgets are:
 * <ul>
 * <li><b>Sized</b> : Yes - default expands to fill parent container.</li>
 * <li><b>Positioned</b> : No - widgets are located at 0,0.</li>
 * </ul>
 * <p>
 * <b>Note:</b> All children added to the container must be
 * <code>ContentPanel</code> instances.
 * </p>
 */
public class AccordionLayout extends FitLayout {

  private boolean titleCollapse = true;
  private boolean fill = true;
  private boolean hideCollapseTool = false;
  private boolean autoWidth;
  private boolean activeOnTop = false;
  private Listener listener;

  public AccordionLayout() {
    listener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        onBeforeExpand(ce);
      }
    };
  }

  /**
   * Returns true if the active item if first.
   * 
   * @return the active on top state
   */
  public boolean getActiveOnTop() {
    return activeOnTop;
  }

  /**
   * Returns true if auto width is enabled.
   * 
   * @return the auto width state
   */
  public boolean getAutoWidth() {
    return autoWidth;
  }

  /**
   * Returns true if fill is enabled.
   * 
   * @return the fill state
   */
  public boolean getFill() {
    return fill;
  }

  /**
   * Returns true if the collapse tool is hidden.
   * 
   * @return the hide collapse tool state
   */
  public boolean getHideCollapseTool() {
    return hideCollapseTool;
  }

  /**
   * Returns true if title collapse is enabled.
   * 
   * @return the title collapse state
   */
  public boolean getTitleCollapse() {
    return titleCollapse;
  }

  /**
   * Sets the active item.
   * 
   * @param item the active item
   */
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

    if (activeOnTop) {
      target.insertChild(item.getElement(), 0);
    }

    layout();
  }

  /**
   * True to swap the position of each panel as it is expanded so that it
   * becomes the first item in the container, false to keep the panels in the
   * rendered order. (defaults to false).
   * 
   * @param activeOnTop true to keep the active item on top
   */
  public void setActiveOnTop(boolean activeOnTop) {
    this.activeOnTop = activeOnTop;
  }

  /**
   * True to set each contained item's width to 'auto', false to use the item's
   * current width (defaults to false).
   * 
   * @param autoWidth true for auto width
   */
  public void setAutoWidth(boolean autoWidth) {
    this.autoWidth = autoWidth;
  }

  /**
   * True to adjust the active item's height to fill the available space in the
   * container, false to use the item's current height (defaults to true).
   * 
   * @param fill true to fill
   */
  public void setFill(boolean fill) {
    this.fill = fill;
  }

  /**
   * True to hide the contained panels' collapse/expand toggle buttons, false to
   * display them (defaults to false). When set to true, {@link #titleCollapse}
   * should be true also.
   * 
   * @param hideCollapseTool true to hide
   */
  public void setHideCollapseTool(boolean hideCollapseTool) {
    this.hideCollapseTool = hideCollapseTool;
  }

  /**
   * True to allow expand/collapse of each contained panel by clicking anywhere
   * on the title bar, false to allow expand/collapse only when the toggle tool
   * button is clicked (defaults to true). When set to false,
   * {@link #hideCollapseTool} should be false also.
   * 
   * @param titleCollapse true for title collapse
   */
  public void setTitleCollapse(boolean titleCollapse) {
    this.titleCollapse = titleCollapse;
  }
  
  protected void onBeforeExpand(ComponentEvent ce) {
    setActiveItem(ce.component);
  }

  @Override
  protected void renderComponent(Component component, int index, El target) {
    ContentPanel cp = (ContentPanel) component;

    if (!cp.isRendered()) {
      cp.setCollapsible(true);
      cp.setAnimCollapse(false);
      if (titleCollapse) {
        cp.setTitleCollapse(true);
      }
      if (hideCollapseTool) {
        cp.setHideCollapseTool(true);
      }
    }

    if (autoWidth) {
      cp.setAutoWidth(autoWidth);
    }

    super.renderComponent(component, index, target);

    if (activeItem == null || activeItem == component) {
      activeItem = component;
    } else {
      cp.collapse();
    }

    El.fly(cp.getElement("header")).addStyleName("x-accordion-hd");
    cp.addListener(Events.BeforeExpand, listener);
  }

  @Override
  protected void setItemSize(Component item, Size size) {
    if (fill && item != null) {
      int count = container.getItemCount();
      int hh = 0;
      for (int i = 0, len = count; i < len; i++) {
        ContentPanel cp = (ContentPanel) container.getItem(i);
        if (cp != item) {
          hh += (cp.getHeight() - El.fly(cp.getElement("bwrap")).getHeight());
          cp.el().setWidth(size.width);
        }
      }
      size.height -= hh;
      ContentPanel cp = (ContentPanel) item;
      if (cp.isExpanded()) {
        setSize(item, size.width, size.height);
      } else {
        item.el().setWidth(size.width, true);
      }
    }
  }

  private native void markExpanded(ContentPanel panel) /*-{
     panel.@com.extjs.gxt.ui.client.widget.ContentPanel::collapsed = false;
   }-*/;

}
