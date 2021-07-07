function getDateTimeInfo(dt){
    const nowDt = new Date();
    const targetDt = new Date(dt);

    const nowDtSec = parseInt(nowDt.getTime() / 1000);
    const targetDtSec = parseInt(targetDt.getTime()/1000);

    const diffSec = nowDtSec - targetDtSec; // 초
    if(diffSec < 60 ){
        return `${diffSec}초 전`;
    }else if(diffSec<3600){ //분단위
        return `${parseInt(diffSec/60)}분 전`;
    }else if(diffSec<86400){//시간단위
        return `${parseInt(diffSec/3600)}시간 전`;
    }else if(diffSec<604800){
        return `${parseInt(diffSec/86400)}일 전`;
    }
    return targetDt.toLocaleString();
}

const feedObj = { //home2.js에서 값 넣어줌
    limit: 5,
    itemLength: 0,
    currentPage: 1,
    url: '',
    iuser:0,
    swiper: null,
    containerElem: document.querySelector('#feedContainer'),
    loadingElem: document.querySelector('.loading'),

    makeFeedList: function(data) {
        if(data.length == 0) { return; }

        for(let i=0; i<data.length; i++) {
            const item = data[i];
            //var로 하면 마지막 값으로 저장
            //클로저? 안됨 호이스팅 됨
            const itemContainer = document.createElement('div');
            itemContainer.classList.add('item');

            //글쓴이 정보 영역( top)
            const regDtInfo = getDateTimeInfo(item.regdt);
            const topDiv = document.createElement('div');
            topDiv.classList.add('top')
            topDiv.innerHTML = `
                <div class="itemProfileCont">
                    <img src="/pic/profile/${item.iuser}/${item.mainProfile}">
                </div>
                <div>
                    <div>${item.writer}  ${regDtInfo}</div>
                    <div>${item.location == null ? '' : item.location}</div>
                </div>
            `;
            //이미지영역
            const imgDiv = document.createElement('div');
            imgDiv.classList.add('itemImg');

            const swiperContainerDiv = document.createElement('div');
            swiperContainerDiv.classList.add('swiper-container');

            const swiperWrapperDiv = document.createElement('div');
            swiperWrapperDiv.classList.add('swiper-wrapper');

            swiperContainerDiv.append(swiperWrapperDiv);
            imgDiv.append(swiperContainerDiv);

            for(let z=0; z<item.imgList.length; z++) {
                const imgObj = item.imgList[z];

                const swiperSlideDiv = document.createElement('div');
                swiperSlideDiv.classList.add('swiper-slide');

                const img = document.createElement('img');
                img.src = `/pic/feed/${item.ifeed}/${imgObj.img}`;
                swiperSlideDiv.append(img);
                swiperWrapperDiv.append(swiperSlideDiv);
            }

            itemContainer.append(topDiv);
            itemContainer.append(imgDiv);

            //좋아요 영역 이벤트 걸어야해서 innerHtml 안함
            const favDiv = document.createElement('div');
            favDiv.classList.add('favCont');
            const heartIcon = document.createElement('i');
            // heartIcon.classList.add('fa-heart');
            // heartIcon.classList.add('pointer');
            heartIcon.className = 'fa-heart pointer';

            if(item.isFav ===1){ //좋 o
                heartIcon.classList.add('fas');
            } else { //좋아요 x
                heartIcon.classList.add('far');
            }
            const heartCntSpan = document.createElement('span');
            heartCntSpan.innerText = item.favCnt;

            //이벤트를 거는순간 클로저 현상으로 메모리가 저장?고정? 된다
            //클로저 : 소멸되야하는데 소멸되지 않는 특성성
           heartIcon.addEventListener('click',()=>{
                console.log('ifeed:'+item.ifeed);
                // const type = heartIcon.classList.contains('fas')?0:1;//앞으로 변경되어야 할 값!
                item.isFav = 1-item.isFav;
                fetch(`fav?ifeed=${item.ifeed}&type=${item.isFav}`)
                    .then(res => res.json())
                    .then(myJson=>{
                        if(myJson===1){
                            switch (item.isFav){
                                case 0: // o-> x
                                    heartIcon.classList.remove('fas');
                                    heartIcon.classList.add('far');
                                    heartCntSpan.innerText--;
                                    break;
                                case 1: //x -> o
                                    heartIcon.classList.remove('far');
                                    heartIcon.classList.add('fas');
                                    heartCntSpan.innerText++;
                                    break;
                            }
                        }
                    })
            });
            favDiv.append(heartIcon);

            favDiv.append(heartCntSpan);

            itemContainer.append(favDiv);

            //글 내용 영역
            if(item.ctnt != null) {
                const ctntDiv = document.createElement('div');
                ctntDiv.innerText = item.ctnt;
                ctntDiv.classList.add('itemCtnt');
                itemContainer.append(ctntDiv);
            }
            //댓글 영역
            const cmtDiv = document.createElement('div');
            // cmtDiv.classList.add('cmt');
            // if(item.cmt!=null){
            //     const cmtItemContDiv = document.createElement('div');
            //     cmtDiv.innerHTML =`
            //     <div class="cmt1">
            //         <div class="cmtWriterProfile">
            //             <img src="/pic/profile/${item.cmt.iuser}/${item.mainProfile}">
            //         </div>
            //         <div>
            //             <div>${item.cmt.writer}</div>
            //             <div>${item.cmt.cmt}</div>
            //         </div>
            //     </div>
            // `;
            // }

            const cmtListDiv = document.createElement('div');
            const cmtFormDiv = document.createElement('div');
            cmtDiv.append(cmtListDiv);
            if(item.cmt!=null && item.cmt.isMore===1){
                const moreCmtDiv = document.createElement('div');
                const moreCmtSpan = document.createElement('span');
                moreCmtDiv.className = 'pointer';
                moreCmtSpan.innerText = '댓글 더보기';
                moreCmtSpan.addEventListener('click',()=>{
                    moreCmtSpan.remove();
                    fetch(`cmt?ifeed=${item.ifeed}`)
                        .then(res=>res.json())
                        .then(result =>{
                            console.log(result);
                            result.forEach(obj =>{
                                const cmtItemContainerDiv =this.makeCmtItem(obj);
                                cmtListDiv.append(cmtItemContainerDiv);
                            })
                        })
                })
                moreCmtDiv.append(moreCmtSpan);
                cmtDiv.append(moreCmtDiv);
            }
            cmtDiv.append(cmtListDiv);

            cmtDiv.append(cmtFormDiv);
            const cmtInput = document.createElement('input');
            cmtInput.type='text';
            cmtInput.placeholder = '댓글을 입력하세요...';
            cmtInput.addEventListener('keyup',(e)=>{
               console.log(e.key);
               console.log(e.code);
               if(e.key ==='Enter'){
                   cmtBtn.click();
               }
            });

            if(item.cmt!=null){ //댓글 있음
                const cmtItemContainertDiv = this.makeCmtItem(item.cmt);
                cmtListDiv.append(cmtItemContainertDiv);
            }
            const cmtBtn = document.createElement('input');
            cmtBtn.type = 'button';
            cmtBtn.value = '등록';
            cmtBtn.addEventListener('click',()=>{
                const cmt = cmtInput.value;
                if(cmt.length===0){
                    alert('댓글 내용을 작성해 주세요.');
                    return;
                }

                const param = {
                    ifeed: item.ifeed,
                    cmt: cmt
                }
                fetch('cmt',{
                    method:'POST',
                    headers:{
                        'Accept':'application/json',
                        'Content-Type':'application/json'
                    },
                    body:JSON.stringify(param)
                })
                    .then(res=>res.json())
                    .then(myJson=>{
                        console.log(myJson);
                        switch (myJson){
                            case 0:
                                alert('댓글 등록 불가');
                                break;
                            case 1:
                                const globalConstElem = document.querySelector('#globalConst');
                                const param = {...globalConstElem.dataset};//객체 복사
                                param.cmt = cmtInput.value;
                                //cmt속성값 추가
                                // 복사해서 썼기에 html에 적용X

                                const cmtItemDiv = this.makeCmtItem(param);
                                cmtListDiv.append(cmtItemDiv);

                                cmtInput.value='';
                                //댓글 추가한다
                                break;
                        }
                    })
                    .catch(err=>{
                        console.log('1 '+err);
                    });
            });

            cmtFormDiv.append(cmtInput);
            cmtFormDiv.append(cmtBtn);

            itemContainer.append(cmtDiv);
            this.containerElem.append(itemContainer);
        }
        if(this.swiper != null) { this.swiper = null; }
        this.swiper = new Swiper('.swiper-container', {
            direction: 'horizontal',
            loop: false,
        });
    },
    setScrollInfinity: function(target) {
        target.addEventListener('scroll', () => {
            const {
                scrollTop,
                scrollHeight,
                clientHeight
            } = document.documentElement;

            if (scrollTop + clientHeight >= scrollHeight - 5 && this.itemLength === this.limit) {
                this.itemLength = 0;
                this.getFeedList(++this.currentPage);
            }
        }, { passive: true });
    },
    getFeedList: function(page) {
        this.showLoading();
        console.log(page);
        fetch(`${this.url}?iuserForMyFeed=${this.iuser}&page=${page}&limit=${this.limit}`)
            .then(res => res.json())
            .then(myJson => {
                console.log(myJson);
                this.itemLength = myJson.length;
                this.makeFeedList(myJson);
            }).catch(err => {
            console.log(err);
        }).then(() => {
            this.hideLoading();
        });
    },
    makeCmtItem: function ({iuser, writerProfile, writer, cmt}){ //객체를 받아서 필요한 값만 쓰겠다
        const cmtItemContainertDiv = document.createElement('div');
        cmtItemContainertDiv.className='cmtItemCont';
        //프로필
        const cmtItemProfileDiv = document.createElement('div');
        cmtItemProfileDiv.className='cmtItemProfile';
        const cmtItemWriterProfileImg = document.createElement('img');
        cmtItemWriterProfileImg.src = `/pic/profile/${iuser}/${writerProfile}`;
        cmtItemWriterProfileImg.className = 'profile w30';

        cmtItemProfileDiv.append(cmtItemWriterProfileImg);
        cmtItemContainertDiv.append(cmtItemProfileDiv);

        //댓글
        const cmtItemCtntDiv = document.createElement('div');
        cmtItemCtntDiv.className='cmtItemCtnt';
        cmtItemCtntDiv.innerHTML = `<div>${writer}</div><div>${cmt}</div>`;
        cmtItemContainertDiv.append(cmtItemCtntDiv);

        return cmtItemContainertDiv;
    },
    hideLoading: function() { this.loadingElem.classList.add('hide');},
    showLoading: function() { this.loadingElem.classList.remove('hide'); }
}