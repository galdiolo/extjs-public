/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A <code>ModelData</code> instance that wraps a bean.
 * 
 * <p/> Nested beans are supported when creating BeanModel instances with
 * limited support for nested lists of beans. Any child lists must be defined
 * with java.util.List and must be paramertized with a BeanModelTag class or
 * subclass.
 * 
 * <p/> When working with bean models, avoid settting beans as values, rather,
 * set the wrapping bean model instance.
 * 
 * <pre><code>
 * private List<NumberTag> numbers;
 * </code></pre>
 */
public class BeanModel extends BaseModel {

  protected Object bean;
  protected Map<String, BeanModel> nestedModels = new HashMap<String, BeanModel>();
  protected List<String> beanProperties = new ArrayList<String>();

  protected BeanModel() {

  }

  /**
   * Returns the bean.
   * 
   * @return the bean
   */
  public <X> X getBean() {
    return (X) bean;
  }

  @Override
  public Collection<String> getPropertyNames() {
    Collection c = super.getPropertyNames();
    for (String s : beanProperties) {
      c.add(s);
    }
    return c;
  }

  @Override
  public String toString() {
    return ((Object) getBean()).toString();
  }

  protected Object processValue(Object value) {
    return value;
  }

  /**
   * Sets the bean.
   * 
   * @param bean the bean
   */
  protected void setBean(Object bean) {
    this.bean = bean;
  }

}
