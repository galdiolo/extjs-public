/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.data.TreeLoadEvent;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.util.Util;

/**
 * A store for hierarchical data.
 * 
 * <p/>
 * The parent child relationships are handled internally by the store. It is
 * important to note that the store does not use the the parent and children of
 * any TreeModel instances added to the store.
 * 
 * <p/>
 * It is important to note the sorting behavior when working with TreeStore.
 * When a sorter is set, it is applied to all existing models in the cache and
 * the Sort event is fired. At this point, the sorter is enabled and active. All
 * sorter will be applied to all inserts into the store.
 * 
 * <p/>
 * Remote sorting is not supported with TreeStore.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Store.BeforeDataChanged</b> : TreeStoreEvent(store)<br>
 * <div>Fires before the store's data is changed. Apply applies when a store is
 * bound to a loader.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.DataChange</b> : TreeStoreEvent(store)<br>
 * <div>Fires when the data cache has changed, and a widget which is using this
 * Store as a Model cache should refresh its view.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.Filter</b> : TreeStoreEvent(store)<br>
 * <div>Fires when filters are applied and removed from the store.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.Add</b> : TreeStoreEvent(store, parent, child)<br>
 * <div>Fires when models have been added to the store.</div>
 * <ul>
 * <li>store : this</li>
 * <li>parent : the parent</li>
 * <li>children : this list of model(s) added</li>
 * <li>index : the insert index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.Remove</b> : TreeStoreEvent(store, parent, child)<br>
 * <div>Fires when a model has been removed from the store.</div>
 * <ul>
 * <li>store : this</li>
 * <li>parent : the parent</li>
 * <li>child : the removed child</li>
 * <li>children : all the children of the removed item</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.Update</b> : TreeStoreEvent(store, model, record)<br>
 * <div>Fires when a model has been updated via its record.</div>
 * <ul>
 * <li>store : this</li>
 * <li>model : the model that was updated</li>
 * <li>record : the record that was updated</li>
 * <li>operation : the update operation being performed.</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.Clear</b> : TreeStoreEvent(store)<br>
 * <div>Fires when the data cache has been cleared.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Store.Sort</b> : TreeStoreEvent(store)<br>
 * <div>Fires after a sorter is applied to the store.</div>
 * <ul>
 * <li>store : this</li>
 * </ul>
 * </dd>
 * 
 * </dt>
 */
public class TreeStore<M extends ModelData> extends Store<M> {

  protected Map<M, TreeModel> modelMap = new HashMap<M, TreeModel>();
  protected Map<TreeModel, M> wrapperMap = new HashMap<TreeModel, M>();
  protected BaseTreeModel rootWrapper = new BaseTreeModel();

  protected TreeLoader loader;

  public TreeStore() {
    super();
  }

  public TreeStore(TreeLoader loader) {
    this.loader = loader;
    loader.addLoadListener(new LoadListener() {

      public void loaderBeforeLoad(LoadEvent le) {
        onBeforeLoad(le);
      }

      public void loaderLoad(LoadEvent le) {
        onLoad((TreeLoadEvent) le);
      }

      public void loaderLoadException(LoadEvent le) {
        onLoadException(le);
      }

    });
  }

  /**
   * Adds the models to the root of the store and fires the <i>Add</i> event.
   * 
   * @param models the models to be added
   * @param addChildren true to recursively add all children
   */
  public void add(List<M> models, boolean addChildren) {
    insert(models, rootWrapper.getChildCount(), addChildren);
  }

  /**
   * Adds the items to the store and fires the <i>Add</i> event.
   * 
   * @param item the item to add
   * @param addChildren true to recursively add all children
   */
  public void add(M item, boolean addChildren) {
    add(Util.createList(item), addChildren);
  }

  /**
   * Adds the models to the given parent and fires the <i>Add</i> event.
   * 
   * @param parent the parent
   * @param children the children
   * @param addChildren true to recursively add all children
   */
  public void add(M parent, List<M> children, boolean addChildren) {
    insert(parent, children, getChildCount(parent), addChildren);
  }

