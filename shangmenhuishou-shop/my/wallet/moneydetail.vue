<template>
	<view>
		<view style="text-align: left;padding-bottom: 10rpx;">
			<view v-for="(item, index) in list" :key="index" class="item">
				<view>
					<view style="color: #999999;font-size: 28rpx;">
						<view class="flex align-center" style="margin-bottom: 8rpx">
							{{item.title}}
						</view>
						<view style="margin-bottom: 8rpx">{{item.description}}</view>
						<view style="margin-bottom: 8rpx"> 创建时间：{{item.createTime}}</view>
						<view style="margin-bottom: 8rpx;text-align: right;">
							<text v-if="item.direction == 1" class="text-olive"
								style="font-size: 32rpx;font-weight: 600"><text
									class="text-olive">+</text>{{item.amount}}元</text>
							<text v-if="item.direction == 2" class="text-red" style="font-size: 32rpx;font-weight: 600"><text
									class="text-red">-</text>{{item.amount}}元</text>
						</view>
					</view>
				</view>
			</view>
			<empty v-if="list.length == 0" contont="暂无明细"></empty>
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
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();
	const list = reactive([]);
	//分页
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	onLoad(option => {
		getList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getList();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1;
			getList();
		}
	});
	const copyClick = (title) => {
		uni.setClipboardData({
			data: title,
			success: function(res) {
				uni.showToast({
					title: "复制成功",
					icon: 'none',
				});
			},
		});
	};
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value
		}
		proxy.$http.postJson('/app/userWalletTransaction/page', data).then(res => {
			if (res.code === 0) {
				pages.value = res.data.totalPage
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
</script>

<style lang="less">
	page {
		background: #FFFFFF;
	}

	.tui-tab-item-title {
		// color: #ffffff;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		border-bottom: 1px solid #53D8A4;
		color: #53D8A4;
		font-size: 32rpx;
		font-weight: bold;
		border-bottom-width: 6rpx;
		text-align: center;
	}

	.item {
		background: #FFFFFF;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16rpx;
	}
	.text-olive{
		color: #70c541;
	}
	.text-red{
		color: #ff0000;
	}
</style>