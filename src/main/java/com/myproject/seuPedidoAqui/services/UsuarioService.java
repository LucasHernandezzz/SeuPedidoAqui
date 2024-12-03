package com.myproject.seuPedidoAqui.services;

import com.myproject.seuPedidoAqui.exceptions.EmailJaCadastradoException;
import com.myproject.seuPedidoAqui.models.Usuario;
import com.myproject.seuPedidoAqui.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("E-mail já cadastrado!");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> autenticarUsuario(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            return usuario;
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

}
