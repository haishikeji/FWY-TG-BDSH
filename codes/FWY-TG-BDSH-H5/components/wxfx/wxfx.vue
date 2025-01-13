<template>
</template>
<script>
	var jweixin = require('jweixin-module')
	export default {
		name: 'wxfx',
		created() {
			let _this = this
			const oScript = document.createElement('script')
			oScript.type = 'text/javascript'
			oScript.src = 'https://res2.wx.qq.com/open/js/jweixin-1.6.0.js'
			document.body.appendChild(oScript)
			setTimeout(() => {
				_this.wxmini()
			}, 1000)
		},

		methods: {
			/* 公众号获取位置 */
			async wxmini() {
				let encodeUrl = location.origin + '/';
				let url = encodeURIComponent(encodeUrl);
				let res = await this.$api.details.getSignatrue({
					url: url
				});
				let user = JSON.parse(uni.getStorageSync('user'));
				console.log(user, '222222222222222');
				let userIdbb = '';
				if (user) {
					userIdbb = user.id;
				}
				let newurl = `${encodeUrl}?userId=${userIdbb}`;
				jWeixin.config({
					debug: false, // 开启调试模式,调用的所有api的返回值会在客户端console.log出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印
					appId: "wx73c5f42f04fc4021", // TODO 必填，公众号的唯一标识！！！！公众号的appid
					timestamp: res.data.timestamp, // 必填，生成签名的时间戳
					nonceStr: res.data.nonceStr, // 必填，生成签名的随机串
					signature: res.data.signature, // 必填，签名
					openTagList: [],
					jsApiList: [
						"updateTimelineShareData",
						"updateAppMessageShareData",
						"onMenuShareAppMessage",
						"onMenuShareTimeline"
					], // 必填，需要使用的JS接口列表
					// 可选，需要使用的开放标签列表，例如['wx-open-launch-app']
				})
				//配置成功之后的函数，按钮生成成功
				jweixin.ready(res => {
					jweixin.updateAppMessageShareData({
						title: '本地生活', // 分享标题
						desc: '本地生活', // 分享描述
						link: newurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						imgUrl: '../../static/images/home/bg1.png',
						success: function() {

						}
					})
					jweixin.onMenuShareAppMessage({
						title: '本地生活', // 分享标题
						desc: '本地生活', // 分享描述
						link: newurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						imgUrl: '../../static/images/home/bg1.png',
						success: function() {
							// 用户点击了分享后执行的回调函数
						}
					});
					jweixin.updateTimelineShareData({
						title: '本地生活', // 分享标题
						desc: '本地生活', // 分享描述
						link: newurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						imgUrl: '../../static/images/home/bg1.png',
						// 分享图标
						success: function() {}

					});
					jweixin.onMenuShareTimeline({
						title: '本地生活', // 分享标题
						desc: '本地生活', // 分享描述
						link: newurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						imgUrl: '../../static/images/home/bg1.png',
						success: function() {
							// 用户点击了分享后执行的回调函数
						}
					})
				});
				jweixin.error(res => {
					// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名
				});
			},



		},


	}
</script>

<style scoped lang="scss">
	.wxfx {
		height: 200rpx;
		width: 750rpx;
	}
</style>