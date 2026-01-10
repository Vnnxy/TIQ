/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

import 'vuetify/styles'

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'
import 'leaflet/dist/leaflet.css' // for the OSM map

// Composables
import { createApp } from 'vue'

// Styles
import 'unfonts.css'

const app = createApp(App)

registerPlugins(app)

app.mount('#app')
