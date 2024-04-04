<%--
  Created by IntelliJ IDEA.
  User: kwak-yejun
  Date: 4/3/24
  Time: 7:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.bson.Document" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
    <h1>Dashboard</h1>
    <h2>Operation Analytics</h2>
    <%
        // Retrieve the operations analytics from the request scope
        long planetCount = (Long) request.getAttribute("planetCount");
        long characterCount = (Long) request.getAttribute("characterCount");
        List<Document> topSearchTerms = (List<Document>) request.getAttribute("topSearchTerms");
        List<Document> modelCounts = (List<Document>) request.getAttribute("modelCounts");
    %>

    <p>Planet count: <%= planetCount %></p>
    <p>Character count: <%= characterCount %></p>

    <h3>Top search terms</h3>
    <% for (Document doc : topSearchTerms) { %>
    <p><%= doc.getString("_id") %>: <%= doc.getInteger("count") %></p>
    <% } %>

    <h3>Model counts</h3>
    <% for (Document doc : modelCounts) { %>
    <p><%= doc.getString("_id") %>: <%= doc.getInteger("count") %></p>
    <% } %>

    <h3>Logs</h3>
    <%
        // Retrieve the logs from the request scope
        List<Document> logs = (List<Document>) request.getAttribute("logs");
    %>

    <table border="1">
        <tr>
            <th>Option</th>
            <th>Search Term</th>
            <th>Model</th>
            <th>Timestamp</th>
            <th>Response</th>
        </tr>
        <% for (Document doc : logs) { %>
        <tr>
            <td><%= doc.getString("option") %></td>
            <td><%= doc.getString("searchTerm") %></td>
            <td><%= doc.getString("model") %></td>
            <td><%= doc.getDate("timestamp").toString() %></td>
            <td><%= doc.getString("response") %></td>
        </tr>
        <% } %>
    </table>
</body>
</html>
