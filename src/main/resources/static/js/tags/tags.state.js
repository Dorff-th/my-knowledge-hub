// tags.state.js
// - 현재 태그 목록 상태를 관리 (아주 단순)
// - normalize: trim + 소문자 + 앞뒤 # 제거 + 연속 공백 -> 하이픈
// - validate: 길이, 허용문자

const STATE = {
  list: [],   // ["spring", "jpa", ...]
  max: 5
};

export function setMaxCount(n) {
  STATE.max = Number(n) || 5;
}

export function getTags() {
  return [...STATE.list];
}

export function setTags(arr = []) {
  const unique = [];
  arr.forEach(t => {
    const n = normalize(t);
    if (n && !unique.includes(n)) unique.push(n);
  });
  STATE.list = unique.slice(0, STATE.max);
}

export function addTag(raw) {
  const tag = normalize(raw);
  if (!tag) return { ok: false, reason: 'invalid' };
  if (STATE.list.includes(tag)) return { ok: false, reason: 'duplicate' };
  if (STATE.list.length >= STATE.max) return { ok: false, reason: 'max' };
  STATE.list.push(tag);
  return { ok: true, tag };
}

export function removeTag(tag) {
  const idx = STATE.list.indexOf(tag);
  if (idx >= 0) STATE.list.splice(idx, 1);
}

export function joinForSubmit() {
  return STATE.list.join(',');
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
