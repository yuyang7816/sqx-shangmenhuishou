<template>
	<view style="text-align: left">
		<view v-if="list.length" v-for="(item, index) in list" :key="index" class="item">
			<view>
				<view class="flex align-center justify-between" style="margin-bottom: 8rpx;">
					<view >{{item.accountType==1?'微信':'支付宝'}}提现
					</view>
					<text style="margin-bottom: 8rpx;color: green" v-if="item.status==1"> 提现中</text>
					<text style="margin-bottom: 8rpx;color: green" v-if="item.status==2"> 提现成功</text>
					<text style="margin-bottom: 8rpx;color: red" v-if="item.status==3"> 提现失败</text>
				</view>

				<view style="color: #999999;font-size: 28rpx;">
					<view style="margin-bottom: 8rpx"> 提现金额：¥{{item.amount}}</view>
					<view style="margin-bottom: 8rpx"> 提现手续费：¥{{item.serviceFee}}</view>
					
					<view style="margin-bottom: 8rpx"> 发起时间：{{item.createTime}}</view>


					<view style="margin-bottom: 8rpx" v-if="item.state==1">转账时间 {{item.withdrawTime}}</view>
					<!-- <view style="margin-bottom: 8rpx;color: #448AFD" v-if="item.status==2">{{item.refund}}</view> -->

					<view style="margin-bottom: 8rpx;text-align: right;">
						<!-- 提现金额： -->
						<text style="color: #448AFD;font-size: 32rpx;font-weight: 600"> ￥{{item.actualAmount}}</text>
					</view>
				</view>
			</view>
		</view>

		<empty v-if="list.length == 0" contont="暂无明细" />

	</view>
</template>

<script setup>
	import empty from '@/components/empty/empty.vue'
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
	const {
		proxy
	} = getCurrentInstance();
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	//记录列表
	const list = reactive([]);
	onLoad(() => {
		getList()
	});
	onPullDownRefresh(() => {
		page.value = 1
		getList();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			getList();
		}
	});
	//获取记录列表
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value
		}
		proxy.$http.postJson('/app/withdrawRecord/page', data).then(res => {
			uni.stopPullDownRefresh();
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		})
	};
</script>

<style lang='scss' scoped>
	page {
		background: #FFFFFF;
	}

	.item {
		background: white;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16rpx;
	}

	.centre {
		text-align: center;
		margin: 200rpx auto;
		font-size: 32rpx;

		image {
			width: 360rpx;
			height: 360rpx;
			// margin-bottom: 20rpx;
			margin: 0 auto 20rpx;
			// border: 1px dotted #000000;
		}

		.tips {
			font-size: 34rpx;
			color: #999999;
			margin-top: 20rpx;
		}

		.btn {
			margin: 80rpx auto;
			width: 600rpx;
			border-radius: 32rpx;
			line-height: 90rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #ff7581;
		}
	}
</style>