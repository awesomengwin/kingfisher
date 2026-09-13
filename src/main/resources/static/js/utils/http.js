import { getCsrfHeader, getCsrfToken } from "./csrf.js";

export const post = async (url, headers, body) => {
  return doFetch(url, 'POST', headers, body);
}

export const put = async (url, headers, body) => {
  return doFetch(url, 'PUT', headers, body);
}

const doFetch = async (url, method, headers, body) => {
  const csrfToken = getCsrfToken();
  const csrfHeader = getCsrfHeader();

  const options = {
    method: method,
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken,
      ...headers
    }
  }

  if (body) {
    options.body = JSON.stringify(body);
  }

  const response = await fetch(url, options);

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`);
  }

  return response;
}
