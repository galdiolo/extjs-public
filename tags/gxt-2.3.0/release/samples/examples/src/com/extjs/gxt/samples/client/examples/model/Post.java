/*
 * Sencha GXT 2.3.0 - Sencha for GWT
 * Copyright(c) 2007-2013, Sencha, Inc.
 * licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
 package com.extjs.gxt.samples.client.examples.model;

import java.io.Serializable;
import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class Post extends BaseTreeModel implements Serializable {

  protected Date dummy;

  public Post() {

  }

  public String getUsername() {
    return (String) get("username");
  }

  public void setUsername(String username) {
    set("username", username);
  }

  public String getForum() {
    return (String) get("forum");
  }

  public void setForum(String forum) {
    set("forum", forum);
  }

  public Date getDate() {
    return (Date) get("date");
  }

  public void setDate(Date date) {
    set("date", date);
  }

  public String getSubject() {
    return (String) get("subject");
  }

  public void setSubject(String subject) {
    set("subject", subject);
  }

}
