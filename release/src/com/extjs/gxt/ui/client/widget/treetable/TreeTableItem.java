/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.treetable;


import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.TreeTableEvent;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.extjs.gxt.ui.client.widget.tree.TreeItemUI;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A item in a <code>TreeTable</code>. All events are bubbled to the item's
 * parent treetable.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : TreeTableEvent(treeTable, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : TreeTableEvent(widget, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeExpand</b> : TreeTableEvent(widget, item)<br>
 * <div>Fires before a item is expanded. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : TreeTableEvent(widget, item)<br>
 * <div>Fires before a item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : TreeTableEvent(widget, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : TreeEvent(item, child)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : TreeEvent(item)<br>
 * <div>Fires after a item has been expanded.</div>
 * <ul>
 * <li>item : the item being expanded</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : TreeEvent(item)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>item : the item being collapsed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : TreeTableEvent(item)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellClick</b> : TreeTableEvent(item, columnIndex)<br>
 * <div>Fires after a cell has been clicked.</div>
 * <ul>
 * <li>item : this</li>
 * <li>columnIndex : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellDoubleClick</b> : RowDoubleClick(item, columnIndex)<br>
 * <div>Fires after a cell has been double clicked.</div>
 * <ul>
 * <li>item : this</li>
 * <li>columnIndex : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowClick</b> : (item, index)<br>
 * <div>Fires after a cell has been clicked.</div>
 * <ul>
 * <li>item : this</li>
 * <li>columnIndex : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowDoubleClick</b> : TreeTableEvent(item, columnIndex)<br>
 * <div>Fires after a cell has been double clicked.</div>
 * <ul>
 * <li>item : this</li>
 * <li>columnIndex : cell column index</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tree-item (the item itself)</dd>
 * <dd>.my-tree-item-text span (the tree item text)</dd>
 * </dl>
 */
public class TreeTableItem extends TreeItem {

  protected TreeTable treeTable;

  String[] toolTips;
  ToolTip cellToolTip;
  boolean hasWidgets;
  String[] cellStyles;
  boolean cellsRendered;
  private Object[] values;

  /**
   * Creates a new tree table item.
   * 
   * @param values the cell values
   */
  public TreeTableItem(Object[] values) {
    this.values = values;
  }

  /**
   * Creates a new tree table item.
   * 
   * @param text the text
   * @param values the values
   */
  public TreeTableItem(String text, Object[] values) {
    super(text);
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
   * Returns the item's tree table.
   * 
   * @return the tree table
   */
  public TreeTable getTreeTable() {
    return treeTable;
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
    if (ce.component != this) {
      return;
    }
    TreeTableEvent tte = new TreeTableEvent(treeTable);
    tte.item = this;
    tte.event = ce.event;
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        tree.fireEvent(Events.MouseOver, tte);
        break;
      case Event.ONMOUSEDOWN:
        tree.fireEvent(Events.MouseDown, tte);
        break;
      case Event.ONKEYDOWN:
        tree.fireEvent(Events.KeyDown, tte);
        break;
      case Event.ONMOUSEOUT:
        tree.fireEvent(Events.MouseOut, tte);
        break;
      case Event.ONCLICK:
        tree.fireEvent(Events.Click, tte);
        break;
      case Event.ONDBLCLICK:
        tree.fireEvent(Events.DoubleClick, tte);
        break;
    }
    if (ui != null) {
      getUI().getListener().handleEvent(tte);
    }

//    if (cellToolTip != null) {
//      cellToolTip.handleEvent(be);
//    }

    switch (ce.type) {
      case Events.Click:
        onClick(ce);
        break;
      case Events.DoubleClick:
        onDoubleClick(ce);
        break;
      case Events.MouseOver:
        onMouseOver(ce);
        break;
    }

  }

  /**
   * Sets the style for a table cell.
   * 
   * @param index the column index
   * @param style the new style
   */
  public void setCellStyle(int index, String style) {
    if (cellStyles == null) cellStyles = new String[values.length];
    cellStyles[index] = style;
    if (isRendered()) {
      treeTable.getView().setCellStyle(this, index, style);
    }
  }

  /**
   * Returns the style for the given cell.
   * 
   * @param index the column index
   * @return the style
   */
  public String getCellStyle(int index) {
    if (cellStyles != null) {
      return cellStyles[index];
    }
    return null;
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
      treeTable.getView().renderItemValue(this, index, value);
    }
  }

  /**
   * Sets the item's values.
   * 
   * @param values the values
   */
  public void setValues(Object[] values) {
    this.values = values;
  }

  protected String[] getRenderedValues() {
    String[] svalues = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      svalues[i] = treeTable.getRenderedValue(i, values[i]);
    }
    return svalues;
  }

  /**
   * Subclasses may override.
   * 
   * @return the ui
   */
  protected TreeItemUI getTreeItemUI() {
    return new TreeTableItemUI(this);
  }
  
  protected void onRender(Element target, int index) {
    ui = getTreeItemUI();
    ui.render(target, index);
  }

  protected void init(TreeTable treeTable) {
    this.tree = treeTable;
    this.treeTable = treeTable;
  }

  protected void initCellToolTips() {
//    if (cellToolTip == null && isRendered()) {
//      cellToolTip = new ToolTip(this);
//      cellToolTip.setTrackMouse(true);
//    }
  }

  public void setElement(Element elem) {
    super.setElement(elem);
  }

  protected void onMouseOver(ComponentEvent ce) {
    onCellMouseOver(ce);
  }

  protected void onCellMouseOver(ComponentEvent ce) {
    Element target = ce.getTarget();

    int index = treeTable.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

//    if (cellToolTip != null) {
//      if (toolTips != null && toolTips[index] != null && toolTips[index].length() > 0) {
//        cellToolTip.setText(null, toolTips[index]);
//        cellToolTip.setVisible(true);
//      } else {
//        cellToolTip.setVisible(false);
//      }
//    }
  }

  protected void onClick(ComponentEvent ce) {
    Element target = ce.getTarget();

    int index = treeTable.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }

    TreeTableEvent evt = new TreeTableEvent(treeTable);
    evt.item = this;
    evt.index = index;
    evt.event = ce.event;

    treeTable.fireEvent(Events.CellClick, evt);
    treeTable.fireEvent(Events.RowClick, evt);
  }

  protected void onDoubleClick(ComponentEvent ce) {
    Element target = ce.getTarget();

    int index = treeTable.getView().getCellIndex(target);
    if (index == Style.DEFAULT) {
      return;
    }
    TreeTableEvent evt = new TreeTableEvent(treeTable);
    evt.item = this;
    evt.event = ce.event;
    evt.index = index;

    treeTable.fireEvent(Events.CellDoubleClick, evt);
    treeTable.fireEvent(Events.RowDoubleClick, evt);
  }

  protected void setTree(Tree tree) {
    super.setTree(tree);
    treeTable = (TreeTable) tree;
  }

}
