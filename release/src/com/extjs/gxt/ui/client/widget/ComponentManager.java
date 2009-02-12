/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ComponentManagerEvent;

/**
 * Provides a registry of all attached components. Only components currently
 * part of the page (DOM) are part of the registry as all components register
 * when attached, and unregister when detached.
 * 
 * <pre>
    Component comp = ComponentManager.get().get("foo");
    // do something with comp ...
    Collection<Component> components = ComponentManager.get().getAll();
    for (Component c : components) {
      // do something with c ...
    }
 * </pre>
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Register</b> : ComponentManagerEvent(source, component)<br>
 * <div>Fires after the component is registered.</div>
 * <ul>
 * <li>source : this</li>
 * <li>component : the registered component</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Unregister</b> : ComponentManagerEvent(source, component)<br>
 * <div>Fires after the component is unregistered.</div>
 * <ul>
 * <li>source : this</li>
 * <li>component : the registered component</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class ComponentManager extends BaseObservable {

  private static ComponentManager instance = new ComponentManager();

  /**
   * Returns the component manager instance.
   * 
   * @return the component manager
   */
  public static ComponentManager get() {
    return instance;
  }

  private Map<String, Component> map;

  ComponentManager() {
    map = new HashMap<String, Component>();
  }

  /**
   * Returns all component by class.
   * 
   * @param clazz the class to match
   * @return the list of matching components
   */
  public List<Component> get(Class clazz) {
    List<Component> temp = new ArrayList<Component>();
    for (Component c : map.values()) {
      if (c.getClass() == clazz) {
        temp.add(c);
      }
    }
    return temp;
  }

  /**
   * Returns a component by id.
   * 
   * @param id the component id
   * @return the component or null if no match
   */
  public Component get(String id) {
    return map.get(id);
  }

  /**
   * Returns a collection of all registered components.
   * 
   * @return the collection of components
   */
  public Collection<Component> getAll() {
    return map.values();
  }

  void register(Component component) {
    map.put(component.getId(), component);
    fireEvent(Events.Register, new ComponentManagerEvent(this, component));
  }

  void unregister(Component component) {
    map.remove(component.getId());
    fireEvent(Events.Unregister, new ComponentManagerEvent(this, component));
  }

}
