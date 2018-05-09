package dao.jdbc;

/**
 * Exception thrown when DB manipulation is unsuccessful
 * @author Meszaros Gergely
 *
 */
public class DbException extends Exception {
	private static final long serialVersionUID = 1L;
	// constructor without parameters
	public DbException() {
		super();
	}
	public DbException(DbException e){
		super(e);
	}
	// constructor for exception description
	public DbException(String description) {
		super(description);
	}
}
