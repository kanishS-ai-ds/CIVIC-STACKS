# CivicStacks — Automated Municipal Library E-Book Licensing & Digital Asset Management

A Spring Boot (Java) + JPA backend and a warm, readable HTML/JS dashboard for:
- Publisher and patron master data (contacts)
- E-book license catalog with per-title concurrent checkout caps (product master)
- Checkout desk: borrow → concurrency check → return, with automatic late access fees
- Publisher license purchase orders/vendor bills and non-resident membership invoices
- Department media-acquisition budget vs. actual, and a Profit & Loss report

## Why this mirrors a mini-DBMS / mini-ERP
Each of the seven sections in the assignment brief maps directly onto a package:

| Brief section | Project piece |
|---|---|
| Contact Master (Publisher, Patron) | `model/Publisher.java`, `model/Patron.java` |
| Product Master (E-Book License) | `model/EBookLicense.java` |
| Chart of Accounts (conceptual) | Revenue/expense fields totalled in `FinanceService` |
| Journal / Journal Entries | `MembershipInvoice` (sales), `LicenseBill` (purchase) |
| Transaction flow (PO → Bill → Payment, SO → Invoice → Payment) | `FinanceController` |
| Budget flow (Analytic Account + Budget) | `model/DepartmentBudget.java` |
| Reporting (Balance-Sheet-style P&L, Budget report) | `FinanceService.profitAndLoss()` / `budgetReport()` |

## Project layout
```
civic-stacks/
  pom.xml
  Dockerfile
  src/main/java/com/library/stacks/
    model/        JPA entities (Publisher, Patron, EBookLicense, Checkout,
                   MembershipInvoice, LicenseBill, DepartmentBudget)
    repository/   Spring Data JPA repositories
    service/      LibraryService (checkout/return + concurrency cap)
                  + FinanceService (P&L / budget report)
    controller/    REST controllers under /api/ebooks/*
    DataSeeder.java  seeds demo data on first run
  src/main/resources/
    application.properties
    static/index.html   the dashboard UI (served at http://localhost:8080)
```

## Run it locally (no database setup needed — uses in-memory H2)
```bash
cd civic-stacks
mvn spring-boot:run
```
Then open **http://localhost:8080** — the dashboard talks to the REST API automatically.
H2 console (if you want to inspect data): http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:stacksdb`).

## Key REST endpoints
| Method | Path | Purpose |
|---|---|---|
| GET/POST | `/api/ebooks/publishers` | list / add publishers |
| GET/POST | `/api/ebooks/patrons` | list / add patrons |
| GET/POST | `/api/ebooks/licenses` | list / register e-book licenses |
| GET | `/api/ebooks/licenses/near-capacity` | licenses at their concurrent checkout cap |
| POST | `/api/ebooks/checkout?patronId=&licenseId=` | borrow an e-book (rejected if at capacity) |
| PUT | `/api/ebooks/checkouts/{id}/return` | return the e-book, settle late access fee |
| POST | `/api/ebooks/checkouts/expire-overdue` | system job: revoke overdue digital keys |
| GET/POST | `/api/ebooks/invoices` | non-resident membership invoices |
| PUT | `/api/ebooks/invoices/{id}/pay` | collect membership payment |
| GET/POST | `/api/ebooks/vendor-bills` | publisher license bills |
| PUT | `/api/ebooks/vendor-bills/{id}/pay` | pay a publisher license bill |
| GET | `/api/ebooks/reports/pnl` | Profit & Loss |
| GET | `/api/ebooks/reports/budget` | department acquisition budget vs actual |

## Example use-case walkthrough
1. **Create master data** — add a publisher, then a license:
   ```bash
   curl -X POST "http://localhost:8080/api/ebooks/publishers" \
        -H "Content-Type: application/json" \
        -d '{"name":"Penguin Civic Press","contactEmail":"rights@penguincivic.example","phone":"+91-8000000001"}'

   curl -X POST "http://localhost:8080/api/ebooks/licenses" \
        -H "Content-Type: application/json" \
        -d '{"title":"The Municipal Reader","isbn":"978-1-000-00001-1","publisher":{"id":1},"licenseType":"PERPETUAL","concurrentCheckoutLimit":5,"unitCost":1200}'
   ```
2. **Checkout & access control** — borrow the e-book; a 6th simultaneous request against a
   5-seat license is rejected with `409`-style error text naming the concurrent cap:
   ```bash
   curl -X POST "http://localhost:8080/api/ebooks/checkout?patronId=1&licenseId=1"
   ```
3. **Record a license expense** — bill the publisher for extra concurrent keys, then pay it:
   ```bash
   curl -X POST "http://localhost:8080/api/ebooks/vendor-bills?publisherId=1&description=Extra+50-seat+key+bundle&amount=45000"
   curl -X PUT  "http://localhost:8080/api/ebooks/vendor-bills/1/pay"
   ```
4. **Generate reports**:
   ```bash
   curl "http://localhost:8080/api/ebooks/reports/pnl"
   curl "http://localhost:8080/api/ebooks/reports/budget"
   ```

## Deploying to Google Cloud (Cloud Run)

1. **Install** the Google Cloud CLI and authenticate:
   ```bash
   gcloud init
   gcloud auth login
   ```
2. **Build and deploy directly from source** (Cloud Build creates the container from the included `Dockerfile`):
   ```bash
   cd civic-stacks
   gcloud run deploy civic-stacks \
     --source . \
     --region asia-south1 \
     --allow-unauthenticated \
     --port 8080
   ```
   Cloud Run prints a public HTTPS URL when it finishes — that's your live site.
3. **(Optional, recommended for production) use Cloud SQL for PostgreSQL** instead of the in-memory H2
   database, so data survives restarts:
   ```bash
   gcloud sql instances create stacks-db --database-version=POSTGRES_15 --tier=db-f1-micro --region=asia-south1
   gcloud sql databases create stacksdb --instance=stacks-db
   ```
   Then uncomment the PostgreSQL block in `application.properties`, set `DB_USER` / `DB_PASSWORD` as
   Cloud Run environment variables, and attach the instance:
   ```bash
   gcloud run deploy civic-stacks --source . --region asia-south1 \
     --add-cloudsql-instances=<PROJECT_ID>:asia-south1:stacks-db \
     --set-env-vars DB_USER=postgres,DB_PASSWORD=<your-password>
   ```
4. Re-deploy any time you change code by re-running the `gcloud run deploy` command.

## Notes
- The dashboard (`static/index.html`) calls the API on the same origin, so once deployed the whole
  app — UI + backend — is served from the single Cloud Run URL.
- `DataSeeder` pre-loads two publishers, three licenses, three patrons and a department budget so the
  dashboard has data to show immediately.
- Business rule to note in a demo: try borrowing the same license more times than its
  `concurrentCheckoutLimit` — the system rejects the checkout, exactly like the brief's
  "reject access if active checkouts match license cap" requirement.
