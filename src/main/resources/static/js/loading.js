// static/js/loading.js

// 오버레이 DOM 요소 생성 (중복 방지)
let overlay = document.getElementById('loading-overlay');
if (!overlay) {
  overlay = document.createElement('div');
  overlay.id = 'loading-overlay';

  Object.assign(overlay.style, {
    position: 'fixed',
    top: '0',
    left: '0',
    width: '100vw',
    height: '100vh',
    backgroundColor: 'rgba(255, 255, 255, 0.7)',
    display: 'none',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: '9999',
  });

  const spinner = document.createElement('div');
  spinner.className = 'loading-spinner';

  Object.assign(spinner.style, {
    width: '50px',
    height: '50px',
    border: '6px solid #ccc',
    borderTop: '6px solid #3498db',
    borderRadius: '50%',
    animation: 'spin 1s linear infinite',
  });

  overlay.appendChild(spinner);
  document.body.appendChild(overlay);
}

// ✅ show/hide 함수
export function showLoading() {
  overlay.style.display = 'flex';
}

export function hideLoading() {
  overlay.style.display = 'none';
}
