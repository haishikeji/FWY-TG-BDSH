<template>
    <div id="static">
      <el-progress v-if="percentage !== 0 && percentage !== 100" :percentage="percentage"></el-progress>
      <div class="block" style="padding: 10px;display: flex;justify-content: space-between;">
        <div>
            <el-button type="success" plain @click="getdown">导出</el-button>
           <div style="color:#ef8619;line-height: 30px;margin-left: 20px;">
              <span v-if="percentage === 1">文件准备中...</span>
              <span v-if="percentage > 1 && percentage < 100">文件下载中...</span>
            </div>
        </div>
        <div>
          <el-date-picker
            v-model="times"
            @change="getTimeVal"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
          <el-button style="margin-left:20px" size="" @click="getSearch">查询</el-button>
        </div>
      </div>
      <div class="topTitle">
        <p style="font-size: 16px;line-height: 36px;">订单统计数据：</p>
        <el-tag>发放总量: {{topData.grantTotal}}</el-tag>
        <el-tag>领取总量: {{topData.receiveTotal}}</el-tag>
        <!-- <el-tag>发放总量: {{topData.grantTotal}}</el-tag> -->
        <el-tag>审核未通过数量: {{topData.rollback}}</el-tag>
        <el-tag>订单完成量: {{topData.complete}}</el-tag>
        <el-tag>待审核数量: {{topData.waiting}}</el-tag>
        <el-tag style="cursor: pointer;" @click="getRefusedList">审核拒绝数量: {{topData.refused}}</el-tag>
        <el-tag style="cursor: pointer;" @click="getCancelList">订单取消量: {{topData.cancel}}</el-tag>
        <!-- <div class="top-box">
          <p>发放总量</p>
          <p>{{topData.grantTotal}}</p>
        </div> -->
      </div>
      <el-tabs v-model="activeName" @tab-click="handleClick" style="padding:10px">
      <el-tab-pane label="优惠券统计" name="0">
        <el-table :data="couponData">
          <el-table-column
              v-for="(item, index) in tableLabel"
              :key="index"
              :prop="item.prop"
              :label="item.label"
          ></el-table-column>
        </el-table>
      </el-tab-pane>
        <el-tab-pane label="粉丝统计" name="1">
          <el-table :data="orderData">
            <el-table-column
                v-for="(item, index) in tableLabels"
                :key="index"
                :prop="item.prop"
                :label="item.label"
            ></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <el-pagination
        style="text-align: right;margin-top: 10px;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="tab.current"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="tab.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
      <el-dialog title="审核拒绝订单列表" :visible.sync="refusePop" width="80%" :before-close="closeRefusedList">
        <el-date-picker
          style="display: flex;"
          v-model="refuse.time"
          type="daterange"
          range-separator="至"
          @change="getListTime"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
        <el-table
          :data="closeRefuseData"
          style="width: 100%">
          <el-table-column v-for="(item,index) in refuseTitle" :key="index+''" :prop="item.prop" :label="item.label" :width="item.width"> </el-table-column>
        </el-table>
        <el-pagination
          style="text-align: right;margin-top: 10px;"
          @size-change="refuseSizeChange"
          @current-change="refuseCurrentChange"
          :current-page="refuse.current"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="refuse.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="refusetotal">
        </el-pagination>
      </el-dialog>
      <el-dialog title="订单取消列表" :visible.sync="cancelPop" width="80%" :before-close="closeCancelList">
        <el-date-picker
          style="display: flex;"
          v-model="cancel.time"
          type="daterange"
          range-separator="至"
          @change="getListTimes"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
        <el-table
          :data="closeCancelData"
          style="width: 100%">
          <el-table-column v-for="(item,index) in cancelTitle" :key="index+''" :prop="item.prop" :label="item.label" :width="item.width"> </el-table-column>
        </el-table>
        <el-pagination
          style="text-align: right;margin-top: 10px;"
          @size-change="cancelSizeChange"
          @current-change="cancelCurrentChange"
          :current-page="cancel.current"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="cancel.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="canceltotal">
        </el-pagination>
      </el-dialog>
    </div>
