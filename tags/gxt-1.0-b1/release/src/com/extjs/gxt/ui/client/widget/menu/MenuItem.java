/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.widget.Component;

/**
 * The base class for all items that render into menus. BaseItem provides
 * default rendering, activated state management.
 */
public class MenuItem extends Component {

  /**
   * The CSS class to use when the item becomes activated (defaults to
   * "x-menu-item-active").
   */
  public String activeStyle = "x-menu-item-active";

  /**
   * True if this item can be visually activated (defaults to false).
   */
  protected boolean canActivate;

  /**
   * True to hide the containing menu after this item is clicked (defaults to
   * true).
   */
  protected boolean hideOnClick = true;

  /**
   * The item's containing menu.
   */
  protected Menu parentMenu;

  /**
   * Returns the item's containing menu.
   * 
   * @return the menu
   */
  public Menu getParentMenu() {
    return parentMenu;
  }

  /**
   * Adds a selection listener.
   * 
   * @param listener the listener to add
   */
  public void addSelectionListener(SelectionListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Events.Select, tl);
  }

  /**
   * Returns the hide on click state.
   * 
   * @return the hide on click state
   */
  public boolean getHideOnClick() {
    return hideOnClick;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeSelectionListener(SelectionListener listener) {
    removeListener(Events.Select, listener);
  }

  /**
   * True to hide the containing menu after this item is clicked (defaults to
   * true).
   * 
   * @param hideOnClick true to hide, otherwise false
   */
  public void setHideOnClick(boolean hideOnClick) {
    this.hideOnClick = hideOnClick;
  }

  protected void activate(boolean autoExpand) {
    if (disabled) {
      return;
    }
    El li = el.getParent();
    li.addStyleName(activeStyle);
    MenuEvent me = new MenuEvent(parentMenu);
    me.item = this;
    fireEvent(Events.Activate, me);
  }

  protected void deactivate() {
    El li = el.getParent();
    li.removeStyleName(activeStyle);
    MenuEvent me = new MenuEvent(parentMenu);
    me.item = this;
    fireEvent(Events.Deactivate, me);
  }

  protected void expandMenu(boolean autoActivate) {

  }

  protected void handleClick(ComponentEvent be) {
    if (hideOnClick) {
      parentMenu.hide(true);
    }
  }

  protected void onClick(ComponentEvent be) {
    MenuEvent me = new MenuEvent(parentMenu);
    me.item = this;
    if (!disabled && fireEvent(Events.Select, me)) {
      handleClick(be);
    }
  }

  protected boolean shouldDeactivate(ComponentEvent ce) {
    return true;
  }

}
