<template>
  <div id="container">
    <!-- <div class="title">基本信息</div> -->
    <div slot="header" class="clearfix title">
      <span style="float: left;">基本信息</span>
      <el-button style="float: right; padding: 6px" type="success" @click="getPath">返回</el-button>
    </div>
    <el-form label-width="120px" label-position="left" ref="basicForm" :rules="rules" :model="basicForm">
      <el-col :span="12">
        <el-form-item label="优惠券简介：">
        <el-input v-model="basicForm.couponName" placeholder="优惠券简介" size="mini" style="width:200px;"></el-input>
      </el-form-item>
      <el-form-item label="所属平台：" prop="couponPlatform">
        <el-radio-group v-model="basicForm.couponPlatform" @change="comCoods">
          <el-radio label="0">饿了么</el-radio>
          <el-radio label="1">美团</el-radio>
        </el-radio-group>
      </el-form-item>
       <el-form-item label="发放方式：" prop="grantType">
        <el-radio-group v-model="basicForm.grantType" @change="getTable" style="width:300px;">
            <el-radio label="0">每日等量</el-radio>
            <el-radio label="1">总量发放</el-radio>
            <el-radio label="2">自定义</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="发放数量：" prop="grantCount" v-if="basicForm.grantType === '0'">
        <el-input v-model="basicForm.grantCount" @input="getTable" size="mini" placeholder="发放数量"></el-input>
      </el-form-item>
      <el-form-item label="发放总量：" prop="grantCountTotal" v-if="basicForm.grantType === '1'">
        <el-input v-model="basicForm.grantCountTotal" @input="getTable" size="mini" placeholder="发放数量"></el-input>
      </el-form-item>
      <el-form-item label="平台佣金：" prop="commission">
        <el-input v-model="basicForm.commission" size="mini" placeholder="佣金金额"></el-input>
      </el-form-item>
      <el-form-item label="优惠券标签：" prop="">
        <!-- <el-input v-model="basicForm.couponLabel" size="mini" placeholder="不同标签使用|隔开(最多两个)"></el-input> -->
        <el-tag
  :key="tag"
  v-for="tag in dynamicTags"
  closable
  :disable-transitions="false"
  @close="handleClose(tag)">
  {{tag}}
</el-tag>
<el-input
  class="input-new-tag"
  v-if="inputVisible"
  v-model.trim="basicForm.couponLabel"
  ref="saveTagInput"
  size="small"
  @keyup.enter.native="handleInputConfirm"
  @blur="handleInputConfirm"
