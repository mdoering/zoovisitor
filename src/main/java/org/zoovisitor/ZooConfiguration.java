package org.zoovisitor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 */
public class ZooConfiguration extends Configuration {

  /**
   * The connection string to connect to ZooKeeper
   */
  @NotNull
  @JsonProperty
  private String connection;

  /**
   * The namespace in ZooKeeper under which all data lives
   */
  @NotNull
  @JsonProperty
  private String namespace = "dev";

  @Min(1)
  private int baseSleepTime = 1000;

  @Min(1)
  private int maxRetries = 10;

  public String getConnection() {
    return connection;
  }

  public void setConnection(String connection) {
    this.connection = connection;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public int getBaseSleepTime() {
    return baseSleepTime;
  }

  public void setBaseSleepTime(int baseSleepTime) {
    this.baseSleepTime = baseSleepTime;
  }

  public int getMaxRetries() {
    return maxRetries;
  }

  public void setMaxRetries(int maxRetries) {
    this.maxRetries = maxRetries;
  }
}