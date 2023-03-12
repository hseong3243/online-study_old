<template>
  <div class="black-bg bg-black bg-opacity-50" @click="$emit('close')">
    <div class="white-bg" style="z-index: 1">
      <div class="modal-content h-100">
        <slot/>
      </div>
<!--      <footer class="modal-footer p-2 bg-secondary bg-opacity-25">-->
<!--        <slot name="footer">-->
<!--          <button @click="$emit('close')">닫기</button>-->
<!--        </slot>-->
<!--      </footer>-->
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style>
body {
  margin : 0;
}
div {
  box-sizing: border-box;
}
.black-bg {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
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