<template>
  <div class="site-wrapper site-page--login login-page">
    <div class="nf-login">
      <div class="nf-login__container">
        <div class="nf-login__brand">
          <div class="nf-login__brand-inner">
            <img class="nf-login__logo" src="~@/assets/img/avatar.png" alt="logo">
            <h1 class="nf-login__brand-title">{{ entryName }}</h1>
            <p class="nf-login__brand-desc">高效、安全的管理平台</p>
            <div class="nf-login__brand-features">
              <div class="nf-login__brand-feature">
                <i class="nf-login__feature-dot"></i>
                <span>数据安全</span>
              </div>
              <div class="nf-login__brand-feature">
                <i class="nf-login__feature-dot"></i>
                <span>高效运营</span>
              </div>
              <div class="nf-login__brand-feature">
                <i class="nf-login__feature-dot"></i>
                <span>智能管理</span>
              </div>
            </div>
          </div>
        </div>
        <div class="nf-login__panel">
          <div class="nf-login__card">
            <h3 class="nf-login__title">账号登录</h3>
            <p class="nf-login__subtitle">欢迎回来，请登录您的管理账号</p>
            <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
              <el-form-item prop="userName">
                <el-input v-model="dataForm.userName" placeholder="帐号">
                  <icon-svg slot="prefix" name="yonghu" class="nf-login__input-icon"></icon-svg>
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="dataForm.password" type="password" placeholder="密码">
                  <icon-svg slot="prefix" name="suoding" class="nf-login__input-icon"></icon-svg>
                </el-input>
              </el-form-item>
              <el-form-item prop="captcha">
                <el-row :gutter="12">
                  <el-col :span="14">
                    <el-input v-model="dataForm.captcha" placeholder="验证码">
                      <icon-svg slot="prefix" name="duanxin" class="nf-login__input-icon"></icon-svg>
                    </el-input>
                  </el-col>
                  <el-col :span="10" class="login-captcha">
                    <img :src="captchaPath" @click="getCaptcha()" alt="验证码">
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item>
                <el-button class="login-btn-submit nf-login-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { getUUID } from '@/utils'
	import { entryName } from '@/utils/httpRequest'
  export default {
    data () {
      return {
        dataForm: {
          userName: '',
          password: '',
          uuid: '',
          captcha: ''
        },
        dataRule: {
          userName: [
            { required: true, message: '帐号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          captcha: [
            { required: true, message: '验证码不能为空', trigger: 'blur' }
          ]
        },
        captchaPath: '22',
				entryName:'',
      }
    },
    created () {
			this.entryName = entryName
      this.getCaptcha()
    },
    methods: {
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('sys/login'),
              method: 'post',
              data: this.$http.adornData({
                'username': this.dataForm.userName,
                'password': this.dataForm.password,
                'uuid': this.dataForm.uuid,
                'captcha': this.dataForm.captcha
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$cookie.set('token', data.data.token)
                this.$router.replace({ name: 'home' })
              } else {
                this.getCaptcha()
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      getCaptcha () {
        this.dataForm.uuid = getUUID()
        this.captchaPath = this.$http.adornUrl(`captcha.jpg?uuid=${this.dataForm.uuid}`)
      }
    }
  }
</script>

<style lang="scss">
  .site-wrapper.site-page--login {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: auto;
    background-color: var(--nf-brand, #01af74);
    background-image:
      radial-gradient(ellipse 85% 70% at 0% 0%, rgba(225, 245, 238, 0.92) 0%, rgba(111, 214, 173, 0.55) 36%, transparent 72%),
      radial-gradient(ellipse 75% 60% at 100% 100%, rgba(1, 175, 116, 0.45) 0%, rgba(1, 124, 82, 0.35) 42%, transparent 68%),
      linear-gradient(
        120deg,
        var(--nf-brand-lite, #e1f5ee) 0%,
        #8fe0c3 22%,
        var(--nf-brand, #01af74) 52%,
        #03a872 82%,
        #029a68 100%
      );
    background-repeat: no-repeat;
    background-size: cover;
    background-attachment: fixed;

    .nf-login {
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100vh;
      padding: 32px 24px;
      box-sizing: border-box;
    }

    .nf-login__container {
      display: flex;
      width: 100%;
      max-width: 920px;
      min-height: 520px;
      background: var(--nf-page-bg, #f4f6fa);
      border-radius: var(--nf-radius-lg, 10px);
      box-shadow: 0 16px 48px rgba(1, 124, 82, 0.18);
      border: 1px solid rgba(255, 255, 255, 0.72);
      overflow: hidden;
    }

    .nf-login__brand {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(
        160deg,
        var(--nf-brand, #01af74) 0%,
        var(--nf-brand-deep, #017c52) 100%
      );
      padding: 48px 40px;
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: -80px;
        right: -80px;
        width: 320px;
        height: 320px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.08);
      }

      &::after {
        content: '';
        position: absolute;
        bottom: -120px;
        left: -60px;
        width: 400px;
        height: 400px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.05);
      }
    }

    .nf-login__brand-inner {
      position: relative;
      z-index: 1;
      text-align: center;
      color: #fff;
      max-width: 360px;
    }

    .nf-login__brand-features {
      display: flex;
      justify-content: center;
      gap: 24px;
      margin-top: 32px;
      padding-top: 24px;
      border-top: 1px solid rgba(255, 255, 255, 0.15);
    }

    .nf-login__brand-feature {
      text-align: center;

      .nf-login__feature-dot {
        display: block;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.9);
        margin: 0 auto 8px;
      }

      span {
        font-size: 12px;
        opacity: 0.75;
        letter-spacing: 0.5px;
      }
    }

    .nf-login__logo {
      width: 72px;
      height: 72px;
      border-radius: 16px;
      object-fit: cover;
      margin-bottom: 24px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    }

    .nf-login__brand-title {
      font-size: 28px;
      font-weight: 600;
      margin: 0 0 12px;
      letter-spacing: 0.5px;
    }

    .nf-login__brand-desc {
      font-size: 15px;
      opacity: 0.85;
      margin: 0;
      line-height: 1.6;
    }

    .nf-login__panel {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 40px 32px;
      background: var(--nf-card-bg, #ffffff);
    }

    .nf-login__card {
      width: 100%;
      max-width: 360px;
      padding: 8px 4px 4px;
      background: transparent;
      border: none;
      box-shadow: none;
      position: relative;
      overflow: visible;

      &::before {
        content: '';
        position: absolute;
        top: -40px;
        left: 0;
        right: 0;
        height: 3px;
        border-radius: 999px;
        background: linear-gradient(
          90deg,
          var(--nf-brand, #01af74) 0%,
          var(--nf-brand-deep, #017c52) 100%
        );
      }
    }

    .nf-login__title {
      font-size: 22px;
      font-weight: 600;
      color: var(--nf-text-primary, #1a2433);
      margin: 0 0 8px;
      text-align: left;
    }

    .nf-login__subtitle {
      font-size: 13px;
      color: var(--nf-text-secondary, #909399);
      margin: 0 0 24px;
    }

    .el-form-item {
      margin-bottom: 20px;
    }

    .el-input__inner {
      height: 40px;
      line-height: 40px;
      border-radius: 8px;
      border-color: var(--nf-border, #e8eaed);
      transition: border-color 0.2s, box-shadow 0.2s;

      &:focus {
        border-color: var(--nf-brand, #01af74);
        box-shadow: 0 0 0 2px rgba(1, 175, 116, 0.12);
      }
    }

    .el-input__prefix {
      left: 12px;
      color: var(--nf-text-secondary, #909399);
    }

    .el-input--prefix .el-input__inner {
      padding-left: 36px;
    }

    .nf-login__input-icon {
      width: 16px;
      height: 16px;
      margin: 12px 0 0 12px;
      color: var(--nf-brand, #01af74);
    }

    .login-captcha {
      overflow: hidden;
      height: 40px;
      border-radius: 4px;
      border: 1px solid var(--nf-border, #e8eaed);

      > img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        cursor: pointer;
        display: block;
      }
    }

    .login-btn-submit {
      width: 100%;
      height: 44px;
      margin-top: 4px;
      font-size: 15px;
      font-weight: 500;
      border-radius: 8px;
      letter-spacing: 2px;
      transition: background-color 0.2s, border-color 0.2s, transform 0.15s;

      &:active {
        transform: scale(0.98);
      }
    }

    @media (max-width: 860px) {
      .nf-login {
        padding: 20px 16px;
      }

      .nf-login__container {
        flex-direction: column;
        min-height: auto;
      }

      .nf-login__brand {
        flex: none;
        padding: 32px 24px;
      }

      .nf-login__brand-features {
        display: none;
      }

      .nf-login__brand-title {
        font-size: 22px;
      }

      .nf-login__logo {
        width: 56px;
        height: 56px;
        margin-bottom: 16px;
      }

      .nf-login__panel {
        flex: none;
        padding: 28px 20px 32px;
      }

      .nf-login__card {
        max-width: none;
        padding: 0;

        &::before {
          top: -24px;
        }
      }
    }
  }
</style>
