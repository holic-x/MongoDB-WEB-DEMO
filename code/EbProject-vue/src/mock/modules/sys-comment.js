import Mock from 'mockjs'

// 生成数据列表
var dataList = []
for (let i = 0; i < Math.floor(Math.random() * 10 + 1); i++) {
  dataList.push(Mock.mock({
    'commentId': '@increment',
    'productName': '@first',
    'content': '@csentence',
    'commentor': '@last',
    'commentTime': '@time'
  }))
}

// 获取参数列表
export function list () {
  return {
    // isOpen: false,
    url: '/sys/comment/list',
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

// 删除参数
export function del () {
  return {
    // isOpen: false,
    url: '/sys/comment/delete',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0
    }
  }
}
