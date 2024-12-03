document.addEventListener('DOMContentLoaded', () => {
    const usuarioLogado = sessionStorage.getItem("usuarioId");
    const usuarioId = usuarioLogado;
    console.log("ID do usuário logado:", usuarioId);
    const corpoTabela = document.getElementById('corpo-tabela');
    const mensagem = document.getElementById('mensagem');
    const nomeUsuarioElement = document.getElementById('nome-usuario');

    fetch(`http://localhost:8080/api/pedidos/listarPedidoId/${usuarioId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar pedidos');
            }
            return response.json();
        })
        .then(data => {
            if (data.pedidos.length === 0) {
                mensagem.textContent = 'Nenhum pedido encontrado.';
            } else {
                const nomeUsuario = data.mensagem.split(": ")[1]; // Verifique se isso está correto
                nomeUsuarioElement.textContent = `Pedidos de ${nomeUsuario}`;
                mensagem.textContent = `Pedidos encontrados: ${data.pedidos.length}`;
                data.pedidos.forEach(pedido => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${pedido.id}</td>
                        <td>${pedido.numeroPedido}</td>
                        <td>${pedido.idProduto}</td>
                        <td>R$ ${pedido.preco.toFixed(2)}</td>
                        <td>${pedido.pagamento}</td>
                    `;
                    corpoTabela.appendChild(row);
                });
            }
        })
        .catch(error => {
            mensagem.textContent = error.message;
        });
});
