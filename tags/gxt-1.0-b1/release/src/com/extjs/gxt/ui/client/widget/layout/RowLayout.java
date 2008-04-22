/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT <darrell@extjs.com> - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.widget.layout;


import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

/**
 * This layout positions the container's children in a single horiztonal or
 * vertical row. Unlike <code>FillLayout</code>, each child widget can be
 * configured using an associated <code>RowData</code>.
 * 
 * @see RowData
 */
public class RowLayout extends Layout {

  /**
   * Specifies how widgets will be positioned within the layout (default value
   * is VERTICAL).
   * <p>
   * Possible values are:
   * <ul>
   * <li>HORIZONTAL: Position the widgets horizontally from left to right</li>
   * <li>VERTICAL: Position the widgets vertically from top to bottom</li>
   * </p>
   */
  public Orientation type = Orientation.VERTICAL;
  
  /**
   * The number of pixels of margin that will be placed along the edges of the
   * layout (default is 0).
   */
  public int margin = 0;

  /**
   * The number of pixels between the edge of one cell and the edge of its
   * neighbouring cell (default value is 0).
   */
  public int spacing = 0;

  /**
   * Creates a new vertical row layout.
   */
  public RowLayout() {
    this(Orientation.VERTICAL);
  }

  /**
   * Creates a new row layout with the given type.
   * 
   * @param type the type of row layout
   */
  public RowLayout(Orientation type) {
    this.type = type;
  }

  public RowLayout(Orientation type, int margin, int spacing) {
    this.type = type;
    this.margin = margin;
    this.spacing = spacing;
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);

    Rectangle rect = target.getLayoutBounds();

    int size = container.getItemCount();

    int height = rect.height - (2 * margin);
    int width = rect.width - (2 * margin);
    int top = rect.y += margin;
    int left = rect.x += margin;
    left -= target.getScrollLeft();
    top -= target.getScrollTop();

    // adjust for spacing
    if (type == Orientation.HORIZONTAL) {
      width -= ((size - 1) * spacing);
    } else {
      height -= ((size - 1) * spacing);
    }

    int fillHeight = height;
    int fillWidth = width;

    int fillingHeight = 0;
    int fillingWidth = 0;

    // 1st pass
    for (int i = 0; i < size; i++) {
      Component c = container.getItem(i);
      if (!c.isVisible()) continue;
      
      c.el.makePositionable(true);

      RowData data = (RowData) c.getData();
      if (data == null) {
        data = new RowData();
        c.setData(data);
      }

      if (type == Orientation.HORIZONTAL) {
        if (data.fillWidth) {
          fillingWidth++;
        } else if (data.width != Style.DEFAULT) {
          fillWidth -= data.width;
          data.calcWidth = data.width;
        } else {
          int flowWidth = c.getOffsetWidth();
          fillWidth -= flowWidth;
          data.calcWidth = flowWidth;
        }
        if (data.fillHeight) {
          data.calcHeight = height;
        } else if (data.height != Style.DEFAULT) {
          data.calcHeight = data.height;
        } else {
          data.calcHeight = c.getOffsetHeight();
        }
      } else {
        if (data.fillHeight) {
          fillingHeight++;
        } else if (data.height != Style.DEFAULT) {
          fillHeight -= data.height;
          data.calcHeight = data.height;
        } else {
          int flowHeight = c.getOffsetHeight();
          fillHeight -= flowHeight;
          data.calcHeight = flowHeight;
        }
        if (data.fillWidth) {
          data.calcWidth = width;
        } else if (data.width != Style.DEFAULT) {
          data.calcWidth = data.width;
        } else {
          data.calcWidth = c.getOffsetWidth();
        }
      }

    }

    // 2nd pass
    for (int i = 0; i < size; i++) {
      Component c = container.getItem(i);
      if (!c.isVisible()) continue;
      RowData data = (RowData) c.getData();

      int w = (int) data.calcWidth;
      int h = (int) data.calcHeight;

      if (type == Orientation.VERTICAL) {
        if (data.fillHeight) {
          h = fillHeight / fillingHeight;
        }
      } else {
        if (data.fillWidth) {
          w = fillWidth / fillingWidth;
        }
      }

      top = Math.max(0, top);

      // do not set size for normal flow
      int fh = h;
      int fw = w;
      if (!data.fillHeight && data.height == Style.DEFAULT) {
        fh = -1;
      }
      if (!data.fillWidth && data.width == Style.DEFAULT) {
        fw = -1;
      }

      setBounds(c, left, top, fw, fh);

      if (type == Orientation.VERTICAL) {
        top = top + h + spacing;
      } else {
        left = left + w + spacing;
      }

    }
  }

}
