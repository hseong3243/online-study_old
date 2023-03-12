<template>
  <div class="d-flex flex-column">
    <div>
      <div class="d-flex align-items-center gap-2">
      <h5 class="ps-2">내 그룹 공부방</h5>
      <button type="button" class="btn btn-sm btn-secondary" @click="groupRoomPre" v-if="memberGroupsData && memberGroupsData.previous">이전</button>
      <button type="button" class="btn btn-sm btn-outline-secondary disabled"  v-if="memberGroupsData && !memberGroupsData.previous">이전</button>
      <button type="button" class="btn btn-sm btn-secondary" @click="groupRoomNext" v-if="memberGroupsData && memberGroupsData.next">다음</button>
      <button type="button" class="btn btn-sm btn-outline-secondary disabled" v-if="memberGroupsData && !memberGroupsData.next">다음</button>
      </div>
      <div class="p-2 gap-3 grid-container">
        <div v-for="room in memberGroups" :key="room.roomId">
          <group-study-room-info :room="room" :group-study-map="groupStudyMap"/>
        </div>
    </div>
    </div>
    <div class="mt-2">
      <h5 class="ps-2">그룹 공부방</h5>
    <div class="p-2 gap-3 grid-container">
      <div v-for="room in rooms" :key="room.roomId">
        <group-study-room-info :room="room" :group-study-map="studyMap"/>
      </div>
    </div>
    </div>
    <div class="d-flex justify-content-center mt-5">
      <hr v-if="this.rooms && hasNext">
      <button type="button" class="btn btn-sm btn-secondary" style="z-index: 1" @click="nextPage" v-if="this.rooms && hasNext">더보기</button>
      <p style="z-index: 1" v-if="this.rooms && !hasNext">마지막 페이지입니다.</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {useTokenStore} from "@/store/token";
import GroupStudyRoomInfo from "@/components/studyroom/GroupStudyRoomInfo";

export default {
  name: "StudyGroupRoomList",
  components: {GroupStudyRoomInfo},
  data() {
    return {
      hasNext: null,
      hasPre: null,
      rooms: null,
      page: 0,
      memberGroupsData: null,
      memberGroups: [],
      studyMap: null,
      groupStudyMap: null,
      groupPage: 0,
    }
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },
  mounted() {
    axios.get('http://localhost:8000/study-service/rooms/groups', {
      params: {
        page: this.page,
        size: 10,
        members: 28,
      }
    })
    .then(response => {
      const result = response.data;
      this.rooms = result.data
      this.studyMap = new Map();
      for(const room of this.rooms){
        this.studyMap.set(room.groupId, room.groupDto.study)
      }
      console.log(this.studyMap)
      this.hasPre = result.hasPrevious;
      this.hasNext = result.hasNext;
      console.log(result)
    })

    this.getMemberGroups()

  },
  methods: {
    nextPage() {
      this.page++;
      axios.get('http://localhost:8000/study-service/rooms/groups', {
        params: {
          page: this.page,
          size: 10
        }
      })
          .then(response => {
            const result = response.data;
            this.rooms = this.rooms.concat(result.data);
            for(const room of result.data){
              this.studyMap.set(room.groupId, room.groupDto.study)
            }

            this.hasPre = result.hasPrevious;
            this.hasNext = result.hasNext;
          })
    },
    getMemberGroups() {
      axios.get('http://localhost:8000/group-service/groups', {
        params: {
          memberId: this.tokenStore.getMemberId,
          page: this.groupPage,
          size: 3
        }
      })
      .then(response => {
        this.memberGroupsData = response.data;
        const groups = response.data.data;

        console.log(groups)

        const roomsData = groups.map(group => group.rooms);

        this.memberGroups = [];
        for(const rooms of roomsData) {
          if(rooms[0].count >= rooms[1].count && rooms[0].count<28)
            this.memberGroups.push(rooms[0])
          else
            this.memberGroups.push(rooms[1]);
        }

        this.groupStudyMap = new Map();
        for(const group of groups) {
          this.groupStudyMap.set(group.groupId, group.study);
        }

      })
    },
    groupRoomPre() {
      this.groupPage--;
      this.getMemberGroups()
    },
    groupRoomNext() {
      this.groupPage++;
      this.getMemberGroups()
    },
    getUrl() {
      return "/studyrooms/"+this.room.roomId
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: "Noto Sans KR", sans-serif;
  font-weight: 400;
}
h5 {
  font-family: "Noto Sans KR", sans-serif;
  font-weight: 400;
}
button[name=tag] {
  font-size: 12px;
}
.room-card {
  border: 2px solid #D9D9D9;
  border-radius: 10px;
}
.room-card:hover {
  background: #D9D9D9;
}
.grid-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}
.text-title {

}
</style>