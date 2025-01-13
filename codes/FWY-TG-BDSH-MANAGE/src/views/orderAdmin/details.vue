<template>
  <div id="order-detail">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>审核信息</span>
        <el-button style="float: right; padding: 6px" type="success" @click="getPath('/reviewlist')">返回</el-button>
      </div>
      <div class="text item">
        <el-form ref="form" :model="form" :rules="rules" label-width="1.8rem">
          <el-form-item label="下单平台:">
            <el-input v-model="form.orderBuild" disabled></el-input>
          </el-form-item>
          <el-form-item label="店铺名:">
            <el-input v-model="shopName" disabled></el-input>
          </el-form-item>
          <el-form-item label="优惠券规则:">
            <el-input v-model="comForm.msg" disabled></el-input>
          </el-form-item>
          <el-form-item label="会员返利规则：">
            <div style="display:flex">
              <div style="display: flex;width:50%">
                <span><pre>满 </pre></span><el-input disabled class="cash-back" v-model="form.couponMerchantPrice" placeholder=""></el-input>
              </div>
              <div style="display: flex;width:50%">
                <span><pre> 返 </pre></span><el-input disabled class="cash-back" v-model="form.memberCashBack" placeholder=""></el-input>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="非会员返利规则：">
            <div style="display:flex">
              <div style="display: flex;width:50%">
                <span><pre>满 </pre></span><el-input disabled class="cash-back" v-model="form.couponMerchantPrice" placeholder=""></el-input>
              </div>
              <div style="display: flex;width:50%">
                <span><pre> 返 </pre></span><el-input disabled class="cash-back" v-model="form.cashBack" placeholder=""></el-input>
              </div>
            </div>
          </el-form-item>
          <!-- <el-form-item label="购买要求备注:">
            <el-input v-model="form.consumeRemark" disabled></el-input>
          </el-form-item> -->
          <el-form-item label="下单时间:">
            <el-input v-model="form.consumerOrderTime" disabled></el-input>
          </el-form-item>
          <el-form-item label="平台下单单号:">
            <el-input v-model="form.consumeOrderNo" :disabled="types === 'detail'"></el-input>
          </el-form-item>
          <!-- <el-form-item label="平台下单金额">
            <el-input v-model="form.consumeAmount" :disabled="types === 'detail'"></el-input>
          </el-form-item> -->
          <el-form-item label="是否通过:" v-if="types !== 'detail'">
            <el-radio-group v-model="status" style="width:500px" @change="getRemark">
              <el-radio label="通过"></el-radio>
              <el-radio label="拒绝"></el-radio>
            </el-radio-group>
         </el-form-item>
         <div v-if="types !== 'detail' && status === '通过'">
            <el-form-item label="备注:">
              <el-input type="textarea" autosize :rows="2" v-model="remark"></el-input>
            </el-form-item>
          </div>
         <div v-if="types !== 'detail' && status === '拒绝'">
            <el-form-item label="备注:">
              <el-select v-model="remark" style="width:300px">
                <el-option v-for="(item,index) in refuseremark" :key="index+''" :label="item.name" :value="item.detail"></el-option>
              </el-select>
            </el-form-item>
          </div>
         <!-- <div v-if="types !== 'detail' && status === '退回修改'">
            <el-form-item label="备注:">
              <el-select v-model="remark" style="width:300px">
                <el-option show-overflow-tooltip v-for="(item,index) in returnremark" :key="index+''" :label="item.name" :value="item.detail"></el-option>
              </el-select>
            </el-form-item>
          </div> -->
          <div v-if="types !== 'detail'">
            <el-form-item label="" style="dispaly:block!important">
              <el-button type="success" @click="getReview('form')">提交</el-button>
            </el-form-item>
          </div>
        </el-form>
        <div class="imgBox">
          <!-- <div class="oneImags">
            <span class="title">订单号截图:</span>
            <img style="width:2rem;height:2rem;margin-left: 15px;" v-if="form.orderOnImg" :src="form.orderOnImg" alt="" @click="getimg(form.orderOnImg)">
            <span v-else>暂无截图</span>
          </div> -->
          <div class="oneImags">
            <!-- <span class="title">:</span> -->
            <p style="text-align: center;line-height: 20px;font-size: 14px;color: #606266;">订单详情截图:</p>
            <div v-if="form.orderDetailImg">
              <img v-for="(item,index) in form.orderDetailImg" :key="index+''" style="width:2rem;height:2rem;margin-left: 15px;" :src="item" alt="" @click="getimg(item)">
            </div>
            <p style="text-align: center;line-height: 20px;font-size: 14px;color: #606266;">评价详情截图:</p>
            <div v-if="form.evaluateImg">
              <img v-for="(item,index) in form.evaluateImg" :key="index+''" style="width:2rem;height:2rem;margin-left: 15px;" :src="item" alt="" @click="getimg(item)">
            </div>
            <!-- <span v-else>暂无截图</span> -->
            <!-- <img style="width:2rem;height:2rem;margin-left: 15px;" src="../../assets/images/favicon.jpg" alt=""> -->
          </div>
          <div class="remark-input">审核结果：<el-input disabled autosize type="textarea" class="remarks" v-model="form.orderRemark"></el-input></div>
        </div>
      </div>
    </el-card>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>基本信息</span>
      </div>
      <div class="text item">
        <el-form ref="form" :model="form" label-width="2rem">
            <el-form-item label="系统订单号:">
                <el-input v-model="form.orderOn" disabled></el-input>
            </el-form-item>
            <el-form-item label="消费者号码:">
                <el-input v-model="form.consumerPhone" disabled></el-input>
            </el-form-item>
            <el-form-item label="审核时间:">
                <el-input v-model="form.checkTime" disabled></el-input>
            </el-form-item>
            <el-form-item label="审核人:">
                <el-input v-model="form.platformAuditorId" disabled></el-input>
            </el-form-item>
            <el-form-item label="再次审核时间:" v-if="form.buildCheckTime !== null && form.buildCheckTime">
                <el-input v-model="form.buildCheckTime" disabled></el-input>
            </el-form-item>
            <el-form-item label="审核人:" v-if="form.buildCheckTime !== null && form.buildCheckTime">
                <el-input v-model="form.orderOnImg" disabled></el-input>
            </el-form-item>
            <el-form-item v-if="form.orderCheck === '订单已取消'" label="取消订单时间:">
                <el-input v-model="form.cancelTime" disabled></el-input>
            </el-form-item>
            <el-form-item label="返利到账时间:">
                <el-input v-model="form.arrivesTime" disabled></el-input>
            </el-form-item>
            <el-form-item label="审核备注:">
                <el-input v-model="form.checkRefuseRemark" type="textarea" disabled></el-input>
            </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-dialog class="delet" title="" :visible.sync="bigImg" width="5.4rem">
      <div style="display: flex;justify-content: space-between;width:4rem">
        <i style="font-size: 40px;display: flex;align-items: center;" class="el-icon-arrow-left" @click="getLsatImg"></i>
        <div>
          <p style="font-size:14px;font-weight:700">{{nowImg.name}}</p>
          <img :src="nowImg.img" alt="" style="width:4rem">
        </div>
        <i style="font-size: 40px;display: flex;align-items: center;" class="el-icon-arrow-right" @click="getNextImg"></i>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import Serve from '@/serve/hc-api/order'
