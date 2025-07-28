// toast.js

(function (global) {
  // toast 컨테이너 생성 (없을 경우)
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.style.position = 'fixed';
    container.style.bottom = '30px';
    container.style.left = '50%';
    container.style.transform = 'translateX(-50%)';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
  }

  function createToast(message, type = 'default') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;

    // 공통 스타일
    toast.style.padding = '12px 20px';
    toast.style.marginTop = '10px';
    toast.style.borderRadius = '6px';
    toast.style.color = '#fff';
    toast.style.boxShadow = '0 4px 8px rgba(0,0,0,0.3)';
    toast.style.fontSize = '14px';
    toast.style.opacity = 0;
    toast.style.animation = 'fadeInOut 3s ease forwards';

    // 타입별 색상
    switch (type) {
      case 'success':
        toast.style.backgroundColor = '#4caf50';
        break;
      case 'error':
        toast.style.backgroundColor = '#f44336';
        break;
      case 'info':
        toast.style.backgroundColor = '#2196f3';
        break;
      default:
        toast.style.backgroundColor = '#333';
    }

    container.appendChild(toast);

    setTimeout(() => {
      container.removeChild(toast);
    }, 3000);
  }

  // 전역으로 등록
  global.toast = {
    success: (msg) => createToast(msg, 'success'),
    error: (msg) => createToast(msg, 'error'),
    info: (msg) => createToast(msg, 'info'),
    show: (msg) => createToast(msg)
  };
})(window);
