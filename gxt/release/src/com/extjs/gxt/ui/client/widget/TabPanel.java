/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.viewer.DefaultSelection;
import com.extjs.gxt.ui.client.viewer.Selection;
import com.extjs.gxt.ui.client.viewer.SelectionChangedEvent;
import com.extjs.gxt.ui.client.viewer.SelectionChangedListener;
import com.extjs.gxt.ui.client.viewer.SelectionProvider;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A basic tab container.
 * 
 * <p>
 * When using a tab panel as a selection provider the model element for each tab
 * item should be set as the item's data {@link DataListItem#setData(Object)}.
 * </p>
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeSelect</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires after an item is selected. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item about to be selected.</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires after a item is selected.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item that was selected</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeAdd</b> : TabPanelEvent(tabPanel, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : TabPanelEvent(tabPanel, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : TabPanelEvent(tabPanel, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>tabPanel : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * <p>
 * 
 * <pre><code>
 TabPanel panel = new TabPanel();
 panel.resizeTabs = true;
 panel.enableTabScroll = true;
 panel.animScroll = true;

 TabItem item = new TabItem();
 item.closable = true;
 item.setText("Tab Item");

 item.setLayout(new FitLayout());
 item.add(new Label("Test Content"));

 panel.add(item);
 * </code></pre>
 * 
 * </p>
 */
public class TabPanel<T extends TabItem> extends AbstractContainer<T> implements
    SelectionProvider {

  /**
   * Tab position enumeration.
   */
  public enum TabPosition {
    TOP, BOTTOM;
  }

  private class AccessStack {

    Stack<T> stack = new Stack<T>();

    void add(T item) {
      if (stack.contains(item)) {
        stack.remove(item);
      }
      stack.add(item);
      if (stack.size() > 10) {
        stack.remove(0);
      }
    }

    void clear() {
      stack.clear();
    }

    T next() {
      return stack.size() > 0 ? stack.pop() : null;
    }

    void remove(T item) {
      stack.remove(item);
    }
  }

  /**
   * Default tab item template.
   */
  public static Template itemTemplate;

  /**
   * True to animate tab scrolling so that hidden tabs slide smoothly into view
   * (defaults to true). Only applies when {@link #enableTabScroll} = true.
   */
  public boolean animScroll = false;

  /**
   * True to display an interior border on the body element of the panel, false
   * to hide it (defaults to true).
   */
  public boolean bodyBorder = true;

  /**
   * True to display a border around the tabs (defaults to true).
   */
  public boolean border = true;

  /**
   * True to enable scrolling to tabs that may be invisible due to overflowing
   * the overall TabPanel width. Only available with tabs on top. (defaults to
   * false).
   */
  public boolean enableTabScroll = false;

  /**
   * The minimum width in pixels for each tab when {@link #resizeTabs} = true
   * (defaults to 30).
   */
  public int minTabWidth = 30;

  /**
   * True to automatically resize each tab so that the tabs will completely fill
   * the tab strip (defaults to false). Setting this to true may cause specific
   * widths that might be set per tab to be overridden in order to fit them all
   * into view (although {@link #minTabWidth} will always be honored).
   */
  public boolean resizeTabs = false;

  /**
   * The number of milliseconds that each scroll animation should last (defaults
   * to 150).
   */
  public int scrollDuration = 150;

  /**
   * The number of pixels to scroll each time a tab scroll button is pressed
   * (defaults to 100, or if {@link #resizeTabs} = true, the calculated tab
   * width). Only applies when {@link #enableTabScroll} = true.
   */
  public int scrollIncrement = 100;

  /**
   * The number of pixels of space to calculate into the sizing and scrolling of
   * tabs (defaults to 2).
   */
  public int tabMargin = 2;

  /**
   * The position where the tab strip should be rendered (defaults to TOP). The
   * only other supported value is BOTTOM. Note that tab scrolling is only
   * supported for position TOP.
   */
  public TabPosition tabPosition = TabPosition.TOP;

  /**
   * The initial width in pixels of each new tab (defaults to 120).
   */
  public int tabWidth = 120;

  /**
   * True to have the first item selected when the panel is displayed for the
   * first time if there is not selection (defaults to true)..
   */
  public boolean autoSelect = true;

  private T activeItem;
  private El body, bar, stripWrap, strip;
  private Container<TabItem> container;
  private El edge, scrollLeft, scrollRight;
  private CardLayout layout;
  private boolean scrolling;
  private AccessStack stack;
  private List<SelectionChangedListener> selectionListeners;

  /**
   * Creates a new tab panel.
   */
  public TabPanel() {
    baseStyle = "x-tab-panel";
    attachChildren = false;
    container = new Container<TabItem>();
    container.setLayoutOnChange(true);
  }

  /**
   * Adds a tab item. Fires the <i>BeforeAdd</i> event before inserting, then
   * fires the <i>Add</i> event after the widget has been inserted.
   * 
   * @param item the item to be added
   */
  public void add(T item) {
    insert(item, getItemCount());
  }

  /**
   * Adds a listener interface to receive selection events.
   * 
   * @param listener the listener to add
   */
  public void addClickListener(SelectionListener listener) {
    TypedListener tl = new TypedListener(listener);
    addListener(Events.Click, tl);
  }

  public void addSelectionListener(SelectionChangedListener listener) {
    if (selectionListeners == null) {
      selectionListeners = new ArrayList<SelectionChangedListener>();
    }
    if (!selectionListeners.contains(listener)) {
      selectionListeners.add(listener);
    }
  }

  /**
   * Searches for an item based on its id and optionally the item's text.
   * 
   * @param id the item id
   * @param checkText true to match the items id and text
   * @return the item
   */
  public TabItem findItem(String id, boolean checkText) {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      TabItem item = getItem(i);
      if (item.getId().equals(id) || (checkText && item.getText().equals(id))) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the current selection tab item.
   * 
   * @return the selected item
   */
  public TabItem getSelectedItem() {
    return activeItem;
  }

  /**
   * Returns the current selection.
   * 
   * @return the selection.
   */
  public Selection getSelection() {
    if (activeItem == null) {
      return DefaultSelection.emptySelection();
    }
    return new DefaultSelection(activeItem.getData());
  }

  /**
   * Inserts a tab item. Fires the <i>BeforeAdd</i> event before inserting,
   * then fires the <i>Add</i> event after the widget has been inserted.
   * 
   * @param item the item to be inserted
   * @param index the insert position
   */
  public void insert(T item, int index) {
    TabPanelEvent tpe = new TabPanelEvent(this);
    tpe.item = item;
    tpe.index = index;
    if (fireEvent(Events.BeforeAdd, tpe)) {
      item.tabPanel = this;
      super.insert(item, index);
      container.insert(item, index);
      if (rendered) {
        renderItem(item, index);
        if (isAttached()) {
          WidgetHelper.doAttach(item.header);
        }
        delegateUpdates();
      }
      fireEvent(Events.Add, tpe);
    }
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    if (ce.type == Event.ONCLICK) {
      El target = ce.getTargetEl();
      if (target.is(".x-tab-scroller-left")) {
        onScrollLeft();
      }
      if (target.is(".x-tab-scroller-right")) {
        onScrollRight();
      }

    }
  }

  /**
   * Removes the tab item. Fires the <i>BeforeRemove</i> event before removing,
   * then fires the <i>Remove</i> event after the widget has been removed.
   * 
   * @param item the item to be removed
   */
  public boolean remove(T item) {
    TabPanelEvent tpe = new TabPanelEvent(this);
    tpe.item = item;
    if (fireEvent(Events.BeforeRemove, tpe)) {
      super.remove(item);
      if (rendered) {

        if (item.header.isRendered()) {
          strip.removeChild(item.header.getElement());
          if (autoDestroy) {
            item.header.destroy();
          }
        }
        stack.remove(item);
        container.remove(item);
        if (item == activeItem) {
          activeItem = null;
          T next = this.stack.next();
          if (next != null) {
            setSelection(next);
          } else if (getItemCount() > 0) {
            setSelection(getItem(0));
          }
        }

      }
      fireEvent(Events.Remove, tpe);
      return true;
    }
    return false;
  }

  /**
   * Removes all items.
   */
  public void removeAll() {
    int size = getItemCount();
    for (int i = 0; i < size; i++) {
      T item = getItem(0);
      remove(item);
    }
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeClickListener(SelectionListener listener) {
    removeListener(Events.Click, listener);
  }

  public void removeSelectionListener(SelectionChangedListener listener) {
    if (selectionListeners != null) {
      selectionListeners.remove(listener);
    }
  }

  /**
   * Scrolls to a particular tab if tab scrolling is enabled.
   * 
   * @param item the item to scroll to
   * @param animate true to animate the scroll
   */
  public void scrollToTab(TabItem item, boolean animate) {
    if (item == null) return;
    int pos = getScollPos();
    int area = getScrollArea();
    El itemEl = item.header.el;
    int left = itemEl.getOffsetsTo(stripWrap.dom).x + pos;
    int right = left + itemEl.getWidth();
    if (left < pos) {
      scrollTo(left, animate);
    } else if (right > (pos + area)) {
      scrollTo(right - area, animate);
    }
  }

  public void setSelection(Selection selection) {
    Object element = selection.getFirstElement();
    if (element != null) {
      for (T item : items) {
        if (element == item.getData() || element.equals(item.getData())) {
          setSelection(item);
          return;
        }
      }
    }
  }

  /**
   * Sets the selected tab item. Fires the <i>BeforeSelect</i> event before
   * selecting, then fires the <i>Select</i> event after the widget has been
   * selected.
   * 
   * @param item the item to be selected
   */
  public void setSelection(T item) {
    TabPanelEvent tpe = new TabPanelEvent(this);
    tpe.item = item;
    if (item == null || !fireEvent(Events.BeforeSelect, tpe)) {
      return;
    }

    if (!rendered) {
      activeItem = item;
      return;
    }
    if (activeItem != item) {
      if (activeItem != null) {
        activeItem.header.removeStyleName("x-tab-strip-active");
      }
      item.header.addStyleName("x-tab-strip-active");
      activeItem = item;
      stack.add(activeItem);
      layout.setActiveItem(item);
      container.layout(true);

      if (scrolling) {
        scrollToTab(item, animScroll);
      }
      fireEvent(Events.Select, tpe);
      SelectionChangedEvent se = new SelectionChangedEvent(this, getSelection());
      fireSelectionChanged(se);
    }
  }

  protected void afterRender() {
    super.afterRender();
    if (activeItem != null) {
      T item = activeItem;
      activeItem = null;
      setSelection(item);
    } else if (activeItem == null && autoSelect && getItemCount() > 0) {
      setSelection(getItem(0));
    }
    if (resizeTabs) {
      autoSizeTabs();
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(container);
    for (TabItem item : items) {
      WidgetHelper.doAttach(item.header);
    }
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(container);
    for (TabItem item : items) {
      WidgetHelper.doDetach(item.header);
    }
  }

  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);
    stack = new AccessStack();

    if (tabPosition == TabPosition.TOP) {
      bar = el.createChild("<div class='x-tab-panel-header'></div>");
      body = el.createChild("<div class='x-tab-panel-body x-tab-panel-body-top'></div");
    } else {
      body = el.createChild("<div class='x-tab-panel-body x-tab-panel-body-bottom'></div");
      bar = el.createChild("<div class='x-tab-panel-footer'></div>");
    }

    if (!bodyBorder) {
      body.setStyleAttribute("border", "none");
    }

    if (!border && tabPosition == TabPosition.TOP) {
      bar.setStyleAttribute("borderLeft", "none");
      bar.setStyleAttribute("borderRight", "none");
      bar.setStyleAttribute("borderTop", "none");
    }
    if (!border && tabPosition == TabPosition.BOTTOM) {
      bar.setStyleAttribute("borderLeft", "none");
      bar.setStyleAttribute("borderRight", "none");
      bar.setStyleAttribute("borderBottom", "none");
    }

    String pos = tabPosition == TabPosition.BOTTOM ? "bottom" : "top";
    stripWrap = bar.createChild("<div class=x-tab-strip-wrap><ul class='x-tab-strip x-tab-strip-"
        + pos + "'></ul>");
    bar.createChild("<div class=x-tab-strip-spacer></div>");
    strip = stripWrap.firstChild();
    edge = strip.createChild("<li class=x-tab-edge></li>");
    strip.createChild("<div class=x-clear></div>");

    body.addStyleName("x-tab-panel-body-" + tabPosition);

    if (itemTemplate == null) {
      StringBuffer sb = new StringBuffer();
      sb.append("<li class='{style}' id={id}><a class=x-tab-strip-close onclick='return false;'></a>");
      sb.append("<a class='x-tab-right' href='#' onclick='return false;'><em class='x-tab-left'>");
      sb.append("<span class='x-tab-strip-inner'><span class='x-tab-strip-text {textStyle} {iconStyle}'>{text}</span></span>");
      sb.append("</em></a></li>");
      itemTemplate = new Template(sb.toString());
      itemTemplate.compile();
    }
    layout = new CardLayout();
    container.setLayout(layout);

    renderAll();
    container.render(body.dom);

    el.addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS);
  }

  protected void onResize(int width, int height) {
    int hh = getFrameHeight();
    width -= el.getFrameWidth("lr");
    if (height != Style.DEFAULT) {
      body.setHeight(height - hh, true);
    }
    if (width != Style.DEFAULT) {
      bar.setWidth(width, true);
      body.setWidth(width, true);
    }
    int adj = XDOM.isVisibleBox ? 1 : 1;
    container.setSize(width - adj, height - hh - adj);
    delegateUpdates();
  }

  protected void onUnload() {
    if (stack != null) {
      stack.clear();
    }
  }

  void onItemClick(T item, ComponentEvent ce) {
    ce.stopEvent();
    Element target = ce.getTarget();
    if (fly(target).getStyleName().equals("x-tab-strip-close")) {
      remove(item);
      return;
    } else if (item != activeItem) {
      setSelection(item);
    }
  }

  void onItemOver(TabItem item, boolean over) {
    item.header.el.setStyleName("x-tab-strip-over", over);
  }

  void onItemRender(TabItem item, Element target, int pos) {
    item.header.disabledStyle = "x-item-disabled";

    String style = item.closable ? "x-tab-strip-closable " : "";
    if (!item.header.isEnabled()) {
      style += " x-item-disabled";
    }
    if (item.getIconStyle() != null) {
      style += " x-tab-with-icon";
    }
    Params p = new Params();
    p.set("id", item.getId());
    p.set("text", item.getText());
    p.set("style", style);
    p.set("textStyle", item.textStyle);
    p.set("iconStyle", item.getIconStyle());
    if (item.template == null) {
      item.template = itemTemplate;
    }
    item.header.setElement(item.template.create(p));

    item.header.el.addEventsSunk(Event.ONCLICK);

    DOM.insertChild(target, item.header.getElement(), pos);
  }

  private void autoScrollTabs() {
    int count = items.size();
    int tw = bar.getClientWidth();

    int cw = stripWrap.getWidth();
    int pos = getScollPos();
    int l = edge.offsetsTo(stripWrap.dom).x + pos;

    if (!enableTabScroll || count < 1 || cw < 20) {
      return;
    }

    if (l <= tw) {
      stripWrap.setScrollLeft(0);
      stripWrap.setWidth(tw);
      if (scrolling) {
        scrolling = false;
        bar.removeStyleName("x-tab-scrolling");
        scrollLeft.setVisible(false);
        scrollRight.setVisible(false);
      }
    } else {
      if (!scrolling) {
        bar.addStyleName("x-tab-scrolling");
      }
      tw -= stripWrap.getMargins("lr");
      stripWrap.setWidth(tw > 20 ? tw : 20);
      if (!scrolling) {
        if (scrollLeft == null) {
          createScrollers();
        } else {
          scrollLeft.setVisible(true);
          scrollRight.setVisible(true);
        }
      }
      scrolling = true;
      if (pos > (l - tw)) {
        stripWrap.setLeft(l - tw);
      } else {
        scrollToTab(activeItem, false);
      }
      updateScrollButtons();
    }
  }

  private void autoSizeTabs() {
    int count = items.size();
    if (count == 0) return;
    int aw = bar.getClientWidth();
    int each = (int) Math.max(
        Math.min(Math.floor((aw - 4) / count) - tabMargin, tabWidth), minTabWidth);

    for (int i = 0; i < count; i++) {
      El el = getItem(i).header.el;
      Element inner = el.childNode(1).firstChild().firstChild().dom;
      int tw = el.getWidth();
      int iw = fly(inner).getWidth();
      fly(inner).setWidth((each - (tw - iw)) + "px");
    }
  }

  private void createScrollers() {
    int h = stripWrap.getHeight();
    scrollLeft = bar.insertFirst("<div class='x-tab-scroller-left'></div>");
    addStyleOnOver(scrollLeft.dom, "x-tab-scroller-left-over");
    scrollLeft.setHeight(h);

    scrollRight = bar.insertFirst("<div class='x-tab-scroller-right'></div>");
    addStyleOnOver(scrollRight.dom, "x-tab-scroller-right-over");
    scrollRight.setHeight(h);
  }

  private void delegateUpdates() {
    if (resizeTabs && rendered) {
      autoSizeTabs();
    }
    if (enableTabScroll && rendered) {
      autoScrollTabs();
    }
  }

  private void fireSelectionChanged(SelectionChangedEvent se) {
    if (selectionListeners != null) {
      for (SelectionChangedListener listener : selectionListeners) {
        listener.selectionChanged(se);
      }
    }
  }

  private int getFrameHeight() {
    int h = el.getFrameWidth("tb");
    h += bar.getHeight();
    return h;
  }

  private int getScollPos() {
    return stripWrap.getScrollLeft();
  }

  private int getScrollArea() {
    return Math.max(0, stripWrap.getWidth());
  }

  private int getScrollIncrement() {
    return scrollIncrement;
  }

  private int getScrollWidth() {
    return edge.offsetsTo(stripWrap.dom).x + getScollPos();
  }

  private void onScrollLeft() {
    int pos = getScollPos();
    int s = Math.max(0, pos - getScrollIncrement());
    if (s != pos) {
      scrollTo(s, animScroll);
    }
  }

  private void onScrollRight() {
    int sw = getScrollWidth() - getScrollArea();
    int pos = getScollPos();
    int s = Math.min(sw, pos + getScrollIncrement());
    if (s != pos) {
      scrollTo(s, animScroll);
    }
  }

  private void renderAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      TabItem item = getItem(i);
      renderItem(item, i);
    }
  }

  private void renderItem(TabItem item, int index) {
    item.header.render(strip.dom, index);
  }

  private void scrollTo(int pos, boolean animate) {
    if (animate) {
      stripWrap.scrollTo("left", pos, scrollDuration, new Listener<FxEvent>() {
        public void handleEvent(FxEvent fe) {
          updateScrollButtons();
        }
      });
    } else {
      stripWrap.scrollTo("left", pos);
      updateScrollButtons();
    }
  }

  private void updateScrollButtons() {
    int pos = getScollPos();
    scrollLeft.setStyleName("x-tab-scroller-left-disabled", pos == 0);
    scrollRight.setStyleName("x-tab-scroller-right-disabled",
        pos >= (getScrollWidth() - getScrollArea()));
  }
}
