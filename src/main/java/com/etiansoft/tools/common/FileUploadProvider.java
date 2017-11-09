package com.etiansoft.tools.common;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadProvider {

	@Value("${uploadPath}")
	public String uploadPath;

	public String getUploadPath(String relativePath) {
		File rootFolder = new File(uploadPath);
		if (!rootFolder.isDirectory()) {
			rootFolder.mkdir();
		}
		File file = new File(rootFolder + File.separator + relativePath);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public FileEntry uploadFile(MultipartFile multipartFile) throws IOException {
		if (multipartFile == null) {
			return null;
		}
		if (StringUtils.isBlank(multipartFile.getOriginalFilename())) {
			return null;
		}
		String filePath = getRandomFilePath(multipartFile);

		String uploadPath = getUploadPath(filePath);
		File uploadFile = new File(uploadPath);
		multipartFile.transferTo(uploadFile);

		return new FileEntry(multipartFile.getOriginalFilename(), filePath, uploadFile.length());
	}

	public FileEntry uploadScaleImage(MultipartFile imageFile, int x, int y, int w, int h) throws IOException {
		String relativePath = getRandomFilePath(imageFile);
		String imagePath = getUploadPath(relativePath);
		scale(imageFile.getInputStream(), imagePath);
		Image img;
		ImageFilter cropFilter;
		BufferedImage bi = ImageIO.read(new File(imagePath));
		int srcWidth = bi.getWidth();// 原图宽度
		int srcHeight = bi.getHeight();// 原图高度
		File uploadFile = new File(imagePath);
		if (srcWidth >= w && srcHeight >= h) {
			Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
			cropFilter = new CropImageFilter(x, y, w, h);
			img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
			BufferedImage scaleImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
			Graphics g = scaleImage.getGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();

			ImageIO.write(scaleImage, "JPEG", uploadFile);
		}
		return new FileEntry(imageFile.getOriginalFilename(), relativePath, uploadFile.length());
	}

	private String getRandomFilePath(MultipartFile multipartFile) {
		String randomFileName = UUID.randomUUID().toString().replaceAll("-", "");
		String fileName = multipartFile.getOriginalFilename();
		int index = fileName.lastIndexOf('.');
		if (index != -1) {
			randomFileName += fileName.substring(index);
		}

		return DateTool.formatDate(DateTool.currentDate()) + "/" + randomFileName;
	}

	private final static void scale(InputStream is, String imagePath) throws IOException {
		BufferedImage src = ImageIO.read(is); // 读入文件
		int width = src.getWidth(); // 得到源图宽
		int height = src.getHeight(); // 得到源图长
		if (width > 500 || height > 500) {
			if (width > height) {
				for (int i = 1; i < 20; i++) {
					if ((width / i) <= 500) {
						width = width / i;
						height = height / i;
						break;
					}
				}
			} else {
				for (int i = 1; i < 20; i++) {
					if ((height / i) <= 500) {
						width = width / i;
						height = height / i;
						break;
					}
				}
			}
		} else {
			if (width > height) {
				for (int i = 1; i < 20; i++) {
					if ((width * i) >= 400) {
						width = width * i;
						height = height * i;
						break;
					}
				}
			} else {
				for (int i = 1; i < 20; i++) {
					if ((height * i) >= 400) {
						width = width * i;
						height = height * i;
						break;
					}
				}
			}
		}
		Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();
		ImageIO.write(tag, "JPEG", new File(imagePath));// 输出到文件流
	}
}
