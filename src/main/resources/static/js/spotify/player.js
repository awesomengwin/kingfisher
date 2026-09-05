// noinspection JSUnresolvedReference

import { post } from "../utils/http.js";
import { setError, setSuccess } from "../common/popup.js";

let player;

export const SpotifyPlayerEvent = {
  STATE_CHANGED: 'spotify-player:state-changed'
}

export const initSpotifyPlayer = () => {
  window.onSpotifyWebPlaybackSDKReady = () => {
    if (player) return;

    // noinspection JSUnresolvedVariable, JSUnusedGlobalSymbols
    player = new Spotify.Player({
      name: 'kingfisher',
      getOAuthToken: async (cb) => {
        const resp = await post('/spotify/token');
        const token = await resp.text();
        cb(token);
      },
      volume: 0.5
    });

    // noinspection JSDeprecatedSymbols, JSCheckFunctionSignatures
    player.addListener('ready', ({ device_id }) => {
      console.log('Connected with Device ID', device_id);
      document.body.dataset.deviceId = device_id;

      setSuccess('Spotify Player is ready and connected.', 5000);
    });

    // noinspection JSDeprecatedSymbols, JSCheckFunctionSignatures
    player.addListener('not_ready', ({ device_id }) => {
      console.error('Device ID is not ready for playback', device_id);

      setError('Spotify Player is not ready. Please try again.');
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
