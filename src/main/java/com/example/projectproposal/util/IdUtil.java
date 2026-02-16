package com.example.projectproposal.util;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.UUID;

public final class IdUtil {
  private static final SecureRandom RNG = new SecureRandom();
  private static final HexFormat HEX = HexFormat.of();

  private IdUtil() {}

  /** Simple uid: "U" + 12 hex chars */
  public static String newUserId() {
    byte[] b = new byte[6];
    RNG.nextBytes(b);
    return "U" + HEX.formatHex(b).toUpperCase();
  }

  /** Simple pid: "P" + UUID without dashes */
  public static String newProjectId() {
    return "P" + UUID.randomUUID().toString().replace("-", "");
  }
}
