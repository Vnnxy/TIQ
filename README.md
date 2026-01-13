This document might change in future versions.

# Deadlines:

- D2 : 2/12 Req 1, Req 2, Req3
- D3 : 16/12 Req 4, Req 6 (4 days were added due to the possible complexity of this section of the project.)
- D4 : 6/1 Req 5, Req 6

# Project setup

Create a .env file with the following information (for local setup, the env is already in the warpgate server):

```
POSTGRES_DB=<db>
POSTGRES_USER=<postgres>
POSTGRES_PASSWORD=<password>

BACKEND_PORT=<backend_port> (8080)
FRONTEND_PORT=<Frontend_port> (80)
DB_PORT=5432

TRANSACTIONS_CSV_PATH=transactions-data (or the name of the folder with the csv)

```

# Running in Warp Gate
## NOTE: The csv files that are contained within the server are from 2014 to 2016 (inclusive). 
  ## Running using CI/CD pipeline
  The project is automatically built using GitLab CI/CD:

  - Backend and frontend Docker images are built on each push

  - Images are pushed to the GitLab Container Registry

  - The WarpGate VM pulls the images and runs them via docker compose

### Steps:

1. Log in to GitLab Container Registry: (Initial setup)

``` bash 
docker login registry.gitlab.com
```
*** Note: if 2FA is enabled, login with the [personal token](https://docs.gitlab.com/user/profile/personal_access_tokens/) by doing:
```bash
docker login registry.gitlab.com -u <gitlab-username> -p <personal-access-token>
``` 

You can access the frontend via: https://webeng.digital-lab.dev/?warpgate-target=Group%2044%20HTTP%20Frontend#/transactions

2. Run on the root (without cd into the repo):
```bash
docker compose pull
docker compose up -d
```

  ## Running using the existing code in the vm (This is included in case gitlab has an outage)
  Alternatively, the project can be deployed by cloning or copying the repository to the WarpGate VM and building the images locally. 
  Steps:

  1. Copy or clone the repository to the VM (Repository already on WarpGate so this can be skipped)

  2. Place the CSV files outside the repository directory

  3. Run (You have to be inside the repository):
  ```bash
  cd group-44-project
  docker compose up --build
  ```

You can access the frontend via: https://webeng.digital-lab.dev/?warpgate-target=Group%2044%20HTTP%20Frontend#/transactions
# Running Locally using Docker
The application can be run locally using Docker Compose.

Ensure Docker and Docker Compose are installed

Place CSV files in transactions-data outside.

Add to .env file:
```bash
TRANSACTIONS_DATA_PATH=transactions-data
```

Run:
```bash
docker compose up --build
```
You can access the frontend via:
http://localhost/

