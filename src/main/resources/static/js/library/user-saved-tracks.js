import { initPagination } from "../common/pagination.js";

export const initUserSavedTracks = () => {
  const root = document.querySelector('[data-user-saved-tracks]');

  if (!root) return;

  const pagination = root.querySelector('[data-pagination]');

  if (!pagination) return;

  const currentPage = Number(pagination.dataset.currentPage);
  const totalPages = Number(pagination.dataset.totalPages);

  initPagination(pagination, currentPage, totalPages);
}
