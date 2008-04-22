/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.extjs.gxt.ui.client.viewer;

import java.util.Arrays;
import java.util.List;

/**
 * A basic <code>IStructuredContentProvider</code> that supports arrays and
 * lists.
 */
public class DefaultContentProvider implements StructuredContentProvider {

  public void getElements(Object input, AsyncContentCallback callback) {
    if (input instanceof Object[]) {
      callback.setElements(Arrays.asList((Object[]) input));
    } else if (input instanceof List) {
      callback.setElements(((List) input));
    }
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

}
