package com.myproject.seuPedidoAqui;

import com.myproject.seuPedidoAqui.models.Pedido;

import java.util.List;

public class ResponsePedidos {

    private Long numeroPedido;
    private String nomeCliente;
    private Double preco;
    private Pedido.Status status;

    public ResponsePedidos(Long numeroPedido, String nomeCliente, Double preco, Pedido.Status status) {
        this.numeroPedido = numeroPedido;
        this.nomeCliente = nomeCliente;
        this.preco = preco;
        this.status = status;
    }

    public ResponsePedidos(List<Object> objects, String nomeCliente) {
    }

    // Getters
    public Long getNumeroPedido() {
        return numeroPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Double getPreco() {
        return preco;
    }

    public Pedido.Status getStatus() {
        return status;
    }
}

