/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;


import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Container;

/**
 * Inherits the anchoring of {@link AnchorLayout} and adds the ability for x/y
 * positioning using the <code>AbsoluteData</code> x and y properties.
 * 
 * @see AbsoluteData
 */
public class AbsoluteLayout extends AnchorLayout {

  public AbsoluteLayout() {
    extraStyle = "x-abs-layout-item";
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);
    target.makePositionable();
  }
  
}
