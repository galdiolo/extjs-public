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

import com.extjs.gxt.ui.client.Style;


/**
 * Layout data for <code>RowLayout</code> that controls the width and height of a widget.
 * 
 * @see RowLayout
 */
public class RowData {

  /**
   * Constant for filling in both directions.
   */
  public static final int FILL_BOTH = 0;

  /**
   * Constant for filling horizontally.
   */
  public static final int FILL_HORIZONTAL = 1;

  /**
   * Constant for filling vertically.
   */
  public static final int FILL_VERTICAL = 2;

  /**
   * Specifies the preferred height in pixels (default value is DEFAULT). The
   * default value indicates that the layout should use the widget's computed
   * height.
   */
  public float height = Style.DEFAULT;

  /**
   * Specifies the preferred height in pixels (default value is DEFAULT). The
   * default value indicates that the layout should use the widget's computed
   * width.
   */
  public float width = Style.DEFAULT;

  /**
   * Specifies if the widget's width should fill it's region. When
   * <code>true</code>, the width field is ignored (default value is
   * <code>false</code>).
   */
  public boolean fillWidth;

  /**
   * Specifies if the widget's height should fill it's region. When
   * <code>true</code>, the height field is ignored (default value is
   * <code>false</code>.
   */
  public boolean fillHeight;

  float calcWidth, calcHeight;

  /**
   * Creates a new row data.
   */
  public RowData() {

  }

  /**
   * Creates a new row data.
   * 
   * @param height the height in pixels
   * @param width the width in pixels
   */
  public RowData(float height, float width) {
    this.height = height;
    this.width = width;
  }

  /**
   * Creates a new row data with the given type.
   * 
   * <p>
   * Possible values are:
   * <ul>
   * <li>FILL_BOTH</li>
   * <li>FILL_HORIZONTALLY</li>
   * <li>FILL_VERTICALLY</li>
   * </ul>
   * </p>
   * @param type the type
   */
  public RowData(int type) {
    switch (type) {
      case FILL_BOTH:
        fillWidth = true;
        fillHeight = true;
        break;
      case FILL_HORIZONTAL:
        fillWidth = true;
        break;
      case FILL_VERTICAL:
        fillHeight = true;
        break;
    }
  }

}
