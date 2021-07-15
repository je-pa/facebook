const modalElem = document.querySelector('section .modal');
const modalCloseElem = document.querySelector('section .modal .modal_close');
//모든
//모든 no-main-profile 아이콘에 이벤트를 걸어준다.
//이벤트는 메인 이미지 변경처리
const btnFollowElem = document.querySelector('#btnFollow');
const profileImgParentList = document.querySelectorAll('.profile-img-parent');

const profileImgElem = document.querySelector('#profileImg');


//모달창 띄우기 이벤트
profileImgElem.addEventListener('click', () => {
    modalElem.classList.remove('hide');
});

profileImgParentList.forEach(item => {
    const iElem = item.querySelector('i');//부모(profile-)안에 있는 i를 가져온다
    if(iElem != null) { //메인  프로필일때 null이다 => unless를 사용했기 때문
        addIElemEvent(iElem);
    }
});

//메인이미지 바꾸기 아이콘에 이벤트 설정
function addIElemEvent(target) { //target은 자식인 i이다
    target.addEventListener('click', () => {
        const iprofile = target.parentNode.dataset.iprofile;
        //부모의 attr="data-iprofile값을 들고온다
        console.log(iprofile);
        changeMainProfile(iprofile);
    });
}

//메인 이미지 변경
function changeMainProfile(iprofile) {
    fetch(`/user/mainProfile?iprofile=${iprofile}`)
        .then(res => res.json())
        .then(myJson => {
            switch(myJson.result) {
                case 0:
                    alert('메인 이미지 변경에 실패하였습니다.');
                    break;
                case 1:
                    setMainProfileIcon(iprofile);


                    //바뀐 메인 이미지 img값을 찾기
                    const findParentDiv = profileImgParentList.find(item=> item.dataset.iprofile === iprofile)
                    // const findParentDiv = profileImgParentList.find(item=>{
                    //     return item.dataset.iprofile === iprofile;
                    // })
                    // const findParentDiv = profileImgParentList.find(function(item){
                    //     return item.dataset.iprofile === iprofile;
                    // }) true가 되는순간 주소값 리턴
                    const container = findParentDiv.parentNode;
                    const imgElem = containerElem.querySelector('img');

                    //sector에 있는 프로필 이미지 변경
                    // const src = profileImgElem.src;
                    //
                    // const frontSrc = substring(0, src.lastIndexOf("/"));
                    // const resultSrc = `${frontSrc}/${myJson.img}`;
                    // profileImgElem.src= resultSrc;
                    profileImgElem.src= imgElem.src;

                    //헤더에 있는 프로필 이미지 변경
                    const headerProfileImgElem = document.querySelector('header .span__profile img');
                    headerProfileImgElem.src = resultSrc;
                    break;
            }
        });
}
                            //현재 mainprofile로 바뀐 iprofile값
function setMainProfileIcon(iprofile) {
    profileImgParentList.forEach(item => {
        item.innerHTML = '';

        const itemIprofile = item.dataset.iprofile;
        if(iprofile !== itemIprofile) {
            const iElem = document.createElement('i');
            iElem.className = 'no-main-profile pointer fas fa-bell';
            item.append(iElem);

            addIElemEvent(iElem);
        }
    });
}



//모달창 닫기
if(modalCloseElem){
    modalCloseElem.addEventListener('click', () => {
        modalElem.classList.add('hide');
        //  location.reload();
    })
}

if(btnFollowElem) {
    btnFollowElem.addEventListener('click', () => {
        const param = {iuserYou: localConstElem.dataset.iuser};
        const init = {}
        let queryString = '';
        switch (btnFollowElem.dataset.follow) {
            case '0': //no팔로우 > 팔로우
                init.method = 'POST';
                init.headers = {'Content-Type': 'application/json'};
                init.body = JSON.stringify(param);
                break;
            case '1': //팔로우 > 팔로우취소
                init.method = 'DELETE';
                queryString = `?iuserYou=${param.iuserYou}`;
                break;
        }

        fetch('follow' + queryString, init)
            .then(res => res.json())
            .then(myJson => {
                console.log(myJson);
                if (myJson.result == 1) {
                    let buttnNm = '팔로우 취소';
                    if (btnFollowElem.dataset.follow === '1') {
                        if (myJson.youFollowMe = null) {
                            buttnNm = '팔로우';
                        } else {
                            buttnNm = '맞팔로우';
                        }
                    }
                    btnFollowElem.classList.toggle('instaBtnEnable');
                    btnFollowElem.value = buttnNm;
                    btnFollowElem.dataset.follow = 1 - btnFollowElem.dataset.follow;
                } else {
                    alert('에러가 발생하였습니다.');
                }
            });
    });
}

