// static/js/posts/writer.js
// 페이지 오케스트레이터: 초기화 + 저장 클릭 처리만 담당

import { initTempKey, initEditor, getEditorContent, getUsedImageUrls } from './writer.editor.js';
import { initAttachments } from './attachments.js';

//에디터에서 이미지를 db에 저장하기 위한 임시 키값
const tempKey = initTempKey('#tempKey');

//html이 로드된 후에 작업 (마치 jQuery의 $document.reay()와 같다)
document.addEventListener('DOMContentLoaded', () => {

  //마크다운 에디터 초기화
  initEditor('#editor', {
    height: '400px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    placeholder: '내용을 작성하세요...',
    imageBasePath: '/uploads/images/',

    upload: {
      endpoint: '/api/images/upload',       // 에디터 이미지 전송 엔드포인트
      fieldName: 'image',
      extraFields: {
        mode: 'CREATE',       // 수정 화면이면 'UPDATE'
        identifier: tempKey,  // 수정 화면이면 postId
      },
    },
  });

  //첨부파일 기능 초기화
  initAttachments('#fileInput', '#fileList', 3);


  //저장 버튼 이벤트
  const btnSave = document.getElementById('btnSave');

  btnSave?.addEventListener('click', async () => {

      //1.마크다운 내용 가져와서 content 에 넣기
      const content = getEditorContent();
      document.getElementById('content').value = content;

      //2. 정규표현식으로 이미지 경로 추출 및 이미지 경로에서 파일명(storedName)만 추출
      const storedNames = getUsedImageUrls(content);

      //3. 서버에 이미지 정리 요청 보내기
      fetch("/api/images/temp/cleanup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                tempKey: tempKey,
                mode : "CREATE",
                storedNames: storedNames
            })
      });

      //5. 첨부파일을 아무것도 안한 상태라면
      document.querySelectorAll('input[type="file"]').forEach(input => {
            if (!input.value) {
                input.remove(); // 아무 파일도 선택 안 한 경우 제거
            }
      });

      //전송!
      document.getElementById("postForm").submit();

  }); // btnSave 이벤트 end


}); // DOMContentLoaded end
