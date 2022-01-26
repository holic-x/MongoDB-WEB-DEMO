import Mock from 'mockjs'

// 登录
export function login () {
  return {
    // isOpen: false,
    url: '/sys/login',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0,
      'expire': Mock.Random.natural(60 * 60 * 1, 60 * 60 * 12),
      'token': Mock.Random.string('abcdefghijklmnopqrstuvwxyz0123456789', 32)
    }
  }
}

// 退出
export function logout () {
  return {
    // isOpen: false,
    url: '/sys/logout',
    type: 'post',
    data: {
      'msg': 'success',
      'code': 0
    }
  }
}

// 获取登录用户信息
export function getLoginUser () {
  return {
    // isOpen: false,
    url: '/login/getLoginUser',
    type: 'get',
    data: {
      'msg': 'success',
      'code': 0,
      'user': {
        "userId":"1",
        "userName":"testUser"
      }
    }
  }
}
