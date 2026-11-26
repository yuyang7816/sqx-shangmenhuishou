<template>
  <div class="shop-admin-page">
    <div class="shop-admin-page__nav">
      <a href="#" class="shop-admin-page__back" @click.prevent="prev">
        <icon-svg name="jiantou" class="shop-admin-page__back-icon"></icon-svg>
        返回
      </a>
      <span class="shop-admin-page__nav-sep">|</span>
      <span class="shop-admin-page__nav-title">商品列表</span>
    </div>

    <div class="nf-page-hero shop-admin-page__hero">
      <span class="nf-page-hero__badge">商铺商品管理</span>
      <h2 class="nf-page-hero__title">商品列表</h2>
      <p class="nf-page-hero__desc">管理商铺下的商品、分类与规格，支持按名称与类型筛选，可上下架、改库存及批量操作</p>
    </div>

    <div class="nf-filter-card shop-admin-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body shop-admin-page__filter-body">
        <div class="shop-admin-page__field">
          <span class="shop-admin-page__label">商铺名称：</span>
          <el-input class="shop-admin-page__input" @keydown.enter.native="shopSelect" placeholder="请输入商铺名称"
            v-model="shopName">
          </el-input>
        </div>
        <div class="shop-admin-page__field">
          <span class="shop-admin-page__label">商品名称：</span>
          <el-input class="shop-admin-page__input" @keydown.enter.native="select" clearable placeholder="请输入商品名称"
            v-model="title">
          </el-input>
        </div>
        <div class="shop-admin-page__field">
          <span class="shop-admin-page__label">商品类型：</span>
          <el-select v-model="classifyId" class="shop-admin-page__select" placeholder="请选择商品类型"
            @change="handleChange()">
            <el-option v-for="(item,index) in classDatas" :key="index" :label="item.classifyName"
              :value="item.classifyId">
            </el-option>
          </el-select>
        </div>
        <div class="shop-admin-page__field">
          <span class="shop-admin-page__label">商品状态：</span>
          <el-select v-model="status" class="shop-admin-page__select-sm" placeholder="请选择商品状态"
            @change="select(status)">
            <el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="shop-admin-page__actions">
          <el-button size="mini" type="primary" icon="el-icon-search" @click="select">查询</el-button>
          <el-button size="mini" type="primary" icon="el-icon-refresh-left" @click="cleans">重置</el-button>
          <el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary" icon="el-icon-plus"
            @click="addShop1">新增商品</el-button>
          <el-button size="mini" type="primary" icon="el-icon-sort" @click="shanxiajia()"
            :disabled="checkBoxData.length <= 0">批量上下架</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="deletesPl()"
            :disabled="checkBoxData.length <= 0">批量删除</el-button>
          <el-button size="mini" type="primary" icon="el-icon-edit-outline" @click="kiucun()"
            :disabled="checkBoxData.length <= 0">批量改库存</el-button>
        </div>
      </div>
    </div>

    <div class="shop-admin-page__tabs-wrap">
      <el-tabs v-model="activeName" class="shop-admin-page__tabs" @tab-click="handleClick">
      <el-tab-pane label="商品列表" name="first">
        <div class="nf-table-card shop-admin-page__table-card">
        <el-table v-loading="tableDataLoading" :data="tableData.list" class="shop-admin-page__table"
          @selection-change="changeFun">
          <el-table-column type="selection">
          </el-table-column>
          <el-table-column fixed prop="goodsId" label="编号" width="80">
          </el-table-column>
          <el-table-column fixed prop="goodsCover" label="商品封面图">
            <template slot-scope="scope">
              <img :src="scope.row.goodsCover" alt="" width="60" height="60">
            </template>
          </el-table-column>
          <el-table-column prop="goodsCover" label="商品图" width="150">
            <template slot-scope="scope">
              <!-- <img :src="scope.row.goodsCover" alt="" width="60" height="60"> -->
              <div v-for="(item,index) in scope.row.goodsPicture" :key="index"
                style="display: inline-block; margin: 3px;">
                <el-popover placement="top-start" title="" trigger="hover">
                  <img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
                  <img style="width: 300px; height: auto" :src="item" alt="">
                </el-popover>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="商品类型">
            <template slot-scope="scope">
              <span v-if="scope.row.classifyName == null">暂无分类</span>
              <span v-else>{{scope.row.classifyName}}</span>
            </template>
          </el-table-column>
          <el-table-column fixed prop="goodsName" label="商品标题" width="200">
          </el-table-column>
          <el-table-column prop="goodsDescribe" label="商品描述" width="300">
          </el-table-column>
          <el-table-column prop="originalMoney" label="商品原价">
            <template slot-scope="scope">
              <span>{{scope.row.originalMoney | numFilter}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="inventory" label="库存" width="180">
            <template slot-scope="scope">
              <span>{{scope.row.inventory}}</span>
              <div style="position: relative;display: inline-block;">
                <el-input style="width: 160px;" @keydown.enter.native="kcselect(scope.row,scope.row.kucun)" clearable
                  placeholder="请输入修改库存" v-model="scope.row.kucun">
                </el-input>
                <span @click="kcselect(scope.row,scope.row.kucun)" style="position: absolute;right: 18px;top:8px;">
                  <icon-svg name="bianji" class="site-sidebar__menu-icon"></icon-svg>
                </span>
              </div>

            </template>
          </el-table-column>
          <el-table-column prop="goodsMoney" label="商品售价">
            <template slot-scope="scope">
              <span>{{scope.row.goodsMoney | numFilter}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="packMoney" label="打包费">
            <template slot-scope="scope">
              <span>{{scope.row.packMoney | numFilter}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsLabel" label="标签">
          </el-table-column>
          <el-table-column prop="salesVolume" label="商品销量">
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="150">
            <template slot-scope="scope">
              <el-input-number size="small" v-model="scope.row.sort" :min="0" @change="paixu(scope.row)">
              </el-input-number>
            </template>
          </el-table-column>
          <el-table-column prop="gsrStatus" label="库存状态" width="80">
            <template slot-scope="scope">
              <span v-if="scope.row.gsrStatus === 2 ">已售完</span>
              <span v-else>剩余：{{scope.row.inventory}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="商品状态">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.goodsId)"
                :active-value="openValue" :inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
              </el-switch>

            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="180">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:update')"
                @click="shopUpdates(scope.$index, scope.row)">修改
              </el-button>
              <el-button size="mini" type="danger" :disabled="!isAuth('shopAdmin:delete')"
                @click="shopdeletes(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="shop-admin-page__pagination">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
            layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
          </el-pagination>
        </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="商品分类" name="second">
        <div class="nf-table-card shop-admin-page__table-card">
        <div class="shop-admin-page__toolbar">
          <el-button :disabled="!isAuth('shopsList:add')" icon="el-icon-plus" size="mini" type="primary"
            @click="addNotice">添加分类</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="typeDatas.records" row-key="id" class="shop-admin-page__table">
          <el-table-column label="编号" prop="classifyId">
          </el-table-column>
          <el-table-column label="名称" prop="classifyName">
          </el-table-column>
          <el-table-column prop="sort" label="排序">
            <template slot-scope="scope">
              <el-input-number size="small" v-model="scope.row.sort" :min="0" @change="paixuS(scope.row)">
              </el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button :disabled="!isAuth('shopsList:update')" size="mini" type="primary"
                @click="updates(scope.$index, scope.row)">修改
              </el-button>
              <el-button :disabled="!isAuth('shopsList:delete')" size="mini" type="danger" @click="deletes(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="shop-admin-page__pagination">
          <el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
            :page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
            layout="total,sizes, prev, pager, next,jumper" :total="typeDatas.total">
          </el-pagination>
        </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="商品规格" name="third">
        <div class="nf-table-card shop-admin-page__table-card">
        <div class="shop-admin-page__toolbar">
          <el-button :disabled="!isAuth('shopsList:add')" size="mini" type="primary" icon="el-icon-plus"
            @click="addNotice1">添加规格</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="typeDatas1.records" row-key="id" class="shop-admin-page__table">
          <el-table-column prop="id" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="ruleName" label="规格名称">
          </el-table-column>
          <el-table-column label="规格值">
            <template slot-scope="scope">
              <div v-for="(item,index) in scope.row.ruleValue" :key="index">
                <div>
                  <span>{{ item.value }}:</span>
                  <span>{{ item.detail }}</span>
                </div>
              </div>

            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间">
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('shopsList:update')"
                @click="updates1(scope.row)">
                修改
              </el-button>
              <el-button size="mini" type="danger" :disabled="!isAuth('shopsList:delete')" @click="deletes1(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="shop-admin-page__pagination">
          <el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
            :page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
            layout="total,sizes, prev, pager, next,jumper" :total="typeDatas1.total">
          </el-pagination>
        </div>
        </div>
      </el-tab-pane>
      </el-tabs>
    </div>

      <!-- 已有商品列表 -->
      <el-dialog :visible.sync="dialogFormVisible" center title="商品列表">
        <div style="margin:2% 0;display: inline-block;">
          <span>商品标题：</span>
          <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
            v-model="title1">
          </el-input>&nbsp;&nbsp;&nbsp;&nbsp;
          <!-- <span>商品类型：</span>
        <el-select v-model="classifyId" class="margin15" placeholder="请选择商品类型" style="width:200px;"
          @change="handleChange()">
          <el-option v-for="(item,index) in classDatas" :key="index" :label="item.classifyName"
            :value="item.classifyId">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>商品状态：</span>
        <el-select v-model="status" placeholder="请选择商品状态" style="width:150px;" @change="select(status)">
          <el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;&nbsp; -->
        </div>
        <div style="display: inline-block;">
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询
          </el-button>
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置
          </el-button>
          <el-button style='margin-left:15px;' :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary"
            icon="document" @click="add(1)">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading1" :data="tableData1.list" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column fixed prop="goodsId" label="编号" width="80">
          </el-table-column>
          <el-table-column fixed prop="goodsName" label="商品标题" width="200">
          </el-table-column>
          <el-table-column prop="goodsCover" label="商品封面图">
            <template slot-scope="scope">
              <img :src="scope.row.goodsCover" alt="" width="60" height="60">
            </template>
          </el-table-column>
          <el-table-column prop="goodsCover" label="商品图" width="150">
            <template slot-scope="scope">
              <!-- <img :src="scope.row.goodsCover" alt="" width="60" height="60"> -->
              <div v-for="(item,index) in scope.row.goodsPicture" :key="index"
                style="display: inline-block; margin: 3px;">
                <el-popover placement="top-start" title="" trigger="hover">
                  <img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
                  <img style="width: 300px; height: auto" :src="item" alt="">
                </el-popover>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="商品类型">
            <template slot-scope="scope">
              <span v-if="scope.row.classifyName == null">暂无分类</span>
              <span v-else>{{scope.row.classifyName}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsDescribe" label="商品描述" width="300">
          </el-table-column>
          <el-table-column prop="originalMoney" label="商品原价">
            <template slot-scope="scope">
              <span>{{scope.row.originalMoney | numFilter}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsMoney" label="商品售价">
            <template slot-scope="scope">
              <span>{{scope.row.goodsMoney | numFilter}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="packMoney" label="打包费">
            <template slot-scope="scope">
              <span>{{scope.row.packMoney | numFilter}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="goodsLabel" label="标签">
          </el-table-column>
          <el-table-column prop="salesVolume" label="商品销量">
          </el-table-column>
          <el-table-column prop="status" label="商品状态">
            <template slot-scope="scope">
              <span style="color: #01af74;cursor: pointer;" v-if="scope.row.status === 0 ">上架</span>
              <span style="color: #01af74;cursor: pointer;" v-if="scope.row.status === 1 ">下架</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:add')" @click="add(scope.row)">添加
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
            :page-sizes="[10, 20, 30,50,100]" :page-size="size1" :current-page="page1"
            layout="total,sizes, prev, pager, next,jumper" :total="tableData1.totalCount">
          </el-pagination>
        </div>
      </el-dialog>
      <!-- 添加/修改分类 -->
      <el-dialog :visible.sync="dialogFormVisible1" center :title="titles">
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
          <el-input v-model="classifyName" placeholder="请输入分类名称" style="width:50%;"></el-input>
        </div>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">分类排序：</span>
          <el-input v-model="sort" placeholder="请输入分类排序" style="width:50%;"></el-input>
        </div>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 修改规格 -->
      <el-dialog title="修改规格" customClass="customWidth" :visible.sync="dialogFormVisible2">
        <el-form :model="form">
          <el-form-item label="规格标题：">
            <el-input v-model="form.ruleName" style="width:200px;" placeholder="请输入分类名称"></el-input>
          </el-form-item>
          <el-form-item>
            <div style="margin-left:73px;">
              <div class="ruleitem" v-for="(item,index) in form.ruleValue" :key="index">
                <div>{{ item.value }} <span @click="deleterule1(index)">
                    <icon-svg name="schu" class="ruleicon"></icon-svg>
                  </span></div>
                <div>
                  <el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable :disable-transitions="false"
                    @close="handleClose1(tag,j,item)">
                    {{ tag }}
                  </el-tag>
                  <el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
                    style="width:120px;" placeholder="请输入属性名" @keyup.enter.native="handleInputConfirm1(index,item)">
                  </el-input>
                  <el-button class="button-new-tag" size="small" @click="handleInputConfirm(index,item)">添加</el-button>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <div style="margin-left:83px;">
              <div v-if="isshow" class="btn_specif">
                <el-button type="primary" @click="btnToclick1()">添加新规格</el-button>
              </div>
            </div>
            <div style="margin-top:10px;">
              <div v-if="!isshow" style="margin-top: 20px;" :visible.sync="dialogFormVisible">
                <div style="display: inline-block;">
                  <span style="width: 100px;display: inline-block;text-align: right;">规格：</span>
                  <el-input style="width:50%;" v-model="releobject.value" placeholder="请输入规格"></el-input>
                </div>
                <div style="display: inline-block;">
                  <span style="width: 100px;display: inline-block;text-align: right;">规格值：</span>
                  <el-input style="width:50%;" v-model="releobject.detail" placeholder="请输入规格值"></el-input>
                </div>
                <div style="display: inline-block;">
                  <el-button type="primary" @click="speciTo1()">确 定</el-button>
                  <el-button @click="speciTotwo1()">取 消</el-button>
                </div>
              </div>
            </div>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 添加规格 -->
      <el-dialog title="添加规格" customClass="customWidth" :visible.sync="dialogFormVisible3">
        <div style="margin-bottom: 10px;">
          <span style="width: 80px;display: inline-block;text-align: right;">规格标题：</span>
          <el-input style="width:200px" v-model="ruleName" placeholder="请输入标题名称 例:衣服"></el-input>
        </div>
        <div style="margin:40px 0 0 73px;">
          <div class="ruleitem" v-for="(item,index) in this.ruleValue" :key="index">
            <div>{{ item.value }} <span @click="deleterule(index)">
                <icon-svg name="schu" class="ruleicon"></icon-svg>
              </span></div>
            <div>
              <el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable :disable-transitions="false"
                @close="handleClose(tag,j,item)">
                {{ tag }}
              </el-tag>
              <el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
                style="width:120px;" placeholder="请输入属性名" @keyup.enter.native="handleInputConfirm(index,item)">
              </el-input>
              <el-button class="button-new-tag" size="small" @click="handleInputConfirm(index,item)">添加</el-button>
            </div>
          </div>
        </div>
        <div style="margin:30px 0 0 83px;">
          <div v-if="isshow" class="btn_specif">
            <el-button type="primary" @click="btnToclick()">添加新规格</el-button>
          </div>
        </div>
        <div style="margin:10px 0 0 0px;">
          <div v-if="!isshow" style="margin-top: 20px;" :visible.sync="dialogFormVisible">
            <div style="display: inline-block;">
              <span style="width: 80px;display: inline-block;text-align: right;">规格：</span>
              <el-input style="width:50%;" v-model="releobject.value" placeholder="例:颜色"></el-input>
            </div>
            <div style="display: inline-block;">
              <span style="width: 80px;display: inline-block;text-align: right;">规格值：</span>
              <el-input style="width:50%;" v-model="releobject.detail" placeholder="例:黑色"></el-input>
            </div>
            <div style="display: inline-block;">
              <el-button type="primary" @click="speciTo()">确 定</el-button>
              <el-button @click="speciTotwo()">取 消</el-button>
            </div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible3 = false">取 消</el-button>
          <el-button type="primary" @click="specificationTo()">确 定</el-button>
        </div>
      </el-dialog>

      <!-- 批量上下架 -->
      <el-dialog title="批量上下架" customClass="customWidth" center :visible.sync="dialogFormVisibleSxj">
        <div style="text-align: center;">
          <span style="display: inline-block;text-align: right;">状态：</span>
          <el-radio-group v-model="guigeType">
            <el-radio :label="0">上架</el-radio>
            <el-radio :label="1">下架</el-radio>
          </el-radio-group>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisibleSxj = false">取 消</el-button>
          <el-button type="primary" @click="transferClcik()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 批量改库存 -->
      <el-dialog title="批量修改库存" customClass="customWidth" center :visible.sync="dialogFormVisibleKc">
        <div style="text-align: center;">
          <span style="display: inline-block;text-align: right;">库存：</span>
          <el-input style="width:50%;" v-model="num" placeholder="请输入库存"></el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisibleKc = false">取 消</el-button>
          <el-button type="primary" @click="transferClcikKc()">确 定</el-button>
        </div>
      </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        activeName: 'first',
        openValue: 0,
        closeValue: 1,
        size: 10,
        page: 1,
        size1: 10,
        page1: 1,
        pagelist: [],
        id: '',
        img: '',
        title: '',
        title1: '',
        type: '',
        typeId: '',
        readonly: true,
        merchants: '',
        price: '',
        memberPrice: '',
        originalPrice: '',
        sales: '',
        status: '',
        status2: '',
        descrition: '',
        totalnum: 0,
        imgUrl: '',
        linkUrl: '',
        classifyId: '',
        classDatas: [],
        statusmain: [{
            value: '',
            label: '全部'
          },
          {
            value: 0,
            label: '上架'
          },
          {
            value: 1,
            label: '下架'
          }
        ],
        statusmain2: [{
            value: 1,
            label: '上架'
          },
          {
            value: 2,
            label: '下架'
          }
        ],
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible: false,
        tableDataLoading1: false,

        advertData: [],
        tableData: [],
        tableData1: [],
        shopId: '',
        xuanze: [],
        kucun: '',
        // 分类
        classifyId: '',
        classifyName: '',
        tableDataLoading: false,
        dialogFormVisible1: false,
        typeDatas: {},
        titles: '添加',
        // 规格
        ruleName: '',
        releobject: {
          value: '',
          detail: '',
        },
        detailarr: [],
        inputVisible: false,
        inputValues: {
          inputValue0: '',
          inputValue1: '',
          inputValue2: '',
          inputValue3: '',
          inputValue4: '',
          inputValue5: ''
        },
        ruleValue: [],
        form: {
          id: '',
          ruleName: '',
          ruleValue: [],
          createTime: ''

        },
        isshow: true,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        typeDatas1: {},
        shopName: '',

        checkBoxData: [], //多选框选择的值
        dialogFormVisibleSxj: false,
        guigeType: '',
        num: '',
        dialogFormVisibleKc: false,
        openValue: 0,
        closeValue: 1,
        sort:0,

      }
    },
    filters: {
      numFilter(value) {
        let realVal = ''
        if (!isNaN(value) && value !== '') {
          // 截取当前数据到小数点后三位
          let tempVal = parseFloat(value).toFixed(3)
          realVal = tempVal.substring(0, tempVal.length - 1)
        } else {
          realVal = '--'
        }
        return realVal
      }
    },
    methods: {
      // tabs切换
      handleClick(tab, event) {
        if (tab._props.label == '商品列表') {
          this.page = 1
          this.limit = 5
          this.shopSelect()
        }
        if (tab._props.label == '商品分类') {
          this.page = 1
          this.limit = 5
          this.dataSelect()
        }
        if (tab._props.label == '商品规格') {
          this.page = 1
          this.limit = 5
          this.dataSelect1()
        }
      },
      handleChange(value) {
        this.shopSelect()
      },

      handleAvatarSuccess(file) {
        this.img = file.data.path;
      },
      handleSizeChange(val) {
        this.size = val;
        this.shopSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.shopSelect()
      },
      handleSizeChange1(val) {
        this.size1 = val;
        this.shopSelect1()
      },
      handleCurrentChange1(val) {
        this.page1 = val;
        this.shopSelect1()
      },
      handleSizeChange2(val) {
        this.size = val;
        this.dataSelect()
      },
      handleCurrentChange2(val) {
        this.page = val;
        this.dataSelect()
      },
      handleSizeChange3(val) {
        this.size = val;
        this.dataSelect1()
      },
      handleCurrentChange3(val) {
        this.page = val;
        this.dataSelect1()
      },
      //搜索
      select() {
        this.page = 1
        this.shopSelect()
      },
      // 重置
      cleans() {
        this.page = 1
        this.title = ''
        this.status = ''
        this.classifyId = ''
        this.shopName = ''
        this.shopSelect()
      },
      //搜索
      select1() {
        this.shopSelect1()
      },
      // 重置
      cleans1() {
        this.page = 1
        this.title1 = ''
        this.shopSelect1()
      },
      // 新增商品
      addShop1() {
        this.$router.push({
          path: '/shopPublish?shopId=' + this.shopId
        })
      },
      //添加商品
      addShop() {
        this.shopSelect1()
        this.dialogFormVisible = true
      },
      // 修改库存
      kcselect(row, kucun) {
        console.log('修改库存', row, kucun)
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/updateGoodsByShopId'),
          method: 'post',
          data: this.$http.adornData({
            'goodsId': row.goodsId,
            'shopId': this.shopId,
            'inventory': kucun
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '修改成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.shopSelect()
              }
            })
            this.xuanze = []
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
      // 选择商品
      handleSelectionChange(val) {
        console.log('选择商品', val)
        var arr = []

        if (val.length > 0) {
          for (var i in val) {
            var data = {}
            data.shopId = this.shopId
            data.goodsId = val[i].goodsId
            arr.push(data)
          }

        }
        this.xuanze = arr
      },
      add(row) {
        var goodsShopRelevancyList = []
        var datas = {}
        if (row == 1) {

          goodsShopRelevancyList = this.xuanze
          console.log('多个添加商品', goodsShopRelevancyList)
        } else {
          datas.shopId = this.shopId
          datas.goodsId = row.goodsId
          goodsShopRelevancyList.push(datas)
          console.log('单个添加商品', goodsShopRelevancyList)

        }
        if (goodsShopRelevancyList.length == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择商品',
            type: 'warning'
          });
          return
        }
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/addGoodsByShopId'),
          method: 'post',
          data: this.$http.adornData(
            goodsShopRelevancyList)
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '添加成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.shopSelect()
              }
            })
            this.xuanze = []
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
      // 修改商品
      shopUpdates(index, rows) {
        this.$router.push({
          path: '/shopAmend',
          query: {
            goodsId: rows.goodsId
          }
        });
      },
      // 删除商品
      shopdeletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('admin/goodsShop/deleteGoodsByShopId'),
            method: 'post',
            params: this.$http.adornParams({
              'shopId': this.shopId,
              'goodsId': row.goodsId
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.shopSelect()
              }
            })
          })
        }).catch(() => {})
      },
      // 获取商铺商品数据
      shopSelect() {
        let page = this.page
        this.tableDataLoading = true
        this.shopId = this.$route.query.shopId
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/selectGoodsByShopId'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'limit': this.size,
            'goodsName': this.title,
            'classifyId': this.classifyId,
            'status': this.status,
            'shopId': this.shopId,
            'shopName': this.shopName
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          for (var i in data.data.list) {
            if (data.data.list[i].goodsPicture) {
              data.data.list[i].goodsPicture = data.data.list[i].goodsPicture.split(',')
            }

          }
          let returnData = data.data;

          this.tableData = returnData
          this.totalnum = returnData.totalElements
        })
      },
      // 获取添加的商品数据
      shopSelect1() {
        let page = this.page1
        this.tableDataLoading1 = true
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/selectGoodsByNotShopId'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'limit': this.size1,
            'goodsName': this.title1,
            'shopId': this.shopId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading1 = false
          for (var i in data.data.list) {
            if (data.data.list[i].goodsPicture) {
              data.data.list[i].goodsPicture = data.data.list[i].goodsPicture.split(',')
            }
          }
          let returnData = data.data;
          this.tableData1 = returnData
          for (var i in this.tableData1.list) {
            this.tableData1.list[i].kucun = ''
          }

          this.totalnum = returnData.totalElements
        })
      },

      // 返回上一级
      prev() {
        this.$router.go(-1)
      },
      // 状态
      // 状态
      change(status, goodsId) {
      	this.$http({
      		url: this.$http.adornUrl('admin/goods/updateBatchStatus'),
      		method: 'get',
      		params: this.$http.adornParams({
      			'goodsIds': goodsId,
      			'status': status
      		})
      	}).then(({
      		data
      	}) => {
      		this.$message({
      			message: '操作成功',
      			type: 'success',
      			duration: 1500,
      			onClose: () => {
      				this.shopSelect()
      			}
      		})
      	})
      },
      // ··分类······························
      // 添加分类弹框
      addNotice() {
        this.dialogFormVisible1 = true
        this.titles = '添加'
        this.classifyId = ''
        this.classifyName = ''
        this.sort = 0
        this.classSelect()
      },
      // 添加分类
      releasNoticeTo() {
        if (this.classifyName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类名称',
            type: 'warning'
          })
          return
        }
        if (this.titles == '添加') {
          var url = 'admin/goods/insertClassify'
        }
        if (this.titles == '修改') {
          var url = 'admin/goods/updateClassify'
        }
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post',
          data: this.$http.adornData({
            'classifyId': this.classifyId,
            'classifyName': this.classifyName,
            'shopId': this.shopId,
            'sort':this.sort
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible1 = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {

              this.dataSelect()
            }
          })
        })
      },
      // 修改分类弹框
      updates(index, rows) {
        this.classifyId = rows.classifyId
        this.classifyName = rows.classifyName
         this.sort = rows.sort
        this.titles = '修改'
        this.dialogFormVisible1 = true

      },

      // 删除分类
      deletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('admin/goods/deleteClassify'),
            method: 'get',
            params: this.$http.adornParams({
              'classifyId': row.classifyId
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dataSelect()
              }
            })
          })
        }).catch(() => {})

      },
      // 获取分类数据
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/goods/selectAllClassify'),
          method: 'get',
          params: this.$http.adornParams({
            'shopId': this.shopId,
            'page': this.page,
            'limit': this.size
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.typeDatas = returnData
          this.classDatas = returnData
        })
      },
      // ··规格··············································
      
      deleterule(index) {
        this.ruleValue.splice(index, 1)
      },
      btnToclick() {
        this.isshow = false
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      speciTotwo() {
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      speciTo() {
        if (this.ruleName == '') {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '请输入标题名称',
            type: 'warning'
          })
          return
        }
        if (this.releobject.value == '') {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '请输入规格',
            type: 'warning'
          })
          return
        }
        if (this.releobject.detail == '') {
          this.$message({
            title: '提示',
            duration: 1800,
            type: 'error',
            message: '请输入规格值',
            type: 'warning'
          })
          return
        }

        this.ruleValue.push({
          value: this.releobject.value,
          detail: this.releobject.detail
        })
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      handleClose(tag, j, item) {
        var detailarr = item.detail.split(',')
        detailarr.splice(detailarr.indexOf('tag'), 1)
        if (detailarr.length < 1) {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '规格值至少要有一个',
            type: 'warning'
          })
          return
        } else {
          item.detail = String(detailarr)
        }
      },
      handleInputConfirm(index, item) {
        let inputValue = this.inputValues[index]
        if (inputValue) {
          var detailarr = item.detail.split(',')
          detailarr.push(inputValue)
          for (var i = 0; i < detailarr.length; i++) {
            for (var j = 0; j < detailarr.length; j++) {
              if (detailarr[i] == detailarr[j] && i != j) {
                detailarr.splice(j, 1)
              }
            }
          }
          item.detail = String(detailarr)
          this.inputValues[index] = ''
        }
        this.inputVisible = false

      },
      // 添加规格弹框
      addNotice1() {
        this.ruleName = ''
        this.ruleValue = []
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
        this.dialogFormVisible3 = true
      },
      // 添加规格
      specificationTo() {
        if (this.ruleName == '') {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '请输入标题名称',
            type: 'warning'
          })
          return
        }
        if (this.ruleValue.length == 0) {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '请至少添加一种规格',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('selfGoodsRule/save'),
          method: 'post',
          data: this.$http.adornData({
            'ruleName': this.ruleName,
            'ruleValue': this.ruleValue,
            'shopId': this.shopId
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible3 = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.ruleName = ''
              this.ruleValue = []
              this.dataSelect1()
            }
          })
        })
      },
      // 修改规格弹框
      updates1(rows) {
        this.dialogFormVisible2 = true
        this.form.id = rows.id
        this.form.ruleName = rows.ruleName
        this.form.ruleValue = rows.ruleValue
        this.form.createTime = rows.createTime
      },
      // 修改商品规格
      amendNoticeTo() {
        this.$http({
          url: this.$http.adornUrl('selfGoodsRule/update'),
          method: 'post',
          data: this.$http.adornData({
            'id': this.form.id,
            'ruleName': this.form.ruleName,
            'ruleValue': this.form.ruleValue,
            'createTime': this.form.createTime,
            'shopId': this.shopId
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible2 = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.dataSelect1()
            }
          })
        })
      },
      btnToclick1() {
        this.isshow = false
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      speciTotwo1() {
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      deleterule1(index) {
        this.form.ruleValue.splice(index, 1)
      },
      speciTo1() {
        this.form.ruleValue.push({
          value: this.releobject.value,
          detail: this.releobject.detail
        })
        this.isshow = true
        this.releobject.value = ''
        this.releobject.detail = ''
      },
      handleClose1(tag, j, item) {
        var detailarr = item.detail.split(',')
        detailarr.splice(detailarr.indexOf('tag'), 1)
        if (detailarr.length < 1) {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '规格值至少要有一个',
            type: 'warning'
          })
          return
        } else {
          item.detail = String(detailarr)
        }
      },
      handleInputConfirm1(index, item) {
        let inputValue = this.inputValues[index]
        if (inputValue) {
          var detailarr = item.detail.split(',')
          detailarr.push(inputValue)
          for (var i = 0; i < detailarr.length; i++) {
            for (var j = 0; j < detailarr.length; j++) {
              if (detailarr[i] == detailarr[j] && i != j) {
                detailarr.splice(j, 1)
              }
            }
          }
          item.detail = String(detailarr)
          this.inputValues[index] = ''
        }
        this.inputVisible = false
      },
      // 删除规格
      deletes1(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('selfGoodsRule/delete'),
            method: 'get',
            params: this.$http.adornParams({
              'id': row.id
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dataSelect1()
              }
            })
          })
        }).catch(() => {})
      },
      // 获取规格数据
      dataSelect1() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('selfGoodsRule/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.size,
            'shopId': this.shopId
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false

          let returnData = data.data
          this.typeDatas1 = returnData

        })
      },
      // 修改库存
      kcselect(row, kucun) {
        console.log('修改库存', row, kucun)
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/updateGoodsByShopId'),
          method: 'post',
          data: this.$http.adornData({
            'goodsId': row.goodsId,
            'shopId': row.shopId,
            'inventory': kucun
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '修改成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.shopSelect()
              }
            })
            this.xuanze = []
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
      // 排序
      paixu(row) {
        this.$http({
          url: this.$http.adornUrl('admin/goods/updateSort'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsId': row.goodsId,
            'sort': row.sort
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
                this.shopSelect()
              }
            })
          } else {
            this.$message({
              message: '操作成功',
              type: 'error',
              duration: 1500,
              onClose: () => {}
            })
          }
        })
      },
      // 批量修改状态
      transferClcik(id) {
        if (this.guigeType === '') {
          this.$message({
            title: '提示',
            type: 'error',
            duration: 1800,
            message: '请选择状态',
            type: 'warning'
          })
          return
        }
        var ids = id ? [id] : this.checkBoxData.map(item => {
          return item.goodsId
        })
        this.$http({
          url: this.$http.adornUrl('admin/goods/updateBatchStatus'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsIds': ids.toString(),
            'status': this.guigeType
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
                this.shopSelect()
                this.dialogFormVisibleSxj = false
              }
            })
          } else {
            this.$message({
              message: '操作成功',
              type: 'error',
              duration: 1500,
              onClose: () => {}
            })
          }
        })
      },
      // 多选
      changeFun(val) {
        console.log('val', val)
        this.checkBoxData = val
      },
      // 上下架弹框
      shanxiajia() {
        this.guigeType = ''
        this.dialogFormVisibleSxj = true
      },
      // 批量删除
      deletesPl(id) {
        var ids = id ? [id] : this.checkBoxData.map(item => {
          return item.goodsId
        })
        this.$http({
          url: this.$http.adornUrl('admin/goods/deleteBatchIds'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsIds': ids.toString(),
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
                this.shopSelect()
              }
            })
          } else {
            this.$message({
              message: '操作成功',
              type: 'error',
              duration: 1500,
              onClose: () => {}
            })
          }
        })
      },
      // 批量修改库存弹框
      kiucun() {
        this.num = ''
        this.dialogFormVisibleKc = true
      },
      transferClcikKc(id) {
        var ids = id ? [id] : this.checkBoxData.map(item => {
          return item.goodsId
        })
        this.$http({
          url: this.$http.adornUrl('admin/goods/updateBatchInventory'),
          method: 'get',
          params: this.$http.adornParams({
            'goodsIds': ids.toString(),
            'inventory': this.num
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
                this.shopSelect()
                this.dialogFormVisibleKc = false
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
      },
      // 分类排序
      paixuS(row) {
        this.$http({
          url: this.$http.adornUrl('admin/goods/updateClassify'),
          method: 'post',
          data: this.$http.adornData({
            'classifyId': row.classifyId,
            'sort': row.sort
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
          } else {
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {}
            })
          }
        })
      },
      
    },
    mounted() {
      this.shopId = this.$route.query.shopId
      this.shopSelect()
      this.dataSelect()
    },
    watch: {
      '$route': 'shopSelect'

    }
  };
</script>

<style lang="scss" scoped>
.shop-admin-page {
  padding: 0;
  overflow-x: hidden;
}

.shop-admin-page__nav {
  display: flex;
  align-items: center;
  font-size: 14px;
  margin-bottom: 12px;
  color: var(--nf-text-primary, #1a2433);
}

.shop-admin-page__back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  color: var(--nf-text-secondary, #5a6578);
  transition: color 0.2s;

  &:hover {
    color: #01af74;
  }
}

.shop-admin-page__back-icon {
  width: 1.2em;
  height: 1.2em;
}

.shop-admin-page__nav-sep {
  margin: 0 12px;
  color: #d9d9d9;
}

.shop-admin-page__nav-title {
  font-size: 16px;
  font-weight: 500;
}

.shop-admin-page__hero {
  margin-bottom: 12px;
}

.shop-admin-page__filter {
  margin-bottom: 12px;
}

.shop-admin-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.shop-admin-page__field {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.shop-admin-page__label {
  font-size: 13px;
  color: var(--nf-text-secondary, #5a6578);
  white-space: nowrap;
}

.shop-admin-page__input {
  width: 200px;
}

.shop-admin-page__select {
  width: 200px;
}

.shop-admin-page__select-sm {
  width: 150px;
}

.shop-admin-page__actions {
  display: inline-flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.shop-admin-page__tabs-wrap {
  margin-bottom: 0;
}

.shop-admin-page__tabs {
  margin-bottom: 0;
}

.shop-admin-page__tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.shop-admin-page__table-card {
  margin-bottom: 0;
  margin-top: 0;
  padding: 0;
  overflow: hidden;
}

.shop-admin-page__table-card >>> .el-table {
  border-radius: 0;
}

.shop-admin-page__toolbar {
  display: flex;
  justify-content: flex-end;
  padding: 12px 16px;
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  background: #fff;
}

.shop-admin-page__table >>> .el-table__header th {
  background: var(--nf-page-bg, #f4f6fa);
  color: var(--nf-text-primary, #1a2433);
  font-weight: 600;
}

.shop-admin-page__table >>> .el-table__body td {
  background: #fff;
}

.shop-admin-page__pagination {
  display: flex;
  justify-content: center;
  padding: 12px 16px;
  border-top: 1px solid var(--nf-border, #e8eaed);
  background: #fff;
}
</style>
