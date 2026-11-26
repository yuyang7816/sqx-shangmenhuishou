<template>
	<view>
		<view class="Addbox">
			<view class="AddItem">
				<view class="AddItem_Title">收货人</view>
				<view class="AddItem_Input">
					<up-input placeholder="请填写姓名" border="none" v-model="name"></up-input>
				</view>
			</view>
			<view class="AddItem">
				<view class="AddItem_Title">手机号码</view>
				<view class="AddItem_Input">
					<up-input placeholder="请填写手机号" border="none" v-model="phone"></up-input>
				</view>
			</view>
			<view class="AddItem">
				<view class="AddItem_Title">选择地区</view>
				<view class="AddItem_Input" @click="openMap()">
					<up-input placeholder="请填写地区(省，市，区)" border="none" v-model="addName" :readonly="true"></up-input>
				</view>
			</view>
			<view class="AddItem" style="border-bottom: none;">
				<view class="AddItem_Title">详细地址</view>
				<view class="AddItem_Input">
					<up-input placeholder="例如街道，门牌号，小区，楼栋号" border="none" v-model="address"></up-input>
				</view>
			</view>
		</view>
		<view class="AddSwtch">
			<view class="">设为默认地址</view>
			<view class="">
				<up-switch v-model="isDefault" @change="change" activeColor="#01AF74" size="20"
					inactiveColor="#CCCCCC"></up-switch>
			</view>
		</view>

		<view class="btn" @click="submit">确定</view>
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

	const phone = ref(''); //手机号
	const name = ref(''); //姓名
	const addName = ref('');
	const address = ref('');
	const lat = ref('');
	const lng = ref('');
	const province = ref(''); //省
	const city = ref(''); //市
	const district = ref(''); //区
	const isDefault = ref(false); //是否默认 1=默认 2=非默认	
	const addressId = ref('');

	onLoad(option => {
		if (option.addressId) {
			addressId.value = option.addressId
			uni.setNavigationBarTitle({
				title:'编辑地址'
			})
			getDetail()
		}
	});
	onShow(() => {

	});
	const change = (e) => {
		console.log('change', e);
	};
	// 获取地址详情
	const getDetail = () => {
		proxy.$http.postJson('/app/address/info', {
			id: addressId.value
		}).then(res => {
			if (res.code === 0) {
				name.value = res.data.name
				phone.value = res.data.phone
				addName.value = res.data.province + res.data.city + res.data.district
				address.value = res.data.address
				lat.value = res.data.lat
				lng.value = res.data.lng
				province.value = res.data.province
				city.value = res.data.city
				district.value = res.data.district
				if (Number(res.data.isDefault) == 1) {
					isDefault.value = true
				} else {
					isDefault.value = false
				}

			}
		});

	};

	// 打开地图选择地址
	const openMap = () => {
		let that = this
		uni.chooseLocation({
			success: function(res) {
				console.log('位置名称：' + res.name);
				console.log('详细地址：' + res.address);
				console.log('纬度：' + res.latitude);
				console.log('经度：' + res.longitude);
				address.value = res.name
				lat.value = res.latitude
				lng.value = res.longitude
				selectCity(lat.value, lng.value);

			}
		});
	};
	// 经纬度换省市区
	const selectCity = (lat, lng) => {
		proxy.$http.get('/app/map/reverseGeocode?lat=' + lat + '&lng=' + lng).then(res => {
			if (res.code === 0) {
				province.value = res.data.province
				city.value = res.data.city
				district.value = res.data.district
				addName.value = res.data.province + res.data.city + res.data.district
			}
		});

	};
	/* 提交地址 */
	const submit = () => {
		if (!name.value) {
			uni.showToast({
				title: '请填写姓名',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请填写手机号',
				icon: 'none'
			})
			return
		}
		if (!addName.value) {
			uni.showToast({
				title: '请填写地区',
				icon: 'none'
			})
			return
		}
		if (!address.value) {
			uni.showToast({
				title: '请填写详细地址',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '保存中...'
		})
		let userId = uni.getStorageSync('userId');
		let data = {
			name: name.value,
			phone: phone.value,
			address: address.value,
			lat: lat.value,
			lng: lng.value,
			province: province.value,
			city: city.value,
			district: district.value,
			userId: userId,
			id: addressId.value,
			isDefault: isDefault.value ? 1 : 2,
		}
		if(addressId.value){
			proxy.$http.postJson('/app/address/updateDefault', data).then(res => {
				if (res.code == 0) {
					uni.hideLoading()
					uni.showToast({
						title: '修改成功!',
						icon: 'none'
					})
					uni.navigateBack()
				} else {
					uni.hideLoading()
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
		}else{
			proxy.$http.postJson('/app/address/save', data).then(res => {
				if (res.code == 0) {
					uni.hideLoading()
					uni.showToast({
						title: '保存成功!',
						icon: 'none'
					})
					uni.navigateBack()
				} else {
					uni.hideLoading()
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			});
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

	.Addbox {
		background: #FFFFFF;
		padding-bottom: 60rpx;
	}

	.AddItem {
		border-bottom: 1px solid #f2f2f2;
		padding: 30rpx 30rpx;
		display: flex;

		.AddItem_Title {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 32rpx;
			color: #0D0D0D;
			width: 20%;
		}

		.AddItem_Input {
			flex: 1;
			margin-left: 30rpx;
		}
	}

	.AddSwtch {
		margin: 10rpx 0;
		background: #FFFFFF;
		padding: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-family: PingFang SC;
		font-weight: 800;
		font-size: 32rpx;
		color: #0D0D0D;
	}

	.input-placeholder {
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 28rpx;
		color: #999999;
	}

	.btn {
		margin: 30rpx;
		height: 88rpx;
		background: #01AF74;
		border-radius: 44rpx;
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
		position: fixed;
		bottom: 60rpx;
		left: 0;
		right: 0;
	}
</style>