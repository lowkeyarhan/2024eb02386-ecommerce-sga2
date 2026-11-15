<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<section class="panel empty"><p class="eyebrow">Request status <c:out value="${status}"/></p><strong><c:out value="${title}"/></strong><p><c:out value="${message}"/></p><a class="button" href="/">Return to dashboard</a></section>
<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
