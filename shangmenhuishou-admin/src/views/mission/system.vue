<template>
	<div class="system-page">
		<div class="nf-page-hero system-page__hero">
			<span class="nf-page-hero__badge">系统服务</span>
			<h2 class="nf-page-hero__title">系统服务</h2>
			<p class="nf-page-hero__desc">管理服务大厅、派单、接单审核、维权与系统用户，支持发布服务、置顶与用户维护</p>
		</div>

		<div class="system-page__tabs-wrap">
			<el-tabs v-model="activeName" class="system-page__tabs" @tab-click="handleClick">
				<el-tab-pane label="服务大厅" name="first">
					<div class="nf-filter-card system-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body system-page__filter-body">
							<div class="system-page__field">
								<span class="system-page__label">系统用户：</span>
								<el-select v-model="userId" class="system-page__select">
									<el-option v-for="item in userRwData" :key="item.userId" :label="item.nickName"
										:value="item.userId">
									</el-option>
								</el-select>
							</div>
							<div class="system-page__actions">
								<el-button :disabled="!isAuth('system:issue')" size="mini" type="primary"
									icon="el-icon-document" @click="passClick()">发布服务</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card system-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.list" class="system-page__table">
				<el-table-column fixed prop="label" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.label" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="nickName" label="用户姓名" width="150">
					<template slot-scope="scope">
						<span class="system-page__link"
							@click="updates(scope.row)">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="content" label="服务要求">
				</el-table-column>
				<el-table-column prop="verifyContent" label="验证信息" width="160">
				</el-table-column>
				<el-table-column prop="openContent" label="打开方式" width="160">
				</el-table-column>
				<el-table-column prop="taskOriginalPrice" label="价格" width="160">
				</el-table-column>
				<el-table-column prop="taskNum" label="数量" width="160">
				</el-table-column>
				<el-table-column label="置顶状态" width="160">
					<template slot-scope="scope">
						<span v-if="scope.row.isTop==1" class="system-page__status">置顶中</span>
						<span v-if="scope.row.isTop==0">未置顶</span>
					</template>
				</el-table-column>
				<el-table-column prop="endNum" label="接单数量" width="160">
				</el-table-column>
				<el-table-column prop="createTime" label="发布时间" width="160">
				</el-table-column>
				<el-table-column prop="endTime" label="截止时间" width="160">
				</el-table-column>
				<el-table-column prop="auditTime" label="审核时间（按分）" width="160">
				</el-table-column>
				<el-table-column prop="restrictTime" label="服务限时（按分）" width="160">
				</el-table-column>
				<el-table-column fixed='right' prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="system-page__status" v-if="scope.row.state === 0 ">待审核</span>
						<span class="system-page__status" v-if="scope.row.state === 1 ">进行中</span>
						<span class="system-page__status" v-if="scope.row.state === 2 ">已结束</span>
						<span class="system-page__status" v-if="scope.row.state === 3 ">已拒绝</span>
						<span class="system-page__status" v-if="scope.row.state === 4 ">已结算</span>
					</template>
				</el-table-column>
						</el-table>
						<div class="system-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="我的派单" name="fourthly">
					<div class="nf-filter-card system-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body system-page__filter-body">
							<div class="system-page__field">
								<span class="system-page__label">服务状态：</span>
								<el-select v-model="state" class="system-page__select" @change="animeDat1(state)">
									<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
						</div>
					</div>
					<div class="nf-table-card system-page__table-card">
						<el-table v-loading="tableDataLoading" :data="tableData2.list" class="system-page__table">
				<el-table-column fixed prop="label" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.label" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="content" label="服务要求" width="150">
				</el-table-column>
				<el-table-column prop="verifyContent" label="验证信息" width="160">
				</el-table-column>
				<el-table-column prop="openContent" label="打开方式" width="160">
				</el-table-column>
				<el-table-column prop="taskPrice" label="价格" width="160">
				</el-table-column>
				<el-table-column prop="taskNum" label="数量" width="160">
				</el-table-column>
				<el-table-column prop="endNum" label="接单数量" width="160">
				</el-table-column>
				<el-table-column prop="createTime" label="发布时间" width="160">
				</el-table-column>
				<el-table-column prop="endTime" label="截止时间" width="160">
				</el-table-column>
				<el-table-column prop="auditTime" label="审核时间（按分）" width="160">
				</el-table-column>
				<el-table-column prop="restrictTime" label="服务限时（按分）" width="160">
				</el-table-column>
				<el-table-column fixed='right' prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="system-page__status" v-if="scope.row.state === 0 ">待审核</span>
						<span class="system-page__status" v-if="scope.row.state === 1 ">已通过</span>
						<span class="system-page__status" v-if="scope.row.state === 3 ">已拒绝</span>
						<span class="system-page__status" v-if="scope.row.state === 4 ">已结算</span>
					</template>
				</el-table-column>
				<el-table-column fixed='right' label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.isTop == 0 && scope.row.state==1"
							@click="stick(scope.row)">置顶
						</el-button>
						<el-button size="mini" type="primary" v-if="scope.row.isTop !== 0" disabled>置顶
						</el-button>
					</template>
				</el-table-column>
						</el-table>
						<div class="system-page__pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData2.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="接单审核" name="second">
					<div class="nf-filter-card system-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body system-page__filter-body">
							<div class="system-page__field">
								<span class="system-page__label">服务状态：</span>
								<el-select v-model="state" class="system-page__select" @change="animeDat2(state)">
									<el-option v-for="item in taskstates" :key="item.value" :label="item.label" :value="item.value">
									</el-option>
								</el-select>
							</div>
						</div>
					</div>
					<div class="nf-table-card system-page__table-card">
						<el-table v-loading="tableDataLoading" :data="safeguardData.list" class="system-page__table">
				<el-table-column fixed prop="label" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.label" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="content" label="服务要求" width="150">
				</el-table-column>
				<el-table-column prop="verifyContent" label="验证信息" width="160">
				</el-table-column>
				<el-table-column prop="openContent" label="打开方式" width="160">
				</el-table-column>
				<el-table-column prop="taskPrice" label="价格" width="160">
				</el-table-column>
				<el-table-column prop="taskNum" label="数量" width="160">
				</el-table-column>
				<el-table-column prop="endNum" label="接单数量" width="160">
				</el-table-column>
				<el-table-column prop="createTime" label="发布时间" width="160">
				</el-table-column>
				<el-table-column prop="endTime" label="截止时间" width="160">
				</el-table-column>
				<el-table-column prop="auditTime" label="审核时间（按分）" width="160">
				</el-table-column>
				<el-table-column prop="restrictTime" label="服务限时（按分）" width="160">
				</el-table-column>
				<el-table-column fixed='right' prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span class="system-page__status" v-if="scope.row.state === 0 ">待审核</span>
						<span class="system-page__status" v-if="scope.row.state === 1 ">进行中</span>
						<span class="system-page__status" v-if="scope.row.state === 2 ">已结束</span>
						<span class="system-page__status" v-if="scope.row.state === 3 ">已拒绝</span>
						<span class="system-page__status" v-if="scope.row.state === 4 ">已结算</span>
					</template>
				</el-table-column>
						</el-table>
						<div class="system-page__pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="safeguardData.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="我的维权" name="third">
					<div class="nf-table-card system-page__table-card">
						<el-table v-loading="tableDataLoading" :data="classData.list" class="system-page__table">
				<el-table-column fixed prop="label" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.label" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="content" label="服务要求" width="150">
				</el-table-column>
				<el-table-column prop="verifyContent" label="验证信息" width="160">
				</el-table-column>
				<el-table-column prop="openContent" label="打开方式" width="160">
				</el-table-column>
				<el-table-column prop="taskPrice" label="价格" width="160">
				</el-table-column>
				<el-table-column prop="taskNum" label="数量" width="160">
				</el-table-column>
				<el-table-column prop="endNum" label="接单数量" width="160">
				</el-table-column>
				<el-table-column prop="createTime" label="发布时间" width="160">
				</el-table-column>
				<el-table-column prop="endTime" label="截止时间" width="160">
				</el-table-column>
				<el-table-column prop="auditTime" label="审核时间（按分）" width="160">
				</el-table-column>
				<el-table-column prop="restrictTime" label="服务限时（按分）" width="160">
				</el-table-column>
				<el-table-column fixed='right' prop="state" label="状态" width="160">
					<template slot-scope="scope">
						<span class="system-page__status" v-if="scope.row.state === 0 ">待审核</span>
						<span class="system-page__status" v-if="scope.row.state === 1 ">已通过</span>
						<span class="system-page__status" v-if="scope.row.state === 2 ">已拒绝</span>
						<span class="system-page__status" v-if="scope.row.state === 3 ">已维权</span>
						<span class="system-page__status" v-if="scope.row.state === 4 ">已放弃</span>
						<span class="system-page__status" v-if="scope.row.state === 5 ">已结束</span>
					</template>
				</el-table-column>
				<el-table-column fixed='right' label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="protect(scope.$index, scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
						</el-table>
						<div class="system-page__pagination">
							<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="classData.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="系统用户" name="fifxd">
					<div class="nf-filter-card system-page__filter">
						<div class="nf-filter-card__head">
							<i class="el-icon-search"></i>
							<span>筛选条件</span>
						</div>
						<div class="nf-filter-card__body system-page__filter-body">
							<div class="system-page__field">
								<span class="system-page__label">手机号：</span>
								<el-input class="system-page__input" @keydown.enter.native="select" clearable placeholder="请输入手机号"
									v-model="phone"></el-input>
							</div>
							<div class="system-page__actions">
								<el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
								<el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
								<el-button size="mini" type="primary" :disabled="!isAuth('system:add')"
									icon="el-icon-plus" @click="addUser">添加用户</el-button>
							</div>
						</div>
					</div>
					<div class="nf-table-card system-page__table-card">
						<el-table v-loading="tableDataLoading" :data="userData.list" class="system-page__table">
				<el-table-column prop="nickName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.imageUrl==null" src="~@/assets/img/avatar.png" alt="" width="40"
							height="40">
						<img v-else :src="scope.row.imageUrl" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span class="system-page__link"
							@click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="invitationCode" label="邀请码">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="320">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('system:update')"
							@click="Compile(scope.row)">修改用户
						</el-button>
						<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('system:delete')"
							@click="deleteuser(scope.row)">删除用户
						</el-button>
					</template>
				</el-table-column>
						</el-table>
						<div class="system-page__pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 发布服务弹框 -->
		<el-dialog title="发布服务" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择服务：</span>
				<el-select v-model="id" style="width:50%;">
					<el-option v-for="item in renwuData" :key="item.id" :label="item.classifyName"
						:value="item.list[0].id">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
				<el-input style="width:50%;" v-model="title" type="text" placeholder="请输入简短标题"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">服务说明：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入服务说明要求">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务数量：</span>
				<el-input style="width:50%;" v-model="taskNum" type="number" placeholder="最低设置20个"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">每单金额：</span>
				<el-input style="width:50%;" v-model="taskOriginalPrice" type="number" placeholder="最低设置0.32元">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">打开方式：</span>
				<el-radio-group v-model="way" @change="wayChange">
					<el-radio :label="1">链接打开</el-radio>
					<el-radio :label="2">口令打开</el-radio>
					<el-radio :label="3">扫码打开</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="isway">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="openContent" placeholder="请输入完整链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="isway1">
				<span style="width: 200px;display: inline-block;text-align: right;">口令：</span>
				<el-input style="width:50%;" v-model="openContent" placeholder="请输入口令"></el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="isway2">
				<p style="font-size: 14px;color: #01af74;padding-left:200px;">在步骤1中添加需要扫描的二维码图片</p>
			</div>
			<div style="margin-bottom: 10px;padding:0 25%;">
				<div class="step_item" style="margin-bottom: 10px;" v-for="(item,index) in stepList" :key="index">
					<div class="step_title" style="margin-bottom: 15px;background: rgb(240, 240, 240);padding: 0 10px;">
						第{{index+1}}步 <span @click="deleteStep(index)" style="float: right;">删除</span></div>
					<div style="display: flex;">
						<div
							style="display: inline-block;width:148px;height:137px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 137px;">
							<el-upload class="avatar-uploader" v-model="picture"
								:action="$http.adornUrl('file/upload')" :show-file-list="false"
								:on-success="handleAvatarSuccess">
								<img v-if="item.picture" :src="item.picture" class="avatar"
									style="width: 148px;height:137px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon" @click="curRowIndex=index"></i>
							</el-upload>
						</div>
						<div style="display: inline-block;margin-left:50px;">
							<el-input style="width:148px;" v-model="item.content" type="textarea" :rows="6"
								placeholder="请输入步骤说明"></el-input>
						</div>
					</div>
					<div>
						<el-switch v-model="item.isVerify" @change="switch1Change(item.isVerify,index)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
						<span>设为验证截图</span>
					</div>
				</div>
			</div>
			<div style="margin-bottom: 10px;padding-left:205px;">
				<el-button size="mini" type="primary" @click="addStep">+增加步骤</el-button>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">验证信息：</span>
				<el-input style="width:50%;" v-model="verifyContent" placeholder="请设置验证信息"></el-input>
			</div>
			<h3 style="padding-left: 130px;">其他信息</h3>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务限时：</span>
				<el-select v-model="restrictTime" style="width:50%;" @change="bindPickerChange(restrictTime)">
					<el-option v-for="item in restrictTimes" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核限时：</span>
				<el-select v-model="auditTime" style="width:50%;" @change="timePickerChange(auditTime)">
					<el-option v-for="item in auditTimes" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">服务截止：</span>
				<el-select v-model="endTime" style="width:50%;" @change="datePickerChange(endTime)">
					<el-option v-for="item in endTimes" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="triumph()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加系统用户弹框 -->
		<el-dialog title="添加系用户" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">昵称：</span>
				<el-input style="width:50%;" v-model="nickName" type="text" placeholder="请输入昵称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
				<el-input style="width:50%;" v-model="phone" type="number" placeholder="请输入手机号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">密码：</span>
				<el-input style="width:50%;" v-model="password" type="password" placeholder="请输入密码"></el-input>
			</div>
			<div style="margin-bottom: 10px;position: relative;">
				<span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
				<el-input style="width:50%;" v-model="invitationCode" placeholder="请填写邀请码"></el-input>
				<span style="position: absolute;right: 22%;top: 6px;cursor: pointer;" @click="guanCode">官方邀请码</span>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图像：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="userAtionto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改系统服务 -->
		<el-dialog title="修改系统服务" :visible.sync="dialogFormVisible2" center>
			<el-form :model="form">
				<el-form-item label="昵称：" :label-width="formLabelWidth">
					<el-input v-model="form.nickName" placeholder="请输入昵称" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="手机号：" :label-width="formLabelWidth">
					<el-input v-model="form.phone" placeholder="请输入手机号" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="支付宝账号：" :label-width="formLabelWidth">
					<el-input v-model="form.zhifubao" placeholder="请输入支付宝账号" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="支付宝名称：" :label-width="formLabelWidth">
					<el-input v-model="form.zhifubaoName" placeholder="请输入支付宝名称" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="图像：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							:action="$http.adornUrl('file/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 置顶弹框 -->
		<el-dialog title="置顶弹框" :visible.sync="dialogFormVisible3" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">置顶时间：</span>
				<el-input style="width:50%;" v-model="time" type="number" :min="1" placeholder="请输入置顶时间"></el-input> 小时
				<p style="padding-left: 205px;margin: 0;color: #01af74;font-size: 14px;">置顶收费为2元/小时</p>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="sticktionto()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: '1',
				closeValue: '0',
				limit: 10,
				page: 1,
				isVerify: 0,
				nickName: '',
				member: '',
				phone: '',
				password: '',
				invitationCode: '',
				title: '',
				content: '',
				taskNum: '',
				taskOriginalPrice: '',
				openContent: '',
				verifyContent: '',
				picture: '',
				sort: 0,
				way: 1,
				isway: true,
				isway1: false,
				isway2: false,
				label: '',
				index: '0',
				imageUrl: '',
				index_time: '0',
				index_date: '0',
				tiem: '',
				helpTaskId: '',
				form: {
					userId: '',
					nickName: '',
					phone: '',
					imageUrl: '',
					zhifubao: '',
					zhifubaoName: '',
				},
				sorts: [{
						label: '全部',
						value: 0
					},
					{
						label: '最多',
						value: 1
					},
					{
						label: '最热',
						value: 2
					},
					{
						label: '最早',
						value: 3
					}
				],
				restrictTime: '10分钟',
				restrictTimes: [{
						label: '10分钟',
						value: 0
					},
					{
						label: '30分钟',
						value: 1
					},
					{
						label: '1小时',
						value: 2
					},
					{
						label: '6小时',
						value: 3
					},
					{
						label: '24小时',
						value: 4
					},
					{
						label: '2天',
						value: 5
					},
					{
						label: '3天',
						value: 6
					},
					{
						label: '5天',
						value: 7
					}
				],
				auditTime: '12小时',
				auditTimes: [{
						label: '12小时',
						value: 0
					},
					{
						label: '24小时',
						value: 1
					},
					{
						label: '48小时',
						value: 2
					},
				],
				endTime: '3小时后',
				endTimes: [{
						label: '3小时后',
						value: 0
					},
					{
						label: '6小时后',
						value: 1
					},
					{
						label: '12小时后',
						value: 2
					},
					{
						label: '18小时后',
						value: 3
					},
					{
						label: '1天后',
						value: 4
					},
					{
						label: '2天后',
						value: 5
					},
					{
						label: '3天后',
						value: 6
					},
					{
						label: '6天后',
						value: 7
					},
					{
						label: '12天后',
						value: 8
					},
					{
						label: '30天后',
						value: 9
					}
				],
				array1: ['10', '30', '60', '360', '1440', '2880', '4320', '7200'],
				time1: ['720', '1440', '2880'],
				date1: ['180', '360', '720', '1080', '1440', '2880', '4320', '8640', '17280', '43200'],
				helpTaskDetailss: [],
				stepList: [],
				logo: [],
				userData: [],
				userId: '',
				state: -1,
				describes: '',
				token: '',
				states: [{
						label: '全部',
						value: -1
					},
					{
						label: '待审核',
						value: 0
					},
					{
						label: '进行中',
						value: 1
					},
					{
						label: '已结束',
						value: 2
					},
					{
						label: '已拒绝',
						value: 3
					},
					{
						label: '已结算',
						value: 4
					}
				],
				taskstates: [{
						label: '全部',
						value: -1
					},
					{
						label: '待审核',
						value: 1
					},
					{
						label: '已通过',
						value: 2
					},
					{
						label: '已拒绝',
						value: 3
					},
					{
						label: '已维权',
						value: 4
					},
					{
						label: '已放弃',
						value: 5
					},
					{
						label: '已超时',
						value: 6
					}
				],
				id: '',
				time: '',
				btnChangeEnable: true,
				helpTakeId: '',
				helpMaintainId: '',
				content: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: [],
				tableData2: [],
				helpTaskData: [],
				protectjieData: [],
				protectpaiData: [],
				helpTaskData: [],
				safeguardData: [],
				classData: [],
				renwuData: [],
				userRwData: []
			}
		},
		methods: {
			// 图片上传
			handleAvatarSuccess1(file) {
				this.imageUrl = file.data.path;
			},
			handleAvatarSuccess2(file) {
				this.form.imageUrl = file.data.path;
			},
			// 是否验证图
			switch1Change(event, index) {
				var sort1 = event == true ? '1' : '0';
				this.stepList[index].isVerify = sort1;
			},
			bindPickerChange(value) {
				this.index = value;
				var restrictTime = this.array1[this.index];
				console.log(restrictTime)
			},
			timePickerChange(value) {
				this.index_time = value;
				var auditTime = this.time1[this.index_time];
				console.log(auditTime)
			},
			datePickerChange(value) {
				this.index_date = value;
				var endTime = this.date1[this.index_date];
				console.log(endTime)
			},
			// 增加步
			addStep() {
				const step = {};
				step.picture = '';
				step.content = '';
				step.isVerify = '0';
				step.sort = '';
				this.stepList.push(step);
			},
			// 删除步
			deleteStep(index) {
				this.stepList.splice(index, 1);
			},
			handleAvatarSuccess(file) {
				if (this.curRowIndex == 0) {
					this.picture = file.data.path;
					this.stepList[0].picture = this.picture
					return
				} else {
					this.picture = file.data.path;
					this.stepList[this.curRowIndex].picture = this.picture
					this.stepList[this.curRowIndex].sort = this.curRowIndex
				}
			},
			// 查询
			select() {
				this.userSelect()
			},
			// 重置
			cleans() {
				this.phone = ''
				this.userSelect()
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val;
				this.safeguardSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.safeguardSelect()
			},
			handleSizeChange2(val) {
				this.limit = val;
				this.classifySelect()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.classifySelect()
			},
			handleSizeChange3(val) {
				this.limit = val;
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val;
				this.taskdataSelect()
			},
			// 打开方式
			wayChange(val) {
				if (val == 1) {
					this.isway = true
					this.isway1 = false
					this.isway2 = false
				}
				if (val == 2) {
					this.isway = false
					this.isway1 = true
					this.isway2 = false
				}
				if (val == 3) {
					this.isway = false
					this.isway1 = false
					this.isway2 = true
				}
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '服务大厅') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
					this.userRwSelect()
				}
				if (tab._props.label == '我的派单') {
					this.page = 1
					this.limit = 10
					this.taskdataSelect()
				}
				if (tab._props.label == '接单审核') {
					this.page = 1
					this.limit = 10
					this.safeguardSelect()
				}
				if (tab._props.label == '我的维权') {
					this.page = 1
					this.limit = 10
					this.classifySelect()
				}
				if (tab._props.label == '系统用户') {
					this.page = 1
					this.limit = 10
					this.userSelect()
				}
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// select选择事件
			animeDat1(state) {
				this.taskdataSelect()
			},
			animeDat2(state) {
				this.safeguardSelect()
			},
			animeDat3(state) {
				this.classifySelect()
			},
			// 发布服务
			passClick() {
				this.dialogFormVisible = true
			},
			// 置顶弹框
			stick(row) {
				this.dialogFormVisible3 = true
				this.helpTaskId = row.id
			},
			// 置顶
			sticktionto() {
				this.$http({
					url: this.$http.adornUrl('helpTask/topHelpTask'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.time,
						'helpTaskId': this.helpTaskId,
						'userId': this.userId
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.time = ''
								this.dialogFormVisible3 = false
								this.taskdataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 获取服务数据信息
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTask/selectHelpTask'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sort': this.sort,
						'userId': this.userId
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data.pageUtils;
						this.tableData = returnData
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 发布
			triumph() {
				var auditTime = this.time1[this.index_time];
				var restrictTime = this.array1[this.index];
				var endTime = this.date1[this.index_date];
				if (this.picture == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '图片信息不能为空',
						type: 'warning'
					});
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入服务要求',
						type: 'warning'
					});
					return
				}
				if (this.taskNum < 20) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '服务数量不能低于20个',
						type: 'warning'
					});
					return
				}
				if (this.taskOriginalPrice < 0.32) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '每单金额不能低于0.32元',
						type: 'warning'
					});
					return
				}
				if (this.openContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请打开方式内容',
						type: 'warning'
					});
					return
				}

				if (this.verifyContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入验证消息',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpTask/saveMyHelpTask'),
					method: 'post',
					data: this.$http.adornData({
						title: this.title,
						content: this.content,
						taskOriginalPrice: this.taskOriginalPrice,
						taskNum: this.taskNum,
						label: this.label,
						helpTaskDetailss: JSON.stringify(this.stepList),
						openType: this.way,
						openContent: this.openContent,
						openApp: '',
						verifyContent: this.verifyContent,
						auditTime: auditTime,
						restrictTime: restrictTime,
						endTimes: endTime,
						userId: this.userId,
						classifyDetailsId: this.id
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
								this.dialogFormVisible = false
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 获取我的派单列表
			taskdataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTask/selectMyHelpTask'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.userId,
						'state': this.state
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data.pageUtils;
						this.tableData2 = returnData
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 接单审核
			safeguardSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTask/selectAuditHelpTask'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.userId,
						'state': this.state
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data.pageUtils;
						this.safeguardData = returnData
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 我的维权
			classifySelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTask/selectHelpMaintainListBySendOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': this.userId,
						'state': 0
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.classData = returnData
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 获取服务分类
			renwuSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpClassify/select'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.renwuData = returnData
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 获取官方邀请码
			guanCode() {
				this.$http({
					url: this.$http.adornUrl('common/type/88'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						let returnData = data.data.value
						this.invitationCode = returnData
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 添加系统用户弹框
			addUser() {
				this.dialogFormVisible1 = true
			},
			// 添加系统用户
			userAtionto() {
				if (this.nickName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入用户名',
						type: 'warning'
					});
					return
				}
				if (this.phone == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入手机号',
						type: 'warning'
					});
					return
				}
				if (this.password == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入密码',
						type: 'warning'
					});
					return
				}
				if (this.invitationCode == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请填写邀请码',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('user/insertSysUser'),
					method: 'post',
					data: this.$http.adornData({
						nickName: this.nickName,
						phone: this.phone,
						invitationCode: this.invitationCode,
						password: this.password,
						imageUrl: this.imageUrl
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
								this.dialogFormVisible1 = false
								this.nickName = ''
								this.phone = ''
								this.invitationCode = ''
								this.password = ''
								this.userSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改系统用户
			Compile(rows) {
				this.dialogFormVisible2 = true;
				this.form.userId = rows.userId;
				this.form.nickName = rows.nickName;
				this.form.phone = rows.phone;
				this.form.password = rows.password;
				this.form.imageUrl = rows.imageUrl;
				this.form.zhifubao = rows.zhifubao;
				this.form.zhifubaoName = rows.zhifubaoName;
			},
			// 修改系统用户确定
			CompileNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('user/updateSysUser'),
					method: 'post',
					data: this.$http.adornData({
						'userId': this.form.userId,
						'nickName': this.form.nickName,
						'phone': this.form.phone,
						'imageUrl': this.form.imageUrl,
						'zhifubao': this.form.zhifubao,
						'zhifubaoName': this.form.zhifubaoName
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.userSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteByUserId/?userId=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data && data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.userSelect()
								}
							})
						} else {
							this.$message.error(data.msg)
						}

					})
				})
			},
			//系统用户列表
			userSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectSysUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.userData = returnData
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			//系统用户列表服务
			userRwSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectSysUserLists'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.userRwData = returnData
					if (this.userRwData.length === 0) {
						this.$message({
							message: '请先添加系统用户',
							type: 'error',
							duration: 1500,
						})
					} else {
						if (this.userId == '') {
							this.userId = this.userRwData[0].userId
						}
					}
				})
			}
		},
		mounted() {
			this.dataSelect()
			this.renwuSelect()
			this.userRwSelect()
		}
	};
</script>

<style lang="scss" scoped>
.system-page {
	padding: 0;
	overflow-x: hidden;
}

.system-page__hero {
	margin-bottom: 12px;
}

.system-page__filter {
	margin-bottom: 12px;
}

.system-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.system-page__field {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.system-page__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #5a6578);
	white-space: nowrap;
}

.system-page__input,
.system-page__select {
	width: 150px;
}

.system-page__actions {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.system-page__tabs-wrap {
	margin-bottom: 0;
}

.system-page__tabs {
	margin-bottom: 0;
}

.system-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.system-page__table-card {
	margin-bottom: 0;
	margin-top: 0;
	padding: 0;
	overflow: hidden;
}

.system-page__table-card >>> .el-table {
	border-radius: 0;
}

.system-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.system-page__table >>> .el-table__body td {
	background: #fff;
}

.system-page__link {
	color: #01af74;
	cursor: pointer;
}

.system-page__status {
	color: #01af74;
}

.system-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
