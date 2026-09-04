import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { initPlayingBar } from "./spotify/playing-bar.js";
import { getCsrfHeader, getCsrfToken } from "./utils/csrf.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer();
  initPlayingBar();
  initUserSavedTracks();
});

document.addEventListener('user-saved-tracks:init', initUserSavedTracks);

htmx.on('htmx:configRequest', (evt) => {
  if (evt.detail.verb !== 'GET') {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    evt.detail.headers[csrfHeader] = csrfToken;
  }
});
