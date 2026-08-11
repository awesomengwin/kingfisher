let deviceId;

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
  player.addListener('ready', ({device_id}) => {
    console.log('Ready with Device ID', device_id);
    deviceId = device_id;
  });

  player.connect();
}

const post = async (url, headers, body) => {
  const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken,
      ...headers
    }
  }

  if (body) {
    options.body = JSON.stringify(body);
  }

  return fetch(url, options);
}
