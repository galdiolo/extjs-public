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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.CompositeElement;
import com.extjs.gxt.ui.client.core.DomQuery;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A mechanism for displaying data using custom layout templates. ListView uses
 * an {@link XTemplate} as its internal templating mechanism.
 * <p>
 * <b>In order to use these features, an {@link #setItemSelector(String)} must
 * be provided for the ListView to determine what nodes it will be working with.</b>
 * </p>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Select</b> : ListViewEvent(listView, event)<br>
 * <div>Fires when a template node is clicked.</div>
 * <ul>
 * <li>listView : this</li>
 * <li>event : the dom event</li>
 * <li>index : the index of the target node</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DoubleClick</b> : ListViewEvent(listView, index, element, event)<br>
 * <div>Fires when a template node is double clicked.</div>
 * <ul>
 * <li>listView : this</li>
 * <li>index : the index of the target node</li>
 * <li>element : the target nodet</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : ListViewEvent(listView, index, element, event)<br>
 * <div>Fires when a template node is right clicked.</div>
 * <ul>
 * <li>listView : this</li>
 * <li>index : the index of the target node</li>
 * <li>element : the target node</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * </dt>
 */
public class ListView<M extends ModelData> extends BoxComponent {

  private ToolTip toolTip;
  private String selectStyle = "x-view-selected";
  private String overStyle = "";
  private String itemSelector = ".x-view-item";
  private boolean selectOnHover;
  private CompositeElement all;
  private ListStore<M> store;
  private ListViewSelectionModel<M> sm;
  private XTemplate template;
  private boolean initial;
  private StoreListener storeListener;
  private String displayProperty = "text";
  private boolean enableTips = false;
  private String loadingText;

  /**
   * Creates a new view.
   */
  public ListView() {
    initComponent();
    setSelectionModel(new ListViewSelectionModel<M>());
    all = new CompositeElement();
    baseStyle = "x-view";
  }

  /**
   * Creates a new view.
   */
  public ListView(ListStore<M> store) {
    this();
    setStore(store);
  }

  /**
   * Creates a new template list.
   * 
   * @param template the template
   */
  public ListView(ListStore<M> store, XTemplate template) {
    this(store);
    this.template = template;
  }

  /**
   * Returns the display property.
   * 
   * @return the display property
   */
  public String getDisplayProperty() {
    return displayProperty;
  }

  /**
   * Returns the element at the given index.
   * 
   * @param index the index
   * @return the element
   */
  public Element getElement(int index) {
    return all.getElement(index);
  }

  /**
   * Returns all of the child elements.
   * 
   * @return the elements
   */
  public List<Element> getElements() {
    return all.getElements();
  }

  /**
   * Returns the number of models in the view.
   * 
   * @return the count
   */
  public int getItemCount() {
    return store.getCount();
  }

  /**
   * Returns the item selector.
   * 
   * @return the selector
   */
  public String getItemSelector() {
    return itemSelector;
  }

  public String getLoadingText() {
    return loadingText;
  }

  /**
   * Returns the over style.
   * 
   * @return the over style
   */
  public String getOverStyle() {
    return overStyle;
  }

  /**
   * Returns the view's selection model.
   * 
   * @return the selection model
   */
  public ListViewSelectionModel<M> getSelectionModel() {
    return sm;
  }

  /**
   * Returns true if select on hover is enabled.
   * 
   * @return the select on hover state
   */
  public boolean getSelectOnOver() {
    return selectOnHover;
  }

  /**
   * Returns the select style.
   * 
   * @return the select style
   */
  public String getSelectStyle() {
    return selectStyle;
  }

  /**
   * Returns the combo's stote.
   * 
   * @return the store
   */
  public ListStore<M> getStore() {
    return store;
  }

  /**
   * Returns the list's template.
   * 
   * @return the template
   */
  public XTemplate getTemplate() {
    return template;
  }

  /**
   * Reuturns the index of the element.
   * 
   * @param element the element
   * @return the index
   */
  public int indexOf(Element element) {
    if (element.getPropertyString("viewIndex") != null) {
      return element.getPropertyInt("viewIndex");
    }
    return all.indexOf(element);
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    ListViewEvent le = (ListViewEvent) ce;
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        if (le.index != -1) {
          Element item = all.getElement(le.index);
          Element from = DOM.eventGetFromElement(ce.event);
          if (from != null && !DOM.isOrHasChild(item, from)) {
            onMouseOver(le);
            onMouseAction(le);
          }
        }
        break;
      case Event.ONMOUSEOUT:
        if (le.index != -1) {
          Element item = all.getElement(le.index);
          Element to = DOM.eventGetToElement(ce.event);
          if (to != null && !DOM.isOrHasChild(item, to)) {
            onMouseOut(le);
            onMouseAction(le);
          }
        }
        break;
      case Event.ONCLICK:
        onClick(ce);
        break;
    }
  }

  /**
   * Refreshes the view by reloading the data from the store and re-rendering
   * the template.
   */
  public void refresh() {
    if (!rendered) {
      return;
    }
    el().setInnerHtml("");
    List models = store.getModels();
    if (models.size() < 1) {
      all.removeAll();
      return;
    }
    template.overwrite(getElement(), Util.getJsObjects(collectData(models, 0),
        template.getMaxDepth()));
    all = new CompositeElement(el().select(itemSelector));
    updateIndexes(0, -1);
    fireEvent(Events.Refresh);
  }

  /**
   * Refreshes an individual node's data from the store.
   * 
   * @param index the items data index in the store
   */
  public void refreshNode(int index) {
    onUpdate(store.getAt(index), index);
  }

  /**
   * Sets the display property. Applies when using the default template for each
   * item's text.
   * 
   * @param displayProperty the display property
   */
  public void setDisplayProperty(String displayProperty) {
    this.displayProperty = displayProperty;
  }

  /**
   * This is a required setting. A simple CSS selector (e.g. div.some-class or
   * span:first-child) that will be used to determine what nodes this DataView
   * will be working with (defaults to 'x-view-item').
   * 
   * @param itemSelector the item selector
   */
  public void setItemSelector(String itemSelector) {
    this.itemSelector = itemSelector;
  }

  public void setLoadingText(String loadingText) {
    this.loadingText = loadingText;
  }

  /**
   * Sets the style name to apply on mouse over (defaults to
   * 'x-view-item-over').
   * 
   * @param overStyle the over style
   */
  public void setOverStyle(String overStyle) {
    this.overStyle = overStyle;
  }

  /**
   * Sets the selection model.
   * 
   * @param sm the selection model
   */
  public void setSelectionModel(ListViewSelectionModel<M> sm) {
    if (this.sm != null) {
      this.sm.bindList(null);
    }
    this.sm = sm;
    if (sm != null) {
      sm.bindList(this);
    }
  }

  /**
   * True to select the item when mousing over a element (defaults to false).
   * 
   * @param selectOnHover true to select on mouse over
   */
  public void setSelectOnOver(boolean selectOnHover) {
    this.selectOnHover = selectOnHover;
  }

  /**
   * The style to be applied to each selected item (defaults to
   * 'x-view-item-sel').
   * 
   * @param selectStyle the select style
   */
  public void setSelectStyle(String selectStyle) {
    this.selectStyle = selectStyle;
  }

  /**
   * Changes the data store bound to this view and refreshes it.
   * 
   * @param store the store to bind this view
   */
  public void setStore(ListStore store) {
    if (!initial && this.store != null) {
      this.store.removeStoreListener(storeListener);
    }
    if (store != null) {

      store.addStoreListener(storeListener);
    }
    this.store = store;
    sm.bindList(this);

    if (store != null && isRendered()) {
      refresh();
    }
  }

  /**
   * Sets the view's template.
   * 
   * @param html the HTML fragment
   */
  public void setTemplate(String html) {
    setTemplate(XTemplate.create(html));
  }

  /**
   * Sets the view's template.
   * 
   * @param template the template
   */
  public void setTemplate(XTemplate template) {
    this.template = template;
  }

  protected List collectData(List<M> models, int startIndex) {
    List<M> list = new ArrayList<M>();
    for (int i = 0, len = models.size(); i < len; i++) {
      list.add(prepareData(models.get(i)));
    }
    return list;
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    ListViewEvent e = new ListViewEvent(this, event);
    e.index = -1;
    if (event != null) {
      El el = e.getTarget(itemSelector, 10);
      if (el != null) {
        int idx = indexOf(el.dom);
        if (idx != -1) {
          e.index = indexOf(el.dom);
          e.element = all.getElement(e.index);
        }
      }
    }
    return e;
  }

  protected void focusItem(int index) {
    Element elem = all.getElement(index);
    if (elem != null) {
      fly(elem).scrollIntoView(getElement(), false);
    }
  }

  protected void initComponent() {
    storeListener = new StoreListener() {
      @Override
      public void storeAdd(StoreEvent se) {
        onAdd(se.models, se.index);
      }

      @Override
      public void storeBeforeDataChanged(StoreEvent se) {
        onBeforeLoad();
      }

      @Override
      public void storeClear(StoreEvent se) {
        refresh();
      }

      @Override
      public void storeDataChanged(StoreEvent se) {
        refresh();
      }

      @Override
      public void storeFilter(StoreEvent se) {
        refresh();
      }

      @Override
      public void storeRemove(StoreEvent se) {
        onRemove(se.model, se.index);
      }

      @Override
      public void storeSort(StoreEvent se) {
        refresh();
      }

      @Override
      public void storeUpdate(StoreEvent se) {
        onUpdate((M) se.model, se.index);
      }

    };
  }

  protected void onAdd(List<M> models, int index) {
    Element[] nodes = bufferRender(models);
    el().insertChild(nodes, index);
    all.insert(nodes, index);
  }

  protected void onBeforeLoad() {
    if (loadingText != null) {
      if (rendered) {
        el().setInnerHtml("<div class='loading-indicator'>" + loadingText + "</div>");
      }
      all.removeAll();
    }
  }

  protected void onClick(ComponentEvent be) {
    El el = be.getTarget(itemSelector, 10);
    if (el != null) {
      fireEvent(Events.Select, be);
    }
  }

  protected void onMouseAction(ListViewEvent le) {
    if (le.index != -1) {
      Element item = all.getElement(le.index);
      String tip = item.getAttribute("qtip");
      if (tip != null && (tip.equals("null") || tip.equals(""))) {
        tip = null;
      }
      
      if (tip != null) {
        enableTips = true;
      }
      
      if (!enableTips) {
        return;
      }
   
      if (toolTip == null) {
        toolTip = getToolTip();
        if (toolTip == null) {
          toolTip = new ToolTip(this);
        }
      }
      ToolTipConfig config = toolTip.getConfig();
      config.setTarget(item);
      config.setText(item.getAttribute("qtip"));
      config.setTitle(item.getAttribute("qtitle"));
      config.setEnabled(tip != null);
      toolTip.update(config);
    }

  }

  protected void onMouseOut(ListViewEvent ce) {
    if (ce.index != -1) {
        fly(all.getElement(ce.index)).removeStyleName(overStyle);
    }
  }

  protected void onMouseOver(ListViewEvent ce) {
    if (ce.index != -1) {
      if (selectOnHover) {
        sm.select(ce.index);
      } else {
        fly(all.getElement(ce.index)).addStyleName(overStyle);
      }
    }
  }

  protected void onRemove(ModelData data, int index) {
    if (all != null) {
      all.remove(index);
    }
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);
    el().setStyleAttribute("overflow", "auto");
    el().setStyleAttribute("padding", "0px");

    if (store != null && store.getCount() > 0) {
      refresh();
    }

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS);
  }

  protected void onSelectChange(M model, boolean select) {
    if (rendered && all != null) {
      int index = store.indexOf(model);
      if (index != -1) {
        if (select) {
          fly(all.getElement(index)).addStyleName(selectStyle);
        } else {
          fly(all.getElement(index)).removeStyleName(selectStyle);
        }
      }
    }
  }

  protected void onUpdate(M model, int index) {
    Element original = all.getElement(index);
    List list = Util.createList(model);
    Element node = bufferRender(list)[0];
    all.replaceElement(original, node);
  }

  protected M prepareData(M model) {
    return model;
  }

  private Element[] bufferRender(List<M> models) {
    Element div = DOM.createDiv();
    template.overwrite(div,
        Util.getJsObjects(collectData((List) models, 0), template.getMaxDepth()));
    return DomQuery.select(itemSelector, div);
  }

  private void updateIndexes(int startIndex, int endIndex) {
    List<Element> elems = all.getElements();
    endIndex = endIndex == -1 ? elems.size() - 1 : endIndex;
    for (int i = startIndex; i <= endIndex; i++) {
      elems.get(i).setPropertyInt("viewIndex", i);
    }
  }

}
