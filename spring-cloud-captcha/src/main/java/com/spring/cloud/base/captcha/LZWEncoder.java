package com.spring.cloud.base.captcha;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/17 15:00
 */
class LZWEncoder {

	private static final int EOF = -1;

	private final int imgW;
	private final int imgH;
	private final byte[] pixAry;
	private final int initCodeSize;

	private int remaining;
	private int curPixel;

	
	
	
	

	

	static final int BITS = 12;

	static final int HSIZE = 5003; 

	
	
	
	
	
	
	
	
	
	

	int n_bits; 
	int maxbits = BITS; 
	int maxcode; 
	int maxmaxcode = 1 << BITS; 

	int[] htab = new int[HSIZE];
	int[] codetab = new int[HSIZE];

	int hsize = HSIZE; 

	int free_ent = 0; 

	
	
	boolean clear_flg = false;

	
	
	
	
	
	
	
	
	
	
	

	int g_init_bits;

	int ClearCode;
	int EOFCode;

	
	
	
	
	
	
	
	
	
	
	
	
	
	

	int cur_accum = 0;
	int cur_bits = 0;

	final int[] masks =
		{
			0x0000,
			0x0001,
			0x0003,
			0x0007,
			0x000F,
			0x001F,
			0x003F,
			0x007F,
			0x00FF,
			0x01FF,
			0x03FF,
			0x07FF,
			0x0FFF,
			0x1FFF,
			0x3FFF,
			0x7FFF,
			0xFFFF };

	
	int a_count;

	
	byte[] accum = new byte[256];

	
	LZWEncoder(int width, int height, byte[] pixels, int color_depth) {
		imgW = width;
		imgH = height;
		pixAry = pixels;
		initCodeSize = Math.max(2, color_depth);
	}

	
	
	void char_out(byte c, OutputStream outs) throws IOException {
		accum[a_count++] = c;
		if (a_count >= 254)
			flush_char(outs);
	}

	

	
	void cl_block(OutputStream outs) throws IOException {
		cl_hash(hsize);
		free_ent = ClearCode + 2;
		clear_flg = true;

		output(ClearCode, outs);
	}

	
	void cl_hash(int hsize) {
		for (int i = 0; i < hsize; ++i)
			htab[i] = -1;
	}

	void compress(int init_bits, OutputStream outs) throws IOException {
		int fcode;
		int i /* = 0 */;
		int c;
		int ent;
		int disp;
		int hsize_reg;
		int hshift;

		
		g_init_bits = init_bits;

		
		clear_flg = false;
		n_bits = g_init_bits;
		maxcode = MAXCODE(n_bits);

		ClearCode = 1 << (init_bits - 1);
		EOFCode = ClearCode + 1;
		free_ent = ClearCode + 2;

		a_count = 0; 

		ent = nextPixel();

		hshift = 0;
		for (fcode = hsize; fcode < 65536; fcode *= 2)
			++hshift;
		hshift = 8 - hshift; 

		hsize_reg = hsize;
		cl_hash(hsize_reg); 

		output(ClearCode, outs);

		outer_loop : while ((c = nextPixel()) != EOF) {
			fcode = (c << maxbits) + ent;
			i = (c << hshift) ^ ent; 

			if (htab[i] == fcode) {
				ent = codetab[i];
				continue;
			} else if (htab[i] >= 0) 
				{
				disp = hsize_reg - i; 
				if (i == 0)
					disp = 1;
				do {
					if ((i -= disp) < 0)
						i += hsize_reg;

					if (htab[i] == fcode) {
						ent = codetab[i];
						continue outer_loop;
					}
				} while (htab[i] >= 0);
			}
			output(ent, outs);
			ent = c;
			if (free_ent < maxmaxcode) {
				codetab[i] = free_ent++; 
				htab[i] = fcode;
			} else
				cl_block(outs);
		}
		
		output(ent, outs);
		output(EOFCode, outs);
	}

	
	void encode(OutputStream os) throws IOException {
		os.write(initCodeSize); 

		remaining = imgW * imgH; 
		curPixel = 0;

		compress(initCodeSize + 1, os); 

		os.write(0); 
	}

	
	void flush_char(OutputStream outs) throws IOException {
		if (a_count > 0) {
			outs.write(a_count);
			outs.write(accum, 0, a_count);
			a_count = 0;
		}
	}

	final int MAXCODE(int n_bits) {
		return (1 << n_bits) - 1;
	}

	
	
	
	private int nextPixel() {
		if (remaining == 0)
			return EOF;

		--remaining;

		byte pix = pixAry[curPixel++];

		return pix & 0xff;
	}

	void output(int code, OutputStream outs) throws IOException {
		cur_accum &= masks[cur_bits];

		if (cur_bits > 0)
			cur_accum |= (code << cur_bits);
		else
			cur_accum = code;

		cur_bits += n_bits;

		while (cur_bits >= 8) {
			char_out((byte) (cur_accum & 0xff), outs);
			cur_accum >>= 8;
			cur_bits -= 8;
		}

		
		
		if (free_ent > maxcode || clear_flg) {
			if (clear_flg) {
				maxcode = MAXCODE(n_bits = g_init_bits);
				clear_flg = false;
			} else {
				++n_bits;
				if (n_bits == maxbits)
					maxcode = maxmaxcode;
				else
					maxcode = MAXCODE(n_bits);
			}
		}

		if (code == EOFCode) {
			
			while (cur_bits > 0) {
				char_out((byte) (cur_accum & 0xff), outs);
				cur_accum >>= 8;
				cur_bits -= 8;
			}

			flush_char(outs);
		}
	}
}
