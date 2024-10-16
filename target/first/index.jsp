<%
    if (session.getAttribute("id") == null){
        response.sendRedirect("login.jsp"); 
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Web</title>
</head>
<body>
    Hello, 
    <p>
        <%= session.getAttribute("id")%>
    </p>

    <a href="/first/logout">Logout</a>
</body>
</html>