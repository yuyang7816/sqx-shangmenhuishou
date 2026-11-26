<template>
	<view>
		<view class="bgColor">

			<view class="userTop">
				<image :src="avatar?avatar:'/static/logo.png'" @click="goNav('/pages/my/userinfo')"></image>
				<view class="userTop_info" v-if="isLogin">
					<view class="userTop_infoName">{{userName}}</view>
					<view class="userTop_infoTxt">ID:{{invitationCode}}</view>
				</view>
				<view class="userTop_logo" v-else @click="login()">登录</view>
			</view>

			<view class="tanbox " v-if="XCXIsSelect!='否'">
				<view class="tanboxTit">我的订单</view>
				<view class="tanbox_Item">
					<view class=" tanbox_ItemInfo " @click="goNavSwit(1)">
						<image src="../../static/image/my/daifuwu.png" mode="widthFix"></image>
						<view>待服务</view>
					</view>
					<view class="tanbox_ItemInfo" @click="goNavSwit(2)">
						<image src="../../static/image/my/jinxingz.png" mode="widthFix"></image>
						<view>进行中</view>
					</view>
					<view class="tanbox_ItemInfo" @click="goNavSwit(3)">
						<image src="../../static/image/my/wancheng.png" mode="widthFix"></image>
						<view>已完成</view>
					</view>
					<view class="tanbox_ItemInfo" @click="goNavSwit(4)">
						<image src="../../static/image/my/all.png" mode="widthFix"></image>
						<view>全部订单</view>
					</view>
				</view>
			</view>



			<view class="boxtan ">
				<view class="tanboxTit ">推荐工具</view>
				<view class="flex flex-wrap margin-top">
					<view class="image_tu" @click="goNav('/pages/my/invitationUser')" v-if="XCXIsSelect != '否'">
						<image src="../../static/image/my/yaoqing.png" mode="aspectFill"></image>
						<view>邀请好友</view>
					</view>

					<view class="image_tu" @click="goNav('/my/message/index') " v-if="XCXIsSelect != '否'">
						<image src="../../static/image/my/xinxi.png"></image>
						<view>消息中心</view>
						<view class="image_tuIcon">
							<up-badge max="99" :value="messageCount"></up-badge>
						</view>
					</view>
					<view class="image_tu" @click="goNav('/my/address/index')">
						<image src="../../static/image/my/addre.png"></image>
						<view>我的地址</view>
					</view>
					<view class="image_tu" @click="goNav('/my/other/work')" v-if="XCXIsSelect != '否'">
						<image src="../../static/image/my/zhaopin.png"></image>
						<view>招募回收员</view>
					</view>

					<view class="image_tu" @click="Changekefu()" v-if="XCXIsSelect != '否'">
						<image src="../../static/image/my/kefu.png"></image>
						<view>在线客服</view>
					</view>
					<view class="image_tu" @click="goNav('/my/setting/feedbackIndex') ">
						<image src="../../static/image/my/help.png"></image>
						<view>帮助中心</view>
					</view>
					<view class="image_tu" @click="goNav('/my/setting/feedback') ">
						<image src="../../static/image/my/yijian.png"></image>
						<view>意见反馈</view>
					</view>
					<view class="image_tu" @click="goNav('/my/setting/index')">
						<image src="../../static/image/my/set.png"></image>
						<view>设置中心</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const isLogin = ref(false);
	const avatar = ref('');
	const userName = ref('');
	const invitationCode = ref('');
	const userId = ref('');
	const XCXIsSelect = ref('否')

	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框

	const messageCount = ref(0);
	const setin = ref(null)
	onLoad(option => {

	});
	onShow(() => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
		userId.value = uni.getStorageSync('userId')
		// 用户端_师傅接单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_take_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 用户端_师傅取消订单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_cancel_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 用户端_订单完成小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_complete_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		if (userId.value) {
			isLogin.value = true
			getUserInfo()

			messageCount.value = uni.getStorageSync('messageCount')
			setin.value = setInterval(d => { //定时器，定时去调取聊天未读消息
				messageCount.value = uni.getStorageSync('messageCount')
			}, 6000);
			// this.getRenZheng()
			// this.getRenZhengs()
			// this.getAmount()
			// this.getIsVip()

			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		} else {
			clearInterval(setin.value)

			isLogin.value = false
			userName.value = '匿名'
			avatar.value = ''
			invitationCode.value = ''
		}
	});

	/* 获取用户信息 */
	const getUserInfo = () => {
		proxy.$http.get("/app/base/user/info").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				invitationCode.value = res.data.invitationCode
				avatar.value = res.data.avatar ? res.data.avatar : '../../static/logo.png'
				// 			this.isAuthentication = res.data.isAuthentication

				// 			if (res.data.isPromotion == null || res.data.isPromotion == 0) { //推广员
				// 				this.isPromotion = '0'
				// 			} else {
				// 				this.isPromotion = res.data.isPromotion
				// 			}
				// 			if (res.data.isAgent == null || res.data.isAgent == 0) { //代理商
				// 				this.isAgent = '0'
				// 			} else {
				// 				this.isAgent = res.data.isAgent
				// 			}
				// 			uni.setStorageSync('feiRate', res.data.feiRate) //代理商佣金比例
				// 			uni.setStorageSync('isAuthentication', res.data.isAuthentication)
				uni.setStorageSync('avatar', res.data.avatar)
				uni.setStorageSync('invitationCode', res.data.invitationCode)
				// uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
				// 			uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)

			}
		});
	};
	// 联系客服
	const Changekefu = () => {
		// #ifdef MP-WEIXIN
		if (uni.getStorageSync('sendMsg')) {
			uni.requestSubscribeMessage({
				tmplIds: arr,
				success(re) {
					var datas = JSON.stringify(re);
					if (datas.indexOf("accept") != -1) {
						console.log(re)
					}
				},
				fail: (res) => {
					console.log(res)
				}
			})
		}
		// #endif
		let SelKeFu = uni.getStorageSync('SelKeFu');
		if (SelKeFu == '1') {
			// #ifdef MP-WEIXIN
			try {
				wx.openCustomerServiceChat({
					extInfo: {
						url: uni.getStorageSync('SelKeFuLink')
					},
					corpId: uni.getStorageSync('SelKeFuAppId'),
					success(res) {},
					fail(res) {
						console.error(res)
					}
				})
			} catch (error) {
				console.error("catchcatch" + error)
				uni.showToast({
					title: '请更新至微信最新版本'
				});
			}
			// #endif
			// #ifndef MP-WEIXIN
			let url = uni.getStorageSync('SelKeFuLink');

			if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
				uni.navigateTo({
					url
				});
			} else {
				//#ifndef H5
				uni.navigateTo({
					url: '/pages/index/webView?url=' + url
				});
				//#endif
				//#ifdef H5
				window.location.href = url;
				//#endif
			}
			// #endif
		} else if (SelKeFu == '2') {
			let token = uni.getStorageSync('token');
			if (token) {
				uni.makePhoneCall({
					phoneNumber: uni.getStorageSync('kefuPhone')
				});
			} else {
				login();
			}
		} else if (SelKeFu == '4') {
			let token = uni.getStorageSync('token');
			if (token) {
				uni.navigateTo({
					url: '/my/setting/customer'
				});
			} else {
				login();
			}
		}
	};
	const goNav = (url) => {
		if (userId.value) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.navigateTo({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	const goNavSwit = (index) => {
		uni.setStorageSync('currentIndex', index)
		if (userId.value) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.switchTab({
				url: '/pages/order/index'
			})

		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	/* 登录 */
	const login = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 开启订阅消息
	const openMsg = () => {
		uni.getSetting({
			withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
			success(ret) {
				if (ret.subscriptionsSetting.itemSettings) {
					uni.setStorageSync('sendMsg', true)
					uni.openSetting({ // 打开设置页 
						success(rea) {
							console.log(rea.authSetting)
						}
					});
				} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.setStorageSync('sendMsg', false)
					uni.showModal({
						title: '提示',
						content: '为了更好的体验,请绑定消息推送',
						confirmText: '确定',
						cancelText: '取消',
						success: function(res) {
							if (res.confirm) {
								wx.requestSubscribeMessage({
									tmplIds: arr,
									success(re) {
										var datas = JSON.stringify(re);
										if (datas.indexOf("accept") != -1) {
											console.log(re)
										}
									},
									fail: (res) => {
										console.log(res)
									}
								})
								showModalMsg.value = false
							} else if (res.cancel) {
								showModalMsg.value = true
							}
						}
					})
				}
			}
		})
	};

	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F2F2F7;
	}

	.bgColor {
		width: 100%;
		background: linear-gradient(to bottom, #CFF4DC 0%, #F2F2F7 43%);
	}

	.userTop {
		display: flex;
		align-items: center;
		margin: 0 30rpx;

		image {
			width: 100rpx;
			height: 100rpx;
			border-radius: 55%;
		}

		.userTop_info {
			flex: 1;
			margin-left: 15rpx;

			.userTop_infoName {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 32rpx;
				color: #1D1D1D;
			}

			.userTop_infoTxt {
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 24rpx;
				color: #1D1D1D;
			}
		}

		.userTop_logo {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 34rpx;
			color: #1D1D1D;
			margin-left: 15rpx;
		}
	}

	.tanbox {
		background: #FFFFFF;
		padding: 30rpx;
		border-radius: 16rpx;
		margin: 30rpx;

		.tanboxTit {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #272727;
		}

		.tanbox_Item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 30rpx;

			.tanbox_ItemInfo {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #000000;
				text-align: center;

				image {
					width: 48rpx;
					height: 48rpx;
				}
			}
		}


	}

	.boxtan {
		background: #FFFFFF;
		border-radius: 16rpx;

		margin: 30rpx;

		.tanboxTit {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #272727;
			padding: 30rpx 30rpx 0;
		}

		.image_tu {
			width: 25%;
			text-align: center;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 24rpx;
			color: #000000;
			padding-bottom: 30rpx;
			position: relative;

			image {
				width: 48rpx;
				height: 48rpx;
			}
			.image_tuIcon{
				position: absolute;
				top: 0;
				right: 0;
			}
		}
	}
</style>