/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DataListEvent;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.selection.AbstractListSelectionModel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * Displays a list of list items.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : DateListEvent(dataList, item, index)<br>
 * <div>Fires before an item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : DateListEvent(dataList, item)<br>
 * <div>Fires before an item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : DateListEvent(dataList, item, index)<br>
 * <div>Fires after an item has been added or inserted.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : DateListEvent(dataList, item)<br>
 * <div>Fires after an item has been removed.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>item : the item that was removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SelectionChange</b> : DateListEvent(dataList, item)<br>
 * <div>Fires after the selection changes.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>item : the item that is selected (single select)</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : DateListEvent(dataList)<br>
 * <div>Fires before the list's context menu is shown. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>menu : menu</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : DateListEvent(dataList, item)<br>
 * <div>Fires after an item is selected.</div>
 * <ul>
 * <li>dataList : this</li>
 * <li>item : the item that is selected (single select)</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-list (the list itself)</dd>
 * <dd>.my-listitem (list item)</dd>
 * <dd>.my-listitem .my-listitem-text (list item text)</dd>
 * </dl>
 */
public class DataList extends ScrollContainer<DataListItem> {

  /**
   * The default template for data list items.
   */
  public static Template defaultItemTemplate;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<table id='{id}' class='{style}' cellpadding=0 cellspacing=0><tbody><tr>");
    sb.append("<td class='{style}-l'><div>&nbsp;</div></td>");
    sb.append("<td class='{style}-icon' style='{icon}'><div class='x-icon-btn {iconStyle}'></div></td>");
    sb.append("<td class='{style}-c'><span class='{style}-text'>{text}</span></td>");
    sb.append("<td class='{style}-r'><div>&nbsp;</div></td>");
    sb.append("</tr></tbody></table>");
    defaultItemTemplate = new Template(sb.toString());
    defaultItemTemplate.compile();
  }

  /**
   * The max number of parent nodes to search in {@link #findItem(Element)}
   * (defaults to 15).
   */
  protected int maxDepth = 15;

  private boolean flat;
  private boolean checkable;
  private Template itemTemplate;
  private El inner;
  private String itemStyle;
  private List<DataListItem> checked;
  private DataListSelectionModel sm;

  /**
   * Creates a new single select list.
   */
  public DataList() {
    focusable = true;
    baseStyle = "my-list";
    attachChildren = false;
    setScrollMode(Scroll.AUTO);
    sm = new DataListSelectionModel(this);
    sm.bind(this);
  }

  /**
   * Adds an item to the list. Fires the <i>BeforeAdd</i> event before
   * inserting, then fires the <i>Add</i> event after the widget has been
   * inserted.
   * 
   * @param item the item to add
   */
  public void add(DataListItem item) {
    insert(item, getItemCount());
  }

  /**
   * Creates then adds an item to the list. Fires the <i>BeforeAdd</i> event
   * before inserting, then fires the <i>Add</i> event after the widget has
   * been inserted.
   * 
   * @param text the item's text
   * @return the newly created item
   */
  public DataListItem add(String text) {
    DataListItem item = new DataListItem(text);
    add(item);
    return item;
  }

  /**
   * Deselects the item(s).
   * 
   * @param items the item(s)
   */
  public void deselect(DataListItem... items) {
    sm.deselect(items);
  }

  /**
   * Deselects the item(s).
   * 
   * @param start the start index
   * @param end the end index
   */
  public void deselect(int start, int end) {
    sm.deselect(start, end);
  }

  /**
   * Deselects the item(s).
   * 
   * @param items the item(s)
   */
  public void deselect(List<DataListItem> items) {
    sm.deselect(items);
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    sm.deselectAll();
  }

  /**
   * Returns an array of checked items.
   * 
   * @return the checked items
   */
  public List<DataListItem> getChecked() {
    return new ArrayList<DataListItem>(checked);
  }

  @Override
  public Menu getContextMenu() {
    return super.getContextMenu();
  }

  /**
   * Returns the selected item. If the list is multi-select, returns the first
   * selected item.
   * 
   * @return the item or <code>null</code> if no selections
   */
  public DataListItem getSelectedItem() {
    return sm.getSelectedItem();
  }

  /**
   * Returns the selected items.
   * 
   * @return the selected items
   */
  public List<DataListItem> getSelectedItems() {
    return sm.getSelectedItems();
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public SelectionMode getSelectionMode() {
    return sm.getSelectionMode();
  }

  /**
   * Inserts an item into the list at the given index. Fires the <i>BeforeAdd</i>
   * event before inserting, then fires the <i>Add</i> event after the widget
   * has been inserted.
   * 
   * @param item the item
   * @param index the insert location
   */
  public void insert(DataListItem item, int index) {
    DataListEvent dle = new DataListEvent(this);
    dle.item = item;
    dle.index = index;
    if (fireEvent(Events.BeforeAdd, dle)) {
      item.list = this;
      if (checkable) {
        // item.markup = Markup.ITEM_CHECK;
      }
      super.insert(item, index);
      if (rendered) {
        item.render(inner.dom, index);
      }
      fireEvent(Events.Add, dle);
    }
  }

  /**
   * Returns true if check boxes are enabled.
   * 
   * @return the check box state
   */
  public boolean isCheckable() {
    return checkable;
  }

  /**
   * Returns true if the list is using the "flat" style.
   * 
   * @return the flat state
   */
  public boolean isFlat() {
    return flat;
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @param item the item
   * @return the select state
   */
  public boolean isSelected(DataListItem item) {
    return sm.isSelected(item);
  }

  /**
   * Moves the current selections down one level.
   */
  public void moveSelectedDown() {
    List<DataListItem> selected = sm.getSelectedItems();
    if (selected.size() == 0) {
      return;
    }

    Collections.sort(selected, new Comparator<DataListItem>() {
      public int compare(DataListItem li1, DataListItem li2) {
        return indexOf(li1) < indexOf(li2) ? 1 : 0;
      }
    });

    for (DataListItem item : selected) {
      int index = indexOf(item);
      if (index != (getItemCount() - 1)) {
        remove(item);
        insert(item, ++index);
      }
    }
    doSelect(sm, new Items(selected));

    fireEvent(Events.SelectionChange, new DataListEvent(this));
  }

  /**
   * Moves the current selections up one level.
   */
  public void moveSelectedUp() {
    List<DataListItem> selected = sm.getSelectedItems();
    if (selected.size() == 0) {
      return;
    }

    Collections.sort(selected, new Comparator<DataListItem>() {
      public int compare(DataListItem o1, DataListItem o2) {
        return indexOf(o1) > indexOf(o2) ? 1 : 0;
      }
    });
    for (DataListItem item : selected) {
      int index = indexOf(item);
      if (index > 0) {
        remove(item);
        insert(item, --index);
      }
    }
    doSelect(sm, new Items(selected));
    fireEvent(Events.SelectionChange, new DataListEvent(this));
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    DataListItem item = findItem(ce.getTarget());
    if (item != null) {
      DataListEvent dle = (DataListEvent) ce;
      switch (ce.type) {
        case Event.ONMOUSEOVER:
          onOverChange(item, true, dle);
          break;
        case Event.ONMOUSEOUT:
          onOverChange(item, false, dle);
          break;
      }
    }
  }

  /**
   * Removes the item from the list.
   * 
   * @param item the item to be removed
   * @return true if the item was removed
   */
  public boolean remove(DataListItem item) {
    DataListEvent dle = new DataListEvent(this);
    dle.item = item;
    if (fireEvent(Events.BeforeRemove, dle)) {
      item.list = null;
      boolean result = super.remove(item);
      fireEvent(Events.Remove, dle);
      return result;
    }
    return false;
  }

  /**
   * Removes all the items from the list.
   */
  public void removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Scrolls the item into view.
   * 
   * @param item the item
   */
  public void scrollIntoView(DataListItem item) {
    item.el.scrollIntoView(inner.dom, false);
  }

  /**
   * Selects the item(s).
   * 
   * @param items the item(s) to select
   */
  public void select(DataListItem... items) {
    sm.select(items);
  }

  /**
   * Selects the item(s).
   * 
   * @param start the start index
   * @param end the end index
   */
  public void select(int start, int end) {
    sm.select(start, end);
  }

  /**
   * Selects the item(s).
   * 
   * @param items the item(s) to select
   */
  public void select(List<DataListItem> items) {
    sm.select(items);
  }

  /**
   * Selects all items.
   */
  public void selectAll() {
    sm.select(0, getItemCount());
  }

  /**
   * Sets whether items shoud have a check box (defaults to false, pre-render).
   * 
   * @param checkable true to enable checbox
   */
  public void setCheckable(boolean checkable) {
    this.checkable = checkable;
  }

  @Override
  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  /**
   * Sets whether the list should use a "flat" style without rounded corners
   * (defaults to false, pre-render). The flat style supports variable height list items.
   * 
   * @param flat the flat state
   */
  public void setFlatStyle(boolean flat) {
    this.flat = flat;
  }

  /**
   * Sets the optional template to be used by the data list items (pre-render). The custom
   * template will be rendered with the following parameters: id, style,
   * iconStyle, text.
   * 
   * @param itemTemplate the template
   */
  public void setItemTemplate(Template itemTemplate) {
    this.itemTemplate = itemTemplate;
  }

  /**
   * Sets the list's selection mode (defaults {@link SelectionMode#SINGLE}).
   * 
   * @param selectionMode the selection mode
   */
  public void setSelectionMode(SelectionMode selectionMode) {
    sm.setSelectionMode(selectionMode);
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    Element target = DOM.eventGetTarget(event);
    return new DataListEvent(this, findItem(target));
  }

  @Override
  protected void createStyles(String baseStyle) {
    if (itemStyle == null) {
      itemStyle = baseStyle + "-item";
    }
  }

  @Override
  protected El getLayoutTarget() {
    return inner;
  }

  protected void onCheckChange(DataListItem item, boolean checked) {
    String s = checked ? "icon-checked" : "icon-notchecked";
    item.checkBtn.changeStyle(s);
    if (checked) {
      this.checked.add(item);
    } else {
      this.checked.remove(item);
    }
  }

  protected void onKeyPress(DataListEvent e) {
    fireEvent(Events.KeyPress, e);
  }

  protected void onOverChange(DataListItem item, boolean over, DataListEvent e) {
    item.el.setStyleName(itemStyle + "-over", over);
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv());
    el.insertInto(target, index);

    inner = new El(DOM.createDiv());
    el.appendChild(inner.dom);

    inner.setStyleName(baseStyle + "-inner");

    if (flat) {
      setStyleName(baseStyle + "-flat");
    } else {
      setStyleName(baseStyle);
    }

    setScrollMode(getScrollMode());
    disableTextSelection(true);

    new KeyNav<DataListEvent>(this) {

      @Override
      public void onDown(DataListEvent e) {
        DataList.this.onKeyPress(e);
      }

      @Override
      public void onUp(DataListEvent e) {
        DataList.this.onKeyPress(e);
      }
    };

    if (itemTemplate == null) {
      itemTemplate = defaultItemTemplate;
    }

    renderAll();

    el.addEventsSunk(Event.ONCLICK | Event.ONDBLCLICK | Event.KEYEVENTS
        | Event.MOUSEEVENTS);
  }

  protected void onRenderItem(DataListItem item, Element target, int index) {
    Params p = new Params();
    p.set("style", itemStyle);
    p.set("iconStyle", item.getIconStyle());
    p.set("icon", item.getIconStyle() != null ? "" : "display: none");
    p.set("text", item.getText());
    p.set("id", item.getId());
    item.setElement(itemTemplate.create(p), target, index);

    if (!GXT.isIE) {
      item.el.setTabIndex(0);
    }

    if (checkable) {
      item.checkBtn = new IconButton("icon-notchecked");
      item.checkBtn.setStyleAttribute("marginRight", "4px");
      Element elem = el.selectNode(".my-listitem-check").dom;
      DOM.appendChild(elem, item.checkBtn.getElement());

      if (item.isChecked()) {
        item.setChecked(true);
      }
    }
  }

  protected void onResize(int width, int height) {
    if (height != Style.DEFAULT) {
      height -= el.getBorderWidth("tb");
      height -= 2;// inner padding
      inner.setHeight(height, true);
    }
    if (width != Style.DEFAULT) {
      width -= el.getBorderWidth("lr");
      width -= 2;// inner padding
      inner.setWidth(width, true);
    }
  }

  @Override
  protected void onRightClick(ComponentEvent ce) {
    ce.cancelBubble();
    DataListItem item = findItem(ce.getTarget());
    if (item != null) {
      select(item);
    }
    super.onRightClick(ce);
  }

  protected void renderAll() {
    int ct = getItemCount();
    for (int i = 0; i < ct; i++) {
      DataListItem item = getItem(i);
      item.render(inner.dom);
    }
  }

  void onSelectChange(DataListItem item, boolean select) {
    if (select) {
      item.removeStyleName(itemStyle + "-over");
      item.addStyleName(itemStyle + "-sel");
    } else {
      item.removeStyleName(itemStyle + "-sel");
    }
  }

  private native void doSelect(AbstractListSelectionModel sm, Items items) /*-{
     sm.@com.extjs.gxt.ui.client.widget.selection.AbstractListSelectionModel::doSelect(Lcom/extjs/gxt/ui/client/widget/Items;ZZ)(items, false, false);
   }-*/;
}
