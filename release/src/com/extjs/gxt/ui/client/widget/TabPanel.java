/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.Stack;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A basic tab container.
 * 
 * <pre>
   TabPanel panel = new TabPanel();
   panel.setResizeTabs(true);
   panel.setEnableTabScroll(true);
   panel.setAnimScroll(true);
  
   TabItem item = new TabItem();
   item.setClosable(true);
   item.setText("Tab Item");
  
   item.setLayout(new FitLayout());
   item.add(new Label("Test Content"));
  
   panel.add(item);
 * </pre>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeSelect</b> : TabPanelEvent(container, item)<br>
 * <div>Fires after an item is selected. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item about to be selected.</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : TabPanelEvent(container, item)<br>
 * <div>Fires after a item is selected.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item that was selected</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeAdd</b> : TabPanelEvent(container, item, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : TabPanelEvent(container, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : TabPanelEvent(container, item, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : TabPanelEvent(container, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>container : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class TabPanel extends Container<TabItem> {

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
  private El edge, scrollLeft, scrollRight;
  private CardLayout cardLayout;
  private boolean scrolling;
  private AccessStack stack;
  private boolean plain;

  /**
   * Creates a new tab panel.
   */
  public TabPanel() {
    baseStyle = "x-tab-panel";
    cardLayout = new CardLayout();
    setLayout(cardLayout);
    enableLayout = true;
  }

  /**
   * Adds a tab item. Fires the <i>BeforeAdd</i> event before inserting, then
   * fires the <i>Add</i> event after the widget has been inserted.
   * 
   * @param item the item to be added
   */
  public boolean add(TabItem item) {
    return super.add(item);
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
      if (item.getItemId().equals(id) || item.getId().equals(id)
          || (checkText && item.getText().equals(id))) {
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
   * Adds a tab item. Fires the <i>BeforeAdd</i> event before inserting, then
   * fires the <i>Add</i> event after the widget has been inserted.
   * 
   * @param item the item to be inserted
   * @param index the insert position
   */
  @Override
  public boolean insert(TabItem item, int index) {
    boolean added = super.insert(item, index);
    if (added) {
      item.tabPanel = this;
      item.setAutoHeight(isAutoHeight());
      if (rendered) {
        renderItem(item, index);
        if (isAttached()) {
          ComponentHelper.doAttach(item.header);
        }
        if (activeItem == null && autoSelect) {
          setSelection(item);
        }
        delegateUpdates();
      }
    }
    return added;
  }

  /**
   * Returns true if auto select is enabled.
   * 
   * @return the auto select state
   */
  public boolean isAutoSelect() {
    return autoSelect;
  }

  /**
   * Returns true if the tab strip will be rendered without a background.
   * 
   * @return the plain state
   */
  public boolean isPlain() {
    return plain;
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    if (ce.type == Event.ONCLICK) {
      El target = ce.getTargetEl();
      if (target.is(".x-tab-scroller-left")) {
        ce.cancelBubble();
        onScrollLeft();
      }
      if (target.is(".x-tab-scroller-right")) {
        ce.cancelBubble();
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
    boolean removed = super.remove(item);
    if (removed) {
      if (stack != null) {
        stack.remove(item);
      }
      if (rendered) {
        if (item.header.isRendered()) {
          item.header.removeStyleName("x-tab-strip-active");
          strip.dom.removeChild(item.header.getElement());
          if (item.header.isAttached()) {
            ComponentHelper.doDetach(item.header);
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
            getLayout().activeItem = null;
          }
        }
      }
      delegateUpdates();
    }

    return removed;
  }

  @Override
  public boolean removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
    return getItemCount() == 0;
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
    El itemEl = item.header.el();
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

  @Override
  public void setAutoHeight(boolean autoHeight) {
    super.setAutoHeight(autoHeight);
    for (TabItem item : getItems()) {
      item.setAutoHeight(autoHeight);
    }
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
   * True to render the tab strip without a background container image (defaults
   * to false, pre-render).
   * 
   * @param plain
   */
  public void setPlain(boolean plain) {
    assertPreRender();
    this.plain = plain;
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

  /**
   * Sets the selected tab item. Fires the <i>BeforeSelect</i> event before
   * selecting, then fires the <i>Select</i> event after the widget has been
   * selected.
   * 
   * @param item the item to be selected
   */
  public void setSelection(TabItem item) {
    TabPanelEvent tpe = new TabPanelEvent(this, item);
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
      cardLayout.setActiveItem(item);

      if (isAttached()) ComponentHelper.doAttach(item);
      item.layout();

      if (scrolling) {
        scrollToTab(item, getAnimScroll());
      }
      fireEvent(Events.Select, tpe);
      item.fireEvent(Events.Select, tpe);
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
   * Sets the position where the tab strip should be rendered (defaults to TOP,
   * pre-render). The only other supported value is BOTTOM. Note that tab
   * scrolling is only supported for position TOP.
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

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new TabPanelEvent(this);
  }

  @Override
  protected ContainerEvent createContainerEvent(TabItem item) {
    return new TabPanelEvent(this, item);
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    for (TabItem item : getItems()) {
      ComponentHelper.doAttach(item.header);
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    for (TabItem item : getItems()) {
      ComponentHelper.doDetach(item.header);
    }
  }

  @Override
  protected El getLayoutTarget() {
    return body;
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    if (activeItem != null) {
      TabItem item = activeItem;
      activeItem = null;
      setSelection(item);
    } else if (activeItem == null && autoSelect && getItemCount() > 0) {
      setSelection(getItem(0));
    }
    if (resizeTabs) {
      autoSizeTabs();
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);
    stack = new AccessStack();

    if (tabPosition == TabPosition.TOP) {
      bar = el().createChild("<div class='x-tab-panel-header'></div>");
      body = el().createChild("<div class='x-tab-panel-body x-tab-panel-body-top'></div");
    } else {
      body = el().createChild("<div class='x-tab-panel-body x-tab-panel-body-bottom'></div");
      bar = el().createChild("<div class='x-tab-panel-footer'></div>");
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

    if (plain) {
      String p = tabPosition == TabPosition.BOTTOM ? "bottom" : "header";
      bar.addStyleName("x-tab-panel-" + p + "-plain");
    }

    if (itemTemplate == null) {
      StringBuffer sb = new StringBuffer();
      sb.append("<li class='{style}' id={id}><a class=x-tab-strip-close onclick='return false;'></a>");
      sb.append("<a class='x-tab-right' href='#' onclick='return false;'><em class='x-tab-left'>");
      sb.append("<span class='x-tab-strip-inner'><span class='x-tab-strip-text {textStyle} {iconStyle}'>{text}</span></span>");
      sb.append("</em></a></li>");
      itemTemplate = new Template(sb.toString());
      itemTemplate.compile();
    }

    renderAll();

    el().addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS);

  }

  @Override
  protected void onResize(int width, int height) {
    stripWrap.setScrollLeft(0);

    int hh = getFrameHeight();
    width -= el().getFrameWidth("lr");
    if (height != Style.DEFAULT) {
      body.setHeight(height - hh, true);
    } else {
      body.setHeight("auto");
    }
    if (width != Style.DEFAULT) {
      bar.setWidth(width, true);
      body.setWidth(width, true);
    }

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
      TabPanelEvent e = new TabPanelEvent(this, item);
      if (item.fireEvent(Events.BeforeClose, e) && remove(item)) {
        item.fireEvent(Events.Close, new TabPanelEvent(this, item));
      }
      return;
    } else if (item != activeItem) {
      setSelection(item);
    }
  }

  void onItemOver(TabItem item, boolean over) {
    item.header.el().setStyleName("x-tab-strip-over", over);
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
    item.header.el().addEventsSunk(Event.ONCLICK | Event.MOUSEEVENTS);

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
        stripWrap.setScrollLeft(l - tw);
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
    int each = (int) Math.max(Math.min(Math.floor((aw - 4) / count) - getTabMargin(), tabWidth),
        getMinTabWidth());

    for (int i = 0; i < count; i++) {
      El el = getItem(i).header.el();
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
    if (tabScroll && rendered) {
      autoScrollTabs();
    }
  }

  private int getFrameHeight() {
    int h = el().getFrameWidth("tb");
    h += bar.getHeight();
    return h;
  }

  private int getScollPos() {
    return stripWrap.getScrollLeft();
  }

  private int getScrollArea() {
    return Math.max(0, stripWrap.getClientWidth());
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
    if (item.header.isRendered()) {
      strip.insertChild(item.header.getElement(), index);
    } else {
      item.header.render(strip.dom, index);
    }
  }

  private void scrollTo(int pos, boolean animate) {
    if (animate) {
      stripWrap.scrollTo("left", pos, new FxConfig(new Listener<FxEvent>() {
        public void handleEvent(FxEvent fe) {
          updateScrollButtons();
        }
      }));
    } else {
      stripWrap.scrollTo("left", pos);
      updateScrollButtons();
    }
  }

  private void updateScrollButtons() {
    int pos = getScollPos();
    scrollLeft.setStyleName("x-tab-scroller-left-disabled", pos == 0);
    scrollRight.setStyleName("x-tab-scroller-right-disabled", pos >= (getScrollWidth()
        - getScrollArea() - 2));
  }
}
