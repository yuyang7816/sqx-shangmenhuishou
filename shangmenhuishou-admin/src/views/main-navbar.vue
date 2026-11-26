<template>
	<nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
		<div class="site-navbar__header">
			<h1 class="site-navbar__brand">
				<a class="site-navbar__brand-lg" href="javascript:;">
					<img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
					<span>{{ entryName }}</span>
				</a>
				<a class="site-navbar__brand-mini" href="javascript:;">
					<img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="logo">
				</a>
			</h1>
		</div>
		<div class="site-navbar__body clearfix">
			<el-menu class="site-navbar__menu" mode="horizontal">
				<el-menu-item index="1" style="position: relative;">
					<template slot="title">
						<audio id="audio" autoplay="autoplay"
							src="https://pw.xianmxkj.com/file/uploadPath/2022/01/19/0753211f78d718d44ee6372e33eae9ee.mp3"
							ref='audio' controls hidden></audio>
					</template>
				</el-menu-item>
				<el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
					<icon-svg name="zhedie"></icon-svg>
				</el-menu-item>
			</el-menu>
			<div class="site-navbar__context">
				<span class="nf-page-badge">{{ pageBadge }}</span>
				<span class="nf-page-title">{{ pageTitle }}</span>
			</div>
			<el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
				<el-menu-item index="1" style="position: relative;" @click="$router.push({ name: 'vueMchat' })">
					<template slot="title">
						<span class="left-number" v-if="chatNum">{{ chatNum }}</span>
						<icon-svg name="tixing" class="el-icon-setting" style="font-size: 28px;"></icon-svg>
						<audio id="audio" autoplay="autoplay"
							src="https://pw.xianmxkj.com/file/uploadPath/2022/01/19/0753211f78d718d44ee6372e33eae9ee.mp3"
							ref='audio' controls hidden></audio>
					</template>
				</el-menu-item>
				<el-menu-item class="site-navbar__avatar" index="3">
					<el-dropdown :show-timeout="0" placement="bottom">
						<span class="el-dropdown-link user-pill">
							<img class="user-pill__avatar" src="~@/assets/img/avatar.png" :alt="userName">
							<span class="user-pill__info">
								<span class="user-pill__name">{{ username || userName || '管理员' }}</span>
								<span class="user-pill__hint">欢迎您登录</span>
							</span>
							<i class="el-icon-arrow-down user-pill__arrow"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</el-menu-item>
			</el-menu>
		</div>

		<update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
	</nav>
</template>

