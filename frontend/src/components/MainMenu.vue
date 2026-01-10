<template>
  <v-container class="fill-height" max-width="900">
    <div>
      <v-img
        class="mb-4"
        height="150"
        src="@/assets/logo.png"
      />

      <div class="mb-8 text-center">
        <div class="text-body-2 font-weight-light mb-n1">Welcome to</div>
        <h1 class="text-h2 font-weight-bold">Vuetify</h1>
      </div>

      <v-navigation-drawer permanent>
        <v-list-item title="Transaction manager" ></v-list-item>
        <v-divider></v-divider>
        <v-list-item link title="Transactions"></v-list-item>
        <v-list-item link title="Merchants"></v-list-item>
      </v-navigation-drawer>

    </div>
  </v-container>
</template>


