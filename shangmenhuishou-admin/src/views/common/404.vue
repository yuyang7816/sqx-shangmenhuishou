<template>
  <div class="site-wrapper site-page--not-found nf-404">
    <div class="nf-404__center">
      <div class="nf-404__card">
        <div class="nf-404__code">404</div>
        <span class="nf-404__badge">页面未找到</span>
        <h2 class="nf-404__title">抱歉，页面走丢了</h2>
        <p class="nf-404__desc">
          您访问的页面<em>失联</em>啦，可能已被移除或地址有误
        </p>
        <div class="nf-404__actions">
          <el-button @click="$router.go(-1)">返回上一页</el-button>
          <el-button type="primary" @click="$router.push({ name: 'home' })">进入首页</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Page404'
}
</script>

<style lang="scss">
.site-wrapper.site-page--not-found.nf-404 {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  background: var(--nf-page-bg, #f4f6fa);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;

  .nf-404__center {
    width: 100%;
    max-width: 480px;
  }

  .nf-404__card {
    position: relative;
    padding: 40px 32px 32px;
    text-align: center;
    background: #fff;
    border: 1px solid var(--nf-border, #e8eaed);
    border-radius: var(--nf-radius-md, 8px);
    box-shadow: var(--nf-shadow-card, 0 1px 4px rgba(0, 0, 0, 0.06));
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 3px;
      background: linear-gradient(90deg, var(--nf-brand, #01af74) 0%, var(--nf-brand-deep, #017c52) 100%);
    }

    &::after {
      content: '';
      position: absolute;
      left: 32px;
      right: 32px;
      bottom: 0;
      height: 2px;
      background: linear-gradient(90deg, var(--nf-brand, #01af74), transparent);
      border-radius: 1px;
    }
  }

  .nf-404__code {
    margin: 0 0 16px;
    font-size: 72px;
    font-weight: 700;
    line-height: 1;
    letter-spacing: 4px;
    color: var(--nf-brand, #01af74);
    text-shadow: 0 4px 16px rgba(1, 175, 116, 0.15);
  }

  .nf-404__badge {
    display: inline-flex;
    align-items: center;
    padding: 2px 8px;
    margin-bottom: 12px;
    font-size: 12px;
    font-weight: 500;
    color: var(--nf-brand, #01af74);
    background: var(--nf-brand-lite, #e1f5ee);
    border-radius: var(--nf-radius-sm, 6px);
  }

  .nf-404__title {
    margin: 0 0 8px;
    font-size: 20px;
    font-weight: 600;
    color: var(--nf-text-primary, #303133);
  }

  .nf-404__desc {
    margin: 0 0 28px;
    font-size: 14px;
    line-height: 1.6;
    color: var(--nf-text-secondary, #909399);

    > em {
      font-style: normal;
      font-weight: 500;
      color: var(--nf-brand, #01af74);
    }
  }

  .nf-404__actions {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    flex-wrap: wrap;

    .el-button {
      min-width: 120px;
      border-radius: var(--nf-radius-sm, 6px);
    }
  }
}

@media (max-width: 480px) {
  .site-wrapper.site-page--not-found.nf-404 {
    .nf-404__card {
      padding: 32px 20px 24px;
    }

    .nf-404__code {
      font-size: 56px;
    }

    .nf-404__actions {
      flex-direction: column;

      .el-button {
        width: 100%;
        margin-left: 0 !important;
      }
    }
  }
}
</style>
