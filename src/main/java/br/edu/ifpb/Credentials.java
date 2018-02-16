package br.edu.ifpb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
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
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/02/2018, 10:42:51
 */
public class Credentials {

    private final String consumerKey = "2URxXsnyMBfn71XTtRs8A";
    private final String consumerSecret = "CM8WbuGDFPIQGhkLhFEVQMyCK6sZFq10uXM4IzHQQ";
    private final String token;
    private final String verifier;

    public Credentials(String token, String verifier) {
        this.token = token;
        this.verifier = verifier;
    }

    public void client(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String oauth_token = request.getParameter("oauth_token");
        String oauth_verifier = request.getParameter("oauth_verifier");

        Credentials app = new Credentials(oauth_token, oauth_verifier);
        AuthenticatorOfTwitter auth = new AuthenticatorOfTwitter(app);
        EndpointInTwitter endpoint = new EndpointInTwitter("POST",
                "https://api.twitter.com/oauth/access_token");
        String authenticateHeader = auth.in(endpoint).authenticate();

        //Conclui o processo de autenticacao
        String access_token = "";
        //oauth_token=90332417-YWgBgJkfrMNizwRi5vXMih4n3ikarxj9ZO3uRVocn&oauth_token_secret=wjdFh2OTy6EXaAuoGI9B3V8V3G571Bb1jWwb39sXevgQu&user_id=90332417&screen_name=ricardojob&x_auth_expires=0
        String token = access_token.split("&")[0].split("=")[1];
        String verifier = access_token.split("&")[1].split("=")[1];

        TwitterAutenticate headerAuth = new TwitterAutenticate(token, verifier);
        Credentials credentials = new Credentials(oauth_token, oauth_verifier);
        AuthenticatorOfTwitter auth2 = new AuthenticatorOfTwitter(app);
        EndpointInTwitter endpoint2 = new EndpointInTwitter("GET",
                "https://api.twitter.com/oauth/access_token");
        String authenticateHeader2 = auth2.in(endpoint2).authenticate();
        

        String headerAuthorization = headerAuth.headerAuthorization("GET", "https://api.twitter.com/1.1/statuses/user_timeline.json");
        System.out.println("headerAuthorization = " + headerAuthorization);
        WebTarget timelineTarget = builder.target("https://api.twitter.com/1.1/statuses/user_timeline.json");
        Response time = timelineTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .get();
//        JsonArray readEntity = time.readEntity(JsonArray.class);
//        System.out.println(readEntity.getJsonObject(0));        

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

        Form status = new Form(new MultivaluedHashMap<>(map));

        WebTarget updateTarget = builder.target("https://api.twitter.com/1.1/statuses/update.json");
        Response update = updateTarget.request().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", headerAuthorization)
                .post(Entity.form(status));
    }
//    public String autenticate(String method, String twitterEndpoint) {
//        return autenticate(method, twitterEndpoint, new ArrayList<>());
//    }
//
//    public String autenticate(String method, String twitterEndpoint, Map<String, String> map) {
//        List<Pair> pairs = map.entrySet()
//                .stream()
//                .map((Map.Entry<String, String> t) -> Pair.create(t.getKey(), t.getValue()))
//                .collect(Collectors.toList());
//
//        return autenticate(method, twitterEndpoint, pairs);
//    }
//    public String autenticate(String method, String twitterEndpoint, List<Pair> params) {
//        HeaderInTwitter headerInTwitter = new HeaderInTwitter(method, twitterEndpoint);
//        String parameters = headerInTwitter.composeParameters(consumerKey, token, params);
//        String oauthSignature = headerInTwitter.composeOAuthSignature(consumerSecret, verifier, parameters);
//        String authorizationHeader = headerInTwitter.composeHeader(consumerKey, token, oauthSignature);
//        return authorizationHeader;
//    }

    public String consumerKey() {
        return consumerKey;
    }

    public String consumerSecret() {
        return consumerSecret;
    }

    public String token() {
        return token;
    }

    public String verifier() {
        return verifier;
    }
}

class AuthenticatorOfTwitter {

    private EndpointInTwitter endpoint;
    private Credentials app;

    public AuthenticatorOfTwitter(Credentials app) {
        this.app = app;
    }

    private AuthenticatorOfTwitter(Credentials app, EndpointInTwitter endpoint) {
        this.endpoint = endpoint;
        this.app = app;
    }

