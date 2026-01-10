// Базовый URL вашего API
const API_URL = 'http://localhost:8090/api';

// Показать сообщение
function showMessage(elementId, message, isError = false) {
    const el = document.getElementById(elementId);
    el.innerHTML = `<div class="${isError ? 'error' : 'success'}">${message}</div>`;
    setTimeout(() => el.innerHTML = '', 5000);
}

// ====== РАБОТА С БЮДЖЕТАМИ ======

// Загрузить все бюджеты
async function loadBudgets() {
    const list = document.getElementById('budgetsList');
    list.innerHTML = '<div class="loading"></div>';

    try {
        const response = await fetch(`${API_URL}/budgets`);
        const budgets = await response.json();

        const html = budgets.map(b => `
            <div class="item">
                <div class="item-content">
                    <div class="item-title">${b.account}</div>
                    <div class="item-subtitle">Лимит: ${b.limitAmount.toLocaleString()} ₽ | ${b.periodType} | ${b.startDate} → ${b.endDate}</div>
                </div>
                <button class="delete-btn" onclick="deleteBudgetById(${b.id})">🗑️ Удалить</button>
            </div>
        `).join('');

        list.innerHTML = html || '<p class="empty-state">Бюджетов пока нет. Создайте первый!</p>';
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка загрузки бюджетов', true);
        list.innerHTML = '<p class="empty-state">Ошибка загрузки</p>';
    }
}

// Удалить бюджет по ID
async function deleteBudgetById(id) {
    if (!confirm('❗ Вы уверены, что хотите удалить этот бюджет?')) return;

    try {
        const response = await fetch(`${API_URL}/budgets/id/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('budgetsList', '✅ Бюджет успешно удалён!');
            loadBudgets();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка при удалении бюджета', true);
    }
}

// Добавить бюджет
document.getElementById('budgetForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('budgetAccount').value;
    const limitAmount = document.getElementById('budgetLimit').value;

    try {
        const response = await fetch(`${API_URL}/budgets`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                account: account,
                limitAmount: parseInt(limitAmount),
                startDate: new Date().toISOString().split('T')[0],
                periodType: 'MONTHLY'
            })
        });

        if (response.ok) {
            showMessage('budgetsList', `✅ Бюджет "${account}" успешно создан!`);
            loadBudgets();
            document.getElementById('budgetForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка подключения к серверу', true);
    }
});

// ====== ПРОВЕРКА ОСТАТКА ======

document.getElementById('budgetRemainsForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('remainsAccount').value;
    const date = document.getElementById('remainsDate').value;
    const resultDiv = document.getElementById('remainsResult');

    resultDiv.innerHTML = '<div class="loading"></div>';
    resultDiv.className = 'result-box';

    try {
        const url = date
            ? `${API_URL}/budgets/remains/account/date/${account}/${date}`
            : `${API_URL}/budgets/remains/account/date/${account}/${new Date().toISOString().split('T')[0]}`;

        const response = await fetch(url);

        if (response.ok) {
            const remains = await response.json();

            if (remains < 0) {
                resultDiv.innerHTML = `❌ Бюджет "${account}" превышен на ${Math.abs(remains).toLocaleString()} ₽`;
                resultDiv.className = 'result-box result-error';
            } else {
                resultDiv.innerHTML = `✅ Остаток на счёте "${account}": ${remains.toLocaleString()} ₽`;
                resultDiv.className = 'result-box result-success';
            }
        } else {
            const error = await response.json();
            resultDiv.innerHTML = `❌ ${error.message}`;
            resultDiv.className = 'result-box result-error';
        }
    } catch (error) {
        resultDiv.innerHTML = '❌ Ошибка при проверке остатка';
        resultDiv.className = 'result-box result-error';
    }
});

// ====== ИЗМЕНЕНИЕ СЧЁТА ======

document.getElementById('changeAccountForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const oldAccount = document.getElementById('oldAccount').value;
    const newAccount = document.getElementById('newAccount').value;

    try {
        const response = await fetch(`${API_URL}/budgets/changeAccount/${oldAccount}/${newAccount}`, {
            method: 'PUT'
        });

        if (response.ok) {
            showMessage('budgetsList', `✅ Счёт переименован: "${oldAccount}" → "${newAccount}"`);
            loadBudgets();
            document.getElementById('changeAccountForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка при изменении счёта', true);
    }
});

// ====== ИЗМЕНЕНИЕ ЛИМИТА ======

document.getElementById('changeLimitForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('limitAccount').value;
    const newLimit = document.getElementById('newLimit').value;

    try {
        const response = await fetch(`${API_URL}/budgets/changeLimitAmount/${account}/${newLimit}`, {
            method: 'PUT'
        });

        if (response.ok) {
            showMessage('budgetsList', `✅ Лимит для "${account}" изменён на ${parseInt(newLimit).toLocaleString()} ₽`);
            loadBudgets();
            document.getElementById('changeLimitForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка при изменении лимита', true);
    }
});

// ====== РАБОТА С ТРАНЗАКЦИЯМИ ======

// Загрузить все транзакции
async function loadTransactions() {
    const list = document.getElementById('transactionsList');
    list.innerHTML = '<div class="loading"></div>';

    try {
        const response = await fetch(`${API_URL}/transactions`);
        const transactions = await response.json();

        const html = transactions.map(t => `
            <div class="item">
                <div class="item-content">
                    <div class="item-title">${t.account}</div>
                    <div class="item-subtitle">${t.amount.toLocaleString()} ₽ | ${t.createdAt}</div>
                </div>
                <button class="delete-btn" onclick="deleteTransaction(${t.id})">🗑️ Удалить</button>
            </div>
        `).join('');

        list.innerHTML = html || '<p class="empty-state">Транзакций пока нет</p>';
    } catch (error) {
        showMessage('transactionsList', '❌ Ошибка загрузки транзакций', true);
        list.innerHTML = '<p class="empty-state">Ошибка загрузки</p>';
    }
}

// Удалить транзакцию по ID
async function deleteTransaction(id) {
    if (!confirm('❗ Удалить эту транзакцию?')) return;

    try {
        const response = await fetch(`${API_URL}/transactions/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('transactionsList', '✅ Транзакция удалена!');
            loadTransactions();
            loadBudgets();
        } else {
            const error = await response.json();
            showMessage('transactionsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('transactionsList', '❌ Ошибка при удалении транзакции', true);
    }
}

// Добавить транзакцию
document.getElementById('transactionForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('transactionAccount').value;
    const amount = document.getElementById('transactionAmount').value;

    try {
        const response = await fetch(`${API_URL}/transactions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                account: account,
                amount: parseInt(amount)
            })
        });

        if (response.ok) {
            showMessage('transactionsList', `✅ Расход ${parseInt(amount).toLocaleString()} ₽ добавлен!`);
            loadTransactions();
            loadBudgets();
            document.getElementById('transactionForm').reset();
        } else {
            const error = await response.json();
            showMessage('transactionsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('transactionsList', '❌ Ошибка подключения к серверу', true);
    }
});

// Загрузить данные при старте
window.onload = function() {
    loadBudgets();
    loadTransactions();
};