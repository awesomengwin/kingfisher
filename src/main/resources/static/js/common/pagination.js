export const initPagination = (container, current, total) => {
  container.innerHTML = '';

  container.appendChild(createPreviousPageItem(current));

  for (const p of buildPageList(current, total)) {
    if (p === '...') {
      container.appendChild(createEllipsisPageItem());
    } else {
      container.appendChild(createPageItem(p, current));
    }
  }

  container.appendChild(createNextPageItem(current, total));
}

const createPageItem = (page, current) => {
  const li = document.createElement('li');
  li.classList.add('page-item');

  const link = document.createElement('a');
  link.classList.add('page-link');

  link.href = `?page=${page}`;
  link.textContent = page;

  if (page === current) {
    li.classList.add('active');
  }

  li.appendChild(link);

  return li;
}

const createEllipsisPageItem = () => {
  const li = document.createElement('li');
  li.classList.add('page-item');

  const span = document.createElement('span');
  span.classList.add('page-link');
  span.textContent = '...';

  li.appendChild(span);

  return li;
}

const createNextPageItem = (current, total) => {
  const li = document.createElement('li');
  li.classList.add('page-item');

  const link = document.createElement('a');
  link.classList.add('page-link');

  link.href = `?page=${current + 1}`;
  link.textContent = 'Next';

  if (current === total) {
    li.classList.add('disabled');
  }

  li.appendChild(link);

  return li;
}

const createPreviousPageItem = (current) => {
  const li = document.createElement('li');
  li.classList.add('page-item');

  const link = document.createElement('a');
  link.classList.add('page-link');

  link.href = `?page=${current - 1}`;
  link.textContent = 'Previous';

  if (current === 1) {
    li.classList.add('disabled');
  }

  li.appendChild(link);

  return li;
}

const buildPageList = (c, m) => {
  const current = c;
  const last = m;
  const delta = 2;
  const left = current - delta;
  const right = current + delta + 1;

  let range = [];
  let rangeWithDots = [];
  let l;

  for (let i = 1; i <= last; i++) {
    if (i === 1 || i === last || i >= left && i < right) {
      range.push(i);
    }
  }

  for (let i of range) {
    if (l) {
      if (i - l === 2) {
        rangeWithDots.push(l + 1);
      } else if (i - l !== 1) {
        rangeWithDots.push('...');
      }
    }
    rangeWithDots.push(i);
    l = i;
  }

  return rangeWithDots;
}
