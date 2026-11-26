<template>
	<div class="order-details-page">
		<div class="order-details-page__nav">
			<a href="#" class="order-details-page__back-link" @click.prevent="prev">
				<icon-svg name="jiantou" class="order-details-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="order-details-page__nav-sep">|</span>
			<span class="order-details-page__nav-title">订单详情</span>
		</div>

		<div class="nf-page-hero order-details-page__hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">订单详情</h2>
			<p class="nf-page-hero__desc">查看订单基本信息、商品明细、收货与物流及用户评价</p>
		</div>

		<el-row :gutter="12" class="order-details-page__main">
			<el-col :span="15">
				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-s-order"></i>
						<span>订单信息</span>
					</div>
					<div class="nf-filter-card__body order-details-page__card-body">
						<div class="order-details-page__detail-table">
							<table>
								<tbody>
									<tr>
										<th>订单号</th>
										<td>{{ tableData.orderNum }}</td>
										<th>订单备注</th>
										<td class="border-rt">{{ tableData.descrition ? tableData.descrition : '未填写' }}</td>
									</tr>
									<tr>
										<th>订单状态</th>
										<td>
											<span v-if="tableData.status == 1">待付款</span>
											<span v-if="tableData.status == 2">待发货</span>
											<span v-if="tableData.status == 3">已发货</span>
											<span v-if="tableData.status == 4">已收货</span>
											<span v-if="tableData.status == 5">已取消</span>
											<span v-if="tableData.status == 6">退款中</span>
											<span v-if="tableData.status == 7">已退款 (<span class="order-details-page__danger">*原因：{{ tableData.refund }}</span>)</span>
											<span v-if="tableData.status == 8">已驳回 (<span class="order-details-page__danger">*原因：{{ tableData.refund }}</span>)</span>
											<span v-if="tableData.status == 9">已拼团</span>
											<span v-if="tableData.status == 10">已评价</span>
										</td>
										<th>支付方式</th>
										<td class="border-rt">
											<span v-if="tableData.payWay === 1">微信</span>
											<span v-if="tableData.payWay === 2">微信公众号</span>
											<span v-if="tableData.payWay === 3">微信小程序</span>
											<span v-if="tableData.payWay === 4">app支付宝</span>
											<span v-if="tableData.payWay === 5">H5支付宝</span>
											<span v-if="tableData.payWay === 6">零钱</span>
											<span v-if="tableData.payWay === 7">积分</span>
											<span v-if="tableData.payWay === 8">支付宝</span>
											<span v-if="tableData.payWay === null || tableData.payWay === ''">未支付</span>
										</td>
									</tr>
									<tr>
										<th class="border-bt">创建时间</th>
										<td class="border-bt">{{ tableData.createAt }}</td>
										<th class="border-bt">付款时间</th>
										<td class="border-bt border-rt">{{ tableData.payTime ? tableData.payTime : '未付款' }}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-goods"></i>
						<span>商品信息</span>
					</div>
					<div class="nf-filter-card__body order-details-page__card-body">
						<div class="order-details-page__product-table">
							<div class="order-details-page__product-head">
								<span class="order-details-page__product-col order-details-page__product-col--img">商品图片</span>
								<span class="order-details-page__product-col order-details-page__product-col--title">商品标题</span>
								<span class="order-details-page__product-col order-details-page__product-col--price">商品价格</span>
								<span class="order-details-page__product-col order-details-page__product-col--pay">支付金额</span>
							</div>
							<div class="order-details-page__product-row">
								<div class="order-details-page__product-col order-details-page__product-col--img">
									<img :src="tableData.img" alt="" class="order-details-page__product-img">
								</div>
								<div class="order-details-page__product-col order-details-page__product-col--title">
									<span class="order-details-page__product-name">{{ tableData.title }}</span>
									<div class="order-details-page__product-spec">规格：{{ tableData.detailJson ? tableData.detailJson : '-' }}</div>
								</div>
								<div class="order-details-page__product-col order-details-page__product-col--price">
									￥{{ tableData.price }} x {{ tableData.number }}
								</div>
								<div class="order-details-page__product-col order-details-page__product-col--pay">
									￥{{ tableData.payMoney }}
								</div>
							</div>
						</div>
					</div>
				</div>
			</el-col>

			<el-col :span="9">
				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-location-outline"></i>
						<span>收货信息</span>
					</div>
					<div class="nf-filter-card__body order-details-page__card-body">
						<div class="order-details-page__detail-table">
							<table>
								<tbody>
									<tr>
										<th>收货人</th>
										<td>{{ tableData.consignee }}</td>
									</tr>
									<tr>
										<th>手机号</th>
										<td>{{ tableData.mobile }}</td>
									</tr>
									<tr>
										<th class="border-bt">收货地址</th>
										<td class="border-bt border-rt">{{ tableData.detail }}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-truck"></i>
						<span>物流信息</span>
					</div>
					<div class="nf-filter-card__body order-details-page__card-body">
						<div v-if="tableData.status == 3 || tableData.status == 4" class="order-details-page__detail-table">
							<table>
								<tbody>
									<tr>
										<th>物流公司</th>
										<td>{{ tableData.expressName }}</td>
									</tr>
									<tr>
										<th class="border-bt">运单号</th>
										<td class="border-bt border-rt">{{ tableData.expressNumber }}</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div v-else class="order-details-page__empty">暂无信息</div>
					</div>
				</div>

				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-chat-dot-round"></i>
						<span>评价信息</span>
					</div>
					<div class="nf-filter-card__body order-details-page__card-body">
						<div v-if="discussData" class="order-details-page__detail-table">
							<table>
								<tbody>
									<tr>
										<th>评价用户</th>
										<td>{{ discussData.userName }}</td>
									</tr>
									<tr>
										<th>用户图像</th>
										<td>
											<img :src="discussData.img" alt="" class="order-details-page__avatar">
										</td>
									</tr>
									<tr>
										<th>评价内容</th>
										<td>{{ discussData.content }}</td>
									</tr>
									<tr>
										<th class="border-bt">评价时间</th>
										<td class="border-bt border-rt">{{ discussData.createTime }}</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div v-else class="order-details-page__empty">暂无评价</div>
					</div>
				</div>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				expressName: '',
				expressNumber: '',
				orderName: '',
				orderNumber: '',
				dialogFormVisible: false,
				tableData: {},
				discussData: {}
			}
		},

		methods: {
			prev() {
				this.$router.back()
			},
			dataSelect() {
				let id = this.$route.query.id
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/orders/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			discusselect() {
				let id = this.$route.query.id
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsComment/findByOrderId'),
					method: 'get',
					params: this.$http.adornParams({
						'orderId': id
					})
				}).then(({
					data
				}) => {
					if (data && data.status == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.discussData = returnData
					} else {
						this.$message.error(data.msg)
					}

				})
			}
		},
		mounted() {
			this.dataSelect()
			this.discusselect()
		}
	};
