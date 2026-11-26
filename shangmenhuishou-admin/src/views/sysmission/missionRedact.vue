<template>
  <div class="mission-redact-page">
    <div class="nf-page-hero mission-redact-page__hero">
      <span class="nf-page-hero__badge">服务管理</span>
      <h2 class="nf-page-hero__title">修改服务</h2>
      <p class="nf-page-hero__desc">编辑服务标题、分类、幸运值与标题图片，修改富文本服务内容</p>
    </div>

    <div class="nf-filter-card mission-redact-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-operation"></i>
        <span>基本信息</span>
      </div>
      <div class="nf-filter-card__body mission-redact-page__filter-body">
        <div class="mission-redact-page__row">
          <span class="mission-redact-page__label">服务标题：</span>
          <div class="mission-redact-page__control">
            <el-input class="mission-redact-page__input" v-model="title" autosize></el-input>
          </div>
        </div>
        <div class="mission-redact-page__row">
          <span class="mission-redact-page__label">服务分类：</span>
          <div class="mission-redact-page__control">
            <el-select v-model="classifyId" placeholder="请选择服务分类" class="mission-redact-page__input">
              <el-option v-for="(item,index) in artiCletypes" :key="item.index" :label="item.classifyName" :value="item.id">
              </el-option>
            </el-select>
          </div>
        </div>
        <div class="mission-redact-page__row">
          <span class="mission-redact-page__label">幸运值：</span>
          <div class="mission-redact-page__control">
            <el-input class="mission-redact-page__input" type="number" v-model="luckyValue" autosize></el-input>
          </div>
        </div>
        <div class="mission-redact-page__row mission-redact-page__row--top">
          <span class="mission-redact-page__label">标题图片：</span>
          <div class="mission-redact-page__control">
            <el-upload
              v-model="titlePicture"
              :class="{hide:hideUpload}"
              :action="$http.adornUrl('file/upload')"
              :limit="1"
              :on-success="handleUploadSuccess"
              list-type="picture-card"
              :on-remove="handleRemove"
              :on-change="handleChange"
            >
              <img v-if="titlePicture" :src="titlePicture" class="mission-redact-page__upload-img"/>
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
        </div>
      </div>
    </div>

    <div class="nf-table-card mission-redact-page__content-card">
      <div class="nf-filter-card__head">
        <i class="el-icon-document"></i>
        <span>服务内容</span>
      </div>
      <div class="mission-redact-page__editor-wrap tinymce-content">
        <textarea id="tinymceId" :value="content" class="tinymce-textarea"/>
      </div>
      <div class="mission-redact-page__footer">
        <el-button size="mini" type="primary" icon="document" @click="artiReleass">修改服务</el-button>
      </div>
    </div>
  </div>
</template>

