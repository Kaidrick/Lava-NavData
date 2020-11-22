<template>
  <div class="navigational-aids-wrapper" v-loading="isLoading">
    <div class="search-bar-wrapper">
      <el-input v-model="pageObject.keyword" size="small" placeholder="Navaid Code / Name / Frequency"></el-input>
      <el-button size="small" @click="handlePageSearch">Search</el-button>
      <el-button size="small" @click="handleSearchReset">Reset</el-button>
    </div>
    <div class="nav-aid-list-view-wrapper">
      <el-scrollbar class="scroll-area" style="height: calc(100vh - 160px)">
        <data-entry v-for="(entry, index) in listView" :entry="entry" :key="index"></data-entry>
      </el-scrollbar>
      <el-pagination class="pagination" :page-sizes="[10, 20, 50, 100]"
                     layout="slot, prev, pager, next"
                     @current-change="handlePageSearch"
                     :current-page.sync="pageObject.currentPageNo"
                     :page-size="pageObject.pageSize"
                     :total="total">
        <el-select @change="handleSearchReset" size="mini" v-model="pageObject.pageSize">
          <el-option :value="10" label="10 / page"></el-option>
          <el-option :value="20" label="20 / page"></el-option>
          <el-option :value="50" label="50 / page"></el-option>
          <el-option :value="100" label="100 / page"></el-option>
        </el-select>
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import DataEntry from "@/components/DataEntry";
  import NavFixService from "@/api/NavFixService";
  export default {
    name: "NavFixQuery",
    components: {DataEntry},
    data() {
      return {
        isLoading: false,

        listView: [],

        pageObject: {
          currentPageNo: 1,
          pageSize: 20,

          keyword: null
        },

        total: 0
      }
    },

    props: {
      service: {
        type: String,
        default: '',
      }
    },

    mounted() {
      this.handlePageSearch();
    },

    methods: {
      handlePageSearch(page) {
        if (typeof page !== 'number') {
          this.pageObject.currentPageNo = 1;
        }

        this.isLoading = true;
        NavFixService[this.service](this.pageObject).then(res => {
          if (res.data.status === 200) {
            this.listView = res.data.data;
            this.total = res.data.total;
          } else {
            this.$message.error(res.data.message);
          }
        }).finally(() => this.isLoading = false);
      },

      handleSearchReset(size) {
        this.pageObject.keyword = null;
        this.pageObject.pageSize = typeof size === 'number' ? size : 20;
        this.pageObject.currentPageNo = 1;

        this.handlePageSearch();
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "src/assets/style/color-scheme";

  .navigational-aids-wrapper {
    display: flex;
    flex-direction: column;

    .search-bar-wrapper {
      display: inline-flex;
      margin: 0 10px 10px;

      .el-input {
        width: 350px;
        padding-right: 10px;
      }
    }
    .nav-aid-list-view-wrapper {
      margin: 0 10px;

      ::v-deep .el-scrollbar {
        .el-scrollbar__wrap {
          overflow-x: hidden;
        }
      }

      .scroll-area {
        background-color: $primary_light;
      }

      .pagination {
        margin-top: 10px;
      }
    }
  }
</style>