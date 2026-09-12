import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { initPlayingBar } from "./spotify/playing-bar.js";
import { getCsrfHeader, getCsrfToken } from "./utils/csrf.js";
import { initLyrics } from "./lyrics/lyrics.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer();
  initPlayingBar();
  initUserSavedTracks();
});

htmx.on('user-saved-tracks:init', initUserSavedTracks);

let cleanupLyricsFn;
htmx.on('lyrics:init', () => {
  cleanupLyricsFn = initLyrics();
});

htmx.on('htmx:before:swap', () => {
  cleanupLyricsFn?.();
  cleanupLyricsFn = null;
});

htmx.on('htmx:config:request', ({ detail: { ctx } }) => {
  if (ctx.request.method !== 'GET') {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    ctx.request.headers[csrfHeader] = csrfToken;
  }
});

htmx.onLoad(elt => {
  if (elt.matches('.toast')) {
    new bootstrap.Toast(elt).show();
  }
});
