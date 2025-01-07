package com.myproject.seuPedidoAqui.controllers;

import com.myproject.seuPedidoAqui.services.SendEmail;
import com.myproject.seuPedidoAqui.exceptions.EmailJaCadastradoException;
import com.myproject.seuPedidoAqui.models.Usuario;
import com.myproject.seuPedidoAqui.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SendEmail sendEmail;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            List<String> mensagensDeErro = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(mensagensDeErro);
        }
        Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);

        try {
            sendEmail.sendEmail(usuario.getEmail(), "Cadastro criado", "Caro " + usuario.getNome() + ", Sua conta foi criada no Seu Pedido Aqui.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar email de confirmação.");
        }

        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<String> handleEmailJaCadastradoException(EmailJaCadastradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioService.autenticarUsuario(usuario.getEmail(), usuario.getSenha());
        if (usuarioOptional.isPresent()) {
            Usuario usuarioLogado = usuarioOptional.get();
            return ResponseEntity.ok(Map.of("message", "Login bem-sucedido!", "id", usuarioLogado.getId(), "role", usuarioLogado.getRole()));
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
