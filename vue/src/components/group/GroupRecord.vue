<template>
  <div class="d-flex p-2">
    <div class="col border border-1 rounded bg-origin shadow-sm">
      <div class="Chart p-2 pt-4 pb-4 pe-3">
        <line-bar-chart :study-data="studyData" :from="'group'" :members="group.members" :date="date" :data-type="dataType" v-if="studyData[0]"/>
      </div>
    </div>
  </div>
  <div class="p-2 gap-2">
      <group-ranking :group="group" :members="group.members" v-if="dataReady" />
  </div>
</template>

<script>
import axios from "axios";
import LineBarChart from "@/components/record/LineBarChart";
import GroupRanking from "@/components/group/GroupRanking";
import {useTokenStore} from "@/store/token";
import {useGroupStore} from "@/store/group";

export default {
  name: "GroupRecord",
  components: {
    LineBarChart,
    GroupRanking
  },
  props: ['group']
  ,
  setup() {
    const tokenStore = useTokenStore();
    const groupStore = useGroupStore();
    return {
      tokenStore,
      groupStore
    }
  },
  data() {
    return {
      memberRecords: null,
      studyRecords: null,
      studyData: [null, null],
      date: this.getDate(),
      dataType: 'MONTH',
      dataReady: false
    }
  },
  mounted() {
    console.log(this.group)
    const isMember = this.group.members.filter(member => member.memberId===this.tokenStore.getMemberId);
    if(isMember.length===0){
      alert('그룹 멤버만 이용 가능합니다.')
      this.groupStore.changeCategory('INTRO')
    }
    this.getData()
    this.dataReady = true;
  },
  methods: {
    getDate() {
      const date = new Date();
      const year = date.getFullYear();
      let month = date.getMonth()+1;
      month = month<10 ? '0'+month : month;
      let day = date.getDate();
      day = day<10 ? '0' +day : day

      return year + '-' + month + '-' + day
    },
    async getData() {
      const member = await axios.get('http://localhost:8000/record-service/records', {
        params: {
          memberId: this.tokenStore.getMemberId,
          studyId: this.group.study.studyId,
          date: this.getDate(),
          period: 'MONTH'
        }
      });

      let memberRecords = member.data

      const study = await axios.get('http://localhost:8000/record-service/records', {
        params: {
          groupId: this.group.groupId,
          date: this.getDate(),
          period: 'MONTH'
        }
      });

      let studyRecords = study.data

      this.studyData[0] = memberRecords
      this.studyData[1] = studyRecords

    }
  }
}
</script>

<style scoped>
.records {
  display: grid;
  grid-template-columns: 1fr 1fr;
}
</style>