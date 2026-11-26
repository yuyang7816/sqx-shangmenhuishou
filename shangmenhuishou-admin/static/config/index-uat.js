/**
 * 验收环境
 */
;(function () {
  function getBasePath () {
    var currentScript = document.currentScript
    var scriptSrc = currentScript && currentScript.src

    if (!scriptSrc) {
      var scripts = document.getElementsByTagName('script')
      scriptSrc = scripts.length ? scripts[scripts.length - 1].src : window.location.href
    }

    return scriptSrc.replace(/\/(?:static\/)?config\/index\.js(?:\?.*)?$/, '')
  }

  window.SITE_CONFIG = {};

  // api接口请求地址
  window.SITE_CONFIG['baseUrl'] = 'http://localhost:8080/sz-fast';

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain']  = getBasePath(); // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.version ? (window.SITE_CONFIG.domain + '/' + window.SITE_CONFIG.version) : window.SITE_CONFIG.domain;
})();
