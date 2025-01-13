<template>

	<view class="progress">
		<view class="title">
			<view class="title-box">
				<text>{{setstatus(orderData.status)}}</text>
				<image src="../../static/xiangqiangkefu.png" class="kefu" mode="" @click="getData"></image>

			</view>
				<view class="time">
				<text>返利金额</text><text class="time_money">￥{{orderData.cashBack}}</text>
			</view>
			
		</view>
		<view class="list">
			<view class="list-title">
				<text>返利流程</text>
			</view>
			<view class="list-box" v-if="!orderData.cancelTime">
				<view class="list-box-shu">
					<image src="../../static/images/home/jindudui.png" class="jindudui" mode="" v-if="orderData.completeTime"></image>
					<view class="color-ridus" v-else  ></view>
					<view class="color-shu" :class="[orderData.consumerSubmitTime?'color-b':'']"></view>
				</view>
				<view class="list-box-text">
					<view>提交资料</view>
					<view>{{orderData.completeTime}}</view>
				</view>
			</view>
	<!-- 		<view class="list-box" v-if="!orderData.cancelTime">
				<view class="list-box-shu">
					<view class="color-ridus" :class="[orderData.consumerSubmitTime?'color-ridus-b':'']"></view>
					<view class="color-shu" :class="[orderData.consumerSubmitTime?'color-b':'']"></view>
				</view>
				<view class="list-box-text">
					<view>提交二审资料</view>
					<view>{{orderData.consumerSubmitTime}}</view>
				</view>
			</view> -->
			<view class="list-box" v-if="!orderData.cancelTime&&!orderData.checkTime&&!orderData.arrivesTime">
				<view class="list-box-shu">
					<image src="../../static/images/home/jindutime.png" mode="" class="shenhezhong" v-if="orderData.checkTime"></image>
					<view class="color-ridus"></view>
					<view class="color-shu"></view>
				</view>
				<view class="list-box-text">
					<view>审核中</view>
					<view>{{orderData.checkTime}}</view>
				</view>
			</view>
			<view class="list-box" v-if="orderData.status === 14 && !orderData.cancelTime">
				<view class="list-box-shu">
					<view class="color-ridus" :class="[orderData.checkTime?'color-ridus-b':'']"></view>
					<view class="color-shu"></view>
				</view>
				<view class="list-box-text  list-box-none">
					<view>未通过，请重新上传凭证</view>
					<view>{{orderData.checkTime}}</view>
				</view>
			</view>
			<view class="list-box" v-if="orderData.status === 3||orderData.arrivesTime">
				<view class="list-box-shu">
						<image src="../../static/images/home/jindudui.png" class="jindudui" mode="" v-if="orderData.arrivesTime"></image>
					<view class="color-ridus"  v-else></view>
					<view class="color-shu" :class="[orderData.arrivesTime?'color-b':'']"></view>
				</view>
				<view class="list-box-text">
					<view>平台审核通过</view>
					<view>{{orderData.arrivesTime}}</view>
				</view>
			</view>

			<view class="list-box" v-if="!orderData.cancelTime">
				<view class="list-box-shu">
					<image src="../../static/images/home/jindudui.png" class="jindudui" mode="" v-if="orderData.arrivesTime"></image>
					<view class="color-ridus" v-else ></view>

				</view>
				<view class="list-box-text">
					<view>到账</view>

				</view>
			</view>
		<!-- 	<view class="service" @click="getData()">
				<image src="../../static/images/couponinfo/kefu.png" class="service-icon" mode=""></image>
				<text>联系客服</text>
			</view> -->
		</view>
		<uni-popup ref="popup" type="center">
			<view class="popup">
				<view class="popup-title">
					<text>联系客服</text>
					<image src="../../static/images/mine/close.png" @click="$refs.popup.close()" class="close" mode="">
					</image>
				</view>
				<view class="popup-img">
					<image :src="contactData.qrCode" class="code-img" mode="" :draggable='false'></image>
				</view>

			</view>
		</uni-popup>
		<wxfx></wxfx>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderData: '',
				orderId: "",
				user: "",
				contactData: "",
				statusFmt: [{
						id: 0,
						name: '待上传'
					},
					{
						id: 1,
						name: '已支付'
					},
					{
						id: 2,
						name: '平台待审核'
					},
					{
						id: 3,
						name: '积分即将到账，请耐心等待'
					},
					{
						id: 4,
						name: '待上传二审（请在下单6小时内上传）'
					},
					{
						id: 5,
						name: '订单失效'
					},
					{
						id: 6,
						name: '订单已拒绝'
					},
					{
						id: 7,
						name: '充值已完成'
					},
					{
						id: 8,
						name: '提现已完成'
					},
					{
						id: 9,
						name: '已完成（申请提现 ，1个工作日内到账)'
					},
					{
						id: 10,
						name: '订单已取消'
					},
					{
						id: 11,
						name: '支付失败'
					},
					{
						id: 12,
						name: '退款成功'
					},
					{
						id: 13,
						name: '退款失败'
					},
					{
						id: 14,
						name: '审核未通过，请重新上传凭证'
					},
					{
						id: 15,
						name: '订单超时'
					},


				],
			};
		},
		onLoad(option) {
			this.orderId = option.id;
			this.getShopDetails()
		},
		/* 下拉刷新 */
		/* 下拉刷新 */
		onPullDownRefresh() {
			this.getShopDetails()

		},
		/* 
		/* 获取订单详情 */
		methods: {

			async getShopDetails() {
				let user = uni.getStorageSync('user')
				this.user = JSON.parse(user);
				let {
					data
				} = await this.$api.order.getOrderDetailsData({
					orderId: this.orderId
				})

				this.orderData = data
				console.log(data, '22222222222222222222222222222222’')
				if (data) {
					uni.stopPullDownRefresh()
				}

			},
			/* 设置订单状态 */
			setstatus(data) {
				let newarr = this.statusFmt.filter(item => item.id == data)
				if (newarr[0]) {
					return newarr[0].name;
				}

			},
			/* 获取二维码 */
			async getData() {
				this.$refs.popup.open('')
				let {
					data
				} = await this.$api.details.getServiceData({
					typeEnum: 2
				})

				this.contactData = data;
				console.log(data);
			},
			/* 跳转客服 */
			linkkefu(){
				uni.navigateTo({
					url:`../service/service?id=${2}`
				})
			}
		},

	}
