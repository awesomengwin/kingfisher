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

document.addEventListener('user-saved-tracks:init', initUserSavedTracks);
document.addEventListener('lyrics:init', initLyrics);

document.body.addEventListener('htmx:config:request', ({ detail: { ctx } }) => {
  if (ctx.request.method !== 'GET') {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    ctx.request.headers[csrfHeader] = csrfToken;
  }
});
