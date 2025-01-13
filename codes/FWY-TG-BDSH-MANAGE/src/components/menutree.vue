<template>
    <div class="menutree">
      <!-- <label v-for="menu in data" :key="menu.url">
        <el-submenu :index="menu.url" v-if="menu.vo.length > 0">
            <template slot="title">
            <span>{{ menu.name }}</span>
            </template>
            <label>
            <menutree :data="menu.vo"></menutree>
            </label>
        </el-submenu>
        <el-menu-item v-else :index="menu.url">
            <span slot="title">{{menu.name}}</span>
        </el-menu-item>
      </label> -->
      <label v-for="menu in data" :key="menu.id">
            <el-submenu :index="menu.url" v-if="menu.name==='首页' > 0">
            <template slot="title">
            <span>{{ menu.name }}</span>
            </template>
            <label>
            </label>
        </el-submenu>
        <el-submenu :index="menu.url" v-else-if="menu.menuVo && menu.menuVo.length > 0">
            <template slot="title">
            <span>{{ menu.name }}</span>
            </template>
            <label>
            <menutree :data="menu.menuVo"></menutree>
            </label>
        </el-submenu>
        <el-menu-item v-else :index="menu.url">
            <span slot="title">{{menu.name}}</span>
        </el-menu-item>
      </label>
  </div>
</template>

<script>
import menutree from '@/components/menutree'
export default {
  name: 'menutree',
  components: {
    menutree: menutree
  },
  props: ['data'],
  data () {
    return {
      menu_data: {}
    }
  },
  methods: {
  }
}
</script>
<style scoped lang="less">
</style>
