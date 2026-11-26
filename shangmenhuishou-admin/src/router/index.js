/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import {isURL} from '@/utils/validate'
import {clearLoginInfo} from '@/utils'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
	{path: '/404', component: _import('common/404'), name: '404', meta: {title: '404未找到'}},
	{path: '/login', component: _import('common/login'), name: 'login', meta: {title: '登录'}}
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
	path: '/',
	component: _import('main'),
	name: 'main',
	redirect: {name: 'home'},
	meta: {title: '主入口整体布局'},
	children: [
		// 通过meta对象设置路由展示方式
		// 1. isTab: 是否通过tab展示内容, true: 是, false: 否
		// 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
		// 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
		{path: '/home', component: _import('common/home'), name: 'home', meta: {title: '首页'}},
		{path: '/userList', component: _import('user/userList'), name: 'userList', meta: {title: '用户列表', isTab: true}},
		{path: '/allocationList',component: _import('allocation/allocationList'),name: 'allocationList',meta: {title: '配置列表', isTab: true}},
		{path: '/financeList', component: _import('finance/financeList'), name: 'financeList', meta: {title: '财务中心', isTab: true}},
		{path: '/message', component: _import('message/message'), name: 'message', meta: {title: '消息中心', isTab: true}},
		{path: '/taskConfig', component: _import('taskConfig/taskConfig'), name: 'taskConfig', meta: {title: '服务配置', isTab: true}},
		{path: '/bannerList', component: _import('banner/bannerList'), name: 'bannerList', meta: {title: '首页装修', isTab: true}},
		{path: '/mission', component: _import('mission/mission'), name: 'mission', meta: {title: '服务列表', isTab: true}},
		{path: '/system', component: _import('mission/system'), name: 'system', meta: {title: '系统服务', isTab: true}},
		{path: '/missionsye', component: _import('sysmission/missionsye'), name: 'missionsye', meta: {title: '活动派送', isTab: true}},
		{path: '/materialsList', component: _import('materials/materialsList'), name: 'materialsList', meta: {title: '帮助中心', isTab: true}},
		{path: '/missionAdd', component: _import('mission/missionAdd'), name: 'missionAdd', meta: {title: '发布服务', isTab: false}},
		{path: '/missionRedact',component: _import('sysmission/missionRedact'),name: 'missionRedact',meta: {title: '修改服务', isTab: false}},
		{path: '/userDetail', component: _import('user/userDetail'), name: 'userDetail', meta: {title: '用户详情', isTab: false}},
		{path: '/userDetail1', component: _import('user/userDetail1'), name: 'userDetail1', meta: {title: '用户详情', isTab: false}},
		{path: '/missionDetails',component: _import('mission/missionDetails'),name: 'missionDetails',meta: {title: '服务详情', isTab: false}},
		{path: '/fitmentList', component: _import('fitment/fitmentList'), name: 'fitmentList', meta: {title: '首页装修', isTab: false}},
		{path: '/integral', component: _import('integral/integral'), name: 'integral', meta: {title: '积分推送', isTab: true}},
		{path: '/integralDetail',component: _import('integral/integralDetail'),name: 'integralDetail',meta: {title: '活动详情', isTab: false}},
		{path: '/campus', component: _import('campus/campus'), name: 'campus', meta: {title: '搜索列表', isTab: true}},
		{path: '/missionComplain',component: _import('mission/missionComplain'),name: 'missionComplain',meta: {title: '服务投诉', isTab: true}},
		{path: '/locality', component: _import('locality/locality'), name: 'locality', meta: {title: '项目列表', isTab: true}},
		{path: '/classification', component: _import('locality/classification'), name: 'classification', meta: {title: '废品分类', isTab: true}},
		{path: '/specifications', component: _import('locality/specifications'), name: 'specifications', meta: {title: '价格配置', isTab: true}},
		{path: '/orderCenter',component: _import('orderCenter/orderCenter'),name: 'orderCenter',meta: {title: '订单中心',isTab: true}},
		{path: '/vueMchat',component: _import('vueMchat/vueMchat'),name: 'vueMchat',meta: {title: '聊天会话',isTab: true}},
		{path: '/memberDetails',component: _import('members/memberDetails'),name: 'memberDetails',meta: {title: '会员配置',isTab: true}},
		{path: '/vipPrivilege',component: _import('user/vipPrivilege'),name: 'vipPrivilege',meta: {title: '会员特权',isTab: true}},
		{path: '/app', component: _import('app/app'), name: 'app', meta: { title: '升级配置', isTab: true } }, 
		{path: '/chatRecord',component: _import('chatRecord/chatRecord'),name: 'chatRecord',meta: {title: '聊天记录',isTab: true}},
		{path: '/universalMission',component: _import('universalMission/universalMission'),name: 'universalMission',meta: {title: '派单列表',isTab: true}},
		{path: '/dispatchMission',component: _import('universalMission/dispatchMission'),name: 'dispatchMission',meta: {title: '接单列表',isTab: true}},
		// {path: '/classification',component: _import('universalMission/classification'),name: 'classification',meta: {title: '万能任务分类',isTab: true}},
		{path: '/riderScheduling',component: _import('riderScheduling/shifuScheduling'),name: 'riderScheduling',meta: {title: '员工调度', isTab: false}},
		{path: '/staffList', component: _import('tuanList/staffList'), name: 'staffList', meta: { title: '员工列表',isTab: true} },
		{path: '/orderDetails',component: _import('orderCenter/orderDetails'),name: 'orderDetails',meta: {title: '订单详情',isTab: false}},
		//2022.11.10 5.0版本新增
		{path: '/comment', component: _import('comment/comment'), name: 'comment', meta: { title: '评价中心',isTab: true} },
		{path: '/top', component: _import('riderTop/top'), name: 'top', meta: {title: '排行榜管理', isTab: true}},
		{path: '/couponYhq', component: _import('campus/couponYhq'), name: 'couponYhq', meta: { title: '优惠券',isTab: true} },
		{path: '/couponYhqList', component: _import('campus/couponYhqList'), name: 'couponYhqList', meta: { title: '优惠券领取列表',isTab: true} },
		//2023.03.14 6.0版本新增
		{path: '/autonym', component: _import('autonym/autonym'), name: 'autonym', meta: {title: '招募回收员', isTab: true}},
		{path: '/autonymPeople', component: _import('autonym/autonymPeople'), name: 'autonymPeople', meta: {title: '个人认证', isTab: true}},
		// 商家数据
		{path: '/shopsList', component: _import('shopsList/shopsList'), name: 'shopsList', meta: {title: '商家列表', isTab: true}},
		{path: '/shopDatas', component: _import('shopsList/shopDatas'), name: 'shopDatas', meta: {title: '商铺数据', isTab: false}},
		{path: '/shopsListAdmin', component: _import('shopsList/shopAdmin'), name: 'shopsListAdmin', meta: {title: '商铺商品管理', isTab: false}},
		{path: '/shopsOrder', component: _import('shopsList/shopsOrder'), name: 'shopsOrder', meta: { title: '任务订单',isTab: false} },
		{path: '/shopsOrderFw', component: _import('shopsList/shopsOrderFw'), name: 'shopsOrderFw', meta: { title: '服务订单',isTab: false} },
		{path: '/shopWallet', component: _import('shopsList/shopWallet'), name: 'shopWallet', meta: {title: '商铺钱包', isTab: false}},
		{path: '/yuangongtop', component: _import('shopsList/yuangongtop'), name: 'yuangongtop', meta: {title: '员工排行', isTab: false}},
		// 2023.06.13 7.1版本 新增
		{path: '/bondList', component: _import('shopsList/bondList'), name: 'bondList', meta: {title: '保证金明细', isTab: false}},
		// 2023.08.02 8.0版本 新增
		{path: '/masterWorker', component: _import('masterWorker/masterWorker'), name: 'masterWorker', meta: {title: '师傅列表', isTab: true}},
		{path: '/messageZx', component: _import('message/messageZx'), name: 'messageZx', meta: {title: '注销信息', isTab: true}},
		{path: '/masterWorkerDatas', component: _import('masterWorker/masterWorkerDatas'), name: 'masterWorkerDatas', meta: {title: '师傅收入统计', isTab: true}},
		{path: '/masterWorkerWallet', component: _import('masterWorker/masterWorkerWallet'), name: 'masterWorkerWallet', meta: {title: '钱包信息', isTab: false}},
		{path: '/shifuSchedulingSf',component: _import('riderScheduling/shifuSchedulingSf'),name: 'shifuSchedulingSf',meta: {title: '师傅地图', isTab: true}},
		{path: '/masterPosition', component: _import('masterWorker/masterPosition'), name: 'masterPosition', meta: {title: '师傅位置记录', isTab: true}},
		
	],
	beforeEnter (to, from, next) {
		let token = Vue.cookie.get('token')
		if (!token || !/\S/.test(token)) {
			clearLoginInfo()
			next({ name: 'login' })
			return
		}
		next()
	}
}

