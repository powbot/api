package org.powerbot.script.rt4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class GrandExchangeItem {
	private int id;
	private String name;
	private int dailyVolume;
	private int guidePrice;
	private boolean members;
	private int buyLimit;
	private int high;
	private long highTime;
	private int low;
	private long lowTime;

	public final static GrandExchangeItem NIL = new GrandExchangeItem();

	public GrandExchangeItem(String name) {
		this.name = name;
		try {
			String itemNameURL = name.replaceAll(" ", "_");
			URLConnection connection = null;
			boolean connectionFailed = false;
			if (name.contains("teleport")) {
				try {
					connection = new URL("https://oldschool.runescape.wiki/w/Exchange:" + itemNameURL + "_(tablet)").openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
					connection.connect();
				} catch (Exception ignored) {
					connectionFailed = true;
				}
			}
			if (connectionFailed) {
				connection = new URL("https://oldschool.runescape.wiki/w/Exchange:" + itemNameURL).openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				connection.connect();
			}


			BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();

			String string;
			while ((string = r.readLine()) != null) {
				sb.append(string);
			}

			String s = sb.toString().substring(sb.toString().indexOf("gemw-section-left"), sb.toString().indexOf("gemw-section-right"));
			int index = s.indexOf("GEPrice\">") + 9;
			guidePrice = Integer.parseInt(s.substring(index, s.indexOf("</", index)).replaceAll(",", ""));

			s = sb.toString().substring(sb.toString().lastIndexOf("gemw-section-left"), sb.toString().lastIndexOf("</dl>"));
			index = s.indexOf("<dd>") + 4;
			members = s.substring(index, s.indexOf("</dd>")).equalsIgnoreCase("members");
			index = s.indexOf("<dd>", index) + 4;
			buyLimit = Integer.parseInt(s.substring(index, s.indexOf("</dd>", index)).replaceAll(",", ""));
			index = s.indexOf("<dd>", index) + 4;
			id = Integer.parseInt(s.substring(index, s.indexOf("</dd>", index)));
			dailyVolume = (Integer.parseInt(s.substring(s.lastIndexOf("<dd>") + 4, s.lastIndexOf("</dd>")).replaceAll(",", "")));

		} catch (Exception e) {
			e.printStackTrace();
			initNIL();
			return;
		}
		initLivePrices();
	}

	private GrandExchangeItem() {
		initNIL();
	}

	/**
	 * uses wiki's api to get high/low price and the time of high/low prices in unix timestamp SECONDS
	 * for more info see: https://oldschool.runescape.wiki/w/RuneScape:Real-time_Prices
	 * @return returns false if failed to connect
	 */
	public boolean initLivePrices() {
		try {
			URLConnection connection = new URL("https://prices.runescape.wiki/api/v1/osrs/latest?id=" + id).openConnection();
			connection.setRequestProperty("User-Agent", "price_checker");
			connection.connect();

			BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();

			String string;
			while ((string = r.readLine()) != null) {
				sb.append(string);
			}
			int indexOfId = sb.toString().indexOf(Integer.toString(id));
			int indexOfColonAfterId = sb.toString().indexOf(":", indexOfId);
			string = sb.toString().substring(indexOfColonAfterId + 1);
			string = string.replace("\"", "").replace("{", "").replace("}", "");
			String[] strings = string.split(",");

			high = Integer.parseInt(strings[0].split(":")[1]);
			highTime = Long.parseLong(strings[1].split(":")[1]);
			low = Integer.parseInt(strings[2].split(":")[1]);
			lowTime = Long.parseLong(strings[3].split(":")[1]);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static GrandExchangeItem nil() {
		return NIL;
	}

	private void initNIL() {
		id = -1;
		name = "";
		dailyVolume = -1;
		guidePrice = -1;
		buyLimit = -1;
		members = false;
		high = -1;
		highTime = -1;
		low = -1;
		lowTime = -1;
	}

	public int getGuidePrice() {
		return guidePrice;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getDailyVolume() {
		return dailyVolume;
	}

	public int getBuyLimit() {
		return buyLimit;
	}

	public boolean isMembers() {
		return members;
	}

	public int getHigh() {
		return high;
	}

	public int getLow() {
		return low;
	}

	public long getHighTime() {
		return highTime;
	}

	public long getLowTime() {
		return lowTime;
	}
}
