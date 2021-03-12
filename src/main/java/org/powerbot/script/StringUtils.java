package org.powerbot.script;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A utility class for {@link java.lang.String}s, used internally.
 */
public class StringUtils {
	/**
	 * Removes tags from a string, which is anything around the characters &lt; and &gt;.
	 *
	 * @param s the {@link java.lang.String}
	 * @return the modified {@link java.lang.String} with tags stripped out
	 */
	public static String stripHtml(final String s) {
		return s!= null ? s.replaceAll("\\<.*?\\>", "") : null;
	}

	/**
	 * Converts a string to display case, where the first letter of every word is capitalised.
	 *
	 * @param s the {@link java.lang.String}
	 * @return the modified {@link java.lang.String} which has been converted to display (title) case
	 */
	public static String toDisplayCase(final String s) {
		final String d = " '-_/";
		final StringBuilder b = new StringBuilder();
		boolean n = true;

		for (char c : s.toCharArray()) {
			c = n ? Character.toUpperCase(c) : Character.toLowerCase(c);
			b.append(c);
			n = d.indexOf(c) >= 0;
		}

		return b.toString();
	}

	/**
	 * Safely parse an integer from a {@link java.lang.String}.
	 *
	 * @param s the {@link java.lang.String}
	 * @return the {@link java.lang.Integer} value, or {@code -1} if the parse failed
	 */
	public static int parseInt(final String s) {
		try {
			return Integer.parseInt(s.trim().replace(",", ""));
		} catch (final NumberFormatException ignored) {
		}
		return -1;
	}

	/**
	 * URL encode a UTF-8 {@link java.lang.String}.
	 *
	 * @param text the {@link java.lang.String}
	 * @return the encoded {@link java.lang.String}
	 */
	public static String urlEncode(final String text) {
		if (text == null) {
			return null;
		}
		try {
			return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
		} catch (final UnsupportedEncodingException ignored) {
			return text;
		}
	}


	/**
	 * URL decode a UTF-8 {@link java.lang.String}.
	 *
	 * @param text the {@link java.lang.String}
	 * @return the decoded {@link java.lang.String}
	 */
	public static String urlDecode(final String text) {
		try {
			return URLDecoder.decode(text, StandardCharsets.UTF_8.name());
		} catch (final Exception ignored) {
			return text;
		}
	}

	/**
	 * Get the bytes of a UTF-8 encoded {@link java.lang.String}.
	 *
	 * @param string the {@link java.lang.String}
	 * @return a byte array
	 */
	public static byte[] getBytesUtf8(final String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Construct a new UTF-8 string from raw bytes.
	 *
	 * @param bytes the byte array
	 * @return a new {@link java.lang.String}
	 */
	public static String newStringUtf8(final byte[] bytes) {
		return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Convert a byte array to a hex sequence.
	 *
	 * @param b the byte array
	 * @return a hex sequence, each byte converts to a two character {@link java.lang.String} {@code [0-9a-f]}
	 */
	public static String byteArrayToHexString(final byte[] b) {
		final StringBuilder s = new StringBuilder(b.length * 2);
		for (final byte aB : b) {
			s.append(Integer.toString((aB & 0xff) + 0x100, 16).substring(1));
		}
		return s.toString();
	}

	/**
	 * Convert a hex sequence to a byte array.
	 *
	 * @param s the hex sequence
	 * @return a byte array
	 */
	public static byte[] hexStringToByteArray(final String s) {
		final int l = s.length();
		final byte[] data = new byte[l / 2];
		for (int i = 0; i < l; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static int filterCoinsText(String text) {
		text = text.split(" ")[0];
		return Integer.parseInt(text.replaceAll(",", ""));
	}
}