const router = new Router({
	mode: 'history', //  hash
	scrollBehavior: () => ({y: 0}),
	isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
	routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
	// 添加动态(菜单)路由
	// 1. 已经添加 or 全局路由, 直接访问
	// 2. 获取菜单列表, 添加并保存本地存储
	if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
		next()
	} else {
		http({
			url: http.adornUrl('sys/menu/nav'),
			method: 'get',
			params: http.adornParams()
		}).then(({data}) => {
			if (data && data.code === 0) {
				fnAddDynamicMenuRoutes(data.data.menuList)
				router.options.isAddDynamicMenuRoutes = true
				sessionStorage.setItem('menuList', JSON.stringify(data.data.menuList || '[]'))
				sessionStorage.setItem('permissions', JSON.stringify(data.data.permissions || '[]'))
				next({...to, replace: true})
			} else {
				sessionStorage.setItem('menuList', '[]')
				sessionStorage.setItem('permissions', '[]')
				next()
			}
		}).catch((e) => {
			console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
			router.push({name: 'login'})
		})
	}
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
	var temp = []
	for (var i = 0; i < globalRoutes.length; i++) {
		if (route.path === globalRoutes[i].path) {
			return 'global'
		} else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
			temp = temp.concat(globalRoutes[i].children)
		}
	}
	return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
	var temp = []
	for (var i = 0; i < menuList.length; i++) {
		if (menuList[i].list && menuList[i].list.length >= 1) {
			temp = temp.concat(menuList[i].list)
		} else if (menuList[i].url && /\S/.test(menuList[i].url)) {
			menuList[i].url = menuList[i].url.replace(/^\//, '')
			var route = {
				path: menuList[i].url.replace('/', '-'),
				component: null,
				name: menuList[i].url.replace('/', '-'),
				meta: {
					menuId: menuList[i].menuId,
					title: menuList[i].name,
					isDynamic: true,
					isTab: true,
					iframeUrl: ''
				}
			}
			// url以http[s]://开头, 通过iframe展示
			if (isURL(menuList[i].url)) {
				route['path'] = `i-${menuList[i].menuId}`
				route['name'] = `i-${menuList[i].menuId}`
				route['meta']['iframeUrl'] = menuList[i].url
			} else {
				try {
					route['component'] = _import(`modules/${menuList[i].url}`) || null
				} catch (e) {
				}
			}
			routes.push(route)
		}
	}
	if (temp.length >= 1) {
		fnAddDynamicMenuRoutes(temp, routes)
	} else {
		mainRoutes.name = 'main-dynamic'
		mainRoutes.children = routes
		router.addRoutes([
			mainRoutes,
			{path: '*', redirect: {name: '404'}}
		])
		sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
		console.log('\n')
		console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
		console.log(mainRoutes.children)
		console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
	}
}

export default router
