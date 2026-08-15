let player;
let elements = {};
let rafId;

let isDragging = false;
let isPaused = true;
let positionMs = 0;
let durationMs = 0;
let lastUpdateTimestamp = 0;

export const initPlayingBar = (spotifyPlayer) => {
  player = spotifyPlayer;

  const bar = document.querySelector('[data-playing-bar]');
  if (!bar) return;

  cacheElements(bar);
  bindEvents();
  startProgressLoop();
}

export const handlePlayerStateChange = (state) => {
  if (!state) return;

  const currentTrack = state.track_window?.current_track;
  if (!currentTrack) return;

  isPaused = state.paused;
  positionMs = state.position;
  durationMs = state.duration;
  lastUpdateTimestamp = performance.now();

  const { trackAlbumCover, trackName, trackArtists, playerToggle, duration, progress } = elements;

  if (trackAlbumCover) {
    trackAlbumCover.src = currentTrack.album?.images?.[0]?.url;
    trackAlbumCover.alt = `Album cover for ${currentTrack.album?.name}`;
  }

  if (trackName) {
    trackName.textContent = currentTrack.name;
  }

  if (trackArtists) {
    trackArtists.textContent = currentTrack.artists.map((artist) => artist.name).join(', ');
  }

  if (playerToggle) {
    playerToggle.textContent = state.paused ? 'Play' : 'Pause';
  }

  if (duration) {
    duration.textContent = getTimeFormatted(durationMs);
  }

  if (progress) {
    progress.max = durationMs;
    progress.setAttribute('aria-label', `${currentTrack.name} progress bar`);
  }

  if (!isDragging) {
    updateProgressDisplay(positionMs);
  }
}

const cacheElements = (bar) => {
  elements = {
    bar,
    trackAlbumCover: bar.querySelector('[data-track-album-cover]'),
    trackName: bar.querySelector('[data-track-name]'),
    trackArtists: bar.querySelector('[data-track-artists]'),
    playerToggle: bar.querySelector('[data-player-toggle]'),
    position: bar.querySelector('[data-position]'),
    duration: bar.querySelector('[data-duration]'),
    progress: bar.querySelector('[data-progress]')
  };
}

const bindEvents = () => {
  const { playerToggle, progress } = elements;

  if (playerToggle) {
    playerToggle.addEventListener('click', () => {
      player?.togglePlay().catch((err) =>
        console.error('Spotify SDK toggle play failed', err));
    });
  }

  if (progress) {
    progress.addEventListener('pointerdown', onSeekStart);
    progress.addEventListener('input', onSeekInput);
    progress.addEventListener('change', onSeekCommit);
  }
}

const onSeekStart = () => {
  isDragging = true;
}

const onSeekInput = (e) => {
  const value = Number(e.target.value);
  if (elements.position) {
    elements.position.textContent = getTimeFormatted(value);
  }
}

const onSeekCommit = async (e) => {
  const seekMs = Number(e.target.value);

  try {
    await player?.seek(seekMs);
    positionMs = seekMs;
    lastUpdateTimestamp = performance.now();
  } catch (err) {
    console.error('Failed to seek track', err);
  } finally {
    isDragging = false;
  }
}

const startProgressLoop = () => {
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

// noinspection JSUnusedGlobalSymbols
export const stopProgressLoop = () => {
  if (rafId) {
    cancelAnimationFrame(rafId);
    rafId = null;
  }
}

const updateProgressDisplay = (ms) => {
  const { position, progress } = elements;

  if (progress) {
    progress.value = ms;
  }

  if (position) {
    position.textContent = getTimeFormatted(ms);
  }
}

const getTimeFormatted = (ms) => {
  const totalSeconds = Math.floor(ms / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;

  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
}
