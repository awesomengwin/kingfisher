import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { initPlayingBar } from "./spotify/playing-bar.js";
import { getCsrfHeader, getCsrfToken } from "./utils/csrf.js";
import { initLyrics } from "./lyrics/lyrics.js";
import { initUserPlaylists } from "./library/user-playlists.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer();
  initPlayingBar();
  initUserSavedTracks();
  initUserPlaylists();
});

htmx.on('user-saved-tracks:init', initUserSavedTracks);
htmx.on('user-playlists:init', initUserPlaylists);

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
