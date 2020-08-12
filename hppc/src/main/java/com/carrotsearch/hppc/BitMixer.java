package com.carrotsearch.hppc;

/**
 * Bit mixing utilities. The purpose of these methods is to evenly distribute key space over int32
 * range.
 */
public final class BitMixer {

  // Don't bother mixing very small key domains much.
  public static int mix(byte key)             { return key * PHI_C32; }
  public static int mix(byte key, int seed)   { return (key ^ seed) * PHI_C32; }

  public static int mix(short key)            { return mixPhi(key); }
  public static int mix(short key, int seed)  { return mixPhi(key ^ seed); }

  public static int mix(char key)             { return mixPhi(key); }
  public static int mix(char key, int seed)   { return mixPhi(key ^ seed); }

  // Better mix for larger key domains.
  public static int mix(int key)              { return mix32(key); }
  public static int mix(int key, int seed)    { return mix32(key ^ seed); }

  public static int mix(float key)            { return mix32(Float.floatToIntBits(key)); }
  public static int mix(float key, int seed)  { return mix32(Float.floatToIntBits(key) ^ seed); }

  public static int mix(double key)           { return (int) mix64(Double.doubleToLongBits(key)); }
  public static int mix(double key, int seed) { return (int) mix64(Double.doubleToLongBits(key) ^ seed); }

  public static int mix(long key)             { return (int) mix64(key); }
  public static int mix(long key, int seed)   { return (int) mix64(key ^ seed); }

  public static int mix(Object key)           { return key == null ? 0 : mix32(key.hashCode()); }
  public static int mix(Object key, int seed) { return key == null ? 0 : mix32(key.hashCode() ^ seed); }

  /**
   * MH3's plain finalization step.
   */
  public static int mix32(int k) {
    k = (k ^ (k >>> 16)) * 0x85ebca6b;
    k = (k ^ (k >>> 13)) * 0xc2b2ae35;
    return k ^ (k >>> 16);
  }

  /**
   * Computes David Stafford variant 9 of 64bit mix function (MH3 finalization step,
   * with different shifts and constants).
   * 
   * Variant 9 is picked because it contains two 32-bit shifts which could be possibly
   * optimized into better machine code.
   * 
   * @see "http://zimbry.blogspot.com/2011/09/better-bit-mixing-improving-on.html"
   */
  public static long mix64(long z) {
      z = (z ^ (z >>> 32)) * 0x4cd6944c5cc20b6dL;
      z = (z ^ (z >>> 29)) * 0xfc12c5b19d3259e9L;
      return z ^ (z >>> 32);
  }

  /*
   * Golden ratio bit mixers.
   */

  private static final int PHI_C32 = 0x9e3779b9;
  private static final long PHI_C64 = 0x9e3779b97f4a7c15L;

  public static int mixPhi(byte k)   { final int h = k * PHI_C32; return h ^ (h >>> 16); }
  public static int mixPhi(byte k, int seed)   { final int h = (k ^ seed) * PHI_C32; return h ^ (h >>> 16); }

  public static int mixPhi(char k)   { final int h = k * PHI_C32; return h ^ (h >>> 16); }
  public static int mixPhi(char k, int seed)   { final int h = (k ^ seed) * PHI_C32; return h ^ (h >>> 16); }

  public static int mixPhi(short k)  { final int h = k * PHI_C32; return h ^ (h >>> 16); }
  public static int mixPhi(short k, int seed)  { final int h = (k ^ seed) * PHI_C32; return h ^ (h >>> 16); }

  public static int mixPhi(int k)    { final int h = k * PHI_C32; return h ^ (h >>> 16); }
  public static int mixPhi(int k, int seed)    { final int h = (k ^ seed) * PHI_C32; return h ^ (h >>> 16); }

  public static int mixPhi(float k)  { final int h = Float.floatToIntBits(k) * PHI_C32; return h ^ (h >>> 16); }
  public static int mixPhi(float k, int seed)  { final int h = (Float.floatToIntBits(k) ^ seed) * PHI_C32; return h ^ (h >>> 16); }

  public static int mixPhi(double k) { final long h = Double.doubleToLongBits(k) * PHI_C64; return (int) (h ^ (h >>> 32)); }
  public static int mixPhi(double k, int seed) { final long h = (Double.doubleToLongBits(k) ^ seed) * PHI_C64; return (int) (h ^ (h >>> 32)); }

  public static int mixPhi(long k)   { final long h = k * PHI_C64; return (int) (h ^ (h >>> 32)); }
  public static int mixPhi(long k, int seed)   { final long h = (k ^ seed) * PHI_C64; return (int) (h ^ (h >>> 32)); }

  public static int mixPhi(Object k) { final int h = (k == null ? 0 : k.hashCode() * PHI_C32); return h ^ (h >>> 16); }
  public static int mixPhi(Object k, int seed) { final int h = (k == null ? 0 : (k.hashCode() ^ seed) * PHI_C32); return h ^ (h >>> 16); }
}
