<template>
    <div id="agent-admin-list">
      <!-- <Breadcrumb/> -->
      <div class="search">
        <div class="btn">
            <el-button type="success" @click="getAdd({id: 'add'})">新增</el-button>
        </div>
      </div>
      <Tables :ajaxs="ajaxs">
        <template slot="header">
            <el-table-column type="index" label="编号" width="80"></el-table-column>
            <el-table-column v-for="(item,index) in tableTitle" :key="index+''" :prop="item.prop" :label="item.label"></el-table-column>
            <el-table-column fixed="right" label="操作" width="500">
            <template slot-scope="scope">
                <el-button type="success" class="el-icon-edit" @click="getAdd(scope.row)" size="small">基本信息</el-button>
                <!-- <el-button type="success" class="el-icon-circle-plus-outline" @click="getInterAuth(scope.row)" size="small">接口权限</el-button> -->
                <el-button type="success" class="el-icon-circle-plus-outline" @click="getMenuAuth(scope.row)" size="small">菜单权限</el-button>
                <!-- <el-button type="success" @click="getType(scope.row)" size="small"> <span v-html="scope.row.disabled === '已激活' ? '冻结' : '激活'"></span></el-button> -->
                <el-button type="success" class="el-icon-del" @click="getDel(scope.row)" size="small">删除</el-button>
            </template>
            </el-table-column>
        </template>
      </Tables>
      <!-- 新增编辑 -->
      <el-dialog
        :title="titles"
        :visible.sync="dialogVisible"
        width="50%"
        :before-close="handleClose"
      >
        <div class="dialogSpan">
          <el-form label-width="1.2rem" label-position="right">
            <el-form-item label="角色名称">
                <el-input v-model="editForm.name"/>
            </el-form-item>
            <el-form-item label="角色描述">
                <el-input type="" v-model="editForm.des"/>
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="getCommit">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 菜单 -->
      <el-dialog
        title="菜单权限"
        :visible.sync="menuTreeShow"
        width="50%"
        :before-close="menuTreeClose"
      >
        <div class="dialogSpan">
          <el-tree
            :data="menuTree"
            show-checkbox
            node-key="id"
            ref="treeMenu"
            :default-checked-keys="isCheckMenuIds"
            :props="defaultProps">
          </el-tree>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="menuTreeClose">取 消</el-button>
            <el-button type="primary" @click="getmenuTree">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 接口(暂不用) -->
      <el-dialog
        title="接口权限"
        :visible.sync="InterTreeShow"
        width="50%"
        :before-close="InterTreeClose"
      >
        <div class="dialogSpan">
          <el-tree
            :data="InterTree"
            show-checkbox
            node-key="id"
            ref="treeInter"
            :default-checked-keys="isCheckInterId"
            :props="defaultProps">
          </el-tree>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="InterTreeClose">取 消</el-button>
            <el-button type="primary" @click="getInterTree">确 定</el-button>
        </span>
      </el-dialog>
    </div>
