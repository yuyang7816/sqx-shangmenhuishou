<template>
	<view>
		<view style="line-height: 26px;padding: 32rpx;" class="home1">
			<view style="font-size: 28rpx;" v-html="content"></view>
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

	const content = ref('');

	// 获取"关于我们"协议  187
	const getText = () => {
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_about_us'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					content.value = res.data.value;
				}
			}
		});
		
	}

	onLoad(option => {
		getText()
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
</style>