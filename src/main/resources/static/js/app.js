import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { handlePlayerStateChange, initPlayingBar } from "./spotify/playing-bar.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer(handlePlayerStateChange, initPlayingBar);
  initUserSavedTracks();
});

document.addEventListener('htmx:afterSwap', (evt) => {
  const elt = evt.detail.requestConfig.elt;
  if (elt.matches('[data-user-saved-tracks] .list-group-item')) return;

  initUserSavedTracks();
});
