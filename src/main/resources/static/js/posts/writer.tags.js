// writer.tags.js
// - writer.html에서 태그 UI 초기화만 담당
// - 서버에서 기존 태그가 있다면 hidden에 미리 "a,b,c" 형태로 넣어둘 수 있음

import { initTagsUI } from '../tags/tags.ui.js';

document.addEventListener('DOMContentLoaded', () => {
  const hidden = document.getElementById('tagsHidden');
  const initial = (hidden?.value || '')
    .split(',')
    .map(s => s.trim())
    .filter(Boolean);

  initTagsUI({
    inputEl: '#tagInput',
    chipListEl: '#tagChipList',
    suggestPanelEl: '#tagSuggestPanel',
    suggestListEl: '#tagSuggestListbox',
    hiddenEl: '#tagsHidden',
    maxCount: 5,
    initial
  });
});
