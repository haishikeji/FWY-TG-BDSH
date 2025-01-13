<template>
	<view class="message">
		<view class="message-box" v-for="item in list" :key='item.id' @click="setUserMessageData(item.id)">
			<image src="../../../static/images/mine/read.png" class="icon" mode="" v-if="item.readStatus"></image>
			<view class="readStatus" v-else>
				<image src="@/static/lengdang2.png" mode=""></image>
			</view>
			<!-- <image src="../../../static/images/mine/new.png"  class="icon" mode="" v-else></image> -->
			<!-- <view class="radio" v-if="item.readStatus==0"></view> -->
			<view class="box-center">
				<view class="">系统通知</view>
				<view class="box-center-msg">
					{{item.msg}}
				</view>
				<view class="">
					<text>{{item.createTime}}</text>
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
				size: 10,
				current: 1,
				list: '',
				total: ''
			}
		},
		onShow() {
			this.getlistData()
		},
		onReachBottom() {

			if (this.total > this.list.length) {
				this.size += 5
				this.getlistData()
			} else {

				uni.$u.toast('没有更多消息了');
			}

		},
		methods: {
			/* 获取消息列表 */
			async getlistData() {
				let res = await this.$api.message.getUserMessage({
					size: this.size,
					current: this.current
				})
				if (res.code == 200) {
					this.list = res.data.records;
					this.total = res.data.total
				}

			},
			/* 更新已读状态 */
			async setUserMessageData(id) {
				let res = await this.$api.message.setUserMessage(id)
				if (res.code == 200) {
					this.getlistData()
				}
			}

		}
	}
</script>

<style lang="scss" scoped>
	.message {
		width: 100vw;
		min-height: 100vh;
		background: #F8F8F8;
		padding: 30rpx 0;
	}

	.message-box {
		width: 690rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		margin: 20rpx auto;

		padding: 29rpx 30rpx 39rpx 30rpx;
		box-sizing: border-box;
		position: relative;

		display: flex;
	}

	.icon {


		width: 80rpx;
		height: 80rpx;
		vertical-align: top;

	}

	.box-center {
		width: 540rpx;
		display: inline-block;
		margin-left: 10rpx;
		padding: 0 5rpx;
		box-sizing: border-box;

		view:nth:child(1) {
			font-size: 34rpx;
			font-weight: bold;
			color: #222222;

		}

		view:nth-child(2) {
			font-size: 28rpx;
			font-weight: 500;
			color: #666666;
			margin: 10rpx 0 10rpx;

		}

		view:nth-child(3) {
			font-size: 26rpx;
			font-weight: 500;
			color: #999999;
		}
	}

	.radio {

		position: absolute;
		width: 20rpx;
		height: 20rpx;
		background: #FF4B10;
		border-radius: 50%;
		top: 26rpx;
		left: 80rpx;
	}

	.box-center-msg {
		word-wrap: break-word;
		white-space: normal;
		word-break: break-all;
	}

	.readStatus {
		width: 80rpx;
		height: 80rpx;
		text-align: center;
		background: #FA6D38;
		border-radius: 50%;
		display: inline-block;
		padding-top: 10rpx;
		box-sizing: border-box;

		image {
			width: 55rpx;
			height: 55rpx;
		}
	}
</style>
