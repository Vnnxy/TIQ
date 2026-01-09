<template>
  <v-app>
    <v-navigation-drawer permanent>
      <v-list>
        <v-list-item title="Transaction Manager" />
        <v-divider />

        <v-list-item
          title="Transactions"
          to="/transactions"
          link
        />
        <v-list-item
          title="Transactions Data"
          to="/transactions/data"
          link
        />
        <v-list-item
          title="Merchants"
          to="/merchants"
          link
        />
        <v-list-item
          title="Summaries"
          to="/summaries"
          link
        />
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>
