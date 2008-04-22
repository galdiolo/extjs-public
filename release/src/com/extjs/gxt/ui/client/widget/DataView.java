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
import com.extjs.gxt.ui.client.util.Elements;
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

  public enum ViewSelectionMode {
    SINGLE, MULTI, SIMPLE;
  }

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

  /**
   * The container style name (defaults to 'x-view').
   */
  public String containerStyle = "x-view";

  /**
   * This is a required setting. A simple CSS selector (e.g. div.some-class or
   * span:first-child) that will be used to determine what nodes this DataView
   * will be working with (defaults to 'x-view-item').
   */
  public String itemSelector = ".x-view-item";

  /**
   * The style name to apply on mouse over (defaults to 'x-view-item-over').
   */
  public String overStyle = "fx-view-item-over";

  /**
   * The style to be applied to each selected item (defaults to
   * 'x-view-item-sel').
   */
  public String selectStyle = "x-view-item-sel";

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
   */
  public ViewSelectionMode selectionMode = ViewSelectionMode.SINGLE;

  /**
   * True to select the item when mousing over a element (defaults to false).
   */
  public boolean selectOnHover;

  /**
   * The default HTML frament to be used if no template is specified.
   */
  public String defaultTemplate = "<div class=x-view-item>{text}</div>";

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
          fly(elem).removeStyleName(selectStyle);
        }
      });
      selected.removeAll();
      last = -1;
      DataViewEvent dve = new DataViewEvent(this);
      fireEvent(Events.SelectionChange, dve);
    }
  }

  /**
   * Returns the number of records in the view.
   * 
   * @return the count
   */
  public int getItemCount() {
    return store.getCount();
  }

  public int getSelectedIndex() {
    if (selected.getCount() > 0) {
      return indexOf(selected.getElement(0));
    }
    return -1;
  }

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
        DataViewEvent dve = new DataViewEvent(this);
        dve.event = ce.event;
        fireEvent(Events.Click, dve);
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
        fly(elem).removeStyleName(overStyle);
        fly(elem).addStyleName(selectStyle);
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
   * Changes the data store bound to this view and refreshes it.
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
    if (index < all.getCount()) {
      Element elem = all.getElement(index);
      fly(elem).insertSibling(nodes, "before");
      all.insert(nodes, index);
    } else {
      fly(all.last()).insertSibling(nodes, "after");
      all.add(new Elements(nodes));
    }
  }

  protected void onBeforeLoad(StoreEvent se) {

  }

  protected void onClick(ComponentEvent be) {
    El el = be.getTarget(itemSelector, 10);
    if (el != null) {
      onItemClick(el.dom, indexOf(el.dom), be);
    }
  }

  protected void onDoubleClick(ComponentEvent ce) {
    El el = ce.getTarget(itemSelector, 10);
    if (el != null) {
      int index = indexOf(el.dom);
      Record record = store.getAt(index);
      fireEvent(Events.DoubleClick, record, el.dom, index, ce.event);
    }
  }

  protected void onItemClick(Element elem, int index, ComponentEvent ce) {
    if (selectionMode == ViewSelectionMode.MULTI) {
      doMultiSelect(elem, index, ce);
    } else if (selectionMode == ViewSelectionMode.SINGLE) {
      doSingleSelect(elem, index, ce);
    }
  }

  protected void onMouseOut(ComponentEvent ce) {
    El el = ce.getTarget(itemSelector, 10);
    if (el != null) {
      el.removeStyleName(overStyle);
    }
  }

  protected void onMouseOver(ComponentEvent ce) {
    El el = ce.getTarget(itemSelector, 10);
    if (el != null) {
      if (selectOnHover) {
        select(new NodeInfo(el.dom));
      } else {
        el.addStyleName(overStyle);
      }

    }
  }

  protected void onRemove(Record record, int index) {
    deselect(index);
    all.remove(index);
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);
    setStyleName(containerStyle);
    el.setStyleAttribute("overflow", "auto");

    singleSelect = selectionMode == ViewSelectionMode.SINGLE;
    multiSelect = selectionMode == ViewSelectionMode.MULTI;
    simpleSelect = selectionMode == ViewSelectionMode.SIMPLE;

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
      fly(all.getElement(index)).addStyleName(selectStyle);
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

    all = new CompositeElement(el.select(itemSelector));
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

    return fly(div).select(itemSelector);
  }

  private void deselectAll(boolean suppresEvent) {
    if (multiSelect || singleSelect) {
      selected.each(new CompositeFunction() {
        public void doFunction(Element elem, CompositeElement ce, int index) {
          ce.remove(elem);
          fly(elem).removeStyleName(selectStyle);
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
