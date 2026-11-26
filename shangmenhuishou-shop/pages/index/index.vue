<template>
	<view>
		<up-sticky bgColor="#FFFFFF">
			<view class="">
				<up-tabs :list="listTab" @click="clickTab" lineColor="#01AF74" :activeStyle="activeStyle"
					:inactiveStyle="inactiveStyle" :scrollable="false" :current="current"></up-tabs>
			</view>
		</up-sticky>

		<view class="" v-if="current==0">
			<view class="takebox" v-for="(item,index) in list" :key="index" @click="goTakeDet(item)">
				<view class="takebox_title">
					{{item.orderItems[0].categoryName}}/{{item.orderItems[0].categoryChildrenName}}
				</view>
				<view class="takeboxIte">
					<text>上门地址：</text>
					{{item.address}}
				</view>
				<view class="takeboxIte">
					<text>预约时间：</text>
					{{item.appointmentTime}}
				</view>
				<view v-for="(ite,ind) in item.orderItems" :key="ind">
					<view class="takeboxIte">
						<text>回收物品：</text>
						{{ite.categoryName}}/{{ite.categoryChildrenName}}
					</view>
					<view class="takeboxIte">
						<text>物品信息：</text>
						{{ite.unit}}
					</view>
				</view>
				<view class="btn" @click.stop="bindJd(item)">接单</view>
			</view>
		</view>
		<view class="" v-if="current!=0">
			<view class="boxIte" v-for="(item,index) in list" :key='index' @click="goOrderDet(item.id)">
				<view class="boxIte_Head">
					<view class="boxIte_Head_tit" v-if="item.status==0">待接单</view>
					<view class="boxIte_Head_tit" v-if="item.status==1">待服务</view>
					<view class="boxIte_Head_tit" v-if="item.status==2">进行中</view>
					<view class="boxIte_Head_tit" v-if="item.status==3">已完成</view>
					<view class="boxIte_Head_tit" v-if="item.status==4">已取消</view>

					<view class="boxIte_Head_data">{{item.createTime}}</view>
				</view>
				<view class="margin-tb-sm" style="width: 100%;height: 1rpx;background: #EEEEEE;"></view>
				<view class="boxIte_cot" v-if="item.orderItems.length!=0">
					<view class="boxIte_cotTitle">
						{{item.orderItems[0].categoryName}}/{{item.orderItems[0].categoryChildrenName}}
					</view>
					<view class="boxIte_cotTit">上门时间:{{item.appointmentTime}}</view>
				</view>
				<view class="boxIte_bot">
					<view v-if="item.status ==1" @click.stop="cancelOrder(item)" class="btn">取消订单
					</view>
					<view @click.stop="goOrderDet(item.id)" class="btn">查看详情</view>
					<view v-if="item.status == 1" @click.stop="StarOrder(item)" class="btn">开始服务</view>
					<view v-if="item.status == 2" @click.stop="CompletedOrder(item)" class="btn">完成服务</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0"></empty>
		<!-- 加载数据 -->
		<up-loadmore :status="status" v-if="list.length>0" />
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
	import empty from '@/components/empty/empty.vue'
	const {
		proxy
	} = getCurrentInstance();
	const activeStyle = ref({
		color: '#01AF74',
		fontWeight: 'bold',
		transform: 'scale(1.05)'
	});
	const inactiveStyle = ref({
		color: '#999999',
		transform: 'scale(1)'
	});
	// 创建响应式数据  
	const listTab = reactive([{
		name: '抢单池',
		id: ''
	}, {
		name: '待服务',
		id: 1
	}, {
		name: '进行中',
		id: 2
	}, {
		name: '已完成',
		id: 3
	}]);
	const current = ref(0);
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const city = ref('');
	const latitude = ref('');
	const longitude = ref('');

	const isDeposit = ref(false); //是否缴纳保证金
	const isAuthentication = ref(0); //状态 0=未认证 1=待审核 2=通过 3=拒绝

	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框



	onLoad(option => {
		if (uni.getStorageSync('latitude') && uni.getStorageSync('longitude')) {
			latitude.value = uni.getStorageSync('latitude')
			longitude.value = uni.getStorageSync('longitude')
			selectCity(latitude.value, longitude.value);
		} else {
			uni.getLocation({
				type: 'gcj02', //wgs84  gcj02
				success: function(res) {
					console.log(res, '地理位置')
					latitude.value = res.latitude
					longitude.value = res.longitude
					uni.setStorageSync('latitude', res.latitude)
					uni.setStorageSync('longitude', res.longitude)
					selectCity(latitude.value, longitude.value);
				},
				fail: function() {
					console.log('获取地址失败')
				}
			})
		}
	});
	onShow(() => {
		uni.$once('update', function(data) {
			console.log('监听到事件来自 update ，携带参数 msg 为：' + data);
			current.value = Number(data)
			page.value = 1
			getlist()
		})


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




		if (uni.getStorageSync('currentIndex')) {
			current.value = Number(uni.getStorageSync('currentIndex'))
			uni.removeStorageSync('currentIndex')
			page.value = 1
			getlist()
		}
		if (uni.getStorageSync('token')) {
			getUserRz()
			getBzj()

			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
		}

	});

	// 获取用户是否实名认证
	const getUserRz = () => {
		proxy.$http.postJson("/app/userAuthentication/owner").then(res => {
			if (res.code == 0 && res.data) {
				isAuthentication.value = res.data.status //状态 0=未认证 1=待审核 2=通过 3=拒绝
			} else {
				isAuthentication.value = 0
			}

		});
	};
	/* 跳转接单池详情 */
	const goTakeDet = (e) => {
		if (uni.getStorageSync('token')) {
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
				url: '/my/order/takeDetail?id=' + e.id
			})
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}


	};
	// 获取是否缴纳保证金
	const getBzj = () => {
		proxy.$http.getT("/app/base/user/info").then(res => {
			if (res.code === 0 && res.data) {
				let baozhengjin = uni.getStorageSync('baozhengjin')
				let mayMoney = res.data.deposit ? res.data.deposit : 0;

				if (mayMoney < baozhengjin) {
					isDeposit.value = false
				} else {
					isDeposit.value = true
				}
			}
		});
	};
	// 接单
	const bindJd = (item) => {
		if (uni.getStorageSync('token')) {
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
			// 校验是否实名认证   isAuthentication  状态 0=未认证 1=待审核 2=通过 3=拒绝
			if (isAuthentication.value == 2) {
				if (isDeposit.value) {
					proxy.$http.postJson('/app/waste/order/take', {
						orderId: item.id,
						userId: uni.getStorageSync('userId')
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '接单成功',
								icon: 'none'
							})
							setTimeout(() => {
								current.value = 1
								page.value = 1
								getlist()
							}, 800)
						}
					})
				} else {
					uni.showModal({
						title: '提示',
						content: '还未缴纳保证金，请先去缴纳保证金',
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/my/wallet/baozhengjin'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			} else if (isAuthentication.value == 0) {
				uni.showModal({
					title: '提示',
					content: '您还未实名认证请先去认证',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/my/renzheng/index'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			} else if (isAuthentication.value == 1) {
				uni.showToast({
					title: '实名认证审核中',
					icon: 'none'
				})
			} else if (isAuthentication.value == 3) {
				uni.showModal({
					title: '提示',
					content: '实名认证被拒绝，请重新提交认证',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.navigateTo({
								url: '/my/renzheng/index'
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			}

		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};


	// 完成服务
	const CompletedOrder = (e) => {
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
		let data = {
			orderId: e.id,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.postJson('/app/waste/order/complete', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '完成服务',
					icon: 'none'
				})
				current.value = 3
				page.value = 1
				getlist()
			}
		})
	};
	// 开始服务
	const StarOrder = (e) => {
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
		let data = {
			orderId: e.id,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.postJson('/app/waste/order/start', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '开始服务',
					icon: 'none'
				})
				current.value = 2
				page.value = 1
				getlist()
			}
		})
	};
	// 取消订单
	const cancelOrder = (e) => {
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
		let data = {
			orderId: e.id,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.postJson('/app/waste/order/wasteCancel', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '取消成功',
					icon: 'none'
				})
				page.value = 1
				getlist()
			}
		})
	};
	/* 跳转订单详情*/
	const goOrderDet = (id) => {
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
			url: '/my/order/detail?id=' + id
		})
	};
	/* 点击顶部状态栏切换 */
	const clickTab = (e) => {
		console.log(e)
		if (uni.getStorageSync('token')) {
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
			current.value = e.index
			page.value = 1
			getlist()
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}


	};
	// 经纬度换省市区
	const selectCity = (lat, lng) => {
		proxy.$http.get('/app/map/reverseGeocode?lat=' + lat + '&lng=' + lng).then(res => {
			if (res.code === 0) {
				city.value = res.data.city
				if (uni.getStorageSync('token')) {
					getlist()
				}
			}
		});

	};
	/* 获取订单列表 */
	const getlist = () => {
		status.value = 'loadmore';
		if (current.value == 0) {
			let data = {
				page: page.value,
				limit: limit.value,
				city: city.value,

			}
			proxy.$http.get('/app/waste/order/pool', data).then(res => {
				if (res.code == 0) {
					pages.value = res.data.pages
					if (page.value < pages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}
					if (page.value === 1) {
						list.splice(0, list.length, ...res.data.records);
					} else {
						list.push(...res.data.records);
					}
					status.value = 'nomore';
				}
				status.value = 'nomore';
				uni.stopPullDownRefresh();
				uni.hideLoading()
			});
		} else {
			let state = listTab[current.value].id
			let data = {
				page: page.value,
				limit: limit.value,
				status: state,
				wasteUserId: uni.getStorageSync('userId')
			}
			proxy.$http.postJson('/app/waste/order/page', data).then(res => {
				if (res.code == 0) {
					pages.value = res.data.pages
					if (page.value < pages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}
					if (page.value === 1) {
						list.splice(0, list.length, ...res.data.records);
					} else {
						list.push(...res.data.records);
					}
					status.value = 'nomore';
				}
				status.value = 'nomore';
				uni.stopPullDownRefresh();
				uni.hideLoading()
			});
		}
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
		if (uni.getStorageSync('token')) {
			page.value = 1;
			getlist();
		}else{
			uni.stopPullDownRefresh()
		}

	});
	onReachBottom(() => {
		if (uni.getStorageSync('token')) {
			if (page.value < pages.value) {
				page.value += 1
				status.value = 'loading';
				getlist();
			}
		}else{
			uni.stopPullDownRefresh()
		}
	});
