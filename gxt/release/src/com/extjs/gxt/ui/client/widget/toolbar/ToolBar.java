/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.toolbar;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableRowLayout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A standard tool bar.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : ToolBarEvent(container, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : ToolBarEvent(container, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : ToolBarEvent(container, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : ToolBarEvent(container, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * <dl>
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>BoxComponent Move</dd>
 * <dd>BoxComponent Resize</dd>
 * <dd>Component Enable</dd>
 * <dd>Component Disable</dd>
 * <dd>Component BeforeHide</dd>
 * <dd>Component Hide</dd>
 * <dd>Component BeforeShow</dd>
 * <dd>Component Show</dd>
 * <dd>Component Attach</dd>
 * <dd>Component Detach</dd>
 * <dd>Component BeforeRender</dd>
 * <dd>Component Render</dd>
 * <dd>Component BrowserEvent</dd>
 * <dd>Component BeforeStateRestore</dd>
 * <dd>Component StateRestore</dd>
 * <dd>Component BeforeStateSave</dd>
 * <dd>Component SaveState</dd>
 * </dl>
 * 
 * <dl>
 * <dt><b>CSS:</b></dt>
 * <dd>x-toolbar (the tool bar)</dd>
 * </dl>
 * 
 * @see ToolItem
 * @see ToggleToolItem
 * @see SplitToolItem
 */
public class ToolBar extends Container<ToolItem> {

  private HorizontalAlignment buttonAlign = HorizontalAlignment.LEFT;

  /**
   * Creates a new tool bar.
   */
  public ToolBar() {
    baseStyle = "x-toolbar";
    layoutOnChange = true;
    enableLayout = true;
  }

  /**
   * Adds a item to the tool bar.
   * 
   * @param item the item to add
   */
  @Override
  public boolean add(ToolItem item) {
    return super.add(item);
  }

  /**
   * Returns the button alignment.
   * 
   * @return the button alignment
   * @deprecated use a FillToolItem as the first item to right align the toolbar
   */
  public HorizontalAlignment getButtonAlign() {
    return buttonAlign;
  }

  /**
   * Inserts a item into the tool bar.
   * 
   * @param item the item to add
   * @param index the insert location
   */
  public boolean insert(ToolItem item, int index) {
    boolean added = super.insert(item, index);
    if (added) {
      item.toolBar = this;
      TableData data = new TableData();
      data.setVerticalAlign(VerticalAlignment.MIDDLE);
      ComponentHelper.setLayoutData(item, data);
      if (item instanceof FillToolItem) {
        data.setWidth("100%");
      }
    }
    return added;
  }

  /**
   * Removes a component from the tool bar.
   * 
   * @param item the item to be removed
   */
  public boolean remove(ToolItem item) {
    return super.remove(item);
  }

  /**
   * Specifies the button alignment (defaults to LEFT).
   * 
   * @param buttonAlign the button alignment
   * @deprecated use a FillToolItem as the first item to right align the toolbar
   */
  public void setButtonAlign(HorizontalAlignment buttonAlign) {
    this.buttonAlign = buttonAlign;
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new ToolBarEvent(this);
  }

  @Override
  protected ContainerEvent createContainerEvent(ToolItem item) {
    return new ToolBarEvent(this, item);
  }

  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);

    addStyleName(baseStyle + " x-small-editor");
    setStyleAttribute("paddingRight", "8px");

    TableRowLayout layout = new TableRowLayout();
    layout.setCellSpacing(0);
    setLayout(layout);
    layout();
  }

}
