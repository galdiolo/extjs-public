/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Basic implementation of the <code>ModelStringProvider</code> interface.
 * Simply calls toSring on the value.
 */
public class BaseModelStringProvider implements ModelStringProvider {

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.data.ModelStringProvider#getStringValue(com.extjs.gxt.ui.client.data.ModelData,
   *      java.lang.String)
   */
  public String getStringValue(ModelData model, String property) {
    Object value = model.get(property);
    return value == null ? "" : value.toString();
  }

}
