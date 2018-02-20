package br.edu.ifpb.twitter.start;

import br.edu.ifpb.TwitterAutenticate;
import br.edu.ifpb.twitter.AuthenticatorOfTwitter;
import br.edu.ifpb.twitter.Credentials;
import br.edu.ifpb.twitter.EndpointInTwitter;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/02/2018, 11:38:55
 */
public class ClientTwitter {

    public static void main(String[] args) throws IOException {
        Credentials c = new Credentials("", "");
        AuthenticatorOfTwitter authenticator = new AuthenticatorOfTwitter(c);
        EndpointInTwitter endpoint = new EndpointInTwitter("POST", "https://api.twitter.com/oauth/request_token");
        String authorization = authenticator.in(endpoint).authenticate();
        System.out.println("authorization = " + authorization);
        
        Client newBuilder = ClientBuilder.newBuilder().build();
        WebTarget target = newBuilder.target("https://api.twitter.com/oauth/request_token"); 

        Response post = target.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .post(Entity.json(""));
        
        String oauth_token = post.readEntity(String.class);
        System.out.println("readObject = " + oauth_token);

        Desktop d = Desktop.getDesktop();
        d.browse(URI.create("https://api.twitter.com/oauth/authenticate?" + oauth_token));
    }
}
