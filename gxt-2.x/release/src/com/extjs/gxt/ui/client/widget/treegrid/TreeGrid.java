/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.treegrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.TreeGridEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.store.TreeStoreEvent;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.treepanel.TreeStyle;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.Joint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A hierarchical tree grid bound to a <code>TreeStore</code>.
 * 
 * <p />
 * A <code>TreeGridCellRenderer</code> can be assigned to the
 * <code>ColumnConfig</code> in which the tree will be displayed.
 * 
 * <p />
 * With state enabled, TreePanel will save and restore the expand state of the
 * nodes in the tree. A <code>ModelKeyProvider</code> must specified with the
 * <code>TreeStore</code> this tree is bound to. Save and restore works with
 * both local, and asynchronous loading of children.
 * 
 * @param <M> the model type
 */
public class TreeGrid<M extends ModelData> extends Grid<M> {

  public class TreeNode {

    protected M m;
    protected String id;
    protected Element joint, check, text;
    private boolean expanded = false;
    private boolean expand;
    private boolean leaf = true;
    private boolean childrenRendered;
    private boolean loaded;

    public TreeNode(String id, M m) {
      this.id = id;
      this.m = m;
      if (loader != null && !loaded) {
        leaf = !loader.hasChildren(m);
      }
    }

    public int getItemCount() {
      return treeStore.getChildCount(m);
    }

    public M getModel() {
      return m;
    }

    public TreeNode getParent() {
      M p = treeStore.getParent(m);
      return findNode(p);
    }

    public int indexOf(TreeNode child) {
      M c = child.getModel();
      return store.indexOf(c);
    }

    public boolean isExpanded() {
      return expanded;
    }

    public boolean isLeaf() {
      return !hasChildren(m);
    }

    public void setExpanded(boolean expand) {
      TreeGrid.this.setExpanded(m, expand);
    }
  }

  protected Map<String, TreeNode> nodes = new FastMap<TreeNode>();
  protected Map<M, String> cache = new HashMap<M, String>();
  protected TreeStore<M> treeStore;
  protected TreeLoader<M> loader;
  protected TreeGridView treeGridView;
  protected int maxExpandEntries = 100;

  private ModelIconProvider<M> iconProvider;
  private TreeStyle style = new TreeStyle();
  private boolean autoLoad, filtering;
  private boolean caching = true;
  private StoreListener<M> storeListener = new StoreListener<M>() {
    @Override
    public void storeAdd(StoreEvent<M> se) {
      onAdd((TreeStoreEvent<M>) se);
    }

    @Override
    public void storeClear(StoreEvent<M> se) {
      onDataChanged((TreeStoreEvent<M>) se);
    }

    @Override
    public void storeDataChanged(StoreEvent<M> se) {
      onDataChanged((TreeStoreEvent<M>) se);
    }

    @Override
    public void storeFilter(StoreEvent<M> se) {
      onFilter((TreeStoreEvent<M>) se);
    }

    @Override
    public void storeRemove(StoreEvent<M> se) {
      onRemove((TreeStoreEvent<M>) se);
    }
    
    @Override
    public void storeUpdate(StoreEvent<M> se) {
      onUpdate((TreeStoreEvent<M>) se);
    }
  };

  private ListStore<M> listStore = new ListStore<M>() {
    public Record getRecord(M model) {
      return treeStore.getRecord(model);
    };

    public boolean hasRecord(M model) {
      return treeStore.hasRecord(model);

    };

  };

  @SuppressWarnings("unchecked")
  public TreeGrid(TreeStore store, ColumnModel cm) {
    this.store = listStore;
    this.cm = cm;
    this.view = new GridView();
    focusable = true;
    baseStyle = "x-grid-panel";
    setSelectionModel(new GridSelectionModel<M>());

    this.treeStore = store;
    this.loader = treeStore.getLoader();

    addStyleName("x-treegrid");

    treeStore.addStoreListener(storeListener);

    treeGridView = new TreeGridView();
    setView(treeGridView);

    setSelectionModel(new TreeGridSelectionModel<M>());
  }