</template>
<script>
// import Breadcrumb from '@/components/breadcrumb.vue'
import Tables from '@/components/table.vue'
import Deserve from '@/serve/hc-api/hc-server'
import Serve from '@/serve/hc-api/auth/menu'
import { Message } from 'element-ui'
export default {
  components: {
    Tables
    // Breadcrumb
  },
  data () {
    return {
      tableTitle: [
        { prop: 'roleName', label: '角色类型' },
        { prop: 'roleDesc', label: '角色描述' },
        { prop: 'deleted', label: '状态' }
      ],
      // 基本信息
      ajaxs: '',
      editForm: {
        name: '',
        des: '',
        roleId: ''
      },
      dialogVisible: false,
      titles: '新增角色',
      // 菜单权限选择
      menuTreeShow: false,
      menuTree: [],
      defaultProps: {
        children: 'menuVo',
        label: 'name'
      },
      menuId: '',
      isCheckMenuIds: [], // 默认选中的菜单,
      menuOne: [],
      // 接口权限
      InterTreeShow: false,
      InterTree: [],
      isCheckInterId: [],
      interId: ''
    }
  },
  created () {
    this.ajaxs = `${Deserve.Auth.roleList}?`
    this.finMenuList()
    // this.getInterList()
  },
  methods: {
    // 刷新表格
    putTable () {
      if (this.ajaxs === `${Deserve.Auth.roleList}?`) {
        this.ajaxs = `${Deserve.Auth.roleList}?Specialcharacters`
      } else {
        this.ajaxs = `${Deserve.Auth.roleList}?`
      }
    },
    /**
     * 基本信息操作
     */
    // 关闭
    handleClose () {
      this.dialogVisible = false
    },
    // 禁用启用
    // getType (val) {
    //   let type
    //   if (val.disabled === '已激活') {
    //     type = 'FREEZE'
    //   } else {
    //     type = 'ACTIVATION'
    //   }
    //   const data = {
    //     'roleId': val.id,
    //     'disableEnum': type
    //   }
    //   Serve.getchooseRole(data).then(res => {
    //     // this.$router.go(0)
    //     if (res.data.code === 200) {
    //       Message.success('更新成功')
    //       this.putTable()
    //     }
    //   })
    // },
    // 删除
    getDel (val) {
      Serve.roleDel({ id: val.id }).then(res => {
        if (res.data.code === 200) {
          Message.success('删除成功')
          this.putTable()
        }
      })
    },
    // 新增编辑窗口
    getAdd (val) {
      this.dialogVisible = true
      if (val.id === 'add') {
        this.titles = '新增角色'
        this.editForm = {
          'name': '',
          'des': '',
          'roleId': ''
        }
      } else {
        this.titles = '编辑角色'
        this.editForm = {
          'name': val.roleName,
          'des': val.roleDesc,
          'roleId': val.id
        }
      }
    },
    getCommit () {
      if (this.editForm.roleId === '') {
        Serve.addRole(this.editForm).then(res => {
          if (res.data) {
            this.dialogVisible = false
            Message.success('新增成功')
            this.putTable()
          }
        })
      } else {
        Serve.roleUpdate(this.editForm).then(res => {
          if (res.data) {
            this.dialogVisible = false
            Message.success('编辑成功')
            this.putTable()
          }
        })
      }
    },
    /**
     * 菜单权限
     */
    // 构建树状
    getTree (val, val1) {
      for (const item of val) {
        if (item.parentId !== '0') {
          for (const i of val1) {
            if (i.id === item.parentId) {
              i.menuVo.push(item)
            } else {
              // ..
            }
          }
        }
      }
    },
    // 获取菜单列表
    finMenuList () {
      Serve.menurecordList().then(res => {
        for (const item of res.data.data.records) {
          if (item.parentId === '0') {
            item.menuVo = []
            this.menuOne.push(item)
          }
        }
        this.menuTree = res.data.data.records
        this.getTree(res.data.data.records, this.menuOne)
        this.menuTree = this.menuOne
      })
    },
    // 获取已有的菜单的节点id
    // getisCheckMenuId (val) {
    //   for (const item of val) {
    //     if (item.isCheck && item.isCheck === true) {
    //       this.isCheckMenuId.push(item.id)
    //       if (item.menuVo.length > 0) {
    //         this.getisCheckMenuId(item.menuVo)
    //       } else {
    //         continue
    //       }
    //     } else {
    //       if (item.parentId !== '0') {
    //         this.isCheckMenuId = this.isCheckMenuId.filter(i => {
    //           return i !== item.parentId
    //         })
    //       } else {
    //         continue
    //       }
    //     }
    //   }
    // },
    getIds (val) {
      for (const item of val) {
        this.isCheckMenuIds.push(item.id)
        if (item.menuVo.length > 0) {
          this.getIds(item.menuVo)
        } else {
          continue
        }
      }
    },
    // 菜单窗口打开
    getMenuAuth (val) {
      this.menuId = val.id
      Serve.getRoleMenu({ id: val.id }).then(res => {
        if (res.data.code === 200) {
          const a = res.data.data
          this.getIds(a)
          this.menuTreeShow = true
          for (const item of a) {
            if (item.parentId === '0') {
              this.isCheckMenuIds = this.isCheckMenuIds.filter(i => {
                return i !== item.id
              })
            }
          }
          this.$refs.treeMenu.setCheckedKeys(this.isCheckMenuIds)
          console.log(this.isCheckMenuIds)
        }
      })
    },
    // 关闭菜单
    menuTreeClose () {
      this.menuTreeShow = false
      this.$refs.treeMenu.setCheckedKeys([])
      this.isCheckMenuIds = []
    },
    // 提交
    getmenuTree () {
      let b = this.$refs.treeMenu.getCheckedKeys()
      const a = this.$refs.treeMenu.getCheckedNodes()
      for (const item of a) {
        if (item.parentId !== '0') {
          for (const i of this.menuTree) {
            if (i.id !== item.parentId && i.menuVo.length > 0) {
              for (const iterator of i.menuVo) {
                if (iterator.id !== item.parentId) {
                  continue
                } else if (iterator.id === item.parentId) {
                  b.push(iterator.parentId)
                } else {
                  continue
                }
              }
            } else if (i.id === item.parentId) {
              b.push(i.id)
            } else {
              continue
            }
          }
        }
      }
      b = Array.from(new Set(b))
      Serve.addRoleMenu({ roleId: this.menuId, menuIds: b }).then(res => {
        if (res.data.code === 200) {
          Message.success('菜单权限更改成功')
          this.menuTreeShow = false
          this.isCheckMenuIds = []
          this.$refs.treeMenu.setCheckedKeys([])
        }
      })
    },
    /**
     * 接口(暂不用)
     */
    // 获取接口列表
    getInterList () {
      Serve.getRoleList().then(res => {
        if (res.data.code === 200) {
          Serve.roleCheckInter({ id: res.data.data.records[0].id }).then(res => {
            if (res.data.code === 200) {
              for (let i = 0; i < res.data.data.length; i++) {
                for (const iterator of res.data.data[i].interfaceInfoVos) {
                  iterator.disabled = false
                }
                this.InterTree.push({
                  name: res.data.data[i].applicationName,
                  vo: res.data.data[i].interfaceInfoVos
                })
              }
            }
          })
        }
      })
    },
    // 打开弹窗并获取被选中接口
    getInterAuth (val) {
      this.interId = val.id
      Serve.roleCheckInter({ id: val.id }).then(res => {
        if (res.data.code === 200) {
          this.isCheckInterId = []
          this.InterTreeShow = true
          const InterList = res.data.data
          for (const item of InterList) {
            for (const i of item.interfaceInfoVos) {
              if (i.isCheck && i.isCheck === true) {
                this.isCheckInterId.push(i.id)
              }
            }
          }
          this.$refs.treeInter.setCheckedKeys(this.isCheckInterId)
        }
      })
    },
    // 关闭弹窗
    InterTreeClose () {
      this.InterTreeShow = false
      this.$refs.treeInter.setCheckedKeys([])
    },
    // 提交
    getInterTree () {
      let b = this.$refs.treeInter.getCheckedKeys()
      b = b.filter(item => {
        return item !== undefined
      })
      Serve.getroleInterface({ id: this.interId, interfaceIds: b }).then(res => {
        if (res.data.code === 200) {
          this.InterTreeShow = false
          this.$refs.treeInter.setCheckedKeys([])
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
#agent-admin-list{
  width: 100%;
  height: 100%;
  background: #fff;
  .search{
    width: 100%;
    height: 0.8rem;
    display: flex;
    justify-content: space-between;
    box-sizing: border-box;
    align-items: center;
    .btn{
        display: flex;
        margin-left: 0.2rem;
        .el-button{
          padding: 0.1rem 0.12rem;
          height: 0.4rem;
        }
    }
    .el-form{
        display: flex;
        justify-content: space-around;
        margin-top: 0.25rem;
        .block{
            margin-right: 0.2rem;
        }
    }
  }
}
</style>
