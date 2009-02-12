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
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.BaseModelStringProvider;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DomEvent;
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
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.PagingToolBar;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A combobox component.
 * 
 * <p />
 * When not forcing a selection ({@link #setForceSelection(boolean)})
 * {@link #getValue()} can return null event if the user has typed text into the
 * field if that text cannot be tied to a model from from the combo's store. In
 * this case, you can use {@link #getRawValue()} to get the fields string value.
 * 
 * <p />
 * Combo uses a <code>XTemplate</code> to render it's drop down list. A custom
 * template can be specified to customize the display of the drop down list. See
 * {@link #setTemplate(XTemplate)}.
 * 
 * <p />
 * A custom <code>PropertyEditor</code> can be used to "format" the value that
 * is displayed in the combo's text field. For example:
 * 
 * <pre>
 *  combo.setPropertyEditor(new ListModelPropertyEditor&lt;State>(){
      public String getStringValue(State value) {
        return value.getAbbr() + " " + value.getName();
      }
    });
 * </pre>
 * 
 * A <code>ModelProcessor</code> can be used to "format" the values in the drop
 * down list:
 * 
 * <pre>
    combo.getView().setModelProcessor(new ModelProcessor<State>() {
      public State prepareData(State model) {
        model.set("test", model.getAbbr() + " " + model.getName());
        return model;
      }
    });
 * </pre>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
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
 * <div>Fires before a list item is selected. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * <dd><b>Select</b> : FieldEvent(field)<br>
 * <div>Fires when a list item is selected.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeQuery</b> : FieldEvent(field, value)<br>
 * <div>Fires before all queries are processed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>field : this</li>
 * <li>value : query</li>
 * </ul>
 * </dd>
 * </dl>
 * <dl>
 * 
 * <dt>Inherited Events:</dt>
 * <dd>Field Focus</dd>
 * <dd>Field Blur</dd>
 * <dd>Field Change</dd>
 * <dd>Field Invalid</dd>
 * <dd>Field Valid</dd>
 * <dd>Field KeyPress</dd>
 * <dd>Field SpecialKey</dd>
 * <dd>TriggerField TriggerClick</dd>
 * </dl>
 * 
 * @param <D> the model data type
 */
public class ComboBox<D extends ModelData> extends TriggerField<D> implements
    SelectionProvider<D> {

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
   * TriggerAction enum.
   */
  public enum TriggerAction {
    QUERY, ALL;
  }

  protected ListStore<D> store;
  protected boolean autoComplete = false;

  private String listStyle = "x-combo-list";
  private String selectedStyle = "x-combo-selected";
  private ModelStringProvider modelStringProvider;
  private boolean forceSelection;
  private String listAlign = "tl-bl?";
  private int maxHeight = 300;
  private int minListWidth = 70;
  private boolean editable = true;
  private BaseEventPreview eventPreview, focusPreview;
  private boolean expanded;
  private D selectedItem;
  private StoreListener storeListener;
  private XTemplate template;
  private ListView<D> listView;
  private String lastSelectionText;
  private LayoutContainer list;
  private boolean typeAhead;
  private int queryDelay = 500;
  private int typeAheadDelay = 250;
  private int minChars = 4;
  private String lastQuery, allQuery = "";
  private DelayedTask taTask, dqTask;
  private TriggerAction triggerAction = TriggerAction.QUERY;
  private String mode = "remote";
  private String itemSelector;
  private String loadingText = "Loading...";
  private int pageSize;
  private int assetHeight;
  private El footer;
  private PagingToolBar pageTb;
  private boolean lazyRender, initialized;
  private String valueField;
  private InputElement hiddenInput;

  /**
   * Creates a combo box.
   */
  public ComboBox() {
    messages = new ComboBoxMessages();
    modelStringProvider = new BaseModelStringProvider();
    listView = new ListView();
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
   * Hides the dropdown list if it is currently expanded. Fires the
   * <i>Collapse</i> event on completion.
   */
  public void collapse() {
    if (!expanded) {
      return;
    }
    expanded = false;
    // avoid call to layer for shadow issues when collapsing
    list.getElement().getStyle().setProperty("height", "auto");
    RootPanel.get().remove(list);
    eventPreview.remove();
    // do not clear selections before key processing
    Timer t = new Timer() {
      @Override
      public void run() {
        listView.getSelectionModel().deselectAll();
      }
    };
    t.schedule(100);
    fireEvent(Events.Collapse, new FieldEvent(this));
  }

  /**
   * Execute a query to filter the dropdown list. Fires the BeforeQuery event
   * prior to performing the query allowing the query action to be canceled if
   * needed.
   * 
   * @param q the query
   * @param forceAll true to force the query to execute even if there are
   *          currently fewer characters in the field than the minimum specified
   *          by the minChars config option. It also clears any filter
   *          previously saved in the current store
   */
  public void doQuery(String q, boolean forceAll) {
    if (q == null) {
      q = "";
    }

    FieldEvent fe = new FieldEvent(this);
    fe.value = q;
    if (!fireEvent(Events.BeforeQuery, fe)) {
      return;
    }

    if (forceAll || q.length() >= minChars) {
      if (lastQuery == null || !lastQuery.equals(q)) {
        lastQuery = q;
        if (mode.equals("local")) {
          selectedItem = null;
          if (forceAll) {
            store.clearFilters();
          } else {
            store.filter(getDisplayField(), q);
          }
          onLoad(null);
        } else {
          store.getLoader().load(getParams(q));
          expand();
        }
      } else {
        selectedItem = null;
        onLoad(null);
      }
    }
  }

  /**
   * Expands the dropdown list if it is currently hidden. Fires the
   * <i>expand</i> event on completion.
   */
  public void expand() {
    if (expanded || !hasFocus) {
      return;
    }
    expanded = true;

    if (!initialized) {
      createList(false);
    } else {
      RootPanel.get().add(list);
    }

    list.layout();

    list.el().setVisibility(false);
    list.el().updateZIndex(0);
    list.show();
    restrict();
    list.el().setVisibility(true);

    eventPreview.add();
    fireEvent(Events.Expand, new FieldEvent(this));
  }

  /**
   * Returns the all query.
   * 
   * @return the all query
   */
  public String getAllQuery() {
    return allQuery;
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
   * Returns the item selector.
   * 
   * @return the item selector
   */
  public String getItemSelector() {
    return itemSelector;
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
   * Returns the list style.
   * 
   * @return the list style
   */
  public String getListStyle() {
    return listStyle;
  }

  /**
   * Returns the combo's list view.
   * 
   * @return the view
   */
  public ListView<D> getListView() {
    return listView;
  }

  /**
   * Returns the loading text.
   * 
   * @return the loading text
   */
  public String getLoadingText() {
    return loadingText;
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
   * Returns the min characters used for autocompete and typeahead.
   * 
   * @return the minimum number of characters
   */
  public int getMinChars() {
    return minChars;
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
   * @deprecated see {@link #setModelStringProvider(ModelStringProvider)}
   */
  public ModelStringProvider<D> getModelStringProvider() {
    return modelStringProvider;
  }

  /**
   * Returns the page size.
   * 
   * @return the page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the combo's paging tool bar.
   * 
   * @return the tool bar
   */
  public PagingToolBar getPagingToolBar() {
    return pageTb;
  }

  @Override
  public ListModelPropertyEditor<D> getPropertyEditor() {
    return (ListModelPropertyEditor<D>) propertyEditor;
  }

  /**
   * Returns the query delay.
   * 
   * @return the query delay
   */
  public int getQueryDelay() {
    return queryDelay;
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
  public XTemplate getTemplate() {
    return template;
  }

  /**
   * Returns the trigger action.
   * 
   * @return the trigger action
   */
  public TriggerAction getTriggerAction() {
    return triggerAction;
  }

  /**
   * Returns the type ahead delay in milliseconds.
   * 
   * @return the type ahead delay
   */
  public int getTypeAheadDelay() {
    return typeAheadDelay;
  }

  @Override
  public D getValue() {
    if (!initialized) {
      return value;
    }
    if (store != null) {
      getPropertyEditor().setList(store.getModels());
    }
    D v = super.getValue();
    // a value was set directly and there is not a
    // matching value in the drop down list
    String rv = getRawValue();
    boolean empty = rv == null || rv.equals("");
    if (!rendered && !empty && v == null && value != null && !forceSelection) {
      return value;
    }
    return super.getValue();
  }

  /**
   * Returns the value field name.
   * 
   * @return the value field name
   */
  public String getValueField() {
    return valueField;
  }

  /**
   * Returns the combo's list view.
   * 
   * @return the view
   */
  public ListView<D> getView() {
    return listView;
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
   * Returns true if lazy rendering is enabled.
   * 
   * @return true of lazy rendering
   */
  public boolean isLazyRender() {
    return lazyRender;
  }

  /**
   * Returns true if type ahead is enabled.
   * 
   * @return the type ahead state
   */
  public boolean isTypeAhead() {
    return typeAhead;
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
    if (listView != null) {
      D sel = store.getAt(index);
      if (sel != null) {
        selectedItem = sel;
        listView.getSelectionModel().select(sel);
        fly(listView.getElement(index)).scrollIntoView(listView.getElement(), false);
      }
    }
  }

  /**
   * The text query to send to the server to return all records for the list
   * with no filtering (defaults to '').
   * 
   * @param allQuery the all query
   */
  public void setAllQuery(String allQuery) {
    this.allQuery = allQuery;
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
   * This setting is required if a custom XTemplate has been specified.
   * 
   * @param itemSelector the item selector
   */
  public void setItemSelector(String itemSelector) {
    this.itemSelector = itemSelector;
  }

  /**
   * True to lazily render the combo's drop down list (default to false,
   * pre-render).
   * 
   * @param lazyRender true to lazy render the drop down list
   */
  public void setLazyRender(boolean lazyRender) {
    this.lazyRender = lazyRender;
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
   * Sets the style for the drop down list (defaults to 'x-combo-list');
   * 
   * @param listStyle the list style
   */
  public void setListStyle(String listStyle) {
    this.listStyle = listStyle;
  }

  /**
   * Sets the loading text.
   * 
   * @param loadingText the loading text
   */
  public void setLoadingText(String loadingText) {
    this.loadingText = loadingText;
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
   * Sets the minimum number of characters the user must type before
   * autocomplete and typeahead active (defaults to 4 if remote, or 0 if local).
   * 
   * @param minChars
   */
  public void setMinChars(int minChars) {
    this.minChars = minChars;
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
   * Sets the model string provider (defaults to {@link BaseModelStringProvider}
   * .
   * 
   * @param modelStringProvider the string provider
   * @deprecated the preferred way to provide "formatted" values is to use a
   *             <code>ModelProcessor</code> with the comobo's view.
   */
  public void setModelStringProvider(ModelStringProvider<D> modelStringProvider) {
    this.modelStringProvider = modelStringProvider;
  }

  /**
   * Sets the page size. Only applies when using a paging toolbar.
   * 
   * @param pageSize the page size
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public void setPropertyEditor(PropertyEditor<D> propertyEditor) {
    assert propertyEditor instanceof ListModelPropertyEditor : "PropertyEditor must be a ModelPropertyEditor instance";
    super.setPropertyEditor(propertyEditor);
  }

  /**
   * The length of time in milliseconds to delay between the start of typing and
   * sending the query to filter the dropdown list.
   * 
   * @param queryDelay the query delay
   */
  public void setQueryDelay(int queryDelay) {
    this.queryDelay = queryDelay;
  }

  @Override
  public void setRawValue(String text) {
    if (rendered) {
      if (text == null) {
        String msg = getMessages().getValueNoutFoundText();
        text = msg != null ? msg : "";
      }
      getInputEl().setValue(text);
    }
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
   * Sets the template fragment to be used for the text of each combo list item.
   * 
   * <pre>
   * 
   * <code> combo.setSimpleTemplate("{abbr} {name}"); </code>
   * 
   * </pre>
   * 
   * @param html the html used only for the text of each item in the list
   */
  public void setSimpleTemplate(String html) {
    assertPreRender();
    html = "<tpl for=\".\"><div class=x-combo-list-item>" + html + "</div></tpl>";
    template = XTemplate.create(html);
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
   * Sets the custom template used to render the combo's drop down list.Use this
   * to create custom UI layouts for items in the list.
   * <p>
   * If you wish to preserve the default visual look of list items, add the CSS
   * class name 'x-combo-list-item' to the template's container element.
   * 
   * @param html the html
   */
  public void setTemplate(String html) {
    assertPreRender();
    template = XTemplate.create(html);
  }

  /**
   * Sets the custom template used to render the combo's drop down list.
   * 
   * @param template the template
   */
  public void setTemplate(XTemplate template) {
    assertPreRender();
    this.template = template;
  }

  /**
   * The action to execute when the trigger field is activated. Use
   * {@link TriggerAction#ALL} to run the query specified by the allQuery config
   * option (defaults to {@link TriggerAction#QUERY}).
   * 
   * @param triggerAction the trigger action
   */
  public void setTriggerAction(TriggerAction triggerAction) {
    this.triggerAction = triggerAction;
  }

  /**
   * True to populate and autoselect the remainder of the text being typed after
   * a configurable delay ({@link #typeAheadDelay}) if it matches a known value
   * (defaults to false)
   * 
   * @param typeAhead
   */
  public void setTypeAhead(boolean typeAhead) {
    this.typeAhead = typeAhead;
  }

  /**
   * The length of time in milliseconds to wait until the typeahead text is
   * displayed if typeAhead = true (defaults to 250).
   * 
   * @param typeAheadDelay the type ahead delay
   */
  public void setTypeAheadDelay(int typeAheadDelay) {
    this.typeAheadDelay = typeAheadDelay;
  }

  @Override
  public void setValue(D value) {
    super.setValue(value);
    updateHiddenValue();
    this.lastSelectionText = getRawValue();
    SelectionChangedEvent se = new SelectionChangedEvent(this, getSelection());
    fireEvent(Events.SelectionChange, se);
  }

  /**
   * Sets the model field used to retrieve the "value" from the model. If
   * specified, a hidden form field will contain the value. The hidden form
   * field name will be the combo's field name plus "-hidden".
   * 
   * @param valueField the value field name
   */
  public void setValueField(String valueField) {
    this.valueField = valueField;
  }

  /**
   * Sets the combo's view.
   * 
   * @param view the view
   */
  public void setView(ListView view) {
    this.listView = view;
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    switch (ce.type) {
      case Event.ONMOUSEDOWN:
        onMouseDown(ce);
        break;
    }
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    if (pageTb != null && pageTb.isRendered()) {
      ComponentHelper.doAttach(pageTb);
    }
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    if (list.isRendered()) {
      RootPanel.get().remove(list);
    }
    if (pageTb != null && pageTb.isRendered()) {
      ComponentHelper.doDetach(pageTb);
    }
  }

  protected void doForce() {
    String rv = getRawValue();
    if (getAllowBlank() && (rv == null || rv.equals(""))) {
      return;
    }
    if (getValue() == null) {
      setRawValue(lastSelectionText != null ? lastSelectionText : "");
      initQuery();
    }
  }

  protected D findModel(String property, String value) {
    if (value == null) return null;
    for (D model : store.getModels()) {
      if (value.equals(getPropertyEditor().getStringValue(model))) {
        return model;
      }
    }
    return null;
  }

  @Override
  protected El getFocusEl() {
    return input;
  }

  protected PagingLoadConfig getParams(String query) {
    BasePagingLoadConfig config = new BasePagingLoadConfig();
    config.setLimit(pageSize);
    config.setOffset(0);
    config.getParams().put("query", query);
    return config;
  }

  /**
   * The string value of the model.
   * 
   * @param model the model
   * @param propertyName the property name
   * @return the string value
   * @deprecated the property editor is used to obtain the string value of the
   *             model
   */
  protected String getStringValue(D model, String propertyName) {
    return getPropertyEditor().getStringValue(model);
  }

  protected boolean hasFocus() {
    return hasFocus || expanded;
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

    focusPreview = new BaseEventPreview();
    eventPreview = new BaseEventPreview() {
      protected boolean onAutoHide(PreviewEvent ce) {
        if (list.getElement() == ce.getTarget()) {
          return false;
        }
        collapse();
        return true;
      }

      protected void onClick(PreviewEvent pe) {
        Element target = pe.getTarget();
        if (DOM.isOrHasChild(listView.getElement(), target)) {
          onViewClick(pe, true);
        }
      }
    };

    new KeyNav(this) {
      public void onDown(ComponentEvent ce) {
        ce.cancelBubble();
        if (!isExpanded()) {
          onTriggerClick(ce);
        } else {
          selectNext();
        }
      }

      @Override
      public void onEnter(ComponentEvent ce) {
        if (expanded) {
          ce.cancelBubble();
          onViewClick(ce, false);
        }
      }

      @Override
      public void onEsc(ComponentEvent ce) {
        if (expanded) {
          collapse();
        }
      }

      @Override
      public void onTab(ComponentEvent ce) {
        if (expanded) {
          onViewClick(ce, false);
        }
      }

      @Override
      public void onUp(ComponentEvent ce) {
        if (expanded) {
          ce.cancelBubble();
          selectPrev();
        }
      }

    };
  }

  protected void initList() {
    if (listView == null) {
      listView = new ListView();
    }

    String style = listStyle;
    listView.setStyleAttribute("overflow", "visible");
    listView.setStyleName(style + "-inner");
    listView.setStyleAttribute("padding", "0px");
    listView.setItemSelector(itemSelector != null ? itemSelector : "." + style + "-item");
    listView.setSelectOnOver(true);
    listView.setBorders(false);
    listView.setLoadingText(loadingText);
    listView.getSelectionModel().addListener(Events.SelectionChange,
        new Listener<SelectionChangedEvent<D>>() {
          public void handleEvent(SelectionChangedEvent<D> se) {
            selectedItem = listView.getSelectionModel().getSelectedItem();
          }
        });

    if (template == null) {
      String html = "<tpl for=\".\"><div class=\"" + style + "-item\">{"
          + getDisplayField() + "}</div></tpl>";
      template = XTemplate.create(html);
    }

    assetHeight = 0;

    list = new LayoutContainer() {
      @Override
      protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        eventPreview.getIgnoreList().add(getElement());
      }
    };
    list.setScrollMode(Scroll.AUTOY);
    list.setShim(true);
    list.setShadow(true);
    list.setBorders(true);
    list.setStyleName(style);
    list.hide();

    assert store != null;

    list.add(listView);

    if (pageSize > 0) {
      pageTb = new PagingToolBar(pageSize);
      pageTb.bind((PagingLoader) store.getLoader());
    }

    if (!lazyRender) {
      createList(true);
    }

    listView.setStyleAttribute("backgroundColor", "white");
    listView.setTemplate(template);
    listView.setSelectStyle(selectedStyle);

    bindStore(store, true);
  }

  protected void onBeforeLoad(StoreEvent se) {
    if (!hasFocus()) {
      return;
    }
    if (expanded) {
      restrict();
    }
  }

  @Override
  protected void onBlur(final ComponentEvent ce) {
    Rectangle rec = trigger.getBounds();
    if (rec.contains(BaseEventPreview.getLastXY())) {
      ce.stopEvent();
      return;
    }
    if (expanded) {
      rec = list.el().getBounds();
      if (rec.contains(BaseEventPreview.getLastXY())) {
        ce.stopEvent();
        return;
      }
    }
    hasFocus = false;
    doBlur(ce);
  }

  protected void onMouseDown(ComponentEvent ce) {
    if (!editable && ce.getTarget() == getInputEl().dom) {
      onTriggerClick(ce);
    }
  }

  protected void onEmptyResults() {
    collapse();
  }

  @Override
  protected void onFocus(ComponentEvent ce) {
    super.onFocus(ce);
    focusPreview.add();
  }

  @Override
  protected void onKeyDown(FieldEvent fe) {
    if (fe.getKeyCode() == KeyboardListener.KEY_TAB) {
      if (expanded) {
        onViewClick(fe, false);
      }
    }
    super.onKeyDown(fe);
  }

  @Override
  protected void onKeyUp(FieldEvent fe) {
    super.onKeyUp(fe);
    if (editable && !fe.isSpecialKey()) {
      // last key
      dqTask.delay(queryDelay);
    }
  }

  protected void onLoad(StoreEvent se) {
    if (!isAttached() || !hasFocus()) {
      return;
    }
    if (store.getCount() > 0) {
      expand();
      restrict();
      if (lastQuery != null && lastQuery.equals(allQuery)) {
        if (editable) {
          selectAll();
        }
        if (!selectByValue(getRawValue())) {
          select(0);
        }
      } else {
        selectNext();
        if (typeAhead) {
          taTask.delay(typeAheadDelay);
        }
      }
    } else {
      onEmptyResults();
    }
  }

  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    el().addEventsSunk(Event.KEYEVENTS);
    initList();

    if (!autoComplete) {
      getInputEl().dom.setAttribute("autocomplete", "off");
    }

    if (!this.editable) {
      this.editable = true;
      this.setEditable(false);
    }

    if (mode.equals("local")) {
      minChars = 0;
    }

    if (value != null) {
      setRawValue(getPropertyEditor().getStringValue(value));
    }

    dqTask = new DelayedTask(new Listener<BaseEvent>() {
      public void handleEvent(BaseEvent be) {
        initQuery();
      }
    });

    if (valueField != null) {
      hiddenInput = Document.get().createHiddenInputElement().cast();
      hiddenInput.setName(getName() + "-hidden");
      getElement().appendChild(hiddenInput);
      updateHiddenValue();
    }

    if (typeAhead) {
      taTask = new DelayedTask(new Listener<BaseEvent>() {
        public void handleEvent(BaseEvent be) {
          onTypeAhead();
        }
      });
    }

    eventPreview.getIgnoreList().add(getElement());
  }

  protected void onSelect(D model, int index) {
    FieldEvent fe = new FieldEvent(this);
    if (fireEvent(Events.BeforeSelect, fe)) {
      focusValue = getValue();
      setValue(model);
      collapse();
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
      onFocus(null);
      if (triggerAction == TriggerAction.ALL) {
        doQuery(allQuery, true);
      } else {
        doQuery(getRawValue(), true);
      }

    }
    getInputEl().focus();
  }

  protected void onTypeAhead() {
    if (store.getCount() > 0) {
      D m = store.getAt(0);
      Object obj = m.get(getDisplayField());
      String newValue = obj.toString();
      int len = newValue.length();
      int selStart = getRawValue().length();
      if (selStart != len) {
        setRawValue(newValue);
        select(selStart, newValue.length());
      }
    }
  }

  protected void onViewClick(DomEvent de, boolean focus) {
    int idx = -1;
    // when testing in selenium the items will not be selected as the mouse
    // is not moved during the test
    Element elem = listView.findElement(de.getTarget());
    if (elem != null) {
      idx = listView.indexOf(elem);
    } else {
      D sel = listView.getSelectionModel().getSelectedItem();
      if (sel != null) {
        idx = store.indexOf(sel);
      }
    }
    if (idx != -1) {
      D sel = store.getAt(idx);
      onSelect(sel, idx);
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
        if (listView != null) {
          listView.setStore(null);
        }
      }
    }
    if (store != null) {
      this.store = store;
      if (store.getLoader() == null) {
        mode = "local";
      }
      if (listView != null) {
        listView.setStore(store);
      }
      store.addStoreListener(storeListener);
    }
  }

  private void createList(boolean remove) {
    RootPanel.get().add(list);
    if (remove) {
      RootPanel.get().remove(list);
    }

    initialized = true;

    if (pageTb != null) {
      footer = list.el().createChild("<div class='" + listStyle + "'></div>");
      pageTb.setBorders(false);
      pageTb.render(footer.dom);
      assetHeight += footer.getHeight();
      ComponentHelper.doAttach(pageTb);
    }
  }

  private void doBlur(ComponentEvent ce) {
    if (forceSelection) {
      doForce();
      if (getValidateOnBlur()) {
        validate();
      }
    }

    super.onBlur(ce);

    updateHiddenValue();

    focusPreview.remove();
  }

  private void initQuery() {
    doQuery(getRawValue(), false);
  }

  private void restrict() {
    listView.setHeight("");
    list.setHeight("");
    int w = Math.max(getWidth(), minListWidth);
    list.setWidth(w);
    listView.setWidth("100%");

    int fh = footer != null ? footer.getHeight() : 0;
    int fw = list.el().getFrameWidth("tb") + fh;

    int h = listView.el().getHeight() + fw;

    h = Math.min(h, maxHeight - fw);
    list.setHeight(h);
    list.el().makePositionable(true);
    list.el().alignTo(getElement(), listAlign, new int[] {0, GXT.isIE ? -2 : 0});

    if (footer != null) {
      h -= fh;
    }
    listView.setHeight("100%");

    int y = list.el().getY();
    int b = y + h;
    int vh = XDOM.getViewportSize().height + XDOM.getBodyScrollTop();
    if (b > vh) {
      y = y - (b - vh) - 5;
      list.el().setTop(y);
    }
  }

  private boolean selectByValue(String value) {
    D r = findModel(getDisplayField(), value);
    if (r != null) {
      listView.getSelectionModel().select(r);
      return true;
    }
    return false;
  }

  private void selectNext() {
    int count = store.getCount();
    if (count > 0) {
      int selectedIndex = store.indexOf(selectedItem);
      if (selectedIndex == -1) {
        select(0);
      } else if (selectedIndex < count - 1) {
        select(selectedIndex + 1);
      }
    }
  }

  private void selectPrev() {
    int count = store.getCount();
    if (count > 0) {
      int selectedIndex = store.indexOf(selectedItem);
      if (selectedIndex == -1) {
        select(0);
      } else if (selectedIndex != 0) {
        select(selectedIndex - 1);
      }
    }
  }

  private void updateHiddenValue() {
    if (hiddenInput != null) {
      String v = "";
      D val = getValue();
      if (val != null && val.get(valueField) != null) {
        v = ((Object) val.get(valueField)).toString();
      }
      hiddenInput.setValue(v);
    }
  }

}
