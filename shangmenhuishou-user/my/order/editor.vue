<template>
	<view style="padding-bottom: 200rpx;">
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

		<view class="InfoItem">
			<view class="InfoItem_add" v-if="orderInfo.length==0">
				<view class="InfoItem_addTitle">请添加您需要上门回收的物品吧</view>
				<view class="InfoItem_addIcon" @click="showSku = true">
					<image src="../static/tianjia.png" mode="widthFix"></image>
					<text>立即添加</text>
				</view>
			</view>
			<view v-else>
				<view class="InfoItem_info" v-for="(item,index) in orderInfo" :key="index">
					<view class="InfoItem_info_cot">
						<view class="InfoItem_info_cotTitle">{{item.categoryName}}/{{item.categoryChildrenName}}</view>
						<view class="InfoItem_info_cotTit">
							<text v-if="item.unit">{{item.unit}}</text>
							<text class="margin-left" style="color: #F1413D;">{{item.price}}元</text>
						</view>
					</view>
					<view class="InfoItem_infoIcon" @click="deteOrderInfo(index)">
						<image src="../static/icon_down.png" mode="widthFix">
						</image>
					</view>

				</view>
				<view class="InfoItem_info_tit " @click="showSku = true">继续添加</view>
			</view>
		</view>

		<view class="boxInfo">

			<view class="boxInfo_Item">
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


		<!-- 物品弹框 -->
		<up-popup :show="showSku" @close="showSku = false" :round="10">
			<view class="popup">
				<view class="popup_title">添加废品
					<text @click="showSku = false">取消</text>
				</view>

				<up-tabs :list="list" @click="clickTab" lineColor="#2FB57A" :activeStyle="activeStyle"
					:inactiveStyle="inactiveStyle" :current="current"></up-tabs>

				<view class="popup_sku">
					<view class="popup_sku_title" v-if="typeList.length!=0">选择分类</view>
					<view class="popup_skubox">
						<view class="skubox" v-for="(item,index) in typeList" :key="index"
							:class="typeIndex==index?'skubox_active':''" @click="change(index)">{{item.name}}</view>
					</view>
					<view class="popup_skuboxP" v-if="weightList.length!=0">选择重量</view>
					<view class="popup_skuboxPrice">
						<view class="pricebox" v-for="(item,index) in weightList" :key="index"
							:class="weightIndex==index?'pricebox_active':''" @click="changeWeight(index)">
							<view class="num">{{item.unit}}</view>
							<view class="price">约￥{{item.price}}</view>
						</view>
					</view>
				</view>


				<view class="popup_bot">
					<view class="popup_botTite">成交价格以实际估价为准</view>
					<view class="popup_bot_add" @click="Addpay">添加 </view>
				</view>
			</view>
		</up-popup>

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
	const {
		proxy
	} = getCurrentInstance();
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	const orderAdd = ref({}); //地址信息
	const orderInfo = reactive([]); //回收物品信息
	const showSku = ref(false); //回收信息弹框
	const list = reactive([]); //回收分类列表
	const current = ref(0);
	const categoryId = ref(''); //回收一级id
	const categoryName = ref('');
	const activeStyle = ref({
		color: '#2FB57A',
		fontWeight: 'bold',
		transform: 'scale(1.05)'
	});
	const inactiveStyle = ref({
		color: '#979997',
		transform: 'scale(1)'
	});
	const typeList = reactive([]); //回收二级分类
	const typeIndex = ref(0);
	const categoryChildrenId = ref(''); //回收二级分类id
	const categoryChildrenName = ref('');
	const weightList = reactive([]); //回收价格分类
	const weightIndex = ref(0);
	const weight = ref(''); //回收重量
	const money = ref(''); //回收价格


	const remark = ref('');
	const startTime = ref('');
	const showData = ref(false);
	const valueData = ref(Date.now());
	const addressId = ref('');



	onLoad(option => {
		getwastelist();
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
	/* 切换分类 */
	const clickTab = (e) => {
		console.log(e)
		current.value = e.index
		let children = list[current.value].children
		typeList.splice(0, typeList.length, ...children);
		categoryChildrenId.value = typeList[0].id
		categoryChildrenName.value = typeList[0].name
		getPrice()
	};
	/* 选择分类 */
	const change = (index) => {
		typeIndex.value = index
		categoryChildrenId.value = typeList[typeIndex.value].id
		categoryChildrenName.value = typeList[typeIndex.value].name
		getPrice()
	};
	// 获取废品分类
	const getwastelist = () => {
		proxy.$http.postJson('/app/waste/category/list', {}).then(res => {
			if (res.code == 0) {
				list.splice(0, list.length, ...res.data);
				if (list.length != 0 && list[0].children.length != 0) {
					let children = res.data[0].children
					typeList.splice(0, typeList.length, ...children);
					categoryChildrenId.value = typeList[0].id
					categoryChildrenName.value = typeList[0].name
					getPrice()
				}
			}
		});
	};
	/* 选择价格 */
	const changeWeight = (index) => {
		weightIndex.value = index

	};
	/* 获取价格 */
	const getPrice = () => {
		proxy.$http.postJson('/app/waste/price/list', {
			categoryId: categoryChildrenId.value
		}).then(res => {
			if (res.code == 0) {
				weightList.splice(0, weightList.length, ...res.data);

			}
		});
	};
	const Addpay = () => {
		categoryName.value = list[current.value].name
		categoryId.value = list[current.value].id

		weight.value = weightList[weightIndex.value].unit
		money.value = weightList[weightIndex.value].price
		let data = {
			categoryName: categoryName.value,
			categoryChildrenName: categoryChildrenName.value,
			unit: weight.value,
			price: money.value,
			categoryId: categoryId.value,
			categoryChildrenId: categoryChildrenId.value
		}


		orderInfo.push(data)
		current.value = 0
		typeIndex.value = 0
		weightIndex.value = 0
		categoryName.value = ''
		categoryChildrenName.value = ''
		weight.value = ''
		categoryId.value = ''
		categoryChildrenId.value = ''
		money.value = 0
		showSku.value = false
		getwastelist();
	};
	/* 删除选择回收 */
	const deteOrderInfo = (index) => {
		orderInfo.splice(index, 1)
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
		if (orderInfo.length == 0) {
			uni.showToast({
				title: '请添加需要上门回收的物品',
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
			addressName: orderAdd.value.name,
			addressPhone: orderAdd.value.phone,
			province: orderAdd.value.province,
			city: orderAdd.value.city,
			district: orderAdd.value.district,
			address: orderAdd.value.address,
			note: remark.value,
			appointmentTime: startTime.value,
			orderItems: orderInfo
		}
		proxy.$http.postJson('/app/waste/order/save', data).then(res => {
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
		background-color: #F5F5F5;
	}

	::v-deep.u-tabs__wrapper__nav__item {
		padding: 0 30rpx 0 0 !important;
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

	.InfoItem {
		background: #FFFFFF;
		border-radius: 16rpx;
		margin: 20rpx 30rpx;

		.InfoItem_add {
			text-align: center;
			padding: 50rpx;

			.InfoItem_addTitle {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
			}

			.InfoItem_addIcon {
				display: flex;
				align-items: center;
				justify-content: center;
				margin-top: 30rpx;

				image {
					width: 48rpx;
					height: 48rpx;
				}

				text {
					font-family: PingFang SC;
					font-weight: bold;
					font-size: 28rpx;
					color: #01AF74;
					margin-left: 10rpx;
				}
			}
		}

		.InfoItem_info {
			padding: 30rpx;

			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 1rpx solid #E6E6E6;

			.InfoItem_info_cot {



				.InfoItem_info_cotTitle {
					font-family: PingFang SC;
					font-weight: bold;
					font-size: 32rpx;
					color: #333333;
				}

				.InfoItem_info_cotTit {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 28rpx;
					color: #333333;
				}
			}

			.InfoItem_infoIcon {
				image {
					width: 42rpx;
					height: 42rpx;
				}
			}
		}

		.InfoItem_info_tit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #333333;
			padding: 20rpx 0;
			text-align: center;
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
		display: flex;
		align-items: center;
		justify-content: space-between;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		padding: 0 30rpx;

		.taber_tit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #2FB57A;

		}

		.taber_btn {
			width: 260rpx;
			height: 78rpx;
			background: #2FB57A;
			border-radius: 39rpx;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	/* 添加废品弹框 */
	.popup {
		padding: 30rpx;

		.popup_title {
			text-align: center;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 30rpx;
			color: #191A19;
			position: relative;

			text {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #979997;
				position: absolute;
				right: 0;
				bottom: 0;
			}
		}

		.popup_sku {
			margin-top: 30rpx;

			.popup_sku_title {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 28rpx;
				color: #000000;
			}

			.popup_skubox {
				display: flex;
				flex-wrap: wrap;

				.skubox {
					background: #F2F2F2;
					border-radius: 55rpx;
					margin-right: 20rpx;
					padding: 12rpx 40rpx;
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 28rpx;
					color: #999999;
					margin: 20rpx 30rpx 20rpx 0;
				}

				.skubox_active {
					background: #01AF74;
					color: #FFFFFF;

				}
			}

			.popup_skuboxP {
				margin: 20rpx 0;
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 28rpx;
				color: #000000;
			}

			.popup_skuboxPrice {
				display: flex;
				flex-wrap: wrap;
				justify-content: space-between;

				.pricebox {
					background: #EEEEEE;
					border-radius: 12rpx;
					width: 292rpx;
					height: 88rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					padding: 0 20rpx;
					margin-bottom: 20rpx;


				}

				.pricebox_active {
					height: 88rpx;
					background: #EBFFF6;
					border: 1px solid #2FB57A;
					border-radius: 12rpx;

				}

				.num {
					color: #2FB57A;
					font-family: PingFang SC;
					font-weight: 800;
					font-size: 28rpx;
				}

				.price {
					color: #F1413D;
					font-size: 26rpx;
					font-family: PingFang SC;
					font-weight: 800;
				}
			}



		}

		.popup_bot {
			display: flex;
			align-items: end;
			justify-content: space-between;
			margin-top: 30rpx;

			.popup_botTite {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #01AF74;
			}

			.popup_bot_add {
				width: 260rpx;
				height: 78rpx;
				background: #2FB57A;
				border-radius: 39rpx;

				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}

	}
</style>