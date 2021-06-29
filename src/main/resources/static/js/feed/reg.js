const fileList = [];
const ctntElem = document.querySelector('#ctnt');
const locationElem = document.querySelector('#location');
const selectImgArrElem = document.querySelector('#selectImgArr');
const btnUploadElem = document.querySelector('#btnUpload');
const displayImgListElem = document.querySelector('#displayImgList');

//글내용 변경시
//change : 포커스(마우스?)가 다른데로 이동해야 이벤트 발생
//keyup : 키보드 누르고 땔때! 이벤트 발생
ctntElem.addEventListener('keyup', ()=>{
    toggleBtnUpload();
});

//이미지들이 선택되면 fileList에 추가하기
selectImgArrElem.addEventListener('change', () => {
    const files = selectImgArrElem.files;
    for(let i=0; i<files.length; i++) {
        fileList.push(files[i]); //추가
    }
    displaySelectedImgArr();//이미지 뿌려라
});

//fileList에 추가 된 이미지들 디스플레이하기
function displaySelectedImgArr() {
    toggleBtnUpload();
    displayImgListElem.innerHTML = ''; //비운다

    for(let i=0; i<fileList.length; i++) { //넣기 시작
        const item = fileList[i];
        const reader = new FileReader(); //파일 정보 들고오는 객체
        reader.readAsDataURL(item); //읽어라
        //로드 한 후
        reader.onload = () => {
            //로딩이 끝나면 실행하는 이벤트로 처리
            // 더빨리 처리되는거 부터 실행됨
            const img = document.createElement('img'); //이미지 만듦
            img.addEventListener('click', () => {
                fileList.splice(i, 1);
                displaySelectedImgArr();
            });
            img.src = reader.result; //로딩 결과값을 줌 파일주소값?
            displayImgListElem.append(img);
        };
    }
}

//등록버튼 활성화/비활성화 결정
function toggleBtnUpload(){
    btnUploadElem.disabled=true;
    if(ctntElem.value.length > 0 || fileList.length>0){
        btnUploadElem.disabled = false;
    }
}

//등록버튼 클릭시 (Ajax로 파일 업로드)
//<form>데이터를 만들어준다
btnUploadElem.addEventListener('click', () => {
    const data = new FormData();
    if(ctntElem.value.length>0){  data.append(ctntElem.id,ctntElem.value); }
    if(locationElem.value.length>0){  data.append(locationElem.id,locationElem.value); }
    if(fileList.length>0){ //FileReader는 뭐지
        for(let i=0;i<fileList.length;i++){
            data.append('imgArr',fileList[i]);
        }
    }
    fetch('/feed/reg',{
        method: 'POST',
        body: data
    })  .then(res => res.json())
        .then(myJson => {
            console.log(myJson);
            switch (myJson.result){
                case 0:
                    alert('피드에 등록 실패');
                    break;
                case 1:
                    location.href='/feed/home';
                    break;
            }
    });
})
    /*
    fileList.forEach(item=>{
        reader.readAsDataURL(item);
        const img = document.createElement('img');
        img.src = reader.result;

        selectedImgListElem.append(img);
    });*/
