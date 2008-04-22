/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.widget.Layout;


/**
 * This is the layout style of choice for creating structural layouts in a
 * multi-column format where the width of each column can be specified as a
 * percentage or fixed width, but the height is allowed to vary based on the
 * content.
 * <p>
 * ColumnLayout supports a ColumnData layout object. The layout will use the
 * width (if pixels) or columnWidth (if percent) of each panel during layout to
 * determine how to size each panel. If width or columnWidth is not specified
 * for a given panel, its width will default to the panel's width (or auto).
 * </p>
 * <p>
 * The width property is always evaluated as pixels. The columnWidth property is
 * always evaluated as a percentage, and must be a decimal value greater than 0
 * and less than 1 (e.g., .25).
 * </p>
 * 
 * @see ColumnData
 */
public class ColumnLayout extends Layout {

}
