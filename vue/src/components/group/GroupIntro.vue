<template>
  <div class="d-flex flex-column gap-2">
  <div class="group-intro-page gap-2">
    <div class="group-intro border rounded shadow-sm p-3 item" v-if="!introWrite">
      <div class="d-flex justify-content-between">
        <h2>그룹 소개</h2>
        <button class="btn btn-sm btn-outline-secondary" @click="changeIntro" v-if="group.memberId===tokenStore.getMemberId">수정</button>
      </div>
      <span v-html="replaceBrTag(intro)"></span>
    </div>
    <div class="group-intro border rounded shadow-sm p-3 item" v-if="introWrite">
      <div class="d-flex justify-content-between">
        <h2>그룹 소개</h2>
        <button class="btn btn-sm btn-outline-secondary" @click="updateIntro">완료</button>
      </div>
      <textarea class="form-control mt-2" rows="15" v-model="intro"/>
    </div>
    <div class="group-intro-info border rounded shadow-sm p-3 item">
      <div class="col">
        <h3>인원</h3>
        <span class="fs-4">{{group.members.length}}명/28명</span>
      </div>
      <div class="col">
        <h3>출석율</h3>
        <span class="fs-4">{{this.attendance}}%</span>
      </div>
      <div class="col">
        <h3>그룹장</h3>
        <span class="fs-5">{{bossName}}</span>
      </div>
    </div>
    <div class="border rounded shadow-sm p-3 item d-flex flex-column">
      <div class="col">
        <study-room-modal class="d-flex flex-column" v-if="showModal">
          <div class="d-flex justify-content-between align-items-center mt-2">
          <h5>목표 추가</h5>
          <button type="button" class="btn btn-sm btn-outline-secondary" @click="closeModal">닫기</button>
          </div>
          <div>
            <div class="d-flex">
              <button v-for="purpose in groupPurposeList" :key="purpose.purposeId"
                      class="btn btn-sm btn-tag" v-html="'#'+purpose.content"/>
            </div>
