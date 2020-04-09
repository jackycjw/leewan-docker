package com.leewan.util;

import java.util.UUID;

public class KeyUtils {

	public static String generateKey() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}
}
