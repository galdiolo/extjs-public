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

import com.extjs.gxt.ui.client.data.BaseLoadResult.ModelCollectionLoadResult;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * A <code>DataReader</code> implementation that reads JSON data using a
 * <code>ModelType</code> definition and produces a set of <code>Model</code>
 * instances.
 */
public class JsonReader implements DataReader {

  private ModelType modelType;

  public JsonReader(ModelType modelType) {
    this.modelType = modelType;
  }

  public LoadResult read(LoadConfig loadConfig, Object data) {
    JSONObject jsonRoot = (JSONObject) JSONParser.parse((String) data);
    JSONArray root = (JSONArray) jsonRoot.get(modelType.root);
    int size = root.size();
    ArrayList<Model> records = new ArrayList<Model>();
    for (int i = 0; i < size; i++) {
      JSONObject obj = (JSONObject) root.get(i);
      Model model = new BaseModel();
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
            if (field.type.equals("date")) {
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
    return new ModelCollectionLoadResult<Model>(records);
  }
}
