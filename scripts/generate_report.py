#!/usr/bin/env python3
"""Generate the SGA2 Project 4 report PDF from the checked project documentation."""
from pathlib import Path
from xml.sax.saxutils import escape
from reportlab.lib import colors
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_RIGHT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (
    BaseDocTemplate, PageTemplate, Frame, Paragraph, Spacer, PageBreak,
    Table, TableStyle, Image, Preformatted, KeepTogether, HRFlowable,
)
from reportlab.platypus.tableofcontents import TableOfContents
from reportlab.lib.utils import ImageReader

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "2024EB02386_ECommerce_SGA2_Report.pdf"
ASSETS = ROOT / "docs" / "screenshots"

FONT_REG = str(ROOT / ".font-cache/Inter-Regular.ttf") if (ROOT / ".font-cache/Inter-Regular.ttf").exists() else "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"
FONT_BOLD = str(ROOT / ".font-cache/Inter-SemiBold.ttf") if (ROOT / ".font-cache/Inter-SemiBold.ttf").exists() else "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"
FONT_ITALIC = str(ROOT / ".font-cache/Inter-Italic.ttf") if (ROOT / ".font-cache/Inter-Italic.ttf").exists() else "/usr/share/fonts/truetype/dejavu/DejaVuSans-Oblique.ttf"
FONT_MONO = "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf"
pdfmetrics.registerFont(TTFont("DV", FONT_REG))
pdfmetrics.registerFont(TTFont("DV-Bold", FONT_BOLD))
pdfmetrics.registerFont(TTFont("DV-Italic", FONT_ITALIC))
pdfmetrics.registerFont(TTFont("DV-Mono", FONT_MONO))

INK = colors.HexColor("#111111")
MUTED = colors.HexColor("#656565")
GREEN = colors.HexColor("#111111")
PALE = colors.HexColor("#F4F4F4")
LINE = colors.HexColor("#DADADA")
PAPER = colors.white
AMBER = colors.HexColor("#555555")

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="BodyX", fontName="DV", fontSize=8.5, leading=13.2, textColor=INK, spaceAfter=5.2))
styles.add(ParagraphStyle(name="SmallX", fontName="DV", fontSize=7.1, leading=10, textColor=MUTED, spaceAfter=3))
styles.add(ParagraphStyle(name="SectionX", fontName="DV-Bold", fontSize=15.5, leading=19, textColor=INK, spaceBefore=8, spaceAfter=8, keepWithNext=True))
styles.add(ParagraphStyle(name="SubX", fontName="DV-Bold", fontSize=10, leading=13, textColor=GREEN, spaceBefore=7, spaceAfter=4, keepWithNext=True))
styles.add(ParagraphStyle(name="TitleX", fontName="DV-Bold", fontSize=31, leading=36, textColor=INK, spaceAfter=10))
styles.add(ParagraphStyle(name="CoverLabel", fontName="DV-Bold", fontSize=8, leading=11, textColor=GREEN, tracking=1.4))
styles.add(ParagraphStyle(name="CoverSub", fontName="DV", fontSize=12, leading=18, textColor=MUTED))
styles.add(ParagraphStyle(name="CoverMetaLabel", fontName="DV-Bold", fontSize=7.2, leading=9.5, textColor=GREEN))
styles.add(ParagraphStyle(name="TableHeadX", fontName="DV-Bold", fontSize=7.2, leading=9, textColor=colors.white))
styles.add(ParagraphStyle(name="TableCellX", fontName="DV", fontSize=7.1, leading=9.6, textColor=INK))
styles.add(ParagraphStyle(name="CodeX", fontName="DV-Mono", fontSize=6.5, leading=9, textColor=INK, backColor=PALE, borderColor=LINE, borderWidth=.5, borderPadding=7, leftIndent=4, rightIndent=4, spaceBefore=3, spaceAfter=6))
styles.add(ParagraphStyle(name="CaptionX", fontName="DV-Italic", fontSize=7, leading=9, textColor=MUTED, alignment=TA_CENTER, spaceBefore=3, spaceAfter=8))
styles.add(ParagraphStyle(name="TOCHeadX", fontName="DV-Bold", fontSize=20, leading=25, textColor=INK, spaceAfter=12))
styles.add(ParagraphStyle(name="TOCEntryX", fontName="DV", fontSize=8.4, leading=12, leftIndent=0, firstLineIndent=0, textColor=INK, spaceAfter=2))
styles.add(ParagraphStyle(name="CalloutX", fontName="DV", fontSize=8, leading=12, textColor=INK, backColor=PALE, borderColor=LINE, borderWidth=.6, borderPadding=8, spaceBefore=5, spaceAfter=7))
styles.add(ParagraphStyle(name="StatusX", fontName="DV-Bold", fontSize=7.5, leading=10, textColor=AMBER))

