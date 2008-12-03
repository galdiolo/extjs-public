/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcMap;

/**
 * Records wrap model instances and provide specialized editing features,
 * including modification tracking and editing capabilities.
 */
public class Record {

  /**
   * Update enumeration.
   */
  public enum RecordUpdate {
    EDIT, REJECT, COMMIT;
  }

  /**
   * Contains a map of all fields that have been modified and their original
   * values or null if no fields have been modified.
   */
  protected RpcMap modified;

  /**
   * The wrapped model.
   */
  protected ModelData model;

  private boolean dirty;
  private transient Store store;
  private boolean editing;
  private String error;

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
    if (wrappedModel == null) throw new RuntimeException("Record cannot wrap a null model");
    this.model = wrappedModel;
  }

  /**
   * Begin an edit. While in edit mode, no events are relayed to the containing
   * store.
   */
  public void beginEdit() {
    editing = true;
    modified = new RpcMap();
  }

  /**
   * Cancels all changes made in the current edit operation.
   */
  public void cancelEdit() {
    editing = false;
    modified = null;
  }

  /**
   * Usually called by the {@link ListStore} which owns the Record. Commits all
   * changes made to the Record since either creation, or the last commit
   * operation.
   * 
   * @param silent true to skip notification of the owning store of the change
   */
  public void commit(boolean silent) {
    dirty = false;
    modified = null;
    editing = false;
    if (store != null && !silent) {
      store.afterCommit(this);
    }
  }

  /**
   * End an edit. If any data was modified, the model is updated and the
   * containing store is notified.
   */
  public void endEdit() {
    editing = false;
    if (dirty && store != null) {
      store.afterEdit(this);
    }
  }

  /**
   * Returns the value for the property.
   * 
   * @param property the property name
   * @return the value
   */
  public Object get(String property) {
    return model.get(property);
  }

  /**
   * Gets a map of only the fields that have been modified since this record was
   * created or commited.
   * 
   * @return the changed fields
   */
  public Map<String, Object> getChanges() {
    Map<String, Object> changed = new HashMap<String, Object>();
    if (modified != null) {
      changed.putAll(modified.getTransientMap());
    }
    return changed;
  }

  /**
   * Returns the wrapped model instance.
   * 
   * @return the model
   */
  public ModelData getModel() {
    return model;
  }

  public Collection<String> getPropertyNames() {
    Set<String> names = new HashSet<String>();

    for (String name : model.getPropertyNames()) {
      names.add(name);
    }

    if (editing) {
      if (modified != null) {
        names.addAll(modified.keySet());
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

  /**
   * Returns true if the record is being updated.
   * 
   * @return the editing state
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * Returns true if the field passed has been modified since the load or last
   * commit.
   * 
   * @param property the property name
   * @return true if modified
   */
  public boolean isModified(String property) {
    return modified != null && modified.containsKey(property);
  }

  /**
   * Usually called by the {@link ListStore} which owns the Record. Rejects all
   * changes made to the Record since either creation, or the last commit
   * operation. Modified fields are reverted to their original values.
   * 
   * @param silent true to skip notification of the owning store of the change
   */
  public void reject(boolean silent) {
    if (modified != null) {
      for (String p : modified.keySet()) {
        model.set(p, modified.get(p));
      }
    }
    dirty = false;
    modified = null;
    editing = false;
    if (store != null && !silent) {
      store.afterReject(this);
    }
  }

  /**
   * Set the named field to the specified value.
   * 
   * @param name the name of the field to set
   * @param value the value of the field to set
   */
  public void set(String name, Object value) {
    if (model.get(name) != null && model.<Object> get(name).equals(value)) {
      return;
    }
    dirty = true;
    if (modified == null) {
      modified = new RpcMap();
    }

    if (!modified.containsKey(name)) {
      modified.put(name, model.get(name));
    }

    model.set(name, value);

    if (!editing && store != null) {
      store.afterEdit(this);
    }
  }

  /**
   * Manually sets the dirty state of the record. Typically, the dirty state is
   * managed by the record itself.
   * 
   * @param dirty the dirty state
   */
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
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

}
