package controller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ParameterParser;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;

public class UserController extends BasicController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Override
    public HttpResponse doGet(HttpRequest request, HttpResponse response) throws IOException {
        if(request.hasParameters()) {
            DataBase.addUser(createUser(request));
            response.responseStartLine("HTTP/1.1 200 OK");
            response.responseHeader();
        }
        return response;
    }

    @Override
    public HttpResponse doPost(HttpRequest request, HttpResponse response) throws IOException {
        log.debug("{}", request.hasBody());

        if(request.hasBody()) {
            String body = request.getBody().toString();
            body = URLDecoder.decode(body, "UTF-8");
            Map<String, String> bodyData = ParameterParser.parse(body);

            DataBase.addUser(createUser(bodyData));

            // response 만들기
            //헤더 없고 쓰는거 없고
            response.addHeader(Arrays.asList("Location: /index.html\r\n"));
            response.responseStartLine("HTTP/1.1 302 FOUND\r\n");
            response.responseHeader();
            response.responseBody(new byte[]{});
        }

        return response;
    }

    private User createUser(HttpRequest request) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        return new User(userId, password, name, email);
    }

    private User createUser(Map<String, String> bodyData) {
        String userId = bodyData.get("userId");
        String password = bodyData.get("password");
        String name = bodyData.get("name");
        String email = bodyData.get("email");

        return new User(userId, password, name, email);
    }
}