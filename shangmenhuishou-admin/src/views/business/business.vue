<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="投诉列表" name="first">
      <div style="margin-right:2%;">
        <span>状态：</span>
        <el-select v-model="complaintState" style="width:200px;margin-left: 10px;" @change="animeDat(complaintState)">
          <el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>投诉类型：</span>
        <el-select v-model="illegalId" style="width:200px;margin-left: 10px;" @change="animeDat1(illegalId)">
          <el-option v-for="item in illegalList" :key="item.id" :label="item.illegal" :value="item.id">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
        <div style="position: relative;display: inline-block;">
          <span>订单编号：</span>
          <el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号" v-model="indentNumber">
          </el-input>&nbsp;&nbsp;
        </div>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
        </el-button>
        <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
        </el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData.list">
        <el-table-column fixed prop="complaintId" label="编号" width="80">
          <!-- <template slot-scope="scope">
					<span>{{scope.$index+1}}</span>
				</template> -->
        </el-table-column>
        <el-table-column prop="indentNumber" label="订单号" width="200" align="center">
        </el-table-column>
        <el-table-column prop="nickName" label="被投诉的骑手" width="180">
          <template slot-scope="scope">
            <span style="color: #01af74;cursor: pointer;" @click="updates(scope.row)">
              {{ scope.row.nickName?scope.row.nickName:'未绑定'}}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="投诉来源" width="150">
          <template slot-scope="scope">
        	<div v-if="scope.row.type==1">用户投诉</div>
        	<div v-if="scope.row.type==2">商户投诉</div>
        </template>
        </el-table-column>
        <el-table-column prop="illegal" label="投诉类型" width="150">
          <template slot-scope="scope">
					<div v-if="scope.row.type==1">{{scope.row.illegal}}</div>
					<div v-if="scope.row.type==2">{{scope.row.shopIllegal}}</div>
				</template>
        </el-table-column>
        <el-table-column prop="wrongExplain" label="违规说明" width="180"></el-table-column>
        <el-table-column prop="resultHanding" label="处理结果" width="120" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.complaintState=='5'">待审核</span>
            <span v-else-if="scope.row.complaintState=='6'">投诉未通过</span>
            <span v-else>投诉通过</span>
          </template>
        </el-table-column>
        <el-table-column prop="complaintTime" label="投诉时间" width="100"></el-table-column>
        <!-- <el-table-column prop="appealType" label="申诉类型" width="100">
				<template slot-scope="scope">
					<div v-if="scope.row.appealType=='1'">取消订单</div>
					<div v-if="scope.row.appealType=='2'">拒绝系统推单</div>
					<div v-if="scope.row.appealType=='3'">残损违规</div>
				</template>
			</el-table-column> -->
        <el-table-column prop="appealMessage" label="申诉理由" width="200" align="center">
        </el-table-column>

        <el-table-column prop="appealTime" label="申诉时间" width="100"></el-table-column>
        <el-table-column prop="auditMessage" label="申诉结果" width="200" align="center">
        </el-table-column>
        <el-table-column prop="complaintState" label="状态" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.complaintState=='1'">可申诉</span>
            <span v-if="scope.row.complaintState=='2'">申诉中</span>
            <span v-if="scope.row.complaintState=='3'">申诉未通过</span>
            <!-- <span v-if="scope.row.complaintState=='4'">申诉未通过</span> -->
            <span v-if="scope.row.complaintState=='4'">申诉通过</span>
            <span v-if="scope.row.complaintState=='5'">待审核</span>
            <span v-if="scope.row.complaintState=='6'">投诉未通过</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" v-if="scope.row.complaintState=='5'" @click="refuseClick(scope.row)"
              :disabled="!isAuth('business:jujue')">拒绝投诉
            </el-button>
            <!-- :disabled="!isAuth('autonym:jujue') || scope.row.checkCertification !== '0'" -->
            <el-button size="mini" type="primary" v-else @click="refuseClick(scope.row)"
              :disabled="!isAuth('business:jujue') || scope.row.complaintState != '2'">拒绝申诉
            </el-button>
            <el-button size="mini" type="primary" v-if="scope.row.complaintState=='5'" @click="passClicks1(scope.row)"
              :disabled="!isAuth('business:tongguo') || scope.row.complaintState != '5'">通过投诉
            </el-button>
            <!--  :disabled="!isAuth('autonym:tongguo') || scope.row.checkCertification !== '0'" -->
            <el-button size="mini" type="primary" v-else @click="passClicks(scope.row)"
              :disabled="!isAuth('business:tongguo') || scope.row.complaintState != '2'">通过申诉
            </el-button>



          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;float:right">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
          layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
        </el-pagination>
      </div>
      <!-- 服务拒绝弹框 -->
      <el-dialog title="服务拒绝" :visible.sync="dialogFormVisible" center>
        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
          <el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由">
          </el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="refuseto()">确 定</el-button>
        </div>
      </el-dialog>
    </el-tab-pane>
    <el-tab-pane label="投诉分类" name="second">
      <div style="float: right;margin-right:2%;">
        <el-button style="margin: 10px 0;" :disabled="!isAuth('business:add')" size="mini" type="primary"
          icon="document" @click="advertNotice">添加</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="guangaoData.list">
        <el-table-column fixed prop="id" label="编号" width="80"></el-table-column>
        <el-table-column prop="illegal" label="名称">

        </el-table-column>
        <!-- <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column> -->
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('business:update')"
              @click="advertUpdates(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" :disabled="!isAuth('business:delete')"
              @click="advertdeletes(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
          :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
          layout="total,sizes, prev, pager, next,jumper" :total="guangaoData.totalCount">
        </el-pagination>
      </div>
      <!-- 添加素材 -->
      <el-dialog title="添加" :visible.sync="dialogFormVisible2" center>

        <div style="margin-bottom: 10px;">
          <span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
          <el-input style="width:50%;" v-model="illegal" placeholder="请输入标题" type="text">
          </el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="advertNoticeTo()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 修改素材 -->
      <el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
        <el-form :model="form">
          <el-form-item label="标题：" :label-width="formLabelWidth">
            <el-input style="width:90%;" v-model="form.illegal" placeholder="请输入标题" type="text"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="amendadvertTo()">确 定</el-button>
        </div>
      </el-dialog>
    </el-tab-pane>
  </el-tabs>
