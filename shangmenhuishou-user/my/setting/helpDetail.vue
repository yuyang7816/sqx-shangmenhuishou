<template>
	<view>

		<view class="helpTitle">{{ resultData.title }}</view>
		<view class="helpCon" v-html="content "></view>

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

	const helpWordId = ref('');
	const resultData = ref()
	const content = ref('')

	// 获取详情
	const getContent = () => {
		let data = {
			id: helpWordId.value,
		}
		proxy.$http.postJson("/app/help/article/info", data).then(res => {
			if (res.code == 0) {
				resultData.value = res.data
				content.value = res.data.content.replace(new RegExp("img", "g"),
					'img style="width:100%;height:auto;"')
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}

	onLoad(options => {
		helpWordId.value = options.helpWordId
		uni.setNavigationBarTitle({
			title: options.title
		});
		getContent()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style>
	page {
		background: #FFFFFF;
	}

	.helpTitle {
		font-size: 40rpx;
		font-weight: bold;
		margin: 50rpx 30rpx 30rpx;

	}

	.helpCon {
		font-size: 30rpx;
		margin: 30rpx 30rpx 50rpx;

		line-height: 2em;
	}
</style>