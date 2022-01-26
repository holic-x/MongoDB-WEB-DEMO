// 左侧菜单栏控制
var navDataList = [
  {
    'menuId': 1,
    'parentId': 0,
    'parentName': null,
    'name': '系统管理',
    'url': null,
    'perms': null,
    'type': 0,
    'icon': 'system',
    'orderNum': 0,
    'open': null,
    'list': [
      {
        'menuId': 2,
        'parentId': 1,
        'parentName': null,
        'name': '用户列表',
        'url': 'sys/user',
        'perms': 'sys:user:list,sys:user:save,sys:user:info,,sys:user:update,sys:user:delete',
        'type': 1,
        'icon': 'admin',
        'orderNum': 1,
        'open': null,
        'list': null
      },
      {
        'menuId': 27,
        'parentId': 1,
        'parentName': null,
        'name': '商品管理',
        'url': 'sys/product',
        'perms': 'sys:product:list,sys:product:info,sys:product:save,sys:product:update,sys:product:delete',
        'type': 1,
        'icon': 'config',
        'orderNum': 6,
        'open': null,
        'list': null
      },
      {
        'menuId': 28,
        'parentId': 1,
        'parentName': null,
        'name': '评论管理',
        'url': 'sys/comment',
        'perms': 'sys:comment:list,sys:comment:delete',
        'type': 1,
        'icon': 'config',
        'orderNum': 6,
        'open': null,
        'list': null
      }
    ]
  }
]

// 获取导航菜单列表 / 权限
export function nav () {
  // 根据登录用户角色限定菜单访问权限控制
  // 获取登录用户信息
  // var userRole = this.$store.state.user.role;
  // this.$store.commit('loginUser/clearLoginUser');
  //  // 如果是普通用户只可以访问首页
  // if(userRole=='2'){
  //   return null;
  // }

  return {
    // isOpen: false,
    url: '/sys/menu/nav',
    type: 'get',
    data: {
      'msg': 'success',
      'code': 0,
      'menuList': navDataList,
      'permissions': [
        // 用户列表处理相关
        'sys:user:list',
        'sys:user:update',
        'sys:user:delete',
        'sys:user:info',
        'sys:user:save',
        // 商品信息处理相关
        'sys:product:list',
        'sys:product:update',
        'sys:product:delete',
        'sys:product:info',
        'sys:product:save',
        // 商品前台页面展示
        'platform:showProduct:list',
        'platform:showProduct:info',
        'platform:comment:list',
        'platform:comment:add',
        'platform:comment:delete'
      ]
    }
  }
}