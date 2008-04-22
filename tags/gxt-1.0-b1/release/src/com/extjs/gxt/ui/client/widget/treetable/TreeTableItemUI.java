/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.treetable;


import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.event.TreeTableEvent;
import com.extjs.gxt.ui.client.util.Markup;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.tree.TreeItemUI;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class TreeTableItemUI extends TreeItemUI {

  protected static Listener<TreeTableEvent> listener = new Listener<TreeTableEvent>() {
    public void handleEvent(TreeTableEvent te) {
      TreeTableItem item = (TreeTableItem) te.item;
      TreeItemUI ui = item.getUI();
      Element target = te.getTarget();
      int type = te.type;
      switch (type) {
        case Events.MouseOver:
        case Events.MouseOut:
          treeTableMouseListener.handleEvent(te);
          break;
        case Events.Click:
        case Events.DoubleClick:
          te.stopEvent();
          if (DOM.isOrHasChild(ui.getCheckEl(), target)) {
            item.setChecked(!item.isChecked());
          } else {
            // TODO fix this.. since this is a ALWAYS class cast exception 
            clickListener.handleEvent((TreeEvent)((Object)te));
          }
          break;
      }
    }

  };

  protected static Listener<TreeTableEvent> treeTableMouseListener = new Listener<TreeTableEvent>() {
    public void handleEvent(TreeTableEvent te) {
      TreeTableItem item = (TreeTableItem)te.item;
      TreeTableItemUI treeUI = (TreeTableItemUI) item.getUI();
      Rectangle rect = item.el.firstChild().getBounds();
      if (rect.contains(te.getClientX(), te.getClientY())) {
        if (!treeUI.hovering) {
          treeUI.hovering = true;
          treeUI.onMouseOver(te);
        }
      } else {
        treeUI.hovering = false;
        treeUI.onMouseOut(te);
      }

    }
  };

  boolean hovering;
  private Element tableItemEl;
  private Element rowTableEl;
  private Element rowTrEl;
  private Element[] cells;

  public TreeTableItemUI(TreeTableItem item) {
    super(item);
    styleTreeOver = "my-treetbl-over";
    styleTreeJointOver = "my-treetbl-joint-over";
    styleTreeChecked = "my-treetbl-checked";
    styleTreeNotChecked = "my-treetbl-notchecked";
    styleTreeLoading = "my-treetbl-loading";
    styleTreeSelected = "my-treetbl-sel";
    classTreeOpen = "my-treetbl-open";
    classTreeClose = "my-treetbl-close";
  }

  public Element getContainer() {
    return containerEl;
  }

  public Listener getListener() {
    return listener;
  }

  public TreeTableItem getTreeTableItem() {
    return (TreeTableItem) item;
  }

  public void onMouseOut(BaseEvent be) {
//    ToolTip tooltip = getTreeTableItem().getCellToolTip();
//    if (tooltip != null && tooltip.isShowing()) {
//      tooltip.hide();
//    }
    if (!item.isRoot()) {
      El.fly(tableItemEl).removeStyleName("my-treetbl-item-over");
    }
  }

  public void onMouseOver(BaseEvent be) {
    if (!item.isRoot()) {
      El.fly(tableItemEl).addStyleName("my-treetbl-item-over");
    }
  }

  public void onSelectedChange(boolean selected) {
    if (item.isRendered()) {
      El.fly(tableItemEl).setStyleName("my-treetbl-item-sel", selected);
    }
  }

  public void render(Element target, int index) {
    if (item.isRoot() == true) {
      return;
    }

    item.setElement(DOM.createDiv());
    item.setStyleName("my-treeitem");
    DOM.insertChild(target, item.getElement(), index);
    
    TableColumnModel cm = getTreeTableItem().getTreeTable().getColumnModel();

    DOM.appendChild(item.getParentItem().getContainer(), item.getElement());
    DOM.setInnerHTML(item.getElement(), Markup.TREETABLE_ITEM);
    tableItemEl = item.el.firstChild().dom;

    rowTableEl = DOM.getFirstChild(tableItemEl);
    rowTrEl = El.fly(rowTableEl).getSubChild(2);
    updateCellValues(0, DOM.getFirstChild(rowTrEl), cm.getColumn(0).getAlignment());
    itemEl = new El(El.fly(rowTrEl).getSubChild(4));
    Element td = itemEl.subChild(3).dom;
    indentEl = DOM.getFirstChild(td);
    jointEl = DOM.getNextSibling(td);
    jointDivEl = DOM.getFirstChild(jointEl);
    checkEl = DOM.getNextSibling(DOM.getNextSibling(jointEl));
    checkDivEl = DOM.getFirstChild(checkEl);
    iconEl = DOM.getNextSibling(checkEl);
    iconDivEl = DOM.getFirstChild(iconEl);
    textEl = DOM.getNextSibling(iconEl);
    textSpanEl = DOM.getFirstChild(textEl);
    Element tbl = DOM.getFirstChild(item.getElement());
    containerEl = DOM.getNextSibling(tbl);

    int numColumns = cm.getColumnCount();
    cells = new Element[numColumns];

    for (int i = 1; i < numColumns; i++) {
      cells[i] = DOM.createTD();
      DOM.appendChild(rowTrEl, cells[i]);

      DOM.setElementProperty(cells[i], "className", "my-treetbl-cell");
      DOM.setElementAttribute(cells[i], "index", String.valueOf(i));
      
      Element overflowDiv = DOM.createDiv();
      DOM.setElementProperty(overflowDiv, "className", "my-treetbl-cell-overflow");
      DOM.appendChild(cells[i], overflowDiv);
      Element textDiv = DOM.createDiv();
      
      String textStyle = "my-treetbl-cell-text";
      if (((TreeTableItem)item).getCellStyle(i) != null) {
        textStyle += " " + ((TreeTableItem)item).getCellStyle(i);
      }
      DOM.setElementProperty(textDiv, "className", textStyle);
      DOM.appendChild(overflowDiv, textDiv);
      updateCellValues(i, cells[i], cm.getColumn(i).getAlignment());
    }

    boolean checkable = getTreeTableItem().getTreeTable().checkable;
    El.fly(checkEl).setVisible(checkable);


    onValuesChanged(getTreeTableItem().getTreeTable(),
        getTreeTableItem().getRenderedValues());

    onIconStyleChange(item.getIconStyle());

    if (item.isChecked()) {
      onCheckChange(true);
    }

    El.fly(indentEl).setWidth(getIndent());

    if (!GXT.isIE) {
      DOM.setElementPropertyInt(item.getElement(), "tabIndex", 0);
    }
    
    getTreeTableItem().initCellToolTips();

    updateJoint();
    item.disableTextSelection(true);

  }

  public void setContainer(Element container) {
    containerEl = container;
  }

  protected Element getTextCellElement(int column) {
    if (column == 0) {
      return textSpanEl;
    } else {
      return El.fly(cells[column]).getSubChild(2);
    }
  }

  protected void onValuesChanged(TreeTable table, String[] values) {
    onTextChange(item.getText());
    for (int i = 1; i < cells.length; i++) {
      updateText(i, values[i]);
    }
  }

  protected void updateCellValues(int col, Element cell, HorizontalAlignment align) {
    String salign = "left";
    if (align == HorizontalAlignment.CENTER) {
      salign = "center";
    } else if (align == HorizontalAlignment.RIGHT) {
      salign = "right";
    }

    String widthClassName = ((TreeTableItem) item).treeTable.getId() + "-col-" + col;

    String className = DOM.getElementProperty(cell, "className");
    className = (className == null) ? widthClassName : className + " " + widthClassName;
    DOM.setElementProperty(cell, "className", className);

    className = DOM.getElementProperty(DOM.getFirstChild(cell), "className");
    className = (className == null) ? widthClassName : className + " " + widthClassName;
    DOM.setElementProperty(DOM.getFirstChild(cell), "className", className);

    El.fly(cell).subChild(2).setStyleAttribute("textAlign", salign);
  }

  protected void updateText(int column, String value) {
    Element textElem = getTextCellElement(column);
    if (textElem != null) {
      El.fly(textElem).setInnerHtml(value);
    }
  }
}
