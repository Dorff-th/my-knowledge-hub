    document.addEventListener("DOMContentLoaded", () => {
      const msg = document.body.dataset.msg;

      if (msg === "loginSuccess") {
        toast.success("로그인 완료!");
      } else if (msg === "logoutSuccess") {
        toast.success("로그아웃 완료!");
      }
    });