>
</el-input>
<el-button v-else class="button-new-tag" size="small" @click="showInput">+ 标签</el-button>

      </el-form-item>
      <!-- <el-form-item label="优惠券价格：" prop="couponPrice">
        <el-input v-model="basicForm.couponPrice" size="mini" placeholder="发放金额"></el-input>
      </el-form-item> -->
      <el-form-item label="返利规则（会员）：">
        <div style="display:flex">
          <div style="display: flex;">
            <span><pre>满 </pre></span><el-input v-model="basicForm.couponMerchantPrice" size="mini" placeholder=""></el-input>
          </div>
          <div style="display: flex;">
            <span><pre> 减 </pre></span><el-input v-model="basicForm.memberCashBack" size="mini" placeholder=""></el-input>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="图片上传">
          <el-upload
            class="upload-demo"
            action=""
            :show-file-list="true"
            :beforeUpload="beforeUploadPicture"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            multiple
            :file-list="fileList"
            list-type="picture"
            style="text-align: left">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="所属店铺：" prop="shopId">
        <el-select v-model="basicForm.shopId" size="mini" filterable placeholder="请选择所属店铺" @change="comCoods">
          <el-option v-for="(item,index) in shopList" :key="index+''" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="活动时间：" style="margin-right: 150px;" prop="date">
        <el-date-picker
          style="width:300px;"
          size="mini"
          @change="getTable"
          v-model="basicForm.date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
          </el-date-picker>
      </el-form-item>
      <el-form-item label="发放时间：" class="timeRg">
        <el-time-picker
          style="width:120px"
          v-model="valusse1"
          @change="getval1"
          size="mini"
          placeholder="开始时间">
        </el-time-picker>
        至
        <el-time-picker
          style="width:120px"
          v-model="valdfue2"
          @change="getval2"
          size="mini"
          placeholder="结束时间">
        </el-time-picker>
      </el-form-item>
      <el-form-item label="是否跨天：" prop="">
        <el-radio-group v-model="basicForm.crossDay"  >
            <el-radio :label="true">是</el-radio>
            <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="发放数量：" prop="grantCount" v-if="basicForm.grantType === '0'">
        <el-input v-model="basicForm.grantCount" @input="getTable" size="mini" placeholder="发放数量"></el-input>
      </el-form-item>
      <el-form-item label="发放总量：" prop="grantCountTotal" v-if="basicForm.grantType === '1'">
        <el-input v-model="basicForm.grantCountTotal" @input="getTable" size="mini" placeholder="发放数量"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="优惠券价格：" prop="couponPrice">
        <el-input v-model="basicForm.couponPrice" size="mini" placeholder="发放金额"></el-input>
      </el-form-item> -->
       <el-form-item label="发放天数：">
        <el-input v-model="basicForm.couponday" size="mini" disabled placeholder="发放天数"></el-input>
      </el-form-item>
       <el-form-item label="返利规则（普通）：">
        <div style="display:flex">
          <div style="display: flex;">
            <span><pre>满 </pre></span><el-input v-model="basicForm.couponMerchantPrice" size="mini" placeholder=""></el-input>
          </div>
          <div style="display: flex;">
            <span><pre> 减） </pre></span><el-input v-model="basicForm.cashBack" size="mini" placeholder=""></el-input>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="使用规则：" style="text-align:left">
        <el-tag effect="dark" size="mini" type="success">过期作废</el-tag>
        <el-tag effect="dark" size="mini" type="success">不与商家好评返现卡同享</el-tag>
      </el-form-item>
      <el-form-item label="是否上架：">
        <el-select v-model="basicForm.couponStatus" size="mini" style="width:200px;" placeholder="请选择">
          <el-option label="上架" value="0"></el-option>
          <el-option label="下架" value="1"></el-option>
        </el-select>
      </el-form-item>
      </el-col>
        <div style="width:100%;display:flex">
          <el-form-item label="好评要求：" style="width:30%" prop="evaluationRequirements">
            <el-select v-model="basicForm.evaluationRequirements" size="mini" style="width:200px;" placeholder="请选择">
              <el-option label="不需评价" value="0"></el-option>
              <el-option label="仅5星" value="1"></el-option>
              <el-option label="5星+10字" value="2"></el-option>
              <el-option label="5星+1图+20字" value="3"></el-option>
              <el-option label="5星+3图+20字" value="4"></el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="收单价：" style="width:30%" prop="serviceCharge">
            <el-input v-model="basicForm.serviceCharge" size="mini" placeholder="收单价"></el-input>
          </el-form-item>
          <el-form-item label="购买要求备注：" style="width:30%" prop="consumeRemark">
            <el-input type="textarea" v-model="basicForm.consumeRemark" size="mini" placeholder="购买要求备注"></el-input>
          </el-form-item> -->
        </div>
        <div style="width: 100%;">
          <el-form-item label="" style="text-align:left">
            <el-button size="mini" type="success" @click="preventDuplicate">保存</el-button>
          </el-form-item>
        </div>
    </el-form>
    <div v-if="basicForm.grantType === '2'" class="title">发放情况</div>
    <div v-if="basicForm.grantType === '2'">
      <el-table :data="tableData" style="width: 80%;height: 200px;overflow-y: auto;" border>
        <el-table-column prop="time" label="时间" ></el-table-column>
        <el-table-column prop="count" label="日发放数量" >
           <template slot-scope="scope">
             <el-input v-model="scope.row.count"/>
           </template>
        </el-table-column>
      </el-table>
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
              <el-table-column label="操作" width="280">
                <template slot-scope="scope">
                  <el-button @click="getNum(scope.row)" type="primary" :disabled="ifStatus === '下架'" size="small">修改总量</el-button>
                </template>
              </el-table-column>
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
    <el-button v-if="ids !== 'add' && ifStatus !== '下架'" type="primary" size="mini" @click="getAddAgent">下架结算</el-button>
    <el-dialog
        title=""
        :visible.sync="comCoodsId"
        width="30%">
        <span style="font-size: 0.2rem">此店铺暂未有相关平台配送范围，是否前往此店铺添加？</span>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="comCoodsId = false">选择其他店铺</el-button>
            <el-button @click="getSaveInfo">前往此店铺添加</el-button>
        </span>
    </el-dialog>
    <!-- 修改总量 -->
    <el-dialog
          :visible.sync="nums"
          width="20%">
          <span>
            <el-form style="width:100%;">
              <el-form-item label="总量" style="display:flex">
                <el-input v-model="total"></el-input>
              </el-form-item>
            </el-form>
          </span>
          <span slot="footer" class="dialog-footer">
              <el-button @click="nums = false">取消</el-button>
              <el-button type="primary" @click="getnums">确定</el-button>
          </span>
      </el-dialog>
  </div>
