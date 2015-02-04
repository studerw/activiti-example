<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
    <link href="${pageContext.request.contextPath}/resources/js/chosen_v1.3.0/chosen.css" rel="stylesheet">
    <%--<link href="${pageContext.request.contextPath}/resources/css/bootstrap-choosen.css" rel="stylesheet">--%>

    <title>Chosen Test</title>

    <style type="text/css">
        .panel-task {
            margin: 15px 0;
        }

        div#diagram {
            margin: 20px auto;
        }
    </style>
</head>
<body>
<jsp:include page="../fragments/navbar-top.jsp"/>

<div class="container">
    <h4>Chosen Test</h4>

    <div>
        <select data-placeholder="Choose a Country..." class="chosen-select" style="width:350px;" tabindex="1">
            <option value=""></option>
            <option value="United States">United States</option>
            <option value="United Kingdom">United Kingdom</option>
            <option value="Afghanistan">Afghanistan</option>
            <option value="Aland Islands">Aland Islands</option>
            <option value="Suriname">Suriname</option>
            <option value="Svalbard and Jan Mayen">Svalbard and Jan Mayen</option>
            <option value="Swaziland">Swaziland</option>
            <option value="Sweden">Sweden</option>
            <option value="Switzerland">Switzerland</option>
            <option value="Syrian Arab Republic">Syrian Arab Republic</option>
            <option value="Taiwan, Province of China">Taiwan, Province of China</option>
            <option value="Tajikistan">Tajikistan</option>
            <option value="Tanzania, United Republic of">Tanzania, United Republic of</option>
            <option value="Zimbabwe">Zimbabwe</option>
        </select>
    </div>

    <div style="margin-top: 5em;">
        <select data-placeholder="Choose a Country..." class="chosen-select" style="width:350px;" tabindex="1" multiple>
            <option value=""></option>
            <option value="United States">United States</option>
            <option value="United Kingdom">United Kingdom</option>
            <option value="Afghanistan">Afghanistan</option>
            <option value="Aland Islands">Aland Islands</option>
            <option value="Suriname">Suriname</option>
            <option value="Svalbard and Jan Mayen">Svalbard and Jan Mayen</option>
            <option value="Swaziland">Swaziland</option>
            <option value="Sweden">Sweden</option>
            <option value="Switzerland">Switzerland</option>
            <option value="Syrian Arab Republic">Syrian Arab Republic</option>
            <option value="Taiwan, Province of China">Taiwan, Province of China</option>
            <option value="Tajikistan">Tajikistan</option>
            <option value="Tanzania, United Republic of">Tanzania, United Republic of</option>
            <option value="Zimbabwe">Zimbabwe</option>
        </select>
    </div>

</div>


</div>

<jsp:include page="../fragments/footer.jsp"/>

<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>--%>
<script src="${pageContext.request.contextPath}/resources/js/chosen_v1.3.0/chosen.jquery.js"></script>
<script>
    (function ($) {
        $(document).ready(function () {
            $('li#nav-workflows').addClass('active');
            //set up JQuery choosen plugin
            var config = {
                '.chosen-select': {},
                '.chosen-select-deselect': {allow_single_deselect: true},
                '.chosen-select-no-single': {disable_search_threshold: 10},
                '.chosen-select-no-results': {no_results_text: 'Oops, nothing found!'}
//        '.chosen-select-width': {width: "95%"}
            }
            for (var selector in config) {
                $(selector).chosen(config[selector]);
            }

        });
    })(jQuery);
</script>

</body>
</html>
