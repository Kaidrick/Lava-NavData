<template>
  <div class="navigational-aids-wrapper">
    <div class="search-bar-wrapper">
      <el-input v-model="filterText" size="small" placeholder="Navaid Code / Name / Frequency"></el-input>
    </div>
    <div class="nav-aid-list-view-wrapper">
      <el-scrollbar style="height: calc(100vh - 125px)">
        <data-entry v-for="(entry, index) in listView" :entry="entry" :key="index"></data-entry>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
import DataEntry from "@/components/DataEntry";
export default {
  name: "Waypoints",
  components: {DataEntry},
  data() {
    return {
      listView: [],
      filterText: ''
    }
  },

  mounted() {
    for (let i = 0; i < 100; i++) {
      this.listView.push({
        name: 'Test Nav Aid Name',
        frequency: 123.12,
        radial: 0,
        d2: 0,
        nationalityCode: 0,
        code: 'HB',
        position: {
          latitude: ((Math.random() - 0.5) * 180).toFixed(6),
          longitude: ((Math.random() - 0.5) * 360).toFixed(6),
          altitude: Math.random() * 30000
        },
        description: 'Test description for this very common navigational aid. Nothing to see here. Move along.'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navigational-aids-wrapper {
  .search-bar-wrapper {
    margin: 0 10px 10px
  }
  .nav-aid-list-view-wrapper {
    ::v-deep .el-scrollbar {
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }
  }
}
</style>