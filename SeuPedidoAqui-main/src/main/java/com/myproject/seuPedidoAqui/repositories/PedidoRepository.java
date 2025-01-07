package com.myproject.seuPedidoAqui.repositories;

import com.myproject.seuPedidoAqui.models.Pedido;
import com.myproject.seuPedidoAqui.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE p.numeroPedido = :numeroPedido")
    Optional<Pedido> findByNumeroPedido(@Param("numeroPedido") Long numeroPedido);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clientId")
    List<Pedido> findByClienteId(@Param("clientId") Long clientId);
}