<!--            <div class="d-flex">-->
<!--              <input type="text" class="mt-2 form-control" v-model="purpose"/>-->
<!--              <button type="button" class="btn btn-sm btn-outline-secondary col-2" @click="findPurpose">검색</button>-->
<!--            </div>-->
            <div class="mt-2 d-flex gap-1 flex-column" v-if="dataReady">
              <span>일간 출석</span>
              <div class="d-flex flex-wrap gap-2">
              <button v-for="purpose in dailyAttendPurposes" :key="purpose.purposeId"
                      class="btn btn-sm btn-tag" v-html="'#'+purpose.content"
                      @click="addPurpose(purpose)"/>
              </div>
            </div>
            <div class="mt-2 d-flex gap-1 flex-column" v-if="dataReady">
              <span>주간 출석</span>
              <div class="d-flex flex-wrap gap-2">
              <button v-for="purpose in weeklyAttendPurposes" :key="purpose.purposeId"
                      class="btn btn-sm btn-tag" v-html="'#'+purpose.content"
                      @click="addPurpose(purpose)"/>
              </div>
            </div>
            <div class="mt-2 d-flex gap-1 flex-column" v-if="dataReady">
              <span>일간 목표</span>
              <div class="d-flex flex-wrap gap-2">
              <button v-for="purpose in dailyPurposes" :key="purpose.purposeId"
                      class="btn btn-sm btn-tag" v-html="'#'+purpose.content"
                      @click="addPurpose(purpose)"/>
              </div>
            </div>
            <div class="mt-2 d-flex gap-1 flex-column" v-if="dataReady">
              <span>주간 목표</span>
              <div class="d-flex flex-wrap gap-2">
              <button v-for="purpose in weeklyPurposes" :key="purpose.purposeId"
                      class="btn btn-sm btn-tag" v-html="'#'+purpose.content"
                      @click="addPurpose(purpose)"/>
              </div>
            </div>
          </div>
        </study-room-modal>
        <button class="float-end btn btn-sm btn-outline-secondary" @click="showModal = !showModal" v-if="group.memberId===tokenStore.getMemberId">수정</button>
        <h3>그룹 목표</h3>
        <div class="d-flex gap-2">
          <button v-for="purpose in group.purposes" :key="purpose.purposeId"
                  class="btn btn-sm btn-tag" v-html="'#'+purpose.content"/>
          <button class="btn btn-sm btn-tag" v-if="!dataReady">#하루 3시간</button>
        </div>
      </div>
      <div class="d-flex">
      <div class="col">
        <h3>공부</h3>
        <span class="fs-4">{{ group.study.name }}</span>
      </div>
      <div class="col">
        <h3>분류</h3>
        <span class="fs-4">{{ groupCategory }}</span>
      </div>
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
  name: "GroupIntro",
  components: {
    StudyRoomModal,
  },
  data() {
    return {
      intro: '이것은 소개입니다.\n보고있나요.',
      introWrite: false,
      showModal: false,
      purpose: null,
      purposeList: [],
      groupPurposeList: [],
      dataReady: false,
      attendance: null,
      weeklyAttendPurposes: [],
      dailyAttendPurposes: [],
      dailyPurposes: [],
      weeklyPurposes: [],
      bossName: null,
      groupCategory: null,
    }
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore,

    }
  },
  props: ['group'],
  mounted() {
    this.intro = this.group.intro
    this.attendance = this.findAttendance();
    this.getPurposeList()
    console.log(this.group)
    const members = this.group.members;
    const bossList = members.filter(member => member.memberId===this.group.memberId);
    this.bossName = bossList[0].memberName
    this.groupCategory = this.groupCategorySwitch(this.group.groupCategory)
  },
  watch: {

  },
  methods: {
    groupCategorySwitch(category) {
      switch (category) {
        case 'HIGH':
          return '고등학생'
        case 'UNIV':
          return '대학생'
        case 'CERT':
          return '자격증'
        case 'LANG':
          return '어학'
        case 'GOV':
          return '공무원'
        case 'JOB':
          return '취업'
        case 'ETC':
          return '기타'
      }
    },
    classifyPurpose(str) {
      let list = []
      for(const purpose of this.purposeList) {
        if(purpose.purposeCategory===str)
          list.push(purpose);
      }

      return list;
    },
    addPurpose(purpose) {
      const data = {
        groupPurposeId: purpose.purposeId,
        content: purpose.content
      }
      axios.post('http://localhost:8000/group-service/groups/'+this.group.groupId+'/purposes', data)
      .then(() => {
        this.groupPurposeList.push(purpose)
      })
    },
    findAttendance() {
      const time = Date.now();
      const now = new Date(time);
      const month1 = now.getMonth();
      const date1 = now.getDate();
      const nowHours = now.getHours();
      const membersSize = this.group.members.length;
      const attendedMember = []

      this.group.members.forEach(member => {
        const attend = new Date(member.attend * 1000);
        const month2 = attend.getMonth();
        const date2 = attend.getDate();
        const attendHours = attend.getHours();

        if (nowHours < 5 && attendHours < 5) {
          if (month1 === month2 && date1 === date2)
            attendedMember.push(member)
        } else if (nowHours < 5 && attendHours >= 5) {
          if (month1 === month2 && date1 - 1 === date2)
            attendedMember.push(member)
        } else if (nowHours >= 5 && attendHours >= 5) {
          if (month1 === month2 && date1 === date2)
            attendedMember.push(member)
        }
      })

      return Math.floor(attendedMember.length / membersSize * 100);

    },
    findPurpose() {
      axios.get('http://localhost:8000/group-service/purposes', {
      })
      .then(response => {
        const result = response.data;
        const data = result.data;
        this.purposeList = [];
        this.purposeList = data;
      })
    },
    async getPurposeList() {
      await axios.get('http://localhost:8000/group-service/purposes')
      .then((response) => {
        const result = response.data;
        this.purposeList = result.data
        this.weeklyPurposes = this.classifyPurpose('WEEK')
        this.dailyPurposes = this.classifyPurpose('DAY')
        this.dailyAttendPurposes = this.classifyPurpose('DAYATTEND')
        this.weeklyAttendPurposes = this.classifyPurpose('WEEKATTEND')
        this.dataReady = true;
      })

    },
    replaceBrTag(str) {
      if (str === undefined || str == null)
      {
        return "";
      }

      str = str.replace(/\r\n/ig, '<br>');
      str = str.replace(/\\n/ig, '<br>');
      str = str.replace(/\n/ig, '<br>');
      return str;
    },
    changeIntro() {
      this.introWrite = !this.introWrite
    },
    updateIntro() {
      this.introWrite = false;

      let data = {
        intro: this.intro,
      }


      axios.post('http://localhost:8000/group-service/groups/'+this.group.groupId, data)
    },
    closeModal() {
      this.showModal = false;
    },


  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500;700&display=swap');
h2 {
  color: #8F9D9D;
}
h3 {
  color: #8F9D9D;
}
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
.group-intro-page {
  display: grid;
  grid-template-columns: 1fr 1fr;
}
.group-intro {
  height: 450px;
}
.item:nth-child(1) {
  grid-row: 1 / span 2;
}
.group-intro-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
}
.btn-tag {
  background-color: #EFF3FA;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
</style>