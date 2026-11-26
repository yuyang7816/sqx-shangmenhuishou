<template>
	<view>
		<!-- 分享图片 -->
		<view class="backgroundImg flex align-center justify-center">
			<view class="backgroundImg-box flex">
				<image :src="backgroundImage" mode="aspectFill"></image>
			</view>
		</view>
		<!-- 个人信息 -->
		<view class="userInfo flex align-center justify-center">
			<view class="userInfo-box flex align-center justify-between">
				<view class="flex align-center">
					<image :src="avatar?avatar:'/static/logo.png'" mode="aspectFill"></image>
					<view class="userInfo-box-info">
						<view class="userInfo-box-info-name">
							{{userName?userName:'匿名用户'}}
						</view>
						<view class="userInfo-box-info-code">
							ID:{{invitationCode}}
						</view>
					</view>
				</view>
				<image style="width: 100rpx;height: 100rpx;border-radius: 0;" v-if="qrImg" :src="qrImg"
					mode="aspectFill"></image>
			</view>
		</view>
		<!-- 分享按钮 -->
		<view class="shareBtn flex align-center justify-center">
			<view class="shareBtn-box flex align-center justify-between">
				<view @click="sharurl()" class="shareBtn-box-item flex align-center justify-center shareBtn-box-items">
					文案推广
				</view>
				<view @click="getShareImg()" class="shareBtn-box-item flex align-center justify-center">
					分享海报
				</view>
			</view>
		</view>
		<!-- 二维码 -->
		<!-- #ifdef H5 -->
		<view class="qrcodeBox" v-if="shareH5Url">
			<up-qrcode :size="200" @result="resultQr" :val="shareH5Url"></up-qrcode>
		</view>

		<canvas canvas-id="myShareImg" id="myShareImg" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}"
			class="myShareImg"></canvas>
		<!-- #endif -->

		<up-popup :show="showShareImg" @close="closeShareImg" mode="center" :safeAreaInsetBottom="false">
			<view class="flex">
				<image :src="canvasImgSrc" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}" mode="">
				</image>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		reactive
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import configdata from '../../common/config.js';
	const {
		proxy
	} = getCurrentInstance();
	const backgroundImage = ref('');
	const invitationCode = ref('');
	const userName = ref('');
	const avatar = ref('');
	//推广文案
	const tuiguang1 = ref('');
	const url = ref('');
	const qrImg = ref('');
	const shareH5Url = ref('');
	//海报宽度
	const shareImgWidth = ref(uni.rpx2px(686));
	//海报高度
	const shareImgHeight = ref(0);
	//canvas生成的图片
	const canvasImgSrc = ref('');
	//是否显示海报弹窗
	const showShareImg = ref(false);
	onLoad(option => {
		getBackImageList();
		if (uni.getStorageSync('token')) {
			getUserInfo();
		}

	});
	//关闭海报弹窗
	const closeShareImg = () => {
		showShareImg.value = false
	};
	const getShareImg = async () => {
		//如果已经生成了海报，则直接显示
		if (canvasImgSrc.value) {
			showShareImg.value = true
		} else { //没有则去用canvas生成
			await shareImg();
		}
	};
	//生成海报
	const shareImg = async () => {
		uni.showLoading({
			title: '生成中...'
		})
		let _imgInfo = await getImageInfo(backgroundImage.value);
		console.log(_imgInfo)
		//获取canvas的上下文对象
		const ctx = uni.createCanvasContext('myShareImg');
		//设置画布的高度（根据图片的宽高比）
		// shareImgHeight.value = (shareImgWidth.value * _imgInfo.height) / _imgInfo.width;
		console.log(shareImgHeight.value)
		ctx.setFillStyle('#ffffff'); //canvas背景颜色
		ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value); //canvas画布大小
		//绘制背景图片
		ctx.drawImage(_imgInfo.path, 0, 0, shareImgWidth.value, shareImgHeight.value);
		// 在底部绘制个人信息与二维码
		//绘制头像
		const _avatarInfo = ref('');
		//头像宽高
		const avatarWidthOrHeight = ref(60);
		//边距
		const paddingNum = ref(20);
		//如果有头像
		if (avatar.value) {
			_avatarInfo.value = await getImageInfo(avatar.value)
		} else { //没有头像使用本地logo绘制
			_avatarInfo.value = await getImageInfo('/static/logo.png')
			console.log(_avatarInfo.value.path)
		}
		ctx.save();
		ctx.beginPath();
		//计算头像的中心坐标
		const avatarCenterX = paddingNum.value + avatarWidthOrHeight.value / 2;
		const avatarCenterY = shareImgHeight.value - paddingNum.value - avatarWidthOrHeight.value / 2;
		//绘制圆形头像
		ctx.arc(avatarCenterX, avatarCenterY, avatarWidthOrHeight.value / 2, 0, 2 * Math.PI);
		ctx.clip();
		//绘制头像
		ctx.drawImage(_avatarInfo.value.path, paddingNum.value, shareImgHeight.value - paddingNum.value -
			avatarWidthOrHeight.value, avatarWidthOrHeight.value,
			avatarWidthOrHeight.value);
		ctx.restore();
		//绘制用户昵称
		ctx.setFontSize(18);
		ctx.setFillStyle('#333333');
		ctx.fillText(userName.value, paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight.value,
			shareImgHeight.value - (
				avatarWidthOrHeight.value / 2) - paddingNum.value - paddingNum.value / 2);
		//绘制用户邀请码
		ctx.fillText('ID:' + invitationCode.value, paddingNum.value + paddingNum.value / 2 + avatarWidthOrHeight
			.value, shareImgHeight
			.value - avatarWidthOrHeight.value / 2);
		//绘制二维码
		ctx.drawImage(qrImg.value, shareImgWidth.value - paddingNum.value - avatarWidthOrHeight.value,
			shareImgHeight.value - paddingNum
			.value -
			avatarWidthOrHeight.value, avatarWidthOrHeight.value,
			avatarWidthOrHeight.value);

		ctx.draw(false, () => {
			console.log('绘制完成')
			uni.hideLoading()
			getCanvasNewImage();
		});
	};
	//把canvas画布转为图片
	const getCanvasNewImage = () => {
		uni.canvasToTempFilePath({
			canvasId: 'myShareImg',
			quality: 1,
			complete: res => {
				console.log(res.tempFilePath)
				canvasImgSrc.value = res.tempFilePath;
				showShareImg.value = true;
			}
		});
	};
	//异步获取图片信息
	const getImageInfo = async (imgSrc) => {
		return new Promise((resolve, errs) => {
			uni.getImageInfo({
				src: imgSrc,
				success: function(image) {
					resolve(image);
				},
				fail(err) {
					errs(err);
					uni.showToast({
						title: '海报生成失败'
					})
					uni.hideLoading()
				}
			});
		});
	};
	//生成二维码回掉的图片（base64）
	const resultQr = (e) => {
		qrImg.value = e;
	};
	//文案推广
	const getTuiguang1 = () => {
		//  海报邀请语
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_poster_invitation'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					tuiguang1.value = res.data.value;
				}
			}
		});
		
		// #ifdef APP
		// 通用APP下载地址 sys_download_app
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_download_app'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					url.value = res.data.value;
				}
			}
		});

		//#endif
		// #ifndef APP
		// h5服务域名配置 
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_h5_domain'
		}).then(ress => {
			if (ress.code == 0) {
				if (ress.data && ress.data.value) {
					url.value = ress.data.value;
					shareH5Url.value = url.value
				} else {
					url.value = proxy.$publicYuMing();
					shareH5Url.value = url.value
				}
			}
		});

		//#endif
	};
	//获取用户信息
	const getUserInfo = () => {
		proxy.$http.getT('/app/base/user/info').then(res => {
			if (res.code == 0) {
				invitationCode.value = res.data.invitationCode
				userName.value = res.data.userName ? res.data.userName : res.data.nickName
				avatar.value = res.data.avatar
				uni.setStorageSync('avatar', res.data.avatar)
				getTuiguang1();
			}
		})
	};
	//获取分享海报的图片
	const getBackImageList = () => {
		proxy.$http.postJson('/app/base/banner/page', {
			classify: 3, // 分类1banner图 2金刚区分类	 3海报背景图
			state: 1, //状态1正常 2隐藏
		}).then(async res => {
			if (res.code == 0 && res.data && res.data.records.length != 0) {
				backgroundImage.value = res.data.records[0].imageUrl;
				let _imgInfo = await getImageInfo(backgroundImage.value);
				let caveHeight = (shareImgWidth.value * _imgInfo.height) / _imgInfo
					.width;
				shareImgHeight.value = (shareImgWidth.value * _imgInfo.height) / _imgInfo.width;
			}
		})


	};
	const sharurl = () => {
		let relationId = invitationCode.value;
		uni.showModal({
			title: '文案推广',
			content: tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' + relationId,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#01C3C5',
			success: res => {
				if (res.confirm) {
					uni.setClipboardData({
						data: tuiguang1.value + relationId + '\n' + url.value + '/?inviterCode=' +
							relationId,
						success: function() {
							console.log('success');
							uni.showToast({
								title: '复制成功',
								icon: 'none'
							})
						}
					});
				}
			}
		});
	};
