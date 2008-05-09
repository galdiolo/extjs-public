/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

/**
 * <code>FillLayout</code> lays out widgets in a single row or column, forcing
 * them to be the same size. <code>FillLayout</code> does not wrap, but you
 * can specify margins and spacing.
 * 
 * <p>
 * Example code: first a <code>FillLayout</code> is created and its type field
 * is set, and then the layout is set into the <code>WidgetContainer</code>.
 * Note that in a <code>FillLayout</code>, children are always the same size,
 * and they fill all available space.
 * 
 * <pre>
 *      FillLayout fillLayout = new FillLayout();
 *      fillLayout.orientation = Orientation.HORIZONTAL;
 *      container.setLayout(fillLayout);
 * </pre>
 * 
 * </p>
 */
public class FillLayout extends Layout {

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
   * Specifies how widgets will be positioned within the layout (default value
   * is HORIZONTAL).
   */
  public Orientation orientation = Orientation.HORIZONTAL;

  /**
   * Creates a new fill layout.
   */
  public FillLayout() {

  }

  /**
   * Creates a new fill layout with the given margin.
   * 
   * @param margin the margin in pixels
   */
  public FillLayout(int margin) {
    this.margin = margin;
  }

  @Override
  protected void onLayout(Container container, El target) {
    super.onLayout(container, target);

    int count = container.getItemCount();
    if (count < 1) {
      return;
    }

    for (int i = 0; i < count; i++) {
      Component c = container.getItem(i);
      c.el.makePositionable(i != 0);
    }

    Rectangle rect = new Rectangle();
    rect.x = 0;
    rect.y = 0;
    
    Size s = target.getStyleSize();
    rect.width = s.width;
    rect.height = s.height;

    int width = rect.width - margin * 2;
    int height = rect.height - margin * 2;

    if (orientation == Orientation.HORIZONTAL) {
      width -= (count - 1) * spacing;
      int x = rect.x + margin, extra = width % count;
      int y = rect.y + margin, cellWidth = width / count;
      x -= target.getScrollLeft();
      y -= target.getScrollTop();
      for (int i = 0; i < count; i++) {
        Component child = container.getItem(i);
        int childWidth = cellWidth;
        if (i == 0) {
          childWidth += extra / 2;
        } else {
          if (i == count - 1) childWidth += (extra + 1) / 2;
        }
        child.el.setLeftTop(x, y);
        setSize(child, childWidth, height);
        x += childWidth + spacing;
      }
    } else {
      height -= (count - 1) * spacing;
      int x = rect.x + margin, cellHeight = height / count;
      int y = rect.y + margin, extra = height % count;
      x -= target.getScrollLeft();
      y -= target.getScrollTop();
      for (int i = 0; i < count; i++) {
        Component child = container.getItem(i);
        int childHeight = cellHeight;
        if (i == 0) {
          childHeight += extra / 2;
        } else {
          if (i == count - 1) childHeight += (extra + 1) / 2;
        }
        
        child.el.setLeftTop(x, y);
        setSize(child, width, childHeight);
        y += childHeight + spacing;
      }
    }
  }

}
