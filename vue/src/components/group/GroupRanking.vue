<template>
  <div class="col d-flex border border-1 rounded bg-origin shadow-sm">
    <div class="col">
    <table class="table">
      <thead>
      <tr>
        <th scope="col" class="col-5">이름</th>
        <th scope="col" class="col-3">접속</th>
        <th scope="col" class="col-4">시간</th>
      </tr>
      </thead>
      <tbody>
        <tr v-for="member in memberList1" :key="member.memberId">
          <group-member-bar :group="group" :member="member"/>
        </tr>
      </tbody>
    </table>
    </div>
    <div class="col">
    <table class="table">
      <thead>
      <tr>
        <th scope="col" class="col-5">이름</th>
        <th scope="col" class="col-3">접속</th>
        <th scope="col" class="col-4">시간</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="member in memberList2" :key="member.memberId">
        <group-member-bar :group="group" :member="member"/>
      </tr>
      </tbody>
    </table>
    </div>
    <div class="col">
    <table class="table">
      <thead>
      <tr>
        <th scope="col" class="col-5">이름</th>
        <th scope="col" class="col-3">접속</th>
        <th scope="col" class="col-4">시간</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="member in memberList3" :key="member.memberId">
        <group-member-bar :group="group" :member="member"/>
      </tr>
      </tbody>
    </table>
      <div class="d-flex p-3 justify-content-center">
        <nav aria-label="...">
          <ul class="pagination pagination-sm">
            <li class="page-item" aria-current="page" @click="page=1">
              <span class="page-link">1</span>
            </li>
            <li class="page-item"><a class="page-link" href="#" @click="page=2" v-if="hasNextPage">2</a></li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import GroupMemberBar from "@/components/group/GroupMemberBar";

export default {
  name: "GroupRanking",
  props: ['members', 'group'],
  components: {
    GroupMemberBar
  },
  data() {
    return {
      groupMember: null,
      memberList1: null,
      memberList2: null,
      memberList3: null,
      hasNextPage: false,
      page: 1,
    }
  },
  watch: {
    page() {
      if(this.page===1) {
        if(this.members.length<10){
          this.memberList1 = this.members.slice(0, this.members.length)
        }
        else{
          if(this.members.length<20) {
            this.memberList1 = this.members.slice(0, 10);
            this.memberList2 = this.members.slice(10,this.members.length)
          }
          else {
            this.memberList1 = this.members.slice(0, 10);
            this.memberList1 = this.members.slice(10, 20);
            this.memberList2 = this.members.slice(20,this.members.length)
          }
        }
      }else if(this.page===2) {
        if(this.members.length<38){
          this.memberList1 = this.members.slice(28, this.members.length)
        }
        else{
          if(this.members.length<48) {
            this.memberList1 = this.members.slice(28, 38);
            this.memberList2 = this.members.slice(38,this.members.length)
          }
          else {
            this.memberList1 = this.members.slice(28, 38);
            this.memberList1 = this.members.slice(38, 48);
            this.memberList2 = this.members.slice(48,this.members.length)
          }
        }
      }
    }
  },
  mounted() {
    this.groupMember = this.members
    if(this.members.length<10){
      this.memberList1 = this.members.slice(0, this.members.length)
    }
    else{
      if(this.members.length<20) {
        this.memberList1 = this.members.slice(0, 10);
        this.memberList2 = this.members.slice(10,this.members.length)
      }
      else {
        this.memberList1 = this.members.slice(0, 10);
        this.memberList1 = this.members.slice(10, 20);
        this.memberList2 = this.members.slice(20,this.members.length)
      }
    }
    if(this.members.length>28) {
      this.hasNextPage = true;
    }
    console.log(this.groupMember)


  },
  methods: {
    getMemberStudyTime(member) {
      const str = this.getAttend(member);
      if(str.includes('일'))
        return '00:00:00'
      else {
        axios.get('http://localhost:8000/record-service/records/ranked', {
          params: {
            period: 'DAY',
            date: this.getDate(),
            studyStatus: 'STUDY',
            page: 0, size: 28,
            memberId: member.memberId,
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

              return hours + ':' + min + ':' + sec
            })
        .catch(() => {
          return '01:12:11'
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
td {
  font-size: 14px;
}
</style>