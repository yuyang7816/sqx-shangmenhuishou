<template>
	<view>
		<up-sticky bgColor="#FFFFFF" offsetTop="0">
			<view class="">
				<up-tabs :list="listTab" @click="clickTab" lineColor="#01AF74" :activeStyle="activeStyle"
					:inactiveStyle="inactiveStyle" :scrollable="true" :current="current"></up-tabs>
			</view>
		</up-sticky>


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
				<view v-if="item.status ==0" @click.stop="cancelOrder(item)" class="btn">取消订单
				</view>
				<view @click.stop="goOrderDet(item.id)" class="btn">查看详情</view>
				<view v-if="item.status == 4" @click.stop="delOrder(item)" class="btn">删除订单</view>
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
		color: '#000000',
		transform: 'scale(1)'
	});
	// 创建响应式数据  
	const listTab = reactive([{
		name: '全部',
		id: ''
	}, {
		name: '待接单',
		id: 0
	}, {
		name: '待服务',
		id: 1
	}, {
		name: '进行中',
		id: 2
	}, {
		name: '已完成',
		id: 3
	}, {
		name: '已取消',
		id: 4
	}]);
	const current = ref(0);
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const arr = reactive([]); //小程序订阅模板
	const showModalMsg = ref(true); //小程序订阅弹框

	onLoad(option => {

	});
	onShow(() => {
		// 用户端_师傅接单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_take_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 用户端_师傅取消订单小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_cancel_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		// 用户端_订单完成小程序消息模板ID
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_complete_order_template'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					arr.push(res.data.value)
				}
			}
		});
		if (uni.getStorageSync('token')) {
			// #ifdef MP-WEIXIN
			if (showModalMsg.value) {
				openMsg()
			}
			// #endif
			page.value = 1
			getlist()
		}
	});
	// 删除订单
	const delOrder = (e) => {
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
			id: e.id,
		}
		proxy.$http.postJson('/app/waste/order/delete', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '删除成功',
					icon: 'none'
				})
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
		}
		proxy.$http.postJson('/app/waste/order/cancel', data).then(res => {
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
		console.log(e)
		current.value = e.index
		page.value = 1
		getlist()
	};
	/* 获取订单列表 */
	const getlist = () => {
		let state = listTab[current.value].id
		let data = {
			page: page.value,
			limit: limit.value,
			status: state,
			userId: uni.getStorageSync('userId')
		}
		status.value = 'loadmore';
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
		} else {
			uni.stopPullDownRefresh();
		}

	});
	onReachBottom(() => {
		if (uni.getStorageSync('token')) {
			if (page.value < pages.value) {
				page.value += 1
				status.value = 'loading';
				getlist();
			}
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
</style>