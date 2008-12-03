/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.widget.layout.CardLayout;

/**
 * This container contains multiple widgets, each fit to the container, where
 * only a single widget can be visible at any given time. This style is most
 * commonly used for wizards, tab implementations, etc.
 */
public class CardPanel extends LayoutContainer {

  protected CardLayout layout;

  /**
   * Creates a new card panel.
   */
  public CardPanel() {
    layout = new CardLayout();
    setLayout(layout);
  }

  /**
   * Returns the active component.
   * 
   * @return the top widget
   */
  public Component getActiveItem() {
    return layout.getActiveItem();
  }

  /**
   * Sets the active (visible) item in the layout.
   * 
   * @param component the active widget
   */
  public void setActiveItem(Component component) {
    layout.setActiveItem(component);
  }

}
