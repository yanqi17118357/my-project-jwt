<script setup>
</script>

<template>
  <div class="w-screen h-screen overflow-hidden flex">
    <div class="flex-1">
      <el-image class="w-full h-full" fit="cover" src="/_bg_sky2.webp" />
    </div>
    <div class="position-absolute bottom-30px left-30px color-white shadow-md text-shadow-black">
      <div class="text-30px font-bold">Sonna</div>
      <div class="mt-10px">她沉鱼落雁、闭月羞花</div>
      <div class="mt-5px">她风姿绰约、她风华绝代</div>
    </div>
    <div class="w-400px z-1 bg-white">
      <router-view v-slot="{ Component }">
        <!-- 淡入淡出过渡效果 -->
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"></component>
        </transition>
      </router-view>
    </div>
  </div>
</template>
