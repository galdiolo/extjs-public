/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>DataReader</code> for beans using a <code>BeanModelFactory</code>.
 * Valid return types are a list of beans or a list load result containing a
 * list of beans. Beans must be of the same type (if factoryForEachBean is set to false) and beans must implement
 * {@link BeanModelTag} or have a companion {@link BeanModelMarker} marker
 * class.
 * 
 * @param <C> the config type
 */
public class BeanModelReader<C> implements DataReader<C, ListLoadResult<ModelData>> {
  
  private boolean factoryForEachBean;

  /**
   * Return if a BeanModelFactory is created for each bean or not.
   * 
   * @return true if a BeanModelFactory is created for each bean or not
   */
  public boolean isFactoryForEachBean() {
    return factoryForEachBean;
  }
  
  public ListLoadResult<ModelData> read(C loadConfig, Object data) {

    if (data instanceof List) {
      List<Object> beans = (List) data;

      if (beans.size() > 0) {
        if (factoryForEachBean) {
          List models = new ArrayList(beans.size());
          for (Object o : beans) {
            BeanModelFactory factory = BeanModelLookup.get().getFactory(o.getClass());
            if (factory == null) {
              throw new RuntimeException("No BeanModelFactory found for " + o.getClass());
            }
            models.add(factory.createModel(o));
          }
          return newLoadResult(loadConfig, models);
        } else {
          BeanModelFactory factory = BeanModelLookup.get().getFactory(
              beans.get(0).getClass());
          if (factory == null) {
            throw new RuntimeException("No BeanModelFactory found for "
                + beans.get(0).getClass());
          }
          return newLoadResult(loadConfig, (List) factory.createModel(beans));
        }
      }
      return newLoadResult(loadConfig, (List) beans);

    } else if (data instanceof ListLoadResult) {
      ListLoadResult result = (ListLoadResult) data;
      List beans = result.getData();
      if (beans.size() > 0) {
        List converted;
        if (factoryForEachBean) {
          converted = new ArrayList(beans.size());
          for (Object o : beans) {
            BeanModelFactory factory = BeanModelLookup.get().getFactory(o.getClass());
            if (factory == null) {
              throw new RuntimeException("No BeanModelFactory found for " + o.getClass());
            }
            converted.add(factory.createModel(o));
          }
        } else {
          BeanModelFactory factory = BeanModelLookup.get().getFactory(
              beans.get(0).getClass());
          if (factory == null) {
            throw new RuntimeException("No BeanModelFactory found for "
                + beans.get(0).getClass());
          }
          converted = factory.createModel(beans);
        }
        beans.clear();
        beans.addAll(converted);
      }
      return (ListLoadResult) data;
    } else {
      throw new RuntimeException("Error converting data");
    }
  }
  
  /**
   * Set to true to create a BeanModelFactory for each bean in the list
   * (defaults to false).
   * 
   * @param factoryForEachBean true to enable
   */
  public void setFactoryForEachBean(boolean factoryForEachBean) {
    this.factoryForEachBean = factoryForEachBean;
  }

  /**
   * Template method that provides load result.
   * 
   * @param models the models
   * @return the load result
   */
  protected ListLoadResult newLoadResult(C loadConfig, List<ModelData> models) {
    return new BaseListLoadResult(models);
  }

}
