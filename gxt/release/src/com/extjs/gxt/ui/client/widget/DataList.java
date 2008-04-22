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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DataListEvent;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Displays a list of list items.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : DateListEvent(component, item, index)<br>
 * <div>Fires before an item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
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
 * <dd><b>ContextMenu</b> : ComponentEvent(component)<br>
 * <div>Fires before the list's context menu is shown. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
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
public class DataList<T extends DataListItem> extends ScrollContainer<T> {

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
   * True for a check box tree (defaults to false).
   */
  public boolean checkable;

  /**
   * True to display the list item's without rounded corners (defaults to
   * false). The flat style supports variable height list items.
   */
  public boolean flat;

  /**
   * The optional list item template (defaults to null). The custom template
   * will be rendered with the following parameters: id, style, iconStyle, text.
   */
  public Template itemTemplate;

  /**
   * Sets the tree selection mode (defaults to SINGLE). Valid values are SINGLE
   * and MULTI.
   */
  public SelectionMode selectionMode = SelectionMode.SINGLE;

  /**
   * The max number of parent nodes to search in {@link #findItem(Element)}
   * (defaults to 15).
   */
  protected int maxDepth = 15;

  private DataListItem hoverItem;
  private El inner;
  private String itemStyle;
  private DataListItem lastSelected;
  private Map<String, T> nodes = new HashMap<String, T>();
  private List<T> selected;
  private boolean singleSelect = true;
  private boolean multiSelect;
  private boolean initialzied;

  /**
   * Creates a new single select list.
   */
  public DataList() {
    focusable = true;
    baseStyle = "my-list";
    attachChildren = false;
    selected = new ArrayList<T>();
    setScrollMode(Scroll.AUTO);
  }

  /**
   * Adds an item to the list. Fires the <i>BeforeAdd</i> event before
   * inserting, then fires the <i>Add</i> event after the widget has been
   * inserted.
   * 
   * @param item the item to add
   */
  public void add(T item) {
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
    add((T) item);
    return item;
  }

  /**
   * Deselects the item at the given index.
   * 
   * @param item the item to deselect
   */
  public void deselect(DataListItem item) {
    deselect(indexOf(item));
  }

  /**
   * Deselects the item at the given index.
   * 
   * @param index the index of the item to deselect
   */
  public void deselect(int index) {
    selectItems(index, index, false, true);
  }

  /**
   * Deselects the items at the given indices.
   * 
   * @param start the start index
   * @param end the end index
   */
  public void deselect(int start, int end) {
    selectItems(start, end, false, true);
  }

  /**
   * Deselects all selected items.
   */
  public void deselectAll() {
    if (getItemCount() > 0) {
      selectItems(0, getItemCount() - 1, false, false);
    }
  }

  /**
   * Returns the item using the specified target.
   * 
   * @param element the element or child element
   * @return the item
   */
  public T findItem(Element element) {
    if (getItemCount() > 0) {
      El elem = fly(element).findParent("." + itemStyle, maxDepth);
      if (elem != null) {
        return nodes.get(elem.getId());
      }
    }
    return null;
  }

