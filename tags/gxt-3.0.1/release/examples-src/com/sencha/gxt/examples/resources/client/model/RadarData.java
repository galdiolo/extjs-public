/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.examples.resources.client.model;

public class RadarData {
  private String name;
  private double data;

  public RadarData(String name, double data) {
    this.name = name;
    this.data = data;
  }

  public double getData() {
    return data;
  }

  public String getName() {
    return name;
  }

  public void setData(double data) {
    this.data = data;
  }

  public void setName(String name) {
    this.name = name;
  }
}
