<template>
  <div class="mission-add-page">
    <div class="mission-add-page__nav">
      <a href="#" class="mission-add-page__back-link" @click.prevent="prev">
        <icon-svg name="jiantou" class="mission-add-page__back-icon"></icon-svg>
        返回
      </a>
      <span class="mission-add-page__nav-sep">|</span>
      <span class="mission-add-page__nav-title">{{ titles }}</span>
    </div>

    <div class="nf-page-hero mission-add-page__hero">
      <span class="nf-page-hero__badge">服务管理</span>
      <h2 class="nf-page-hero__title">{{ titles }}</h2>
      <p class="nf-page-hero__desc">填写服务基本信息、满返规则、分类与地址，支持普通服务与商铺服务发布</p>
    </div>

    <div class="nf-filter-card mission-add-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-operation"></i>
        <span>基本信息</span>
      </div>
      <div class="nf-filter-card__body mission-add-page__filter-body">
        <div class="mission-add-page__row">
          <span class="mission-add-page__label">服务类型：</span>
          <div class="mission-add-page__control">
            <el-radio-group v-model="goodsType">
              <el-radio :label="1">普通服务</el-radio>
              <el-radio :label="2">商铺服务</el-radio>
            </el-radio-group>
          </div>
        </div>
        <div class="mission-add-page__row" v-if="goodsType==2">
          <span class="mission-add-page__label">商铺：</span>
          <div class="mission-add-page__control">
            <el-input v-model="shangpuName" class="mission-add-page__input-lg" placeholder="请选择商铺" @focus="shangpu"></el-input>
          </div>
        </div>
      </div>
    </div>

    <div class="nf-table-card mission-add-page__form-card" :title="titles">
      <div class="mission-add-page__form-body">
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">服务标题：</span>
        <div class="mission-add-page__control">
          <el-input v-model="goodsTitle" class="mission-add-page__input-lg" placeholder="请输入服务标题"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row mission-add-page__row--top">
        <span class="mission-add-page__label">封面图：</span>
        <div class="mission-add-page__control">
          <div class="mission-add-page__upload-box">
            <el-upload class="avatar-uploader" v-model="titleImg"
              :action="$http.adornUrl('file/upload')" :show-file-list="false"
              :on-success="handleAvatarSuccess">
              <img v-if="titleImg" :src="titleImg" class="mission-add-page__avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
            </el-upload>
          </div>
        </div>
      </div>
      <div class="mission-add-page__row mission-add-page__row--top">
        <span class="mission-add-page__label">轮播图：</span>
        <div class="mission-add-page__control mission-add-page__carousel">
          <div class="imgs" v-for="(item,index) in img" :key="index">
            <img width="100%" class="images" height="100%" :src="item" alt="">
            <span class="dels">
              <i class="el-icon-delete" @click="dels(index)"></i>
            </span>
          </div>
          <div class="imgs mission-add-page__upload-trigger">
            <el-upload :action="$http.adornUrl('file/upload')" list-type="picture-card"
              :show-file-list="false" :on-success="handleRemove">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </div>
        </div>
      </div>
      <div class="mission-add-page__row" v-if="goodsType==2">
        <span class="mission-add-page__label">商家返利金额：</span>
        <div class="mission-add-page__control">
          <el-input v-model="merchantMoney" class="mission-add-page__input-lg" placeholder="请输入返现积分"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">会员满返：</span>
        <div class="mission-add-page__control mission-add-page__inline-group">
          <span>满 </span>
          <el-input v-model="memberPrice" class="mission-add-page__input-sm" placeholder="请输入金额要求"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"></el-input>
          <span>返 </span>
          <el-input v-model="memberMoney" class="mission-add-page__input-sm" placeholder="请输入优惠金额"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">普通用户满返：</span>
        <div class="mission-add-page__control mission-add-page__inline-group">
          <span>满 </span>
          <el-input v-model="price" class="mission-add-page__input-sm" placeholder="请输入金额要求"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"></el-input>
          <span>返 </span>
          <el-input v-model="money" class="mission-add-page__input-sm" placeholder="请输入优惠金额"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">服务品类：</span>
        <div class="mission-add-page__control">
          <el-select v-model="fromType" class="mission-add-page__input-lg" @change="onChang">
            <el-option v-for="(item,index) in cities" :key="index" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">小程序跳转链接：</span>
        <div class="mission-add-page__control">
          <el-input v-model="url" class="mission-add-page__input-lg" placeholder="请输入小程序跳转链接"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">服务分类：</span>
        <div class="mission-add-page__control">
          <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange" class="mission-add-page__checkbox-group">
            <span v-for="(item,index) in fromTypes" :key="index" class="mission-add-page__checkbox-item">
              <el-checkbox v-if="item.state==1" :label="item.id">{{item.classifyName}}
              </el-checkbox>
            </span>
          </el-checkbox-group>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">评星数量：</span>
        <div class="mission-add-page__control">
          <el-input v-model="numStar" class="mission-add-page__input-lg" placeholder="请输入评星数量"
            onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">上传图片数量：</span>
        <div class="mission-add-page__control">
          <el-input v-model="numImg" class="mission-add-page__input-lg" placeholder="请输入上传图片数量"
            onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">评论字数：</span>
        <div class="mission-add-page__control">
          <el-input v-model="numWord" class="mission-add-page__input-lg" placeholder="请输入评论字数"
            onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row mission-add-page__row--top">
        <span class="mission-add-page__label">备注：</span>
        <div class="mission-add-page__control">
          <el-input v-model="remark" class="mission-add-page__input-hidden" placeholder="请输入备注"></el-input>
          <quill-editor ref="myTextEditor" v-model="remark" :options="quillOption"
            class="mission-add-page__editor">
          </quill-editor>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">服务平台：</span>
        <div class="mission-add-page__control">
          <el-radio-group v-model="classify">
            <el-radio :label="1">饿了么</el-radio>
            <el-radio :label="2">美团</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">起止时间：</span>
        <div class="mission-add-page__control">
          <el-date-picker v-model="value1" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至"
            start-placeholder="开始日期" end-placeholder="结束日期" @change="changeaa" :default-time="['09:00:00', '23:59:59']">
          </el-date-picker>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">接单数量：</span>
        <div class="mission-add-page__control">
          <el-input v-model="sumNum" class="mission-add-page__input-lg" placeholder="请输入接单数量"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">城市：</span>
        <div class="mission-add-page__control">
          <el-cascader class="mission-add-page__input-lg" size="large" :options="options" ref="cascaderAddr" v-model="storeAddress"
            :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
          </el-cascader>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">地址：</span>
        <div class="mission-add-page__control">
          <el-input v-model="address" @keydown.enter.native="select" class="mission-add-page__input-lg" placeholder="请输入详细地址"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">纬度：</span>
        <div class="mission-add-page__control">
          <el-input v-model="latitude" class="mission-add-page__input-lg" placeholder="请输入纬度"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">经度：</span>
        <div class="mission-add-page__control">
          <el-input v-model="longitude" class="mission-add-page__input-lg" placeholder="请输入经度"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">配送范围(单位：米)：</span>
        <div class="mission-add-page__control">
          <el-input v-model="scope" class="mission-add-page__input-lg" placeholder="请输入配送范围" type="number" min="0"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">是否渠道用户：</span>
        <div class="mission-add-page__control">
          <el-radio-group v-model="isChannel">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="mission-add-page__row" v-if="isChannel==1">
        <span class="mission-add-page__label">渠道用户：</span>
        <div class="mission-add-page__control">
          <el-input v-model="nickName" @focus="userselect" class="mission-add-page__input-lg" placeholder="请选择用户"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row" v-if="isChannel==1">
        <span class="mission-add-page__label">渠道佣金：</span>
        <div class="mission-add-page__control">
          <el-input v-model="channelMoney" class="mission-add-page__input-lg" placeholder="请输入渠道佣金"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">返现金额：</span>
        <div class="mission-add-page__control">
          <el-input v-model="returnMoney" class="mission-add-page__input-lg" placeholder="请输入返现积分"
            onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')"></el-input>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">级别：</span>
        <div class="mission-add-page__control">
          <el-radio-group v-model="isGoods">
            <el-radio :label="1">精选</el-radio>
            <el-radio :label="2">普通</el-radio>
          </el-radio-group>
        </div>
      </div>
      <div class="mission-add-page__row">
        <span class="mission-add-page__label">状态：</span>
        <div class="mission-add-page__control">
          <el-radio-group v-model="status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="2">下架</el-radio>
          </el-radio-group>
        </div>
      </div>
      </div>
      <div class="mission-add-page__map-wrap">
        <div id="container1" class="mission-add-page__map"></div>
      </div>
      <div class="mission-add-page__footer">
        <el-button @click="prev">取 消</el-button>
        <el-button type="primary" @click="addmissionNoticeTo()">保 存</el-button>
      </div>
    </div>
    <!-- 用户列表 -->
    <el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
      <div style="margin:2% 0;display: inline-block;">
        <el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
          v-model="phone"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <!-- <el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
    			v-model="nickName"></el-input> -->
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
          查询
        </el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
          重置
        </el-button>
      </div>
      <el-table width="780px" v-loading="tableDataLoading4" v-if="userIds" :data="userIds.list">
        <el-table-column fixed prop="channelName" width="220" label="渠道商">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.channelName ? scope.row.channelName : '未设置'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" width="220" label="手机号">
          <template slot-scope="scope">
            <span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" width="220" label="创建时间">
        </el-table-column>
        </el-table-column>
        <el-table-column label="操作" fixed='right' width="120">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="confirm(scope.row)">确定
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;" v-if="userIds">
        <el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
          :page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="pages"
          layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalCount">
        </el-pagination>
      </div>
    </el-dialog>
    <el-dialog title="商铺列表" :visible.sync="dialogFormVisible5" center>
      <div style="display: inline-block;">
        <div style="position: relative;display: inline-block;">
          <span>商铺名：</span>
          <el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入申请商铺名" v-model="merchantName">
          </el-input>&nbsp;&nbsp;
          <span>电话：</span>
          <el-input style="width: 200px;" @keydown.enter.native="select1" placeholder="请输入申请电话" v-model="merchantPhone">
          </el-input>&nbsp;&nbsp;
        </div>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select1">查询</el-button>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="renwuData.records">
        <el-table-column fixed prop="merchantId" label="编号" align="center" width="80">
        </el-table-column>
        <el-table-column prop="nickName" label="渠道商">
          <template slot-scope="scope">
            <span style="color: #01af74;cursor: pointer;" @click="updates(scope.row)">{{scope.row.nickName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="merchantTypeName" label="商铺类型">
        </el-table-column>
        <el-table-column prop="merchantName" label="商铺名称">
        </el-table-column>
        <el-table-column prop="merchantLogo" label="商铺logo" width="180" align="center">
          <template slot-scope="scope">
            <img v-if="scope.row.merchantLogo" :src="scope.row.merchantLogo" alt="" width="80" height="80"
              style="border-radius: 50%;">
            <span v-else>暂无</span>
          </template>
        </el-table-column>
        <el-table-column prop="merchantPhone" label="电话">
        </el-table-column>
        <el-table-column prop="merchantAddress" label="地址" width="300">
          <template slot-scope="scope">
            <span>{{scope.row.merchantProvince}}{{scope.row.merchantCity}}{{scope.row.merchantDistrict}}{{scope.row.merchantAddress}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isGoods" label="状态" width="100">
          <template slot-scope="scope">
            <!-- <el-switch  v-model="scope.row.status" @change="change2(scope.row.merchantId)" :active-value="openValue1"
              :inactive-value="closeValue1" active-color="#13ce66" inactive-color="#ff4949">
            </el-switch> -->
            <span v-if="scope.row.status==1">待审核</span>
            <span v-if="scope.row.status==2">已上架</span>
            <span v-if="scope.row.status==3">拒绝</span>
            <span v-if="scope.row.status==4">下架</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="queren(scope.row)">确认</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
          layout="total,sizes, prev, pager, next,jumper" :total="renwuData.total">
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
  import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
  import quillConfig from './quill-config.js'
  import {
    provinceAndCityData,
    regionData,
    provinceAndCityDataPlus,
    regionDataPlus,
    CodeToText,
    TextToCode
  } from 'element-china-area-data'
  var cityOptions = []
  var geocoder, map, markersArray = [];
  import axios from 'axios';
  import {
    jsonp
  } from 'vue-jsonp'
  export default {
    name: 'news',
    components: {
      BaiduMap,
      quillEditor
    },
    data() {
      return {
        titles: '添加服务',
        goodsType:'',
        goodsId: '', //服务id
        shangpuList:[],//商铺列表
        shangpuId:'',//商铺Id
        classify: '', //服务平台 1饿了么 2美团
        goodsTitle: '', //服务名称
        goodsPrice: '', //服务价格
        titleImg: '', //服务封面图
        img: [], //服务轮播图
        isGoods: 0, //是否是精选商品 0不是  1是
        isChannel: '', //是否为渠道用户 0否 1是
        channelUserid: '', //渠道用户Id
        channelMoney: '', //渠道用户佣金
        returnMoney: '', //返现积分
        numImg: '', //服务图片数
        numStar: '', //服务评星数
        numWord: '', //服务评论字数
        memberPrice: '', //会员满
        memberMoney: '', //会员减
        price: '', //普通用户满
        money: '', //普通用户减
        startTime: '', //开始时间
        endTime: '', //结束时间
        remark: '', //备注（富文本）
        editorOption: {},
        url: '', //小程序跳转链接
        typeId: '', //品类id
        options: regionData,
        address: '',
        sumNum: '', //服务总数量
        status: 0, //状态 1上架  2下架
        fromType: [],
        fromTypes: [],
        checkAll: false,
        activityIds: '', //分类id
        activityNames: '', //分类名称
        checkedCities: [], //选中分类id合集
        province: '', //省
        city: '', //市
        district: '', //区
        cities: cityOptions,
        isIndeterminate: true,
        longitude: '', //维度
        latitude: '', //经度
        scope: '', //配送范围
        merchantMoney:'',//商家返利金额
        dialogVisible: false,
        // 地图相关
        loadingMap: true,
        BMap: '',
        map: '',
        geoc: '',
        keyword: '',
        showMap: false,
        pointLngLat: '',
        center: {
          lng: 109.45744048529967,
          lat: 36.49771311230842
        },
        storeAddress: [],
        storeAddre: '请选择城市',
        form: {
          province: '',
          city: '', //市
          district: '', //区
        },
        pickerOptions: {
          value1: [],
          value2: ''
        },
        value1: '',
        value2: '',
        missions: 0, //获取修改数据
        dialogImageUrl: [],
        disabled: false,
        quillOption: quillConfig,
        dialogFormVisible3: false,
        tableDataLoading4: false,
        dialogFormVisible5: false,
        tableDataLoading:false,
        phone: '',
        nickName: '',
        member: '',
        userIds: {},
        page: 1,
        pages: 1,
        size: 10,
        // 商铺列表
        limit:5,
        page:1,
        renwuData: {},
        merchantName:'',
        merchantPhone:'',
        shangpuName:'',
      }
    },
    methods: {
      handleSizeChange2(val) {
        this.size = val
        this.tableDataLoading4 = true
        this.userClass()
      },
      handleCurrentChange2(val) {
        this.pages = val
        this.tableDataLoading4 = true
        this.userClass()
      },
      // 返回上一级
      prev() {
        sessionStorage.setItem('page', this.page);
        this.$router.back()
      },
      // 确定添加
      addmissionNoticeTo() {
        this.select()
        console.log('this.img', this.img)
        if(this.goodsType==''){
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择服务类型',
            type: 'warning'
          });
          return
        }
        if(this.goodsType==2){
          if (this.shangpuId == '') {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '请选择商铺',
              type: 'warning'
            });
            return
          }
          if (this.merchantMoney == '') {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '请输入商家返利金额',
              type: 'warning'
            });
            return
          }
        }
        if (this.goodsTitle == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入服务标题',
            type: 'warning'
          });
          return
        }
        if (this.titleImg == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传服务封面图',
            type: 'warning'
          });
          return
        }
        if (this.img.length == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传服务轮播图',
            type: 'warning'
          });
          return
        }
        if (this.memberPrice == '' || this.memberMoney == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入会员满返金额',
            type: 'warning'
          });
          return
        }
        if (this.memberPrice < 0 || this.memberMoney < 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入有效的满返金额',
            type: 'warning'
          });
          return
        }
        if (this.price == '' || this.money == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入普通用户满减金额',
            type: 'warning'
          });
          return
        }
        if (this.price < 0 || this.money < 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入普通用户有效的满减金额',
            type: 'warning'
          });
          return
        }
        if (this.fromType == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择服务品类',
            type: 'warning'
          });
          return
        }
        if (this.url == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入小程序跳转链接',
            type: 'warning'
          });
          return
        }
        if (this.checkedCities.length == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择服务分类',
            type: 'warning'
          });
          return
        }
        if (this.numStar == '' || Number(this.numStar) == null) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入有效的评星数量',
            type: 'warning'
          });
          return
        }
        if (this.numImg == '' || Number(this.numImg) == null) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入有效的上传图片数量',
            type: 'warning'
          });
          return
        }
        if (this.numWord == '' || Number(this.numWord) == null) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入有效的评论字数数量',
            type: 'warning'
          });
          return
        }
        if (this.remark == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入备注',
            type: 'warning'
          });
          return
        }
        if (this.classify == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择服务平台',
            type: 'warning'
          });
          return
        }
        if (this.value1 == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择起止时间',
            type: 'warning'
          });
          return
        }
        if (this.sumNum == '' || Number(this.sumNum) == null) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入有效的接单数量',
            type: 'warning'
          });
          return
        }
        if (this.province == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择所在城市',
            type: 'warning'
          });
          return
        }
        if (this.address == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '详细地址不能为空',
            type: 'warning'
          });
          return
        }
        if (this.scope == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入配送范围',
            type: 'warning'
          });
          return
        }
        if (this.isChannel == 1) {
          if (this.channelUserid == '') {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '请选择渠道用户',
              type: 'warning'
            });
            return
          }
          if (this.channelMoney == '') {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '请输入渠道佣金',
              type: 'warning'
            });
            return
          }
        }
        // if(this.returnMoney==''){
        //   this.$notify({
        //     title: '提示',
        //     duration: 1800,
        //     message: '请输入返现金额',
        //     type: 'warning'
        //   });
        //   return
        // }
        if (this.isChannel == 0 || this.isChannel == '') {
          this.channelMoney = ''
          this.channelUserid = ''
        }
        if (this.isGoods == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择服务是否为精选',
            type: 'warning'
          });
          return
        }
        if (this.status == 0) {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择服务状态',
            type: 'warning'
          });
          return
        }
        // toString()
        let urls = ''
        if (this.missions == 0) {
          urls = 'wm/insertGoods'
        } else {
          urls = 'wm/updateGoods'
        }
        let that = this
        setTimeout(function() {
          that.$http({
            url: that.$http.adornUrl(urls),
            method: 'post',
            data: that.$http.adornData({
              'activityIds': that.checkedCities.toString(),
              'address': that.address,
              'city': that.city,
              'classify': that.classify,
              'district': that.district,
              'endNum': that.endNum,
              'endTime': that.value1[1].toString(),
              'goodsPrice': that.goodsPrice,
              'goodsTitle': that.goodsTitle,
              'img': that.img.toString(),
              'isGoods': that.isGoods,
              'latitude': that.latitude,
              'longitude': that.longitude,
              'memberMoney': that.memberMoney,
              'memberPrice': that.memberPrice,
              'money': that.money,
              'price': that.price,
              'numImg': that.numImg,
              'numStar': that.numStar,
              'numWord': that.numWord,
              'province': that.province,
              'remark': that.remark,
              'startTime': that.value1[0].toString(),
              'status': that.status,
              'sumNum': that.sumNum,
              'titleImg': that.titleImg,
              'typeId': that.typeId,
              'url': that.url,
              'goodsId': that.goodsId,
              'scope': that.scope,
              'channelUserId': that.channelUserid,
              'channelMoney': that.channelMoney,
              'returnMoney': that.returnMoney,
              'merchantId':that.shangpuId,
              'merchantMoney':that.merchantMoney
            })
          }).then(({
            data
          }) => {
            console.log('提交结果', data)
            if(data.code==0){
              that.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  that.prev()
                }
              })
            }else{
              that.$message({
                message: data.msg,
                type: 'warning',
                duration: 1500,
                onClose: () => {
                }
              })
            }

          })
        }, 1000)

      },
      // 获取服务分类
      task() {
        this.$http({
          url: this.$http.adornUrl('banner/selectBannerList'),
          method: 'get',
          params: this.$http.adornParams({
            'state': -1,
            'classify': 2,
          })
        }).then(({
          data
        }) => {
          console.log('服务分类', data.data)
          if (data && data.code === 0) {
            let returnData = data.data;
            this.cities = returnData;
          }
        })
      },
      // 获取服务品类
      category() {
        this.$http({
          url: this.$http.adornUrl('helpClassify/selectClassifyList'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          console.log('服务分品类', data.data)
          if (data && data.code === 0) {
            let returnData = data.data;
            this.fromTypes = returnData;
          }
        })
      },
      onChang(e) {
        console.log(e)
        this.typeId = e
      },
      onChang1(e){
        this.shangpuId = e
      },
      // 封面图片上传
      handleAvatarSuccess(file) {
        this.titleImg = file.data.path;
      },
      // 轮播图片上传
      handleRemove(file) {
        console.log(file, this.img)
        this.img.push(file.data.path);
      },
      handleDownload(file) {

        console.log(file, this.img)
      },
      // 删除轮播图
      dels(index) {
        this.img.splice(index, 1);
        console.log(this.img)
      },
      // 分类选择
      handleCheckAllChange(val) {
        this.checkedCities = val ? cityOptions : [];
        this.isIndeterminate = false;
      },
      handleCheckedCitiesChange(value) {
        console.log(value)
        let checkedCount = value.length;
        this.checkAll = checkedCount === this.cities.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;
      },
      handleChange55(value) {
        value = this.$refs['cascaderAddr'].currentLabels
        this.province = value[0]
        this.city = value[1]
        this.district = value[2]
        this.form.city = value[1]
        if (this.city == '市辖区') {
          this.city = this.province
        }
        if (this.form.city == '市辖区') {
          this.city = this.province
        }
        console.log(this.$refs['cascaderAddr'].currentLabels)
      },
      //定位获得当前位置信息
      getMyLocation() {
        var geolocation = new qq.maps.Geolocation("WYJBZ-BOVK2-QZSU2-CJHY2-7BFHE-JEFNP", "来点小收入");
        geolocation.getIpLocation(this.showPosition, this.showErr);
        // geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
      },
      showPosition(position) {
        console.log(position);
        // this.latitude = position.lat;
        // this.longitude = position.lng;
        // this.city = position.city;
        this.setMap();
      },
      showErr(e) {
        console.log("定位失败", e);
        this.getMyLocation(); //定位失败再请求定位，测试使用
      },
      //位置信息在地图上展示
      setMap() {
        //步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
        //设置地图中心点
        var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
        //定义工厂模式函数
        var myOptions = {
          zoom: 13, //设置地图缩放级别
          center: myLatlng, //设置中心点样式
          mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
        }
        // //获取dom元素添加地图信息
        var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
        //给地图添加点击事件

        //给定位的位置添加图片标注
        var marker = new qq.maps.Marker({
          position: myLatlng,
          map: map
        });
        // `````````````
        var that = this;
        if (that.longitude == '') {
          var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
        } else {
          var center = new qq.maps.LatLng(that.latitude, that.longitude);
        }

        var map = new qq.maps.Map(document.getElementById("container1"), {
          center: center,
          zoom: 13
        });
        var marker = new qq.maps.Marker({
          position: center,
          map: map
        });
        var latlngBounds = new qq.maps.LatLngBounds();
        qq.maps.event.addListener(map, "click", function(event) {
          console.log(event, qq.maps);
          that.longitude = event.latLng.getLng(); // 经度
          that.latitude = event.latLng.getLat(); // 纬度
          jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event.latLng
            .getLng() + '&key=WYJBZ-BOVK2-QZSU2-CJHY2-7BFHE-JEFNP&get_poi=1&output=jsonp', {
              myCustomUrlParam: 'veryNice'
            }).then(response => {
            console.log('response', response, response.result.formatted_addresses.recommend)
            that.address = response.result.formatted_addresses.recommend

          }).catch(error => {
            // handle error
          }).then(() => {
            // always executed
          });
          if (markersArray) {
            for (let i in markersArray) {
              markersArray[i].setMap(null);
            }
          }
          var marker = new qq.maps.Marker({
            map: map,
            position: event.latLng
          });
          markersArray.push(marker);

        });
        geocoder = new qq.maps.Geocoder({
          complete: function(result) {
            console.log(result);
            that.longitude = result.detail.location.lng;
            that.latitude = result.detail.location.lat;
            map.setCenter(result.detail.location);
            var marker = new qq.maps.Marker({
              map: map,
              position: result.detail.location
            });
            markersArray.push(marker);
          }
        });
      },
      // 地图定位
      select() {
        console.log(this.address)
        // this.address = this.province+ this.city+this.district+this.address
        var addr = this.province + this.city + this.district + this.address
        let that = this
        jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
          '&key=WYJBZ-BOVK2-QZSU2-CJHY2-7BFHE-JEFNP&get_poi=1&output=jsonp', {
            myCustomUrlParam: 'veryNice'
          }).then(response => {
          // handle success
          console.log('response', response)
          that.longitude = response.result.location.lng; // 经度
          that.latitude = response.result.location.lat; // 纬度
          // that.address = response.result.title
          that.setMap()
        }).catch(error => {
          // handle error
        }).then(() => {
          // always executed
        });
      },
      changeaa(e) {
        console.log('eeee', e)
      },
      // 获取用户
      userselect() {
        this.dialogFormVisible3 = true
        this.tableDataLoading4 = true
        this.userClass()
      },
      // 获取用户列表
      userClass() {
        let phone = -1
        if (this.phone) {
          phone = this.phone
        }
        let nickName = -1
        if (this.nickName) {
          nickName = this.nickName
        }
        this.$http({
          url: this.$http.adornUrl('user/selectUserList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pages,
            'limit': this.size,
            'isChannel': 1,
            'phone': this.phone,
            'member': -1,
            'address': '',
            'teamIndex': -2
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading4 = false
          let returnData = data.data
          this.userIds = returnData
        })
      },
      // 查询用户列表
      userclick() {
        this.pages = 1
        this.tableDataLoading4 = true
        this.userClass()
      },
      // 重置用户列表数据
      userclose() {
        this.phone = ''
        this.nickName = ''
        this.tableDataLoading4 = true
        this.userClass()
      },
      // 确定用户
      confirm(row) {
        this.channelUserid = row.userId
        console.log(row, this.channelUserid)
        this.nickName = row.channelName
        this.phone = row.phone
        if (this.nickName == '' || this.nickName == null) {
          this.nickName = row.phone
        }
        this.dialogFormVisible3 = false
      },
      // 获取商铺列表
      spList() {
        this.$http({
          url: this.$http.adornUrl(`goodsMerchant/selectGoodsMerchantList`),
          method: 'get',
          params: this.$http.adornParams({
            'merchantPhone':'',
            'merchantName': '',
            'status': ''
          })
        }).then(({
          data
        }) => {
          console.log('服务分品类', data.data)
          if (data && data.code === 0) {
            let returnData = data.data;
            this.shangpuList = returnData;
          }
        })
      },
      shangpu(){
        this.dialogFormVisible5 = true
        this.dataSelect()
      },
      // 获取商铺列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl(`goodsMerchant/selectGoodsMerchantPage`),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'merchantPhone':this.merchantPhone,
            'merchantName': this.merchantName,
            'status': 0
          })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            console.log(data)
            this.tableDataLoading = false
            let returnData = data.data;
            this.renwuData = returnData
          }
        })
      },
      select1(){
        this.dataSelect()
      },
      cleans(){
        this.merchantName = ''
        this.merchantPhone = ''
        this.dataSelect()
      },
      handleSizeChange(val) {
        this.limit = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      queren(row){
        this.shangpuId = row.merchantId
        this.shangpuName = row.merchantName
        this.dialogFormVisible5 = false
      },
    },
    width() {
      console.log('111111111111111')
    },
    mounted() {
      this.spList()
      let missions = this.$route.query.missions
      this.missions = missions
      console.log('missions',missions,missions.img)
      if (missions != 0) {
        this.titles = '修改服务'
        if(missions.activityIds){
          this.activityIds = missions.activityIds.split(',');
        }
        if(missions.merchantId){
          this.goodsType = 2
          this.shangpuId = missions.merchantId
          this.shangpuName = missions.merchantName
        }else{
          this.goodsType = 1
        }
        this.address = missions.address;
        this.city = missions.city;
        this.classify = missions.classify;
        this.district = missions.district;
        this.endNum = missions.endNum;
        this.endTime = missions.endTime;
        this.goodsPrice = missions.goodsPrice;
        this.goodsTitle = missions.goodsTitle;
        this.img = missions.img.split(',');
        this.dialogImageUrl = missions.img.split(',');
        console.log('this.dialogImageUrl', this.dialogImageUrl)
        this.isGoods = missions.isGoods;
        this.latitude = missions.latitude;
        this.longitude = missions.longitude;
        this.memberMoney = missions.memberMoney;
        this.memberPrice = missions.memberPrice;
        this.money = missions.money;
        this.price = missions.price;
        this.numImg = missions.numImg;
        this.numStar = missions.numStar;
        this.numWord = missions.numWord;
        this.province = missions.province;
        this.remark = missions.remark;
        this.startTime = missions.startTime;
        this.status = missions.status;
        this.sumNum = missions.sumNum;
        this.titleImg = missions.titleImg;
        this.typeId = missions.typeId;
        this.url = missions.url;
        this.goodsId = missions.goodsId
        this.fromType = missions.typeName;
        this.scope = missions.scope;
        this.merchantMoney = missions.merchantMoney
        if(missions.activityIds){
          this.checkedCities = missions.activityIds.split(',')
          let a = []
          for (var i in this.checkedCities) {
            a.push(Number(this.checkedCities[i]))
          }
          console.log(this.checkedCities, a)
          this.checkedCities = a
        }


        let value1 = [];
        value1.push(this.startTime)
        value1.push(this.endTime)
        this.value1 = value1
        this.returnMoney = missions.returnMoney
        this.storeAddress.push(this.province)
        this.storeAddress.push(this.city)
        this.storeAddress.push(this.district)
        this.storeAddre = this.province + '/' + this.city + '/' + this.district
        if (missions.channelUserId) {
          this.isChannel = 1
          this.channelMoney = missions.channelMoney
          this.channelUserid = missions.channelUserId
          this.nickName = missions.channelName
        } else {
          this.isChannel = 0
        }
      }
      console.log("获取数据", missions, this.$route.query.page)
      this.page = this.$route.query.page
      this.getMyLocation();
      this.task();
      this.category()
    }
  }
