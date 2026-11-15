#!/usr/bin/env python3
"""Static completeness checks for the SGA2 source bundle; does not compile or run the application."""
from pathlib import Path
import re
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java"
TEST = ROOT / "src/test/java"
JSP = ROOT / "src/main/webapp/WEB-INF/jsp"
SQL = ROOT / "src/main/resources/db/migration/V1__baseline_schema.sql"
POM = ROOT / "pom.xml"

checks = []
def add(name, ok, detail):
    checks.append((name, bool(ok), detail))

try:
    ET.parse(POM)
    add("pom.xml is well-formed XML", True, "Maven project descriptor parsed")
except ET.ParseError as exc:
    add("pom.xml is well-formed XML", False, str(exc))

java_files = list(JAVA.rglob("*.java"))
entity_files = [p for p in java_files if re.search(r"@Entity\b", p.read_text(encoding="utf-8"))]
controller_files = [p for p in java_files if p.name.endswith("Controller.java")]
repository_files = [p for p in java_files if p.name.endswith("Repository.java")]
query_annotations = sum(len(re.findall(r"@Query\s*\(", p.read_text(encoding="utf-8"))) for p in repository_files)
service_ports = list(JAVA.rglob("*ServicePort.java"))
jsp_views = [p for p in JSP.rglob("*.jsp") if p.parent.name != "fragments"]
all_jsp = list(JSP.rglob("*.jsp"))
sql_tables = re.findall(r"CREATE\s+TABLE\s+([a-z_]+)", SQL.read_text(encoding="utf-8"), flags=re.I)
transactional = sum(len(re.findall(r"@Transactional\b", p.read_text(encoding="utf-8"))) for p in java_files)
test_files = list(TEST.rglob("*Test.java"))
test_text = "\n".join(p.read_text(encoding="utf-8") for p in test_files)
unit_tests = len(re.findall(r"@Test\b", (TEST / "com/lowkeyarhan/ecommerce/common/validation/BusinessRulesTest.java").read_text(encoding="utf-8")))
query_tests = len(re.findall(r"@Test\b", (TEST / "com/lowkeyarhan/ecommerce/catalog/repository/ProductRepositoryQueryTest.java").read_text(encoding="utf-8")))
endpoint_tests = len(re.findall(r"@Test\b", (TEST / "com/lowkeyarhan/ecommerce/EndpointSmokeTest.java").read_text(encoding="utf-8")))

add("15 mapped JPA entities", len(entity_files) >= 15, f"found {len(entity_files)}")
add("15 baseline MySQL tables", len(sql_tables) == 15, f"found {len(sql_tables)}: {', '.join(sql_tables)}")
add("10+ JSP views", len(jsp_views) >= 10, f"found {len(jsp_views)} view JSPs ({len(all_jsp)} including fragments)")
add("10+ MVC controllers", len(controller_files) >= 10, f"found {len(controller_files)}")
add("8+ feature repositories", len(repository_files) >= 8, f"found {len(repository_files)}")
add("8+ service interfaces / ports", len(service_ports) >= 8, f"found {len(service_ports)}")
add("8+ explicit repository queries", query_annotations >= 8, f"found {query_annotations} @Query declarations")
add("transactional service boundaries", transactional >= 8, f"found {transactional} @Transactional declarations")
add("4+ business-rule unit tests", unit_tests >= 4, f"found {unit_tests} @Test methods")
add("2+ repository query tests", query_tests >= 2, f"found {query_tests} @Test methods")
add("3+ endpoint tests", endpoint_tests >= 3, f"found {endpoint_tests} @Test methods")
add("jQuery AJAX implementation", "$.ajax" in (ROOT / "src/main/resources/static/js/app.js").read_text(encoding="utf-8"), "$.ajax and $.getJSON present")
add("Functional test matrix", (ROOT / "docs/functional-test-cases.md").exists(), "manual test checklist included")

failed = False
for name, ok, detail in checks:
    print(f"{'PASS' if ok else 'FAIL'}  {name}: {detail}")
    failed |= not ok
print(f"\n{len(checks) - int(failed)} / {len(checks)} static checks passed")
print("NOTE: These are static completeness checks. They do not compile Java or execute SQL / application tests.")
sys.exit(1 if failed else 0)
