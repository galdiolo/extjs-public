/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.Comparator;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.util.DefaultComparator;

/**
 * A <code>Comparator</code> for <code>Model</code> instances.
 */
public class ModelComparator implements Comparator<ModelData> {

  public final String compareProperty;

  public ModelComparator(String compareProperty) {
    this.compareProperty = compareProperty;
  }
  
  public ModelComparator() {
    this.compareProperty = "name";
  }

  public int compare(ModelData m1, ModelData m2) {
    return DefaultComparator.INSTANCE.compare(m1.get(compareProperty), m2.get(compareProperty));
  }

}
