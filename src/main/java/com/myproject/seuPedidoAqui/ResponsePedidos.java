package com.myproject.seuPedidoAqui;

import com.myproject.seuPedidoAqui.models.Pedido;

import java.util.List;

public class ResponsePedidos {
    private List<Pedido> pedidos;
    private String mensagem;

    public ResponsePedidos(List<Pedido> pedidos, String mensagem) {
        this.pedidos = pedidos;
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
