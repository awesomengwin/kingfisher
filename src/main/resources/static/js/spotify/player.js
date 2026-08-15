import { post } from "../utils/http.js";
import { getCsrfHeader, getCsrfToken } from "../utils/csrf.js";

let deviceId;
let player;

export const initSpotifyPlayer = (onPlayerStateChanged) => {
  setupSpotifyWebPlaybackSDK(onPlayerStateChanged);
  setupSpotifyDeviceIdHtmxConfigRequest();
}

const setupSpotifyWebPlaybackSDK = (onPlayerStateChanged) => {
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
    player.addListener('ready', ({device_id}) => {
      console.log('Ready with Device ID', device_id);
      deviceId = device_id;
    });

    // noinspection JSDeprecatedSymbols, JSCheckFunctionSignatures
    player.addListener('player_state_changed', (state) => {
      if (!state) return;
      onPlayerStateChanged(state);
    });

    player.connect();
  }

  const script = document.createElement('script');
  script.src = 'https://sdk.scdn.co/spotify-player.js';
  script.async = true;

  document.body.appendChild(script);
}

const setupSpotifyDeviceIdHtmxConfigRequest = () => {
  document.body.addEventListener('htmx:configRequest', (evt) => {
    if (evt.detail.verb !== 'GET') {
      const csrfToken = getCsrfToken();
      const csrfHeader = getCsrfHeader();

      evt.detail.headers[csrfHeader] = csrfToken;
    }

    if (evt.detail.elt.matches('[data-user-saved-tracks] .list-group-item')) {
      if (!deviceId) {
        console.error('Spotify Player Device ID is missing');
      }

      evt.detail.parameters.deviceId = deviceId;
    }
  });
}
