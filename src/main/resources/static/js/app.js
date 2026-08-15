import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";
import { handlePlayerStateChange, initPlayingBar } from "./spotify/playing-bar.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer(handlePlayerStateChange, initPlayingBar);
  initUserSavedTracks();
});

document.addEventListener('htmx:afterSwap', () => {
  initUserSavedTracks();
});
