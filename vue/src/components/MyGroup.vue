<template>
  <div>
    <div class="bg-success" style="--bs-bg-opacity: .7;">
      <div class="HeaderContainer m-auto p-2 d-flex justify-content-between">
        <div class="d-flex flex-column">
          <span class="text-white fs-3">그룹</span>
          <span class="text-white fs-5">{{ groupTitle }}</span>
        </div>
      </div>
    </div>
    <div class="MyContainer m-auto mt-3">
      <router-view/>
    </div>
  </div>
</template>

<script>

import {useGroupStore} from "@/store/group";

export default {
  name: "MyGroup",
  setup() {
    const groupStore = useGroupStore();
    return {
        groupStore
    }
  },
  components: {
  },
  data() {
    return {
      groupTitle: '목록',
    }
  },
  mounted() {
  }
  ,watch: {
  }
}
</script>

<style scoped>
.MyContainer {
  width: 1100px;
}
.HeaderContainer {
  width: 1100px;
}
.bg-success {
  width: 1100px;
}
@media (min-width: 1100px) {
  .bg-success {
    width: 100%;
  }

}
.room-card {
  border: 1px solid #D9D9D9;
  border-radius: 10px;
  box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.075) !important;
}
.room-card:hover {
  background: #D9D9D9;
}
.list-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  gap: 15px;
  margin-top: 10px;

}
</style>