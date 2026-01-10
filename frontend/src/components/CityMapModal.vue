<template>
  <div class="overlay" @click.self="$emit('close')">
    <div class="modal">
      <div ref="map" class="map"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, watch, ref } from 'vue'
import L from 'leaflet'

const props = defineProps(
  {
    lat: Number,
    lng: Number
  }
)

const map = ref(null)
let leafletMap = null
let marker = null

onMounted(() =>
{
  if (!map.value) return
  leafletMap = L.map(map.value)

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(leafletMap)

  if (props.lat != null && props.lng != null) {
    leafletMap.setView([props.lat, props.lng], 12)
    marker = L.marker([props.lat, props.lng]).addTo(leafletMap)
  }
})

watch(() => [props.lat, props.lng], ([lat, lng]) => {
  if (!leafletMap || lat == null || lng == null) return

  leafletMap.setView([lat, lng], 12)

  if (marker) {
    marker.setLatLng([lat, lng])
  } else {
    marker = L.marker([lat, lng]).addTo(leafletMap)
  }
})
</script>

<style scoped>
.overlay
{
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal
{
  background: white;
  width: 600px;
  height: 400px;
}

.map
{
  width: 100%;
  height: 100%;
}
</style>
