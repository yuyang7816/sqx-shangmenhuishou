<script setup>
	import {
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	import {
		onLaunch,
		onShow,
		onHide
	} from '@dcloudio/uni-app';

	onLaunch(() => {
		let userId = uni.getStorageSync('userId');
		let setin = setInterval(d => { //定时器，定时去调取聊天未读消息
			let token = uni.getStorageSync('token');
			if (token) {
				proxy.$http.get('/app/messages/data').then(res => {
					if (res.code === 0) {
						// 消息类型: 1=系统公告, 2=用户消息 3=系统通知
						let messageCount = res.data[1].unreadCount+res.data[2].unreadCount
						uni.setStorageSync('messageCount', messageCount)
						if(messageCount!=0){
							uni.setTabBarBadge({
								index: 2,
								text: messageCount + ""
							})
						}else{
							uni.removeTabBarBadge({
								index: 2
							})
						}
					}
				});
			} else {
				uni.removeTabBarBadge({
					index: 2
				})
				uni.removeStorageSync('messageCount');
				clearInterval(setin)
			}
		}, 6000);
		// #ifdef MP-WEIXIN
		//是否开启小程序自动登录	957
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_enable_mini_login'
		}).then(rep => {
			// proxy.$http.get('/app/common/type/957').then(rep => {
			if (rep.code == 0 && rep.data) {
				if (rep.data.value == '是') {
					//没有登录的时候开始自动登录
					if (!uni.getStorageSync('token')) {
						uni.login({
							provider: 'weixin',
							success: function(loginRes) {
								console.log(loginRes, 'code')
		
								let data = {
									code: loginRes.code,
									client:'user'
								}
								proxy.$http.get('/app/base/auth/wxLogin', data).then(res => {
									if (res.code == 0 && res.data) {
										uni.setStorageSync('openId', res.data.openid)
										uni.setStorageSync('unionId', res.data.unionid)
										uni.setStorageSync('flag', res.data.bindPhone)
										uni.setStorageSync('sessionkey', res.data.sessionKey)
										console.log(res.data.bindPhone, 'flagflagflag')
										if (res.data.bindPhone) {
											uni.setStorageSync('token', res.data.loginVO.token)
											uni.setStorageSync('userName', res.data.userEntity.userName)
											uni.setStorageSync('avatar', res.data.userEntity.avatar)
											uni.setStorageSync('phone', res.data.userEntity.phone)
											uni.setStorageSync('invitationCode', res.data.userEntity.invitationCode)
											uni.setStorageSync('userId', res.data.userEntity.userId)
											uni.setStorageSync('openId', res.data.userEntity.openId)
										}
										
		
									}
								})
		
							}
						});
					}
				}
			}
		});
		// #endif
		
		//#ifdef APP-PLUS
		// APP检测更新 具体打包流程可以参考：https://ask.dcloud.net.cn/article/35667
		plus.screen.lockOrientation('portrait-primary'); //竖屏正方向锁定
		//获取是否热更新过
		const updated = uni.getStorageSync('updated'); // 尝试读取storage
		
		if (updated.completed === true) {
			// 如果上次刚更新过
			// 删除安装包及安装记录
			console.log('安装记录被删除，更新成功');
			uni.removeSavedFile({
				filePath: updated.packgePath,
				success: res => {
					uni.removeStorageSync('updated');
				}
			});
		} else if (updated.completed === false) {
			uni.removeStorageSync('updated');
			plus.runtime.install(updated.packgePath, {
				force: true
			});
			uni.setStorage({
				key: 'updated',
				data: {
					completed: true,
					packgePath: updated.packgePath
				},
				success: res => {
					console.log('成功安装上次的更新，应用需要重启才能继续完成');
				}
			});
			uni.showModal({
				title: '温馨提示',
				content: '应用将重启以完成更新',
				showCancel: false,
				complete: () => {
					plus.runtime.restart();
				}
			});
		} else {
			//获取当前系统版本信息
			plus.runtime.getProperty(plus.runtime.appid, widgetInfo => {
				//请求后台接口 解析数据 对比版本
				proxy.$http.getT('/app/app/selectNewApp').then(res => {
					console.log("res__" + JSON.stringify(res))
					res = res.data[0];
					if (res.wgtUrl && widgetInfo.version < res.version) {
						let downloadLink = '';
						let androidLink = res.androidWgtUrl;
						let iosLink = res.iosWgtUrl;
						let ready = false;
						//校验是是不是热更新
						if (res.wgtUrl.match(RegExp(/.wgt/))) {
							// 判断系统类型
							if (plus.os.name.toLowerCase() === 'android') {
								console.log('安卓系统');
								if (androidLink && androidLink !== '#') {
									// 我这里默认#也是没有地址，请根据业务自行修改
									console.log('发现下载地址');
									// 安卓：创建下载任务
									if (androidLink.match(RegExp(/.wgt/))) {
										console.log('确认wgt热更新包');
										downloadLink = androidLink;
										ready = true;
									} else {
										console.log('安卓推荐.wgt强制更新，.apk的强制更新请您自行修改程序');
									}
								} else {
									console.log('下载地址是空的，无法继续');
								}
							} else {
								console.log('苹果系统');
								if (iosLink && iosLink !== '#') {
									// 我这里默认#也是没有地址，请根据业务自行修改
									console.log('发现下载地址');
									// 苹果(A)：进行热更新（如果iosLink是wgt更新包的下载地址）判断文件名中是否含有.wgt
									if (iosLink.match(RegExp(/.wgt/))) {
										console.log('确认wgt热更新包');
										downloadLink = iosLink;
										ready = true;
									} else {
										console.log('苹果只支持.wgt强制更新');
									}
								} else {
									console.log('下载地址是空的，无法继续');
								}
							}
							if (ready) {
								console.log('任务开始');
								let downloadTask = uni.downloadFile({
									url: downloadLink,
									success: res => {
										if (res.statusCode === 200) {
											// 保存下载的安装包
											console.log('保存安装包');
											uni.saveFile({
												tempFilePath: res.tempFilePath,
												success: res => {
													const packgePath = res
														.savedFilePath;
													// 保存更新记录到stroage，下次启动app时安装更新
													uni.setStorage({
														key: 'updated',
														data: {
															completed: false,
															packgePath: packgePath
														},
														success: () => {
															console
																.log(
																	'成功保存记录'
																);
														}
													});
													// 任务完成，关闭下载任务
													console.log(
														'任务完成，关闭下载任务，下一次启动应用时将安装更新'
													);
													downloadTask.abort();
													downloadTask = null;
												}
											});
										}
									}
								});
							} else {
								console.log('下载地址未准备，无法开启下载任务');
							}
						} else {
							//不是热更新是在线更新 校验是否强制升级
							if (res.method == 'true') {
								uni.showModal({
									showCancel: false,
									confirmText: '立即更新',
									title: '发现新版本',
									content: res.des,
									success: res => {
										if (res.confirm) {
											that.$queue.showLoading('下载中...');
											if (uni.getSystemInfoSync().platform ==
												'android') {
												uni.downloadFile({
													url: androidLink,
													success: downloadResult => {
														if (downloadResult
															.statusCode ===
															200) {
															plus.runtime
																.install(
																	downloadResult
																	.tempFilePath, {
																		force: false
																	},
																	d => {
																		console
																			.log(
																				'install success...'
																			);
																		plus.runtime
																			.restart();
																	},
																	e => {
																		console
																			.error(
																				'install fail...'
																			);
																	}
																);
														}
													}
												});
											}
											if (uni.getSystemInfoSync().platform ==
												'ios') {
												plus.runtime.openURL(iosLink, function(
													res) {});
											}
										} else if (res.cancel) {
											console.log('取消');
										}
									}
								});
							} else {
								uni.showModal({
									title: '发现新版本',
									confirmText: '立即更新',
									cancelText: '下次更新',
									content: res.des,
									success: res => {
										if (res.confirm) {
											that.$queue.showLoading('下载中...');
											if (uni.getSystemInfoSync().platform ==
												'android') {
												uni.downloadFile({
													url: androidLink,
													success: downloadResult => {
														if (downloadResult
															.statusCode ===
															200) {
															plus.runtime
																.install(
																	downloadResult
																	.tempFilePath, {
																		force: false
																	},
																	d => {
																		console
																			.log(
																				'install success...'
																			);
																		plus.runtime
																			.restart();
																	},
																	e => {
																		console
																			.error(
																				'install fail...'
																			);
																	}
																);
														}
													}
												});
											}
											if (uni.getSystemInfoSync().platform ==
												'ios') {
												plus.runtime.openURL(iosLink, function(
													res) {});
											}
										} else if (res.cancel) {
											console.log('取消');
										}
									}
								});
							}
						}
					}
				});
			});
		}
		
		//#endif
		
		
		// 客服类型   1 企业客服 2电话联系 3小程序客服 4 二维码客服
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_customer_type'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					uni.setStorageSync('SelKeFu', res.data.value)
				}
			}
		});
		
		
		//企业微信客服链接
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_customer_link'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					uni.setStorageSync('SelKeFuLink', res.data.value)
				}
			}
		});
		
		//企业微信客服appid
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_customer_appid'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					uni.setStorageSync('SelKeFuAppId', res.data.value)
				}
			}
		});
		
		//客服电话
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_customer_phone'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					uni.setStorageSync('kefuPhone', res.data.value)
				}
			}
		});
		// 用户端小程序上架状态 1=上架 2=下架
		proxy.$http.postJson('/app/config/info', {
			key: 'sys_user_ma_enable'
		}).then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value && res.data.value==2) {
					uni.setStorageSync('XCXIsSelect', '否')
				}else{
					uni.setStorageSync('XCXIsSelect', '是')
				}
			}
		});
		// // 用户端小程序上架状态 1=上架 2=下架
		// proxy.$http.postJson('/app/config/info', {
		// 	key: 'sys_waste_ma_enable'
		// }).then(res => {
		// 	if (res.code == 0) {
		// 		if (res.data && res.data.value && res.data.value==2) {
		// 			uni.setStorageSync('XCXIsSelect', '否')
		// 		}else{
		// 			uni.setStorageSync('XCXIsSelect', '是')
		// 		}
		// 	}
		// });
	
		
		
	});
	onShow(() => {
		console.log('onShow')
		
		
		// #ifdef APP-PLUS
		let clientid = plus.push.getClientInfo().clientid;
		let sysPhone = 1;
		if (plus.os.name.toLowerCase() === 'android') {
			sysPhone = 1;
		} else {
			sysPhone = 2;
		}
		let userId =uni.getStorageSync('userId')
		if (userId) {
			console.log(clientid,'==============')
			proxy.$http.getT('/app/base/user/updateCid?cid=' + clientid + '&userId=' + userId +
				'&sysPhone=' + sysPhone).then(
				res => {
					console.log(res,'========')
				});
		}
		//#endif
	});
	onHide(() => {
		console.log('onHide')
	});
</script>

<style lang="scss">
	/*每个页面公共css */
	//uview-plus公共样式
	@import "@/uni_modules/uview-plus/index.scss";
	//自定义公共样式
	@import "@/static/css/main.css";

	page {
		background-color: #F5F7FF;
	}
</style>