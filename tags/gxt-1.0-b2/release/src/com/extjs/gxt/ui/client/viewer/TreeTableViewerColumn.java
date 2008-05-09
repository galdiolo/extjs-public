/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     GXT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import com.extjs.gxt.ui.client.widget.table.TableColumn;

/**
 * <code>ViewerColumn</code> implementation for TreeTableViewer to enable
 * column-specific label providers.
 */
public class TreeTableViewerColumn extends ViewerColumn {
  private TableColumn column;

  /**
   * Creates a new viewer column for the given <code>TreeTableViewer</code> on
   * the given <code>TreeTableColumn</code>.
   * 
   * @param viewer the tree table viewer to which this column belongs
   * @param column the underlying table column
   */
  public TreeTableViewerColumn(TreeTableViewer viewer, TableColumn column) {
    super(viewer);

    this.column = column;
    this.column.setData(ViewerColumn.COLUMN_VIEWER_KEY, this);
  }

  /**
   * Returns the table column.
   * 
   * @return the table column
   */
  public TableColumn getColumn() {
    return column;
  }
}
