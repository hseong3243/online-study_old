<template>
  <div>
    <StudyRoomModal v-if="modal">
      <div>
        <div class="d-flex justify-content-between align-items-center">
          <h4>티켓 발급</h4>
          <div class="d-flex gap-1">
          <button type="button" class="btn btn-sm btn-outline-secondary" @click="closeModal">취소</button>
          <button type="button" class="btn btn-sm btn-outline-secondary" @click="issueTicket">발급</button>
          </div>
        </div>
        <div class="d-flex justify-content-between ps-2 pe-2">
          <div class="col-5">
            <span class="BoardTitle">닉네임</span>
            <p>{{ticket.memberName}}</p>
          </div>
          <div class="col-5">
            <span class="BoardTitle">공부</span>
            <p v-if="this.study">{{ study.name }}</p>
          </div>
          <div class="col-2">
            <span class="BoardTitle">자리</span>
            <p v-html="this.seat"></p>
          </div>
        </div>
        <div>
          <span>최근 스터디</span>
          <div class="d-flex gap-1" >
            <button type="button" class="btn btn-sm btn-outline-secondary rounded-5"
                    v-for="study in recentStudies" :key="study.studyId" @click="this.study=study">#{{study.name}}</button>
          </div>
        </div>
        <div class="mt-2">
          <span>스터디 검색</span>
          <div class="d-flex">
          <input type="text" class="form-control" v-model="studyName"/>
          <button type="button" class="col-2 btn btn-outline-secondary" @click="getStudies">검색</button>
          </div>
        </div>

        <div class="pt-2 gap-1 d-flex flex-wrap flex-fill">
          <button type="button" class="btn btn-sm btn-outline-secondary rounded-5"
                  v-for="study in studies" :key="study.studyId" @click="this.study=study">#{{ study.name }}</button>
        </div>
      </div>
      <template v-slot:footer>
        <button @click="closeModal">닫기</button>
      </template>
    </StudyRoomModal>
  <div class="Board d-flex flex-column border border-2 flex-fill justify-content-between p-1">
    <div class="d-flex justify-content-between p-1">
      <button class="btn btn-bread pt-1 pb-1" v-if="ticket.ticketStatus" @click="expireTicket">종료</button>
      <button class="btn btn-bread pt-1 pb-1" v-if="!ticket.ticketStatus" disabled>중지</button>
      <div class="border-bottom d-flex align-items-center ps-2 pe-2">
        <span>내 정보</span>
      </div>
      <button class="btn btn-bread pt-1 pb-1" v-if="ticket.studyStatus === 'STUDY'" @click="restTicket">휴식</button>
      <button class="btn btn-bread pt-1 pb-1" v-if="ticket.studyStatus === 'REST'" @click="studyTicket">시작</button>
      <button class="btn btn-bread pt-1 pb-1" v-if="!ticket.studyStatus" @click="[modal = true, getMemberStudies]">시작</button>
    </div>
    <div class="d-flex justify-content-between ps-2 pe-2">
      <div class="col-6">
        <span class="BoardTitle">닉네임</span>
        <p id="myUsername">{{ ticket.memberName }}</p>
      </div>
      <div class="col-4">
        <span class="BoardTitle">상태</span>
        <p v-html="studyStatus"></p>
      </div>
      <div class="col-2">
        <span class="BoardTitle">자리</span>
        <p v-html="this.seat" v-if="!ticket.seat"></p>
        <p v-html="this.ticket.seat" v-if="ticket.seat"></p>
      </div>
    </div>
    <div class="d-flex justify-content-between ps-2 pe-2">
      <div class="col-8">
        <span class="BoardTitle">공부</span>
        <p id="myStudy" class="my-study" ref="myStudy" v-if="ticket.studyName">{{ ticketStudyName }}</p>
        <p v-if="!ticket.studyStatus" class="text-white">없음</p>
      </div>
      <div class="col-4">
        <span class="BoardTitle">사용시간</span>
        <p id="myStudyTime" v-if="ticket.startTime">{{ this.studyTime }}</p>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import StudyRoomModal from "@/components/studyroom/StudyRoomModal";
import {useTokenStore} from "@/store/token";

