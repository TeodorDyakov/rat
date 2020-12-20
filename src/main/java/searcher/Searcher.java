package searcher;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.ArrayUtils;

public class Searcher {
	public static List<File> getSearchResults(int k, String folderPath, BufferedImage query, boolean loadIndexFromFile,
			boolean useTexture, boolean useColor) throws ClassNotFoundException, IOException {

		int binsPerColor = 8;
		Index index = new Index(binsPerColor);

		File serializedIndexFile = new File("seriallized_index.bin");
		System.out.println(serializedIndexFile.getAbsolutePath());
		if (loadIndexFromFile) {
			index = Index.loadIndexFromFile(serializedIndexFile);
		} else {
			index.addToIndex(folderPath);
			index.saveToFile(serializedIndexFile);
		}
		System.out.println(index.index.size());
		long tic = System.currentTimeMillis();

		float[] imageQueryDescriptor = ImageDescriptors.computeImageDescriptor(query, binsPerColor);

		List<File> results = new ArrayList<>();
		/*
		 * Precompute the similarity between the query and all the images in the index.
		 */
		float[] similarityCache = new float[index.index.size()];

		for (int i = 0; i < index.index.size(); i++) {
			similarityCache[i] = IndexedImage.similarity(imageQueryDescriptor, index.index.get(i).imageDescriptor,
					useTexture, useColor);
		}

		for (int i = 0; i < k; i++) {
			// index of the minimum distance
			int argmax = ArrayUtils.argmax(similarityCache);

			File argmaxFile = index.index.get(argmax).file;
			float maxSimilarity = similarityCache[argmax];
			System.out.println(argmaxFile.getName() + " similarity: " + String.format("%.3f", maxSimilarity));
			results.add(argmaxFile);

			/*
			 * set the similarity to argmax on negative infinity since the file at index
			 * argmax has been added to the results.
			 */
			similarityCache[argmax] = Float.NEGATIVE_INFINITY;
		}

		System.out.println("Search done in: " + (System.currentTimeMillis() - tic) / 1000f + "s");

		return results;
	}

}
