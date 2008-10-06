/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.binder;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionProvider;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;

/**
 * Base class for all binders.
 * 
 * @param <S> the store type
 * @param <C> the component
 * @param <M> the model type
 */
public abstract class StoreBinder<S extends Store<M>, C extends Component, M extends ModelData>
    extends BaseObservable implements SelectionProvider<M> {

  protected S store;
  protected C component;
  protected StoreListener listener = new StoreListener() {

    public void storeAdd(StoreEvent se) {
      onAdd(se);
    }

    public void storeBeforeDataChanged(StoreEvent se) {
      onBeforeDataChanged(se);
    }

    public void storeClear(StoreEvent se) {
      onClear(se);
    }

    public void storeDataChanged(StoreEvent se) {
      onDataChanged(se);
    }

    public void storeFilter(StoreEvent se) {
      onFilter(se);
    }

    public void storeRemove(StoreEvent se) {
      onRemove(se);
    }

    public void storeSort(StoreEvent se) {
      onSort(se);
    }

    public void storeUpdate(StoreEvent se) {
      onUpdate(se);
    }

  };;
  protected ModelStringProvider stringProvider;
  protected ModelStringProvider iconProvider;
  protected ModelStringProvider styleProvider;
  protected ModelStringProvider defaultStringProvider = new ModelStringProvider() {
    public String getStringValue(ModelData model, String property) {
      Object o = model.get(property);
      if (o == null) {
        return "";
      } else {
        return String.valueOf(o);
      }
    }
  };
  
  private boolean mask = false;
  private boolean autoSelect;

  /**
   * Creates a new binder instance.
   * 
   * @param store the store
   * @param container the container
   */
  public StoreBinder(S store, C container) {
    this.component = container;
    this.store = store;
    hook();
    bind(store);
  }

  public void addSelectionChangedListener(SelectionChangedListener listener) {
    addListener(Events.SelectionChange, listener);
  }

  /**
   * Returns the matching component for the given model.
   * 
   * @param model the model
   * @return the component
   */
  public abstract Component findItem(M model);

  public List<M> getSelection() {
    return getSelectionFromComponent();
  }

  /**
   * Must be called after the binder had been configured.
   */
  public void init() {
    createAll();
  }

  /**
   * Returns true if select on load is enabled.
   * 
   * @return the auto select state
   */
  public boolean isAutoSelect() {
    return autoSelect;
  }

  /**
   * Returns whether the given model is filtered.
   * 
   * @param parent the model parent
   * @param child the child model
   * @return true if the child is filtered
   */
  public boolean isFiltered(ModelData parent, ModelData child) {
    if (store.isFiltered() && store.getFilters().size() > 0) {
      for (Object filter : store.getFilters()) {
        boolean result = ((StoreFilter) filter).select(store, parent, child, null);
        if (!result) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns true if masking is enabled.
   * 
   * @return the mask state
   */
  public boolean isMask() {
    return mask;
  }

  public void removeSelectionListener(SelectionChangedListener listener) {
    removeListener(Events.SelectionChange, listener);
  }

  /**
   * True to select the first model after the store's data changes.
   * 
   * @param autoSelect true to auto select
   */
  public void setAutoSelect(boolean autoSelect) {
    this.autoSelect = autoSelect;
  }

  /**
   * Sets the binder's icon provider.
   * 
   * @param iconProvider the icon provider
   */
  public void setIconProvider(ModelStringProvider iconProvider) {
    this.iconProvider = iconProvider;
  }

  /**
   * Sets whether a mask should be displayed during a load operation.
   * 
   * @param mask true for mask
   */
  public void setMask(boolean mask) {
    this.mask = mask;
  }

  public void setSelection(List<M> selection) {
    if (selection == null) {
      selection = new ArrayList<M>();
    }
    setSelectionFromProvider(selection);
  }

  /**
   * Sets the current selection for this selection provider.
   * 
   * @param selection the selection
   */
  public void setSelection(M selection) {
    List<M> sel = new ArrayList<M>();
    if (selection != null) {
      sel.add(selection);
    }
    setSelection(sel);
  }

  /**
   * Sets the binder's string provider.
   * 
   * @param stringProvider the string provider
   */
  public void setStringProvider(ModelStringProvider stringProvider) {
    this.stringProvider = stringProvider;
  }

  /**
   * Sets the binder's style provider.
   * 
   * @param styleProvider the style provider
   */
  public void setStyleProvider(ModelStringProvider styleProvider) {
    this.styleProvider = styleProvider;
  }

  protected void bind(S store) {
    if (this.store != null) {
      this.store.removeStoreListener(listener);
    }
    this.store = store;
    if (store != null) {
      store.addStoreListener(listener);
    } else {
      store.removeAll();
    }
  }

  protected abstract void createAll();

  protected void fireSelectionChanged(SelectionChangedEvent se) {
    fireEvent(Events.SelectionChange, se);
  }

  protected String getIconValue(ModelData model, String property) {
    if (iconProvider != null) {
      return iconProvider.getStringValue(model, property);
    }
    return null;
  }

  protected abstract List<M> getSelectionFromComponent();

  protected String getTextValue(ModelData model, String property) {
    if (stringProvider != null) {
      return stringProvider.getStringValue(model, property);
    }
    return null;
  }

  protected String getTextValue(ModelData model, String property, boolean useDefault) {
    if (stringProvider != null) {
      return stringProvider.getStringValue(model, property);
    }
    if (useDefault) {
      return defaultStringProvider.getStringValue(model, property);
    }
    return null;
  }

  protected void hook() {
    component.addListener(Events.SelectionChange, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        SelectionChangedEvent se = new SelectionChangedEvent(StoreBinder.this, getSelection());
        fireSelectionChanged(se);
      }
    });
  }

  protected abstract void onAdd(StoreEvent se);

  protected void onBeforeDataChanged(StoreEvent se) {
    if (mask && component != null && component.isRendered()) {
      component.el().mask(GXT.MESSAGES.loadMask_msg());
    }
  }

  protected void onClear(StoreEvent se) {
    removeAll();
  }

  protected void onDataChanged(StoreEvent se) {
    if (mask && component != null && component.isRendered()) {
      component.el().unmask();
    }
  }
  
  protected void setModel(Component c, ModelData model) {
    ComponentHelper.setModel(c, model);
  }

  protected void onFilter(StoreEvent se) {

  }

  protected abstract void onRemove(StoreEvent se);

  protected void onSort(StoreEvent se) {

  }

  protected abstract void onUpdate(StoreEvent se);

  protected abstract void removeAll();

  protected abstract void setSelectionFromProvider(List<M> selection);

  protected abstract void update(M model);
}
