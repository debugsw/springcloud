package com.spring.cloud.base.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author: ls
 * @Description: 动态GIF动画生成器，可生成一个或多个帧的GIF
 * @Date: 2023/4/17 15:00
 */
public class AnimatedGifEncoder {

	protected int width;

	protected int height;
	protected Color transparent = null;
	protected boolean transparentExactMatch = false;
	protected Color background = null;
	protected int transIndex;
	protected int repeat = -1;
	protected int delay = 0;
	protected boolean started = false;
	protected OutputStream out;
	protected BufferedImage image;
	protected byte[] pixels;
	protected byte[] indexedPixels;
	protected int colorDepth;
	protected byte[] colorTab;
	protected boolean[] usedEntry = new boolean[256];
	protected int palSize = 7;
	protected int dispose = -1;
	protected boolean closeStream = false;
	protected boolean firstFrame = true;
	protected boolean sizeSet = false;
	protected int sample = 10;

	/**
	 * 设置每一帧的间隔时间
	 *
	 * @param ms 间隔时间，单位毫秒
	 */
	public void setDelay(int ms) {
		delay = Math.round(ms / 10.0f);
	}

	public void setDispose(int code) {
		if (code >= 0) {
			dispose = code;
		}
	}

	public void setRepeat(int iter) {
		if (iter >= 0) {
			repeat = iter;
		}
	}

	public void setTransparent(Color c) {
		setTransparent(c, false);
	}


	public void setTransparent(Color c, boolean exactMatch) {
		transparent = c;
		transparentExactMatch = exactMatch;
	}


	public void setBackground(Color c) {
		background = c;
	}

	public boolean addFrame(BufferedImage im) {
		if ((im == null) || !started) {
			return false;
		}
		boolean ok = true;
		try {
			if (!sizeSet) {

				setSize(im.getWidth(), im.getHeight());
			}
			image = im;
			getImagePixels();
			analyzePixels();
			if (firstFrame) {
				writeLSD();
				writePalette();
				if (repeat >= 0) {

					writeNetscapeExt();
				}
			}
			writeGraphicCtrlExt();
			writeImageDesc();
			if (!firstFrame) {
				writePalette();
			}
			writePixels();
			firstFrame = false;
		} catch (IOException e) {
			ok = false;
		}

		return ok;
	}

	public boolean finish() {
		if (!started) {
			return false;
		}
		boolean ok = true;
		started = false;
		try {
			out.write(0x3b);
			out.flush();
			if (closeStream) {
				out.close();
			}
		} catch (IOException e) {
			ok = false;
		}


		transIndex = 0;
		out = null;
		image = null;
		pixels = null;
		indexedPixels = null;
		colorTab = null;
		closeStream = false;
		firstFrame = true;

		return ok;
	}

	public void setFrameRate(float fps) {
		if (fps != 0f) {
			delay = Math.round(100f / fps);
		}
	}

	public void setQuality(int quality) {
		if (quality < 1) {
			quality = 1;
		}
		sample = quality;
	}

	public void setSize(int w, int h) {
		if (started && !firstFrame) {
			return;
		}
		width = w;
		height = h;
		if (width < 1) {
			width = 320;
		}
		if (height < 1) {
			height = 240;
		}
		sizeSet = true;
	}

	public boolean start(OutputStream os) {
		if (os == null) {
			return false;
		}
		boolean ok = true;
		closeStream = false;
		out = os;
		try {
			writeString("GIF89a");
		} catch (IOException e) {
			ok = false;
		}
		return started = ok;
	}

	public boolean start(String file) {
		boolean ok;
		try {
			out = new BufferedOutputStream(Files.newOutputStream(Paths.get(file)));
			ok = start(out);
			closeStream = true;
		} catch (IOException e) {
			ok = false;
		}
		return started = ok;
	}

	public boolean isStarted() {
		return started;
	}