  /**
   * Returns the tree node for the given target.
   * 
   * @param target the target element
   * @return the tree node or null if no match
   */
  public TreeNode findNode(Element target) {
    Element row = (Element) getView().findRow(target);
    El item = fly(row).selectNode(".x-tree3-node");
    if (item != null) {
      String id = item.getId();
      TreeNode node = nodes.get(id);
      return node;
    }
    return null;
  }

  /**
   * Returns the model icon provider.
   * 
   * @return the icon provider
   */
  public ModelIconProvider<M> getIconProvider() {
    return iconProvider;
  }

  /**
   * Returns the tree style.
   * 
   * @return the tree style
   */
  public TreeStyle getStyle() {
    return style;
  }

  /**
   * Returns the tree's tree store.
   * 
   * @return the tree store
   */
  public TreeStore<M> getTreeStore() {
    return treeStore;
  }

  /**
   * Returns the tree's view.
   * 
   * @return the view
   */
  public TreeGridView getTreeView() {
    return treeGridView;
  }

  /**
   * Returns true if the model is expanded.
   * 
   * @param model the model
   * @return true if expanded
   */
  public boolean isExpanded(M model) {
    TreeNode node = findNode(model);
    return node.isExpanded();
  }

  /**
   * Returns true if the model is a leaf node. The leaf state allows a tree item
   * to specify if it has children before the children have been realized.
   * 
   * @param model the model
   * @return the leaf state
   */
  public boolean isLeaf(M model) {
    TreeNode node = findNode(model);
    return node.isLeaf();
  }

  /**
   * Sets the item's expand state.
   * 
   * @param model the model
   * @param expand true to expand
   */
  public void setExpanded(M model, boolean expand) {
    setExpanded(model, expand, false);
  }

  /**
   * Sets the item's expand state.
   * 
   * @param model the model
   * @param expand true to expand
   * @param deep true to expand all children recursively
   */
  @SuppressWarnings("unchecked")
  public void setExpanded(M model, boolean expand, boolean deep) {
    TreeNode node = findNode(model);
    if (node != null) {
      TreeGridEvent<M> tge = new TreeGridEvent<M>(this);
      tge.setModel(model);
      if (expand) {
        if (!node.isLeaf()) {
          // if we have a loader and node is not loaded make
          // load request and exit method
          if (loader != null && (!node.loaded || !caching) && !filtering) {
            treeStore.removeAll(model);
            node.expand = true;
            loader.loadChildren(model);
            return;
          }
          if (!node.expanded && fireEvent(Events.BeforeExpand, tge)) {
            node.expanded = true;

            if (!node.childrenRendered) {
              renderChildren(model);
              node.childrenRendered = true;
            }
            // expand
            treeGridView.expand(node);
            
            if (isStateful() && store.getKeyProvider() != null) {
              Map<String, Object> state = getState();
              List<String> expanded = (List) state.get("expanded");
              if (expanded == null) {
                expanded = new ArrayList<String>();
                state.put("expanded", expanded);
              }
              String id = store.getKeyProvider().getKey(model);
              if (!expanded.contains(id)) {
                if (expanded.size() > maxExpandEntries) {
                  expanded.remove(0);
                }
                expanded.add(id);
                saveState();
              }
            }

            M parent = treeStore.getParent(model);
            while (parent != null) {
              TreeNode pnode = findNode(parent);
              if (!pnode.expanded) {
                setExpanded(pnode.m, true);
              }
              parent = treeStore.getParent(parent);
            }
            fireEvent(Events.Expand, tge);
          }
        }
        if (deep) {
          setExpandChildren(model, true);
        }

      } else {
        if (node.expanded && fireEvent(Events.BeforeCollapse, tge)) {
          node.expanded = false;
          // collapse
          treeGridView.collapse(node);
          
          if (isStateful() && store.getKeyProvider() != null) {
            Map<String, Object> state = getState();
            List<String> expanded = (List) state.get("expanded");
            String id = store.getKeyProvider().getKey(model);
            if (expanded != null && expanded.contains(id)) {
              expanded.remove(id);
              saveState();
            }
          }
          
          fireEvent(Events.Collapse, tge);
        }
        if (deep) {
          setExpandChildren(model, false);
        }
      }
    }
  }

