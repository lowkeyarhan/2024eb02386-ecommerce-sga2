# Delivery Notes

This archive contains the SGA2 Project 4 source code, the MySQL 8 baseline migration and seed data, automated tests, documentation, UI design previews, and the final PDF report.

## Verification status

- Static completeness validator: 14/14 checks passed (`python scripts/validate_bundle.py`).
- Maven POM: valid XML.
- PDF: 14 pages; rendered and visually reviewed.
- Maven compile/test: not run successfully in the authoring environment. `./mvnw -q test` was attempted, but Maven Wrapper could not download Maven because DNS resolution for `repo.maven.apache.org` failed (`curl: (6) Could not resolve host`).
- MySQL integration/runtime: not executed because MySQL and Docker are unavailable in the authoring environment.

Do not represent the pending runtime checks as passing. Run the commands in `README.md` on a machine with internet access, JDK 21, Docker and Maven Wrapper connectivity.

## Git history

The working repository contains incremental commits for project bootstrap, schema/entity mapping, MVC workflows, test coverage and verification evidence. The archive excludes `.git` so it stays portable. A separate Git bundle can preserve the local history.

Updated presentation history: 27 curated commits, three local feature branches and three local merge commits. No GitHub PR records exist yet. See GITHUB_PUBLISH.md.
