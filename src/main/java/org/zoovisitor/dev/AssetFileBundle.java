package org.zoovisitor.dev;


import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A bundle for serving static asset files from the classpath.
 */
public class AssetFileBundle implements Bundle {
    private final String localPath;
    private final String uriPath;

    public AssetFileBundle(String localPath, String uriPath) {
        checkArgument(localPath.startsWith("/"), "%s is not an absolute path", localPath);
        this.localPath = localPath.endsWith("/") ? localPath : (localPath + '/');
        this.uriPath = uriPath.endsWith("/") ? uriPath : (uriPath + '/');
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // nothing doing
    }

    @Override
    public void run(Environment env) {
      env.servlets()
        .addServlet(getClass().getSimpleName(), createServlet())
        .addMapping(uriPath + "*");
    }


    private AssetFileServlet createServlet() {
        return new AssetFileServlet(localPath, uriPath);
    }
}