  /**
   * Sets the tree's model icon provider which provides the icon style for each
   * model.
   * 
   * @param iconProvider the icon provider
   */
  public void setIconProvider(ModelIconProvider<M> iconProvider) {
    this.iconProvider = iconProvider;
  }

  /**
   * Toggles the model's expand state.
   * 
   * @param model the model
   */
  public void toggle(M model) {
    TreeNode node = findNode(model);
    if (node != null) {
      setExpanded(model, !node.expanded);
    }
  }

  protected void afterRenderView() {
    super.afterRenderView();

    if (treeStore.getRootItems().size() == 0 && loader != null) {
      loader.load();
    } else {
      renderChildren(null);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new TreeGridEvent(this, event);
  }

  protected TreeNode findNode(M model) {
    if (model == null) return null;
    return nodes.get(cache.get(model));
  }

  protected boolean hasChildren(M model) {
    TreeNode node = findNode(model);
    if (loader != null && !node.loaded) {
      return loader.hasChildren(model);
    }
    if (!node.leaf || treeStore.getChildCount(model) > 0) {
      return true;
    }
    return false;
  }

  protected void onAdd(TreeStoreEvent<M> se) {
    M p = se.getParent();
    if (p == null) {
      for (M child : se.getChildren()) {
        register(child);
      }
      if (se.getIndex() > 0) {
        M prev = treeStore.getChild(se.getIndex() - 1);
        int index = findLastOpenChildIndex(prev);
        store.insert(se.getChildren(), index + 1);
      } else {
        store.insert(se.getChildren(), se.getIndex());
      }
    } else {
      TreeNode node = findNode(p);
      if (node != null) {
        for (M child : se.getChildren()) {
          register(child);
        }
        if (!node.expanded) {
          refresh(p);
          return;
        }
        int index = se.getIndex();
        int pindex = store.indexOf(p);

        if (index == 0) {
          store.insert(se.getChildren(), pindex + 1);
        } else {
          index = store.indexOf(treeStore.getChild(p, index - 1));
          TreeNode mark = findNode(store.getAt(index));
          index = findLastOpenChildIndex(mark.m);
          store.insert(se.getChildren(), index + 1);
        }
        refresh(p);
      }
    }
  }

  @Override
  protected void onClick(GridEvent<M> e) {
    M m = e.getModel();
    if (m != null) {
      TreeNode node = findNode(m);
      if (node != null) {
        Element jointEl = treeGridView.getJointElement(node);
        if (jointEl != null && e.within(jointEl)) {
          toggle(m);
        } else {
          super.onClick(e);
        }
      }
    }
  }

  protected void onDataChanged(TreeStoreEvent<M> se) {
    if (!isRendered() || !viewReady) {
      return;
    }

    M p = se.getParent();
    if (p == null) {
      store.removeAll();
      nodes.clear();
      renderChildren(null);
      if (isStateful() && treeStore.getKeyProvider() != null) {
        statefulExpand(treeStore.getRootItems());
      }
    } else {
      TreeNode n = findNode(p);
      n.loaded = true;

      renderChildren(p);

      if (n.expand && !n.isLeaf()) {
        n.expand = false;
        setExpanded(p, true);
      }
      if (isStateful() && treeStore.getKeyProvider() != null) {
        statefulExpand(treeStore.getChildren(p));
      }
    }
  }

  @Override
  protected void onDoubleClick(GridEvent<M> e) {
    super.onDoubleClick(e);
    M m = e.getModel();
    if (m != null) {
      TreeNode node = findNode(m);
      setExpanded(node.m, !node.expanded);
    }
  }

  protected void onFilter(TreeStoreEvent<M> se) {
    onDataChanged(se);
  }

  @Override
  protected void onMouseDown(GridEvent<M> e) {
    super.onMouseDown(e);
    // stop text from selection on double click
    if (GXT.isChrome || GXT.isSafari4) {
      e.preventDefault();
    }
  }

  protected void onRemove(TreeStoreEvent<M> se) {
    unregister(se.getChild());
    store.remove(se.getChild());
    for (M child : se.getChildren()) {
      unregister(child);
      store.remove(child);
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);

    el().setTabIndex(0);
    el().setElementAttribute("hideFocus", "true");

    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS | Event.KEYEVENTS);
  }

