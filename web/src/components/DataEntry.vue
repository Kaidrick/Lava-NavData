<template>
  <div class="data-entry-wrapper" @click="expandMiscInfo">
    <el-row class="main-data">
      <el-col :span="6">
        <div class="ident-code">
          <span>{{ entry.code }}</span>
        </div>
      </el-col>
      <el-col :span="6">
        <el-row>
          <div class="entry-name">
            {{ entry.name }}
          </div>
        </el-row>
        <el-row>
          <div class="frequency">
            {{ entry.frequency }} / {{ entry.radial }} {{ entry.d2 }}
          </div>
        </el-row>
      </el-col>
      <el-col :span="6">
        <div class="latitude">{{ entry.position.latitude }}</div>
        <div class="longitude">{{ entry.position.longitude }}</div>
      </el-col>
      <el-col :span="6">
        <div class="altitude">{{ entry.position.altitude }}</div>
      </el-col>
    </el-row>
    <el-collapse-transition>
      <div v-show="expanded && entry.description" class="misc-info-wrapper">
        <div>{{ entry.description }}</div>
      </div>
    </el-collapse-transition>
  </div>
</template>

<script>
  export default {
    name: "DataEntry",

    props: {
      entry: {},
    },

    data() {
      return {
        expanded: false
      }
    },

    methods: {
      expandMiscInfo() {
        this.expanded = !this.expanded;
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import 'src/assets/style/color-scheme';
  .data-entry-wrapper {

    padding: 10px;

    &:last-of-type {
      margin-bottom: 0;
    }

    &:hover {
      cursor: default;
      background-color: darken($primary, 10);
    }

    .main-data {
      text-align: left;

      .ident-code {
        font-size: 24px;
        font-weight: bold;
      }
      .entry-name {
        font-size: 12px;
      }
      .frequency, .latitude, .longitude {
        font-size: 14px;
      }
    }

    .misc-info-wrapper {
      text-align: left;
    }
  }
</style>