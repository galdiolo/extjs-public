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
 * <p/>This is a base class for layouts that contain a single item that
 * automatically expands to fill the layout's container.
 * 
 * <p/>FitLayout does not have any associated layout data. If the container has
 * multiple children, only the first one will be displayed.
 */
public class FitLayout extends Layout {

  public FitLayout() {
    monitorResize = true;
  }

  @Override
  protected void onLayout(Container container, El target) {
    if (container.getItemCount() == 0) {
      return;
    }
    super.onLayout(container, target);
    Component a = activeItem != null ? activeItem : container.getItem(0);
    setItemSize(a, target.getStyleSize());
  }

  protected void setItemSize(Component item, Size size) {
    if (item != null && item.isRendered() && size.height > 10) {
      size.width -= item.el().getMargins("lr");
      size.height -= item.el().getMargins("tb");
      setSize(item, size.width, size.height);
    } 
  }

}
