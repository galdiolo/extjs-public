/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.DataViewBinder;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.data.BaseModelStringProvider;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionProvider;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.DataView;
import com.extjs.gxt.ui.client.widget.DataViewItem;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
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
 * 
 * @param <D> the model data type
 */
public class ComboBox<D extends ModelData> extends TriggerField<D> implements SelectionProvider<D> {

  /**
   * ComboBox error messages.
   */
  public class ComboBoxMessages extends TextFieldMessages {

    private String valueNoutFoundText;

    /**
     * Returns the value not found error text.
     * 
     * @return the error text
     */
    public String getValueNoutFoundText() {
      return valueNoutFoundText;
    }

    /**
     * When using a name/value combo, if the value passed to setValue is not
     * found in the store, valueNotFoundText will be displayed as the field text
     * if defined.
     * 
     * @param valueNoutFoundText
     */
    public void setValueNoutFoundText(String valueNoutFoundText) {
      this.valueNoutFoundText = valueNoutFoundText;
    }

  }

  /**
   * CSS style name to apply to the selected item in the dropdown list (defaults
   * to 'x-combo-selected')
   */
  protected String selectedStyle = "x-combo-selected";

  private ModelStringProvider modelStringProvider;
  private String valueField;
  private boolean forceSelection;
  private String listAlign = "tl-bl?";
  private int maxHeight = 300;
  private int minListWidth = 70;
  private boolean editable = true;
  private BaseEventPreview eventPreview;
  private boolean expanded;
  private DataViewItem selectedItem;
  private ListStore<D> store;
  private StoreListener storeListener;
  private Template template;
  private DataView view;
  private DataViewBinder<D> binder;
  private String lastSelectionText;
  private LayoutContainer list;
  private boolean ignoreNext;

  /**
   * Creates a combo box.
   */
  public ComboBox() {
    messages = new ComboBoxMessages();
    modelStringProvider = new BaseModelStringProvider();
    setPropertyEditor(new ListModelPropertyEditor<D>());
    initComponent();
  }

  public void addSelectionChangedListener(SelectionChangedListener listener) {
    addListener(Events.SelectionChange, listener);
  }

  /**
   * Clears any text/value currently set in the field.
   */
  public void clearSelections() {
    setRawValue("");
    lastSelectionText = "";
    applyEmptyText();
    value = null;
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
    list.setHeight("auto");
    list.hide();
    eventPreview.remove();
    view.getSelectionModel().deselectAll();

    fireEvent(Events.Collapse, new FieldEvent(this));
  }

  /**
   * Expands the dropdown list if it is currently hidden. Fires the <i>expand</i>
   * event on completion.
   */
  public void expand() {
    if (expanded || (store != null && store.getCount() == 0)) {
      return;
    }
    expanded = true;

    D r = findModel(getDisplayField(), getRawValue());
    if (r != null) {
      binder.setSelection(r);
    }
    list.el().setVisibility(false);
    list.el().updateZIndex(0);
    list.show();
    restrict();
    list.el().setVisibility(true);

    eventPreview.add();
    fireEvent(Events.Expand, new FieldEvent(this));
  }

  /**
   * Returns the combos data view.
   * 
   * @return the view
   */
  public DataView getDataView() {
    return view;
  }

  /**
   * Returns the display field.
   * 
   * @return the display field
   */
  public String getDisplayField() {
    return getPropertyEditor().getDisplayProperty();
  }

