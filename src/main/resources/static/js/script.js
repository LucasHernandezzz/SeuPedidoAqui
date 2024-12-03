document.getElementById('increase-qty').addEventListener('click', function () {
    let quantidade = document.getElementById('quantidade');
    let preco = parseFloat(document.getElementById('produto-preco').innerText.replace('R$', '').trim());
    let subtotal = parseFloat(document.getElementById('subtotal').innerText.replace('R$', '').trim());

    quantidade.innerText = parseInt(quantidade.innerText) + 1;
    subtotal += preco;

    document.getElementById('total').innerText = 'R$ ' + (preco * parseInt(quantidade.innerText)).toFixed(2);
    document.getElementById('subtotal').innerText = 'R$ ' + subtotal.toFixed(2);
    document.getElementById('total-final').innerText = 'R$ ' + subtotal.toFixed(2);
});

document.getElementById('decrease-qty').addEventListener('click', function () {
    let quantidade = document.getElementById('quantidade');
    let preco = parseFloat(document.getElementById('produto-preco').innerText.replace('R$', '').trim());
    let subtotal = parseFloat(document.getElementById('subtotal').innerText.replace('R$', '').trim());

    if (parseInt(quantidade.innerText) > 1) {
        quantidade.innerText = parseInt(quantidade.innerText) - 1;
        subtotal -= preco;
        document.getElementById('total').innerText = 'R$ ' + (preco * parseInt(quantidade.innerText)).toFixed(2);
        document.getElementById('subtotal').innerText = 'R$ ' + subtotal.toFixed(2);
        document.getElementById('total-final').innerText = 'R$ ' + subtotal.toFixed(2);
    }
});

document.getElementById("pedidoForm").addEventListener("submit", async function (event) {
    event.preventDefault();


    const produtoId = 1;
    const preco = parseFloat(document.getElementById('produto-preco').innerText.replace('R$', '').trim());
    const quantidade = parseInt(document.getElementById('quantidade').innerText);
    const total = preco * quantidade;
    const pagamento = document.getElementById('pagamento').value;

    const usuarioId = sessionStorage.getItem("usuarioId");

    if (!usuarioId) {
        alert("Você precisa estar logado para fazer um pedido.");
        window.location.href = "/login.html";
        return;
    }

    const pedidoData = {
        idProduto: produtoId,
        cliente: { id: usuarioId },
        preco: total,
        pagamento: pagamento
    };

    const response = await fetch("/api/pedidos/cadastro", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(pedidoData)
    });

    if (response.ok) {
        alert("Pedido realizado com sucesso!");
        window.location.href = "/index.html";
    } else {
        alert("Erro ao realizar o pedido.");
    }
});

const removeButtons = document.querySelectorAll('.remove');


removeButtons.forEach(button => {
    button.addEventListener('click', function() {

        const row = this.closest('tr');
        row.remove();
    });
});