export const handlePlayerStateChange = (state) => {
  const currentTrack = state.track_window.current_track;

  const bar = document.querySelector('[data-playing-bar]');

  if (!bar) return;

  const trackAlbumCover = bar.querySelector('[data-track-album-cover]');
  const trackName = bar.querySelector('[data-track-name]');
  const trackArtists = bar.querySelector('[data-track-artists]');
  const playerToggle = bar.querySelector('[data-player-toggle]');
  const position = bar.querySelector('[data-position]');
  const progress = bar.querySelector('[data-progress]');
  const progressBar = bar.querySelector('[data-progress-bar]');
  const duration = bar.querySelector('[data-duration]');

  if (trackAlbumCover) {
    trackAlbumCover.src = currentTrack.album.images[0].url;
    trackAlbumCover.alt = `Album cover for ${currentTrack.album.name}`;
  }

  if (trackName) {
    trackName.textContent = currentTrack.name;
  }

  if (trackArtists) {
    trackArtists.textContent = currentTrack.artists.map(artist => artist.name).join(', ');
  }

  if (playerToggle) {
    playerToggle.textContent = state.paused ? 'Play' : 'Pause';
  }

  if (position) {
    position.textContent = getTimeFormatted(state.position);
  }

  if (duration) {
    duration.textContent = getTimeFormatted(state.duration);
  }

  const positionPercentage = state.duration ? (state.position / state.duration) * 100 : 0;

  if (progress && progressBar) {
    progress.setAttribute('aria-valuenow', `${positionPercentage}`);
    progress.setAttribute('aria-label', `${currentTrack.name} progress`);
    progressBar.style.width = `${positionPercentage}%`;
  }
}

const getTimeFormatted = (ms) => {
  const totalSeconds = Math.floor(ms / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;

  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
}
