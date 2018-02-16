package br.edu.ifpb.twitter;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 16/02/2018, 15:57:12
 */
public class EndpointInTwitter {

    private final String method;
    private final String url;

    public EndpointInTwitter(String method, String twitterEndpoint) {
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
