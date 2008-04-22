/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;

/**
 * Layout data for <code>TableLayout</code>.
 */
public class TableData {

  /**
   * Specifies the number of columns to span (defaults to 1).
   */
  public int colspan = 1;

  /**
   * Specifies the cell's padding (default to 0).
   */
  public int padding;

  /**
   * Specifies the cell's margin (defaults to 0).
   */
  public int margin;

  /**
   * Specifies the cell's style (defaults to null).
   */
  public String style;

  /**
   * Specifies the cell's horizontal alignment (defaults to LEFT).
   * <p>
   * Valid values are:
   * <ul>
   * <li>HorizontalAlignment.LEFT</li>
   * <li>HorizontalAlignment.CENTER</li>
   * <li>HorizontalAlignment.RIGHT</li>
   * </ul>
   * </p>
   */
  public HorizontalAlignment horizontalAlign;

  /**
   * Specifies the cell's vertical alignment (defaults to TOP).
   * <p>
   * Valid values are:
   * <ul>
   * <li>VerticalAlignment.TOP</li>
   * <li>VerticalAlignment.MIDDLE</li>
   * <li>VertcialAlginment.BOTTOM</li>
   * </ul>
   * </p>
   */
  public VerticalAlignment verticalAlign = VerticalAlignment.TOP;

  /**
   * Specifies the cell's width (default value is null).
   */
  public String width;

  /**
   * Specifies the cell's height (default value is null).
   */
  public String height;

  /**
   * Creates a new table data instance.
   */
  public TableData() {

  }

  /**
   * Creates a new table data instance.
   * 
   * @param horizontalAlign the horiztonal alignment
   * @param verticalAlign the vertical aligment
   */
  public TableData(HorizontalAlignment horizontalAlign, VerticalAlignment verticalAlign) {
     this.horizontalAlign = horizontalAlign;
     this.verticalAlign = verticalAlign;
  }

}
