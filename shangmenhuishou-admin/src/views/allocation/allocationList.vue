<template>
	<div class="allocation-list-page">
		<div class="nf-page-hero allocation-list-page__hero">
			<span class="nf-page-hero__badge">系统配置</span>
			<h2 class="nf-page-hero__title">配置列表</h2>
			<p class="nf-page-hero__desc">管理系统全局参数、支付、协议及各类业务配置项</p>
		</div>

		<div class="nf-filter-card allocation-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body allocation-list-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="dataSelect">刷新数据</el-button>
			</div>
		</div>

		<div class="allocation-list-page__tabs-wrap">
			<el-tabs v-model="activeName" class="allocation-list-page__tabs" @tab-click="handleClick">
				<el-tab-pane v-for="(item,index) in tableData" :key="index" :label="item.paramTypeDesc" :name="'first'+index">
					<div class="nf-table-card allocation-list-page__table-card">
						<el-table v-loading="tableDataLoading" :data="item.sysConfigEntityList" class="allocation-list-page__table">
							<el-table-column fixed prop="id" label="编号" align="center" width="80">
							</el-table-column>
							<el-table-column prop="remark" label="类型">
							</el-table-column>
							<el-table-column prop="paramValue" label="内容" width="500">
								<template slot-scope="scope">
									<div class="allocation-list-page__cell-clamp">
										{{scope.row.paramValue}}
									</div>
								</template>
							</el-table-column>
							<!-- <el-table-column prop="createAt" label="创建时间">
							</el-table-column> -->
							<el-table-column label="操作" prop="id" width="120">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
										@click="amend(scope.$index, scope.row)">编辑
									</el-button>
								</template>
							</el-table-column>
						</el-table>
					</div>
				</el-tab-pane>
			<!-- <el-tab-pane label="服务费配置" name="fuwufei">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="佣金配置" name="yongjin">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="首页配置" name="thirdly">
				<el-table
					v-loading="tableDataLoading"
					:data="tableData">
					<el-table-column
					  fixed
						prop="type"
					  label="编号"
					  align="center"
					  width="80">
					</el-table-column>
					<el-table-column
					  prop="min"
					  label="类型">
					</el-table-column>
					<el-table-column
					  prop="value"
					  label="是否启用"
					  width="500">
					</el-table-column>
					<el-table-column
						prop="createAt"
						label="创建时间">
					</el-table-column>
					<el-table-column
						label="操作"
						prop="id"
						width="120">
						<template slot-scope="scope">
							<el-button
							  size="mini"
							  type="primary"
								:disabled="!isAuth('allocationList:update')"
							  @click="amendWhether(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				 修改弹框
				<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
					<el-form :model="form2">
					  <el-form-item label="配置类型：" :label-width="formLabelWidth" >
						<el-input v-model="form2.min" style="width:65%;" readonly ></el-input>
					  </el-form-item>
					  <el-form-item label="是否启用：" :label-width="formLabelWidth" >
						<el-select v-model="form2.value" placeholder="请选择类型" style="width:65%;">
						  <el-option v-for="item in values" :key="item.value" :label="item.label" :value="item.label">
						  </el-option>
						</el-select>
					  </el-form-item>
					</el-form>
				  <div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="WhetherNoticeTo()">确 定</el-button>
				  </div>
				</el-dialog>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="微信配置" name="fifth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="支付宝配置" name="sixth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="短信配置" name="seventh">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="协议配置" name="xieyi">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="APP消息推送配置" name="app">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="文件上传配置" name="wenjian">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="开关配置" name="kaiguan">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="图片配置" name="image">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="优惠券配置" name="coupon">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								<div>
									<div v-if="scope.row.selfCouponList">
										<span v-for="(item,index) in scope.row.selfCouponList">
										<span v-if="item">{{item.couponName}};</span></span>
									</div>
									
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			</el-tabs>
		</div>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form.remark" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<quill-editor v-if="form.paramType=='xieyi' " ref="myTextEditor" v-model="form.paramValue"
						:options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
					<el-upload class="avatar-uploader" v-else-if="form.paramType=='pic'" v-model="form.paramValue"
						:action="$http.adornUrlUp('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="form.paramValue" :src="form.paramValue" class="avatar"
							style="border-radius: 6px;width:148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
					<div v-else-if="form.paramType=='kaiguan'">
						<div>
							<el-radio-group v-model="form.paramValue">
								<el-radio label="是">是</el-radio>
								<el-radio label="否">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<div v-else-if="form.paramType=='coupon'">
						<div
							style="height: 150px;width: 72%;border: 1px solid #efefef;border-radius: 6px;padding: 10px;">
							<span v-for="(item,index) in form.couponList" style="border: 1px;margin-right: 5px;">
								<span v-if="item">
									{{item.couponName}} <i class="el-icon-circle-close" @click="del(index,item)"></i> ;
								</span>
							</span>
							<el-button type="primary" @click="yhqbtn()" style="margin: 5px;">选择优惠券</el-button>
						</div>
					</div>
					<el-input v-else v-model="form.paramValue" style="width:65%;"></el-input>
					<!-- <el-input v-model="form.value" style="width:65%;"></el-input> -->
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="优惠券列表" :visible.sync="dialogFormVisibleYhq" center>
			<div style="display: inline-block;float: right;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新
				</el-button>
			</div>
			<el-table v-loading="tableDataLoadingY" :data="tableDataYhq.records">
				<el-table-column prop="couponId" label="编号" width="80" fixed="left">
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="150" fixed="left">
				</el-table-column>
				<el-table-column prop="couponPicture" label="优惠券图片">
					<template slot-scope="scope">
						<img v-if="scope.row.couponPicture" :src="scope.row.couponPicture" alt="" width="40"
							height="40">
					</template>
				</el-table-column>
				<el-table-column prop="type" label="优惠券类型">
					<template slot-scope="scope">
						<span v-if="scope.row.couponType==1">新手赠送</span>
						<span v-if="scope.row.couponType==2">出售</span>
						<span v-if="scope.row.couponType==3">免费领取</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="优惠券抵扣金额" width="100">
				</el-table-column>
				<el-table-column prop="buyMoney" label="优惠券购买金额" width="100">
				</el-table-column>
				<el-table-column prop="minMoney" label="优惠券最低消费" width="150">
				</el-table-column>
				<el-table-column prop="maxReceive" label="最多领取/购买数量" width="150">
				</el-table-column>
				<el-table-column prop="validDays" label="优惠券有效期限(天)" width="150">
				</el-table-column>
				<el-table-column prop="couponNum" label="优惠券数量" width="150">
				</el-table-column>
				<el-table-column prop="isEnable" label="状态" fixed="right">
					<template slot-scope="scope">
						<span v-if="scope.row.isEnable==1">开启</span>
						<span v-else>关闭</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="150" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="discuss(scope.$index,scope.row)">确认选择
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="tableDataYhq.total">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				openValue: '是',
				closeValue: '否',
				limit: 10,
				page: 0,
				page1: 0,
				size1: 10,

				min: '',
				value: '',
				id: '',
				condition: 'xitong',
				activeName: 'first0',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					paramKey: "",
					paramValue: "",
					status: "",
					remark: "",
					paramType: "",
					paramTypeDesc: "",
				},
				form2: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
				values: [{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				tableData: [],
				quillOption: quillConfig,
				checkBoxData: [], //多选框选择的值
				dialogFormVisibleYhq: false,
				tableDataYhq: {},
				tableDataLoadingY: false,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val - 1;
				this.dataSelect();
			},
			handleClick(tab, event) {
				if (tab._props.label == '全局系统配置') {
					this.condition = 'xitong'
					this.dataSelect()
				}
				if (tab._props.label == '首页配置') {
					this.condition = 'shouye'
					this.dataSelect()
				}
				if (tab._props.label == '微信配置') {
					this.condition = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '支付宝配置') {
					this.condition = 'zhifubao'
					this.dataSelect()
				}
				if (tab._props.label == '短信配置') {
					this.condition = 'duanxin'
					this.dataSelect()
				}
				if (tab._props.label == '服务系统配置') {
					this.condition = 'renwu'
					this.dataSelect()
				}
				if (tab._props.label == '协议配置') {
					this.condition = 'xieyi'
					this.dataSelect()
				}
				if (tab._props.label == 'APP消息推送配置') {
					this.condition = 'push'
					this.dataSelect()
				}
				if (tab._props.label == '文件上传配置') {
					this.condition = 'oss'
					this.dataSelect()
				}
				if (tab._props.label == '服务费配置') {
					this.condition = 'fuwufei'
					this.dataSelect()
				}
				if (tab._props.label == '佣金配置') {
					this.condition = 'yongjin'
					this.dataSelect()
				}
				if (tab._props.label == '开关配置') {
					this.condition = 'kaiguan'
					this.dataSelect()
				}
				if (tab._props.label == '图片配置') {
					this.condition = 'image'
					this.dataSelect()
				}
				if (tab._props.label == '优惠券配置') {
					this.deployname = 'coupon'
					this.dataSelect()
				}


			},
			// 修改弹框
			amend(index, rows) {
				this.dialogFormVisible = true;
				this.form.id = rows.id;
				this.form.paramKey = rows.paramKey
				this.form.paramValue = rows.paramValue;
				this.form.status = rows.status;
				this.form.remark = rows.remark;
				this.form.paramType = rows.paramType;
				this.form.paramTypeDesc = rows.paramTypeDesc
				
				
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('sys/config/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'paramKey': this.form.paramKey,
						'paramValue': this.form.paramValue,
						'status': this.form.status,
						'remark': this.form.remark,
						'paramType': this.form.paramType,
						'paramTypeDesc': this.form.paramTypeDesc,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改首页
			amendWhether(index, rows) {
				this.dialogFormVisible1 = true;
				this.form2.id = rows.id;
				this.form2.type = rows.type
				this.form2.min = rows.min;
				this.form2.value = rows.value;
				this.form2.max = rows.max;
				this.form2.createAt = rows.createAt;
				this.form2.conditionFrom = rows.conditionFrom
			},
			WhetherNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'type': this.form2.type,
						'value': this.form2.value,
						'min': this.form2.min,
						'max': this.form2.max,
						'createAt': this.form2.createAt,
						'conditionFrom': this.form2.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			handleAvatarSuccess1(file) {
				this.form.paramValue = file.data.path;
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('sys/config/list'),
					method: 'get',
					params: this.$http.adornParams({
						// 'condition':this.condition
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			// 点击获取优惠券
			yhqbtn() {
				console.log('this.couponList', this.form.couponList)

				this.page1 = 1
				this.yhqSelect()
				this.dialogFormVisibleYhq = true
			},
			// 优惠券列表数据
			yhqSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/coupon/getCouponPageList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1 - 1,
						'limit': this.size1
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableDataYhq = returnData
					}
				})
			},
			refresh() {
				this.page1 = 1
				this.yhqSelect()
			},
			// 确认选择
			discuss(index, row) {
				// this.values.push(row.couponId)
				this.form.couponList.push(row)
				this.dialogFormVisibleYhq = false

			},
			del(index, row) {
				// this.values.splice(index,1)
				this.form.couponList.splice(index, 1)
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style lang="scss" scoped>
.allocation-list-page {
	padding: 0;
	overflow-x: hidden;
}

.allocation-list-page__hero {
	margin-bottom: 12px;
}

.allocation-list-page__filter {
	margin-bottom: 12px;
}

.allocation-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

/* tabs 零外距 + 表格白卡 */
.allocation-list-page__tabs-wrap {
	margin-bottom: 0;
}

.allocation-list-page__tabs {
	margin-bottom: 0;
}

.allocation-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.allocation-list-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.allocation-list-page__table-card >>> .el-table {
	border-radius: 0;
}

.allocation-list-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.allocation-list-page__table >>> .el-table__body td {
	background: #fff;
}

.allocation-list-page__cell-clamp {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
	max-height: 80px;
	line-height: 1.5;
	color: var(--nf-text-primary, #1a2433);
}

.eit {
	height: 120px;
}
</style>