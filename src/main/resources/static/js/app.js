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
            <div class="item compact-item">
                <div class="item-content">
                    <div class="item-title">${b.account}</div>
                    <div class="item-subtitle">
                        Лимит: ${b.limitAmount.toLocaleString()} ₽ |
                        ${b.periodType} | ${b.startDate} → ${b.endDate}
                    </div>
                </div>
                <button class="delete-btn compact-btn" onclick="deleteBudgetById(${b.id})">🗑️</button>
            </div>
        `).join('');

        list.innerHTML = html || '<p class="empty-state">Бюджетов нет</p>';
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка загрузки', true);
        list.innerHTML = '<p class="empty-state">Ошибка</p>';
    }
}

// Удалить бюджет по ID
async function deleteBudgetById(id) {
    if (!confirm('❗ Удалить бюджет?')) return;

    try {
        const response = await fetch(`${API_URL}/budgets/id/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('budgetsList', '✅ Удалён!');
            loadBudgets();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка', true);
    }
}

// Добавить бюджет
document.getElementById('budgetForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('budgetAccount').value;
    const limitAmount = document.getElementById('budgetLimit').value;
    const periodType = document.getElementById('budgetPeriod').value;

    try {
        const response = await fetch(`${API_URL}/budgets`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                account: account,
                limitAmount: parseInt(limitAmount),
                startDate: new Date().toISOString().split('T')[0],
                periodType: periodType
            })
        });

        if (response.ok) {
            showMessage('budgetsList', `✅ "${account}" создан!`);
            loadBudgets();
            document.getElementById('budgetForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка', true);
    }
});

// ====== ПРОВЕРКА ОСТАТКА ======

document.getElementById('budgetRemainsForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('remainsAccount').value;
    const date = document.getElementById('remainsDate').value;
    const resultDiv = document.getElementById('remainsResult');

    resultDiv.innerHTML = '<div class="loading"></div>';

    try {
        const url = date
            ? `${API_URL}/budgets/remains/account/date/${account}/${date}`
            : `${API_URL}/budgets/remains/account/date/${account}/${new Date().toISOString().split('T')[0]}`;

        const response = await fetch(url);

        if (response.ok) {
            const remains = await response.json();

            if (remains < 0) {
                resultDiv.innerHTML = `❌ Превышен на ${Math.abs(remains).toLocaleString()} ₽`;
                resultDiv.className = 'result-box compact-result result-error';
            } else {
                resultDiv.innerHTML = `✅ ${remains.toLocaleString()} ₽`;
                resultDiv.className = 'result-box compact-result result-success';
            }
        } else {
            const error = await response.json();
            resultDiv.innerHTML = `❌ ${error.message}`;
            resultDiv.className = 'result-box compact-result result-error';
        }
    } catch (error) {
        resultDiv.innerHTML = '❌ Ошибка';
        resultDiv.className = 'result-box compact-result result-error';
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
            showMessage('budgetsList', `✅ "${oldAccount}" → "${newAccount}"`);
            loadBudgets();
            document.getElementById('changeAccountForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка', true);
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
            showMessage('budgetsList', `✅ Лимит "${account}" ${newLimit} ₽`);
            loadBudgets();
            document.getElementById('changeLimitForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('budgetsList', '❌ Ошибка', true);
    }
});

// ====== ТРАНЗАКЦИИ ======

// Загрузить транзакции (с опциональным фильтром)
async function loadTransactions(filters = {}) {
    const list = document.getElementById('transactionsList');
    list.innerHTML = '<div class="loading"></div>';

    let url = `${API_URL}/transactions`;

    // ИСПРАВЛЕННАЯ ЛОГИКА ФИЛЬТРА
    if (filters.budgetAccount && filters.category) {
        url = `${API_URL}/transactions/budgetAccount/${filters.budgetAccount}/category/${filters.category}`;
        if (filters.date) {
            url += `?date=${filters.date}`;
        }
    } else if (filters.budgetAccount) {
        url = `${API_URL}/transactions/account/${filters.budgetAccount}`;
    } else if (filters.category) {
        url = `${API_URL}/transactions/category/${filters.category}`;
    }

    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const transactions = await response.json();

        const html = transactions.map(t => `
            <div class="item compact-item">
                <div class="item-content">
                    <div class="item-title">${t.account}${t.category ? ` → ${t.category}` : ''}</div>
                    <div class="item-subtitle">${t.amount.toLocaleString()} ₽ | ${t.createdAt}</div>
                </div>
                <button class="delete-btn compact-btn" onclick="deleteTransaction(${t.id})">🗑️</button>
            </div>
        `).join('');

        list.innerHTML = html || '<p class="empty-state">Нет транзакций</p>';
    } catch (error) {
        console.error('Ошибка загрузки транзакций:', error);
        showMessage('transactionsList', '❌ Ошибка загрузки', true);
        list.innerHTML = '<p class="empty-state">Ошибка</p>';
    }
}

// Удалить транзакцию
async function deleteTransaction(id) {
    if (!confirm('❗ Удалить?')) return;

    try {
        const response = await fetch(`${API_URL}/transactions/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('transactionsList', '✅ Удалена!');
            loadTransactions();
            loadBudgets();
        } else {
            const error = await response.json();
            showMessage('transactionsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('transactionsList', '❌ Ошибка', true);
    }
}

// Добавить транзакцию
document.getElementById('transactionForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const account = document.getElementById('transactionAccount').value;
    const category = document.getElementById('transactionCategory').value;
    const amount = document.getElementById('transactionAmount').value;

    try {
        const response = await fetch(`${API_URL}/transactions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                account: account,
                category: category,
                amount: parseInt(amount)
            })
        });

        if (response.ok) {
            showMessage('transactionsList', `✅ ${amount} ₽`);
            loadTransactions();
            loadBudgets();
            document.getElementById('transactionForm').reset();
        } else {
            const error = await response.json();
            showMessage('transactionsList', `❌ ${error.message}`, true);
        }
    } catch (error) {
        showMessage('transactionsList', '❌ Ошибка', true);
    }
});

// ИСПРАВЛЕННАЯ ФИЛЬТРАЦИЯ
document.getElementById('filterForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const budgetAccount = document.getElementById('filterBudgetAccount').value;
    const category = document.getElementById('filterCategory').value;
    const date = document.getElementById('filterDate').value;

    // Очищаем поля если они пустые
    const filters = {};
    if (budgetAccount.trim()) filters.budgetAccount = budgetAccount.trim();
    if (category.trim()) filters.category = category.trim();
    if (date) filters.date = date;

    loadTransactions(filters);
});

// СТАБИЛЬНАЯ ЗАГРУЗКА ПРИ СТАРТЕ
window.addEventListener('DOMContentLoaded', function() {
    // Загружаем с небольшой задержкой для стабильности
    setTimeout(() => {
        loadBudgets();
        loadTransactions();
    }, 100);
});

// Проверка подключения к API
async function checkApiConnection() {
    try {
        const response = await fetch(`${API_URL}/budgets`);
        return response.ok;
    } catch (error) {
        console.error('API не доступен:', error);
        return false;
    }
}

// Периодическое обновление каждые 30 секунд (опционально)
setInterval(() => {
    if (document.visibilityState === 'visible') {
        loadBudgets();
        loadTransactions();
    }
}, 30000);