import { Message } from 'element-ui'
import formRules from '@/serve/formRules.js'
export default {
  data () {
    return {
      // 查看大图图片
      bigImg: false,
      linkUrk: '',
      form: {
      },
      ids: '',
      status: '通过',
      refuseremark: [
        { label: '实付金额不满足商家补贴要求', val: '实付金额不满足商家补贴要求' },
        { label: '购买产品不满足商家补贴要求', val: '购买产品不满足商家补贴要求' },
        { label: '购买店铺或平台与商家补贴店铺或平台不符', val: '购买店铺或平台与商家补贴店铺或平台不符（联系客服，进行线下重新提交资料审核）' },
        { label: '禁止使用到店自取配送服务', val: '禁止使用到店自取配送服务' },
        { label: '使用虚假资料上传', val: '使用虚假资料上传' },
        { label: '评价未带图', val: '评价未带图' },
        { label: '评价非五星', val: '评价非五星' },
        { label: '订单超时提交', val: '订单超时提交' }
      ],
      returnremark: [
        { label: '提交资料缺少项', val: '提交资料缺少项“①订单详情截图（包含店铺名和产品详情）；“②订单详情截图（实付金额，订单号，下单时间）”；“③好评凭证截图”（安卓手机可以截屏①+②长图）' },
        { label: '美团外卖好评截图提交不规范', val: '美团外卖好评截图提交不规范，必须从【APP首页右下角-我的-我的评价】截图好评凭证' },
        { label: '饿了么好评截图提交不规范', val: '饿了么好评截图提交不规范，必须从【APP首页右下角-订单-订单详情-我的评价】截图好评凭证（饿了么APP上传带图好评后，等1分钟后“我的评价”中图片才会加载出来，另外用支付宝购买外卖，请切换APP，否则无法找到好评)退回修改' },
        { label: '订单号提交错误', val: '订单号提交错误' }
      ],
      remark: '',
      types: '',
      // 审核验证规则
      rules: {
        orderRemark: [
          { required: true, message: '请输入下单单号', trigger: 'blur' }
        ],
        handlingFee: [
          { required: true, validator: formRules.clearNoNum, trigger: 'blur' }
        ]
      },
      // 店铺名
      shopName: '',
      comForm: {},
      // 大图轮播
      bigImgSwipe: [],
      nowImg: {},
      // 当前图片索引
      indexs: 0
    }
  },
  created () {
    this.getStartInfo()
  },
  mounted () {
    var _this = this
    document.addEventListener('keydown', function (e) {
      if (e.keyCode === 37) {
        _this.getLsatImg()
      } else if (e.keyCode === 39) {
        _this.getNextImg()
      }
    })
  },
  methods: {
    // 切换备注选项
    getRemark () {
      this.remark = ''
      const data = { type: '' }
      if (this.status === '通过') {
        data.type = 3
        return false
      } else if (this.status === '拒绝') {
        data.type = 5
      }
      Serve.reviewType(data).then(res => {
        if (res.data.code === 200) {
          if (this.status === '拒绝') {
            this.refuseremark = res.data.data
          } else if (this.status === '退回修改') {
            this.returnremark = res.data.data
          }
        }
      })
    },
    // 查看图片大图
    getimg (val) {
      this.bigImg = true
      for (let i = 0; i < this.bigImgSwipe.length; i++) {
        if (val === this.bigImgSwipe[i].img) {
          this.nowImg = this.bigImgSwipe[i]
          this.indexs = i
        }
      }
    },
    getLsatImg () {
      if (this.indexs > 0) {
        this.indexs = this.indexs - 1
      } else {
        this.indexs = this.bigImgSwipe.length - 1
      }
      this.nowImg = this.bigImgSwipe[this.indexs]
    },
    getNextImg () {
      if (this.indexs < this.bigImgSwipe.length - 1) {
        this.indexs = this.indexs + 1
      } else {
        this.indexs = 0
      }
      this.nowImg = this.bigImgSwipe[this.indexs]
    },
    getStartInfo () {
      this.ids = this.$route.query.id
      this.types = this.$route.query.type
      Serve.infoOrder({ id: this.$route.query.id }).then(res => {
        if (res.data.code === 200) {
          this.form = res.data.data
          this.form.orderDetailImg = res.data.data.orderDetailImg.split(',')
          this.form.evaluateImg = res.data.data.evaluateImg.split(',')
          // const orderDetailImgLg = this.form.orderDetailImg.length
          // this.bigImgSwipe = [{ img: res.data.data.orderOnImg, name: '初审截图' }]
          const list = []
          for (let i = 0; i < this.form.orderDetailImg.length; i++) {
            list.push({
              img: res.data.data.orderDetailImg[i],
              name: '初审截图' + (i + 1)
            })
          }
          for (let i = 0; i < this.form.evaluateImg.length; i++) {
            list.push({
              img: res.data.data.evaluateImg[i],
              name: '二审截图' + (i + 1)
            })
          }
          this.bigImgSwipe = list
          console.log(list)
          // this.bigImgSwipe.push({ img: res.data.data.evaluateImg, name: '评价截图' })
          Serve.saveName({ id: res.data.data.shopId }).then(res => {
            if (res.data.code === 200) {
              this.shopName = res.data.data
            }
          })
          Serve.comName({ id: this.$route.query.id }).then(res => {
            if (res.data.code === 200) {
              this.comForm = res.data.data
            }
          })
        }
      })
    },
    getReview (formName) {
      this.$refs[formName].validate((valid) => {
        // eslint-disable-next-line prefer-const
        let back = {
          agree: false,
          note: '',
          orderId: ''
        }
        if (valid) {
          const a = {
            id: this.ids,
            status: 1,
            remark: this.remark
          }
          if (this.status === '通过') {
            a.status = 3
          } else if (this.status === '拒绝') {
            a.status = 14
            console.log(this.remark, '备注')
            if (this.remark === '') {
              Message.warning('请选择拒绝原因')
              return false
            }
          }
          console.log(this.form, '00000000000000')
          back.agree = true
          back.note = this.remark
          back.orderId = this.$route.query.id
          /*  Serve.reviewSave(a).then(res => {
            if (res.data) {
              this.dialogVisible = true
              Message.success('提交审核成功')
              this.getPath('/reviewlist')
            }
          }) */
          Serve.cashBack(back).then(res => {
            if (res.data.code === 200) {
              Message.success(res.data.msg)
              this.getPath('/reviewlist')
            } else {
              Message.warning(res.data.msg)
            }
          })
        } else {
          return false
        }
      })
    },
    // 返回列表
    getPath (val) {
      this.$router.go(-1)
    }
  }
}
</script>
<style lang="less" scoped>
#order-detail{
  height: inherit;
  .box-card{
    text-align: left;
    .text{
      display: flex;
      .imgBox{
        // display: flex;
        // flex-wrap: wrap;
        .oneImags{
          display: flex;
          flex-direction: column;
          align-items: center;
          margin: 0 0 20px 0.8rem;
          .title{
            font-size: 14px;
            color: #606266;
          }
        }
        .remark-input{
          font-size: 14px;
          // .remarks{
          //   border: none;
          //   color: #606266;
          //   background-color:#fff;
          //   height:200px;
          //   /deep/.el-textarea__inner{
          //     border:none;
          //   }
          // }
          .el-textarea.is-disabled /deep/.el-textarea__inner{
            border: 0px solid #DCDFE6;
            color: #606266;
            background-color:#fff!important;
            height:200px;
          }
        }
      }
      .el-form{
        width: 60%;
        /deep/.el-form-item{
          display: inline-block;
          .el-form-item__content{
            width:300px;
            .cash-back{
              input{
                width:100%!important;
              }
            }
            .avatar-uploader{
              .el-upload{
                border: 1px dashed #d9d9d9;
                border-radius: 6px;
                cursor: pointer;
                position: relative;
                overflow: hidden
              }
              .el-upload:hover{
                border-color: #409EFF;
              }
              .avatar{
                width: 178px;
                height: 178px;
                display: block;
              }
              .avatar-uploader-icon{
                font-size: 28px;
                color: #8c939d;
                width: 178px;
                height: 178px;
                line-height: 178px;
                text-align: center;
              }
            }
            .el-input .el-input__inner{
              width: 300px
            }
            .el-input.is-disabled .el-input__inner{
              border: 1px solid #DCDFE6;
              color: #606266;
              background-color:#fff;
            }
            .el-textarea.is-disabled .el-textarea__inner{
              border: 1px solid #DCDFE6;
              color: #606266;
              background-color:#fff;
              height:200px;
            }
          }
        }
      }
    }
  }
}
// .el-select-dropdown__item{
//   display: inline-block;
// }
</style>
