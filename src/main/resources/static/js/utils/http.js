import { getCsrfHeader, getCsrfToken } from "./csrf.js";

export const post = async (url, headers, body) => {
  const csrfToken = getCsrfToken();
  const csrfHeader = getCsrfHeader();

  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken,
      ...headers
    }
  }

  if (body) {
    options.body = JSON.stringify(body);
  }

  return fetch(url, options);
}
