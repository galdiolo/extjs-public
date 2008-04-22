/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.google.gwt.user.client.Element;

/**
 * Adds a menu item that contains a checkbox by default, but can also be part of
 * a radio group.
 * 
 * A horizontal row of buttons.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeCheckChange</b> : MenuEvent(menu, item)<br>
 * <div>Fires before the item is checked or unchecked.</div>
 * <ul>
 * <li>item : this</li>
 * <li>menu : the parent menu</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : MenuEvent(menu, item)<br>
 * <div>Fires after the item is checked or unchecked.</div>
 * <ul>
 * <li>item : this</li>
 * <li>menu : the parent menu</li>
 * </ul>
 * </dd>
 * </dt>
 */
public class CheckMenuItem extends Item {

  /**
   * The default CSS class to use for radio group check items (defaults to
   * "x-menu-group-item"),
   */
  public String groupStyle = "x-menu-group-item";

  /**
   * True to initialize this checkbox as checked (defaults to false). Note that
   * if this checkbox is part of a radio group (group = true) only the last item
   * in the group that is initialized with checked = true will be rendered as
   * checked.
   */
  public boolean checked;

  /**
   * All check items with the same group name will automatically be grouped into
   * a single-select radio button group (defaults to null).
   */
  public String group;

  public CheckMenuItem() {
    hideOnClick = true;
    itemStyle = "x-menu-item x-menu-check-item";
    canActivate = true;
  }

  public CheckMenuItem(String text) {
    this();
    setText(text);
  }

  /**
   * Returns true if the item is checked.
   * 
   * @return the checked state
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Set the checked state of this item.
   * 
   * @param checked the new checked state
   * @return this
   */
  public CheckMenuItem setChecked(boolean checked) {
    return setChecked(checked, false);
  }

  /**
   * Set the checked state of this item.
   * 
   * @param state the new checked state
   * @param supressEvent true to prevent the checkchange event from firing
   * @return this
   */
  public CheckMenuItem setChecked(boolean state, boolean supressEvent) {
    MenuEvent me = new MenuEvent(parentMenu);
    me.item = this;
    if (fireEvent(Events.BeforeCheckChange, me)) {
      if (el.getParent() != null) {
        el.getParent().setStyleName("x-menu-item-checked", state);
      }
      checked = state;
      if (!supressEvent) {
        fireEvent(Events.CheckChange, me);
      }
    }
    return this;
  }

  protected void onClick(ComponentEvent ce) {
    if (!disabled && group == null) {
      setChecked(!checked);
    }
    if (!disabled && !checked && group != null) {
      setChecked(!checked);
      onRadioClick(ce);
    }
    super.onClick(ce);
  }

  protected void onRadioClick(ComponentEvent ce) {
    if (parentMenu != null) {
      for (MenuItem item : parentMenu.getItems()) {
        if (item instanceof CheckMenuItem) {
          CheckMenuItem check = (CheckMenuItem) item;
          if (check != this && check.isChecked()) {
            check.setChecked(false);
          }
        }
      }
    }
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    if (group != null) {
      el.addStyleName(groupStyle);
    }
    if (checked) {
      setChecked(true, true);
    }
  }

}
