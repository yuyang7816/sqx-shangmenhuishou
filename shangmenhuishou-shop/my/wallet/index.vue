<template>
	<view class="">
		<view class="bg padding">
			<view class="box">
				<view class="tit">可用资金(元)</view>
				<view class="pri"><text>{{money?money:'0'}}</text> 元</view>
				<view class="flex align-center justify-between margin-top-xl">
					<view class="">
						<view>总资金</view>
						<view style="font-size: 44rpx;">{{money?money:'0'}}</view>
					</view>

				</view>
			</view>
			<view class="flex align-center justify-between padding-top">
				<view>
					<view class="padding-bottom-xs">总资产（元）</view>
					<view class="text-bold" style="font-size: 48rpx;">{{money?money:'0'}}</view>
				</view>
				<view class="flex align-center"  v-if="XCXIsSelect!='否'">
					<view class="cz" @click="goTo('/my/wallet/tixian')">提现</view>
				</view>
			</view>
		</view>

		<!-- 我的列表 -->
		<view class="mylist">
			<view class="list_box" @click="goTo('/my/wallet/moneydetail')">
				<view class="list_left">
					<image src="../static/zijin.png"></image>
					<view class="t">钱包明细</view>
				</view>
				<image src="/static/image/my/right.png" mode="widthFix" style="width: 18rpx;height: 18rpx;"></image>
			</view>
			<view class="list_box" @click="goTo('/my/wallet/zhifubao')">
				<view class="list_left">
					<image src="../static/zhanghao.png"></image>
					<view class="t">提现账号</view>
				</view>
				<image src="/static/image/my/right.png" mode="widthFix" style="width: 18rpx;height: 18rpx;"></image>
			</view>
			<view class="list_box" @click="goTo('/my/wallet/cashList')">
				<view class="list_left">
					<image src="../static/tixian.png"></image>
					<view class="t">提现记录</view>
				</view>
				<image src="/static/image/my/right.png" mode="widthFix" style="width: 18rpx;height: 18rpx;"></image>
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
	const balance = ref(''); //可用资金
	const money = ref(''); //余额
	const XCXIsSelect = ref('是')

	// 跳转
	const goTo = (url) => {
		uni.navigateTo({
			url
		})
	};
	// 获取余额
	const getMoney = () => {
		proxy.$http.getT('/app/userWallet/my').then(res => {
			if (res.code == 0) {
				money.value = res.data.amount
			}
		});
	};
	onLoad(option => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
	});
	onShow(() => {
		getMoney();
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background: #F5F5F5;
	}

	.bg {
		background: #FFFFFF;
	}

	.box {
		background: #2FB57A;
		border-radius: 16rpx;
		padding: 40rpx;
		color: #FFFFFF;

		.tit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 32rpx;
			color: #FFFFFF;
		}

		.pri {
			font-family: DINPro;
			font-weight: 500;
			font-size: 30rpx;

			text {
				font-size: 68rpx;
			}
		}
	}

	.cz {
		width: 160rpx;
		height: 70rpx;
		background: #2FB57A;
		border-radius: 8rpx;
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 32rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.mylist {
		background: #FFFFFF;
		margin-top: 20rpx;
		padding: 20rpx 30rpx;

		.list_box {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 25rpx 0;

			.list_left {
				display: flex;
				align-items: center;

				image {
					width: 54rpx;
					height: 54rpx;
				}

				.t {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 32rpx;
					color: #333333;
					margin-left: 20rpx;
				}
			}
		}

	}
</style>