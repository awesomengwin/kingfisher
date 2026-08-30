import { post } from "../utils/http.js";

export const SpotifyPlayerEvent = {
  STATE_CHANGED: 'spotify-player:state-changed'
}

export const initSpotifyPlayer = () => {
  return new Promise((resolve) => {
    window.onSpotifyWebPlaybackSDKReady = () => {
      // noinspection JSUnresolvedVariable, JSUnusedGlobalSymbols
      const player = new Spotify.Player({
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

      player.connect().then(() => resolve(player));
    }

    const script = document.createElement('script');
    script.src = 'https://sdk.scdn.co/spotify-player.js';
    script.async = true;
    document.body.appendChild(script);
  });
}
