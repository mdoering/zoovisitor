package org.zoovisitor.dev;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import org.eclipse.jetty.http.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modified version of the classpath based AssetFileServlet that comes with dropwizard
 * to serve assets uncached from the filesystem for quick developments without the need to restart the server.
 */
public class AssetFileServlet extends HttpServlet {
    private static final long serialVersionUID = 6393345594784987908L;
    private final static Logger LOG = LoggerFactory.getLogger(AssetFileServlet.class);

    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.HTML_UTF_8;

    private final File assetRoot;
    private final String uriPath;

    private final transient MimeTypes mimeTypes;

    private Charset defaultCharset = Charsets.UTF_8;

    public AssetFileServlet(String filePath, String uriPath) {
        this.assetRoot = new File(filePath);
        LOG.info("Serving asset files from {}", assetRoot.getAbsolutePath());
        final String trimmedUri = CharMatcher.is('/').trimTrailingFrom(uriPath);
        this.uriPath = trimmedUri.length() == 0 ? "/" : trimmedUri;
        this.mimeTypes = new MimeTypes();
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public Charset getDefaultCharset() {
        return this.defaultCharset;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final File asset = findAsset(req.getRequestURI());
            if (asset == null || !asset.isFile()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final String mimeTypeOfExtension = mimeTypes.getMimeByExtension(req.getRequestURI());
            MediaType mediaType = DEFAULT_MEDIA_TYPE;

            if (mimeTypeOfExtension != null) {
                try {
                    mediaType = MediaType.parse(mimeTypeOfExtension);
                    if (defaultCharset != null && mediaType.is(MediaType.ANY_TEXT_TYPE)) {
                        mediaType = mediaType.withCharset(defaultCharset);
                    }
                }
                catch (IllegalArgumentException ignore) {}
            }

            resp.setContentType(mediaType.type() + "/" + mediaType.subtype());

            if (mediaType.charset().isPresent()) {
                resp.setCharacterEncoding(mediaType.charset().get().toString());
            }

            final ServletOutputStream output = resp.getOutputStream();
            try {
                Files.copy(asset, output);
            } finally {
                output.close();
            }
        } catch (RuntimeException ignored) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private File findAsset(String key) throws IOException {
        Preconditions.checkArgument(key.startsWith(uriPath));
        final String requestedSubPath = CharMatcher.is('/').trimFrom(key.substring(uriPath.length()));
        return new File(assetRoot, requestedSubPath);
    }

}
