package com.myproject.seuPedidoAqui.services;

import com.myproject.seuPedidoAqui.ResponsePedidos;
import com.myproject.seuPedidoAqui.models.Pedido;
import com.myproject.seuPedidoAqui.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> obterPedidoPorNumero(Long numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    public void cancelarPedido(Pedido pedido) {
        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoRepository.save(pedido);
    }

    public Pedido salvarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<ResponsePedidos> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(pedido -> new ResponsePedidos(pedido.getNumeroPedido(), pedido.getCliente().getNome(), pedido.getPreco(), pedido.getStatus()))
                .collect(Collectors.toList());
    }
}
