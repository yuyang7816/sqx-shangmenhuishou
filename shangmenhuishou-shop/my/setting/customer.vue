<template>
	<view style="height: 100vh;margin: 32rpx;">
		<view style="text-align: center;background: #FFFFFF;padding: 40rpx;border-radius: 32rpx;">
			<view style="font-size: 38rpx;color: #000">添加客服微信咨询</view>
			<view style="font-size: 32rpx;margin-top: 32rpx;color: #000">微信号：{{weixin}}</view>
			<view @click="copyHref"
				style="background: #2FD7C8;width:200rpx;font-size: 30rpx;color: #FFFFFF;padding: 4rpx 20rpx;border-radius: 24rpx;margin: 20rpx auto;">
				一键复制</view>

			<image @click="prewImg(image)" mode="aspectFit" style="margin-top: 32rpx" :src="image"></image>
			<view style="font-size: 28rpx;color: #000;margin-top: 32rpx" v-if="isWeiXin">
				{{ isWeiXin ? '长按识别上方二维码' : '' }}
			</view>
			<view v-if="isWeiXin" style="font-size: 24rpx;color: #FFFFFF;margin-top: 80rpx" @click="rests">无法识别？</view>
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
	const image = ref('');
	const isWeiXin = ref(false);
	const weixin = ref('710070994');
	const webviewStyles = reactive({
		progress: {
			color: '#1A1929 '
		}
	});
	const SelKeFu = ref('2');
	onLoad(option => {
		SelKeFu.value = uni.getStorageSync('SelKeFu');
		// #ifdef H5
		let ua = navigator.userAgent.toLowerCase();
		if (ua.indexOf('micromessenger') !== -1) {
			isWeiXin.value = true;
		}
		// #endif
		// 客服二维码
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_customer_qrcode'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					image.value = res.data.value;
				}
			}
		});
		
		// 客服微信号
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_customer_wx'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					weixin.value = res.data.value;
				}
			}
		});
	
	});
	//邀请码复制
	const copyHref = () => {
		uni.setClipboardData({
			data: weixin.value,
			success: r => {
				uni.showToast({
					title: '复制成功'
				})
			}
		});
	};
	const prewImg = (img) => {
		uni.previewImage({
			urls: [img],
			current: 0
		})
	};
	const rests = () => {
		uni.showToast({
			title: '已刷新请再次长按识别',
			mask: false,
			duration: 1500,
			icon: 'none'
		});
		window.location.reload();
	};
</script>

<style>

</style>