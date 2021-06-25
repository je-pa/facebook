const profileImgElem = document.querySelector('#flexContainer .profile.w200.pointer');
const modalElem = document.querySelector('section .modal');
const modalCloseElem = document.querySelector('section .modal .modal_close');
//모든
const noMainProfileList = document.querySelectorAll('.no-main-profile');

noMainProfileList.forEach((item) => {
    item.addEventListener('click', () => {
        const iprofile = item.dataset.iprofile;
        changeMainProfile(iprofile);
    });
});

//메인 이미지 변경
function changeMainProfile(iprofile) {
    fetch(`/user/mainProfile?iprofile=${iprofile}`)
        .then(res => res.json())
        .then(myJson => {
            console.log(myJson.result);
        });
}

//모달창 띄우기
profileImgElem.addEventListener('click', () => {
    modalElem.classList.remove('hide');
});

//모달창 닫기
modalCloseElem.addEventListener('click', () => {
    modalElem.classList.add('hide');
})