	protected void analyzePixels() {
		int len = pixels.length;
		int nPix = len / 3;
		indexedPixels = new byte[nPix];
		NeuQuant nq = new NeuQuant(pixels, len, sample);
		colorTab = nq.process();
		for (int i = 0; i < colorTab.length; i += 3) {
			byte temp = colorTab[i];
			colorTab[i] = colorTab[i + 2];
			colorTab[i + 2] = temp;
			usedEntry[i / 3] = false;
		}

		int k = 0;
		for (int i = 0; i < nPix; i++) {
			int index =
					nq.map(pixels[k++] & 0xff,
							pixels[k++] & 0xff,
							pixels[k++] & 0xff);
			usedEntry[index] = true;
			indexedPixels[i] = (byte) index;
		}
		pixels = null;
		colorDepth = 8;
		palSize = 7;
		if (transparent != null) {
			transIndex = transparentExactMatch ? findExact(transparent) : findClosest(transparent);
		}
	}

	protected int findClosest(Color c) {
		if (colorTab == null) {
			return -1;
		}
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int minpos = 0;
		int dmin = 256 * 256 * 256;
		int len = colorTab.length;
		for (int i = 0; i < len; ) {
			int dr = r - (colorTab[i++] & 0xff);
			int dg = g - (colorTab[i++] & 0xff);
			int db = b - (colorTab[i] & 0xff);
			int d = dr * dr + dg * dg + db * db;
			int index = i / 3;
			if (usedEntry[index] && (d < dmin)) {
				dmin = d;
				minpos = index;
			}
			i++;
		}
		return minpos;
	}

	boolean isColorUsed(Color c) {
		return findExact(c) != -1;
	}

	protected int findExact(Color c) {
		if (colorTab == null) {
			return -1;
		}

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int len = colorTab.length / 3;
		for (int index = 0; index < len; ++index) {
			int i = index * 3;

			if (usedEntry[index] && r == (colorTab[i] & 0xff) && g == (colorTab[i + 1] & 0xff) && b == (colorTab[i + 2] & 0xff)) {
				return index;
			}
		}
		return -1;
	}

	protected void getImagePixels() {
		int w = image.getWidth();
		int h = image.getHeight();
		int type = image.getType();
		if ((w != width)
				|| (h != height)
				|| (type != BufferedImage.TYPE_3BYTE_BGR)) {

			BufferedImage temp =
					new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = temp.createGraphics();
			g.setColor(background);
			g.fillRect(0, 0, width, height);
			g.drawImage(image, 0, 0, null);
			image = temp;
		}
		pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	}

	protected void writeGraphicCtrlExt() throws IOException {
		out.write(0x21);
		out.write(0xf9);
		out.write(4);
		int transp, disp;
		if (transparent == null) {
			transp = 0;
			disp = 0;
		} else {
			transp = 1;
			disp = 2;
		}
		if (dispose >= 0) {
			disp = dispose & 7;
		}
		disp <<= 2;
		out.write(0 | disp | 0 | transp);
		writeShort(delay);
		out.write(transIndex);
		out.write(0);
	}

	protected void writeImageDesc() throws IOException {
		out.write(0x2c);
		writeShort(0);
		writeShort(0);
		writeShort(width);
		writeShort(height);
		if (firstFrame) {
			out.write(0);
		} else {
			out.write(0x80 | 0 | 0 | 0 | palSize);
		}
	}

	protected void writeLSD() throws IOException {
		writeShort(width);
		writeShort(height);
		out.write((0x80 | 0x70 | 0x00 | palSize));
		out.write(0);
		out.write(0);
	}

	protected void writeNetscapeExt() throws IOException {
		out.write(0x21);
		out.write(0xff);
		out.write(11);
		writeString("NETSCAPE" + "2.0");
		out.write(3);
		out.write(1);
		writeShort(repeat);
		out.write(0);
	}

	protected void writePalette() throws IOException {
		out.write(colorTab, 0, colorTab.length);
		int n = (3 * 256) - colorTab.length;
		for (int i = 0; i < n; i++) {
			out.write(0);
		}
	}

	protected void writePixels() throws IOException {
		LZWEncoder encoder = new LZWEncoder(width, height, indexedPixels, colorDepth);
		encoder.encode(out);
	}

	protected void writeShort(int value) throws IOException {
		out.write(value & 0xff);
		out.write((value >> 8) & 0xff);
	}

	protected void writeString(String s) throws IOException {
		for (int i = 0; i < s.length(); i++) {
			out.write((byte) s.charAt(i));
		}
	}
}
