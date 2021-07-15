<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
.home-box{
	width:50%;
	height:100%;
	float : left;
}
.home{
	border-radius: 5%;
	overflow: hidden;
}
.img{
    position: relative;                                                               
    height: 100%;
    background-size: cover;
    display:flex;
	align-items:center;
  }

.img-cover {
	position: absolute;
	height: 100%;
	width: 100%;
	background-color: rgba(0, 0, 0, 0.7);                                                                 
	z-index:1;
}

.img .content {
	position: absolute;
	top:50%;
	left:50%;
	transform: translate(-50%, -50%);                                                                   
	font-size:4rem;
	color: white;
	z-index: 2;
	text-align: center;
}
</style>
<c:set var="pageTitle" value="<span><i class='fas fa-home'></i></span><span> </span><span>HOME</span>"/>
<%@ include file ="../common/head.jspf"%>
<div class="section section-home">
	<div class="home container mx-auto cursor-pointer mb-4">
		<a href="../article/list?boardId=1" class="home-box cursor-pointer">
			<div class="img">
				<div class="content">
					<div class="text-7xl">공지사항</div>
				</div>
				<div class="img-cover"></div>
				<img src="/resource/img/확성기.png" alt="">
			</div>
		</a>
		<a href="../article/list?boardId=2" class="home-box cursor-pointer">
			<div class="img">
				<div class="content">
					<div class="text-7xl">자유게시판</div>
				</div>
				<div class="img-cover"></div>
				<img src="/resource/img/자유게시판.png" alt="">
			</div>
		</a>
    </div>
</div>
<%@ include file ="../common/foot.jspf"%>