const ui = {
  success: document.querySelector('[data-popup-success]'),
  error: document.querySelector('[data-popup-error]'),
}

let successTimer;
let errorTimer;

export const setSuccess = (msg, ms) => {
  resetPopups();
  ui.success.textContent = msg;
  ui.success.classList.remove('d-none');

  if (ms) {
    clearTimeout(successTimer);
    successTimer = setTimeout(resetPopups, ms);
  }
}

export const setError = (msg, ms) => {
  resetPopups();
  ui.error.textContent = msg;
  ui.error.classList.remove('d-none');

  if (ms) {
    clearTimeout(errorTimer);
    errorTimer = setTimeout(resetPopups, ms);
  }
}

const resetPopups = () => {
  ui.success.classList.add('d-none');
  ui.error.classList.add('d-none');
}
