/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * A <code>DataReader</code> implementation that reads JSON data using a
 * <code>ModelType</code> definition and produces a set of
 * <code>ModelData</code> instances. Subclass may override
 * {@link #newModelInstance()} to return any model data subclass.
 */
public class JsonTreeReader<C> implements DataReader<C, List<ModelData>> {

  private ModelType modelType;

  /**
   * Creates a new JSON reader.
   * 
   * @param modelType the model type definition
   */
  public JsonTreeReader(ModelType modelType) {
    this.modelType = modelType;
  }

  public List<ModelData> read(C loadConfig, Object data) {
    JSONObject jsonRoot = (JSONObject) JSONParser.parse((String) data);
    JSONArray root = (JSONArray) jsonRoot.get(modelType.root);
    int size = root.size();
    ArrayList<ModelData> records = new ArrayList<ModelData>();
    for (int i = 0; i < size; i++) {
      JSONObject obj = (JSONObject) root.get(i);
      ModelData model = newModelInstance();
      for (int j = 0; j < modelType.getFieldCount(); j++) {
        DataField field = modelType.getField(j);
        String map = field.map != null ? field.map : field.name;
        JSONValue value = obj.get(map);

        if (value == null) continue;
        if (value.isArray() != null) {
          // nothing
        } else if (value.isBoolean() != null) {
          model.set(field.name, value.isBoolean().booleanValue());
        } else if (value.isNumber() != null) {
          model.set(field.name, value.isNumber().doubleValue());
        } else if (value.isObject() != null) {
          // nothing
        } else if (value.isString() != null) {
          String s = value.isString().stringValue();
          if (field.type != null) {
            if (field.type.equals(Date.class)) {
              DateTimeFormat format = DateTimeFormat.getFormat(field.format);
              Date d = format.parse(s);
              model.set(field.name, d);
            }
          } else {
            model.set(field.name, s);
          }

        } else if (value.isNull() != null) {
          model.set(field.name, null);
        }
      }
      records.add(model);
    }
    return records;
  }

  /**
   * Returns the new model instances. Subclasses may override to provide an
   * model data subclass.
   * 
   * @return the new model data instance
   */
  protected ModelData newModelInstance() {
    return new BaseModelData();
  }
}
