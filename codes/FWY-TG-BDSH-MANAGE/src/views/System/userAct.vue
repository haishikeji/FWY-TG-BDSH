<template>
    <div id="agent-admin-list">
      <div class="search">
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="">
                <el-input v-model="form.func" placeholder="请输入功能地址"></el-input>
            </el-form-item>
            <el-form-item label="">
                <el-input v-model="form.funcName" placeholder="请输入功能名称"></el-input>
            </el-form-item>
            <el-form-item label="">
                <el-date-picker
                    v-model="date"
                    type="date"
                    placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="">
                <el-date-picker
                    v-model="dates"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
        </el-form>
        <div class="btn">
            <el-button type="success" @click="getSearch">查询</el-button>
        </div>
      </div>
        <el-table :data="tableData" style="overflow-x: auto;" height="600">
            <el-table-column type="index" label="编号"></el-table-column>
            <el-table-column v-for="(item,index) in tableTitle" :key="index+''" :prop="item.prop" :label="item.label" :width="item.width"></el-table-column>
        </el-table>
      <div class="block" style="text-align: right">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="form.current"
          :page-sizes="[10, 20, 30]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        >
        </el-pagination>
        </div>
    </div>
</template>
<script>
import Serve from '@/serve/hc-api/sys'
import Times from '@/serve/date'
export default {
  data () {
    return {
      tableData: [],
      tableTitle: [
        { prop: 'ipAddr', label: '用户IP', width: '140' },
        { prop: 'model', label: '模块', width: '80' },
        { prop: 'operation', label: '功能', width: '140' },
        { prop: 'requestArgs', label: '请求参数', width: '300' },
        { prop: 'operateTime', label: '访问时间', width: '120' },
        { prop: 'response', label: '响应', width: '150' },
        { prop: 'takeTime', label: '响应时长', width: '80' },
        { prop: 'exception', label: '异常信息', width: '150' },
        { prop: 'url', label: '功能地址', width: '120' },
        { prop: 'method', label: '请求方式', width: '80' },
        { prop: 'userId', label: '用户ID', width: '140' },
        { prop: 'userName', label: '用户名', width: '100' },
        { prop: 'phone', label: '号码', width: '140' },
        { prop: 'account', label: '账户', width: '120' }
      ],
      total: 0,
      form: {
        current: 1,
        size: 10,
        date: '',
        startTime: '',
        endTime: '',
        func: '',
        funcName: ''
      },
      date: new Date(),
      dates: null
    }
  },
  created () {
    this.getSearch()
  },
  methods: {
    getSearch () {
      const data = { ...this.form }
      if (this.date !== null) {
        data.date = `${Times.getDate(this.date)}`
      } else {
        data.date = ''
      }
      if (this.dates !== null) {
        data.startTime = `${Times.getDate(this.dates[0])} ${Times.getTime(this.dates[0])}`
        data.endTime = `${Times.getDate(this.dates[1])} ${Times.getTime(this.dates[1])}`
      } else {
        data.startTime = ''
        data.endTime = ''
      }
      Serve.userAct(data).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data.records
          this.total = res.data.data.total
        }
      })
    },
    handleSizeChange (val) {
      this.form.size = val
      this.getSearch()
    },
    handleCurrentChange (val) {
      this.form.current = val
      this.getSearch()
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
