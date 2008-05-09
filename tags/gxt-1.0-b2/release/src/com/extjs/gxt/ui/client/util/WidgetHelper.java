/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import com.google.gwt.user.client.ui.Widget;

/**
 * Allows doAttach and doDetach to be called on widgets.
 */
public class WidgetHelper {

  public static native void doAttach(Widget widget) /*-{
      widget.@com.google.gwt.user.client.ui.Widget::onAttach()();
    }-*/;

  public static native void doDetach(Widget widget) /*-{
   widget.@com.google.gwt.user.client.ui.Widget::onDetach()();
  }-*/;
}
