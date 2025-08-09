// attachments.js — 첨부파일 컨트롤 모듈
export function initAttachments(fileInputSelector, fileListSelector, maxFiles = 3) {
  const fileInput = document.querySelector(fileInputSelector);
  const fileList = document.querySelector(fileListSelector);

  if (!fileInput || !fileList) {
    console.warn('첨부파일 UI 요소를 찾을 수 없습니다.');
    return;
  }

  let selectedFiles = [];

  // 파일 선택 이벤트
  fileInput.addEventListener('change', () => {
    selectedFiles = [...selectedFiles, ...Array.from(fileInput.files)];

    // 중복 제거
    selectedFiles = selectedFiles.filter(
      (file, index, self) =>
        index === self.findIndex(f => f.name === file.name && f.size === file.size)
    );

    // 개수 제한
    if (selectedFiles.length > maxFiles) {
      alert(`최대 ${maxFiles}개까지 첨부 가능합니다.`);
      selectedFiles = selectedFiles.slice(0, maxFiles);
    }

    updateInputFiles(fileInput, selectedFiles);
    renderFileList(fileList, selectedFiles, fileInput);
  });

  function renderFileList(container, files, inputEl) {
    container.innerHTML = '';
    files.forEach((file, index) => {
      const li = document.createElement('li');
      li.className = "flex items-center justify-between bg-gray-50 p-2 rounded border";

      const fileName = document.createElement('span');
      fileName.textContent = file.name;
      fileName.className = "text-gray-700 text-sm truncate";

      const removeBtn = document.createElement('button');
      removeBtn.textContent = '×';
      removeBtn.className = "text-red-500 font-bold px-2 hover:text-red-700";
      removeBtn.addEventListener('click', () => {
        selectedFiles.splice(index, 1);
        updateInputFiles(inputEl, selectedFiles);
        renderFileList(container, selectedFiles, inputEl);
      });

      li.appendChild(fileName);
      li.appendChild(removeBtn);
      container.appendChild(li);
    });
  }

  function updateInputFiles(inputEl, files) {
    const dt = new DataTransfer();
    files.forEach(file => dt.items.add(file));
    inputEl.files = dt.files;
  }
}
