export const getCsrfToken = () => {
  return document.querySelector('meta[name="_csrf"]')?.content;
}

export const getCsrfHeader = () => {
  return document.querySelector('meta[name="_csrf_header"]')?.content;
}
