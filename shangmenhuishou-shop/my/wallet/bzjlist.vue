<template>
	<view>
		<view class="list_box" v-for="(item,index) in list" :key="index">
			<view class="list_left">
				<view class="name">{{item.title}}</view>
				<view class="flex justify-between align-center">
					<view style="color: red;">{{item.direction==1?'+':'-'}} {{item.amount}}</view>
					<view class="data">{{item.createTime}}</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0"></empty>
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
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(20);


	// 获取明细
	const getlist = () => {
		let data = {
			page: page.value,
			limit: limit.value
		}
		proxy.$http.postJson('/app/depositRecord/page', data).then(res => {
			if (res.code === 0) {
				pages.value = res.data.pages
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records);
				} else {
					list.push(...res.data.records)
				}
			}
			uni.stopPullDownRefresh();
			uni.hideLoading();
		});
	};
	onLoad(option => {
		getlist()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {
		page.value = 1;
		getlist();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1;
			getlist();
		}
	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.list_box {
		width: 90%;
		margin: 0 auto;
		display: flex;
		line-height: 45rpx;
		padding-top: 20rpx;
		border-bottom: 1rpx solid #f8f8f8;
	}

	.list_left {
		flex: 2;
	}

	.name {
		font-size: 26rpx;
		color: #333333;
		font-weight: bold;
		letter-spacing: 2rpx;
	}

	.data {
		color: #999999;
		font-size: 24rpx;
	}

	.list_right {
		flex: 1;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		font-size: 31rpx;
	}
</style>