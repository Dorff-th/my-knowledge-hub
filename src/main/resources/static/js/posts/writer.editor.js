// static/js/posts/writer.editor.js
// (A안: 래퍼 유지, 슬림 구현)

let editor = null;

/**
 * Toast UI Editor 초기화
 * @param {string|HTMLElement} selector  '#editor' 또는 DOM 노드
 * @param {object} opts
 *   - height, initialEditType, previewStyle, placeholder, initialValue
 *   - upload: { endpoint, fieldName, extraFields }  // 이미지 업로드만 처리(검증 X)
 *   - imageBasePath: '/uploads/images/'              // 사용중 이미지 필터용(선택)
 * @param {object} handlers
 *   - onChange?: (markdown: string) => void
 * @returns {any} 에디터 인스턴스
 */

export function initTempKey(selector) {
    let tempKey = localStorage.getItem("tempPostKey");
    if (!tempKey) {
        tempKey = crypto.randomUUID();
        localStorage.setItem("tempPostKey", tempKey);
    }
    const el = typeof selector === 'string' ? document.querySelector(selector) : selector;
    el.value = tempKey;
    return tempKey;
}

export function initEditor(selector, opts = {}, handlers = {}) {
  const el = typeof selector === 'string' ? document.querySelector(selector) : selector;
  if (!el || !window.toastui?.Editor) return null;

  const {
    height = '400px',
    initialEditType = 'markdown',
    previewStyle = 'vertical',
    placeholder = '내용을 작성하세요...',
    initialValue = '',
    upload = null,
    imageBasePath = '/uploads/images/',
  } = opts;

  editor = new window.toastui.Editor({
    el,
    height,
    initialEditType,
    previewStyle,
    placeholder,
    initialValue,
    hooks: {
      addImageBlobHook: upload
        ? async (blob, callback) => {
            try {
              const fd = new FormData();
              fd.append(upload.fieldName || 'image', blob);
              if (upload.extraFields) {
                Object.entries(upload.extraFields).forEach(([k, v]) => fd.append(k, `${v ?? ''}`));
              }
              const res = await fetch(upload.endpoint, { method: 'POST', body: fd });
              const json = await res.json();
              if (json?.success === 1 && json?.url) callback(json.url, 'image');
              else alert(json?.message || '이미지 업로드 실패');
            } catch {
              alert('이미지 업로드 중 오류');
            }
          }
        : undefined,
    },
  });

  if (typeof handlers.onChange === 'function') {
    editor.on('change', () => handlers.onChange(editor.getMarkdown()));
  }

  editor.__imageBasePath = imageBasePath; // getUsedImageUrls()에서 사용 예정
  return editor;
}

/** 본문 내 사용중 이미지 URL 의 파일명만 추출 */
export function getUsedImageUrls() {
  const md = getEditorContent();
  if (!md) return [];

  const base = editor?.__imageBasePath || '';
  const regex = /!\[[^\]]*]\(([^)]+)\)/g;

  return Array.from(md.matchAll(regex))
    .map(match => match[1].trim())                  // URL만 추출
    .filter(url => url && (!base || url.startsWith(base))) // base 조건
    .map(url => url.split('/').pop());              // 파일명만
}


export function getEditorContent() {
  if (!editor) return '';
  return editor.getMarkdown();
}
