<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>test</title>
    <jsp:include page="fragments/head.jsp"/>
</head>

<body>

<jsp:include page="fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">
        <h1>Deferred</h1>

        <form>
            <div class="form-group">
                <label for="input1">Input1</label>
                <input type="text" class="form-control" id="input1" >
            </div>
            <div class="form-group">
                <label for="time1">Time2</label>
                <input type="text" class="form-control" id="time1">
            </div>
            <div class="form-group">
                <label for="input2">Input 2</label>
                <input type="text" class="form-control" id="input2" >
            </div>
            <div class="form-group">
                <label for="time2">Time 2</label>
                <input type="text" class="form-control" id="time2">
            </div>

            <button type="button" id="submitBtn1" class="btn btn-default">Call Async</button>
            <button type="button" id="submitBtn2" class="btn btn-default">Call Deferred</button>
        </form>
    </div>

</div>
<!-- /.container -->
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript">
    var SERVLET_CONTEXT = '${pageContext.request.contextPath}';
    (function ($) {
        $(document).ready(function () {
            $('li#nav-home').addClass('active');
            $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
            $('#submitBtn1').click(function(){
               $.ajax({
                    type: 'GET',
                    url: SERVLET_CONTEXT + '/deferred/capitalize?arg=' + $('#input1').val(),
                    headers: {
                        Accept: "application/json"
                    },
                    success: function (data) {
                        console.dir(data);
                        $('#input1').val(data.result);
                        var timeInMs = Date.now();
                        $('#time1').val(timeInMs);
                    },
                    error: function (error) {
                        console.dir(error);
                        alert("There was an error getting val1: " + error);
                    }
                });

               $.ajax({
                    type: 'GET',
                    url: SERVLET_CONTEXT + '/deferred/capitalize?arg=' + $('#input2').val(),
                    headers: {
                        Accept: "application/json"
                    },
                    success: function (data) {
                        console.dir(data);
                        $('#input2').val(data.result);
                        var timeInMs = Date.now();
                        $('#time2').val(timeInMs);
                    },
                    error: function (error) {
                        console.dir(error);
                        alert("There was an error getting the val2: " + error);
                    }
                });
             });

            var a = function() {
                var arg = $('#input1').val();
                return $.ajax({
                    type: 'GET',
                    url: SERVLET_CONTEXT + '/deferred/capitalize?arg=' + arg,
                    headers: {
                        Accept: "application/json"
                    }
                });
            }


            var b = function() {
                var arg = $('#input2').val();
                return $.ajax({
                    type: 'GET',
                    url: SERVLET_CONTEXT + '/deferred/capitalize?arg=' + arg,
                    headers: {
                        Accept: "application/json"
                    }
                });
            }

            $('#submitBtn2').click(function() {

                $.when(a(), b()).done(function (result1, result2) {
                    console.dir(result1);
                    console.dir(result2);
                    var timeInMs = Date.now();
                    $('#input1').val(result1[0].result);
                    $('#time1').val(timeInMs);
                    $('#input2').val(result2[0].result);
                    $('#time2').val(timeInMs);
                });

            });



        });
    })(jQuery);
</script>
</body>
</html>
