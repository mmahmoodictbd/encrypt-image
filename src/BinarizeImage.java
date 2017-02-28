import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class BinarizeImage {

	private static Logger logger = Logger.getLogger(BinarizeImage.class.getName());

	public String binarize(BufferedImage image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "png", baos);
		} catch (Exception e) {
			logger.info("Exception in binarize(): " + e);
		}

		byte[] byteArray = baos.toByteArray();

		// convert jpgByteArray to a String with 0s and 1s
		StringBuffer sb = new StringBuffer();

		for (byte b : byteArray) {
			sb.append(Integer.toBinaryString(b & 0xFF) + " ");
		}

		return sb.toString();
	}

	public String decimalize(BufferedImage image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "png", baos);
		} catch (Exception e) {
			logger.info("Exception in binarize(): " + e);
		}

		byte[] byteArray = baos.toByteArray();

		// convert jpgByteArray to a String with 0s and 1s
		StringBuffer sb = new StringBuffer();

		for (byte b : byteArray) {
			sb.append(b + "\n");
		}

		return sb.toString();
	}

	public BufferedImage encrypt(BufferedImage image, String secretKey) {

		BufferedImage encBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		int[][] pixels = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				pixels[i][j] = image.getRGB(i, j);
			}
		}

		int[][] chaoticMappedPixels = chaoticMapping(pixels, secretKey);

		try {
			for (int i = 0; i < encBufferedImage.getWidth(); i++) {
				for (int j = 0; j < encBufferedImage.getHeight(); j++) {
					encBufferedImage.setRGB(i, j, chaoticMappedPixels[i][j]);
				}
			}
		} catch (Exception e) {
			logger.info("Exception in binarize(): " + e);
		}

		getHistogram(image);
		getHistogram(encBufferedImage);

		return encBufferedImage;
	}

	private int[][] chaoticMapping(int pixels[][], String secretKey) {

		int secretKeyInt = Integer.parseInt(secretKey);
		int[][] chaoticMappedData = new int[pixels.length][pixels[0].length];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[0].length; j++) {

				Color color = new Color(pixels[i][j]);
				int encRed = color.getRed() ^ secretKeyInt;
				int encGreen = color.getGreen() ^ secretKeyInt;
				int encBlue = color.getBlue() ^ secretKeyInt;

				int encRGB = new Color(encRed, encGreen, encBlue).getRGB();
				chaoticMappedData[i][j] = encRGB;

			}
		}

		return chaoticMappedData;
	}

	private int[] getHistogram(BufferedImage image) {

		int[][] pixels = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				pixels[i][j] = image.getRGB(i, j);
			}
		}

		int[] histogram = new int[256];

		for (int[] pixel : pixels) {
			for (int j = 0; j < pixels[0].length; j++) {

				Color color = new Color(pixel[j]);
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				histogram[red]++;
				histogram[green]++;
				histogram[blue]++;
			}
		}

		return histogram;
	}

	public BufferedImage loadImage(URL url) {

		BufferedImage bImage = null;

		try {
			bImage = ImageIO.read(url);
		} catch (Exception e) {
			logger.info("Exception in loadImage(): " + e);
		}

		return bImage;
	}

	public BufferedImage loadImage(File file) {

		BufferedImage bImage = null;

		try {
			if (file != null) {
				System.out.println(file.getAbsolutePath());
				bImage = ImageIO.read(file);

			}
		} catch (Exception e) {
			logger.info("Exception in loadImage(): " + e);
		}

		return bImage;
	}

	public void write(String content, String fileName) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(new File(fileName));
			fileWriter.write(content);
		} catch (IOException e) {
			logger.info("Exception in write(): " + e);

		} finally {
			try {
				fileWriter.close();
			} catch (IOException ex) {
				logger.info("fileWriter can't close.");
			}
		}
	}

	public void writeImage(BufferedImage bufferedImage, String fileName) {

		try {
			ImageIO.write(bufferedImage, "png", new File(fileName));
		} catch (Exception e) {
			logger.info("Exception in : writeImage()" + e);
		}

	}

	public static void main(String args[]) {
		logger.info("Program running...");
		/*
		BinarizeImage bi = new BinarizeImage();

		File file = new File("mini.png");
		BufferedImage bufferedImage = bi.loadImage(file);
		BufferedImage encImage = bi.encrypt(bufferedImage, "24");
		bi.writeImage(encImage, "enc.png");

		file = new File("enc.png");
		bufferedImage = bi.loadImage(file);
		BufferedImage decImage = bi.encrypt(bufferedImage, "24");
		System.out.println(decImage.hashCode());
		bi.writeImage(decImage, "dec.png");
		 */

		//LFSR lfsr = new LFSR("10010001001000100101000000011001");
		LFSR lfsr = new LFSR("1010111");
		lfsr.writePseudoRandomSequencesList(lfsr.getPseudoRandomSequencesList(127), "res");

	}
}