</script>

<style lang="scss" scoped>
	.progress {
		background-color: #F8F8F8;
		width: 100vw;
		height: 100vh;
		padding-top: 28rpx;
	}

	.title {

		width: 702rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
		margin: 0rpx auto 20rpx;
		padding: 50rpx 35rpx 50rpx;
		box-sizing: border-box;

		.title-box {
			display: flex;
			justify-content: space-between;

			text:nth-child(1) {
				font-size: 36rpx;
				font-weight: bold;
				color: #FF4B10;

			}

			text:nth-child(2) {
				font-size: 28rpx;
				font-weight: 500;
				color: #FF4B10;
				line-height: 70rpx;
			}
		}

		.time {
			font-size: 28rpx;
			color: #666666;
			margin-top: 20rpx;
			border-top: 2rpx solid #EEEEEE;
			padding-top: 22rpx;
			display: flex;
			justify-content: space-between;
			.time_money{
				text{
					font-weight: 500;
					color: #222222;
				}
			}
		}
		
	}

	.list {

		width: 702rpx;
		height: 819rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
		box-sizing: border-box;
		margin: 0 auto;
		padding: 28rpx 0rpx;

		.list-title {
			padding: 0 40rpx;
			font-size: 32rpx;
			font-weight: 800;
			color: #222222;
			margin-bottom: 55rpx;
		}
	}

	.list-box {
		padding: 0 40rpx;
		display: flex;
	}

	.color-ridus {
		width: 20rpx;
		height: 20rpx;
		background: #E5E5E5;
		border-radius: 50%;
		margin-top: 7rpx;

	}

	.color-ridus-b {
		width: 15rpx;
		height: 15rpx;
		border: 5rpx solid #FA6D38;
		border-radius: 50%;
		background:#FA6D38 ;
	}

	.color-shu {
		width: 0rpx;
		height: 137rpx;
		margin-left: 10rpx;
	/* 	background: #EEEEEE; */
		border-left: 5rpx dashed #EEEEEE;
	}

	.color-b {
		width: 0rpx;
		height: 137rpx;
		margin-left: 10rpx;
		border-left: 5rpx dashed #FA6D38;
		/* background: #FA6D38; */


	}

	.list-box-text {
		margin-left: 19rpx;

		view:nth-child(1) {
			font-size: 28rpx;
			font-weight: bold;
			color: #222222;

		}

		view:nth-child(2) {
			font-size: 24rpx;
			font-weight: 500;
			color: #999999;
			margin-top: 18rpx;
		}
	}

	.list-box-none {
		view:nth-child(1) {
			color: #FF4B10 !important;
		}

	}

	.service {
		padding: 0 40rpx;
		box-sizing: border-box;
		font-size: 28rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #202121;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		margin-top: 71rpx;
		height: 96rpx;
		border-top: 1rpx solid #EEEEEE;

		.service-icon {
			width: 42rpx;
			height: 42rpx;
			margin-right: 11rpx;
		}
	}

	.popup {

		background-color: #FFFFFF;
		padding: 40rpx;

		.popup-title {
			height: 80rpx;
			text-align: center;
			width: 600rpx;
			line-height: 80rpx;
			position: relative;
			font-size: 35rpx;
			font-weight: bold;
		}

		.close {
			position: absolute;
			width: 44rpx;
			height: 44rpx;
			right: 0;
			top: 20rpx;

		}

		.code-img {
			width: 600rpx;
			height: 600rpx;
			margin-top: 20rpx;
		}
	}
	.kefu{
		width: 48rpx;
		height: 48rpx;
	}
	.jindudui{
		width: 26rpx;
		height: 26rpx;
	}
	.shenhezhong{
		width: 44rpx;
		height: 44rpx;
		margin-left: -10rpx;
	}
</style>
