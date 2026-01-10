// Базовый URL вашего API
const API_URL = 'http://localhost:8090/api';

// Показать сообщение
function showMessage(elementId, message, isError = false) {
    const el = document.getElementById(elementId);
    el.innerHTML = `<div class="${isError ? 'error' : 'success'}">${message}</div>`;
    setTimeout(() => el.innerHTML = '', 3000);
}

// ====== РАБОТА С БЮДЖЕТАМИ ======

// Загрузить все бюджеты
async function loadBudgets() {
    try {
        const response = await fetch(`${API_URL}/budgets`);
        const budgets = await response.json();

        const html = budgets.map(b => `
            <div class="item">
                <strong>${b.category}</strong> | Лимит: ${b.limitAmount} руб. | Период: ${b.periodType}
                <br>Дата: ${b.startDate} - ${b.endDate}
                <button onclick="deleteBudgetById(${b.id})" style="background: #e74c3c; float: right;">
                    Удалить
                </button>
            </div>
        `).join('');

        document.getElementById('budgetsList').innerHTML = html || '<p>Бюджетов пока нет</p>';
    } catch (error) {
        showMessage('budgetsList', 'Ошибка загрузки бюджетов', true);
    }
}

// Удалить бюджет по ID
async function deleteBudgetById(id) {
    if (!confirm('Вы уверены, что хотите удалить этот бюджет?')) return;

    try {
        const response = await fetch(`${API_URL}/budgets/id/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('budgetsList', 'Бюджет удалён!');
            loadBudgets();
        } else {
            const error = await response.json();
            showMessage('budgetsList', error.message, true);
        }
    } catch (error) {
        showMessage('budgetsList', 'Ошибка при удалении бюджета', true);
    }
}

// Добавить бюджет
document.getElementById('budgetForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const category = document.getElementById('budgetCategory').value;
    const limitAmount = document.getElementById('budgetLimit').value;

    try {
        const response = await fetch(`${API_URL}/budgets`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                category: category,
                limitAmount: parseInt(limitAmount),
                startDate: new Date().toISOString().split('T')[0],
                periodType: 'MONTHLY'
            })
        });

        if (response.ok) {
            showMessage('budgetsList', 'Бюджет успешно создан!');
            loadBudgets();
            document.getElementById('budgetForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', error.message, true);
        }
    } catch (error) {
        showMessage('budgetsList', 'Ошибка подключения к серверу', true);
    }
});

// ====== ПРОВЕРКА ОСТАТКА БЮДЖЕТА ======

document.getElementById('budgetRemainsForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const category = document.getElementById('remainsCategory').value;
    const date = document.getElementById('remainsDate').value;

    try {
        const url = date
            ? `${API_URL}/budgets/remains/category/date/${category}/${date}`
            : `${API_URL}/budgets/remains/category/date/${category}/${new Date().toISOString().split('T')[0]}`;

        const response = await fetch(url);

        if (response.ok) {
            const remains = await response.json();
            const resultDiv = document.getElementById('remainsResult');

            if (remains < 0) {
                resultDiv.innerHTML = `❌ Бюджет превышен на ${Math.abs(remains)} руб.`;
                resultDiv.style.color = 'red';
            } else {
                resultDiv.innerHTML = `✅ Остаток: ${remains} руб.`;
                resultDiv.style.color = 'green';
            }
        } else {
            const error = await response.json();
            showMessage('remainsResult', error.message, true);
        }
    } catch (error) {
        showMessage('remainsResult', 'Ошибка при проверке остатка', true);
    }
});

// ====== ИЗМЕНЕНИЕ КАТЕГОРИИ ======

document.getElementById('changeCategoryForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const oldCategory = document.getElementById('oldCategory').value;
    const newCategory = document.getElementById('newCategory').value;

    try {
        const response = await fetch(`${API_URL}/budgets/changeCategory/${oldCategory}/${newCategory}`, {
            method: 'PUT'
        });

        if (response.ok) {
            showMessage('budgetsList', `Категория изменена с '${oldCategory}' на '${newCategory}'`);
            loadBudgets();
            document.getElementById('changeCategoryForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', error.message, true);
        }
    } catch (error) {
        showMessage('budgetsList', 'Ошибка при изменении категории', true);
    }
});

// ====== ИЗМЕНЕНИЕ ЛИМИТА ======

document.getElementById('changeLimitForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const category = document.getElementById('limitCategory').value;
    const newLimit = document.getElementById('newLimit').value;

    try {
        const response = await fetch(`${API_URL}/budgets/changeLimitAmount/${category}/${newLimit}`, {
            method: 'PUT'
        });

        if (response.ok) {
            showMessage('budgetsList', `Лимит для '${category}' изменён на ${newLimit} руб.`);
            loadBudgets();
            document.getElementById('changeLimitForm').reset();
        } else {
            const error = await response.json();
            showMessage('budgetsList', error.message, true);
        }
    } catch (error) {
        showMessage('budgetsList', 'Ошибка при изменении лимита', true);
    }
});

// ====== РАБОТА С ТРАНЗАКЦИЯМИ ======

// Загрузить все транзакции
async function loadTransactions() {
    try {
        const response = await fetch(`${API_URL}/transactions`);
        const transactions = await response.json();

        const html = transactions.map(t => `
            <div class="item">
                <strong>${t.category}</strong> | Сумма: ${t.amount} руб. | Дата: ${t.createdAt}
                <button onclick="deleteTransaction(${t.id})" style="background: #e74c3c; float: right;">
                    Удалить
                </button>
            </div>
        `).join('');

        document.getElementById('transactionsList').innerHTML = html || '<p>Транзакций пока нет</p>';
    } catch (error) {
        showMessage('transactionsList', 'Ошибка загрузки транзакций', true);
    }
}

// Удалить транзакцию по ID
async function deleteTransaction(id) {
    if (!confirm('Вы уверены, что хотите удалить эту транзакцию?')) return;

    try {
        const response = await fetch(`${API_URL}/transactions/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('transactionsList', 'Транзакция удалена!');
            loadTransactions();
            loadBudgets();
        } else {
            const error = await response.json();
            showMessage('transactionsList', error.message, true);
        }
    } catch (error) {
        showMessage('transactionsList', 'Ошибка при удалении транзакции', true);
    }
}

// Добавить транзакцию
document.getElementById('transactionForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const category = document.getElementById('transactionCategory').value;
    const amount = document.getElementById('transactionAmount').value;

    try {
        const response = await fetch(`${API_URL}/transactions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                category: category,
                amount: parseInt(amount)
            })
        });

        if (response.ok) {
            showMessage('transactionsList', 'Транзакция добавлена!');
            loadTransactions();
            loadBudgets();
            document.getElementById('transactionForm').reset();
        } else {
            const error = await response.json();
            showMessage('transactionsList', error.message, true);
        }
    } catch (error) {
        showMessage('transactionsList', 'Ошибка подключения к серверу', true);
    }
});

// Загрузить данные при старте
window.onload = function() {
    loadBudgets();
    loadTransactions();
};