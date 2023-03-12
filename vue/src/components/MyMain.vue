<template>
  <div>
    <div class="background bg-light">
  <div class="MyContainer m-auto d-flex p-2 pt-5 pb-5 justify-content-between">
      <div class="col-7 d-flex border border-2 rounded p-3 gap-3">
        <button class="btn btn-outline-secondary d-flex flex-column justify-content-center align-items-center">
          <img :src="require('@/assets/img/desk_office_home_work_icon.png')" alt="test">
          <span >공부빵</span>
          <span >바로 입장하기</span>
        </button>
        <div class="d-flex col flex-column">
          <div class="d-flex flex-column">
            <div class="d-flex col justify-content-between">
              <span>각오</span>
              <button type="button" class="btn btn-sm btn-outline-secondary align-self-start" @click="showChangePromise=true" v-if="!showChangePromise">설정</button>
              <button type="button" class="btn btn-sm btn-outline-secondary align-self-start" @click="changePromise" v-if="showChangePromise">완료</button>
            </div>
            <div class="d-flex col justify-content-center" v-if="!dataReady">
              <span>{{promise}}</span>
            </div>
            <div class="d-flex col justify-content-center" v-if="dataReady">
              <span v-if="!showChangePromise">{{promise}}</span>
              <input type="text" class="form-control form-control-sm" v-model="promise" v-if="showChangePromise"/>
            </div>
          </div>
          <hr/>
          <div class="d-flex col">
            <div class="d-flex flex-column col justify-content-between">
              <div class="d-flex justify-content-between">
              <span>목표 공부 시간</span>
              <button class="btn btn-sm btn-outline-secondary align-self-start" @click="showTargetTime=true" v-if="!showTargetTime">설정</button>
              <button class="btn btn-sm btn-outline-secondary align-self-start" @click="changeTargetTime" v-if="showTargetTime">완료</button>
              </div>
              <div>
                <span>{{ nowStudyTime }}</span>
                <span>&nbsp;/&nbsp;</span>
                <span class="text-secondary">{{ targetStudyTime }}</span>
                <div class="progress" v-if="!showTargetTime">
                  <div class="progress-bar bg-warning" role="progressbar" ref="bar"></div>
                </div>
                <div v-if="showTargetTime">
                  <date-picker v-model:value="targetTime" :minute-step="10"
                               format="HH:mm" type="time" placeholder="HH:mm"></date-picker>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-4 d-flex flex-column border border-2 rounded p-3">
        <div class="d-flex flex-column col justify-content-between">
          <div class="d-flex justify-content-between">
            <span>디데이</span>
            <button class="btn btn-sm btn-outline-secondary align-self-start" @click="showChangeDDay = true" v-if="!showChangeDDay">설정</button>
            <button class="btn btn-sm btn-outline-secondary align-self-start" @click="ChangeDDay" v-if="showChangeDDay">완료</button>
          </div>
          <div class="col d-flex flex-column justify-content-center align-items-center" v-if="!showChangeDDay">
            <div>
            <span>{{targetDay}}</span>
            </div>
            <div class="d-flex flex-wrap justify-content-center align-items-center">
            <span class="fs-2">{{goal}}&nbsp;</span>
            <span class="fs-2">&nbsp;D{{dDay}}</span>
            </div>
          </div>
          <div class="col d-flex flex-column justify-content-center align-items-center gap-2" v-if="showChangeDDay">
            <input type="text" class="form-control" v-model="goal"/>
            <date-picker v-model:value="date"></date-picker>
          </div>
        </div>
      </div>
    </div>
  <div class="MyContainer m-auto">
    <div>
    </div>
  </div>
  </div>
    <div class="MyContainer m-auto d-flex flex-column mt-3">
      <div class="d-flex col">
        <div class="col-7 d-flex flex-column justify-content-between p-2" v-if="this.tokenStore.getMemberId">
          <h4>내 그룹</h4>
          <div class="grid-container align-self-center gap-3">
            <div class="border rounded rounded-3 shadow-sm" v-for="group in memberGroups" :key="group.groupId">
              <group-card  :group="group" />
            </div>
            <div class="bg-secondary rounded shadow-sm d-flex flex-column pt-5" style="grid-column: 1/3; grid-row: 2;" v-if="!memberGroupPre && !memberGroupNext">
              <span class="text-white align-self-center fs-5">새로운 그룹에 참여해보세요.</span>
            </div>
          </div>
          <div class="d-flex justify-content-evenly">
            <button type="button" class="btn btn-sm disabled text-white" ref="pre" v-if="!memberGroupPre">이전</button>
            <button type="button" class="btn btn-sm btn-secondary" ref="pre" v-if="memberGroupPre" @click="getMemberGroups(-1)">이전</button>
            <button type="button" class="btn btn-sm disabled text-white" ref="next" v-if="!memberGroupNext">다음</button>
            <button type="button" class="btn btn-sm btn-secondary" ref="next" v-if="memberGroupNext" @click="getMemberGroups(1)">다음</button>
          </div>
        </div>
        <div class="col-7 d-flex flex-column justify-content-between p-2" v-if="!this.tokenStore.getMemberId">
          <h4>스터디 그룹</h4>
          <div class="grid-container align-self-center gap-3">
            <div class="border rounded rounded-3 shadow-sm" v-for="group in groups" :key="group.groupId">
              <group-card  :group="group" />
            </div>
          </div>
          <div class="d-flex justify-content-evenly">
            <button type="button" class="btn btn-sm disabled text-white" ref="pre" v-if="groupsData && !groupsData.previous">이전</button>
            <button type="button" class="btn btn-sm btn-secondary" ref="pre" v-if="groupsData && groupsData.previous" @click="getMemberGroups(-1)">이전</button>
            <button type="button" class="btn btn-sm disabled text-white" ref="next" v-if="groupsData && !groupsData.next">다음</button>
            <button type="button" class="btn btn-sm btn-secondary" ref="next" v-if="groupsData && groupsData.next" @click="getMemberGroups(1)">다음</button>
          </div>
        </div>
        <div class="col p-2">
          <div class="border-bottom">
            <ul class="nav nav-tabs">
              <li class="nav-item">
                <a class="nav-link active" ref="day" @click="getRank('DAY')" href="#">일간</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" ref="week" @click="getRank('WEEK')" href="#">주간</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" ref="month" @click="getRank('MONTH')" href="#">월간</a>
              </li>
            </ul>
          </div>
          <div class="border" style="height: 410px;">
          <table class="table table-striped mb-0">
            <thead>
            </thead>
            <tbody>
            <main-ranking v-for="rank in rankList" :key="rank.memberName" :rank="rank"/>
            </tbody>
          </table>
          </div>
        </div>
      </div>
