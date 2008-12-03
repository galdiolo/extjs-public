/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.server;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.extjs.gxt.samples.client.ExampleService;
import com.extjs.gxt.samples.client.examples.model.BeanPost;
import com.extjs.gxt.samples.client.examples.model.Photo;
import com.extjs.gxt.samples.client.examples.model.Post;
import com.extjs.gxt.samples.resources.client.model.Customer;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ExampleServiceImpl extends RemoteServiceServlet implements ExampleService {

  private List<Post> posts;
  private List<BeanPost> beanPosts;
  private List<Photo> photos;

  public List<Photo> getPhotos() {
    if (photos == null) {
      loadPhotos();
    }
    return photos;
  }

  public PagingLoadResult<BeanPost> getBeanPosts(PagingLoadConfig config) {
    if (beanPosts == null) {
      loadPosts();
    }
    if (config.getSortInfo().getSortField() != null) {
      final String sortField = config.getSortInfo().getSortField();
      if (sortField != null) {
        Collections.sort(beanPosts, config.getSortInfo().getSortDir().comparator(new Comparator() {
          public int compare(Object o1, Object o2) {
            BeanPost p1 = (BeanPost) o1;
            BeanPost p2 = (BeanPost) o2;
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

    ArrayList<BeanPost> sublist = new ArrayList<BeanPost>();
    int start = config.getOffset();
    int limit = beanPosts.size();
    if (config.getLimit() > 0) {
      limit = Math.min(start + config.getLimit(), limit);
    }
    for (int i = config.getOffset(); i < limit; i++) {
      sublist.add(beanPosts.get(i));
    }
    return new BasePagingLoadResult<BeanPost>(sublist, config.getOffset(), beanPosts.size());
  }

  public List<Customer> getCustomers() {
    List<Customer> customers = new ArrayList<Customer>();
    customers.add(new Customer("Darrell", "darrell@foo.com", 1));
    customers.add(new Customer("Maro", "maro@foo.com", 2));
    customers.add(new Customer("Alec", "alec@foo.com", 3));
    customers.add(new Customer("Lia", "lia@foo.com", 4));
    return customers;
  }

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

  private String getValue(NodeList fields, int index) {
    NodeList list = fields.item(index).getChildNodes();
    if (list.getLength() > 0) {
      return list.item(0).getNodeValue();
    } else {
      return "";
    }
  }

  private void loadPhotos() {
    photos = new ArrayList<Photo>();

    URL url = getClass().getClassLoader().getResource(
        "com/extjs/gxt/samples/public/view/images/thumbs/");
    File folder = new File(url.getFile());
    File[] pics = folder.listFiles();
    Arrays.sort(pics, new Comparator<File>() {
      public int compare(File o1, File o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });

    for (File pic : pics) {
      Photo photo = new Photo();
      photo.setName(pic.getName());
      photo.setDate(new Date(pic.lastModified()));
      photo.setSize(pic.length());
      String path = getThreadLocalRequest().getRequestURI();
      path = path.substring(0, path.length() - 7) + "view/";
      photo.setPath(path + "images/thumbs/" + pic.getName());
      photos.add(photo);
    }
  }

  private void loadPosts() {
    posts = new ArrayList<Post>();
    beanPosts = new ArrayList<BeanPost>();

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(getClass().getResourceAsStream("posts.xml"));
      doc.getDocumentElement().normalize();

      NodeList nodeList = doc.getElementsByTagName("row");

      for (int s = 0; s < nodeList.getLength(); s++) {
        Node fstNode = nodeList.item(s);
        if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
          Element fstElmnt = (Element) fstNode;
          NodeList fields = fstElmnt.getElementsByTagName("field");
          Post p = new Post();
          p.setForum(getValue(fields, 0));
          p.setDate(sf.parse(getValue(fields, 1)));
          p.setSubject(getValue(fields, 2));
          p.setUsername(getValue(fields, 4));
          posts.add(p);

          BeanPost beanPost = new BeanPost();
          beanPost.setForum(getValue(fields, 0));
          beanPost.setDate(sf.parse(getValue(fields, 1)));
          beanPost.setSubject(getValue(fields, 2));
          beanPost.setUsername(getValue(fields, 4));
          beanPosts.add(beanPost);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
