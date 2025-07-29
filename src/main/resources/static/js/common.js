    /*  01. Toast 메시지  */
    document.addEventListener("DOMContentLoaded", () => {
      const msg = document.body.dataset.msg;

      if (msg === "loginSuccess") {
        toast.success("로그인 완료!");
      } else if (msg === "logoutSuccess") {
        toast.success("로그아웃 완료!");
      }
    });

    /* 02. 전역 Loading Overlay */
    function showLoading() {
        document.getElementById('loading-overlay').style.display = 'flex';
    }

    function hideLoading() {
        document.getElementById('loading-overlay').style.display = 'none';
    }

    // 폼 제출 시 로딩 띄우기
    document.addEventListener("DOMContentLoaded", () => {
        document.querySelectorAll("form").forEach(form => {
          form.addEventListener("submit", () => {
            showLoading();
          });
        });
    });

