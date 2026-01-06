## Running the project

The frontend is using Node, Vue and Vuetify.

### Using Docker
To run the project using Docker, use the same command as the backend:

```bash
docker compose up
```

### Running manually
```bash
npm install
npm run dev
```

### Notes

At the moment, the frontend requires a merchant to be added directly to the database in order to create a transaction. (As we need a valid Merchant id)

