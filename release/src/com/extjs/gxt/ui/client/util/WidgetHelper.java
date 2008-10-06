/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows doAttach and doDetach to be called on widgets.
 * 
 * @deprecated use ComponentHelper
 */
public class WidgetHelper {

  public static void doAttach(Widget widget) {
    ComponentHelper.doAttach(widget);
  }
  
  public static void doDetach(Widget widget) {
    ComponentHelper.doDetach(widget);
  }
  
  public static void setLayoutData(Component c, LayoutData data){
    ComponentHelper.setLayoutData(c, data);
  }
  
  public static LayoutData getLayoutData(Component c) {
    return ComponentHelper.getLayoutData(c);
  }
  
  public static void setModel(Component c, ModelData model){
    ComponentHelper.setModel(c, model);
  }

}
