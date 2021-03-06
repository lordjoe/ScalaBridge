package com.rogerpf.aabridge.dds;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * <i>native declaration : line 268</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class contractType extends Structure {
	/** 0 = make 1-13 = sacrifice */
	public int underTricks;
	/** 0-3, e.g. 1 for 4S + 1. */
	public int overTricks;
	/** 1-7 */
	public int level;
	/**
	 * 0 = No Trumps, 1 = trump Spades, 2 = trump Hearts,<br>
	 * 3 = trump Diamonds, 4 = trump Clubs
	 */
	public int denom;
	/**
	 * One of the cases N, E, W, S, NS, EW;<br>
	 * 0 = N 1 = E, 2 = S, 3 = W, 4 = NS, 5 = EW
	 */
	public int seats;

	public contractType() {
		super();
	}

	protected List<?> getFieldOrder() {
		return Arrays.asList("underTricks", "overTricks", "level", "denom", "seats");
	}

	/**
	 * @param underTricks 0 = make 1-13 = sacrifice<br>
	 * @param overTricks 0-3, e.g. 1 for 4S + 1.<br>
	 * @param level 1-7<br>
	 * @param denom 0 = No Trumps, 1 = trump Spades, 2 = trump Hearts,<br>
	 * 3 = trump Diamonds, 4 = trump Clubs<br>
	 * @param seats One of the cases N, E, W, S, NS, EW;<br>
	 * 0 = N 1 = E, 2 = S, 3 = W, 4 = NS, 5 = EW
	 */
	public contractType(int underTricks, int overTricks, int level, int denom, int seats) {
		super();
		this.underTricks = underTricks;
		this.overTricks = overTricks;
		this.level = level;
		this.denom = denom;
		this.seats = seats;
	}

	public contractType(Pointer peer) {
		super(peer);
	}

	public static class ByReference extends contractType implements Structure.ByReference {

	};

	public static class ByValue extends contractType implements Structure.ByValue {

	};
}
