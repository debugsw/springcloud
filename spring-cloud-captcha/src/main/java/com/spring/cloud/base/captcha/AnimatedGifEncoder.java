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

	protected int width; // image size
	protected int height;
	protected Color transparent = null; // transparent color if given
	protected boolean transparentExactMatch = false; // transparent color will be found by looking for the closest color
	// or for the exact color if transparentExactMatch == true
	protected Color background = null;  // background color if given
	protected int transIndex; // transparent index in color table
	protected int repeat = -1; // no repeat
	protected int delay = 0; // frame delay (hundredths)
	protected boolean started = false; // ready to output frames
	protected OutputStream out;
	protected BufferedImage image; // current frame
	protected byte[] pixels; // BGR byte array from frame
	protected byte[] indexedPixels; // converted frame indexed to palette
	protected int colorDepth; // number of bit planes
	protected byte[] colorTab; // RGB palette
	protected boolean[] usedEntry = new boolean[256]; // active palette entries
	protected int palSize = 7; // color table size (bits-1)
	protected int dispose = -1; // disposal code (-1 = use default)
	protected boolean closeStream = false; // close stream when finished
	protected boolean firstFrame = true;
	protected boolean sizeSet = false; // if false, get size from first frame
	protected int sample = 10; // default sample interval for quantizer

	/**
	 * 设置每一帧的间隔时间
	 * Sets the delay time between each frame, or changes it
	 * for subsequent frames (applies to last frame added).
	 *
	 * @param ms 间隔时间，单位毫秒
	 */
	public void setDelay(int ms) {
		delay = Math.round(ms / 10.0f);
	}

	/**
	 * Sets the GIF frame disposal code for the last added frame
	 * and any subsequent frames.  Default is 0 if no transparent
	 * color has been set, otherwise 2.
	 *
	 * @param code int disposal code.
	 */
	public void setDispose(int code) {
		if (code >= 0) {
			dispose = code;
		}
	}

	/**
	 * Sets the number of times the set of GIF frames
	 * should be played.  Default is 1; 0 means play
	 * indefinitely.  Must be invoked before the first
	 * image is added.
	 *
	 * @param iter int number of iterations.
	 */
	public void setRepeat(int iter) {
		if (iter >= 0) {
			repeat = iter;
		}
	}

	/**
	 * Sets the transparent color for the last added frame
	 * and any subsequent frames.
	 * Since all colors are subject to modification
	 * in the quantization process, the color in the final
	 * palette for each frame closest to the given color
	 * becomes the transparent color for that frame.
	 * May be set to null to indicate no transparent color.
	 *
	 * @param c Color to be treated as transparent on display.
	 */
	public void setTransparent(Color c) {
		setTransparent(c, false);
	}

	/**
	 * Sets the transparent color for the last added frame
	 * and any subsequent frames.
	 * Since all colors are subject to modification
	 * in the quantization process, the color in the final
	 * palette for each frame closest to the given color
	 * becomes the transparent color for that frame.
	 * If exactMatch is set to true, transparent color index
	 * is search with exact match, and not looking for the
	 * closest one.
	 * May be set to null to indicate no transparent color.
	 *
	 * @param c          Color to be treated as transparent on display.
	 * @param exactMatch If exactMatch is set to true, transparent color index is search with exact match
	 */
	public void setTransparent(Color c, boolean exactMatch) {
		transparent = c;
		transparentExactMatch = exactMatch;
	}


	/**
	 * Sets the background color for the last added frame
	 * and any subsequent frames.
	 * Since all colors are subject to modification
	 * in the quantization process, the color in the final
	 * palette for each frame closest to the given color
	 * becomes the background color for that frame.
	 * May be set to null to indicate no background color
	 * which will default to black.
	 *
	 * @param c Color to be treated as background on display.
	 */
	public void setBackground(Color c) {
		background = c;
	}

	/**
	 * Adds next GIF frame.  The frame is not written immediately, but is
	 * actually deferred until the next frame is received so that timing
	 * data can be inserted.  Invoking {@code finish()} flushes all
	 * frames.  If {@code setSize} was not invoked, the size of the
	 * first image is used for all subsequent frames.
	 *
	 * @param im BufferedImage containing frame to write.
	 * @return true if successful.
	 */
	public boolean addFrame(BufferedImage im) {
		if ((im == null) || !started) {
			return false;
		}
		boolean ok = true;
		try {
			if (!sizeSet) {
				// use first frame's size
				setSize(im.getWidth(), im.getHeight());
			}
			image = im;
			getImagePixels(); // convert to correct format if necessary
			analyzePixels(); // build color table & map pixels
			if (firstFrame) {
				writeLSD(); // logical screen descriptior
				writePalette(); // global color table
				if (repeat >= 0) {
					// use NS app extension to indicate reps
					writeNetscapeExt();
				}
			}
			writeGraphicCtrlExt(); // write graphic control extension
			writeImageDesc(); // image descriptor
			if (!firstFrame) {
				writePalette(); // local color table
			}
			writePixels(); // encode and write pixel data
			firstFrame = false;
		} catch (IOException e) {
			ok = false;
		}

		return ok;
	}

	/**
	 * Flushes any pending data and closes output file.
	 * If writing to an OutputStream, the stream is not
	 * closed.
	 *
	 * @return is ok
	 */
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

		// reset for subsequent use
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

	/**
	 * Sets frame rate in frames per second.  Equivalent to
	 * {@code setDelay(1000/fps)}.
	 *
	 * @param fps float frame rate (frames per second)
	 */
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
		// map image pixels to new palette
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
			// If the entry is used in colorTab, then check if it is the same exact color we're looking for
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
			// create new image with right size/format
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
		out.write(0x21); // extension introducer
		out.write(0xf9); // GCE label
		out.write(4); // data block size
		int transp, disp;
		if (transparent == null) {
			transp = 0;
			disp = 0; // dispose = no action
		} else {
			transp = 1;
			disp = 2; // force clear if using transparent color
		}
		if (dispose >= 0) {
			disp = dispose & 7; // user override
		}
		disp <<= 2;

		// packed fields
		//noinspection PointlessBitwiseExpression
		out.write(0 | // 1:3 reserved
				disp | // 4:6 disposal
				0 | // 7   user input - 0 = none
				transp); // 8   transparency flag

		writeShort(delay); // delay x 1/100 sec
		out.write(transIndex); // transparent color index
		out.write(0); // block terminator
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
		// logical screen size
		writeShort(width);
		writeShort(height);
		// packed fields
		//noinspection PointlessBitwiseExpression
		out.write((0x80 | // 1   : global color table flag = 1 (gct used)
				0x70 | // 2-4 : color resolution = 7
				0x00 | // 5   : gct sort flag = 0
				palSize)); // 6-8 : gct size

		out.write(0); // background color index
		out.write(0); // pixel aspect ratio - assume 1:1
	}

	protected void writeNetscapeExt() throws IOException {
		out.write(0x21); // extension introducer
		out.write(0xff); // app extension label
		out.write(11); // block size
		writeString("NETSCAPE" + "2.0"); // app id + auth code
		out.write(3); // sub-block size
		out.write(1); // loop sub-block id
		writeShort(repeat); // loop count (extra iterations, 0=repeat forever)
		out.write(0); // block terminator
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
