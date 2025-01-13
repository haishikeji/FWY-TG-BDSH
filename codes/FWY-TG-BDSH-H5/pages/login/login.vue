<template>
	<view class="page">
		<!-- <uni-nav-bar  title="登录"></uni-nav-bar> -->
		<view class="all-cont">
			<view class="login-top">
				<image src="../../static/login.png" class="login_logo"></image>
			</view>
			<form>
				<view class="login_user_name">
					<image src="../../static/login/phone.png" mode=""></image>
					<view style="border-right: 1rpx solid #E2E2E2;height:40rpx; margin: 0 10rpx 0 10rpx"></view>
					<input type="number" name='phone' value="" v-model="telephone" placeholder="请输入手机号"
						class="login_user_name_input" />
					<!-- <text>|</text>
					<u-toast ref="uToast"></u-toast>
					<u-code :seconds="seconds" ref="uCode" @change="codeChange" changeText="Xs"></u-code>
					<text class="fs_code_font" @tap="getCode">{{tips}}</text> -->

				</view>
				<!-- 			<view class="login_user_name">
					<image src="../../static/login/msgcode.png" mode=""></image>
					<view style="border-right: 1rpx solid #E2E2E2;height:40rpx; margin: 0 10rpx 0 10rpx"></view>
					<input type="number" value="" v-model="yzCode" maxlength="6" name='password' placeholder="请输入验证码"
						class="login_user_name_input" />

				</view> -->
				<button class="login" @click="getUserLoginData">
					<!-- 	<image class="login-btn" src='../../static/yzm.png'>
					</image> -->
					登录
				</button>
			</form>
			<view style="margin:18rpx auto 0;font-size:24rpx;color:rgba(155,155,155,1);line-height:34rpx;">未注册手机将自动创建账号
			</view>

			<view class="agreement">

				<view style="margin-left:10rpx;font-size:24rpx;margin-bottom:30rpx">

					<checkbox-group @change='change'>
						<checkbox value="22" color="#1890ff" style="transform:scale(0.7)" />我已阅读并同意<span
							style="color:#5194E3" @click="userAgreement()">《用户服务协议》</span>
					</checkbox-group>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

				codeFont: '发送验证码',
				yzCode: '1234',
				timer: '',
				telephone: '',
				hasCheck: false,
				countDownNum: 60,
				tips: '',
				seconds: 60,
				userId: "",
				check: []
			};

		},
		onLoad() {
			let userId = uni.getStorageSync('userId')

			if (userId) {
				this.userId = userId
			}
		},
		methods: {

			codeChange(text) {
				this.tips = text;
			},
			async getCode() {

				if (this.$refs.uCode.canGetCode) {
					const res = await this.$api.login.getVerificationCode(this.telephone)
					if (res.code == 200) {
						uni.$u.toast('验证码已发送');
						this.$refs.uCode.start();
					}

				}
			},
			change(e) {
				console.log(e);
				this.check = e.detail
				console.log(this.check);
			},
			/* 手机号登录 */
			async getUserLoginData() {
				if (this.check.length == 0) {
					return uni.$u.toast('请勾选用户协议')
				}
				if (this.yzCode) {
					const res = await this.$api.login.getUserLogin({
						phone: this.telephone,
						code: this.yzCode,
						distributionId: this.userId
					})
					if (res.code == 200) {
						await uni.setStorageSync('token', res.data.token)
						let user = await this.$api.home.getuserBasicData()
						if (user.code == 200) {
							uni.setStorageSync('user', JSON.stringify(user.data))
							uni.$u.toast('登录成功')
							uni.navigateBack({
								delta: 1
							})
						}
					}
				} else if (this.telephone && !this.yzCode) {
					uni.$u.toast('请输入验证码')

				} else {
					uni.$u.toast('请输入手机号')
				}


			},

			/* 用户协议 */
			userAgreement() {
				uni.navigateTo({
					url: 'agreement/agreement'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.page {
		width: 750rpx;
		background-color: #fff;
	}

	.login-top {
		width: 100%;
		margin-top: 80rpx;
		font-size: 48rpx;
		font-weight: 600;
		color: rgba(39, 39, 39, 1);
		line-height: 66rpx;
		text-align: left;
	}

	.btn {
		box-sizing: border-box;
		padding: 24rpx 21rpx;
		background: #1890ff;
		color: #fff;
		border-radius: 12rpx;
	}

	.all-cont {
		width: 100%;
		box-sizing: border-box;
		padding: 0 70rpx;
		text-align: center;
	}

	.input-box {
		width: 608rpx;
		height: 116rpx;
		background: rgba(248, 248, 248, 1);
		border-radius: 58rpx;
	}

	.bg {
		width: 100%;
		position: absolute;
		top: 0;
		left: 0;
		z-index: -1;
	}

	.box {
		width: 100%;
		background-color: #fff;
		border-radius: 50rpx;
		margin-top: 150rpx;
		box-shadow: 0 0 15rpx 5rpx rgba(255, 255, 255, 0.6);
		box-sizing: border-box;
		padding: 0 50rpx;
		padding-bottom: 90rpx;
		border-bottom: 15rpx solid rgba(241, 139, 60, 0.5);
	}

	.logo {
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		box-sizing: border-box;
		padding: 50rpx 0;
	}

	.border-bottom {
		border: 0;
	}

	.cu-form-group+.cu-form-group {

		border: 0;

	}

	.code {
		display: flex;
		width: 100%;
		justify-content: space-between;
		align-items: flex-end;
	}


	.agreement {
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 42rpx;
	}

	.size {
		transform: scale(0.65);
	}

	.login {
		width: 608rpx;
		height: 80rpx;

		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 40rpx;
		margin-top: 122rpx;
		color: #FFFFFF;
		border: 0;
		outline: none;
		background-color: #1890ff;

	}

	button::after {
		border: none;
	}

	.login-btn {
		width: 150rpx;
		height: 150rpx;
	}

	.secTitle {
		width: 100%;
		text-align: center;
		margin-top: 116rpx;
	}

	.weixin {
		width: 100%;
		display: flex;
		justify-content: center;
		margin-top: 128rpx;
	}

	.weixin>button {
		padding: 0
	}

	.tips {
		margin-top: 20rpx;
		margin-left: 30rpx;
		color: #f00;
		display: flex;
		align-items: center;
	}

	@font-face {
		font-weight: normal;
		font-style: normal;
		font-display: swap;
	}


	.iconfont {
		font-family: "iconfont" !important;
		font-size: 16px;
		font-style: normal;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	.icon-shouji:before {
		content: "\e61a";
	}

	.icon-youxiang:before {
		content: "\e611";
	}

	.icon-icon_home:before {
		content: "\e503";
	}

	.custorm_logo {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background: #37D47F;
	}

	.login_logo {
		width: 372rpx;
		height: 372rpx;

		display: block;
		margin: 0 auto 80rpx;

	}

	.page_ipt {
		width: 50rpx;
	}

	.login_user_name {
		box-sizing: border-box;
		background-color: var(--white);
		padding: 1rpx 30rpx;
		display: flex;
		align-items: center;
		min-height: 100rpx;
		margin-top: 30rpx;
		width: 608rpx;
		height: 116rpx;
		background: rgba(248, 248, 248, 1);
		border-radius: 58rpx;
		border: 1rpx solid #D4D4D6;
		text-align: left;

		image {
			width: 40rpx;
			height: 40rpx;

		}

		.fs_code_font {
			font-size: 25rpx;
			color: #666666;
			text-align: center;
			margin-left: 5rpx;
		}
	}
</style>