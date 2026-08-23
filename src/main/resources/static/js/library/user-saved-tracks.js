import { initPagination } from "../common/pagination.js";
import { initTrackItem } from "../common/track-item.js";

export const initUserSavedTracks = () => {
  const root = document.querySelector('[data-user-saved-tracks]');

  if (!root) return;

  setupPagination(root);
  setupTrackItem(root);
}

const setupTrackItem = (root) => {
  root.querySelectorAll('[data-track-item]').forEach(item => {
    initTrackItem(item);
  });
}

const setupPagination = (root) => {
  const pagination = root.querySelector('[data-pagination]');

  if (!pagination) return;

  const currentPage = Number(pagination.dataset.currentPage);
  const totalPages = Number(pagination.dataset.totalPages);

  initPagination(pagination, currentPage, totalPages);
}
