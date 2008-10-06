/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.custom;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.DragListener;
import com.extjs.gxt.ui.client.event.PortalEvent;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;

/**
 * A Portal container of Portlets.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>ValidateDrop</b> : PortalEvent(portal, startColumn, startRow,
 * column, row)<br>
 * <div>Fires before a dragged portlet can be inserted into a new location.</div>
 * <ul>
 * <li>portal : this</li>
 * <li>portlet : the portlet being dragged</li>
 * <li>startColumn : the start column</li>
 * <li>startRow : the start row</li>
 * <li>column : the new column</li>
 * <li>row : the new row</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Drop</b> : PortalEvent(portal, startColumn, startRow, column, row)<br>
 * <div>Fires after a portlet has been moved.</div>
 * <ul>
 * <li>portal : this</li>
 * <li>portlet : the portlet being dropped</li>
 * <li>startColumn : the start column</li>
 * <li>startRow : the start row</li>
 * <li>column : the new column</li>
 * <li>row : the new row</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Portal extends Container<LayoutContainer> {

  private List<LayoutContainer> columns = new ArrayList<LayoutContainer>();
  private DragListener listener;
  private List<Integer> startColumns;
  private int numColumns;
  private int startCol, startRow;
  private int insertCol = -1, insertRow = -1;
  private Portlet active;
  private El dummy;
  private ColumnLayout cl;

  /**
   * Creates a new portal container.
   * 
   * @param numColumns the number of columns
   */
  public Portal(int numColumns) {
    this.numColumns = numColumns;
    cl = new ColumnLayout();
    cl.setAdjustForScroll(true);
    setLayout(cl);
    setStyleAttribute("overflow", "auto");
    baseStyle = "x-portal";
    enableLayout = true;

    for (int i = 0; i < numColumns; i++) {
      LayoutContainer l = new LayoutContainer();
      l.addStyleName("x-portal x-portal-column");
      l.setStyleAttribute("minHeight", "20px");
      l.setStyleAttribute("padding", "10px 0px 0px 10px");
      l.setLayout(new RowLayout());
      l.setLayoutOnChange(true);
      add(l);
      columns.add(l);
    }

    listener = new DragListener() {

      @Override
      public void dragCancel(DragEvent de) {
        onDragCancel(de);
      }

      @Override
      public void dragEnd(DragEvent de) {
        onDragEnd(de);
      }

      @Override
      public void dragMove(DragEvent de) {
        onDragMove(de);
      }

      @Override
      public void dragStart(DragEvent de) {
        onDragStart(de);
      }

    };
  }

  /**
   * Adds a portlet to the portal.
   * 
   * @param portlet the portlet to add
   * @param column the column to insert into
   */
  public void add(Portlet portlet, int column) {
    Draggable d = new Draggable(portlet, portlet.getHeader());
    d.setUseProxy(true);
    d.addDragListener(listener);
    d.setMoveAfterProxyDrag(false);
    d.setSizeProxyToSource(true);
    columns.get(column).add(portlet, new RowData(1, -1));
  }

  /**
   * Removes a portlet from the portal.
   * 
   * @param portlet the porlet to remove
   * @param column the column
   */
  public void remove(Portlet portlet, int column) {
    columns.get(column).remove(portlet);
  }

  /**
   * True to adjust the layout for a vertical scroll bar (defaults to true).
   * 
   * @param adjust true to adjust
   */
  public void setAjustForScroll(boolean adjust) {
    cl.setAdjustForScroll(adjust);
  }

  /**
   * Sets the column's width.
   * 
   * @param colIndex the column index
   * @param width the column width
   */
  public void setColumnWidth(int colIndex, double width) {
    ComponentHelper.setLayoutData(columns.get(colIndex), new ColumnData(width));
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        el().setVisible(false);
        el().setVisible(true);
      }
    });
  }

  protected void onDragEnd(DragEvent de) {
    dummy.removeFromParent();

    if (insertCol != -1 && insertRow != -1) {
      if (startCol == insertCol && insertRow > startRow) {
        insertRow--;
      }
      active.setVisible(true);
      active.removeFromParent();
      columns.get(insertCol).insert(active, insertRow);
      active.addStyleName("x-repaint");

      fireEvent(Events.Drop,
          new PortalEvent(this, active, startCol, startRow, insertCol, insertRow));
    }
    active.setVisible(true);
    active = null;
    insertCol = -1;
    insertRow = -1;
  }

  protected void onDragMove(DragEvent de) {
    active.setVisible(false);
    
    int col = getColumn(de.getClientX());
    int row = getRowPosition(col, de.getClientY());
    
    if (col != insertCol || row != insertRow) {
      PortalEvent pe = new PortalEvent(this, active, startCol, startRow, col, row);
      if (fireEvent(Events.ValidateDrop, pe)) {
        addInsert(col, row);
      } else {
        insertCol = startCol;
        insertRow = startRow;
      }
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);
  }

  private void addInsert(int col, int row) {
    insertCol = col;
    insertRow = row;
    dummy.removeFromParent();
    LayoutContainer lc = columns.get(insertCol);
    lc.el().insertChild(dummy.dom, row);
  }

  private int getColumn(int x) {
    for (int i = startColumns.size() - 1; i >= 0; i--) {
      if (x > startColumns.get(i)) {
        return i;
      }
    }
    return 0;
  }

  private int getRow(int col, int y) {
    LayoutContainer con = columns.get(col);
    int count = con.getItemCount();

    for (int i = 0; i < count; i++) {
      Component c = con.getItem(i);
      int b = c.getAbsoluteTop();
      int t = b + c.getOffsetHeight();

      if (y < t) {
        return i;
      }
    }

    return 0;
  }

  private int getRowPosition(int col, int y) {
    LayoutContainer con = columns.get(col);
    List<Component> list = new ArrayList<Component>(con.getItems());
    list.remove(dummy);
    int count = list.size();

    if (count == 0) {
      return 0;
    }
    for (int i = 0; i < count; i++) {
      Component c = list.get(i);
      int b = c.getAbsoluteTop();
      int t = b + c.getOffsetHeight();
      int m = b + (c.getOffsetHeight() / 2);
      if (y < t) {
        if (y < m) {
          return i;
        } else {
          return i + 1;
        }
      }
    }
    return list.size();
  }

  private void onDragCancel(DragEvent event) {
    active.setVisible(true);
    active = null;
    insertCol = -1;
    insertRow = -1;
    dummy.removeFromParent();
  }

  private void onDragStart(DragEvent de) {
    active = (Portlet) de.component;
    
    if (dummy == null) {
      dummy = new El(XDOM.create("<div class='x-portal-insert' style='margin-bottom: 10px'><div></div></div>"));
      dummy.setStyleName("x-portal-insert");
    }

    dummy.setStyleAttribute("padding", active.el().getStyleAttribute("padding"));

    int h = active.el().getHeight() - active.el().getFrameWidth("tb");
    dummy.firstChild().setHeight(h);

    startColumns = new ArrayList<Integer>();
    for (int i = 0; i < numColumns; i++) {
      LayoutContainer con = columns.get(i);
      int x = con.getAbsoluteLeft();
      startColumns.add(x);
    }
    startCol = getColumn(de.getClientX());
    startRow = getRow(startCol, de.getClientY());
    
    
  }
}
