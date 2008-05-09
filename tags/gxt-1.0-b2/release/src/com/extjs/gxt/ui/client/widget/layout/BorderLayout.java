/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SplitBarEvent;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.CollapsePanel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.SplitBar;
import com.extjs.gxt.ui.client.widget.ToolButton;

/**
 * This is a multi-pane, application-oriented UI layout style that supports
 * multiple regions, automatic split bars between regions and built-in expanding
 * and collapsing of regions.
 */
public class BorderLayout extends Layout {

  /**
   * True to enabled state (defaults to true). When true, expand / collapse and
   * size state is persisted across user sessions.
   */
  public boolean enableState = true;

  protected Map<LayoutRegion, SplitBar> splitBars;
  private Listener collapseListener;
  private int minimumSize = 100;
  private BoxComponent north, south;
  private BoxComponent west, east, center;
  private boolean rendered;

  public BorderLayout() {
    splitBars = new HashMap<LayoutRegion, SplitBar>();
    collapseListener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        ToolButton btn = (ToolButton) ce.component;
        ContentPanel panel = (ContentPanel) btn.getData("panel");
        onCollapse(panel);
      }
    };
  }

  protected SplitBar createSplitBar(LayoutRegion region, BoxComponent component) {
    return new SplitBar(region, component);
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);

    if (!rendered) {
      target.makePositionable();
      target.addStyleName("x-border-layout-ct");
      int count = container.getItemCount();
      for (int i = 0; i < count; i++) {
        Component c = container.getItem(i);
        if (!c.isRendered()) {
          c.addStyleName("x-border-panel");
          c.render(target.dom, i);
        }
        if (enableState) {
          BorderLayoutData data = (BorderLayoutData) c.getData();
          Map<String, Object> st = c.getState();
          if (st.containsKey("collapsed")) {
            switchPanels((ContentPanel) c);
          } else if (st.containsKey("size") && (!(c instanceof CollapsePanel))) {
            data.size = (Float) st.get("size");
          }
        }
      }
      rendered = true;
    }

    Rectangle rect = target.getBounds();
    int w = rect.width;
    int h = rect.height;
    int centerW = w, centerH = h, centerY = 0, centerX = 0;

    north = getRegionWidget(LayoutRegion.NORTH);
    south = getRegionWidget(LayoutRegion.SOUTH);
    west = getRegionWidget(LayoutRegion.WEST);
    east = getRegionWidget(LayoutRegion.EAST);
    center = getRegionWidget(LayoutRegion.CENTER);

    if (north != null) {
      BorderLayoutData data = (BorderLayoutData) north.getData();
      if (north.getData("init") == null) {
        initPanel(north);
      }
      if (data.split) {
        initSplitBar(LayoutRegion.SOUTH, north, data);
      } else {
        removeSplitBar(LayoutRegion.SOUTH);
      }
      Rectangle b = new Rectangle();
      Margins m = getMargins(north, data);
      float s = data.size < 1 ? data.size * rect.width : data.size;
      b.height = (int) s;
      b.width = w - (m.left + m.right);
      b.x = m.left;
      b.y = m.top;
      centerY = b.height + b.y + m.bottom;
      centerH -= centerY;
      applyLayout(north, b);

    }
    if (south != null) {
      BorderLayoutData data = (BorderLayoutData) south.getData();
      if (south.getData("init") == null) {
        initPanel(south);
      }
      if (data.split) {
        initSplitBar(LayoutRegion.NORTH, south, data);
      } else {
        removeSplitBar(LayoutRegion.NORTH);
      }
      Rectangle b = south.getBounds(false);
      Margins m = getMargins(south, data);
      float s = data.size < 1 ? data.size * rect.width : data.size;
      b.height = (int) s;
      b.width = w - (m.left + m.right);
      b.x = m.left;
      int totalHeight = (b.height + m.top + m.bottom);
      b.y = h - totalHeight + m.top;
      centerH -= totalHeight;
      applyLayout(south, b);
    }

    if (west != null) {
      if (west.getData("init") == null) {
        initPanel(west);
      }
      BorderLayoutData data = (BorderLayoutData) west.getData();
      if (data.split) {
        initSplitBar(LayoutRegion.EAST, west, data);
      } else {
        removeSplitBar(LayoutRegion.EAST);
      }

      Rectangle box = new Rectangle();
      Margins m = getMargins(west, data);
      float s = data.size < 1 ? data.size * rect.width : data.size;
      box.width = (int) s;
      box.height = centerH - (m.top + m.bottom);
      box.x = m.left;
      box.y = centerY + m.top;
      int totalWidth = (box.width + m.left + m.right);
      centerX += totalWidth;
      centerW -= totalWidth;
      applyLayout(west, box);
    }
    if (east != null) {
      if (east.getData("init") == null) {
        initPanel(east);
      }
      BorderLayoutData data = (BorderLayoutData) east.getData();
      if (data.split) {
        initSplitBar(LayoutRegion.WEST, east, data);
      } else {
        removeSplitBar(LayoutRegion.WEST);
      }
      Rectangle b = east.getBounds(false);
      Margins m = getMargins(east, data);
      float s = data.size < 1 ? data.size * rect.width : data.size;
      b.width = (int) s;
      b.height = centerH - (m.top + m.bottom);
      int totalWidth = (b.width + m.left + m.right);
      b.x = w - totalWidth + m.left;
      b.y = centerY + m.top;
      centerW -= totalWidth;
      applyLayout(east, b);
    }
    BorderLayoutData data = (BorderLayoutData) center.getData();
    Margins m = data.margins != null ? data.margins : data.defaultMargins;
    Rectangle box = new Rectangle();
    box.x = centerX + m.left;
    box.y = centerY + m.top;
    box.width = centerW - (m.left + m.right);
    box.height = centerH - (m.top + m.bottom);
    applyLayout(center, box);
  }

  private void applyLayout(BoxComponent component, Rectangle box) {
    component.el.makePositionable(true);
    component.el.setLeftTop(box.x, box.y);
    component.setSize(box.width, box.height);
  }

  private CollapsePanel createCollapsePanel(ContentPanel panel, BorderLayoutData data) {
    CollapsePanel cp = new CollapsePanel(panel, data) {
      protected void onExpandButton(BaseEvent be) {
        if (isExpanded()) {
          setExpanded(false);
        }
        onExpandClick(this);
      }
    };
    BorderLayoutData collapseData = new BorderLayoutData(data.region);
    collapseData.size = 24;
    collapseData.margins = data.margins;
    cp.setData(collapseData);
    cp.setData("panel", panel);
    panel.setData("collapse", cp);
    return cp;
  }

  private Margins getMargins(BoxComponent component, BorderLayoutData data) {
    if (data.margins != null) {
      return data.margins;
    }
    switch (data.region) {
      case NORTH:
      case SOUTH:
        return data.defaultNSMargins;
      case WEST:
      case EAST:
        return data.defaultEWMargins;
      default:
        return data.defaultMargins;
    }
  }

  private BoxComponent getRegionWidget(LayoutRegion region) {
    for (int i = 0; i < container.getItemCount(); i++) {
      container.el.makePositionable(true);
    }
    for (int i = 0; i < container.getItemCount(); i++) {
      BoxComponent w = (BoxComponent) container.getItem(i);
      if (w.getData() != null && w.getData() instanceof BorderLayoutData) {
        BorderLayoutData data = (BorderLayoutData) w.getData();
        if (data.region == region) {
          return w;
        }
      }
    }
    return null;
  }

  private void initPanel(BoxComponent component) {
    BorderLayoutData data = (BorderLayoutData) component.getData();
    String icon = null;
    switch (data.region) {
      case WEST:
        icon = "left";
        break;
      case EAST:
        icon = "right";
        break;
      case NORTH:
        icon = "up";
        break;
      case SOUTH:
        icon = "down";
        break;
    }
    if (data.collapsible && component instanceof ContentPanel) {
      ContentPanel panel = (ContentPanel) component;
      ToolButton collapse = (ToolButton) panel.getWidget("collapseBtn");
      if (collapse == null) {
        collapse = new ToolButton("x-tool-" + icon);
        panel.getHeader().addTool(collapse);
      }
      collapse.setData("panel", panel);
      collapse.removeListener(Events.Select, collapseListener);
      collapse.addListener(Events.Select, collapseListener);
      panel.setData("collapseBtn", collapse);
      panel.setData("init", "true");
    }

  }

  private void initSplitBar(final LayoutRegion region, BoxComponent component,
      final BorderLayoutData data) {
    SplitBar bar = (SplitBar) component.getData("splitBar");
    if (bar == null || bar.getResizeWidget() != component) {
      bar = createSplitBar(region, component);
      final SplitBar fBar = bar;
      Listener splitBarListener = new Listener<ComponentEvent>() {

        public void handleEvent(ComponentEvent ce) {
          switch (ce.type) {
            case Events.DragStart:
              switch (region) {
                case WEST: {
                  int min = Math.max(minimumSize, data.minSize);
                  int max = east.getOffsetWidth() + center.getOffsetWidth() - minimumSize;
                  if (data.maxSize > 0) {
                    max = Math.min(max, data.maxSize);
                  }
                  fBar.setMinSize(min);
                  fBar.setMaxSize(max);
                  break;
                }
                case EAST: {
                  int min = Math.max(minimumSize, data.minSize);
                  int max = west.getOffsetWidth() + center.getOffsetWidth() - minimumSize;
                  max = Math.min(data.maxSize, max);
                  fBar.setMinSize(min);
                  fBar.setMaxSize(max);
                  break;
                }
                case NORTH:
                  int max = south.getOffsetHeight() + center.getOffsetHeight()
                      - minimumSize;
                  max = Math.min(max, data.maxSize);
                  fBar.setMaxSize(max);
                  break;
                case SOUTH:
                  // TODO
                  break;
              }
              break;
          }
        }
      };
      component.setData("splitBar", bar);

      bar.addListener(Events.DragStart, splitBarListener);
      bar.setMinSize(data.minSize);
      bar.setMaxSize(data.maxSize == 0 ? bar.getMaxSize() : data.maxSize);
      bar.setAutoSize(false);
      bar.addListener(Events.Resize, new Listener<SplitBarEvent>() {
        public void handleEvent(SplitBarEvent sbe) {
          if (sbe.size < 1) {
            return;
          }
          data.size = sbe.size;
          Component c = sbe.splitBar.getResizeWidget();
          Map<String, Object> state = c.getState();
          state.put("size", data.size);
          c.saveState();
          layout(container);
        }
      });
      component.setData("splitBar", bar);
    }
  }

  private void switchPanels(ContentPanel panel) {
    BorderLayoutData data = (BorderLayoutData) panel.getData();
    container.remove(panel);
    CollapsePanel cp = (CollapsePanel) panel.getData("collapse");
    if (cp == null) {
      cp = createCollapsePanel(panel, data);
    }
    container.add(cp);
  }

  private void onCollapse(ContentPanel panel) {
    BorderLayoutData data = (BorderLayoutData) panel.getData();
    container.remove(panel);
    Map<String, Object> st = panel.getState();
    st.put("collapsed", true);
    panel.saveState();

    CollapsePanel cp = (CollapsePanel) panel.getData("collapse");
    if (cp == null) {
      cp = createCollapsePanel(panel, data);
    }
    container.add(cp);
    container.layout(true);
  }

  private void onExpandClick(CollapsePanel cp) {
    ContentPanel panel = cp.getContentPanel();
    Map<String, Object> st = panel.getState();
    st.remove("collapsed");
    panel.saveState();
    container.remove(cp);
    container.add(panel);
    container.layout(true);
  }

  private void removeSplitBar(LayoutRegion region) {
    splitBars.put(region, null);
  }

}
