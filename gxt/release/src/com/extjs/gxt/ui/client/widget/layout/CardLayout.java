/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.CardPanel;
import com.extjs.gxt.ui.client.widget.Component;

/**
 * This layout contains multiple widgets, each fit to the container, where only
 * a single widget can be visible at any given time. This layout style is most
 * commonly used for wizards, tab implementations, etc.
 * 
 * <p /> Child Widgets are:
 * <ul>
 * <li><b>Sized</b> : Yes - expands to fill parent container.</li>
 * <li><b>Positioned</b> : No - widgets are located at 0,0.</li>
 * </ul>
 * 
 * <p /> The CardLayout's focal method is {@link #setActiveItem(Component)}.
 * Since only one panel is displayed at a time, the only way to move from one
 * panel to the next is by calling setActiveItem the next panel to display. The
 * layout itself does not provide a mechanism for handling this navigation, so
 * that functionality must be provided by the developer.
 * 
 * @see CardPanel
 */
public class CardLayout extends FitLayout {

  /**
   * Creates a new card layout instance.
   */
  public CardLayout() {
    monitorResize = true;
    renderHidden = true;
  }

  /**
   * Returns the active component.
   * 
   * @return the top widget
   */
  public Component getActiveItem() {
    return activeItem;
  }

  /**
   * Sets the active (visible) item in the layout.
   * 
   * @param component the active component
   */
  public void setActiveItem(Component component) {
    if (activeItem != component) {
      if (activeItem != null) {
        activeItem.setVisible(false);
      }
      activeItem = component;
      if (activeItem != null) {
        activeItem.setVisible(true);
        layoutContainer();
      }
    }
  }

  @Override
  protected void renderComponent(Component component, int index, El target) {
    if (activeItem == component) {
      super.renderComponent(component, index, target);
    }
  }

}
