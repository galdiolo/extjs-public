/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.data.BaseModelStringProvider;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListenerAdapter;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.DataView;
import com.extjs.gxt.ui.client.widget.DataView.ViewSelectionMode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A combobox control.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Expand</b> : FieldEvent(field)<br>
 * <div>Fires when the dropdown list is expanded.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : FieldEvent(field)<br>
 * <div>Fires when the dropdown list is collapsed.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeSelect</b> : FieldEvent(field)<br>
 * <div>Fires before a list item is selected.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : FieldEvent(field)<br>
 * <div>Fires when a list item is selected.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class ComboBox extends TriggerField {

  /**
   * The default HTML fragment for each option in the drop down.
   */
  public String defaultTemplate = "<div class=x-combo-item>{text}</div>";

  /**
   * The underlying data field name to bind to this ComboBox (defaults to
   * 'text').
   */
  public String displayField = "text";

  /**
   * CSS style name to apply to the selected item in the dropdown list (defaults
   * to 'x-combo-selected')
   */
  public String selectedStyle = "x-combo-selected";

  /**
   * The underlying data value name to bind to this ComboBox.
   */
  public String valueField;

  /**
   * The model string provider (defaults to {@link BaseModelStringProvider}.
   */
  public ModelStringProvider modelStringProvider = new BaseModelStringProvider();

  private boolean forceSelction;
  private String listAlign = "tl-bl?";
  private int maxHeight = 300;
  private int minListWidth = 70;
  private boolean editable = true;
  private BaseEventPreview eventPreview;
  private boolean expanded;
  private int selectedIndex = -1;
  private Store store;
  private StoreListenerAdapter storeListener;
  private Template template;
  private DataView view;
  private KeyNav keyNav;

  private Container<DataView> list;

  public ComboBox() {
    initComponent();
  }

  /**
   * Hides the dropdown list if it is currently expanded. Fires the <i>Collapse</i>
   * event on completion.
   */
  public void collapse() {
    if (!isExpanded()) {
      return;
    }
    expanded = false;
    list.hide();
    eventPreview.remove();

    fireEvent(Events.Collapse, new FieldEvent(this));
  }

  /**
   * Expands the dropdown list if it is currently hidden. Fires the <i>expand</i>
   * event on completion.
   */
  public void expand() {
    if (isExpanded()) {
      return;
    }
    expanded = true;

    Record r = findRecord(displayField, getRawValue());
    if (r != null) {
      view.select(store.indexOf(r));
    }
    list.el.setVisibility(false);
    list.show();
    restrictHeight();
    list.el.setVisibility(true);

    eventPreview.add();
    fireEvent(Events.Expand, new FieldEvent(this));
  }

  /**
   * Returns true if the field's value is forced to one of the value in the
   * list.
   * 
   * @return the force selection state
   */
  public boolean getForceSelction() {
    return forceSelction;
  }

  /**
   * Returns the list's list align value.
   * 
   * @return the list align valu
   */
  public String getListAlign() {
    return listAlign;
  }

  /**
   * Returns the dropdown list's max height.
   * 
   * @return the max height
   */
  public int getMaxHeight() {
    return maxHeight;
  }

  /**
   * Returns the dropdown list's min width.
   * 
   * @return the min width
   */
  public int getMinListWidth() {
    return minListWidth;
  }

  public Record getSelectedRecord() {
    if (selectedIndex > -1) {
      return store.getAt(selectedIndex);
    }
    return null;
  }

  /**
   * Returns the combo's store.
   * 
   * @return the store
   */
  public Store getStore() {
    return store;
  }

  /**
   * Returns <code>true</code> if the panel is expanded.
   * 
   * @return the expand state
   */
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Select an item in the dropdown list by its numeric index in the list. This
   * function does NOT cause the select event to fire. The list must expanded
   * for this function to work, otherwise use setValue.
   * 
   * @param index
   */
  public void select(int index) {
    selectedIndex = index;
    view.select(index);
  }

  /**
   * Allow or prevent the user from directly editing the field text. If false is
   * passed, the user will only be able to select from the items defined in the
   * dropdown list.
   * 
   * @param value true to allow the user to directly edit the field text
   */
  public void setEditable(boolean value) {
    if (value == this.editable) {
      return;
    }
    this.editable = value;
    if (rendered) {
      El fromEl = getInputEl();
      if (!value) {
        fromEl.setElementAttribute("readOnly", true);
        fromEl.addStyleName("x-combo-noedit");
      } else {
        fromEl.setElementAttribute("readOnly", false);
        fromEl.removeStyleName("x-combo-noedit");
      }
    }
  }

  /**
   * Sets the panel's expand state.
   * 
   * @param expand <code>true<code> true to expand
   */
  public void setExpanded(boolean expand) {
    this.expanded = expand;
    if (isRendered()) {
      if (expand) {
        expand();
      } else {
        collapse();
      }
    }
  }

  /**
   * Sets whether the combo's value is restricted to one of the values in the
   * list, false to allow the user to set arbitrary text into the field
   * (defaults to false).
   * 
   * @param forceSelction true to force selection
   */
  public void setForceSelction(boolean forceSelction) {
    this.forceSelction = forceSelction;
  }

  /**
   * Sets a valid anchor position value. See {@link El#alignTo} for details on
   * supported anchor positions (defaults to 'tl-bl?').
   * 
   * @param listAlign the new list align value
   */
  public void setListAlign(String listAlign) {
    this.listAlign = listAlign;
  }

  /**
   * Sets the maximum height in pixels of the dropdown list before scrollbars
   * are shown (defaults to 300).
   * 
   * @param maxHeight the max hieght
   */
  public void setMaxHeight(int maxHeight) {
    this.maxHeight = maxHeight;
  }

  /**
   * Sets the minimum width of the dropdown list in pixels (defaults to 70, will
   * be ignored if listWidth has a higher value).
   * 
   * @param minListWidth the min width
   */
  public void setMinListWidth(int minListWidth) {
    this.minListWidth = minListWidth;
  }

  /**
   * Sets the combo's store.
   * 
   * @param store the store
   */
  public void setStore(Store store) {
    this.store = store;
  }

  @Override
  public void setValue(Object value) {
    if (value instanceof String && valueField != null) {
      String v = value.toString();
      Record r = findRecord(valueField, v);
      if (r != null) {
        super.setValue(r.get(displayField));
      }
    } else if (value instanceof Record) {
      Record r = (Record) value;
      super.setValue(r.get(displayField));
    }
  }

  protected Record findRecord(String property, String value) {
    if (value == null) return null;
    for (Object r : store.getRecords()) {
      if (value.equals(getStringValue((Record) r, property))) {
        return (Record) r;
      }
    }
    return null;
  }

  @Override
  protected El getFocusEl() {
    return input;
  }

  protected String getStringValue(ModelData model, String propertyName) {
    return modelStringProvider.getStringValue(model, propertyName);
  }

  protected void initComponent() {
    storeListener = new StoreListenerAdapter() {
      public void beforeLoad(StoreEvent se) {
        onBeforeLoad(se);
      }

      public void load(StoreEvent se) {
        onLoad(se);
      }

      public void loadException(StoreEvent se) {
        collapse();
      }

    };

    eventPreview = new BaseEventPreview() {
      protected boolean onAutoHide(PreviewEvent ce) {
        if (list.getElement() == ce.getTarget()) {
          return false;
        }
        collapse();
        return true;
      }

      protected void onClick(PreviewEvent pe) {
        Element target = DOM.eventGetTarget(pe.event);
        if (DOM.isOrHasChild(view.getElement(), target)) {
          onViewClick(false);
        }
      }
    };

    keyNav = new KeyNav(this) {
      public void onDown(ComponentEvent ce) {
        if (!isExpanded()) {
          onTriggerClick(null);
        } else {
          selectNext();
        }
      }

      public void onEnter(ComponentEvent ce) {
        onViewClick(false);
      }

      public void onTab(ComponentEvent ce) {
        onViewClick(false);
      }

      public void onUp(ComponentEvent ce) {
        selectPrev();
      }

    };
  }

  protected void initList() {
    if (view == null) {
      final String style = "x-combo-list";

      list = new Container();
      list.shadow = true;
      list.setBorders(true);
      list.setStyleName(style);
      list.setScrollMode(Scroll.AUTO);
      list.hide();

      view = new DataView();
      view.selectOnHover = true;
      view.setBorders(false);
      view.setStyleAttribute("overflow", "hidden");
      view.addListener(Events.SelectionChange, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          selectedIndex = view.getSelectedIndex();
        }
      });
      list.add(view);
      RootPanel.get().add(list);
      list.layout(true);

      view.setStyleAttribute("backgroundColor", "white");

      if (template == null) {
        String t = "<div class=" + style + "-item>{" + displayField + "}</div>";
        template = new Template(t);
      }

      view.setTemplate(template);
      view.selectionMode = ViewSelectionMode.SINGLE;
      view.selectStyle = selectedStyle;
      view.itemSelector = "." + style + "-item";
    }

    bindStore(store, true);
  }

  protected void onBeforeLoad(StoreEvent se) {

  }

  @Override
  protected void onClick(ComponentEvent ce) {
    if (!editable && ce.getTarget() == getInputEl().dom) {
      onTriggerClick(ce);
      return;
    }
    super.onClick(ce);
  }

  protected void onDestroy() {
    super.onDestroy();
    keyNav.bind(null);
  }

  protected void onLoad(StoreEvent se) {
    if (store.getCount() > 0) {
      expand();
      restrictHeight();
    }
  }

  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    el.addEventsSunk(Event.KEYEVENTS);
    initList();

    if (!this.editable) {
      this.editable = true;
      this.setEditable(false);
    }

    eventPreview.ignoreList.add(getElement());
    eventPreview.ignoreList.add(view.getElement());
  }

  protected void onSelect(Record record, int index) {
    FieldEvent fe = new FieldEvent(this);
    if (fireEvent(Events.BeforeSelect, fe)) {
      setValue(record);
      collapse();
      fireEvent(Events.Select, fe);
    }
  }

  protected void onTriggerClick(ComponentEvent ce) {
    if (disabled) {
      return;
    }
    if (expanded) {
      collapse();
    } else {
      expand();
    }
    focus();
  }

  protected void onViewClick(boolean focus) {
    Record r = view.getSelection().get(0);
    if (r != null) {
      int index = store.indexOf(r);
      onSelect(r, index);
    }
    if (focus) {
      focus();
    }
  }

  private void bindStore(Store store, boolean initial) {
    if (this.store != null && !initial) {
      this.store.removeStoreListener(storeListener);
      if (store == null) {
        this.store = null;
        if (view != null) {
          view.setStore(null);
        }
      }
    }
    if (store != null) {
      this.store = store;
      store.addStoreListener(storeListener);
      if (view != null) {
        view.setStore(store);
      }
    }
  }

  private void restrictHeight() {
    int w = Math.max(getWidth(), minListWidth);
    list.setWidth(w);

    int fw = list.el.getFrameWidth("tb");
    int h = list.el.getHeight();
    h = Math.min(h, maxHeight - fw);
    list.el.alignTo(el.dom, listAlign, new int[] {0, -2});

    int y = list.el.getY();
    int b = y + h;
    int vh = XDOM.getViewportSize().height;
    if (b > vh) {
      y = y - (b - vh) - 5;
      list.el.setTop(y);
    }
    list.setHeight(h);
    list.el.sync(true);
  }

  private void selectNext() {
    int count = view.getItemCount();
    if (count > 0) {
      if (selectedIndex == -1) {
        select(0);
      } else if (selectedIndex < count - 1) {
        select(selectedIndex + 1);
      }
    }
  }

  private void selectPrev() {
    int count = view.getItemCount();
    if (count > 0) {
      if (selectedIndex == -1) {
        select(0);
      } else if (selectedIndex != 0) {
        select(selectedIndex - 1);
      }
    }
  }

}
