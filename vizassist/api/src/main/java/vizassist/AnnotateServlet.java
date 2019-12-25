package vizassist;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;

/**
 * Servlet implementation class AnnotateServlet
 */
@WebServlet("/annotate")
public class AnnotateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnnotateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		ByteString imgBytes = ByteString.readFrom(request.getInputStream());
		Image img = Image.newBuilder().setContent(imgBytes).build();

		try {
			response.getWriter().print(AnnotateHelper.Annotate(img));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
