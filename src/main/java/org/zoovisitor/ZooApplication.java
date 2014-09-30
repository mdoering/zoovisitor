package org.zoovisitor;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.google.common.base.Joiner;
import com.sun.jersey.api.core.ResourceConfig;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZooApplication extends Application<ZooConfiguration> {

  private final static Logger LOG = LoggerFactory.getLogger(ZooApplication.class);
  public static ZooConfiguration cfg;

  public static void main(String[] args) throws Exception {
    new ZooApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<ZooConfiguration> bootstrap) {
    bootstrap.addBundle(new ViewBundle());
    // use this for development
    //bootstrap.addBundle(new AssetFileBundle("/Users/markus/code/zoo-visitor/src/main/resources/static", "/static"));
    // use this in production
    bootstrap.addBundle(new AssetsBundle("/static", "/_static"));
  }

  @Override
  public void run(ZooConfiguration cfg, Environment env) throws IOException {
    this.cfg = cfg;
    // resources
    ZooKeeper zk = connect(cfg.getConnection(), 5000);
    env.jersey().register(new BrowseResource(cfg, zk));
    // register request filter
    env.jersey().property(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS,
      Joiner.on(";").skipNulls().join(PathToParamFilter.class, null).toString());
  }

  public ZooKeeper connect(String hosts, int sessionTimeout) throws IOException {
    final CountDownLatch connectedSignal = new CountDownLatch(1);
    ZooKeeper zk = new ZooKeeper(hosts, sessionTimeout, new Watcher() {
      @Override
      public void process(WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
          connectedSignal.countDown();
        }
      }
    });
    try {
      connectedSignal.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return zk;
  }
}