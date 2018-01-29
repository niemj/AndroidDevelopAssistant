package com.example.jun.myapplication.util.crypto;

public final class Base64 {

	public Base64() {
	}

	protected static boolean isWhiteSpace(char c) {
		return c == ' ' || c == '\r' || c == '\n' || c == '\t';
	}

	protected static boolean isPad(char c) {
		return c == '=';
	}

	protected static boolean isData(char c) {
		return base64Alphabet[c] != -1;
	}

	protected static boolean isBase64(char c) {
		return isWhiteSpace(c) || isPad(c) || isData(c);
	}

	public static String encode(byte abyte0[]) {
		if (abyte0 == null)
			return null;
		int i = abyte0.length * 8;
		if (i == 0)
			return "";
		int j = i % 24;
		int k = i / 24;
		int l = j == 0 ? k : k + 1;
		int i1 = (l - 1) / 19 + 1;
		char ac[] = null;
		ac = new char[l * 4 + i1];
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		int j1 = 0;
		int k1 = 0;
		int l1 = 0;
		for (int i2 = 0; i2 < i1 - 1; i2++) {
			for (int j2 = 0; j2 < 19; j2++) {
				byte byte7 = abyte0[k1++];
				byte byte11 = abyte0[k1++];
				byte byte14 = abyte0[k1++];
				byte byte4 = (byte) (byte11 & 15);
				byte byte0 = (byte) (byte7 & 3);
				byte byte21 = (byte7 & -128) != 0
						? (byte) (byte7 >> 2 ^ 192)
						: (byte) (byte7 >> 2);
				byte byte23 = (byte11 & -128) != 0
						? (byte) (byte11 >> 4 ^ 240)
						: (byte) (byte11 >> 4);
				byte byte24 = (byte14 & -128) != 0
						? (byte) (byte14 >> 6 ^ 252)
						: (byte) (byte14 >> 6);
				ac[j1++] = lookUpBase64Alphabet[byte21];
				ac[j1++] = lookUpBase64Alphabet[byte23 | byte0 << 4];
				ac[j1++] = lookUpBase64Alphabet[byte4 << 2 | byte24];
				ac[j1++] = lookUpBase64Alphabet[byte14 & 63];
				l1++;
			}

			ac[j1++] = '\n';
		}

		for (; l1 < k; l1++) {
			byte byte8 = abyte0[k1++];
			byte byte12 = abyte0[k1++];
			byte byte15 = abyte0[k1++];
			byte byte5 = (byte) (byte12 & 15);
			byte byte1 = (byte) (byte8 & 3);
			byte byte16 = (byte8 & -128) != 0
					? (byte) (byte8 >> 2 ^ 192)
					: (byte) (byte8 >> 2);
			byte byte19 = (byte12 & -128) != 0
					? (byte) (byte12 >> 4 ^ 240)
					: (byte) (byte12 >> 4);
			byte byte22 = (byte15 & -128) != 0
					? (byte) (byte15 >> 6 ^ 252)
					: (byte) (byte15 >> 6);
			ac[j1++] = lookUpBase64Alphabet[byte16];
			ac[j1++] = lookUpBase64Alphabet[byte19 | byte1 << 4];
			ac[j1++] = lookUpBase64Alphabet[byte5 << 2 | byte22];
			ac[j1++] = lookUpBase64Alphabet[byte15 & 63];
		}

		if (j == 8) {
			byte byte9 = abyte0[k1];
			byte byte2 = (byte) (byte9 & 3);
			byte byte17 = (byte9 & -128) != 0
					? (byte) (byte9 >> 2 ^ 192)
					: (byte) (byte9 >> 2);
			ac[j1++] = lookUpBase64Alphabet[byte17];
			ac[j1++] = lookUpBase64Alphabet[byte2 << 4];
			ac[j1++] = '=';
			ac[j1++] = '=';
		} else if (j == 16) {
			byte byte10 = abyte0[k1];
			byte byte13 = abyte0[k1 + 1];
			byte byte6 = (byte) (byte13 & 15);
			byte byte3 = (byte) (byte10 & 3);
			byte byte18 = (byte10 & -128) != 0
					? (byte) (byte10 >> 2 ^ 192)
					: (byte) (byte10 >> 2);
			byte byte20 = (byte13 & -128) != 0
					? (byte) (byte13 >> 4 ^ 240)
					: (byte) (byte13 >> 4);
			ac[j1++] = lookUpBase64Alphabet[byte18];
			ac[j1++] = lookUpBase64Alphabet[byte20 | byte3 << 4];
			ac[j1++] = lookUpBase64Alphabet[byte6 << 2];
			ac[j1++] = '=';
		}
		ac[j1] = '\n';
		return new String(ac);
	}