  /**
   * Adds the child to the parent and fires the <i>Add</i> event.
   * 
   * @param parent the parent item
   * @param item the child item
   * @param addChildren true to recursivly add all children
   */
  public void add(M parent, M item, boolean addChildren) {
    insert(parent, item, getChildCount(parent), addChildren);
  }

  /**
   * Returns all the stores items. The items are not returned in any order.
   * 
   * @return the items
   */
  public List<M> getAllItems() {
    return all;
  }

  /**
   * Returns the root level child.
   * 
   * @param index the index
   * @return the child
   */
  public M getChild(int index) {
    TreeModel child = rootWrapper.getChild(index);
    if (child != null) {
      return unwrap(child);
    }
    return null;
  }

  /**
   * Returns the child at the given index.
   * 
   * @param parent the parent model
   * @param index the index
   * @return the child of the parent at the given index
   */
  public M getChild(M parent, int index) {
    TreeModel p = findWrapper(parent);

    if (p != null) {
      TreeModel child = p.getChild(index);
      if (child != null) {
        return unwrap(child);
      }
    }
    return null;
  }

  /**
   * Returns the root level child count.
   * 
   * @return the child count
   */
  public int getChildCount() {
    return rootWrapper.getChildCount();
  }

  /**
   * Returns the child count for the parent.
   * 
   * @param parent the parent
   * @return the child count or -1 if parent not found in the store
   */
  public int getChildCount(M parent) {
    TreeModel p = findWrapper(parent);
    if (p != null) {
      return p.getChildCount();
    }
    return -1;
  }

  /**
   * Returns the children of the parent.
   * 
   * @param parent the children
   * @return the children or null if parent not found in stote
   */
  public List<M> getChildren(M parent) {
    TreeModel p = findWrapper(parent);
    if (p != null) {
      return unwrapChildren(p);
    }
    return null;
  }

  /**
   * Returns the parent-child relationships for the given model. The actual
   * model can be retrieved in each TreeModel's "model" property using the
   * {@link TreeModel#get(String)} method. The children of each tree model
   * contains tree model instances which wrap the actual child model.
   * 
   * @param model the model
   * @return the model and it's children
   */
  public TreeModel getModelState(M model) {
    TreeModel tm = new BaseTreeModel();
    tm.set("model", model);
    int count = getChildCount(model);
    for (int i = 0; i < count; i++) {
      tm.add(getModelState(getChild(model, i)));
    }
    return tm;
  }

  /**
   * Returns the store's loader.
   * 
   * @return the loader
   */
  public TreeLoader getLoader() {
    return loader;
  }

  /**
   * Returns the parent of the item.
   * 
   * @param item the item
   * @return the item's parent
   */
  public M getParent(M item) {
    TreeModel child = findWrapper(item);
    if (child != null) {
      TreeModel p = child.getParent();
      if (p != null) {
        return unwrap(p);
      }
    }
    return null;
  }

  /**
   * Returns the root level items.
   * 
   * @return the items
   */
  public List<M> getRootItems() {
    return unwrapChildren(rootWrapper);
  }

  /**
   * Inserts the models into the store and fires the <i>Add</i> event.
   * 
   * @param models the models to insert
   * @param index the insert index
   * @param addChildren true to recursivly add all children
   */
  public void insert(List<M> models, int index, boolean addChildren) {
    List<TreeModel> insert = new ArrayList<TreeModel>();
    for (M model : models) {
      insert.add(wrap(model));
    }
    doInsert(rootWrapper, insert, index, addChildren, false);
  }

  /**
   * Adds the item to the store and fires the <i>Add</i> event.
   * 
   * @param item the item to insert
   * @param index the insert index
   * @param addChildren true to recursivly add all children
   */
  public void insert(M item, int index, boolean addChildren) {
    doInsert(rootWrapper, Util.createList(wrap(item)), index, addChildren, false);
  }

  /**
   * Inserts the children to the parent and fires the <i>Add</i> event.
   * 
   * @param parent the parent
   * @param children the children
   * @param index the insert index
   * @param addChildren true to recursively add all children
   */
  public void insert(M parent, List<M> children, int index, boolean addChildren) {
    TreeModel wrapper = findWrapper(parent);
    if (wrapper != null) {
      List<TreeModel> insert = new ArrayList<TreeModel>();
      for (M model : children) {
        insert.add(wrap(model));
      }
      doInsert(wrapper, insert, index, addChildren, false);
    }
  }

