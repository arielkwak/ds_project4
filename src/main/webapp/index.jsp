<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Rick and Morty</title>
</head>
<body>
    <h1><%= "Rick and Morty Web Service!" %></h1>
    <br/>
    <h2>Option Selected <%= request.getAttribute("option") %></h2>
    <br/>

    <% if (request.getAttribute("option").equals("planet")) { %>
        <h3>Planets</h3>
        <ul>
            <% for (String planet : (List<String>) request.getAttribute("planets")) {%>
                <li><%= planet %></li>
            <% } %>
        </ul>
    <% } else if (request.getAttribute("option").equals("character")) { %>
        <h3>Character Result</h3>
        <p>User's search on <%=request.getAttribute("searchTerm")%></p>
        <ul>
            <% for (Map.Entry<String, Object> entry : ((Map<String, Object>) request.getAttribute("characterResults")).entrySet()) { %>
                <li><%= entry.getKey() %>:<%= entry.getValue() %></li>
            <% } %>
        </ul>
    <% } %>
</body>
</html>