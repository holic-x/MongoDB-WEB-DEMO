<template>
  <div class="block">
    
  <el-dialog
    title="评论区"
    :visible.sync="dialogVisible"
    width="30%"
    :before-close="handleClose">
    <!-- 对话框内容：评论展示 -->
    <h4>评论区展示</h4>
    <h4 v-if="dataList.size==0">该商品暂无评论内容，快来发布一条吧~</h4>
    <el-timeline>
      <p v-for="(item,i) in dataList" :key="i">
        <el-timeline-item :timestamp="item.commentTime" placement="top">
          <el-card>
            <h4 v-if="item.isAnonymous='0'">{{item.commentor}} 提交</h4>
            <h4 v-else-if="item.isAnonymous='1'">匿名用户 提交</h4>
            <p>{{ item.content }}</p>
          </el-card>
        </el-timeline-item>
      </p>
<!--       
      <el-timeline-item timestamp="2018/4/2" placement="top">
        <el-card>
          <h4>nice</h4>
          <p>王小虎 提交于 2018/4/2 20:46</p>
        </el-card>
      </el-timeline-item>
       -->
    </el-timeline>

    <!-- 评论区 -->
    <el-form ref="form" :model="form" label-width="80px">
      <h4>发表评论</h4>
      <el-form-item label="评论内容">
        <el-input type="textarea" v-model="form.content"></el-input>
        <el-form-item label="是否匿名" size="mini" prop="userRole">
          <el-radio v-model="form.isAnonymous" label="0">否</el-radio>
          <el-radio v-model="form.isAnonymous" label="1">是</el-radio>
      </el-form-item>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="onSubmitHandle">立即创建</el-button>
        <el-button @click="resetFromHandel">取消</el-button>
      </el-form-item>
    </el-form>

    <!-- 对话框底部 -->
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关 闭</el-button>
    </span>
  </el-dialog>

  </div>

</template>

<script>
  // import AddOrUpdate from './product-add-or-update'
  export default {
    data () {
      return {
        dialogVisible: false,
         // 当前表单绑定的商品id
        productId:'',
        // 列表数据
        dataList:[],
        
        // 表单内容
        form:{
          content:'',
          isAnonymous:'0' // 默认非匿名
        }
      }
    },
    components: {
      // AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 初始化
       init (productId) {
         // 初始化赋值
        this.productId  = productId;
        // 展示对话框数据
        this.dialogVisible = true;
        // 加载数据信息
        this.getDataList();
       },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/sys/comment/getByCond'),
          method: 'post',
          data: this.$http.adornData({
            // 请求业务参数(绑定商品id)
            'productId': this.productId
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.list
          } else {
            this.dataList = []
          }
          this.dataListLoading = false
        })
      },
      // 评论提交处理
      onSubmitHandle () {
        this.$confirm(`确认发表该评论？（评论发表后不可编辑）`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/comment/save'),
            method: 'post',
            data: this.$http.adornData({
              'content':this.form.content,
              'isAnonymous':this.form.isAnonymous,
              'productId':this.productId,
              'commentor':''
            }, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  // 刷新列表数据
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      },
      // 处理对话框
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },

      // 处理对话框（重置表单）
      resetFromHandel(){
        // 只重置内容和单选框
        this.form.content = '';
        this.form.isAnonymous = '0';
      }
    }
  }
</script>
