/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A base class for all menu items that require menu-related functionality (like
 * sub-menus) and are not static display items. Item extends the base
 * functionality of {@link MenuItem} by adding menu-specific activation and
 * click handling.
 */
public class Item extends MenuItem {

  protected Menu subMenu;
  protected String itemStyle = "x-menu-item";
  protected String iconStyle;
  protected String text;

  /**
   * Creates a new item.
   */
  public Item() {
    canActivate = true;
  }

  /**
   * Creates a new item with the given text.
   * 
   * @param text the item's text
   */
  public Item(String text) {
    this.text = text;
  }

  /**
   * Expands the item's sub menu.
   */
  public void expandMenu() {
    if (isEnabled() && subMenu != null) {
      subMenu.show(el.dom, "tl-tr?");
    }
  }

  /**
   * Returns the item's icon style.
   * 
   * @return the icon style
   */
  public String getIconStyle() {
    return iconStyle;
  }

  /**
   * Returns the item's sub menu.
   * 
   * @return the sub menu
   */
  public Menu getSubMenu() {
    return subMenu;
  }

  /**
   * Returns the item's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the item's icon style.
   * 
   * @param iconStyle the icon style
   */
  public void setIconStyle(String iconStyle) {
    String oldStyle = this.iconStyle;
    this.iconStyle = iconStyle;
    if (rendered) {
      el.child("img.x-menu-item-icon").replaceStyleName(oldStyle, iconStyle);
    }
  }

  /**
   * Sets the item's sub menu.
   * 
   * @param menu the sub menu
   */
  public void setSubMenu(Menu menu) {
    this.subMenu = menu;
    menu.parentItem = this;
  }

  /**
   * Sets the item's text.
   * 
   * @param text the text
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      String img = "<img src='{0}' class='x-menu-item-icon {2}' />{1}";
      String[] vals = new String[3];
      vals[0] = GXT.BLANK_IMAGE_URL;
      vals[1] = text;
      vals[2] = iconStyle;
      img = Format.substitute(img, (Object[]) vals);
      el.setInnerHtml(img);
    }
  }

  @Override
  protected void activate(boolean autoExpand) {
    super.activate(autoExpand);
    if (autoExpand && subMenu != null) {
      expandMenu();
    }
  }

  @Override
  protected void afterRender() {
    super.afterRender();
    if (text != null) {
      setText(text);
    }
  }

  @Override
  protected void deactivate() {
    super.deactivate();
    if (subMenu != null && subMenu.isVisible()) {
      subMenu.hide();
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createAnchor(), target, index);

    el.setElementAttribute("href", "#");
    String s = itemStyle + (subMenu != null ? " x-menu-item-arrow" : "");
    setStyleName(s);

    String img = "<img src='{0}' class='x-menu-item-icon {1}' />{2}";
    img = Format.substitute(img, GXT.BLANK_IMAGE_URL, iconStyle, text);
    el.update(img);
  }

  @Override
  protected boolean shouldDeactivate(ComponentEvent ce) {
    if (super.shouldDeactivate(ce)) {
      if (subMenu != null && subMenu.isVisible()) {
        return !subMenu.el.getBounds().contains(ce.getXY());
      }
    }
    return true;
  }

}
