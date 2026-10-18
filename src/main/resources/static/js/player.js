import { SpotifyPlayerEvent } from "./spotify-player.js";
import { showToast } from "./common/toast.js";
import { get, put } from "./utils/http.js";
import { getCurrentDeviceId } from "./playback.js";

const container = document.querySelector('[data-playback-state]');
const ui = {
  playbackToggle: container.querySelector('[data-playback-toggle]'),
  availableDevices: container.querySelector('[data-available-devices]'),
}

let currentDeviceId;

export const initPlayer = () => {
  document.addEventListener(SpotifyPlayerEvent.READY, initPlaybackState);
}

const initPlaybackState = async () => {
  const [ currentPlayback, availableDevices ] = await Promise.all([
    getCurrentPlayback(),
    getAvailableDevices()
  ]);

  if (!availableDevices) return;

  currentDeviceId = currentPlayback.device.id;

  const menuItems = availableDevices.devices.map(device => {
    const checked = currentDeviceId === device.id;

    const menuItem = document.createElement('button');
    menuItem.className = `menu-item ${checked ? 'selected' : ''}`;
    menuItem.type = 'button';
    menuItem.setAttribute('data-bs-value', device.id);
    menuItem.setAttribute('aria-selected', String(checked));

    menuItem.innerHTML = `
      <span class="menu-item-content">
        <span>${device.name}</span>
        <small class="menu-item-description">Computer</small>
      </span>
      <svg class="menu-item-check" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 16 16"><path d="m2 7 4 5 8-8"/></svg>
    `;

    return menuItem;
  });

  ui.availableDevices.append(...menuItems);

  // Transfer playback
  ui.playbackToggle.addEventListener('change.bs.combobox', async (e) => {
    const newDeviceId = e.value;
    if (!newDeviceId || newDeviceId === currentDeviceId) return;

    ui.playbackToggle.disabled = true;

    try {
      const params = new URLSearchParams({
        deviceId: newDeviceId
      });

      await put(`/player?${params}`);

      currentDeviceId = newDeviceId;
    } catch (err) {
      showToast(err);
    } finally {
      ui.playbackToggle.disabled = false;
    }
  });
}

const getAvailableDevices = async () => {
  try {
    const resp = await get('/player/devices');
    return await resp.json();
  } catch (err) {
    showToast(err);
  }
}

const getCurrentPlayback = async () => {
  try {
    const resp = await get('/player');
    return await resp.json();
  } catch (err) {
    showToast(err);
  }
}

export const togglePlaybackShuffle = (enabled) => {
  const params = new URLSearchParams({
    deviceId: getCurrentDeviceId(),
    enabled,
  });

  return put(`/player/shuffle?${params}`);
}

export const setRepeatMode = (mode) => {
  const params = new URLSearchParams({
    deviceId: getCurrentDeviceId(),
    mode,
  });

  return put(`/player/repeat?${params}`);
}
