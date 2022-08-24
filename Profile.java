package socialnetwork;


public class Profile {
	public Profile(String name) {
		this.name = name;
		this.status = "Hi!";
	}
	private String name;
	private String image;
	private String status;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name = ");
		builder.append(name);
		builder.append(",\nimage = ");
		builder.append(image);
		builder.append(",\nstatus = ");
		builder.append(status);
		builder.append(",");
		return builder.toString();
	}
	
	

}
