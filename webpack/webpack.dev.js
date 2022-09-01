'use strict';
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const webpack = require('webpack');

const { styleLoaders } = require('./vue.utils');
const config = require('./config');

module.exports = (env, options) => {
  const devConfig = {
    module: {
      rules: styleLoaders({ sourceMap: config.dev.cssSourceMap, usePostCSS: true }),
    },
    // cheap-module-eval-source-map is faster for development
    devtool: config.dev.devtool,
    optimization: {
      moduleIds: 'named',
    },
    plugins: [],
  };
  if (!options.env.WEBPACK_SERVE) return devConfig;
  devConfig.plugins.push(
    new BrowserSyncPlugin(
      {
        https: false, // cambiar estado para activar o descativar el tls
        host: 'localhost',
        // host: '192.168.10.11',
        // cambiar siempre por puerto 9000
        port: 9000,
        proxy: {
          target: `http://localhost:${options.watch ? '8081' : '9060'}`,
          // target: `http://192.168.10.11:${options.watch ? '8081' : '9060'}`,
          ws: true,
        },
        socket: {
          clients: {
            heartbeatTimeout: 60000,
          },
        },
        /*
        ,ghostMode: { // uncomment this part to disable BrowserSync ghostMode; https://github.com/jhipster/generator-jhipster/issues/11116
          clicks: false,
          location: false,
          forms: false,
          scroll: false
        } */
      },
      {
        reload: true,
      }
    )
  );
  return devConfig;
};