</script>

<style lang="scss">
	page {
		background-color: #ffffff;
	}

	.backgroundImg {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.backgroundImg-box {
			width: 686rpx;
			height: 100%;

			image {
				width: 100%;
				height: 800rpx;
				border-radius: 24rpx 24rpx 0 0;
			}
		}
	}

	.userInfo {
		width: 100%;
		height: auto;

		.userInfo-box {
			width: calc(682rpx - 40rpx);
			height: 100%;
			border-left: 1px solid #d6d7d9;
			border-right: 1px solid #d6d7d9;
			border-bottom: 1px solid #d6d7d9;
			border-radius: 0 0 24rpx 24rpx;
			padding: 20rpx;

			image {
				width: 100rpx;
				height: 100rpx;
				border-radius: 50%;
			}

			.userInfo-box-info {
				margin-left: 20rpx;
				color: #333333;
			}

			.userInfo-box-info-name {
				font-size: 30rpx;
				font-weight: bold;
			}

			.userInfo-box-info-code {
				font-size: 24rpx;
			}
		}
	}

	.shareBtn {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.shareBtn-box {
			width: 686rpx;
			height: 100%;
		}

		.shareBtn-box-items {
			background-color: #ffffff !important;
			color: #FD6301 !important;
			border: 1px solid #FD6301;
		}

		.shareBtn-box-item {
			width: 333rpx;
			height: 80rpx;
			background-color: #FD6301;
			color: #ffffff;
			border-radius: 14rpx;
		}
	}

	.qrcodeBox {
		position: fixed;
		left: -1000px;
	}

	.myShareImg {
		position: fixed;
		left: -1000px;
	}
</style>