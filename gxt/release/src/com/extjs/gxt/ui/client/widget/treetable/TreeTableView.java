/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.treetable;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class encapsulates the user interface of a {@link TreeTable}.
 */
public class TreeTableView {

  private static String bodyHTML;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div style='overflow: hidden;'>");
    sb.append("<div style='overflow: scroll;'>");
    sb.append("<div class='my-treetbl-data'>");
    sb.append("<div class='my-treetbl-tree'></div>");
    sb.append("</div></div></div>");
    bodyHTML = sb.toString();
  }

  // styles
  protected String baseStyle = "my-tbl-item";
  protected String overStyle = baseStyle + "-over";
  protected String selStyle = baseStyle + "-sel";
  protected String cellStyle = baseStyle + "-" + "cell";
  protected String cellOverflowStyle = baseStyle + "-" + "overflow";
  protected String textStyle = cellStyle + "-text";
  protected String widgetStyle = cellStyle + "-widget";

  protected TableColumnModel cm;
  protected Element treeDiv;
  protected El scrollEl, dataEl;
  protected TreeTable treeTable;
  protected int scrollBarWidth;

  public void applyCellStyles(TreeTableItem item) {
    if (item.cellStyles != null) {
      for (int i = 0; i < item.cellStyles.length; i++) {
        setCellStyle(item, i, item.cellStyles[i]);
      }
    }
  }

  public int getCellIndex(Element target) {
    String index = target.getAttribute("index");
    if (index != null && index.length() == 0) {
      target = DOM.getParent(target);
      while (target != null && target != null) {
        index = target.getAttribute("index");
        if (index != null && index.length() == 0) {
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

  public Element getTextCellElement(TreeTableItem item, int cell) {
    return ((TreeTableItemUI) item.getUI()).getTextCellElement(cell);
  }

  public void init(TreeTable treeTable) {
    this.treeTable = treeTable;
    this.cm = treeTable.getColumnModel();
  }

  public void removeItem(TableItem item) {
    dataEl.dom.removeChild(item.el().dom);
  }

  public void render() {
    scrollBarWidth = XDOM.getScrollBarWidth();

    Element div = DOM.createDiv();
    DOM.setInnerHTML(div, bodyHTML.toString());
    scrollEl = new El(El.fly(div).getSubChild(2));
    dataEl = new El(DOM.getFirstChild(scrollEl.dom));
    treeDiv = dataEl.firstChild().dom;
    DOM.appendChild(treeDiv, treeTable.getRootItem().getElement());
    DOM.appendChild(treeTable.getElement(), DOM.getFirstChild(div));

    if (!GXT.isIE) {
      DOM.setElementPropertyInt(treeTable.getElement(), "tabIndex", 0);
    }

    treeTable.disableTextSelection(true);

    DOM.sinkEvents(scrollEl.dom, Event.ONSCROLL);
  }

  public void renderItemValue(TreeTableItem item, int index, Object value) {
    Element textElem = getTextCellElement(item, index);
    if (textElem != null) {
      DOM.setInnerHTML(textElem, "");
      if (value instanceof Widget) {
        Widget widget = (Widget) value;
        XDOM.setStyleName(textElem, widgetStyle);
        DOM.appendChild(textElem, widget.getElement());
        if (treeTable.isAttached()) {
          ComponentHelper.doAttach(widget);
        }
      } else {
        String s = treeTable.getRenderedValue(item, index, value);
        textElem.setInnerHTML(s);
      }
    }
    applyCellStyles(item);
  }

  public void resize() {
    int width = treeTable.getOffsetWidth();
    int headerHeight = treeTable.getTableHeader().getOffsetHeight();
    int bodyHeight = treeTable.getOffsetHeight() - headerHeight;
    int bodyWidth = width;

    if (treeTable.isAutoHeight()) {
      scrollEl.setHeight("auto");
      dataEl.setHeight("auto");
      bodyHeight = dataEl.getHeight();
      bodyHeight += treeTable.el().getBorderWidth("tb");
    }

    int columnModelWidth = cm.getTotalWidth();
    dataEl.setWidth(Math.min(width, columnModelWidth));
    treeTable.getTableHeader().setWidth(columnModelWidth);

    bodyHeight -= treeTable.el().getBorderWidth("tb");
    bodyWidth -= treeTable.el().getBorderWidth("lr");

    scrollEl.setStyleAttribute("overflowY", "auto");

    if (treeTable.getHorizontalScroll()) {
      scrollEl.setStyleAttribute("overflowX", "auto");
      if (columnModelWidth < width) {
        scrollEl.setStyleAttribute("overflowX", "hidden");
        treeTable.getTableHeader().el().setLeft(0);
      }
    }

    if (treeTable.isAutoHeight()) {
      bodyHeight = -1;
    }
    scrollEl.setSize(bodyWidth, bodyHeight);
  }

  public void resizeCells(int columnIndex) {
    TreeTableColumn c = (TreeTableColumn) cm.getColumn(columnIndex);
    int w = ((TreeTableColumnModel) cm).getWidthInPixels(c.getIndex());
    String sel = "." + treeTable.getId() + "-col-" + columnIndex;
    String rule = "width:" + w + "px;" + (!c.isHidden() ? "" : "display: none;");
    treeTable.styleTemplate.set(sel, rule);
    treeTable.styleTemplate.apply();
  }

  public void setCellStyle(TreeTableItem item, int index, String style) {
    if (item.cellsRendered) {
      Element cell = getTextCellElement(item, index);
      XDOM.setStyleName(cell, textStyle + " " + style);
    }
  }

  public void showColumn(int columnIndex, boolean show) {
    TreeTableColumn c = (TreeTableColumn) cm.getColumn(columnIndex);
    int w = ((TreeTableColumnModel) cm).getWidthInPixels(c.getIndex());
    String sel = "." + treeTable.getId() + "-col-" + columnIndex;
    String rule = "width:" + w + "px;" + (!c.isHidden() ? "" : "display: none;");
    treeTable.styleTemplate.set(sel, rule);
    treeTable.styleTemplate.apply();
  }
}
