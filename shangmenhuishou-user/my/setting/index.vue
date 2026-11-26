<template>
	<view class="padding-lr">
		<view class="flex align-center padding-tb" @click="goNav('/my/setting/feedback')" v-if="XCXIsSelect!='否'">
			<view class="title ">意见反馈</view>
			<image src="../../static/image/my/right.png" mode="widthFix" style="width: 16rpx;height: 27rpx;"></image>
		</view>

		<!-- <view class="flex align-center padding-tb" @click="goNav('/my/setting/feedbackIndex')">
			<view class="title" style="line-height: 50rpx;">帮助中心</view>
			<image src="../../static/image/my/right.png" mode="widthFix" style="width: 16rpx;height: 27rpx;"></image>
		</view> -->
		
		<view class="flex align-center padding-tb" @click="goNav('/my/setting/about')">
			<view class="title" style="line-height: 50rpx;">关于我们</view>
			<image src="../../static/image/my/right.png" mode="widthFix" style="width: 16rpx;height: 27rpx;"></image>
		</view>

		<view class="flex align-center padding-tb" @click="goNav('/my/setting/xieyi')">
			<view class="title" style="line-height: 50rpx;">用户协议</view>
			<image src="../../static/image/my/right.png" mode="widthFix" style="width: 16rpx;height: 27rpx;"></image>
		</view>

		<view class="flex align-center padding-tb" @click="goNav('/my/setting/mimi')">
			<view class="title" style="line-height: 50rpx;">隐私政策</view>
			<image src="../../static/image/my/right.png" mode="widthFix" style="width: 16rpx;height: 27rpx;"></image>
		</view>

		<view v-if="userId" class="flex  padding-tb" @click="goNav('/my/setting/zhuxiao')">
			<view class="title" style="line-height: 50rpx;">注销账号</view>
			<image src="../../static/image/my/right.png" mode="widthFix" style="width: 16rpx;height: 27rpx;"></image>
		</view>
		<view class="btn" @click="goOut" v-if="userId">退出登录</view>
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
	// 引入底部 Tabbar 组件
	const userId = ref('');
	// 小程序上架开关
	const XCXIsSelect = ref('是');
	const checked = ref(true);
	// 个性化推荐
	const checkedgx = ref(true);
	// 消息通知
	const change = (status) => {
		proxy.$http.post('/app/user/updateSendMsg', {
			isSendMsg: status ? 1 : 2
		}).then(res => {
			if (res.code == 0) {

			}
		});
	};
	// 个性化推荐
	const changegx = (status) => {
		console.log(status)
		if (status) {
			uni.setStorageSync('gexing', 1);
		} else {
			uni.setStorageSync('gexing', 2);
		}
	};
	// 跳转中心
	const goNav = (e) => {
		if (e == "/my/detail/goreport" || e == "/my/setting/zhuxiao") {
			if (!uni.getStorageSync('userId')) {
				uni.navigateTo({
					url: '/pages/public/login'
				})
			} else {
				uni.navigateTo({
					url: e
				})
			}
		} else {
			uni.navigateTo({
				url: e
			})
		}
	};
	// 退出登录
	const goOut = () => {
		uni.showModal({
			title: '提示',
			content: '确定退出登录吗？',
			success: function(res) {
				if (res.confirm) {
					console.log('用户点击确定');
					uni.removeStorageSync('userName')
					uni.removeStorageSync('avatar')
					uni.removeStorageSync('userId')
					uni.removeStorageSync('token')
					uni.removeStorageSync('phone')
					uni.removeStorageSync('zhiFuBaoName')
					uni.removeStorageSync('zhiFuBao')
					uni.removeStorageSync('invitationCode')
					uni.removeStorageSync('unionId')
					uni.removeStorageSync('openId')
					uni.removeStorageSync('isVIP')
					// uni.clearStorageSync()
					uni.showToast({
						title: '退出成功！',
						icon: 'none'
					})
					setTimeout(function() {
						uni.navigateBack()
					}, 1000)
				} else if (res.cancel) {
					console.log('用户点击取消');
				}
			}
		})
	};
	onLoad(option => {
		userId.value = uni.getStorageSync('userId')
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')

	});
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

	.btn {
		width: 100%;
		height: 88rpx;
		border-radius: 14rpx;
		margin-top: 40rpx;
		font-size: 34rpx;
		background: #2FB57A;
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 28rpx;
		color: #FFFFFF;
		
		display: flex;
		align-items: center;
		justify-content: center;

	}

	.title {
		flex: 1;
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 28rpx;
		color: #1A1A1A;
	}
</style>