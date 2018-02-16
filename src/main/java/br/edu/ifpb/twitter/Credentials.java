package br.edu.ifpb.twitter;

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

//class AuthenticatorOfTwitter {
//
//    private EndpointInTwitter endpoint;
//    private final Credentials credentials;
//    private final HeaderInTwitter header = new HeaderInTwitter();
//
//    public AuthenticatorOfTwitter(Credentials app) {
//        this(app, new EndpointInTwitter("", ""));
//    }
//
//    private AuthenticatorOfTwitter(Credentials app, EndpointInTwitter endpoint) {
//        this.endpoint = endpoint;
//        this.credentials = app;
//    }
//
//    public AuthenticatorOfTwitter in(EndpointInTwitter endpoint) {
//        return new AuthenticatorOfTwitter(this.credentials, endpoint);
//    }
//
//    public String authenticate() {
//        return authenticate(new ArrayList<>());
//    }
//
//    public String authenticate(Map<String, String> map) {
//        List<Pair> pairs = map.entrySet()
//                .stream()
//                .map((Map.Entry<String, String> t) -> Pair.create(t.getKey(), t.getValue()))
//                .collect(Collectors.toList());
//
//        return authenticate(pairs);
//    }
//
//    public String authenticate(List<Pair> params) {
//        String parameters = header.composeParameters(credentials.consumerKey(), credentials.token(), params);
//        String signatureBase = header.composeSignatureBase(endpoint.method(), endpoint.url(), parameters);
//        String oauthSignature = header.composeOAuthSignature(credentials.consumerSecret(), credentials.verifier(), signatureBase);
//        String authorizationHeader = header.composeHeader(credentials.consumerKey(), credentials.token(), oauthSignature);
//        return authorizationHeader;
//    }
//}

//class EndpointInTwitter {
//
//    private final String method;
//    private final String url;
//
//    protected EndpointInTwitter(String method, String twitterEndpoint) {
//        this.method = method;
//        this.url = twitterEndpoint;
//    }
//
//    public String method() {
//        return method;
//    }
//
//    public String url() {
//        return url;
//    }
//}

//class HeaderInTwitter {
//
//    private final SignatureInTwitter signature = new SignatureInTwitter();
//
//    private final String oauthSignatureMethod = "HMAC-SHA1";
//    private final String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
//    private final String oauthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;
//
//    public String composeHeader(String consumerKey, String token, String oauthSignature) {
//        String oauthToken = ("".equals(token.trim())) ? "" : "\", oauth_token=\"" + token;
//
//        String authorizationHeader = "OAuth "
//                + "oauth_consumer_key=\"" + consumerKey
//                + "\", oauth_nonce=\"" + oauthNonce
//                + "\", oauth_signature=\"" + signature.encode(oauthSignature)
//                + "\", oauth_signature_method=\"HMAC-SHA1"
//                + "\", oauth_timestamp=\"" + oauthTimestamp
//                + oauthToken
//                + "\", oauth_version=\"1.0\"";
//        return authorizationHeader;
//    }
//
//    public String composeParameters(String consumerKey, String token, List<Pair> params) {
//        List<Pair> allParams = new ArrayList<>();
//        allParams.add(Pair.create("oauth_consumer_key", consumerKey));
//        allParams.add(Pair.create("oauth_nonce", oauthNonce));
//        allParams.add(Pair.create("oauth_signature_method", oauthSignatureMethod));
//        allParams.add(Pair.create("oauth_timestamp", oauthTimestamp));
//        allParams.add(Pair.create("oauth_version", "1.0"));
//
//        if (!"".equals(token.trim())) {
//            allParams.add(Pair.create("oauth_token", token));
//        }
//        allParams.addAll(params);
//
//        String parameters = allParams.stream()
//                .sorted((p1, p2) -> p1.key().compareTo(p2.key()))
//                .map(toPair())
//                .collect(Collectors.joining("&"));
//        return parameters;
//    }
//
//    public String composeOAuthSignature(String consumerSecret, String verifier, String signatureBase) {
//        String oauthSignature = "";
//        try {
//            String oauthVerifier = ("".equals(verifier.trim())) ? "" : signature.encode(verifier);
//            String composite = signature.encode(consumerSecret) + "&" + oauthVerifier;
//            oauthSignature = signature.compute(signatureBase, composite);
//        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
//            // TODO throw new RuntimeExpection();
//            e.printStackTrace();
//        }
//        return oauthSignature;
//    }
//
//    public String composeSignatureBase(String method, String url, String parameters) {
//        String signatureBase = method + "&"
//                + signature.encode(url) + "&"
//                + signature.encode(parameters);
//        return signatureBase;
//    }
//
//    private Function<Pair, String> toPair() {
//        return (Pair t) -> {
//            return new StringBuilder(signature.encode(t.key()))
//                    .append("=")
//                    .append(signature.encode(t.value().toString()))
//                    .toString();
//        };
//    }
//}

//class SignatureInTwitter {
//
//    protected String compute(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
//        byte[] keyBytes = keyString.getBytes();
//        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
//        Mac mac = Mac.getInstance("HmacSHA1");
//        mac.init(secretKey);
//        byte[] text = baseString.getBytes();
//        return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
//    }
//
//    public String encode(String value) {
//        String encoded = null;
//        try {
//            encoded = URLEncoder.encode(value, "UTF-8");
//        } catch (UnsupportedEncodingException ignore) {
////            throw new AuthenticatorException(ignore);
//        }
//        StringBuilder buf = new StringBuilder(encoded.length());
//        char focus;
//        for (int i = 0; i < encoded.length(); i++) {
//            focus = encoded.charAt(i);
//            if (focus == '*') {
//                buf.append("%2A");
//            } else if (focus == '+') {
//                buf.append("%20");
//            } else if (focus == '%'
//                    && (i + 1) < encoded.length()
//                    && encoded.charAt(i + 1) == '7'
//                    && encoded.charAt(i + 2) == 'E') {
//                buf.append('~');
//                i += 2;
//            } else {
//                buf.append(focus);
//            }
//        }
//        return buf.toString();
//    }
//}
