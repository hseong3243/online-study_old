<template>
  <div>
  <div id="study-room-list" class="background bg-bread">
    <div class="MyContainer m-auto p-2 d-flex justify-content-between">
      <div class="d-flex flex-column">
        <span class="SmallTitle fs-3">공부방</span>
        <span class="fs-5">목록</span>
      </div>
      <img class="float-end" :src="require('@/assets/img/desk_icon.png')" alt="test">
    </div>
  </div>
  <div class="MyContainer m-auto d-flex align-items-start">
    <div class="d-flex flex-column col-3 pt-3 gap-2 ps-2" id="list">
      <h5 class="ps-3">자유 공부방</h5>
      <div class="d-flex flex-wrap justify-content-center gap-2">
        <template v-for="room in rooms" :key="room.roomId">
          <study-room-info :room="room" />
        </template>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[0]">
          <span>자유공부 {{page*8+1}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[1]">
          <span>자유공부 {{page*8+2}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[2]">
          <span>자유공부 {{page*8+3}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[3]">
          <span>자유공부 {{page*8+4}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[4]">
          <span>자유공부 {{page*8+5}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[5]">
          <span>자유공부 {{page*8+6}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[6]">
          <span>자유공부 {{page*8+7}}번방</span>
          <span>0 / 28</span>
        </router-link>
        <router-link to="" class="btn align-items-start room-card d-flex flex-column disabled" v-if="!rooms[7]">
          <span>자유공부 {{page*8+8}}번방</span>
          <span>0 / 28</span>
        </router-link>
      </div>
      <div class="d-flex justify-content-evenly">
        <button class="btn btn-bread" v-if="hasPrevious" @click="previousPage">이전</button>
        <button class="btn btn-bread disabled" v-if="!hasPrevious">이전</button>
        <button class="btn btn-bread" v-if="hasNext" @click="nextPage">다음</button>
        <button class="btn btn-bread disabled" v-if="!hasNext">다음</button>
      </div>
      <div>

      </div>
    </div>
    <div class="col-8 p-3">
      <router-view />
    </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import StudyRoomInfo from "@/components/StudyRoomInfo";

export default {
  name: "StudyRoomList",
  data() {
    return {
      rooms: [],
      page: 0,
      hasNext: null, hasPrevious:null
    }
  },
  components: {
    StudyRoomInfo
  },
  async mounted() {
    const response = await axios.get('http://localhost:8000/study-service/rooms', {
      params: {
        groupId: 0,
        page:this.page, size:8
      }
    });
    const result = response.data;
    this.hasNext = result.hasNext;
    this.hasPrevious = result.hasPrevious
    this.rooms = result.data

    let count = 0;
    let id=0;
    for(const room of this.rooms) {
      count += room.ticketDtos.length
      id = room.roomId
    }

    if(count > 28*(this.page*8 + this.rooms.length)*0.7){
      let data = {
        name: '자유공부 '+id+'번방',
      }
      await axios.post('http://localhost:8000/study-service/rooms',data)
    }

    // .then((response) => {
    //   console.log(response)
    //   const result = response.data;
    //   this.rooms = result
    //
    // })
  },
  methods: {
    previousPage() {
      this.page--;
      axios.get('http://localhost:8000/study-service/rooms', {
        params: {
          groupId: 0,
          page:this.page, size:8
        }
      })
      .then(response => {
        const result = response.data;
        this.hasNext = result.hasNext;
        this.hasPrevious = result.hasPrevious
        this.rooms = result.data
      })
    },
    nextPage() {
      this.page++;
      axios.get('http://localhost:8000/study-service/rooms', {
        params: {
          groupId: 0,
          page:this.page, size:8
        }
      })
          .then(response => {
            const result = response.data;
            this.hasNext = result.hasNext;
            this.hasPrevious = result.hasPrevious
            this.rooms = result.data
          })
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
.SmallTitle {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
}
h5 {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
.MyContainer {
  width: 1200px;
}
.room-card {
  border: 2px solid #D9D9D9;
  border-radius: 10px;
}
.room-card:hover {
  background: #D9D9D9;
}
.background {
  width: 100%;
  background: #F6E2BD;
}
@media (max-width: 1100px) {
  .background {
    width: 1200px;
  }
}
.btn-bread {
  background-color: #F6E2BD;
  border-radius: 10px;
}
</style>