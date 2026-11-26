<template>
	<view style="padding-bottom:100px;">
		<view class="title" v-if="order.status==0">待接单</view>
		<view class="title" v-if="order.status==1">待服务</view>
		<view class="title" v-if="order.status==2">进行中</view>
		<view class="title" v-if="order.status==3">已完成</view>
		<view class="title" v-if="order.status==4">已取消</view>

		<view class="addbox">
			<view class="addbox_cot">
				<view class="addbox_cot_title">
					{{order.addressName}}
					<text>{{maskPhoneNumber(order.addressPhone)}}</text>
				</view>
				<view class="addbox_cot_tit">{{order.province}}/{{order.city}}/{{order.district}}/{{order.address}}
				</view>

			</view>
		</view>

		<view class="infobox" v-if="order.wasteUser&&(order.status==1||order.status==2)">
			<view class="infobox_title">联系回收员</view>
			<view class="flex align-center margin-top-sm">
				<image :src="order.wasteUser?order.wasteUser.avatar:'/static/logo.png'"
					style="width: 80rpx;height: 80rpx;border-radius: 55rpx;" mode="aspectFill"></image>
				<view class="flex-sub margin-left-sm flex align-center justify-between">
					<view class="">
						<view>{{order.wasteUser.userName}}</view>
						<view>{{maskPhoneNumber(order.wasteUser.phone)}}</view>
					</view>
					<view @click="callPhone(order.wasteUser.phone)">
						<up-icon name="phone-fill" color="#2979ff" size="28"></up-icon>
					</view>
				</view>
			</view>
		</view>
		<view class="infobox">
			<view class="infobox_title">下单信息</view>
			<view v-for="(item,index) in order.orderItems" :key="index" class="infobox_Ite">
				<view>{{item.categoryName}}/{{item.categoryChildrenName}}</view>
				<view>{{item.unit}}</view>
			</view>
			<view class="infobox_Item">
				<view class="infobox_Item_title">预约时间</view>
				<view class="infobox_Item_tit" style="color: #FF6027;">{{order.appointmentTime}}</view>
			</view>
			<view class=" margin-top-sm">
				<view class="infobox_Item_title">备注</view>
				<view class="infobox_remktit">{{order.note?order.note:'暂无备注'}}</view>
			</view>
		</view>


		<view class="infobox">
			<view class="infobox_title">订单信息</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">订单编号</view>
				<view class="infoboxItem_tit">{{order.orderNo}}
					<image src="../static/copy.png" mode="widthFix" @click.stop="copyClick(order.orderNo)"></image>
				</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">下单时间</view>
				<view class="infoboxItem_tit">{{order.createTime}}</view>
			</view>
			<view class="infoboxItem">
				<view class="infoboxItem_title">支付方式</view>
				<view class="infoboxItem_tit">线下支付</view>
			</view>
		</view>


		<view class="taber">
			<view class="taber_tit">成交价格以实际估价为准</view>
			<view @click="cancelOrder()" class="taber_btn" v-if="order.status == 0">取消订单</view>
			<view @click="delOrder()" class="taber_btn" v-if="order.status == 4">删除订单</view>

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
	const id = ref('');
	const order = ref({});

	onLoad(option => {
		if (option.id) {
			id.value = option.id
			getDetail()
		}
	});
	onShow(() => {

	});
	// 联系人
	const callPhone = (phone) => {
		uni.makePhoneCall({
			phoneNumber: Number(phone) //仅为示例
		});
	};
	// 打开地图
	const openMap = () => {
		uni.openLocation({
			latitude: latitude,
			longitude: longitude,
			success: function() {
				console.log('success');
			}
		});
	};
	//加密中间四位
	const maskPhoneNumber = (phoneNumber) => {
		// 将手机号码转换为字符串类型，并根据其长度确定要替换的位数
		if (phoneNumber) {
			phoneNumber = phoneNumber.toString() //先强制转换成字符串类型
			return phoneNumber.replace(/^(\d{3})\d{4}(\d{4})$/, '$1****$2')
		}

	};
	/* 获取订单详情 */
	const getDetail = () => {
		proxy.$http.postJson('/app/waste/order/info', {
			id: id.value
		}).then(res => {
			if (res.code == 0) {
				order.value = res.data

			}
		});
	};
	// 取消订单
	const cancelOrder = () => {
		let data = {
			orderId: id.value,
		}
		proxy.$http.postJson('/app/waste/order/cancel', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '取消成功',
					icon: 'none'
				})
				setTimeout(function() {
					uni.navigateBack()
				}, 1000)
			}
		})
	};
	// 删除订单
	const delOrder = () => {
		let data = {
			id: id.value,
		}
		proxy.$http.postJson('/app/waste/order/delete', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '删除成功',
					icon: 'none'
				})
				uni.navigateBack()
			}
		})
	};
	// 一键复制
	const copyClick = (copy) => {
		uni.setClipboardData({
			data: copy,
			success: function(res) {
				uni.getClipboardData({
					success: function(res) {
						uni.showToast({
							title: "复制成功",
						});
					},
				});
			},
		});
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	.title {
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 38rpx;
		color: #333333;
		margin: 20rpx 30rpx;
	}

	.addbox {
		background: #FFFFFF;
		border-radius: 16rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.addbox_cot {
			flex: 1;

			.addbox_cot_title {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 32rpx;
				color: #1A1A1A;
			}

			.addbox_cot_tit {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #999999;
			}
		}
	}

	.infobox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.infobox_title {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 32rpx;
			color: #1A1A1A;
		}

		.infobox_Ite {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #333333;
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;
		}

		.infobox_Item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;
		}

		.infobox_Item_title {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 28rpx;
			color: #1A1A1A;
		}

		.infobox_Item_tit {
			font-family: DIN;
			font-weight: bold;
			font-size: 28rpx;
			color: #FF6027;
		}

		.infobox_remktit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #666666;
			margin-top: 10rpx;
		}

		.infoboxItem {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 30rpx;

			.infoboxItem_title {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
			}

			.infoboxItem_tit {
				image {
					width: 30rpx;
					height: 30rpx;
					margin-left: 5rpx;
				}

				display: flex;
				align-items: center;

				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #313131;
			}
		}

	}

	.taber {
		height: 98rpx;
		background: #FFFFFF;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 30rpx;

		.taber_tit {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #2FB57A;
		}

		.taber_btn {
			width: 200rpx;
			height: 69rpx;
			background: #01AF74;
			border-radius: 35rpx;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>