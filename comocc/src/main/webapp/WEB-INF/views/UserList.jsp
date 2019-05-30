<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ocupload-1.1.2.js"></script>

    <script type="text/javascript">

        $(function () {
            /*$(element).upload({
                name: 'file',//上传组件的name属性，即<input type='file' name='file'/>
                action: '',//向服务器请求的路径
                enctype: 'multipart/form-data',//mime类型，默认即可
                params: {},//请求时额外传递的参数，默认为空
                autoSubmit: true,//是否自动提交，即当选择了文件，自动关闭了选择窗口后，是否自动提交请求。
                onSubmit: function() {},//提交表单之前触发事件
                onComplete: function() {},//提交表单完成后触发的事件
                onSelect: function() {}//当用户选择了一个文件后触发事件
            });*/
            //调用OCUpload插件的方法
            $("#uploadExcel").upload({

                action:"${pageContext.request.contextPath}/user/upload", //表单提交的地址
                name:"myFile",
                onComplete:function (data) { //提交表单之后
                    if(data == "0"){
                        //$("#myModal").modal(); //提示框，Excel导入成功
                        alert("Excel导入成功!");
                    }else{
                        //$("#myModal2").modal(); //提示框，Excel导入失败
                        alert("Excel导入失败!");
                    }
                },
                onSelect: function() {//当用户选择了一个文件后触发事件
                    //当选择了文件后，关闭自动提交
                    this.autoSubmit=false;
                    //校验上传的文件名是否满足后缀为.xls或.xlsx
                    var regex =/^.*\.(?:xls|xlsx)$/i;
                    //this.filename()返回当前选择的文件名称 (ps：我使用这个方法没好使，自己写了一个获取文件的名的方法) $("[name = '"+this.name()+"']").val())
                    //alert(this.filename());
                    if(regex.test($("[name = '"+this.name()+"']").val())){
                        //通过校验
                        this.submit();
                    }else{
                        //未通过
                        //$("#myModal3").modal(); //错误提示框，文件格式不正确，必须以.xls或.xlsx结尾
                        alert("文件格式不正确!");
                    }
                }
            });

            $('#js-export').click(function(){
                window.location.href="${pageContext.request.contextPath}/user/export";
            });
        });


    </script>

</head>
<body>
<div >
    <button type="button" id="uploadExcel" class="btn btn-primary disabled" data-btn-type="selectIcon">
        <i class="fa fa-mail-forward">&nbsp;上传Excel</i>
    </button>
    <button id="js-export" type="button" class="btn btn-primary disabled" data-btn-type="selectIcon">
        <i class="fa fa-mail-forward">&nbsp;导出Excel</i>
    </button>
</div>
<form id="myForm" action="${pageContext.request.contextPath}/user/getUserModel" method="post" style="text-align: center" >
    <label>姓名：</label>
    <input type="text" name="username" maxlength="64" placeholder="姓名" style="width:150px;"/>
    <label>地址：</label>
    <input type="text" name="address" maxlength="64" placeholder="地址" style="width:150px;"/>
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" style="margin-left: 10px;"/>
</form>

<div style="text-align: center;">
    <table border="1" style="margin: auto" width='60%'>
        <tr>
            <td>姓名</td>
            <td>生日</td>
            <td>性别</td>
            <td>地址</td>
        </tr>

        <c:forEach items="${users}" var="u">
            <tr>
                <td>${u.username}</td>
                <td>
                    <c:if test="${u.birthday != null}">
                        <fmt:formatDate pattern="yyyy-MM-dd" value="${u.birthday}"/>
                    </c:if>
                </td>
                <td>
                    <c:if test="${u.sex eq '1'}">
                        男
                    </c:if>
                    <c:if test="${u.sex eq '2'}">
                        女
                    </c:if>
                </td>

                <td>${u.address}</td>
            </tr>
        </c:forEach>

    </table>
</div>
</body>
</html>
