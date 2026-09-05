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

  // Loop
  startSyncLoop();
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