PAGE_W, PAGE_H = A4
LEFT, RIGHT, TOP, BOTTOM = 11*mm, 11*mm, 15*mm, 14*mm

class ReportDoc(BaseDocTemplate):
    def __init__(self, filename, **kwargs):
        super().__init__(filename, **kwargs)
        frame = Frame(LEFT, BOTTOM, PAGE_W-LEFT-RIGHT, PAGE_H-TOP-BOTTOM, id="normal", leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
        self.addPageTemplates([PageTemplate(id="main", frames=[frame], onPage=self.draw_page)])
        self.section_no = 0
    def beforeDocument(self):
        self.section_no = 0
    def draw_page(self, canvas, doc):
        canvas.saveState()
        if doc.page > 1:
            canvas.setFillColor(PAPER)
            canvas.rect(0, 0, PAGE_W, PAGE_H, stroke=0, fill=1)
            canvas.setStrokeColor(LINE)
            canvas.setLineWidth(.5)
            canvas.line(LEFT, PAGE_H-12*mm, PAGE_W-RIGHT, PAGE_H-12*mm)
            canvas.setFont("DV-Bold", 6.7)
            canvas.setFillColor(GREEN)
            canvas.drawString(LEFT, PAGE_H-9.5*mm, "BUILDING DATABASE APPLICATIONS  /  SGA2")
            canvas.setFont("DV", 6.7)
            canvas.setFillColor(MUTED)
            canvas.drawRightString(PAGE_W-RIGHT, PAGE_H-9.5*mm, "PROJECT 04  ·  E-COMMERCE")
            canvas.line(LEFT, 12*mm, PAGE_W-RIGHT, 12*mm)
            canvas.setFont("DV", 6.5)
            canvas.setFillColor(MUTED)
            canvas.drawString(LEFT, 8.5*mm, "2024EB02386  |  Report version 2.0  |  15 November 2026")
            canvas.drawRightString(PAGE_W-RIGHT, 8.5*mm, f"{doc.page}")
        else:
            canvas.setFillColor(PAPER)
            canvas.rect(0, 0, PAGE_W, PAGE_H, stroke=0, fill=1)
        canvas.restoreState()
    def afterFlowable(self, flowable):
        if isinstance(flowable, Paragraph) and flowable.style.name == "SectionX":
            text = flowable.getPlainText()
            key = f"section-{self.section_no}"
            self.section_no += 1
            self.canv.bookmarkPage(key)
            self.canv.addOutlineEntry(text, key, 0, False)
            self.notify("TOCEntry", (0, text, self.page, key))


def P(text, style="BodyX"):
    return Paragraph(text, styles[style])

def bullets(items):
    return [P("<font color='#111111'><b>•</b></font> " + s) for s in items]

def table(rows, widths=None, header=True, repeatRows=1):
    data=[]
    for i,row in enumerate(rows):
        sty="TableHeadX" if header and i==0 else "TableCellX"
        data.append([P(str(v), sty) for v in row])
    t=Table(data, colWidths=widths, repeatRows=repeatRows if header else 0, hAlign="LEFT")
    cmds=[("VALIGN",(0,0),(-1,-1),"TOP"),("LEFTPADDING",(0,0),(-1,-1),6),("RIGHTPADDING",(0,0),(-1,-1),6),("TOPPADDING",(0,0),(-1,-1),5),("BOTTOMPADDING",(0,0),(-1,-1),5),("LINEBELOW",(0,0),(-1,-1),.35,LINE)]
    if header:
        cmds += [("BACKGROUND",(0,0),(-1,0),GREEN),("ROWBACKGROUNDS",(0,1),(-1,-1),[colors.white, colors.HexColor("#F7F7F7")])]
    else:
        cmds += [("ROWBACKGROUNDS",(0,0),(-1,-1),[colors.white, colors.HexColor("#F7F7F7")])]
    t.setStyle(TableStyle(cmds))
    return t

def section(title):
    return P(title, "SectionX")

def sub(title):
    return P(title, "SubX")

def img(path, maxw, maxh):
    obj=Image(str(path))
    scale=min(maxw/obj.imageWidth, maxh/obj.imageHeight)
    obj.drawWidth=obj.imageWidth*scale
    obj.drawHeight=obj.imageHeight*scale
    return obj

story=[]
# Cover
story += [Spacer(1, 23*mm), P("STAFF GRADED ASSIGNMENT 02", "CoverLabel"), Spacer(1, 12*mm),
          P("E-Commerce Order<br/>and Inventory<br/>Management", "TitleX"),
          P("Full-stack database application development", "CoverSub"), Spacer(1, 13*mm),
          HRFlowable(width="100%", thickness=1.3, color=GREEN), Spacer(1, 11*mm)]
cover_rows=[
    ["COURSE", "Building Database Applications"],
    ["PROJECT", "Project 04 · E-Commerce Order & Inventory Management"],
    ["STUDENT", "Arhan Das"],
    ["BITS ID", "2024EB02386"],
    ["INSTITUTION", "Scaler School of Technology, Bengaluru"],
    ["DATABASE", "MySQL 8 · migrated from SGA1 PostgreSQL model"],
    ["SUBMISSION", "15 November 2026"],
]
cover_table=Table([[P(a,"CoverMetaLabel"),P(b,"TableCellX")] for a,b in cover_rows], colWidths=[34*mm, 120*mm], hAlign="LEFT")
cover_table.setStyle(TableStyle([("VALIGN",(0,0),(-1,-1),"TOP"),("LEFTPADDING",(0,0),(-1,-1),8),("RIGHTPADDING",(0,0),(-1,-1),8),("TOPPADDING",(0,0),(-1,-1),7),("BOTTOMPADDING",(0,0),(-1,-1),7),("BACKGROUND",(0,0),(0,-1),PALE),("ROWBACKGROUNDS",(1,0),(1,-1),[colors.white, colors.HexColor("#F7F7F7")]),("LINEBELOW",(0,0),(-1,-1),.4,LINE),("BOX",(0,0),(-1,-1),.6,LINE)]))
story += [cover_table, Spacer(1, 16*mm), P("Prepared against the SGA2 assignment brief and the Project 4 database design submitted in SGA1.", "BodyX"), Spacer(1, 35*mm), P("IMPLEMENTATION NOTE", "CoverLabel"), P("The source project and report are complete as a development bundle. Automated build and runtime checks remain pending because Maven, MySQL and Docker were unavailable in the authoring environment. Test evidence is reported transparently in Section 24.", "CalloutX"), PageBreak()]

# Contents
story += [P("Contents", "TOCHeadX")]
toc=TableOfContents()
toc.levelStyles=[styles["TOCEntryX"]]
story += [toc, PageBreak()]

# 1..27
story += [section("1. Student Details"), table([
    ["Field","Value"], ["Student name","Arhan Das"], ["BITS ID","2024EB02386"],
    ["Institution","Scaler School of Technology, Bengaluru"], ["Course","Building Database Applications"],
    ["Assignment","SGA2 - Full-Stack Database Application Development"], ["Project type","Individual"],
    ["Submission date","15 November 2026"]], [48*mm,106*mm]),
    P("Student details verified against a previous course submission: Arhan Das, BITS ID 2024EB02386."),

    section("2. Selected Project Details"),
    P("The project continues the SGA1 selection: <b>Project 04 - E-Commerce Order and Inventory Management System</b>. The application manages a multi-seller catalogue, customer profiles and addresses, shopping carts, orders, payments, fulfilment, returns and inventory movements."),
    table([["Item","Project definition"],["Project number","4"],["Core workflow","Browse catalogue → cart → checkout → stock deduction → order history"],["Return workflow","Return request → review decision → receipt → refund and stock restoration"],["Primary users","Customers, catalogue operators, order operators, inventory operators and return operators"],["Database baseline","15-table relational model designed for SGA1; migrated to MySQL 8 for SGA2"]],[43*mm,111*mm]),

    section("3. Abstract"),
    P("This assignment develops the SGA1 e-commerce database design into a full-stack web application using Spring Boot, Maven, MySQL, JPA/Hibernate, JSP with JSTL, CSS, JavaScript, jQuery and AJAX. The implementation uses a feature-first modular monolith. Each business module owns its controllers, DTOs, mappers, entities, repositories and service contracts. The application supports catalogue and customer CRUD, cart management, transactional checkout, inventory ledger tracking, order history, return processing, refunds, restocking, dashboard metrics and three report pages."),
    P("The original 15-table model is retained. The PostgreSQL partial unique index enforcing one open cart per customer is translated into a MySQL generated column and unique constraint. A Flyway baseline migration creates the schema and a second migration adds deterministic demonstration data. The source includes automated test classes and a twelve-case functional test plan."),

    section("4. Problem Statement and Objectives"),
    P("An online seller needs one consistent system to coordinate customer data, seller catalogues, product prices, inventory, cart contents, orders, payments, shipments and returns. Without enforced rules, stock can become negative, the same customer can create conflicting open carts, an order can use another customer's address, and refunds can exceed the original charge."),
    sub("Objectives"),
    *bullets(["Translate the SGA1 relational model to the MySQL platform mandated by SGA2 without changing its business domain.","Expose usable MVC pages for product, category, seller and customer CRUD.","Implement the cart and checkout as a transaction, preserving purchase-time prices and auditing stock changes.","Support return requests, approval/rejection and receipt with linked refunds and inventory restoration.","Provide AJAX add-to-cart and live stock lookup, searchable listings, pagination, a dashboard and three database-backed reports.","Separate domain, persistence, application services and presentation concerns to improve maintainability and testability."]),

    section("5. Scope and Functional Requirements"),
    table([["Area","Included functions"],["Catalogue","Create, view, edit and delete products, categories and sellers; search/filter products; inspect stock."],["Customers","Create, view, edit and delete customers; store customer contact details and addresses."],["Cart","Maintain one open cart per customer; add/update/remove cart items; retrieve stock without a full page reload."],["Orders","Validate customer/address ownership, snapshot prices, calculate charges, decrement stock and create payment/shipment records."],["Returns","Create requests for delivered orders; validate remaining quantity; approve/reject; receive items and issue a linked refund."],["Inventory","Restock products; record restock, sale, return and adjustment movement entries."],["Reports","Dashboard, low-stock report, monthly net-sales report and top-selling products report."]],[30*mm,124*mm]),
    P("Out of scope: real payment gateway, customer authentication/authorization, email notifications, real carrier integration, multiple warehouses, coupon engine, recommendations and a production deployment pipeline. The application uses a clearly labelled demo customer selector and a COD payment simulation; it is not a production commerce service."),

    section("6. Non-Functional Requirements and Design Principles"),
    table([["Concern","Implementation"],["Maintainability","Feature-first modules, service ports, DTOs, mappers, repositories and thin MVC controllers."],["Consistency","Foreign keys, unique constraints, checks, generated totals, transactional service methods and validation."],["Concurrency","Pessimistic locks on carts/orders/products and stable product lock ordering during checkout."],["Auditability","Every stock-changing workflow records an inventory movement with quantity delta, reason and timestamp."],["Security baseline","Bean Validation, parameterized repository/JDBC access, restricted JSP paths, environment-variable DB credentials; no public production security is claimed."],["Performance","Pagination, whitelisted sort fields, purpose-built queries, indexes and HikariCP connection pool settings."],["Portability","Flyway SQL targets MySQL 8; H2 is used only by the test profile."]],[32*mm,122*mm]),

    section("7. Summary of the SGA1 Database Design"),
    P("SGA1 delivered a database-first PostgreSQL design. It used 15 normalized relational tables, primary and foreign keys, unique and check constraints, generated totals, programmable validation, indexes, stored routines, views, reports and SQL tests. The central records are customers and addresses, sellers and categories, products and stock, carts and items, orders and items, payments, shipments, returns and return items, product reviews, and inventory movements."),
    table([["Table","Purpose"],["customers","Customer identity and contact details"],["customer_addresses","Customer-owned shipping addresses"],["sellers","Seller identity and contact details"],["categories","Product categories with optional parent category"],["products","SKU, seller/category links, price, stock, reorder level and active flag"],["carts / cart_items","Customer open cart and product quantities"],["orders / order_items","Order header, shipping address, status, purchase-time price snapshot and line quantities"],["payments","Charges and refunds; refund references original payment"],["shipments","Carrier, tracking number, status and fulfilment timestamps"],["product_returns / return_items","Return request state and quantities against purchased lines"],["product_reviews","Customer product rating and review text"],["inventory_movements","Auditable stock deltas for sale, restock, return and adjustment"]],[44*mm,110*mm]),

    section("8. Schema Migration and Changes Made"),
    P("SGA2 explicitly requires MySQL. The PostgreSQL DDL was translated into MySQL 8 / InnoDB and mapped through JPA/Hibernate. The 15 baseline table names and business responsibilities are preserved. No new business tables were added."),
    sub("Documented portability adjustment"),
    P("PostgreSQL supports a partial unique index on carts where status = 'OPEN'. MySQL does not support partial unique indexes. The MySQL migration therefore adds a stored generated column: <font name='DV-Mono'>open_customer_id = CASE WHEN status = 'OPEN' THEN customer_id ELSE NULL END</font>, then places a unique constraint on that column. Open carts remain unique per customer; closed carts receive NULL and can coexist."),
    sub("Other migration notes"),
    *bullets(["Generated order and order-line totals remain generated database values.","The customer/address ownership relationship is represented through a composite foreign-key constraint.","The order/payment refund relationship keeps its self-reference and same-order linkage.","The return-item, cart-item, order-item and product-review links remain association entities because they hold domain attributes.","Flyway V2 adds sample records for local demonstration only; sample rows do not change the schema."]),

    section("9. System Architecture (MVC)"),
    P("The project is a modular monolith organized by business capability. A browser request enters a Spring MVC controller, binds a request DTO, delegates to a service port, and is handled by a service implementation. Services apply business rules and transaction boundaries, then use Spring Data repositories or focused JDBC reporting queries. Results are mapped to view models and rendered through JSP/JSTL templates."),
    img(ASSETS/"architecture.png", 155*mm, 91*mm),
    P("Figure 1. MVC and persistence architecture. The diagram is generated from docs/architecture.dot and is included as a design artifact.", "CaptionX"),
    sub("Dependency direction"),
    *bullets(["Controllers depend on service interfaces rather than concrete service classes.","Services coordinate domain operations and may use multiple repositories within one transaction.","Repositories encapsulate persistence and custom query behavior.","JSP pages render model data and submit form/AJAX requests; business rules do not live in JSP files."]),

    section("10. Technology Stack"),
    table([["Layer","Technology","Purpose"],["Language","Java 21","Application and domain logic"],["Build","Maven + Maven Wrapper","Dependency management and reproducible build entry point"],["Application framework","Spring Boot 3.3.5","Dependency injection, configuration and application runtime"],["Web","Spring MVC","Controller routing, binding and model/view handling"],["Persistence","Spring Data JPA / Hibernate","Entity mapping and data access"],["Database","MySQL 8 / InnoDB","Relational persistence"],["Migrations","Flyway","Versioned schema and seed-data migrations"],["Views","JSP + JSTL","Server-rendered MVC pages"],["Client","CSS, JavaScript, jQuery 3.7.1, AJAX","Responsive styling and partial page updates"],["Tests","JUnit 5, Spring Boot Test, MockMvc, H2","Unit, query and endpoint test source"],["Packaging","WAR","JSP-compatible packaging and embedded/external Tomcat deployment"]],[26*mm,44*mm,84*mm]),

    section("11. Project Structure"),
    Preformatted("com.lowkeyarhan.ecommerce\n  common/       shared validation, errors and health endpoint\n  customer/     customer and address model, DTO, mapper, repository, service, controller\n  catalog/      product, category and seller features\n  cart/         cart and cart-item features\n  orders/       orders, order items, payments, shipments and returns\n  inventory/    inventory movements and restock workflow\n  reviews/      purchase-qualified product reviews\n  reports/      dashboard, query projections and report views\nsrc/main/resources\n  application.yml\n  db/migration/ V1 baseline schema, V2 sample data\n  static/css/   application styling\n  static/js/    AJAX and client interactions\nsrc/main/webapp/WEB-INF/jsp/\n  JSP pages and shared fragments\nsrc/test/java/  unit, repository query and endpoint tests\ndocs/           diagrams, requirements, test cases, guide and evidence", styles["CodeX"]),
    P("This structure follows the organization principles observed in the supplied Riyura backend reference: feature-oriented modules, distinct model/repository/service/controller packages and interface-driven service boundaries. Riyura domain classes and dependencies were not reused."),

    section("12. Database Configuration and Persistence"),
    P("The default local datasource targets <font name='DV-Mono'>jdbc:mysql://localhost:3306/ecommerce_sga2</font>. Runtime credentials are read from <font name='DV-Mono'>DB_URL</font>, <font name='DV-Mono'>DB_USERNAME</font> and <font name='DV-Mono'>DB_PASSWORD</font>, with local-only fallbacks in application.yml. Flyway manages migrations. Hibernate uses <font name='DV-Mono'>ddl-auto: validate</font>, so the ORM validates rather than silently creating or dropping the MySQL schema."),
    P("The Docker Compose file provides a local MySQL 8 service. The test profile uses H2 in MySQL compatibility mode and disables Flyway; this isolates tests from the developer's MySQL installation. Demo credentials are for local development only and must be replaced before deployment."),
    Preformatted("# Start MySQL\ndocker compose up -d mysql\n\n# Compile, run tests and package the application\n./mvnw clean verify\n\n# Start the application\n./mvnw spring-boot:run\n\n# Open the application\nhttp://localhost:8080", styles["CodeX"]),

    section("13. Entity Classes and Relationships"),
    P("Fifteen JPA entity classes map one-to-one to the SGA1 table set. The implementation models relationships explicitly with JPA associations. Where an M:N relationship contains business attributes, the join table is represented as its own entity rather than as a bare @ManyToMany collection."),
    img(ASSETS/"er-diagram.png", 155*mm, 115*mm),
    P("Figure 2. Relational entity overview for the 15-table baseline. The editable source is docs/er_diagram.dot.", "CaptionX"),
    table([["Relationship","Mapping"],["Customer → Address","One-to-many; each address belongs to one customer"],["Seller → Product","One-to-many"],["Category → Product","One-to-many; category also has a self-parent relationship"],["Customer → Cart / Order","One-to-many"],["Cart ↔ Product","Many-to-many through CartItem; quantity and timestamp are attributes"],["Order ↔ Product","Many-to-many through OrderItem; quantity and purchase-time unit price are attributes"],["Customer ↔ Product review","Association entity ProductReview stores rating and review text"],["Return ↔ OrderItem","Many-to-many through ReturnItem; quantity is an attribute"],["Order → Payment / Shipment","One-to-many payments; optional one-to-one shipment"]],[42*mm,112*mm]),

    section("14. Data Access and Custom Queries"),
    P("Spring Data repositories provide standard create/read/update/delete operations and custom access methods. The source contains 13 explicit @Query declarations across the feature repositories, plus derived finder methods and a JDBC query for monthly sales."),
    table([["Query group","Purpose"],["Product catalogue","Keyword/category filter with pageable results and safe sorting"],["Low-stock products","Products at or below reorder threshold"],["Top-selling products","Aggregate units sold and line value"],["Customer search","Filter customers by searchable name/email fields"],["Cart access","Pessimistically lock the active cart and fetch its items"],["Order search/history","Filter by status/keyword and return paginated customer history"],["Refund aggregation","Sum completed refunds against original charge"],["Return reservation","Sum requested/approved/received quantities to prevent over-return"],["Inventory movement","Fetch recent stock ledger entries"],["Monthly sales","Aggregate completed charges less completed refunds by month"]],[39*mm,115*mm]),
    P("Pagination uses Spring Data Page/Pageable. User-controlled sort keys are checked against an allowlist before they are passed to Sort, avoiding arbitrary entity-property selection from request parameters."),

    section("15. Business Rules and Transactional Operations"),
    table([["Rule","Enforcement location"],["Customer email is required and unique","DTO validation + unique database constraint"],["Seller name/email, category name and product SKU are unique","DTO validation + unique database constraints"],["Product price must be positive; stock/reorder level non-negative","DTO validation + service rule checks + database checks"],["One OPEN cart per customer","Generated column + unique constraint in MySQL"],["Cart/order quantities must be positive","DTO validation + service checks + database checks"],["Shipping address must belong to the order customer","Checkout service validation + composite foreign key"],["Checkout may not oversell or include inactive products","Pessimistic product locks + quantity checks inside transaction"],["Return quantity cannot exceed unreturned purchased quantity","Order lock + aggregate reservation query"],["Refund total cannot exceed original completed charge","Payment aggregation + domain rule validation"]],[71*mm,83*mm]),
    sub("Transaction 1 - checkout"),
    P("The service locks the customer's open cart, validates that it is non-empty and that the shipping address belongs to the customer, then locks products in a stable product-id order. It calculates totals, creates the order, snapshots unit prices into order items, decrements stock, writes SALE inventory movements, creates a demo COD charge and shipment, and converts the cart. A failure raises an exception and rolls back the unit of work."),
    sub("Transaction 2 - return receipt and refund"),
    P("The return workflow locks the return and order, validates state and purchased quantities, calculates the refund against the original charge, verifies the refund ceiling, restores stock, writes RETURN movements, creates a refund payment linked to the charge, and updates the return/order state atomically. Approval and rejection are separate state transitions."),
    sub("Additional transactional operation"),
    P("Restocking locks the product, applies a positive quantity change and writes a RESTOCK ledger entry in the same transaction."),

    section("16. URL Mapping Table"),
    P("The following routes are implemented by Spring MVC controllers. The full mapping table is also maintained in docs/url-mappings.md."),
    table([["HTTP","URL","Purpose"],["GET","/","Dashboard"],["GET","/products","Product listing/search"],["GET / POST","/products/new · /products/save","Create product form and save"],["GET / POST","/products/{id}/edit · /products/{id}/delete","Edit/delete product"],["GET","/api/products/{id}/stock","AJAX stock lookup"],["GET / POST","/categories, /categories/save","Category listing and save"],["GET / POST","/sellers, /sellers/save","Seller listing and save"],["GET / POST","/customers, /customers/save","Customer listing and save"],["GET / POST","/cart, /api/cart/items","Cart view and AJAX add-to-cart"],["POST","/cart/items/{id}/update · /remove","Update/remove cart item"],["GET / POST","/orders, /orders/checkout","Order listing and transactional checkout"],["GET","/orders/history · /orders/{id}","Customer history and order detail"],["GET / POST","/returns, /returns/new, /returns/{id}/action","Return queue, request and processing"],["GET / POST","/inventory, /inventory/restock","Inventory ledger and restock"],["GET","/reports, /reports/low-stock, /reports/monthly-sales, /reports/top-products","Three reports and landing page"],["GET","/api/health","Health response"]],[27*mm,64*mm,63*mm]),

    section("17. User Interface Pages and Navigation"),
    P("The interface uses a shared navigation/header/footer and consistent design tokens in one stylesheet. Server-rendered views are stored under WEB-INF/jsp and are not directly addressable as static files."),
    table([["Module","Pages"],["Dashboard","Overview metrics, recent orders, low-stock snapshot"],["Products","Product list, create/edit form"],["Categories","Category list, create/edit form"],["Sellers","Seller list, create/edit form"],["Customers","Customer list, create/edit form"],["Cart","Cart contents, quantity editing, remove and checkout form"],["Orders","Order listing, customer history, order detail"],["Returns","Return queue and return request form"],["Inventory","Stock alert and movement ledger, restock form"],["Reports","Reports landing, low-stock, monthly sales, top products"],["Errors","Friendly not-found and validation error view"]],[34*mm,120*mm]),

    section("18. Client-Side Validation, Interactivity and AJAX"),
    P("Forms use HTML5 constraints and validation attributes (for example required, min, max, email and numeric bounds) before submission. Server-side Jakarta Bean Validation and service rules remain authoritative; client checks are usability aids, not a security boundary."),
    table([["Feature","Interaction"],["AJAX add-to-cart","jQuery $.ajax posts product/customer/quantity data to /api/cart/items and updates cart feedback without a full reload."],["Live stock check","jQuery $.getJSON requests /api/products/{id}/stock and refreshes displayed availability."],["Delete confirmation","A confirmation prompt guards destructive catalogue/customer actions."],["Form feedback","Browser constraint validation surfaces missing or out-of-range fields before request submission."]],[43*mm,111*mm]),

    section("19. Server-Side Validation and Error Handling"),
    P("Form DTOs use Jakarta Validation annotations. Controllers inspect BindingResult and return the form or a redirect with a user-facing message. Services throw domain-specific BusinessRuleException for invalid workflow transitions and ResourceNotFoundException for missing records. A global exception handler maps failures into a shared error view rather than exposing a raw stack trace as the normal user experience."),
    P("The code is a coursework demonstration without a full authentication/authorization layer. Do not deploy the customer selector or demo payment behavior to a public production environment."),

    section("20. End-to-End Workflows"),
    sub("Workflow A - cart to order"),
    Preformatted("1. Select customer and browse/filter products.\n2. Check live stock; add items via AJAX.\n3. Review quantities and shipping address.\n4. Submit checkout.\n5. Service locks cart and products.\n6. Validate quantity and ownership rules.\n7. Save order and price snapshots.\n8. Deduct stock; write SALE ledger rows.\n9. Save demo COD charge and shipment.\n10. Convert cart and display order detail/history.", styles["CodeX"]),
    sub("Workflow B - return to refund"),
    Preformatted("1. Open a delivered order and select an item.\n2. Submit reason and return quantity.\n3. Validate remaining purchased quantity.\n4. Save REQUESTED return and return line.\n5. Operator approves or rejects.\n6. On receipt, lock return and order.\n7. Calculate refund and enforce charge ceiling.\n8. Restore product stock and write RETURN ledger rows.\n9. Save linked REFUND payment.\n10. Mark return RECEIVED; update fully returned order.", styles["CodeX"]),

    section("21. Search, Navigation, Dashboard and Reports"),
    P("Product and customer listing pages support keyword filtering, pagination and sort allowlists. Order listing/history pages are also paginated. The common navigation provides links across all major modules. The dashboard reads aggregate counts and recent activity."),
    table([["Report","Measure"],["Low-stock inventory","Products where stock quantity is at or below the configured reorder threshold"],["Monthly net sales","Completed charges minus completed refunds, grouped by order month"],["Top-selling products","Ordered units and gross line value grouped by product"]],[49*mm,105*mm]),

    section("22. Testing Strategy and Test Cases"),
    P("Automated test source is supplied in three groups: four pure business-rule unit tests, two repository query tests and three MockMvc endpoint tests. The endpoint suite covers health, live stock and AJAX cart insertion. Functional-test-cases.md contains twelve manual test cases with expected outcomes and actual/status fields deliberately left pending until local execution."),
    table([["Test group","Included cases","Execution status"],["Business rule unit tests","Positive price, sufficient stock, refund ceiling, positive quantity","Not run here"],["Repository query tests","Catalogue keyword/category filter; low-stock threshold","Not run here"],["Endpoint tests","GET /api/health; GET /api/products/{id}/stock; POST /api/cart/items","Not run here"],["Functional tests","12 cases covering catalogue, cart, stock, checkout, address ownership, return, refund, restock and reports","Pending local run"]],[39*mm,81*mm,34*mm]),
    P("Verification limitation: Java 21 is available, but Maven is not installed. The included Maven Wrapper attempted to download Maven and failed because this environment could not resolve repo.maven.apache.org. MySQL and Docker CLIs are also unavailable. Therefore, no Java compilation, database migration, application runtime, or test pass is claimed. Static completeness checks passed 14 of 14 checks; these checks inspect files and requirement counts only."),
    P("Local acceptance sequence: start MySQL with Docker Compose, run ./mvnw clean verify, run the application, then execute the twelve functional cases and replace each Pending/Not executed entry with observed evidence."),

    section("23. Version Control History"),
    P("The development bundle is maintained in a local Git repository with incremental commits after reversible implementation milestones. The repository includes a curated 27-commit reconstruction with three local feature-branch merges. These are Git merge commits, not GitHub pull-request records."),
    table([["Milestone","History"],["Foundation","Bootstrap, Flyway migrations, core entities, repository and DTO layers"],["Catalogue PR-style branch","Products, categories, sellers, customers, MVC pages and AJAX"],["Orders PR-style branch","Cart, checkout, payments, inventory, returns and order history"],["Reports PR-style branch","Dashboard, reports, tests, documentation and report"]],[40*mm,114*mm]),
    P("No GitHub repository was created or pushed. GitHub pull requests must be opened and merged on GitHub; local merge commits do not create PR records. See GITHUB_PUBLISH.md for the precise publication and PR instructions."),

    section("24. Application Screenshots and Visual Evidence"),
    P("The following images are static UI design previews rendered from the project's stylesheet and page layout. They demonstrate visual composition only; they are <b>not runtime screenshots</b> because the application could not be started without Maven and MySQL in the authoring environment. Replace them with actual browser screenshots after a successful local run if the evaluator requires runtime evidence."),
    img(ASSETS/"dashboard-ui-preview.png", 153*mm, 78*mm), P("Figure 3. Dashboard design preview (not runtime evidence).", "CaptionX"),
    img(ASSETS/"products-ui-preview.png", 153*mm, 78*mm), P("Figure 4. Product listing design preview (not runtime evidence).", "CaptionX"),
    img(ASSETS/"cart-ui-preview.png", 153*mm, 78*mm), P("Figure 5. Cart design preview (not runtime evidence).", "CaptionX"),

    section("25. User Guide"),
    *bullets(["Install JDK 21 and Docker Desktop, then extract the project ZIP.","From the project root, run docker compose up -d mysql to start MySQL 8.","Run ./mvnw clean verify to compile and execute automated tests.","Run ./mvnw spring-boot:run and open http://localhost:8080.","Use the seeded customer (ID 1) and address (ID 1) for demo checkout.","Browse products, use the live stock check and add products to cart. Adjust quantities and submit checkout.","Open Orders → History to inspect the new order and its detail page.","Use Returns to create a request for a delivered order; approve/reject it, then receive an approved return.","Use Inventory to restock a product and inspect its movement ledger. Open Reports for low stock, monthly sales and top products."]),
    P("Configuration: replace the local fallback database password with environment variables before deployment. No payment gateway or login system is configured. The customer dropdown is a demonstration shortcut."),

    section("26. Conclusion and Future Enhancements"),
    P("The implementation carries the SGA1 e-commerce model into an MVC web application while preserving its 15-table domain baseline. It delivers CRUD screens, cart and checkout behavior, stock movement auditing, returns/refunds, customer order history, interactive stock/cart updates, pagination, a dashboard and report pages. Its modular structure separates controllers, service contracts and implementations, repositories, entities, DTOs and JSP views."),
    P("The source bundle includes schema migrations, demo data, tests, diagrams, user instructions, test plans and verification notes. Runtime acceptance remains a local step because build dependencies and MySQL were unreachable in the authoring environment."),
    sub("Future enhancements"),
    *bullets(["Add authentication and role-based access for customers, catalogue, inventory, order and return operators.","Integrate a payment provider with idempotent references, reconciliation and secure webhook handling.","Add shipping-carrier integration and delivery-event ingestion.","Support multiple warehouses, stock reservation and fulfilment allocation if required.","Add CI to run Maven tests and MySQL integration tests on every change.","Capture actual deployed application screenshots and attach executed functional-test evidence."]),

    section("27. References"),
    *bullets(["Building Database Applications, Staff Graded Assignment 1 brief and the student's SGA1 Project 4 database report, supplied as course materials.","Building Database Applications, Staff Graded Assignment 2 brief, supplied 15 November 2026.","Spring Boot Reference Documentation: https://docs.spring.io/spring-boot/3.3/reference/ .","Spring Data JPA Reference Documentation: https://docs.spring.io/spring-data/jpa/reference/ .","Hibernate ORM User Guide: https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html .","MySQL 8.0 Reference Manual: https://dev.mysql.com/doc/refman/8.0/en/ .","Jakarta Bean Validation: https://beanvalidation.org/ .","Apache Maven: https://maven.apache.org/guides/ ."]),
    P("Source-code architecture reference: the user's Riyura backend repository (lowkeyarhan/Riyura-backend) was consulted only for package organization and modular design conventions. No Riyura business domain or database schema was reused."),
]

# Improve image and page-break behavior by keeping each section heading with a following paragraph where appropriate.
doc = ReportDoc(str(OUT), pagesize=A4, title="E-Commerce Order and Inventory Management - SGA2", author="Arhan Das", subject="Building Database Applications SGA2 Project Report", leftMargin=LEFT, rightMargin=RIGHT, topMargin=TOP, bottomMargin=BOTTOM)
doc.multiBuild(story)
print(OUT)
