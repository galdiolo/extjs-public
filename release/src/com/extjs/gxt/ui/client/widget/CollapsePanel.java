/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * The collapse state of a content panel.
 */
public class CollapsePanel extends ContentPanel {

  private ToolButton collapseBtn;
  private BorderLayoutData parentData;
  private boolean expanded;
  private El headerEl;
  private ContentPanel panel;
  private Popup popup;

  /**
   * Creates a new collapse panel.
   * 
   * @param panel the parent content panel
   * @param data the border layout data
   */
  public CollapsePanel(ContentPanel panel, BorderLayoutData data) {
    this.panel = panel;
    this.parentData = data;
    this.collapsed = true;
  }

  /**
   * Returns the panel's content panel.
   * 
   * @return the content panel
   */
  public ContentPanel getContentPanel() {
    return panel;
  }

  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    if (!ce.within(collapseBtn.getElement())) {
      if (ce.type == Event.ONCLICK) {
        setExpanded(!expanded);
      }
    }
  }

  public void setExpanded(boolean expanded) {
    ContentPanel panel = (ContentPanel) getData("panel");
    if (!this.expanded && expanded) {
      onShowPanel(panel);
    } else if (this.expanded && !expanded) {
      onHidePanel(panel);
    }
  }

  protected void adjustPosition(Rectangle box) {
    // hack adjustments
    if (XDOM.isVisibleBox) {
      box.x--;
    } else if (GXT.isGecko) {
      box.x -= 2;
      box.y--;
    }
  }

  protected void afterHidePanel(ContentPanel panel) {
    SplitBar bar = (SplitBar) panel.getData("splitBar");
    if (bar != null) {
      bar.show();
    }
  }

  protected void afterShowPanel(ContentPanel panel) {
    SplitBar bar = (SplitBar) panel.getData("splitBar");
    if (bar != null) {
      bar.hide();
    }
  }

  protected void doAttachChildren() {
    super.doAttachChildren();
    collapseBtn.onAttach();
  }

  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(collapseBtn);
  }

  protected void onExpand() {
    panel.getHeader().show();
  }

  protected void onExpandButton(BaseEvent be) {

  }

  protected void onHidePanel(ContentPanel panel) {
    this.expanded = false;
    if (popup != null) {
      panel.body.removeStyleName("x-panel-popup-body");
      panel.getHeader().show();
      popup.hide();
      afterHidePanel(panel);
    }
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    el.removeChildren();
    headerEl = el.createChild("<div class=x-panel-header></div>");

    String icon = null;
    switch (parentData.region) {
      case WEST:
        icon = "right";
        break;
      case EAST:
        icon = "left";
        break;
      case NORTH:
        icon = "down";
        break;
      case SOUTH:
        icon = "up";
        break;
    }

    setStyleName("x-layout-collapsed");
    collapseBtn = new ToolButton("x-tool-" + icon);
    collapseBtn.render(headerEl.dom);

    collapseBtn.addListener(Events.Select, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        if (expanded) {
          setExpanded(false);
        }
        onExpandButton(ce);
      }
    });

    if (parentData.floatable) {
      el.addEventsSunk(Event.MOUSEEVENTS | Event.ONCLICK);
      addStyleOnOver(getElement(), "x-layout-collapsed-over");
    }

    el.setVisibility(true);
  }

  protected void onShowPanel(ContentPanel panel) {
    this.expanded = true;
    Rectangle box = getBounds(false);
    adjustPosition(box);

    panel.setPosition(0, 0);
    popup = new Popup() {
      protected boolean onAutoHide(Event event) {
        setExpanded(false);
        return false;
      }

    };
    popup.getIgnoreList().add(collapseBtn.getElement());
    popup.setStyleName("x-layout-popup");
    popup.setLayout(new FillLayout(4));
    popup.shadow = true;
    int hh = fly(el.firstChild().dom).getHeight();
    panel.getHeader().hide();
    panel.body.addStyleName("x-panel-popup-body");
    popup.add(panel);
    int w = (int) parentData.size;
    int h = box.height - hh;
    popup.setSize(w, h);
    popup.showAt(box.x + box.width, box.y + hh);
    layout(true);

    afterShowPanel(panel);
  }
}