  /**
   * Returns an array of checked items.
   * 
   * @return the checked items
   */
  public List<DataListItem> getChecked() {
    List<DataListItem> temp = new ArrayList<DataListItem>();
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      if (getItem(i).isChecked()) {
        temp.add(getItem(i));
      }
    }
    return temp;
  }

  public Menu getContextMenu() {
    return super.getContextMenu();
  }

  public El getLayoutTarget() {
    return inner;
  }

  /**
   * Returns the selected item. If the list is multi-select, returns the first
   * selected item.
   * 
   * @return the item or <code>null</code> if no selections
   */
  public DataListItem getSelectedItem() {
    if (selected.size() > 0) {
      return getSelection().get(0);
    }
    return null;
  }

  /**
   * Returns the selected items.
   * 
   * @return the selected items
   */
  public List<T> getSelection() {
    return new ArrayList<T>(selected);
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public SelectionMode getSelectionMode() {
    return selectionMode;
  }

  /**
   * Returns the index of the item or -1 if not found.
   * 
   * @param item the item
   * @return the index
   */
  public int indexOf(DataListItem item) {
    return items.indexOf(item);
  }

  /**
   * Inserts an item into the list at the given index. Fires the <i>BeforeAdd</i>
   * event before inserting, then fires the <i>Add</i> event after the widget
   * has been inserted.
   * 
   * @param item the item
   * @param index the insert location
   */
  public void insert(T item, int index) {
    DataListEvent dle = new DataListEvent(this);
    dle.item = item;
    dle.index = index;
    if (fireEvent(Events.BeforeAdd, dle)) {
      item.list = this;
      if (checkable) {
        // item.markup = Markup.ITEM_CHECK;
      }
      items.add(index, item);
      if (rendered) {
        item.render(inner.dom, index);
      }
      register(item);
      fireEvent(Events.Add, dle);
    }
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @param item the item
   * @return the select state
   */
  public boolean isSelected(DataListItem item) {
    return selected.contains(item);
  }

  /**
   * Moves the current selections down one level.
   */
  public void moveSelectedDown() {
    int count = selected.size();
    if (count == 0) {
      return;
    }
    Collections.sort(selected, new Comparator<DataListItem>() {
      public int compare(DataListItem li1, DataListItem li2) {
        return indexOf(li1) < indexOf(li2) ? 1 : 0;
      }
    });
    DataListItem[] items = new DataListItem[count];
    for (int i = 0; i < count; i++) {
      items[i] = (DataListItem) selected.get(i);
    }
    for (int j = 0; j < count; j++) {
      int index = indexOf(items[j]);
      if (index != (getItemCount() - 1)) {
        remove((T) items[j]);
        insert((T) items[j], ++index);
        selectItems(index, index, true, true, true);
      }
    }
  }

  /**
   * Moves the current selections up one level.
   */
  public void moveSelectedUp() {
    int count = selected.size();
    if (count == 0) return;
    Collections.sort(selected, new Comparator<DataListItem>() {
      public int compare(DataListItem o1, DataListItem o2) {
        return indexOf(o1) > indexOf(o2) ? 1 : 0;
      }
    });
    DataListItem[] items = new DataListItem[count];
    for (int i = 0; i < count; i++) {
      items[i] = (DataListItem) selected.get(i);
    }
    for (int j = 0; j < count; j++) {
      int index = indexOf(items[j]);
      if (index > 0) {
        remove((T) items[j]);
        insert((T) items[j], --index);
        selectItems(index, index, true, true, true);
      }
    }
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    DataListItem item = findItem(ce.getTarget());
    if (item != null) {
      DataListEvent dle = new DataListEvent(this);
      dle.event = ce.event;
      dle.item = item;
      switch (ce.type) {
        case Event.ONMOUSEOVER:
          onItemOver(item, ce);
          fireEvent(Events.MouseOver, dle);
          break;
        case Event.ONMOUSEOUT:
          onItemOut(item, ce);
          fireEvent(Events.MouseOut, dle);
          break;
        case Event.ONCLICK:
          onItemClick(item, ce);
          fireEvent(Events.Click, dle);
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
  public boolean remove(T item) {
    DataListEvent dle = new DataListEvent(this);
    dle.item = item;
    if (fireEvent(Events.BeforeRemove, dle)) {
      if (lastSelected == item) {
        lastSelected = null;
      }
      selected.remove(item);
      item.list = null;

      unregister(item);

      boolean result = super.remove(item);
      fireEvent(Events.Remove, dle);
      return result;
    }
    return false;
  }

  /**
   * Removes all the items from the list.
   * 
   * @return this
   */
  public DataList removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
    return this;
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
   * Selects the specified item.
   * 
   * @param item the item to be selected
   */
  public void select(DataListItem item) {
    select(indexOf(item));
  }

  /**
   * Selects the item at the index. If the item at the index was already
   * selected, it remains selected.
   * 
   * @param index the index of the item to select
   */
  public void select(int index) {
    selectItems(index, index, true, multiSelect);
  }

  /**
   * Selects the items in the range specified by the given indices. The current
   * selection is not cleared before the new items are selected.
   * 
   * @param start the start of the range
   * @param end the end of the range
   */
  public void select(int start, int end) {
    selectItems(start, end, true, true);
  }

  /**
   * Selects all of the items in the list. If the list is single-select, do
   * nothing.
   */
  public void selectAll() {
    if (multiSelect) {
      selectItems(0, getItemCount() - 1, true, true);
    }
  }

  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  /**
   * Selects the item. The current selection is cleared.
   * 
   * @param item the item to select
   */
  public void setSelection(DataListItem item) {
    int index = indexOf(item);
    selectItems(index, index, true, false);
  }

  /**
   * Selects the items. The current selection is cleared.
   * 
   * @param items the items to select
   */
  public void setSelection(List<DataListItem> items) {
    deselectAll();
    for (DataListItem item : items) {
      int index = indexOf(item);
      selectItems(index, index, true, false);
    }
  }

  protected void createStyles(String baseStyle) {
    if (itemStyle == null) {
      itemStyle = baseStyle + "-item";
    }
  }

  protected void initSelectionMode() {
    singleSelect = selectionMode != SelectionMode.MULTI;
    multiSelect = selectionMode == SelectionMode.MULTI;
  }

  protected void onCheckChange(DataListItem item, boolean checked) {
    String s = checked ? "icon-checked" : "icon-notchecked";
    item.checkBtn.changeStyle(s);
  }
  @Override
  protected void onHideContextMenu() {
    super.onHideContextMenu();
    clearHoverStyles();
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

    new KeyNav(this) {

      @Override
      public void onDown(ComponentEvent ce) {
        onKeyPress(ce);
      }

      @Override
      public void onUp(ComponentEvent ce) {
        onKeyPress(ce);
      }

    };

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.KEYEVENTS | Event.MOUSEEVENTS);

    if (itemTemplate == null) {
      itemTemplate = defaultItemTemplate;
    }

    renderAll();

    if (!initialzied) {
      initSelectionMode();
      initialzied = true;
    }
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
      DOM.setElementPropertyInt(item.getElement(), "tabIndex", 0);
    }

    if (checkable) {
      item.checkBtn = new IconButton("icon-notchecked");
      item.checkBtn.setStyleAttribute("marginRight", "4px");
      Element elem = el.selectNode("my-listitem-check").dom;
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

  protected void onRightClick(ComponentEvent ce) {
    ce.cancelBubble();
    clearHoverStyles();
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

  private void clearHoverStyles() {
    if (hoverItem != null) {
      onItemOut(hoverItem, null);
    }
  }

  private void onItemClick(DataListItem item, ComponentEvent ce) {
    ce.stopEvent();
    if (checkable) {
      Element checkElem;
      if (item.checkBtn == null) {
        checkElem = item.getElement();
      } else {
        checkElem = item.checkBtn.getElement();
      }
      if (DOM.isOrHasChild(checkElem, ce.getTarget())) {
        item.setChecked(!item.isChecked());
        DataListEvent dle = new DataListEvent(this);
        dle.item = item;
        fireEvent(Events.CheckChange, dle);
        return;
      }
    }

    int index = indexOf(item);

    if (DOM.eventGetButton(ce.event) == Event.BUTTON_RIGHT) {
      if (singleSelect || getSelection().size() == 0) {
        selectItems(index, index, true, false);
      } else {
        selectItems(index, index, true, true);
      }
      return;
    }

    if (singleSelect) {
      boolean sel = true;
      if (isSelected(item) && ce.isControlKey()) {
        sel = false;
      }
      if (isSelected(item)) {
        return;
      }
      selectItems(index, index, sel, false);
      return;
    }

    if (multiSelect) {
      if (ce.isShiftKey()) {
        if (lastSelected != null) {
          selectItems(indexOf(lastSelected), index, true, true);
        } else {
          selectItems(0, index, true, false);
        }
      } else if (ce.isControlKey()) {
        selectItems(index, index, !isSelected(item), true);
      } else {
        selectItems(index, index, true, false);
      }
    }
  }

  private void onKeyPress(ComponentEvent ce) {
    T item = getItem(indexOf(lastSelected));
    switch (ce.getKeyCode()) {
      case KeyboardListener.KEY_UP: {
        int index = indexOf(lastSelected) - 1;
        if (index < 0) return;
        item = getItem(index);
        if (item != null) {
          selectItems(index, index, true, false);
          item.el.scrollIntoView(getElement(), false);
          ce.preventDefault();
        }
        break;
      }
      case KeyboardListener.KEY_DOWN: {
        int index = indexOf(lastSelected) + 1;
        if (index > getItemCount()) return;
        item = getItem(index);
        if (item != null) {
          selectItems(index, index, true, false);
          item.el.scrollIntoView(getElement(), false);
          ce.preventDefault();
        }
        break;
      }
    }
  }

  protected void onItemOut(DataListItem item, BaseEvent be) {
    item.removeStyleName(itemStyle + "-over");
  }

  protected void onItemOver(DataListItem item, BaseEvent be) {
    item.addStyleName(itemStyle + "-over");
  }

  private void onItemSelect(DataListItem item, boolean select) {
    if (select) {
      item.removeStyleName(itemStyle + "-over");
      item.addStyleName(itemStyle + "-sel");
    } else {
      item.removeStyleName(itemStyle + "-sel");
    }
  }

  private void register(T item) {
    nodes.put(item.getId(), item);
  }

  private void selectItems(int startIndex, int endIndex, boolean state,
      boolean keepSelected) {
    selectItems(startIndex, endIndex, state, keepSelected, false);
  }

  private void selectItems(int startIndex, int endIndex, boolean state,
      boolean keepSelected, boolean supressEvents) {
    if (startIndex < 0 || endIndex > getItemCount()) {
      return;
    }

    if (!initialzied) {
      initSelectionMode();
      initialzied = true;
    }

    setSelectionStyles(false);

    if (!keepSelected) {
      selected.clear();
    }

    lastSelected = getItem(endIndex);

    int begin = startIndex < endIndex ? startIndex : endIndex;
    int end = startIndex < endIndex ? endIndex : startIndex;

    for (int i = begin; i <= end; i++) {
      T item = getItem(i);
      if (state) {
        lastSelected = item;
        if (!selected.contains(item)) {
          selected.add(item);
        }
        if (i == begin) {
          scrollIntoView(item);
        }
      } else {
        selected.remove(item);
      }
    }

    if (!supressEvents) {
      DataListEvent dle = new DataListEvent(this);
      fireEvent(Events.SelectionChange, dle);
    }

    if (GXT.isSafari) {
      focus();
    }
    setSelectionStyles(true);
  }

  private void setSelectionStyles(boolean select) {
    int count = selected.size();
    for (int i = 0; i < count; i++) {
      DataListItem item = (DataListItem) selected.get(i);
      onItemSelect(item, select);
    }
  }

  private void unregister(DataListItem item) {
    nodes.remove(item.getId());
  }

}