</template>
<script>
import Times from '@/serve/date'
import { Message } from 'element-ui'
import Tables from '@/components/table.vue'
import Serve from '@/serve/hc-api/commod/commod'
import Deserve from '@/serve/hc-api/hc-server'
// import FormTime from '@/serve/date'
export default {
  components: {
    Tables
  },
  data () {
    return {
      // 基本信息
      // 品类
      basicForm: {
        couponName: '',
        shopName: '',
        couponPlatform: '',
        couponMerchantPrice: '',
        // 活动时间
        date: '',
        beginTime: '',
        endTime: '',
        grantConllection: '', // 发放方式
        // 发放时间
        hairDate: [new Date(0, 0, 0, 10, 0, 0), new Date(0, 0, 0, 23, 0, 0)],
        grantStartTime: '',
        grantEndTime: '',
        couponTotal: '',
        couponPrice: '',
        cashBack: '',
        evaluationRequirements: '',
        grantCollection: [],
        shopId: '',
        couponStatus: '1',
        // 获取到家人配送范围id
        platformCoordinate: '',
        serviceCharge: '',
        consumeRemark: ''
      },
      timer: null,
      lock: true,
      // 发放情况
      tableData: [
      ],
      // 列表时间
      imgArr: [],
      fileList: [],
      tableTime: [],
      dialogVisible: false,
      editForm: {
        address: ''
      },
      formdata: '',
      fileName: '',
      shopList: [],
      // 验证
      rules: {
        couponName: [
          { required: true, message: '请输入优惠券名称', trigger: 'blur' }
        ],
        commission: [
          { required: true, message: '请输入平台佣金', trigger: 'blur' }
        ],
        shopId: [
          { required: true, message: '请选择所属店铺', trigger: 'blur' }
        ],
        couponPlatform: [
          { required: true, message: '请选择所属平台', trigger: 'change' }
        ],
        date: [
          { required: true, message: '请选择活动时间', trigger: 'change' }
        ],
        grantType: [
          { required: true, message: '请选择发放方式', trigger: 'change' }
        ],
        grantCount: [
          { required: true, message: '请输入发放数量', trigger: 'blur' }
        ],
        grantCountTotal: [
          { required: true, message: '请输入发放总量', trigger: 'blur' }
        ],
        couponPrice: [
          { required: true, message: '请输入发放金额', trigger: 'blur' }
        ],
        cashBack: [
          { required: true, message: '请输入返利金额', trigger: 'blur' }
        ],
        evaluationRequirements: [
          { required: true, message: '请选择好评要求', trigger: 'change' }
        ],
        couponStatus: [
          { required: true, message: '请选择是否上架', trigger: 'change' }
        ],
        serviceCharge: [
          { required: true, message: '请输入收单价', trigger: 'blur' }
        ],
        consumeRemark: [
          { required: true, message: '请输入购买要求备注', trigger: 'blur' }
        ]
      },
      comCoodsId: false,
      dynamicTags: [],
      inputVisible: false,
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
        { prop: 'fee', label: '收单金额' },
        { prop: 'settlement', label: '结算金额' },
        { prop: 'putawayTime', label: '时间' },
        { prop: 'invalid', label: '状态' }
      ],
      ajaxs: '',
      // 修改总量
      nums: false,
      total: '',
      numId: '',
      // 初始化id
      ids: '',
      // 状态，日过没有变化就不传过去
      ifStatus: '',
      // 发放时间
      valusse1: new Date(0, 0, 0, 10, 0, 0), // 开始时间
      valdfue2: new Date(0, 0, 0, 23, 0, 0), // 结束时间,
      // 来源路径
      frompath: ''
    }
  },
  beforeRouteEnter (to, from, next) {
    sessionStorage.setItem('frompath', from.path)
    next()
  },
  created () {
    this.frompath = sessionStorage.getItem('frompath')
    this.getStartInfo()
    this.ids = this.$route.query.id
    if (this.$route.query.id !== 'add') {
      this.couponRecordtimes()
    }
  },
  methods: {
    getdatas () {
      let a = ''
      let b = ''
      a = Times.getDate(this.basicForm.date[0])
      b = Times.getDate(this.basicForm.date[1])
      this.$set(this.basicForm, 'date', [new Date(a), new Date(b)])
    },
    /**
     * 结算及历史记录相关
     */
    couponRecordtimes () {
      Serve.couponRecord({ couponId: this.$route.query.id }).then(res => {
        if (res.data.code === 200) {
          // this.couponRecordTime = res.data.data.map(item => {
          //   return item.replace(/T/, ' ')
          // })
          this.couponRecordTime = res.data.data
          this.couponRecordTimeOne = this.couponRecordTime[0]
          this.getHistoryData()
        }
      })
    },
    handleClose (tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
    },
    showInput () {
      if (this.dynamicTags.length >= 2) {
        Message.warning('最多添加两个标签')
        return false
      }
      this.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleInputConfirm () {
      const inputValue = this.basicForm.couponLabel
      if (inputValue) {
        this.dynamicTags.push(inputValue)
      }
      this.inputVisible = false
      this.basicForm.couponLabel = ''
    },
    // 结算
    getHistoryData () {
      const data = {
        couponId: this.$route.query.id,
        putawayTime: this.couponRecordTimeOne
      }
      if (data.putawayTime === undefined || data.putawayTime === 'undefined') {
        data.putawayTime = ''
      }
      Serve.couponRecordSet(data).then(res => {
        if (res.data.code === 200) {
          this.couponRecordSets = res.data.data
        }
      })
      // 历史记录列表
      this.ajaxs = `${Deserve.Commod.couponRecordList}?couponId=${data.couponId}&putawayTime=${data.putawayTime}`
    },
    // 修改总量
    getNum (val) {
      this.nums = true
      this.total = val.totalNum
      this.numId = val.id
    },
    getnums () {
      if (this.total !== '') {
        Serve.couponNum({ id: this.numId, total: this.total }).then(res => {
          if (res.data.code === 200) {
            Message.success('修改成功')
            this.nums = false
            if (this.ajaxs === `${Deserve.Commod.couponRecordList}?couponId=${this.$route.query.id}&putawayTime=${this.couponRecordTimeOne}`) {
              this.ajaxs = `${Deserve.Commod.couponRecordList}?tests=&couponId=${this.$route.query.id}&putawayTime=${this.couponRecordTimeOne}`
            } else {
              this.ajaxs = `${Deserve.Commod.couponRecordList}?couponId=${this.$route.query.id}&putawayTime=${this.couponRecordTimeOne}`
            }
          }
        })
      } else {
        Message.success('请输入总量')
        return false
      }
    },
    // 下架结算
    getAddAgent () {
      Serve.sortType({ id: this.$route.query.id, status: 1 }).then(res => {
        if (res.data) {
          Message.success('已成功下架结算')
          this.$router.push('/commodAdminlist')
        }
      })
    },
    /**
     * 基本信息相关
     */
    // 加入配送范围id
    comCoods () {
      if (this.basicForm.couponPlatform !== '' && this.basicForm.shopId !== '') {
        const data = {
          platform: this.basicForm.couponPlatform,
          shopId: this.basicForm.shopId
        }
        Serve.comCood(data).then(res => {
          if (res.data.data !== null) {
            this.basicForm.platformCoordinate = res.data.data
          } else {
            this.comCoodsId = true
          }
        })
      }
    },
    // 返回列表
    getSaveInfo () {
      this.$router.push({ name: 'saveaddEdit', query: { id: this.basicForm.shopId } })
    },
    // 图片上传前
    beforeUploadPicture (file) {
      this.fileName = file.name
      this.formdata = new FormData()
      this.formdata.append('file', file)
      Serve.fileup(this.formdata).then(res => {
        if (res.data) {
          Message.success('上传成功')
          this.imgArr.push(res.data.data)
          this.fileList.push({
            url: res.data.data
          })
        }
      })
    },
    handleRemove (file, fileList) {
      console.log(fileList, '909090')
      this.fileList = this.fileList.filter(item => {
        return item.url !== file.url
      })
      this.imgArr = this.fileList.map(item => {
        if (item.url !== file.url) {
          return item.url
        }
      })
      console.log(this.imgArr, '11212')
    },
    beforeRemove (file, fileList) {
      return this.$confirm('确定移除选中图片？')
    },
    // 自定义数量操作
    getTable () {
      this.tableData = []
      const a = Times.formatEveryDay(Times.getDate(this.basicForm.date[0]), Times.getDate(this.basicForm.date[1]))
      // this.basicForm.couponday = a.length
      this.$set(this.basicForm, 'couponday', a.length)
      if (this.basicForm.grantType === '2') {
        for (const item of a) {
          this.tableData.push({
            time: item,
            count: 0
          })
        }
      }
    },
    // 。。
    getStartInfo () {
      Serve.saveListAll().then(res => {
        if (res.data) {
          this.shopList = res.data.data
          if (this.$route.query.shopId) {
            this.basicForm.shopId = this.$route.query.shopId
          }
        }
      })
      // 获取id，发送请求获取详细信息
      if (this.$route.query.id === 'add') {
        // ...
      } else {
        Serve.sortInfo({ id: this.$route.query.id }).then(res => {
          if (res.data) {
            console.log(res.data.data, '优惠券详情')
            this.basicForm = res.data.data
            // 图片回显
            if (this.basicForm.couponImg !== '') {
              this.imgArr = JSON.parse(this.basicForm.couponImg)
              for (const item of this.imgArr) {
                this.fileList.push({
                  name: '',
                  url: item
                })
              }
            } else {
              this.imgArr = []
              this.fileList = []
            }
            this.ifStatus = this.basicForm.couponStatus
            this.basicForm.couponPlatform = this.basicForm.couponPlatform === '饿了么' ? '0' : this.basicForm.couponPlatform === '美团' ? '1' : ''
            this.basicForm.grantType = this.basicForm.grantType === '总量' ? '1' : this.basicForm.grantType === '每日等量' ? '0' : this.basicForm.grantType === '自定义' ? '2' : ''
            if (this.basicForm.grantCollection !== '') {
              this.basicForm.grantCollection = JSON.parse(this.basicForm.grantCollection)
              this.tableData = this.basicForm.grantCollection
            }
            this.$set(this.basicForm, 'date', [new Date(this.basicForm.beginTime), new Date(this.basicForm.endTime)])
            // this.basicForm.date = [new Date(this.basicForm.beginTime), new Date(this.basicForm.endTime)]
            this.basicForm.couponday = Times.formatEveryDay(Times.getDate(this.basicForm.date[0]), Times.getDate(this.basicForm.date[1])).length
            // this.basicForm.couponday = [new Date(this.basicForm.grantCollection[0].time), new Date(this.basicForm.grantCollection[this.basicForm.grantCollection.length - 1].time)]
            // this.basicForm.hairDate = [new Date(this.basicForm.grantStartTime), new Date(this.basicForm.grantEndTime)]
            let counyStartTime = this.basicForm.grantStartTime
            let counyEndTime = this.basicForm.grantEndTime
            if (counyStartTime.indexOf(':') > 0) {
              counyStartTime = counyStartTime.split(':')
            }
            if (counyEndTime.indexOf(':') > 0) {
              counyEndTime = counyEndTime.split(':')
            }
            this.valusse1 = new Date(0, 0, 0, counyStartTime[0], counyStartTime[1], counyStartTime[2])
            this.valdfue2 = new Date(0, 0, 0, counyEndTime[0], counyEndTime[1], counyEndTime[2])
            this.dynamicTags = this.basicForm.couponLabel.split('|')
            // this.$set(this.basicForm, 'hairDate', [new Date(this.basicForm.grantStartTime), new Date(this.basicForm.grantEndTime)])
            // this.$set(this.valusse1, [new Date(0, 0, 0, counyStartTime[0], counyStartTime[1], counyStartTime[2]), new Date(0, 0, 0, counyEndTime[0], counyEndTime[1], counyEndTime[2])])
          }
        })
      }
    },
    getval1 () {
    },
    getval2 () {
    },
    /**
     * 基本信息
     * **/
    // 提交基本信息
    getbasicInfo () {
      // 活动时间提示
      if (this.basicForm.shopId === '' || !this.basicForm.shopId) {
        Message.warning('请选择所属店铺')
        return false
      }
      if (this.basicForm.date === '' || !this.basicForm.date || this.basicForm.date.length < 2) {
        Message.warning('请选择活动时间')
        return false
      }
      if (parseFloat(this.basicForm.couponMerchantPrice) < 0 || this.basicForm.couponMerchantPrice === '' || this.basicForm.couponMerchantPrice === null || !this.basicForm.couponMerchantPrice) {
        Message.warning('请输入不小于0的满足金额')
        return false
      }
      if (parseFloat(this.basicForm.cashBack) < 0 || this.basicForm.cashBack === '' || this.basicForm.cashBack === null || !this.basicForm.cashBack) {
        Message.warning('请输入不小于0减免金额')
        return false
      }
      if (parseFloat(this.basicForm.commission) < 0 || this.basicForm.commission === '' || this.basicForm.commission === null || !this.basicForm.commission) {
        Message.warning('请输入不小于0平台佣金')
        return false
      }
      if (parseFloat(this.basicForm.memberCashBack) < 0 || this.basicForm.memberCashBack === '' || this.basicForm.memberCashBack === null || !this.basicForm.memberCashBack) {
        Message.warning('请输入不小于0会员减免金额')
        return false
      }
      // if (this.basicForm.serviceCharge === '') {
      //   Message.warning('请输入收单价')
      //   return false
      // }
      const a = {
        id: '',
        beginTime: `${Times.getDate(this.basicForm.date[0])} ${Times.getTime(this.basicForm.date[0])}`,
        cashBack: this.basicForm.cashBack,
        couponName: this.basicForm.couponName,
        couponPlatform: this.basicForm.couponPlatform,
        couponPrice: 0.5,
        couponMerchantPrice: this.basicForm.couponMerchantPrice,
        // couponStatus: this.basicForm.couponStatus,
        dayCount: this.basicForm.dayCount,
        endTime: `${Times.getDate(this.basicForm.date[1])} ${Times.getTime(this.basicForm.date[1])}`,
        evaluationRequirements: this.basicForm.evaluationRequirements,
        grantType: parseInt(this.basicForm.grantType),
        grantStartTime: `${Times.getTime(this.valusse1)}`,
        grantEndTime: `${Times.getTime(this.valdfue2)}`,
        shopId: this.basicForm.shopId,
        grantCount: this.basicForm.grantCount,
        grantCountTotal: this.basicForm.grantCountTotal,
        grantCollection: this.tableData,
        couponImg: JSON.stringify(this.imgArr),
        platformCoordinate: this.basicForm.platformCoordinate,
        serviceCharge: this.basicForm.serviceCharge,
        consumeRemark: this.basicForm.consumeRemark,
        commission: this.basicForm.commission,
        couponLabel: this.dynamicTags.join('|'),
        memberCashBack: this.basicForm.memberCashBack,
        crossDay: this.basicForm.crossDay
      }
      for (const item of this.shopList) {
        if (item.id === a.shopId) {
          a.shopName = item.name
        }
      }
      if (this.$route.query.id === 'add') {
        a.couponStatus = this.basicForm.couponStatus
        Serve.sortAdd(a).then(res => {
          if (res.data) {
            Message.success('新增成功')
            this.$router.push(this.frompath)
          } else {
            // Message.error('请输入  信息')
          }
        })
      } else {
        a.id = this.$route.query.id
        // 判断编辑时是否传上架字段
        if (this.ifStatus === '上架' || this.ifStatus === '0' || this.ifStatus === 0) {
          this.ifStatus = 0
        } else if (this.ifStatus === '下架' || this.ifStatus === '1' || this.ifStatus === 1) {
          this.ifStatus = 1
        } else {
          this.ifStatus = '即将下架'
        }
        if (this.basicForm.couponStatus === '上架' || this.basicForm.couponStatus === '0' || this.basicForm.couponStatus === 0) {
          this.basicForm.couponStatus = 0
        } else if (this.basicForm.couponStatus === '下架' || this.basicForm.couponStatus === '1' || this.basicForm.couponStatus === 1) {
          this.basicForm.couponStatus = 1
        } else {
          this.basicForm.couponStatus = 3
        }

        console.log(this.basicForm.couponStatus)
        if (this.ifStatus !== this.basicForm.couponStatus) {
          a.couponStatus = this.basicForm.couponStatus
        } else {
          // delete a.couponStatus
        }
        a.couponStatus = this.basicForm.couponStatus
        // 优惠券类型
        // eslint-disable-next-line eqeqeq
        if (a.evaluationRequirements == '品鉴要求：不需评价' || a.evaluationRequirements === '0' || a.evaluationRequirements === 0) {
          a.evaluationRequirements = 0
        // eslint-disable-next-line eqeqeq
        } else if (a.evaluationRequirements == '品鉴要求：仅5星' || a.evaluationRequirements === '1' || a.evaluationRequirements === 1) {
          a.evaluationRequirements = 1
        // eslint-disable-next-line eqeqeq
        } else if (a.evaluationRequirements == '品鉴要求：5星+10字' || a.evaluationRequirements === '2' || a.evaluationRequirements === 2) {
          a.evaluationRequirements = 2
        // eslint-disable-next-line eqeqeq
        } else if (a.evaluationRequirements == '品鉴要求：5星+1图+20字' || a.evaluationRequirements === '3' || a.evaluationRequirements === 3) {
          a.evaluationRequirements = 3
        // eslint-disable-next-line eqeqeq
        } else if (a.evaluationRequirements == '品鉴要求：5星+3图+20字' || a.evaluationRequirements === '4' || a.evaluationRequirements === 4) {
          a.evaluationRequirements = 4
        }
        Serve.sortEdit(a).then(res => {
          if (res.data) {
            Message.success('编辑成功')
            this.$router.push(this.frompath)
          } else {
          }
        })
      }
    },
    preventDuplicate () {
      console.log(this.lock, '000')
      if (this.lock) {
        this.timer = null
        this.getbasicInfo()
        this.lock = false
      } else {
        Message.warning('请不要重复提交')
      }
      this.timer = setTimeout(() => {
        this.lock = true
      }, 2000)
    },
    // 返回列表
    getPath () {
      this.$router.push(this.frompath)
    }
  }
}
</script>
<style lang="less" scoped>
 .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
#container{
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
    // border-bottom: 1px solid #999;
    margin-bottom: 10px;
  }
  .el-form{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    /deep/.el-form-item{
      width: 500px;
      .el-form-item__content{
        width: 200px;
        .upload-demo{
          .el-upload-list,.el-upload-list--picture{
            display: flex;
            .el-upload-list__item{
              // width: 0.8rem;
              .el-upload-list__item-thumbnail{
                width:0.7rem;
                height:0.7rem
              }
            }
          }
        }
      }
    }
    /deep/.timeRg{
      .el-form-item__content{
        width:100%;
        text-align: left;
      }
    }
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
