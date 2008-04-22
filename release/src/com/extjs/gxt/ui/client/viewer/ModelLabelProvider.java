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
 * A <code>LabelProvider</code> implementation for <code>Model</code>
 * instances. The text is retrieved from the property specified by the
 * 'textProperty'.
 */
public class ModelLabelProvider extends LabelProvider<ModelData> {

  /**
   * textProperty specifies the property name to be used to retrieve a model's
   * label. Default value is "name".
   */
  public String textProperty = "name";

  public ModelStringProvider modelStringProvider = new BaseModelStringProvider();

  /**
   * Creates a new label provider.
   */
  public ModelLabelProvider() {

  }

  /**
   * Creates a new label provider.
   * 
   * @param textPropery the property name
   */
  public ModelLabelProvider(String textPropery) {
    this.textProperty = textPropery;
  }

  public String getText(ModelData element) {
    return modelStringProvider.getStringValue((ModelData)element, textProperty);
  }

}
