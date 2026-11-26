<template>
	<view>
		<view class="bgColor">

			<view class="userTop">
				<image :src="avatar?avatar:'/static/logo.png'" @click="goNav('/pages/my/userinfo')"></image>
				<view class="userTop_info" v-if="isLogin">
					<view class="userTop_infoName">{{userName}}</view>
					<view class="flex align-center">
						<view class="userTop_infoTxt">ID:{{invitationCode}}</view>

						<view class="userTop_infoTxt margin-left-sm"
							@click.stop="isAuthentication !=2?goNav('/my/renzheng/index'):''">
							<text v-if="isAuthentication == 0">未入驻</text>
							<text v-if="isAuthentication == 1">审核中</text>
							<text v-if="isAuthentication == 2">已认证</text>
							<text v-if="isAuthentication == 3">已拒绝</text>
						</view>
					</view>
				</view>
				<view class="userTop_logo" v-else @click="login()">登录</view>
			</view>


			<view class="numbox" v-if="XCXIsSelect!='否'">
				<view class="numbox_ite">
					<text>{{allCount?allCount:0}}</text>
					<view>总接单</view>
				</view>
				<view class="numbox_ite">
					<text>{{todayCount?todayCount:0}}</text>
					<view>今日接单</view>
				</view>
				<view class="numbox_ite">
					<text>{{monthCount?monthCount:0}}</text>
					<view>本月接单</view>
				</view>
			</view>
		</view>

		<view class="orderbox" v-if="listNum.length!=0&&XCXIsSelect!='否'">
			<view class="orderbox_head">
				<view class="orderbox_head_title">订单数据</view>
				<view class="orderbox_head_tit" @click="goNavSwit(3)">
					查看全部
					<up-icon name="arrow-right" color="#666666" size="12"></up-icon>
				</view>
			</view>
			<view class="orderboxItem">
				<view class="orderboxItem_info" @click="goNavSwit(1)">
					<view class="orderboxItem_info_num">{{listNum[1].count}}</view>
					<view class="orderboxItem_info_tit">待服务</view>
				</view>
				<view class="orderboxItem_info" @click="goNavSwit(2)">
					<view class="orderboxItem_info_num">{{listNum[2].count}}</view>
					<view class="orderboxItem_info_tit">进行中</view>
				</view>
				<view class="orderboxItem_info" @click="goNavSwit(3)">
					<view class="orderboxItem_info_num">{{listNum[3].count}}</view>
					<view class="orderboxItem_info_tit">已完成</view>
				</view>
			</view>
		</view>

		<view class="boxInfo">
			<view class="boxInfo_ite" @click.stop="goNav('/my/message/index')" v-if="XCXIsSelect!='否'">
				<image src="../../static/image/my/qianbao.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">消息中心</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
				<view class="image_tuIcon">
					<up-badge max="99" :value="messageCount"></up-badge>
				</view>
			</view>
			<view class="boxInfo_ite" @click.stop="goNav('/my/wallet/index')" v-if="XCXIsSelect!='否'">
				<image src="../../static/image/my/qianbao.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">我的钱包</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>

			<view class="boxInfo_ite" @click.stop="goNav('/my/renzheng/index')" v-if="XCXIsSelect!='否'">
				<image src="../../static/image/my/shiming.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">实名认证</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>
			<view class="boxInfo_ite" @click="goNav('/my/wallet/baozhengjin')" v-if="isBzj!='否'&&XCXIsSelect!='否'">
				<image src="../../static/image/my/baozhengjin.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">保证金</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>

			<view class="boxInfo_ite" @click="Changekefu()">
				<image src="../../static/image/my/kefu.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">联系客服</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>
			<view class="boxInfo_ite" @click.stop="goNav('/my/setting/about')">
				<image src="../../static/image/my/about.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">关于我们</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>
			<view class="boxInfo_ite" @click.stop="goNav('/my/setting/feedback')">
				<image src="../../static/image/my/yijian.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">意见反馈</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>

			<view class="boxInfo_ite" @click.stop="goUser" v-if="XCXIsSelect!='否'">
				<image src="../../static/image/my/usr.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">前往用户端</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
			</view>

			<view class="boxInfo_ite" @click="goNav('/my/setting/index')">
				<image src="../../static/image/my/set.png" mode="aspectFit"></image>
				<view class="boxInfo_iteTitle">设置中心</view>
				<up-icon name="arrow-right" color="#808080" size="12"></up-icon>
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
	const isLogin = ref(false);
	const avatar = ref('');
	const userName = ref('');
	const invitationCode = ref('');
	const userId = ref('');
	const XCXIsSelect = ref('是')
	const isBzj = ref('否')
	const allCount = ref(0); //总接单
	const monthCount = ref('0'); //月接单
	const todayCount = ref('0'); //今日接单
	const isAuthentication = ref(0); //状态 0=未认证 1=待审核 2=通过 3=拒绝

	const listNum = ref([]);

	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框

	const messageCount = ref(0);
	const setin = ref(null)

	onLoad(option => {

	});
	onShow(() => {
		// 师傅端_用户取消订单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_waste_cancel_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 师傅端_订单完成小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_waste_complete_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		isBzj.value = uni.getStorageSync('isBzj')
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
		userId.value = uni.getStorageSync('userId')
		if (userId.value) {
			isLogin.value = true
			getUserInfo()
			gettakeOrderNum()
			getorderNum()
			getUserRz()

			messageCount.value = uni.getStorageSync('messageCount')
			setin.value = setInterval(d => { //定时器，定时去调取聊天未读消息
				messageCount.value = uni.getStorageSync('messageCount')
			}, 6000);

			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		} else {
			isLogin.value = false
			userName.value = '匿名'
			avatar.value = ''
			invitationCode.value = ''
			allCount.value = 0
			monthCount.value = 0
			todayCount.value = 0
			isAuthentication.value = 0

			listNum.value = []
		}
	});

	/* 获取 回收员订单数据 */
	const getorderNum = () => {
		proxy.$http.getT("/app/waste/order/wasteOrderData").then(res => {
			if (res.code == 0) {
				listNum.value = res.data
			}
		});
	};
	/* 获取接单数据 */
	const gettakeOrderNum = () => {

		proxy.$http.get("/app/waste/order/takeOrderData").then(res => {
			if (res.code == 0) {
				allCount.value = res.data.allCount //总接单
				monthCount.value = res.data.monthCount //月接单
				todayCount.value = res.data.todayCount //今日接单
			}
		});
	};
	/* 跳转用户端 */
	const goUser = () => {
		let userAppid = uni.getStorageSync('userAppid')

		//#ifdef MP-WEIXIN 

		if (uni.getStorageSync('sendMsg')) {
			uni.requestSubscribeMessage({
				tmplIds: arr,
				success(re) {
					var datas = JSON.stringify(re);
					if (datas.indexOf("accept") != -1) {
						console.log(re)
					}
				},
				fail: (res) => {
					console.log(res)
				}
			})
		}


		wx.navigateToMiniProgram({
			appId: userAppid,
			path: '/pages/index/index',
			envVersion: 'release', // 打开正式版
			success(res) {
				// 打开成功
			},
			fail: function(err) {
				console.log(err);
			}
		})
		// #endif 
		//#ifndef MP-WEIXIN
		uni.showModal({
			title: '码兄上门回收',
			content: '请打开微信搜索【码兄上门回收】入驻',
			showCancel: false,
			cancelText: '',
			confirmText: '我知道了',
			success: res => {},
			fail: () => {},
			complete: () => {}
		});
		// #endif 
	};
	/* 获取用户信息 */
	const getUserInfo = () => {
		proxy.$http.get("/app/base/user/info").then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
				invitationCode.value = res.data.invitationCode
				avatar.value = res.data.avatar ? res.data.avatar : '../../static/logo.png'


				uni.setStorageSync('avatar', res.data.avatar)
				uni.setStorageSync('invitationCode', res.data.invitationCode)

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
			uni.setStorageSync('isAuthentication', isAuthentication.value)
		});
	};
	// 联系客服
	const Changekefu = () => {
		// #ifdef MP-WEIXIN
		if (uni.getStorageSync('sendMsg')) {
			uni.requestSubscribeMessage({
				tmplIds: arr,
				success(re) {
					var datas = JSON.stringify(re);
					if (datas.indexOf("accept") != -1) {
						console.log(re)
					}
				},
				fail: (res) => {
					console.log(res)
				}
			})
		}
		// #endif
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
	const goNav = (url) => {
		if (userId.value) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.navigateTo({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	const goNavSwit = (index) => {
		uni.setStorageSync('currentIndex', index)
		if (userId.value) {
			// #ifdef MP-WEIXIN
			if (uni.getStorageSync('sendMsg')) {
				uni.requestSubscribeMessage({
					tmplIds: arr,
					success(re) {
						var datas = JSON.stringify(re);
						if (datas.indexOf("accept") != -1) {
							console.log(re)
						}
					},
					fail: (res) => {
						console.log(res)
					}
				})
			}
			// #endif
			uni.switchTab({
				url: '/pages/index/index'
			})

		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	/* 登录 */
	const login = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	// 开启订阅消息
	const openMsg = () => {
		uni.getSetting({
			withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
			success(ret) {
				if (ret.subscriptionsSetting.itemSettings) {
					uni.setStorageSync('sendMsg', true)
					uni.openSetting({ // 打开设置页 
						success(rea) {
							console.log(rea.authSetting)
						}
					});
				} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
					uni.setStorageSync('sendMsg', false)
					uni.showModal({
						title: '提示',
						content: '为了更好的体验,请绑定消息推送',
						confirmText: '确定',
						cancelText: '取消',
						success: function(res) {
							if (res.confirm) {
								wx.requestSubscribeMessage({
									tmplIds: arr,
									success(re) {
										var datas = JSON.stringify(re);
										if (datas.indexOf("accept") != -1) {
											console.log(re)
										}
									},
									fail: (res) => {
										console.log(res)
									}
								})
								showModalMsg.value = false
							} else if (res.cancel) {
								showModalMsg.value = true
							}
						}
					})
				}
			}
		})
	};
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #F2F2F7;
	}

	.bgColor {
		width: 100%;
		background: #FFFFFF;
	}

	.userTop {
		display: flex;
		align-items: center;
		margin: 0 30rpx;

		image {
			width: 100rpx;
			height: 100rpx;
			border-radius: 55%;
		}

		.userTop_info {
			flex: 1;
			margin-left: 15rpx;

			.userTop_infoName {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 32rpx;
				color: #1D1D1D;
			}

			.userTop_infoTxt {
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 24rpx;
				color: #1D1D1D;

				text {
					color: #53b84f
				}
			}
		}

		.userTop_logo {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 34rpx;
			color: #1D1D1D;
			margin-left: 15rpx;
		}
	}

	.numbox {
		background: #01AF74;
		border-radius: 16rpx 16rpx 0rpx 0rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 30rpx 30rpx 0;
		padding: 10rpx 30rpx;


		.numbox_ite {
			text-align: center;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 22rpx;
			color: #FFFFFF;

			text {
				font-family: DINPro;
				font-weight: 500;
				font-size: 38rpx;
				color: #FFFFFF;
			}
		}
	}

	.orderbox {
		background: #FFFFFF;
		border-radius: 16rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.orderbox_head {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.orderbox_head_title {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
			}

			.orderbox_head_tit {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #666666;
				display: flex;
				align-items: center;
			}
		}

		.orderboxItem {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;

			.orderboxItem_info {
				text-align: center;

				.orderboxItem_info_num {
					font-family: DINPro;
					font-weight: 500;
					font-size: 42rpx;
					color: #333333;
				}

				.orderboxItem_info_tit {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 24rpx;
					color: #333333;
				}
			}
		}
	}

	.boxInfo {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx 30rpx 0;

		.boxInfo_ite {
			display: flex;
			align-items: center;
			padding-bottom: 40rpx;
			position: relative;

			image {
				width: 43rpx;
				height: 43rpx;
			}

			.boxInfo_iteTitle {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #1A1A1A;
				flex: 1;
				margin-left: 20rpx;
			}

			.image_tuIcon {
				position: absolute;
				top: 5rpx;
				right: 30rpx;
			}
		}
	}
</style>