</script>

<style lang="scss">
	page {
		background: #F2F6FC;
	}

	.boxIte {
		background: #FFFFFF;
		margin: 20rpx 30rpx;
		border-radius: 24rpx;

		.boxIte_Head {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 20rpx 30rpx 0;

			.boxIte_Head_tit {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #01AF74;
			}

			.boxIte_Head_data {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.boxIte_cot {
			padding: 5rpx 30rpx;

			.boxIte_cotTitle {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #1A1A1A;
			}

			.boxIte_cotTit {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #999999;
				margin-top: 20rpx;
			}
		}

		.boxIte_bot {
			padding: 30rpx;
			display: flex;
			align-items: center;
			justify-content: flex-end;

			.btn {
				width: 170rpx;
				height: 65rpx;
				background: #01AF74;
				border-radius: 33rpx;
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 24rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-left: 20rpx;
			}
		}
	}

	.takebox {
		background: #FFFFFF;
		border-radius: 10rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.takebox_title {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 32rpx;
			color: #333333;
		}

		.takeboxIte {
			display: flex;
			align-items: center;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 24rpx;
			color: #333333;
			margin-top: 20rpx;

			text {
				color: #999999;
			}
		}

		.btn {
			height: 78rpx;
			background: #01AF74;
			border-radius: 4rpx;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 20rpx;
		}
	}
</style>