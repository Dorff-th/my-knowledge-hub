export async function checkEmailDuplicate(email) {
  const res = await fetch(`/api/register/checked-email?email=${encodeURIComponent(email)}`);
  //if (!res.ok) throw new Error("이메일 중복 확인 실패");
    //return await res.json(); // true or false
    return res;
}

export async function checkNicknameDuplicate(nickname) {
  const res = await fetch(`/api/register/checked-nickname?nickname=${encodeURIComponent(nickname)}`);
  //if (!res.ok) throw new Error("닉네임 중복 확인 실패");
  //return await res.json(); // true or false
  return res;
}