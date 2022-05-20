<%--
  Created by IntelliJ IDEA.
  User: s_rudwhd515
  Date: 2022-02-28(028)
  Time: 오후 3:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html>

<head>
    <title>코딩 전문가를 만들기 위한 온라인 강의 시스템</title>
    <meta charset="UTF-8">
    <title>공지사항목록</title>

    <link href="/css/layout.css" type="text/css" rel="stylesheet" />
    <link href="/css/index.css" type="text/css" rel="stylesheet" />
    <script>

    </script>
</head>

<body>
<!-- header 부분 -->
<tiles:insertAttribute name="header"/>

<!-- --------------------------- <body> --------------------------------------- -->

<!-- content 부분 -->
<tiles:insertAttribute name="main"/>


<!-- ------------------- <footer> --------------------------------------- -->

<tiles:insertAttribute name="footer"/>
</body>

</html>