/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

/**
 * This layout contains multiple widgets, each fit to the container, where only
 * a single widget can be visible at any given time. This layout style is most
 * commonly used for wizards, tab implementations, etc.
 * <p>
 * The CardLayout's focal method is setActiveItem. Since only one panel is
 * displayed at a time, the only way to move from one panel to the next is by
 * calling setActiveItem the next panel to display. The layout itself does not
 * provide a mechanism for handling this navigation, so that functionality must
 * be provided by the developer.
 * </p>
 */
public class CardLayout extends Layout {

  /**
   * Returns the active component.
   * 
   * @return the top widget
   */
  public Component getActiveItem() {
    return activeItem;
  }

  /**
   * Sets the active (visible) item in the layout.
   * 
   * @param component the active widget
   */
  public void setActiveItem(Component component) {
    if (activeItem != component) {
      if (activeItem != null) {
        activeItem.setVisible(false);
      }
      activeItem = component;
      if (activeItem != null) {
        activeItem.setVisible(true);
      }
    }
  }

  @Override
  protected void renderAll(Container container, El target) {
    if (activeItem != null) {
      renderComponent(activeItem, 0, target);
    }
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);

    Size s = target.getStyleSize();

    if (activeItem == null && container.getItemCount() > 0) {
      setActiveItem(container.getItem(0));
    }

    int size = container.getItemCount();
    for (int i = 0; i < size; i++) {
      Component child = container.getItem(i);
      child.setVisible(activeItem == child);
      if (activeItem == child) {
        setSize(child, s.width, s.height);
      }
    }
  }

}