<script>
    import load from './tinymce-components/dynamicLoadScript'
    import toolbar from './tinymce-components/toolbar'
    import plugins from './tinymce-components/plugins'

    const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

    export default {
        name: 'Tinymce',
        data() {
            return {
                classifyId: '',
                title: '',
                luckyValue: '',
				titlePicture:'',
				isShow: false,
				hideUpload: false,
				limit:1,
				userId:'',
				id:'',
                hasChange: false,
                hasInit: false,
                tinymceId: 'tinymceId',
                height: 235,
                toolbar: [],
                menubar: 'file edit insert view format table',
                languageTypeList: {
                    'en': 'en',
                    'zh': 'zh_CN'
                },
				artiCletypes: [],
                // 富文本内容
                content: ''
            }
        },
        watch: {
            content(val) {
                if (!this.hasChange && this.hasInit) {
                    this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ''))
                }
            }
        },
        methods: {
			handleChange(file, fileList){
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			//上传成功
			handleUploadSuccess(file,fileList) {
			  this.titlePicture= file.data.path
			},
			// 修改服务
			artiReleass(){
				if (this.title == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入服务标题',
				        type: 'warning'
				    });
				    return
				}
				if (this.classifyId == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择服务分类',
				        type: 'warning'
				    });
				    return
				}
				if (this.luckyValue == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择服务幸运值',
				        type: 'warning'
				    });
				    return
				}
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择服务内容',
				        type: 'warning'
				    });
				    return
				}
				let id = this.$route.query.id
				this.$http({
				  url: this.$http.adornUrl('helpTaskPlatform/updateHelpTaskPlatform'),
				  method: 'post',
				  data: this.$http.adornData({
					 'id':id,
				    'title':this.title,
					'state':this.state,
				    'classifyId':this.classifyId,
				    'content':this.content,
				    'luckyValue':this.luckyValue,
				    'titlePicture':this.titlePicture,
					'createTime' : this.createTime
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '修改成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.$router.push({path: '/missionsye'});
				      }
				    })
				})
			},
            init() {
                load(tinymceCDN, (err) => {
                    if (err) {
                        this.$message.error(err.message)
                        return
                    }
                    this.initTinymce()
                })
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTaskPlatform/selectHelpTaskPlatformById'),
					method: 'get',
					params: this.$http.adornParams({
						'id':id
					})
				}).then(({data}) => {
					let returnData = data.data;
					this.title = returnData.title
					this.classifyId = returnData.classifyId
					this.title = returnData.title
					this.state = returnData.state
					this.content = returnData.content
					this.titlePicture = returnData.titlePicture
					this.luckyValue = returnData.luckyValue
					this.createTime = returnData.createTime
				})
            },

            initTinymce() {
                const _this = this
                window.tinymce.init({
                    selector: `#${this.tinymceId}`,
                    images_upload_handler: function (blobInfo, success, failure) {
                        var xhr, formData;

                        xhr = new XMLHttpRequest();
                        xhr.withCredentials = false;
                        xhr.open('POST', this.$http.adornUrl('file/upload'));
                        xhr.onload = function () {
                            var json;

                            if (xhr.status != 200) {
                                failure('HTTP Error: ' + xhr.status);
                                return;
                            }
                            json = xhr.responseText;
                            if (json == "" || json == null) {
                                failure('Invalid JSON: ' + xhr.responseText);
                                return;
                            }
                            success(json);
                        };
                        formData = new FormData();
                        formData.append('file', blobInfo.blob(), blobInfo.filename());
                        xhr.send(formData);
                    },
                    language: this.languageTypeList['zh'],
                    height: this.height,
                    body_class: 'panel-body ',
                    object_resizing: false,
                    toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
                    plugins,
                    menubar: this.menubar,
                    end_container_on_empty_block: true,
                    powerpaste_word_import: 'clean',
                    code_dialog_height: 450,
                    code_dialog_width: 1000,
                    advlist_bullet_styles: 'square',
                    advlist_number_styles: 'default',
                    imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
                    default_link_target: '_blank',
                    link_title: false,
                    // Image
                    imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
                    nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
                    init_instance_callback: editor => {
                        if (_this.content) {
                            editor.setContent(_this.content)
                        }
                        _this.hasInit = true
                        editor.on('NodeChange Change KeyUp SetContent', () => {
                            this.hasChange = true
                            this.content = editor.getContent()
                        })
                    },
                    setup(editor) {
                        editor.on('FullscreenStateChanged', (e) => {
                            _this.fullscreen = e.state
                        })
                    }
                })
            },
            destroyTinymce() {
                const tinymce = window.tinymce.get(this.tinymceId)
                if (this.fullscreen) {
                    tinymce.execCommand('mceFullScreen')
                }
                if (tinymce) {
                    tinymce.destroy()
                }
            },
			// 获取分类
			classifySelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('platformClassify/selectClassifyList'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.artiCletypes = returnData
			  })
			},
        },
        mounted() {
            this.init()
			this.classifySelect()
        },
        beforeDestroy() {
            this.destroyTinymce()
        },
        activated() {
            if (window.tinymce) {
                this.initTinymce()
            }
        },
        deactivated() {
            this.destroyTinymce()
        }
    }
</script>

<style lang="scss" scoped>
.mission-redact-page {
  padding: 0;
  overflow-x: hidden;
}

.mission-redact-page__hero {
  margin-bottom: 12px;
}

.mission-redact-page__filter {
  margin-bottom: 12px;
}

.mission-redact-page__filter-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.mission-redact-page__row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mission-redact-page__row--top {
  align-items: flex-start;
}

.mission-redact-page__label {
  flex: 0 0 100px;
  text-align: right;
  font-size: 13px;
  color: var(--nf-text-secondary, #5a6578);
  line-height: 32px;
}

.mission-redact-page__row--top .mission-redact-page__label {
  line-height: 1.5;
  padding-top: 6px;
}

.mission-redact-page__control {
  flex: 1;
  min-width: 0;
}

.mission-redact-page__input {
  width: 280px;
  max-width: 100%;
}

.mission-redact-page__upload-img {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  object-fit: cover;
}

.mission-redact-page__content-card {
  margin-bottom: 0;
  padding: 16px;
}

.mission-redact-page__editor-wrap {
  position: relative;
  line-height: normal;
  margin-bottom: 12px;
}

.mission-redact-page__footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid var(--nf-border, #e8eaed);
}
</style>

<style>
.mission-redact-page .tinymce-textarea {
  visibility: hidden;
  z-index: -1;
}

.mission-redact-page .hide .el-upload--picture-card {
  display: none;
}

.mission-redact-page .mce-fullscreen {
  z-index: 10000;
}
</style>
