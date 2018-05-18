var webpack = require('webpack');  
module.exports = {
  mode: 'development',
  entry: [
    __dirname + '/project/static/scripts/jsx'
  ],
  output: {
    path: __dirname + '/project/static/scripts/js',
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