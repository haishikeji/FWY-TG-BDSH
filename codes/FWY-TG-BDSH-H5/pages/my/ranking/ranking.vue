<template>
	<view class="ranking">

		<view class="ranking_top">
			<view class="tab">
				<view class="tab-text" v-for="item in tab" :key='item.id' :class="[item.id==tabId?'acitv-tab-taxt':'']"
					@click="activeTabId(item.id)">
					{{item.name}}
					<view class="tab-border" v-if="item.id==tabId"></view>
				</view>
			</view>
		
				
		
			<view class="ranking_top_box_b" v-if="tabId==1">
				<image src="../../../static/paihangtaijie.png" mode="" class="paihangtaijie"></image>
				<!-- 第一名 -->
					<template  v-for="(item,index) in botData" >
				<view class="ranking_top_box_b_one" :key='index' v-if='item.rowNum==1'>
					<image :src="item.icon" mode="" class="ranking_top_box_b_one_active">
						<image src="../../../static/huangguan1.png" mode="" class="huangguan"></image>
					</image>
					<view class="ranking_top_box_b_one_num">
						1
					</view>
					<view class="ranking_top_box_b_one_title">
						<view class="ranking_top_box_b_one_title_a">
						{{splieData(item.consumerName)}}
						</view>
						<view class="ranking_top_box_b_one_title_b">
							累计收益：<text>{{(item.consumeAmount-0).toFixed(2)}}</text>
						</view>
					</view>
				</view>
				<!-- 第二名 -->
				<view class="ranking_top_box_b_tow" :key='index' v-else-if='item.rowNum==2'>
					<image :src="item.icon" mode="" class="ranking_top_box_b_one_active">
						<image src="../../../static/huangguan2.png" mode="" class="huangguan"></image>
					</image>
					<view class="ranking_top_box_b_one_num ranking_top_box_b_one_two">
						2
					</view>
					<view class="ranking_top_box_b_one_title">
						<view class="ranking_top_box_b_one_title_a">
							{{splieData(item.consumerName)}}
						</view>
						<view class="ranking_top_box_b_one_title_b">
							累计收益：<text>{{(item.consumeAmount-0).toFixed(2)}}</text>
						</view>
					</view>
				</view>
				<!-- 第三名 -->
				<view class="ranking_top_box_b_three" :key='index' v-else-if='item.rowNum==3'>
					<image :src="item.icon" mode="" class="ranking_top_box_b_one_active">
						<image src="../../../static/huangguan3.png" mode="" class="huangguan"></image>
					</image>
					<view class="ranking_top_box_b_one_num ranking_top_box_b_one_three">
						3
					</view>
					<view class="ranking_top_box_b_one_title">
						<view class="ranking_top_box_b_one_title_a">
						{{splieData(item.consumerName)}}
						</view>
						<view class="ranking_top_box_b_one_title_b">
							累计收益：<text>{{(item.consumeAmount-0).toFixed(2)}}</text>
						</view>
					</view>
				</view>
					</template>
			</view>
		</view>
		<view class="list" :class="[tabId!=1?'list-table-tow':'']">
			<view class="list-title" v-if="tabId==1">
				<text>排名</text>
				<text>昵称</text>
				<text>累计收益</text>
			</view>
			<view class="list-title list-title-two" v-if="tabId==2">
				<view class="list-title-b">明细列表</view>
			</view>
			<view class="list-title" v-if="tabId==3">

				<text> </text>
				<text>提现￥{{cashBack.widthdrawAmount}} 返利￥{{cashBack.cashBackAmount}}</text>
			</view>
			<view class="list-table" v-if="tabId==1">
			
				<view class="list-table-tr"  v-for="(item,index) in botData" :key='index'>
				<!-- 	<image src="@/static/v2img/fx/no1.png" mode="" v-if='item.rowNum==1' class="list-table-tr-icon">
					</image>
					<image src="@/static/v2img/fx/no2.png" mode="" v-else-if='item.rowNum==2'
						class="list-table-tr-icon"></image>
					<image src="@/static/v2img/fx/no3.png" mode="" v-else-if='item.rowNum==3'
						class="list-table-tr-icon"></image> -->
						<!--  v-if="item.rowNum!=1&&item.rowNum!=2&&item.rowNum!=3" -->
					<view class="list-table-tr-on">
						<view> {{item.rowNum}}</view>
					</view>
					<!-- {{splieData(item.consumerName)}} -->
					<view class="list-table-tr-text"><image :src="item.icon" mode="" class="list-table-tr-text-icon"></image> {{splieData(item.consumerName)}}</view>
					<!-- {{}}50 -->
					<text class="list-table-tr-money">{{(item.consumeAmount-0).toFixed(2)}}</text>
				</view>

			</view>
			<view class="sand" v-if="tabId==1&&userData">
				<view class="list-table list-table-my">
					<view class="list-table-tr">
					<!-- 	<image src="@/static/v2img/fx/no1.png" mode="" v-if='userData.rowNum==1'
							class="list-table-tr-icon">
						</image>
						<image src="@/static/v2img/fx/no2.png" mode="" v-else-if='userData.rowNum==2'
							class="list-table-tr-icon"></image>
						<image src="@/static/v2img/fx/no3.png" mode="" v-else-if='userData.rowNum==3'
							class="list-table-tr-icon"></image> -->
						<view class="list-table-tr-on" >
							<view>{{userData.rowNum}}</view>
						</view>
						<view class="list-table-tr-text"><image :src="userData.icon" mode="" class="list-table-tr-text-icon"></image>{{splieData(userData.consumerName)}}</view>
						<text class="list-table-tr-money">{{(userData.consumeAmount-0).toFixed(2)}}</text>
					</view>
				</view>
			</view>
			<view class="sand" v-else-if="tabId==1&&!userData">
				<view class=" list-table-my">
					<view class="list-table-tr">
						<view class="list-table-tr-on">
							<view class="text-none">暂无排名</view>
						</view>
						<text class="list-table-tr-text" v-if="user.nickName"><image :src="user.icon" mode="" class="list-table-tr-text-icon"></image>{{user.nickName}}</text>
						<text class="list-table-tr-text" v-else>{{splieData(user.phone)}}</text>
						<text class="list-table-tr-money">0.00</text>
					</view>

				</view>

			</view>
			<view class="" v-if="tabId==2">
				<view class="list-table-tr list-table-tr_two" v-for="(item,index) in sharData" :key='index'>


					<view class="list-table-tr-td">
						<view class="list-table-tr-td-box">
							<!-- <text class="list-table-tr-text-usename">{{splieData(item.consumerName)}}</text> -->
							<view class="list-table-tr-time">
							<image :src="item.icon" class="darenfenxico_B" mode=""></image> {{item.createTime}}
							</view>
						</view>
					</view>
					<text class="list-table-tr-money list-table-tr-money-two">￥{{item.consumeAmount}}</text>
				</view>
			</view>
			<view class="list-table" v-if="tabId==3">
				<view class="list-table-tr" v-for="(item,index) in fxlist" :key='index'>
					<view class="list-table-tr-td">
				<!-- 		<image src="@/static/v2img/fx/tx.png" class="userActive" mode="" v-if="item.consumeWay=='分销提现'">
						</image>
						<image src="../../../static/v2img/fx/qb.png" class="userActive" mode=""
							v-else-if="item.consumeWay=='分销返利'"></image>
						<image src="../../../static/v2img/fx/qb.png" class="userActive" mode=""
							v-else-if="item.consumeWay=='分销返佣撤回'"></image> -->
							<image src="../../../static/images/darenfenxicon.png" class="darenfenxicon" mode="">
						<view class="list-table-tr-td-box">
							<text class="list-table-tr-text-usename">{{item.consumeWay}}</text>
							<view class="list-table-tr-time">
								{{item.operationTime}}
							</view>
						</view>
					</view>
					<text class="list-table-tr-money list-table-tr-money-two">{{moneyClass(item.consumeWay)}}{{item.consumeAmount}}</text>
				</view>
			</view>
		</view>
		<wxfx></wxfx>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				tab: [{
						id: 1,
						name: "收益排行"
					},
					{
						id: 2,
						name: "近期收益"
					},
					{
						id: 3,
						name: "收益明细"
					}
				],
				tabId: 1,
				topData: [],
				botData: [],
				sharData: [],
				pageb: {
					current: 1,
					size: 10,
					consumeWayEnums: ["分销返利", "分销兑换", '分销返佣撤回']
				},
				total: 0,
				fxlist: [],
				user: "",
				userData: null,
				cashBack: ""
			};
		},
		onLoad() {
			this.getuserData()
			this.getListData()

		},
		onPullDownRefresh() {
			this.getListData()
			this.getConsumeTotalAsync()

		},
		/* 触底加载 */
		onReachBottom() {
			if (this.tabId == 3) {
				if (this.total > this.fxlist.length) {
					this.pageb.size += 8;
					this.getListData()
				} else {
					uni.$u.toast('没有更多了');

				}
			}

		},
		methods: {
			/* 切换tab栏 */
			activeTabId(id) {
				this.tabId = id
				this.getListData()
			},
			/* 获取不同数据 */
			async getListData() {
				let tabId = this.tabId
				if (tabId == 1) {
					/* 获取收益排行 */
					let {
						data
					} = await this.$api.leader.getLeaderData({
						orderStatisticsEnum: 3
					})
					this.botData = data.filter(item => item.rowNum <= 10)
					
					this.userData = data.filter(item => this.user.id == item.consumerId)[0]
					console.log(this.userData);
				} else if (tabId == 2) {
					/* 获取分销下级的收益 */
					let {
						data
					} = await this.$api.leader.getSharEarningData({
						orderStatisticsEnum: 3
					})
					this.sharData = data

					/* 获取用户的分销收益 */
				} else if (tabId == 3) {
					this.getConsumeTotalAsync()
					let {
						data
					} = await this.$api.my.getUserConsumerFlowData({
						...this.pageb
					})


					this.fxlist = data.records;
					this.total = data.total;
				}
				uni.stopPullDownRefresh()
			},
			/* 判断提取类型 */
			moneyClass(i) {
				let a = '+';
				let b = '-'
				switch (i) {
					case '分销兑换':
						return b
					case '兑换中':
						return b
					case '分销返利':
						return a

					case '返现':
						return a
					case '分销返佣撤回':
						return b
					default:
						return ''

				}
			},
			/* 获取用户信息 */
			getuserData() {
				let user = uni.getStorageSync('user')
				if (user) {

					this.user = JSON.parse(user)
					console.log(this.user);
				}
			},
			/* 电话格式化 */
			splieData(item) {
				if (item.length == 11) {
					let n = '****'
					let phoneA = item.slice(0, 3);
					let phoneB = item.slice(7);
					this.phone = `${phoneA}${n}${phoneB}`;
					return `${phoneA}${n}${phoneB}`
				} else {
					return item
				}
			},
			/* 获取用户提现总额，返利总额 */
			async getConsumeTotalAsync() {
				const {
					data
				} = await this.$api.my.getConsumeTotalAsync()
				console.log(data)
				this.cashBack = data
				uni.stopPullDownRefresh()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ranking {
		overflow: hidden;
		width: 100vw;
		min-height: 100vh;
		background-color: #F6F6F6;
	}

	.tab {
		width: 100%;

		margin: 88rpx 0 38rpx;
		padding: 0 40rpx;
		box-sizing: border-box;
		display: flex;
		align-items: center;

		.tab-text {
			text-align: center;
			line-height: 24rpx;
			width: 214rpx;
			height: 24rpx;
			font-size: 32rpx;
			font-family: PingFangSC-Regular, PingFang SC;
			font-weight: 400;



		}

		.acitv-tab-taxt {
			font-size: 32rpx;
			font-weight: 500;


		}

		.tab-border {
			width: 48rpx;
			height: 6rpx;
			background: #FFFFFF;
			margin: 18rpx auto 0;
			border-radius: 3rpx;
		}


	}

	.list {
		position: relative;
		z-index: 9;
		padding-top: 10rpx;
		width: 702rpx;
		background: #FFFFFF;
		border-radius: 24rpx 24rpx 0rpx 0rpx;
		margin: -40rpx auto 0;
		min-height: 80vh;	
		

		.list-title {
			padding: 15rpx 34rpx;
			box-sizing: border-box;
			margin: 0rpx 0 0rpx;
			font-size: 24rpx;
			font-weight: 400;
			color: #999999;
			display: flex;
			align-items: center;
			justify-content: space-between;
			
		}
	}

	.list-table {
		padding: 0 20rpx;
		box-sizing: border-box;
		 min-height: 65vh;
	}
	.list-table-my{
		padding: 0 45rpx;
		box-sizing: border-box;
		 
		 
	}
.list-table-tow{
	
		margin: -480rpx auto 0;
}
	.list-table-tr {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 0;
		.list-table-tr-icon {
			width: 72rpx;
			height: 72rpx;

		}

		.list-table-tr-on {
			margin-right: 0rpx;
			font-size: 40rpx;
			font-weight: 500;
			text-align: center;
			color: #666666;
			width: 75rpx;
			

			.list-table-tr-on-card {
				width: 38rpx;
				height: 16rpx;
				margin-left: 20rpx;
				background: rgba(0, 0, 0, 0.12);
				border-radius: 4rpx;
				
			}
		}
	}

	.sand {
		height: 80rpx;
		border: 1rpx solid;
		border-radius: 120rpx;
		background: #FFFFFF;
		box-shadow: 0rpx 16rpx 50rpx 0rpx rgba(0, 0, 0, 0.3);
		
	}


	.text-none {
		font-size: 24rpx;
		width: 100rpx;
	}

	.list-table-tr-td {
		display: flex;
		align-items: center;
	}

	.userActive {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		margin-right: 10rpx;

	}

	.list-table-tr-text {
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-grow: 1;
			
			padding-left: 70rpx;
	}

	.list-table-tr-text-usename {
		font-size: 32rpx;
		font-weight: 400;
		color: #333333;

	}

	.list-table-tr-time {
		font-size: 24rpx;
		font-weight: 400;
		color: #666666;

	}

	.list-title-two {
		padding-left: 40rpx !important;
		padding-right: 60rpx !important;

	}

	.list-table-tr-money {
		width: 150rpx;
		text-align: center;
		font-size: 28rpx;
		font-weight: 500;
		color: #FA6D38;
		
		
		
	}

	.ranking_top {
		border: 1rpx solid rgba($color: #000000, $alpha: 0);
		height: 650rpx;
		background: url(../../../static/paihangbangTOP.png)no-repeat 50% 50% /100% 100%;
		color: #FFFFFF;
	}

	.ranking_top_box_b {

		height: 472rpx;
		width: 676rpx;
		margin: 0 auto;
		position: relative;

		.paihangtaijie {

			width: 100%;
			height: 242rpx;
			position: absolute;
			bottom: 0;

		}
	}

	.ranking_top_box_b_one {

		text-align: center;
		width: 275rpx;
		position: absolute;
		z-index: 2;
		left: 198rpx;
		top: 80rpx;
	}

	.ranking_top_box_b_tow {

		text-align: center;
		width: 240rpx;
		position: absolute;
		z-index: 2;
		left: -10rpx;
		top: 140rpx;
	}

	.ranking_top_box_b_three {

		text-align: center;
		width: 240rpx;
		position: absolute;
		z-index: 2;
		right: -10rpx;
		top: 150rpx;
	}

	.ranking_top_box_b_one_active {
		width: 152rpx;
		height: 152rpx;
		border-radius: 50%;
		border: 5rpx solid #FFFFFF;
	}

	.ranking_top_box_b_one_title {
		margin-top: 46rpx;

		.ranking_top_box_b_one_title_a {
			font-size: 32rpx;
			font-weight: bold;
			color: #FFFFFF;
		}

		.ranking_top_box_b_one_title_b {

			font-size: 22rpx;

			text {
				font-size: 32rpx;
			}
		}
	}

	.ranking_top_box_b_one_num {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(321deg, #FA6D38 0%, #FEA724 100%);
		border-radius: 40rpx;
		border: 2rpx solid #FFFFFF;
		position: absolute;
		font-size: 28rpx;
		text-align: center;
		line-height: 48rpx;
		top: 100rpx;
		right: 50rpx;
	}

	.ranking_top_box_b_one_two {

		background: linear-gradient(321deg, #CECDCF 0%, #F6F3F5 100%);
	}

	.ranking_top_box_b_one_three {

		background: linear-gradient(321deg, #ECB787 0%, #FCDBBD 100%);
	}

	.huangguan {
		width: 66rpx;
		height: 47rpx;
		position: absolute;
		top: -40rpx;
		left: 36%;
		z-index: -1;
	}
	.list-table-tr-text-icon{
		width: 50rpx;
		height: 50rpx;
		border-radius: 50%;
		vertical-align: middle;
		margin-right: 10rpx;
		
	}
	.list-title-b{
		font-size: 32rpx;
		font-weight: bold;
		color: #000000;
	}
	.list-table-tr_two{
		border-bottom:2rpx solid #EEEEEE ;
		padding-bottom: 20rpx;
		
	}
	.list-table-tr-money-two{
		color: #000000;
	}
	.darenfenxicon{
		width: 80rpx;
		height: 80rpx;
		vertical-align: middle;
		margin-right: 20rpx;
		margin-left: 30rpx;
		
	}
	.darenfenxico_B{
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		vertical-align: middle;
		margin-right: 20rpx;
		margin-left: 30rpx;
	}
</style>
