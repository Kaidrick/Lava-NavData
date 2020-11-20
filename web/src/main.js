import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import ElementUI from 'element-ui';
import router from './router'
import i18n from './i18n'
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/style/base.scss';

Vue.config.productionTip = false;

axios.defaults.baseURL = process.env.NODE_ENV === 'production' ? '' : '/api';

Vue.use(ElementUI);
Vue.use(VueAxios, axios);

new Vue({
  router,
  i18n,
  render: h => h(App)
}).$mount('#app')
