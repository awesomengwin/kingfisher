import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { initPlayingBar } from "./spotify/playing-bar.js";
import { getCsrfHeader, getCsrfToken } from "./utils/csrf.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer();
  initPlayingBar();
  initUserSavedTracks();
});

document.addEventListener('htmx:afterSwap', (evt) => {
  const elt = evt.detail.requestConfig.elt;
  if (elt.matches('[data-user-saved-tracks] .list-group-item')) return;

  initUserSavedTracks();
});

document.body.addEventListener('htmx:configRequest', (evt) => {
  if (evt.detail.verb !== 'GET') {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    evt.detail.headers[csrfHeader] = csrfToken;
  }
});