  protected void onUpdate(TreeStoreEvent<M> se) {
    store.fireEvent(Store.Update, se);
  }

  protected void refresh(M model) {
    TreeNode node = findNode(model);
    if (node != null) {
      AbstractImagePrototype style = calculateIconStyle(model);
      treeGridView.onIconStyleChange(findNode(model), style);
      Joint j = calcualteJoint(model);
      treeGridView.onJointChange(node, j);
    }
  }

  protected String register(M m) {
    String id = XDOM.getUniqueId();
    if (cache.get(m) != null) {
      id = cache.get(m);
    }
    if (!nodes.containsKey(id)) {
      cache.put(m, id);
      nodes.put(id, new TreeNode(id, m));
    }
    return id;
  }

  protected void renderChildren(M parent) {
    List<M> children = parent == null ? treeStore.getRootItems() : treeStore.getChildren(parent);

    for (M child : children) {
      register(child);
    }

    if (parent == null) {
      store.add(children);
    }

    for (M child : children) {
      if (loader != null) {
        if (autoLoad) {
          if (store.isFiltered()) {
            renderChildren(child);
          } else {
            loader.loadChildren(child);
          }
        }
      }
    }
  }

  protected void unregister(M m) {
    if (m != null) {
      nodes.remove(cache.get(m));
      cache.remove(m);
    }
  }

  Joint calcualteJoint(M model) {
    if (model == null) {
      return Joint.NONE;
    }
    TreeNode node = findNode(model);
    if (node == null) {
      
      System.out.println("sdfsdf");
    }
    Joint joint = Joint.NONE;

    if (!node.isLeaf()) {
      boolean children = true;

      if (node.isExpanded()) {
        joint = children ? Joint.EXPANDED : Joint.NONE;
      } else {
        joint = children ? Joint.COLLAPSED : Joint.NONE;
      }
    }
    return joint;
  }

  AbstractImagePrototype calculateIconStyle(M model) {
    AbstractImagePrototype style = null;
    if (iconProvider != null) {
      AbstractImagePrototype iconStyle = iconProvider.getIcon((M) model);
      if (iconStyle != null) {
        return iconStyle;
      }
    }
    TreeNode node = findNode(model);
    TreeStyle ts = getStyle();
    if (!node.isLeaf()) {
      if (isExpanded(model)) {
        style = ts.getNodeOpenIcon();
      } else {
        style = ts.getNodeCloseIcon();
      }
    } else {
      style = ts.getLeafIcon();
    }
    return style;
  }

  int findLastOpenChildIndex(M model) {
    TreeNode mark = findNode(model);
    M lc = null;
    while (mark != null && mark.expanded) {
      lc = treeStore.getLastChild(mark.m);
      mark = findNode(lc);
    }
    if (lc != null) {
      return store.indexOf(lc);
    }

    return store.indexOf(model);
  }

  private void setExpandChildren(M m, boolean expand) {
    for (M child : treeStore.getChildren(m)) {
      setExpanded(child, expand, true);
    }
  }
  
  @SuppressWarnings("unchecked")
  private void statefulExpand(List<M> children) {
    if (isStateful() && treeStore.getKeyProvider() != null) {
      List<String> expanded = (List) getState().get("expanded");
      for (M child : children) {
        String id = treeStore.getKeyProvider().getKey(child);
        if (expanded != null && expanded.contains(id)) {
          setExpanded(child, true);
        }
      }
    }
  }
}
