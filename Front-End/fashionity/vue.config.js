const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true,
//   devServer: {
//     proxy: {
//       '/vue': { 
//         target: 'http://localhost:8080',
//         changeOrigin: true,
//       }, 
//     },
//   },
// })
