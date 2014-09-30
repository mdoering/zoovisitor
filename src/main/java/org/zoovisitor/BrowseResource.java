package org.zoovisitor;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.jersey.api.NotFoundException;
import io.dropwizard.views.View;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

@Produces(MediaType.TEXT_HTML)
@Path("/")
public class BrowseResource {

  @Context UriInfo uriInfo;

  private final ZooConfiguration cfg;
  private final ZooKeeper zk;
  private final Splitter PATH_SPLITTER = Splitter.on("/").omitEmptyStrings();

  public BrowseResource(ZooConfiguration cfg, ZooKeeper zk) {
    this.cfg = cfg;
    this.zk = zk;
  }

  @GET
  @Path("/_about")
  public View about() {
    return null;
  }

  @GET
  @Path("/browse")
  public NodeView browse(@QueryParam("path") String path) throws KeeperException, InterruptedException {
    if (Strings.isNullOrEmpty(path)) {
      path = "/";
    }
    try {
      Stat stat = new Stat();
      String data = new String(zk.getData(path, false, stat));
      List<String> children = zk.getChildren(path, false);
      return new NodeView(cfg.getConnection(), data, Lists.newArrayList(PATH_SPLITTER.split(path)), children);
    } catch (IllegalArgumentException e) {
      throw new NotFoundException("Path "+path+" not existing.");
    }
  }

}
