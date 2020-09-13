
import java.io.File;
import java.io.Serializable;

public class IndexedImage implements Serializable {
	private static final long serialVersionUID = 1L;
	File file;
	float[] imageDescriptor;

	public IndexedImage(File file, float[] imageDescriptor) {
		this.file = file;
		this.imageDescriptor = imageDescriptor;
	}
}
