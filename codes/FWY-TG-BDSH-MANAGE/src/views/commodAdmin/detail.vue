<template>
  <div class="container">
    <div slot="header" class="clearfix">
      <span class="flexs-title" style="float:left">基本信息</span>
      <el-button style="float: right; padding: 6px" type="success" size="mini" @click="getPath">返回</el-button>
    </div>
    <div class="form">
      <el-form label-width="120px" label-position="left" :model="basicForm">
        <div class="item">
          <el-form-item label="优惠券名称：" style="width:300px;">
            <el-input v-model="basicForm.couponName" placeholder="优惠券名称" size="mini" disabled></el-input>
          </el-form-item>
          <el-form-item label="所属店铺：" style="width:300px;">
            <el-input v-model="merchantName" placeholder="所属店铺" size="mini" disabled></el-input>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="所属平台：" style="width:300px;">
            <el-input v-model="basicForm.couponPlatform" placeholder="所属平台" size="mini" disabled style="width:200px;"></el-input>
          </el-form-item>
          <el-form-item label="活动开始时间：" style="width:300px;">
            <el-input v-model="basicForm.beginTime" placeholder="活动开始时间" size="mini" disabled style="width:200px;"></el-input>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="活动结束时间：" style="width:300px;">
            <el-input v-model="basicForm.endTime" placeholder="活动结束时间" size="mini" disabled style="width:200px;"></el-input>
          </el-form-item>
          <el-form-item label="使用规则：" style="width:300px;text-align:left">
            <el-tag effect="dark" size="mini" type="success">过期作废</el-tag>
            <el-tag effect="dark" size="mini" type="success">不与商家好评返现卡同享</el-tag>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="发放开始时间：" style="width:300px;">
            <el-input v-model="basicForm.grantStartTime" disabled placeholder="活动开始时间" size="mini" style="width:200px;"></el-input>
          </el-form-item>
          <el-form-item label="发放结束时间：" style="width:300px;">
            <el-input v-model="basicForm.grantEndTime" disabled placeholder="活动结束时间" size="mini" style="width:200px;"></el-input>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="发放方式：" style="width:300px;">
            <el-input v-model="basicForm.grantType" size="mini" disabled></el-input>
          </el-form-item>
          <el-form-item label="发放金额：" style="width:300px;">
            <el-input v-model="basicForm.couponPrice" disabled size="mini" placeholder="发放金额" style="width:200px"></el-input>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="返利金额：" style="width:300px;">
            <el-input v-model="basicForm.cashBack" disabled size="mini" placeholder="返利金额" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item label="好评要求：" style="width:300px;">
            <el-select v-model="basicForm.evaluationRequirements" size="mini" disabled style="width:200px;" placeholder="请选择">
              <el-option label="好评5星+图+20字" value="0"></el-option>
              <el-option label="仅5星+文字不带图" value="1"></el-option>
              <el-option label="仅5星" value="2"></el-option>
              <el-option label="纯销量，不需要评价" value="3"></el-option>
              <el-option label="优质好评，五星+3张图+30字以上" value="4"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="是否上架：" style="width:300px;">
            <el-select v-model="basicForm.couponStatus" size="mini" disabled style="width:200px;" placeholder="请选择">
              <el-option label="上架" value="0"></el-option>
              <el-option label="暂不上架" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="收单价：" style="width:300px;">
            <el-input v-model="basicForm.serviceCharge" disabled size="mini" placeholder="发放金额" style="width:200px"></el-input>
          </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="优惠券图片：" style="width:300px;">
            <div style="display:flex">
              <img style="margin-left:0.1rem;width:0.7rem;height:0.7rem" v-for="(item,index) in basicForm.couponImg" :key="index" :src="item" alt="">
            </div>
          </el-form-item>
        <el-form-item label="返利规则：">
          <div style="display:flex">
            <div style="display: flex;">
              <span><pre>满 </pre></span><el-input v-model="basicForm.couponMerchantPrice" size="mini" placeholder=""></el-input>
            </div>
            <div style="display: flex;">
              <span><pre> 减 </pre></span><el-input v-model="basicForm.cashBack" size="mini" placeholder=""></el-input>
            </div>
          </div>
        </el-form-item>
        </div>
        <div class="item">
          <el-form-item label="购买要求备注：" style="width:300px;">
            <el-input v-model="basicForm.consumeRemark" size="mini" disabled></el-input>
          </el-form-item>
        </div>
      </el-form>
    </div>
    <div style="display:flex">
      <span style="font-size: 15px;line-height:40px;color: #333;font-weight:700">优惠券历史时间节点筛选:</span>
      <el-select style="margin-left:10px" v-model="couponRecordTimeOne" @change="getHistoryData">
        <el-option v-for="(item,index) in couponRecordTime" :key="index+''"
        :label="item" :value="item"></el-option>
      </el-select>
    </div>
    <div class="title">优惠券历史发放记录</div>
    <div class="freezon">
        <div class="freezonItem">
          <Tables :ajaxs="ajaxs" style="width:17rem">
            <template slot="header">
              <el-table-column v-for="(item,index) in tableTitles" :key="index+''" :prop="item.prop" :label="item.label"></el-table-column>
              <!-- <el-table-column label="操作" width="280">
                <template slot-scope="scope">
                  <el-button @click="getNum(scope.row)" type="primary" size="small">修改总量</el-button>
                </template>
              </el-table-column> -->
            </template>
          </Tables>
        </div>
    </div>
    <!-- <div class="title">结算信息</div>
    <div class="freezon">
      <div class="freezonItem">
        <div class="money"><div style="margin-right:100px">冻结金额：</div> <div v-html="couponRecordSets ? couponRecordSets.freeze : ''"></div></div>
        <div class="money"><div  style="margin-right:100px">手续费：</div> <div v-html="couponRecordSets ? couponRecordSets.fee : ''"></div></div>
        <div class="money"><div  style="margin-right:100px">结算金额：</div> <div v-html="couponRecordSets ? couponRecordSets.settlement : ''"></div></div>
      </div>
    </div> -->
    <div class="title" v-if="type === 'review'">审核</div>
    <el-form v-if="type === 'review'">
        <el-form-item  label="是否通过" style="width: 225px;">
            <el-radio-group v-model="status">
                <el-radio label="通过"></el-radio>
                <el-radio label="不通过"></el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item v-if="type === 'review'" style="width: 70px;">
            <el-button type="success" @click="getAddAgent">审核</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>