<!--      <div>-->
<!--        <h4>스터디룸</h4>-->
<!--        <div class="group-container align-self-center gap-3">-->
<!--          <div class="border rounded rounded-3 shadow-sm" v-for="group in groups" :key="group.groupId">-->
<!--            <group-card  :group="group" />-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
    </div>
  </div>
</template>

<script>
import DatePicker from 'vue-datepicker-next';
import 'vue-datepicker-next/index.css'
import axios from "axios";
import MainRanking from "@/components/record/MainRanking";
import {useTokenStore} from "@/store/token";
import GroupCard from "@/components/group/GroupCard";
import moment from "moment"

export default {
  name: "MyMain",
  components: {
    GroupCard,
    MainRanking,
    DatePicker,
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },
  data() {
    return {
      date: new Date(),
      dDay: 'DAY',
      rankList: [],
      rankPage: 0,
      memberGroups: null,
      memberGroupPage: 0,
      memberGroupPre: false,
      memberGroupNext: false,
      member: null,
      dataReady: false,
      goal: '목표까지',
      promise: '목표는 최대 30자까지 설정 가능합니다.',
      showChangePromise: false,
      showChangeDDay: false,
      targetStudyTime: this.epoch_to_hour_min(0),
      nowStudyTime: 0+'시간 ' + 0 + '분',
      showTargetTime: false,
      targetTime: null,
      studyProgress: 0,
      memberInfo: null,
      nowStudyTimeOfDateTime: 0,
      targetDay: moment(new Date()).format().substring(0, 10),
      groupPage: 0,
      groups: null,
      groupsData: null,

    }
  },
  watch: {
    date() {
      this.findDDay()
      this.targetDay = moment(this.date).format().substring(0, 10)
    },
    studyProgress() {
      const targetTime = this.targetTime.getHours()*3600+this.targetTime.getMinutes()*60;
      console.log('studyProgress' , this.targetTime)
      if(this.nowStudyTimeOfDateTime-targetTime>0) {
        // const number = 100
        this.$refs.bar.setAttribute('style', 'width: '+this.studyProgress+'%')
      }
      else {
        // const number = 100-(targetTime-this.nowStudyTimeOfDateTime)/targetTime*100;
        this.$refs.bar.setAttribute('style', 'width: '+this.studyProgress+'%')
      }
    }
  },
  mounted() {
    this.getRank('DAY');


    if(this.tokenStore.getToken != null) {
      this.getMemberGroups(0);
      this.getMember()
      this.getMemberTime();
    }
    else {
      this.getGroups()
    }


  },
  methods: {
    getRank(period) {


      switch (period) {
        case 'DAY':
          this.$refs.day.className = 'nav-link active'
          this.$refs.week.className = 'nav-link'
          this.$refs.month.className = 'nav-link'
          break;
        case 'WEEK':
          this.$refs.day.className = 'nav-link'
          this.$refs.week.className = 'nav-link active'
          this.$refs.month.className = 'nav-link'
          break;
        case 'MONTH':
          this.$refs.day.className = 'nav-link'
          this.$refs.week.className = 'nav-link'
          this.$refs.month.className = 'nav-link active'
          break;
      }

      this.rankList = []
      axios.get('http://localhost:8000/record-service/records/ranked', {
        params: {
          period: period,
          date: this.getDate(),
          studyStatus: 'STUDY',
          page: 0, size: 10,
        }
      })
      .then(response => {

        const result = response.data;
        this.rankList = result.data;
      })
    },
    getDate() {
      const date = new Date();
      const year = date.getFullYear();
      let month = date.getMonth()+1;
      month = month<10 ? '0'+month : month;
      let day = date.getDate();
      day = day<10 ? '0' +day : day

      return year + '-' + month + '-' + day
    },
    getMemberGroups(i) {
      this.memberGroupPage = this.memberGroupPage+i;
      axios.get("http://localhost:8000/group-service/groups", {
        params: {
          memberId: this.tokenStore.getMemberId,
          page: this.memberGroupPage,
          size: 4,
        }
      })
      .then(response => {
        const result = response.data;
        this.memberGroups = result.data
        this.memberGroupPre = result.previous;
        this.memberGroupNext = result.next;

      })
    },
    findDDay(dDay) {
      const date = new Date(dDay);

      const bet = date.getTime()-Date.now();


      return Math.ceil(bet/(1000*60*60*24));

    },
    getMember() {
      axios.get('http://localhost:8000/member-service/members/'+this.tokenStore.getMemberId)
      .then(response => {
        this.member = response.data;
        this.dataReady = true;
        const memberInfo = this.member.memberInfo;
        this.memberInfo = memberInfo;

        this.goal = memberInfo.goal;
        this.promise = memberInfo.promise;
        this.dDay = memberInfo.dday;
        this.date = new Date(this.dDay)
        this.dDay = this.findDDay(this.dDay)

        this.targetTime = new Date('1970-01-01')
        this.targetTime.setHours(0);
        this.targetTime.setSeconds(memberInfo.targetTime);

        this.targetStudyTime = this.epoch_to_hour_min(memberInfo.targetTime)
        if(this.dDay > 0) {
          this.dDay = '-' + this.dDay
        }

        if(this.nowStudyTimeOfDateTime !== null) {
          const targetTime = this.targetTime.getHours()*3600+this.targetTime.getMinutes()*60;

          if(this.nowStudyTimeOfDateTime-targetTime>0) {
            const number = 100
            this.$refs.bar.setAttribute('style', 'width: '+number+'%')
            this.studyProgress = 100
          }
          else {
            const number = 100-(targetTime-this.nowStudyTimeOfDateTime)/targetTime*100;
            this.$refs.bar.setAttribute('style', 'width: '+number+'%')
            this.studyProgress = 100-(targetTime-this.nowStudyTimeOfDateTime)/targetTime*100
          }
        }


      })
    },
    changePromise() {
      this.showChangePromise = false;
    },
    ChangeDDay() {
      this.showChangeDDay = false
      const date = this.date

      const year = date.getFullYear();
      let month = date.getMonth()+1;
      month = month<10 ? '0'+month : month;
      let day = date.getDate();
      day = day<10 ? '0' +day : day

      this.dDay = this.findDDay(year+'-'+month+'-'+(day))

      if(this.dDay > 0) {
        this.dDay = '-' + this.dDay
      }


      let data = {
        goal: this.goal,
        dDay: year+ '-' +month + '-' + day,
        memberId: this.tokenStore.getMemberId
      }


      axios.post('http://localhost:8000/member-service/member-info/'+this.tokenStore.getMemberId, data)
      .then(() => {

      })

    },
    epoch_to_hour_min(epoch) {
      let h = Math.floor(epoch / 3600);
      let m = Math.floor(epoch % 3600 / 60);
      // const date = new Date(epoch*1000);
      // const hour = date.getHours();
      // const min = date.getMinutes();
      return h + '시간 ' + m + '분';
    },
    getMemberTime() {
      axios.get('http://localhost:8000/record-service/records/ranked', {
        params: {
          period: 'DAY',
          date: this.getDate(),
          studyStatus: 'STUDY',
          page: 0, size: 1,
          memberId: this.tokenStore.getMemberId,
        }
      })
      .then(response => {
        const data = response.data.data;
        this.nowStudyTime = this.epoch_to_hour_min(data[0].time)
        this.nowStudyTimeOfDateTime = data[0].time

        if(this.targetTime !== null) {
          const targetTime = this.targetTime.getHours()*3600+this.targetTime.getMinutes()*60;

          if(data[0].time-targetTime>0) {
            const number = 100
            this.$refs.bar.setAttribute('style', 'width: '+number+'%')
            this.studyProgress = 100
          }
          else {
            const number = 100-(targetTime-data[0].time)/targetTime*100;
            this.$refs.bar.setAttribute('style', 'width: '+number+'%')
            this.studyProgress = 100-(targetTime-data[0].time)/targetTime*100
          }
        }
      })
    },
    changeTargetTime() {
      const hours = this.targetTime.getHours();
      const min = this.targetTime.getMinutes();

      this.targetStudyTime = hours + '시간 ' + min + '분'


      const date = new Date('1970-01-01')
      date.setHours(0)

      const targetTime = this.targetTime.getTime()-date.getTime();

      let data = {
        targetTime: targetTime/1000
      }


      const time = this.targetTime.getHours()*3600+this.targetTime.getMinutes()*60;

      if(this.nowStudyTimeOfDateTime-time>0) {
        // const number = 100
        this.studyProgress = 100
      }
      else {
        // this.studyProgress = 100 - (time - this.nowStudyTimeOfDateTime) / time * 100;
      }

      axios.post('http://localhost:8000/member-service/member-info/'+this.tokenStore.getMemberId, data)
      .then(() => {
        this.targetStudyTime = hours + '시간 ' + min + '분'
        this.$router.go()
      })
      .catch(() => {
        alert("잠시 후에 다시 시도해주세요.")
      })

      this.showTargetTime = false

    },
    getGroups() {
      axios.get('http://localhost:8000/group-service/groups', {
        params: {
          order: 'attend',
          page: this.groupPage,
          size: 4
        }
      })
      .then(response => {
        this.groupsData = response.data;
        this.groups = response.data.data;
        console.log(this.groupsData)
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
li {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
button {
  z-index: 1;
}
.MyContainer {
  width: 1100px;
}
.background {
  width: 100%;
  border-bottom: 2px solid #E9ECEF;
}
@media (max-width: 1100px) {
  .background {
    width: 1100px;
  }
}
.grid-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
}
.group-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
}
</style>