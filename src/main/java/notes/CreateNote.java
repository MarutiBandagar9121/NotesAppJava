package notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateNote
 */
@WebServlet(
		description = "Creating new note servlet",
		urlPatterns = { "/createnote" },
		initParams = {
				@WebInitParam(name = "databaseUrl", value = "jdbc:mysql://localhost:3306/notesappservlet"),
				@WebInitParam(name = "databaseUser", value = "root"),
				@WebInitParam(name = "password", value = "Maruti@12345")
	}
)
public class CreateNote extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection=null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNote() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		System.out.println("server initialized");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String databaseUrl=getServletConfig().getInitParameter("databaseUrl");
		String databaseUser=getServletConfig().getInitParameter("databaseUser");
		String databasePassword=getServletConfig().getInitParameter("password");
    	
    	try {
			connection=DriverManager.getConnection(databaseUrl,databaseUser,databasePassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Executing post request of createnote");
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		String noteTitle=request.getParameter("note-title");
		String note=request.getParameter("note");
		
		if(connection!=null) {
			if(noteTitle.length()==0) {
				out.println("<html><head><title>Notes</title></head><body>");
 				out.println("<h3>Note title cant be empty<h3>");
 				out.println("<a href='/NotesApp/createnote.html'>Create Note</a>");
 				out.println("</body></html>");
			}
			else if(note.length()==0) {
				out.println("<html><head><title>Notes</title></head><body>");
 				out.println("<h3>Note cant be empty<h3>");
 				out.println("<a href='/NotesApp/createnote.html'>Create Note</a>");
 				out.println("</body></html>");
			}
			else {
				String insertQuery="insert into notes (notetitle,note) values(?,?)";
				
				try {
					PreparedStatement stmt=connection.prepareStatement(insertQuery);
					stmt.setString(1, noteTitle);
					stmt.setString(2, note);
					
					stmt.executeUpdate();
					out.println("<html><head><title>Notes</title></head><body>");
	 				out.println("<h3>Note Saved Successfully.<h3>");
	 				out.println("<a href='/NotesApp/createnote.html'>Create Note</a>");
	 				out.println("<br>");
	 				out.println("<a href='/NotesApp/viewnote.jsp'>View All Notes</a>");
	 				out.println("<br>");
	 				out.println("<a href='/NotesApp'>Log Out</a>");
	 				out.println("</body></html>");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			out.println("Databse Connection Failed");
		}
	}

}
