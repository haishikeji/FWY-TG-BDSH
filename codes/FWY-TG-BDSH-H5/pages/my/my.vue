<template>
	<view class="my">
		<wxfx></wxfx>
		<view class="gai-top-box">
			<view class="top">
				<view class="top-user-name">
					<view class="top-user-name-top">
						<text v-if="user.nickName">{{user.nickName}}</text>
						<text v-else>游客</text>
					</view>
					<view class="top-user-name-top_icon" v-if="user.distributionMember">分销员</view>
				</view>

			</view>
			<view class="tim_text_div">
				<text v-if="user.expireDate">会员到期时间：{{user.expireDate.split(' ')[0]}}</text>
			</view>
			<view class="money">
				<view class="money-title">
					<view class="money-title-but-a" @click="limkMoney('money/money?id=1',1)">
						提现
					</view>
					<!-- <view class="money-title-but-b" @click="linkRecline">
								去赚钱
							</view> -->
					<image :src="user.icon" mode="" class="user-icon"></image>
					<image src="@/static/myimage/active_bg.png" class="active_bg" mode="" v-if="user.userMember">
					</image>
					<image src="../../static/newMyvip.png" class="newMyvip" mode="" v-if="user.userMember"></image>
					<view class="money-title_right">
						<view class="money-title_right_a">
							{{(money.amount-0).toFixed(2)}}
						</view>
						<view class="money-title_right_b">
							余额
						</view>
					</view>
					<view class="money-title_right">
						<view class="money-title_right_a">
							{{(money.settling-0).toFixed(2)}}
						</view>
						<view class="money-title_right_b">
							待结算
						</view>
					</view>
				</view>

				<view class="money-list">
					<view class="money-list-box">
						<!-- 	<view class="">{{money.distributeAmount||0}}</view> -->
						<view class="">{{money.pointsIncome||0}}</view>
						<text>累积节省</text>
					</view>
					<view class="money-list-box box-center">
						<!-- 	<view class="">{{money.amount||0}}</view> -->
						<view class="">{{money.distributeAmount||0}}</view>
						<text>收益积分</text>
					</view>
					<view class="money-list-box">
						<view class="">{{user.inviteNum||0}}</view>
						<text>邀请好友</text>
					</view>
				</view>


			</view>
		</view>



		<view class="nav">
			<template v-for="item in navList">
				<view class="nav-list" :key='item.id' @click="limkMoney(item.path,item.id)">
					<image :src="item.img" class="nav-img" mode=""></image>
					<view class="">
						<text>{{item.name}}</text>
					</view>
				</view>
			</template>
			<!-- 	 -->
			<view class="mesg" v-if="mesg">
			</view>
		</view>
		<button type="default" class="defaule-but" @click="$refs.popuptuchu.open('')" v-if="user.id">退出登录</button>
		<button type="default" class="defaule-but" @click="login()" v-else>去登录</button>
		<template>
			<uni-popup ref="popupb" type="center" :mask-click='false'>
				<view class="popup">
					<view class="popup-title-box">
						<view class="popup-title">更换收款密码</view>
						<image src="../../static/images/mine/close.png" class="popup-title-gb" @click="close()" mode="">
						</image>
					</view>
					<view class="popup-title-input">
						<view class="popup-title-input-title">手机号</view>
						<text class="phone-title">+86{{">"}} </text>
						<text v-if="user.phone">{{user.phone}}</text>
					</view>

					<view class="popup-title-input">
						<view class="popup-title-input-title">验证码</view>
						<view class="yz-code-box ">
							<input type="number" v-model="yzCode" placeholder="输入验证码" />
							<view class="fs_code_font" @click="getCodepwd">{{tipsc}}</view>
						</view>
					</view>
					<view class="popup-title-input">
						<view class="popup-title-input-title">新收款密码</view>
						<view class="yz-code-box">
							<input type="password" v-model="newpwd" maxlength="6" placeholder="请输入新收款密码" />

						</view>
					</view>
					<button type="default" class="popop-but" @click="setMoneyPassword">确定</button>
				</view>
			</uni-popup>
		</template>
		<template>
			<uni-popup ref="popupcode" type="center" :mask-click='false'>
				<view class="popup">
					<view class="popup-title-box popup-title-box-code">
						<view class="popup-title">上传收款二维码</view>
						<image src="../../static/images/mine/close.png" class="popup-title-gb"
							@click="$refs.popupcode.close()" mode="">
						</image>
					</view>
					<template>
						<view class="fileList">
							<u-upload :fileList="fileList1" @afterRead="afterRead" @delete="deletePic"
								@beforeRead="beforeRead" name="1" multiple :maxCount="1" uploadText='上传收款二维码'
								width="350rpx" height="350rpx" class="updet" :deletable="false">
								<image :src="wxwema" mode="widthFix" class="wxwema-img" v-if="wxwema"> </image>
								<image src="../../static/images/mine/code1.png" mode="widthFix"
									style="width:350rpx;height:350rpx;" v-else></image>
								<view class="posho" v-if="fileList1.length==0&&!wxwema">
									<image src="@/static/images/mine/ceamre.png" mode=""></image>
									<view class="">上传收款二维码</view>
								</view>
							</u-upload>
						</view>
					</template>
				</view>
			</uni-popup>
		</template>
		<template>
			<uni-popup ref="popupphone" type="center" :mask-click='false'>
				<view class="popup">
					<view class="popup-title-box">
						<view class="popup-title">更换手机号</view>
						<image src="../../static/images/mine/close.png" class="popup-title-gb"
							@click="$refs.popupphone.close()" mode="">
						</image>
					</view>
					<view class="popup-title-input ">
						<view class="popup-title-input-title">手机号</view>
						<view class="phone-code-box">
							<text class="phone-title">+86{{">"}} </text>
							<input type="number" v-model="newphone" placeholder="请输入手机号" />
						</view>
					</view>
					<view class="popup-title-input">
						<view class="popup-title-input-title">验证码</view>
						<view class="yz-code-box ">
							<input type="number" v-model="phoneCode" placeholder="输入验证码" />
							<view class="fs_code_font" @click="getCodePhone">{{tipsb}}</view>
						</view>
					</view>

					<button type="default" class="popop-but" @click="setphoneDataAsunc">验证</button>
					<view class="popop-but-text">验证成功后，将与账号绑定</view>
				</view>
			</uni-popup>
		</template>
		<template>
			<view class="">
				<uni-popup ref="popuptuchu" type="center" :mask-click='false'>
					<view class="popup-tuicu">
						<view class="popup-title">提示</view>
						<view class="popup-text">确定要退出吗</view>
						<view class="popup-but">
							<view class="popup-but-view" @click="$refs.popuptuchu.close('')">
								取消
							</view>
							<view class="popup-but-view" @click="removeUser()">
								确认
							</view>
						</view>
					</view>
				</uni-popup>
			</view>
		</template>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: {},
				money: {},
				navList: [{
						id: 1,
						name: '我的钱包',
						img: '../../static/myimage/qianbao.png',
						path: 'money/money?id=1'
					},

					{
						id: 2,
						name: '消息通知',
						img: '../../static/myimage/xiaoxiu.png',
						path: 'message/message'
					},
					{
						id: 3,
						name: '分销排行',
						img: '../../static/myimage/fenxiaopaih.png',
						path: './ranking/ranking'
					},
					{
						id: 4,
						name: '换绑手机',
						img: '../../static/myimage/phone.png',
						path: ''
					},
					{
						id: 5,
						name: '商务合作',
						img: '../../static/myimage/shangwuhezuo.png',
						path: '../service/service?id=1'
					},
					{
						id: 6,
						name: '收款密码',
						img: '../../static/myimage/shoukuanmima.png',
						path: ''
					},
					{
						id: 7,
						name: '新手引导',
						img: '../../static/myimage/xinshouyindao.png',
						path: './Introduction/Introduction'
					},
					{
						id: 8,
						name: '设置中心',
						img: '../../static/myimage/shezhi.png',
						path: './system/system'
					},

				],
				tabList: [{
						id: 1,
						name: '换绑手机',
						img: '../../static/images/mine/phone.png'
					},
					{
						id: 2,
						name: '商务合作',
						img: '../../static/images/mine/cooperation.png'
					},

					{
						id: 3,
						name: '收款密码',
						img: '../../static/images/mine/pwd.png'
					},
					{
						id: 4,
						name: '新手引导',
						img: '../../static/images/mine/kefu.png'
					},
					{
						id: 5,
						name: '设置中心',
						img: '../../static/v2img/szzx.png'
					},

				],
				mark: "",
				tips: '',
				seconds: 60,
				yzCode: '',
				telephone: '',
				newpwd: "",
				phone: "",
				fileList1: [],
				wxwema: '',
				newphone: "",
				tipsb: "发送验证码",
				tipsc: '发送验证码',
				phoneCode: "",
				phoneCodefala: true,
				pwdCodefala: true,
				mesg: 0,
			};
		},

		onShow() {
			this.wxAddCode()
			this.getUserInfo()
			this.getAllMoney()
			this.getPicByAccountDataAsync()
			this.getUserCountData()
			this.mark = uni.getStorageSync('mark')
			let mark = uni.getStorageSync('mark')
			if (mark == 1) {
				this.open()
			} else if (mark == 2) {
				this.$refs.popupcode.open('')
				uni.removeStorageSync('mark');
			}
		},
		/* 下拉刷新 */
		onPullDownRefresh() {
			this.getUserInfo()
			this.getAllMoney()
			this.getPicByAccountDataAsync()
			this.getUserCountData()

		},
		methods: {
			/* 获取用户信息 */
			async getUserInfo() {

				let user = await this.$api.home.getuserBasicData()
				console.log(user);
				if (user.code == 200) {
					this.user = user.data

					uni.setStorageSync('user', JSON.stringify(user.data))
					if (user.data.openId.length == 0) {
						if (location.href.indexOf('code') == -1) {
							this.wxlogin()
						}

					}
					uni.stopPullDownRefresh()
				} else {
					uni.$u.toast('您还未登录');
					setTimeout(
						uni.navigateTo({
							url: '../login/login'
						}), 100)


				}
			},
			/* 登录 */
			login() {

				uni.navigateTo({
					url: '../login/login'
				})

			},
			/* 电话格式化 */
			splieData(item) {
				let n = '****'
				let phoneA = item.slice(0, 3);
				let phoneB = item.slice(7);
				this.phone = `${phoneA}${n}${phoneB}`;
				return `${phoneA}${n}${phoneB}`
			},
			/* 获取收益 */
			async getAllMoney() {
				let {
					data
				} = await this.$api.my.getUserIncoming(

				)
				if (data) {
					uni.stopPullDownRefresh()
				}
				this.money = data

			},
			/* 个人信息设置页面 */
			linkSystem() {
				uni.navigateTo({
					url: './system/system'
				})
			},
			/* 微信登录 */
			wxlogin() {
				let url = location.href.split('pages')[0] + 'pages/my/my'
				console.log(url, 'url');
				const path = encodeURIComponent(url);
				/* const path = encodeURIComponent('http://h5dev.alading365.cn/'); */
				const WxURL = 'https://open.weixin.qq.com/connect/oauth2/authorize'
				const APPIP = "wx73c5f42f04fc4021" // TODO 公众号的appid
				let newURL =
					`${WxURL}?appid=${APPIP}&redirect_uri=${path}&response_type=code&scope=snsapi_userinfo&state=0#wechat_redirect`;
				window.location.replace(newURL);
			},
			/* 微信登录 */
			wxloginB() {
				let url = location.href.split('pages')[0] + 'pages/my'
				console.log(url, 'url');
				const path = encodeURIComponent(url);
				const WxURL = 'https://open.weixin.qq.com/connect/oauth2/authorize'
				const APPIP = "wx73c5f42f04fc4021" //TODO 公众号的appid
				let newURL =
					`${WxURL}?appid=${APPIP}&redirect_uri=${path}&response_type=code&scope=snsapi_base&state=0#wechat_redirect`;
				window.location.replace(newURL);

			},
			/* 我的钱包 */
			limkMoney(i, id) {
				if (id == 1) {
					if (!this.user.openId) {
						uni.$u.toast('请先授权微信')
						this.wxlogin()
					} else {
						uni.navigateTo({
							url: i
						})
					}
				} else if (id == 6) {
					this.open()
				} else if (id == 4) {
					this.$refs.popupphone.open("")
				} else {
					uni.navigateTo({
						url: i
					})
				}


			},
			async wxAddCode() {
				if (location.href.indexOf('code') != -1 && !this.user.openId) {
					let codea = location.href.split('code=')[1]
					let code = codea.split('&state=')[0]
					let res = await this.$api.my.getUserinfo({
						code
					})
					if (res.code == 200) {
						location.href = location.href.split('?code')[0]
					}
				}
			},

			/* 跳转会员中心 */
			linkVip() {
				this.wxloginB()
				/* 	uni.navigateTo({
						url:'vip/vip'
					}) */
			},
			/* 开启定时发放 */

			async getReminderAsync() {
				let res = await this.$api.my.getReminderAsync()
				console.log(res);
				this.getUserInfo()
				if (res.data) {
					uni.$u.toast('已开启点餐闹钟')

				} else {
					uni.$u.toast('亲记得按时吃饭哦')

				}
			},

			/* 跳转过来设置密码 */
			open() {

				setTimeout(item => {
					if (!this.user.phone) {
						uni.$u.toast('请先绑定手机号')
						this.$refs.popupphone.open("")
					} else {
						this.$refs.popupb.open('')
					}

				}, 500)

			},
			close() {
				this.$refs.popupb.close()
				this.newpwd = ''
				this.yzCode = ''
				uni.removeStorageSync('mark');
			},
			/* 获取验证码 */
			codeChange(text) {
				this.tips = text;
			},
			async getCode() {
				/* 
				获取本账号手机验证码 */
				const res = await this.$api.my.getVerificationCode(this.phone)
				if (res.code == 200) {
					uni.$u.toast('验证码已发送');
					this.$refs.uCode.start();
				}
			},
			/* 提交收款密码变更 */
			async setMoneyPassword() {
				if (this.newpwd.length == 6) {
					if (this.yzCode) {
						let res = await this.$api.my.setUserPwd({
							code: this.yzCode,
							pwd: this.newpwd
						})
						if (res.code == 200) {
							uni.$u.toast('更换收款密码成功')
							this.$refs.popupb.close()
						}
						try {
							uni.removeStorageSync('mark');
						} catch (e) {
							// error
						}
					} else {
						uni.$u.toast('请先输入验证码');
					}
				} else {
					uni.$u.toast('请输入完整6位密码')
				}

			},
			// 删除图片
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			async afterRead(event) {
				console.log(1111111111111);
				console.log(event);
				let m = 5 * 1024 * 1024
				let size = event.file[0].size
				if (size <= m) {
					let lists = [].concat(event.file)

					let fileListLen = this[`fileList${event.name}`].length
					lists.map((item) => {
						this[`fileList${event.name}`].push({
							...item,
							status: 'uploading',
							message: '上传中'
						})
					})

					for (let i = 0; i < lists.length; i++) {

						const result = await this.uploadFilePromise(lists[i].thumb, event)

						let item = this[`fileList${event.name}`][fileListLen]
						this[`fileList${event.name}`].splice(fileListLen, 1, Object.assign(item, {
							status: 'success',
							message: '',
							url: result
						}))
						fileListLen++
					}
				} else {
					uni.$u.toast('图片不能大于5M');
				}


			},

			/* 上传图片 */
			uploadFilePromise(url, e) {
				let token = uni.getStorageSync("token");
				return new Promise((resolve, reject) => {
					let a = uni.uploadFile({
						url: '/api/file/user',
						filePath: url,
						name: 'file',
						formData: {
							'file': url
						},
						header: {
							/* "content-type": 'multipart/form-data', */
							"CURRENT_USER_TOKEN": token
						},
						success: (res) => {
							let newdata = JSON.parse(res.data);
							if (newdata.code == 200) {
								resolve(res.data.data);
								this.updetwechatWithdrawPicData(newdata.data)
							} else {
								uni.$u.toast(newdata.msg)
							}
						}
					});
				})
			},
			/* 更新二维码 */
			async updetwechatWithdrawPicData(url) {
				let res = await this.$api.my.updetwechatWithdrawPic(url)
				if (res.code == 200) {
					uni.$u.toast('上传成功')
					this.getPicByAccountDataAsync()
				}
			},
			/* 获取二维码 */
			async getPicByAccountDataAsync() {
				let {
					data
				} = await this.$api.my.getPicByAccountData();
				this.wxwema = data
				this.fileList1 = []
			},
			/* 获取换绑手机验证码 */
			getCodePhone() {
				if (this.newphone.length == 11) {
					let i = 60;
					if (this.phoneCodefala) {
						this.getCodePhoneAsync()
						let seid = setInterval(item => {
								this.phoneCodefala = false
								i--
								this.tipsb = i + 's';
								if (i <= 0) {
									clearInterval(seid)
									this.tipsb = '发送验证码'
									this.phoneCodefala = true
								}
							}

							, 1000)
					}
				} else {
					uni.$u.toast('手机号格式不正确');
				}


			},
			/* 获取换绑密码验证码 */
			getCodepwd() {
				let i = 60;
				if (this.pwdCodefala) {
					this.getCode()
					let seid = setInterval(item => {
							this.pwdCodefala = false
							i--
							this.tipsc = i + 's';
							if (i <= 0) {
								clearInterval(seid)
								this.tipsc = '发送验证码'
								this.pwdCodefala = true
							}
						}

						, 1000)
				}



			},
			/* 获取换绑手机验证码 */
			async getCodePhoneAsync() {
				const res = await this.$api.my.getCodeChangePhoneData(this.newphone)
				if (res.code == 200) {
					uni.$u.toast('验证码已发送');
				}
			},
			/* 提交换绑手机号*/
			async setphoneDataAsunc() {
				if (this.phoneCode) {
					let res = await this.$api.my.setphoneData({
						phone: this.newphone,
						code: this.phoneCode
					})
					if (res.code == 200) {
						uni.$u.toast('更换成功');
						this.$refs.popupphone.close()
					}
				} else {
					uni.$u.toast('请先输入验证码');
				}

			},
			/* 获取消息未读数量*/
			async getUserCountData() {
				let {
					data
				} = await this.$api.message.getUserCount()
				this.mesg = data
				if (data) {
					uni.stopPullDownRefresh()
				}
			},
			/* 去赚钱*/
			linkRecline() {
				uni.navigateTo({
					url: '../recline/recline'
				})
			},
			/* 分销排行 */
			linkRanking() {
				uni.navigateTo({
					url: './ranking/ranking'
				})
			},
			/* 退出登录*/
			removeUser() {
				this.user = {};
				this.money = '';
				uni.clearStorageSync();
				this.$refs.popuptuchu.close()
				location.href = location.href.split('?code')[0]
			},
			/* 去vip页面 */
			wxViplogin() {
				uni.navigateTo({
					url: 'vip/vip'
				})

			},
		},
		watch: {
			mark() {
				if (this.mark == 1) {
					this.open()
				} else if (this.mark == 2) {
					this.$refs.popupcode.open('')
					uni.removeStorageSync('mark');
				}
			}
		}

	}