<script>
// import Times from '@/serve/date'
import { Message } from 'element-ui'
import Tables from '@/components/table.vue'
import Serve from '@/serve/hc-api/commod/commod'
import Deserve from '@/serve/hc-api/hc-server'
export default {
  components: {
    Tables
  },
  data () {
    return {
      basicForm: {
      },
      // 发放情况
      tableData: [
      ],
      status: '',
      type: '',
      agentList: [],
      merchantName: '',
      // 历史记录及结算记录时间列表
      couponRecordTime: [],
      couponRecordTimeOne: '', // 选择的时间
      couponRecordSets: {},
      couponRecordTab: [],
      // 列表
      tableTitles: [
        { prop: 'receive', label: '领取数量' },
        { prop: 'remainder', label: '剩余数量' },
        { prop: 'totalNum', label: '总数量' },
        { prop: 'grantTime', label: '发放时间' },
        { prop: 'freeze', label: '冻结金额' },
        { prop: 'fee', label: '手续费' },
        { prop: 'settlement', label: '结算金额' },
        { prop: 'putawayTime', label: '时间' },
        { prop: 'invalid', label: '状态' }
      ],
      ajaxs: ''
    }
  },
  beforeRouteEnter (to, from, next) {
    sessionStorage.setItem('frompath', from.path)
    next()
  },
  created () {
    this.type = this.$route.query.type
    // this.getAgent()
    this.getInfo()
    this.couponRecordtimes()
    this.frompath = sessionStorage.getItem('frompath')
  },
  methods: {
    /**
     * 结算及历史记录相关
     */
    couponRecordtimes () {
      Serve.couponRecord({ couponId: this.$route.query.id }).then(res => {
        if (res.data.code === 200) {
          this.couponRecordTime = res.data.data.map(item => {
            return item.replace(/T/, ' ')
          })
          // this.putawayTime = this.couponRecordTime[0]
          this.couponRecordTimeOne = this.couponRecordTime[0]
          // this.ajaxs = `${Deserve.Commod.couponRecordList}?couponId=${this.$route.query.id}&putawayTime=${this.putawayTime}`
          this.getHistoryData()
        }
      })
    },
    // 结算
    getHistoryData () {
      const data = {
        couponId: this.$route.query.id,
        putawayTime: this.couponRecordTimeOne
      }
      Serve.couponRecordSet(data).then(res => {
        if (res.data.code === 200) {
          this.couponRecordSets = res.data.data
        }
      })
      // 历史记录列表
      this.ajaxs = `${Deserve.Commod.couponRecordList}?couponId=${data.couponId}&putawayTime=${data.putawayTime}`
    },
    // 获取所属商家
    getAgent () {
      Serve.saveListAll().then(res => {
        if (res.data) {
          this.agentList = res.data.data
        }
      })
    },
    // 获取详情
    getInfo () {
      Serve.sortInfo({ id: this.$route.query.id }).then(res => {
        if (res.data) {
          this.basicForm = res.data.data
          Serve.saveListAll().then(res => {
            if (res.data) {
              this.agentList = res.data.data
              for (const item of this.agentList) {
                if (item.id === this.basicForm.shopId) {
                  this.merchantName = item.name
                }
              }
            }
          })
          if (res.data.data.grantType === '自定义') {
            this.tableData = JSON.parse(res.data.data.grantCollection)
          }
          this.basicForm.couponImg = this.basicForm.couponImg !== '' ? JSON.parse(this.basicForm.couponImg) : ''
          // this.basicForm.date = [this.basicForm.beginTime.split(','), this.basicForm.endTime.split(',')]
          // const a = this.basicForm.date[0].split(':')
          // const b = this.basicForm.date[1].split(':')
          // this.basicForm.date = [new Date(a[0], a[1], a[2], a[3], a[4], a[5]), new Date(b[0], b[1], b[2], b[3], b[4], b[5])]
          // this.basicForm.hairDate = [this.basicForm.grantStartTime.split(','), this.basicForm.grantEndTime.split(',')]
          // const c = this.basicForm.hairDate[0].split(':')
          // const d = this.basicForm.hairDate[1].split(':')
          // this.basicForm.hairDate = [new Date(0, 0, 0, c[0], c[1], 0), new Date(0, 0, 0, d[0], d[1], 0)]
        }
      })
    },
    // 审核
    getAddAgent () {
      let a
      if (this.status === '通过') {
        a = 0
      } else {
        a = 1
      }
      Serve.sortType({ id: this.$route.query.id, status: a }).then(res => {
        if (res.data) {
          Message.success('审核成功')
          this.$router.push(this.frompath)
        }
      })
    },
    // 返回
    getPath () {
      this.$router.push(this.frompath)
    }
  }
}
</script>
<style lang="less" scoped>
.container {
  width: 100%;
  box-sizing: border-box;
  padding: 10px;
  background-color: #fff;
  .title {
    font-size: 15px;
    color: #333;
    box-sizing: border-box;
    padding: 10px 0;
    width: 100%;
    text-align: left;
    border-bottom: 1px solid #999;
  }
  .form {
    width: 100%;
    box-sizing: border-box;
    font-size: 13px;
    padding: 22px 0;
    .item {
      display: flex;
      align-items: center;
      /deep/.el-form-item {
        width: 300px!important;
        margin-right: 250px;
        /deep/.el-form-item__content {
          width: fit-content;
        }
      }
    }
  }
  .el-form /deep/.el-form-item .el-input.is-disabled .el-input__inner{
    border: 1px solid #DCDFE6!important;
    color: #606266!important;
    background-color:#fff!important;
  }
  .btnbox {
    display: flex;
    justify-content: center;
  }
  .title {
    font-size: 15px;
    color: #333;
    box-sizing: border-box;
    padding: 10px 0;
    width: 100%;
    text-align: left;
    border-bottom: 1px solid #999;
  }
  .freezon{
      font-size: 13px;
      .freezonItem{
          display: flex;
          align-items: center;
          margin: 20px 0;
          .money{
              display: flex;
              align-items: center;
              margin-right: 50px;
          }
      }
  }
}
</style>
