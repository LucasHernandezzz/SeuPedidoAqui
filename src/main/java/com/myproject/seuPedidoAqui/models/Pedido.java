package com.myproject.seuPedidoAqui.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Random;

@Entity
@Table(name = "pedidos")
public class Pedido {

    public enum Status {
        EM_PREPARACAO,
        EM_ENTREGA,
        ENTREGUE,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long numeroPedido;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @Column(nullable = false)
    private Long idProduto;

    @Column(nullable = false)
    private double preco;

    private String pagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.EM_PREPARACAO;

    public Pedido() {
    }

    public Pedido(Long numeroPedido, Usuario cliente, Long idProduto, double preco, String pagamento, Status status) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.idProduto = idProduto;
        this.preco = preco;
        this.pagamento = pagamento;
        this.status = status;
    }

    @PrePersist
    private void generateNumeroPedido() {
        this.numeroPedido = new Random().nextLong(100000);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Long numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){
        return status;
    }

}
