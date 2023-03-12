<template>
  <td class="text">{{ memberName }}</td>
  <td class="">{{attend}}</td>
  <td class="">
    <div class="progress">
      <span class="position-absolute bar">{{studyTime}}</span>
      <div class="progress-bar bg-warning" role="progressbar" ref="bar" style="width: 50%"></div>
    </div>
  </td>
</template>

<script>
import axios from "axios";

export default {
  name: "GroupMemberBar"
  ,props:['member', 'group'],
  data() {
    return {
      attend: null,
      studyTime: null,
      memberName: null,
    }
  },
  mounted() {
    this.getAttend()
    // if(this.attend.includes('일'))
      this.$refs.bar.style.width= 0

    this.getStudyTime()

    this.memberName= this.member.memberName
    if(this.memberName.length >6) {
      const slice = this.memberName.slice(0, 5);
      this.memberName = slice + '...'
    }
  },
  methods: {
    getAttend() {
      const attend = this.member.attend;
      const date = new Date(0);
      date.setSeconds(attend)
      const number = Date.now();
      const nowDate = new Date(number);

      if(nowDate.getHours()<5) {
        if(date.getHours()<5 && date.getDate()===nowDate.getDate())
          this.attend=  date.getHours() + '시'
      }
      else {
        if(date.getHours()>5 && date.getDate()===nowDate.getDate())
          this.attend = date.getHours()+'시'
      }
      if(this.attend == null)
      this.attend = '' + nowDate.getDate() - date.getDate() + '일 전'
    },
    getStudyTime() {
      if(this.attend.includes('일'))
        this.studyTime = '00:00:00'
      else {
        axios.get('http://localhost:8000/record-service/records/ranked', {
          params: {
            period: 'DAY',
            date: this.getDate(),
            studyStatus: 'STUDY',
            page: 0, size: 28,
            memberId: this.member.memberId,
            studyId: this.group.study.studyId,
          }
        })
            .then(response => {
              const result = response.data;
              const data = result.data[0];
              const studyTime = data.time;
              const date = new Date(0);
              date.setSeconds(studyTime)

              let hours = date.getHours() - 9;
              let min = date.getMinutes();
              min = min<10 ? '0'+min : min;
              let sec = date.getSeconds();
              sec = sec<10 ? '0'+sec : sec;

              this.studyTime =  hours + ':' + min + ':' + sec
            })
      }
    },
    getDate() {
      const number = Date.now();
      const date = new Date(number);

      return date.getFullYear() + '-' + (date.getMonth()+1) + '-' + date.getDate();
    }
  }
}
</script>

<style scoped>
.bar {
  padding-left: 18px;
}
</style>