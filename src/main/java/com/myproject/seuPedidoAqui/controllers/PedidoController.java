package com.myproject.seuPedidoAqui.controllers;

import com.myproject.seuPedidoAqui.ResponsePedidos;
import com.myproject.seuPedidoAqui.services.SendEmail;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private SendEmail sendEmail;


    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, Object>> criarPedido(@RequestBody Pedido pedido) {

        Long clientId = pedido.getCliente().getId();
        Optional<Usuario> user = usuarioRepository.findByid(clientId);

        String emailUser = user.get().getEmail();

        Pedido novoPedido = pedidoService.criarPedido(pedido);
        System.out.println(emailUser);

        try {
            sendEmail.sendEmail(emailUser, "Pedido Feito", "Seu pedido "+ novoPedido.getNumeroPedido() +  " no valor de "+ novoPedido.getPreco() +"  foi confirmado e está em andamento");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("numeroPedido", novoPedido.getNumeroPedido());
        response.put("mensagem", "Seu pedido foi feito com sucesso");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/atualizarPedido")
    public ResponseEntity<Map<String, Object>> atualizarPedido(@RequestBody Map<String, Object> payload) {

        if (!payload.containsKey("numeroPedido") || !payload.containsKey("novoStatus")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", "Parâmetros 'numeroPedido' e 'novoStatus' são obrigatórios"));
        }

        Long numeroPedido;
        try {
            numeroPedido = Long.parseLong(payload.get("numeroPedido").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", "Formato inválido para 'numeroPedido'"));
        }

        Pedido.Status novoStatus;
        try {
            novoStatus = Pedido.Status.valueOf(payload.get("novoStatus").toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", "Formato inválido para 'novoStatus'"));
        }


        Optional<Pedido> pedidoOptional = pedidoService.obterPedidoPorNumero(numeroPedido);

        if (pedidoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", "Pedido não encontrado"));
        }

        Pedido pedido = pedidoOptional.get();
        pedido.setStatus(novoStatus);
        pedidoService.salvarPedido(pedido);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Status do pedido atualizado com sucesso");
        response.put("pedido", pedido);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cancelar/{numeroPedido}")
    public ResponseEntity<Map<String, Object>> cancelarPedido(@PathVariable Long numeroPedido) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findByNumeroPedido(numeroPedido);

        if (pedidoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", "Pedido não encontrado"));
        }

        Pedido pedido = pedidoOptional.get();

        if(pedido.getStatus() == Pedido.Status.EM_ENTREGA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", "Não é possível cancelar um pedido que está em entrega"));
        } else if (pedido.getStatus() == Pedido.Status.CANCELADO) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", "O status do pedido já consta como cancelado"));
        }

        pedidoService.cancelarPedido(pedido);

        try {
            sendEmail.sendEmail(pedido.getCliente().getEmail(), "Pedido Cancelado", "Seu pedido "+ pedido.getNumeroPedido() +  " foi cancelado");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(Map.of("mensagem", "Pedido cancelado com sucesso"));

    }

    @PostMapping("/listarPedido")
    public ResponseEntity<Pedido> listarPedido(@RequestBody Pedido pedido) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findByNumeroPedido(pedido.getNumeroPedido());
        return pedidoOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/listarPedidoId/{usuarioId}")
    public ResponseEntity<?> listarPedidoId(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário não encontrado.");
        }

        String nomeUsuario = usuarioOptional.get().getNome();
        List<Pedido> pedidos = pedidoRepository.findByClienteId(usuarioId);

        if (pedidos.isEmpty()) {
            String mensagem = "Nenhum pedido encontrado para o cliente " + nomeUsuario + ".";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagem);
        }

        // Mapear os pedidos para ResponsePedidos
        List<ResponsePedidos> responsePedidos = pedidos.stream()
                .map(pedido -> new ResponsePedidos(
                        pedido.getNumeroPedido(),
                        pedido.getCliente().getNome(),
                        pedido.getPreco(),
                        pedido.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responsePedidos);
    }

    @GetMapping("/listarPedidos")
    public ResponseEntity<List<ResponsePedidos>> listarPedidos() {
        List<ResponsePedidos> pedidos = pedidoService.listarTodos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
        }
    }