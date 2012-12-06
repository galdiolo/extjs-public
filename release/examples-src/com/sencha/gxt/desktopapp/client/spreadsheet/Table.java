/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.spreadsheet;

import java.util.List;

public interface Table {

  int getNextRowId();

  List<Row> getRows();

  int incrementNextRowId();

  void setNextRowId(int nextRowId);

  void setRows(List<Row> rows);
}
