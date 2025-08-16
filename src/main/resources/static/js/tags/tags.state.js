// tags.state.js
// - 현재 태그 목록 상태를 관리 (아주 단순)
// - normalize: trim + 소문자 + 앞뒤 # 제거 + 연속 공백 -> 하이픈
// - validate: 길이, 허용문자

const STATE = {
  list: [],
  max: 5
};

export function setMaxCount(n) {
  STATE.max = Number(n) || 5;
}

export function getTags() {
  return [...STATE.list];
}

export function setTags(arr = []) {
  STATE.list = arr;
}

export function addTag(raw) {
  const tagName = normalize(raw);
  if (!tagName) return { ok: false, reason: 'invalid' };

  // 중복 체크: tagName 속성 기준
  if (STATE.list.some(item => item.tagName === tagName)) {
    return { ok: false, reason: 'duplicate' };
  }

  if (STATE.list.length >= STATE.max) {
    return { ok: false, reason: 'max' };
  }

  // 새 태그 객체 추가
  const newTag = { tagName, dataId: null };
  STATE.list.push(newTag);

  return { ok: true, tag: newTag };
}

export function removeTag(tag) {

  const dataId = tag.dataId;
  console.log(dataId);

  if (dataId !== null) {
      const deleteInput = document.getElementById('deleteTagIds'); // hidden input
      const current = deleteInput.value ? deleteInput.value.split(',') : [];
      current.push(dataId);
      deleteInput.value = current.join(',');
 }

  const idx = STATE.list.indexOf(tag);      // tag로 받아야 Stat.list에서 제거가 되는듯
  if (idx >= 0) STATE.list.splice(idx, 1);
}

export function joinForSubmit() {
  //return STATE.list.join(',');
  return STATE.list.map(tag => tag.tagName).join(',');
}

// === helpers ===
function normalize(s) {
  if (typeof s !== 'string') return '';
  let v = s.trim().toLowerCase();
  if (!v) return '';

  // 앞뒤 # 제거
  v = v.replace(/^#+|#+$/g, '');

  // 공백류 -> 하이픈
  v = v.replace(/\s+/g, '-');

  // 허용 문자: 영문소문자, 숫자, 하이픈, 언더스코어, 점, 한글
  if (!/^[가-힣a-z0-9._-]+$/.test(v)) return '';

  // 길이 제한(간단)
  if (v.length < 2 || v.length > 30) return '';

  return v;
}
