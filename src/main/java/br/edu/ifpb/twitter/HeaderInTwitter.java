package br.edu.ifpb.twitter;

import br.edu.ifpb.Pair;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 16/02/2018, 15:57:31
 */
public class HeaderInTwitter {

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
