<template>
	<view class="content">
		<image class="bgimg" src="../static/workbg.png" mode="widthFix"></image>

		<view class="lx">
			<view class="lx-box">
				<view class="lx-box-con">
					<view class="lx-box-con-item">
						<view class="lx-box-con-item-t">意向代理位置</view>
						<view class="padding-top-xs">
							<up-input placeholder="请输入代理城市" border="none" v-model="city" :readonly="true"
								@click="bindMap()"></up-input>
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
						<view class="lx-box-con-item-t">请上传身份证正反面</view>

						<view class="lx-box-con-item_Image">
							<view class="lx-box-con-item_ImageImg" v-for="(image,index) in image" :key="index">
								<image :src="image" mode="aspectFill"></image>
								<view class="lx-box-con-item_ImageImg_close" @click="removeImg(index)">
									<u-icon name="close-circle-fill" color="#2979ff" size="50rpx"></u-icon>
								</view>
							</view>

							<view class="lx-box-con-item_ImageIcon" @click="addImage" v-if="image.length<=5">
								<image src="../static/photo.png" mode="widthFix"></image>
								<view>上传图片</view>
							</view>

						</view>
						
					</view>

					<!-- <view class="margin-top-xl">拒绝理由：{{return_word}}</view> -->
				</view>


				<view class="btn" @click="submit" v-if="state==0">
					<view class="btn-box">确定</view>
				</view>
			</view>
		</view>


	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	export default {
		data() {
			return {
				name: '',
				phone: '',
				age: '',
				image: [],


				province: '',
				city: '',
				area: '',
				address_info: '',
				longitude: '',
				latitude: '',
				state: 0,
				return_word: ''

			};
		},
		onLoad() {
			this.getInfo()
		},
		onShow() {

		},
		methods: {
			getInfo() {
				this.$Request.getT('/api/index/user/is_write_message').then(res => {
					if (res.code === 1 && res.data) {
						this.state = res.data.is_write
						if (res.data.write_message) {
							let datas = res.data.write_message
							console.log(datas)
							this.name = datas.name
							this.phone = datas.phone
							this.age = datas.age
							this.image = datas.image.split(',')
							this.province = datas.province
							this.city = datas.city
							this.area = datas.area
							this.address_info = datas.address_info
							this.longitude = datas.longitude
							this.latitude = datas.latitude
							this.return_word = datas.return_word
						}

					} else {
						this.state = 0
					}
				});
			},
			removeImg(index) {
				this.image.splice(index, 1)
			},
			bindMap() {
				const that = this;
				uni.chooseLocation({
					success: function(res) {
						console.log(res)
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						if (res.name && res.address) {
							that.latitude = res.latitude
							that.longitude = res.longitude

							that.shengcheng(that.longitude, that.latitude);
							that.address_info = res.address
						} else {
							that.$queue.showToast('请选择正确地址信息！');
						}
					}
				});
			},
			shengcheng(longitude, latitude) {
				this.$Request.getT('/api/index/index/get_all_city?lat=' + latitude + '&lng=' + longitude).then(res => {
					if (res.code === 1) {
						// this.cityaddress = res.data.province + res.data.city + res.data.district
						// console.log(this.address)
						this.province = res.data.province
						this.city = res.data.city
						this.area = res.data.district
					}
				});

			},
			Changekefu() {
				let that = this
				// #ifdef MP-WEIXIN
				wx.openCustomerServiceChat({
					extInfo: {
						url: that.$queue.getData('kefu')
					},
					corpId: that.$queue.getData('kefuAppId'),
					success(res) {}
				})
				// #endif
				// #ifdef H5
				window.location.href = that.$queue.getData('kefu');
				// #endif
				// #ifdef APP
				let kefu = that.$queue.getData('kefu')
				console.log(kefu)
				plus.runtime.openURL(kefu, function(res) {});
				// #endif
			},
			submit() {
				if (!this.city) {
					uni.showToast({
						title: '请选择意向代理位置',
						icon: 'none'
					})
					return
				}
				if (!this.name) {
					uni.showToast({
						title: '请输入联系人姓名',
						icon: 'none'
					})
					return
				}
				if (!this.phone) {
					uni.showToast({
						title: '请输入联系人手机号码',
						icon: 'none'
					})
					return
				}
				if (!this.age) {
					uni.showToast({
						title: '请输入年龄',
						icon: 'none'
					})
					return
				}
				if (!this.image) {
					uni.showToast({
						title: '请上传身份证正反面',
						icon: 'none'
					})
					return
				}
				let data = {
					name: this.name,
					phone: this.phone,
					age: this.age,
					province: this.province,
					city: this.city,
					area: this.area,
					address_info: this.address_info,
					longitude: this.longitude,
					latitude: this.latitude,
					image: this.image.toString()
				}

				this.$Request.postJson("/api/index/user/apply_artificer", data).then(res => {
					if (res.code == 1) {
						uni.showToast({
							title: '提交成功！',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 图片上传
			addImage() {
				let that = this
				uni.chooseImage({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								url: this.config("APIHOST1") + '/api/index/ajax/upload', //真实的接口地址
								// url: 'https://jiazhengv5.xianmaxiong.com/sqx_fast/alioss/upload',
								filePath: res.tempFilePaths[i],
								name: 'image',
								success: (uploadFileRes) => {
									that.image.push(JSON.parse(uploadFileRes.data).data)
									uni.hideLoading();
								}
							});
						}
					}
				})
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
		}
	}
</script>

<style lang="less">
	page {
		background: #fac698;
	}

	.content {
		width: 100%;
		height: 100vh;
		padding-bottom: 30px;
	}

	.bgimg {
		width: 100%;
		height: 100%;
		position: relative;
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
			padding-bottom: 120rpx;
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