package com.myproject.seuPedidoAqui.services;

import com.myproject.seuPedidoAqui.models.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido criarPedido(Pedido pedido);
    Optional<Pedido> obterPedidoPorNumero(Long numeroPedido);
}