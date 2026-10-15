import { Drawer } from "../../bootstrap/js/bootstrap.bundle.min.js";
import { getPositionMs } from "../spotify/playing-bar.js";

let rafId;
let currentActiveIdx = -1;

const ui = {
  lines: [],
}

export const initLyrics = () => {
  const root = document.querySelector('[data-lyrics]');

  if (!root) return;

  ui.lines = root.querySelectorAll('[data-lyrics-line]');

  if (ui.lines.length === 0) return;

  currentActiveIdx = -1;

  initAnnotations(root);

  // Loop
  startSyncLoop();

  return () => stopSyncLoop();
}

const initAnnotations = (root) => {
  const drawerEl = document.getElementById('annotationDrawer');
  let hoveredId;

  const highlight = (id, on) => {
    if (!id) return;

    root.querySelectorAll(`[data-annotation-id="${id}"]`)
      .forEach(el => {
        el.classList.toggle('bg-subtle-warning', on);
        el.classList.toggle('opacity-75', on);
      });
  }

  root.addEventListener('mouseover', (e) => {
    const id = e.target.closest('[data-annotation-id]')?.dataset.annotationId;
    if (id === hoveredId) return;

    highlight(hoveredId, false);
    highlight(id, true);
    hoveredId = id;
  });

  root.addEventListener('mouseleave', () => {
    highlight(hoveredId, false);
    hoveredId = undefined;
  });

  root.addEventListener('click', (e) => {
    const lineEl = e.target.closest('[data-annotation-id]');
    if (!lineEl) return;

    drawerEl.querySelector('[data-annotation-body]').innerHTML = lineEl.dataset.annotationBody;

    Drawer.getOrCreateInstance(drawerEl).show();
  });
}

const startSyncLoop = () => {
  stopSyncLoop();

  const positionMs = getPositionMs();
  let newActiveIdx = -1;

  for (let i = ui.lines.length - 1; i >= 0; i--) {
    const startTimeMs = Number(ui.lines[i].dataset.startTimeMs);
    if (positionMs >= startTimeMs) {
      newActiveIdx = i;
      break;
    }
  }

  if (newActiveIdx !== currentActiveIdx && newActiveIdx !== -1) {
    if (currentActiveIdx !== -1) {
      ui.lines[currentActiveIdx].classList.replace('opacity-100', 'opacity-25');
    }

    ui.lines[newActiveIdx].classList.replace('opacity-25', 'opacity-100');

    ui.lines[newActiveIdx].scrollIntoView({
      behavior: 'smooth',
      block: 'center'
    });

    currentActiveIdx = newActiveIdx;
  }

  rafId = requestAnimationFrame(startSyncLoop);
}

const stopSyncLoop = () => {
  if (rafId) {
    cancelAnimationFrame(rafId);
    rafId = null;
  }
}