</script>

<style lang="scss" scoped>
.mission-add-page {
  padding: 0;
  overflow-x: hidden;
}

.mission-add-page__nav {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: var(--nf-text-primary, #1a2433);
}

.mission-add-page__back-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--nf-brand, #01af74);
  text-decoration: none;
  cursor: pointer;
}

.mission-add-page__back-link:hover {
  color: var(--nf-brand-deep, #017c52);
}

.mission-add-page__back-icon {
  width: 1.2em;
  height: 1.2em;
}

.mission-add-page__nav-sep {
  margin: 0 12px;
  color: var(--nf-border, #e8eaed);
}

.mission-add-page__nav-title {
  font-size: 16px;
  font-weight: 600;
}

.mission-add-page__hero {
  margin-bottom: 12px;
}

.mission-add-page__filter {
  margin-bottom: 12px;
}

.mission-add-page__filter-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mission-add-page__form-card {
  margin-bottom: 0;
  padding: 16px;
  overflow: hidden;
}

.mission-add-page__form-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mission-add-page__row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mission-add-page__row--top {
  align-items: flex-start;
}

.mission-add-page__label {
  flex: 0 0 200px;
  text-align: right;
  font-size: 13px;
  color: var(--nf-text-secondary, #5a6578);
  line-height: 32px;
}

.mission-add-page__row--top .mission-add-page__label {
  line-height: 1.5;
  padding-top: 6px;
}

.mission-add-page__control {
  flex: 1;
  min-width: 0;
}

.mission-add-page__input-lg {
  width: 45%;
  max-width: 480px;
}

.mission-add-page__input-sm {
  width: 200px;
}

.mission-add-page__input-hidden {
  display: none;
}

.mission-add-page__inline-group {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.mission-add-page__checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 20px;
}

.mission-add-page__checkbox-item {
  display: inline-flex;
}

.mission-add-page__upload-box {
  width: 148px;
  height: 148px;
  border: 1px dashed var(--nf-border, #c0ccda);
  border-radius: 6px;
  text-align: center;
  line-height: 148px;
  overflow: hidden;
}

.mission-add-page__avatar {
  border-radius: 6px;
  width: 148px;
  height: 148px;
  object-fit: cover;
}

.mission-add-page__carousel {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 10px;
}

.mission-add-page__upload-trigger {
  width: auto;
}

.mission-add-page__editor {
  width: 72%;
  max-width: 100%;
  height: 300px;
  margin-bottom: 60px;
  padding-bottom: 50px;
  display: inline-table;
}

.mission-add-page__map-wrap {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--nf-border, #e8eaed);
}

.mission-add-page__map {
  width: 80%;
  height: 500px;
  margin: 0 auto;
  border-radius: var(--nf-radius-md, 8px);
  overflow: hidden;
}

.mission-add-page__footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid var(--nf-border, #e8eaed);
}

.imgs {
  position: relative;
  border-radius: 6px;
  width: 148px;
  height: 148px;
  display: inline-block;
}

.dels {
  position: absolute;
  top: 0;
  left: 0;
  display: none;
}

.dels .el-icon-delete {
  line-height: 148px;
  padding-left: 58px;
  font-size: 25px;
  color: #fff;
}

.imgs:hover .dels {
  width: 100%;
  height: 100%;
  background: #000;
  display: block;
  opacity: 0.5;
}
</style>
