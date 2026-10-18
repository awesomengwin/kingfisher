let appDeviceId;
let currentDeviceId;

export const PlaybackEvent = {
  INACTIVE: 'playback:inactive'
}

export const setDeviceId = (deviceId) => {
  appDeviceId ??= deviceId;
  currentDeviceId = deviceId;

  if (appDeviceId !== currentDeviceId) {
    document.dispatchEvent(new CustomEvent(PlaybackEvent.INACTIVE));
  }
}

export const getCurrentDeviceId = () => currentDeviceId;
