<template>
	<view style="padding-bottom: 120rpx;">
		<view class="padding" style="background: #FFFFFF;color: red;" v-if="status===3&&reason">
			拒绝理由：{{reason}}
		</view>
		
		<view class="box">
			<view class="boxItem">
				<view class="boxItem_tit">姓名</view>
				<view class="boxItem_Input">
					<up-input placeholder="请输入真实姓名" border="none" v-model="form.name"></up-input>
				</view>
			</view>
			<view class="boxItem padding-top">
				<view class="boxItem_tit">证件号码</view>
				<view class="boxItem_Input">
					<up-input placeholder="请输入证件号码" border="none" v-model="form.idCard"></up-input>
				</view>
			</view>
			<view class="boxItem padding-top">
				<view class="boxItem_tit">出生日期</view>
				<view class="boxItem_Input" @click="showData = true">
					<view v-if="form.birthday">{{form.birthday}}</view>
					<view class="input-placeholder" v-else>请输入出生日期</view>
				</view>
			</view>
			<view class="boxItem padding-top">
				<view class="boxItem_tit">性别</view>
				<view class="boxItem_Input" @click="sexShow = true">
					<view v-if="sex">{{sex}}</view>
					<view class="input-placeholder" v-else>请输入性别</view>
				</view>
			</view>
			<view class="boxItem padding-top">
				<view class="boxItem_tit">联系方式</view>
				<view class="boxItem_Input" style="border-bottom: none;">
					<up-input placeholder="请输入联系方式" border="none" v-model="form.contact"></up-input>
				</view>
			</view>
		</view>

		<view class="box">
			<view class="box_tit">上传身份证件</view>

			<view class="box_Image">
				<view class="box_Image_Ite" @click="addImages(2)" v-if="form.idCardFront">
					<image :src="form.idCardFront" mode="aspectFill"></image>
				</view>
				<view class="box_ImageAdd" v-else @click="addImages(2)">
					<image src="../static/photo.png" mode="widthFix"></image>
					<text>添加图片</text>
				</view>
			</view>

			<view class="box_tit margin-top">上传身份证反面</view>

			<view class="box_Image">
				<view class="box_Image_Ite" @click="addImages(3)" v-if="form.idCardBack">
					<image :src="form.idCardBack" mode="aspectFill"></image>
				</view>
				<view class="box_ImageAdd" v-else @click="addImages(3)">
					<image src="../static/photo.png" mode="widthFix"></image>
					<text>添加图片</text>
				</view>

			</view>

		</view>


		<view class="taber" @click="submit" v-if="status!=1&&status!=2">提交</view>



		<!-- 出生日期选择 -->
		<up-datetime-picker :show="showData" v-model="valueData" mode="date" @cancel="showData = false"
			@confirm="confirmData" :minDate="30"></up-datetime-picker>
		<!-- 性别选择 -->
		<up-action-sheet :actions="sexlist" :show="sexShow" cancelText="取消" @close="sexShow = false"
			@select="selectSex"></up-action-sheet>
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
	import config from '@/common/config.js'
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	const {
		proxy
	} = getCurrentInstance();
	const form = ref({
		name: '', //姓名
		idCard: '', //证件号码
		gender: '', //性别
		contact: '', //联系方式
		birthday: '', //生日
		drivingYear: '', //驾龄
		idCardFront: '', //身份证件
		idCardBack: '', //驾驶证正面
		cashDeposit: '',
		id:''
	});
	const sex = ref('');

	const show = ref(false);
	const showData = ref(false);
	const valueData = ref(Date.now());

	const checkCertification = ref(null); //0 审核中 1已认证 2 已拒绝

	const sexShow = ref(false);
	const sexlist = ref([{
		name: '男',
		id: 1
	}, {
		name: '女',
		id: 2
	}]);
	const status = ref(-1); //状态 1=待审核 2=通过 3=拒绝
	const reason = ref('')
	onLoad(option => {

		getUserInfo();

	});
	onShow(() => {

	});


	// 性别选择
	const selectSex = (e) => {
		console.log(e, '============')
		form.value.gender = e.id;
		sex.value = e.name
		sexShow.value = false
	};
	// 生日选择
	const confirmData = (e) => {
		console.log(e, '============')
		form.value.birthday = timeFormat(e.value, 'yyyy-mm-dd');
		showData.value = false
	};
	//页面跳转
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	};
	// 获取用户认证信息
	const getUserInfo = () => {
		proxy.$http.postJson("/app/userAuthentication/owner").then(res => {
			if (res.code == 0 && res.data) {
				form.value = {
					name: res.data.name, //姓名
					idCard: res.data.idCard, //证件号码
					contact: res.data.contact, //联系方式
					idCardBack: res.data.idCardBack, //身份证反面
					idCardFront: res.data.idCardFront, //驾驶正面
					gender: res.data.gender, //驾驶正面
					birthday: res.data.birthday,
					id:res.data.id

				}
				if (res.data.gender == 1) {
					sex.value = '男'
				} else {
					sex.value = '女'
				}
				status.value = res.data.status
				reason.value = res.data.reason

			}
		});
	};
	const submit = () => {

		if (!form.value.name) {
			uni.showToast({
				title: '请输入姓名',
				icon: 'none',
				duration: 1000
			})
			return;
		}
		if (!form.value.idCard) {
			uni.showToast({
				title: '请输入证件号码',
				icon: 'none',
				duration: 1000
			})
			return;
		}

		if (!form.value.birthday) {
			uni.showToast({
				title: '请选择出生日期',
				icon: 'none',
				duration: 1000
			})
			return;
		}

		if (!form.value.gender) {
			uni.showToast({
				title: '请选择性别',
				icon: 'none',
				duration: 1000
			})
			return;
		}
		if (!form.value.contact) {
			uni.showToast({
				title: '请输入联系方式',
				icon: 'none',
				duration: 1000
			})
			return;
		}

		if (!form.value.idCardFront) {
			uni.showToast({
				title: '请上传身份证正面',
				icon: 'none',
				duration: 1000
			})
			return;
		}
		if (!form.value.idCardBack) {
			uni.showToast({
				title: '请上传身份证背面',
				icon: 'none',
				duration: 1000
			})
			return;
		}

		if (form.value.id) {
			proxy.$http.postJson("/app/userAuthentication/update", form.value).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '修改成功',
						icon: 'none'
					})

					setTimeout(() => {
						uni.navigateBack()
					}, 800)
				}
			});
		} else {
			proxy.$http.postJson("/app/userAuthentication/save", form.value).then(res => {
				if (res.code == 0) {
					uni.showToast({
						title: '提交成功',
						icon: 'none'
					})

					setTimeout(() => {
						uni.navigateBack()
					}, 800)
				}
			});
		}

	};
	//上传图片
	const addImages = (e) => {
		uni.chooseImage({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < 1; i++) {
					uni.showLoading({
						title: '上传中...'
					})
					uni.uploadFile({ // 上传接口
						// url: config.APIHOST + '/alioss/upload',
						url: 'https://smhssf.xianmxkj.com/sqx_fast/file/upload',
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							if (e == 2) { //身份证正面
								form.value.idCardFront = JSON.parse(uploadFileRes
									.data).data.path
							} else if (e == 3) { //身份证发面
								form.value.idCardBack = JSON.parse(uploadFileRes.data)
									.data.path
							}
							uni.hideLoading();
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
		background: #F7F4F2;
	}

	::v-deep .input-placeholder {
		font-family: PingFang SC;
		font-weight: 500;
		font-size: 24rpx;
		color: #333333 !important;
	}

	.box {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin: 20rpx 30rpx;

		.boxItem {

			.boxItem_tit {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 28rpx;
				color: #333333;
			}

			.boxItem_Input {
				padding: 15rpx 0 10rpx;
				border-bottom: 1px solid #E6E6E6;
			}
		}

		.box_tit {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #333333;
		}

		.box_Image {
			margin-top: 20rpx;

			.box_Image_Ite {
				width: 150rpx;
				height: 150rpx;

				image {
					width: 100%;
					height: 100%;
				}
			}

			.box_ImageAdd {
				width: 150rpx;
				height: 150rpx;
				background: #F5F5F5;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;

				image {
					width: 54rpx;
					height: 47rpx;
				}

				text {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 20rpx;
					color: #333333;
					margin-top: 5rpx;
				}
			}
		}


	}

	.taber {
		height: 88rpx;
		background: #01AF74;
		border-radius: 8rpx;
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 30rpx;
		color: #FFFFFF;
		margin: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>