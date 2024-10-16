<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <p>
        <%= request.getAttribute("error") == null ? "" : request.getAttribute("error")%>
    </p>
    <form name="formRegister" action="/first/register" method="POST">
        <input type="text" name="username" placeholder="Username">
        <input type="text" name="password" placeholder="Password">
        <input type="submit" value="Register">
    </form>
</body>
</html>