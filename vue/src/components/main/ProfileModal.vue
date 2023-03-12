<template>
  <div class="bg" @click="$emit('close')">
    <div class="white-bg" style="z-index: 1">
      <div class="modal-content h-100">
        <slot/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "profileModal"
}
</script>

<style>
body {
  margin : 0;
}
div {
  box-sizing: border-box;
}
.bg {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.5);
}
.white-bg {
  width: 400px;
  background: #fff;
  border-radius: 4px;
  overflow: hidden;
}
.modal-content {
  padding: 10px 20px;
}
.modal-footer {
  text-align: right;
}
</style>