package br.edu.ifpb;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ricardo Job
 */
//http://localhost:8080/pos-oauth/github
@WebServlet(name = "Github", urlPatterns = {"/github"})
public class Github extends HttpServlet {
//https://github.com/login/oauth/authorize?scope=repo&client_id=6b07f2b4d2888e6b8ace

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        System.out.println("code = " + code);

        Client newBuilder = ClientBuilder.newBuilder().build();
        WebTarget target = newBuilder.target("https://github.com/login/oauth/access_token");
        Form form = new Form("client_id", "6b07f2b4d2888e6b8ace")
                .param("client_secret", "d8daf5268924776c897c7cb55a8c0425ddb3ab4a")
                .param("code", code);

        Response post = target.request().post(Entity.form(form));
        String token = post.readEntity(String.class);
        System.out.println("token = " + token);

        StringTokenizer tokenizer = new StringTokenizer(token, "&");
        String acessToken = tokenizer.nextToken().split("=")[1];
        //access_token=2981f6be16975954d197db935fe87b3431d085d3&scope=repo&token_type=bearer

        //https://api.github.com/user
        response.addHeader("Authorization", "token " + acessToken);
//        response.sendRedirect("https://api.github.com/user");
        response.sendRedirect("https://api.github.com/user?" + token);

//        request.getRequestDispatcher("https://api.github.com/user")
//                .forward(request, response);
    }

}
