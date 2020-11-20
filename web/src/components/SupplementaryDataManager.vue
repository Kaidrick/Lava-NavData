<template>
  <div class="supplementary-data-manager-wrapper">
    <el-row class="header-row">
      <div class="search-bar-wrapper">
        <el-input v-model="filterText" size="small" placeholder="Navaid Code / Name / Frequency"></el-input>
      </div>
      <div class="manage-button-wrapper">
        <el-button size="small" @click="$refs.addDataPointDialog.show()">Add</el-button>
        <el-button size="small">Edit</el-button>
        <el-button size="small">Delete</el-button>
        <el-button size="small">Move Up</el-button>
        <el-button size="small">Move Down</el-button>
      </div>
    </el-row>

    <div class="supplementary-data-list-view-wrapper">
      <el-scrollbar style="height: calc(100vh - 125px)">
        <data-entry v-for="(entry, index) in listView" :entry="entry" :key="index"></data-entry>
      </el-scrollbar>
    </div>

    <add-data-point-dialog ref="addDataPointDialog"/>
  </div>
</template>

<script>
import DataEntry from "@/components/DataEntry";
import AddDataPointDialog from "@/components/AddDataPointDialog";
export default {
  name: "SupplementaryDataManager",
  components: {AddDataPointDialog, DataEntry},
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
.supplementary-data-manager-wrapper {
  .header-row {
    display: flex;

    .search-bar-wrapper {
      margin: 0 10px 10px;
      flex: 1;
    }

    .manage-button-wrapper {
      margin: 0 10px;
    }
  }

  .supplementary-data-list-view-wrapper {
    ::v-deep .el-scrollbar {
      .el-scrollbar__wrap {
        overflow-x: hidden;
      }
    }
  }
}
</style>