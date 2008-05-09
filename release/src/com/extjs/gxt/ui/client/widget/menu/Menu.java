/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.AbstractContainer;
import com.extjs.gxt.ui.client.widget.Shadow.ShadowPosition;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A menu component.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeShow</b> : MenuEvent(menu)<br>
 * <div>Fires before this menu is displayed. Listener can set the doit field to
 * <code>false</code> to cancel the menu being displayed.</div>
 * <ul>
 * <li>menu : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Show</b> : MenuEvent(menu)<br>
 * <div>Fires after this menu is displayed.</div>
 * <ul>
 * <li>menu : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeHide</b> : MenuEvent(menu)<br>
 * <div>Fired before the menu is hidden. Listener can set the doit field to
 * <code>false</code> to cancel the menu being hidden.</div>
 * <ul>
 * <li>menu : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Hide</b> : MenuEvent(menu)<br>
 * <div>Fires after this menu is hidden.</div>
 * <ul>
 * <li>menu : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeAdd</b> : MenuEvent(menu, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>menu : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : MenuEvent(menu, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>menu : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : MenuEvent(menu, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>menu : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : MenuEvent(menu, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>menu : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public class Menu extends AbstractContainer<MenuItem> {

  KeyNav keyNav;
  MenuItem parentItem;
  
  private String subMenuAlign = "tl-tr-?";
  private String defaultAlign = "tl-bl?";
  private ShadowPosition shadowPosition = ShadowPosition.SIDES;
  private int minWidth = 120;
  private MenuItem activeItem;
  private Menu parentMenu;
  private boolean showing;
  private String menuList;
  private String menuListItem;
  private Element ul;
  private BaseEventPreview eventPreview;

  /**
   * Creates a new menu.
   */
  public Menu() {
    baseStyle = "x-menu";
    setShadow(true);
    attachChildren = false;
    eventPreview = new BaseEventPreview() {
      @Override
      protected boolean onAutoHide(PreviewEvent pe) {
        hide(true);
        return true;
      }
    };
    eventPreview.setAutoHideCancelEvent(false);
  }

  /**
   * Adds a item to the menu.
   * 
   * @param item the new item
   */
  public void add(MenuItem item) {
    insert(item, getItemCount());
  }

  /**
   * Returns the default alignment.
   * 
   * @return the default align
   */
  public String getDefaultAlign() {
    return defaultAlign;
  }

  /**
   * Returns the menu's minimum width.
   * 
   * @return the width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns the menu's parent item.
   * 
   * @return the parent item
   */
  public MenuItem getParentItem() {
    return parentItem;
  }

  /**
   * Returns the shadow position.
   * 
   * @return the shadow position
   */
  public ShadowPosition getShadowPosition() {
    return shadowPosition;
  }

  /**
   * Returns the sub menu alignment.
   * 
   * @return the alignment
   */
  public String getSubMenuAlign() {
    return subMenuAlign;
  }

  /**
   * Hides the menu.
   */
  public void hide() {
    hide(false);
  }

  /**
   * Hides this menu and optionally all parent menus
   * 
   * @param deep true to close all parent menus
   * @return this
   */
  public Menu hide(boolean deep) {
    if (showing) {
      MenuEvent me = new MenuEvent(this);
      if (fireEvent(Events.BeforeHide, me)) {
        if (activeItem != null) {
          activeItem.deactivate();
          activeItem = null;
        }
        onHide();
        RootPanel.get().remove(this);
        eventPreview.remove();
        showing = false;
        hidden = true;
        fireEvent(Events.Hide, me);
      }
      if (deep && parentItem != null) {
        parentItem.parentMenu.hide(true);
      }
    }
    return this;
  }

  /**
   * Inserts an item into the menu.
   * 
   * @param item the item to insert
   * @param index the insert location
   */
  public void insert(MenuItem item, int index) {
    MenuEvent me = new MenuEvent(this);
    me.item = item;
    me.index = index;
    if (fireEvent(Events.BeforeAdd, me)) {
      item.parentMenu = this;
      super.insert(item, index);
      if (rendered) {
        renderItem(item, index);
      }
      fireEvent(Events.Add, me);
    }
  }

  @Override
  public boolean isVisible() {
    return showing;
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    switch (ce.type) {
      case Event.ONCLICK:
        onClick(ce);
        break;
      case Event.ONMOUSEOVER:
        onMouseOver(ce);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(ce);
        break;
    }
  }

  /**
   * Removes a item from the menu.
   * 
   * @param item the menu to remove
   */
  public boolean remove(MenuItem item) {
    MenuEvent me = new MenuEvent(this);
    me.item = item;
    if (fireEvent(Events.BeforeRemove, me)) {
      super.remove(item);
      fireEvent(Events.Remove, me);
      return true;
    }
    return false;
  }

  /**
   * Removes all menu items from this menu.
   */
  public void removeAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Sets the default {@link El#alignTo} anchor position value for this menu
   * relative to its element of origin (defaults to "tl-bl?").
   * 
   * @param defaultAlign the default align
   */
  public void setDefaultAlign(String defaultAlign) {
    this.defaultAlign = defaultAlign;
  }

  /**
   * Sets he minimum width of the menu in pixels (defaults to 120).
   * 
   * @param minWidth the min width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * Sets the shadow position (defaults to SIDES).
   * 
   * @param shadowPosition the position
   */
  public void setShadowPosition(ShadowPosition shadowPosition) {
    this.shadowPosition = shadowPosition;
  }

  /**
   * The {@link El#alignTo} anchor position value to use for submenus of this
   * menu (defaults to "tl-tr-?").
   * 
   * @param subMenuAlign the sub alignment
   */
  public void setSubMenuAlign(String subMenuAlign) {
    this.subMenuAlign = subMenuAlign;
  }

  /**
   * Displays this menu relative to another element.
   * 
   * @param elem the element to align to
   * @param pos the {@link El#alignTo} anchor position to use in aligning to the
   *            element (defaults to defaultAlign)
   * @return this;
   */
  public Menu show(Element elem, String pos) {
    MenuEvent me = new MenuEvent(this);
    if (fireEvent(Events.BeforeShow, me)) {
      RootPanel.get().add(this);
      onShow();
      el.updateZIndex(0);
      el.makePositionable(true);
      el.alignTo(elem, pos, new int[] {0, 0});
      eventPreview.add();
      showing = true;
      focus();
      fireEvent(Events.Show, me);

    }
    return this;
  }

  public Menu show(Widget widget) {
    return show(widget.getElement(), getDefaultAlign());
  }

  /**
   * Displays this menu at a specific xy position.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void showAt(int x, int y) {
    MenuEvent me = new MenuEvent(this);
    if (fireEvent(Events.BeforeShow, me)) {
      if (!rendered) {
        render(XDOM.getBody());
      }
      RootPanel.get().add(this);
      onShow();
      setPagePosition(x, y);
      eventPreview.add();
      showing = true;
      focus();
      fireEvent(Events.Show, me);
    }
  }

  protected void afterRender() {
    super.afterRender();
    autoWidth();
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new MenuEvent(this);
  }

  protected void createStyles(String baseStyle) {
    menuList = baseStyle + "-list";
    menuListItem = baseStyle + "-list-item";
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(XDOM.create("<div style='position:absolute'></div>"), target, index);

    keyNav = new KeyNav(this) {
      public void onDown(ComponentEvent be) {
        if (tryActivate(indexOf(activeItem) + 1, 1) == null) {
          tryActivate(0, 1);
        }
      }

      public void onEnter(ComponentEvent be) {
        if (activeItem != null) {
          be.cancelBubble();
          activeItem.onClick(be);
        }
      }

      public void onLeft(ComponentEvent be) {
        hide();
        if (parentMenu != null && parentMenu.activeItem != null) {
          parentMenu.activeItem.activate(false);
        }
      }

      public void onRight(ComponentEvent be) {
        if (activeItem != null) {
          activeItem.expandMenu(true);
        }
      }

      public void onUp(ComponentEvent be) {
        if (tryActivate(indexOf(activeItem) - 1, -1) == null) {
          tryActivate(getItemCount() - 1, -1);
        }
      }
    };

    ul = DOM.createElement("ul");
    fly(ul).setStyleName(menuList);
    el.appendChild(ul);

    renderAll();

    // add menu to ignore list
    eventPreview.getIgnoreList().add(getElement());

    autoWidth();
    el.addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
  }

  protected void renderAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      MenuItem item = getItem(i);
      renderItem(item, i);
    }
  }

  protected void renderItem(MenuItem item, int index) {
    Element li = DOM.createElement("li");
    fly(ul).insertChild(li, index);
    fly(li).setStyleName(menuListItem);
    item.render(li);
  }

  protected void setActiveItem(MenuItem item, boolean autoExpand) {
    if (item != activeItem) {
      if (activeItem != null) {
        activeItem.deactivate();
      }
      this.activeItem = item;
      item.activate(autoExpand);
    } else if (autoExpand) {
      item.expandMenu(autoExpand);
    }
  }

  private void autoWidth() {
    if (rendered) {
      if (GXT.isIE) {
        el.setWidth(getMinWidth());
        getWidth();
        el.setWidth(fly(ul).getWidth() + el.getFrameWidth("lr"));
      } else {
        setWidth(getMinWidth());
      }
    }
  }

  private void onClick(ComponentEvent ce) {
    ce.stopEvent();
    MenuItem item = findItem(ce.getTarget());
    if (item != null) {
      item.onClick(ce);
    }
  }

  private void onMouseOut(ComponentEvent ce) {
    MenuItem item = findItem(ce.getTarget());
    if (item != null) {
      if (item == activeItem && activeItem.shouldDeactivate(ce)) {
        activeItem.deactivate();
        activeItem = null;
      }
    }
  }

  private void onMouseOver(ComponentEvent ce) {
    MenuItem item = findItem(ce.getTarget());
    if (item != null) {
      if (item.canActivate && item.isEnabled()) {
        setActiveItem(item, true);
      }
    }
  }

  private MenuItem tryActivate(int start, int step) {
    for (int i = start, len = getItemCount(); i >= 0 && i < len; i += step) {
      MenuItem item = getItem(i);
      if (item.isEnabled() && item.canActivate) {
        setActiveItem(item, false);
        return item;
      }
    }
    return null;
  }
}
