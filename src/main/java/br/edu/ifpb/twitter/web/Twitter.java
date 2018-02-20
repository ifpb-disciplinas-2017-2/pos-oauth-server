package br.edu.ifpb.twitter.web;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 20/02/2018, 15:01:51
 */
public class Twitter {

    private String text;
    private String id;

    public Twitter(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

}
