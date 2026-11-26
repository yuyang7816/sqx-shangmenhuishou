<template>
	<view>
		<view class="usermain">
			<view class="usermain-item ">
				<view class="usermain-itemTit">头像</view>
				<view>
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar"
						style="width: 111rpx;height: 111rpx;border-radius: 50%;">
						<image :src="avatar?avatar:'../../static/logo.png'" mode=""
							style="width: 111rpx;height: 111rpx;border-radius: 50%;">
						</image>
					</button>
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<image @click="uploadImg()" :src="avatar?avatar:'../../static/logo.png'" mode=""
						style="width: 111rpx;height: 111rpx;border-radius: 50%;">
					</image>
					<!-- #endif -->

				</view>
			</view>
			<view class="usermain-item  ">
				<view class="usermain-itemTit">用户名</view>
				<view class="cu-form-group">
					<input v-model="userName" placeholder="请输入用户名" />
				</view>
			</view>
			<view class="usermain-item  ">
				<view class="usermain-itemTit">年龄</view>
				<view>
					<view class="cu-form-group">
						<input v-model="age" placeholder="请输入年龄" />
					</view>
				</view>
			</view>

			<view class="usermain-item  ">
				<view class="usermain-itemTit">手机</view>
				<view>
					<view class="cu-form-group">
						<input v-model="phone" placeholder="请输入联系电话" disabled/>
					</view>
				</view>
			</view>
			<view class="usermain-item  " style="border-bottom: none;">
				<view class="usermain-itemTit">性别</view>
				<view class="cu-form-group">
					<up-radio-group v-model="sex" placement="row">
						<up-radio activeColor="#2FB57A" :name="1" label="男"></up-radio>
						<up-radio activeColor="#2FB57A" :name="2" label="女"></up-radio>
					</up-radio-group>
				</view>
			</view>
		</view>
		<view class="usermain-btn" @click="messagebtn()">保存</view>
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
	import config from '@/common/config.js'

	const avatar = ref('');
	const userName = ref('');
	const age = ref('');
	const phone = ref('');
	const sex = ref(1);
	const userId = ref('');


	onLoad(option => {

	});
	onShow(() => {
		getUserInfo();
	});
	/* 获取用户信息 */
	const getUserInfo = () => {
		proxy.$http.get("/app/base/user/info").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				phone.value = res.data.phone
				age.value = res.data.age
				sex.value = res.data.sex
				avatar.value = res.data.avatar ? res.data.avatar : '../../static/logo.png'
				userId.value = res.data.userId

			}
		});
	};
	/* 修改个人信息 */
	const messagebtn = () => {
		if (!userName.value) {
			uni.showToast({
				title: "用户名不能为空",
				icon: "none"
			})
			return
		}
		if (!age.value) {
			uni.showToast({
				title: "请输入年龄",
				icon: "none"
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: "联系电话不能为空",
				icon: "none"
			})
			return
		}
		uni.showModal({
			title: '温馨提示',
			content: '确定保存信息',
			success: e => {
				if (e.confirm) {
					proxy.$http.postJson("/app/base/user/update", {
						userName: userName.value,
						avatar: avatar.value,
						phone: phone.value,
						sex: sex.value,
						age: age.value,
						userId: userId.value

					}).then(res => {
						if (res.code === 0) {
							uni.showToast({
								title: '保存成功',
								icon: "none"
							})
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
						} else {
							uni.showToast({
								title: res.msg,
								icon: "none"
							})
						}
					})
				}
			}
		});
	};
	/* 上传头像 */
	const uploadImg = () => {
		let token = uni.getStorageSync('token')

		if (!token) {
			goLoginInfo();
			return;
		}
		var url = null;
		uni.showActionSheet({
			// itemList按钮的文字接受的是数组
			itemList: ["查看头像", "从相册选择图片"],
			success(e) {
				var index = e.tapIndex
				if (index === 0) {
					// 用户点击了预览当前图片
					// 可以自己实现当前头像链接的读取
					let url = avatar.value;
					let arr = []
					arr.push(url)
					uni.previewImage({
						// 预览功能图片也必须是数组的
						urls: arr
					})
				} else if (index === 1) {
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
								url: 'https://smhssf.xianmxkj.com/sqx_fast/file/upload', //真实的接口地址
								// url: config.APIHOST + '/alioss/upload',
								filePath: res.tempFilePaths[0],
								header: {
									token: token
								},
								name: 'file',
								success: uploadFileRes => {
									url = JSON.parse(uploadFileRes.data);
									avatar.value = url.data.path
									uni.hideLoading();
								}
							});
						}
					});
				}
			}
		})
	};
	// 微信头像能力
	const onChooseAvatar = (e) => {
		uni.showLoading({
			title: '上传中...'
		});
		uni.uploadFile({
			// url: config.APIHOST1 + '/alioss/upload', //仅为示例，非真实的接口地址
			url: config.APIHOST + '/file/upload', //仅为示例，非真实的接口地址
			filePath: e.detail.avatarUrl,
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			success: uploadFileRes => {
				let url = JSON.parse(uploadFileRes.data).data;
				avatar.value = url.path
			}
		});
	};
	const goLoginInfo = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	button {
		padding: 0 !important;
	}
	
	button::after {
		border: none;
	}
	
	.input-placeholder {
		font-family: PingFang SC;
		font-weight: 400;
		font-size: 26rpx;
		color: #999999;
	}

	.usermain {
		background: #FFFFFF;
		margin: 20rpx 30rpx;
		padding: 30rpx;
		border-radius: 24rpx;

		.usermain-item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: 1px solid #f2f2f2;
			padding: 30rpx 0;

			.usermain-itemTit {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
			}

			.cu-form-group {
				text-align: right;
			}
		}
	}

	.usermain-btn {
		background: #2FB57A;
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 28rpx;
		color: #FFFFFF;
		margin: 120rpx 30rpx 30rpx;
		border-radius: 55rpx;
		text-align: center;
		padding: 20rpx;
	}
</style>