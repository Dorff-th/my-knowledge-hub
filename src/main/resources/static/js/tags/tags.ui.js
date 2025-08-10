// tags.ui.js
// - 칩 렌더링, 입력(Enter/콤마), 자동완성 리스트 표시/선택
// - hidden 필드에 항상 최신 값 동기화

import { getTags, setTags, addTag, removeTag, joinForSubmit, setMaxCount } from './tags.state.js';
import { fetchSuggest } from './tags.api.js';

export function initTagsUI(opts) {
  const {
    inputEl, chipListEl, suggestPanelEl, suggestListEl, hiddenEl,
    maxCount = 5, initial = []
  } = resolveElements(opts);

  setMaxCount(maxCount);
  setTags(initial);
  renderChips();
  syncHidden();

  // 입력: 엔터/콤마로 확정
  inputEl.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ',') {
      e.preventDefault();
      tryAddFromInput();
    } else if (e.key === 'Backspace' && inputEl.value === '') {
      // 비어있을 때 백스페이스: 마지막 칩 삭제 UX
      const list = getTags();
      if (list.length > 0) {
        removeTag(list[list.length - 1]);
        renderChips();
        syncHidden();
      }
    }
  });

  // 입력: 변경 시 자동완성 업데이트(레벨1: 간단 호출)
  inputEl.addEventListener('input', async () => {
    const q = inputEl.value.trim();
    if (!q) {
      hideSuggest();
      return;
    }
    const items = await fetchSuggest(q);
    if (!items.length) {
      hideSuggest();
      return;
    }
    showSuggest(items);
  });

  // 포커스 아웃 시 자동완성 닫기(간단 처리)
  document.addEventListener('click', (e) => {
    if (!suggestPanelEl.contains(e.target) && e.target !== inputEl) {
      hideSuggest();
    }
  });

  function tryAddFromInput() {
    const raw = inputEl.value;
    inputEl.value = '';
    const { ok, reason, tag } = addTag(raw);
    if (!ok) {
      // 레벨1: 간단한 경고 (필요시 토스트로 교체)
      if (reason === 'max') alert(`태그는 최대 ${maxCount}개까지 가능합니다.`);
      return;
    }
    renderChips();
    syncHidden();
    hideSuggest();
  }

  function renderChips() {
    chipListEl.innerHTML = '';
    getTags().forEach(tag => {
      const li = document.createElement('li');
      li.className = 'px-2 py-1 rounded-full bg-gray-100 text-gray-700 text-xs flex items-center gap-1';

      const span = document.createElement('span');
      span.textContent = `#${tag}`;

      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'inline-flex items-center justify-center w-4 h-4 rounded-full hover:bg-gray-200';
      btn.setAttribute('aria-label', `태그 ${tag} 삭제`);
      btn.dataset.role = 'remove-chip';
      btn.dataset.tag = tag;
      btn.textContent = '×';
      btn.addEventListener('click', () => {
        removeTag(tag);
        renderChips();
        syncHidden();
      });

      li.appendChild(span);
      li.appendChild(btn);
      chipListEl.appendChild(li);
    });
  }

  function syncHidden() {
    hiddenEl.value = joinForSubmit();
  }

  function showSuggest(items) {
    suggestListEl.innerHTML = '';
    items.forEach(tag => {
      const li = document.createElement('li');
      li.setAttribute('role', 'option');
      li.tabIndex = -1;
      li.className = 'px-3 py-2 text-sm hover:bg-gray-50 cursor-pointer';
      li.dataset.tag = tag;
      li.textContent = tag;
      li.addEventListener('click', () => {
        inputEl.value = tag;
        tryAddFromInput();
      });
      suggestListEl.appendChild(li);
    });
    suggestPanelEl.classList.remove('hidden');
    inputEl.setAttribute('aria-expanded', 'true');
  }

  function hideSuggest() {
    suggestPanelEl.classList.add('hidden');
    inputEl.setAttribute('aria-expanded', 'false');
    suggestListEl.innerHTML = '';
  }

  function resolveElements(o) {
    const q = sel => (typeof sel === 'string' ? document.querySelector(sel) : sel);
    return {
      inputEl: q(o.inputEl),
      chipListEl: q(o.chipListEl),
      suggestPanelEl: q(o.suggestPanelEl),
      suggestListEl: q(o.suggestListEl),
      hiddenEl: q(o.hiddenEl),
      maxCount: o.maxCount,
      initial: o.initial || []
    };
  }
}
