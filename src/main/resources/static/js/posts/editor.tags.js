import { initTagsUI } from '../tags/tags.ui.js';

document.addEventListener('DOMContentLoaded', () => {

  //기존 post에 입력된 태그가 있다면 [{tag:"태그명", dataId:아이디 }, ... ] 형태로 jsonArray 형태로 만들고
  const tagsArray = [...document.querySelectorAll('#tagChipList li')].map(li => {
      const dataId = Number(li.querySelector('.tag-chip')?.dataset.tagId);
      const tagName = li.querySelector('.tag-chip')?.dataset.tagName;
      return { tagName: tagName, dataId: dataId };
  });

  const hidden = document.getElementById('tagsHidden');
  hidden.value = '';    // tagsArray가 있다면 무의미 (설령 기존 tag 데이터가 없어도 tagsArray는 빈 배열로 존재하기때문에  불필요)

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
  }, tagsArray);


});



