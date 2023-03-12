<template>
  <div class="Board d-flex flex-column border border-2 flex-fill justify-content-between p-1">
    <div class="d-flex justify-content-between p-1 align-self-center">
      <div class="border-bottom d-flex align-items-center ps-2 pe-2">
        <span>상대 정보</span>
      </div>
    </div>
    <div class="d-flex ps-2 pe-2">
      <div class="col-6">
        <span class="BoardTitle">닉네임</span>
        <p id="myUsername" ref="memberName" v-if="this.ticket">{{ ticket.memberName }}</p>
        <p v-if="!this.ticket" class="text-white">없음</p>
      </div>
      <div class="col-4">
        <span class="BoardTitle">상태</span>
        <p v-if="this.ticket && this.ticket.studyStatus === 'STUDY'">공부중</p>
        <p v-if="this.ticket && this.ticket.studyStatus === 'REST'">휴식중</p>
        <p v-if="!this.ticket" class="text-white">쉬는중</p>
      </div>
      <div class="col-2">
        <span class="BoardTitle">자리</span>
        <p v-html="this.ticket.seat" v-if="!ticket.seat"></p>
        <p v-html="this.ticket.seat" v-if="ticket.seat"></p>
      </div>
    </div>
    <div class="d-flex ps-2 pe-2">
      <div class="col-7">
        <span class="BoardTitle">공부</span>
        <p id="myStudy" v-if="this.ticket">{{ ticket.studyName }}</p>
        <p v-if="!this.ticket" class="text-white">없음</p>
      </div>
      <div class="col-5">
        <span class="BoardTitle">사용시간</span>
        <p id="myStudyTime" v-if="this.ticket" ref="studyTime">{{ this.studyTime }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "StudyRoomMyInfo",
  data() {
    return{
      studyTime: ''
    }
  },
  props: {
    ticket: {
      memberName:'',
      roomName:'',
      seat:'',
      count:'',
      startTime:'',
      endTime:'',
      ticketStatus:'',
    }
  },
  watch: {
    ticket() {
      if(this.ticket.startTime !== undefined) {
        console.log(this.ticket)
        this.getTime(this.ticket.startTime)
        setInterval(this.getTime.bind(this), 1000)
      }
    }
  },
  mounted() {

  },
  methods: {
    getTime() {
      const time = Date.now()/1000 - this.ticket.startTime;

      let h = Math.floor(time/3600);
      let m = Math.floor((time%3600)/60);
      let s = Math.floor(time - (h*3600) - (m*60));
      m = m<10 ? '0'+m : m;
      s = s<10 ? '0'+s : s;

      this.studyTime = h + ':' + m + ':' + s;
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