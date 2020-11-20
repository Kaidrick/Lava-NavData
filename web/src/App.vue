<template>
  <div id="app">
    <el-tabs class="option-tabs">
      <el-tab-pane label="Navigational Aids">
        <navigational-aids/>
      </el-tab-pane>
      <el-tab-pane label="Waypoints">
        <waypoints/>
      </el-tab-pane>
      <el-tab-pane label="Supplementary Data">
        <supplementary-data-manager/>
      </el-tab-pane>
      <el-tab-pane label="Reference Points"></el-tab-pane>
    </el-tabs>
<!--    <div id="nav">-->
<!--      <router-link to="/">Home</router-link> |-->
<!--      <router-link to="/about">About</router-link>-->
<!--    </div>-->
<!--    <router-view/>-->
  </div>
</template>

<style lang="scss" scoped>

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #2c3e50;

  ::v-deep .option-tabs {
    flex: 1;
  }
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
<script>
import NavigationalAids from "@/components/NavigationalAids";
import Waypoints from "@/components/Waypoints";
import SupplementaryDataManager from "@/components/SupplementaryDataManager";
export default {
  components: {SupplementaryDataManager, Waypoints, NavigationalAids}
}
</script>