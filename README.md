# Before run

Please add `.env` on root path.

Example

```sh
BIRDSEYEAPI_EXECUTION_MODE={PRODUCTION|DEBUG}
OPENAI_API_KEY=*****
```

`BIRDSEYEAPI_EXECUTION_MODE` accept `DEBUG` or `PRODUCTION`.
If you specify `PRODUCTION`, docker compose execute serve.sh.
If you specify `DEBUG`, nothing.

# Local serve

Please run `serve.sh`.

# Before deploy

Mysql require a database. If you haven't prepare them, please create.

SQL Example

```sql
CREATE DATABASE IF NOT EXISTS birds_eye;
```

# Deploy

GitHub Actions will deploy when pushed on main branch.
