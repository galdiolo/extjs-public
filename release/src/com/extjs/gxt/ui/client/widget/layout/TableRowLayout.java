/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A single row TableLayout.
 * 
 * @see TableData
 */
public class TableRowLayout extends TableLayout {

  public HorizontalAlignment align = HorizontalAlignment.LEFT;

  private Element row;

  protected Element getNextCell(Component component) {
    TableData data = (TableData) component.getData();
    if (data == null) {
      data = new TableData();
      component.setData(data);
    }

    Element td = DOM.createTD();

    if (horizontalAlign != null) {
      DOM.setElementProperty(td, "align", horizontalAlign.name());
    }
    if (data.padding > 0) {
      DOM.setIntStyleAttribute(td, "padding", data.padding);
    }
    if (data.style != null) {
      fly(td).setStyleName(data.style);
    }
    if (data.horizontalAlign != null) {
      DOM.setElementProperty(td, "align", data.horizontalAlign.name());
    }
    if (data.verticalAlign != null) {
      DOM.setStyleAttribute(td, "verticalAlign", data.verticalAlign.name());
    }
    if (data.height != null) {
      DOM.setElementProperty(td, "height", data.height);
    }
    if (data.width != null) {
      DOM.setElementProperty(td, "width", data.width);
    }
    DOM.appendChild(row, td);

    return td;
  }

  @Override
  protected void onLayout(Container container, El target) {
    currentColumn = 0;
    currentRow = 0;

    target.removeChildren();

    table = new El(DOM.createTable());

    if (cellPadding != -1) {
      table.setElementAttribute("cellPadding", cellPadding);
    }
    if (cellSpacing != -1) {
      table.setElementAttribute("cellSpacing", cellSpacing);
    }

    if (align == HorizontalAlignment.RIGHT) {
      table.addStyleName("x-float-right");
    }

    if (border > 0) {
      table.setElementAttribute("border", border);
    }

    if (width != null) {
      table.setWidth(width);
    }

    if (height != null) {
      table.setHeight(height);
    }

    tbody = DOM.createTBody();
    table.appendChild(tbody);

    row = DOM.createTR();
    DOM.appendChild(tbody, row);

    if (insertSpacer) {
      Element td = DOM.createTD();
      fly(td).setWidth("100%");
      DOM.appendChild(row, td);
    }

    target.appendChild(table.dom);
    renderAll(container, target);
  }

  @Override
  protected void renderComponent(Component c, int index, El target) {
    if (!c.isRendered()) {
      c.render(getNextCell(c));
    } else {
      DOM.appendChild(getNextCell(c), c.getElement());
    }
  }
}
