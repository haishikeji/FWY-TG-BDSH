<template>
	<view class="money">
		<view class="top">

		</view>
		<view class="all-money">

			<view class="all-money-title">积分<text class="red-text">（1积分=<text class="red-text-b">￥1.00</text>）</text>
			</view>

			<view class="all-money-num">
				<text>￥</text><text>{{money.distributeAmount}}</text>
			</view>
			<button type="default" class="all-money-but" @click="getWithdrawal">提现</button>
			<!-- <view class="new_vip_text">
				非会员兑换猫豆  手续费1%
			</view> -->
		</view>
		<view class="list-text">
			明细列表
		</view>
		<view class="list-table" v-if="list.length">
			<view class="list-table-box" v-for="(item,index) in list" :key='item.orderNo'>
				<view class="list-table-box-title">
					<view class="list-table-box-title-box">
						{{item.consumeWay.split('')[0]}}
					</view>

				</view>
				<view class="list-table-center">
					<view class="list-table-center-title">订单号:{{item.orderNo}}</view>
					<view class="list-table-center-tim">
						{{item.operationTime}}
					</view>
					<view class="nnnnn_text" v-if="item.notes">
						备注：{{item.notes}}
					</view>
				</view>
				<view class="list-table-right">
					<view class="">{{item.consumeWay}}</view>
					<view class="">{{moneyClass(item.consumeWay)}}{{item.consumeAmount}}</view>
				</view>
			</view>
			<view class="list-botton-text" v-if="bottonText">
				到底啦~~
			</view>
		</view>
		<view class="none" v-else>
			<view class=""><text>暂无流水</text></view>

		</view>
		<template>
			<view class="popup-box">
				<uni-popup ref="popup" type="center" :mask-click='false'>
					<view class="popup">
						<view class="popup-title">提示</view>
						<view class="popup-text">请先设置收款密码</view>
						<view class="popup-but">
							<view class="popup-but-view" @click="close(1)">
								取消
							</view>
							<view class="popup-but-view" @click="linkSetPwd()">
								确认
							</view>
						</view>
					</view>
				</uni-popup>
			</view>
		</template>
		<template>
		</template>
		<template>
			<uni-popup ref="popupmoney" type="center" :mask-click='false'>
				<view class="popupmoney">
					<view class="popupmoney-title">提现</view>
					<view class="popupmoney-text">可提现积分{{money.distributeAmount}}(1积分=1Y)</view>
					<view class="popupmoney-text">提现积分</view>
					<view class="input">
						<input type="text" class="input-number" v-model="distributeAmount" value=""
							placeholder="请输入需要提现的积分数" />
						<image src="../../../static/images/mine/close1.png" mode="" @click="distributeAmount=''">
						</image>
					</view>
					<view class="popupmoney-text">收款密码</view>
					<view class="input">
						<input type="password" :maxlength="6" v-model="pwd" value="" placeholder="请输入6位收款密码" />
					</view>
					<button type="default" class="popupmoney-but" @click="moneyextract">提现</button>
					<image src="../../../static/images/mine/close.png" mode="" class="gb" @click="monetClose()"></image>
				</view>
			</uni-popup>
		</template>
		<wxfx></wxfx>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],
				page: {
					pageFrom: 1,
					size: 10,

				},
				pageb: {
					current: 1,
					size: 10,
					consumeWayEnums: ["返利", "返利兑换", "问题订单撤回"]
				},
				total: 0,
				money: "",
				pwdState: null,
				wxwema: "",
				distributeAmount: '',
				pwd: "",
				user: '',
				code: '',
				bottonText: false
			};
		},
		onLoad(option) {
			this.getUserInfo()
			this.code = option.code
			console.log(option.code);


		},
		onShow() {
			this.getuserConsumerFlowData()
			this.getAllMoney()
			this.getPwd()
			this.getPicByAccountDataAsync()
			this.wxAddCode()
		},
		/* 触底加载 */
		onReachBottom() {
			if (this.total > this.list.length) {
				this.pageb.size += 10;
				this.getuserConsumerFlowData()
			} else {
				this.bottonText = true
			}
		},
		/* 下拉刷新 */
		onPullDownRefresh() {
			this.getuserConsumerFlowData()
			this.getAllMoney()
		},
		methods: {

			/* 获取流水信息 */
			async getuserConsumerFlowData() {

				let {
					data
				} = await this.$api.my.getUserConsumerFlowData({
					...this.pageb
				})

				this.list = data.records;
				this.total = data.total;
				if (data) {
					uni.stopPullDownRefresh()
				}

			},
			/* 获取总收益 */
			async getAllMoney() {
				let {
					data
				} = await this.$api.my.getUserIncoming(

				)
				this.money = data

				if (data) {
					uni.stopPullDownRefresh()
				}

			},
			/* 判断提取类型 */
			moneyClass(i) {
				let a = '+';
				let b = '-'
				switch (i) {
					case '返利兑换':
						return b
					case '兑换中':
						return b
					case '返利':
						return a

					case '返现':
						return a
					case '问题订单撤回':
						return b
					default:
						return ''

				}
			},
			/* 检查是否设置收款密码 */
			async getPwd() {
				let {
					data
				} = await this.$api.my.getUserPwd()
				this.pwdState = data
			},
			/* 发起提现 */
			getWithdrawal() {

				if (!this.pwdState) {
					this.$refs.popup.open()
				} else {
					/* 发起提现 */
					/* uni.navigateTo({
						url:`withdrawal/withdrawal?id=${this.opId}`
					}) */
					this.monetOpen()
				}


			},

			/* 发送code  */

			/* 跳转设置密码 */
			linkSetPwd() {
				uni.setStorageSync('mark', "1")
				uni.switchTab({
					url: '../my'
				})
			},
			/* 跳转上传收款码 */
			linkSetMoney() {
				uni.setStorageSync('mark', "2")
				uni.switchTab({
					url: '../my'
				})
			},
			/* 获取收款二维码 */
			/* 获取二维码 */
			async getPicByAccountDataAsync() {
				let {
					data
				} = await this.$api.my.getPicByAccountData();
				this.wxwema = data

			},
			/* 关闭弹窗 */
			close(i) {
				if (i == 1) {
					this.$refs.popup.close()
				} else if (i == 2) {
					this.$refs.popupb.close()
				}
			},
			/* 打开提现的弹框 */
			monetOpen() {
				this.$refs.popupmoney.open()
			},
			monetClose() {
				this.$refs.popupmoney.close()
			},
			async moneyextract() {
				if (this.pwd) {

					if (this.distributeAmount >= 10 && this.distributeAmount <= this.money.distributeAmount) {
						let res = await this.$api.my.getMoney({
							pwd: this.pwd,
							amount: this.distributeAmount - 0,
							consumeWay: 9,
							type: 0
						})

						if (res.code == 200) {
							uni.$u.toast('申请成功')
							uni.reLaunch({
								url: '../../recline/recline'
							})
							/* 	this.pwd = '';
								this.distributeAmount = ''; */
							/* this.monetClose()
							this.getAllMoney()
							this.getuserConsumerFlowData() */

						} else if (res.code == 2028) {
							this.wxlogin()
						}
					} else if (this.distributeAmount == 0) {
						uni.$u.toast('提现金额不能为0')
					} else if (this.distributeAmount > this.money.distributeAmount) {
						uni.$u.toast('提现金额不能大于余额')
					} else {
						uni.$u.toast('提现金额必须大于10')
					}


				} else {
					uni.$u.toast('请输入收款密码')
				}




			},
			/* 微信登录 */
			wxlogin() {
				let url = location.href
				const path = encodeURIComponent(url);
				const WxURL = 'https://open.weixin.qq.com/connect/oauth2/authorize'
				const APPIP = "wx73c5f42f04fc4021"
				let newURL =
					`${WxURL}?appid=${APPIP}&redirect_uri=${path}&response_type=code&scope=snsapi_userinfo&state=0#wechat_redirect`;
				window.location.replace(newURL);
			},
			async wxAddCode() {
				if (this.code && !this.user.openId) {
					let res = await this.$api.my.getUserinfo({
						code: this.code
					})

				}
			},
			async getUserInfo() {

				let user = await this.$api.home.getuserBasicData()
				console.log(user);
				if (user.code == 200) {
					this.user = user.data

				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.money {
		width: 100vw;
		min-height: 100vh;
		background: #F8F8F8;
	}

	.nnnnn_text {
		font-size: 24rpx;
		font-weight: 500;
		color: #222222;
	}

	.top {
		width: 750rpx;
		height: 304rpx;
		background: url(../../../static/moneyBG.png) no-repeat 50% 50%/100% 100%;
	}

	.all-money {
		width: 702rpx;
		height: 240rpx;
		margin: -200rpx auto 0;
		background: #FFFFFF;
		border-radius: 20rpx;
		padding: 30rpx;
		box-sizing: border-box;
		position: relative;
	}

	.all-money-title {
		font-size: 28rpx;
		font-weight: 500;
		color: #222222;
	}

	.all-money-num {

		font-size: 60rpx;
		font-family: PingFang SC;
		font-weight: 800;
		color: #222222;

		text:nth-child(1) {
			font-size: 40rpx;
		}
	}

	.all-money-but {
		position: absolute;
		width: 125rpx;
		height: 64rpx;
		line-height: 64rpx;
		background: linear-gradient(321deg, #FA6D38 0%, #FEA724 100%);
		border-radius: 40rpx;
		font-size: 28rpx;
		font-weight: 500;
		color: #FFFFFF;
		top: 86rpx;
		right: 30rpx;

	}

	.list-text {
		font-size: 32rpx;
		font-weight: 800;
		color: #222222;
		padding-left: 23rpx;
		margin-top: 43rpx;

	}

	.list-table {
		width: 702rpx;
		margin: 20rpx auto 27rpx;
		background: #FFFFFF;
		border-radius: 20rpx;

		.list-table-box-title {
			width: 80rpx;
			height: 80rpx;
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			background-color: #FFA745;


			.list-table-box-title-box {
				width: 60rpx;
				height: 60rpx;
				background-color: #FFA745;
				border-radius: 50%;
				line-height: 60rpx;
				text-align: center;
				color: #FFFFFF;
				font-size: 32rpx;
				font-weight: bold;
			}
		}
	}

	.list-table-box {
		display: flex;
		height: 179rpx;
		padding: 30rpx 40rpx;
		box-sizing: border-box;

		.list-table-center-title {
			font-size: 32rpx;
			font-weight: 500;
			color: #000000;
		}

		.list-table-center-tim {
			font-size: 28rpx;
			font-weight: 500;
			color: #999999;
		}

		.list-table-center {
			margin-left: 26rpx;
			border-bottom: 1rpx solid #EEEEEE;
		}
	}

	.list-table-right {
		flex-grow: 1;
		text-align: right;
		border-bottom: 1rpx solid #EEEEEE;

		view:nth-child(1) {
			font-size: 24rpx;
			font-weight: 500;
			color: #222222;
		}

		view:nth-child(2) {
			font-size: 36rpx;
			font-family: DIN;
			font-weight: 500;
			color: #000000;
		}
	}

	.popup {
		padding-top: 20rpx;
		width: 600rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
		text-align: center;

		.popup-but {

			display: flex;
			margin-top: 70rpx;
			height: 90rpx;
			line-height: 90rpx;
			text-align: center;

			.popup-but-view {
				height: 90rpx;
				border-top: 2rpx solid #dedede;
				width: 50%;
				font-size: 35rpx;
				font-weight: bold;

			}

			view:nth-child(2) {
				border-left: 2rpx solid #EEEEEE;
				color: #007AFF;
			}
		}
	}

	.popup-title {
		font-size: 35rpx;
		font-weight: bold;
		margin-top: 20rpx;
	}

	.popup-text {
		padding-top: 40rpx;
		overflow: hidden;
		color: #C0C0C0;
		font-size: 30rpx;
		box-sizing: border-box;
	}

	.red-text {
		font-size: 28rpx;
		font-weight: 500;
		color: #666666;

	}

	.red-text-b {
		color: #FF4B10 !important;
	}

	.none {
		width: 702rpx;
		text-align: center;
		line-height: 200rpx;
		margin: 20rpx auto 27rpx;
		min-height: 200rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
	}

	.popupmoney {
		width: 640rpx;
		padding: 60rpx 30rpx;
		background: #FFFFFF;
		position: relative;

		.popupmoney-title {
			text-align: center;
			font-size: 36rpx;
			color: rgba(51, 51, 51, 1);
			margin-bottom: 30rpx;
		}

		.popupmoney-text {
			padding: 30rpx 0;
			font-size: 24rpx;
		}

		.input {
			font-size: 30rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 1rpx solid #EBEEF5;
			padding: 30rpx 0;

			image {
				width: 40rpx;
				height: 40rpx;
			}

			.input-number {
				flex-grow: 1;
			}
		}

		.popupmoney-but {
			margin-top: 40rpx;
			width: 600rpx;
			height: 97rpx;
			background: #FFA745;
			color: #FFFFFF;
			line-height: 97rpx;
			border-radius: 49rpx;

		}

		.gb {
			width: 50rpx;
			height: 50rpx;
			position: absolute;
			top: 20rpx;
			right: 30rpx;
		}
	}

	.list-botton-text {
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
	}

	.new_vip_text {
		margin-top: 20rpx;
		width: 100%;
		font-size: 24rpx;
		color: #222222;
		text-align: center;
	}
</style>