  /**
   * Returns true if the field's value is forced to one of the value in the
   * list.
   * 
   * @return the force selection state
   */
  public boolean getForceSelection() {
    return forceSelection;
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

  @Override
  public ComboBoxMessages getMessages() {
    return (ComboBoxMessages) messages;
  }

  /**
   * Returns the dropdown list's min width.
   * 
   * @return the min width
   */
  public int getMinListWidth() {
    return minListWidth;
  }

  /**
   * Returns the model string provider.
   * 
   * @return the model string provider
   */
  public ModelStringProvider<D> getModelStringProvider() {
    return modelStringProvider;
  }

  @Override
  public ListModelPropertyEditor<D> getPropertyEditor() {
    return (ListModelPropertyEditor<D>) propertyEditor;
  }

  /**
   * Returns the selected style.
   * 
   * @return the selected style
   */
  public String getSelectedStyle() {
    return selectedStyle;
  }

  public List<D> getSelection() {
    List<D> sel = new ArrayList<D>();
    D v = getValue();
    if (v != null) {
      sel.add(v);
    }
    return sel;
  }

  /**
   * Returns the combo's store.
   * 
   * @return the store
   */
  public ListStore<D> getStore() {
    return store;
  }

  /**
   * Returns the custom template.
   * 
   * @return the template
   */
  public Template getTemplate() {
    if (template == null) {
      String t = "<div id='{id}' class='x-combo-list-item'>{" + getDisplayField() + "}</div>";
      template = new Template(t);
    }
    return template;
  }

  @Override
  public D getValue() {
    if (store != null) {
      getPropertyEditor().setList(store.getModels());
    }
    return super.getValue();
  }

  /**
   * Returns the combo's value field.
   * 
   * @return the value field
   */
  public String getValueField() {
    return valueField;
  }

  /**
   * Returns <code>true</code> if the panel is expanded.
   * 
   * @return the expand state
   */
  public boolean isExpanded() {
    return expanded;
  }

  public void removeSelectionListener(SelectionChangedListener listener) {
    removeListener(Events.SelectionChange, listener);
  }

  /**
   * Select an item in the dropdown list by its numeric index in the list. This
   * function does NOT cause the select event to fire. The list must expanded
   * for this function to work, otherwise use #setValue.
   * 
   * @param index the index of the item to select
   */
  public void select(int index) {
    if (view != null) {
      DataViewItem item = view.getItem(index);
      if (item != null) {
        selectedItem = item;
        view.setSelectedItem(item);
        view.scrollIntoView(selectedItem);
      }
    }
  }

  /**
   * The underlying data field name to bind to this ComboBox (defaults to
   * 'text').
   * 
   * @param displayField the display field
   */
  public void setDisplayField(String displayField) {
    getPropertyEditor().setDisplayProperty(displayField);
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
        fromEl.dom.setPropertyBoolean("readOnly", true);
        fromEl.addStyleName("x-combo-noedit");
      } else {
        fromEl.dom.setPropertyBoolean("readOnly", false);
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
   * @param forceSelection true to force selection
   */
  public void setForceSelection(boolean forceSelection) {
    this.forceSelection = forceSelection;
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
   * Sets the model string provider (defaults to {@link BaseModelStringProvider}.
   * 
   * @param modelStringProvider the string provider
   */
  public void setModelStringProvider(ModelStringProvider<D> modelStringProvider) {
    this.modelStringProvider = modelStringProvider;
  }

  @Override
  public void setPropertyEditor(PropertyEditor<D> propertyEditor) {
    assert propertyEditor instanceof ListModelPropertyEditor : "PropertyEditor must be a ModelPropertyEditor instance";
    super.setPropertyEditor(propertyEditor);
  }

  @Override
  public void setRawValue(String text) {
    if (text == null) {
      String msg = getMessages().getValueNoutFoundText();
      text = msg != null ? msg : "";
    }
    getInputEl().setValue(text);
  }

  /**
   * Sets the CSS style name to apply to the selected item in the dropdown list
   * (defaults to 'x-combo-selected').
   * 
   * @param selectedStyle the selected style
   */
  public void setSelectedStyle(String selectedStyle) {
    this.selectedStyle = selectedStyle;
  }

  public void setSelection(List<D> selection) {
    if (selection.size() > 0) {
      setValue(selection.get(0));
    } else {
      setValue(null);
    }
  }

  /**
   * Sets the combo's store.
   * 
   * @param store the store
   */
  public void setStore(ListStore<D> store) {
    this.store = store;
  }

  /**
   * Sets the template to be used to render each item in the drop down.
   * 
   * @param template the template
   */
  public void setTemplate(Template template) {
    assertPreRender();
    this.template = template;
  }

  @Override
  public void setValue(D value) {
    super.setValue(value);
    this.lastSelectionText = getRawValue();
    SelectionChangedEvent se = new SelectionChangedEvent(this, getSelection());
    fireEvent(Events.SelectionChange, se);
  }

  /**
   * The underlying data value name to bind to this ComboBox.
   * 
   * @param valueField the value field
   */
  public void setValueField(String valueField) {
    this.valueField = valueField;
  }

  protected void doForce() {
    if (getValue() == null) {
      setRawValue(lastSelectionText != null ? lastSelectionText : "");
    }
  }

  protected D findModel(String property, String value) {
    if (value == null) return null;
    for (D model : store.getModels()) {
      if (value.equals(getStringValue(model, property))) {
        return model;
      }
    }
    return null;
  }

  @Override
  protected El getFocusEl() {
    return input;
  }

  protected String getStringValue(D model, String propertyName) {
    return getModelStringProvider().getStringValue(model, propertyName);
  }

  protected void initComponent() {
    storeListener = new StoreListener() {

      @Override
      public void storeBeforeDataChanged(StoreEvent se) {
        onBeforeLoad(se);
      }

      @Override
      public void storeDataChanged(StoreEvent se) {
        onLoad(se);
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

    new KeyNav(this) {
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
      String style = "x-combo-list";

      list = new LayoutContainer();
      list.setShim(true);
      list.setShadow(true);
      list.setBorders(true);
      list.setStyleName(style);
      list.setScrollMode(Scroll.AUTO);
      list.hide();

      assert store != null;

      view = new DataView();

      view.setSelectOnOver(true);
      view.setBorders(false);
      view.setStyleAttribute("overflow", "hidden");
      view.addListener(Events.SelectionChange, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          selectedItem = view.getSelectedItem();
        }
      });
      list.add(view);

      RootPanel.get().add(list);
      list.layout();

      view.setStyleAttribute("backgroundColor", "white");
      view.setTemplate(getTemplate());
      view.setSelectionMode(SelectionMode.SINGLE);
      view.setSelectStyle(selectedStyle);
      view.setItemSelector("." + style + "-item");

    }

    bindStore(store, true);
  }

  protected void onBeforeLoad(StoreEvent se) {

  }

  @Override
  protected void onBlur(ComponentEvent ce) {
    if (ignoreNext) {
      ignoreNext = false;
      return;
    }
    super.onBlur(ce);
    if (forceSelection) {
      doForce();
    }
  }

  @Override
  protected void onClick(ComponentEvent ce) {
    if (!editable && ce.getTarget() == getInputEl().dom) {
      onTriggerClick(ce);
      return;
    }
    super.onClick(ce);
  }

  @Override
  protected void onKeyDown(FieldEvent fe) {
    if (fe.getKeyCode() == KeyboardListener.KEY_TAB) {
      if (expanded) {
        collapse();
      }
    }
  }

  protected void onLoad(StoreEvent se) {

  }

  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    el().addEventsSunk(Event.KEYEVENTS);
    initList();

    if (!this.editable) {
      this.editable = true;
      this.setEditable(false);
    }

    if (value != null) {
      setRawValue(getPropertyEditor().getStringValue(value));
    }

    eventPreview.getIgnoreList().add(getElement());
    eventPreview.getIgnoreList().add(view.getElement());
  }

  protected void onSelect(D model, int index) {
    FieldEvent fe = new FieldEvent(this);
    if (fireEvent(Events.BeforeSelect, fe)) {
      focusValue = getValue();
      setValue(model);
      collapse();
      D v = getValue();
      if ((focusValue == null && v != null) || !focusValue.equals(v)) {
        fireChangeEvent(focusValue, getValue());
      }
      fireEvent(Events.Select, fe);
    }
  }

  protected void onTriggerClick(ComponentEvent ce) {
    super.onTriggerClick(ce);
    if (disabled || isReadOnly()) {
      return;
    }
    if (expanded) {
      collapse();
    } else {
      expand();
    }
    if (GXT.isIE) {
      ignoreNext = true;
    }

    getInputEl().focus();
  }

  protected void onViewClick(boolean focus) {
    List<D> selection = binder.getSelection();
    if (selection.size() > 0) {
      D r = (D) selection.get(0);
      if (r != null) {
        int index = store.indexOf(r);
        onSelect(r, index);
      }
    }
    if (focus) {
      focus();
    }
  }

  private void bindStore(ListStore store, boolean initial) {
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
        binder = view.getBinder();
      }
    }
  }

  private void restrict() {
    int w = Math.max(getWidth(), minListWidth);
    list.setWidth(w);

    int fw = list.el().getFrameWidth("tb");
    int h = view.el().getHeight() + fw;
    h = Math.min(h, maxHeight - fw);
    list.setHeight(h);
    list.el().makePositionable(true);
    list.el().alignTo(getElement(), listAlign, new int[] {0, -1});

    int y = list.el().getY();
    int b = y + h;
    int vh = XDOM.getViewportSize().height + XDOM.getBodyScrollTop();
    if (b > vh) {
      y = y - (b - vh) - 5;
      list.el().setTop(y);
    }
    
  }

  private void selectNext() {
    int count = view.getItemCount();
    if (count > 0) {
      int selectedIndex = view.indexOf(selectedItem);
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
      int selectedIndex = view.indexOf(selectedItem);
      if (selectedIndex == -1) {
        select(0);
      } else if (selectedIndex != 0) {
        select(selectedIndex - 1);
      }
    }
  }

}
