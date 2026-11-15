# Publishing and pull requests

This repository contains a **curated reconstruction** of the final source into 27 small, topic-specific commits. The original build history had five commits; the 27-commit presentation history is **not** the chronological development history. The three branches are locally merged into `main`; those merges do not constitute GitHub pull requests.

## Publish existing merged history

1. Create an **empty** repository on GitHub (no README, license or .gitignore), e.g. `2024eb02386-ecommerce-sga2`.
2. Open a terminal inside this extracted project folder.
3. Run:

```bash
git remote add origin https://github.com/lowkeyarhan/2024eb02386-ecommerce-sga2.git
git push -u origin main
git push origin feature/catalog-management feature/commerce-workflows feature/reporting-and-quality
```

This displays 27 commits, three feature branches and three **local merge commits** on GitHub. GitHub will **not** show PRs for these already-merged branches.

## To obtain three genuine GitHub pull-request records

Create three *new* follow-up branches from `main`, make one small, legitimate improvement on each, push each branch, then open and merge each PR on GitHub:

```bash
git switch main
git switch -c pr/catalog-improvements
# make and commit a real change
git push -u origin pr/catalog-improvements
# Open PR against main in GitHub UI, review and merge it.
# Then: git switch main && git pull --ff-only
# Repeat for pr/checkout-improvements and pr/reporting-improvements.
```

Do **not** describe the existing local merge commits as GitHub pull requests. To make the original three feature branches themselves PRs, a different history needs to be pushed before merging, and GitHub PR merges must happen remotely.

## Verify history

```bash
git log --oneline --graph --decorate --all
git rev-list --count main
git branch -a
```

## Run

```bash
docker compose up -d mysql
./mvnw clean verify
./mvnw spring-boot:run
```

Runtime verification requires a working Maven repository connection and MySQL.