  /**
   * Adds the child to the parent and fires the <i>Add</i> event.
   * 
   * @param parent the parent model
   * @param model the child model
   * @param index the insert index
   * @param addChildren true to recursively add all children
   */
  public void insert(M parent, M model, int index, boolean addChildren) {
    insert(parent, Util.createList(model), index, addChildren);
  }

  public void move(M model, M newParent) {
    TreeModel tm = findWrapper(model);
    M parent = getParent(model);
    TreeModel tp = findWrapper(parent);
    remove(tp, tm, false);
    doInsert(findWrapper(newParent), Util.createList(tm), getChildCount(newParent),
        false, false);
  }

  /**
   * Removes the model from the store and fires the <i>Remove</i> event.
   * 
   * @param model the item to be removed
   */
  public void remove(M model) {
    remove(rootWrapper, findWrapper(model), false);
  }

  /**
   * Removes the child from the parent and fires the <i>Remove</i> event.
   * 
   * @param parent the parent model
   * @param child the child model
   */
  public void remove(M parent, M child) {
    TreeModel p = findWrapper(parent);
    TreeModel c = findWrapper(child);
    if (p != null && c != null) {
      remove(p, c, false);
    }
  }

  @Override
  public void removeAll() {
    removeAll(false);
  }

  /**
   * Removes all the parent's children.
   * 
   * @param parent the parent
   */
  public void removeAll(M parent) {
    TreeModel p = findWrapper(parent);
    if (p != null) {
      List<M> children = getChildren(parent);
      for (M m : children) {
        TreeModel child = findWrapper(m);
        if (child != null) {
          remove(p, child, false);
        }
      }
    }
  }

  /**
   * Sets the current sort info usen when sorting items in the store.
   * 
   * @param info the sort info
   */
  public void setSortInfo(SortInfo info) {
    this.sortInfo = info;
  }

  @Override
  public void setStoreSorter(StoreSorter storeSorter) {
    super.setStoreSorter(storeSorter);
    applySort(false);
  }

  @Override
  protected void applySort(boolean supressEvent) {
    for (TreeModel wrapper : wrapperMap.keySet()) {
      List<TreeModel> children = wrapper.getChildren();
      if (children.size() > 0) {
        applySort(children);
      }
    }
    if (supressEvent) {
      fireEvent(Sort, createStoreEvent());
    }
  }

  protected void applySort(List<TreeModel> list) {
    if (storeSorter != null) {
      Collections.sort(list, new Comparator<TreeModel>() {
        public int compare(TreeModel m1, TreeModel m2) {
          return storeSorter.compare(TreeStore.this, unwrap(m1), unwrap(m2),
              sortInfo.getSortField());
        }
      });
      if (sortInfo.getSortDir() == SortDir.DESC) {
        Collections.reverse(list);
      }
    }
  }

  @Override
  protected TreeStoreEvent createStoreEvent() {
    return new TreeStoreEvent(this);
  }

  protected TreeModel findWrapper(M item) {
    return modelMap.get(item);
  }

  protected void onBeforeLoad(LoadEvent le) {
    fireEvent(BeforeDataChanged, createStoreEvent());
  }

  protected void onLoad(TreeLoadEvent le) {
    if (le.parent == null) {
      removeAll(true);
      List<TreeModel> insert = new ArrayList<TreeModel>();
      for (M model : (List<M>) le.data) {
        insert.add(wrap(model));
      }
      doInsert(rootWrapper, insert, 0, false, true);
      fireEvent(DataChanged, new TreeStoreEvent(this));
    } else {
      TreeModel wrapper = findWrapper((M) le.parent);
      if (wrapper != null) {
        if (wrapper.getChildren().size() > 0) {
          removeAll((M) le.parent);
        }
        List<TreeModel> insert = new ArrayList<TreeModel>();
        List<M> list = (List<M>) le.data;
        for (M model : list) {
          insert.add(wrap(model));
        }
        doInsert(wrapper, insert, 0, false, true);
        TreeStoreEvent evt = new TreeStoreEvent(this);
        evt.parent = le.parent;
        evt.children = unwrapChildren(wrapper);
        fireEvent(DataChanged, evt);
      }
    }
  }

