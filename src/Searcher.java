
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Searcher {
	public static List<File> getSearchResults(int k, String folderPath, BufferedImage query, boolean loadIndexFromFile,
			boolean useTexture, boolean useColor) throws ClassNotFoundException, IOException {

		int binsPerColor = 8;
		Index index = new Index(binsPerColor, useTexture, useColor);

		File serializedIndexFile = new File("seriallized_index.bin");

		if (loadIndexFromFile) {
			index = Index.loadIndexFromFile(serializedIndexFile);
		} else {
			index.addToIndex(folderPath);
			index.saveToFile(serializedIndexFile);
		}

		return index.getTopKMatches(query, k);
	}
}