</script>

<style lang="scss" scoped>
	.my {
		background: #F8F8F8;
		height: 100vh;
	}

	.top {
		width: 100vw;
		/* 	background: url(../../static/my/grzx.png)no-repeat 50% 50%/100% 100%; */
		display: flex;
		padding: 0;
		color: #FFFFFF;

		.top-user-name {
			padding: 40rpx 36rpx;
			display: flex;
			align-items: center;

			.top-user-name-top {
				font-size: 40rpx;
			}

			.top-user-name-top_icon {
				width: 120rpx;
				height: 45rpx;
				line-height: 45rpx;
				background: url(../../static/myimage/fxs.png) no-repeat 50% 50%/100% 100%;
				font-size: 20rpx;
				text-align: right;
				padding-right: 20rpx;
				box-sizing: border-box;
				margin-left: 22rpx;
			}
		}
	}

	.money {
		width: 100%;

		margin: 60rpx auto 0;
		padding: 0 20rpx 20rpx;
		box-sizing: border-box;

	}

	.active_bg {
		position: absolute;
		width: 171rpx;
		height: 171rpx;
		top: -135rpx;
		right: 26rpx;
	}

	.money-title {

		height: 174rpx;
		display: flex;
		background: url(../../static/myimage/title_bg.png)no-repeat 50% 50%/100% 100%;
		position: relative;
		align-items: center;
		padding-left: 30rpx;

		.money-title_right {
			margin-right: 30rpx;

			.money-title_right_a {
				font-size: 40rpx;
				font-weight: bold;
				color: #222222;
			}

			.money-title_right_b {
				font-size: 24rpx;
				font-weight: bold;
				color: #222222;
			}
		}
	}

	.money-title-text {
		height: 34rpx;
		font-size: 36rpx;
		font-weight: bold;
		color: #FBE8D0;
		padding: 62rpx 0 0 38rpx;


	}

	.money-title-but-a {
		width: 146rpx;
		height: 52rpx;
		background: #5A5A5A;
		border-radius: 40rpx 0rpx 0rpx 40rpx;
		font-size: 28rpx;
		text-align: center;
		line-height: 52rpx;
		font-weight: bold;
		color: #FFFFFF;
		position: absolute;
		right: 17rpx;
		bottom: 46rpx;

	}

	.user-icon {
		width: 152rpx;
		height: 152rpx;
		border-radius: 50%;
		position: absolute;
		top: -118rpx;
		right: 40rpx;
	}

	.money-title-but-b {
		margin-left: 12rpx;
		width: 138rpx;
		height: 62rpx;
		background: linear-gradient(-45deg, #E7D8AF 0%, #F2E4C7 100%);
		border-radius: 31rpx;
		font-size: 28rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #222222;
		line-height: 62rpx;
		text-align: center;
	}


	.money-list {
		display: flex;
		justify-content: space-between;
		text-align: center;
		margin-top: 29rpx;
		box-sizing: border-box;


		.money-list-box {
			flex-grow: 1;
			color: #FFFFFF;

			view {
				font-size: 40rpx;
				font-weight: bold;
			}

			text {
				font-size: 24rpx;
			}
		}
	}

	.nav {
		background-color: #FFFFFF;
		width: 100%;
		margin: 0rpx auto 20rpx;
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		text-align: center;
		padding: 50rpx 50rpx 20rpx;
		box-sizing: border-box;
		position: relative;
		font-size: 24rpx;
		color: #333333;

		.nav-list {
			width: 160rpx;

			height: 200rpx;
		}
	}

	.nav-img {
		width: 60rpx;
		height: 60rpx;
	}

	.table {
		width: 702rpx;
		margin: 0 auto;
		background: #FFFFFF;
		/* border-radius: 20rpx; */
	}

	.defaule-but {
		width: 702rpx;
		height: 84rpx;
		line-height: 84rpx;
		text-align: center;
		background: #FFFFFF;
		border-radius: 42rpx;
		font-size: 28rpx;
		font-weight: 500;
		color: #333333;
		margin: 50rpx auto;
		border: 0rpx !important;
	}

	button::after {
		border: none;
	}

	.table {
		width: 702rpx;
		display: flex;
		padding: 24rpx 0rpx;
		justify-content: space-between;
		flex-wrap: wrap;
		box-sizing: border-box;
	}

	.table-box {

		text-align: center;
		font-size: 28rpx;
		font-weight: 400;
		color: #333333;
		height: 75rpx;
		width: 160rpx;
		margin-bottom: 30rpx;


		image {
			width: 40rpx;
			height: 40rpx;
		}

	}

	.popup {
		width: 616rpx;
		min-height: 662rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
		padding-bottom: 30rpx;

		.popup-title {
			font-size: 30rpx;
			font-weight: bold;
			color: #222222;
			height: 80rpx;
			line-height: 80rpx;

		}

		.popup-title-gb {
			width: 40rpx;
			height: 40rpx;
			position: absolute;
			top: 20rpx;
			right: 20rpx;
		}

		.popup-title-box {
			height: 100rpx;
			position: relative;
			border-bottom: 1rpx solid #E1E1E1;
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.popup-title-input {
			padding: 30rpx;
			box-sizing: border-box;
			height: 157rpx;
			border-bottom: 1rpx solid #E1E1E1;
		}

		.popup-title-input-title {

			color: #222222;
			font-size: 28rpx;
			margin-bottom: 20rpx;
		}
	}

	.yz-code-box {
		display: flex;
		justify-content: space-between;
	}

	.fs_code_font {
		width: 146rpx;
		height: 54rpx;
		border: 2rpx solid #FA6D38;
		border-radius: 4rpx;
		font-size: 24rpx;
		font-weight: 500;
		color: #FA6D38;
		line-height: 54rpx;
		text-align: center;
	}

	.popop-but {
		width: 428rpx;
		height: 81rpx;
		background: #FA6D38;
		border-radius: 41rpx;
		font-size: 32rpx;
		font-weight: 500;
		color: #FFFFFF;
		margin-top: 56rpx;

	}

	.fileList {
		margin: 60rpx auto 0;
		width: 436rpx;
		height: 400rpx;
		background: #F8F8F8;
		padding: 20rpx 40rpx;
		box-sizing: border-box;
		position: relative;
	}

	.posho {
		position: absolute;
		top: 120rpx;
		left: 120rpx;
		text-align: center;
		font-size: 28rpx;
		font-weight: 500;
		color: #000000;

		image {
			width: 98rpx;
			height: 98rpx;
		}
	}

	.popup-title-box-code {
		border: 0 !important;
	}

	.phone-title {
		font-size: 28rpx;
		font-weight: 400;
		color: #666666;
	}

	.phone-code-box {
		display: flex;
		line-height: 50rpx;
	}

	.popop-but-text {
		font-size: 26rpx;
		font-weight: 500;
		width: 100%;
		text-align: center;
		margin: 20rpx 0;
		color: #666666;
	}

	.mesg {
		width: 20rpx;
		height: 20rpx;
		background: #FA6D38;
		border-radius: 50%;
		position: absolute;
		top: 100rpx;
		left: 240rpx;
	}

	.wxwema-img {
		height: 350rpx !important;
		width: 350rpx;
	}

	.gai-top-box {
		background: url(../../static/myimage/mybg.png)repeat-y 50% top/100% 100%;
		border-radius: 0 0 30rpx 30rpx;
	}



	.vip-text {
		position: absolute;
		display: flex;
		align-items: center;
		top: 100rpx;

		text {
			font-size: 24rpx;

			font-weight: 400;
			color: rgba(51, 51, 51, 0.76);

		}

		.vip-text-icon {
			width: 64rpx;
			height: 34rpx;
			margin-left: 20rpx;
		}
	}

	.new-center-box {
		width: 702rpx;
		height: 220rpx;
		margin: 0 auto 22rpx;
		box-shadow: 0rpx 2rpx 30rpx 0rpx rgba(0, 0, 0, 0.02);
		border-radius: 16rpx;
		background: #FFFFFF;
		padding: 24rpx 0;
		box-sizing: border-box;

		.new-center-box-title {
			font-size: 32rpx;
			font-weight: 500;
			color: #333333;
			padding-left: 24rpx;
		}

		.new-center-box-nav {
			display: flex;
			justify-content: space-between;
			padding: 0 28rpx;
			box-sizing: border-box;
			height: 160rpx;
			align-items: center;

		}

		.new-center-box-nav-list {
			text-align: center;
			position: relative;

			image {
				width: 40rpx;
				height: 40rpx;
			}

			.new-center-box-nav-list-text {
				font-size: 28rpx;
				font-weight: 400;
				color: #333333;
			}
		}
	}

	.table-fuwu-title {
		background: #FFFFFF;
		width: 707rpx;
		margin: 0 auto;
		padding: 40rpx 28rpx 20rpx;
		font-size: 32rpx;
		box-sizing: border-box;
		border-top-left-radius: 16rpx;
		border-top-right-radius: 16rpx;
		font-weight: 500;
		color: #333333;
		line-height: 44rpx
	}

	.popup-tuicu {
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
	}

	.new-center-box-nav-list-icon {
		width: 20rpx;
		height: 20rpx;
		background: #F9AE3D;
		border-radius: 50%;
		opacity: 0.5;
		position: absolute;
		top: 5rpx;
		left: 0;
	}

	.newMyvip {
		width: 48rpx;
		height: 48rpx;
		position: absolute;
		right: 130rpx;
		top: 0rpx;
	}

	.tim_text_div {
		position: absolute;
		top: 100rpx;
		left: 35rpx;
		font-size: 20rpx;
		color: #FFFFFF;
	}
</style>