package com.spring.cloud.base.captcha;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/17 15:00
 */
public class NeuQuant {

	protected static final int NETSIZE = 256;
	protected static final int PRIME1 = 499;
	protected static final int PRIME2 = 491;
	protected static final int PRIME3 = 487;
	protected static final int PRIME4 = 503;
	protected static final int MINPICTUREBYTES = (3 * PRIME4);


	protected static final int MAXNETPOS = (NETSIZE - 1);
	protected static final int NETBIASSHIFT = 4;
	protected static final int NCYCLES = 100;

	/* defs for freq and bias */
	protected static final int INTBIASSHIFT = 16;
	protected static final int INTBIAS = (1 << INTBIASSHIFT);
	protected static final int GAMMASHIFT = 10;
	protected static final int GAMMA = (1 << GAMMASHIFT);
	protected static final int BETASHIFT = 10;
	protected static final int BETA = (INTBIAS >> BETASHIFT);
	protected static final int BETAGAMMA =
			(INTBIAS << (GAMMASHIFT - BETASHIFT));
	protected static final int INITRAD = (NETSIZE >> 3);
	protected static final int RADIUSBIASSHIFT = 6;
	protected static final int RADIUSBIAS = (1 << RADIUSBIASSHIFT);
	protected static final int INITRADIUS = (INITRAD * RADIUSBIAS);
	protected static final int RADIUSDEC = 30;

	protected static final int ALPHABIASSHIFT = 10;
	protected static final int INITALPHA = (1 << ALPHABIASSHIFT);

	protected int alphadec;

	protected static final int RADBIASSHIFT = 8;
	protected static final int RADBIAS = (1 << RADBIASSHIFT);
	protected static final int ALPHARADBSHIFT = (ALPHABIASSHIFT + RADBIASSHIFT);
	protected static final int ALPHARADBIAS = (1 << ALPHARADBSHIFT);
	protected byte[] thepicture;
	protected int lengthcount;
	protected int samplefac;
	protected int[][] network;
	protected int[] netindex = new int[256];
	protected int[] bias = new int[NETSIZE];
	protected int[] freq = new int[NETSIZE];
	protected int[] radpower = new int[INITRAD];
	public NeuQuant(byte[] thepic, int len, int sample) {

		int i;
		int[] p;

		thepicture = thepic;
		lengthcount = len;
		samplefac = sample;

		network = new int[NETSIZE][];
		for (i = 0; i < NETSIZE; i++) {
			network[i] = new int[4];
			p = network[i];
			p[0] = p[1] = p[2] = (i << (NETBIASSHIFT + 8)) / NETSIZE;
			freq[i] = INTBIAS / NETSIZE;
			bias[i] = 0;
		}
	}

	public byte[] colorMap() {
		byte[] map = new byte[3 * NETSIZE];
		int[] index = new int[NETSIZE];
		for (int i = 0; i < NETSIZE; i++) {
			index[network[i][3]] = i;
		}
		int k = 0;
		for (int i = 0; i < NETSIZE; i++) {
			int j = index[i];
			map[k++] = (byte) (network[j][0]);
			map[k++] = (byte) (network[j][1]);
			map[k++] = (byte) (network[j][2]);
		}
		return map;
	}

	public void inxbuild() {
		int i, j, smallpos, smallval;
		int[] p;
		int[] q;
		int previouscol, startpos;
		previouscol = 0;
		startpos = 0;
		for (i = 0; i < NETSIZE; i++) {
			p = network[i];
			smallpos = i;
			smallval = p[1];
			for (j = i + 1; j < NETSIZE; j++) {
				q = network[j];
				if (q[1] < smallval) {
					smallpos = j;
					smallval = q[1];
				}
			}
			q = network[smallpos];
			if (i != smallpos) {
				j = q[0];
				q[0] = p[0];
				p[0] = j;
				j = q[1];
				q[1] = p[1];
				p[1] = j;
				j = q[2];
				q[2] = p[2];
				p[2] = j;
				j = q[3];
				q[3] = p[3];
				p[3] = j;
			}
			if (smallval != previouscol) {
				netindex[previouscol] = (startpos + i) >> 1;
				for (j = previouscol + 1; j < smallval; j++) {
					netindex[j] = i;
				}
				previouscol = smallval;
				startpos = i;
			}
		}
		netindex[previouscol] = (startpos + MAXNETPOS) >> 1;
		for (j = previouscol + 1; j < 256; j++) {
			netindex[j] = MAXNETPOS;
		}
	}

	public void learn() {

		int i, j, b, g, r;
		int radius, rad, alpha, step, delta, samplepixels;
		byte[] p;
		int pix, lim;

		if (lengthcount < MINPICTUREBYTES) {
			samplefac = 1;
		}
		alphadec = 30 + ((samplefac - 1) / 3);
		p = thepicture;
		pix = 0;
		lim = lengthcount;
		samplepixels = lengthcount / (3 * samplefac);
		delta = samplepixels / NCYCLES;
		alpha = INITALPHA;
		radius = INITRADIUS;

		rad = radius >> RADIUSBIASSHIFT;
		for (i = 0; i < rad; i++) {
			radpower[i] =
					alpha * (((rad * rad - i * i) * RADBIAS) / (rad * rad));
		}


		if (lengthcount < MINPICTUREBYTES) {
			step = 3;
		} else if ((lengthcount % PRIME1) != 0) {
			step = 3 * PRIME1;
		} else {
			if ((lengthcount % PRIME2) != 0) {
				step = 3 * PRIME2;
			} else {
				if ((lengthcount % PRIME3) != 0) {
					step = 3 * PRIME3;
				} else {
					step = 3 * PRIME4;
				}
			}
		}

		i = 0;
		while (i < samplepixels) {
			b = (p[pix] & 0xff) << NETBIASSHIFT;
			g = (p[pix + 1] & 0xff) << NETBIASSHIFT;
			r = (p[pix + 2] & 0xff) << NETBIASSHIFT;
			j = contest(b, g, r);

			altersingle(alpha, j, b, g, r);
			if (rad != 0) {
				alterneigh(rad, j, b, g, r);
			}

			pix += step;
			if (pix >= lim) {
				pix -= lengthcount;
			}

			i++;
			if (delta == 0) {
				delta = 1;
			}
			if (i % delta == 0) {
				alpha -= alpha / alphadec;
				radius -= radius / RADIUSDEC;
				rad = radius >> RADIUSBIASSHIFT;
				if (rad <= 1) {
					rad = 0;
				}
				for (j = 0; j < rad; j++) {
					radpower[j] =
							alpha * (((rad * rad - j * j) * RADBIAS) / (rad * rad));
				}
			}
		}

	}

