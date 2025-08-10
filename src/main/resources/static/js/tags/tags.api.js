// tags.api.js
// - 레벨1: 딱 하나의 함수만. (실패 시 빈 배열)
// - 서버 엔드포인트 예시: /api/tags/suggest?query=sp

export async function fetchSuggest(query) {
  const q = (query || '').trim();
  if (q.length < 2) return []; // 너무 짧으면 안 보냄 (간단 방어)

  try {
    const res = await fetch(`/api/tags/suggest?query=${encodeURIComponent(q)}`);
    if (!res.ok) return [];
    const data = await res.json();
    // data가 ["spring","spring-boot"] 형태라고 가정
    if (Array.isArray(data)) return data.slice(0, 20);
    return [];
  } catch {
    return [];
  }
}
