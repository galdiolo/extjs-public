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
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows doAttach and doDetach to be called on widgets.
 */
public class WidgetHelper {

  public static void doAttach(Widget widget) {
    if (widget != null && !widget.isAttached()) {
      doAttachNative(widget);
    }
  }
  
  public static void doDetach(Widget widget) {
    if (widget != null && widget.isAttached()) {
      doDetachNative(widget);
    }
  }
  
  public static native void setLayoutData(Component c, LayoutData data) /*-{
    c.@com.extjs.gxt.ui.client.widget.Component::layoutData = data;
  }-*/;
  
  public static native LayoutData getLayoutData(Component c) /*-{
    return c.@com.extjs.gxt.ui.client.widget.Component::layoutData;
  }-*/;
  
  public static native void setModel(Component c, ModelData model) /*-{
    c.@com.extjs.gxt.ui.client.widget.Component::model = model;
  }-*/;
  
  static native void doAttachNative(Widget widget) /*-{
    widget.@com.google.gwt.user.client.ui.Widget::onAttach()();
  }-*/;

  static native void doDetachNative(Widget widget) /*-{
   widget.@com.google.gwt.user.client.ui.Widget::onDetach()();
  }-*/;
}
