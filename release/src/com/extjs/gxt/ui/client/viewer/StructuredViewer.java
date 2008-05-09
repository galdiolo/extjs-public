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

import java.util.ArrayList;
import java.util.List;

/**
 * The ColumnViewer is the abstract superclass of viewers that have columns.
 */
public abstract class StructuredViewer extends Viewer<StructuredContentProvider> {

  @Override
  public void setInput(Object input) {
    getContentProvider().inputChanged(this, this.input, input);
    getContentProvider().getElements(input, new AsyncContentCallback<Object>() {
      public void setElements(List<Object> list) {
        elements = new ArrayList<Object>(list);
        onElementsReceived(elements);
      }
    });
    this.input = input;
  }

}