  protected void onLoadException(LoadEvent le) {

  }

  @Override
  protected void onModelChange(ChangeEvent ce) {
    super.onModelChange(ce);
    switch (ce.type) {
      case ChangeEventSource.Add: {
        if (ce.source == ce.parent) {
          M parent = (M) ce.parent;
          M child = (M) ce.item;
          add(parent, child, false);
        }
        break;
      }
      case ChangeEventSource.Remove: {
        if (ce.source == ce.parent) {
          M parent = (M) ce.parent;
          M child = (M) ce.item;
          remove(parent, child);
        }

        break;
      }
    }

  }

  protected List<M> unwrap(List<TreeModel> models) {
    List<M> children = new ArrayList<M>();
    for (TreeModel child : models) {
      children.add(unwrap(child));
    }
    return children;
  }

  protected M unwrap(TreeModel wrapper) {
    return wrapperMap.get(wrapper);
  }

  protected List<M> unwrapChildren(TreeModel parent) {
    return unwrap(parent.getChildren());
  }

  protected TreeModel wrap(M model) {
    TreeModel wrapper = new BaseTreeModel();
    modelMap.put(model, wrapper);
    wrapperMap.put(wrapper, model);
    return wrapper;
  }

  private void doInsert(TreeModel parent, List<TreeModel> children, int index,
      boolean addChildren, boolean supressEvent) {
    if (parent != null && children != null) {
      M modelParent = unwrap(parent);
      for (int i = children.size() - 1; i >= 0; i--) {
        parent.insert(children.get(i), index);
        M m = unwrap(children.get(i));
        all.add(m);
        registerModel(m);
      }
      if (storeSorter != null) {
        applySort(parent.getChildren());
      }

      if (!supressEvent) {
        if (storeSorter != null) {
          for (TreeModel child : children) {
            TreeStoreEvent evt = createStoreEvent();;
            evt.parent = modelParent;
            evt.index = parent.indexOf(child);
            evt.children = Util.createList(unwrap(child));
            fireEvent(Add, evt);
          }
        } else {
          TreeStoreEvent evt = createStoreEvent();;
          evt.parent = modelParent;
          evt.children = unwrap(children);
          evt.index = index;
          fireEvent(Add, evt);
        }
      }

      if (addChildren) {
        for (TreeModel sub : children) {
          M model = unwrap(sub);
          if (model instanceof TreeModel) {
            TreeModel treeSub = (TreeModel) model;
            List<TreeModel> insert = new ArrayList<TreeModel>();
            List<M> c = treeSub.getChildren();
            for (M m : c) {
              insert.add(wrap(m));
            }
            doInsert(sub, insert, getChildCount(model), true, false);
            update(model);
          }
        }
      }
    }
  }

  private void findChildren(M parent, List<M> children) {
    for (M child : getChildren(parent)) {
      children.add(child);
      findChildren(child, children);
    }
  }

  private void remove(TreeModel parent, TreeModel child, boolean supressEvent) {
    int index = parent.getChildren().indexOf(child);
    if (index != -1) {
      parent.remove(child);
      M model = unwrap(child);
      List<M> children = new ArrayList<M>();
      findChildren(model, children);

      for (M c : children) {
        all.remove(c);
        modelMap.remove(c);
        unregisterModel(c);
        wrapperMap.remove(findWrapper(c));
      }
      all.remove(model);
      wrapperMap.remove(child);
      modelMap.remove(model);
      unregisterModel(model);
      if (!supressEvent) {
        TreeStoreEvent evt = new TreeStoreEvent(this);
        evt.parent = unwrap(parent);
        evt.child = model;
        evt.children = children;
        evt.index = index;
        fireEvent(Remove, evt);
      }
    }
  }

  private void removeAll(boolean supressEvent) {
    all.clear();
    modified.clear();
    modelMap.clear();
    wrapperMap.clear();
    rootWrapper.removeAll();
    if (!supressEvent) {
      fireEvent(Clear, createStoreEvent());
    }
  }

}
