/**
 * notifications.js — Serviço de notificações do portal CEBE
 *
 * Armazena notificações no localStorage e emite o evento
 * custom "notifications-updated" sempre que o estado muda.
 *
 * Uso: window.NotificationService.<método>()
 */
window.NotificationService = (function () {
    'use strict';

    const STORAGE_KEY = 'cebe_notificacoes';

    // Notificações padrão (usadas na primeira visita)
    const DEFAULTS = [
        {
            id: 1,
            type: 'success',
            icon: 'check_circle',
            text: 'Sua matrícula foi confirmada com sucesso.',
            read: false,
            date: new Date(Date.now() - 2 * 24 * 3600 * 1000).toISOString()
        },
        {
            id: 2,
            type: 'warning',
            icon: 'warning',
            text: 'Aviso: Aula de Confeitaria amanhã na Sala 05.',
            read: false,
            date: new Date(Date.now() - 3 * 3600 * 1000).toISOString()
        },
        {
            id: 3,
            type: 'info',
            icon: 'info',
            text: 'Novo material disponível: Receitas de Massa Base.',
            read: true,
            date: new Date(Date.now() - 3 * 24 * 3600 * 1000).toISOString()
        },
        {
            id: 4,
            type: 'info',
            icon: 'payments',
            text: 'Boleto de maio gerado — vencimento em 10/05.',
            read: true,
            date: new Date(Date.now() - 5 * 24 * 3600 * 1000).toISOString()
        }
    ];

    // ── Helpers internos ────────────────────────────────────────────────────

    function load() {
        try {
            const raw = localStorage.getItem(STORAGE_KEY);
            if (!raw) {
                localStorage.setItem(STORAGE_KEY, JSON.stringify(DEFAULTS));
                return [...DEFAULTS];
            }
            return JSON.parse(raw);
        } catch {
            return [...DEFAULTS];
        }
    }

    function persist(list) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
        // Notifica todos os ouvintes na mesma aba
        document.dispatchEvent(new CustomEvent('notifications-updated'));
        // Notifica outras abas abertas
        try { localStorage.setItem('cebe_notif_sync', Date.now().toString()); } catch { /* noop */ }
    }

    // ── API pública ─────────────────────────────────────────────────────────

    return {
        /** Retorna todas as notificações ordenadas da mais recente à mais antiga */
        getAll() {
            return load().sort((a, b) => new Date(b.date) - new Date(a.date));
        },

        /** Retorna somente as não lidas */
        getUnread() {
            return this.getAll().filter(n => !n.read);
        },

        /** Contagem de não lidas */
        getUnreadCount() {
            return load().filter(n => !n.read).length;
        },

        /** Marca uma notificação como lida pelo id */
        markAsRead(id) {
            persist(load().map(n => n.id === id ? { ...n, read: true } : n));
        },

        /** Marca todas como lidas */
        markAllAsRead() {
            persist(load().map(n => ({ ...n, read: true })));
        },

        /** Remove uma notificação pelo id */
        remove(id) {
            persist(load().filter(n => n.id !== id));
        },

        /**
         * Adiciona uma nova notificação.
         * @param {{ type?, icon?, text: string }} data
         */
        add(data) {
            const notification = {
                id: Date.now(),
                type: 'info',
                icon: 'info',
                read: false,
                date: new Date().toISOString(),
                ...data
            };
            persist([notification, ...load()]);
            return notification;
        },

        /**
         * Formata uma data ISO em texto relativo (ex: "Há 2 dias", "Agora").
         * @param {string} isoDate
         */
        formatTime(isoDate) {
            const diff = Date.now() - new Date(isoDate).getTime();
            const mins = Math.floor(diff / 60000);
            if (mins < 1)  return 'Agora';
            if (mins < 60) return `Há ${mins} min`;
            const h = Math.floor(mins / 60);
            if (h < 24)    return `Há ${h}h`;
            const d = Math.floor(h / 24);
            if (d === 1)   return 'Ontem';
            if (d < 7)     return `Há ${d} dias`;
            return new Date(isoDate).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
        },

        /**
         * Ícone Material Symbols por tipo de notificação.
         */
        iconByType: {
            success: 'check_circle',
            warning: 'warning',
            error:   'error',
            info:    'info'
        }
    };
})();
