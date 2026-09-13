import {
  SpotifyPlayerEvent,
  togglePlay,
  seek,
  getCurrentState,
  togglePlaybackShuffle,
  setRepeatMode, previousTrack, nextTrack
} from "./player.js";
import { setError } from "../common/popup.js";

const playingBar = document.querySelector('[data-playing-bar]');
const ui = {
  trackAlbumCover: playingBar.querySelectorAll('[data-track-album-cover]'),
  trackName: playingBar.querySelector('[data-track-name]'),
  trackArtists: playingBar.querySelector('[data-track-artists]'),
  playerToggle: playingBar.querySelector('[data-player-toggle]'),
  lyricsToggle: playingBar.querySelector('[data-lyrics-toggle]'),
  position: playingBar.querySelector('[data-position]'),
  duration: playingBar.querySelector('[data-duration]'),
  progress: playingBar.querySelector('[data-progress]'),
  playerShuffle: playingBar.querySelector('[data-player-shuffle]'),
  playerRepeat: playingBar.querySelector('[data-player-repeat]'),
  playerPrev: playingBar.querySelector('[data-player-prev]'),
  playerNext: playingBar.querySelector('[data-player-next]'),
}

let rafId;
let isDragging = false;
let isPaused = true;
let positionMs = 0;
let durationMs = 0;
let lastUpdateTimestamp = 0;
let currentTrackId;

let shuffle;
let repeatMode;
const REPEAT_MODE_ICONS = [
  `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-repeat-off"><path d="M11.656 6H21l-4-4"/><path d="M17.898 17.898A4 4 0 0 1 17 18H3l4-4"/><path d="m2 2 20 20"/><path d="M21 13v1a4 4 0 0 1-.171 1.159"/><path d="m21 6-4 4"/><path d="M3 11v-1a4 4 0 0 1 3.102-3.898"/><path d="m7 22-4-4"/></svg>`,
  `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-repeat"><path d="m17 2 4 4-4 4"/><path d="M3 11v-1a4 4 0 0 1 4-4h14"/><path d="m7 22-4-4 4-4"/><path d="M21 13v1a4 4 0 0 1-4 4H3"/></svg>`,
  `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-repeat-1"><path d="m17 2 4 4-4 4"/><path d="M3 11v-1a4 4 0 0 1 4-4h14"/><path d="m7 22-4-4 4-4"/><path d="M21 13v1a4 4 0 0 1-4 4H3"/><path d="M11 10h1v4"/></svg>`
];

let isOnLyricsPage;

export const getPositionMs = () => {
  if (isPaused || durationMs === 0) {
    return positionMs;
  }

  const elapsed = performance.now() - lastUpdateTimestamp;
  return Math.min(positionMs + elapsed, durationMs);
};

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

  // Previous track
  ui.playerPrev.addEventListener('click', async () => {
    try {
      await previousTrack();
    } catch (err) {
      setError(err);
    }
  });

  // Next track
  ui.playerNext.addEventListener('click', async () => {
    try {
      await nextTrack();
    } catch (err) {
      setError(err);
    }
  });

  // Toggle shuffle
  ui.playerShuffle.addEventListener('click', async () => {
    try {
      await togglePlaybackShuffle(!shuffle);
    } catch (err) {
      setError(err);
    }
  });

  // Change repeat mode
  ui.playerRepeat.addEventListener('click', async () => {
    try {
      const next = (repeatMode + 1) % 3;
      const repeatMap = [ "off", "context", "track" ];

      await setRepeatMode(repeatMap[next]);
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
    if (isOnLyricsPage) {
      history.back();
      return;
    }

    const state = await getCurrentState();

    const trackId = state?.track_window?.current_track?.id;
    if (!trackId) {
      setError('Failed to obtain current track id');
      return;
    }

    loadLyricsPage(trackId);
  });

  // Loop
  startProgressLoop();

  htmx.on('htmx:after:swap', syncLyricsState);
}

const handlePlayerStateChange = ({ detail: state }) => {
  const currentTrack = state?.track_window?.current_track;
  if (!currentTrack) return;

  isPaused = state.paused;
  positionMs = state.position;
  durationMs = state.duration;
  lastUpdateTimestamp = performance.now();

  const newTrackId = currentTrack.id;
  if (currentTrackId && currentTrackId !== newTrackId) {
    const isLyricsPageActive = document.querySelector('[data-lyrics]');

    if (isLyricsPageActive) {
      loadLyricsPage(newTrackId);
    }
  }
  currentTrackId = newTrackId;

  // Player controls
  shuffle = state.shuffle;
  repeatMode = state.repeat_mode;

  ui.playerShuffle.classList.toggle('active', shuffle);
  ui.playerShuffle.setAttribute('aria-pressed', String(shuffle));

  ui.playerRepeat.innerHTML = REPEAT_MODE_ICONS[repeatMode];
  ui.playerRepeat.classList.toggle('active', !!repeatMode);
  ui.playerRepeat.setAttribute('aria-pressed', String(!!repeatMode));

  ui.trackAlbumCover.forEach(img => {
    img.src = currentTrack.album?.images?.[0]?.url;
    img.alt = `Album cover for ${currentTrack.album?.name}`;
  });
  ui.trackName.textContent = currentTrack.name;
  ui.trackArtists.textContent = currentTrack.artists.map((artist) => artist.name).join(', ');

  ui.playerToggle.innerHTML = state.paused
    ? `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-play"><path d="M5 5a2 2 0 0 1 3.008-1.728l11.997 6.998a2 2 0 0 1 .003 3.458l-12 7A2 2 0 0 1 5 19z"/></svg>`
    : `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pause"><rect x="14" y="3" width="5" height="18" rx="1"/><rect x="5" y="3" width="5" height="18" rx="1"/></svg>`;
  ui.lyricsToggle.disabled = !currentTrack.id;

  ui.duration.textContent = getTimeFormatted(durationMs);
  ui.progress.max = durationMs;
  ui.progress.setAttribute('aria-label', `${currentTrack.name} progress bar`);

  if (!isDragging) {
    updateProgressDisplay(positionMs);
  }
}

const loadLyricsPage = (trackId) => {
  const url = `/lyrics?trackId=${trackId}`;

  htmx.ajax('GET', url, {
    target: 'main',
    select: 'main',
    swap: 'outerHTML',
    push: url,
  }).catch(err => setError(err));
}

const syncLyricsState = () => {
  isOnLyricsPage = !!document.querySelector('[data-lyrics]');

  ui.lyricsToggle.classList.toggle('active', isOnLyricsPage);
  ui.lyricsToggle.setAttribute('aria-pressed', String(isOnLyricsPage));
}

const startProgressLoop = () => {
  stopProgressLoop();

  const tick = () => {
    if (!isDragging && !isPaused && durationMs > 0) {
      const positionMs = getPositionMs();
      updateProgressDisplay(positionMs);
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
