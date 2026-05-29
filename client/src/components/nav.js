/**
 * nav.js — Navegação compartilhada do portal CEBE
 * Injeta sidebar, topbar e bottom-nav em todas as páginas do portal.
 * Coloque este script ao final do <body>.
 */
(function () {
  'use strict';

  const page = location.pathname.split('/').pop() || 'dashboard.html';

  // ─── Itens de navegação ────────────────────────────────────────────────────
  const NAV_ITEMS = [
    { href: 'dashboard.html',   icon: 'dashboard',     label: 'Dashboard'    },
    { href: 'cursos.html',      icon: 'school',        label: 'Meus Cursos'  },
    { href: 'atendimento.html', icon: 'support_agent', label: 'Atendimento'  },
    { href: '#',                icon: 'notifications', label: 'Notificações' },
    { href: '#',                icon: 'payments',      label: 'Financeiro'   },
  ];

  const MOBILE_ITEMS = [
    { href: 'dashboard.html',   icon: 'home',          label: 'Início'  },
    { href: 'cursos.html',      icon: 'menu_book',     label: 'Cursos'  },
    { href: 'atendimento.html', icon: 'chat_bubble',   label: 'Suporte' },
    { href: '#',                icon: 'notifications', label: 'Avisos', badge: true },
  ];

  // ─── Helpers ───────────────────────────────────────────────────────────────
  const isActive = href => href !== '#' && href === page;
  const filled   = href => isActive(href) ? "font-variation-settings:'FILL' 1;" : '';

  function navLink(item) {
    return `<a class="nav-link${isActive(item.href) ? ' active' : ''}" href="${item.href}">
      <span class="material-symbols-outlined" style="${filled(item.href)}">${item.icon}</span>
      <span>${item.label}</span>
    </a>`;
  }

  function mobileLink(item) {
    return `<a class="bottom-nav-link${isActive(item.href) ? ' active' : ''}" href="${item.href}">
      ${item.badge ? '<span class="bottom-nav-dot" aria-hidden="true"></span>' : ''}
      <span class="material-symbols-outlined" style="${filled(item.href)}">${item.icon}</span>
      <span class="bottom-nav-label">${item.label}</span>
    </a>`;
  }

  // ─── Templates ────────────────────────────────────────────────────────────
  const SIDEBAR = `
    <aside class="sidebar" id="sidebar">
      <button class="sidebar-toggle-btn" aria-label="Fechar menu">
        <span class="material-symbols-outlined">menu_open</span>
      </button>
      <div class="sidebar-logo-area">
        <a href="dashboard.html" class="sidebar-logo-wrapper">
          <img src="/client/public/logo.png" alt="Logo CEBE" class="sidebar-logo" />
        </a>
      </div>
      <nav class="sidebar-nav">${NAV_ITEMS.map(navLink).join('')}</nav>
      <div class="sidebar-bottom">
        <button class="btn-new-ticket" id="btn-novo-chamado">
          <span class="material-symbols-outlined">add</span>
          Novo Chamado
        </button>
      </div>
    </aside>`;

  const TOPBAR = `
    <header class="top-bar">
      <div class="top-bar-inner">
        <a href="dashboard.html" class="top-bar-mobile-logo">
          <div class="top-bar-mobile-logo-img-wrapper">
            <img src="/client/public/logo.png" alt="Logo CEBE" class="top-bar-mobile-logo-img" />
          </div>
        </a>
        <div class="top-bar-right">
          <div class="top-bar-user-info">
            <span class="top-bar-username">Olá, João Silva!</span>
            <span class="top-bar-role">Aluno</span>
          </div>
          <div class="top-bar-actions">
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

  const BOTTOM_NAV = `
    <nav class="bottom-nav" aria-label="Navegação principal">
      ${MOBILE_ITEMS.map(mobileLink).join('')}
    </nav>`;

  // ─── Injeção no DOM ────────────────────────────────────────────────────────
  const layout = document.querySelector('.dashboard-layout');
  if (!layout) return;

  layout.insertAdjacentHTML('afterbegin', SIDEBAR);
  layout.querySelector('.main-wrapper')?.insertAdjacentHTML('afterbegin', TOPBAR);
  layout.insertAdjacentHTML('afterend', BOTTOM_NAV);

  // ─── Comportamentos compartilhados ────────────────────────────────────────
  document.getElementById('btn-logout')?.addEventListener('click', () => {
    window.location.href = 'login.html';
  });

  // "Novo Chamado": navega para atendimento (exceto quando já está lá)
  document.getElementById('btn-novo-chamado')?.addEventListener('click', () => {
    if (page !== 'atendimento.html') window.location.href = 'atendimento.html';
    // Na página de atendimento, o script local sobrepõe este comportamento
  });
})();