	public int map(int b, int g, int r) {

		int i, j, dist, a, bestd;
		int[] p;
		int best;

		bestd = 1000;
		best = -1;
		i = netindex[g];
		j = i - 1;

		while ((i < NETSIZE) || (j >= 0)) {
			if (i < NETSIZE) {
				p = network[i];
				dist = p[1] - g;
				if (dist >= bestd) {
					i = NETSIZE;
				} else {
					i++;
					if (dist < 0) {
						dist = -dist;
					}
					a = p[0] - b;
					if (a < 0) {
						a = -a;
					}
					dist += a;
					if (dist < bestd) {
						a = p[2] - r;
						if (a < 0) {
							a = -a;
						}
						dist += a;
						if (dist < bestd) {
							bestd = dist;
							best = p[3];
						}
					}
				}
			}
			if (j >= 0) {
				p = network[j];
				dist = g - p[1];
				if (dist >= bestd) {
					j = -1;
				} else {
					j--;
					if (dist < 0) {
						dist = -dist;
					}
					a = p[0] - b;
					if (a < 0) {
						a = -a;
					}
					dist += a;
					if (dist < bestd) {
						a = p[2] - r;
						if (a < 0) {
							a = -a;
						}
						dist += a;
						if (dist < bestd) {
							bestd = dist;
							best = p[3];
						}
					}
				}
			}
		}
		return (best);
	}

	public byte[] process() {
		learn();
		unbiasnet();
		inxbuild();
		return colorMap();
	}

	public void unbiasnet() {
		for (int i = 0; i < NETSIZE; i++) {
			network[i][0] >>= NETBIASSHIFT;
			network[i][1] >>= NETBIASSHIFT;
			network[i][2] >>= NETBIASSHIFT;
			network[i][3] = i;
		}
	}

	protected void alterneigh(int rad, int i, int b, int g, int r) {

		int j, k, lo, hi, a, m;
		int[] p;

		lo = i - rad;
		if (lo < -1) {
			lo = -1;
		}
		hi = i + rad;
		if (hi > NETSIZE) {
			hi = NETSIZE;
		}

		j = i + 1;
		k = i - 1;
		m = 1;
		while ((j < hi) || (k > lo)) {
			a = radpower[m++];
			if (j < hi) {
				p = network[j++];
				try {
					p[0] -= (a * (p[0] - b)) / ALPHARADBIAS;
					p[1] -= (a * (p[1] - g)) / ALPHARADBIAS;
					p[2] -= (a * (p[2] - r)) / ALPHARADBIAS;
				} catch (Exception ignored) {
				}
			}
			if (k > lo) {
				p = network[k--];
				try {
					p[0] -= (a * (p[0] - b)) / ALPHARADBIAS;
					p[1] -= (a * (p[1] - g)) / ALPHARADBIAS;
					p[2] -= (a * (p[2] - r)) / ALPHARADBIAS;
				} catch (Exception ignored) {
				}
			}
		}
	}
	protected void altersingle(int alpha, int i, int b, int g, int r) {

		int[] n = network[i];
		n[0] -= (alpha * (n[0] - b)) / INITALPHA;
		n[1] -= (alpha * (n[1] - g)) / INITALPHA;
		n[2] -= (alpha * (n[2] - r)) / INITALPHA;
	}

	protected int contest(int b, int g, int r) {
		int i, dist, a, biasdist, betafreq;
		int bestpos, bestbiaspos, bestd, bestbiasd;
		int[] n;

		bestd = ~(1 << 31);
		bestbiasd = bestd;
		bestpos = -1;
		bestbiaspos = bestpos;

		for (i = 0; i < NETSIZE; i++) {
			n = network[i];
			dist = n[0] - b;
			if (dist < 0) {
				dist = -dist;
			}
			a = n[1] - g;
			if (a < 0) {
				a = -a;
			}
			dist += a;
			a = n[2] - r;
			if (a < 0) {
				a = -a;
			}
			dist += a;
			if (dist < bestd) {
				bestd = dist;
				bestpos = i;
			}
			biasdist = dist - ((bias[i]) >> (INTBIASSHIFT - NETBIASSHIFT));
			if (biasdist < bestbiasd) {
				bestbiasd = biasdist;
				bestbiaspos = i;
			}
			betafreq = (freq[i] >> BETASHIFT);
			freq[i] -= betafreq;
			bias[i] += (betafreq << GAMMASHIFT);
		}
		freq[bestpos] += BETA;
		bias[bestpos] -= BETAGAMMA;
		return (bestbiaspos);
	}
}
