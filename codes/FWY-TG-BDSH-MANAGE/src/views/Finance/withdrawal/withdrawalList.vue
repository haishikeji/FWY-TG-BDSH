<template>
  <div id="agent-admin-list">
    <div class="search">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="">
          <el-input
            style="width: 300px"
            v-model="form.account"
            placeholder="请输入用户绑定的手机或商家账号名称"
            @input="getinfo"
          ></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input
            v-model="form.orderNo"
            placeholder="系统订单号"
            @input="getinfo"
          ></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input
            v-model="form.paymentNo"
            placeholder="微信支付订单号"
            @input="getinfo"
          ></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-select v-model="form.payStatus" @change="getinfo">
            <el-option label="全部支付状态" value=""></el-option>
            <el-option label="待处理" value="0"></el-option>
            <el-option label="成功" value="1"></el-option>
            <el-option label="待确认" value="2"></el-option>
            <el-option label="失败" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-select v-model="form.accountType" @change="getinfo">
            <el-option label="全部账户类型" value=""></el-option>
            <el-option label="商家" value="1"></el-option>
            <el-option label="用户" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="setAllMoney">批量提现</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      @selection-change="handleSelectionChange"
      border
      stripe
    >
      <el-table-column :selectable="dd" type="selection" width="55">
      </el-table-column>

      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form
            label-position="left"
            label-width="140px"
            inline
            class="demo-table-expand"
          >
            <el-form-item label="系统订单号">
              <span>{{ props.row.partnerTradeNo }}</span>
            </el-form-item>
            <el-form-item label="微信支付订单号">
              <span>{{ props.row.paymentNo }}</span>
            </el-form-item>
            <el-form-item label="金额">
              <span>{{ props.row.amount }}</span>
            </el-form-item>
            <el-form-item label="支付时间">
              <span>{{ props.row.paymentTime }}</span>
            </el-form-item>
            <el-form-item label="企业付款备注">
              <span>{{ props.row.comDesc }}</span>
            </el-form-item>
            <el-form-item label="提现来源类型">
              <span>{{ props.row.accountType }}</span>
            </el-form-item>
            <el-form-item label="支付状态">
              <span>{{ props.row.payStatus }}</span>
            </el-form-item>
            <el-form-item label="用户id">
              <span>{{ props.row.account }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(item, index) in tableTitle"
        :key="index + ''"
        :prop="item.prop"
        :label="item.label"
        :width="item.width"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="操作" width="140">
        <template slot-scope="scope">
          <el-button
            @click="getWithDraw(scope.row)"
            v-if="
              scope.row.payStatus !== '成功' && scope.row.payStatus !== '失败'
            "
            type="warning"
            size="small"
            >审核通过</el-button
          >
          <el-button
            v-if="scope.row.payStatus === '成功'"
            type="success"
            disabled
            size="small"
            >已提现</el-button
          >
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
        :total="total"
      >
        >
      </el-pagination>
    </div>
    <!-- 图片大图 -->
    <el-dialog class="delet" title="" :visible.sync="bigImg" width="4rem">
      <div v-if="linkUrk !== ''">
        <img :src="linkUrk" alt="" style="width: 100%" />
        <div style="color: red; font-size: 22px">
          提现金额：￥{{ currentMoney }}
        </div>
      </div>
      <span v-else>用户暂未上传付款二维码图片</span>
      <!-- <img src="../../../assets/images/favicon.jpg" style="width:100%;" alt=""> -->
      <el-button
        style="margin: 10px 0 0 67%"
        type="success"
        @click="getwidthSuccess"
        >付款完成</el-button
      >
    </el-dialog>
  </div>
</template>
<script>
import Serve from '@/serve/hc-api/finan'
import { Message } from 'element-ui'
export default {
  components: {},
  data () {
    return {
      roles: [],
      // 列表
      tableTitle: [
        { prop: 'partnerTradeNo', label: '系统订单号', width: '150' },
        { prop: 'paymentNo', label: '微信支付订单号', width: '140' },
        { prop: 'phone', label: '用户手机号', width: '140' },
        { prop: 'amount', label: '金额', width: '' },
        { prop: 'createTime', label: '用户发起时间', width: '180' },
        { prop: 'paymentTime', label: '支付时间', width: '140' },
        { prop: 'accountType', label: '来源类型', width: '' },
        { prop: 'payStatus', label: '支付状态', width: '' },
        { prop: 'errCodeDes', label: '提现状态', width: '' }
      ],
      tableData: '',
      total: null,
      currentMoney: '',
      // 搜索
      form: {
        account: '',
        orderNo: '',
        paymentNo: '',
        payStatus: '0',
        accountType: '',
        current: 1,
        size: 10
      },
      timeOption: [],
      // 大图
      linkUrk: '',
      bigImg: false,
      drawId: '',
      multipleSelection: ''
    }
  },
  created () {
    this.getStartInfo()
    this.getList(this.form)
  },
  methods: {
    /**
     * 手动提现
     */
    getWithDraw (val) {
      console.log(val)
      const arr = []
      arr.push(val.id)
      Serve.adminUpdates({ ids: arr }).then((res) => {
        console.log(res, 'ressssssssssssssssssss')
        // eslint-disable-next-line eqeqeq
      })
    },
    // 确认已付款
    getwidthSuccess () {
      Serve.widthDrawSuccess({ id: this.drawId }).then((res) => {
        if (res.data.code === 200) {
          this.bigImg = false
          this.getList(this.form)
        }
      })
    },
    /**
     * 获取初始信息
     */
    getStartInfo () {
      // 获取角色列表
      Serve.roleList().then((res) => {
        if (res.data.code === 200) {
          this.roles = res.data.data.records
        }
      })
    },
    // 查询表格
    getinfo () {
      this.form.current = 1
      this.form.size = 10
      this.getList(this.form)
      console.log(this.form)
    },
    // 获取表格
    getList (val) {
      console.log(val, '22222222222222222222222222222222')
      Serve.withList(val).then((res) => {
        for (const item of res.data.data.records) {
          item.accountType = item.accountType === 0 ? '用户' : '商家'
          item.amount = parseFloat(item.amount) / 100
        }
        console.log(res.data.data.records, 'res.data.data.records提现的数据')
        this.tableData = res.data.data.records
        this.total = res.data.data.total
      })
    },
    handleSizeChange (val) {
      this.form.size = val
      this.getList(this.form)
    },
    handleCurrentChange (val) {
      this.form.current = val
      this.getList(this.form)
    },
    /* 全选或者单选 */
    handleSelectionChange (val) {
      const newArr = []
      for (let i = 0; i < val.length; i++) {
        newArr.push(val[i].id)
      }
      console.log(val)
      console.log(newArr)
      this.multipleSelection = newArr
    },
    /*  批量提现 */
    setAllMoney () {
      if (this.multipleSelection.length > 0) {
        Serve.adminUpdates({ ids: this.multipleSelection }).then((res) => {
          console.log(res, 'ressssssssssssssssssss')
          // eslint-disable-next-line eqeqeq
          this.getList(this.form)
        })
      } else {
        // eslint-disable-next-line no-undef
        Message.success('请先选择订单')
      }
    },
    dd (row, index) {
      console.log(row)
      console.log(index)
      if (row.payStatus === '待处理') {
        return true
      } else {
        return false
      }
    }
  }
}
</script>
<style lang="less" scoped>
#agent-admin-list {
  width: 100%;
  height: 100%;
  background: #fff;
  .search {
    width: 100%;
    height: 0.8rem;
    display: flex;
    justify-content: space-between;
    box-sizing: border-box;
    align-items: center;
    padding-left: 50px;
    .btn {
      display: flex;
      margin-left: 0.2rem;
      .el-button {
        padding: 0.1rem 0.12rem;
        height: 0.4rem;
      }
    }
    .el-form {
      display: flex;
      justify-content: space-around;
      margin-top: 0.25rem;
      .block {
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
</style>
