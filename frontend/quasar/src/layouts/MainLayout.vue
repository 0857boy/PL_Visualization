<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>

        <q-toolbar-title>
          PL 可視化系統
        </q-toolbar-title>

        <q-space />

        <q-btn flat round :icon="isDark ? 'dark_mode' : 'light_mode'" @click="toggleTheme" />

      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref } from 'vue'
import { useQuasar } from 'quasar'

const $q = useQuasar()
const isDark = ref($q.dark.isActive)

const toggleTheme = () => {
  $q.dark.toggle()
  isDark.value = $q.dark.isActive
}
</script>