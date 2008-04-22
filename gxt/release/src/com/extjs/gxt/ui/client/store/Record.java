/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcMap;
import com.extjs.gxt.ui.client.js.JsObject;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A object contained in a Store.
 */
public class Record implements Model, Serializable {

  /**
   * Constant for a edit update.
   */
  public static final int EDIT = 0;

  /**
   * Constant for a reject udpate.
   */
  public static final int REJECT = 1;

  /**
   * Constnt for a commit update.
   */
  public static final int COMMIT = 2;

  /**
   * Contains a map of all fields that have been modified and their original
   * values or null if no fields have been modified.
   */
  protected RpcMap modified;

  /**
   * Contains a list of the proeprty names for those properties that have been
   * removed
   */
  protected Set<String> removed;

  protected ModelData wrappedModel;
  protected transient ChangeEventSupport changeEventSupport = new ChangeEventSupport();

  private boolean dirty;
  private transient Store store;
  private boolean editing;
  private String error;

  /**
   * Creates a new record.
   */
  public Record() {
    this(new BaseModelData());
  }

  /**
   * Creates a new record.
   * 
   * @param properties the initial values
   */
  public Record(Map<String, Object> properties) {
    this(new BaseModelData(properties));
  }

  /**
   * Creates a new record.
   * 
   * @param wrappedModel the model
   */
  public Record(ModelData wrappedModel) {
    if (wrappedModel == null)
      throw new RuntimeException("Record cannot wrap a null model");
    this.wrappedModel = wrappedModel;
  }

  public void addChangeListener(ChangeListener listener) {
    changeEventSupport.addChangeListener(listener);
  }

  /**
   * Begin an edit. While in edit mode, no events are relayed to the containing
   * store.
   * 
   * Succesive calls to beginEdit without calling endEdit or cancelEdit are
   * no-ops
   */
  public void beginEdit() {
    editing = true;
  }

  /**
   * Cancels all changes made in the current edit operation.
   */
  public void cancelEdit() {
    reject(false);
  }

  /**
   * End an edit. If any data was modified, the containing store is notified.
   */
  public void endEdit() {
    commit(false);
  }

  public Object get(String property) {
    // access to property values through the get method will see any modified
    // properties
    if (modified != null && modified.containsKey(property)) {
      return modified.get(property);
    }
    return wrappedModel.get(property);
  }

  /**
   * Gets a map of only the fields that have been modified since this record was
   * created or commited.
   * 
   * Any removed properties will not be in the returned map
   * 
   * @return the changed fields
   */
  public Map<String, Object> getChanges() {
    Map<String, Object> changed = new HashMap<String, Object>();
    if (modified != null) {
      changed.putAll(modified);
    }
    return changed;
  }

  /**
   * Returns the record's data as an javascript object.
   * 
   * @return the javascript object
   */
  public JavaScriptObject getJsObject() {
    JsObject jsObj = new JsObject();
    for (String key : getPropertyNames()) {
      Object value = get(key);
      jsObj.set(key, value);
    }
    return jsObj.getJsObject();
  }

  /**
   * Returns the wrapped model instance.
   * 
   * @return the model
   */
  public ModelData getModel() {
    return wrappedModel;
  }

  public Collection<String> getPropertyNames() {
    Set<String> names = new HashSet<String>();

    for (String name : wrappedModel.getPropertyNames()) {
      names.add(name);
    }

    if (editing) {
      if (modified != null) {
        names.addAll(modified.keySet());
      }
      if (removed != null) {
        names.removeAll(removed);
      }
    }
    return names;
  }

  /**
   * Returns true if the record has uncommitted changes.
   * 
   * @return the dirty state
   */
  public boolean isDirty() {
    return dirty;
  }

  public void notify(ChangeEvent event) {
    changeEventSupport.notify(event);
  }

  /**
   * Removes a field.
   * 
   * @param property the name of the field to remove
   */
  public Object remove(String property) {
    Object oldValue = get(property);
    if (!editing) {
      if (store != null) store.afterEdit(this);
      return oldValue;
    } else {
      dirty = true;
      if (removed == null) removed = new HashSet<String>();
      if (modified != null) modified.remove(property);
      removed.add(property);
    }
    return oldValue;
  }

  public void removeChangeListener(ChangeListener listener) {
    changeEventSupport.removeChangeListener(listener);
  }

  /**
   * Set the named field to the specified value.
   * 
   * @param name the name of the field to set
   * @param value the value of the field to set
   */
  public Object set(String name, Object value) {
    Object oldValue = get(name);
    if (!editing) {
      wrappedModel.set(name, value);
      if (store != null) {
        store.afterEdit(this);
      }
    } else {
      if (oldValue != null && oldValue.equals(value)) {
        return oldValue;
      }
      dirty = true;
      if (modified == null) modified = new RpcMap();
      modified.put(name, value);
    }
    return oldValue;
  }

  protected void clearError() {
    error = null;
  }

  protected boolean hasError() {
    return error != null;
  }

  protected void join(Store store) {
    this.store = store;
  }

  /**
   * Usually called by the {@link Store} which owns the Record. Commits all
   * changes made to the Record since either creation, or the last commit
   * operation.
   * 
   * @param silent true to skip notification of the owning store of the change
   */
  void commit(boolean silent) {
    // update wrappedModel with all modifications and removed properties
    if (modified != null) {
      for (String property : modified.keySet()) {
        wrappedModel.set(property, modified.get(property));
      }
    }

    if (removed != null) {
      for (String property : removed) {
        wrappedModel.remove(property);
      }
    }

    editing = false;
    dirty = false;
    modified = null;
    removed = null;
    if (store != null && !silent) {
      store.afterCommit(this);
    }
  }

  /**
   * Usually called by the {@link Store} which owns the Record. Rejects all
   * changes made to the Record since either creation, or the last commit
   * operation. Modified fields are reverted to their original values.
   * 
   * @param silent true to skip notification of the owning store of the change
   */
  void reject(boolean silent) {
    modified = null;
    removed = null;
    editing = false;
    if (store != null && !silent) {
      store.afterReject(this);
    }
  }

}
