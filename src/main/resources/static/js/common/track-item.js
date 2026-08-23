export const initTrackItem = (item) => {
  item.addEventListener('click', onLyricsState);
}

const onLyricsState = (e) => {
  const item = e.currentTarget;
  const uri = item.dataset.spotifyUri;
  const toggle = document.getElementById('lyrics-toggle');
  toggle.dataset.spotifyUri = uri;
}
