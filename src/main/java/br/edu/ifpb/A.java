package br.edu.ifpb;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 08/02/2018, 11:38:26
 */
public class A {

    public static void main(String[] args) {

        String oAuthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
        String oAuthNonce = "pos12desdfedrfedwsderd" + oAuthTimestamp;
        String oAuthSignatureMethod = "HMAC-SHA1";
//        String oAuthTimestamp = time();
        String oAuthVersion = "1.0";

        String signatureBaseString1 = "POST";
        String signatureBaseString2 = "";

        List<Pair> urlParams = new ArrayList<>();
        urlParams.add(Pair.create("status", "oiiasfasfafa"));
        urlParams.add(Pair.create("a", "oiiasfasfafa"));

        List<Pair> allParams = new ArrayList<>();
        allParams.add(Pair.create("oauth_consumer_key", ""));
        allParams.add(Pair.create("oauth_nonce", oAuthNonce));
        allParams.add(Pair.create("oauth_signature_method", oAuthSignatureMethod));
        allParams.add(Pair.create("oauth_timestamp", oAuthTimestamp));
        allParams.add(Pair.create("oauth_token", ""));
        allParams.add(Pair.create("oauth_version", oAuthVersion));
        allParams.addAll(urlParams);

 
        String collect = allParams.stream()
                .sorted((p1, p2) -> p1.key().compareTo(p2.key()))
                .map(toPair())
                .collect(Collectors.joining("&"));

        System.out.println(collect);
//        System.out.println(signatureBaseString3.toString());
        //a=oiiasfasfafa&oauth_consumer_key=&oauth_nonce=pos12desdfedrfedwsderd1518101185&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1518101185&oauth_token=&oauth_version=1.0&status=oiiasfasfafa
    }

    private static Function<Pair, String> toPair() {
        return (Pair t) -> {
            return new StringBuilder(encode(t.key()))
                    .append("=")
                    .append(encode(t.value().toString()))
                    .toString();
        };
    }

    private static String encode(String key) {
        return key;
    }
}
