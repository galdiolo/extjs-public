/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.aria;

import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.user.client.ui.Widget;

public class ButtonBarHandler extends FocusHandler {

  @Override
  public boolean canHandleKeyPress(Component component, PreviewEvent pe) {
    if (component.getParent() instanceof ButtonBar) {
      return true;
    }
    return false;
  }
  
  @Override
  public void onTab(Component component, PreviewEvent pe) {
    pe.preventDefault();
    Widget parent = component.getParent();
    focusNextWidget(parent);
  }

  @Override
  public void onRight(Component component, PreviewEvent pe) {
    ButtonBar parent = (ButtonBar)component.getParent();
    int index = parent.indexOf(component);
    parent.getItem(index == parent.getItemCount() -1 ? 0 : ++index).focus();
  }
  
  @Override
  public void onLeft(Component component, PreviewEvent pe) {
    ButtonBar parent = (ButtonBar)component.getParent();
    int index = parent.indexOf(component);
    parent.getItem(index > 0 ? --index : Math.max(0, parent.getItemCount() - 1)).focus();
  }
  
  @Override
  public void onEscape(Component component, PreviewEvent pe) {
    stepOut(component.getParent());
  }
  
}
