/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;


import java.util.ArrayList;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.AbstractContainer;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.Element;

/**
 * A standard tool bar.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : ToolBarEvent(toolBar, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>toolBar : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : ToolBarEvent(toolBar, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>toolBar : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : ToolBarEvent(toolBar, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>toolBar : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : ToolBarEvent(toolBar, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>toolBar : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>x-toolbar (the tool bar)</dd>
 * </dl>
 * 
 * @see ToolItem
 * @see ToggleToolItem
 * @see SplitToolItem
 */
public class ToolBar extends AbstractContainer<ToolItem> {
  
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
  public HorizontalAlignment buttonAlign = HorizontalAlignment.LEFT;

  private HorizontalPanel panel;

  /**
   * Creates a new tool bar.
   */
  public ToolBar() {
    baseStyle = "x-toolbar";
  }
  
  /**
   * Adds a item to the tool bar.
   * 
   * @param item the item to add
   */
  public void add(ToolItem item) {
    insert(item, getItemCount());
  }

  /**
   * Inserts a item into the tool bar.
   * 
   * @param item the item to add
   * @param index the insert location
   */
  public void insert(ToolItem item, int index) {
    ToolBarEvent tbe = new ToolBarEvent(this);
    tbe.item = item;
    tbe.index = index;
    if (fireEvent(Events.BeforeAdd, tbe)) {
      item.toolBar = this;
      items.add(index, item);
      if (rendered) {
        renderItem(item, index);
        if (isAttached()) {
          WidgetHelper.doAttach(item);
        }
      }
      fireEvent(Events.Add, tbe);
    }
  }

  /**
   * Removes a component from the tool bar.
   * 
   * @param item the item to be removed
   */
  public boolean remove(ToolItem item) {
    ToolBarEvent tbe = new ToolBarEvent(this);
    tbe.item = item;
    if (fireEvent(Events.BeforeRemove, tbe)) {
      item.toolBar = null;
      items.remove(item);
      if (rendered) {
        panel.remove(item);
      }
      fireEvent(Events.Remove, tbe);
      return true;
    }
    return false;
  }
  
  /**
   * Removes all items.
   */
  public void removeAll() {
    for (ToolItem item : new ArrayList<ToolItem>(items)) {
      remove(item);
    }
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    panel = new HorizontalPanel();
    panel.setLayoutOnChange(true);
    panel.align = buttonAlign;
    panel.setStyleName(baseStyle + " x-small-editor");
    panel.render(target, index);
    setElement(panel.getElement());
    setHeight(25);
    setStyleAttribute("paddingRight", "8px");
    
    renderAll();
  }

  private void renderAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      Component item = getItem(i);
      renderItem(item, i);
    }
  }

  private void renderItem(Component item, int index) {
    TableData data = new TableData();
    data.verticalAlign = VerticalAlignment.MIDDLE;
    item.setData(data);
    if (item instanceof FillToolItem) {
      data.width = "100%";
    }
    panel.insert(item, index);
  }

}
