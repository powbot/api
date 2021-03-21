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

	private int high = -1;
	private long highTime = -1;
	private int low = -1;
	private long lowTime = -1;

	public final static GrandExchangeItem NIL = new GrandExchangeItem();

	public GrandExchangeItem(String name) {
		this.name = name;
		try {
			String itemNameURL = name.replaceAll(" ", "_");
			URLConnection connection = new URL("https://oldschool.runescape.wiki/w/Exchange:" + itemNameURL).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

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
		}
	}

	private GrandExchangeItem() {
		initNIL();
	}

	/**
	 * uses wikis api to get high/low price and the time of high/low prices in unix timestamp SECONDS
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
			string = sb.toString().subString(sb.toString().indexOf(sb.toString().indexOf(":", sb.toString().indexOf(Integer.toString(id)))));
			string = string.replaceAll("\"{}", "");
			String[] strings = string.split(",");
			high = Integer.parseInt(strings[0]);
			highTime = Long.parseInt(strings[1]);
			low = Integer.parseInt(strings[2]);
			lowTime = Long.parseInt(strings[3]);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			initNIL();
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
}