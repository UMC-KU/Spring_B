<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
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

<tiles:insertAttribute name="main"/>
<!-- ------------------- <footer> --------------------------------------- -->

<tiles:insertAttribute name="footer"/>

</body>

</html>
