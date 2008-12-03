/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.google.gwt.user.client.Element;

/**
 * <code>CenterLayout</code> centers a single widget within its container.
 */
public class CenterLayout extends TableLayout {

  private TableData data;

  /**
   * Creates a new center layout.
   */
  public CenterLayout() {
    setWidth("100%");
    setHeight("100%");

    data = new TableData();
    data.setHorizontalAlign(HorizontalAlignment.CENTER);
    data.setVerticalAlign(VerticalAlignment.MIDDLE);
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);
  }

  @Override
  protected void renderAll(Container container, El target) {
    Component c = container.getItem(0);
    if (c != null && !isValidParent(c.getElement(), target.dom)) {
      c.setStyleAttribute("textAlign", "left");
      renderComponent(c, 0, target);
    }
  }

  protected Element getNextCell(Component c) {
    setLayoutData(c, data);
    return super.getNextCell(c);
  }

}
