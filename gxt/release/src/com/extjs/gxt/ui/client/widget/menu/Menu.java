/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.Container;
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
 * <dd><b>BeforeShow</b> : MenuEvent(container)<br>
 * <div>Fires before this menu is displayed. Listener can set the doit field to
 * <code>false</code> to cancel the menu being displayed.</div>
 * <ul>
 * <li>container : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Show</b> : MenuEvent(container)<br>
 * <div>Fires after this menu is displayed.</div>
 * <ul>
 * <li>container : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeHide</b> : MenuEvent(container)<br>
 * <div>Fired before the menu is hidden. Listener can set the doit field to
 * <code>false</code> to cancel the menu being hidden.</div>
 * <ul>
 * <li>container : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Hide</b> : MenuEvent(container)<br>
 * <div>Fires after this menu is hidden.</div>
 * <ul>
 * <li>container : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeAdd</b> : MenuEvent(container, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : MenuEvent(container, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : MenuEvent(container, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : MenuEvent(container, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 */
public class Menu extends Container<Item> {

  protected KeyNav keyNav;
  protected Item parentItem;
  protected BaseEventPreview eventPreview;

  private String subMenuAlign = "tl-tr?";
  private String defaultAlign = "tl-bl?";
  private ShadowPosition shadowPosition = ShadowPosition.SIDES;
  private int minWidth = 120;
  private Item activeItem;
  private Menu parentMenu;
  private boolean showing;
  private String menuList;
  private String menuListItem;
  private Element ul;
  private boolean constrainViewport = true;

  /**
   * Creates a new menu.
   */
  public Menu() {
    baseStyle = "x-menu";
    shim = true;
    setShadow(true);
    attachChildren = false;
    setAutoWidth(true);
    eventPreview = new BaseEventPreview() {
      @Override
      protected boolean onAutoHide(PreviewEvent pe) {
        return Menu.this.onAutoHide(pe);
      }
    };
    eventPreview.setAutoHideCancelEvent(false);
  }

  /**
   * Adds a item to the menu.
   * 
   * @param item the new item
   */
  @Override
  public boolean add(Item item) {
    return super.add(item);
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
  public Item getParentItem() {
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
  @Override
  public boolean insert(Item item, int index) {
    boolean added = super.insert(item, index);
    if (added) {
      item.parentMenu = this;
      if (rendered) {
        renderItem(item, index);
      }
    }
    return added;
  }

  /**
   * Returns true if constrain to viewport is enabled.
   * 
   * @return the contstrain to viewport state
   */
  public boolean isConstrainViewport() {
    return constrainViewport;
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
  @Override
  public boolean remove(Item item) {
    return super.remove(item);
  }

  /**
   * Sets whether the menu should be constrained to the viewport when shown.
   * Only applies when using {@link #showAt(int, int)}.
   * 
   * @param constrainViewport true to contrain
   */
  public void setConstrainViewport(boolean constrainViewport) {
    this.constrainViewport = constrainViewport;
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
   *          element (defaults to defaultAlign)
   */
  public void show(Element elem, String pos) {
    MenuEvent me = new MenuEvent(this);
    if (fireEvent(Events.BeforeShow, me)) {
      RootPanel.get().add(this);
      showing = true;
      el().makePositionable(true);

      onShow();
      el().alignTo(elem, pos, new int[] {0, 0});

      eventPreview.add();
      focus();
      fireEvent(Events.Show, me);
    }
  }

  /**
   * Displays this menu relative to the widget using the default alignment.
   * 
   * @param widget the align widget
   */
  public void show(Widget widget) {
    show(widget.getElement(), defaultAlign);
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
      RootPanel.get().add(this);
      el().makePositionable(true);

      // add menu to ignore list
      eventPreview.getIgnoreList().add(getElement());

      onShow();
      setPagePosition(x, y);

      if (constrainViewport) {
        int ch = XDOM.getViewportSize().height;
        int cw = XDOM.getViewportSize().width;
        int bottom = el().getBottom(false);
        int right = el().getRight(false);
        if (bottom > ch) {
          y -= (bottom - ch) - 3;
        }
        if (right > cw) {
          x -= (right - cw) - 3;
        }
        setPagePosition(x, y);
      }

      eventPreview.add();
      showing = true;
      focus();
      fireEvent(Events.Show, me);
    }
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new MenuEvent(this);
  }

  @Override
  protected ContainerEvent createContainerEvent(Item item) {
    return new MenuEvent(this, item);
  }

  protected void createStyles(String baseStyle) {
    menuList = baseStyle + "-list";
    menuListItem = baseStyle + "-list-item";
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    for (Item item : getItems()) {
      if (item instanceof AdapterMenuItem) {
        ComponentHelper.doAttach(((AdapterMenuItem) item).widget);
      }
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    for (Item item : getItems()) {
      if (item instanceof AdapterMenuItem) {
        ComponentHelper.doDetach(((AdapterMenuItem) item).widget);
      }
    }
  }

  protected boolean onAutoHide(PreviewEvent pe) {
    // in some cases the double click target element is body
    // which causes the menu to be hidden, so check xy
    Point p = BaseEventPreview.getLastXY();
    if (getBounds(false).contains(p)) {
      return false;
    }
    hide(true);
    return true;
  }

  @Override
  protected void onInsert(Item item, int index) {
    super.onInsert(item, index);
    autoWidth();
  }

  @Override
  protected void onRemove(Item item) {
    if (item.isRendered()) {
      Element li = item.getElement().getParentElement().cast();
      ul.removeChild(li);
    }
    super.onRemove(item);
    autoWidth();
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
    ul.setClassName(menuList);
    getElement().appendChild(ul);

    renderAll();

    // add menu to ignore list
    eventPreview.getIgnoreList().add(getElement());

    autoWidth();
    el().addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
  }

  @Override
  protected void onShow() {
    super.onShow();
    el().updateZIndex(0);
    autoWidth();
  }

  protected void renderAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      Item item = getItem(i);
      renderItem(item, i);
    }
  }

  protected void renderItem(Item item, int index) {
    Element li = DOM.createElement("li");
    fly(ul).insertChild(li, index);
    li.setClassName(menuListItem);
    item.render(li);
  }

  protected void setActiveItem(Item item, boolean autoExpand) {
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
      setWidth("auto");
      int w = fly(ul).getWidth();
      w = Math.max(minWidth, w);
      w += el().getFrameWidth("lr");
      el().setWidth(w);
    }
  }

  private void onClick(ComponentEvent ce) {
    ce.stopEvent();
    Item item = findItem(ce.getTarget());
    if (item != null) {
      item.onClick(ce);
    }
  }

  private void onMouseOut(ComponentEvent ce) {
    Item item = findItem(ce.getTarget());
    if (item != null) {
      if (item == activeItem && !ce.within(getElement()) && activeItem.shouldDeactivate(ce)) {
        activeItem.deactivate();
        activeItem = null;
      }
    } else {
      if (activeItem != null && activeItem.shouldDeactivate(ce)) {
        activeItem.deactivate();
        activeItem = null;
      }
    }
  }

  private void onMouseOver(ComponentEvent ce) {
    Item item = findItem(ce.getTarget());
    if (item != null) {
      if (item.canActivate && item.isEnabled()) {
        setActiveItem(item, true);
      }
    }
  }

  private Item tryActivate(int start, int step) {
    for (int i = start, len = getItemCount(); i >= 0 && i < len; i += step) {
      Item item = getItem(i);
      if (item.isEnabled() && item.canActivate) {
        setActiveItem(item, false);
        return item;
      }
    }
    return null;
  }
}
