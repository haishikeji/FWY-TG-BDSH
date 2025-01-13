<template>
    <div id="agent-admin-list">
      <div class="search">
        <div class="btn">
            <el-button type="success" @click="getAdd({id: 'add'})">新增</el-button>
        </div>
        <!-- <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="">
                <el-input v-model="form.name" placeholder="请输入banner名称"></el-input>
            </el-form-item>
        </el-form> -->
      </div>
      <el-table
        :data="tableData"
        style="width: 100%"
         border
         stripe
        >
            <!-- <el-table-column v-for="(item,index) in tableTitle" :key="index+''" :prop="item.prop" :label="item.label"></el-table-column> -->
            <el-table-column v-for="(item,index) in tableTitle" :key="index+''" :prop="item.prop" :label="item.label" show-overflow-tooltip>
                <template slot-scope="scope">
                    <img style="width:0.4rem;height:0.4rem" v-if="item.prop === 'qrCode'" :src="scope.row.qrCode" alt="" @click="getimg(scope.row.qrCode)">
                    <span v-else-if="item.prop === 'type' && scope.row.type === 'BUSINESS_COOPERATION'">商务合作</span>
                    <span v-else-if="item.prop === 'type' && scope.row.type === 'CONTACT_SERVICE'">联系客服</span>
                  <!--   <span v-else-if="item.prop === 'type' && scope.row.type === 'PROMOTE'">推广挣钱</span> -->
                    <span v-else>{{scope.row[item.prop]}}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="340">
                <template slot-scope="scope">
                    <el-button type="success" size="small" @click="getAdd(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="del(scope.row)">删除</el-button>
                </template>
            </el-table-column>
      </el-table>
      <div class="block" style="text-align: right">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="form.currentPage4"
          :page-sizes="[10, 20, 30]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        >
        </el-pagination>
      </div>
      <!-- 新增 -->
      <el-dialog
      :title="title"
      :visible.sync="editPop"
      width="40%"
      :before-close="editPopClose">
        <el-form ref="editForm" :rules="rules" :model="editForm" label-width="120px" style="text-align:left">
          <el-form-item label="电话号码" prop="contact">
            <el-input type="text" v-model="editForm.contact" placeholder="请输入电话号码"></el-input>
          </el-form-item>
          <el-form-item label="上传二维码">
            <el-upload
              class="upload-demo"
              action=""
              :beforeUpload="beforeUploadPicture"
              style="text-align: left">
              <el-button size="small" type="primary">点击上传</el-button>
              <!-- {{fileName}} -->
            </el-upload>
              <img style="width:0.4rem" :src="editForm.qrCode" alt="" @click="getimg(editForm.qrCode)">
          </el-form-item>
          <el-form-item label="类型" prop="type">
            <el-select v-model="editForm.type">
              <el-option label="商务合作" value="BUSINESS_COOPERATION"></el-option>
              <el-option label="联系客服" value="CONTACT_SERVICE"></el-option>
              <!-- <el-option label="推广挣钱" value="PROMOTE"></el-option> -->
            </el-select>
          </el-form-item>
          <el-form-item label="">
            <el-button style="text-align:right" type="success" @click="getEditPop('editForm')">确定</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <!-- 图片大图 -->
      <el-dialog
        class="delet"
        title=""
        :visible.sync="bigImg"
        width="4rem">
        <img :src="linkUrk" alt="" style="width:100%;">
      </el-dialog>
      <!-- 否定类 -->
      <el-dialog
          title=""
          :visible.sync="delet"
          width="30%">
          <span style="font-size: 0.2rem">是否确认{{negative}}？</span>
          <span slot="footer" class="dialog-footer">
              <el-button @click="delet = false">取消</el-button>
              <el-button type="primary" @click="getnegative">确定</el-button>
          </span>
      </el-dialog>

    </div>
</template>
<script>
import Serve from '@/serve/hc-api/sys'
import { Message } from 'element-ui'
export default {
  components: {
  },
  data () {
    return {
      name: '',
      roles: [],
      // 否定类操作
      delet: false,
      ids: '',
      negative: '',
      // 大图
      bigImg: false,
      linkUrk: '',
      // 列表
      tableTitle: [
        { prop: 'contact', label: '电话号码' },
        { prop: 'qrCode', label: '二维码' },
        { prop: 'type', label: '类型' }
      ],
      tableData: [],
      total: null,
      // 搜索
      form: {
        current: 1,
        size: 10
      },
      timeOption: [],
      // 新增编辑
      title: '',
      type: '',
      editForm: {
        contact: '',
        qrCode: '',
        type: 'BUSINESS_COOPERATION'
      },
      // 是否有跳转地址
      ifLink: '0',
      editPop: false,
      rules: {
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        contact: [
          { required: true, message: '请输入电话号码', trigger: 'blur' }
        ]
      },
      fileName: '',
      // 添加店铺
      addShops: false,
      addId: '', // 当前行数据id
      addLinkurl: '', // 当前行数据回来的关联商家
      defaultProps: {
        children: null,
        label: 'name'
      },
      shopList: [],
      shopListId: []
    }
  },
  created () {

  },
  watch: {
    form: {
      handler (val, oldVal) {
        this.getList(val)
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    // 查看图片大图
    getimg (val) {
      this.bigImg = true
      this.linkUrk = val
    },
    // 获取表格
    getList (val) {
      Serve.getContactPage(val).then(res => {
        if (res.data && res.data.data !== null) {
          this.tableData = res.data.data.records
          this.total = res.data.data.total
        }
      })
    },
    handleSizeChange (val) {
      this.form.size = val
    },
    handleCurrentChange (val) {
      this.form.current = val
    },
    /**
     * 表格操作
     */
    // 删除
    del (val) {
      this.ids = val.id
      this.delet = true
      this.negative = '删除'
    },
    getnegative () {
      Serve.deleteContact({ id: this.ids }).then(res => {
        if (res.data) {
          Message.success('删除成功')
          this.delet = false
          this.getList(this.form)
        }
      })
    },
    /**
     * 新增编辑
     */
    // 新增
    editPopClose () {
      this.editPop = false
    },
    getAdd (val) {
      this.editPop = true
      if (val.id === 'add') {
        this.type = 'add'
        this.fileName = ''
        this.ifLink = '0'
        this.editForm = {
          contact: '',
          qrCode: '',
          type: 'BUSINESS_COOPERATION'
        }
        this.title = '新增'
      } else {
        this.fileName = val.qrCode
        this.editForm = {
          id: val.id,
          contact: val.contact,
          qrCode: val.qrCode,
          type: val.type
        }
        this.type = 'edit'
        this.title = '编辑'
      }
    },
    getEditPop (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.type === 'add') {
            Serve.addContact(this.editForm).then(res => {
              if (res.data) {
                Message.success('新增成功')
                this.editPop = false
                this.getList(this.form)
              }
            })
          } else {
            Serve.updateContact(this.editForm).then(res => {
              if (res.data) {
                Message.success('编辑成功')
                this.editPop = false
                this.type = ''
                this.getList(this.form)
              }
            })
          }
        } else {
          return false
        }
      })
    },
    // 编辑上传头像
    beforeUploadPicture (file) {
      this.fileName = file.name
      this.formdata = new FormData()
      this.formdata.append('file', file)
      Serve.fileup(this.formdata).then(res => {
        if (res.data) {
          Message.success('上传成功')
          this.editForm.qrCode = res.data.data
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
<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  .el-dialog /deep/.el-dialog__header{
    padding: 0!important
  }
</style>
