package br.edu.ifpb.twitter.web;

import br.edu.ifpb.twitter.AuthenticatorOfTwitter;
import br.edu.ifpb.twitter.Credentials;
import br.edu.ifpb.twitter.EndpointInTwitter;
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ricardo Job
 */
@WebFilter(filterName = "TwitterFilter", urlPatterns = {"/faces/*"})
public class TwitterFilter implements Filter {

    public TwitterFilter() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Credentials c = (Credentials) req.getSession().getAttribute("token");

        if (c == null) {
            c = new Credentials("", "");
            AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(c);
            EndpointInTwitter endpoint = new EndpointInTwitter("POST", "https://api.twitter.com/oauth/request_token");
            String authorization = authenticator.in(endpoint).authenticate();
            Client newBuilder = ClientBuilder.newBuilder().build();
            WebTarget target = newBuilder.target("https://api.twitter.com/oauth/request_token");
            Response post = target.request().accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorization)
                    .post(Entity.json(""));

            String oauth_token = post.readEntity(String.class);
            res.sendRedirect("https://api.twitter.com/oauth/authenticate?" + oauth_token);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
