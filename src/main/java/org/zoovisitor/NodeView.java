package org.zoovisitor;

import java.util.Collections;
import java.util.List;

import io.dropwizard.views.View;

public class NodeView extends View {
  private final String connection;
  private final String data;
  private final List<String> path;
  private final List<String> children;

  public NodeView(String connection, String data, List<String> path, List<String> children) {
    super("browse.ftl");
    this.connection = connection;
    this.data = data;
    this.path = path;
    this.children = children;
    Collections.sort(children);
  }

  public String getConnection() {
    return connection;
  }

  public String getData() {
    return data;
  }

  public List<String> getPath() {
    return path;
  }

  public List<String> getChildren() {
    return children;
  }
}
