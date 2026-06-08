/**
 * nav.js
 * Injeta sidebar, topbar e bottom-nav em todas as páginas.
 */
(function () {
  'use strict';

  const page = location.pathname.split('/').pop() || 'dashboard.html';

  // ─── Contagem de notificações (lê localStorage diretamente) ───────────────
  function getUnreadCount() {
    try {
      const raw = localStorage.getItem('cebe_notificacoes');
      if (!raw) return 0;
      return JSON.parse(raw).filter(n => !n.read).length;
    } catch { return 0; }
  }

  function badgeHTML(count) {
    if (!count) return '';
    return `<span class="nav-badge" aria-label="${count} não lidas">${count > 99 ? '99+' : count}</span>`;
  }

  // ─── Itens de navegação ───────────────────────────────────────────────────
  const NAV_ITEMS = [
    { href: 'dashboard.html', icon: 'dashboard', label: 'Dashboard' },
    { href: 'cursos.html', icon: 'school', label: 'Meus Cursos' },
    { href: 'atendimento.html', icon: 'support_agent', label: 'Atendimento' },
    { href: 'notificacoes.html', icon: 'notifications', label: 'Notificações', notifBadge: true },
    { href: 'financeiro.html', icon: 'payments', label: 'Financeiro' },
  ];

  const MOBILE_ITEMS = [
    { href: 'dashboard.html', icon: 'home', label: 'Início' },
    { href: 'cursos.html', icon: 'menu_book', label: 'Cursos' },
    { href: 'atendimento.html', icon: 'chat_bubble', label: 'Suporte' },
    { href: 'notificacoes.html', icon: 'notifications', label: 'Avisos', mobileBadge: true },
  ];

  // ─── Helpers ──────────────────────────────────────────────────────────────
  const isActive = href => href !== '#' && href === page;
  const filled = href => isActive(href) ? "font-variation-settings:'FILL' 1;" : '';

  function navLink(item, unread) {
    const badge = (item.notifBadge && unread) ? badgeHTML(unread) : '';
    return `<a class="nav-link${isActive(item.href) ? ' active' : ''}" href="${item.href}">
      <span class="material-symbols-outlined" style="${filled(item.href)}">${item.icon}</span>
      <span>${item.label}</span>
      ${badge}
    </a>`;
  }

  function mobileLink(item, unread) {
    const dot = (item.mobileBadge && unread)
      ? `<span class="bottom-nav-dot" aria-label="${unread} não lidas"></span>` : '';
    return `<a class="bottom-nav-link${isActive(item.href) ? ' active' : ''}" href="${item.href}">
      ${dot}
      <span class="material-symbols-outlined" style="${filled(item.href)}">${item.icon}</span>
      <span class="bottom-nav-label">${item.label}</span>
    </a>`;
  }

  // ─── Templates ────────────────────────────────────────────────────────────
  function buildSidebar(unread) {
    return `<aside class="sidebar" id="sidebar">
      <button class="sidebar-toggle-btn" aria-label="Fechar menu">
        <span class="material-symbols-outlined">menu_open</span>
      </button>
      <div class="sidebar-logo-area">
        <a href="dashboard.html" class="sidebar-logo-wrapper">
          <img src="/client/public/logo.png" alt="Logo CEBE" class="sidebar-logo" />
        </a>
      </div>
      <nav class="sidebar-nav">
        ${NAV_ITEMS.map(i => navLink(i, unread)).join('')}
      </nav>
      <div class="sidebar-bottom">
        <button class="btn-new-ticket" id="btn-novo-chamado">
          <span class="material-symbols-outlined">add</span>
          <span class="btn-new-ticket-text">Novo Chamado</span>
        </button>
      </div>
    </aside>`;
  }

  const TOPBAR = `<header class="top-bar">
      <div class="top-bar-inner">
        <a href="dashboard.html" class="top-bar-mobile-logo">
          <div class="top-bar-mobile-logo-img-wrapper">
            <img src="/client/public/logo.png" alt="Logo CEBE" class="top-bar-mobile-logo-img" />
          </div>
        </a>
        <div class="top-bar-right">
          <div class="top-bar-user-info">
            <span class="top-bar-username" id="top-bar-username">Olá, Aluno!</span>
            <span class="top-bar-role">Aluno</span>
          </div>
          <div class="top-bar-actions">
            <a class="nav-notifications-btn" href="notificacoes.html" id="topbar-notif-btn"
               aria-label="Notificações">
              <span class="material-symbols-outlined">notifications</span>
              <span class="topbar-notif-badge" id="topbar-notif-badge" style="display:none;"></span>
            </a>
            <button class="btn-logout" id="btn-logout">
              <span class="material-symbols-outlined">logout</span>
              <span class="logout-label">Sair</span>
            </button>
            <div class="top-bar-avatar" id="avatar-btn" role="button" tabindex="0" aria-label="Perfil do usuário">
              <span class="material-symbols-outlined">person</span>
            </div>
          </div>
        </div>
      </div>
    </header>`;

  function buildBottomNav(unread) {
    return `<nav class="bottom-nav" aria-label="Navegação principal">
      ${MOBILE_ITEMS.map(i => mobileLink(i, unread)).join('')}
    </nav>`;
  }

  // ─── Injeção no DOM ───────────────────────────────────────────────────────
  const layout = document.querySelector('.dashboard-layout');
  if (!layout) return;

  const unread = getUnreadCount();

  layout.insertAdjacentHTML('afterbegin', buildSidebar(unread));
  layout.querySelector('.main-wrapper')?.insertAdjacentHTML('afterbegin', TOPBAR);
  layout.insertAdjacentHTML('afterend', buildBottomNav(unread));

  // Badge da topbar
  function refreshTopbarBadge() {
    const count = getUnreadCount();
    const badge = document.getElementById('topbar-notif-badge');
    if (!badge) return;
    if (count > 0) {
      badge.textContent = count > 99 ? '99+' : count;
      badge.style.display = '';
    } else {
      badge.style.display = 'none';
    }
  }
  refreshTopbarBadge();

  // Atualiza badges quando notificações mudam (mesma aba)
  document.addEventListener('notifications-updated', () => {
    const count = getUnreadCount();
    // Sidebar badge
    document.querySelectorAll('.nav-link .nav-badge').forEach(el => {
      const link = el.closest('.nav-link');
      if (link?.href?.includes('notificacoes')) {
        if (count > 0) { el.textContent = count > 99 ? '99+' : count; el.style.display = ''; }
        else el.style.display = 'none';
      }
    });
    // Bottom nav dot
    document.querySelectorAll('.bottom-nav-dot').forEach(el => {
      el.style.display = count > 0 ? '' : 'none';
    });
    // Topbar
    refreshTopbarBadge();
  });

  // Sincroniza com outras abas abertas
  window.addEventListener('storage', e => {
    if (e.key === 'cebe_notif_sync') {
      document.dispatchEvent(new CustomEvent('notifications-updated'));
    }
  });

  // ─── Comportamentos compartilhados ────────────────────────────────────────
  document.getElementById('btn-logout')?.addEventListener('click', () => {
    window.location.href = 'login.html';
  });

  document.getElementById('btn-novo-chamado')?.addEventListener('click', () => {
    if (page !== 'atendimento.html') window.location.href = 'atendimento.html';
  });

  // Sidebar toggle behavior
  const sidebar = document.getElementById('sidebar');
  const sidebarToggleBtn = document.querySelector('.sidebar-toggle-btn');
  if (sidebar && sidebarToggleBtn) {
    sidebarToggleBtn.addEventListener('click', () => {
      sidebar.classList.toggle('collapsed');
    });
  }

  // Fetch user data for top bar
  async function loadUserInfo() {
    if (typeof window.apiFetch !== 'function') return;
    try {
      const aluno = await window.apiFetch('/alunos/me');
      if (aluno && aluno.nome) {
        const parts = aluno.nome.trim().split(' ');
        const primeiroNome = parts[0];
        const nomeCurto = parts.length > 1 ? `${primeiroNome} ${parts[parts.length - 1]}` : primeiroNome;
        const span = document.getElementById('top-bar-username');
        if (span) span.textContent = `Olá, ${nomeCurto}!`;
      }
    } catch (e) {
      console.error('Erro ao carregar usuário da topbar', e);
    }
  }
  loadUserInfo();
})();
