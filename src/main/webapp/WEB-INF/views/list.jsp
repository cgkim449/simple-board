<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 - 목록</title>
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script>
        function viewForm(boardNo) {
            let viewForm = $("#view_form");
            viewForm.attr("action", "/boards/"+boardNo)
            viewForm.submit();
        }
        function boardsForm(page) {
            let queryString = "?page=" + page
                + "&category=" + ${filterVo.category}
                +"&keyword=" + "${filterVo.keyword}"
                + "&startdate=" + "${filterVo.startdate}"
                + "&enddate=" + "${filterVo.enddate}";
            location.href = "boards" + queryString;
        }
        //
        $(document).ready(function(){
            $("#select_category").children().eq(${filterVo.category}).prop('selected', 'selected').change();
            let message = "${message}";
            if(message !== "") {
                alert(message);
            }
        });
        function boardsNewForm() {
            $("#boards_new_form").submit();
        }
    </script>
</head>
<body>
<div>
    <a href="/boards">게시판 - 목록</a>
</div>

<%-- 검색 시작 --%>
<div>
    <form method="get" action="/boards">

        <input type="date" name="startdate" value="${filterVo.startdate}"> ~ <input type="date" name="enddate" value="${filterVo.enddate == "9999-12-30" ? '':filterVo.enddate}">
        <select id="select_category"name="category" >
            <option value="0">전체 카테고리</option>
            <option value="1">Java</option>
            <option value="2">JavaScript</option>
            <option value="3">Database</option>
        </select>
        <input type="text" name="keyword" placeholder="검색어를 입력해주세요. (제목 + 작성자 + 내용)" value="${filterVo.keyword}">
        <input type="hidden" name="page" value="1">
        <input type="submit" value="검색">
    </form>
</div>
<%-- 검색 끝 --%>
<%-- 게시판 목록 시작 --%>
<div>
    <c:out value="총 ${filterVo.totalCnt} 건"></c:out>
</div>
<table border="1">
    <tr>
        <th>게시글 번호</th>
        <th>카테고리</th>
        <th>첨부파일</th>
        <th>제목</th>
        <th>작성자</th>
        <th>조회수</th>
        <th>등록 일시</th>
        <th>수정 일시</th>
    </tr>
    <c:forEach var="boardVo" items="${boardVoList}">
        <tr>
            <td><c:out value="${boardVo.boardNo}"></c:out></td>
            <td><c:out value="${boardVo.categoryName}"></c:out></td>
            <td><c:out value="${boardVo.hasAttach == true ? \"📎\" : \"\"}"></c:out></td>
            <td>
                <a href="javascript:void(0);" onclick="viewForm(${boardVo.boardNo})">
                    <c:out value="${boardVo.title.length() > 80 ? boardVo.title.substring(0, 80).concat(\"...\"): boardVo.title}"></c:out>
                </a>
            </td>
            <td><c:out value="${boardVo.writer}"></c:out></td>
            <td><c:out value="${boardVo.viewCnt}"></c:out></td>
            <td><c:out value="${boardVo.registerDate.toString().substring(0, 16).replaceAll(\"-\", \".\")}"></c:out></td>
            <td><c:out value="${boardVo.registerDate == boardVo.updateDate ? \"-\" : boardVo.updateDate.toString().substring(0, 16).replaceAll(\"-\", \".\")}"></c:out></td>
        </tr>
    </c:forEach>
</table>
<%-- 게시판 목록 끝 --%>
<!-- 페이지네이션 시작 -->
<div>
    <c:if test="${empty boardVoList}">
        검색 결과 없음
    </c:if>
    <c:if test="${!empty boardVoList}">
        <c:if test="${filterVo.page != 1}">
            <a href="javascript:void(0);" onclick="boardsForm(1)"><c:out value="<<"></c:out></a>
            <a href="javascript:void(0);" onclick="boardsForm(${filterVo.page - 1})"><c:out value="<"></c:out></a>
        </c:if>
        <c:forEach var = "i" begin="${filterVo.paginationFirstPage}" end="${filterVo.paginationLastPage}">
            <c:if test = "${i == filterVo.page}">
                <c:out value="${i}"></c:out>
            </c:if>
            <c:if test = "${i != filterVo.page}">
                <a href="javascript:void(0);" onclick="boardsForm(${i})"><c:out value="${i}"></c:out></a>
            </c:if>
        </c:forEach>
        <c:if test="${filterVo.page != filterVo.lastPage}">
            <a href="javascript:void(0);" onclick="boardsForm(${filterVo.page + 1})"><c:out value=">"></c:out></a>
            <a href="javascript:void(0);" onclick="boardsForm(${filterVo.lastPage})"><c:out value=">>"></c:out></a>
        </c:if>
    </c:if>
</div>
<!-- 페이지네이션 끝 -->
<div>
    <button onclick="boardsNewForm()">등록</button>
</div>
<form id="boards_new_form" action="/boards/new" method="get">
    <input type="hidden" name="page" value="${filterVo.page}">
    <input type="hidden" name="category" value="${filterVo.category}">
    <input type="hidden" name="keyword" value="${filterVo.keyword}">
    <input type="hidden" name="startdate" value="${filterVo.startdate}">
    <input type="hidden" name="enddate" value="${filterVo.enddate}">
</form>
<form id="view_form" action="" method="get">
    <input type="hidden" name="page" value="${filterVo.page}">
    <input type="hidden" name="category" value="${filterVo.category}">
    <input type="hidden" name="keyword" value="${filterVo.keyword}">
    <input type="hidden" name="startdate" value="${filterVo.startdate}">
    <input type="hidden" name="enddate" value="${filterVo.enddate}">
</form>
</body>
</html>