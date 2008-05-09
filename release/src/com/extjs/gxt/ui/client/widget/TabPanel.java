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
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
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
public class TabPanel extends AbstractContainer<TabItem> implements SelectionProvider {

  /**
   * Tab position enumeration.
   */
  public enum TabPosition {
    TOP, BOTTOM;
  }

  private class AccessStack {

    Stack<TabItem> stack = new Stack<TabItem>();

    void add(TabItem item) {
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

    TabItem next() {
      return stack.size() > 0 ? stack.pop() : null;
    }

    void remove(TabItem item) {
      stack.remove(item);
    }
  }

  /**
   * Default tab item template.
   */
  public static Template itemTemplate;

  /**
   * True to display an interior border on the body element of the panel, false
   * to hide it (defaults to true).
   */
  private boolean bodyBorder = true;

  private boolean border = true;
  private int tabMargin = 2;
  private int scrollIncrement = 100;
  private int minTabWidth = 30;
  private int scrollDuration = 150;
  private boolean resizeTabs = false;
  private TabPosition tabPosition = TabPosition.TOP;
  private int tabWidth = 120;
  private boolean tabScroll = false;
  private boolean autoSelect = true;
  private boolean animScroll = false;
  private TabItem activeItem;
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
    container.setBorders(false);
  }

