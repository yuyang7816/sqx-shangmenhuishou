<template>
	<view style="padding-bottom: 150rpx;">
		<view class="orderImg">
			<image :src="order.cover?order.cover:'/static/logo.png'" mode="aspectFill" @click="saveImg(order.cover)">
			</image>
		</view>

		<view class="cotbox">
			<view class="flex align-center justify-between">
				<view class="cotbox_title">{{order.name}}</view>
				<view class="cotbox_tit" v-if="order.appointmentNum">已预约{{order.appointmentNum}}</view>
			</view>
			<view class="listboxCot_label">
				<view v-for="(ite,ind) in order.tags" :key="ind" :class="ind%2===0?'label':'labels'">
					{{ite}}
				</view>
			</view>
			<view class="cotbox_price" v-if="order.price">
				<text>￥</text>{{order.price}}
			</view>
		</view>

		<view class="margin-top-sm ">
			<view v-for="(item,index) in order.pic" :key="index">
				<image :src="item" style="width:100%;" mode="widthFix"></image>
			</view>
		</view>

		<view class="taber">
			<view style="width: 120rpx;text-align: center;" @click="openShare">
				<image src="../../static/image/index/share.png" style="width:37rpx;height: 40rpx;"></image>
				<view style="font-size: 22rpx;">分享</view>
			</view>
			<view class="buttons" @click="goOrderDet()">立即预约</view>
		</view>

		<up-popup :show="show" @close="show = false" :round="24">
			<view class="shareBtn ">
				<view @click="sharurl()" class=" shareBtn-box-items">
					文案推广
				</view>
				<view @click="getShareImg()" class="shareBtn-box-item">
					分享海报
				</view>
			</view>
		</up-popup>


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

	const show = ref(false);
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
		if (option.id) {
			uni.showLoading({
				title: '加载中...'
			})
			id.value = option.id
			getDetail()
		}

		// #ifndef H5
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
		// #ifdef H5
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
	});
	onShow(() => {
		invitationCode.value = uni.getStorageSync('invitationCode');
		userName.value = uni.getStorageSync('userName');
		avatar.value = uni.getStorageSync('avatar');

	});
	/* 去预约 */
	const goOrderDet = () => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/my/order/orderDet?id=' + id.value
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 分享 */
	const openShare = () => {
		if (uni.getStorageSync('token')) {
			show.value = true
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	/* 获取服务详情 */
	const getDetail = () => {
		proxy.$http.postJson('/app/wasteItem/info', {
			id: id.value
		}).then(async res => {
			if (res.code == 0) {
				if (res.data.tags) {
					res.data.tags = res.data.tags.split(',')
				}
				if (res.data.pic) {
					res.data.pic = res.data.pic.split(',')
				}
				tuiguang1.value = res.data.name
				order.value = res.data

				backgroundImage.value = res.data.cover;
				let _imgInfo = await getImageInfo(backgroundImage.value);
				let caveHeight = (shareImgWidth.value * _imgInfo.height) / _imgInfo
					.width;
				shareImgHeight.value = (shareImgWidth.value * _imgInfo.height) / _imgInfo.width;
			}
			uni.hideLoading()
		});
	};
	/* 查看图片 */
	const saveImg = (imgs) => {
		let imgArr = []
		imgArr.push(imgs);
		uni.previewImage({
			urls: imgArr,
			current: imgArr[0]
		});
	};


	const sharurl = () => {
		show.value = false
		let relationId = invitationCode.value;
		uni.showModal({
			title: '文案推广',
			content: tuiguang1.value + '\n' + url.value + '/?inviterCode=' + relationId,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#01C3C5',
			success: res => {
				if (res.confirm) {
					uni.setClipboardData({
						data: tuiguang1.value + '\n' + url.value + '/?inviterCode=' +
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

	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F2F2F2;
	}

	.qrcodeBox {
		position: fixed;
		left: -1000px;
	}

	.myShareImg {
		position: fixed;
		left: -1000px;
	}

	.orderImg {
		width: 100%;
		height: 567rpx;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.cotbox {
		background: #FFFFFF;
		padding: 20rpx 30rpx 30rpx;

		.cotbox_title {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 38rpx;
			color: #1E1F31;
		}

		.cotbox_tit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #333333;
		}

		.listboxCot_label {
			display: flex;
			align-items: center;
			margin-top: 15rpx;

			.label {
				background: #EEFFF9;
				border-radius: 4rpx;
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 20rpx;
				color: #01AF74;
				padding: 2rpx 10rpx;
				margin-right: 10rpx;
			}

			.labels {
				background: #FFEFED;
				border-radius: 4rpx;
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 20rpx;
				color: #F75244;
				padding: 2rpx 10rpx;
				margin-right: 10rpx;
			}
		}

		.cotbox_price {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 40rpx;
			color: #FD3C44;
			margin-top: 20rpx;

			text {
				font-size: 24rpx;
			}
		}

	}

	.taber {
		height: 98rpx;
		background: #FFFFFF;
		padding: 15rpx 30rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 9;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.buttons {

			width: 550rpx;
			height: 72rpx;
			background: #01AF74;
			border-radius: 36rpx;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	.shareBtn {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;


		.shareBtn-box-items {
			background-color: #FFFFFF;
			color: #01AF74;
			border: 1px solid #01AF74;
			display: flex;
			align-items: center;
			justify-content: center;
			width: 333rpx;
			height: 80rpx;
			border-radius: 14rpx;
		}

		.shareBtn-box-item {
			width: 333rpx;
			height: 80rpx;
			background-color: #01AF74;
			color: #ffffff;
			border-radius: 14rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>