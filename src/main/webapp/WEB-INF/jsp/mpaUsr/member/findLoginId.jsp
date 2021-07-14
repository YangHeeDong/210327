<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle"
	value="<span><i class='far fa-clipboard'></i></span> <span>FIND LOGIN ID</span>" />

<%@ include file="../common/head.jspf"%>
<script>
let MemberFindLoginId__submitFormDone = false;
function MemberFindLoginId__submitForm(form) {
	
    if ( MemberFindLoginId__submitFormDone ) {
        return;
    }
    
    form.name.value = form.name.value.trim();
    
    if ( form.name.value.length == 0 ) {
        alert('이름을 입력해주세요.');
        form.name.focus();
        return;
    }
    
	form.email.value = form.email.value.trim();
    
    if ( form.email.value.length == 0 ) {
        alert('이메일을 입력해주세요.');
        form.email.focus();
        return;
    }
    
    form.submit();
    
    MemberFindLoginId__submitFormDone = true;
}
</script>

<div class="section section-member-findLoginId px-2">
	<div class="container mx-auto">
	    <form method="POST" action="doFindLoginId" onsubmit="MemberFindLoginId__submitForm(this); return false;" >
	        <input type="hidden" name="redirectUri" value="${param.afterLoginUri }" />
	        <div class="form-control">
                <label class="label">
                    이름
                </label>
                <input class="input input-bordered w-full" type="text" maxlength="30" name="name" placeholder="이름을 입력해주세요." />
            </div>

            <div class="form-control">
                <label class="label">
                    이메일
                </label>
                <input class="input input-bordered w-full" type="email" maxlength="30" name="email" placeholder="이메일을 입력해주세요." />
            </div>
            
            <div class="mt-4 btn-wrap gap-1">
                <button type="submit" class="btn btn-primary btn-sm mb-1">
                    <span><i class="fas fa-user-plus"></i></span>
                    &nbsp;
                    <span>아이디 찾기</span>
                </button>
				
				<a href="join" class="btn btn-sm btn-link mb-1" title="자세히 보기">
                    <span><i class="fas fa-home"></i></span>
                    &nbsp;
                    <span>회원가입</span>
                </a>
                
                <a href="findLoginPw" class="btn btn-sm btn-link mb-1" title="자세히 보기">
                    <span><i class="fas fa-home"></i></span>
                    &nbsp;
                    <span>비밀번호 찾기</span>
                </a>

                <a href="/" class="btn btn-sm mb-1" title="자세히 보기">
                    <span><i class="fas fa-home"></i></span>
                    &nbsp;
                    <span>홈</span>
                </a>
                
            </div>
	    </form>
	</div>
</div>

<%@ include file="../common/foot.jspf"%> 