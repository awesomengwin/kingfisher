const container = document.getElementById('app-toast');

export const showToast = (title = 'Kingfisher', message) => {
  const el = document.createElement('div');
  el.className = 'toast';
  el.setAttribute('role', 'alert');
  el.setAttribute('aria-live', 'assertive');
  el.setAttribute('aria-atomic', 'true');
  el.innerHTML = `
    <div class="toast-header">
        <strong class="me-auto">${title}</strong>
        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
    <div class="toast-body">
        ${message}
    </div>
  `;

  container.appendChild(el);

  const toast = new bootstrap.Toast(el);
  toast.show();

  el.addEventListener('hidden.bs.toast', () => el.remove());
}
