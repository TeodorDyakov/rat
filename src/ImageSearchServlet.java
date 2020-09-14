
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@SuppressWarnings("serial")
public class ImageSearchServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// get the file chosen by the user
		Part filePart = request.getPart("fileToUpload");
		int numberOfResults = Integer.parseInt(request.getParameter("numberOfResults"));
		boolean useColor = !(request.getParameter("color") == null);
		boolean useTexture = !(request.getParameter("texture") == null);

		if (filePart.getSubmittedFileName().endsWith(".jpg") || filePart.getSubmittedFileName().endsWith(".png")) {

			InputStream fileInputStream = filePart.getInputStream();

			File fileToSave = new File("WebContent/uploaded-files/" + filePart.getSubmittedFileName());
			Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);

			BufferedImage queryImg = ImageIO.read(fileToSave);
			List<File> results = null;

			try {
				results = Searcher.getSearchResults(numberOfResults, "WebContent/images", queryImg, true, useTexture,
						useColor);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			response.getOutputStream()
					.println("<p>Thanks " + "! Here are the top " + numberOfResults + " results:</p>");

			for (File f : results) {
				String resultImageName = "http://localhost:8080/images/" + f.getName();
				response.getOutputStream().println("<img src=\"" + resultImageName + "\"/>");
			}
			response.getOutputStream()
					.println("<p>Upload another image <a href=\"http://localhost:8080/index.html\">here</a>.</p>");
		} else {
			// the file was not a JPG or PNG
			response.getOutputStream().println("<p>Please only upload JPG or PNG files.</p>");
			response.getOutputStream()
					.println("<p>Upload another file <a href=\"http://localhost:8080/index.html\">here</a>.</p>");
		}
	}
}