    public AuthenticatorOfTwitter in(EndpointInTwitter endpoint) {
        return new AuthenticatorOfTwitter(this.app, endpoint);
    }

    public String authenticate() {
        return authenticate(new ArrayList<>());
    }

    public String authenticate(Map<String, String> map) {
        List<Pair> pairs = map.entrySet()
                .stream()
                .map((Map.Entry<String, String> t) -> Pair.create(t.getKey(), t.getValue()))
                .collect(Collectors.toList());

        return authenticate(pairs);
    }

    public String authenticate(List<Pair> params) {
        HeaderInTwitter headerInTwitter = new HeaderInTwitter();
        String parameters = headerInTwitter.composeParameters(app.consumerKey(), app.token(), params);
        String signatureBase = headerInTwitter.composeSignatureBase(endpoint.method(), endpoint.url(), parameters);
        String oauthSignature = headerInTwitter.composeOAuthSignature(app.consumerSecret(), app.verifier(), signatureBase);
        String authorizationHeader = headerInTwitter.composeHeader(app.consumerKey(), app.token(), oauthSignature);
        return authorizationHeader;
    }

}

class EndpointInTwitter {

    private final String method;
    private final String url;

    protected EndpointInTwitter(String method, String twitterEndpoint) {
        this.method = method;
        this.url = twitterEndpoint;
    }

    public String method() {
        return method;
    }

    public String url() {
        return url;
    }
}

class HeaderInTwitter {

    private final SignatureInTwitter signature = new SignatureInTwitter();

    private final String oauthSignatureMethod = "HMAC-SHA1";
    private final String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
    private final String oauthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;

    public String composeHeader(String consumerKey, String token, String oauthSignature) {
        String oauthToken = ("".equals(token.trim())) ? "" : "\", oauth_token=\"" + token;

        String authorizationHeader = "OAuth "
                + "oauth_consumer_key=\"" + consumerKey
                + "\", oauth_nonce=\"" + oauthNonce
                + "\", oauth_signature=\"" + signature.encode(oauthSignature)
                + "\", oauth_signature_method=\"HMAC-SHA1"
                + "\", oauth_timestamp=\"" + oauthTimestamp
                + oauthToken
                + "\", oauth_version=\"1.0\"";
        return authorizationHeader;
    }

    public String composeParameters(String consumerKey, String token, List<Pair> params) {
        List<Pair> allParams = new ArrayList<>();
        allParams.add(Pair.create("oauth_consumer_key", consumerKey));
        allParams.add(Pair.create("oauth_nonce", oauthNonce));
        allParams.add(Pair.create("oauth_signature_method", oauthSignatureMethod));
        allParams.add(Pair.create("oauth_timestamp", oauthTimestamp));
        allParams.add(Pair.create("oauth_version", "1.0"));

        if (!"".equals(token.trim())) {
            allParams.add(Pair.create("oauth_token", token));
        }
        allParams.addAll(params);

        String parameters = allParams.stream()
                .sorted((p1, p2) -> p1.key().compareTo(p2.key()))
                .map(toPair())
                .collect(Collectors.joining("&"));
        return parameters;
    }

    public String composeOAuthSignature(String consumerSecret, String verifier, String signatureBase) {
        String oauthSignature = "";
        try {
            String oauthVerifier = ("".equals(verifier.trim())) ? "" : signature.encode(verifier);
            String composite = signature.encode(consumerSecret) + "&" + oauthVerifier;
            oauthSignature = signature.compute(signatureBase, composite);
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            // TODO throw new RuntimeExpection();
            e.printStackTrace();
        }
        return oauthSignature;
    }

    public String composeSignatureBase(String method, String url, String parameters) {
        String signatureBase = method + "&"
                + signature.encode(url) + "&"
                + signature.encode(parameters);
        return signatureBase;
    }

    private Function<Pair, String> toPair() {
        return (Pair t) -> {
            return new StringBuilder(signature.encode(t.key()))
                    .append("=")
                    .append(signature.encode(t.value().toString()))
                    .toString();
        };
    }
}

class SignatureInTwitter {

    protected String compute(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] keyBytes = keyString.getBytes();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = baseString.getBytes();
        return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
    }

    public String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
//            throw new AuthenticatorException(ignore);
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%'
                    && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
}
