<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<script src="/assert/js/jquery.min.js"></script>
<link href="/assert/css/bootstrap.min.css" rel="stylesheet"/>
<script src="/assert/js/bootstrap.min.js"></script>
<title>채팅</title>
<style>
.admin-msg{
	color: gray;
}
.chatbox{
	overflow : scroll;
	overflow-x : hidden;
	background : #fff;
	border-color : #ddd;
	border-width : 1px;
	border-radius: 4px 4px 0 0;
	border-style: solid;
}

</style>
<script>
	var socket;
	
	$(function() {
		console.log(window.location.host);
		socket = new WebSocket("ws://"+window.location.host+"/chatjoin");
		
		socket.onopen = onOpen;
		
		socket.onmessage = onMessage;
		
		socket.onclose = onClose;
		
		$("input[name=message]").keypress(function(e) {
			if(e.keyCode == 13)
				sendMessage();
		})
		
	});
	
	function onOpen(){
		console.log("접속 완료");
	}
	
	function onMessage(event){
		console.log(event);
		var data = JSON.parse(event.data);
		
		if(data.type == "msg"){
			typeMsg(data);
		}else if(data.type == "adminMsg"){
			typeAdmin(data);
		}
	}
	
	function onClose(){
		$("#chatBox").append("<p class='admin-msg'>채팅 서버와 연결이 종료 되었습니다.</p>");
	}
	
	function sendMessage(){
		var message = $("input[name=message]").val();
		$("input[name=message]").val("");
		var msg = {
			type : "msg",
			message : message
		};
		console.log(JSON.stringify(msg));
		socket.send(JSON.stringify(msg));
	}
	
	function typeMsg(data){
		$("#chatBox").append("<p><b>" + data.name +"</b>: " + data.message + "</p>");
		scrollDown();
	}
	
	function typeAdmin(data) {
		$("#chatBox").append("<p class='text-center admin-msg'>" + data.message + "</p>");
		scrollDown();
	}
	
	function scrollDown(){
		$("#chatBox").scrollTop(99999999);
	}
	
</script>
</head>
<body class="container" style="height: 100vh;">

	<div style="display: table; width:100%; height: 100%;">
		<div style="display: table-cell; vertical-align: middle;">
			<h2 style="text-align: left;">
				채팅
				<button class="btn btn-default" style="float:right;" onclick="location.href='/board'">게시판</button>
			</h2>
			<hr>
			
			<div id="chatBox" class="chatbox "  style="height: 400px;">
				
			</div>
			<div class="input-group" style="margin-top: 20px;">
				<input class="form-control" name="message" type="text" >
				<span class="input-group-addon btn" onclick="sendMessage()">전송</span>
			</div>
			<div style="margin-top: 20px;">
				
			</div>
		</div>
	</div>
</body>
</html>