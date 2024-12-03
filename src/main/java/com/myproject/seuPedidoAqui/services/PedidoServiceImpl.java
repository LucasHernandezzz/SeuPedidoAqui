package com.myproject.seuPedidoAqui.services;

import com.myproject.seuPedidoAqui.models.Pedido;
import com.myproject.seuPedidoAqui.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public Pedido criarPedido(Pedido pedido) {
        // Lógica adicional aqui (ex.: validações, cálculos, etc.)
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> obterPedidoPorNumero(Long numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

}
