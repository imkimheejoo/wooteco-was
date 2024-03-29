package webserver;

import http.request.HttpRequest;
import http.request.RequestFactory;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.resolver.RequestResolver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = RequestFactory.createHttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(httpRequest);
            DataOutputStream dos = new DataOutputStream(out);
            RequestResolver.route(httpRequest, httpResponse);

            ResponseWriter responseWriter = new ResponseWriter(httpResponse, dos);
            responseWriter.write();
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
