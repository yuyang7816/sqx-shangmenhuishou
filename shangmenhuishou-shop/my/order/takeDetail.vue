<template>
	<view style="padding-bottom:100px;">
		<view class="title">待抢单</view>

		<view class="addbox">
			<view class="addbox_cot">
				<view class="addbox_cot_title">
					{{order.addressName}}
					<text>{{maskPhoneNumber(order.addressPhone)}}</text>
				</view>
				<view class="addbox_cot_tit">{{order.province}}/{{order.city}}/{{order.district}}/{{order.address}}
				</view>

			</view>
			<!-- <view class="" @click="openMap()">
				<u-icon name="arrow-right" color="#999999"></u-icon>
			</view> -->
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
			<!-- <view class="infoboxItem">
				<view class="infoboxItem_title">支付方式</view>
				<view class="infoboxItem_tit">线下支付</view>
			</view> -->
		</view>


		<view class="taber">

			<view class="taber_btn" @click="bindJd">立即接单</view>


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
	const isDeposit = ref(false); //是否缴纳保证金
	const isAuthentication = ref(0); //状态 0=未认证 1=待审核 2=通过 3=拒绝

	onLoad(option => {
		if (option.id) {
			id.value = option.id
			getDetail()

			getUserRz()
			getBzj()
		}
	});
	onShow(() => {

	});
	const openMap = () => {
		uni.openLocation({
			latitude: latitude,
			longitude: longitude,
			success: function() {
				console.log('success');
			}
		});
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
	// 获取用户是否实名认证
	const getUserRz = () => {
		proxy.$http.postJson("/app/userAuthentication/owner").then(res => {
			if (res.code == 0 && res.data) {
				isAuthentication.value = res.data.status //状态 0=未认证 1=待审核 2=通过 3=拒绝
			} else {
				isAuthentication.value = 0
			}

		});
	};
	// 获取是否缴纳保证金
	const getBzj = () => {
		proxy.$http.getT("/app/base/user/info").then(res => {
			if (res.code === 0 && res.data) {
				let baozhengjin = uni.getStorageSync('baozhengjin')
				let mayMoney = res.data.deposit ? res.data.deposit : 0;

				if (mayMoney<baozhengjin  ) {
					isDeposit.value = false
				} else {
					isDeposit.value = true
				}
			}
		});
	};
	// 接单
	const bindJd = (item) => {
		if (uni.getStorageSync('token')) {
			// 校验是否实名认证   isAuthentication  状态 0=未认证 1=待审核 2=通过 3=拒绝
			if (isAuthentication.value == 2) {
				if (isDeposit.value) {
					proxy.$http.postJson('/app/waste/order/take', {
						orderId: order.value.id,
						userId: uni.getStorageSync('userId')
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '接单成功',
								icon: 'none'
							})
							setTimeout(() => {
								uni.$emit('update', 1)
								uni.navigateBack()
							}, 800)
						}
					})
				} else {
					uni.showModal({
						title: '提示',
						content: '您还未缴纳保证金，请先去缴纳保证金',
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/my/wallet/baozhengjin'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			} else if (isAuthentication.value == 0) {
				uni.showModal({
					title: '提示',
					content: '您还未实名认证请先去认证',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/my/renzheng/index'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			} else if (isAuthentication.value == 1) {
				uni.showToast({
					title: '实名认证审核中',
					icon: 'none'
				})
			} else if (isAuthentication.value == 3) {
				uni.showModal({
					title: '提示',
					content: '实名认证被拒绝，请重新提交认证',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/my/renzheng/index'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			}

		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};

	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F5F5F5;
	}

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
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #313131;
				
				image {
					width: 30rpx;
					height: 30rpx;
					margin-left: 5rpx;
				}
				
				display: flex;
				align-items: center;
			}
		}

	}

	.taber {
		height: 98rpx;
		background: #F5F5F5;
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
			flex: 1;
			height: 88rpx;
			background: #01AF74;
			border-radius: 16rpx;
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