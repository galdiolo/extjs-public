/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.data.BaseLoadResult.ModelCollectionLoadResult;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * A <code>DataReader</code> implementation that reads XML data using a
 * <code>ModelType</code> definition and creates <code>Model</code>
 * instances.
 */
public class XmlReader implements DataReader {

  private ModelType modelType;

  public XmlReader(ModelType modelType) {
    this.modelType = modelType;
  }

  public LoadResult read(LoadConfig loadConfig, Object data) {
    Document doc = XMLParser.parse((String) data);
    Node root = doc.getFirstChild();
    NodeList list = doc.getElementsByTagName(modelType.recordName);
    ArrayList<Model> records = new ArrayList<Model>();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      Element elem = (Element) node;
      Model model = new BaseModel();
      for (int j = 0; j < modelType.getFieldCount(); j++) {
        DataField field = modelType.getField(j);
        String map = field.map != null ? field.map : field.name;
        String v = getValue(elem, map);
        model.set(field.name, v);
      }
      records.add(model);
    }

    int totalCount = records.size();

    if (modelType.totalName != null) {
      Node totalNode = root.getAttributes().getNamedItem(modelType.totalName);
      if (totalNode != null) {
        String sTot = totalNode.getNodeValue();
        totalCount = Integer.parseInt(sTot);
      }
    }

    return new ModelCollectionLoadResult<Model>(records, totalCount);
  }

  protected String getValue(Element elem, String name) {
    if (elem.hasAttribute(name)) {
      return elem.getAttribute(name);
    } else {
      NodeList elems = elem.getElementsByTagName(name);
      if (elems != null) {
        elem = (Element) elems.item(0);
        if (elem != null && elem.getFirstChild() != null) {
          return elem.getFirstChild().getNodeValue();
        }
      }
      return "";
    }
  }

}
