import { initSpotifyPlayer } from "./spotify/player.js";
import { initUserSavedTracks } from "./library/user-saved-tracks.js";

document.addEventListener('DOMContentLoaded', () => {
  initSpotifyPlayer();
  initUserSavedTracks();
});
