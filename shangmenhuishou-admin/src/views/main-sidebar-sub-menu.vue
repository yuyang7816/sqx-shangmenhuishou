<template>
	<el-submenu v-if="menu.list && menu.list.length >= 1" :index="menu.menuId + ''"
		:popper-class="'site-sidebar--' + sidebarLayoutSkin + '-popper'">
		<template slot="title">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</template>
		<sub-menu v-for="item in menu.list" :key="item.menuId" :menu="item" :dynamicMenuRoutes="dynamicMenuRoutes">
		</sub-menu>
	</el-submenu>
	<el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)" class="nf-menu-item">
		<template v-if="menu.menuId==142&&chatNumDrz!=0">
			<el-badge class="item" :value="chatNumDrz">
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			</el-badge>
			<span>{{ menu.name }}</span>
		</template>
		<template v-else-if="menu.menuId==392&&chatNumDts!=0">
			<el-badge class="item" :value="chatNumDts">
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			</el-badge>
			<span>{{ menu.name }}</span>
		</template>
		<template v-else-if="menu.menuId==132&&chatNumDsh!=0">
			<el-badge class="item" :value="chatNumDsh">
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			</el-badge>
			<span>{{ menu.name }}</span>
		</template>
		<template v-else-if="menu.menuId==72&&chatNumDFw!=0">
			<el-badge class="item" :value="chatNumDFw">
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			</el-badge>
			<span>{{ menu.name }}</span>
		</template>
		<template v-else-if="menu.menuId==41&&chatNumTs!=0">
			<el-badge class="item" :value="chatNumTs">
				<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			</el-badge>
			<span>{{ menu.name }}</span>
		</template>
		<template v-else>
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</template>
	</el-menu-item>
</template>

<script>
	import SubMenu from './main-sidebar-sub-menu'
	export default {
		name: 'sub-menu',
		data(){
			return {
				chatNumDrz:0,
				chatNumDts:0,
				chatNumDsh:0,
				chatNumDFw:0,
				chatNumTs:0,
			}
		},
		props: {
			menu: {
				type: Object,
				required: true
			},
			dynamicMenuRoutes: {
				type: Array,
				required: true
			}
		},
		components: {
			SubMenu
		},
		computed: {
			sidebarLayoutSkin: {
				get() {
					return this.$store.state.common.sidebarLayoutSkin
				}
			}
		},
		methods: {
			gotoRouteHandle(menu) {
				var route = this.dynamicMenuRoutes.filter(item => item.meta.menuId === menu.menuId)
				if (route.length >= 1) {
					this.$store.commit('common/updateMenuActiveName', menu.menuId + '')
					this.$router.push({
						name: route[0].name
					})
				}
			}
		},
		mounted() {
			setInterval(() => {
				if(this.$cookie.get('numberDrz')){
					this.chatNumDrz = this.$cookie.get('numberDrz')
				}
				if(this.$cookie.get('numberDts')){
					this.chatNumDts = this.$cookie.get('numberDts')
				}
				if(this.$cookie.get('numberDsh')){
					this.chatNumDsh = this.$cookie.get('numberDsh')
				}
				if(this.$cookie.get('numberDfw')){
					this.chatNumDFw = this.$cookie.get('numberDfw')
				}
				if(this.$cookie.get('numberTs')){
					this.chatNumTs = this.$cookie.get('numberTs')
				}
				
			}, 1000)
		},
	}
</script>
<style>
	.el-badge__content.is-fixed.is-dot {
		right: -5px;
		top: 20px;
	}
	.el-badge__content.is-fixed{
		right: 0px;
		top: 20px;
	}
</style>