export default {
  name: "StudyRoomMyInfo",
  components: {
    StudyRoomModal
  },
  data() {
    return{
      studyTime: '',
      studyStatus: null,
      timer: null,
      modal: false,
      memberStudies: null,
      studies: null,
      study: null,
      studyName: null,
      ticketStudyName: null,
      recentStudies: [],
    }
  },
  props: ['ticket', 'seat', 'roomId', 'groupStudy'],
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },
  mounted() {
    if(this.ticket.studyStatus === 'STUDY')
      this.studyStatus = '공부중'
    else if(this.ticket.studyStatus === 'REST')
      this.studyStatus = '휴식중'

    if(this.ticket.studyStatus === 'STUDY') {
      this.getTime()
      this.timer = setInterval(this.getTime.bind(this), 1000);
    }
    else if(this.ticket.studyStatus === 'REST') {
      this.getRestTime()
    }

    if(this.groupStudy !== null) {
      this.study = this.groupStudy
    }

    if(this.ticket.studyName !== null) {
      if (this.ticket.studyName.length > 8) {
        this.ticketStudyName = this.ticket.studyName.slice(0, 8) + '..'
      }
      else
        this.ticketStudyName = this.ticket.studyName
    }

    this.getRecentStudies()
  },
  methods: {
    getTime() {
      const time = this.ticket.studyTime + Date.now()/1000 - this.ticket.startTime;

      let h = Math.floor(time/3600);
      let m = Math.floor((time%3600)/60);
      let s = Math.floor(time - (h*3600) - (m*60));
      m = m<10 ? '0'+m : m;
      s = s<10 ? '0'+s : s;

      this.studyTime = h + ':' + m + ':' + s;
    },
    getRestTime() {
      const time = this.ticket.studyTime;
      let h = Math.floor(time/3600);
      let m = Math.floor((time%3600)/60);
      let s = Math.floor(time - (h*3600) - (m*60));
      m = m<10 ? '0'+m : m;
      s = s<10 ? '0'+s : s;

      this.studyTime = h + ':' + m + ':' + s;
    }
    ,
    issueTicket() {
      let data = {
        memberId: this.tokenStore.getMemberId,
        roomId: this.roomId,
        seat: this.seat,
        studyId: this.study.studyId
      }
      axios.post('http://localhost:8000/study-service/tickets', data)
      .then((response) => {
        this.$emit('send', response.data)
        this.$router.go()
      })
    },
    restTicket() {
      axios.post('http://localhost:8000/study-service/tickets/'+this.ticket.ticketId, {
        studyStatus: 'REST'
      })
      .then((response) => {
        this.studyStatus = '휴식중'
        clearInterval(this.timer)
        this.$emit('send', response.data)
        this.$router.go()
      })
    },
    studyTicket() {
      axios.post('http://localhost:8000/study-service/tickets/'+this.ticket.ticketId, {
        studyStatus: 'STUDY'
      })
          .then((response) => {
            this.studyStatus = '공부중'
            this.$emit('send', response.data)
            this.$router.go()
          })
    },
    closeModal() {
      this.modal = false
    },
    getStudies() {
      axios.get('http://localhost:8000/study-service/studies', {
        params: {
          studyName: this.studyName
        }
      })
          .then(response => {
            const result = response.data;
            this.studies = result.data;
          })
    },
    getMemberStudies() {
      axios.get('http://localhost:8000/study-service/tickets/studies', {
        params: {
          memberId: this.tokenStore.getMemberId,
        }
      })
          .then(response => {
            const result = response.data;
            this.memberStudies = result.data;
          })
    },
    expireTicket() {
      axios.post('http://localhost:8000/study-service/tickets/'+this.ticket.ticketId+'/expire')
      .then(response => {
        this.$emit('send', response.data)
        this.$router.go()
      })
    },
    getRecentStudies() {
      axios.get('http://localhost:8000/study-service/tickets/studies', {
        params: {
          memberId: this.tokenStore.getMemberId,
          day: 7
        }
      })
      .then(response => {
        this.recentStudies = response.data.data;
      })
    }

  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
p {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
  font-size: 20px;
}
.btn-bread {
  background-color: #F6E2BD;
  border-radius: 10px;
}
.Board {
  width: 293px;
  height: 190px;
}
.BoardTitle {
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 16px;
  color: #575757;

}
</style>