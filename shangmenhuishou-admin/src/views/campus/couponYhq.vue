<template>
	<div class="coupon-yhq-page">
		<div class="nf-page-hero coupon-yhq-page__hero">
			<span class="nf-page-hero__badge">营销管理</span>
			<h2 class="nf-page-hero__title">优惠券管理</h2>
			<p class="nf-page-hero__desc">管理优惠券创建、编辑、赠送与状态配置，支持新手赠送与免费领取类型</p>
		</div>

		<div class="nf-filter-card coupon-yhq-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body coupon-yhq-page__filter-body">
				<el-button size="mini" type="primary" icon="el-icon-refresh" @click="refresh">刷新</el-button>
			</div>
		</div>

		<div class="nf-table-card coupon-yhq-page__table-card">
			<div class="coupon-yhq-page__toolbar">
				<el-button size="mini" type="primary" :disabled="!isAuth('couponYhq:add')" icon="document"
					@click="addCoupon">添加优惠券</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.records" class="coupon-yhq-page__table">
			<el-table-column prop="couponId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="couponName" label="优惠券名称" width="150">
			</el-table-column>
			<el-table-column prop="couponPicture" label="优惠券图片">
				<template slot-scope="scope">
					<img v-if="scope.row.couponPicture" :src="scope.row.couponPicture" alt="" width="40" height="40">
				</template>
			</el-table-column>
			<el-table-column prop="type" label="优惠券类型">
				<template slot-scope="scope">
					<span v-if="scope.row.couponType==1">新手赠送</span>
					<!-- <span v-if="scope.row.couponType==2">出售</span> -->
					<span v-if="scope.row.couponType==3">免费领取</span>
				</template>
			</el-table-column>
			<el-table-column prop="money" label="优惠券抵扣金额" width="100">
			</el-table-column>
			<!-- <el-table-column prop="buyMoney" label="优惠券购买金额" width="100">
			</el-table-column> -->
			<el-table-column prop="minMoney" label="优惠券最低消费" width="150">
			</el-table-column>
			<el-table-column prop="maxReceive" label="最多领取/购买数量" width="150">
			</el-table-column>
			<el-table-column prop="validDays" label="优惠券有效期限(天)" width="150">
			</el-table-column>
			<el-table-column prop="couponNum" label="优惠券发布总数量" width="150">
			</el-table-column>
			<el-table-column prop="isEnable" label="状态">
				<template slot-scope="scope">
					<span v-if="scope.row.isEnable==1">开启</span>
					<span v-else>关闭</span>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="150">
				<template slot-scope="scope">
					<!-- <el-button size="mini" type="primary" :disabled="!isAuth('couponYhq:issue')"
						@click="discuss(scope.$index,scope.row)" style="margin: 3px;">发布
					</el-button> -->
					<el-button style='margin: 3px;' :disabled="!isAuth('couponYhq:zengsong')" size="mini" type="primary"
						icon="document" @click="songCoupon(scope.row)">赠送优惠券</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('couponYhq:update')"
						@click="shopUpdates(scope.$index,scope.row)" style="margin: 3px;">修改
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('couponYhq:delete')"
						@click="shopdeletes(scope.row)" style="margin: 3px;">删除
					</el-button>
				</template>
			</el-table-column>
			</el-table>
			<div class="coupon-yhq-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
		<!-- 添加优惠券-->
		<el-dialog title="添加优惠券" :visible.sync="dialogFormVisible" center width="80%">
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券类型：</span>
				<el-radio-group v-model="type" @change="couponClick">
					<el-radio :label="1">通用券</el-radio>
					<el-radio :label="2">商品券</el-radio>
				</el-radio-group>
			</div>
			<div v-if="type==2">
				<span style="width: 200px;display: inline-block;text-align: right;">选择商品：</span>
				<div style="position: relative;left: 30%;top: -22px; width:148px;height:148px;font-size: 28px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 143px;cursor: pointer;"
					@click="shopClick()">
					<img v-if="goodsImages" :src="goodsImages" class="avatar" style="width: 138px;height: 138px;" />
					<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
				</div>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
				<el-input style="width:50%;" v-model="couponName" placeholder="请输入优惠券名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券图片：</span>
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
					<el-upload class="avatar-uploader" v-model="couponPicture"
						:action="$http.adornUrl('file/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="couponPicture" :src="couponPicture" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
				<el-radio-group v-model="couponType">
					<el-radio :label="1">新手赠送</el-radio>
					<!-- <el-radio :label="2">出售</el-radio> -->
					<el-radio :label="3">免费领取</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券抵扣金额：</span>
				<el-input style="width:50%;" v-model="money" type="number" :min="0" placeholder="请输入优惠券抵扣金额">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="couponType==2">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券购买金额：</span>
				<el-input style="width:50%;" v-model="buyMoney" type="number" :min="0" placeholder="请输入优惠券购买金额">
				</el-input>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券最低消费：</span>
				<el-input style="width:50%;" v-model="minMoney" type="number" :min="0" placeholder="请输入优惠券最低消费">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">最多领取/购买数量：</span>
				<el-input style="width:50%;" v-model="maxReceive" type="number" :min="0" placeholder="请输入最多领取/购买数量">
				</el-input>
				<span style="color: red;">提示：'0' 为不限数量</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券有效期限(天)：</span>
				<el-input style="width:50%;" v-model="validDays" type="number" :min="1" placeholder="请输入优惠券最低消费">
				</el-input>
				<span style="color: red;">提示：'0' 为永久</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券发布总数量：</span>
				<el-input style="width:50%;" v-model="couponNum" type="number" :min="1" placeholder="请输入优惠券发布总数量">
				</el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序号"></el-input>
			</div> -->

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="isEnable">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="0">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改优惠券 -->
		<el-dialog title="修改优惠券" :visible.sync="dialogFormVisible3" center width="80%">
			<el-form :model="form">
				<el-form-item label="优惠券名称：" :label-width="formLabelWidth">
					<el-input v-model="form.couponName" style="width:65%;" placeholder="请输入优惠券名称"></el-input>
				</el-form-item>
				<el-form-item label="优惠券图片：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="form.couponPicture"
							:action="$http.adornUrl('file/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.couponPicture" :src="form.couponPicture" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="优惠券抵扣金额：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.couponType">
						<el-radio :label="1">新手赠送</el-radio>
						<!-- <el-radio :label="2">出售</el-radio> -->
						<el-radio :label="3">免费领取</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="优惠券抵扣金额：" :label-width="formLabelWidth">
					<el-input v-model="form.money" style="width:65%;" placeholder="请输入优惠券抵扣金额"></el-input>
				</el-form-item>
				<el-form-item label="优惠券购买金额：" :label-width="formLabelWidth" v-if="form.couponType==2">
					<el-input v-model="form.buyMoney" style="width:65%;" placeholder="请输入优惠券购买金额"></el-input>
				</el-form-item>

				<el-form-item label="优惠券最低消费：" :label-width="formLabelWidth">
					<el-input v-model="form.minMoney" style="width:65%;" placeholder="请输入优惠券最低消费"></el-input>
				</el-form-item>
				<el-form-item label="最多领取/购买数量：" :label-width="formLabelWidth">
					<el-input v-model="form.maxReceive" style="width:65%;" placeholder="请输入最多领取/购买数量"></el-input>
					<span style="color: red;">提示： '0' 为不限数量</span>
				</el-form-item>

				<el-form-item label="优惠券有效期限(天)：" :label-width="formLabelWidth">
					<el-input v-model="form.validDays" style="width:65%;" placeholder="请输入优惠券有效期限(天)"></el-input>
					<span style="color: red;">提示：'0' 为永久</span>
				</el-form-item>
				<el-form-item label="优惠券发布总数量：" :label-width="formLabelWidth">
					<el-input v-model="form.couponNum" style="width:65%;" placeholder="请输入优惠券发布总数量"></el-input>
				</el-form-item>

				<el-form-item label="状态：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.isEnable">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="0">关闭</el-radio>
					</el-radio-group>
				</el-form-item>

			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 发布优惠券-->
		<el-dialog title="发布优惠券" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券ID：</span>
				<el-input style="width:50%;" v-model="couponId" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
				<el-input style="width:50%;" v-model="couponName1" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">领取开启时间：</span>
				<el-date-picker style="width:200px;" v-model="startTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">券领结束时间：</span>
				<el-date-picker style="width:200px;" v-model="endTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布数量：</span>
				<el-input style="width:50%;" v-model="issueNumber" type="number" :min="0" placeholder="请输入发布数量">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否限量：</span>
				<el-radio-group v-model="isLimit">
					<el-radio :label="1">限量</el-radio>
					<el-radio :label="2">不限量</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="discussNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth" :visible.sync="dialogFormVisible1" center>
			<div>
				<div style="margin:2% 0;display: inline-block;">
					<span>商品标题：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="shopData.content">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="originalPrice" label="商品原价">
					</el-table-column>
					<el-table-column prop="price" label="商品价格">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价格">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="selectClick(scope.$index,scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="shopData.totalElements">
					</el-pagination>
				</div>
			</div>
		</el-dialog>
		<!-- 赠送优惠券-->
		<el-dialog title="赠送优惠券" :visible.sync="dialogFormVisible0" center>
			<!-- <div>
				<span style="width: 200px;display: inline-block;text-align: right;">赠送类型：</span>
				<el-radio-group v-model="types">
					<el-radio :label="1">指定用户</el-radio>
					<el-radio :label="2">所有用户</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
				<el-tag :key="tag" v-for="(tag,j) in userNameListZ" closable :disable-transitions="false"
					@close="couponIdClose1(tag,j)">
					{{tag}}
				</el-tag>
				<el-button class="button-new-tag" size="small" @click="couponIdBtn1(couponIds)">添加</el-button>
			</div>
			<!-- <div style="margin: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择优惠券：</span>
				<el-tag :key="tag" v-for="(tag,j) in couponIdList" closable :disable-transitions="false"
					@close="couponIdClose(tag,j)">
					{{tag}}
				</el-tag>
				<el-button class="button-new-tag" size="small" @click="couponIdBtn(couponIds)">添加</el-button>
			</div> -->

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible0 = false">取 消</el-button>
				<el-button type="primary" @click="songNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible5" center>
			<div style="display: inline-block;margin-top: 5px;">
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入邀请人邀请码"
					v-model="inviterCode">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
					v-model="phone">
				</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入昵称"
					v-model="nickName">
				</el-input>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;margin-top: 5px;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans1">重置
				</el-button>
			</div>
			<el-button style="margin-left:15px;" :disabled="userIdList.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes1()">确定</el-button>
			<el-table v-loading="tableDataLoading1" @selection-change="changeFun1" :data="userData.list"
				:row-key="(row)=>{ return row.userId}" tooltip-effect="dark" ref="multipleTable">
				<el-table-column :reserve-selection="true" type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="userId" label="id" width="80">
				</el-table-column>
				<el-table-column fixed prop="userName" label="昵称" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;"
							@click="updates(scope.row)">{{ scope.row.userName ? scope.row.userName : '未设置' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
					<template slot-scope="scope">
						<span style="color: #01af74;cursor: pointer;" @click="updates(scope.row)">
							{{ scope.row.phone ? scope.row.phone : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="inviterCode" label="邀请人邀请码">
					<template slot-scope="scope">
						<span>{{ scope.row.inviterCode ? scope.row.inviterCode : '-' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="member" label="是否是会员">
					<template slot-scope="scope">
						<span v-if="scope.row.isVip==1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
		<!-- 优惠券列表 -->
		<el-dialog title="优惠券列表" :visible.sync="dialogFormVisible4" center>
			<el-button style="margin-left:15px;" :disabled="couponIds.length <= 0 " size="mini" type="primary"
				icon="document" @click="closes2()">确定</el-button>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData.content"
				:row-key="(row)=>{ return row.couponId}">
				<el-table-column :reserve-selection="true" type="selection" fixed>
				</el-table-column>
				<el-table-column prop="couponName" label="优惠券名称" width="150">
				</el-table-column>
				<el-table-column prop="type" label="优惠券类型" width="100">
					<template slot-scope="scope">
						<span v-if="scope.row.type==1">通用券</span>
						<span style="color: #01af74;" v-if="scope.row.type==2">商品券</span>
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

				<el-table-column prop="failureTime" label="优惠券有效期限" width="150">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column label="状态">
					<template slot-scope="scope">
						<span style="color: #01af74;" v-if="scope.row.status == 1">开启</span>
						<span v-if="scope.row.status == 2">关闭</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				type: 1,
				title: '',
				typeId: '',
				couponName: '',
				couponPicture: '',
				money: '',
				buyMoney: '',
				minMoney: '',
				maxReceive: '',
				validDays: 1,
				couponNum: 1,
				sort: 0,
				status: 1,
				goodsIds: '',
				goodsImages: '',
				issueNumber: 0,
				remainNumber: '',
				isLimit: 1,
				form: {
					couponId: '',
					couponName: '',
					money: '',
					buyMoney: '',
					minMoney: '',
					validDays: '',
					couponNum: '',
					couponPicture: '',
					maxReceive: '',
					isEnable: 1,
					couponType: ''
				},
				coverImg: '',
				couponName1: '',
				couponId: '',
				startTime: '',
				endTime: '',
				checkBoxData: [], //多选框选择的值
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: [],
				shopData: [],
				// 赠送优惠券
				tableDataLoading1: false,
				dialogFormVisible0: false,
				dialogFormVisible5: false,
				dialogFormVisible4: false,
				couponIds: [],
				couponIdList: [],
				userIdList: [],
				userNameList: [],
				isCoupon: 1,
				userData: [],
				types: '',
				phone: '',
				nickName: '',
				member: -1,
				memberList: [{
						value: -1,
						title: '全部'
					},
					{
						value: 0,
						title: '否'
					},
					{
						value: 1,
						title: '是'
					},
				],
				inviterCode: '',
				couponType: '',
				isEnable: 1,
				userIdListZ: [],
				userNameListZ: [],
			}
		},

		methods: {
			// 通用券/商品券
			couponClick(val) {
				if (val = 1) {
					this.goodsIds = ''
					this.goodsImages = ''
					this.form.goodsIds = ''
					this.form.goodsImages = ''
				}
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.shopSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelect1()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelect1()
			},
			// 添加优惠券弹框
			addCoupon() {
				this.couponName = ''
				this.couponPicture = ''
				this.money = ''
				this.buyMoney = ''
				this.validDays = 1
				this.couponNum = ''
				this.minMoney = ''
				this.maxReceive = ''
				this.dialogFormVisible = true
			},
			handleAvatarSuccess1(file, fileList) {
				this.couponPicture = file.data.path
			},
			handleAvatarSuccess2(file, fileList) {
				this.form.couponPicture = file.data.path
			},
			// 添加优惠券
			couponNoticeTo() {
				if (this.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券名称',
						type: 'warning'
					});
					return
				}
				if (this.couponPicture == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (this.couponType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}

				if (Number(this.minMoney) < Number(this.money)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '优惠券金额不能高于最低消费金额',
						type: 'warning'
					});
					return
				}
				// if (this.buyMoney === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入优惠券购买金额',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				if (this.maxReceive === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入最多领取/购买数量',
						type: 'warning'
					});
					return
				}
				if (Number(this.minMoney) == Number(this.money)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '优惠券金额不能高于最低消费金额',
						type: 'warning'
					});
					return
				}
				if (this.couponNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券发布总数量',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/coupon/addCoupon'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'couponName': this.couponName,
						'couponPicture': this.couponPicture,
						'money': this.money,
						'minMoney': this.minMoney,
						'validDays': this.validDays,
						'couponNum': this.couponNum,
						'buyMoney': this.buyMoney,
						'isEnable': this.isEnable,
						'maxReceive': this.maxReceive,
						'couponType': this.couponType
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible = false
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 修改优惠券
			shopUpdates(index, row) {
				this.dialogFormVisible3 = true
				this.form.couponId = row.couponId
				this.form.couponName = row.couponName
				this.form.money = row.money
				this.form.buyMoney = row.buyMoney
				this.form.minMoney = row.minMoney
				this.form.maxReceive = row.maxReceive
				this.form.validDays = row.validDays
				this.form.couponNum = row.couponNum
				this.form.couponPicture = row.couponPicture
				this.form.isEnable = row.isEnable
				this.form.couponType = row.couponType
			},
			amendNoticeTo() {
				if (this.form.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券名称',
						type: 'warning'
					});
					return
				}
				if (this.form.couponPicture == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (Number(this.form.minMoney) < Number(this.form.money)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '优惠券金额不能高于最低消费金额',
						type: 'warning'
					});
					return
				}
				if (this.form.maxReceive === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入最多领取/购买数量',
						type: 'warning'
					});
					return
				}
				// if (this.form.buyMoney === '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入优惠券购买金额',
				// 		type: 'warning'
				// 	});
				// 	return
				// }

				if (Number(this.form.minMoney) == Number(this.form.money)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '优惠券金额不能高于最低消费金额',
						type: 'warning'
					});
					return
				}
				if (this.form.couponNum === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券发布总数量',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/coupon/updateCoupon'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'couponId': this.form.couponId,
						'couponName': this.form.couponName,
						'money': this.form.money,
						'buyMoney': this.form.buyMoney,
						'minMoney': this.form.minMoney,
						'maxReceive': this.form.maxReceive,
						'validDays': this.form.validDays,
						'couponNum': this.form.couponNum,
						'couponPicture': this.form.couponPicture,
						'isEnable': this.form.isEnable,
						'couponType': this.form.couponType
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible3 = false
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 发布优惠券弹框
			discuss(index, row) {
				this.dialogFormVisible2 = true
				this.couponId = row.couponId
				this.couponName1 = row.couponName
				this.goodsIds = row.goodsIds
				this.type = row.type
			},
			// 发布优惠券
			discussNoticeTo() {
				if (this.startTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择优惠券领取时间',
						type: 'warning'
					});
					return
				}
				if (this.endTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择优惠券结束时间',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('selfCouponIssue/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName1,
						'couponId': this.couponId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'issueNumber': this.issueNumber,
						'remainNumber': this.issueNumber,
						'isLimit': this.isLimit,
						'status': this.status,
						'type': this.type,
						'goodsIds': this.goodsIds
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.startTime = ''
							this.endTime = ''
							this.isLimit = 1
							this.status = 1
							this.issueNumber = ''
							this.dataSelect()
						}
					})
					this.dialogFormVisible2 = false
				})
			},
			// 删除优惠券
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/coupon/deleteCoupon'),
						method: 'post',
						params: this.$http.adornParams({
							'couponId': row.couponId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			//获取优惠券数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/coupon/getCouponPageList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			//搜索
			select() {
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.shopSelect()
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
				this.ids.splice(index, 1)
			},
			// 选择商品
			shopClick() {
				this.dialogFormVisible1 = true
				this.shopSelect()
			},
			// 获取商品数据
			shopSelect() {
				let page = this.page - 1
				let status = 0
				this.title = ''
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.shopData = returnData
				})
			},
			// 选择商品
			selectClick(index, row) {
				console.log(row)
				this.$http({
					url: this.$http.adornUrl('goods/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': row.id
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					let returnData = data.data;
					this.goodsIds = returnData.id
					this.goodsImages = returnData.coverImg
					this.form.goodsIds = returnData.id
					this.form.goodsImages = returnData.coverImg
				})
			},
			// 赠送优惠券
			songCoupon(row) {
				this.userIdListZ = []
				this.userNameListZ = []
				this.userIdList = []
				this.couponIds = row.couponId
				this.userNameList = []
				this.couponIdList = []
				this.types = ''
				this.dialogFormVisible0 = true
				console.log('```````', this.userIdList)
			},
			// 赠送优惠券
			songNoticeTo() {

				// if (this.types == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择赠送类型',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.types == 1) {
				if (this.userIdListZ.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					});
					return
				}
				// }

				// if (this.couponIds.length == 0) {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择卡券',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				var userIdList = this.userIdListZ.toString()
				// var couponIds = this.couponIds.toString()
				this.$http({
					url: this.$http.adornUrl('admin/coupon/giveCoupon'),
					method: 'post',
					params: this.$http.adornParams({
						'userIds': userIdList,
						'couponId': this.couponIds
					})
				}).then(({
					data
				}) => {
					if (data.code == 0 || data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.userIdList = []
								this.couponIds = []
								this.userNameList = []
								this.couponIdList = []
								this.types = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible0 = false
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},

			couponIdClose2(index, ietm) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 获取用户数据列表
			dataSelect1() {
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						// 'userType': this.userType,
						'phone': this.phone,
						'userName': this.nickName,
						'inviterCode': this.inviterCode,
						'platform': '',
						'member': this.member
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			select1() {
				this.page1 = 1
				this.dataSelect1()
			},
			// 重置
			cleans1() {
				this.page1 = 1
				this.phone = ''
				this.member = -1
				this.nickName = ''
				this.inviterCode = ''
				this.dataSelect1()
			},
			// 确定选中
			queding(row) {
				this.userName = row.userName
				this.userId = row.userId
				this.dialogFormVisible2 = false
			},
			// 打开用户列表
			couponIdBtn1(rows) {
				this.phone = ''
				this.nickName = ''
				this.inviterCode = ''
				this.page1 = 1
				this.dataSelect1()
				
				
				console.log('this.userIdListZ', this.userIdListZ)
				this.dialogFormVisible5 = true
				// if (this.userIdListZ.length == 0) {
					this.toggleSelection()
				// }
			},
			// 打开优惠券列表
			couponIdBtn(rows) {
				this.dataSelect()
				this.dialogFormVisible4 = true
			},
			// 批量开启
			closes1(id) {
				this.userIdListZ = this.userIdList
				this.userNameListZ = this.userNameList
				this.dialogFormVisible5 = false
			},
			// 批量开启
			closes2(id) {
				this.isCoupon = 1
				this.dialogFormVisible4 = false
				this.dialogFormVisible5 = false
			},
			// 删除优惠券
			couponIdClose(index, ietm) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 删除用户
			couponIdClose1(ietm,index) {
				console.log('index', index, 'ietm', ietm, this.userIdListZ.length)
				if (this.userIdListZ.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一个用户',
						type: 'warning'
					});
					return
				} else {
					this.userIdListZ.splice(index, 1)
					this.userNameListZ.splice(index, 1)
				}
			},
			couponIdClose2(index, ietm) {
				console.log('index', index, 'ietm', ietm, this.couponIdList.length)
				if (this.couponIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一张优惠券',
						type: 'warning'
					});
					return
				} else {
					this.couponIdList.splice(index, 1)
					this.couponIds.splice(index, 1)
				}
			},
			// 多选用户
			changeFun1(val) {
				console.log('多选', val)
				var userIdList = []
				var userNameList = []
				for (var i in val) {
					userIdList.push(val[i].userId)
					userNameList.push(val[i].userName)
				}
				this.userIdList = userIdList
				this.userNameList = userNameList
			},
			// 多选券
			changeFun(val) {
				console.log('多选', val)
				var couponIds = []
				var couponIdList = []
				for (var i in val) {
					couponIds.push(val[i].couponId)
					couponIdList.push(val[i].couponName)
				}
				this.couponIds = couponIds
				this.couponIdList = couponIdList
			},
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			toggleSelection(rows) {
				console.log('this.$refs.multipleTable', this.$refs, '········', rows)
				if (rows) {
					rows.forEach(row => {
						this.$refs.multipleTable.toggleRowSelection(row);
					});
				} else {
					if (this.$refs.multipleTable) {
						this.$refs.multipleTable.clearSelection();
					}
				}
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style lang="scss" scoped>
.coupon-yhq-page {
	padding: 0;
	overflow-x: hidden;
}

.coupon-yhq-page__hero {
	margin-bottom: 12px;
}

.coupon-yhq-page__filter {
	margin-bottom: 12px;
}

.coupon-yhq-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	gap: 12px;
	align-items: center;
}

.coupon-yhq-page__table-card {
	margin-bottom: 0;
	padding: 0;
	overflow: hidden;
}

.coupon-yhq-page__table-card >>> .el-table {
	border-radius: 0;
}

.coupon-yhq-page__toolbar {
	display: flex;
	justify-content: flex-end;
	padding: 12px 16px;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}

.coupon-yhq-page__table >>> .el-table__header th {
	background: var(--nf-page-bg, #f4f6fa);
	color: var(--nf-text-primary, #1a2433);
	font-weight: 600;
}

.coupon-yhq-page__table >>> .el-table__body td {
	background: #fff;
}

.coupon-yhq-page__pagination {
	display: flex;
	justify-content: center;
	padding: 12px 16px;
	border-top: 1px solid var(--nf-border, #e8eaed);
	background: #fff;
}
</style>
