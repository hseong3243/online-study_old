<template>
  <div class="Desk card" id="desk1" @click="clickSeat(1)">
    <div class="d-flex flex-fill justify-content-between">
      <div>
        <span>1</span>
      </div>
      <div id="time1">
        <span>00 : 00</span>
      </div>
    </div>
    <div class="d-flex justify-content-center" id="icon1">
    </div>
    <div id="username1" class="text-center">
      <span>test</span>
    </div>
    <a href="#" class="stretched-link" id="1"></a>
  </div>
</template>

<script>
export default {
  name: "StudyRoomDesk",
  props:['desk']
}
</script>

<style scoped>

</style>