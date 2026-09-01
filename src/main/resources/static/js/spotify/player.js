// noinspection JSUnresolvedReference

import { post } from "../utils/http.js";

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
      console.log('Ready with Device ID', device_id);
      document.body.dataset.deviceId = device_id;
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
  getPlayer().togglePlay().error(err => console.error('Failed to execute Spotify toggle play', err));
}

export const seek = (seekMs) => {
  getPlayer().seek(seekMs).error(err => console.error('Failed to execute Spotify toggle play', err));
}

const getPlayer = () => {
  if (!player) {
    throw new Error('Spotify player has not been initialized');
  }

  return player;
}
