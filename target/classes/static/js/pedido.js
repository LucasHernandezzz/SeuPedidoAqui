document.addEventListener('DOMContentLoaded', () => {
    const usuarioLogado = sessionStorage.getItem("usuarioId");
    const isAdmin = sessionStorage.getItem("isAdmin") === 'true';
    const usuarioId = usuarioLogado;
    console.log("ID do usuário logado:", usuarioId);
    const corpoTabela = document.getElementById('corpo-tabela');
    const mensagem = document.getElementById('mensagem');
    const nomeUsuarioElement = document.getElementById('nome-usuario');
    const tabelaPedidos = document.getElementById('tabela-pedidos');

    const url = isAdmin
        ? 'http://localhost:8080/api/pedidos/listarPedidos'
        : `http://localhost:8080/api/pedidos/listarPedidoId/${usuarioLogado}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao buscar pedidos');
            }
            return response.json();
        })
        .then(data => {
            if (data.length === 0) {
                mensagem.textContent = 'Nenhum pedido encontrado.';
            } else {
                const nomeUsuario = data[0].nomeCliente; // Verifique se isso está correto
                nomeUsuarioElement.textContent = `Pedidos de ${nomeUsuario}`;
                mensagem.textContent = `Pedidos encontrados: ${data.length}`;

                data.forEach(pedido => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${pedido.numeroPedido}</td>
                        <td>R$ ${pedido.preco.toFixed(2)}</td>
                        <td>${pedido.status}</td>
                        <td>
                            ${isAdmin ? `<button class="editar-status" data-numeroPedido="${pedido.numeroPedido}">Editar Status</button>` : ''}
                        </td>
                    `;
                    corpoTabela.appendChild(row);
                });

    if (!isAdmin) {
                        // Remove a coluna "Ações" do cabeçalho se o usuário não for administrador
                        const headerRow = tabelaPedidos.querySelector('thead tr');
                        const actionHeader = headerRow.querySelector('th:last-child');
                        if (actionHeader && actionHeader.textContent === 'Ações') {
                            headerRow.removeChild(actionHeader);
                        }
                    } else {
                        document.querySelectorAll('.editar-status').forEach(button => {
                            button.addEventListener('click', (event) => {
                                const numeroPedido = event.target.getAttribute('data-numeroPedido');
                                abrirSeletorStatus(numeroPedido);
                            });
                        });
                    }
                }
            })
            .catch(error => {
                mensagem.textContent = error.message;
            });

     function abrirSeletorStatus(numeroPedido) {
            const statusOptions = ['EM_PREPARACAO', 'EM_ENTREGA', 'ENTREGUE', 'CANCELADO'];
            const select = document.createElement('select');
            statusOptions.forEach(status => {
                const option = document.createElement('option');
                option.value = status;
                option.textContent = status;
                select.appendChild(option);
            });

            const confirmarButton = document.createElement('button');
            confirmarButton.textContent = 'Confirmar';
            confirmarButton.addEventListener('click', () => {
                const novoStatus = select.value;
                atualizarStatusPedido(numeroPedido, novoStatus);
                document.body.removeChild(select);
                document.body.removeChild(confirmarButton);
            });

            document.body.appendChild(select);
            document.body.appendChild(confirmarButton);
        }

    function atualizarStatusPedido(numeroPedido, novoStatus) {
        fetch(`http://localhost:8080/api/pedidos/atualizarPedido`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                numeroPedido: numeroPedido,
                novoStatus: novoStatus
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao atualizar status do pedido');
            }
            return response.json();
        })
        .then(data => {
            mensagem.textContent = data.mensagem;

            window.location.reload();
        })
        .catch(error => {
            mensagem.textContent = error.message;
        });
    }
});
