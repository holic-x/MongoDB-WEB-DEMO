<template>
  <el-dialog
    :title="!dataForm.productId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="商品编号" prop="productNum">
        <el-input v-model="dataForm.productNum" placeholder="商品编号"></el-input>
      </el-form-item>

      <el-form-item label="商品分类" prop="classify">
        <el-select v-model="dataForm.classify" placeholder="请选择商品分类">
        <el-option
          v-for="item in options.classifyOption"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      </el-form-item>

      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="dataForm.productName" placeholder="商品名称"></el-input>
      </el-form-item>
<!--       
      <el-form-item label="图片路径" prop="picUrl">
        <el-input v-model="dataForm.picUrl" placeholder="图片路径"></el-input>
      </el-form-item> 
      -->

      <el-form-item label="图片路径" prop="picUrl">
        <el-upload
          action=""
          ref="uploadDataFormRef"
          :multiple="false"
          :limit="1"
          accept=".jpg,.jpeg,.png"
          :auto-upload="false"
          :on-exceed="onExceed"
          :http-request="httpRequest"
        >
        <el-button size="small" type="primary">点击上传</el-button>
        <h4>{{dataForm.picUrl}}</h4>
      </el-upload>
      </el-form-item>

      <el-form-item label="库存" prop="stock">
        <el-input v-model="dataForm.stock" placeholder="库存"></el-input>
      </el-form-item>
      <el-form-item label="供货商" prop="vendor">
        <el-input v-model="dataForm.vendor" placeholder="供货商"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="dataForm.remark" placeholder="备注"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        // 定义模板文件上传组件对话框
        uploadData: new FormData(),
        dataForm: {
          id: 0,
          productNum: '',
          picUrl: '',
          classify: '',
          productName: '',
          stock: '0',
          vendor: '',
          remark: '',
        },
        dataRule: {
          productNum: [
            { required: true, message: '商品编号不能为空', trigger: 'blur' }
          ],
          picUrl: [
            { required: false, message: '', trigger: 'blur' }
          ],
          classify: [
            { required: true, message: '商品分类不能为空', trigger: 'blur' }
          ],
          productName: [
            { required: true, message: '商品名称不能为空', trigger: 'blur' }
          ],
          stock: [
            { required: true, message: '库存不能为空', trigger: 'blur' }
          ],
          vendor: [
            { required: false, message: '', trigger: 'blur' }
          ],
          remark: [
            { required: false, message: '', trigger: 'blur' }
          ],
        },
        // option定义
        options:{
          // food_prod\daily_prod\costume_prod\elec_prod\household_prod
          classifyOption:[{
            value: 'food_prod',
            label: '食品'
          }, {
            value: 'daily_prod',
            label: '日用品'
          }, {
            value: 'costume_prod',
            label: '服饰'
          }, {
            value: 'elec_prod',
            label: '电子产品'
          }, {
            value: 'household_prod',
            label: '家居'
          }]
        }
 
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          // 重置表单信息，避免共用表单导致数据残余
         this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            console.log('调用接口获取详情');
            this.$http({
              url: this.$http.adornUrl(`/sys/product/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.productNum = data.product.productNum
                this.dataForm.picUrl = data.product.picUrl
                this.dataForm.classify = data.product.classify
                this.dataForm.productName = data.product.productName
                this.dataForm.stock = data.product.stock
                this.dataForm.vendor = data.product.vendor
                this.dataForm.remark = data.product.remark
              }
            })
          }
        })
      },

      // ---------- 上传对话框方法定义 ----------
    uploadDialogHandleClose() {
      // 清空上传组件数据，关闭对话框
      this.$refs.uploadDataFormRef.clearFiles()
      this.uploadDataFormConfig.dialogVisible = false;
    },

    // 自定义文件上传
    httpRequest(param) {
      const file = param.file;
      this.uploadData.append("uploadFile", file, file.name);
    },
    // 文件过多提示
    onExceed(files, fileList) {
      this.$message.warning("上传文件数目过多，请删除多余附件！");
    },

      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            // 文件上传校验
            const uploadFileSize = this.$refs.uploadDataFormRef.uploadFiles.length;
            // if (uploadFileSize === 0) return this.$message.warning("请先上传文件！"); 不作强制要求
            if (uploadFileSize > 0) {
              const uploadFileList = this.$refs.uploadDataFormRef.uploadFiles;
              for (let i = 0; i < uploadFileList; i++) {
                const file = uploadFileList[i];
                const type = file.name.split(".")[file.name.split(".").length - 1];
                const size = file.size;
                // 校验文件格式.jpg,.jpeg,.png
                if (
                  type !== "jpg" &&
                  type !== "jpeg" &&
                  type !== "png" 
                )
                  return this.$message.warning(
                    "格式错误！请上传.xls，.xlsx，.doc，.docx格式的文件"
                  );
                if (size / 1024 / 1024 > 5)
                  return this.$message.warning("文件过大！请上传小于5MB的文件");
              }
              // 封装上传数据并上传
              // var uploadData = new FormData();
              this.$refs.uploadDataFormRef.submit();
            }
              this.uploadData.append("productId", this.dataForm.id || '');
              this.uploadData.append("productNum", this.dataForm.productNum);
              this.uploadData.append("oldPicUrl", this.dataForm.picUrl);
              this.uploadData.append("classify", this.dataForm.classify);
              this.uploadData.append("productName", this.dataForm.productName);
              this.uploadData.append("stock", this.dataForm.stock);
              this.uploadData.append("vendor", this.dataForm.vendor);
              this.uploadData.append("remark", this.dataForm.remark);

            /*
            普通sjon交互
            this.$http({
              url: this.$http.adornUrl(`/sys/product/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'productId': this.dataForm.id || undefined,
                'productNum': this.dataForm.productNum,
                'picUrl': this.dataForm.picUrl,
                'classify': this.dataForm.classify,
                'productName': this.dataForm.productName,
                'stock': this.dataForm.stock,
                'vendor': this.dataForm.vendor,
                'remark': this.dataForm.remark
              })
            */
           // 文件交互
           this.$http({
              // url: this.$http.adornUrl(`/sys/product/${!this.dataForm.id ? 'save' : 'update'}`),
              url: this.$http.adornUrl('/sys/product/save'),
              method: 'post',
              headers:{'Content-Type':'multipart/form-data'},
              data: this.uploadData
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    // 重置表单数据
                    this.dataForm.id = 0;
                    this.dataForm.productNum = '';
                    this.dataForm.picUrl = '';
                    this.dataForm.classify = '';
                    this.dataForm.productName = '';
                    this.dataForm.stock = '';
                    this.dataForm.vendor = '';
                    this.dataForm.remark = '';
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