<script>
	import UpdatePassword from './main-navbar-update-password'
	import {
		clearLoginInfo
	} from '@/utils'
	import { entryName } from '@/utils/httpRequest'
	export default {
		data() {
			return {
				updatePassowrdVisible: false,
				username: '',
				intervalId: 0,
				storeId: 0,
				isshow: false,
				chatNum: '',
				errCount: 0,
				chatNumJl: 0,
				chatNumQy: 0,
				chatNum1: 0,
				errCount1:0,
				errCount2: 0,
				chatNum2:0,
				entryName:'',
			}
		},
		components: {
			UpdatePassword
		},
		computed: {
			navbarLayoutType: {
				get() {
					return this.$store.state.common.navbarLayoutType
				}
			},
			sidebarFold: {
				get() {
					return this.$store.state.common.sidebarFold
				},
				set(val) {
					this.$store.commit('common/updateSidebarFold', val)
				}
			},
			mainTabs: {
				get() {
					return this.$store.state.common.mainTabs
				},
				set(val) {
					this.$store.commit('common/updateMainTabs', val)
				}
			},
			userName: {
				get() {
					return this.$store.state.user.name
				}
			},
			pageTitle() {
				return (this.$route.meta && this.$route.meta.title) || '管理系统'
			},
			pageBadge() {
				const name = this.$route.name
				if (name === 'home') return '数据中心'
				return '当前页面'
			}
		},
		methods: {
			updatePasswordHandle() {
				this.updatePassowrdVisible = true
				this.$nextTick(() => {
					this.$refs.updatePassowrd.init()
				})
			},
			logoutHandle() {
				this.$confirm(`确定进行[退出]操作?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$cookie.set('token', '')
					this.$cookie.set('userId', '')
					this.$cookie.set('numberDts', '')
					this.$cookie.set('numberDrz', '')
					sessionStorage.clear()
					this.delAllCookie()
					this.$router.push({
						name: 'login'
					})

				}).catch(() => {})
			},
			delAllCookie() {
				var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
				if (keys) {
					for (var i = keys.length; i--;) {
						document.cookie =
							keys[i] + "=0;path=/;expires=" + new Date(0).toUTCString();
						document.cookie =
							keys[i] +
							"=0;path=/;domain=" +
							document.domain +
							";expires=" +
							new Date(0).toUTCString();
						document.cookie =
							keys[i] +
							"=0;path=/;expires=" +
							new Date(0).toUTCString(); 
					}
				}
			},
			userSelect() {
				let userId = this.$cookie.get('userId')
				this.$http({
					url: this.$http.adornUrl(`sys/user/info`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data.user;
					this.username = returnData.username
				})
			},
			chatSelect() {
				this.$http({
					url: this.$http.adornUrl('chats/count'),
					method: 'get',
					params: this.$http.adornParams({
						'storeId': this.storeId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.chatNum = returnData
					if (this.chatNum == 0) {
						this.isshow = false
						this.errCount = 0
					} else {
						if (this.errCount != data.data) {
							this.errCount = data.data
							this.aplayAudio()
							if (document.hidden) {
								var options = {
									body: '您有新的未读消息，请及时处理',
									silent: true
								}
								var notification = new Notification('消息通知', options);
								notification.onclick = function() {
									window.open(window.location.href, '_blank');
								}
								var defaultTitle = document.title;
								if (that.isReceive) {
									return;
								} else {
									that.isReceive = true;
								}
								that.timer = setInterval(function() {
									var title = document.title;
									if (document.hidden && that.isReceive) {
										if (/你有新消息/.test(title) == false) {
											document.title = '【你有新消息】';
										} else {
											document.title = defaultTitle;
										}
									} else {
										that.isReceive = false;
										document.title = defaultTitle;
									}
								}, 500);
							}

						} else {
							this.errCount = data.data
						}
					}
				})
			},
			notification() {
				let that = this;
				if (!("Notification" in window)) {
					console.log("浏览器不支持消息通知");
					return;
				}
				Notification.requestPermission(function(permission) {});
			},
			chatSelect2() {
				this.$http({
					url: this.$http.adornUrl('chats/count'),
					method: 'get',
					params: this.$http.adornParams({
						'storeId': this.storeId
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					if (data.data > 0) {
						this.$notify.info({
							title: '消息',
							dangerouslyUseHTMLString: true,
							message: '您有' + data.data + '条未读消息。',
							offset: 80,
							duration: 2500,
							type: "warning"
						});
					}
				})
			},
			aplayAudio() {
				const audio = document.getElementById('audio')
				audio.play()
				console.log('语音提示')
			},
			dataSelect() {
				this.$http({
					url: this.$http.adornUrl('userCertification/queryCertification'),
					method: 'post',
					params: this.$http.adornParams({
						'classify': 2,
						'page': 1,
						'limit': 10,
						'status': 0,
						'name': ''
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberDrz', data.data.totalCount)
					}

				})
			},
			dataSelectPy() {
				this.$http({
					url: this.$http.adornUrl('admin/userAuthentication/page'),
					method: 'post',
					data: this.$http.adornData({
						'contact': '',
						'page': 1,
						'limit': 10,
						'status': 1,
						'name': ''
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberDts', data.data.total)
					}
				})
			},
			dataSelectPj() {
				this.$http({
					url: this.$http.adornUrl('admin/evaluate/getUserEvaluate'),
					method: 'get',
					params: this.$http.adornParams({
						'riderUserName': '',
						'page': 1,
						'limit': 10,
						'status': 0,
						'userName': '',
						'evaluateMessage':'',
						'indentNumber':'',
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberDsh', data.data.total)
					}
				})
			},
			InformationSelect() {
				this.$http({
					url: this.$http.adornUrl(`orderTaking/queryAllOrderTaking`),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'userName': '',
						'gameId': '',
						'status': 1,
						'city': ''
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberDfw', data.data.totalCount)
					}
			
				})
			},
			dataSelectTs() {
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByType'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 10,
						'state': 3,
						'platform': '',
						'status': 0,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$cookie.set('numberTs', data.data.totalCount)
					}
			
				})
			},
			messageSelect5() {
				var _this = this
				_this.$http({
					url: _this.$http.adornUrl('message/page/10/1/10'),
					method: 'get',
					params: _this.$http.adornParams({
					})
				}).then(({
					data
				}) => {
					if (data.data.totalCount > 0) {
						
						if (_this.chatNum1 != data.data.totalCount) {
							_this.aplayAudio()
							_this.$notify.info({
								title: '消息',
								dangerouslyUseHTMLString: true,
								message: '您有' + data.data.totalCount + '条服务订单消息提醒。',
								offset: 80,
								duration: 0,
								type: "warning",
								onClick() {
									_this.defineCallBack1()
								},
								onClose() {
								}
							});
						}
						_this.chatNum1 = data.data.totalCount
						_this.errCount1 = data.data.totalCount
					}else{
						_this.chatNum1 = 0
						_this.errCount1 = 0
					}
				})
			},
			messageSelect2() {
				var _this = this
				_this.$http({
					url: _this.$http.adornUrl('message/page/11/1/10'),
					method: 'get',
					params: _this.$http.adornParams({
					})
				}).then(({
					data
				}) => {
					if (data.data.totalCount > 0) {
						
						if (_this.chatNum2 != data.data.totalCount) {
							_this.aplayAudio()
							_this.$notify.info({
								title: '消息',
								dangerouslyUseHTMLString: true,
								message: '您有' + data.data.totalCount + '条任务订单消息提醒。',
								offset: 80,
								duration: 0,
								type: "warning",
								onClick() {
									_this.defineCallBack2()
								},
								onClose() {
								}
							});
						}
						_this.chatNum2 = data.data.totalCount
						_this.errCount2 = data.data.totalCount
					}else{
						_this.chatNum2 = 0
						_this.errCount2 = 0
					}
				})
			},
			defineCallBack1() {
				this.$http({
					url: this.$http.adornUrl('message/updateMessageIsSee'),
					method: 'post',
					params: this.$http.adornParams({
						'state': 10,
					})
				}).then(({
					data
				}) => {
				
				})
				this.$router.replace({
					name: 'orderCenter',
					query: {}
				})
			},
			defineCallBack2() {
				this.$http({
					url: this.$http.adornUrl('message/updateMessageIsSee'),
					method: 'post',
					params: this.$http.adornParams({
						'state': 11,
					})
				}).then(({
					data
				}) => {
				
				})
				this.$router.replace({
					name: 'universalMission',
					query: {}
				})
			},
		},
		mounted() {
			this.entryName = entryName
			this.userSelect()
			this.notification();
			this.dataSelectPy()
		},
		created() {
			this.chatNumQy = setInterval(() => {
				this.dataSelectPy()
			}, 6000)
		},
		beforeDestroy() {
			clearInterval(this.chatNum)
			clearInterval(this.chatNum1)
			clearInterval(this.chatNum2)
			
		}
	}
</script>
<style scoped="scoped">
	.site-navbar__brand-lg {
		display: inline-flex;
		align-items: center;
		gap: 8px;
		text-decoration: none;
	}

	.left-number {
		position: absolute;
		top: 0;
		right: 20px;
		width: 18px;
		height: 18px;
		line-height: 18px;
		border-radius: 50%;
		background: #ff4949;
		color: #ffffff;
		text-align: center;
		font-size: 10px;
		font-family: PingFang SC, PingFang SC-Regular;
		font-weight: 400;
		margin-top: 5px;
	}
</style>
