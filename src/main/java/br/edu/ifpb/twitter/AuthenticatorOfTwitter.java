package br.edu.ifpb.twitter;

import br.edu.ifpb.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 16/02/2018, 15:56:46
 */
public class AuthenticatorOfTwitter {

    private final EndpointInTwitter endpoint;
    private final Credentials credentials;
    private final HeaderInTwitter header = new HeaderInTwitter();

    public AuthenticatorOfTwitter(Credentials app) {
        this(app, new EndpointInTwitter("", ""));
    }

    private AuthenticatorOfTwitter(Credentials app, EndpointInTwitter endpoint) {
        this.endpoint = endpoint;
        this.credentials = app;
    }

    public AuthenticatorOfTwitter in(EndpointInTwitter endpoint) {
        return new AuthenticatorOfTwitter(this.credentials, endpoint);
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
        String parameters = header.composeParameters(credentials.consumerKey(), credentials.token(), params);
        String signatureBase = header.composeSignatureBase(endpoint.method(), endpoint.url(), parameters);
        String oauthSignature = header.composeOAuthSignature(credentials.consumerSecret(), credentials.verifier(), signatureBase);
        String authorizationHeader = header.composeHeader(credentials.consumerKey(), credentials.token(), oauthSignature);
        return authorizationHeader;
    }
}