const localConstElem = document.querySelector('#localConst');


feedObj.url = '/user/feedList';
feedObj.setScrollInfinity(window);
feedObj.iuser = localConstElem.dataset.iuser;
feedObj.getFeedList(1);

/******************************************************** 팔로우 모달 **/
const followerElemArr = document.querySelectorAll('.pointer.follower');
const followElemArr = document.querySelectorAll('.pointer.follow');
const modalFollowElem = document.querySelector('.modal-follow');
const modalFollowTitleElem = modalFollowElem.querySelector('#title');
const modalFollowCloseElem = document.querySelector('.modal-follow #modal-follow-close');
const modalFollowItemConElem = modalFollowElem.querySelector('.followCont');

if(followerElemArr) {
    followerElemArr.forEach(item => {
        item.addEventListener('click', () => {
            modalFollowTitleElem.innerText = '팔로워';
            modalFollowElem.classList.remove('hide');
            modalFollowItemConElem.innerHTML = '';

            //프로필 사용자를 팔로우한 사람들 리스트
            fetch(`getFollowerList?iuserYou=${localConstElem.dataset.iuser}`)
                .then(res => res.json())
                .then(myJson => {
                    if(myJson.length > 0) {
                        myJson.forEach(item => {
                            const cont = makeFollowItem(item);
                            modalFollowItemConElem.append(cont);
                        });
                    }
                });
        });
    });
}

if(followElemArr) {
    followElemArr.forEach(item => {
        item.addEventListener('click', () => {
            modalFollowTitleElem.innerText = '팔로우';
            modalFollowElem.classList.remove('hide');
            modalFollowItemConElem.innerHTML = '';

            //프로필 사용자가 팔로우한 사람들 리스트
            fetch(`getFollowList?iuserYou=${localConstElem.dataset.iuser}`)
                .then(res => res.json())
                .then(myJson => {
                    if(myJson.length > 0) {
                        myJson.forEach(item => {
                            const cont = makeFollowItem(item);
                            modalFollowItemConElem.append(cont);
                        });
                    }
                });
        });
    });
}

if(modalFollowCloseElem) {
    modalFollowCloseElem.addEventListener('click', () => {
        modalFollowElem.classList.add('hide');
    });
}

function makeFollowItem(item) {
    const globalContElem = document.querySelector('#globalConst');
    const loginIuser = globalContElem.dataset.iuser;

    const cont = document.createElement('div');
    cont.className = 'follow-item';
    const img = document.createElement('img');
    img.className = 'profile wh30 pointer';
    img.src = `/pic/profile/${item.iuser}/${item.mainProfile}`;
    img.onerror = () => { img.style.visibility = 'hidden'; }
    img.addEventListener('click', ()=> {
        moveToProfile(item.iuser); //feed.js 에 있는 메소드
    });

    const nm = document.createElement('div');
    const nmText = document.createElement('span');
    nmText.innerText = item.nm;
    nmText.className = 'pointer';
    nmText.addEventListener('click', ()=> {
        moveToProfile(item.iuser); //feed.js 에 있는 메소드
    });
    nm.append(nmText);

    const btn = document.createElement('input');
    btn.className = 'instaBtn pointer';
    btn.dataset.follow = '0';
    btn.addEventListener('click', () => {
        const follow = parseInt(btn.dataset.follow);
        followProc(follow, item.iuser, btn);
    });

    cont.append(img);
    cont.append(nm);
    if(parseInt(loginIuser) !== item.iuser) {
        btn.type = 'button';
        if(item.isMeFollowYou) {
            btn.dataset.follow = '1';
            btn.value = '팔로우 취소';
        } else {
            btn.classList.add('instaBtnEnable');
            btn.value = '팔로우';
        }
        cont.append(btn);
    }
    return cont;
}