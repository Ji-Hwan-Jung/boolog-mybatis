const thumbnail = document.querySelector('#thumbnail');
const thumbnailUpload = document.querySelector('#thumbnailUpload');
const closeModal = document.querySelector('#closeModal');

closeModal.addEventListener('click', deleteThumbnail);

thumbnailUpload.addEventListener('change', function() {
    thumbnail.src = URL.createObjectURL(thumbnailUpload.files[0]);
});

function deleteThumbnail() {
    thumbnailUpload.value = '';
    thumbnail.src = 'http://chiye1890.dothome.co.kr/img/no_thumbnail.png';
}

function savePost() {

    let title = document.querySelector('#title').value;
    let content = '';
    let tags = document.querySelector('#tags').value;

    if (editor.isMarkdownMode()) {
        content = editor.getMarkdown();
    }

    if (editor.isWysiwygMode()) {
        content = editor.getHTML();
    }

    if (title.length < 1) {
        alert('제목을 입력해주세요.');
        return;
    }

    if (content.length < 1) {
        alert('내용을 입력해주세요.');
        return;
    }

    const formData = new FormData();
    formData.append('memberId', memberId);
    //formData.append('thumbnail', 'http://chiye1890.dothome.co.kr/img/Spring_Logo.svg');
    formData.append('title', title);
    formData.append('content', content);
    formData.append('tags', tags);

    fetch("/posts/write", {
        method: 'POST',
        body: formData
        })
        .then((response) => response.text())
        .then((data) => {
//            alert('등록 성공');
            window.location.href = '/posts/' + data;
        })
        .catch((error) => {
            console.error('실패', error);
    });
}

function updatePost() {

    let title = document.querySelector('#title').value;
    let content = '';
    let tags = document.querySelector('#tags').value;

    if (editor.isMarkdownMode()) {
        content = editor.getMarkdown();
    }

    if (editor.isWysiwygMode()) {
        content = editor.getHTML();
    }

    if (title.length < 1) {
        alert('제목을 입력해주세요.');
        return;
    }

    if (content.length < 1) {
        alert('내용을 입력해주세요.');
        return;
    }

    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('tags', tags);

    fetch("/posts/" + id + "/edit", {
        method: 'PUT',
        body: formData
        })
        .then((response) => response.text())
        .then((data) => {
            window.location.href = '/posts/' + data;
        })
        .catch((error) => {
            console.error('실패', error);
    });
}

function deletePost() {

    const result = confirm('게시글을 삭제하시겠습니까?');

    if (result) {
        fetch("/posts/" + id + "/delete", {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then((response) => response.text())
        .then((data) => {
            window.location.href = "/";
        })
        .catch((error) => {
            console.error('실패', error);
        });
    }
}

function thumbUpProc(param) {

    let likedCnt = document.querySelector('#likedCnt');
    let likedStatus = document.querySelector('#likedStatus');

    if (param == 0) {
        fetch("/posts/" + id + "/thumb-up", {
            method: 'POST',
        })
        .then((response) => response.text())
        .then((data) => {
            if (data == 'error'){
                alert('로그인 후 이용해주세요')
                window.location.href = "/signin";
            }
            likedStatus.classList.replace('btn-outline-success', 'btn-success')
            likedStatus.setAttribute('onclick', 'thumbUpProc(1)')
            likedCnt.textContent = data;
        })
        .catch((error) => {
            alert('로그인 후 이용해주세요')
            window.location.href = "/signin";
        })
    }

    if (param == 1) {
        fetch("/posts/" + id + "/thumb-up-cancel",{
            method: 'DELETE',
        })
        .then((response) => response.text())
        .then((data) => {
            if (data == 'error'){
                alert('로그인 후 이용해주세요')
                window.location.href = "/signin";
            }
            likedStatus.classList.replace('btn-success', 'btn-outline-success')
            likedStatus.setAttribute('onclick', 'thumbUpProc(0)')
            likedCnt.textContent = data;
        })
        .catch((error) => {
            alert('로그인 후 이용해주세요')
            window.location.href = "/signin";
        })
    }
}

