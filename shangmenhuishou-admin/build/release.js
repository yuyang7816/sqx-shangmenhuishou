'use strict'

require('./check-versions')()

process.env.NODE_ENV = 'production'

const fs = require('fs')
const path = require('path')
const ora = require('ora')
const rm = require('rimraf')
const chalk = require('chalk')
const webpack = require('webpack')
const config = require('../config')
const webpackConfig = require('./webpack.prod.conf')

const distPath = path.resolve(__dirname, '../dist')
const spinner = ora('building for production...')

function getEnvName () {
  if (process.env.npm_config_qa) return 'qa'
  if (process.env.npm_config_uat) return 'uat'
  return 'prod'
}

function patchManifest () {
  const manifestPath = path.join(distPath, 'static/js/manifest.js')
  const publicPath = `"${config.build.assetsPublicPath}"`
  const manifestContent = fs.readFileSync(manifestPath, 'utf8')
  const patchedContent = manifestContent.replace(new RegExp(publicPath, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"')
  fs.writeFileSync(manifestPath, patchedContent)
}

function writeRuntimeConfig () {
  const env = getEnvName()
  const envConfigPath = path.join(distPath, `static/config/index-${env}.js`)
  const initConfigPath = path.join(distPath, 'static/config/init.js')
  const outputDir = path.join(distPath, 'config')
  const outputPath = path.join(outputDir, 'index.js')

  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true })
  }

  const envConfigContent = fs.readFileSync(envConfigPath, 'utf8')
  const initConfigContent = fs.readFileSync(initConfigPath, 'utf8')
  fs.writeFileSync(outputPath, `${envConfigContent}\n${initConfigContent}`)
}

spinner.start()

rm(path.join(config.build.assetsRoot, config.build.assetsSubDirectory), err => {
  if (err) throw err

  webpack(webpackConfig, (error, stats) => {
    spinner.stop()

    if (error) throw error

    process.stdout.write(stats.toString({
      colors: true,
      modules: false,
      children: false,
      chunks: false,
      chunkModules: false
    }) + '\n\n')

    if (stats.hasErrors()) {
      console.log(chalk.red('  Build failed with errors.\n'))
      process.exit(1)
    }

    patchManifest()
    writeRuntimeConfig()

    console.log(chalk.cyan('  Build complete.\n'))
    console.log(chalk.yellow(
      '  Tip: built files are meant to be served over an HTTP server.\n' +
      '  Opening index.html over file:// won\'t work.\n'
    ))
    process.exit(0)
  })
})
