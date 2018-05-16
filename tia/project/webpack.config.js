var webpack = require('webpack');  
module.exports = {
  entry: [
    __dirname + '/project/static/scripts/jsx/index.js'
  ],
  output: {
    path: __dirname + 'project/static/scripts/js/',
    filename: "bundle.js"
  },
  module: {
    rules: [
      {
        test: /\.js?$/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015', 'react']
        },
        exclude: [__dirname + '/node_modules']
      }
    ]
  },
  resolve: {
    extensions: ['*', '.js', '.jsx'],
  },
  plugins: [
  ]
};