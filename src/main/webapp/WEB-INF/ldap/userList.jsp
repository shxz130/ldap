<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="../Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="../Css/bootstrap-responsive.css" />
    <link rel="stylesheet" type="text/css" href="../Css/style.css" />
    <script type="text/javascript" src="../Js/jquery.js"></script>
    <script type="text/javascript" src="../Js/jquery.sorted.js"></script>
    <script type="text/javascript" src="../Js/bootstrap.js"></script>
    <script type="text/javascript" src="../Js/ckform.js"></script>
    <script type="text/javascript" src="../Js/common.js"></script>
    <style type="text/css">
        body {
            padding-bottom: 40px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }


    </style>
</head>
<body>
<form class="form-inline definewidth m20" action="goUserList.shtml" method="post">
    用户名称：
    <input type="text" name="givenName" id="username"class="abc input-default" placeholder="" value="">&nbsp;&nbsp;
    <button type="submit" class="btn btn-primary">查询</button>&nbsp;&nbsp;
    <button type="button" class="btn btn-success" id="addnew">新增用户</button>
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
        <th>GivenName</th>
        <th>userPassword</th>
        <th>homeDirectory</th>
        <th>操作</th>
    </tr>
    </thead>

    <c:forEach items="${users}" var="user">
        <tr>
            <td><c:out value="${user.givenName}"/></td>
            <td><c:out value="${user.userPassword}"/></td>
            <td><c:out value="${user.homeDirectory}"/></td>
            <td><a href="javascript:deleteUser('${user.givenName}');">删除</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
<script>
    $(function () {
        $('#addnew').click(function(){
            window.location.href="goUserAdd.shtml";
        });
    });


    function deleteUser(givenName){
        $.ajax({
                type:"POST",
                url: "userDelete.shtml",
                data: {
                    "givenName": givenName
                },
                dataType: "json",
                success: function(data){
                   if(data.code=='1'){
                       alert("删除成功");
                       window.location.reload();
                   }else{
                       alert("删除失败");
                   }
                }
        });
    }

</script>