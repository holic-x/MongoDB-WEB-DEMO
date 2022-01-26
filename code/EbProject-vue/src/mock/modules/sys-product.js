import Mock from 'mockjs'

// 生成数据列表
var dataList = []
for (let i = 0; i < Math.floor(Math.random() * 10 + 1); i++) {
  dataList.push(Mock.mock({
    'productId': '@increment',
    'productNum': '@increment',
    'picUrl': '@increment',
    'classify': '@increment',
    'productName': '@first',
    'stock': '@increment',
    'vendor': '@last',
    'remark': '@csentence',
    'createBy': '@first',
    'modifyBy': '@first',
    'createTime': '@time',
    'modifyTime': '@time'
  }))
}

// 获取商品列表
export function list () {
  return {
    // isOpen: false,
    url: '/sys/product/list',
    type: 'get',
    data: {
      'msg': 'success',
      'code': 0,
      'page': {
        'totalCount': dataList.length,
        'pageSize': 10,
        'totalPage': 1,
        'currPage': 1,
        'list': dataList
      }
    }
  }
}

// 获取商品信息
export function info () {
  return {
    // isOpen: false,
    url: '/sys/product/info',
    type: 'get',
    data: {
      'msg': 'success',
      'code': 0,
      'product': dataList[0]
    }
  }
}

// 添加商品
export function add () {
  return {
    // isOpen: false,
    url: '/sys/product/save',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0
    }
  }
}

// 修改商品
export function update () {
  return {
    // isOpen: false,
    url: '/sys/product/update',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0
    }
  }
}

// 删除商品
export function del () {
  return {
    // isOpen: false,
    url: '/sys/product/delete',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0
    }
  }
}
