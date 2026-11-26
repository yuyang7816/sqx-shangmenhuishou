var gulp = require('gulp');
var $    = require('gulp-load-plugins')();
var fs   = require('fs');
var path = require('path');
var del  = require('del');
var spawnSync = require('child_process').spawnSync;

var distPath    = path.resolve('./dist');
var env         = ''; // 运行环境

// 编译
gulp.task('build', function (done) {
  var result = spawnSync(process.execPath, ['build/build.js'], { stdio: 'inherit' });

  if (result.status !== 0) {
    done(new Error('build/build.js 执行失败'));
    return;
  }

  done();
});

// 替换dist/static/js/manifest.js中静态资源前缀
gulp.task('replace:cdnUrl', ['build'], function () {
  return gulp.src(`${distPath}/static/js/manifest.js`)
    .pipe($.replace(new RegExp(`"${require('./config').build.assetsPublicPath}"`, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"'))
    .pipe(gulp.dest(`${distPath}/static/js/`))
  });

// 合并dist/static/config/[index-${env}, init].js 至 dist/config/index.js
gulp.task('concat:config', ['replace:cdnUrl'], function () {
  return gulp.src([`${distPath}/static/config/index-${env}.js`, `${distPath}/static/config/init.js`])
    .pipe($.concat('index.js'))
    .pipe(gulp.dest(`${distPath}/config/`))
});

gulp.task('postbuild', ['build'], function (done) {
  var manifestPath = path.join(distPath, 'static/js/manifest.js')
  var envConfigPath = path.join(distPath, `static/config/index-${env}.js`)
  var initConfigPath = path.join(distPath, 'static/config/init.js')
  var outputConfigDir = path.join(distPath, 'config')
  var outputConfigPath = path.join(outputConfigDir, 'index.js')

  var manifestContent = fs.readFileSync(manifestPath, 'utf8')
  manifestContent = manifestContent.replace(new RegExp(`"${require('./config').build.assetsPublicPath}"`, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"')
  fs.writeFileSync(manifestPath, manifestContent)

  if (!fs.existsSync(outputConfigDir)) {
    fs.mkdirSync(outputConfigDir)
  }

  var envConfigContent = fs.readFileSync(envConfigPath, 'utf8')
  var initConfigContent = fs.readFileSync(initConfigPath, 'utf8')
  fs.writeFileSync(outputConfigPath, `${envConfigContent}\n${initConfigContent}`)

  done()
})

// 清空
gulp.task('clean', function () {
  return del([`${distPath}/config/index.js`])
});

gulp.task('default', ['clean'], function () {
  // 获取环境配置
  env = process.env.npm_config_qa ? 'qa' : process.env.npm_config_uat ? 'uat' : 'prod'
  // 开始打包编译
  gulp.start(['postbuild'])
});
