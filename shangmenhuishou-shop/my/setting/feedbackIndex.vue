<!-- 帮助反馈页面 -->
<template>
	<view>
		<view class="bg-list">
			<view v-for="(item,index) in helpClassifyList" :key="index" :title="item.name"
				class="list-title padding-bottom">
				<view class="flex align-center justify-between" @click.stop="openList(item)">
					<view class="text-title">{{item.name}}</view>
					<view @click.stop="openList(item)">
						<image src="../static/up.png" style="width: 21rpx;height: 15rpx;" v-if="item.parentId==0">
						</image>
						<image src="../static/dowm.png" style="width: 21rpx;height: 15rpx;" v-else></image>
					</view>
				</view>
				<view v-for="(problemItem,problemIndex) in item.articleList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)" v-if="item.parentId==0">
					<view class="text-item">{{problemItem.title}}</view>
					<view class="line" v-if="problemIndex!=item.articleList.length-1"></view>
				</view>
			</view>
		</view>
		<view class="bg-box">
			<view class="bg-white-box">
				<view v-if="SelKeFu==3">
					<button type="default" open-type="contact">
						<view class="flex align-center">
							<image src="../static/icon-letter.png" class="image"></image>
							<view class="text-feedback" hover-class="hover">在线客服</view>
						</view>
					</button>
				</view>
				<view v-else class="flex align-center">
					<image src="../static/icon-letter.png" class="image"></image>
					<view class="text-feedback" hover-class="hover" @click="goChat()">联系客服</view>
				</view>
				<view class="vertical-line"></view>

				<image src="../static/icon-edit.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedback()">我要反馈</view>
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

	// 列表
	const helpClassifyList = reactive([]);
	// 客服方式 1 企业客服 2打电话 3小程序客服 4 二维码客服
	const SelKeFu = ref('')

	// 获取列表
	const getlist = () => {
		proxy.$http.get("/app/help/category/list", {
			clientType: 1
		}).then(res => {
			if (res.code == 0) {
				res.data.forEach(d => {
					d.parentId = 1
				})
				helpClassifyList.splice(0, helpClassifyList.length, ...res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 标题内容折叠
	const openList = (item) => {
		var oldhelpClassifyList = item
		if (oldhelpClassifyList.parentId == 1) {
			item.parentId = 0
		} else {
			item.parentId = 1
		}
	};
	// 跳转详情
	const onClick = (item) => {
		uni.navigateTo({
			url: '/my/setting/helpDetail?title=' + item.title + '&helpWordId=' + item.id,
		})
	};

	// 联系客服
	const goChat = () => {
		let SelKeFu = uni.getStorageSync('SelKeFu');
		if (SelKeFu == '1') {
			// #ifdef MP-WEIXIN
			try {
				wx.openCustomerServiceChat({
					extInfo: {
						url: uni.getStorageSync('SelKeFuLink')
					},
					corpId: uni.getStorageSync('SelKeFuAppId'),
					success(res) {},
					fail(res) {
						console.error(res)
					}
				})
			} catch (error) {
				console.error("catchcatch" + error)
				uni.showToast({
					title: '请更新至微信最新版本'
				});
			}
			// #endif
			// #ifndef MP-WEIXIN
			let url = uni.getStorageSync('SelKeFuLink');

			if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
				uni.navigateTo({
					url
				});
			} else {
				//#ifndef H5
				uni.navigateTo({
					url: '/pages/index/webView?url=' + url
				});
				//#endif
				//#ifdef H5
				window.location.href = url;
				//#endif
			}
			// #endif
		} else if (SelKeFu == '2') {
			let token = uni.getStorageSync('token');
			if (token) {
				uni.makePhoneCall({
					phoneNumber: uni.getStorageSync('kefuPhone')
				});
			} else {
				login();
			}
		} else if (SelKeFu == '4') {
			let token = uni.getStorageSync('token');
			if (token) {
				uni.navigateTo({
					url: '/my/setting/customer'
				});
			} else {
				login();
			}
		}
	};

	// 跳转登录
	const goLoginInfo = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 跳转意见反馈
	const toFeedback = () => {
		uni.navigateTo({
			url: '/my/setting/feedback'
		});
	};

	onLoad(option => {
		SelKeFu.value = uni.getStorageSync('SelKeFu');
		getlist()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>


<style>
	page {
		background: #FFFFFF;
	}

	button::after {
		border: none;
		background-color: none;
	}

	button {
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		line-height: 1.35;
		overflow: hidden;
		color: #FFFFFF;
		/* background-color: #fff; */
		background-color: rgba(255, 255, 255, 0) !important;
		font-size: 28rpx;
		width: 100%;
		height: 100%;
	}

	.bg-box {
		/* background-color: #ffffff; */
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.bg-list {
		margin-bottom: 100rpx;
		background-color: #ffffff;
		padding: 30rpx
	}

	.bg-white-box {
		background-color: #ffffff;
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;

		font-size: 32rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #999999;
	}



	.text-title {

		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {

		font-size: 28rpx;
		padding: 24rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
	}

	.list-question {

		font-size: 28rpx;
	}

	.hover {
		background-color: #f5f5f5;
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
		/* color: #000000; */
	}

	.text-top {
		margin: 30rpx;

		font-size: 34rpx;
	}
</style>