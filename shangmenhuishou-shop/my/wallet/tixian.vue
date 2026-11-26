<template>
	<view class="" v-if="XCXIsSelect!='否'">
		<view class="padding">
			<!-- 我的余额 -->
			<view class="box">
				<view class="text-lg">可用余额</view>
				<view class="price">{{money}}<text>元</text></view>
			</view>
			<!-- 提现 -->
			<view class="box">
				<view class="text-lg margin-bottom">提现金额</view>
				<view class="padding-bottom-sm" style="border-bottom: 1px solid #cccccc;">
					<input placeholder="请输入提现金额" v-model="moneyNum">
				</view>
				<view class="tit">{{placeholder}}</view>
			</view>

			<!-- 提现类型 -->
			<view class="boxType">
				<view class="zhifuSlect_btn" @tap="zhifuSelectTap(1)" v-if="zhifubaoTiXian"
					:style="zhifuSelect == 1 ? 'border: 1px solid #2FB57A;' : 'border: 1px solid #ffffff;'">
					<image src="../static/zhifubao.png"></image>
					<view style="margin-left:10rpx">支付宝</view>
				</view>

				<view class="zhifuSlect_btn" @tap="zhifuSelectTap(2)" v-if="weixinTiXian"
					:style="zhifuSelect ==2 ? 'border: 1px solid #2FB57A;' : 'border: 1px solid #ffffff;'">
					<image src="../static/icon_weixin.png"></image>
					<view style="margin-left:10rpx">微信</view>
				</view>
			</view>

			<view class="btn" @click="cashMoney">立即提现</view>

			<up-popup :show="show" :round="20" mode="center" @close="show = false">
				<view class="popup">
					<view class="popup_titname">上传微信提现二维码</view>
					<view class="popup_tit">请提交微信收款二维码</view>
					<view class="popup_close" @click="show = false">
						<image src="../static/colsepop.png"></image>
					</view>
					<view class="popup_Image " @click="addImages">
						<image v-if="!wxImg" src="../static/erweima.png" mode="aspectFill"></image>
						<image v-else :src="wxImg"></image>
					</view>
					<view class="popup_bth" @click="SaveWxImg">保存</view>
				</view>
			</up-popup>
		</view>

		<view class="boxbot">
			<view @click="goNav('/my/wallet/zhifubao')" v-if="zhifubaoTiXian">提现账号</view>
			<view v-if="weixinTiXian" @click="openWxImg()">微信收款码</view>
			<!-- <view @click="goNav('/my/wallet/moneydetail')">钱包明细</view> -->
			<view @click="goNav('/my/wallet/cashList')">提现记录</view>
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
		onReachBottom,
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	import config from '@/common/config.js'
	import {
		number
	} from '../../uni_modules/uview-plus/libs/function/test';
	const money = ref(0); //余额
	const moneyNum = ref(''); //提现金额
	const zhifuSelect = ref(1); //提现类型  1支付宝 2微信
	const zhifubaoTiXian = ref(false); //是否开启支付宝提现
	const weixinTiXian = ref(false); //是否开启微信提现
	const placeholder = ref(''); //提现提示文字
	const charge = ref(''); //手续费
	const minMoney = ref(''); //最低提现
	const maxMoney = ref(''); //最高提现

	const sp = ref(''); //手续费
	const wxImg = ref(''); //微信收款码
	const mywxImg = ref('');

	const WxTXFangShi = ref(1); //微信提现类型  1自动 2手动
	const show = ref(false); //微信收款码弹框

	const bingAccountList = reactive([]); //账户信息
	//账户
	const bingAccountInfo = reactive({});
	const XCXIsSelect = ref('是')

	onLoad(option => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect')
		// 是否开启微信提现	243
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_withdraw_wx'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					if (res.data.value === '是') {
						weixinTiXian.value = true;
						zhifuSelect.value = 2;
					} else {
						weixinTiXian.value = false;
						if (!zhifubaoTiXian.value) {
							zhifuSelect.value = -1;
						}
					}
				}
			}
		});
		// 是否开启支付宝提现	245
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_withdraw_ali'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					if (res.data.value === '是') {
						zhifubaoTiXian.value = true;
						zhifuSelect.value = 1;
					} else {
						zhifubaoTiXian.value = false;
						if (!weixinTiXian.value) {
							zhifuSelect.value = -1;
						}
					}
				}
			}
		});
		// 微信提现方式 1自动 2手动 244
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_withdraw_wx_type'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					WxTXFangShi.value = res.data.value;
				}
			}
		});

	});
	onShow(() => {
		getMoney(); //获取可提现金额
		gettxInfo(); ////获取提现账户
		getCharge(); //提现手续费
		getMinMoney(); //最低提现金额
		getMaxMoney(); //最高提现金额

	});
	// 打开微信收款码弹框
	const openWxImg = () => {
		//判断一下有没有当前类型账户的信息
		let indexs = bingAccountList.findIndex(item => item.type === 2)
		if (indexs != -1) {
			Object.keys(bingAccountInfo).forEach(key => delete bingAccountInfo[key])
			Object.assign(bingAccountInfo, bingAccountList[indexs])
		} else {
			Object.keys(bingAccountInfo).forEach(key => delete bingAccountInfo[key])

		}
		console.log(bingAccountInfo)
		wxImg.value = bingAccountInfo.accountNumber
		show.value = true
	};
	// 跳转
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	};
	// 提现类型切换
	const zhifuSelectTap = (index) => {
		zhifuSelect.value = index;
	};
	const gettxInfo = () => {
		proxy.$http.postJson('/app/withdrawalAccount/page', {}).then(res => {
			if (res.code == 0) {
				if (res.data.records.length > 0) {
					bingAccountList.splice(0, bingAccountList.length, ...res.data.records)
				}
			}
		})
	};
	// 获取提现金额
	const getMoney = () => {
		proxy.$http.get("/app/userWallet/my").then(des => {
			if (des.code == 0) {
				money.value = des.data.amount
			}
		});
	};

	// 提现手续费
	const getCharge = () => {
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_withdraw_rate'
		}).then(res => {
			if (res.code == 0) {
				charge.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};

	// 最低提现额度
	const getMinMoney = () => {
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_withdraw_min_amount'
		}).then(res => {
			if (res.code == 0) {
				minMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最高提现额度
	const getMaxMoney = () => {
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_withdraw_max_amount'
		}).then(res => {
			if (res.code == 0) {
				maxMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 提现
	const cashMoney = () => {
		if (!moneyNum.value) {
			uni.showToast({
				icon: 'none',
				title: '请输入提现金额'
			})
			return
		}
		if (moneyNum.value > money.value * 1) {
			uni.showToast({
				icon: 'none',
				title: '您的余额不足'
			})
			return
		}
		if (moneyNum.value < minMoney.value || moneyNum.value > maxMoney.value * 1) {
			uni.showToast({
				icon: 'none',
				title: '请输入正确的金额'
			})
			return
		}
		//支付宝提现
		if (zhifuSelect.value == 1) {
			//判断是否绑定了账户
			//判断一下有没有当前类型账户的信息
			let indexs = bingAccountList.findIndex(item => item.type === zhifuSelect.value)
			if (indexs == -1) {
				uni.navigateTo({
					url: '/my/wallet/zhifubao'
				})
				return
			}

			sp.value = (moneyNum.value * charge.value).toFixed(2)
			uni.showModal({
				title: '提现申请提示',
				content: '本次提现' + moneyNum.value + '元，服务费' + sp.value + '元，是否确认提现？',
				success: function(res) {
					if (res.confirm) {
						proxy.$http.postJson("/app/withdrawRecord/save", {
							amount: moneyNum.value,
							accountType: 2 ////提现账户 1微信 2支付宝
						}).then(res => {
							if (res.code == 0) {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
								moneyNum.value = null
							} else {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
							}
							getMoney()
						});
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			});
		} else if (zhifuSelect.value == 2) {
			if (WxTXFangShi.value == 2) {
				//判断是否绑定了账户
				//判断一下有没有当前类型账户的信息
				let indexs = bingAccountList.findIndex(item => item.type === Number(WxTXFangShi.value))
				if (indexs == -1) {
					show.value = true;
					return
				}
				sp.value = (moneyNum.value * charge.value).toFixed(2)
				uni.showModal({
					title: "提现申请提示",
					content: '本次提现' + moneyNum.value + '元，服务费' + sp.value + '元，是否确认提现？',
					success: (e) => {
						if (e.confirm) {
							uni.showLoading({
								title: '提现中...',
								icon: 'none'
							})
							proxy.$http.postJson("/app/withdrawRecord/save", {
								amount: moneyNum.value,
								accountType: 1
							}).then(res => {
								uni.hideLoading();
								if (res.code == 0) {
									uni.showToast({
										icon: 'none',
										title: res.msg
									})
									moneyNum.value = null
								} else {
									uni.showToast({
										icon: 'none',
										title: res.msg
									})
								}
								getMoney()
							});
						}
					}
				});

			} else {
				uni.showLoading({
					title: '提现中...',
					icon: 'none'
				})
				proxy.$http.postJson("/app/withdrawRecord/save", {
					amount: moneyNum.value,
					accountType: 1
				}).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
						moneyNum.value = null
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
					getMoney()
				});
			}

		}
	};
	const SaveWxImg = () => {
		uni.showLoading({
			title: '上传中'
		});
		if (wxImg.value == '') {
			uni.hideLoading();
			uni.showToast({
				title: '请上传微信二维码！',
				icon: 'none'
			})
			return;
		}

		let data = {
			accountNumber: wxImg.value,
			accountName: '',
			type: 2
		}
		proxy.$http.postJson('/app/withdrawalAccount/save', data).then(res => {
			uni.hideLoading();
			if (res.code == 0) {
				show.value = false;
				uni.showToast({
					title: '绑定成功！',
					icon: 'none'
				})
				gettxInfo(); ////获取提现账户
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	// 图片上传
	const addImages = () => {
		uni.chooseImage({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < 1; i++) {
					uni.showLoading({
						title: '上传中'
					});
					uni.uploadFile({ // 上传接口
						url: config.APIHOST + '/file/upload',
						// url: 'https://smhssf.xianmxkj.com/sqx_fast/alioss/uploads', //真实的接口地址
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading();
							let url = JSON.parse(uploadFileRes.data)
							if (url.code == 0) {
								wxImg.value = url.data.path
							} else {
								uni.showToast({
									title: url.msg,
									icon: 'none'
								})
							}

						}
					});
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
		background: #F2F2F2;
	}

	.box {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		color: #000000;
	}

	.price {
		font-size: 40rpx;
		font-weight: bold;
		margin-top: 10rpx;

		text {
			font-size: 28rpx;
			font-weight: 500;
			margin-left: 6rpx;
		}
	}

	.boxType {
		display: flex;
		align-items: center;
		justify-content: space-between;

		.zhifuSlect_btn {
			justify-content: center;
			border-radius: 15rpx;
			padding: 20rpx;
			display: flex;
			background-color: #FFFFFF;
			align-items: center;
			color: #000000;
			flex: 1;
			margin: 0 15rpx;

			image {
				width: 50rpx;
				height: 50rpx;
				border-radius: 50rpx;
			}
		}

	}

	.tit {
		color: #999999;
		font-size: 28rpx;
		margin-top: 20rpx;
	}

	.btn {
		margin: 60rpx 0;
		height: 90rpx;
		background: #2FB57A;
		border-radius: 55rpx;
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 28rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.popup {
		position: relative;
		width: 500rpx;
		padding: 30rpx;

		.popup_titname {
			font-size: 34rpx;
			font-weight: bold;
			text-align: center;
		}

		.popup_tit {
			color: red;
			text-align: center;
			padding: 20rpx 0;

		}

		.popup_close {
			position: absolute;
			top: 20rpx;
			right: 30rpx;

			image {
				width: 42rpx;
				height: 42rpx;
			}
		}

		.popup_Image {
			width: 400rpx;
			height: 400rpx;
			margin: 10rpx auto;

			image {
				width: 400rpx;
				height: 400rpx;
				border-radius: 12rpx;
			}
		}

		.popup_bth {
			height: 80rpx;
			background: #2FB57A;
			border-radius: 26rpx;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 40rpx;
		}
	}

	.boxbot {
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 28rpx;
		margin: 0 30rpx;
	}
</style>