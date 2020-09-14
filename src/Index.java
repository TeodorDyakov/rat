
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import util.ArrayUtils;

public class Index implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<IndexedImage> index;

	int binsPerColor = 8;

	Index(int binsPerColor) {
		this.binsPerColor = binsPerColor;
		index = new ArrayList<>();
	}

	/**
	 * Serialize the index to a file
	 * 
	 * @param file
	 * @throws IOException
	 */
	void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(this);
		out.close();
		fos.close();
	}

	/**
	 * returns the deserialized index from the file
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static Index loadIndexFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fis);
		Index index = (Index) in.readObject();
		in.close();
		fis.close();
		return index;
	}

	/**
	 * Loads the image form the given file, computes the image descriptor, packages
	 * the file and descriptor in an object and returns it.
	 * 
	 * @param imageFile
	 * @return an instance ofIndexedImage containing the imageFile and the image
	 *         descriptor.
	 * @throws IOException
	 */
	IndexedImage createIndexedImage(File imageFile) {
		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
		} catch (IOException e) {
			return null;
		}

		float[] imageDescriptor = ImageDescriptors.computeImageDescriptor(img, binsPerColor);
		IndexedImage indexedImage = new IndexedImage(imageFile, imageDescriptor);
		return indexedImage;
	}

	/**
	 * Adds all the image files in the given folder to the index
	 * 
	 * @param pathToImageFolder
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void addToIndex(String pathToImageFolder) throws ClassNotFoundException {

		long tic = System.currentTimeMillis();

		int count = 0;
		File[] listOfFiles = new File(pathToImageFolder).listFiles();

		for (File f : listOfFiles) {

			System.out.println(count++ + "/" + listOfFiles.length + " images indexed");
			IndexedImage ii = createIndexedImage(f);

			if (ii != null)
				index.add(ii);
			System.out.println(f.getName());
		}

		System.out.println("index built in: " + (System.currentTimeMillis() - tic) / 1000f + "seconds");
	}

}
