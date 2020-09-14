
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import util.ArrayUtils;
import util.ImageUtils;
import util.MathUtils;

public class ImageDescriptors {

	/**
	 * computes the global LBPH (local binary pattern histogram) for the given 2d
	 * array. Ref:
	 * https://www.pyimagesearch.com/2015/12/07/local-binary-patterns-with-python-opencv/
	 * 
	 * @param img 2d array of image.
	 * @return LBPH (local binary pattern histogram) for the given 2d
	 */

	public static int[] lbph(int[][] img) {

		final int[] histogram = new int[256];

		final int[] dx = { -1, -1, -1, 0, 1, 1, 1, 0 };
		final int[] dy = { -1, 0, 1, 1, 1, 0, -1, -1 };

		for (int i = 1; i < img.length - 1; i++) {
			for (int j = 1; j < img[0].length - 1; j++) {

				int pow = 1;
				int pattern = 0;
				final int center = img[i][j];

				for (int k = 0; k < dx.length; k++) {
					if (center > img[i + dx[k]][j + dy[k]]) {
						pattern += pow;
					}
					pow *= 2;
				}
				histogram[pattern]++;
			}
		}
		return histogram;

	}

	public static int[] lbp(BufferedImage img) {
		return lbph(ImageUtils.imagetoGreycale2dArray(img));
	}

	/**
	 * Computes the color histogram for img, The returned vector will be of
	 * dimension binsPerColor^3. Reference:
	 * https://en.wikipedia.org/wiki/Color_histogram
	 * 
	 * @param img
	 * @param binsPerColor
	 * @return
	 */
	static int[] colorHistogram(BufferedImage img, int binsPerColor) {
		final int[][][] histogram = new int[binsPerColor][binsPerColor][binsPerColor];
		final int div = (int) Math.ceil(256d / binsPerColor);
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));
				histogram[c.getRed() / div][c.getGreen() / div][c.getBlue() / div]++;
			}
		}
		return (ArrayUtils.flatten(histogram));
	}

	/**
	 * This method computes the given features for the image img, combines the my
	 * concatenation and returns them as one vector
	 * 
	 * @param img
	 * @param binsPerColor - bins per color for the color histogram
	 * @param useLbp       - whether to use lbp (local binary pattern) information
	 * @param useColor     - whether to use the color histogram of the image
	 * @return the image descriptor for img
	 */
	static float[] computeImageDescriptor(BufferedImage img, int binsPerColor) {
		float[] lbph = MathUtils.normalize(lbp(img));
		float[] color = MathUtils.normalize(colorHistogram(img, binsPerColor));
		float[] desc = ArrayUtils.concat(lbph, color);
		return desc;
	}

}