</template>
<script>
  export default {
    data() {
      return {
        page: 1,
        limit: 10,
        classify: 1,
        indentNumber: '',
        imageUrl: '',
        url: '',
        complaintState: '',
        activeName: 'first',
        tableDataLoading: true,
        dialogFormVisible: false,
        dialogFormVisible2: false,
        tableData: [],
        checkBoxData: [], //多选框选择的值
        statesnum: [{
            label: '全部',
            value: ''
          },
          {
            label: '可申诉',
            value: 1
          },
          {
            label: '申诉中',
            value: 2
          },
          {
            label: '申诉未通过',
            value: 3
          },
          {
            label: '申诉通过',
            value: 4
          },
          {
            label: '待审核',
            value: 5
          },
          {
            label: '投诉未通过',
            value: 6
          },
        ],
        content: '',
        status: 1,
        helpTakeId: '',
        indentNumbers: '',
        complaintId: '',
        message: '',
        title: '',
        type: 3,
        form: {
          id: '',
          illegal: ''
        },
        formLabelWidth: '200px',
        dialogFormVisible1: false,
        guangaoData: {},
        illegal: '',
        illegalId: '',
        illegalList: [],
      }
    },
    methods: {
      // 多选
      changeFun(val) {
        this.checkBoxData = val
      },
      //处理默认选中当前日期
      // getNowTime () {
      // 	var now = new Date()
      // 	var year = now.getFullYear() //得到年份
      // 	var month = now.getMonth() //得到月份
      // 	var date = now.getDate() //得到日期
      // 	var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
      // 	var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
      // 	var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
      // 	month = month + 1
      // 	month = month.toString().padStart(2, '0')
      // 	date = date.toString().padStart(2, '0')
      // 	var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
      // 	return defaultDate
      // 	this.$set(this.info, 'stockDate', defaultDate)
      // },
      // tabs切换
      handleClick(tab, event) {
        if (tab._props.label == '投诉列表') {
          this.page = 1
          this.limit = 10
          this.dataSelect()
        }
        if (tab._props.label == '投诉分类') {
          this.page = 1
          this.limit = 10
          this.guangaoSelect()
        }
      },
      handleSizeChange(val) {
        this.limit = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      handleSizeChange1(val) {
        this.limit = val
        this.guangaoSelect()
      },
      handleCurrentChange1(val) {
        this.page = val
        this.guangaoSelect()
      },
      // 查询资源列表
      select() {
        this.page = 1
        this.limit = 10
        this.dataSelect()
      },
      // 重置资源列表
      cleans() {
        this.indentNumber = ''
        this.complaintState = ''
        this.illegalId = ''
        this.page = 1
        this.dataSelect()
      },
      // select选择事件
      animeDat(state) {
        this.page = 1
        this.complaintState = state
        console.log(state)
        this.dataSelect()
      },
      animeDat1(state) {
        this.page = 1
        this.illegalId = state
        this.dataSelect()
      },

      // 删除banner图
      deletes(row) {
        let delid = row.ordersId
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`order/deleteOrders?ids=${delid}`),
            method: 'get',
            data: this.$http.adornData({})
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
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/errandComplaint/findAllAppeal'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'indentNumber': this.indentNumber,
            'complaintState': this.complaintState,
            'illegalId': this.illegalId
          })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            this.tableDataLoading = false
            let returnData = data.data
            this.tableData = returnData
          }
        })
      },
      // 详情跳转
      updates(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.riderUserId
          }
        })
      },
      //服务拒绝
      refuseClick(rows) {
        console.log(rows.indentNumber, rows.complaintState, '1111111111')
        if (rows.complaintState == '2') {
          this.status = 3
        }
        if (rows.complaintState == '5') {
          this.status = 6
        }

        this.helpTakeId = rows.riderUserId
        this.content = ''
        this.indentNumbers = rows.indentNumber
        this.complaintId = rows.complaintId
        this.dialogFormVisible = true
      },
      refuseto() {
        if (this.content == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入拒绝理由',
            type: 'warning'
          });
          return
        }
        if (this.status == 3) {
          this.$http({
            url: this.$http.adornUrl(
              `admin/errandComplaint/updateComplaintState`
            ),
            method: 'post',
            data: this.$http.adornData({
              'riderUserId': this.helpTakeId,
              'indentNumber': this.indentNumbers,
              'complaintState': this.status,
              'auditMessage': this.content,
              'complaintId': this.complaintId
            })
          }).then(({
            data
          }) => {
            this.dialogFormVisible = false
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = -1
                this.dataSelect()
              }
            })
          })
        }
        if (this.status == 6) {
          this.$http({
            url: this.$http.adornUrl('admin/errandComplaint/updateComplaintState'),
            method: 'get',
            data: this.$http.adornData({
              'complaintState': this.status,
              'auditMessage': '驳回投诉',
              'complaintId': this.complaintId
            })
          }).then(({
            data
          }) => {
            if (data.code == 0) {
              this.dialogFormVisible = false
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.content = ''
                  this.status = -1
                  this.dataSelect()
                }
              })
            } else {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.content = ''
                  this.status = -1
                  this.dataSelect()
                }
              })
            }

          })
        }

      },
      //单条服务通过
      passClicks(row) {
        console.log(row.indentNumber, row, '22222222')
        this.status = 4
        this.content = '同意'
        this.$http({
          url: this.$http.adornUrl(
            `admin/errandComplaint/updateComplaintState`),
          method: 'post',
          data: this.$http.adornData({
            'riderUserId': row.riderUserId,
            'indentNumber': row.indentNumber,
            'complaintState': this.status,
            'auditMessage': '审核通过',
            'complaintId': row.complaintId
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
                this.content = ''
                this.status = -1
                this.dataSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = -1
                this.dataSelect()
              }
            })
          }

        })
      },
      //单条投诉通过
      passClicks1(row) {
        console.log(row.indentNumber, row, '22222222')
        this.status = 1
        this.content = '同意'
        this.$http({
          url: this.$http.adornUrl('admin/errandComplaint/updateComplaintState'),
          method: 'post',
          data: this.$http.adornData({
            'complaintState': this.status,
            'auditMessage': '审核通过',
            'complaintId': row.complaintId
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
                this.content = ''
                this.status = -1
                this.dataSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.content = ''
                this.status = -1
                this.dataSelect()
              }
            })
          }

        })
      },
      // 添加素材弹框
      advertNotice() {
        this.dialogFormVisible2 = true
      },
      // 添加
      advertNoticeTo() {
        if (this.illegal == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入名称',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`admin/illegalType/insertIllegalType`),
          method: 'post',
          data: this.$http.adornData({
            'illegal': this.illegal
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
              this.illegal = ''
              this.guangaoSelect()
            }
          })
        })
      },
      // 点击修改
      advertUpdates(rows) {
        this.form.id = rows.id
        this.form.illegal = rows.illegal
        this.dialogFormVisible1 = true
      },
      // 确定修改
      amendadvertTo() {
        if (this.form.illegal == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入名称',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('admin/illegalType/updateIllegalType'),
          method: 'post',
          data: this.$http.adornData({
            'id': this.form.id,
            'illegal': this.form.illegal,
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
                this.guangaoSelect()
                this.dialogFormVisible1 = false
              }
            })
          } else {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: data.msg,
              type: 'warning'
            })
          }

        })
      },
      // 删除广告
      advertdeletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`admin/illegalType/deleteIllegalType`),
            method: 'get',
            params: this.$http.adornParams({
              'id': row.id
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
                  this.guangaoSelect()
                }
              })
            } else {
              this.$notify({
                title: '提示',
                duration: 1800,
                message: data.msg,
                type: 'warning'
              })
            }

          })
        })
      },
      // 获取素材数据
      guangaoSelect() {
        let page = this.page
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/illegalType/selectIllegalTypePage'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.guangaoData = returnData
          if (this.guangaoData.list.length == 0) {
            this.page = this.page - 1
            this.guangaoSelect()
          }
        })
      },
      // /
      guangaoSelect1() {
        let page = this.page
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/illegalType/selectIllegalTypeList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.illegalList = returnData

        })
      },
    },
    mounted() {
      this.dataSelect()
      this.guangaoSelect1()
    }
  }
</script>

<style>
  .customWidth {
    width: 80% !important;
  }
</style>
