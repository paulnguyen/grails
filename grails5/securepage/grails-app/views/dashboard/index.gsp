
<%@ page contentType="text/html;charset=UTF-8" %>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Secure Dashboard</title>
</head>
<body>
<div id="content" role="main">
    <section class="row colset-2-its">
        <sec:ifLoggedIn>
            <h1>Welcome to the secure dashboard <sec:loggedInUserInfo field='fullname'/>!</h1>
            <p><sec:loggedInUserInfo field='address'/></p>
            <h2><g:link controller="logout">Logout</g:link></h2>
        </sec:ifLoggedIn>
    </section>
</div>
</body>
</html>