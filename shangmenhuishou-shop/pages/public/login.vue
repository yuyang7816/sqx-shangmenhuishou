<template>
	<view class="container">
		<!-- #ifdef H5 -->

		<view class="wrapper">
			<view style="margin-top: 200rpx;">
				<image src="/static/logo.png" style="width:200rpx;height:200rpx;border-radius:55%;"></image>
			</view>


			<!-- #ifdef APP -->
			<button class='confirm-btn' @click="login_phone" v-if="AppPhone">本机号码一键登录</button>

			<button v-if="weixinLogin" class="confirm-btn margin-xxxl" @click="weixinLo">微信登录</button>
			<button v-if="!weixinLogin" class='confirm-btn margin-xxxl' @click="register">账号登录</button>
			<!-- #endif -->

			<!-- #ifndef APP -->
			<button v-if="weixinLogin" class="confirm-btn margin-xxxl" @click="weixinLo">微信登录</button>
			<button v-if="!weixinLogin" class='confirm-btn margin-xxxl' @click="register">账号登录</button>
			<!-- #endif -->

			<!-- 协议 -->
			<view class="loginXy flex align-center justify-center">
				<view class="loginXy-box flex align-center justify-center">
					<up-checkbox activeColor="#01AF74" shape="circle" label="" name="agree" usedAlone
						v-model:checked="check">
					</up-checkbox>
					<view @click="check = !check" class="flex align-center">
						登录即代表同意
						<text @click.stop.prevent="goNav('/my/setting/xieyi')">《用户服务协议》</text>
						及
						<text @click.stop.prevent="goNav('/my/setting/mimi')">《隐私政策》</text>
					</view>
				</view>
			</view>
		</view>
		<!-- #endif -->

		<!-- #ifdef MP-WEIXIN -->
		<view class="wxbox">
			<view class='headers'>
				<image src='../../static/logo.png'></image>
			</view>
			<view class='content'>
				<view>申请获取以下权限</view>
				<text>获得你的公开信息(昵称，头像、地区等)</text>
			</view>


			<button class='confirm-btn ' bindtap="getUserProfile" @tap="wxGetUserInfokJ" v-if="!isUserPhone">
				手机号快捷登录
			</button>

			<view v-else class="">
				<button v-if="!check" class='confirm-btn' bindtap="getUserProfile" @tap="wxPhone">
					手机号快捷登录
				</button>
				<button v-else class="confirm-btn" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
					手机号快捷登录
				</button>
			</view>

			<!-- 
			<button v-show="weixinPhone" style="background: #EF276A;color: #FFFFFF;" class="bottom"
				open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
				授权手机号
			</button>
			<button v-show="!weixinPhone" style="background: #EF276A;color: #FFFFFF;" class='bottom'
				bindtap="getUserProfile" @tap="wxGetUserInfo">
				授权登录
			</button> -->
			<!-- 协议 -->
			<view class="loginXy flex align-center justify-center">
				<view class="loginXy-box flex align-center justify-center">
					<up-checkbox activeColor="#01AF74" shape="circle" label="" name="agree" usedAlone
						v-model:checked="check">
					</up-checkbox>
					<view @click="check = !check" class="flex align-center">
						登录即代表同意
						<text @click.stop.prevent="goNav('/my/setting/xieyi')">《用户服务协议》</text>
						及
						<text @click.stop.prevent="goNav('/my/setting/mimi')">《隐私政策》</text>
					</view>
				</view>
			</view>
		</view>
		<!-- #endif -->

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
	// 用户 userId
	const userId = ref('');

	const weixinPhone = ref(false);
	const sessionkey = ref('');

	const userName = ref('');
	const userAvatarUrl = ref('');
	const phone = ref('');
	const invitation = ref('');


	const isUserPhone = ref(true);
	const sendDataList = ref({});
	const flag = ref('');


	//是否选择协议
	const check = ref(false);
	
	
	const wxPhone = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			})
		}

	};
	// 返回上一页
	const rightClick = () => {
		var pages = getCurrentPages();
		if (pages.length > 1) {
			uni.navigateBack()
		} else {
			uni.switchTab({
				url: '/pages/index/index'
			})
		}
	};
	// 账号登录
	const register = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			});
			return
		};
		uni.navigateTo({
			url: '/pages/public/loginphone'
		});
	};
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	};
	onLoad(option => {
		userId.value = uni.getStorageSync('userId');

		// #ifdef MP-WEIXIN
		uni.login({
			provider: 'weixin',
			success: function(loginRes) {
				console.log(loginRes, 'code')
				let data = {
					code: loginRes.code,
					client:'waste'
				}
				proxy.$http.get('/app/base/auth/wxLogin', data).then(res => {
					if (res.code == 0 && res.data) {
						uni.setStorageSync('openId', res.data.openid)
						uni.setStorageSync('unionId', res.data.unionid)
						uni.setStorageSync('sessionkey', res.data.sessionKey)
						let inviterCode = '';
						if (uni.getStorageSync('inviterCode')) {
							invitationCode = uni.getStorageSync(
								'inviterCode')
						}
						let sendData = {
							openId: uni.getStorageSync('openId'),
							unionId: uni.getStorageSync('unionId'),
							userName: '微信用户',
							avatar: '',
							sex: '', //性别
							inviterCode: inviterCode //别人登录进来携带你的邀请码
						};
						sendDataList.value = sendData;
						if (res.data.bindPhone) {
							isUserPhone.value = false
						} else {
							isUserPhone.value = true
						}
					}
				})

			}
		});
		// #endif

	});
	// 快捷登录
	const wxGetUserInfokJ = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			});
			return
		};
		uni.login({
			provider: 'weixin',
			success: function(loginRes) {
				console.log(loginRes, 'code')

				let data = {
					code: loginRes.code,
					client:'waste'
				}
				proxy.$http.get('/app/base/auth/wxLogin', data).then(res => {
					if (res.code == 0 && res.data) {

						uni.setStorageSync('openId', res.data.openid)
						uni.setStorageSync('unionId', res.data.unionid)
						uni.setStorageSync('flag', res.data.bindPhone)
						uni.setStorageSync('sessionkey', res.data
							.sessionKey)

						if (res.data.bindPhone) {
							uni.setStorageSync('token', res.data.loginVO.token)

							uni.setStorageSync('userName', res.data.userEntity.userName)
							uni.setStorageSync('avatar', res.data.userEntity.avatar)
							uni.setStorageSync('phone', res.data.userEntity.phone)
							uni.setStorageSync('invitationCode', res.data.userEntity
								.invitationCode)
							uni.setStorageSync('userId', res.data.userEntity.userId)
							uni.setStorageSync('openId', res.data.userEntity.openId)
							setTimeout(() => {
								rightClick()
							}, 1000)
						} else {
							let invitationCode = '';
							if (uni.getStorageSync('inviterCode')) {
								invitationCode = uni.getStorageSync(
									'inviterCode')
							}
							let sendData = {
								openId: uni.getStorageSync('openId'),
								unionId: uni.getStorageSync('unionId'),
								userName: '游客',
								avatar: '/static/logos.png',
								sex: '', //性别
								inviterCode: invitationCode, //别人登录进来携带你的邀请码
							};
							getWeixinInfo(sendData);
						}

					}
				})

			}
		});
	};
	//小程序微信登录后获取手机号
	const getPhoneNumber = (e) => {
		if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
			console.log('用户拒绝提供手机号');
		} else {
			console.log('用户同意提供手机号');
			if (e.detail.iv) {
				setPhoneByInsert(e.detail.encryptedData, e.detail.iv);
			}

		}
	};
	//小程序微信登录后获取手机号
	const setPhoneByInsert = (decryptData, iv) => {
		// console.log(decryptData, iv, '565656565656')
		let data = {
			decryptData: decryptData,
			key: uni.getStorageSync('sessionkey'),
			iv: iv
		};
		proxy.$http.postJson('/app/base/auth/decryptPhone', data).then(res => {
			if (res.code == 0) {
				phone.value = res.data.phoneNumber;
				getWeixinInfo(sendDataList.value);
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				});
			}
		})
	};
	//获取个人信息
	const getWeixinInfo = (sendData) => {
		if (!sendData) {
			let sendData = sendDataList.value
		}
		uni.showLoading({
			title: '登录中...'
		});
		let postData = {
			openId: sendData.openId ? sendData.openId : uni.getStorageSync('openId'), //小程序openId
			// unionId: sendData.unionId ? sendData.unionId : uni.getStorageSync('unionId'), //unionId
			userName: sendData.userName, //微信名称
			avatar: '', //头像
			sex: sendData.sex, //性别
			phone: phone.value,
			superiorInvitationCode: sendData.inviterCode ? sendData.inviterCode : uni.getStorageSync('inviterCode'),
		};
		
		proxy.$http.postJson('/app/base/auth/wxJsRegister', postData).then(res => {
			uni.hideLoading();
			if (res.code == 0) {
				uni.setStorageSync('token', res.data.token)
				uni.setStorageSync('userName', res.data.user.userName)
				uni.setStorageSync('avatar', res.data.user.avatar)
				uni.setStorageSync('phone', res.data.user.phone)
				uni.setStorageSync('invitationCode', res.data.user.invitationCode)
				uni.setStorageSync('sex', res.data.user.sex)
				uni.setStorageSync('userId', res.data.user.userId)
				uni.setStorageSync('openId', res.data.user.openId)

				setTimeout(() => {
					rightClick()
				}, 1000)
			} else {
				uni.showModal({
					showCancel: false,
					title: '登录失败',
					content: res.msg,
				});
			}
		})
	};
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.container {
		padding-bottom: 0;
		padding-bottom: constant(safe-area-inset-bottom);
		/* 兼容 IOS<11.2 */
		padding-bottom: env(safe-area-inset-bottom);
		/* 兼容 IOS>=11.2 */

		.bgBot {
			width: 100%;
			position: fixed;
			top: 0;
			left: 0;
			right: 0;
			z-index: 9;
		}

		// 小程序
		.mp_wxBox {
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			z-index: 90;
			padding-bottom: 20px;
			text-align: center;

			.headers {
				margin: 35% auto 20rpx;
				text-align: center;
				border-radius: 60rpx;
				width: 650rpx;
				height: 106rpx;

				image {
					width: 393rpx;
					height: 116rpx;
				}
			}

			.content {
				font-size: 32rpx;
				text-align: center;
				letter-spacing: 2px;
				margin-top: 20rpx;
			}

			text {
				display: block;
				color: #FFFFFF;

			}

			.bottom {
				line-height: 100rpx;
				border-radius: 80rpx;
				margin: 260px 50rpx 30rpx 50rpx;
				height: 100rpx;
				font-size: 35rpx;
			}
		}

		// H5 样式
		.wrapper {
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			z-index: 9;
			padding-bottom: 20px;
			text-align: center;
		}

	}


	.confirm-btn {
		width: 630rpx;
		height: 100rpx;
		line-height: 100rpx;
		border-radius: 30px;
		font-size: 32rpx;
		background: #01AF74;
		color: #FFFFFF;
	}

	.margin-xxxl {
		margin-top: 500rpx;
	}

	.loginXy {
		width: 100%;
		margin-top: 26rpx;

		.loginXy-box {
			width: 646rpx;
			font-size: 24rpx;
			color: #333333;

			text {
				color: #01AF74;
			}
		}
	}


	.wxbox {
		padding: 40rpx;

		.headers {
			border-bottom: 1px solid #ccc;
			padding-bottom: 50rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				border-radius: 55%;
				width: 200rpx;
				height: 200rpx;
			}
		}

		.content {
			margin: 90rpx 0;

			text {
				display: block;
				color: #9d9d9d;
				margin-top: 40rpx;
			}
		}

		.bottom {
			line-height: 80rpx;
			border-radius: 80rpx;
			margin: 70rpx 0rpx;
			height: 80rpx;
			font-size: 35rpx;
		}
	}
</style>