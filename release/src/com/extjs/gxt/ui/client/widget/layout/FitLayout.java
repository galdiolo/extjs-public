/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

/**
 * <p/>This is a base class for layouts that contain a single item that
 * automatically expands to fill the layout's container.
 * 
 * <p/>FitLayout does not have any associated layout data. If the container has
 * multiple children, only the first one will be displayed.
 */
public class FitLayout extends Layout {

  /**
   * True to render all children (defaults to false).
   */
  protected boolean renderAll;

  @Override
  protected void onLayout(Container container, El target) {
    Rectangle rect = target.getLayoutBounds();
    Component a = activeItem != null ? activeItem : container.getItem(0);
    if (a == null) return;

    if (renderAll) {
      super.onLayout(container, target);
      setItemSize(a, rect.width, rect.height);
    } else {
      setItemSize(a, rect.width, rect.height);
      if (!a.isRendered()) {
        renderComponent(a, 0, target);
      }
    }
  }

  protected void setItemSize(Component item, int width, int height) {
    item.setStyleAttribute("position", "static");
    setSize(item, width, height);
  }

}
