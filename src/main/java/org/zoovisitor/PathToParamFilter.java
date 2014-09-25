package org.zoovisitor;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class PathToParamFilter implements ContainerRequestFilter {
  public static final String PATH_KEY = "path";
  private static final UriBuilder builder = UriBuilder.fromPath("/browse");
  @Context
  ExtendedUriInfo uriInfo;

  @Override
  public ContainerRequest filter(ContainerRequest request) {

    if (request.getMethod().equals("GET") && !uriInfo.getPath().startsWith("_")) {
      String path = "/" + uriInfo.getPath();
      request.setUris(request.getBaseUri(), builder.replaceQueryParam(PATH_KEY, path).build());
    }
    return request;
  }
}