<template>
	<view class="container" v-if="XCXIsSelect != '否'">
		<view class="inputItem flex align-center">
			<text>
				收款人姓名
			</text>
			<up-input placeholder="请输入支付宝收款人姓名" border="none" type="text" v-model="zhiFuBaoName"></up-input>
		</view>
		<view class="inputItem flex align-center">
			<text>支付宝账号</text>
			<up-input placeholder="请输入要绑定的支付宝手机号" border="none" type="text" v-model="zhiFuBao"></up-input>
		</view>
		<view class="btn flex align-center justify-center" @click="toLogin()">
			绑定账户
		</view>
		<view style="padding: 32rpx 64rpx;font-size: 24rpx;color: #999999;">提示：请正确填写收款人的支付宝账户和真实的收款人姓名，否则将无法正常收款</view>
		<!-- </view> -->
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const XCXIsSelect = ref('否');
	const zhiFuBao = ref('');
	const zhiFuBaoName = ref('');

	const bingAccountList = reactive([]); //账户信息
	//账户
	const bingAccountInfo = reactive({});

	onLoad(option => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect');
		if (XCXIsSelect.value == '否') {
			uni.setNavigationBarTitle({
				title: '隐私协议'
			})
		}
		gettxInfo(); ////获取提现账户


	});
	const gettxInfo = () => {
		proxy.$http.postJson('/app/withdrawalAccount/page', {}).then(res => {
			if (res.code == 0) {
				if (res.data.records.length > 0) {
					bingAccountList.splice(0, bingAccountList.length, ...res.data.records)
					getInfo()
				}
			}
		})
	};
	const getInfo = () => {
		//判断一下有没有当前类型账户的信息
		let indexs = bingAccountList.findIndex(item => item.type === 1)
		if (indexs != -1) {
			Object.keys(bingAccountInfo).forEach(key => delete bingAccountInfo[key])
			Object.assign(bingAccountInfo, bingAccountList[indexs])
		} else {
			Object.keys(bingAccountInfo).forEach(key => delete bingAccountInfo[key])

		}
		console.log(bingAccountInfo)
		zhiFuBao.value = bingAccountInfo.accountNumber;
		zhiFuBaoName.value = bingAccountInfo.accountName;

	};
	const toLogin = () => {
		if (!zhiFuBaoName.value) {
			uni.showToast({
				title: '请输入收款人姓名',
				icon: 'none'
			})
			return
		}
		if (!zhiFuBao.value) {
			uni.showToast({
				title: '请输入支付宝账号',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '提交中...'
		})
		let data = {
			accountNumber: zhiFuBao.value,
			accountName: zhiFuBaoName.value,
			type: 1
		}
		proxy.$http.postJson('/app/withdrawalAccount/save', data).then(res => {
			if (res.code === 0) {
				uni.hideLoading();
				uni.showToast({
					title: '已提交',
					icon: 'none',
					complete() {
						setTimeout(function() {
							uni.navigateBack();
						}, 1000)
					}
				})
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
</script>

<style lang='scss'>
	page {
		background-color: #FFFFFF;
	}

	.container {
		padding-top: 32rpx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #FFFFFF;
	}

	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 70rpx;
		background: #2FB57A;
		color: #fff;
		font-size: 32rpx;

		&:after {
			border-radius: 60px;
		}
	}

	.inputItem {
		width: calc(686rpx - 40rpx);
		border: 1px solid #d6d7d9;
		margin: 40rpx auto;
		padding: 20rpx 20rpx;
		border-radius: 40rpx;

		text {
			margin-right: 10rpx;
		}
	}

	.btn {
		width: 686rpx;
		height: 88rpx;
		background:#2FB57A;
		color: #FFFFFF;
		border-radius: 44rpx;
		margin: 0 auto;
	}
</style>