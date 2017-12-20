package br.edu.ifpb.infra;

import br.edu.ifpb.domain.Autor;
import br.edu.ifpb.domain.Endereco;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 20/12/2017, 14:11:00
 */
@ApplicationScoped
//@Named
public class Autores {

    private List<Autor> autores;// = new ArrayList<>();

    public List<Autor> todos() {
        return this.autores;
    }

    @PostConstruct
    public void init() {
        autores = new ArrayList<>();
        Endereco endereco = new Endereco("Rua sem casa", "Bairro");
        Autor autor = new Autor("Chaves", "123.123.123-12");
        autor.setEndereco(endereco);
        autores.add(autor);
        Autor autor2 = new Autor("Kiko", "111.666.123-12");
        autores.add(autor2);
    }

    public void novo(Autor autor) {
        this.autores.add(autor);
    }
}
