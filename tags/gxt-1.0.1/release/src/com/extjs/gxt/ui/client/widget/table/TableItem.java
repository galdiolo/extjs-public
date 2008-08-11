/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * A row in a <code>Table</code>.
 */
public class TableItem extends Component {

  String[] toolTips;
  ToolTip cellToolTip;
  boolean hasWidgets;
  String[] cellStyles;
  boolean cellsRendered;
  private Table table;
  private Object[] values;

  /**
   * Creates a new table item.
   * 
   * @param values the cell values
   */
  public TableItem(Object[] values) {
    this.values = values;
  }

  /**
   * Returns the item's cell tooltip.
   * 
   * @return the tooltip
   */
  public ToolTip getCellToolTip() {
    return cellToolTip;
  }

  /**
   * Returns the item's parent table.
   * 
   * @return the table
   */
  public Table getTable() {
    return table;
  }

  /**
   * Returns a cell value.
   * 
   * @param index the cell index
   * @return the value
   */
  public Object getValue(int index) {
    return values[index];
  }

  /**
   * Returns the item's values.
   * 
   * @return the values
   */
  public Object[] getValues() {
    return values;
  }

  public void onComponentEvent(ComponentEvent ce) {
    // Pass along the event to the related cell tooltip if it exists
    // so that we don't consume the event before the tooltip processes it
    if (cellToolTip != null) {
      // cellToolTip.handleEvent(be);
    }
    switch (ce.type) {
      case Event.ONDBLCLICK:
        onDoubleClick(ce);
        break;
      case Event.ONCLICK:
        onClick(ce);
        break;
      case Event.ONMOUSEOVER:
        onMouseOver(ce);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(ce);
        break;
    }
  }

  /**
   * Sets the style for a cell.
   * 
   * @param index the column index
   * @param style the new style
   */
  public void setCellStyle(int index, String style) {
    if (cellStyles == null) cellStyles = new String[values.length];
    cellStyles[index] = style;
    if (isRendered()) {
      table.getView().setCellStyle(this, index, style);
    }
  }

  public String[] getCellStyles() {
    return cellStyles;
  }

  /**
   * Sets a cell tooltip.
   * 
   * @param index the column index
   * @param text the text of the tool tip
   */
  public void setCellToolTip(int index, String text) {
    if (toolTips == null) toolTips = new String[values.length];
    toolTips[index] = text;
    initCellToolTips();
  }

  /**
   * Sets all of the cell tooltips
   * 
   * @param toolTips the tool tips to use
   */
  public void setCellToolTips(String[] toolTips) {
    this.toolTips = toolTips;
    initCellToolTips();
  }

  /**
   * Sets a cell value.
   * 
   * @param index the column index
   * @param text the text
   */
  public void setText(int index, String text) {
    setValue(index, text);
  }

  /**
   * Sets a cell value.
   * 
   * @param index the column index
   * @param value the value
   */
  public void setValue(int index, Object value) {
    values[index] = value;
    if (rendered) {
      table.getView().renderItemValue(this, index, value);
    }
  }

  /**
   * Sets a widget as a cell value. When using widgets in cells, bulk rendering
   * must be disabled.
   * 
   * @param index the column index
   * @param widget the widget
   */
  public void setWidget(int index, Widget widget) {
    hasWidgets = true;
    setValue(index, widget);
  }

  protected void init(Table table) {
    this.table = table;
  }

  protected void initCellToolTips() {
    if (cellToolTip == null && isRendered()) {
      cellToolTip = new ToolTip(this);
      cellToolTip.setTrackMouse(true);
    }
  }

  protected void onCellMouseOver(ComponentEvent ce) {
    Element target = ce.getTarget();

    int index = table.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

    if (cellToolTip != null) {
      if (toolTips != null && toolTips[index] != null && toolTips[index].length() > 0) {
//         cellToolTip.setText(null, toolTips[index]);
        // cellToolTip.setVisible(true);
        // } else {
        // cellToolTip.setVisible(false);
      }
    }
  }

  protected void onClick(ComponentEvent ce) {
    Element target = ce.getTarget();

    int index = table.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

    TableEvent te = new TableEvent(table);
    te.item = this;
    te.rowIndex = table.indexOf(this);
    te.cellIndex = index;
    te.event = ce.event;

    table.fireEvent(Events.CellClick, te);
    table.fireEvent(Events.RowClick, te);
  }

  protected void onDoubleClick(ComponentEvent ce) {
    Element target = ce.getTarget();

    int index = table.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }
    TableEvent evt = new TableEvent(table);
    evt.item = this;
    evt.rowIndex = table.indexOf(this);
    evt.cellIndex = index;

    table.fireEvent(Events.CellDoubleClick, evt);
    table.fireEvent(Events.RowDoubleClick, evt);
  }

  protected void onMouseOut(BaseEvent be) {
    table.getView().onHighlightRow(this, false);
  }

  protected void onMouseOver(ComponentEvent ce) {
    table.getView().onHighlightRow(this, true);
    onCellMouseOver(ce);
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv());
    el().insertInto(target, index);
  }

  protected void onSelectChange(boolean select) {
    table.getView().onSelectItem(this, select);
  }

}
