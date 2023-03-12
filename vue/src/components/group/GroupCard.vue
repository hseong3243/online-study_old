<template>
  <router-link :to="`/groups/${this.group.groupId}`" class="btn room-card d-flex flex-column p-3"
               @mouseover="mouseOverEvent" @mouseleave="mouseLeave">
    <div class="d-flex justify-content-between">
      <span ref="name" class="fs-4">그룹 이름은 이렇게 짓는 것이다</span>
      <span ref="size" class="fs-5">0 / 000</span>
    </div>
    <div class="d-flex">
      <span class="text-secondary">공부&nbsp;</span>
      <span ref="study">테스트스터디</span>
    </div>
    <div class="d-flex">
      <div class="d-flex col">
      <span class="text-secondary">출석율&nbsp;</span>
      <span ref="attend"></span>
      </div>
      <div class="d-flex col">
      <span class="text-secondary">분류&nbsp;</span>
      <span>{{groupCategory}}</span>
      </div>
    </div>
    <div class="d-flex gap-1 pt-2 flex-wrap">
      <button class="btn btn-tag btn-outline-secondary rounded-4" name="tag"
              v-for="purpose in group.purposes" :key="purpose.purposeId"
              v-html="'#'+purpose.content"></button>
    </div>
<!--    <div class="intro-card" v-if="showIntro">-->
<!--      <span v-html="replaceBrTag(group.intro)"></span>-->
<!--    </div>-->
  </router-link>
</template>

<script>
import {useGroupStore} from "@/store/group";

export default {
  name: "GroupCard",
  props: ['group'],
  setup() {
    const groupStore = useGroupStore();
    return {
      groupStore
    }
  },
  data() {
    return {
      showIntro: false,
      groupCategory: 'ALL'
    }
  },
  mounted() {

    this.$refs.name.textContent = this.group.name
    let count;
    count = this.group.level*56;
    this.$refs.size.textContent = this.group.members.length + '/' + (count)
    if(this.group.studyId === null)
      this.$refs.study.textContent = '자유'
    else
      this.$refs.study.textContent = this.group.study.name

    this.$refs.attend.textContent = this.findAttendance()+'%'
    this.groupCategory = this.groupCategorySwitch(this.group.groupCategory)

  }
  ,methods: {
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
    mouseOverEvent() {
      this.showIntro=true;
    },
    mouseLeave() {
      this.showIntro=false;
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

  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
button {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
.room-card {
  width: 280px;
  height: 160px;
}
.intro-card {
  color: black;
  text-align: left;
}
.btn-tag {
  font-size: 12px;
  padding: 5px 5px;
}
</style>