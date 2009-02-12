/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;

public abstract class SummaryType {

  public static final SummaryType SUM = new SummaryType() {
    @Override
    public double render(Object v, ModelData m, String field, Map<String, Object> data) {
      if (v == null) {
        v = 0d;
      }
      return ((Double) v) + ((Number) m.get(field)).doubleValue();
    }
  };

  public static final SummaryType AVG = new SummaryType() {
    @Override
    public double render(Object v, ModelData m, String field, Map<String, Object> data) {
      Integer count = (Integer) data.get(field + "count");
      if (count == null) {
        count = new Integer(0);
      }
      int i = count.intValue();
      i++;
      data.put(field + "count", i);

      Double total = (Double) data.get(field + "total");
      if (total == null) total = 0d;
      total += ((Number) m.get(field)).doubleValue();
      data.put(field + "total", total);

      return total == 0 ? 0 : total / i;
    }
  };

  public static final SummaryType COUNT = new SummaryType() {
    @Override
    public double render(Object v, ModelData m, String field, Map<String, Object> data) {
      Integer count = (Integer) data.get(field + "count");
      if (count == null) {
        count = new Integer(0);
      }
      int i = count.intValue();
      i++;
      data.put(field + "count", i);
      return i;
    }
  };

  public abstract double render(Object v, ModelData m, String field, Map<String, Object> data);

}