</template>
<script>
import Deserver from '@/serve/hc-api/hc-server'
import Serve from '@/serve/hc-api/order'
import Times from '@/serve/date'
import { http } from '@/serve/aixos/index'
export default {
  data () {
    return {
      /** common */
      activeName: '0',
      times: [],
      tableData: [],
      tab: {
        start: '',
        end: '',
        current: 1,
        size: 10
      },
      /** 订单统计 */
      topData: {
        grantTotal: 0,
        receiveTotal: 0,
        rollback: 0,
        complete: 0,
        waiting: 0,
        refused: 0,
        cancel: 0
      }, // 直接展示的数量
      /** 优惠券表格 */
      tableLabel: [
        { label: '优惠券名字', prop: 'couponName' },
        { label: '发放总量', prop: 'grantTotal' },
        { label: '领取总量', prop: 'receiveTotal' },
        { label: '审核未通过', prop: 'rollback' },
        { label: '订单完成', prop: 'complete' },
        { label: '待审核', prop: 'waiting' },
        { label: '审核拒绝', prop: 'refused' },
        { label: '取消订单', prop: 'cancel' }
      ],
      couponData: [],
      /** 粉丝统计 */
      tableLabels: [
        { label: '昵称', prop: 'nickName' },
        { label: '手机号', prop: 'phone' },
        { label: '下单量', prop: 'totalCount' },
        { label: '完成量', prop: 'completeCount' },
        { label: '取消量', prop: 'cancelCount' }
      ],
      orderData: [],
      total: 0,
      // 导出
      exportss: null,
      percentage: 0,
      // 拒绝订单列表
      refusePop: false,
      closeRefuseData: [],
      refuseTitle: [
        { prop: 'consumerPhone', label: '下单手机号', width: '' },
        { prop: 'orderCheck', label: '订单状态', width: '' },
        { prop: 'checkRefuseRemark', label: '订单拒绝理由', width: '' }
      ],
      refuse: {
        time: null,
        current: 1,
        size: 10
      },
      refusetotal: 0,
      // 取消订单列表
      cancelPop: false,
      closeCancelData: [],
      cancelTitle: [
        { prop: 'orderOn', label: '订单号', width: '' },
        { prop: 'consumerPhone', label: '下单手机号', width: '' },
        { prop: 'consumerOrderTime', label: '下单时间', width: '' },
        { prop: 'cancelTime', label: '取消时间', width: '' },
        { prop: 'orderCheck', label: '订单状态', width: '150' }
      ],
      cancel: {
        time: null,
        current: 1,
        size: 10
      },
      canceltotal: 0,
      pretimes: 10, // 定时器
      setTime: null
    }
  },
  created () {
    let nowTime = new Date()
    nowTime = nowTime.getHours() < 9 ? `${Times.getDate(nowTime)} ${Times.getTime(nowTime)}` : `${Times.getDate(nowTime)} 09:00:00`
    let oldTime = new Date(new Date(`${Times.getDate(new Date())} 09:00:00`).valueOf() - 24 * 60 * 60 * 1000)
    oldTime = `${Times.getDate(oldTime)} 09:00:00`
    const data = {
      end: nowTime,
      start: oldTime
    }
    this.$set(this.refuse, 'time', [new Date(data.start), new Date(data.end)])
    this.$set(this.cancel, 'time', [new Date(data.start), new Date(data.end)])
    this.exportss = `${Deserver.Order.excelStaList}?endTime=${data.end}&startTime=${data.start}`
    this.times = [new Date(new Date(`${Times.getDate(new Date())} 09:00:00`).valueOf() - 24 * 60 * 60 * 1000), new Date(nowTime)]
    this.tab.end = nowTime
    this.tab.start = oldTime
    this.getOrderStatic(data)
    this.getCouponList()
  },
  methods: {
    /** @module 新增模块功能 */
    // 拒绝
    getRefusedList () {
      this.refusePop = true
      // const reTime = [new Date(this.tab.start), new Date(this.tab.end)]
      // this.$set(this.refuse, 'time', reTime)
      const data = { ...this.refuse }
      if (data.time !== null) {
        data.start = `${Times.getDate(data.time[0])} ${Times.getTime(data.time[0])}`
        data.end = `${Times.getDate(data.time[1])} ${Times.getTime(data.time[1])}`
      } else {
        data.start = ''
        data.end = ''
      }
      delete data.time
      Serve.getRefusedList(data).then(res => {
        if (res.data.code === 200) {
          this.closeRefuseData = res.data.data.records
          this.refusetotal = res.data.data.total
        }
      })
    },
    closeRefusedList () {
      this.refusePop = false
    },
    refuseSizeChange (val) {
      this.refuse.size = val
      this.getRefusedList()
    },
    refuseCurrentChange (val) {
      this.refuse.current = val
      this.getRefusedList()
    },
    getListTime () {
      this.getRefusedList()
    },
    // 取消
    getCancelList () {
      this.cancelPop = true
      this.cancel.time = [new Date(this.tab.start), new Date(this.tab.end)]
      const data = { ...this.cancel }
      if (data.time !== null) {
        data.start = `${Times.getDate(data.time[0])} ${Times.getTime(data.time[0])}`
        data.end = `${Times.getDate(data.time[1])} ${Times.getTime(data.time[1])}`
      } else {
        data.start = ''
        data.end = ''
      }
      delete data.time
      Serve.getCancelList(data).then(res => {
        if (res.data.code === 200) {
          this.closeCancelData = res.data.data.records
          this.canceltotal = res.data.data.total
        }
      })
    },
    closeCancelList () {
      this.cancelPop = false
    },
    cancelSizeChange (val) {
      this.cancel.size = val
      this.getCancelList()
    },
    cancelCurrentChange (val) {
      this.cancel.current = val
      this.getCancelList()
    },
    getListTimes () {
      this.getCancelList()
    },
    /** 1.@module 获取顶部直接展示的信息 */
    // 获取数据
    getOrderStatic (val) {
      Serve.orderStatic(val).then(res => {
        if (res.data.code === 200) {
          this.topData = { ...res.data.data }
        }
      })
    },
    // 选择时间
    getSearch () {
      if (this.times !== null) {
        this.tab.end = `${Times.getDate(this.times[1])} ${Times.getTime(this.times[1])}`
        this.tab.start = `${Times.getDate(this.times[0])} ${Times.getTime(this.times[0])}`
      } else {
        this.tab.end = ''
        this.tab.start = ''
      }
      this.exportss = `${Deserver.Order.excelStaList}?endTime=${this.tab.end}&startTime=${this.tab.start}`
      this.tab.size = 10
      this.tab.current = 1
      this.getOrderStatic(this.tab)
      if (this.activeName === '0') {
        this.getCouponList()
      } else {
        this.getOrderList()
      }
    },
    /** 2..@module 优惠券列表 */
    getCouponList () {
      const data = { ...this.tab }
      Serve.couponStaList(data).then(res => {
        if (res.data.code === 200) {
          this.couponData = res.data.data.records
          this.total = res.data.data.total
        }
      })
    },
    /** @module 订单列表 */
    getOrderList () {
      const data = { ...this.tab }
      Serve.consumerStaList(data).then(res => {
        if (res.data.code === 200) {
          this.orderData = res.data.data.records
          this.total = res.data.data.total
        }
      })
    },
    /** @module 导出 */
    getTimeVal () {
      if (this.times !== null) {
        this.tab.end = `${Times.getDate(this.times[1])} ${Times.getTime(this.times[1])}`
        this.tab.start = `${Times.getDate(this.times[0])} ${Times.getTime(this.times[0])}`
      } else {
        this.tab.end = ''
        this.tab.start = ''
      }
      this.$set(this.refuse, 'time', [new Date(this.tab.start), new Date(this.tab.end)])
      this.$set(this.cancel, 'time', [new Date(this.tab.start), new Date(this.tab.end)])
      this.exportss = `${Deserver.Order.excelStaList}?endTime=${this.tab.end}&startTime=${this.tab.start}`
    },
    getdown () {
      var _this = this
      _this.pretimes = 10
      _this.percentage = 1
      if (_this.pretimes > 0) {
        _this.setTime = setInterval(function () {
          _this.pretimes--
          if (_this.pretimes === 0 || _this.percentage === 100) {
            clearInterval(_this.setTime)
          } else {
            _this.percentage = (11 - _this.pretimes) * 10 - 1
          }
        }, 1000)
      }
      http({
        url: _this.exportss,
        method: 'post',
        responseType: 'arraybuffer',
        onDownloadProgress (progress) {
          // const a = Math.round(progress.loaded / progress.total * 100)
          // _this.$nextTick(() => {
          //   if (_this.times > 0) {
          //     _this.setTime = setInterval(function () {
          //       _this.times--
          //       _this.percentage = _this.times * 10 - 1
          //       if (_this.times === 0) {
          //        clearInterval(_this.setTime)
          //       }
          //     }, 1000)
          //   }
          //   // _this.percentage = a
          // })
        }
      }).then(res => {
        const names = res.headers['content-disposition'].split('=')[1]
        const name = decodeURI(names)
        const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
        const objectUrl = URL.createObjectURL(blob)
        const a = document.createElement('a')
        document.body.appendChild(a)
        a.setAttribute('style', 'display:none')
        a.setAttribute('href', objectUrl)
        const filename = name
        a.setAttribute('download', filename)
        a.click()
        URL.revokeObjectURL(objectUrl)
        _this.percentage = 100
      }).catch(() => {
        // Message.error('下载失败')
        _this.percentage = 100
      })
    },
    /** 3.@module common */
    handleSizeChange (val) {
      this.tab.size = val
      if (this.activeName === '0') {
        this.getCouponList()
      } else {
        this.getOrderList()
      }
    },
    handleCurrentChange (val) {
      this.tab.current = val
      if (this.activeName === '0') {
        this.getCouponList()
      } else {
        this.getOrderList()
      }
    },
    handleClick () {
      this.tab.size = 10
      this.tab.current = 1
      if (this.activeName === '0') {
        this.getCouponList()
      } else {
        this.getOrderList()
      }
    }
  }
}
</script>
<style lang="less" scoped>
#static{
  width: 100%;
  // height: 100vh;
  background: #fff;
  .block{
    text-align: right;
  }
  .topTitle{
    padding: 10px;
    display: flex;
    justify-content: space-between;;
    .top-box{
        width: 150px;
        height: 150px;
        border-radius: 50%;
        background: #ef8619;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-size: 20px;
        color:#fff;
    }
  }
}
</style>
