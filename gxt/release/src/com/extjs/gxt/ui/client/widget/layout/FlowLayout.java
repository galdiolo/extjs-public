/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

/**
 * <code>Layout</code> that positions its children using normal HTML layout
 * behavior. The margin and spacing can be specified. The layout method will be
 * called to any child widgets that are containers. Recalculate will be called
 * on any child widgets that are components.
 */
public class FlowLayout extends Layout {

  /**
   * The number of pixels of margin that will be placed along the edges of the
   * layout (default is 0).
   */
  public int margin = 0;

  /**
   * The number of pixels between the edge of one cell and the edge of its
   * neighbouring cell (default value is 0).
   */
  public int spacing = 0;

  /**
   * True to remove postioning from the container's children (defaults to true).
   */
  public boolean removePositioning = true;

  /**
   * Creates a new layout instance.
   */
  public FlowLayout() {

  }

  /**
   * Creates a new flow layout.
   * 
   * @param removePostioning true to remove positioning on child components
   */
  public FlowLayout(boolean removePostioning) {
    this.removePositioning = removePostioning;
  }

  /**
   * Creates a new layout instance with the given margin.
   * 
   * @param margin the margin
   */
  public FlowLayout(int margin) {
    this.margin = margin;
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);
    if (margin != 0) {
      target.setStyleAttribute("margin", margin);
    }
  }

  @Override
  protected void renderComponent(Component c, int index, El target) {
    super.renderComponent(c, index, target);
    if (removePositioning) {
      c.setStyleAttribute("position", "static");
    }

    if (index != 0 && spacing > 0) {
      c.el.setStyleAttribute("marginTop", spacing);
      c.el.setStyleAttribute("marginRight", spacing);
    }

    if (c instanceof Container) {
      ((Container) c).layout();
    } else {
      c.recalculate();
    }
  }

}
