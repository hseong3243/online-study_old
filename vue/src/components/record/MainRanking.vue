<template>
  <tr>
    <td class="col-2 name">{{ rank.rank }}위</td>
    <td class="col-5 name">{{rank.memberName}}</td>
    <td class="col-2">{{ time }}</td>
    <td class="col-3">{{category}}</td>
  </tr>
</template>

<script>
export default {
  name: "MainRanking",
  props: ['rank'],
  data() {
    return {
      time: null,
      category: null,
    }
  },
  mounted() {
    this.time = this.getTimeStringHMS(this.rank.time);
    this.category = this.groupCategorySwitch(this.rank.studyCategory)
  },
  methods: {
    getTimeStringHMS(time) {
      // const date = new Date(1970, 0, 1);
      let hours = Math.floor(time / 3600);
      let minutes = Math.floor(time / 60 - hours*60)
      let seconds = Math.floor(time%60)
      hours = hours<10 ? '0'+hours : hours;
      minutes = minutes<10 ? '0'+minutes : minutes;
      seconds = seconds<10 ? '0'+seconds : seconds;
      return hours + ":" + minutes + ":" + seconds;
    },
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
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
td {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
.name {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
</style>