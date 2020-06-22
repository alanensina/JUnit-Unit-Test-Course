package builders;

import aensina.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {
    }

    public static UsuarioBuilder umUsuario() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario("Usuário 1");
        return builder;
    }

    public Usuario agora() {
        return usuario;
    }
    
    public UsuarioBuilder comNome(String nome) {
    	usuario.setNome(nome);
    	return this;
    }

}