	public static byte[] decode(String s) {
		if (s == null)
			return null;
		char ac[] = s.toCharArray();
		int i = removeWhiteSpace(ac);
		if (i % 4 != 0)
			return null;
		int j = i / 4;
		if (j == 0)
			return new byte[0];
		byte abyte0[] = null;
		byte byte0 = 0;
		byte byte1 = 0;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		char c = '\0';
		char c1 = '\0';
		char c2 = '\0';
		char c3 = '\0';
		int k = 0;
		int l = 0;
		int i1 = 0;
		abyte0 = new byte[j * 3];
		for (; k < j - 1; k++) {
			if (!isData(c = ac[i1++]) || !isData(c1 = ac[i1++])
					|| !isData(c2 = ac[i1++]) || !isData(c3 = ac[i1++]))
				return null;
			byte0 = base64Alphabet[c];
			byte1 = base64Alphabet[c1];
			byte byte2 = base64Alphabet[c2];
			byte byte5 = base64Alphabet[c3];
			abyte0[l++] = (byte) (byte0 << 2 | byte1 >> 4);
			abyte0[l++] = (byte) ((byte1 & 15) << 4 | byte2 >> 2 & 15);
			abyte0[l++] = (byte) (byte2 << 6 | byte5);
		}

		if (!isData(c = ac[i1++]) || !isData(c1 = ac[i1++]))
			return null;
		byte0 = base64Alphabet[c];
		byte1 = base64Alphabet[c1];
		c2 = ac[i1++];
		c3 = ac[i1++];
		if (!isData(c2) || !isData(c3)) {
			if (isPad(c2) && isPad(c3))
				if ((byte1 & 15) != 0) {
					return null;
				} else {
					byte abyte1[] = new byte[k * 3 + 1];
					System.arraycopy(abyte0, 0, abyte1, 0, k * 3);
					abyte1[l] = (byte) (byte0 << 2 | byte1 >> 4);
					return abyte1;
				}
			if (!isPad(c2) && isPad(c3)) {
				byte byte3 = base64Alphabet[c2];
				if ((byte3 & 3) != 0) {
					return null;
				} else {
					byte abyte2[] = new byte[k * 3 + 2];
					System.arraycopy(abyte0, 0, abyte2, 0, k * 3);
					abyte2[l++] = (byte) (byte0 << 2 | byte1 >> 4);
					abyte2[l] = (byte) ((byte1 & 15) << 4 | byte3 >> 2 & 15);
					return abyte2;
				}
			} else {
				return null;
			}
		} else {
			byte byte4 = base64Alphabet[c2];
			byte byte6 = base64Alphabet[c3];
			abyte0[l++] = (byte) (byte0 << 2 | byte1 >> 4);
			abyte0[l++] = (byte) ((byte1 & 15) << 4 | byte4 >> 2 & 15);
			abyte0[l++] = (byte) (byte4 << 6 | byte6);
			return abyte0;
		}
	}

	protected static int removeWhiteSpace(char ac[]) {
		if (ac == null)
			return 0;
		int i = 0;
		int j = ac.length;
		for (int k = 0; k < j; k++)
			if (!isWhiteSpace(ac[k]))
				ac[i++] = ac[k];

		return i;
	}

	private static final int BASELENGTH = 255;
	private static final int LOOKUPLENGTH = 64;
	private static final int TWENTYFOURBITGROUP = 24;
	private static final int EIGHTBIT = 8;
	private static final int SIXTEENBIT = 16;
	private static final int SIXBIT = 6;
	private static final int FOURBYTE = 4;
	private static final int SIGN = -128;
	private static final char PAD = 61;
	private static final boolean fDebug = false;
	private static final byte base64Alphabet[];
	private static final char lookUpBase64Alphabet[];

	static {
		base64Alphabet = new byte[255];
		lookUpBase64Alphabet = new char[64];
		for (int i = 0; i < 255; i++)
			base64Alphabet[i] = -1;

		for (int j = 90; j >= 65; j--)
			base64Alphabet[j] = (byte) (j - 65);

		for (int k = 122; k >= 97; k--)
			base64Alphabet[k] = (byte) ((k - 97) + 26);

		for (int l = 57; l >= 48; l--)
			base64Alphabet[l] = (byte) ((l - 48) + 52);

		base64Alphabet[43] = 62;
		base64Alphabet[47] = 63;
		for (int i1 = 0; i1 <= 25; i1++)
			lookUpBase64Alphabet[i1] = (char) (65 + i1);

		int j1 = 26;
		for (int k1 = 0; j1 <= 51; k1++) {
			lookUpBase64Alphabet[j1] = (char) (97 + k1);
			j1++;
		}

		j1 = 52;
		for (int l1 = 0; j1 <= 61; l1++) {
			lookUpBase64Alphabet[j1] = (char) (48 + l1);
			j1++;
		}

		lookUpBase64Alphabet[62] = '+';
		lookUpBase64Alphabet[63] = '/';
	}


}