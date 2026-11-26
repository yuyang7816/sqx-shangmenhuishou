<template>
	<view>
		<view class="acont_part1">
			<view class="part1_box">
				<view class="mingxi" @click="goDet()">查看明细</view>
				<view class="balance">
					<view class="balance_name">保证金余额(元)</view>
					<view class="balance_price">{{mayMoney}}</view>
				</view>

				<view class="part1_btn">
					<view class="btn_right" @click="tuiBtn()" v-if="isDeposit">退保证金</view>
					<view class="btn_right" @click="openPay()" v-if="!isDeposit">缴纳保证金</view>
				</view>
			</view>
		</view>
		<view class="acont_part2">
			<view class="acount_type">保证金规则</view>

			<view class="acount_box">
				<view class="name">保证金缴纳</view>
				<view class="tit">{{JiaoContent}}</view>
			</view>
			<view class="acount_box">
				<view class="name">保证金退款</view>
				<view class="tit">{{TuiContent}}</view>
			</view>
		</view>
		<!-- 支付方式 -->
		<up-popup :show="show" @close="show = false" mode="center" :round="12" :closeable="true">
			<view class="payType">
				<view class="payType_title">请输入支付金额</view>
				<view class="payTypebox">
					<up-input placeholder="请输入支付金额" border="none" v-model="earnestMoney" type="number"></up-input>
				</view>
				<view class="popup_payType" v-for="(item,index) in openList" :key='index'>
					<view class="popup_payType_left">
						<image :src="item.image" mode="aspectFill"></image>
						<text>{{item.text}}</text>
					</view>
					<radio-group name="openWay">
						<label class="tui-radio">
							<radio color="#01AF74" :checked="openWay == item.id ? true : false"
								@click='selectWay(item)' />
						</label>
					</radio-group>
				</view>

				<view class="payType_btn" @click="jiaoBtn">立即支付</view>
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
	const mayMoney = ref(0);
	const baozhengjin = ref(0);
	const TuiContent = ref('');
	const JiaoContent = ref('');

	const openList = reactive([]); //支付类型
	const openWay = ref('');
	const show = ref(false);
	const earnestMoney = ref('');

	const isDeposit = ref(false)


	// 跳转保证金明细
	const goDet = () => {
		uni.navigateTo({
			url: '/my/wallet/bzjlist'
		})
	};
	// 支付方式切换
	const selectWay = (item) => {
		openWay.value = item.id;
	};
	const openPay = () => {
		show.value = true
	};
	// 获取保证金
	const getBzj = () => {
		proxy.$http.getT("/app/base/user/info").then(res => {
			if (res.code === 0 && res.data) {
				mayMoney.value = res.data.deposit ? res.data.deposit : 0;
				console.log(baozhengjin.value, mayMoney.value)
				if (mayMoney.value>baozhengjin.value ) {
					isDeposit.value = true
				} else {
					isDeposit.value = false
				}
			}
		});
	};
	// 退保证金
	const tuiBtn = () => {
		if (mayMoney.value == 0) {
			uni.showToast({
				icon: 'none',
				title: '暂无保证金'
			})
			return
		}
		uni.showModal({
			title: '提示',
			content: '确认退还保证金吗？如果存在未完成订单和未处理完成的投诉无法退还，需要先处理完毕后再申请',
			success: function(res) {
				if (res.confirm) {
					proxy.$http.postJson('/app/base/user/refundDeposit', {
						userId: uni.getStorageSync('userId'),
						amount: mayMoney.value
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '申请成功'
							})
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
						setTimeout(function() {
							getBzj();
						}, 1000)
					});
				}
			},
		})

	};
	// 缴纳
	const jiaoBtn = () => {
		let userId = uni.getStorageSync('userId');
		if (!earnestMoney.value) {
			uni.showToast({
				title: '请输入支付金额',
				icon: 'none'
			})
			return;
		}
		uni.showLoading({
			title: '支付中'
		});

		if (openWay.value == 1) { //支付宝
			let tradeType = ''
			// #ifdef MP-WEIXIN
			tradeType = 'JSAPI'
			// #endif
			// #ifdef H5
			tradeType = 'WEB'
			// #endif
			// #ifdef APP
			tradeType = 'APP'
			// #endif
			let data = {
				businessType: 1,
				redirectUri: '/my/wallet/baozhengjin',
				amount: earnestMoney.value,
				clientId: 'sys_shop_domain',
				tradeType: tradeType,
				userId: uni.getStorageSync('userId'),
			}
			proxy.$http.postJson('/app/base/alipay/wapPay', data).then(ret => {
				if (ret.code == 0) {
					// #ifdef APP
					isCheckPay(ret.code, 'alipay', ret.data);
					// #endif
					// #ifdef H5
					const div = document.createElement('div')
					div.innerHTML = ret.data //此处form就是后台返回接收到的数据
					document.body.appendChild(div)
					document.forms[0].submit()
					// #endif
				} else {
					uni.hideLoading()
					uni.showToast({
						title: '支付失败',
						icon: 'none'
					})
				}
			})
		} else if (openWay.value == 2) { //微信
			let tradeType = ''
			// #ifdef MP-WEIXIN
			tradeType = 'JSAPI'
			// #endif
			// #ifdef APP
			tradeType = 'APP'
			// #endif
			// #ifdef H5
			tradeType = 'WEB'
			// #endif
			let data = {
				businessType: 1,
				redirectUri: '/my/wallet/baozhengjin',
				amount: earnestMoney.value,
				clientId: 'sys_shop_domain',
				tradeType: tradeType,
				userId: uni.getStorageSync('userId'),
				openId: uni.getStorageSync('openId')
			}

			proxy.$http.postJson('/app/base/wxpay/pay', data).then(ret => {
				if (ret.code == 0) {
					// #ifdef APP
					isCheckPay(ret.code, 'wxpay', JSON.stringify(ret.data));
					// #endif

					// #ifdef H5
					callPay(ret.data);
					// #endif

					// #ifdef MP-WEIXIN
					uni.hideLoading()
					uni.requestPayment({
						provider: 'wxpay',
						timeStamp: ret.data.timeStamp,
						nonceStr: ret.data.nonceStr,
						package: ret.data.packageValue,
						signType: ret.data.signType,
						paySign: ret.data.paySign,
						success: function(suc) {
							uni.showToast({
								title: '支付成功',
								icon: 'success'
							})
							getBzj();
						},
						fail: function(err) {
							uni.hideLoading()
							uni.showToast({
								title: '支付失败',
								icon: 'none'
							})
						}
					});

					// #endif
				} else {
					uni.hideLoading()
					uni.showToast({
						title: '支付失败',
						icon: 'none'
					})
				}
			})
		}

	};
	const callPay = (response) => {
		if (typeof WeixinJSBridge === "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response));
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response));
			}
		} else {
			onBridgeReady(response);
		}
	};
	const onBridgeReady = (response) => {
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.data.appid, //公众号名称，由商户传入
				"timeStamp": response.data.timestamp, //时间戳，自1970年以来的秒数
				"nonceStr": response.data.noncestr, //随机串
				"package": response.data.package,
				"signType": response.data.signType, //微信签名方式：
				"paySign": response.data.sign //微信签名
			},
			function(res) {
				if (res.err_msg === "get_brand_wcpay_request:ok") {
					// 使用以上方式判断前端返回,微信团队郑重提示：
					//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
					getBzj();
					uni.showLoading({
						title: '支付成功'
					});
					setTimeout(function() {
						uni.hideLoading();
					}, 1000);
				} else {
					uni.hideLoading();
				}
				WeixinJSBridge.log(response.err_msg);
			}
		);
	};
	const isCheckPay = (status, name, order) => {
		if (status == 0) {
			setPayment(name, order);
		} else {
			uni.hideLoading();
			uni.showToast({
				title: '支付信息有误',
				icon: 'none'
			});
		}
	};
	const setPayment = (name, order) => {
		uni.requestPayment({
			provider: name,
			orderInfo: order, //微信、支付宝订单数据
			success: function(res) {
				getBzj();
				uni.hideLoading();
				uni.showLoading({
					title: '支付成功'
				});
			},
			fail: function(err) {
				uni.hideLoading();
			},
			complete() {
				uni.hideLoading();
			}
		});
	};
	onLoad(option => {
		getBzj();
		baozhengjin.value = uni.getStorageSync('baozhengjin');
		if (earnestMoney.value < baozhengjin.value) {
			earnestMoney.value = baozhengjin.value - earnestMoney.value
		} else {
			earnestMoney.value = baozhengjin.value
		}

		// biz_deposit_refund_rule 保证金退款规则
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_deposit_refund_rule'
		}).then(res => {
			if (res.code == 0) {
				TuiContent.value = res.data.value
			}
		});
		// biz_deposit_pay_rule 保证金缴纳规则
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_deposit_pay_rule'
		}).then(res => {
			if (res.code == 0) {
				JiaoContent.value = res.data.value
			}
		});


		let payType = []

		// #ifdef APP-PLUS
		payType = [{
			image: '../static/zhifubao.png',
			text: '支付宝支付',
			id: 1
		}, {
			image: '../static/icon_weixin.png',
			text: '微信支付',
			id: 2
		}];
		openWay.value = 1;
		// #endif

		// #ifdef MP-WEIXIN
		payType = [{
			image: '../static/icon_weixin.png',
			text: '微信支付',
			id: 2
		}];
		openWay.value = 2;
		// #endif

		// #ifdef H5
		let target = navigator.userAgent.toLowerCase();
		let isWeixin = (/micromessenger/.test(target)) ? true : false;
		if (isWeixin) {
			payType = [{
				image: '../static/icon_weixin.png',
				text: '微信支付',
				id: 2
			}];
			openWay.value = 2;
		} else {
			payType = [{
				image: '../static/zhifubao.png',
				text: '支付宝支付',
				id: 1
			}];
			openWay.value = 1;
		}
		// #endif
		openList.splice(0, openList.length, ...payType)
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.acont_part1 {
		background: linear-gradient(to bottom, #01AF74, #FFFFFF);
		padding: 79rpx 30rpx 0;
	}

	.part1_box {
		background: #FFFDFC;
		box-shadow: 0rpx 15rpx 43rpx 0rpx rgba(232, 215, 199, 0.29);
		border-radius: 24rpx;
		padding-bottom: 55rpx;
		position: relative;

		.mingxi {
			position: absolute;
			top: 30rpx;
			right: 30rpx;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #333333;
		}

		.balance {
			text-align: center;
			padding-top: 80rpx;

			.balance_name {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #333333;
			}

			.balance_price {
				font-family: DINPro;
				font-weight: 500;
				font-size: 78rpx;
				color: #333333;
			}
		}

		.part1_btn {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 30rpx;
			margin-top: 40rpx;



			.btn_right {
				flex: 1;
				height: 88rpx;
				background: #01AF74;
				border-radius: 8rpx;
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 28rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}

	.acont_part2 {
		margin-top: 60rpx;
		padding: 0 40rpx;

		.acount_type {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 38rpx;
			color: #333333;
		}

		.acount_box {
			margin-top: 30rpx;

			.name {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 32rpx;
				color: #333333;
			}

			.tit {
				font-family: PingFang SC;
				font-weight: 400;
				font-size: 28rpx;
				color: #666666;
				line-height: 42rpx;
			}
		}
	}

	.payType {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx 30rpx 20rpx;
		width: 560rpx;

		.payType_title {
			padding-bottom: 20rpx;
			text-align: center;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 32rpx;
			color: #333333;
		}

		.payTypebox {
			background: #f2f2f2;
			padding: 18rpx 30rpx;
			border-radius: 12rpx;
		}

		.popup_payType {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 40rpx 0;

			.popup_payType_left {
				display: flex;
				align-items: center;

				image {
					width: 55rpx;
					height: 55rpx;
					border-radius: 50rpx;
				}

				text {
					margin-left: 15rpx;
					font-family: PingFang SC;
					font-weight: bold;
					font-size: 28rpx;
					color: #030303;

				}
			}
		}

		.payType_btn {
			height: 90rpx;
			background: #01AF74;
			border-radius: 55rpx;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 30rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 20rpx 0;
		}

	}
</style>