  /**
   * Adds a tab item. Fires the <i>BeforeAdd</i> event before inserting, then
   * fires the <i>Add</i> event after the widget has been inserted.
   * 
   * @param item the item to be added
   */
  public void add(TabItem item) {
    insert(item, getItemCount());
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
   * Returns true if scrolling is animated.
   * 
   * @return the anim scroll state
   */
  public boolean getAnimScroll() {
    return animScroll;
  }

  /**
   * Returns true if the body border is enabled.
   * 
   * @return the body border state
   */
  public boolean getBodyBorder() {
    return bodyBorder;
  }

  /**
   * Returns true if the border style is enabled.
   * 
   * @return the border style
   */
  public boolean getBorderStyle() {
    return border;
  }

  /**
   * Returns the minimum tab width.
   * 
   * @return the minimum tab width
   */
  public int getMinTabWidth() {
    return minTabWidth;
  }

  /**
   * Returns true if tab resizing is enabled.
   * 
   * @return the tab resizing state
   */
  public boolean getResizeTabs() {
    return resizeTabs;
  }

  /**
   * Returns the scroll duration in millseconds.
   * 
   * @return the duration
   */
  public int getScrollDuration() {
    return scrollDuration;
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
   * Returns the panel's tab margin.
   * 
   * @return the margin
   */
  public int getTabMargin() {
    return tabMargin;
  }

  /**
   * Returns the tab position.
   * 
   * @return the tab position
   */
  public TabPosition getTabPosition() {
    return tabPosition;
  }

  /**
   * Returns true if tab scrolling is enabled.
   * 
   * @return the tab scroll state
   */
  public boolean getTabScroll() {
    return tabScroll;
  }

  /**
   * Returns the default tab width.
   * 
   * @return the width
   */
  public int getTabWidth() {
    return tabWidth;
  }

  /**
   * Inserts a tab item. Fires the <i>BeforeAdd</i> event before inserting,
   * then fires the <i>Add</i> event after the widget has been inserted.
   * 
   * @param item the item to be inserted
   * @param index the insert position
   */
  public void insert(TabItem item, int index) {
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

  /**
   * Returns true if auto select is enabled.
   * 
   * @return the auto select state
   */
  public boolean isAutoSelect() {
    return autoSelect;
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    ce.cancelBubble();
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
  public boolean remove(TabItem item) {
    TabPanelEvent tpe = new TabPanelEvent(this);
    tpe.item = item;
    if (fireEvent(Events.BeforeRemove, tpe)) {
      stack.remove(item);
      container.remove(item);
      
      super.remove(item);

      if (rendered) {
        if (item.header.isRendered()) {
          strip.removeChild(item.header.getElement());
          if (isAutoDestroy()) {
            item.header.destroy();
          }
        }

        if (item == activeItem) {
          activeItem = null;
          TabItem next = this.stack.next();
          if (next != null) {
            setSelection(next);
          } else if (getItemCount() > 0) {
            setSelection(getItem(0));
          } else {
            layout.setActiveItem(null);
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
      TabItem item = getItem(0);
      remove(item);
    }
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

  /**
   * True to animate tab scrolling so that hidden tabs slide smoothly into view
   * (defaults to true). Only applies when {@link #tabScroll} = true.
   * 
   * @param animScroll the anim scroll state
   */
  public void setAnimScroll(boolean animScroll) {
    this.animScroll = animScroll;
  }

  /**
   * True to have the first item selected when the panel is displayed for the
   * first time if there is not selection (defaults to true).
   * 
   * @param autoSelect the auto select state
   */
  public void setAutoSelect(boolean autoSelect) {
    this.autoSelect = autoSelect;
  }

  /**
   * True to display an interior border on the body element of the panel, false
   * to hide it (defaults to true, pre-render).
   * 
   * @param bodyBorder the body border style
   */
  public void setBodyBorder(boolean bodyBorder) {
    this.bodyBorder = bodyBorder;
  }

  /**
   * True to display a border around the tabs (defaults to true).
   * 
   * @param border true for borders
   */
  public void setBorderStyle(boolean border) {
    this.border = border;
  }

  /**
   * The minimum width in pixels for each tab when {@link #resizeTabs} = true
   * (defaults to 30).
   * 
   * @param minTabWidth the minimum tab width
   */
  public void setMinTabWidth(int minTabWidth) {
    this.minTabWidth = minTabWidth;
  }

  /**
   * True to automatically resize each tab so that the tabs will completely fill
   * the tab strip (defaults to false). Setting this to true may cause specific
   * widths that might be set per tab to be overridden in order to fit them all
   * into view (although {@link #minTabWidth} will always be honored).
   * 
   * @param resizeTabs true to enable tab resizing
   */
  public void setResizeTabs(boolean resizeTabs) {
    this.resizeTabs = resizeTabs;
  }

  /**
   * Sets the number of milliseconds that each scroll animation should last
   * (defaults to 150).
   * 
   * @param scrollDuration the scroll duration
   */
  public void setScrollDuration(int scrollDuration) {
    this.scrollDuration = scrollDuration;
  }

  /**
   * Sets the number of pixels to scroll each time a tab scroll button is
   * pressed (defaults to 100, or if {@link #setResizeTabs(boolean)} = true, the
   * calculated tab width). Only applies when {@link #setTabScroll(boolean)} =
   * true.
   * 
   * @param scrollIncrement the scroll increment
   */
  public void setScrollIncrement(int scrollIncrement) {
    this.scrollIncrement = scrollIncrement;
  }

  public void setSelection(Selection selection) {
    Object element = selection.getFirstElement();
    if (element != null) {
      for (TabItem item : getItems()) {
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
  public void setSelection(TabItem item) {
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
        scrollToTab(item, getAnimScroll());
      }
      fireEvent(Events.Select, tpe);
      SelectionChangedEvent se = new SelectionChangedEvent(this, getSelection());
      fireSelectionChanged(se);
    }
  }

  /**
   * The number of pixels of space to calculate into the sizing and scrolling of
   * tabs (defaults to 2).
   * 
   * @param tabMargin the tab margin
   */
  public void setTabMargin(int tabMargin) {
    this.tabMargin = tabMargin;
  }

  /**
   * Sets the position where the tab strip should be rendered (defaults to TOP, pre-render).
   * The only other supported value is BOTTOM. Note that tab scrolling is only
   * supported for position TOP.
   * 
   * @param tabPosition the tab position
   */
  public void setTabPosition(TabPosition tabPosition) {
    this.tabPosition = tabPosition;
  }

  /**
   * True to enable scrolling to tabs that may be invisible due to overflowing
   * the overall TabPanel width. Only available with tabs on top. (defaults to
   * false).
   * 
   * @param tabScroll true to enable tab scrolling
   */
  public void setTabScroll(boolean tabScroll) {
    this.tabScroll = tabScroll;
  }

  /**
   * Sets the initial width in pixels of each new tab (defaults to 120).
   * 
   * @param tabWidth
   */
  public void setTabWidth(int tabWidth) {
    this.tabWidth = tabWidth;
  }

  protected void afterRender() {
    super.afterRender();
    if (activeItem != null) {
      TabItem item = activeItem;
      activeItem = null;
      setSelection(item);
    } else if (activeItem == null && isAutoSelect() && getItemCount() > 0) {
      setSelection(getItem(0));
    }
    if (getResizeTabs()) {
      autoSizeTabs();
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(container);
    for (TabItem item : getItems()) {
      WidgetHelper.doAttach(item.header);
    }
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(container);
    for (TabItem item : getItems()) {
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

    body.addStyleName("x-tab-panel-body-" + getTabPosition());

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
//      bar.setWidth(width, true);
//      body.setWidth(width, true);
    }

    container.setSize(width, height - hh);
    container.layout();
    delegateUpdates();
  }

  protected void onUnload() {
    if (stack != null) {
      stack.clear();
    }
  }

  void onItemClick(TabItem item, ComponentEvent ce) {
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

    String style = item.isClosable() ? "x-tab-strip-closable " : "";
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
    p.set("textStyle", item.getTextStyle());
    p.set("iconStyle", item.getIconStyle());
    if (item.template == null) {
      item.template = itemTemplate;
    }
    item.header.setElement(item.template.create(p));

    item.header.el.addEventsSunk(Event.ONCLICK);

    DOM.insertChild(target, item.header.getElement(), pos);
  }

  private void autoScrollTabs() {
    int count = getItemCount();
    int tw = bar.getClientWidth();

    int cw = stripWrap.getWidth();
    int pos = getScollPos();
    int l = edge.offsetsTo(stripWrap.dom).x + pos;

    if (!getTabScroll() || count < 1 || cw < 20) {
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
    int count = getItemCount();
    if (count == 0) return;
    int aw = bar.getClientWidth();
    int each = (int) Math.max(Math.min(Math.floor((aw - 4) / count) - getTabMargin(),
        tabWidth), getMinTabWidth());

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
    if (getResizeTabs() && rendered) {
      autoSizeTabs();
    }
    if (getTabScroll() && rendered) {
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
      scrollTo(s, getAnimScroll());
    }
  }

  private void onScrollRight() {
    int sw = getScrollWidth() - getScrollArea();
    int pos = getScollPos();
    int s = Math.min(sw, pos + getScrollIncrement());
    if (s != pos) {
      scrollTo(s, getAnimScroll());
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
      stripWrap.scrollTo("left", pos, getScrollDuration(), new Listener<FxEvent>() {
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
