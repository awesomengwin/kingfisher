import { SpotifyPlayerEvent, togglePlay, seek, getCurrentState } from "./player.js";
import { setError } from "../common/popup.js";

const playingBar = document.querySelector('[data-playing-bar]');
const ui = {
  trackAlbumCover: playingBar.querySelector('[data-track-album-cover]'),
  trackName: playingBar.querySelector('[data-track-name]'),
  trackArtists: playingBar.querySelector('[data-track-artists]'),
  playerToggle: playingBar.querySelector('[data-player-toggle]'),
  lyricsToggle: playingBar.querySelector('[data-lyrics-toggle]'),
  position: playingBar.querySelector('[data-position]'),
  duration: playingBar.querySelector('[data-duration]'),
  progress: playingBar.querySelector('[data-progress]'),
}

let rafId;
let isDragging = false;
let isPaused = true;
let positionMs = 0;
let durationMs = 0;
let lastUpdateTimestamp = 0;

export const initPlayingBar = () => {
  document.addEventListener(SpotifyPlayerEvent.STATE_CHANGED, handlePlayerStateChange);

  // Toggle Play
  ui.playerToggle.addEventListener('click', async () => {
    try {
      await togglePlay();
    } catch (err) {
      setError(err);
    }
  });

  // Seek
  ui.progress.addEventListener('pointerdown', () => {
    isDragging = true;
  });
  ui.progress.addEventListener('input', (e) => {
    const value = Number(e.target.value);
    ui.position.textContent = getTimeFormatted(value);
  });
  ui.progress.addEventListener('change', async (e) => {
    const seekMs = Number(e.target.value);

    try {
      await seek(seekMs);
    } catch (err) {
      setError(err);
    }
    positionMs = seekMs;
    lastUpdateTimestamp = performance.now();

    isDragging = false;
  });

  // Toggle lyrics
  ui.lyricsToggle.addEventListener('click', async () => {
    try {
      const state = await getCurrentState();

      const trackId = state?.track_window?.current_track?.id;
      if (!trackId) {
        setError('Failed to obtain current track id');
        return;
      }

      await htmx.ajax('get', `/lyrics?trackId=${trackId}`, {
        target: 'main',
        select: 'main',
        swap: 'outerHTML'
      });
    } catch (err) {
      setError(err);
    }
  });

  // Loop
  startProgressLoop();
}

const handlePlayerStateChange = ({ detail: state }) => {
  const currentTrack = state?.track_window?.current_track;
  if (!currentTrack) return;

  isPaused = state.paused;
  positionMs = state.position;
  durationMs = state.duration;
  lastUpdateTimestamp = performance.now();

  ui.trackAlbumCover.src = currentTrack.album?.images?.[0]?.url;
  ui.trackAlbumCover.alt = `Album cover for ${currentTrack.album?.name}`;
  ui.trackName.textContent = currentTrack.name;
  ui.trackArtists.textContent = currentTrack.artists.map((artist) => artist.name).join(', ');

  ui.playerToggle.textContent = state.paused ? 'Play' : 'Pause';
  ui.lyricsToggle.disabled = !currentTrack.id;

  ui.duration.textContent = getTimeFormatted(durationMs);
  ui.progress.max = durationMs;
  ui.progress.setAttribute('aria-label', `${currentTrack.name} progress bar`);

  if (!isDragging) {
    updateProgressDisplay(positionMs);
  }
}

const startProgressLoop = () => {
  stopProgressLoop();

  const tick = () => {
    if (!isDragging && !isPaused && durationMs > 0) {
      const elapsed = performance.now() - lastUpdateTimestamp;
      const interpolated = Math.min(positionMs + elapsed, durationMs);
      updateProgressDisplay(interpolated);
    }
    rafId = requestAnimationFrame(tick);
  }

  rafId = requestAnimationFrame(tick);
}

const stopProgressLoop = () => {
  if (rafId) {
    cancelAnimationFrame(rafId);
    rafId = null;
  }
}

const updateProgressDisplay = (ms) => {
  ui.progress.value = ms;
  ui.position.textContent = getTimeFormatted(ms);
}

const getTimeFormatted = (ms) => {
  const totalSeconds = Math.floor(ms / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;

  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
}
