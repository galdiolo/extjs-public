/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

/**
 * This layout positions the container's children in a single horiztontal or
 * vertical row. Each component may specify its height and width in pixels or as
 * percentage.
 * 
 * <p/> Each components margin may also be specified using a RowData instance.
 * Only 1 component should specify a margin on adjacent sides. The following
 * code would provide a 4px margin for 3 components aligned vertically:
 * 
 * <code><pre>
 * child1.setStyleAttribute("margin", "4px");
 * child2.setStyleAttribute("margin", "0 4px");
 * child3.setStyleAttribute("margin", "4px");
 * </pre></code>
 * 
 * @see RowData
 */
public class RowLayout extends Layout {

  private Orientation orientation = Orientation.VERTICAL;

  /**
   * Creates a new vertical row layout.
   */
  public RowLayout() {
    this(Orientation.VERTICAL);
  }

  /**
   * Creates a new row layout with the given orientation.
   * 
   * @param orientation the orientation of row layout
   */
  public RowLayout(Orientation orientation) {
    this.orientation = orientation;
    monitorResize = true;
  }

  /**
   * Returns the orientation of the layout.
   * 
   * @return the orientation
   */
  public Orientation getOrientation() {
    return orientation;
  }

  /**
   * Sets the orientation of the layout.
   * 
   * @param orientation the orientation
   */
  public void setOrientation(Orientation orientation) {
    this.orientation = orientation;
  }

  protected void layoutHorizontal(El target) {
    for (Component c : this.container.getItems()) {
      c.addStyleName("x-column");
    }

    Size size = target.getStyleSize();

    int w = size.width;
    int h = size.height;
    int pw = w;

    int count = container.getItemCount();

    // some columns can be percentages while others are fixed
    // so we need to make 2 passes
    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      RowData data = (RowData) c.getData();
      if (data.getWidth() > 1) {
        pw -= data.getWidth();
      } else if (data.getWidth() == -1) {
        pw -= c.getOffsetWidth();
      }
    }

    pw = pw < 0 ? 0 : pw;

    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      RowData data = (RowData) c.getData();
      if (data == null) {
        data = new RowData();
        c.setData(data);
      }
      double height = data.getHeight();

      if (height > 0 && height <= 1) {
        height = height * h;
      } else

      if (height == -1) {
        height = c.el().getHeight(true);
      }

      double width = data.getWidth();
      if (width > 0 && width <= 1) {
        width = width * pw;
      }

      setSize(c, (int) width, (int) height);

    }
  }

  protected void layoutVertical(El target) {
    Size size = target.getStyleSize();

    int w = size.width;
    int h = size.height;
    int ph = h;

    int count = container.getItemCount();

    // some columns can be percentages while others are fixed
    // so we need to make 2 passes
    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      RowData data = (RowData) c.getData();
      if (data == null) {
        data = new RowData();
        c.setData(data);
      }

      if (data.getHeight() > 1) {
        ph -= data.getHeight();
      } else if (data.getHeight() == -1) {
        ph -= c.getOffsetHeight();
      }

      ph -= c.el().getMargins("tb");
    }

    ph = ph < 0 ? 0 : ph;

    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      RowData data = (RowData) c.getData();

      double width = data.getWidth();

      if (width > 0 && width <= 1) {
        width = width * w;
      }

      int adj = c.el().getMargins("lr");
      width -= adj;

      double height = data.getHeight();
      if (height > 0 && height <= 1) {
        height = height * ph;
      }

      adj = c.el().getMargins("tb");
      height -= adj;

      setSize(c, (int) width, (int) height);
    }
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);
    target.setStyleAttribute("overflow", "hidden");
    target.makePositionable();

    if (orientation == Orientation.VERTICAL) {
      layoutVertical(target);
    } else {
      layoutHorizontal(target);
    }
  }

}
