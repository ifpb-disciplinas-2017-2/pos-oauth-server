package br.edu.ifpb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.json.JsonArray;
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
//        System.out.println("oauth_verifier = " + oauth_verifier);

        Client builder = ClientBuilder.newBuilder().build();
        WebTarget target = builder.target("https://api.twitter.com/oauth/access_token");
        Form form = new Form("oauth_verifier", oauth_verifier);
        TwitterAutenticate twitterAutenticate = new TwitterAutenticate(oauth_token, oauth_verifier);
        String authorization = twitterAutenticate
                .headerAuthorization("POST", "https://api.twitter.com/oauth/access_token");

        Response post = target.request()
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .post(Entity.form(form));
        //Conclui o processo de autenticacao
        String access_token = post.readEntity(String.class);
        //oauth_token=90332417-YWgBgJkfrMNizwRi5vXMih4n3ikarxj9ZO3uRVocn&oauth_token_secret=wjdFh2OTy6EXaAuoGI9B3V8V3G571Bb1jWwb39sXevgQu&user_id=90332417&screen_name=ricardojob&x_auth_expires=0
        String token = access_token.split("&")[0].split("=")[1];
        String verifier = access_token.split("&")[1].split("=")[1];

        TwitterAutenticate headerAuth = new TwitterAutenticate(token, verifier);

        String headerAuthorization = headerAuth.headerAuthorization("GET", "https://api.twitter.com/1.1/statuses/user_timeline.json");
        System.out.println("headerAuthorization = " + headerAuthorization);
        WebTarget timelineTarget = builder.target("https://api.twitter.com/1.1/statuses/user_timeline.json");
        Response time = timelineTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .get();
        JsonArray readEntity = time.readEntity(JsonArray.class);
        System.out.println(readEntity.getJsonObject(0));
//        

        Map<String, String> map = new HashMap<>();
        map.put("status", "enviandoooooo");

//        List<Pair> pairs = new ArrayList<>();
//        pairs.add(Pair.create("status", "enviando :)"));
        List<Pair> pairs = map.entrySet()
                .stream()
                .map((Map.Entry<String, String> t) -> Pair.create(t.getKey(), t.getValue()))
                .collect(Collectors.toList());

        headerAuthorization = headerAuth.headerAuthorization(
                "POST",
                "https://api.twitter.com/1.1/statuses/update.json",
                pairs);

        Form forma = new Form(new MultivaluedHashMap<>(map));

        WebTarget updateTarget = builder.target("https://api.twitter.com/1.1/statuses/update.json");
        Response update = updateTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .post(Entity.form(forma));

        JsonObject ent = update.readEntity(JsonObject.class);
        System.out.println(ent);

    }
}
