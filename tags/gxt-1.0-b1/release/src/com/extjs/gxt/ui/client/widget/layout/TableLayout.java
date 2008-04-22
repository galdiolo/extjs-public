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
import com.extjs.gxt.ui.client.widget.Layout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * <code>TableLayout</code> allows you to easily render content into an HTML
 * table. The total number of columns can be specified.
 * <p>
 * Rather than explicitly creating and nesting rows and columns as you would in
 * HTML, you simply specify the total column count and start adding widgets in
 * their natural order from left to right, top to bottom. The layout will
 * automatically figure out, based on the column count how to position each
 * panel within the table.
 * </p>
 */
public class TableLayout extends Layout {

  /**
   * True to insert a spacer cell into each row with 100% width so that all
   * other cells are right aligned (defaults to false).
   */
  public boolean insertSpacer;

  /**
   * Specifies the cell's horizontal alignment (defaults to LEFT).
   * <p>
   * Valid values are:
   * <ul>
   * <li>HorizontalAlignment.LEFT</li>
   * <li>HorizontalAlignment.CENTER</li>
   * <li>HorizontalAlignment.RIGHT</li>
   * </ul>
   * </p>
   */
  public HorizontalAlignment horizontalAlign;

  /**
   * Specifies the cell's vertical alignment (defaults to TOP).
   * <p>
   * Valid values are:
   * <ul>
   * <li>VerticalAlignment.TOP</li>
   * <li>VerticalAlignment.MIDDLE</li>
   * <li>VertcialAlginment.BOTTOM</li>
   * </ul>
   * </p>
   */
  public VerticalAlignment verticalAlign = VerticalAlignment.TOP;

  /**
   * The number of columns (defaults to 1).
   */
  public int columns = 1;

  /**
   * The table's cellpadding property (defaults to -1).
   */
  public int cellPadding = -1;

  /**
   * Sets the table's cellspacing property (defaults to -1).
   */
  public int cellSpacing = -1;

  /**
   * The table's border property (defaults to 0).
   */
  public int border = 0;

  public String width;

  public String height;

  protected El table;
  protected Element tbody;
  protected int currentColumn;
  protected int currentRow;

  /**
   * Creates a new table layout.
   */
  public TableLayout() {

  }

  /**
   * Creates a new table layout.
   * 
   * @param columns the number of columns
   */
  public TableLayout(int columns) {
    this.columns = columns;
  }

  /**
   * Sets the table's cell padding.
   * 
   * @param padding the cell padding
   */
  public void setCellPadding(int padding) {
    this.cellPadding = padding;
    if (table != null) {
      table.setIntElementProperty("cellPadding", padding);
    }
  }

  /**
   * Sets the table's cell spacing.
   * 
   * @param spacing the cell spacing
   */
  public void setCellSpacing(int spacing) {
    this.cellSpacing = spacing;
    if (table != null) {
      table.setIntElementProperty("cellSpacing", spacing);
    }
  }
  
  protected Element getNextCell(Component widget) {
    TableData data = (TableData) widget.getData();
    if (data == null) {
      data = new TableData();
      widget.setData(data);
    }

    El td = new El(DOM.createTD());
    El row;
    
    if (currentColumn != 0 && (currentColumn % columns == 0)) {
      row = getRow(++currentRow);
      currentColumn += data.colspan != -1 ? data.colspan : 1;
    } else {
      row = getRow(currentRow);
      currentColumn += data.colspan != -1 ? data.colspan : 1;
    }
    if (data.colspan != 1) {
      td.setIntElementProperty("colSpan", data.colspan);
    }

    if (data.padding > 0) {
      td.setIntElementProperty("padding", data.padding);
    }
    if (data.style != null) {
      td.addStyleName(data.style);
    }
    if (data.horizontalAlign != null) {
      td.setElementAttribute("align", data.horizontalAlign.name().toLowerCase());
    }
    if (data.verticalAlign != null) {
      td.setElementAttribute("verticalAlign", data.verticalAlign.name().toLowerCase());
    }
    if (data.height != null) {
      td.setElementAttribute("height", data.height);
    }
    if (data.width != null) {
      td.setElementAttribute("width", data.width);
    }
    DOM.appendChild(row.dom, td.dom);
    return td.dom;
  }

  protected El getRow(int index) {
    Element row = DOM.getChild(tbody, index);
    if (row == null) {
      row = DOM.createTR();
      DOM.appendChild(tbody, row);
    }
    return new El(row);
  }

  @Override
  protected boolean isValidParent(Element elem, Element parent) {
    return false;
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

    if (border > 0) {
      table.setElementAttribute("border", border);
    }

    if (width != null) {
      table.setElementAttribute("width", width);
    }

    if (height != null) {
      table.setElementAttribute("height", height);
    }

    tbody = DOM.createTBody();
    
    table.appendChild(tbody);
    target.appendChild(table.dom);
    renderAll(container, target);
  }

  @Override
  protected void renderComponent(Component c, int index, El target) {
    Element td = getNextCell(c);
    if (c.isRendered()) {
      DOM.appendChild(td, c.getElement());
    } else {
      c.render(td);
    }

  }

}
