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
import com.extjs.gxt.ui.client.core.CompositeFunction;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DataViewEvent;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.StoreListenerAdapter;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A mechanism for displaying data using custom layout templates. DataView uses
 * an {@link Template} as its internal templating mechanism.
 * <p>
 * The view also provides built-in behavior for many common events that can
 * occur for its contained items including <i>Click</i>, <i>DoubleClick</i>,
 * <i>MouseOver</i>, <i>MouseOut</i>, etc. as well as a built-in selection
 * model. <b>In order to use these features, an {@link #itemSelector} config
 * must be provided for the DataView to determine what nodes it will be working
 * with.</b>
 * </p>
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeSelect</b> : DataViewEvent(view, index, element, event)<br>
 * <div>Fires before a click is processed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>view : this</li>
 * <li>index : the index of the target node</li>
 * <li>element : the target node</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : DataViewEvent(view, index, element, event)<br>
 * <div>Fires when a template node is selected.</div>
 * <ul>
 * <li>view : this</li>
 * <li>index : the index of the target node</li>
 * <li>element : the target node</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Click</b> : DataViewEvent(view, event)<br>
 * <div>Fires when a template node is clicked.</div>
 * <ul>
 * <li>view : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContainerClick</b> : DataViewEvent(view, event)<br>
 * <div>Fires when a click occurs and it is not on a template node.</div>
 * <ul>
 * <li>view : this</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DoubleClick</b> : DataViewEvent(view, index, element, event)<br>
 * <div>Fires when a template node is double clicked.</div>
 * <ul>
 * <li>view : this</li>
 * <li>index : the index of the target node</li>
 * <li>element : the target nodet</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : DataViewEvent(view, index, element, event)<br>
 * <div>Fires when a template node is right clicked.</div>
 * <ul>
 * <li>view : this</li>
 * <li>index : the index of the target node</li>
 * <li>element : the target node</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeSelect</b> : DataViewEvent(view, elements)<br>
 * <div>Fires before a selection is made. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>view : this</li>
 * <li>elements : the selected nodes</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SelectChange</b> : DataViewEvent(view)<br>
 * <div>Fires when the selected nodes change.</div>
 * <ul>
 * <li>view : this</li>
 * </ul>
 * </dd>
 * 
 * </dt>
 */
public class DataView extends BoxComponent {

  /**
   * Identifies an node in the view either by the model object, DOM element, or
   * index.
   */
  public static class NodeInfo {

    public Element elem;
    public int index;
    public Record record;

    public NodeInfo(Element elem) {
      this.elem = elem;
    }

    public NodeInfo(int index) {
      this.index = index;
    }

    public NodeInfo(Record record) {
      this.record = record;
    }
  }

  public enum ViewSelectionMode {
    SINGLE, MULTI, SIMPLE;
  }

  /**
   * The default HTML frament to be used if no template is specified.
   */
  public String defaultTemplate = "<div class=x-view-item>{text}</div>";

  /**
   * The style to be applied to each selected item (defaults to
   * 'x-view-item-sel').
   */
  private String selectStyle = "x-view-item-sel";

  /**
   * Sets the container style name (defaults to 'x-view').
   */
  private String containerStyle = "x-view";

  /**
   * The style name to apply on mouse over (defaults to 'x-view-item-over').
   */
  private String overStyle = "x-view-item-over";

  /**
   * This is a required setting. A simple CSS selector (e.g. div.some-class or
   * span:first-child) that will be used to determine what nodes this DataView
   * will be working with (defaults to 'x-view-item').
   */
  private String itemSelector = ".x-view-item";

  private ViewSelectionMode selectionMode = ViewSelectionMode.SINGLE;
  private boolean selectOnHover;
  private int last = -1;
  private CompositeElement all, selected;
  private boolean singleSelect, multiSelect, simpleSelect;
  private Store store;
  private Template template;
  private boolean initial;
  private StoreListener storeListener;

  /**
   * Creates a new template list.
   */
  public DataView() {
    initComponent();
  }

  /**
   * Creates a new template list.
   * 
   * @param template the template
   */
  public DataView(Template template) {
    this();
    this.template = template;
  }

  /**
   * Deselects a node.
   * 
   * @param index the index of the node to deselect
   */
  public void deselect(int index) {
    deselect(new NodeInfo(index));
  }

  /**
   * Deselects a node.
   * 
   * @param node the node to deselect
   */
  public void deselect(NodeInfo node) {
    if (isSelected(node)) {
      selected.remove(getNode(node));
    }
  }

  /**
   * Deselects all nodes.
   */
  public void deselectAll() {
    if (multiSelect || singleSelect) {
      selected.each(new CompositeFunction() {
        public void doFunction(Element elem, CompositeElement ce, int index) {
          fly(elem).removeStyleName(getSelectStyle());
        }
      });
      selected.removeAll();
      last = -1;
      DataViewEvent dve = new DataViewEvent(this);
      fireEvent(Events.SelectionChange, dve);
    }
  }

  /**
   * Returns the container style name.
   * 
   * @return the style name
   */
  public String getContainerStyle() {
    return containerStyle;
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
   * Returns the number of records in the view.
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

  /**
   * Returns the over style.
   * 
   * @return the over style
   */
  public String getOverStyle() {
    return overStyle;
  }

  /**
   * Returns the first selected index.
   * 
   * @return the selected index
   */
  public int getSelectedIndex() {
    if (selected.getCount() > 0) {
      return indexOf(selected.getElement(0));
    }
    return -1;
  }

  /**
   * Returns the selected indexes.
   * 
   * @return the selected indexes
   */
  public int[] getSelectedIndexes() {
    int[] indexes = new int[selected.getCount()];
    for (int i = 0; i < indexes.length; i++) {
      indexes[i] = indexOf(selected.getElement(i));
    }
    return indexes;
  }

  public List<Record> getSelection() {
    final List<Record> list = new ArrayList<Record>();
    selected.each(new CompositeFunction() {
      public void doFunction(Element elem, CompositeElement ce, int index) {
        int idx = indexOf(elem);
        list.add(store.getAt(idx));
      }
    });
    return list;
  }

  /**
   * Returns the selection mode
   * 
   * @return the selection mode
   */
  public ViewSelectionMode getSelectionMode() {
    return selectionMode;
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
   * Returns the list's template.
   * 
   * @return the template
   */
  public Template getTemplate() {
    return template;
  }

  /**
   * Reuturns the index of the element.
   * 
   * @param element the element
   * @return the index
   */
  public int indexOf(Element element) {
    return all.indexOf(element);
  }

  /**
   * Returns the index of the node
   * 
   * @param node the node
   * @return the index
   */
  public int indexOf(NodeInfo node) {
    Element elem = getNode(node);
    return indexOf(elem);
  }

  /**
   * Returns true if the node is selected.
   * 
   * @param node the node
   * @return the select state
   */
  public boolean isSelected(NodeInfo node) {
    return selected.contains(getNode(node));
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    switch (ce.type) {
      case Event.ONMOUSEOVER:
        onMouseOver(ce);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(ce);
        break;
      case Event.ONCLICK:
        onClick(ce);
        fireEvent(Events.Select, ce);
        break;
    }
  }

  /**
   * Refreshes the view by reloading the data from the store and re-rendering
   * the template.
   */
  public void refresh() {
    deselectAll(false);
    el.setInnerHtml("");
    if (all != null) {
      all.removeAll();
    }
    renderAll();
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
   * Selected a node.
   * 
   * @param index the index of the node to select
   */
  public void select(int index) {
    select(new NodeInfo(index));
  }

  /**
         * Selects a node.
         * 
         * @param node the node to select
         */
  public void select(NodeInfo node) {
    select(node, false);
  }

  /**
                 * Selects a node.
                 * 
                 * @param node the node to select
                 * @param keepExisting true to keep existing selections
                 */
  public void select(NodeInfo node, boolean keepExisting) {
    Element elem = getNode(node);
    if (!keepExisting) {
      deselectAll(true);
    }
    if (elem != null && !isSelected(node)) {
      DataViewEvent dve = new DataViewEvent(this);
      if (fireEvent(Events.BeforeSelect, dve)) {
        fly(elem).removeStyleName(getOverStyle());
        fly(elem).addStyleName(getSelectStyle());
        selected.add(elem);
        last = indexOf(elem);
        fireEvent(Events.SelectionChange, dve);
      }
    }
  }

  /**
   * Selects a range of nodes.
   * 
   * @param start the index of the first node in the range
   * @param end the index of the last node in the range
   * @param keepSelected true to retain existing selections
   */
  public void selectRange(int start, int end, boolean keepSelected) {
    if (!keepSelected) {
      deselectAll(true);
    }
  }

  /**
   * Sets the container style name (defaults to 'x-view').
   * 
   * @param containerStyle the container style name
   */
  public void setContainerStyle(String containerStyle) {
    this.containerStyle = containerStyle;
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
   * Sets the selection mode (defaults to SINGLE).
   * <p>
   * Valid values are:
   * <ul>
   * <li>SINGLE - single selection</li>
   * <li>MULTI - multi selection</li>
   * <li>SIMPLE - multiselection by clicking on multiple items without
   * requiring the user to hold Shift or Ctrl, false to force the user to hold
   * Ctrl or Shift to select more than on item</li>
   * </ul>
   * </p>
   * 
   * @param selectionMode the selection mode
   */
  public void setSelectionMode(ViewSelectionMode selectionMode) {
    this.selectionMode = selectionMode;
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
                               * Changes the data store bound to this view and
                               * refreshes it.
                               * 
                               * @param store the store to bind this view
                               */
  public void setStore(Store store) {
    if (!initial && this.store != null) {
      this.store.removeStoreListener(storeListener);
    }
    if (store != null) {
      store.addStoreListener(storeListener);
    }
    this.store = store;
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
    setTemplate(new Template(html));
  }

  /**
                   * Sets the view's template.
                   * 
                   * @param template the template
                   */
  public void setTemplate(Template template) {
    this.template = template;
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new DataViewEvent(this);
  }

  protected void doMultiSelect(Element elem, int index, ComponentEvent ce) {
    NodeInfo info = new NodeInfo(elem);
    if (ce.isShiftKey() && last != -1) {
      int l = last;
      selectRange(last, index, ce.isControlKey());
      last = l;
    } else {
      if ((ce.isControlKey() || simpleSelect) && isSelected(info)) {
        deselect(info);
      } else {
        select(new NodeInfo(index),
            (ce.isControlKey() || ce.isShiftKey() || simpleSelect));
      }
    }
  }

  protected void doSingleSelect(Element elem, int index, ComponentEvent be) {
    NodeInfo info = new NodeInfo(index);
    if (be.isControlKey() && isSelected(info)) {
      deselect(info);
    } else {
      select(info);
    }
  }

  protected void fireEvent(int type, Object item, Element elem, int index, Event event) {
    DataViewEvent dve = new DataViewEvent(this);
    dve.element = elem;
    dve.index = index;
    dve.event = event;
    fireEvent(type, dve);
  }

  protected void initComponent() {
    storeListener = new StoreListenerAdapter() {
      public void add(StoreEvent se) {
        onAdd(se.records, se.index);
      }

      public void beforeLoad(StoreEvent se) {
        onBeforeLoad(se);
      }

      public void clear(StoreEvent se) {
        refresh();
      }

      public void dataChanged(StoreEvent se) {
        refresh();
      }

      public void remove(StoreEvent se) {
        onRemove(se.record, se.index);
      }

      public void update(StoreEvent se) {
        onUpdate(se.record, se.index);
      }

    };
  }

  protected void onAdd(List<Record> records, int index) {
    Element[] nodes = bufferRender(records);
    el.insertChild(nodes, index);
    all.insert(nodes, index);
  }

  protected void onBeforeLoad(StoreEvent se) {

  }

  protected void onClick(ComponentEvent be) {
    El el = be.getTarget(getItemSelector(), 10);
    if (el != null) {
      onItemClick(el.dom, indexOf(el.dom), be);
    }
  }

  protected void onDoubleClick(ComponentEvent ce) {
    El el = ce.getTarget(getItemSelector(), 10);
    if (el != null) {
      int index = indexOf(el.dom);
      Record record = store.getAt(index);
      fireEvent(Events.DoubleClick, record, el.dom, index, ce.event);
    }
  }

  protected void onItemClick(Element elem, int index, ComponentEvent ce) {
    if (getSelectionMode() == ViewSelectionMode.MULTI) {
      doMultiSelect(elem, index, ce);
    } else if (getSelectionMode() == ViewSelectionMode.SINGLE) {
      doSingleSelect(elem, index, ce);
    }
  }

  protected void onMouseOut(ComponentEvent ce) {
    El el = ce.getTarget(getItemSelector(), 10);
    if (el != null) {
      el.removeStyleName(getOverStyle());
    }
  }

  protected void onMouseOver(ComponentEvent ce) {
    El el = ce.getTarget(getItemSelector(), 10);
    if (el != null) {
      if (getSelectOnOver()) {
        select(new NodeInfo(el.dom));
      } else {
        el.addStyleName(getOverStyle());
      }

    }
  }

  protected void onRemove(Record record, int index) {
    deselect(index);
    if (all != null) {
      all.remove(index);
    }
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);
    setStyleName(getContainerStyle());
    el.setStyleAttribute("overflow", "auto");

    singleSelect = getSelectionMode() == ViewSelectionMode.SINGLE;
    multiSelect = getSelectionMode() == ViewSelectionMode.MULTI;
    simpleSelect = getSelectionMode() == ViewSelectionMode.SIMPLE;

    selected = new CompositeElement();

    if (store != null) {
      renderAll();
    }

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS);
  }

  protected void onUpdate(Record record, int index) {
    boolean sel = isSelected(new NodeInfo(index));
    Element original = all.getElement(index);
    List<Record> records = new ArrayList<Record>();
    records.add(record);
    Element node = bufferRender(records)[0];
    all.replaceElement(original, node);
    if (sel) {
      selected.replaceElement(original, node);
      fly(all.getElement(index)).addStyleName(getSelectStyle());
    }
  }

  protected void renderAll() {
    if (template == null) {
      template = new Template(defaultTemplate);
    }

    StringBuffer sb = new StringBuffer();
    int size = store.getCount();
    for (int i = 0; i < size; i++) {
      Record item = store.getAt(i);
      sb.append(renderItem(item));
    }
    el.setInnerHtml(sb.toString());

    all = new CompositeElement(el.select(getItemSelector()));
  }

  protected String renderItem(Record record) {
    return template.applyTemplate(record.getJsObject());
  }

  private Element[] bufferRender(List<Record> records) {
    StringBuffer sb = new StringBuffer();
    for (Record r : records) {
      sb.append(template.applyTemplate(r.getJsObject()));
    }
    Element div = DOM.createDiv();
    fly(div).setInnerHtml(sb.toString());

    return fly(div).select(getItemSelector());
  }

  private void deselectAll(boolean suppresEvent) {
    if (multiSelect || singleSelect) {
      selected.each(new CompositeFunction() {
        public void doFunction(Element elem, CompositeElement ce, int index) {
          ce.remove(elem);
          fly(elem).removeStyleName(getSelectStyle());
        }
      });
      last = -1;
      if (!suppresEvent) {
        DataViewEvent dve = new DataViewEvent(this);
        fireEvent(Events.SelectionChange, dve);
      }
    }
  }

  private Element getNode(NodeInfo node) {
    if (node.elem != null) {
      return node.elem;
    } else if (node.index != -1) {
      return (Element) all.getElement(node.index);
    } else if (node.record != null) {
      return (Element) all.getElement(store.indexOf(node.record));
    }
    return null;
  }

}
