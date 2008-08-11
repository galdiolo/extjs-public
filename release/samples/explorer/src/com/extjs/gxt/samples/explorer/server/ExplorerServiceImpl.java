/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.server;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.extjs.gxt.samples.explorer.client.ExplorerService;
import com.extjs.gxt.samples.explorer.client.model.Post;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ExplorerServiceImpl extends RemoteServiceServlet implements ExplorerService {

  private List<Post> posts;

  public PagingLoadResult<Post> getPosts(final PagingLoadConfig config) {
    if (posts == null) {
      loadPosts();
    }

    if (config.getSortInfo().getSortField() != null) {
      final String sortField = config.getSortInfo().getSortField();
      if (sortField != null) {
        Collections.sort(posts, config.getSortInfo().getSortDir().comparator(new Comparator() {
          public int compare(Object o1, Object o2) {
            Post p1 = (Post) o1;
            Post p2 = (Post) o2;
            if (sortField.equals("forum")) {
              return p1.getForum().compareTo(p2.getForum());
            } else if (sortField.equals("username")) {
              return p1.getUsername().compareTo(p2.getUsername());
            } else if (sortField.equals("subject")) {
              return p1.getSubject().compareTo(p2.getSubject());
            } else if (sortField.equals("date")) {
              return p1.getDate().compareTo(p2.getDate());
            }
            return 0;
          }
        }));
      }
    }

    ArrayList<Post> sublist = new ArrayList<Post>();
    int start = config.getOffset();
    int limit = posts.size();
    if (config.getLimit() > 0) {
      limit = Math.min(start + config.getLimit(), limit);
    }
    for (int i = config.getOffset(); i < limit; i++) {
      sublist.add(posts.get(i));
    }
    return new BasePagingLoadResult(sublist, config.getOffset(), posts.size());
  }

  private String getValue(Element elem, int index) {
    Element f = (Element) elem.elements().get(index);
    return f.getStringValue();
  }

  private void loadPosts() {
    posts = new ArrayList<Post>();
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      URL url = getClass().getResource("posts.xml");
      SAXReader reader = new SAXReader();
      Document document = reader.read(url);

      Element root = document.getRootElement();

      // iterate through child elements of root
      for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
        Element elem = i.next();
        Post post = new Post();
        post.setForum(getValue(elem, 0));
        post.setDate(sf.parse(getValue(elem, 1)));
        post.setSubject(getValue(elem, 2));
        post.setUsername(getValue(elem, 4));
        posts.add(post);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
