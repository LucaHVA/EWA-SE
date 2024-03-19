module.exports = {
  chainWebpack: config => {
    // Add a new rule for handling SVG files
    config.module
        .rule('svg')
        .test(/\.svg$/)
        .use('vue-svg-loader')
        .loader('vue-svg-loader');
  }
};
