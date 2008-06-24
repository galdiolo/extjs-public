/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import java.util.Collections;
import java.util.Comparator;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class encapsulates the user interface of a {@link Table}.
 */
public class TableView {

  private static String bodyHTML;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div style='overflow: hidden;'>");
    sb.append("<div style='overflow: scroll;'>");
    sb.append("<div class='my-tbl-data'></div>");
    sb.append("</div></div>");
    bodyHTML = sb.toString();
  }

  // styles
  protected String baseStyle = "my-tbl-item";
  protected String overStyle = baseStyle + "-over";
  protected String selStyle = baseStyle + "-sel";
  protected String cellStyle = baseStyle + "-" + "cell";
  protected String cellOverflowStyle = cellStyle + "-" + "overflow";
  protected String textStyle = cellStyle + "-text";
  protected String widgetStyle = cellStyle + "-widget";

  protected TableColumnModel cm;
  protected El dataEl, scrollEl;
  protected Table table;
  protected int scrollBarWidth;

  public El getDataEl() {
    return dataEl;
  }

  /**
   * Sorts the table items based on the current order.
   */
  public void reorderItems() {
    dataEl.removeChildren();
    int numRows = table.getItemCount();
    for (int i = 0; i < numRows; i++) {
      TableItem item = table.getItem(i);
      dataEl.dom.appendChild(item.getElement());
    }
    table.getSelectionModel().refresh();
  }

  public void applyCellStyles(TableItem item) {
    if (item.cellStyles != null) {
      for (int i = 0; i < item.cellStyles.length; i++) {
        setCellStyle(item, i, item.cellStyles[i]);
      }
    }
  }

  public void clearHoverStyles() {
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = table.getItem(i);
      item.onMouseOut(null);
    }
  }

  public void onSelectItem(TableItem item, boolean select) {
    if (select) {
      item.addStyleName(selStyle);
    } else {
      item.removeStyleName(selStyle);
      item.removeStyleName(overStyle);
    }
  }

  public void doSort(int index, final SortDir direction) {
    TableColumn column = table.getColumn(index);
    final Comparator comparator = direction.comparator(column.getComparator());
    final int col = index;
    Collections.sort(table.getItems(), new Comparator() {
      public int compare(Object arg0, Object arg1) {
        TableItem item1 = (TableItem) arg0;
        TableItem item2 = (TableItem) arg1;
        Object o1 = item1.getValue(col);
        Object o2 = item2.getValue(col);
        return comparator.compare(o1, o2);
      }
    });

    reorderItems();
  }

  public int getCellIndex(Element target) {
    String index = target.getAttribute("index");
    if (index == null) {
      target = DOM.getParent(target);
      while (target != null) {
        index = target.getAttribute("index");
        if (index == null) {
          target = DOM.getParent(target);
        } else {
          break;
        }
      }
    }
    return index == null ? Style.DEFAULT : Integer.parseInt(index);
  }

  public El getScrollEl() {
    return scrollEl;
  }

  public Element getTextCellElement(TableItem item, int cell) {
    return getTextCellInternal(item.getElement(), cell);
  }

  public native Element getTextCellInternal(Element elem, int column) /*-{
   return elem.firstChild.firstChild.firstChild.childNodes[column].firstChild.firstChild;
   }-*/;

  public void init(final Table table) {
    this.table = table;
    this.cm = table.getColumnModel();

    Listener l = new Listener<TableEvent>() {

      public void handleEvent(TableEvent be) {
        switch (be.type) {
          case Events.HeaderChange: {
            TableColumn c = cm.getColumn(be.columnIndex);
            table.getTableHeader().getColumnUI(be.columnIndex).onTextChange(c.getText());
            break;
          }
          case Events.WidthChange:
            table.getTableHeader().resizeColumn(be.columnIndex, true);
            break;
          case Events.HiddenChange: {
            TableColumn c = cm.getColumn(be.columnIndex);
            table.getTableHeader().showColumn(be.columnIndex, !c.isHidden());
            break;
          }
        }
      }

    };

    cm.addListener(Events.HeaderChange, l);
    cm.addListener(Events.WidthChange, l);

  }

  public void onHighlightRow(TableItem item, boolean highlight) {
    if (highlight) {
      item.addStyleName(overStyle);
    } else {
      item.removeStyleName(overStyle);
    }
  }

  public void removeItem(TableItem item) {
    if (item.isRendered()) {
      item.el().removeFromParent();
    }
  }

  public void render() {
    scrollBarWidth = XDOM.getScrollBarWidth();

    Element div = DOM.createDiv();

    div.setInnerHTML(bodyHTML.toString());
    scrollEl = new El(El.fly(div).getSubChild(2));
    dataEl = scrollEl.firstChild();
    DOM.appendChild(table.getElement(), DOM.getFirstChild(div));

    if (table.getVerticalLines()) {
      table.addStyleName("my-tbl-vlines");
    }

    if (!GXT.isIE) {
      DOM.setElementPropertyInt(table.getElement(), "tabIndex", 0);
    }

    DOM.sinkEvents(scrollEl.dom, Event.ONSCROLL);

    table.disableTextSelection(true);

    table.el().addEventsSunk(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
  }

  public void bulkRender() {
    int count = table.getItemCount();
    int cols = cm.getColumnCount();

    TableColumn[] columns = new TableColumn[cols];
    int[] widths = new int[cols];
    String[] align = new String[cols];
    for (int i = 0; i < columns.length; i++) {
      columns[i] = cm.getColumn(i);
      widths[i] = cm.getWidthInPixels(i) - (table.getVerticalLines() && !XDOM.isVisibleBox ? 1 : 0);
      columns[i].lastWidth = widths[i];
      HorizontalAlignment ha = columns[i].getAlignment();
      switch (ha) {
        case LEFT:
          align[i] = "left";
          break;
        case CENTER:
          align[i] = "center";
          break;
        case RIGHT:
          align[i] = "right";
          break;
      }
    }

    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < count; i++) {
      TableItem item = table.getItem(i);
      item.init(table);
      markRendered(item);
      Object[] values = item.getValues();
      Object[] styles = item.getCellStyles();
      Object[] svalues = new Object[cols];
      for (int k = 0; k < cols; k++) {
        svalues[k] = table.getRenderedValue(item, k, values[k]);
      }

      sb.append("<div class=my-tbl-item><table cellpadding=0 cellspacing=0 tabIndex=1><tr>");
      for (int j = 0; j < cols; j++) {
        String display = columns[j].isHidden() ? "none" : "static";
        sb.append("<td class='" + cellStyle + "my-tbl-td-" + j + "' style='display: " + display
            + ";width: " + widths[j] + "px' index=" + j + "><div class='" + cellOverflowStyle
            + " my-tbl-td-inner-" + j + "' style='width:" + widths[j]
            + "'><div class='my-tbl-td-cell-" + j + " " + textStyle
            + (styles == null ? "" : " " + styles[j]) + "' style='text-align:" + align[j] + "'>"
            + svalues[j] + "</div></div></td>");

      }
      sb.append("</tr></table></div>");

    }

    dataEl.dom.setInnerHTML(sb.toString());

    Element[] elems = dataEl.select(".my-tbl-item");
    int ct = table.getItemCount();
    for (int i = 0; i < ct; i++) {
      TableItem item = table.getItem(i);
      item.setElement(elems[i]);
      applyCellStyles(item);
    }

  }

  public void renderItem(TableItem item, int index) {
    item.setStyleName(baseStyle);
    item.init(table);

    int cols = cm.getColumnCount();
    Object[] values = item.getValues();
    Object[] styles = item.getCellStyles();
    Object[] svalues = new Object[cols];
    for (int i = 0; i < cols; i++) {
      if (!item.hasWidgets && values[i] instanceof Widget) {
        item.hasWidgets = true;
        if (table.bulkRender) {
          throw new RuntimeException(
              "Bulk rendering must be disabled when adding widgets to table items");
        }
      }
      svalues[i] = table.getRenderedValue(item, i, values[i]);
    }

    StringBuffer sb = new StringBuffer();
    sb.append("<table cellpadding=0 cellspacing=0 tabIndex=1><tr>");
    for (int i = 0; i < cols; i++) {
      TableColumn c = cm.getColumn(i);
      String display = c.isHidden() ? "none" : "static";
      int w = table.getColumnModel().getWidthInPixels(c.index);
      if (!XDOM.isVisibleBox) {
        w -= table.getVerticalLines() ? 1 : 0;
      }
      HorizontalAlignment align = c.getAlignment();
      String salign = "left";
      if (align == HorizontalAlignment.CENTER) {
        salign = "center";
      } else if (align == HorizontalAlignment.RIGHT) {
        salign = "right";
      }
      sb.append("<td class=" + cellStyle + " style='display: " + display + ";width: " + w
          + "px' index=" + i + "><div class=" + cellOverflowStyle + " style='width:" + w
          + "'><div class='" + textStyle + (styles == null ? "" : " " + styles[i])
          + "' style='text-align:" + salign + "'>" + svalues[i] + "</div></div></td>");
    }
    sb.append("</tr></table>");

    item.render(dataEl.dom, index);
    item.getElement().setInnerHTML(sb.toString());

    if (item.hasWidgets) {
      for (int i = 0; i < cols; i++) {
        if (values[i] instanceof Widget) {
          Widget w = (Widget) values[i];
          Element text = getTextCellElement(item, i);
          El textEl = El.fly(text);
          textEl.dom.setInnerHTML("");
          textEl.dom.setClassName(widgetStyle);
          textEl.dom.appendChild(w.getElement());
          if (table.isAttached()) {
            WidgetHelper.doAttach(w);
          }
        }
      }
    }
    applyCellStyles(item);

    item.cellsRendered = true;
  }

  public void renderItems() {
    if (table.getBulkRender()) {
      bulkRender();
    } else {
      int count = table.getItemCount();
      for (int i = 0; i < count; i++) {
        TableItem item = table.getItem(i);
        renderItem(item, i);
      }
    }
  }

  public void renderItemValue(TableItem item, int index, Object value) {
    Element textElem = getTextCellElement(item, index);
    if (textElem != null) {
      Element child = DOM.getChild(textElem, 0);
      if (child != null) {
        DOM.removeChild(textElem, DOM.getChild(textElem, 0));
      }
      DOM.setInnerHTML(textElem, "");
      if (value instanceof Widget) {
        Widget widget = (Widget) value;
        XDOM.setStyleName(textElem, widgetStyle);
        DOM.appendChild(textElem, widget.getElement());
        if (table.isAttached()) {
          WidgetHelper.doAttach(widget);
        }
      } else {
        String s = table.getRenderedValue(item, index, value);
        textElem.setInnerHTML(s);
      }
    }
    applyCellStyles(item);
  }

  public void resize() {
    int width = table.getOffsetWidth();
    int headerHeight = table.getTableHeader().getOffsetHeight();
    int bodyHeight = table.getOffsetHeight() - headerHeight;
    int bodyWidth = width;
    
    if (table.isAutoHeight()) {
      scrollEl.setHeight("auto");
      dataEl.setHeight("auto");
      bodyHeight = dataEl.getHeight();
      bodyHeight += table.el().getBorderWidth("tb");
    }

    int columnModelWidth = cm.getTotalWidth();
    dataEl.setWidth(Math.max(width, columnModelWidth));
    table.getTableHeader().setWidth(columnModelWidth);

    bodyHeight -= table.el().getBorderWidth("tb");
    bodyWidth -= table.el().getBorderWidth("lr");

    if (dataEl.getHeight() < bodyHeight) {
      scrollEl.setStyleAttribute("overflowY", "hidden");
    } else {
      scrollEl.setStyleAttribute("overflowY", "auto");
    }

    if (table.getHorizontalScroll()) {
      scrollEl.setStyleAttribute("overflowX", "auto");
      if (columnModelWidth < width) {
        scrollEl.setStyleAttribute("overflowX", "hidden");
        table.getTableHeader().el().setLeft(0);
        scrollEl.setScrollLeft(0);
      }
    }

    if (table.isAutoHeight()) {
      bodyHeight = -1;
    }
    scrollEl.setSize(bodyWidth, bodyHeight);
  }

  public void resizeCells(int columnIndex) {
    TableColumn c = cm.getColumn(columnIndex);
    int w = cm.getWidthInPixels(c.index);
    if (!XDOM.isVisibleBox) {
      w -= table.getVerticalLines() ? 1 : 0;
    }
    if (c.lastWidth != 0 && c.lastWidth == w) {
      return;
    }
    c.lastWidth = w;

    int rows = table.getItemCount();
    for (int j = 0; j < rows; j++) {
      TableItem item = table.getItem(j);
      sizeCell(item.getElement(), columnIndex, w);
      if (j == 0) {
        showColumn(item.getElement(), !c.isHidden(), columnIndex);
      }
    }
  }

  public void setCellStyle(TableItem item, int index, String style) {
    if (item.cellsRendered) {
      Element cell = getTextCellElement(item, index);
      XDOM.setStyleName(cell, textStyle + " " + style);
    }
  }

  public native void showColumn(Element elem, boolean show, int index) /*-{
   var tbl = elem.firstChild;
   var cell = tbl.firstChild.firstChild.childNodes[index]
   cell.style.display = show ? '' : 'none';
   }-*/;

  public void showColumn(int index, boolean show) {
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      showColumn(table.getItem(i).getElement(), show, index);
    }
  }

  public native void sizeCell(Element elem, int index, int width) /*-{
   var tbl = elem.firstChild;
   var cell = tbl.firstChild.firstChild.childNodes[index];
   cell.style.width = width;
   cell.firstChild.style.width = width;
   }-*/;

  public void sort(int index, SortDir direction) {
    doSort(index, direction);
  }

  public static native void markRendered(TableItem item) /*-{
   item.@com.extjs.gxt.ui.client.widget.Component::rendered = true;
   }-*/;

}
