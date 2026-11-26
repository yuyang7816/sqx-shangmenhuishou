<template>
	<view class="content">
		<image class="bgimg" src="../static/workbg.png" mode="widthFix"></image>

		<view class="lx">
			<view class="lx-box">
				<view class="lx-box-con">
					<view class="lx-box-con-item">
						<view class="lx-box-con-item-t">意向代理位置</view>
						<view class="padding-top-xs">
							<up-input placeholder="请选择代理城市" border="none" v-model="city" :readonly="true"
								@click="openMap()"></up-input>
						</view>
					</view>
					<view class="lx-box-con-item">
						<view class="lx-box-con-item-t">姓名</view>
						<view class="padding-top-xs">
							<up-input placeholder="请输入姓名" border="none" v-model="name"></up-input>
						</view>
					</view>
					<view class="lx-box-con-item">
						<view class="lx-box-con-item-t">联系电话</view>
						<view class="padding-top-xs">
							<up-input placeholder="请输入联系电话" border="none" v-model="phone"></up-input>
						</view>
					</view>
					<view class="lx-box-con-item">
						<view class="lx-box-con-item-t">年龄</view>
						<view class="padding-top-xs">
							<up-input placeholder="请输入年龄" border="none" v-model="age"></up-input>
						</view>
					</view>

					<view class="lx-box-con-item" style="border-bottom: none;">
						<view class="lx-box-con-item-t">请上传身份证正面</view>

						<view class="lx-box-con-item_Image">
							<view class="lx-box-con-item_ImageImg" v-if="idCardFront">
								<image :src="idCardFront" mode="aspectFill"></image>
							</view>

							<view class="lx-box-con-item_ImageIcon" @click="addImage(1)" v-else>
								<image src="../static/photo.png" mode="widthFix"></image>
								<view>上传图片</view>
							</view>
						</view>
					</view>

					<view class="lx-box-con-item" style="border-bottom: none;">
						<view class="lx-box-con-item-t">请上传身份证反面</view>

						<view class="lx-box-con-item_Image">
							<view class="lx-box-con-item_ImageImg" v-if="idCardBack">
								<image :src="idCardBack" mode="aspectFill"></image>
							</view>

							<view class="lx-box-con-item_ImageIcon" @click="addImage(2)" v-else>
								<image src="../static/photo.png" mode="widthFix"></image>
								<view>上传图片</view>
							</view>

						</view>

					</view>

					<view class="margin-top" v-if="reason">拒绝理由：{{reason}}</view>
				</view>

				<view class="btn" @click="submit" v-if="!status">
					<view class="btn-box">确定</view>
				</view>
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

	const city = ref(''); //市
	const name = ref('');
	const phone = ref('');
	const age = ref('');
	const idCardFront = ref(''); //身份证正面
	const idCardBack = ref(''); //身份证反面	
	const id = ref('');
	const status = ref(false)
	const reason = ref('');



	onLoad(option => {
		getInfo()
	});
	onShow(() => {

	});

	const getInfo = () => {
		proxy.$http.get('/app/recruitment/myInfo').then(res => {
			if (res.code === 0) {
				city.value = res.data.city
				name.value = res.data.name
				phone.value = res.data.phone
				age.value = res.data.age
				idCardFront.value = res.data.idCardFront
				idCardBack.value = res.data.idCardBack
				id.value = res.data.id
				reason.value = res.data.reason
				if (res.data.status == 1 || res.data.status == 0) {
					status.value = true
				} else {
					status.value = false
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
				selectCity(res.latitude, res.longitude);

			}
		});
	};
	// 经纬度换省市区
	const selectCity = (lat, lng) => {
		proxy.$http.get('/app/map/reverseGeocode?lat=' + lat + '&lng=' + lng).then(res => {
			if (res.code === 0) {
				city.value = res.data.city
			}
		});

	};
	/* 提交地址 */
	const submit = () => {
		if (!city.value) {
			uni.showToast({
				title: '请选择代理城市',
				icon: 'none'
			})
			return
		}
		if (!name.value) {
			uni.showToast({
				title: '请输入姓名',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请输入联系电话',
				icon: 'none'
			})
			return
		}
		if (!age.value) {
			uni.showToast({
				title: '请输入年龄',
				icon: 'none'
			})
			return
		}

		if (!idCardFront.value) {
			uni.showToast({
				title: '请上传身份证正面',
				icon: 'none'
			})
			return
		}
		if (!idCardBack.value) {
			uni.showToast({
				title: '请上传身份证反面',
				icon: 'none'
			})
			return
		}

		uni.showLoading({
			title: '保存中...'
		})
		let userId = uni.getStorageSync('userId');
		let data = {
			city: city.value,
			name: name.value,
			phone: phone.value,
			age: age.value,
			idCardFront: idCardFront.value,
			idCardBack: idCardBack.value,
			id: id.value,
			userId: userId
		}
		if (id.value) {
			proxy.$http.postJson('/app/address/update', data).then(res => {
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
		} else {
			proxy.$http.postJson('/app/recruitment/save', data).then(res => {
				if (res.code == 0) {
					uni.hideLoading()
					uni.showToast({
						title: '提交成功!',
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
	/* 上传图片 */
	const addImage = (index) => {
		uni.chooseImage({
			count: 1, //默认9
			sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
			sourceType: ['album'], //从相册选择
			success: function(res) {
				uni.showLoading({
					title: '上传中...'
				});
				let token = uni.getStorageSync('token');
				let userId = uni.getStorageSync('userId');
				uni.uploadFile({
					url: 'https://smhs.xianmxkj.com/sqx_fast/file/upload', //真实的接口地址
					// url: config.APIHOST + '/alioss/upload',
					filePath: res.tempFilePaths[0],
					header: {
						token: token
					},
					name: 'file',
					success: uploadFileRes => {
						if (index == 1) {
							idCardFront.value = JSON.parse(uploadFileRes.data).data.path
						} else {
							idCardBack.value = JSON.parse(uploadFileRes.data).data.path
						}
						uni.hideLoading();
					}
				});
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
		background: #fac698;
	}

	.content {
		width: 100%;
		height: 100vh;

	}

	.bgimg {
		width: 100%;
		height: 100%;
		position: relative;
		padding-bottom: 30px;
	}


	.lx {
		width: 100%;
		// height: 321rpx;
		display: flex;
		justify-content: center;
		position: absolute;
		top: 430rpx;

		.lx-box {
			width: 92%;
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			display: flex;
			justify-content: center;
			padding-bottom: 90rpx;
			border: 6rpx solid #000;

			.lx-box-con {
				width: 100%;
				margin: 0rpx 30rpx;
				height: 100%;
			}


			.lx-box-con-item {
				width: 100%;
				border-bottom: 1rpx solid #E5E5E5;
				padding: 40rpx 0 10rpx;

				.lx-box-con-item-t {
					font-family: PingFang SC;
					font-weight: 800;
					font-size: 28rpx;
					color: #333333;
					margin-bottom: 10rpx;
				}

				.input-placeholder {
					font-family: PingFang SC;
					font-weight: 400;
					font-size: 24rpx;
					color: #333333 !important;
				}

				.lx-box-con-item_Image {
					display: flex;
					flex-wrap: wrap;
					align-items: center;
					justify-content: space-between;

					.lx-box-con-item_ImageIcon {
						width: 150rpx;
						height: 150rpx;
						background: #F5F5F5;
						font-family: PingFang SC;
						font-weight: 500;
						font-size: 20rpx;
						color: #333333;
						display: flex;
						align-items: center;
						justify-content: center;
						flex-direction: column;

						image {
							width: 54rpx;
							height: 47rpx;
							margin-bottom: 10rpx;
						}
					}

					.lx-box-con-item_ImageImg {
						width: 150rpx;
						height: 150rpx;
						position: relative;

						image {
							width: 100%;
							height: 100%;
						}

						.lx-box-con-item_ImageImg_close {
							z-index: 9;
							position: absolute;
							top: -15rpx;
							right: -15rpx;
						}
					}
				}
			}
		}
	}

	.btn {
		width: 100%;
		height: 88rpx;
		display: flex;
		justify-content: center;
		position: absolute;
		bottom: -200rpx;

		.btn-box {
			width: 90%;
			height: 100%;
			display: flex;
			justify-content: center;
			background: #FFFFFF;
			align-items: center;
			border-radius: 20rpx;
		}
	}
</style>