// noinspection JSUnresolvedReference

import { post } from "./utils/http.js";
import { showToast } from "./common/toast.js";
import { setDeviceId } from "./playback.js";

let player;

export const SpotifyPlayerEvent = {
  STATE_CHANGED: 'spotify-player:state-changed',
  READY: 'spotify-player:ready'
}

export const initSpotifyPlayer = () => {
  window.onSpotifyWebPlaybackSDKReady = () => {
    if (player) return;

    // noinspection JSUnresolvedVariable, JSUnusedGlobalSymbols
    player = new Spotify.Player({
      name: 'kingfisher',
      getOAuthToken: async (cb) => {
        const resp = await post('/token');
        const token = await resp.text();
        cb(token);
      },
    });

    // noinspection JSDeprecatedSymbols, JSCheckFunctionSignatures
    player.addListener('ready', ({ device_id }) => {
      console.log('Connected with Device ID', device_id);
      setDeviceId(device_id);

      showToast('Spotify Player is ready and connected.', 'Spotify Player');

      document.dispatchEvent(new CustomEvent(SpotifyPlayerEvent.READY));
    });

    // noinspection JSDeprecatedSymbols, JSCheckFunctionSignatures
    player.addListener('not_ready', ({ device_id }) => {
      console.error('Device ID is not ready for playback', device_id);

      showToast('Spotify Player is not ready. Please try again.', 'Spotify Player');
    });

    // noinspection JSDeprecatedSymbols, JSCheckFunctionSignatures
    player.addListener('player_state_changed', (state) => {
      document.dispatchEvent(new CustomEvent(SpotifyPlayerEvent.STATE_CHANGED, {
        detail: state
      }));
    });

    player.connect();
  }

  const script = document.createElement('script');
  script.src = 'https://sdk.scdn.co/spotify-player.js';
  script.async = true;
  document.body.appendChild(script);
}

export const togglePlay = () => {
  return player.togglePlay();
}

export const seek = (seekMs) => {
  return player.seek(seekMs);
}

export const getCurrentState = () => {
  return player.getCurrentState();
}

export const nextTrack = () => {
  return player.nextTrack();
}

export const previousTrack = () => {
  return player.previousTrack();
}
