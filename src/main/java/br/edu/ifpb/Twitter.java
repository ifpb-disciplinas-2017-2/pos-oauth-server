package br.edu.ifpb;

import br.edu.ifpb.twitter.AuthenticatorOfTwitter;
import br.edu.ifpb.twitter.Credentials;
import br.edu.ifpb.twitter.EndpointInTwitter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.json.JsonObject;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ricardo Job
 */
@WebServlet(name = "Twitter", urlPatterns = {"/twitter"})
public class Twitter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oauth_token = request.getParameter("oauth_token");
        String oauth_verifier = request.getParameter("oauth_verifier");
        Client builder = ClientBuilder.newBuilder().build();
        Credentials c = new Credentials(oauth_token, oauth_verifier);
        String access_token = readAcessToken(c, builder);
        String token = access_token.split("&")[0].split("=")[1];
        String verifier = access_token.split("&")[1].split("=")[1];
        Credentials credentialsAuthenticated = new Credentials(token, verifier);

        request.getSession().setAttribute("token", credentialsAuthenticated);
        response.sendRedirect("http://localhost:8080/pos-oauth/faces/timeline.xhtml");

//        readTimeline(credentialsAuthenticated, builder);
//        updateTimeline(credentialsAuthenticated, builder);
    }

    private String readAcessToken(Credentials credentials, Client builder) {
        Credentials c = new Credentials(credentials.token(), credentials.verifier());
        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(c);
        EndpointInTwitter endpoint = new EndpointInTwitter("POST", "https://api.twitter.com/oauth/access_token");
        String authorization = authenticator.in(endpoint).authenticate();
        WebTarget target = builder.target("https://api.twitter.com/oauth/access_token");
        Form form = new Form("oauth_verifier", credentials.verifier());
        Response post = target.request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .post(Entity.form(form));
        return post.readEntity(String.class);
    }

    private void readTimeline(Credentials c, Client builder) {
        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(c);
        EndpointInTwitter endpoint = new EndpointInTwitter("GET", "https://api.twitter.com/1.1/statuses/user_timeline.json");
        String headerAuthorization = authenticator.in(endpoint).authenticate();

        WebTarget timelineTarget = builder.target("https://api.twitter.com/1.1/statuses/user_timeline.json");
        Response time = timelineTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .get();
        String readEntity = time.readEntity(String.class);
        System.out.println("---TIMELINE-----\n" + readEntity);
    }

    private void updateTimeline(Credentials credentials, Client builder) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "exemplo em aula com @JoeNihon :)");

        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(credentials);
        EndpointInTwitter endpoint = new EndpointInTwitter("POST", "https://api.twitter.com/1.1/statuses/update.json");
        String headerAuthorization = authenticator.in(endpoint).authenticate(map);

        Form status = new Form(new MultivaluedHashMap<>(map));
        WebTarget updateTarget = builder.target("https://api.twitter.com/1.1/statuses/update.json");
        Response update = updateTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .post(Entity.form(status));
        JsonObject ent = update.readEntity(JsonObject.class);
        System.out.println("\n---UPDATE-----\n" + ent);
    }
}
