package br.edu.ifpb.domain;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 20/12/2017, 13:53:18
 */
public class Endereco {

    private String rua;
    private String bairro;

    public Endereco() {
    }

    public Endereco(String rua, String bairro) {
        this.rua = rua;
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

}
