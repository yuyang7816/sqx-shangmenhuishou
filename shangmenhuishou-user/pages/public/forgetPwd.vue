<template>
	<view class="container">
		

		<view class="wrapper">

			<view class="cu-form-group">
				<view class="title ">手机号</view>
				<up-input v-model="phone" maxlength="11" type="number" border="none" placeholder="请输入手机号"
					placeholder-class="input-placeholder"></up-input>
			</view>
			<view class="cu-form-group">
				<view class="title ">验证码</view>
				<view class="flex align-center justify-between flex-sub">
					<up-input v-model="code" maxlength="20" border="none" type="number" placeholder="请输入验证码"
						placeholder-class="input-placeholder"></up-input>
					<view @click="getCode()" :class="timer?'activeCode':''" class="send-msg">
						{{text}}
					</view>
				</view>
			</view>
			<view class="cu-form-group">
				<view class="title ">设置密码</view>
				<up-input v-model="password" maxlength="20" border="none" type="password" placeholder="请设置新密码"
					placeholder-class="input-placeholder"></up-input>
			</view>

			<view class="confirm-btn" @click="toLogin">立即找回</view>


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
	// 顶部导航栏背景色
	const bgColor = ref('transparent');
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	// 验证码
	const code = ref('')
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码')
	//手机号效验
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	//获取验证码
	const getCode = () => {
		if (timer.value) {
			return
		}
		if (phone.value == '') {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		// proxy.$http.get(`/app/Login/sendMsg/${phone.value}/forget`).then(res => {
			proxy.$http.postJson('/app/base/verifyCode/sendPhoneCode',{
				phone: phone.value,
				bizCode: 'forget'
			}).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '验证码已发送',
					icon: 'none'
				})
				timer.value = setInterval(() => {
					countDown.value--;
					if (countDown.value == 0) {
						clearInterval(timer.value)
						timer.value = null;
						countDown.value = 60;
						text.value = '获取验证码'
					} else {
						text.value = countDown.value + 's后重新获取'
					}
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})

	};
	// 找回密码
	const toLogin = () => {
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			});
			return
		};

		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		if (!code.value) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			});
			return
		}
		if (!password.value) {
			uni.showToast({
				title: '请输入密码',
				icon: 'none'
			});
			return
		}
		uni.showLoading({
			title: '正在找回中...'
		})
		proxy.$http.postJson('/app/base/auth/resetPassword', {
			password: password.value,
			phone: phone.value,
			code: code.value,
		}).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '找回成功'
				})
				setTimeout(() => {
					uni.navigateTo({
						url: '/pages/public/loginphone'
					});
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	onLoad(option => {


	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page{
		background: #FFFFFF;
	}
	.input-placeholder {
		font-family: Microsoft YaHei;
		font-weight: 400;
		font-size: 26rpx;
		color: #868484 !important;
	}

	.container {
		padding-bottom: 0;
		padding-bottom: constant(safe-area-inset-bottom);
		/* 兼容 IOS<11.2 */
		padding-bottom: env(safe-area-inset-bottom);
		/* 兼容 IOS>=11.2 */
		background: transparent;
		position: relative;
		width: 100%;
		height: 100vh;
		overflow: hidden;

		.wrapper {
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			z-index: 9;
			margin-top: 100px;
			padding: 16px 25px;

			.cu-form-group {
				background: #f4f4f4;
				padding: 0px 30rpx;
				height: 90rpx;
				display: flex;
				align-items: center;
				margin-bottom: 20px;
				border-radius: 30px;

				.title {
					padding-right: 15px;
					font-size: 28rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;

				}

				.send-msg {
					padding: 0 20rpx;
					border-radius: 30px;
					background: #01AF74;
					color: #fff;
					height: 30px;
					font-size: 14px;
					display: flex;
					align-items: center;
					justify-content: center;
				}
			}

			.confirm-btn {
				width: 100%;
				height: 90rpx;
				border-radius: 30px;
				font-size: 32rpx;
				background: #01AF74;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}

	}
</style>