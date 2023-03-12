<template>
  <div class="MyContainer m-auto d-flex justify-content-center">
    <div class="col-2">
      <div class="d-flex justify-content-between p-1">
        <a class="btn btn-sm btn-outline-secondary" :href="'/groups'" >그룹 목록</a>
        <button class="btn btn-sm btn-outline-secondary" v-if="dataReady && !isMember" @click="joinGroup">그룹 가입</button>
      </div>
      <div class="d-flex flex-column p-3 gap-2">
        <span class="category" @click="groupStore.changeCategory('INTRO')" ref="intro">그룹 소개</span>
        <span class="category" @click="groupStore.changeCategory('COMMUNITY')">커뮤니티</span>
        <span class="category" @click="groupStore.changeCategory('STUDY')">공부방</span>
        <span class="category" @click="groupStore.changeCategory('RECORD')">출석부</span>
      </div>
      <div class="room-list" v-if="groupStore.getCategory === 'STUDY'">
        <div v-for="room in rooms" :key="room.roomId">
          <button class="btn align-items-start room-card d-flex flex-column" @click="setRoomId(room)">
            <span>{{ room.name }}</span>
            <span>{{room.ticketDtos.length}} / 28</span>
          </button>
        </div>
      </div>
    </div>
    <div class="col-10 p-3">
      <group-intro :group="group" v-if="groupStore.getCategory === 'INTRO' && dataReady"/>
      <group-community :group="group" :group-id="groupId" v-if="dataReady && groupStore.getCategory === 'COMMUNITY'"/>
      <study-room :id="roomId" :group-study="group.study" v-if="roomId && groupStore.getCategory === 'STUDY'"/>
      <group-record :group="group" v-if="dataReady && groupStore.getCategory === 'RECORD'"/>
    </div>

  </div>
</template>

<script>
import GroupCommunity from "@/components/group/GroupCommunity";
import StudyRoom from "@/components/StudyRoom";
import GroupRecord from "@/components/group/GroupRecord";
import GroupIntro from "@/components/group/GroupIntro";
import axios from "axios";
import {useGroupStore} from "@/store/group";
import {useTokenStore} from "@/store/token";


export default {
  name: "GroupDetail",
  data() {
    return{
      tap: 'INTRO',
      rooms: null,
      group: null,
      dataReady: false,
      roomId: null,
      isMember: false,
    }
  },
  props: ['groupId'],
  components: {
    GroupIntro,
    GroupCommunity,
    StudyRoom,
    GroupRecord
  },
  mounted() {
    this.getGroup(this.groupId).then((data) => {
      this.group=data
      console.log(this.group)
      this.rooms=this.group.rooms
      this.roomId = this.group.rooms[0].roomId
      this.isMember = this.isMemberCheck()

      this.dataReady = true;

      this.groupStore.changeTitle(this.group.name)
    })



  },
  setup() {
    const groupStore = useGroupStore();
    const tokenStore = useTokenStore();
    return {
      groupStore,
      tokenStore
    }
  },
  methods: {
    isMemberCheck() {
      const memberId = this.tokenStore.getMemberId;
      const members = this.group.members;
      const isMember = members.filter(member => member.memberId===memberId);

      console.log(isMember.length)

      return isMember.length !== 0;
    },
    setRoomId(room) {
      this.roomId = room.roomId
    },
    getStudyRooms() {
      axios.get('http://localhost:8000/study-service/rooms', {
        params: {
          groupId: this.group.groupId
        }
      })
      .then(response => {
        this.rooms = response.data
      })
    },
    changeTap(str) {
      this.tap = str;
    },
    async getGroup(groupId) {
      const response = await axios.get('http://localhost:8000/group-service/groups/' + groupId);

      return response.data;
    },
    joinGroup() {
      axios.post('http://localhost:8000/group-service/groups/'+this.groupId+'/members')
      .then(() => {
        alert(this.group.name+' 그룹에 가입하였습니다.')
        this.$router.go()
      })
      .catch(() => {
        alert('잠시 후에 다시 시도해주세요.')
      })
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500;700&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
  font-size: 17px;
}
.MyContainer {
  width: 1100px;
}
.category:hover {
  cursor: pointer;
}
.room-list {
  margin-top: 20px;
  display: grid;
  grid-template-columns: 1fr;
  padding: 5px;
  gap: 10px;
}
.room-card {
  border: 2px solid #D9D9D9;
  border-radius: 10px;
}
.room-card:hover {
  background: #D9D9D9;
}
</style>