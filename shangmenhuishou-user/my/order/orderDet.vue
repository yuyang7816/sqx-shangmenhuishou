<template>
	<view>
		<view class="addbox" @click="goAddress()">

			<view class="addbox_info" v-if="orderAdd.city">
				<view class="addbox_infoTitle">{{orderAdd.name}}
					<text>{{orderAdd.phone}}</text>
				</view>
				<view class="addbox_infoTit">
					{{orderAdd.province}}/{{orderAdd.city}}/{{orderAdd.district}}/{{orderAdd.address}}
				</view>

			</view>
			<view v-else class="addbox_info">
				<view class="addbox_infoTitle">收货地址</view>
				<view class="addbox_infoTit">请选择收货地址</view>
			</view>
			<u-icon name="arrow-right" color="#CCCCCC"></u-icon>
		</view>


		<view class="boxInfo">
			<view class="boxInfo_Item">
				<view class="boxInfo_ItemTit">回收类型</view>
				<view class="boxInfo_ItemInput">
					<view>{{order.categoryName}}</view>
				</view>
			</view>
			<view class="boxInfo_line"></view>
			<view class="boxInfo_Item">
				<view class="boxInfo_ItemTit">回收单位</view>
				<view class="boxInfo_ItemInput">
					<up-input placeholder="请输入单位(件/kg/元)" border="none" v-model="num"
						inputAlign="right"></up-input>
				</view>
			</view>
			<view class="boxInfo_line"></view>
			<view class="boxInfo_Item ">
				<view class="boxInfo_ItemTit">预约回收时间</view>
				<view class="boxInfo_ItemInput" @click="showData = true">
					<view v-if="startTime">{{startTime}}</view>
					<view class="boxInfo_ItemInput_pla" v-else>请选择回收时间</view>
					<u-icon name="arrow-right" color="#999999"></u-icon>
				</view>
			</view>
			<view class="boxInfo_line"></view>
			<view class="boxInfo_ItemTit ">备注说明</view>
			<textarea v-model="remark" class="padding-tb-xs" placeholder="简单描述下您的要求" style="height: 200rpx;"></textarea>
		</view>

		<view class="taber">
			<view class="taber_tit">
				成交价格以实际估价为准
			</view>
			<view class="taber_btn" @click="orderpay">确认预约</view>
		</view>

		<up-datetime-picker :show="showData" v-model="valueData" mode="datetime" @confirm="confirmData"
			@cancel="showData = false"></up-datetime-picker>

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
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	const {
		proxy
	} = getCurrentInstance();
	const id = ref('');
	const order = ref({});
	const orderAdd = ref({}); //地址信息
	const addressId = ref('');

	const num = ref('');
	const remark = ref('');
	const startTime = ref('');
	const showData = ref(false);
	const valueData = ref(Date.now());




	onLoad(option => {
		if (option.id) {
			uni.showLoading({
				title: '加载中...'
			})
			id.value = option.id
			getDetail()
		}
	});
	onShow(() => {
		uni.$once('AddUpdate', function(data) {
			addressId.value = data.id
		})
		if (addressId.value) {
			getByAddress()
		} else {
			getAddress()
		}
	});
	/* 选择时间 */
	const confirmData = (e) => {
		console.log(e, timeFormat(e.value, 'yyyy-mm-dd hh:MM:ss'))

		const date = new Date();

		let hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
		let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
		// this.startHour = hour

		let myData = new Date(e.value);

		let myday = myData.getDate(e.value) < 10 ? "0" + myData.getDate(e.value) : myData.getDate(e.value);

		if (day > myday) {
			uni.showToast({
				title: '回收时间必须大于当前时间',
				icon: "none"
			})
			return
		}
		startTime.value = timeFormat(e.value, 'yyyy-mm-dd hh:MM:ss')
		showData.value = false
	};
	/* 获取服务详情 */
	const getDetail = () => {
		proxy.$http.postJson('/app/wasteItem/info', {
			id: id.value
		}).then(res => {
			if (res.code == 0) {
				order.value = res.data

			}
			uni.hideLoading()
		});
	};
	// 跳转选择地址
	const goAddress = () => {
		uni.navigateTo({
			url: '/my/address/index'
		})
	};
	/* 根据地址id查询 */
	const getByAddress = () => {
		proxy.$http.postJson('/app/address/info', {
			id: addressId.value
		}).then(res => {
			if (res.code === 0) {
				orderAdd.value = res.data
			}
		});
	};
	/* 获取默认地址 */
	const getAddress = () => {
		proxy.$http.get('/app/address/getDefault').then(res => {
			if (res.code == 0 && res.data) {
				orderAdd.value = res.data
			}
		});
	};
	// 下单
	const orderpay = () => {
		if (!orderAdd.value.id) {
			uni.showToast({
				title: '请选择地址',
				icon: 'none'
			})
			return
		}
		if (!num.value) {
			uni.showToast({
				title: '请输入品类件数',
				icon: 'none'
			})
			return
		}
		if (!startTime.value) {
			uni.showToast({
				title: '请选择回收时间',
				icon: 'none'
			})
			return
		}
		let data = {
			userId: uni.getStorageSync('userId'),
			itemId: id.value,
			addressId:orderAdd.value.id,
			// addressName: orderAdd.value.name,
			// addressPhone: orderAdd.value.phone,
			// province: orderAdd.value.province,
			// city: orderAdd.value.city,
			// district: orderAdd.value.district,
			// address: orderAdd.value.address,
			note: remark.value,
			appointmentTime: startTime.value,
			unit: num.value
		}
		proxy.$http.postJson('/app/waste/order/itemOrder', data).then(res => {
			if (res.code == 0) {
				uni.switchTab({
					url: '/pages/order/index'
				})
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
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

	.addbox {
		background: #FFFFFF;
		border-radius: 16rpx;
		margin: 20rpx 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;

		.addbox_info {
			flex: 1;

			.addbox_infoTitle {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 32rpx;
				color: #1A1A1A;
			}

			.addbox_infoTit {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #999999;
				margin-top: 10rpx;

				text {
					margin-left: 30rpx;
				}
			}
		}
	}


	.boxInfo {
		margin: 20rpx 30rpx;
		border-radius: 24rpx;
		background: #FFFFFF;
		padding: 30rpx;

		.boxInfo_Item {
			display: flex;
			align-items: center;
			justify-content: space-between;
		}

		.boxInfo_ItemTit {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #333333;
		}

		.boxInfo_ItemInput {
			display: flex;
			align-items: center;
			font-size: 28rpx;
		}

		.boxInfo_ItemInput_pla {
			font-family: PingFang SC;
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
		}

		.boxInfo_line {
			width: 100%;
			height: 1rpx;
			background: #F2F2F2;
			margin: 30rpx 0;
		}

		.input-placeholder {
			font-family: PingFang SC;
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
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