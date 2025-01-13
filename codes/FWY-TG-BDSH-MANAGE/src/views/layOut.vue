<!--
 * @Author: your name
 * @Date: 2021-08-26 17:01:53
 * @LastEditTime: 2021-08-28 20:02:55
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \hccd-web-admin\src\views\layOut.vue
-->
<template>
  <div style="display:flex;">
    <el-aside>
      <Menu />
    </el-aside>
    <div style="width: 100%;">
      <el-header>
        <headers />
      </el-header>
      <el-main>
        <div class="cont">
          <keep-alive>
            <router-view v-if="$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!$route.meta.keepAlive"></router-view>
        </div>
      </el-main>
    </div>
  </div>
</template>
<script>
import Menu from "@/components/Menu";
import headers from "@/components/header";
export default {
  components: {
    Menu,
    headers
  }
};
</script>
<style scoped>
.el-aside {
  width: 200px !important;
  /* height: 100vh; */
}
.el-header {
  height: 0.6rem !important;
  padding: 0;
}
.el-menu {
  border: none;
}
.el-main {
  padding: 0.2rem 0.2rem 0.2rem 0.2rem;
  background: #f1f1f1;
  width: 100%;
  height: calc(100vh - 0.6rem);
  display: flex;
  align-items: flex-start;
  box-sizing: border-box;
}
.cont {
  width: 100%;
  height: calc(100vh - 1.2rem);
  overflow-x: hidden;
  overflow-y: scroll;
}
/*滚动条整体样式*/
.cont::-webkit-scrollbar {
  width: 0px;
  height: 1px;
}
/*滚动条滑块*/
.cont::-webkit-scrollbar-thumb {
  border-radius: 1px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: rgb(27, 27, 27);
}
/*滚动条轨道*/
.cont::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0);
  border-radius: 1px;
  background: #ccc;
}
</style>
