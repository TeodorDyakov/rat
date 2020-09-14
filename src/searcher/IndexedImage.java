package searcher;

import java.io.File;
import java.io.Serializable;

import util.MathUtils;

public class IndexedImage implements Serializable {
	File file;

	float[] imageDescriptor;

	public IndexedImage(File file, float[] imageDescriptor) {
		this.file = file;
		this.imageDescriptor = imageDescriptor;
	}

	public static float similarity(float[] img1, float[] img2, boolean useTexture, boolean useColor) {
		if (useTexture == false) {
			for (int i = 0; i < 256; i++) {
				img1[i] = 0;
				img2[i] = 0;
			}
		}
		if (useColor == false) {
			for (int i = 256; i < img1.length; i++) {
				img1[i] = 0;
				img2[i] = 0;
			}
		}
		return MathUtils.similarity(img1, img2);
	}

}
