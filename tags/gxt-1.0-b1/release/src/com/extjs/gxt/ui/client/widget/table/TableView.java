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

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class encapsulates the user interface of a {@link Table}.
 */
public class TableView<T extends TableItem> {

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

  /**
   * Sorts the table items based on the current order.
   */
  public void reorderItems() {
    dataEl.removeChildren();
    int numRows = table.getItemCount();
    for (int i = 0; i < numRows; i++) {
      TableItem item = table.getItem(i);
      dataEl.appendChild(item.getElement());
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
    String index = El.fly(target).getElementAttribute("index");
    if (index == null) {
      target = DOM.getParent(target);
      while (target != null) {
        index = El.fly(target).getElementAttribute("index");
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

  public void init(Table table) {
    this.table = table;
    this.cm = table.getColumnModel();
  }

  public void onHighlightRow(TableItem item, boolean highlight) {
    if (highlight) {
      item.addStyleName(overStyle);
    } else {
      item.removeStyleName(overStyle);
    }
  }

  public void removeItem(TableItem item) {
    dataEl.removeChild(item.getElement());
  }

  public void render() {
    scrollBarWidth = XDOM.getScrollBarWidth();

    Element div = DOM.createDiv();

    El.fly(div).setInnerHtml(bodyHTML.toString());
    scrollEl = new El(El.fly(div).getSubChild(2));
    dataEl = scrollEl.firstChild();
    DOM.appendChild(table.getElement(), DOM.getFirstChild(div));

    if (table.verticalLines) {
      table.addStyleName("my-tbl-vlines");
    }

    if (!GXT.isIE) {
      DOM.setElementPropertyInt(table.getElement(), "tabIndex", 0);
    }

    DOM.sinkEvents(scrollEl.dom, Event.ONSCROLL);

    table.disableTextSelection(true);

    table.el.addEventsSunk(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS
        | Event.KEYEVENTS);
  }

  public void bulkRender() {
    int count = table.getItemCount();
    int cols = cm.getColumnCount();

    TableColumn[] columns = new TableColumn[cols];
    int[] widths = new int[cols];
    String[] align = new String[cols];
    for (int i = 0; i < columns.length; i++) {
      columns[i] = cm.getColumn(i);
      widths[i] = cm.getWidthInPixels(i);
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
      Object[] svalues = new Object[cols];
      for (int k = 0; k < cols; k++) {
        svalues[k] = table.getRenderedValue(k, values[k]);
      }

      sb.append("<div class=my-tbl-item><table cellpadding=0 cellspacing=0 tabIndex=1><tr>");
      for (int j = 0; j < cols; j++) {
        String display = columns[j].isHidden() ? "none" : "static";
        sb.append("<td class=" + cellStyle + " style='display: " + display + ";width: "
            + widths[j] + "px' index=" + j + "><div class=" + cellOverflowStyle
            + " style='width:" + widths[j] + "'><div class=" + textStyle
            + " style='text-align:" + align[j] + "'>" + svalues[j] + "</div></div></td>");

      }
      sb.append("</tr></table></div>");

    }

    dataEl.setInnerHtml(sb.toString());

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
    Object[] svalues = new Object[cols];
    for (int i = 0; i < cols; i++) {
      if (!item.hasWidgets && values[i] instanceof Widget) {
        item.hasWidgets = true;
      }
      svalues[i] = table.getRenderedValue(i, values[i]);
    }

    StringBuffer sb = new StringBuffer();
    sb.append("<table cellpadding=0 cellspacing=0 tabIndex=1><tr>");
    for (int i = 0; i < cols; i++) {
      TableColumn c = cm.getColumn(i);
      String display = c.isHidden() ? "none" : "static";
      int w = table.getColumnModel().getWidthInPixels(c.index);
      HorizontalAlignment align = c.getAlignment();
      String salign = "left";
      if (align == HorizontalAlignment.CENTER) {
        salign = "center";
      } else if (align == HorizontalAlignment.RIGHT) {
        salign = "right";
      }
      sb.append("<td class=" + cellStyle + " style='display: " + display + ";width: " + w
          + "px' index=" + i + "><div class=" + cellOverflowStyle + " style='width:" + w
          + "'><div class=" + textStyle + " style='text-align:" + salign + "'>"
          + svalues[i] + "</div></div></td>");
    }
    sb.append("</tr></table>");

    item.render(dataEl.dom, index);
    item.el.setInnerHtml(sb.toString());

    if (item.hasWidgets) {
      for (int i = 0; i < cols; i++) {
        if (values[i] instanceof Widget) {
          Widget w = (Widget) values[i];
          Element text = getTextCellElement(item, i);
          El textEl = El.fly(text);
          textEl.setInnerHtml("");
          textEl.setStyleName(widgetStyle);
          textEl.appendChild(w.getElement());
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
    if (table.buildRender) {
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
        String s = table.getRenderedValue(index, value);
        El.fly(textElem).setInnerHtml(s);
      }
    }
    applyCellStyles(item);
  }

  public void resize() {
    int width = table.getOffsetWidth();
    int headerHeight = table.getTableHeader().getOffsetHeight();
    int bodyHeight = table.getOffsetHeight() - headerHeight;
    int bodyWidth = width;

    dataEl.setWidth(cm.getTotalWidth());
    table.getTableHeader().setWidth(cm.getTotalWidth());

    boolean vscroll = dataEl.getHeight() > bodyHeight;
    int adj = vscroll ? scrollBarWidth : 0;

    boolean isGecko = GXT.isGecko;
    String overflowX = "visible";

    if (table.horizontalScroll) {
      if (dataEl.getWidth() < (width - adj)) {
        if (isGecko) {
          overflowX = "hidden";
        } else {
          bodyHeight += scrollBarWidth;
          table.getTableHeader().el.setLeft(0);
        }
      }
    } else {
      if (isGecko) {
        overflowX = "hidden";
      } else {
        bodyHeight += scrollBarWidth;
      }
    }
    if (dataEl.getHeight() > bodyHeight) {
      width -= scrollBarWidth;
    }

    if (isGecko) {
      scrollEl.setStyleAttribute("overflowX", overflowX);
    }

    bodyHeight -= table.el.getBorderWidth("tb");
    bodyWidth -= table.el.getBorderWidth("lr");

    scrollEl.setSize(bodyWidth, bodyHeight);

    int w = cm.getTotalWidth();

    if (w < width) {
      adj = width - w;
    }

    dataEl.setWidth(cm.getTotalWidth() + adj);
  }

  public void resizeCells(int columnIndex) {
    TableColumn c = cm.getColumn(columnIndex);
    int w = cm.getWidthInPixels(c.index);

    if (c.lastWidth != 0 && c.lastWidth == w) {
      return;
    }
    c.lastWidth = w;
    if (table.verticalLines) {
      --w;
    }

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
