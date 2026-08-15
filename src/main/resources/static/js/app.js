import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { handlePlayerStateChange } from "./spotify/playing-bar.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer(handlePlayerStateChange);
  initUserSavedTracks();
});

document.addEventListener('htmx:afterSwap', () => {
  initUserSavedTracks();
});
