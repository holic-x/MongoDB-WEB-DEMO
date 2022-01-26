<template>
  <div class="mod-home">
    <h3>商城主页</h3>
    <!-- 走马灯 -->
    <el-carousel :interval="4000" type="card" height="280px">
      <!-- 
      <el-carousel-item v-for="item in 6" :key="item">
        <h3 class="medium">{{ item }}</h3>
      </el-carousel-item>
       -->
      <el-carousel-item>
        <img src= "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
      </el-carousel-item>
      <el-carousel-item>
        <img src= "../../assets/img/home_01.jpg" class="image">
      </el-carousel-item>
      <el-carousel-item>
        <img :src= homePic[1] class="image">
      </el-carousel-item>
      <el-carousel-item>
        <img :src= homePic[2] class="image">
      </el-carousel-item>
      <el-carousel-item>
        <img :src= homePic[3] class="image">
      </el-carousel-item>
    </el-carousel>  

    <!-- 分类图片区展示 -->
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <!-- 限定分类说明：食品、日用品、服饰、电子产品、家居 -->
      <el-tab-pane label="食品" name="food_prod">
        <el-row >
          <el-col :span="8" v-for="(prod, index) in productInfo.food_prod" :key="index" :offset="index > 0 ? index : 0">
            <el-card :body-style="{ padding: '0px' }">
              <!-- <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image"> -->
              <img v-bind:src= "prod.picUrl" class="image">
              <div style="padding: 14px;">
                <span>商品信息</span>
                <div class="bottom clearfix">
                  <time class="time">{{ prod.productName }}</time>
                  <el-button type="text" class="button" @click="showComments(prod.productId)">评论</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="日用品" name="daily_prod">
        <el-row>
          <!-- el-col的offset属性，设置某个el-col元素左侧空多少份的位置，默认为0 -->
          <el-col :span="8" v-for="(prod, index) in productInfo.daily_prod" :key="index" :offset="index > 0 ? index : 0">
            <!-- <el-col :span="8" v-for="prod in productInfo.daily_prod" :key="prod">  -->
            <el-card :body-style="{ padding: '0px' }">
              <!-- <img src= "${prod.picUrl}" class="image"> prod.picUrl -->
              <img v-bind:src= "prod.picUrl" class="image">
              <div style="padding: 14px;">
                <span>商品信息</span>
                <div class="bottom clearfix">
                  <time class="time">{{ prod.productName }}</time>
                  <el-button type="text" class="button" @click="showComments(prod.productId)">评论</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="服饰" name="costume_prod">
        <el-row>
          <el-col :span="8" v-for="(prod, index) in productInfo.costume_prod" :key="index" :offset="index > 0 ? index : 0">
            <el-card :body-style="{ padding: '0px' }">
              <img v-bind:src= "prod.picUrl" class="image">
              <div style="padding: 14px;">
                <span>商品信息</span>
                <div class="bottom clearfix">
                  <time class="time">{{ prod.productName }}</time>
                  <el-button type="text" class="button" @click="showComments(prod.productId)">评论</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="电子产品" name="elec_prod">
        <el-row>
          <el-col :span="8" v-for="(prod, index) in productInfo.elec_prod" :key="index" :offset="index > 0 ? index : 0">
            <el-card :body-style="{ padding: '0px' }">
              <img v-bind:src= "prod.picUrl" class="image">
              <div style="padding: 14px;">
                <span>商品信息</span>
                <div class="bottom clearfix">
                  <time class="time">{{ prod.productName }}</time>
                  <el-button type="text" class="button" @click="showComments(prod.productId)">评论</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

       <el-tab-pane label="家居" name="household_prod">
        <el-row>
          <el-col :span="8" v-for="(prod, index) in productInfo.household_prod" :key="index" :offset="index > 0 ? index : 0">
            <el-card :body-style="{ padding: '0px' }">
              <!-- <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image"> -->
              <img v-bind:src= "prod.picUrl" class="image">
              <div style="padding: 14px;">
                <span>商品信息</span>
                <div class="bottom clearfix">
                  <time class="time">{{ prod.productName }}</time>
                  <el-button type="text" class="button" @click="showComments()">评论</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>


      
    </el-tabs>

    <!-- 弹窗, 查看 / 新增 刷新数据-->
    <!-- <ShowOrAdd v-if="dialogVisible" ref="showOrAdd" @refreshDataList="getDataList"></ShowOrAdd> -->
    <ShowOrAdd v-if="dialogVisible" ref="showOrAdd" ></ShowOrAdd>

  </div>

</template>


<script>
  // 引入评论区模块
  import ShowOrAdd from '../modules/show/comment-show-or-add.vue'

  export default {
    data() {
      return {

        // 轮播图配置
        homePic: [
          require('../../assets/img/home_01.jpg'),
          require('../../assets/img/home_02.jpg'),
          require('../../assets/img/home_03.jpg'),
          require('../../assets/img/home_04.jpg')
        ],

        // 分类区控制
        currentDate: new Date(),
        activeName:'food_prod',

        // 分类区数据来源food_prod\daily_prod\costume_prod\elec_prod\household_prod
        productInfo:{
          food_prod:[],
          daily_prod:[],
          costume_prod:[],
          elec_prod:[],
          household_prod:[]
        },

        // 评论区控制
        dialogVisible:false
      };
    },
    components: {
      ShowOrAdd
    },
    methods:{

      // 分类区控制(判断点击内容调用接口获取分页商品信息)
      handleClick(){
          var chooseTab = this.activeName;
            this.$http({
              url: this.$http.adornUrl('/sys/product/getByCond'),
              method: 'post',
              data: this.$http.adornData({
                // 根据指定分类获取商品的信息
                'classify': chooseTab
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '切换成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    // 封装数据刷新tab内容(food_prod\daily_prod\costume_prod\elec_prod\household_prod)
                    if(chooseTab=='food_prod'){
                      console.log('food_prod init');
                      this.productInfo.food_prod = data.list
                    }else if(chooseTab=='daily_prod'){
                      console.log('daily_prod init');
                      this.productInfo.daily_prod = data.list
                    }else if(chooseTab=='costume_prod'){
                      console.log('costume_prod init');
                      this.productInfo.costume_prod = data.list
                    }else if(chooseTab=='elec_prod'){
                      console.log('elec_prod init');
                      this.productInfo.elec_prod = data.list
                    }else if(chooseTab=='household_prod'){
                      console.log('household_prod init');
                      this.productInfo.household_prod = data.list
                    }
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
      },

      // 封装图片信息
      getProductImage(prodImagePath){
         // return "hhhhh";
      },

      // 查看评论
      showComments(productId){
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs.showOrAdd.init(productId)
        })

      }


    }
  }
</script>

<style>
  .mod-home {
    line-height: 1.5;
  }

  /* 自定义样式 */
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }
  
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

  /* 图片分类展示样式 */
  .time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }

</style>

