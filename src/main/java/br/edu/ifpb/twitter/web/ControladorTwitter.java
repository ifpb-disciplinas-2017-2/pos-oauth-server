package br.edu.ifpb.twitter.web;

import br.edu.ifpb.twitter.AuthenticatorOfTwitter;
import br.edu.ifpb.twitter.Credentials;
import br.edu.ifpb.twitter.EndpointInTwitter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 20/02/2018, 14:15:17
 */
@Named
@RequestScoped
public class ControladorTwitter {

    private String tuite;
    private Client builder = ClientBuilder.newClient();

    public List<Twitter> todos() {
        return readTimeline(getCredentials());
    }

    public String tuitar() {
        Credentials credentials = getCredentials();
        tuitar(credentials);
        return null;
    }

    public String excluir(Twitter t) {
        Credentials credentials = getCredentials();
        excluir(credentials, t);
        return null;
    }

    private void excluir(Credentials credentials, Twitter t) {
        Map<String, String> map = new HashMap<>();
        map.put("id", t.getId());
        //POST https://api.twitter.com/1.1/statuses/destroy/240854986559455234.json
        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(credentials);
        EndpointInTwitter endpoint = new EndpointInTwitter("POST", "https://api.twitter.com/1.1/statuses/destroy/" + t.getId() + ".json");
        String headerAuthorization = authenticator.in(endpoint).authenticate(map);

        Form status = new Form(new MultivaluedHashMap<>(map));
        WebTarget updateTarget = builder.target("https://api.twitter.com/1.1/statuses/destroy/" + t.getId() + ".json");
        Response update = updateTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .post(Entity.form(status));
        System.out.println(update.readEntity(String.class));
    }

    private void tuitar(Credentials c) {
        Map<String, String> map = new HashMap<>();
        map.put("status", tuite);

        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(c);
        EndpointInTwitter endpoint = new EndpointInTwitter("POST", "https://api.twitter.com/1.1/statuses/update.json");
        String headerAuthorization = authenticator.in(endpoint).authenticate(map);

        Form status = new Form(new MultivaluedHashMap<>(map));
        WebTarget updateTarget = builder.target("https://api.twitter.com/1.1/statuses/update.json");
        Response update = updateTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .post(Entity.form(status));
        this.tuite = "";
    }

    private List<Twitter> readTimeline(Credentials c) {

        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(c);
        EndpointInTwitter endpoint = new EndpointInTwitter("GET", "https://api.twitter.com/1.1/statuses/user_timeline.json");
        String headerAuthorization = authenticator.in(endpoint).authenticate();

        WebTarget timelineTarget = builder.target("https://api.twitter.com/1.1/statuses/user_timeline.json");
        Response time = timelineTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .get();
        JsonArray readEntity = time.readEntity(JsonArray.class);
        return readEntity.getValuesAs(JsonObject.class)
                .stream()
                .map((JsonObject t) -> new Twitter(t.getString("text"), t.getString("id_str")))
                .collect(Collectors.toList());
    }

    private Credentials getCredentials() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (Credentials) session.getAttribute("token");
    }

    public String getTuite() {
        return tuite;
    }

    public void setTuite(String tuite) {
        this.tuite = tuite;
    }

}
