package com.myproject.seuPedidoAqui.controllers;

import com.myproject.seuPedidoAqui.ResponsePedidos;
import com.myproject.seuPedidoAqui.models.Pedido;
import com.myproject.seuPedidoAqui.models.Usuario;
import com.myproject.seuPedidoAqui.repositories.PedidoRepository;
import com.myproject.seuPedidoAqui.repositories.UsuarioRepository;
import com.myproject.seuPedidoAqui.services.PedidoService;
import com.myproject.seuPedidoAqui.services.UsuarioService;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, Object>> criarPedido(@RequestBody Pedido pedido) {

        Long clientId = pedido.getCliente().getId();
        Optional<Usuario> cliente = usuarioService.buscarUsuarioPorId(clientId);

        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", "Cliente não encontrado"));
        }

        Pedido novoPedido = pedidoService.criarPedido(pedido);
        Map<String, Object> response = new HashMap<>();
        response.put("numeroPedido", novoPedido.getNumeroPedido());
        response.put("mensagem", "Seu pedido foi feito com sucesso");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/listarPedido")
    public ResponseEntity<Pedido> listarPedido(@RequestBody Pedido pedido) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findByNumeroPedido(pedido.getNumeroPedido());
        return pedidoOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/listarPedidoId/{usuarioId}")
    public ResponseEntity<ResponsePedidos> listarPedidoId(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponsePedidos(Collections.emptyList(), "Usuário não encontrado."));
        }

        String nomeUsuario = usuarioOptional.get().getNome();
        List<Pedido> pedidos = pedidoRepository.findByClienteId(usuarioId);

        String mensagem;
        if (pedidos.isEmpty()) {
            mensagem = "Nenhum pedido encontrado para o cliente " + nomeUsuario + ".";
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponsePedidos(Collections.emptyList(), mensagem));
        } else {
            mensagem = "Estes foram os pedidos encontrados para o usuário: " + nomeUsuario;
        }

        return ResponseEntity.ok(new ResponsePedidos(pedidos, mensagem));
    }
}