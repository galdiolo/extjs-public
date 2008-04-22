/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import com.extjs.gxt.ui.client.data.BaseModelStringProvider;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;

/**
 * A <code>CellLabelProvider</code> for <code>Model</code> instances.
 */
public class ModelCellLabelProvider extends CellLabelProvider<ModelData> {

  protected ViewerCell viewerCell;

  public ModelStringProvider modelStringProvider = new BaseModelStringProvider();

  public void update(ViewerCell<ModelData> cell) {
    this.viewerCell = cell;
    cell.setText(getModelProperty(cell.getElement(), cell.getColumnId()));
  }

  /**
   * Returns the cell's text using the column's id. Method can be subclassed to
   * provide specialized behavior.
   * 
   * @param model the model
   * @param columnId the column id
   * @return the cell's property value
   */
  protected String getModelProperty(ModelData model, String columnId) {
    return modelStringProvider.getStringValue(model, columnId);
  }

}