</script>

<style lang="scss" scoped>
.order-details-page {
	padding: 0;
	overflow-x: hidden;
}

.order-details-page__nav {
	display: flex;
	align-items: center;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-primary, #1a2433);
}

.order-details-page__back-link {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	color: var(--nf-brand, #01af74);
	text-decoration: none;
	cursor: pointer;
}

.order-details-page__back-link:hover {
	color: var(--nf-brand-deep, #017c52);
}

.order-details-page__back-icon {
	width: 1.2em;
	height: 1.2em;
}

.order-details-page__nav-sep {
	margin: 0 12px;
	color: var(--nf-border, #e8eaed);
}

.order-details-page__nav-title {
	font-size: 16px;
	font-weight: 600;
}

.order-details-page__hero {
	margin-bottom: 12px;
}

.order-details-page__main {
	margin-left: 0 !important;
	margin-right: 0 !important;
}

.order-details-page__card {
	margin-bottom: 12px;
	padding: 0;
	overflow: hidden;
}

.order-details-page__card-body {
	padding: 16px;
}

.order-details-page__detail-table {
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-sm, 6px);
	overflow: hidden;
}

.order-details-page__detail-table table {
	width: 100%;
	border-collapse: collapse;
}

.order-details-page__detail-table tr {
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.order-details-page__detail-table tr:last-child {
	border-bottom: none;
}

.order-details-page__detail-table th {
	background: var(--nf-page-bg, #f4f6fa);
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
	font-size: 13px;
	line-height: 1.5;
	white-space: nowrap;
	width: 100px;
}

.order-details-page__detail-table td {
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
	color: var(--nf-text-primary, #1a2433);
	font-size: 13px;
	line-height: 1.5;
	word-break: break-all;
}

.border-rt {
	border-right: none !important;
}

.border-bt {
	border-bottom: none !important;
}

.order-details-page__danger {
	color: #f56c6c;
}

.order-details-page__product-table {
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-sm, 6px);
	overflow: hidden;
}

.order-details-page__product-head {
	display: flex;
	align-items: center;
	padding: 12px 16px;
	background: var(--nf-page-bg, #f4f6fa);
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	font-size: 13px;
	font-weight: 600;
	color: var(--nf-text-primary, #1a2433);
}

.order-details-page__product-row {
	display: flex;
	align-items: flex-start;
	padding: 12px 16px;
	background: #fff;
}

.order-details-page__product-col {
	font-size: 13px;
	color: var(--nf-text-primary, #1a2433);
	line-height: 1.5;
}

.order-details-page__product-col--img {
	flex: 0 0 80px;
}

.order-details-page__product-col--title {
	flex: 1;
	min-width: 0;
	padding-right: 12px;
}

.order-details-page__product-col--price {
	flex: 0 0 120px;
}

.order-details-page__product-col--pay {
	flex: 0 0 100px;
}

.order-details-page__product-img {
	width: 40px;
	height: 40px;
	border-radius: var(--nf-radius-sm, 6px);
	object-fit: cover;
}

.order-details-page__product-name {
	display: block;
	margin-bottom: 4px;
}

.order-details-page__product-spec {
	color: var(--nf-text-secondary, #5a6578);
	font-size: 12px;
}

.order-details-page__avatar {
	width: 40px;
	height: 40px;
	border-radius: var(--nf-radius-sm, 6px);
	object-fit: cover;
}

.order-details-page__empty {
	padding: 16px;
	text-align: center;
	color: var(--nf-text-secondary, #5a6578);
	font-size: 13px;
	background: #fff;
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-sm, 6px);
}
</style>
