<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
<title>image search</title>
<link rel="stylesheet" href="style.css" type="text/css">
</head>
<body>
	<form action="" method="POST" enctype="multipart/form-data" class="format">
        <p class="heading">This is a simple image search engine that let's you upload an image and displays images with similar colors and
        texture</p>
        
        <p>Upload a .jpg or .png image:</p>
        
        <input type="file" name="fileToUpload" id="browse"> <br> <br>

        <input type="checkbox" id="texture" name="texture"
        value="texture" checked > <label for="texture"> use texture</label><br>

        <input type="checkbox" id="color"
        name="color" value="color" checked> <label for="color"> use color</label><br>

        <p>How many results do you want?</p>
        <input type="text" name="numberOfResults" value="10" maxlength="4" size="6" id="howmany"> <input type="submit"
        value="Search">
    </form>
	<% 
	if(request.getAttribute("results") != null){
	%>
		<p>Results:</p>
		<% 
		List<String> messages = (List<String>) request.getAttribute("results"); 
		for(int i = 0; i < messages.size(); i++){
			String message = messages.get(i);
		%>
			<img src= <%= message %>>
		<%
		}
		%>
		<hr/>
	<%
	}
	%>
</body>
</html>