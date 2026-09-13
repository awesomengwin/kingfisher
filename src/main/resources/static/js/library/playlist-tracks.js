import { initPagination } from "../common/pagination.js";

export const initPlaylistTracks = () => {
  const root = document.querySelector('[data-playlist-tracks]');

  if (!root) return;

  setupPagination(root);
}

const setupPagination = (root) => {
  const pagination = root.querySelector('[data-pagination]');

  if (!pagination) return;

  const currentPage = Number(pagination.dataset.currentPage);
  const totalPages = Number(pagination.dataset.totalPages);
  const pageSize = Number(pagination.dataset.pageSize);

  initPagination(pagination, currentPage, totalPages, pageSize);
}
